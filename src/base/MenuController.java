package base;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MenuController {
    private static final int INVALID_INPUT = -7;
    IOSpecialist io = new IOSpecialist();
    GameEngine game = new GameEngine();

    public void quitMessage(List<Domino> dominoList) {
        if (dominoList == null) {
            System.out.println("It is a shame that you did not want to play");
        } else {
            System.out.println("Thankyou for playing");
        }
        System.exit(0);
    }

    public void getInspiration() {
        int index = (int) (Math.random() * (Quotes.stuff.length / 3));
        String what = Quotes.stuff[index * 3];
        String who = Quotes.stuff[1 + index * 3];
        System.out.printf("%s said \"%s\"", who, what);
        System.out.println();
        System.out.println();
    }

    public String greetPlayer() { //
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

    // This method doesn't work correctly now - assuming the wlv.ac.uk link is no longer active (05/05/2025)
    // Replaced the old URL with an example I found online - purely for assignment testing.
    public void showRules() {
        String h4 = "Rules";
        String u4 = h4.replaceAll(".", "=");
        System.out.println(u4);
        System.out.println(h4);
        System.out.println(u4);
        System.out.println(h4);
        JFrame f = new JFrame("Rules by __student");
        f.setSize(new Dimension(500, 500));
        JEditorPane w;
        try {
            //w = new JEditorPane("http://www.scit.wlv.ac.uk/~in6659/abominodo/");
            w = new JEditorPane("https://xhtml.club/");
        } catch (Exception e) {
            w = new JEditorPane("text/plain",
                    "Problems retrieving the rules from the Internet");
        }
        f.setContentPane(new JScrollPane(w));
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void networkPlay() {
        System.out
                .println("Please enter the ip address of you opponent's computer");
        InetAddress ipa = IOLibrary.getIPAddress();
        new ConnectionGenius(ipa).fireUpGame();
    }

    public void playGame () {

    }

    public int selectDifficulty(String playerName) {
        printMenu("Select difficulty", new String[]{
                "1) Simples",
                "2) Not-so-simples",
                "3) Super-duper-shuffled"
        }, false, playerName);

        int difficulty = INVALID_INPUT;
        while (!(difficulty == 1 || difficulty == 2 || difficulty == 3)) {
            try {
                String input = io.getString();
                difficulty = Integer.parseInt(input);
            } catch (Exception e) {
                difficulty = INVALID_INPUT;
            }
        }
        return difficulty;
    }


    private void printMenu(String title, String[] options, boolean includePlayerName, String playerName) {
        System.out.println();
        String underline = title.replaceAll(".", "=");
        System.out.println(underline);
        System.out.println(title);
        System.out.println(underline);

        for (String option : options) {
            System.out.println(option);
        }

        if (includePlayerName) {
            System.out.println("What do you want to do " + playerName + "?");
        } else {
            System.out.println("What do you want to do?");
        }
    }


}
