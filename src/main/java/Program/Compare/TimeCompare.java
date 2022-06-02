package Program.Compare;

public class TimeCompare {

    public static boolean GTOETTime(String postTime, String startTime){
        if(Integer.parseInt(postTime.substring(0,2)) > Integer.parseInt(startTime.substring(0,2))){
            return true;
        }
        else if(Integer.parseInt(postTime.substring(0,2)) < Integer.parseInt(startTime.substring(0,2))){
            return false;
        }
        else{
            if(Integer.parseInt(postTime.substring(3,5)) > Integer.parseInt(startTime.substring(3,5))){
                return true;
            }
            else if(Integer.parseInt(postTime.substring(3,5)) < Integer.parseInt(startTime.substring(3,5))){
                return false;
            }
            else{
                if(Integer.parseInt(postTime.substring(6)) > Integer.parseInt(startTime.substring(6))){
                    return true;
                }
                else return Integer.parseInt(postTime.substring(6)) >= Integer.parseInt(startTime.substring(6));
            }
        }
    }

    public static boolean STOETTime(String postTime, String endTime){
        if(Integer.parseInt(postTime.substring(0,2)) < Integer.parseInt(endTime.substring(0,2))){
            return true;
        }
        else if(Integer.parseInt(postTime.substring(0,2)) > Integer.parseInt(endTime.substring(0,2))){
            return false;
        }
        else{
            if(Integer.parseInt(postTime.substring(3,5)) < Integer.parseInt(endTime.substring(3,5))){
                return true;
            }
            else if(Integer.parseInt(postTime.substring(3,5)) > Integer.parseInt(endTime.substring(3,5))){
                return false;
            }
            else{
                if(Integer.parseInt(postTime.substring(6)) < Integer.parseInt(endTime.substring(6))){
                    return true;
                }
                else return Integer.parseInt(postTime.substring(6)) <= Integer.parseInt(endTime.substring(6));
            }
        }
    }
}
