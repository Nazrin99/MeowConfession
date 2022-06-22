package Program.Utility;


import Program.Confession.ConfessionPost;

import java.sql.*;
import java.util.ArrayList;

/**
 * The SentimentAnalysis class handles bad words contained within a confession post and also to detect meaningless
 * content. The method(s) in this class is to be used alongside SpamChecking class to perform spam checking.
 *
 * This class would also be used alongside VacationMode class although the methods used from this class might be a
 * little different.
 */
public class SentimentAnalysis {
    private int status = initialize();
    private static ArrayList<String> obscenities = new ArrayList<>();
    private static ArrayList<String> allowedWords = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static ArrayList<String> getObscenities() {
        return obscenities;
    }

    public static void setObscenities(ArrayList<String> obscenities) {
        SentimentAnalysis.obscenities = obscenities;
    }

    public static ArrayList<String> getAllowedWords() {
        return allowedWords;
    }

    public static void setAllowedWords(ArrayList<String> allowedWords) {
        SentimentAnalysis.allowedWords = allowedWords;
    }

    public int initialize(){

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM obscenities");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM allowedWords");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                assert obscenities != null;
                obscenities.add(resultSet.getString(1));
            }

            resultSet.close();
            preparedStatement.close();

            resultSet = preparedStatement1.executeQuery();

            while(resultSet.next()){
                assert allowedWords != null;
                allowedWords.add(resultSet.getString(1));
            }

            resultSet.close();
            preparedStatement1.close();
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        for(int i = 0 ; i < allowedWords.size(); i++){
            if(obscenities.contains(allowedWords.get(i))){
                obscenities.remove(allowedWords.get(i));
            }
        }
        return 1;
    }

    /**
     *
     * @return true when at least one obscenity found, false otherwise
     */
    public boolean obscenityFound(ConfessionPost confessionPost){
        for(String obscenity : obscenities){
            if(confessionPost.getConfessionContent().toLowerCase().contains(obscenity.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean checkCaps(ConfessionPost confessionPost){
        //System.out.println("\nConfession post : "+str);
        int capsCount = 0;

        for (int i = 0; i < confessionPost.getConfessionContent().length(); i++) {
            if (Character.isUpperCase(confessionPost.getConfessionContent().charAt(i))) {
                capsCount++;
            }
        }
        //call method reject post here
        return capsCount < (confessionPost.getConfessionContent().length() / 2);
    }

    public void addAllowedWords(String allowedWord){
        getAllowedWords().add(allowedWord);
    }

    public void removeAllowedWords(String toRemove){
        getAllowedWords().remove(toRemove);
    }

    public void addObscenity(String obscenity){
        getObscenities().add(obscenity);
    }

    public void removeObscenity(String toRemove){
        getObscenities().remove(toRemove);
    }

    public static void wrapUp(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM obscenities");
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO obscenities VALUES(?)");
            PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM allowedwords");
            PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO allowedwords VALUES(?)");

            //Update obscenities list in database
            preparedStatement.execute();
            for(int i = 0; i < obscenities.size(); i++){
                preparedStatement1.setString(1, obscenities.get(i));
                preparedStatement1.execute();
            }
            preparedStatement.close();
            preparedStatement1.close();

            //Update allowedWords list in database
            preparedStatement2.execute();
            for(int j = 0; j < allowedWords.size(); j++){
                preparedStatement3.setString(1, allowedWords.get(j));
                preparedStatement3.execute();
            }
            preparedStatement2.close();
            preparedStatement3.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
