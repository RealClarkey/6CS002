package base;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    @Test
        // Test if all dominoes are being generated.
    void testDominoListSizeIs28() {
        List<Domino> dominoes = DominoFactory.generateDominoList(true);
        assertEquals(28, dominoes.size());

        dominoes = DominoFactory.generateDominoList(false);
        assertEquals(28, dominoes.size());
    }
}
