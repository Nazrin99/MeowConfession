package Program;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PushingImage {
    public static void main(String[] args) throws IOException {
        String address = "C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\icon\\email.png";
        byte[] bytesImages = null;

        File imageFile = new File(address);
        FileInputStream fin = new FileInputStream(imageFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        for (int readNum; (readNum = fin.read(buf)) != -1;){
            bos.write(buf, 0, readNum);
        }

        bytesImages = bos.toByteArray();

        try{
            String postID = "UM03016";

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE confessionposts SET images=? WHERE confessionID=?");
            preparedStatement.setBytes(1, bytesImages);
            preparedStatement.setString(2, postID);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
