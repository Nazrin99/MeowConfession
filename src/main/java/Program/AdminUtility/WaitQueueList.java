package Program.AdminUtility;

import Program.Compare.Comparators.EpochCompare;
import Program.Confession.ConfessionPost;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This WaitQueueList will store the necessary methods and implementation to execute our waiting & queueing list
 * concept. Waiting and queueing list will be explained below.
 *
 *
 */
public class WaitQueueList {

    public static void storeHeldConfession(ConfessionPost confessionPost){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO waitinglist VALUES (?,?,?,?,?,?)");

            preparedStatement.setString(1,confessionPost.getConfessionID());
            preparedStatement.setString(2,confessionPost.getConfessionContent());
            preparedStatement.setString(3,confessionPost.getPublishedDate());
            preparedStatement.setString(4,confessionPost.getPublishedTime());
            if(confessionPost.getReplyTo() != null || !(confessionPost.getReplyToID().equalsIgnoreCase(""))){
                preparedStatement.setString(5, confessionPost.getReplyToID());
            }
            else{
                preparedStatement.setString(5, "");
            }
            if(confessionPost.getPostImage() == null){
                preparedStatement.setBytes(6, null);
            }
            else{
                preparedStatement.setBytes(6, confessionPost.getPostImage());
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM waitinglist");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                heldConfession.add(new ConfessionPost(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),resultSet.getString(4), resultSet.getString(5), resultSet.getBytes(6)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return heldConfession;
    }

    public static void approveConfession(ConfessionPost approvedPost){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
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

    public static void initialize(){
        ArrayList<ConfessionPost> heldConfession = obtainHeldConfession();
        ArrayList<ConfessionPost> overdueConfessions = new ArrayList<>();

        //find out which heldConfession is overdue
        try{
            Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(LocalDateTime.now().toString());
            Date date2;
            long seconds;

            for(int i = 0; i < heldConfession.size(); i++){
                date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(heldConfession.get(i).getPublishedDate() + " " + heldConfession.get(i).getPublishedTime());
                seconds = (date1.getTime() - date2.getTime())/1000;

                if((int)seconds > 900){
                    overdueConfessions.add(heldConfession.get(i));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Insert overdue confession into the published confessions section and assert as "Published by overdue"
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO confessionposts(confessionID, confessionContent, publishedDate, publishedTime, replyTo) VALUES(?,?,?,?,?)");

            for(int i = 0 ; i < overdueConfessions.size(); i++){
                preparedStatement.setString(1, overdueConfessions.get(i).getConfessionID());
                preparedStatement.setString(2, overdueConfessions.get(i).getConfessionContent());
                preparedStatement.setString(3, overdueConfessions.get(i).getPublishedDate());
                preparedStatement.setString(4, overdueConfessions.get(i).getPublishedTime());
                if(overdueConfessions.get(i).getReplyToID() == null || !(overdueConfessions.get(i).getReplyToID().equalsIgnoreCase(""))){
                    preparedStatement.setString(5, "");
                }
                else{
                    preparedStatement.setString(5, overdueConfessions.get(i).getReplyToID());
                }
                preparedStatement.execute();
            }
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean destinationConfessionExist(String confessionID){
        ArrayList<ConfessionPost> arrayList = Program.Confession.ConfessionPost.initialize();

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).getConfessionID().equalsIgnoreCase(confessionID)){
                return true;
            }
        }
        return false;
    }
}
