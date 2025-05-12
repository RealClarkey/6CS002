package base;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DominoFactoryTest {

    @Test
    // Test if all dominoes are being generated.
    void testDominoListSizeIs28() {
        List<Domino> dominoes = DominoFactory.generateDominoList(true);
        assertEquals(28, dominoes.size());

        dominoes = DominoFactory.generateDominoList(false);
        assertEquals(28, dominoes.size());
    }

    @Test
    // Test all Dominoes are placed when shouldPlace = true
    void testDominoesArePlacedShouldPlaceTrue() {
        List<Domino> dominoes = DominoFactory.generateDominoList(true);
        assertTrue(dominoes.stream().allMatch(d -> d.placed));
    }

    @Test
    // Test no dominoes are place when shouldPlace = false
    void testDominoesNotPlacedShouldPlaceFalse() {
        List<Domino> dominoes = DominoFactory.generateDominoList(false);
        assertTrue(dominoes.stream().noneMatch(d -> d.placed));
    }

    @Test
    // Test that all Dominoes are Unique.
    void testAllDominoesAreUnique() {
        List<Domino> dominoes = DominoFactory.generateDominoList(false);
        Set<String> pairs = new HashSet<>();

        for (Domino d : dominoes) {
            String pair = d.high + "," + d.low;
            String reversePair = d.low + "," + d.high;
            assertFalse(pairs.contains(pair) || pairs.contains(reversePair));
            pairs.add(pair);
        }
    }

}
