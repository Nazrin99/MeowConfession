package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminBannedUserController {
    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    private Label Menu;

    @FXML
    private Label backMenu;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane slider;

    @FXML
    void activeUserClicked(ActionEvent event) {

    }

    @FXML
    void bannedClicked(ActionEvent event) {

    }

    @FXML
    void bannedUserClicked(ActionEvent event) {

    }

    @FXML
    void confessionButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminApproveConfession.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }

    @FXML
    void dashboardButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }

    @FXML
    void reportButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AdminReportConfession.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }

}
