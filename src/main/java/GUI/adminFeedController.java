package GUI;

import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class adminFeedController {

    @FXML
    private AnchorPane anchor;

    @FXML
    private Label caption;

    @FXML
    private ImageView imgPost;

    @FXML
    private ImageView profileImg;

    @FXML
    private Label userName;
    private ConfessionPost confessionPost;
    private MyListener myListener;
    @FXML
    void click(MouseEvent event) {

    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {

    }
    public void setData(ConfessionPost confessionPost, MyListener myListener){
        this.confessionPost = confessionPost;
        this.myListener = myListener;
        Image img;
        userName.setText(confessionPost.getConfessionID());
        caption.setText(confessionPost.getConfessionContent());

        if (confessionPost.getConfessionContent()!=null && !confessionPost.getConfessionContent().isEmpty()){
            caption.setText(confessionPost.getConfessionContent());
        }else{
            caption.setManaged(false);
        }

        if (true){
            img = new Image(getClass().getResourceAsStream(""));
            imgPost.setImage(img);
        }else{
            imgPost.setVisible(false);
            imgPost.setManaged(false);
        }
    }


}
