package GUI;

import Program.Utility.WordChecker;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        String[] words = {"confession", "post", "heyy"};

        for(int i = 0; i  < words.length; i++){
            System.out.println(WordChecker.check_for_word(words[i]));
        }
    }

}
