package Program.Compare;

public class DateCompare {

    /**
     *
     * @param startDate
     * @param postDate
     * @return true if date is bigger than or equal to confession post date
     * @return false if date is smaller than confession post date
     */
    public static boolean GTOETDate(String postDate, String startDate){
        if(Integer.parseInt(postDate.substring(6)) > Integer.parseInt(startDate.substring(6))){
            return true;
        }
        else if(Integer.parseInt(postDate.substring(6)) < Integer.parseInt(startDate.substring(6))){
            return false;
        }
        else{ //postDate and startDate is on the same year

            if(Integer.parseInt(postDate.substring(3,5)) > Integer.parseInt(startDate.substring(3,5))){
                return true;
            }
            else if(Integer.parseInt(postDate.substring(3,5)) < Integer.parseInt(startDate.substring(3,5))){
                return false;
            }
            else{ //postDate and endDate are on the same year and month
                if(Integer.parseInt(postDate.substring(0,2)) > Integer.parseInt(startDate.substring(0,2))){
                    return true;
                }
                else if(Integer.parseInt(postDate.substring(0,2)) < Integer.parseInt(startDate.substring(0,2))){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }

    /**
     *
     * @param postDate
     * @param endDate
     * @return true if postDate is smaller than or equal to endDate
     * @return false if postDate is bigger than endDate
     */
    public static boolean STOETDate(String postDate, String endDate){
        if(Integer.parseInt(postDate.substring(6)) < Integer.parseInt(endDate.substring(6))){
            return true;
        }
        else if(Integer.parseInt(postDate.substring(6)) > Integer.parseInt(endDate.substring(6))){
            return false;
        }
        else{ //postDate and endDate is on the same year

            if(Integer.parseInt(postDate.substring(3,5)) < Integer.parseInt(endDate.substring(3,5))){
                return true;
            }
            else if(Integer.parseInt(postDate.substring(3,5)) > Integer.parseInt(endDate.substring(3,5))){
                return false;
            }
            else{ //postDate and endDate are on the same year and month
                if(Integer.parseInt(postDate.substring(0,2)) < Integer.parseInt(endDate.substring(0,2))){
                    return true;
                }
                else if(Integer.parseInt(postDate.substring(0,2)) > Integer.parseInt(endDate.substring(0,2))){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }
}
