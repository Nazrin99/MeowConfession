package GUI;

import Program.Login_Interface.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class adminLoginController implements Initializable {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label invalidLabel;

    @FXML
    private TextField usernameField;
    Stage stage;
    Parent root;
    Scene scene;

    @FXML
    void backButtonClicked(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Meow Confession");
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    void loginButtonClicked(ActionEvent event) throws IOException{
        invalidLabel.setText("");
        String username = usernameField.getText().trim(), password = passwordField.getText().trim();

        if(Admin.adminLogin(username, password)){
            adminLoginValid(event);
        }
        else{
            invalidLabel.setText("** Invalid login credentials **");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        usernameField.setPromptText("Enter your username here");
        passwordField.setPromptText("Enter your password here");
    }

    public void adminLoginValid(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000, 800);
        stage.setScene(scene);
        stage.setTitle("Welcome Admin " + usernameField.getText().trim());
        stage.setMaximized(true);
        stage.show();
    }
}
