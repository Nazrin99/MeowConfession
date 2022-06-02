package Program.Confession;


import Program.Compare.Comparators.DateComparator;
import Program.Compare.DateCompare;
import Program.Compare.TimeCompare;
import Program.Utility.Regex;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConfessionPost {
    private String confessionID;
    private ConfessionPost replyTo = null;
    private String replyToID = "";
    private ArrayList<String> repliesFromID = new ArrayList<>();
    private ArrayList<ConfessionPost> repliesFrom = new ArrayList<>();
    private String publishedDate;
    private String publishedTime;
    private String confessionContent;
    private byte[] postImage = null;
    private static int largestID = 3000;

    //Creating confession that is not replying to any other posts
    public ConfessionPost(String confessionContent) {
        this.confessionContent = confessionContent;
        this.publishedDate = givePublishedDate();
        this.publishedTime = givePublishedTime();
        this.confessionID = giveConfessionID();

    }

    //Creating confession that is replying to another post
    public ConfessionPost(ConfessionPost destinationPost, String confessionContent) {
        this.replyTo = destinationPost;
        this.replyToID = destinationPost.getConfessionID();
        this.confessionContent = confessionContent;
        this.publishedTime = givePublishedTime();
        this.publishedDate = givePublishedDate();
        this.confessionID = giveConfessionID();
    }

    //Only used for calling from database for display
    public ConfessionPost(String confessionID, String confessionContent, String publishedDate, String publishedTime, String replyToID, String repliesFromID, byte[] postImage) {
        this.confessionID = confessionID;
        this.publishedDate = publishedDate;
        this.publishedTime = publishedTime;
        this.confessionContent = confessionContent;
        this.replyToID = replyToID;
        this.repliesFromID = giveRepliesFromID(repliesFromID);
        this.postImage = postImage;
        findGreatestID(confessionID);
    }

    //Only used to create objects after being reviewed and approved by admin
    public ConfessionPost(String confessionID, String confessionContent, String publishedDate, String publishedTime, String replyToID, byte[] postImage){
        this.confessionID = confessionID;
        this.publishedDate = publishedDate;
        this.publishedTime = publishedTime;
        this.confessionContent = confessionContent;
        this.replyToID = replyToID;
        this.postImage = postImage;
    }

    public String getConfessionID() {
        return confessionID;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public ConfessionPost getReplyTo() {
        return this.replyTo;
    }

    public void setReplyTo(ConfessionPost replyTo) {
        this.replyTo = replyTo;
    }

    public ArrayList<ConfessionPost> getRepliesFrom() {
        return this.repliesFrom;
    }

    public void setRepliesFrom(ArrayList<ConfessionPost> confessionPosts) {
        this.repliesFrom = confessionPosts;
    }

    public void addRepliesFrom(ConfessionPost confessionPost) {
        this.repliesFrom.add(confessionPost);
        this.repliesFromID.add(confessionPost.getConfessionID());
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public void setConfessionID(String confessionID) {
        this.confessionID = confessionID;
    }

    public String getConfessionContent() {
        return confessionContent;
    }

    public void setConfessionContent(String confessionContent) {
        this.confessionContent = confessionContent;
    }

    public static int getLargestID() {
        return largestID;
    }

    public static void setLargestID(int largestID) {
        ConfessionPost.largestID = largestID;
    }

    public String getReplyToID() {
        return replyToID;
    }

    public void setReplyToID(String replyToID) {
        this.replyToID = replyToID;
    }

    public ArrayList<String> getRepliesFromID() {
        return this.repliesFromID;
    }

    public void setRepliesFromID(ArrayList<String> repliesFromID) {
        this.repliesFromID = repliesFromID;
    }

    public byte[] getPostImage() {
        return postImage;
    }

    public void setPostImage(byte[] postImage) {
        this.postImage = postImage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nConfession ID: " + this.getConfessionID());
        sb.append("\nPublished Date: " + this.getPublishedDate());
        sb.append("\nPublished Time: " + this.getPublishedTime());
        sb.append("\nContent: " + this.getConfessionContent());

        if (this.getReplyTo() != null) {
            sb.append("\nReplying to: " + this.getReplyTo().getConfessionID());
        }
        if (this.getRepliesFrom().size() != 0) {
            sb.append("\nReplies from: ");
            for (int i = 0; i < getRepliesFrom().size(); i++) {
                sb.append("\n" + (i + 1) + ") " + getRepliesFrom().get(i).getConfessionID());
            }
        }

        return sb.toString();
    }

    private static String givePublishedTime() {
        LocalTime time = java.time.LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String publishedTime = time.format(formatter);

        return publishedTime;
    }

    private static String givePublishedDate() {
        LocalDate date = java.time.LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String publishedDate = date.format(formatter);

        return publishedDate;
    }

    public static String giveConfessionID() {
        StringBuilder sb = new StringBuilder();
        sb.append("UM0");
        sb.append(largestID + 1);
        setLargestID(largestID);
        largestID++;

        return sb.toString();
    }

    public static ArrayList<String> giveRepliesFromID(String repliesFromID) {
        if (repliesFromID == null || repliesFromID.equalsIgnoreCase("")) {
            return new ArrayList<>();
        } else {
            ArrayList<String> repliesFromIDs = new ArrayList<>();
            for (int i = 0; i < repliesFromID.length() / 7; i++) {
                repliesFromIDs.add(repliesFromID.substring(7 * i, 7 * i + 7));
            }

            return repliesFromIDs;

        }
    }

    public static ArrayList<ConfessionPost> giveRepliesFrom(ConfessionPost confessionPost, ArrayList<ConfessionPost> arrayList) {
        ArrayList<String> repliesIDs = confessionPost.getRepliesFromID();
        ArrayList<ConfessionPost> repliesFrom = new ArrayList<>();

        for (int i = 0; i < repliesIDs.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                if (repliesIDs.get(i).equalsIgnoreCase(arrayList.get(j).getConfessionID())) {
                    repliesFrom.add(arrayList.get(j));
                }
            }
        }
        return repliesFrom;
    }

    public static ConfessionPost giveReplyTo(ConfessionPost confessionPost, ArrayList<ConfessionPost> arrayList) {
        String replyToID = confessionPost.getReplyToID();

        if (replyToID == null || replyToID.equalsIgnoreCase("")) {
            return null;
        } else {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getConfessionID().equalsIgnoreCase(replyToID)) {
                    return arrayList.get(i);
                }
            }
        }
        return null;
    }

    public static ConfessionPost obtainDestinationPost(String destinationID, ArrayList<ConfessionPost> confessionPostArrayList) {
        for (int i = 0; i < confessionPostArrayList.size(); i++) {
            if (confessionPostArrayList.get(i).getConfessionID().equalsIgnoreCase(destinationID)) {
                return confessionPostArrayList.get(i);
            }
        }
        return null;
    }

    public static void findGreatestID(String confessionID) {
        int ID = Integer.parseInt(confessionID.substring(3));

        if (ID > getLargestID()) {
            setLargestID(ID);
        }
    }

    public static String toSingleString(ArrayList<String> repliesFromID) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < repliesFromID.size(); i++) {
            sb.append(repliesFromID.get(i));
        }

        return sb.toString();
    }


    public static ArrayList<ConfessionPost> initialize() {
        ArrayList<ConfessionPost> arrayList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM confessionposts");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                arrayList.add(new ConfessionPost(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getBytes(7)));
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Reconstruct repliesFrom confession post and do pointings
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setRepliesFrom(giveRepliesFrom(arrayList.get(i), arrayList));
            arrayList.get(i).setReplyTo(giveReplyTo(arrayList.get(i), arrayList));
        }
        return arrayList;
    }

    public static void wrapUp(ArrayList<ConfessionPost> arrayList) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO confessionposts(confessionID,confessionContent,publishedDate, publishedTime, replyTo, repliesFrom) VALUES (?,?,?,?,?,?)");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM confessionposts");

            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            for (int i = 0; i < arrayList.size(); i++) {
                preparedStatement.setString(1, arrayList.get(i).getConfessionID());
                preparedStatement.setString(2, arrayList.get(i).getConfessionContent());
                preparedStatement.setString(3, arrayList.get(i).getPublishedDate());
                preparedStatement.setString(4, arrayList.get(i).getPublishedTime());
                preparedStatement.setString(5, arrayList.get(i).getReplyToID());
                preparedStatement.setString(6, toSingleString(arrayList.get(i).getRepliesFromID()));

                preparedStatement.execute();
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addConfession(ArrayList<ConfessionPost> confessionPostArrayList) {
        Scanner s = new Scanner(System.in);
        String replyTo, confessionContent;

        System.out.print("\nYou are required to enter your confession below, however you can leave the \"Reply To\" field empty by" +
                "\npressing enter. If you entered invalid confession post ID, your confession post will not be posted." +
                "\nReply To: ");
        replyTo = s.nextLine();
        System.out.print("Content (press enter when you are ready to submit): ");
        confessionContent = s.nextLine();

        if (replyTo.equalsIgnoreCase("") || replyTo == null) {
            addConfession(null, confessionContent, confessionPostArrayList);
        } else {
            addConfession(replyTo, confessionContent, confessionPostArrayList);
        }
    }

    public static void addConfession(String destinationID, String confessionContent, ArrayList<ConfessionPost> confessionPostArrayList) {

        if (destinationID == null) {
            confessionPostArrayList.add(new ConfessionPost(confessionContent));
            System.out.println("\nConfession successfully submitted");
        } else {
            ConfessionPost destinationPost = obtainDestinationPost(destinationID, confessionPostArrayList);

            if (destinationPost == null) {
                System.out.println("\nConfession post with ID: " + destinationID + " does not exist!");
                System.out.println("Confession submission failed!");
                return;
            }

            ConfessionPost newPost = new ConfessionPost(destinationPost, confessionContent);
            confessionPostArrayList.get(confessionPostArrayList.indexOf(destinationPost)).addRepliesFrom(newPost);
            confessionPostArrayList.add(newPost);
            System.out.println("\nConfession successfully submitted!!");
        }

    }

    //Batch removal method start
    public static void deleteConfession(String confessionID, ArrayList<ConfessionPost> confessionPostArrayList) {
        ConfessionPost toDelete = null;

        for (int i = 0; i < confessionPostArrayList.size(); i++) {
            if (confessionPostArrayList.get(i).getConfessionID().equalsIgnoreCase(confessionID)) {
                toDelete = confessionPostArrayList.get(i);
                break;
            }
        }

        if (toDelete == null) {
            System.out.println("Confession post to delete doesnt exist!");
            throw new NoSuchElementException();
        }

        while (toDelete.getRepliesFrom().size() != 0) {
            deleteConfession(toDelete.getRepliesFrom().get(0).getConfessionID(), confessionPostArrayList);
            toDelete.getRepliesFrom().remove(toDelete.getRepliesFrom().get(0));
        }

        //Confession post does not receive any repliesFrom other confession posts
        toDelete.setReplyTo(null);
        confessionPostArrayList.remove(toDelete);

    }

    public static void viewCurrentConfessions(ArrayList<ConfessionPost> confessionPostArrayList) {
        for (int i = 0; i < confessionPostArrayList.size(); i++) {
            System.out.println(confessionPostArrayList.get(i).toString());
        }
    }

    public static ConfessionPost searchConfessionByID(String confessionID, ArrayList<ConfessionPost> confessionPostArrayList) {
        for (int i = 0; i < confessionPostArrayList.size(); i++) {
            if (confessionPostArrayList.get(i).getConfessionID().equalsIgnoreCase(confessionID)) {
                System.out.println("Confession post found!");
                System.out.println(confessionPostArrayList.get(i).toString());
                return confessionPostArrayList.get(i);
            }
        }
        return null;
    }

    public static void searchConfessionByDate(String startDate, ArrayList<ConfessionPost> confessionPostArrayList) {
        searchConfessionByDate(startDate, null, confessionPostArrayList);
    }

    public static ArrayList<ConfessionPost> searchConfessionByDate(String startDate, String endDATE, ArrayList<ConfessionPost> confessionPostArrayList) {
        String endDate = endDATE;
        if (endDATE == null || endDATE.equalsIgnoreCase("")) {
            endDate = startDate;
        }
        ArrayList<ConfessionPost> foundByDate = new ArrayList<>();

        if (Regex.validDate(startDate) && Regex.validDate(endDate)) {
            for (int i = 0; i < confessionPostArrayList.size(); i++) {
                String postDate = confessionPostArrayList.get(i).getPublishedDate();
                if (DateCompare.GTOETDate(postDate, startDate) && DateCompare.STOETDate(postDate, endDate)) {
                    foundByDate.add(confessionPostArrayList.get(i));
                }
            }
            if (foundByDate.size() == 0 && endDATE == null) {
                return new ArrayList<>();
            }
            if (foundByDate.size() == 0) {
                return new ArrayList<>();
            }

            Collections.sort(foundByDate, new DateComparator());

            if (startDate.equalsIgnoreCase(endDate)) {
                System.out.println("\nTotal " + foundByDate.size() + " confessions posted on " + startDate);
            } else {
                System.out.println("\nTotal " + foundByDate.size() + " confessions posted between " + startDate + " and " + endDate);
            }
            for (int k = 0; k < foundByDate.size(); k++) {
                System.out.println("\n" + (k + 1) + ") Confession ID: " + foundByDate.get(k).getConfessionID());
                System.out.println("Published Date: " + foundByDate.get(k).getPublishedDate());
                System.out.println("Content: " + foundByDate.get(k).getConfessionContent().substring(0, Math.min(foundByDate.get(k).getConfessionContent().length(), 25)) + "...");
            }
            return foundByDate;
        } else {
            System.out.println("Date are not in the correct format!");
            return new ArrayList<>();
        }
    }

    public static ArrayList<ConfessionPost> searchConfessionByKeyword(String keyword, ArrayList<ConfessionPost> confessionPostArrayList) {
        String[] keywords = keyword.split(",");
        ArrayList<ConfessionPost> foundByKeywords = new ArrayList<>();
        for (int i = 0; i < confessionPostArrayList.size(); i++) {
            int counter = 0;
            for (int j = 0; j < keywords.length; j++) {
                if (confessionPostArrayList.get(i).getConfessionContent().toUpperCase(Locale.ROOT).contains(keywords[j].trim().toUpperCase(Locale.ROOT))) {
                    counter++;
                }
                if (counter == keywords.length) {
                    foundByKeywords.add(confessionPostArrayList.get(i));
                }
            }
        }
        if (foundByKeywords.size() == 0) {
            return new ArrayList<>();
        }
        for (int k = 0; k < foundByKeywords.size(); k++) {
            System.out.println("\n" + (k + 1) + ") Confession ID: " + foundByKeywords.get(k).getConfessionID());
            System.out.println("Content: " + foundByKeywords.get(k).getConfessionContent().substring(0, Math.min(foundByKeywords.get(k).getConfessionContent().length(), 25)) + "...");

        }
        return foundByKeywords;
    }

    public static void searchConfessionByTime(String time, ArrayList<ConfessionPost> confessionPostArrayList) {
        searchConfessionByTime(time, time, confessionPostArrayList);
    }

    public static ArrayList<ConfessionPost> searchConfessionByTime(String startTime, String endTime, ArrayList<ConfessionPost> confessionPostArrayList) {

        ArrayList<ConfessionPost> foundByTime = new ArrayList<>();

        if (Regex.validTime(startTime) && Regex.validTime(endTime)) {
            for (int i = 0; i < confessionPostArrayList.size(); i++) {
                String postTime = confessionPostArrayList.get(i).getPublishedTime();
                if (TimeCompare.GTOETTime(postTime, startTime) && TimeCompare.STOETTime(postTime, endTime)) {
                    foundByTime.add(confessionPostArrayList.get(i));
                }
            }
            if (foundByTime.size() == 0 && (startTime == endTime)) {
                return new ArrayList<>();
            } else if (foundByTime.size() == 0) {
                return new ArrayList<>();
            }

            Collections.sort(foundByTime, new DateComparator());

            if(startTime.equalsIgnoreCase(endTime)){
                System.out.println("\nTotal " + foundByTime.size() + " confessions posted on " + startTime);
            }
            else{
                System.out.println("\nTotal " + foundByTime.size() + " confessions posted between " + startTime + " and " + endTime);
            }
            for (int k = 0; k < foundByTime.size(); k++) {
                System.out.println("\n" + (k + 1) + ") Confession ID: " + foundByTime.get(k).getConfessionID());
                System.out.println("Published Time: " + foundByTime.get(k).getPublishedTime());
                System.out.println("Content: " + foundByTime.get(k).getConfessionContent().substring(0, Math.min(foundByTime.get(k).getConfessionContent().length(), 25)) + "...");
            }
            return foundByTime;
        } else {
            System.out.println("Time are not in the correct format!");
            return new ArrayList<>();
        }
    }

    public static void searchConfessionPostInterface(ArrayList<ConfessionPost> confessionPostArrayList) {
        Scanner s = new Scanner(System.in);
        String answer;

        while (true) {
            System.out.print("\nHow would you like to search for confession posts?" +
                    "\n<A> Search by ID" +
                    "\n<B> Search by keyword" +
                    "\n<C> Search by published date" +
                    "\n<D> Search by published time" +
                    "\n<Enter anything else to go back>" +
                    "\nAnswer: ");
            answer = s.nextLine();

            if (answer.equalsIgnoreCase("A")) {
                String ID;
                System.out.print("\nPlease input confession post ID: ");
                ID = s.nextLine();

                ConfessionPost confessionPost = searchConfessionByID(ID, confessionPostArrayList);
                if (confessionPost == null) {
                    System.out.println("Confession post with ID: " + ID + " not found anywhere!");
                } else {
                    while (true) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("\nWhat would you like to do:");
                        if (confessionPost.getReplyTo() != null) {
                            sb.append("\n<A> View ID: " + confessionPost.getReplyToID() + " post");
                        }
                        if (confessionPost.getRepliesFrom().size() != 0) {
                            sb.append("\n<B> View all posts replying to this confession");
                        }
                        sb.append("\n<Insert anything else to go to back>");
                        System.out.print(sb + "\nAnswer: ");
                        answer = s.nextLine();

                        if (answer.equalsIgnoreCase("A")) {
                            searchConfessionByID(confessionPost.getReplyToID(), confessionPostArrayList);
                        } else if (answer.equalsIgnoreCase("B")) {
                            viewCurrentConfessions(confessionPost.getRepliesFrom());
                        } else {
                            break;
                        }
                    }
                }
            } else if (answer.equalsIgnoreCase("B")) {
                String keyword;
                System.out.print("\nPlease input keywords (to input multiple keywords, separate by commas): " +
                        "\nKeyword(s): ");
                keyword = s.nextLine();

                ArrayList<ConfessionPost> confessionPost = searchConfessionByKeyword(keyword, confessionPostArrayList);
                if (confessionPost.size() == 0) {
                    System.out.println("Confession post with keyword(s): " + keyword + " not found anywhere!");
                    break;
                } else {
                    while (true) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("\nWhat would you like to do next?");

                        for (int i = 0; i < confessionPost.size(); i++) {
                            sb.append("\n<" + (i + 1) + "> View ID: " + confessionPost.get(i).getConfessionID() + " post");
                        }
                        sb.append("\n<99> To go back");
                        sb.append("\nAnswer: ");
                        System.out.print(sb);
                        answer = s.nextLine();
                        if (Integer.parseInt(answer.trim()) <= 0 || (Integer.parseInt(answer.trim()) > confessionPost.size() && Integer.parseInt(answer.trim()) < 99)) {
                            System.out.println("Please enter a value between 1 and " + confessionPost.size());
                        } else if (Integer.parseInt(answer.trim()) > 0 && Integer.parseInt(answer.trim()) <= confessionPost.size()) {
                            ConfessionPost lookInto = confessionPost.get(Integer.parseInt(answer.trim()) - 1);
                            System.out.println("\nViewing confession post ID " + lookInto.getConfessionID());
                            System.out.print("===========================================================");
                            System.out.println(lookInto);

                            StringBuilder sb1 = new StringBuilder();
                            sb1.append("\nWhat would you like to do next?");
                            if (lookInto.getReplyTo() != null) {
                                sb1.append("\n<A> View ID: " + lookInto.getReplyTo().getConfessionID() + " post");
                            }
                            if (lookInto.getRepliesFrom().size() != 0) {
                                sb1.append("\n<B> View all confessions replying to this post (Total: " + lookInto.getRepliesFrom().size() + " posts");
                            }
                            sb1.append("\n<-1> To go back");
                            sb1.append("\nAnswer: ");
                            System.out.print(sb1);
                            answer = s.nextLine();

                            if (answer.equalsIgnoreCase("A")) {
                                System.out.println(lookInto.getReplyTo());
                            } else if (answer.equalsIgnoreCase("B")) {
                                viewCurrentConfessions(lookInto.getRepliesFrom());
                            } else {

                            }
                        } else {
                            break;
                        }
                    }
                }
            } else if (answer.equalsIgnoreCase("C")) {
                String startDate, endDate;
                System.out.print("\nYou are required to enter the date(s) for the confession posts that you want to search for." +
                        "\nShould you enter a single date, confession posts on that date ONLY will be displayed" +
                        "\nShould you enter two dates, confession posts posted between these two dates will be displayed" +
                        "\nStarting Date (dd/mm/yyyy), press Enter key to skip this field: ");
                startDate = s.nextLine();
                System.out.print("Final Date (dd/mm/yyyy), press Enter key to skip this field: ");
                endDate = s.nextLine();

                if ((startDate == null || startDate.equalsIgnoreCase("")) && (endDate == null || endDate.equalsIgnoreCase(""))) {
                    System.out.println("Starting date and final date cannot both be empty");
                } else if (Regex.validDate(startDate) || Regex.validDate(endDate)) {
                    if (startDate == null || startDate.equalsIgnoreCase("")) {
                        startDate = endDate;
                    }
                    if (endDate == null || startDate.equalsIgnoreCase("")) {
                        endDate = startDate;
                    }

                    ArrayList<ConfessionPost> confessionPost = searchConfessionByDate(startDate, endDate, confessionPostArrayList);
                    if (confessionPost.size() == 0) {
                        if (startDate.equalsIgnoreCase(endDate)) {
                            System.out.println("No confessions posted on  " + startDate);
                        } else {
                            System.out.println("No confessions posted between " + startDate + " and " + endDate);
                        }
                        break;
                    } else {
                        while (true) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("\nWhat would you like to do next?");

                            for (int i = 0; i < confessionPost.size(); i++) {
                                sb.append("\n<" + (i + 1) + "> View ID: " + confessionPost.get(i).getConfessionID() + " post");
                            }
                            sb.append("\n<99> To go back");
                            sb.append("\nAnswer: ");
                            System.out.print(sb);
                            answer = s.nextLine();
                            if (Integer.parseInt(answer.trim()) <= 0 || (Integer.parseInt(answer.trim()) > confessionPost.size() && Integer.parseInt(answer.trim()) < 99)) {
                                System.out.println("Please enter a value between 1 and " + confessionPost.size());
                            } else if (Integer.parseInt(answer.trim()) > 0 && Integer.parseInt(answer.trim()) <= confessionPost.size()) {
                                ConfessionPost lookInto = confessionPost.get(Integer.parseInt(answer.trim()) - 1);
                                System.out.println("\nViewing confession post ID " + lookInto.getConfessionID());
                                System.out.print("===========================================================");
                                System.out.println(lookInto);

                                StringBuilder sb1 = new StringBuilder();
                                sb1.append("\nWhat would you like to do next?");
                                if (lookInto.getReplyTo() != null) {
                                    sb1.append("\n<A> View ID: " + lookInto.getReplyTo().getConfessionID() + " post");
                                }
                                if (lookInto.getRepliesFrom().size() != 0) {
                                    sb1.append("\n<B> View all confessions replying to this post (Total: " + lookInto.getRepliesFrom().size() + " posts)");
                                }
                                sb1.append("\n<-1> To go back");
                                sb1.append("\nAnswer: ");
                                System.out.print(sb1);
                                answer = s.nextLine();

                                if (answer.equalsIgnoreCase("A")) {
                                    System.out.println(lookInto.getReplyTo());
                                    System.out.println("\n<Press Enter to view confession posts found by date>");
                                    try {
                                        System.in.read();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (startDate.equalsIgnoreCase(endDate)) {
                                        System.out.println("Displaying confessions posted on " + startDate);
                                        System.out.println("=============================================================================");
                                    } else {
                                        System.out.println("Displaying confessions posted between " + startDate + " and " + endDate);
                                        System.out.println("=============================================================================");
                                    }
                                } else if (answer.equalsIgnoreCase("B")) {
                                    viewCurrentConfessions(lookInto.getRepliesFrom());
                                    System.out.println("\n<Press Enter to return and view confession posts found by date>");
                                    s.nextLine();
                                } else {

                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else if (answer.equalsIgnoreCase("D")) {
                String startTime, endTime;
                System.out.print("\nYou are required to enter the time/timeframe for the confession posts that you want to search for." +
                        "\nShould you enter a single time, confessions posted on that time ONLY will be displayed" +
                        "\nShould you enter a timeframe, confessions posted between this timeframe will be displayed" +
                        "\nStarting Time (hh:mm:ss), press Enter key to skip this field: ");
                startTime = s.nextLine();
                System.out.print("Final Time (hh:mm:ss), press Enter key to skip this field: ");
                endTime = s.nextLine();

                if ((startTime == null || startTime.equalsIgnoreCase("")) && (endTime == null || endTime.equalsIgnoreCase(""))) {
                    System.out.println("Starting time and end date cannot both be empty");
                } else if (Regex.validTime(startTime) || Regex.validTime(endTime)) {
                    if (startTime == null || startTime.equalsIgnoreCase("")) {
                        startTime = endTime;
                    }
                    else if (endTime == null || startTime.equalsIgnoreCase("")) {
                        endTime = startTime;
                    }

                    ArrayList<ConfessionPost> confessionPost = searchConfessionByTime(startTime, endTime, confessionPostArrayList);
                    if (confessionPost.size() == 0) {
                        if (startTime.equalsIgnoreCase(endTime)) {
                            System.out.println("No confessions posted on  " + startTime);
                        } else {
                            System.out.println("No confessions posted between " + startTime + " and " + endTime);
                        }
                        break;
                    } else {
                        while (true) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("\nWhat would you like to do next?");

                            for (int i = 0; i < confessionPost.size(); i++) {
                                sb.append("\n<" + (i + 1) + "> View ID: " + confessionPost.get(i).getConfessionID() + " post");
                            }
                            sb.append("\n<99> To go back");
                            sb.append("\nAnswer: ");
                            System.out.print(sb);
                            answer = s.nextLine();
                            if (Integer.parseInt(answer.trim()) <= 0 || (Integer.parseInt(answer.trim()) > confessionPost.size() && Integer.parseInt(answer.trim()) < 99)) {
                                System.out.println("Please enter a value between 1 and " + confessionPost.size());
                            } else if (Integer.parseInt(answer.trim()) > 0 && Integer.parseInt(answer.trim()) <= confessionPost.size()) {
                                ConfessionPost lookInto = confessionPost.get(Integer.parseInt(answer.trim()) - 1);
                                System.out.println("\nViewing confession post ID " + lookInto.getConfessionID());
                                System.out.print("===========================================================");
                                System.out.println(lookInto);

                                StringBuilder sb1 = new StringBuilder();
                                sb1.append("\nWhat would you like to do next?");
                                if (lookInto.getReplyTo() != null) {
                                    sb1.append("\n<A> View ID: " + lookInto.getReplyTo().getConfessionID() + " post");
                                }
                                if (lookInto.getRepliesFrom().size() != 0) {
                                    sb1.append("\n<B> View all confessions replying to this post (Total: " + lookInto.getRepliesFrom().size() + " posts)");
                                }
                                sb1.append("\n<-1> To go back");
                                sb1.append("\nAnswer: ");
                                System.out.print(sb1);
                                answer = s.nextLine();

                                if (answer.equalsIgnoreCase("A")) {
                                    System.out.println(lookInto.getReplyTo());
                                    System.out.println("\n<Press Enter to view confession posts found by date>");
                                    try {
                                        System.in.read();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (startTime.equalsIgnoreCase(endTime)) {
                                        System.out.println("Displaying confessions posted on " + startTime);
                                        System.out.println("=============================================================================");
                                    } else {
                                        System.out.println("Displaying confessions posted between " + startTime + " and " + endTime);
                                        System.out.println("=============================================================================");
                                    }
                                } else if (answer.equalsIgnoreCase("B")) {
                                    viewCurrentConfessions(lookInto.getRepliesFrom());
                                    System.out.println("\n<Press Enter to return and view confession posts found by date>");
                                    s.nextLine();
                                } else {

                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}

