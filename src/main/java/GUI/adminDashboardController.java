package GUI;

import Program.Confession.ConfessionPost;
import Program.Confession.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class adminDashboardController implements Initializable {

    @FXML
    private TabPane adminTabPane;

    @FXML
    private ListView<String> bannedList;

    @FXML
    private Tab bannedUserTab;

    @FXML
    private Label problemCategory;

    @FXML
    private Label reason;

    @FXML
    private Label reportedID;

    @FXML
    private Tab reportsTab;

    @FXML
    private ScrollPane submissionScrollPane;

    @FXML
    private VBox submissionVBox;

    @FXML
    private Tab submissionsTab;

    @FXML
    private ScrollPane timelineScrollPane;

    @FXML
    private Tab timelineTab;

    @FXML
    private VBox timelineVBox;


    private List<ConfessionPost> confessionPosts = new ArrayList<>();
    private MyListener myListener;
    private MyListener submissionListener;
    String[] confessionIDs;
    private ArrayList<Report> reports;
    private ArrayList<ConfessionPost> reportedConfessions;
    private ArrayList<ConfessionPost> allConfession;
    Stage stage;
    Scene scene;
    Parent root;



    @FXML
    void nextButtonClicked(ActionEvent event) {
        //Get the index of the report displayed
        int index = -1;
        String reportCategory = problemCategory.getText().trim();
        String reportReason = reason.getText().trim();
        String againstID = reportedID.getText().trim();
        for(int i =  0 ; i < reports.size(); i++){
            if(reports.get(i).getReportCategory().equals(reportCategory) && reports.get(i).getReportContent().equals(reportReason) && reports.get(i).getReportAgainstID().equals(againstID)){
                index = i;
                break;
            }
        }

        //We already have the index of the report being displayed, now we display the next report
        if(index == reports.size()-1){
            return;
        }
        else{
            setReportData(reports.get(index+1));
        }

    }

    int getIndexOfCurrentReport(){
        int index = -1;
        String reportCategory = problemCategory.getText().trim();
        String reportReason = reason.getText().trim();
        String againstID = reportedID.getText().trim();
        for(int i =  0 ; i < reports.size(); i++){
            if(reports.get(i).getReportCategory().equals(reportCategory) && reports.get(i).getReportContent().equals(reportReason) && reports.get(i).getReportAgainstID().equals(againstID)){
                index = i;
                break;
            }
        }
        return index;
    }

    @FXML
    void previousButtonClicked(ActionEvent event) {
        //Get the index of the report displayed
        int index = getIndexOfCurrentReport();
        //We already have the index of the report being displayed, now we display the reviosu report
        if(index == 0){
            return;
        }
        else{
            setReportData(reports.get(index-1));
        }
    }

    @FXML
    void rejectButtonClicked(ActionEvent event) {
        int index = getIndexOfCurrentReport();

        //Remove the report from the database
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reports WHERE againstID = ? AND reportCategory = ? AND reportContent = ?");
            preparedStatement.setString(1, reportedID.getText().trim());
            preparedStatement.setString(2, problemCategory.getText().trim());
            preparedStatement.setString(3, reason.getText().trim());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(reports.size() == 1){
            reports.remove(0);
            Report blank = new Report("", "", "");
            setReportData(blank);
        }
        else if(reports.size() <= 2){
            if(index == 0){
                setReportData(reports.get(1));
                reports.remove(0);
            }
            else if(index == 1){
                setReportData(reports.get(0));
                reports.remove(1);
            }
        }
        else{
            if(index == 0){
                setReportData(reports.get(1));
                reports.remove(0);
            }
            else if(index == reports.size()){
                setReportData(reports.get(reports.size()-2));
                reports.remove(reports.size()-1);
            }
            else{
                setReportData(reports.get(index+1));
                reports.remove(index);
            }
        }
    }

    @FXML
    void resolvedButtonClicked(ActionEvent event) {
        //Resolving means deleting, first we delete the confession from the database, then we change screen display
        int index = getIndexOfCurrentReport();

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reports WHERE againstID = ? AND reportCategory = ? AND reportContent = ?");
            preparedStatement.setString(1, reportedID.getText().trim());
            preparedStatement.setString(2, problemCategory.getText().trim());
            preparedStatement.setString(3, reason.getText().trim());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Get the confession post that we going to delete
        ConfessionPost toDelete = null;
        for(int i = 0 ; i < allConfession.size(); i++){
            if(allConfession.get(i).getConfessionID().equalsIgnoreCase(reportedID.getText().trim())){
                toDelete = allConfession.get(i);
                break;
            }
        }

        if(toDelete != null){
            //Confession post have not been deleted...
//            ArrayList<ConfessionPost> everything = ConfessionPost.initialize();
//            ConfessionPost.deleteConfession(toDelete, everything);
//            ConfessionPost.wrapUp(everything);
            ConfessionPost.removeReplyToPointingInDB(toDelete.getConfessionID());
            ConfessionPost.deleteConfession(toDelete.getConfessionID());
        }
        if(reports.size() == 1){
            Report blank = new Report("", "", "");
            setReportData(blank);
            reports.remove(0);
        }
        else if(reports.size() <= 2){
            if(index == 0){
                setReportData(reports.get(1));
                reports.remove(0);
            }
            else if(index == 1){
                setReportData(reports.get(0));
                reports.remove(1);
            }
        }
        else{
            if(index == 0){
                setReportData(reports.get(1));
                reports.remove(0);
            }
            else if(index == reports.size()){
                setReportData(reports.get(reports.size()-2));
                reports.remove(reports.size()-1);
            }
            else{
                setReportData(reports.get(index+1));
                reports.remove(index);
            }
        }

        initializeTimeline();


    }

    @FXML
    void viewReportedConfessionClicked(ActionEvent event) throws IOException {
        //Get the confession to display
        ConfessionPost reportedConfession = null;

        for(int i = 0; i < allConfession.size(); i++){
            if(reportedID.getText().trim().equalsIgnoreCase(allConfession.get(i).getConfessionID())){
                reportedConfession = allConfession.get(i);
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("reportedFeed.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        reportedFeedController reportedFeedController = fxmlLoader.getController();
        if (reportedConfession != null) {
            reportedFeedController.setReportedPostData(reportedConfession);

            Stage newStage = new Stage();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(anchorPane);
            newStage.setScene(scene);
            newStage.setTitle("Reported Feed Interface");
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);
            newStage.show();
        }
    }

    private List<ConfessionPost> getAllConfession() {
        return ConfessionPost.initialize();
    }

    private List<ConfessionPost> getSubmissions(){
        return ConfessionPost.getSubmittedConfession();

    }

    private void setToRemove(ConfessionPost confessionPost){
        //Get the index of the item to be removed
        int index = 0;
        for(int i = 0 ; i < confessionIDs.length; i++){
            if(confessionPost.getConfessionContent().equalsIgnoreCase(confessionIDs[i])){
                index = i;
                break;
            }
        }
        //We already have the index to be removed
        submissionVBox.getChildren().remove(index);
        initializeTimeline();
    }

    private void initializeTimeline(){
        timelineScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timelineScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timelineVBox.setAlignment(Pos.CENTER);
        timelineVBox.setSpacing(30);
        confessionPosts.clear();
        confessionPosts.addAll(getAllConfession());

        if(confessionPosts.size() > 0){
            myListener = new MyListener() {
                @Override
                public void onClickListener(ConfessionPost confessionPost) {

                }
            };
        }
        try{
            for(int i = 0 ; i < confessionPosts.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("feed.fxml"));
                HBox hBox = fxmlLoader.load();
                timelineVBox.setAlignment(Pos.CENTER);

                FeedController feedController = fxmlLoader.getController();
                feedController.setData(confessionPosts.get(i), myListener);
                hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                hBox.setMaxWidth(Region.USE_COMPUTED_SIZE);


                timelineVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                timelineVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                timelineVBox.setMaxHeight(Region.USE_PREF_SIZE);


                timelineVBox.getChildren().add(hBox);

                timelineVBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                timelineVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                timelineVBox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                timelineVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                timelineVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                timelineVBox.setMaxHeight(Region.USE_PREF_SIZE);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeSubmission(){
        submissionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        submissionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        submissionVBox.setAlignment(Pos.CENTER);
        submissionVBox.setSpacing(30);
        confessionPosts.clear();
        confessionPosts.addAll(getSubmissions());
        confessionIDs = new String[confessionPosts.size()];

        if(confessionPosts.size() > 0){
            this.submissionListener = new MyListener() {
                @Override
                public void onClickListener(ConfessionPost confessionPost) {
                    setToRemove(confessionPost);
                }
            };
        }
        try{
            for(int i = 0 ; i < confessionPosts.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submission.fxml"));
                HBox hBox = fxmlLoader.load();
                submissionVBox.setAlignment(Pos.CENTER);
                confessionIDs[i] = confessionPosts.get(i).getConfessionID();

                SubmissionController feedController = fxmlLoader.getController();
                feedController.setSubmission(confessionPosts.get(i), submissionListener);
                hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                hBox.setMaxWidth(Region.USE_COMPUTED_SIZE);


                submissionVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                submissionVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                submissionVBox.setMaxHeight(Region.USE_PREF_SIZE);


                submissionVBox.getChildren().add(hBox);

                submissionVBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                submissionVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                submissionVBox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                submissionVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                submissionVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                submissionVBox.setMaxHeight(Region.USE_PREF_SIZE);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeReports(){
        //First get all the reports from database
        reports = new ArrayList<>();
        reportedConfessions = new ArrayList<>();
        allConfession = ConfessionPost.initialize();
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reports");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                reports.add(new Report(resultSet.getString(2), resultSet.getString(3), resultSet.getString(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < reports.size(); i++){
            for(int j = 0 ; j < allConfession.size(); j++){
                if(allConfession.get(j).getConfessionID().equalsIgnoreCase(reports.get(i).getReportAgainstID())){
                    reportedConfessions.add(allConfession.get(j));
                    break;
                }
            }
        }
        if(reportedConfessions.size() == 0){
            return;
        }

        //By now we already have all the reported confessions ready to be displayed
        setReportData(reports.get(0));
    }

    private void initializeBannedUser(){
        ObservableList<String> bannedUsers = FXCollections.observableArrayList();
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM userlogindata WHERE isBanned = ?");
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                bannedUsers.add(resultSet.getString(1));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //We now contains Observable list containing banned user
        bannedList.setItems(bannedUsers);
    }



    private void setReportData(Report report){
        reportedID.setText(report.getReportAgainstID());
        problemCategory.setText(report.getReportCategory());
        reason.setText(report.getReportContent());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTimeline();
        initializeSubmission();
        initializeReports();
        initializeBannedUser();
    }
}
