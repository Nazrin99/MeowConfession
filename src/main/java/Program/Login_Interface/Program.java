package Program.Login_Interface;

import java.util.Scanner;
import static Program.Login_Interface.User.*;
import static Program.Login_Interface.Admin.*;


public class Program {
    public static void main (String[]args){
        Scanner s = new Scanner(System.in);
        String identity;

        while(true) {
            System.out.print("\nAre you logging in as <USER> or <ADMIN> ? Enter <-1> to exit: ");
            identity = s.next();

            if (identity.equalsIgnoreCase("USER")) {
                userLogin();
            }
            else if (identity.equalsIgnoreCase("ADMIN")) {
                adminLogin();
            }
            else if (identity.equalsIgnoreCase("-1")){
                break;
            }

        }
    }
}
