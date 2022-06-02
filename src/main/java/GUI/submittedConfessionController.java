package GUI;

import Program.Confession.ConfessionPost;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class submittedConfessionController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    @FXML
    private ImageView img;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(confessionPost);
    }

    private ConfessionPost confessionPost;
    private MyListener myListener;

    public void setData(ConfessionPost confessionPost, MyListener myListener) {
        this.confessionPost = confessionPost;
        this.myListener = myListener;
        nameLabel.setText(confessionPost.getConfessionID());
    }
}
