package Program.Utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ds", "root", "CinemaFOP");
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO images(id, idImages)" +
                    "VALUES(?,?);");
            preparedStatement1.setString(1,confessionID);
            preparedStatement1.setBytes(2,data);
            preparedStatement1.execute();
            connection.close();
            System.out.println("Image Stored");

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void getImageFromSQL(String imageID, String imageFormat, String filePath) throws IOException{
        byte[] data = null;

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ds", "root", "CinemaFOP");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM images WHERE id = '"+imageID+"';");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.getRow());

            while(resultSet.next()){
                data = resultSet.getBytes("idImages");
            }
            resultSet.close();
            connection.close();
            System.out.println("Entered");

        } catch(SQLException e){
            e.printStackTrace();
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, imageFormat, new File(filePath));
        System.out.println("Image Created");
    }
}