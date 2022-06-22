package GUI;

import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class FeedController {

    @FXML
    private AnchorPane anchor;

    @FXML
    private Label postContent;

    @FXML
    private Label postPublishedDateTime;

    @FXML
    private Label postID;

    @FXML
    private Label postReplyTo;

    @FXML
    private ImageView postImage;

    Stage stage;
    Parent root;
    Scene scene;

    @FXML
    void click(MouseEvent event) {

    }

    private ConfessionPost confessionPost;
    private MyListener myListener;

    @FXML
    void postCommentButtonClicked(ActionEvent event) throws IOException{
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE runtime SET viewRepliesFromID = ?");
            preparedStatement.setString(1, this.confessionPost.getConfessionID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        root = FXMLLoader.load(getClass().getResource("confessionReplies.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage newStage = new Stage();
        scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("View Replies Interface");
        newStage.initOwner(stage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();

    }

    @FXML
    void replyButtonClicked(ActionEvent event) throws IOException{
        int isBanned = 0;

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                isBanned = resultSet.getInt(7);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(isBanned == 1){
            Stage newStage = new Stage();
            root = FXMLLoader.load(getClass().getResource("submissionBanned.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setTitle("Submission Banned");
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);
            newStage.show();
        }
        else{
            try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE runtime SET replyingTo = ?");
                preparedStatement.setString(1, this.confessionPost.getConfessionID());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            root = FXMLLoader.load(getClass().getResource("submitConfession.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage newStage = new Stage();
            scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setTitle("Replying to Post Interface");
            newStage.initOwner(stage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.show();
            newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    try{
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE runtime SET replyingTo = ?");
                        preparedStatement.setString(1, "");
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        connection.close();
                    } catch (SQLException en) {
                        throw new RuntimeException(en);
                    }
                }
            });
        }

    }

    @FXML
    void reportButtonClicked(ActionEvent event) throws IOException{
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE runtime SET reportTo = ?");
            preparedStatement.setString(1, this.confessionPost.getConfessionID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        root = FXMLLoader.load(getClass().getResource("reportForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage newStage = new Stage();
        scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Post Report Interface");
        newStage.initOwner(stage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
    }

    public void setData(ConfessionPost confessionPost, MyListener myListener){
        this.confessionPost = confessionPost;
        this.myListener = myListener;
        String[] paths = {"C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\buffer\\buffer.png",
        ""};
        if(confessionPost.getPostImage() != null){
            try{
                Image newImage = new Image(new FileInputStream("C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\buffer\\buffer.png"));
                postImage.setImage(newImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        postID.setText(confessionPost.getConfessionID());
        postContent.setText(confessionPost.getConfessionContent());
        if(!(confessionPost.getReplyToID() == null || confessionPost.getReplyToID().equalsIgnoreCase(""))){
            postReplyTo.setText("Replying To: " + confessionPost.getReplyToID());
        }
        postPublishedDateTime.setText("Published on: " + confessionPost.getPublishedDate() + ", " + confessionPost.getPublishedTime());
    }
}
