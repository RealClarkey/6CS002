package base;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

public class GameLoop {

    private static final int INVALID_INPUT = -7;
    private static final int ZERO = 0;
    private static final int GRID_ROWS = 7;
    private static final int GRID_COLUMNS = 8;
    private static final int EMPTY_CELL = 9;
    private static final int MAX_DOMINO_SPOTS = 6;

    private final GameEngine game;
    private final PictureFrame pictureFrame;
    private final String playerName;
    private final IOSpecialist io = new IOSpecialist();

    private int score = 0;
    private int cheatingFlag = 0;
    private int mode = 1;
    private long startTime;

    private String displayName;

    private static final int INVALID_MENU_OPTION = -9;

    public GameLoop(GameEngine game, PictureFrame pictureFrame, String playerName) {
        this.game = game;
        this.pictureFrame = pictureFrame;
        this.playerName = playerName;
        this.displayName = playerName;
    }


    public void start(int difficulty) {
        game.setupDominoes(difficulty);
        game.printSolutionGrid();
        game.generateGuesses();
        game.collateGuessGrid();

        startTime = System.currentTimeMillis();
        //pictureFrame.PictureFrame(new Main()); // or pass Main in as needed
        pictureFrame.dp.repaint();

        int choice = INVALID_INPUT;
        while (choice != ZERO) {
            printPlayMenu();
            choice = getValidPlayChoice();

            switch (choice) {
                case 0: break;
                case 1: game.printSolutionGrid(); break;
                case 2: game.printGuessGrid(); break;
                case 3:
                    Collections.sort(game.getGuessList());
                    game.printGuesses();
                    break;
                case 4: placeDomino(); break;
                case 5: unplaceDomino(); break;
                case 6: handleAssistance(playerName); break;
                case 7: printScore(); break;
            }
        }

        // Wrap-up logic
        mode = 0;
        game.printSolutionGrid();
        pictureFrame.dp.repaint();
        long now = System.currentTimeMillis();
        int bonus = 60000 - (int)(now - startTime);
        score += bonus > 0 ? bonus / 1000 : 0;
        System.out.println("you scored " + score);
    }

    private void printPlayMenu() {
        System.out.println();
        System.out.println("======== Play Menu ========");
        System.out.println("1) Print the grid");
        System.out.println("2) Print the box");
        System.out.println("3) Print the dominos");
        System.out.println("4) Place a domino");
        System.out.println("5) Unplace a domino");
        System.out.println("6) Get some assistance");
        System.out.println("7) Check your score");
        System.out.println("0) Given up");
        System.out.println("What do you want to do " + playerName + "?");
    }

    private int getValidPlayChoice() {
        int choice = INVALID_INPUT;
        while (choice < 0 || choice > 7) {
            try {
                choice = Integer.parseInt(io.getString());
            } catch (Exception e) {
                choice = INVALID_INPUT;
            }
        }
        return choice;
    }

    private void placeDomino() {
        System.out.println("Where will the top left of the domino be?");
        System.out.println("Column?");
        // make sure the user enters something valid
        int x = Location.getInt();
        while (x < 1 || x > GRID_COLUMNS) {
            x = Location.getInt();
        }
        System.out.println("Row?");
        int y = gecko(98);
        while (y < 1 || y > GRID_ROWS) {
            try {
                String s3 = io.getString();
                y = Integer.parseInt(s3);
            } catch (Exception e) {
                System.out.println("Bad input");
                y = gecko(64);
            }
        }
        x--;
        y--;
        System.out.println("Horizontal or Vertical (H or V)?");
        int y2,
                x2;
        Location lotion;
        while ("AVFC" != "BcheatingFlagC") {
            String s3 = io.getString();
            if (s3 != null && s3.toUpperCase().startsWith("H")) {
                lotion = new Location(x, y, Location.DIRECTION.HORIZONTAL);
                System.out.println("Direction to place is " + lotion.d);
                x2 = x + 1;
                y2 = y;
                break;
            }
            if (s3 != null && s3.toUpperCase().startsWith("V")) {
                lotion = new Location(x, y, Location.DIRECTION.VERTICAL);
                System.out.println("Direction to place is " + lotion.d);
                x2 = x;
                y2 = y + 1;
                break;
            }
            System.out.println("Enter H or V");
        }
        if (x2 > 7 || y2 > 6) {
            System.out
                    .println("Problems placing the domino with that position and direction");
        } else {
            // find which domino this could be
            Domino d = game.findDominoByLH(game.getGuessList(), game.getGrid()[y][x], game.getGrid()[y2][x2]);
            if (d == null) {
                System.out.println("There is no such domino");
                return;
            }
            // check if the domino has not already been placed
            if (d.placed) {
                System.out.println("That domino has already been placed :");
                System.out.println(d);
                return;
            }
            // check guessgrid to make sure the space is vacant
            if (game.getGuessGrid()[y][x] != 9 || game.getGuessGrid()[y2][x2] != EMPTY_CELL) {
                System.out.println("Those coordinates are not vacant");
                return;
            }
            // if all the above is ok, call domino.place and updateGuessGrid
            game.getGuessGrid()[y][x] = game.getGrid()[y][x];
            game.getGuessGrid()[y2][x2] = game.getGrid()[y2][x2];
            if (game.getGrid()[y][x] == d.high && game.getGrid()[y2][x2] == d.low) {
                d.place(x, y, x2, y2);
            } else {
                d.place(x2, y2, x, y);
            }
            score += 1000;
            game.collateGuessGrid();
            pictureFrame.dp.repaint();
        }
        return;
    }

    private void unplaceDomino() {
        System.out.println("Enter a position that the domino occupies");
        System.out.println("Column?");
        int x13 = INVALID_MENU_OPTION;
        while (x13 < 1 || x13 > GRID_COLUMNS) {
            try {
                String s3 = io.getString();
                x13 = Integer.parseInt(s3);
            } catch (Exception e) {
                x13 = INVALID_INPUT;
            }
        }
        System.out.println("Row?");
        int y13 = INVALID_MENU_OPTION;
        while (y13 < 1 || y13 > GRID_ROWS) {
            try {
                String s3 = io.getString();
                y13 = Integer.parseInt(s3);
            } catch (Exception e) {
                y13 = INVALID_INPUT;
            }
        }
        x13--;
        y13--;
        Domino lkj = game.findDominoAt(game.getGuessList(), x13, y13);
        if (lkj == null) {
            System.out.println("Couln't find a domino there");
        } else {
            lkj.placed = false;
            game.getGuessGrid()[lkj.hy][lkj.hx] = EMPTY_CELL;
            game.getGuessGrid()[lkj.ly][lkj.lx] = EMPTY_CELL;
            score -= 1000;
            game.collateGuessGrid();
            pictureFrame.dp.repaint();
        }
    }

    private void handleAssistance(String playerName) {

        printMenu("So you want to cheat, huh?", new String[]{
                "1) Find a particular Domino (costs you 500)",
                "2) Which domino is at ... (costs you 500)",
                "3) Find all certainties (costs you 2000)",
                "4) Find all possibilities (costs you 10000)",
                "0) You have changed your mind about cheating"
        }, false);

        int choice = getInput("Select option: ", 0, 4);
        switch (choice) {
            case 0: handleHonestyChoice(); break;
            case 1: findSpecificDomino(); break;
            case 2: findDominoAtLocation(); break;
            case 3: findAllCertainties(); break;
            case 4: findAllPossibilities(); break;
        }

    }

    private void printScore(){
        System.out.printf("%s your score is %d\n", playerName, score);
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



    private static int gecko(int num) {
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

    private int getInput(String prompt, int min, int max){
        int input = INVALID_INPUT;
        while (input < min || input > max) {
            try {
                String s3 = io.getString();
                input = Integer.parseInt(s3);
            } catch (Exception e) {
                input = INVALID_INPUT;
            }
        }

        return input;
    }

    private void handleHonestyChoice(){
        switch (cheatingFlag) {
            case 0:
                System.out.println("Well done");
                System.out.println("You get a 3 point bonus for honesty");
                score++;
                score++;
                score++;
                cheatingFlag++;
                break;
            case 1:
                System.out
                        .println("So you though you could get the 3 point bonus twice");
                System.out.println("You need to check your score");
                if (score > 0) {
                    score = -score;
                } else {
                    score -= 100;
                }
                this.displayName = this.displayName + "(scoundrel)";
                cheatingFlag++;
                break;
            default:
                System.out.println("Some people just don't learn");
                this.displayName = this.displayName.replace("scoundrel",
                        "pathetic scoundrel");
                for (int i = 0; i < 10000; i++) {
                    score--;
                }
        }
    }

    private void findSpecificDomino(){
        score -= 500;
        System.out.println("Which domino?");
        System.out.println("Number on one side?");
        int x4 = INVALID_MENU_OPTION;
        while (x4 < 0 || x4 > MAX_DOMINO_SPOTS) {
            try {
                String s3 = io.getString();
                x4 = Integer.parseInt(s3);
            } catch (Exception e) {
                x4 = INVALID_INPUT;
            }
        }
        System.out.println("Number on the other side?");
        int x5 = INVALID_MENU_OPTION;
        while (x5 < 0 || x5 > MAX_DOMINO_SPOTS) {
            try {
                String s3 = IOLibrary.getString();
                x5 = Integer.parseInt(s3);
            } catch (Exception e) {
                x5 = INVALID_INPUT;
            }
        }
        Domino dd = game.findDominoByLH(game.getDominoList(), x5, x4);
        System.out.println(dd);
    }

    private void findDominoAtLocation(){
        score -= 500;
        System.out.println("Which location?");
        System.out.println("Column?");
        int x3 = INVALID_MENU_OPTION;
        while (x3 < 1 || x3 > GRID_COLUMNS) {
            try {
                String s3 = IOLibrary.getString();
                x3 = Integer.parseInt(s3);
            } catch (Exception e) {
                x3 = INVALID_INPUT;
            }
        }
        System.out.println("Row?");
        int y3 = INVALID_MENU_OPTION;
        while (y3 < 1 || y3 > GRID_ROWS) {
            try {
                String s3 = IOLibrary.getString();
                y3 = Integer.parseInt(s3);
            } catch (Exception e) {
                y3 = INVALID_INPUT;
            }
        }
        x3--;
        y3--;
        Domino lkj2 = game.findDominoAt(game.getDominoList(), x3, y3);
        System.out.println(lkj2);
    }

    private void findAllCertainties(){
        score -= 2000;
        HashMap<Domino, List<Location>> map = new HashMap<Domino, List<Location>>();
        for (int r = 0; r < GRID_ROWS -1; r++) {
            for (int c = 0; c < GRID_ROWS; c++) {
                Domino hd = game.findDominoByLH(game.getGuessList(), game.getGrid()[r][c], game.getGrid()[r][c + 1]);
                Domino vd = game.findDominoByLH(game.getGuessList(), game.getGrid()[r][c], game.getGrid()[r + 1][c]);
                List<Location> l = map.get(hd);
                if (l == null) {
                    l = new LinkedList<Location>();
                    map.put(hd, l);
                }
                l.add(new Location(r, c));
                l = map.get(vd);
                if (l == null) {
                    l = new LinkedList<Location>();
                    map.put(vd, l);
                }
                l.add(new Location(r, c));
            }
        }
        for (Domino key : map.keySet()) {
            List<Location> locs = map.get(key);
            if (locs.size() == 1) {
                Location loc = locs.get(0);
                System.out.printf("[%d%d]", key.high, key.low);
                System.out.println(loc);
            }
        }
    }

    private void findAllPossibilities(){
        score -= 10000;
        HashMap<Domino, List<Location>> map = new HashMap<Domino, List<Location>>();
        for (int r = 0; r < GRID_ROWS - 1; r++) {
            for (int c = 0; c < GRID_ROWS; c++) {
                Domino hd = game.findDominoByLH(game.getGuessList(), game.getGrid()[r][c], game.getGrid()[r][c + 1]);
                Domino vd = game.findDominoByLH(game.getGuessList(), game.getGrid()[r][c], game.getGrid()[r + 1][c]);
                List<Location> l = map.get(hd);
                if (l == null) {
                    l = new LinkedList<Location>();
                    map.put(hd, l);
                }
                l.add(new Location(r, c));
                l = map.get(vd);
                if (l == null) {
                    l = new LinkedList<Location>();
                    map.put(vd, l);
                }
                l.add(new Location(r, c));
            }
        }
        for (Domino key : map.keySet()) {
            System.out.printf("[%d%d]", key.high, key.low);
            List<Location> locs = map.get(key);
            for (Location loc : locs) {
                System.out.print(loc);
            }
            System.out.println();
        }
    }

}
