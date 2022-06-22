package GUI;

import Program.Confession.ConfessionPost;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class reportedFeedController {

    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView postImage;

    @FXML
    private Label reportedPostContent;

    @FXML
    private Label reportedPostID;

    @FXML
    private Label reportedPostPublishedDateTime;

    @FXML
    private Label reportedReplyTo;

    void setReportedPostData(ConfessionPost confessionPost){
        reportedPostID.setText(confessionPost.getConfessionID());
        reportedPostContent.setText(confessionPost.getConfessionContent());
        reportedPostPublishedDateTime.setText("Published on: " + confessionPost.getPublishedDate() + ", " + confessionPost.getPublishedTime());
        reportedReplyTo.setText("Replying to: " + confessionPost.getReplyToID());
    }

    @FXML
    void click(MouseEvent event) {

    }

}
