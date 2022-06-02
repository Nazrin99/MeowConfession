package GUI;


import Program.Confession.ConfessionPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class adminApproveConfession implements Initializable {
    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label idConfessLabel;

    @FXML
    private Label captionLabel;

    @FXML
    private ImageView fruitImg;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox grid;

    private List<ConfessionPost> confesses = new ArrayList<>();
    private Image image;
    private MyListener myListener;

    private List<ConfessionPost> getData() {
        List<ConfessionPost> confessionPosts = ConfessionPost.initialize();

        return confessionPosts;
    }

    private void setChosenCard(ConfessionPost confessionPost) {
        idConfessLabel.setText(confessionPost.getConfessionID());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confesses.addAll(getData());
        if (confesses.size() > 0) {
            setChosenCard(confesses.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(ConfessionPost confessionPost) {

                }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < confesses.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("submittedConfession.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                Region region1 = new Region();
                Region region2 = new Region();
                HBox hBox = new HBox();

                submittedConfessionController itemController = fxmlLoader.getController();
                itemController.setData(confesses.get(i),myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(region1);
                hBox.getChildren().add(anchorPane);
                hBox.getChildren().add(region2);
                grid.setSpacing(20);

                grid.setAlignment(Pos.CENTER);
                grid.getChildren().add(hBox); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void approveButton(ActionEvent event) {

    }

    @FXML
    void confessiomButtonClicked(ActionEvent event) throws IOException {
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
    void rejectButton(ActionEvent event) {

    }

    @FXML
    void searchButton(ActionEvent event) {

    }


}

