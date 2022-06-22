package Program.Utility;

import Program.Confession.ConfessionPost;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

/**
 * Class with methods used to store and retrieve images in SQL server as byte array.
 */

public class ImageFetch_Store {

    public static void insertImageToSQL(String confessionID, String imageFormat, String filePath) throws IOException {

        BufferedImage bImage = ImageIO.read(new File(filePath));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, imageFormat, bos );
        byte[] data = bos.toByteArray();

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE confessionposts SET images = ? WHERE confessionID = ?");
            preparedStatement1.setString(2,confessionID);
            preparedStatement1.setBytes(1,data);
            preparedStatement1.execute();
            preparedStatement1.close();
            connection.close();
            System.out.println("Image Stored");

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertImagePathToSQL(String confessionID, String filePath) throws IOException {

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO imagespath VALUES(?,?);");
            preparedStatement1.setString(1,confessionID);
            preparedStatement1.setString(2,filePath);
            preparedStatement1.execute();
            connection.close();
            System.out.println("Image Stored");

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void getImageFromSQL(String confessionID) throws IOException{
        String filePath = "C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\buffer\\buffer.png";
        byte[] data = null;

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM confessionposts WHERE confessionID = ?;");
            preparedStatement.setString(1, confessionID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                data = resultSet.getBytes(7);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch(SQLException e){
            e.printStackTrace();
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "png", new File(filePath));
        System.out.println("Image Created");
    }

    public static byte[] convertImageToByte(String filePath) throws IOException{
//        BufferedImage bImage = ImageIO.read(new File(filePath));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ImageIO.write(bImage, "jpg", bos );
//        byte[] data = bos.toByteArray();

        return null;
    }

    public static Image convertByteToImage(byte[] data) throws IOException{
        String filepath = "C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\buffer\\buffer.png";
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(new File("C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\icon\\man.png"));
        Image image = SwingFXUtils.toFXImage(bImage2, null);

        System.out.println("Successfully");
        return image;
    }

    public static Image getPostImage(ConfessionPost confessionPost) throws IOException {
        Image image = new Image(new FileInputStream("C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\buffer\\buffer.png"));

        getImageFromSQL(confessionPost.getConfessionID());

        return image;
    }
}