package Program.AdminUtility;


/*
  @author Nazrin
 * This class is used to handle ALL operations involving spam checking. Definitions for spam and conditions which merit a confession post
 * to BE a spam will be explained further
 */

import Program.Confession.ConfessionPost;
import Program.Utility.WordChecker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Spam is defined as :
 *  1) Repeatedly submitting similar confession content
 *  2) Repeatedly submitting meaningless content
 *
 *  How to check for 1):
 *      1) If a confession with the same content already appear a fixed number of times, then the next confession with the same content
 *      will be identified as spam and not be posted.
 *      2) If confession content for two posts is same and the interval between the two posts is less than 15 minutes,
 *      then the confession is a spam.
 *
 *
 *  How to check for 2):
 *      1) If a confession content contains more than a specific percentage of "meaningless" words (definition for
 *      meaningless words will be provided later) , then the confession will be identified as spam and not be posted.
 *      2) Certain lee-ways need to be acknowledged and implemented for certain keywords since some "conventionally
 *      meaningless" words are in-fact meaningful for some.
 */

public class SpamChecking {
    public static boolean checkForRepetitiveContent(ConfessionPost confessionPost){
        int maxBeforeSpam = 5;
        ArrayList<ConfessionPost> confessionPostArrayList = ConfessionPost.initialize();
        confessionPostArrayList.addAll(WaitQueueList.obtainHeldConfession());
        ArrayList<ConfessionPost> repetitivePosts = new ArrayList<>();

        for (ConfessionPost post : confessionPostArrayList) {
            if (similarity(post.getConfessionContent(), confessionPost.getConfessionContent()) > 0.8) {
                repetitivePosts.add(post);
            }
            if (repetitivePosts.size() > maxBeforeSpam) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> obtainSimilarPostsID(ConfessionPost confessionPost){
        ArrayList<ConfessionPost> confessionPostArrayList = ConfessionPost.initialize();
        confessionPostArrayList.addAll(WaitQueueList.obtainHeldConfession());
        ArrayList<String> similarPostIDs = new ArrayList<>();

        for(int i = 0; i < confessionPostArrayList.size(); i++){
            for (ConfessionPost post : confessionPostArrayList) {
                if (similarity(post.getConfessionContent(), confessionPost.getConfessionContent()) > 0.8) {
                    similarPostIDs.add(post.getConfessionID());
                }
            }
        }
        return similarPostIDs;
    }

    public static ConfessionPost obtainMostRecentSimilarPost(ConfessionPost confessionPost){
        ArrayList<ConfessionPost> allConfession = ConfessionPost.initialize();
        allConfession.addAll(WaitQueueList.obtainHeldConfession());
        ArrayList<ConfessionPost> repetitivePosts = new ArrayList<>();

        for(int i = 0 ; i < allConfession.size(); i++){
            if(similarity(allConfession.get(i).getConfessionContent(), confessionPost.getConfessionContent()) > 0.8){
                repetitivePosts.add(allConfession.get(i));
            }
        }
        if(repetitivePosts.size() == 0){
            return null;
        }
        ConfessionPost mostRecent = repetitivePosts.get(0);

        for(int i = 1; i < repetitivePosts.size(); i++){
            mostRecent = mostRecentPost(mostRecent, repetitivePosts.get(i));
        }
        return mostRecent;
    }

    public static boolean validTimeInterval(ConfessionPost repeatedPost){
        ConfessionPost mostRecent = obtainMostRecentSimilarPost(repeatedPost);
        if(mostRecent == null){
            return true;
        }

        long seconds = 0;

        try{
            Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(mostRecent.getPublishedDate() + " " + mostRecent.getPublishedTime());
            Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(repeatedPost.getPublishedDate() + " " + repeatedPost.getPublishedTime());

            seconds = (date1.getTime() - date2.getTime())/1000;
        } catch(ParseException e){
            e.printStackTrace();
        }

        if((int)seconds < (15*60)){
            return false;
        }
        else{
            return true;
        }


    }

    private static ConfessionPost mostRecentPost(ConfessionPost confessionPost, ConfessionPost confessionPost2) {
        long seconds;

        try {
            Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(confessionPost.getPublishedDate() + " " + confessionPost.getPublishedTime());
            Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(confessionPost2.getPublishedDate() + " " + confessionPost2.getPublishedTime());

            seconds = (date1.getTime() - date2.getTime()) / 1000;

            if ((int) seconds < 0) {
                return confessionPost2;
            } else if ((int) seconds > 0) {
                return confessionPost;
            } else {
                return confessionPost;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return confessionPost;
    }

    private static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0;}

        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static boolean meaningFull(ConfessionPost confessionPost){
        String[] words = confessionPost.getConfessionContent().split(" ");
        int count = 0;

        for(int i = 0; i < words.length; i++){
            if(count == 10){
                return false;
            }
            if(!(WordChecker.check_for_word(words[i].toLowerCase()))){
                count++;
            }
        }
        return true;
    }
}
