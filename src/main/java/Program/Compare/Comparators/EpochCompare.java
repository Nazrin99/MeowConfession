package Program.Compare.Comparators;

import Program.Confession.ConfessionPost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class EpochCompare implements Comparator<ConfessionPost> {
    @Override
    public int compare(ConfessionPost o1, ConfessionPost o2) {
        try{
            Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(o1.getPublishedDate() + " " + o2.getPublishedTime());
            Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(o2.getPublishedDate() + " " + o2.getPublishedTime());

            if(date1.compareTo(date2) == 0){
                return 0;
            }
            else if(date1.compareTo(date2) > 0){
                return date1.compareTo(date2);
            }
            else{
                return date2.compareTo(date1);
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return 0;
    }
}
