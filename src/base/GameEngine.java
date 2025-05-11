package base;
import java.util.LinkedList;
import java.util.List;

public class GameEngine {

    // Grid and Domino Attributes
    private static final int MAX_DOMINO_SPOTS = 6;
    private static final int TOTAL_DOMINOS = 28;
    private static final int GRID_ROWS = 7;
    private static final int GRID_COLUMNS = 8;
    private static final int EMPTY_CELL = 9;

    // Data Structure
    public List<Domino> dominoList;
    public List<Domino> guessList;
    public int[][] grid = new int[GRID_ROWS][GRID_COLUMNS];
    public int[][] guessGrid = new int[GRID_ROWS][GRID_COLUMNS];


    // Create a set of dominoes for the game.
    public void setupDominoes(int difficultyLevel){
        generateDominoes();
        shuffleDominoesOrder();
        placeDominoes();
        applyDifficultyModifiers(difficultyLevel);
        collateGrid();
    }

    private void applyDifficultyModifiers(int difficultyLevel) {
        if (difficultyLevel == 2){
            rotateDominoes();
        } else if (difficultyLevel == 3){
            rotateDominoes();
            rotateDominoes();
            invertSomeDominoes();
        }
    }



    // Create a list of Dominoes.
    public List<Domino> generateDominoList(boolean shouldPlace) {
        List<Domino> list = new LinkedList<>();
        int count = 0;
        int x = 0;
        int y = 0;

        for (int l = 0; l <= MAX_DOMINO_SPOTS; l++) {
            for (int h = l; h <= MAX_DOMINO_SPOTS; h++) {
                Domino d = new Domino(h, l);
                if (shouldPlace) {
                    d.place(x, y, x + 1, y);
                    x += 2;
                    if (x > 6) {
                        x = 0;
                        y++;
                    }
                }
                list.add(d);
                count++;
            }
        }

        if (count != TOTAL_DOMINOS) {
            System.out.println("Something went wrong generating dominoes");
            System.exit(0);
        }

        return list;
    }

    public void generateDominoes() {
        dominoList = generateDominoList(true);
    }

    public void generateGuesses() {
        guessList = generateDominoList(false);
    }

    private void shuffleDominoesOrder() {
        List<Domino> shuffled = new LinkedList<Domino>();
        while (dominoList.size() > 0) {
            int n = (int) (Math.random() * dominoList.size());
            shuffled.add(dominoList.get(n));
            dominoList.remove(n);
        }
        dominoList = shuffled;
    }

    private void placeDominoes() {
        int x = 0;
        int y = 0;
        int count = 0;
        for (Domino d : dominoList) {
            count++;
            d.place(x, y, x + 1, y);
            x += 2;
            if (x > 6) {
                x = 0;
                y++;
            }
        }
        if (count != TOTAL_DOMINOS) {
            System.out.println("something went wrong generating dominoes");
            System.exit(0);
        }
    }

    private void rotateDominoes() {
        for (int x = 0; x < GRID_ROWS; x++) {
            for (int y = 0; y < 6; y++) {
                tryToRotateDominoAt(x, y);
            }
        }
    }

    private void invertSomeDominoes() {
        for (Domino d : dominoList) {
            if (Math.random() > 0.5) {
                d.invert();
            }
        }
    }

    public void collateGrid() {
        for (Domino d : dominoList) {
            if (!d.placed) {
                grid[d.hy][d.hx] = EMPTY_CELL;
                grid[d.ly][d.lx] = EMPTY_CELL;
            } else {
                grid[d.hy][d.hx] = d.high;
                grid[d.ly][d.lx] = d.low;
            }
        }
    }

    public void collateGuessGrid() {
        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLUMNS; c++) {
                guessGrid[r][c] = EMPTY_CELL;
            }
        }
        for (Domino d : guessList) {
            if (d.placed) {
                guessGrid[d.hy][d.hx] = d.high;
                guessGrid[d.ly][d.lx] = d.low;
            }
        }
    }

    private void tryToRotateDominoAt(int x, int y) {
        Domino d = findDominoAt(dominoList, x, y);
        if (thisIsTopLeftOfDomino(x, y, d)) {
            if (d.ishl()) {
                boolean weFancyARotation = Math.random() < 0.5;
                if (weFancyARotation) {
                    if (isTopLeftHorizontalBelow(x, y)) {
                        Domino e = findDominoAt(dominoList, x, y + 1);
                        e.hx = x;
                        e.lx = x;
                        d.hx = x + 1;
                        d.lx = x + 1;
                        e.ly = y + 1;
                        e.hy = y;
                        d.ly = y + 1;
                        d.hy = y;
                    }
                }
            } else {
                boolean weFancyARotation = Math.random() < 0.5;
                if (weFancyARotation) {
                    if (isTopLeftVerticalRight(x, y)) {
                        Domino e = findDominoAt(dominoList, x + 1, y);
                        e.hx = x;
                        e.lx = x + 1;
                        d.hx = x;
                        d.lx = x + 1;
                        e.ly = y + 1;
                        e.hy = y + 1;
                        d.ly = y;
                        d.hy = y;
                    }
                }
            }
        }
    }




    public Domino findDominoAt(List<Domino> list, int x, int y) {
        for (Domino d : list) {
            if ((d.lx == x && d.ly == y) || (d.hx == x && d.hy == y)) {
                return d;
            }
        }
        return null;
    }

    public Domino findDominoByLH(List<Domino> list, int x, int y) {
        for (Domino d : list) {
            if ((d.low == x && d.high == y) || (d.high == x && d.low == y)) {
                return d;
            }
        }
        return null;
    }

    public void printDominoes() {
        for (Domino d : dominoList) {
            System.out.println(d);
        }
    }
    public void printGuesses() {
        for (Domino d : guessList) {
            System.out.println(d);
        }
    }

    private boolean thisIsTopLeftOfDomino(int x, int y, Domino d) {
        return (x == Math.min(d.lx, d.hx)) && (y == Math.min(d.ly, d.hy));
    }

    private boolean isTopLeftVerticalRight(int x, int y) {
        Domino e = findDominoAt(dominoList, x + 1, y);
        return thisIsTopLeftOfDomino(x + 1, y, e) && !e.ishl();
    }

    private boolean isTopLeftHorizontalBelow(int x, int y) {
        Domino e = findDominoAt(dominoList, x, y + 1);
        return thisIsTopLeftOfDomino(x, y + 1, e) && e.ishl();
    }

    int printSolutionGrid() {
        return printGrid(grid);
    }

    int printGuessGrid() {
        return printGrid(guessGrid);
    }

    int printGrid(int[][] targetGrid) {
        for (int are = 0; are < GRID_ROWS; are++) {
            for (int see = 0; see < GRID_COLUMNS; see++) {
                if (targetGrid[are][see] != EMPTY_CELL) {
                    System.out.printf("%d", targetGrid[are][see]);
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        return 11;
    }

    //Getters and Setters
    public List<Domino> getDominoList() {
        return dominoList;
    }

    public List<Domino> getGuessList() {
        return guessList;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] getGuessGrid() {
        return guessGrid;
    }



}
// dominoList - game.getDominoList
// guessList - game.getGuessList
// grid - game.getGrid
// guessGrid - game.getGuessGrid