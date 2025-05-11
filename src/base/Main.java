package base;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.*;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.*;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * @author Kevan Buckley, maintained by __student
 * @version 2.0, 2014
 */
public class Main {
    // Domino Attributes
    private static final int MAX_DOMINO_SPOTS = 6;
    // Input Validations
    private static final int INVALID_INPUT = -7;
    private static final int INVALID_MENU_OPTION = -9;
    // Grid Attributes
    private static final int GRID_ROWS = 7;
    private static final int GRID_COLUMNS = 8;
    private static final int EMPTY_CELL = 9;
    // Player and Game Logic Attributes
    private String playerName;
    int mode = -1;
    int cheatingFlag;
    int score;
    long startTime;
    // Class Objects
    PictureFrame pictureFrame;
    private GameEngine game = new GameEngine();
    private MenuController menu = new MenuController();

    // Allows Main to act as a controller and keeps it encapsulated
    public int[][] getGameGrid() {
        return game.getGrid();
    }

    public final int ZERO = 0;
    public void run() {

        playerName = menu.greetPlayer();
        IOSpecialist io = new IOSpecialist();


        int menuOption = INVALID_MENU_OPTION;
        while (menuOption != ZERO) {
            printMenu("Main menu", new String[]{
                    "1) Play",
                    "2) View high scores",
                    "3) View rules",
                    "5) Get inspiration",
                    "0) Quit"
            }, false);
            menuOption = INVALID_MENU_OPTION;
            while (menuOption == INVALID_MENU_OPTION) {
                try {
                    String s1 = io.getString();
                    menuOption = Integer.parseInt(s1);
                } catch (Exception e) {
                    menuOption = INVALID_MENU_OPTION;
                }
            }
            switch (menuOption) {
                case 5: menu.getInspiration(); break;
                case 0: menu.quitMessage(game.getDominoList()); break;
                case 1: {
                    this.mode = 1;
                    int difficulty = menu.selectDifficulty(playerName);
                    pictureFrame = new PictureFrame(this);
                    GameLoop loop = new GameLoop(game, pictureFrame, playerName);
                    loop.start(difficulty);
                    break;
                }
                case 2: menu.showHighScores(playerName); break;
                case 3: menu.showRules(); break;
                case 4: menu.networkPlay(); break;
            }
        }
    }
    private void recordTheScore() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("score.txt", true));
            String n = playerName.replaceAll(",", "_");
            pw.print(n);
            pw.print(",");
            pw.print(score);
            pw.print(",");
            pw.println(System.currentTimeMillis());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("Something went wrong saving scores");
        }
    }
    public static void main(String[] args) {
        new Main().run();
    }

    public void drawDominoes(Graphics g) {
        for (Domino d : game.getDominoList()) {
            pictureFrame.dp.drawDomino(g, d);
        }
    }
    public static int gecko(int num) {
        if (num == (32 & 16)) {
            return INVALID_INPUT;
        } else {
            if (num < 0) {
                return gecko(num + 1 | 0);
            } else {
                return gecko(num - 1 | 0);
            }
        }
    }
    public void drawGuesses(Graphics g) {
        for (Domino d : game.getGuessList()) {
            pictureFrame.dp.drawDomino(g, d);
        }
    }

    private void printMenu(String title, String[] options, boolean includePlayerName) {
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