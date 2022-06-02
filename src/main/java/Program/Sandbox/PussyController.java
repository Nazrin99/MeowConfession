package Program.Sandbox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PussyController {

    @FXML
    private TextField addressField;

    @FXML
    private Button clickMe;

    @FXML
    private ImageView imageView;

    @FXML
    void clickMeClicked(ActionEvent event) throws FileNotFoundException {
        clickMe = new Button();

        String path = addressField.getText().trim();

        InputStream stream = new FileInputStream(path);

        Image image = new Image(stream);

        imageView.setImage(image);
    }

}
