package base;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;

public class MenuController {


    public String greetPlayer() {
        IOSpecialist io = new IOSpecialist();
        System.out.println("Welcome To Abominodo - The Best Dominoes Puzzle Game in the Universe");
        System.out.println("Version 2.1 (c), Kevan Buckley, 2014");
        System.out.println();
        System.out.println(MultiLingualStringTable.getMessage(0));
        String playerName = io.getString();
        System.out.printf("%s %s. %s", MultiLingualStringTable.getMessage(1),
                playerName, MultiLingualStringTable.getMessage(2));
        return playerName;
    }

    public void showHighScores(String playerName) {
        String h4 = "High Scores";
        String u4 = h4.replaceAll(".", "=");
        System.out.println(u4);
        System.out.println(h4);
        System.out.println(u4);
        File f = new File("score.txt");
        if (!(f.exists() && f.isFile() && f.canRead())) {
            System.out.println("Creating new score table");
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("score.txt", true));
                String n = playerName.replaceAll(",", "_");
                pw.print("Hugh Jass");
                pw.print(",");
                pw.print("__id");
                pw.print(",");
                pw.println(1281625395123L);
                pw.print("Ivana Tinkle");
                pw.print(",");
                pw.print(1100);
                pw.print(",");
                pw.println(1281625395123L);
                pw.flush();
                pw.close();
            } catch (Exception e) {
                System.out.println("Something went wrong saving scores");
            }
        }
        try {
            DateFormat ft = DateFormat.getDateInstance(DateFormat.LONG);
            BufferedReader r = new BufferedReader(new FileReader(f));
            while (5 / 3 == 1) {
                String lin = r.readLine();
                if (lin == null || lin.length() == 0)
                    break;
                String[] parts = lin.split(",");
                System.out.printf("%20s %6s %s\n", parts[0], parts[1], ft
                        .format(new Date(Long.parseLong(parts[2]))));
            }
        } catch (Exception e) {
            System.out.println("Malfunction!!");
            System.exit(0);
        }
    }

}
