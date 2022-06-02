package GUI;

import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        stage = new Stage();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("View Replies Interface");
        stage.show();
    }

    @FXML
    void replyButtonClicked(ActionEvent event) throws IOException{
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
        stage = new Stage();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Replying to Post Interface");
        stage.show();

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
        stage = new Stage();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Post Report Interface");
        stage.show();
    }

    public void setData(ConfessionPost confessionPost, MyListener myListener){
        this.confessionPost = confessionPost;
        this.myListener = myListener;
        try{
            Image newImage = new Image(new FileInputStream("C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\icon\\man.png"));
            postImage.setImage(newImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postID.setText(confessionPost.getConfessionID());
        postContent.setText(confessionPost.getConfessionContent());
        if(!(confessionPost.getReplyToID() == null || confessionPost.getReplyToID().equalsIgnoreCase(""))){
            postReplyTo.setText("Replying To: " + confessionPost.getReplyToID());
        }
        postPublishedDateTime.setText("Published on: " + confessionPost.getPublishedDate() + ", " + confessionPost.getPublishedTime());

    }

}
