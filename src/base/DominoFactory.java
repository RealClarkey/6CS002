package base;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DominoFactory {
    private static final int MAX_DOMINO_SPOTS = 6;
    private static final int TOTAL_DOMINOS = 28;

    public static List<Domino> generateDominoList(boolean shouldPlace) {
        // Generate a list of all unique domino pairs (h, l) where h >= l
        List<Domino> dominos = IntStream.rangeClosed(0, MAX_DOMINO_SPOTS)
                .boxed()
                .flatMap(low -> IntStream.rangeClosed(low, MAX_DOMINO_SPOTS)
                        .mapToObj(high -> new Domino(high, low)))
                .collect(Collectors.toList());

        if (dominos.size() != TOTAL_DOMINOS) {
            throw new IllegalStateException("Incorrect number of dominos generated");
        }

        // If placement is requested, return a new list with positioned dominos
        if (shouldPlace) {
            return IntStream.range(0, TOTAL_DOMINOS)
                    .mapToObj(i -> {
                        int x = (i * 2) % 14; // 14 = max X size for placement (7 dominos per row * 2)
                        int y = (i * 2) / 14;
                        Domino d = dominos.get(i);
                        d.place(x, y, x + 1, y);
                        return d;
                    })
                    .collect(Collectors.toList());
        }

        return dominos;
    }
}
