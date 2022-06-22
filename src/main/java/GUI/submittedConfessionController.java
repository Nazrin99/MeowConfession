package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class submittedConfessionController{

    @FXML
    private Label submissionLabel;

    @FXML
    private Label mainLabel;

    @FXML
    void click(MouseEvent event) {

    }

    @FXML
    void thankYouButtonClicked(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setData(String interfaceLabel, String content){
        mainLabel.setText(interfaceLabel);
        submissionLabel.setText(content);
        submissionLabel.setWrapText(true);
    }
}
