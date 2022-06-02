package GUI;

import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class adminPostController implements Initializable {
    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    private VBox postContainer;

    @FXML
    private VBox postVBox;

    @FXML
    private ScrollPane scrollPane;
    private List<ConfessionPost> confessionPosts = new ArrayList<>();
    private MyListener myListener;

    private List<ConfessionPost> getData() {
        List<ConfessionPost> confessionPosts = ConfessionPost.initialize();

        return confessionPosts;
    }
    @FXML
    void refreshHomeButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }

    @FXML
    void reportButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminReportConfession.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                fxmlLoader.setLocation(getClass().getResource("adminFeed2.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                postVBox.setAlignment(Pos.CENTER);
                adminFeedController feedController = fxmlLoader.getController();
                feedController.setData(confessionPosts.get(i), myListener);
                anchorPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                anchorPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                anchorPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                postVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setMaxHeight(Region.USE_PREF_SIZE);


                postVBox.getChildren().add(anchorPane);

                postVBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                postVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                postVBox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                postVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                postVBox.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
