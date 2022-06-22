package GUI;

import Program.Confession.ConfessionPost;
import Program.Utility.Regex;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class searchConfessionController implements Initializable {

    @FXML
    private ScrollPane resultScrollPane;

    @FXML
    private VBox resultVBox;

    @FXML
    private Label searchLabel;

    @FXML
    private Label searchResults;

    private List<ConfessionPost> confessionPosts = new ArrayList<>();
    private MyListener myListener;

    private ArrayList<ConfessionPost> getSearchedConfession(String searchPrompt){
        // Assumptions that search prompt is not empty
        ArrayList<ConfessionPost> allConfessions = ConfessionPost.initialize();
        ArrayList<ConfessionPost> searchedConfession = new ArrayList<>();
        System.out.println(searchPrompt);

        if(Regex.validID(searchPrompt)){
            searchLabel.setText("Search by ID");

            searchedConfession.add(ConfessionPost.searchConfessionByID(searchPrompt.substring(1), allConfessions));

            searchResults.setText(searchedConfession.size() + " search result for Confession ID: " + searchPrompt.substring(1));

            return searchedConfession;
        }
        else if(searchPrompt.length() < 8){
            searchedConfession.addAll(ConfessionPost.searchConfessionByKeyword(searchPrompt, allConfessions));
            return searchedConfession;
        }
        else if(Regex.validTime(searchPrompt.substring(0, 8))){
            if(searchPrompt.length() > 8 && Regex.validTime(searchPrompt.substring(9))){
                searchLabel.setText("Search by Time");
                searchedConfession.addAll(ConfessionPost.searchConfessionByTime(searchPrompt.substring(0,8), searchPrompt.substring(9), allConfessions));
                searchResults.setText(searchedConfession.size() +  " search results for confession(s) posted between " + searchPrompt.substring(0,8) + " and " + searchPrompt.substring(9));
                return searchedConfession;
            }
            else{
                searchLabel.setText("Search by Time");
                searchedConfession.addAll(ConfessionPost.searchConfessionByTime(searchPrompt.substring(0,8), null, allConfessions));
                searchResults.setText(searchedConfession.size() + " search results for confession(s) posted on " + searchPrompt.substring(0, 8));
                return searchedConfession;
            }
        }
        else if(Regex.validDate(searchPrompt.substring(0, 10))){
            if(searchPrompt.length() > 10 && Regex.validDate(searchPrompt.substring(11))){
                searchLabel.setText("Search by Date");
                searchedConfession.addAll(ConfessionPost.searchConfessionByDate(searchPrompt.substring(0,10), searchPrompt.substring(11), allConfessions));
                searchResults.setText(searchedConfession.size() +  " search results for confession(s) posted between " + searchPrompt.substring(0,10) + " and " + searchPrompt.substring(11));
                return searchedConfession;
            }
            else{
                searchLabel.setText("Search by Date");
                searchedConfession.addAll(ConfessionPost.searchConfessionByDate(searchPrompt.substring(0,10), null, allConfessions));
                searchResults.setText(searchedConfession.size() +  " search results for confession(s) posted on " + searchPrompt.substring(0,10));
                return searchedConfession;
            }
        }
        else{
            searchedConfession.addAll(ConfessionPost.searchConfessionByKeyword(searchPrompt, allConfessions));
            return searchedConfession;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String searchPrompt = "";
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                searchPrompt = resultSet.getString(5);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        searchLabel.setText("");
        searchResults.setText("");
        resultScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        resultScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        resultVBox.setAlignment(Pos.CENTER);
        resultVBox.setSpacing(30);
        confessionPosts.addAll(getSearchedConfession(searchPrompt));

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
                resultVBox.setAlignment(Pos.CENTER);

                FeedController feedController = fxmlLoader.getController();
                if(confessionPosts.get(i) != null){
                    feedController.setData(confessionPosts.get(i), myListener);
                }
                hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                hBox.setMaxWidth(Region.USE_COMPUTED_SIZE);


                resultVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                resultVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                resultVBox.setMaxHeight(Region.USE_PREF_SIZE);


                resultVBox.getChildren().add(hBox);

                resultVBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                resultVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                resultVBox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                resultVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                resultVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                resultVBox.setMaxHeight(Region.USE_PREF_SIZE);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
