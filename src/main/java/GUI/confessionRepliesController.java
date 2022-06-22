package GUI;

import Program.Confession.ConfessionPost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class confessionRepliesController implements Initializable {

    @FXML
    private ScrollPane repliesScrollPane;

    @FXML
    private Label repliesToLabel;

    @FXML
    private VBox repliesVBox;

    private List<ConfessionPost> confessionPosts = new ArrayList<>();
    private MyListener myListener;

    private List<ConfessionPost> getReplies(String confessionID) {
        ArrayList<ConfessionPost> repliesConfessions = new ArrayList<>();
        ArrayList<ConfessionPost> allConfessions = ConfessionPost.initialize();

        for(int i = 0; i < allConfessions.size(); i++){
            if(allConfessions.get(i).getReplyToID() != null){
                if(allConfessions.get(i).getReplyToID().equalsIgnoreCase(confessionID)){
                    repliesConfessions.add(allConfessions.get(i));
                }
            }

        }
        return repliesConfessions;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String confessionID = "";
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM runtime");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                confessionID = resultSet.getString(4);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        repliesToLabel.setText("Replies for Confession ID: " + confessionID);
        repliesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        repliesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        repliesVBox.setAlignment(Pos.CENTER);
        repliesVBox.setSpacing(30);
        confessionPosts.addAll(getReplies(confessionID));
        if(confessionPosts.size() > 0){
            myListener = new MyListener() {
                @Override
                public void onClickListener(ConfessionPost confessionPost) {

                }
            };
        }
        else{
            repliesToLabel.setText("No replies for Confession Post: " + confessionID + "!");
        }
        try{
            for(int i = 0 ; i < confessionPosts.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("feed.fxml"));
                HBox hBox = fxmlLoader.load();
                repliesVBox.setAlignment(Pos.CENTER);

                FeedController feedController = fxmlLoader.getController();
                feedController.setData(confessionPosts.get(i), myListener);
                hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                hBox.setMaxWidth(Region.USE_COMPUTED_SIZE);


                repliesVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                repliesVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                repliesVBox.setMaxHeight(Region.USE_PREF_SIZE);


                repliesVBox.getChildren().add(hBox);

                repliesVBox.setMinWidth(Region.USE_COMPUTED_SIZE);
                repliesVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                repliesVBox.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                repliesVBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                repliesVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                repliesVBox.setMaxHeight(Region.USE_PREF_SIZE);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
