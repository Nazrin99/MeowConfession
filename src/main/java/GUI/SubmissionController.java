package GUI;

import Program.AdminUtility.WaitQueueList;
import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class SubmissionController {

    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView postImage;

    @FXML
    private Label submissionContent;

    @FXML
    private Label submissionID;

    @FXML
    private Label submissionPublishedDateTime;

    @FXML
    private Label submissionReplyTo;

    Stage stage;
    Parent root;
    Scene scene;

    private ConfessionPost confessionPost;
    private MyListener myListener;
    private String publishedDate, publishedTime, replyToID;
    private byte[] image;

    @FXML
    void approveButtonClicked(ActionEvent event) {
        //Construct approved confession
        ConfessionPost approvedPost = new ConfessionPost(submissionID.getText().trim(), submissionContent.getText().trim(), this.publishedDate, this.publishedTime, this.replyToID, null);

        WaitQueueList.approveConfession(approvedPost);

        myListener.onClickListener(approvedPost);
    }

    @FXML
    void click(MouseEvent event) {

    }

    @FXML
    void rejectButtonClicked(ActionEvent event) {
        //Construct rejected confession
        ConfessionPost rejectedPost = new ConfessionPost(submissionID.getText().trim(), submissionContent.getText().trim(), this.publishedDate, this.publishedTime, submissionReplyTo.getText().trim(), null);
        WaitQueueList.rejectConfession(rejectedPost);

        myListener.onClickListener(rejectedPost);

    }

    public void setSubmission(ConfessionPost confessionPost, MyListener myListener){
        this.confessionPost = confessionPost;
        this.myListener = myListener;
        if(confessionPost.getPostImage() != null){
            try{
                Image newImage = new Image(new FileInputStream("C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\buffer\\buffer.png"));
                postImage.setImage(newImage);
                this.image = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.publishedDate = confessionPost.getPublishedDate();
        this.publishedTime = confessionPost.getPublishedTime();
        this.replyToID = confessionPost.getReplyToID();
        submissionID.setText(confessionPost.getConfessionID());
        submissionContent.setText(confessionPost.getConfessionContent());
        if(!(confessionPost.getReplyToID() == null || confessionPost.getReplyToID().equalsIgnoreCase(""))){
            submissionReplyTo.setText("Replying To: " + confessionPost.getReplyToID());
        }
        submissionPublishedDateTime.setText("Published on: " + confessionPost.getPublishedDate() + ", " + confessionPost.getPublishedTime());
    }

}
