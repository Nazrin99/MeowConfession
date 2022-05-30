package Program.Login_Interface;

import Program.Passwords.Hashing;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Scanner;

public class Admin {
    public static void adminLogin () {
        Scanner s = new Scanner(System.in);
        String username, password, loginOrSignup;

        while (true) {
            System.out.print("Enter your username: ");
            username = s.nextLine();
            System.out.print("Enter your password: ");
            password = s.nextLine();

            System.out.print("\nWould you like to <LOGIN> or <SIGNUP>? Enter <-1> to go back to main screen: ");
            loginOrSignup = s.next();

            if (loginOrSignup.equalsIgnoreCase("-1")) {
                break;
            } else if (loginOrSignup.equalsIgnoreCase("LOGIN")) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "CinemaFOP");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM adminlogindata");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    int resultSetSize = 0;

                    while (resultSet.next()) {
                        resultSetSize++;
                        if (resultSet.getString(1).equals(username)) {
                            if (Hashing.reconstructPassword(password, resultSet.getString(2))) {
                                String name = resultSet.getString(1);
                                preparedStatement.close();
                                resultSet.close();
                                connection.close();
                                adminInterface(name);
                                break;
                            }
                        }
                    }
                    s.nextLine();
                    preparedStatement.close();
                    resultSet.close();

                    if (resultSetSize == 0) {
                        System.out.println("\n!!Username or password incorrect!!\n");
                        s.nextLine();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            } else if (loginOrSignup.equalsIgnoreCase("SIGNUP")) {
                boolean skip = false;
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "CinemaFOP");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM adminlogindata");
                    PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO adminlogindata VALUES(?,?)");
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        if (resultSet.getString(1).equalsIgnoreCase(username)) {
                            System.out.println("\nUSERNAME ALREADY TAKEN!\n");
                            skip = true;
                            break;
                        }
                    }
                    preparedStatement.close();
                    resultSet.close();

                    if (!skip) {
                        preparedStatement1.setString(1, username);
                        preparedStatement1.setString(2, Hashing.hashedPassword(password));
                        preparedStatement1.execute();
                        preparedStatement1.close();
                        System.out.println("\nSignup successful! Returning to credentials page...\n");
                        s.nextLine();
                    }
                    connection.close();
                    s.nextLine();

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            } else {
                s.nextLine();
            }
        }
    }

    public static void adminInterface (String adminUsername){
        System.out.println("\n!!Welcome " + adminUsername + "!!\n");

    }
}
