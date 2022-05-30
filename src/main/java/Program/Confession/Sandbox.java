package Program.Confession;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandbox {
    public static void main(String[] args)  throws ParseException {
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("28.05.2022 21:27:16");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("28.05.2022 21:35:19");

        long seconds = (date2.getTime() - date1.getTime())/1000;
        System.out.println(seconds);
    }
}
