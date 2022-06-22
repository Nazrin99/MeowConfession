package GUI;

import Program.AdminUtility.SpamChecking;
import Program.AdminUtility.WaitQueueList;
import Program.Confession.ConfessionPost;
import Program.Utility.ImageFetch_Store;
import Program.Utility.SentimentAnalysis;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubmitConfessionController implements Initializable {

    @FXML
    private TextArea confessionContent;

    @FXML
    private TextField replyIDField;

    @FXML
    private Button submitButton;

    @FXML
    private ImageView image, imagePreview;

    @FXML
    private Label imagePreviewLabel, invalidReplyID;

    final FileChooser fileChooser = new FileChooser();
    Stage stage;
    Parent root;
    Scene scene;
    String url = null;
    WaitQueueList waitQueueList;

    EventHandler<MouseEvent> addImageButtonListener= new EventHandler<MouseEvent>(){

        public void handle(MouseEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG =
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg =
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG =
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                url = file.getAbsolutePath();
                System.out.println(url);
                Image toPreview = null;
                try {
                    toPreview = new Image(new FileInputStream(url));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                ImageView temp = new ImageView(toPreview);
                imagePreview = temp;
            }
            else{
                url = null;
            }


        }};

    @FXML
    void goBackButtonClicked(ActionEvent event) throws IOException {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE runtime SET replyingTo = ?");
            preparedStatement.setString(1, "");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        root = FXMLLoader.load(getClass().getResource("userPost.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void imageClicked(MouseEvent event) throws IOException{
        if(url == null || url.equalsIgnoreCase("")){
            return;
        }
        else{
            Image toPreview = new Image(new FileInputStream(url));
            imagePreview.setImage(toPreview);
        }
    }

    @FXML
    void submitButtonClicked(ActionEvent event) throws IOException {
        //Check if the user can submit confession

        String replyToID = replyIDField.getText().trim();
        String confessionContent = this.confessionContent.getText().trim();
        String pathToImage = this.url;
        ConfessionPost confessionPost = new ConfessionPost(confessionContent);
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
        sentimentAnalysis.initialize();

        if(replyToID == null || replyToID.isEmpty()){
            //Not replying to any confession post
            ConfessionPost insertWQList = new ConfessionPost(confessionPost.getConfessionID(), confessionContent, confessionPost.getPublishedDate(), confessionPost.getPublishedTime(), "", ImageFetch_Store.convertImageToByte(pathToImage));
            if(sentimentAnalysis.obscenityFound(insertWQList)){
                String user = "";
                int offensesCount = 0, isBanned = 0;
                String label = "! OBSCENITY FOUND !";
                String content = "Obscenity has been found within this confession and will not accepted. Please refrain from using bad words or you will be banned from submitting";

                //Increment users offenses count;
                try{
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
                    PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE runtime SET offensesCount = ?, isBanned = ?");
                    PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE userlogindata SET offensesCount = ?, isBanned = ? WHERE username = ?");
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()){
                        user = resultSet.getString(1);
                        offensesCount = resultSet.getInt(6);
                        isBanned = resultSet.getInt(7);
                    }
                    offensesCount += 1;
                    if(offensesCount >= 5){
                        isBanned = 1;
                    }
                    resultSet.close();
                    preparedStatement.close();
                    preparedStatement1.setInt(1, offensesCount);
                    preparedStatement1.setInt(2, isBanned);
                    preparedStatement1.execute();
                    preparedStatement1.close();

                    preparedStatement2.setInt(1, offensesCount);
                    preparedStatement2.setInt(2, isBanned);
                    preparedStatement2.setString(3, user);
                    preparedStatement2.execute();
                    preparedStatement2.close();
                    connection.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else if(sentimentAnalysis.checkCaps(insertWQList)){
                String user = "";
                int offensesCount = 0, isBanned = 0;
                String label = "! MANY CAPS DETECTED !";
                String content = "Unfortunately, more than half of your post contains capital letters. Please adjust your post accordingly or you will be banned from posting!";

                //Increment users offenses count;
                try{
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
                    PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE runtime SET offensesCount = ?, isBanned = ?");
                    PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE userlogindata SET offensesCount = ?, isBanned = ? WHERE username = ?");
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()){
                        user = resultSet.getString(1);
                        offensesCount = resultSet.getInt(6);
                        isBanned = resultSet.getInt(7);
                    }
                    offensesCount += 1;
                    if(offensesCount >= 5){
                        isBanned = 1;
                    }
                    resultSet.close();
                    preparedStatement.close();
                    preparedStatement1.setInt(1, offensesCount);
                    preparedStatement1.setInt(2, isBanned);
                    preparedStatement1.execute();
                    preparedStatement1.close();

                    preparedStatement2.setInt(1, offensesCount);
                    preparedStatement2.setInt(2, isBanned);
                    preparedStatement2.setString(3, user);
                    preparedStatement2.execute();
                    preparedStatement2.close();
                    connection.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else if(SpamChecking.checkForRepetitiveContent(insertWQList)){
                ArrayList<String> similarPostIDs = SpamChecking.obtainSimilarPostsID(insertWQList);
                //Is a repeated post
                String label = "! SPAM DETECTED !";
                String content = "Unfortunately, your confession post's content has a very high similarity index with a" +
                        "number of other posts. Please modify your post's content accordingly" +
                        "\n\nConfession IDs with similar content:" +
                        "\n========================================";
                for(int i = 0; i < 5; i++){
                    content += ("\nConfession ID: " + similarPostIDs.get(i));
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();


            }
            else if(!(SpamChecking.validTimeInterval(insertWQList))){
                //Is a common post
                String label = "! SPAM DETECTED !";
                String content = "Unfortunately, another confession post with similar content was posted a short while ago. Please try submitting again later!";

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else if(!(SpamChecking.meaningFull(insertWQList))){
                //Post content is meaningless
                String label = "! SPAM DETECTED !";
                String content = "Unfortunately, your confession post contains a considerable amount of meaningless phrase! Please adjust your wordings and try again later";

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else{
                //Not a spam, store and display confirmation window
                WaitQueueList.storeHeldConfession(insertWQList);

                String label = "Confession Submitted";
                String content = "Thank you for your submission! Your confession have been received and will be displayed shortly." +
                        "\n\nConfession Post Detail:" +
                        "\n--------------------------" +
                        "\nConfession ID: " + insertWQList.getConfessionID() +
                        "\nSubmission Date: " + insertWQList.getPublishedDate() +
                        "\nSubmission Time: " + insertWQList.getPublishedTime() +
                        "\nReplying to: " + ((insertWQList.getReplyToID() == null || insertWQList.getReplyToID().isEmpty()) ? "None" : insertWQList.getReplyToID());


                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();

            }

        }
        else if(WaitQueueList.destinationConfessionExist(replyToID)){
            //Destination confession ID exists
            ConfessionPost insertWQList = new ConfessionPost(confessionPost.getConfessionID(), confessionContent, confessionPost.getPublishedDate(), confessionPost.getPublishedTime(), "", ImageFetch_Store.convertImageToByte(pathToImage));
            if(sentimentAnalysis.obscenityFound(insertWQList)){
                String user = "";
                int offensesCount = 0, isBanned = 0;
                String label = "! OBSCENITY FOUND !";
                String content = "Obscenity has been found within this confession and will not accepted. Please refrain from using bad words or you will be banned from submitting";

                //Increment users offenses count;
                try{
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
                    PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE runtime SET offensesCount = ?, isBanned = ?");
                    PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE userlogindata SET offensesCount = ?, isBanned = ? WHERE username = ?");
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()){
                        user = resultSet.getString(1);
                        offensesCount = resultSet.getInt(6);
                        isBanned = resultSet.getInt(7);
                    }
                    offensesCount += 1;
                    if(offensesCount >= 5){
                        isBanned = 1;
                    }
                    resultSet.close();
                    preparedStatement.close();
                    preparedStatement1.setInt(1, offensesCount);
                    preparedStatement1.setInt(2, isBanned);
                    preparedStatement1.execute();
                    preparedStatement1.close();

                    preparedStatement2.setInt(1, offensesCount);
                    preparedStatement2.setInt(2, isBanned);
                    preparedStatement2.setString(3, user);
                    preparedStatement2.execute();
                    preparedStatement2.close();
                    connection.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else if(!(sentimentAnalysis.checkCaps(insertWQList))){
                String user = "";
                int offensesCount = 0, isBanned = 0;
                String label = "! MANY CAPS DETECTED !";
                String content = "Unfortunately, more than half of your post contains capital letters. Please adjust your post accordingly or you will be banned form posting!";

                //Increment users offenses count;
                try{
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
                    PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE runtime SET offensesCount = ?, isBanned = ?");
                    PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE userlogindata SET offensesCount = ?, isBanned = ? WHERE username = ?");
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()){
                        user = resultSet.getString(1);
                        offensesCount = resultSet.getInt(6);
                        isBanned = resultSet.getInt(7);
                    }
                    offensesCount += 1;
                    if(offensesCount >= 5){
                        isBanned = 1;
                    }
                    resultSet.close();
                    preparedStatement.close();
                    preparedStatement1.setInt(1, offensesCount);
                    preparedStatement1.setInt(2, isBanned);
                    preparedStatement1.execute();
                    preparedStatement1.close();

                    preparedStatement2.setInt(1, offensesCount);
                    preparedStatement2.setInt(2, isBanned);
                    preparedStatement2.setString(3, user);
                    preparedStatement2.execute();
                    preparedStatement2.close();
                    connection.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else if(SpamChecking.checkForRepetitiveContent(insertWQList)){
                ArrayList<String> similarPostIDs = SpamChecking.obtainSimilarPostsID(insertWQList);
                //Is a repeated post
                String label = "! SPAM DETECTED !";
                String content = "Unfortunately, your confession post's content has a very high similarity index with a" +
                        "number of other posts. Please modify your post's content accordingly" +
                        "\n\nConfession IDs with similar content:" +
                        "\n========================================";
                for(int i = 0; i < 5; i++){
                    content += ("\nConfession ID: " + similarPostIDs.get(i));
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();


            }
            else if(!(SpamChecking.validTimeInterval(insertWQList))){
                //Is a common post
                String label = "! SPAM DETECTED !";
                String content = "Unfortunately, another confession post with similar content was posted a short while ago. Please try submitting again later!";

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else if(!(SpamChecking.meaningFull(insertWQList))){
                //Post content is meaningless
                String label = "! SPAM DETECTED !";
                String content = "Unfortunately, your confession post contains a considerable amount of meaningless phrase! Please adjust your wordings and try again later";

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();
            }
            else{
                //Not a spam, store and display confirmation window
                WaitQueueList.storeHeldConfession(insertWQList);

                String label = "Confession Submitted";
                String content = "Thank you for your submission! Your confession have been received and will be displayed shortly." +
                        "\n\nConfession Post Detail:" +
                        "\n--------------------------" +
                        "\nConfession ID: " + insertWQList.getConfessionID() +
                        "\nSubmission Date: " + insertWQList.getPublishedDate() +
                        "\nSubmission Time: " + insertWQList.getPublishedTime() +
                        "\nReplying to: " + ((insertWQList.getReplyToID() == null || insertWQList.getReplyToID().isEmpty()) ? "None" : insertWQList.getReplyToID());


                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                root = fxmlLoader.load();
                submittedConfessionController submittedConfessionController = fxmlLoader.getController();
                submittedConfessionController.setData(label ,content);

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle(label);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(stage);
                newStage.show();

            }
        }
        else{
            //Destination confession ID does not exist
            invalidReplyID.setText("Confession ID: " + replyToID + " does not exist!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.setOnMouseClicked(addImageButtonListener);
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                if(resultSet.getString(2) == null || resultSet.getString(2).isEmpty()){

                }
                else{
                    replyIDField.setText(resultSet.getString(2).trim());
                    replyIDField.setEditable(false);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
