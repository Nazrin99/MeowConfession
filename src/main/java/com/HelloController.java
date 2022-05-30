package com;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;

import Program.Confession.*;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private ImageView image;

    @FXML
    private Button viewImageButton;

    @FXML
    void viewImageButtonClicked(ActionEvent event) throws FileNotFoundException {
        Sandbox sandbox = new Sandbox();
        Class class1 = sandbox.getClass();

        URL url = class1.getResource("MOHAMMADNAZRIN_AI.jpg");

        InputStream stream = new FileInputStream("C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\Program\\Confession\\MOHAMMADNAZRIN_AI.jpg");
        Image thisImage = new Image(stream, 0, 600, true, true);
        image = new ImageView();
        image.setImage(thisImage);
        Group root = new Group(image);
        Scene scene = new Scene(root, 595, 370);
        Stage stage = new Stage();
        stage.setTitle("Bitch");
        stage.setScene(scene);
        stage.show();

        viewImageButton.setText("How you dare you click me!!!");
    }

}
