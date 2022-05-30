package Program;


/**
 * @author Nazrin
 * This class is used to handle ALL operations involving spam checking. Definitions for spam and conditions which merit a confession post
 * to BE a spam will be explained further
 */

import Program.Confession.ConfessionPost;
import Program.Compare.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    public static void checkForRepetitiveContent(ArrayList<ConfessionPost> confessionPostArrayList, ConfessionPost confessionPost){
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
                return;
            }
        }

        checkForTimeInterval(repetitivePosts);
    }

    public static void checkForTimeInterval(ArrayList<ConfessionPost> confessionPosts){
        ConfessionPost mostRecent;


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
