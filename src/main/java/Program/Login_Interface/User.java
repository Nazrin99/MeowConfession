package Program.Login_Interface;

import Program.Passwords.Hashing;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

public class User {
    public static boolean userLogin (String username, String password) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM userlogindata WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String result = resultSet.getString(2);
                if (Hashing.reconstructPassword(password, result)) {
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean userRegister (String username, String password){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM userlogindata WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                preparedStatement.close();
                resultSet.close();
                connection.close();
                return false;
            }
            else{
                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO userlogindata VALUES(?,?)");
                preparedStatement1.setString(1, username);
                preparedStatement1.setString(2, Hashing.hashedPassword(password));
                preparedStatement1.execute();
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
