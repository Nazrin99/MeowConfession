package GUI;

import Program.Login_Interface.User;
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
import java.sql.*;
import java.util.ResourceBundle;

public class userLoginController implements Initializable {
   Stage stage;
    Parent root;
    Scene scene;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label invalidLabel;

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
    void registerButtonClicked(ActionEvent event) throws IOException {
        invalidLabel.setText("");
        String username = usernameField.getText().trim(), password = passwordField.getText().trim();

        if(User.userRegister(username, password)){
            userLoginValid(event);
        }
        else{
            invalidLabel.setText("** Username already taken! **");
        }
    }

    @FXML
    void loginButtonClicked(ActionEvent event) throws IOException, SQLException {
        invalidLabel.setText("");
        String username = usernameField.getText().trim(), password = passwordField.getText().trim();

        if(User.userLogin(username, password)){
            userLoginValid(event);
        }
        else{
            invalidLabel.setText("** Invalid login credentials **");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setPromptText("Enter your username here");
        passwordField.setPromptText("Enter your password here");

    }

    public void userLoginValid(ActionEvent event) throws IOException{
        try{
            String username = usernameField.getText().trim();
            int offensesCount = 0, isBanned = 0;
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO runtime(currentLoggedUser, offensesCount, isBanned) VALUES(?,?,?)");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM runtime");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM userlogindata WHERE username = ?");
            preparedStatement2.setString(1, username);
            ResultSet resultSet = preparedStatement2.executeQuery();

            while(resultSet.next()){
                offensesCount = resultSet.getInt(3);
                isBanned = resultSet.getInt(4);
            }
            resultSet.close();
            preparedStatement2.close();

            preparedStatement1.executeUpdate();
            preparedStatement1.close();
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, offensesCount);
            preparedStatement.setInt(3, isBanned);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        root = FXMLLoader.load(getClass().getResource("userPost.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000, 800);
        stage.setScene(scene);
        stage.setTitle("Meow Confession");
        stage.setMaximized(true);
        stage.show();
    }
}


