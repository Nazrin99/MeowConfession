package Program.AdminUtility;

import java.util.ArrayList;
import java.util.Collections;

public class VacationMode {
    private String str;

    //Arraylist of bad words
    static ArrayList<String> badwords = new ArrayList<>();
    static {
        Collections.addAll(badwords, "belen", "stupid", "bodoh", "babi", "pukimak", "lancau", "butoh",
                "laknat", "waknat", "pundek");
    }

    //method that do checkCaps() and then checkBadWord()
    public void autoCheck(String str){
        System.out.println("\nConfession post : "+str);
        if(checkCaps(str)){
            if(checkBadWord(str)){
                        System.out.println("[/] POST SUBMITTED");
            }
        }
    }

    //method that prints the status of submission
    public void postOnHold(boolean b){
        if(b){
            System.out.println("[.] Reviewing submission");

        }else{
            System.out.println("[X] POST REJECTED");
        }
    }


    //method to check if a submission has too many uppercase letter
    public boolean checkCaps(String str){
        //System.out.println("\nConfession post : "+str);
        int capsCount = 0;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                capsCount++;
            }
        }
        if(capsCount>=(str.length()/2)){
            System.out.println("[!] Too many uppercase letters");
            postOnHold(false);
            return false;
            //call method reject post here
        }else {
            postOnHold(true);
            return true;
        }

    }

    //method to find bad words in given String based on Arraylist
    public boolean checkBadWord(String str){
        //System.out.println("\nConfession post : "+str);
        postOnHold(true);
        for (String badword : badwords) {
            if (str.toLowerCase().contains(badword)) {
                System.out.println("[!] Bad word found : " + badword);
                postOnHold(false);
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return str;
    }
}
