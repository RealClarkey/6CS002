package base;

import java.util.LinkedList;
import java.util.List;

public class DominoFactory {
    // Domino Attributes
    private static final int MAX_DOMINO_SPOTS = 6;
    private static final int TOTAL_DOMINOS = 28;

    public static List<Domino> generateDominoList(boolean shouldPlace) {
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

}
