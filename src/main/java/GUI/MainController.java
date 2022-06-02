package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button adminButton;

    @FXML
    private Button guestButton;

    @FXML
    private Button timelineButton;

    @FXML
    private Label username;
    Stage stage;
    Parent root;
    Scene scene;
    @FXML
    void adminButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminLogin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Meow Confession");
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    void guestButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("userLogin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Meow Confession");
        stage.setMaximized(true);
        stage.show();
    }

}
