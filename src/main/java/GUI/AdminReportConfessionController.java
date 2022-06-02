package GUI;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class AdminReportConfessionController extends Component implements Initializable {
    Parent root;
    Stage stage;
    Scene scene;

    Connection con;
    Statement stmt;
    ResultSet rs,rs1;
    int curRow = 0;
    PreparedStatement ps;

    @FXML
    private Label Menu;

    @FXML
    private Label backMenu;

    @FXML
    private AnchorPane slider;
    @FXML
    private Label id;

    @FXML
    private Label problems;

    @FXML
    private Label spell;
    public AdminReportConfessionController() {
        DoConnect();
    }
    public void DoConnect( ) {
        try{
            //connect to database
            String host ="";
            String uName="";
            String uPass="";
            con = DriverManager.getConnection( host, uName, uPass );

            //execute some SQL and load the records into the resultset
            stmt = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );
            String SQL = "SELECT * FROM ";
            rs = stmt.executeQuery( SQL );

            rs.next();
            int number_col = rs.getInt("");
            String number = Integer.toString( number_col );
            String prob = rs.getString("Problems");
            String reasons = rs.getString("Reason");


            //display the previous record in the text field
            id.setText(number);
            problems.setText(prob);
            spell.setText(reasons);


        }
        catch ( SQLException err ) {
            JOptionPane.showMessageDialog(this, err.getMessage( ) );
        }

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
    void bannedButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminBannedUser.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }


    @FXML
    void dashboardClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 700);
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
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        stage.setScene(scene);
        stage.setTitle("Admin Confession");
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TableColumn id = new TableColumn("ID");
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
    }

    @FXML
    void nextClicked(ActionEvent event) {
        try {
            if ( rs.next( ) ) {
                int number_col = rs.getInt("");
                String number = Integer.toString( number_col );
                String prob = rs.getString("Problems");
                String reasons = rs.getString("Reason");


                //display the previous record in the text field
                id.setText(number);
                problems.setText(prob);
                spell.setText(reasons);

            }
            else {
                rs.previous( );
                JOptionPane.showMessageDialog(this, "End of File");
            }
        }
        catch (SQLException err) {
            JOptionPane.showMessageDialog(this, err.getMessage());
        }
    }

    @FXML
    void previousClicked(ActionEvent event) {
        try {
            if ( rs.previous( ) ) {

                int number_col = rs.getInt("");
                String number = Integer.toString( number_col );
                String prob = rs.getString("Problems");
                String reasons = rs.getString("Reason");


                //display the previous record in the text field
                id.setText(number);
                problems.setText(prob);
                spell.setText(reasons);


            }
            else {
                rs.next( );
                JOptionPane.showMessageDialog(this, "Start of File");
            }
        }
        catch (SQLException err) {
            JOptionPane.showMessageDialog(this, err.getMessage());
        }
    }

    @FXML
    void resolvedClicked(ActionEvent event) {

    }
    @FXML
    void rejectClicked(ActionEvent event) {

    }

    public void ConnectionDb() {
        try {
            //connect to database

            String host = "";
            String uName = "";
            String uPass = "";
            con = DriverManager.getConnection(host, uName, uPass);

            //execute some SQL and load the records into the resultset
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM UsernamePassword";
            rs = stmt.executeQuery(SQL);

            rs.next();
            String username = rs.getString("Username");
            String pass = rs.getString("Password");

            //display the first record in the text field
            //UsernameText.setText("");
            //PasswordText.setText("");

        } catch (SQLException err) {
            JOptionPane.showMessageDialog(this, err.getMessage());
        }

    }
}

