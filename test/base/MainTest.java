package base;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Test File Overview:
 * -------------------
 * 1. Tests for public methods:
 *    - getGameGrid() (1)
 *    - gecko() (2)
 *
 *    Other methods not worth testing due to needing Swing GUI.
 *
 */

public class MainTest {

    @Test
    void testGetGameGridReturnsGrid() {
        Main main = new Main();
        int[][] grid = main.getGameGrid();
        assertNotNull(grid, "Grid should not be null");
    }

    @Test
    void testGeckoWithZeroReturnsInvalidInput() {
        int result = Main.gecko(0);
        assertEquals(-7, result);
    }

    @Test
    void testGeckoWithNegativeNumberReturnsInvalidInput() {
        int result = Main.gecko(-3);
        assertEquals(-7, result);
    }

}
