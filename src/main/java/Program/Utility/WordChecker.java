package Program.Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordChecker {
    public static boolean check_for_word(String word) {
        // System.out.println(word);
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    "C:\\Users\\HUAWEI\\IdeaProjects\\ConfessTime\\src\\main\\resources\\GUI\\directory\\dictionary.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.indexOf(word) != -1) {
                    return true;
                }
            }
            in.close();
        } catch (IOException e) {

        }
        return false;
    }
}
