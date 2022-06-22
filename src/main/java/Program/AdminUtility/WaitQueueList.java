package Program.AdminUtility;

import Program.Compare.Comparators.EpochCompare;
import Program.Confession.ConfessionPost;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * This WaitQueueList will store the necessary methods and implementation to execute our waiting & queueing list
 * concept. Waiting and queueing list will be explained below.
 *
 *
 */
public class WaitQueueList extends TimerTask{
    private static LinkedList<ConfessionPost> submittedPosts = new LinkedList<>();
    private static Date timeOfNextPop = null;
    private static Timer timer = new Timer();

    public int getSize(){
        return submittedPosts.size();
    }

    public void enqueue(ConfessionPost e){
        submittedPosts.addLast(e);
    }

    public ConfessionPost dequeue(){
        return submittedPosts.removeFirst();
    }

    public static void storeHeldConfession(ConfessionPost confessionPost){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO waitinglist VALUES (?,?,?,?,?,?)");

            preparedStatement.setString(1,confessionPost.getConfessionID());
            preparedStatement.setString(2,confessionPost.getConfessionContent());
            preparedStatement.setString(3,confessionPost.getPublishedDate());
            preparedStatement.setString(4,confessionPost.getPublishedTime());
            if(confessionPost.getReplyToID() != null || !(confessionPost.getReplyToID().isEmpty()) || !(confessionPost.getReplyToID().equalsIgnoreCase(""))){
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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO confessionposts VALUES(?,?,?,?,?,?,?)");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM waitinglist WHERE confessionID = ?");

            preparedStatement.setString(1, approvedPost.getConfessionID());
            preparedStatement.setString(2, approvedPost.getConfessionContent());
            preparedStatement.setString(3, approvedPost.getPublishedDate());
            preparedStatement.setString(4, approvedPost.getPublishedTime());
            preparedStatement.setString(5, approvedPost.getReplyToID());
            preparedStatement.setString(6, "");
            preparedStatement.setBytes(7, null);
            preparedStatement.execute();
            preparedStatement.close();

            preparedStatement1.setString(1, approvedPost.getConfessionID());
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
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

    public void initialize(){
        ArrayList<ConfessionPost> heldConfession = obtainHeldConfession();
        ArrayList<ConfessionPost> overdueConfessions;
        try {
            overdueConfessions = getOverdueConfessions(heldConfession);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < overdueConfessions.size(); i++){
            approveConfession(overdueConfessions.get(i));
        }

        this.run();
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

    public static ArrayList<ConfessionPost> getOverdueConfessions(ArrayList<ConfessionPost> submittedConfessions) throws ParseException {
        ArrayList<ConfessionPost> overdueConfessions = new ArrayList<>();
        if(submittedConfessions.size() <= 0){
            Date date = new Date();
            timeOfNextPop = date;
            return new ArrayList<>();
        }
        //Assume there are confessions, we set the initial timer to be the
        int count = 0;
        int size = submittedConfessions.size();
        int delay = 0;

        if(size > 10){
            delay = 5;
        }
        else if(size > 5){
            delay = 10;
        }
        else{
            delay = 15;
        }
        Date dateNow = new Date();
        ConfessionPost firstPost = submittedConfessions.get(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String firstPostDateAndTime = firstPost.getPublishedDate() + " " + firstPost.getPublishedTime();
        Date firstPostDateTime = formatter.parse(firstPostDateAndTime);
        Calendar c = Calendar.getInstance();
        c.setTime(firstPostDateTime);
        c.add(Calendar.MINUTE, delay);
        timeOfNextPop = c.getTime();

        while(dateNow.getTime() > timeOfNextPop.getTime()) {
            if(submittedConfessions.size() == 0){
                break;
            }
            ConfessionPost toPop = submittedConfessions.get(0);
            overdueConfessions.add(toPop);
            submittedConfessions.remove(toPop);

            if (submittedConfessions.size() > 10) {
                    c.add(Calendar.MINUTE, 5);
                    timeOfNextPop = c.getTime();
            } else if (submittedConfessions.size() > 5) {
                    c.add(Calendar.MINUTE, 10);
                    timeOfNextPop = c.getTime();
            } else {
                    c.add(Calendar.MINUTE, 15);
                    timeOfNextPop = c.getTime();
            }
        }
        return overdueConfessions;
    }

    public Date popConfession(){
        //Assume that timer is correct
        int size = submittedPosts.size();
        ConfessionPost poppedPost = this.dequeue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeOfNextPop);
        if(submittedPosts.size() > 10){
            calendar.add(Calendar.MINUTE, 5);
        }
        else if(submittedPosts.size() > 5){
            calendar.add(Calendar.MINUTE, 10);
        }
        else{
            calendar.add(Calendar.MINUTE, 15);
        }
        timeOfNextPop = calendar.getTime();
        return timeOfNextPop;
    }

    @Override
    public void run() {
        if(submittedPosts.size() != 0){
            popConfession();
            System.out.println(timeOfNextPop.getTime());
        }
        this.timer = new Timer();

        timer.schedule(new WaitQueueList(), timeOfNextPop);
    }
}
