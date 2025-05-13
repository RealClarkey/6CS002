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
     *    - printScore() (1)
     *    - printMenu() (1)
     *    - gecko() (2)
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

    @Test
    void testPrintScoreDisplaysCorrectMessage() throws Exception {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));
        // Arrange: create GameLoop and set score manually
        GameLoop loop = new GameLoop(new GameEngine(), new PictureFrame(new Main()), "Tester");
        // Set score field using reflection
        Field scoreField = GameLoop.class.getDeclaredField("score");
        scoreField.setAccessible(true);
        scoreField.setInt(loop, 1234);
        // Act: call printScore() via reflection
        Method method = GameLoop.class.getDeclaredMethod("printScore");
        method.setAccessible(true);
        method.invoke(loop);
        // Restore System.out
        System.setOut(originalOut);
        // Assert: check that the expected message was printed
        String printed = output.toString().trim();
        assertEquals("Tester your score is 1234", printed);
    }

    @Test
    void testPrintMenu() throws Exception {
        // Redirect System.out to capture output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));
        GameLoop loop = new GameLoop(new GameEngine(), new PictureFrame(new Main()), "Tester");
        String title = "Cheat Menu";
        String[] options = {
                "1) Reveal a tile",
                "2) Auto-complete board",
                "0) Exit"
        };
        // Call private printMenu() using Reflection.
        Method method = GameLoop.class.getDeclaredMethod("printMenu", String.class, String[].class, boolean.class);
        method.setAccessible(true);
        method.invoke(loop, title, options, true);
        // Restore System.out
        System.setOut(originalOut);
        // Get the printed output
        String printed = output.toString();
        // System.out.println("Captured output:\n" + printed); Used for debugging the test unit.
        // Assert output contains title, underline, options, and player name
        assertTrue(printed.contains("=========="));
        assertTrue(printed.contains("Cheat Menu"));
        assertTrue(printed.contains("==========")); // underline
        assertTrue(printed.contains("1) Reveal a tile"));
        assertTrue(printed.contains("2) Auto-complete board"));
        assertTrue(printed.contains("0) Exit"));
        assertTrue(printed.contains("What do you want to do Tester?"));
    }

    @Test
    void testGeckoReturnsInvalidInputFromPositiveNumber() throws Exception {
        // Calls gecko() with input 5
        Method method = GameLoop.class.getDeclaredMethod("gecko", int.class);
        method.setAccessible(true);
        int result = (int) method.invoke(null, 5);

        // Assert that the result is INVALID_INPUT (expected to be -7)
        assertEquals(-7, result);
    }

    @Test
    void testGeckoReturnsInvalidInputFromNegativeNumber() throws Exception {
        // Calls gecko() with a negative input -3
        Method method = GameLoop.class.getDeclaredMethod("gecko", int.class);
        method.setAccessible(true);
        int result = (int) method.invoke(null, -3);

        assertEquals(-7, result);
    }








}

