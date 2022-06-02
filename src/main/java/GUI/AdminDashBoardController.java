package GUI;

import Program.Confession.ConfessionPost;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashBoardController implements Initializable{

    @FXML
    private Label Menu;

    @FXML
    private Label backMenu;

    @FXML
    private AnchorPane slider;
    @FXML
    private VBox postVbox;
    @FXML
    private Label username;
    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    private ScrollPane scrollPane;
    private List<ConfessionPost> confessionPosts = new ArrayList<>();
    private MyListener myListener;
    private List<ConfessionPost> getData() {
        List<ConfessionPost> confessionPosts = ConfessionPost.initialize();

        return confessionPosts;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event->{
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)->{
                Menu.setVisible(false);
                backMenu.setVisible(true);
            });
        });
        backMenu.setOnMouseClicked(event->{
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)->{
                Menu.setVisible(true);
                backMenu.setVisible(false);
            });
        });

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        postVbox.setAlignment(Pos.CENTER);
        postVbox.setSpacing(30);
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
                postVbox.setAlignment(Pos.CENTER);
                adminFeedController feedController = fxmlLoader.getController();
                feedController.setData(confessionPosts.get(i), myListener);
                anchorPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                anchorPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                anchorPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                postVbox.setMinHeight(Region.USE_COMPUTED_SIZE);
                postVbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                postVbox.setMaxHeight(Region.USE_PREF_SIZE);


                postVbox.getChildren().add(anchorPane);

                postVbox.setMinWidth(Region.USE_COMPUTED_SIZE);
                postVbox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                postVbox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                postVbox.setMinHeight(Region.USE_COMPUTED_SIZE);
                postVbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                postVbox.setMaxHeight(Region.USE_PREF_SIZE);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void bannedButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminBannedUser.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
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
    void vacationModeClicked(ActionEvent event) {

    }




    @FXML
    void reportButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminReportConfession.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }

}
