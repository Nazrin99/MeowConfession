package Program.Utility;

import Program.Compare.Comparators.EpochCompare;
import Program.Confession.ConfessionPost;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This WaitQueueList will store the necessary methods and implementation to execute our waiting & queueing list
 * concept. Waiting and queueing list will be explained below.
 *
 *
 */
public class WaitQueueList {
    public static void storeHeldConfession(ConfessionPost confessionPost){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "CinemaFOP");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO waitinglist VALUES (?,?,?,?,?)");

            preparedStatement.setString(1,confessionPost.getConfessionID());
            preparedStatement.setString(2,confessionPost.getConfessionContent());
            preparedStatement.setString(3,confessionPost.getPublishedDate());
            preparedStatement.setString(4,confessionPost.getPublishedTime());
            if(confessionPost.getReplyTo() != null){
                preparedStatement.setString(5, confessionPost.getReplyToID());
            }
            else{
                preparedStatement.setString(5, "");
            }
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ConfessionPost> obtainHeldConfession(){
        ArrayList<ConfessionPost> heldConfession = new ArrayList<>();

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://34.143.190.239/confession?useSSL=FALSE", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM waitinglist");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                heldConfession.add(new ConfessionPost(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),resultSet.getString(4), resultSet.getString(5), ""));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return heldConfession;
    }

    public static void approveConfession(ConfessionPost approvedPost){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://34.143.190.239/confession?useSSL=FALSE", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO confessionposts VALUES(?,?,?,?,?,?)");

            preparedStatement.setString(1, approvedPost.getConfessionID());
            preparedStatement.setString(2, approvedPost.getConfessionContent());
            preparedStatement.setString(3, approvedPost.getPublishedDate());
            preparedStatement.setString(4, approvedPost.getPublishedTime());
            preparedStatement.setString(5, approvedPost.getReplyToID());
            preparedStatement.setString(6, "");

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void rejectConfession(ConfessionPost rejectedPost){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://34.143.190.239/confession?useSSL=FALSE", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM waitinglist WHERE confessionID = ?");

            preparedStatement.setString(1, rejectedPost.getConfessionID());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void displayHeldConfession(ArrayList<ConfessionPost> heldPostArrayList){
        Collections.sort(heldPostArrayList, new EpochCompare());

        for(ConfessionPost posts : heldPostArrayList){
            System.out.println(posts);
        }
    }
}
