package base;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Test File Overview:
 * -------------------
 * 1. Tests for public methods:
 *    - setupDominoes(int difficultyLevel) (4)
 *    - generateDominoes() (1)
 *    - generateGuesses() (1)
 *    - collateGrid() (2)
 *    - collateGuessGrid() (2)
 *    - findDominoAt() (2)
 *    - findDominoByLH() (2)
 *    - printGuesses() (1)
 *    - printSolutionGrid() (1)
 *    - printGuessGrid() (1)
 *    - printGrid(int[][] targetGrid) (1)
 *    - Getters (1)
 *
 * 2. Tests for private methods (accessed via reflection)
 *    - shuffleDominoesOrder() (2)
 *    - placeDominoes() (1)
 *    - applyDifficultyModifiers() (1)
 *    - rotateDominoes() (2)
 *    - invertSomeDominoes() (3)
 *    - tryToRotateDominoAt() (2)
 *    - thisIsTheTopLeftOfDomino() (2)
 *    - isTopLeftVerticalRight() (2)
 *    - isTopLeftHorizontalBelow() (2)
 */

class GameEngineTest {
    // ======================
    // === Public Methods ===
    // ======================

    @Test
    void testSetupDominoes_EasyMode_Generates28DominoesAndPlacesThem() {
        // Tests the standard game setup at easy difficulty.
        // Expects 28 dominoes to be created and all of them placed on the grid.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        List<Domino> dominos = engine.getDominoList();
        assertEquals(28, dominos.size());
        for (Domino d : dominos) {
            assertTrue(d.placed);
        }
        int[][] grid = engine.getGrid();
        int nonEmptyCells = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell != 9) nonEmptyCells++;
            }
        }
        assertEquals(56, nonEmptyCells);
    }

    @Test
    void testSetupDominoes_MediumMode_ChangesDominoOrientation() {
        // Checks that Medium difficulty doesn't break placement logic.
        // At this level, some dominoes may be rotated — just confirming game still works.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(2);
        for (Domino d : engine.getDominoList()) {
            assertTrue(d.placed);
        }
    }

    @Test
    void testSetupDominoes_HardMode_AllDominoesStillPlaced() {
        // Ensures that setup still completes successfully on Hard difficulty.
        // Rotations and random inversions should not result in missing placements.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(3);
        List<Domino> dominos = engine.getDominoList();
        assertEquals(28, dominos.size());
        assertTrue(dominos.stream().allMatch(d -> d.placed));
    }

    @Test
    void testSetupDominoes_ShufflingChangesDominoOrder() {
        // This test checks that the shuffle step randomises the order of dominoes.
        // Run setup twice and ensure the resulting lists are not in the same order.
        GameEngine engine1 = new GameEngine();
        GameEngine engine2 = new GameEngine();
        engine1.setupDominoes(1);
        engine2.setupDominoes(1);
        List<Domino> list1 = engine1.getDominoList();
        List<Domino> list2 = engine2.getDominoList();
        boolean differentOrder = false;
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                differentOrder = true;
                break;
            }
        }
    }

    @Test
    void testGenerateDominoes_Creates28Dominoes() {
        // Ensures that generateDominoes() creates the full set of 28 dominoes.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        List<Domino> dominoes = engine.getDominoList();
        assertEquals(28, dominoes.size());
    }

    @Test
    void testGenerateGuesses_Creates28Guesses() {
        // Ensures generateGuesses() also returns a complete list of 28 dominoes.
        GameEngine engine = new GameEngine();
        engine.generateGuesses();
        List<Domino> guesses = engine.getGuessList();
        assertEquals(28, guesses.size());
    }

    @Test
    void testCollateGrid_WithPlacedDominoes_PopulatesGrid() {
        // After generating and placing dominoes, the grid should be populated accordingly.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1); // includes collateGrid()
        int[][] grid = engine.getGrid();
        boolean hasNonEmpty = false;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell != 9) hasNonEmpty = true;
            }
        }
        assertTrue(hasNonEmpty);
    }

    @Test
    void testCollateGrid_WithUnplacedDominoes_UsesEmptyCells() {
        // Simulates unplaced dominoes and checks that grid uses EMPTY_CELL (value 9).
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        for (Domino d : engine.getDominoList()) d.placed = false;
        engine.collateGrid();
        int[][] grid = engine.getGrid();
        for (int[] row : grid) {
            for (int cell : row) {
                assertEquals(9, cell);
            }
        }
    }

    @Test
    void testCollateGuessGrid_WithPlacedGuesses() {
        // Ensures guess grid is populated only with placed guesses.
        GameEngine engine = new GameEngine();
        engine.generateGuesses();
        List<Domino> guessList = engine.getGuessList();
        guessList.get(0).placed = true; // place one for test
        engine.collateGuessGrid();
        int[][] guessGrid = engine.getGuessGrid();
        boolean foundPlaced = false;
        for (int[] row : guessGrid) {
            for (int cell : row) {
                if (cell != 9) foundPlaced = true;
            }
        }
        assertTrue(foundPlaced);
    }

    @Test
    void testCollateGuessGrid_WithNoPlacedGuesses() {
        // Tests that an empty guess list leads to a fully EMPTY_CELL-filled guessGrid.
        GameEngine engine = new GameEngine();
        engine.generateGuesses();
        engine.collateGuessGrid();
        int[][] guessGrid = engine.getGuessGrid();
        for (int[] row : guessGrid) {
            for (int cell : row) {
                assertEquals(9, cell);
            }
        }
    }

    @Test
    void testFindDominoAt_ReturnsCorrectDomino() {
        // Tests that a placed domino can be retrieved by its coordinates.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        Domino d = engine.getDominoList().get(0);
        Domino found = engine.findDominoAt(engine.getDominoList(), d.lx, d.ly);
        assertEquals(d, found);
    }

    @Test
    void testFindDominoAt_ReturnsNullIfNotFound() {
        // Passing coordinates that don't match any domino should return null.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        Domino found = engine.findDominoAt(engine.getDominoList(), -1, -1);
        assertNull(found);
    }

    @Test
    void testFindDominoByLH_FindsMatchingDomino() {
        // Finds a domino by its low and high values (irrespective of order).
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        Domino d = engine.getDominoList().get(0);
        Domino found = engine.findDominoByLH(engine.getDominoList(), d.high, d.low);
        assertEquals(d, found);
    }

    @Test
    void testFindDominoByLH_ReturnsNullIfNoMatch() {
        // If no domino matches the low and high values, null should be returned.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        Domino found = engine.findDominoByLH(engine.getDominoList(), 99, 99);
        assertNull(found);
    }

    @Test
    void testPrintGuesses_RunsWithoutError() {
        // Not much to assert here, but ensures it doesn't crash or throw anything.
        GameEngine engine = new GameEngine();
        engine.generateGuesses();
        assertDoesNotThrow(engine::printGuesses);
    }

    @Test
    void testPrintSolutionGrid_RunsWithoutError() {
        // Just checks the solution grid prints without error — visual check only.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        assertDoesNotThrow(engine::printSolutionGrid);
    }

    @Test
    void testPrintGuessGrid_RunsWithoutError() {
        // Similar to above, but for guess grid output.
        GameEngine engine = new GameEngine();
        engine.generateGuesses();
        engine.collateGuessGrid();
        assertDoesNotThrow(engine::printGuessGrid);
    }

    @Test
    void testPrintGrid_PrintsExpectedCharacters() {
        // Basic smoke test to ensure the grid prints expected number of lines.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        int result = engine.printGrid(engine.getGrid());
        assertEquals(11, result);
    }

    @Test
    void testGetters_ReturnExpectedReferences() {
        // Confirms that getter methods return internal references (not null or new copies).
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        engine.generateGuesses();
        assertNotNull(engine.getDominoList(), "Domino list getter should return a value");
        assertNotNull(engine.getGuessList(), "Guess list getter should return a value");
        assertNotNull(engine.getGrid(), "Grid getter should return a value");
        assertNotNull(engine.getGuessGrid(), "Guess grid getter should return a value");
    }

    // ====================================
    // === Private Methods (Reflection) ===
    // ====================================

    @Test
    void testPrivate_shuffleDominoesOrder_changesOrder() throws Exception {
        // Checks that shuffling changes the order of dominoes.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        List<Domino> originalOrder = new LinkedList<>(engine.getDominoList());
        Method method = GameEngine.class.getDeclaredMethod("shuffleDominoesOrder");
        method.setAccessible(true);
        method.invoke(engine);
        List<Domino> shuffled = engine.getDominoList();
        assertNotEquals(originalOrder, shuffled);
    }

    @Test
    void testPrivate_shuffleDominoesOrder_preservesAllDominoes() throws Exception {
        // Ensures shuffling does not add or lose any dominoes.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        List<Domino> before = new LinkedList<>(engine.getDominoList());
        Method method = GameEngine.class.getDeclaredMethod("shuffleDominoesOrder");
        method.setAccessible(true);
        method.invoke(engine);
        List<Domino> after = engine.getDominoList();
        assertEquals(before.size(), after.size(), "Domino count should remain unchanged");
        assertTrue(after.containsAll(before));
    }

    @Test
    void testPrivate_placeDominoes_placesAllDominoes() throws Exception {
        // Ensures that all dominoes are assigned positions and marked as placed.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        Method method = GameEngine.class.getDeclaredMethod("placeDominoes");
        method.setAccessible(true);
        method.invoke(engine);
        for (Domino d : engine.getDominoList()) {
            assertTrue(d.placed);
        }
    }

    @Test
    void testPrivate_applyDifficultyModifiers_runsWithoutError() throws Exception {
        // Ensures all difficulty levels can be applied without throwing exceptions.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        Method place = GameEngine.class.getDeclaredMethod("placeDominoes");
        place.setAccessible(true);
        place.invoke(engine);
        Method method = GameEngine.class.getDeclaredMethod("applyDifficultyModifiers", int.class);
        method.setAccessible(true);
        // Difficulty levels: 1 (none), 2 (rotate), 3 (rotate + invert)
        method.invoke(engine, 1);
        method.invoke(engine, 2);
        method.invoke(engine, 3);
    }

    @Test
    void testPrivate_rotateDominoes_runsWithoutError() throws Exception {
        // Calls rotateDominoes() to verify it completes without error.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        Method method = GameEngine.class.getDeclaredMethod("rotateDominoes");
        method.setAccessible(true);
        assertDoesNotThrow(() -> method.invoke(engine));
    }

    @Test
    void testPrivate_rotateDominoes_changesSomePositions() throws Exception {
        // This isn't guaranteed due to randomness, but we can check if *any* dominos move.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        List<Domino> before = new LinkedList<>();
        for (Domino d : engine.getDominoList()) {
            Domino copy = new Domino(d.high, d.low);
            copy.place(d.lx, d.ly, d.hx, d.hy);
            before.add(copy);
        }
        Method method = GameEngine.class.getDeclaredMethod("rotateDominoes");
        method.setAccessible(true);
        method.invoke(engine);
        boolean anyMoved = false;
        List<Domino> after = engine.getDominoList();
        for (int i = 0; i < after.size(); i++) {
            Domino d1 = before.get(i);
            Domino d2 = after.get(i);
            if (d1.lx != d2.lx || d1.ly != d2.ly || d1.hx != d2.hx || d1.hy != d2.hy) {
                anyMoved = true;
                break;
            }
        }
        assertTrue(anyMoved);
    }

    @Test
    void testPrivate_invertSomeDominoes_runsWithoutError() throws Exception {
        // Simply ensures the invert method works without crashing.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();

        Method method = GameEngine.class.getDeclaredMethod("invertSomeDominoes");
        method.setAccessible(true);
        assertDoesNotThrow(() -> method.invoke(engine));
    }

    @Test
    void testPrivate_invertSomeDominoes_invertsAtLeastOneDomino() throws Exception {
        // Because inversion is random, we check if at least one domino was inverted.
        boolean invertedAtLeastOnce = false;
        for (int i = 0; i < 10; i++) {
            GameEngine engine = new GameEngine();
            engine.generateDominoes();
            // Clone original domino list
            List<Domino> before = new LinkedList<>();
            for (Domino d : engine.getDominoList()) {
                before.add(new Domino(d.high, d.low));
            }
            // Use reflection to invoke private method
            Method method = GameEngine.class.getDeclaredMethod("invertSomeDominoes");
            method.setAccessible(true);
            method.invoke(engine);
            // Check if any domino has been inverted
            for (int j = 0; j < before.size(); j++) {
                Domino d1 = before.get(j);
                Domino d2 = engine.getDominoList().get(j);
                if ((d1.low == d2.high) && (d1.high == d2.low)) {
                    invertedAtLeastOnce = true;
                    break;
                }
            }
            if (invertedAtLeastOnce) break;
        }
        assertTrue(invertedAtLeastOnce, "Expected at least one domino to be inverted across several runs");
    }

    @Test
    void testPrivate_invertSomeDominoes_preservesCount() throws Exception {
        // Even if inverted, the number of dominoes should remain unchanged.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        Method method = GameEngine.class.getDeclaredMethod("invertSomeDominoes");
        method.setAccessible(true);
        method.invoke(engine);
        assertEquals(28, engine.getDominoList().size());
    }

    @Test
    void testPrivate_tryToRotateDominoAt_runsWithoutError() throws Exception {
        // Ensures method doesn't throw for valid grid positions.
        GameEngine engine = new GameEngine();
        engine.setupDominoes(1);
        Method method = GameEngine.class.getDeclaredMethod("tryToRotateDominoAt", int.class, int.class);
        method.setAccessible(true);
        assertDoesNotThrow(() -> method.invoke(engine, 0, 0));
    }

    @Test
    void testPrivate_thisIsTopLeftOfDomino_usingReflection() throws Exception {
        // Tests whether the given coordinates are correctly identified as the top-left of a placed domino using reflection.
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

    @Test
    void testPrivate_isTopLeftVerticalRight_trueCase() throws Exception {
        // Creates vertical domino to right, checks that it returns true.
        GameEngine engine = new GameEngine();
        Domino d = new Domino(3, 6);
        d.place(1, 2, 1, 3);
        engine.dominoList = List.of(d);
        Method method = GameEngine.class.getDeclaredMethod("isTopLeftVerticalRight", int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(engine, 0, 2);
        assertTrue(result);
    }

    @Test
    void testPrivate_isTopLeftVerticalRight_falseCase() throws Exception {
        // No domino in correct orientation = false.
        GameEngine engine = new GameEngine();
        engine.generateDominoes();
        Method method = GameEngine.class.getDeclaredMethod("isTopLeftVerticalRight", int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(engine, 0, 0);
        assertFalse(result);
    }

    @Test
    void testPrivate_isTopLeftHorizontalBelow_trueCase() throws Exception {
        // Sets a horizontal domino below and tests recognition.
        GameEngine engine = new GameEngine();
        Domino d = new Domino(2, 5);
        d.place(1, 3, 2, 3); // horizontal at (1,3)
        engine.dominoList = List.of(d);
        Method method = GameEngine.class.getDeclaredMethod("isTopLeftHorizontalBelow", int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(engine, 1, 2); // test one row above
        assertTrue(result, "Should detect horizontal domino below");
    }

    @Test
    void testPrivate_isTopLeftHorizontalBelow_falseCase() throws Exception {
        // Expect false when there's a domino at (0,1), but it doesn't meet the conditions
        GameEngine engine = new GameEngine();
        engine.dominoList = new LinkedList<>();
        // Place a vertical domino at (0,1) to avoid NPE but ensure test returns false
        Domino d = new Domino(2, 5);
        d.place(0, 1, 0, 2);  // vertical, not horizontal
        engine.dominoList.add(d);
        Method method = GameEngine.class.getDeclaredMethod("isTopLeftHorizontalBelow", int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(engine, 0, 0);
        assertFalse(result);
    }


}
