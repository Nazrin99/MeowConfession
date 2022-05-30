package Program.Utility;

import java.util.regex.Pattern;

public class Regex {
    public static boolean validDate(String date) {
        if(date == null){
            return false;
        }
        return Pattern.matches("[0-9]{2}[/-][0-9]{2}[/-][0-9]{4}", date);
    }

    public static boolean validTime(String time){
        String regex;

        if(time.startsWith("1") || time.startsWith("0")){
            regex = "[0-1][0-9][:][0-5][0-9][:][0-5][0-9]";
            return Pattern.matches(regex, time);
        }
        else if(time.startsWith(("2"))){
            regex = "[2][0-3][:][0-5][0-9][:][0-5][0-9]";
            return Pattern.matches(regex, time);
        }
        else{
            return false;
        }
    }
}
