package base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

public class GameLoopTest {

    /*
     * Test File Overview:
     * -------------------
     * 1.  Tests for private methods (accessed via reflection):
     *    - printPlayMenu
     *    - placeDomino() (4)
     *    - unplaceDomino() (3)
     *    - handleAssistance(String playerName) (3)
     *    - printScore() (1)
     *    - handleHonestyChoice() (3)
     *    - findSpecificDomino() (2)
     *    - findDominoAtLocation() (2)
     *    - findAllCertainties() (2)
     *    - findAllPossibilities() (2)
     *    - getValidPlayChoice() (2)
     *    - getInput(String prompt, int min, int max) (2)
     *    - gecko(int num) (2)
     */
    private GameLoop loop;

    @BeforeEach
    void setUp() {
        GameEngine engine = new GameEngine();
        PictureFrame frame = new PictureFrame(new Main());
        loop = new GameLoop(engine, frame, "Tester");
    }

    @Test
    void testPrintPlayMenuOutputsCorrectText() throws Exception {
        // Test to compare printPlayMenuOutput.
        ByteArrayOutputStream output = new ByteArrayOutputStream(); // Used to capture the console output.
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        // Call private method using reflection
        Method method = GameLoop.class.getDeclaredMethod("printPlayMenu");
        method.setAccessible(true);
        method.invoke(loop);

        // Restore System.out
        System.setOut(originalOut);

        // Check if expected menu text is present
        String outputText = output.toString();
        assertTrue(outputText.contains("======== Play Menu ========"));
        assertTrue(outputText.contains("1) Print the grid"));
        assertTrue(outputText.contains("0) Given up"));
        assertTrue(outputText.contains("What do you want to do Tester?"));
    }








}
