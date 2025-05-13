package base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

public class GameLoopTest {

    /*
     * Test File Overview:
     * -------------------
     * 1.  Tests for private methods (accessed via reflection):
     *    - printPlayMenu (1)
     *    - getValidPlayChoice(1)
     *    - placeDomino() (1)
     *
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

    @Test
    void testGetValidPlayChoice() throws Exception {
        // Test to check for valid input and invalid input.
        // Simulate user input: "invalid", "99", then "3"
        IOSpecialist testIO = new IOSpecialist() {
            private final String[] inputs = {"invalid", "99", "3"};
            private int index = 0;

            @Override
            public String getString() {
                return inputs[index++];
            }
        };
        // Create GameLoop first
        GameLoop loop = new GameLoop(new GameEngine(), new PictureFrame(new Main()), "Tester");
        // Inject the test IO into the loop
        Field ioField = GameLoop.class.getDeclaredField("io");
        ioField.setAccessible(true);
        ioField.set(loop, testIO);
        // Call getValidPlayChoice using reflection
        Method method = GameLoop.class.getDeclaredMethod("getValidPlayChoice");
        method.setAccessible(true);
        int result = (int) method.invoke(loop);
        // Verify that valid input is eventually returned
        assertEquals(3, result);
    }



}
