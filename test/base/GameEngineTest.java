package base;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Test File Overview:
 * -------------------
 * 1. Tests for public methods:
 *    - setupDominoes(int difficultyLevel)
 *    - generateDominoes()
 *    - generateGuesses()
 *    - collateGrid()
 *    - collateGuessGrid()
 *    - findDominoAt(List<Domino> list, int x, int y)
 *    - findDominoByLH(List<Domino> list, int x, int y)
 *    - printGuesses()
 *    - printSolutionGrid()
 *    - printGuessGrid()
 *    - printGrid(int[][] targetGrid)
 *    - getDominoList()
 *    - getGuessList()
 *    - getGrid()
 *    - getGuessGrid()
 *
 * 2. Tests for private methods (accessed via reflection)
 */

class GameEngineTest {
    // === Public Methods ===

    @Test
    // Test if all dominoes are being generated.
    void testDominoListSizeIs28() {
        List<Domino> dominoes = DominoFactory.generateDominoList(true);
        assertEquals(28, dominoes.size());

        dominoes = DominoFactory.generateDominoList(false);
        assertEquals(28, dominoes.size());
    }

    // === Private Methods (Reflection) ===
    @Test
    // Tests whether the given coordinates are correctly identified as the top-left of a placed domino using reflection.
    void testPrivate_thisIsTopLeftOfDomino_usingReflection() throws Exception {
        GameEngine engine = new GameEngine();

        // Get the private method and its parameters
        Method method = GameEngine.class.getDeclaredMethod(
                "thisIsTopLeftOfDomino", int.class, int.class, Domino.class);

        // Allowing access to the private method
        method.setAccessible(true);


        Domino d = new Domino(6, 1);
        d.place(2, 3, 3, 3); // horizontally placed at (2,3)

        boolean result = (boolean) method.invoke(engine, 2, 3, d);

        assertTrue(result);
    }

}
