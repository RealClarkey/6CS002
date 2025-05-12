package base;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DominoFactoryTest {

    @Test
    void testDominoListSize_shouldAlwaysBe28() {
        List<Domino> dominoes = DominoFactory.generateDominoList(true);
        assertEquals(28, dominoes.size());

        dominoes = DominoFactory.generateDominoList(false);
        assertEquals(28, dominoes.size());
    }

}
