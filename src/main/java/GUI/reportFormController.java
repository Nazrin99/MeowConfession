package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class reportFormController implements Initializable {

    @FXML
    private TextField confessionID;

    @FXML
    private ChoiceBox<String> reportCategory;

    @FXML
    private TextArea reportReason;

    @FXML
    private Label username, reportConfirmation;

    Stage stage;
    Parent root;
    Scene scene;

    @FXML
    void cancelButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("userPost.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Submit Confession Interface");
        stage.show();
    }

    @FXML
    void submitButtonClicked(ActionEvent event) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reports VALUES(?, ?, ?)");
            preparedStatement.setString(1, confessionID.getText().trim());
            preparedStatement.setString(2, reportCategory.getValue());
            preparedStatement.setString(3, reportReason.getText().trim());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reportConfirmation.setText("** Report submitted successfully, thank you for contributing to a healthier community! <3 **");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportConfirmation.setText("");
        String[] reportCategories = {"Nudity","Violence", "Harassment", "Terrorism", "Hate Speech"};

        for(int i = 0 ; i < reportCategories.length; i++){
            reportCategory.getItems().add(reportCategories[i]);
        }

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                confessionID.setText(resultSet.getString(3));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        confessionID.setEditable(false);
    }
}
