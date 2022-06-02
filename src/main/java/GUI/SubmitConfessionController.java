package GUI;

import Program.AdminUtility.WaitQueueList;
import Program.Confession.ConfessionPost;
import Program.Utility.ImageFetch_Store;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SubmitConfessionController implements Initializable {

    @FXML
    private TextArea confessionContent;

    @FXML
    private TextField replyIDField;

    @FXML
    private Button submitButton;

    @FXML
    private ImageView image, imagePreview;

    @FXML
    private Label imagePreviewLabel, invalidReplyID;

    final FileChooser fileChooser = new FileChooser();
    Stage stage;
    Parent root;
    Scene scene;
    String url = null;

    EventHandler<MouseEvent> addImageButtonListener= new EventHandler<MouseEvent>(){

        public void handle(MouseEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG =
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg =
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG =
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                url = file.getAbsolutePath();
            }
            else{
                url = null;
            }


        }};

    @FXML
    void goBackButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("userPost.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Submit Confession Interface");
        stage.show();
    }

    @FXML
    void imageClicked(MouseEvent event) throws IOException{
        if(url == null || url.equalsIgnoreCase("")){
            return;
        }
        else{
            Image toPreview = new Image(new FileInputStream(url));
            ImageView temp = new ImageView(toPreview);
            imagePreview = temp;
        }
    }

    @FXML
    void submitButtonClicked(ActionEvent event) {
        String replyToID = replyIDField.getText().trim();
        String confessionContent = this.confessionContent.getText().trim();
        String pathToImage = this.url;
        ConfessionPost confessionPost = new ConfessionPost(confessionContent);

        if(replyToID == null || replyToID.isEmpty()){
            //Not replying to any confession post
            try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO waitinglist VALUES(?,?,?,?,?,?)");
                preparedStatement.setString(1, confessionPost.getConfessionID());
                preparedStatement.setString(2, confessionPost.getConfessionContent());
                preparedStatement.setString(3, confessionPost.getPublishedDate());
                preparedStatement.setString(4, confessionPost.getPublishedTime());
                preparedStatement.setString(5, null);
                if(pathToImage == null || pathToImage.isEmpty()){
                    preparedStatement.setBytes(6, null);
                }
                else{
                    preparedStatement.setBytes(6, ImageFetch_Store.convertImageToByte(pathToImage));
                }
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(WaitQueueList.destinationConfessionExist(replyToID)){
            //Destination confession ID exists

            try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO waitinglist VALUES(?,?,?,?,?,?)");
                preparedStatement.setString(1, confessionPost.getConfessionID());
                preparedStatement.setString(2, confessionPost.getConfessionContent());
                preparedStatement.setString(3, confessionPost.getPublishedDate());
                preparedStatement.setString(4, confessionPost.getPublishedTime());
                preparedStatement.setString(5, replyToID);
                preparedStatement.setString(6, pathToImage);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            //Destination confession ID does not exist
            invalidReplyID.setText("Confession ID: " + replyToID + " does not exist!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.setOnMouseClicked(addImageButtonListener);
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                if(resultSet.getString(2) == null || resultSet.getString(2).isEmpty()){

                }
                else{
                    replyIDField.setText(resultSet.getString(2).trim());
                    replyIDField.setEditable(false);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
