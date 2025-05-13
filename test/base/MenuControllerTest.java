package base;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Test File Overview:
 * -------------------
 * 1. Tests for public methods:
 *    - getInspiration() (1)
 *    - greetPlayer() (2)
 *    - selectDifficulty (2)
 *    - showHighScores() (2)
 *
 * 2. Tests for private methods (accessed via reflection)
 *    - printMenu() (2)
 *
 */

public class MenuControllerTest {

    // ======================
    // === Public Methods ===
    // ======================

    @Test
    void testGetInspirationPrintsQuote() {
        MenuController menu = new MenuController();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        menu.getInspiration();
        System.setOut(original);
        assertTrue(out.toString().contains("said \""));
    }

    @Test
    void testGreetPlayerReturnsName() {
        MenuController menu = new MenuController();
        menu.io = new IOSpecialist() {
            @Override
            public String getString() {
                return "TestUser";
            }
        };
        String name = menu.greetPlayer();
        assertEquals("TestUser", name);
    }


    @Test
    void testGreetPlayerOutputContainsWelcome() {
        MenuController menu = new MenuController();
        menu.io = new IOSpecialist() {
            @Override
            public String getString() {
                return "TestUser";
            }
        };
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        menu.greetPlayer();
        System.setOut(original);
        assertTrue(out.toString().contains("Welcome To Abominodo"));
    }

    @Test
    void testSelectDifficultyReturnsValidChoice() {
        MenuController menu = new MenuController();
        menu.io = new IOSpecialist() {
            private final String[] inputs = {"0", "abc", "2"};
            private int index = 0;
            @Override
            public String getString() {
                return inputs[index++];
            }
        };
        int result = menu.selectDifficulty("TestPlayer");
        assertEquals(2, result);
    }

    @Test
    void testShowHighScoresDisplaysFormattedScores() throws Exception {
        // Creating a fake score.txt file
        File scoreFile = new File("score.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(scoreFile))) {
            writer.println("Alice,1000," + System.currentTimeMillis());
            writer.println("Bob,950," + System.currentTimeMillis());
        }
        MenuController menu = new MenuController();
        // Capture console output ready for comparison
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        // Call method
        menu.showHighScores("Tester");
        System.setOut(originalOut); // Restore System.out
        String printed = out.toString();
        assertTrue(printed.contains("High Scores"));
        assertTrue(printed.contains("Alice"));
        assertTrue(printed.contains("Bob"));
    }

    // ======================
    // === Private Methods ===
    // ======================

    @Test
    void testPrintMenuIncludesTitleAndOptions() throws Exception {
        MenuController menu = new MenuController();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        Method method = MenuController.class.getDeclaredMethod("printMenu", String.class, String[].class, boolean.class, String.class);
        method.setAccessible(true);
        String[] options = {"1) Easy", "2) Hard"};
        method.invoke(menu, "Select difficulty", options, true, "PlayerOne");
        System.setOut(original);
        String output = out.toString();
        assertTrue(output.contains("Select difficulty"));
        assertTrue(output.contains("1) Easy"));
        assertTrue(output.contains("2) Hard"));
        assertTrue(output.contains("What do you want to do PlayerOne?"));
    }



}
