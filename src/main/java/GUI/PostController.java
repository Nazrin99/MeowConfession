package GUI;

import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PostController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Scene newScene;


    @FXML
    private Label loggedUser;

    @FXML
    private VBox postContainer;

    @FXML
    private VBox postVBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button toSubmitConfessionInterfaceButton;

    @FXML
    private TextField writeConfessionField;

    private List<ConfessionPost> confessionPosts = new ArrayList<>();
    private MyListener myListener;

    private List<ConfessionPost> getData() {
        List<ConfessionPost> confessionPosts = ConfessionPost.initialize();

        return confessionPosts;
    }

    @FXML
    void reportButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("reportForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Post Report Interface");
        stage.show();

    }

    @FXML
    void toSubmitConfessionInterfaceButtonClicked(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("submitConfession.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Submit Confession Interface");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        writeConfessionField.setEditable(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        postVBox.setAlignment(Pos.CENTER);
        postVBox.setSpacing(30);
        confessionPosts.addAll(getData());
        if(confessionPosts.size() > 0){
            myListener = new MyListener() {
                @Override
                public void onClickListener(ConfessionPost confessionPost) {

                }
            };
        }
        try{
            for(int i = 0 ; i < confessionPosts.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("feed.fxml"));
                HBox hBox = fxmlLoader.load();
                postVBox.setAlignment(Pos.CENTER);

                FeedController feedController = fxmlLoader.getController();
                feedController.setData(confessionPosts.get(i), myListener);
                hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                hBox.setMaxWidth(Region.USE_COMPUTED_SIZE);


                postVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setMaxHeight(Region.USE_PREF_SIZE);


                postVBox.getChildren().add(hBox);

                postVBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                postVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                postVBox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                postVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setMaxHeight(Region.USE_PREF_SIZE);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
