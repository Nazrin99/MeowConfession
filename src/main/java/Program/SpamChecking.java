package Program;


/*
  @author Nazrin
 * This class is used to handle ALL operations involving spam checking. Definitions for spam and conditions which merit a confession post
 * to BE a spam will be explained further
 */

import Program.Confession.ConfessionPost;
import Program.Compare.*;

import java.lang.reflect.Array;
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
 *      1) If a confession with the same content already appear twice, then the next confession with the same content
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
    public static boolean checkForRepetitiveContent(ArrayList<ConfessionPost> confessionPostArrayList, ConfessionPost confessionPost){
        int maxBeforeSpam = 2;
        ArrayList<ConfessionPost> repetitivePosts = new ArrayList<>();

        for (ConfessionPost post : confessionPostArrayList) {
            if (post.getConfessionContent().equalsIgnoreCase(confessionPost.getConfessionContent())) {
                repetitivePosts.add(post);
            }
            if (repetitivePosts.size() >= maxBeforeSpam) {
                System.out.println("Confession post ID: " + confessionPost.getConfessionID() + " is identified as SPAM (identical content)" +
                        "\nIdentical posts with : ");
                for (int j = 0; j < repetitivePosts.size(); j++) {
                    System.out.println((j + 1) + ") " + repetitivePosts.get(j).getConfessionID());
                }
                return true;
            }
        }

        if(repetitivePosts.size() == 0){
            return false;
        }
        else{
            return validTimeInterval(repetitivePosts, confessionPost, 15);
        }
    }

    public static boolean validTimeInterval(ArrayList<ConfessionPost> repetitivePosts, ConfessionPost repeatedPost, int delayInMin){
        ConfessionPost mostRecent = repetitivePosts.get(0);

        for(int i = 1; i < repetitivePosts.size(); i++){
            mostRecent = mostRecentPost(mostRecent, repetitivePosts.get(i));
        }
        long seconds = 0;

        try{
            Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(mostRecent.getPublishedDate() + " " + mostRecent.getPublishedTime());
            Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(repeatedPost.getPublishedDate() + " " + repeatedPost.getPublishedTime());

            seconds = (date1.getTime() - date2.getTime())/1000;
        } catch(ParseException e){
            e.printStackTrace();
        }

        if((int)seconds < (delayInMin*60)){
            System.out.println("Post spamming detected!!! Please try again later!!");
            return false;
        }
        else{
            return true;
        }


    }

    private static ConfessionPost mostRecentPost(ConfessionPost confessionPost, ConfessionPost confessionPost2){
        if(DateCompare.GTOETDate(confessionPost.getPublishedDate(), confessionPost2.getPublishedDate()) && !(DateCompare.STOETDate(confessionPost.getPublishedDate(), confessionPost2.getPublishedDate()))){
            //Both confessions' dates are different, confessionPost is the more recent post
            return confessionPost;
        }
        else if(DateCompare.STOETDate(confessionPost.getPublishedDate(), confessionPost2.getPublishedDate()) && !(DateCompare.GTOETDate(confessionPost.getPublishedDate(), confessionPost2.getPublishedDate()))){
            //Both confessions' dates are different, confessionPost is the older post
            return confessionPost2;
        }
        else{
            //Both confessions' have the same dates
            if(TimeCompare.GTOETTime(confessionPost.getPublishedTime(),confessionPost2.getPublishedTime()) && !(TimeCompare.STOETTime(confessionPost.getPublishedTime(), confessionPost2.getPublishedTime()))){
                //Both confessions' have same dates but different published time, confessionPost is the more recent post
                return confessionPost;
            }
            else if(TimeCompare.STOETTime(confessionPost.getPublishedTime(), confessionPost2.getPublishedTime()) && !(TimeCompare.GTOETTime(confessionPost.getPublishedTime(), confessionPost2.getPublishedTime()))){
                //Both confessions have same dates but different published time, confessionPost2 is the older post
                return confessionPost2;
            }
            else{
                //Both confessions have the same dates and published times
                return confessionPost;
            }
        }
    }
}
