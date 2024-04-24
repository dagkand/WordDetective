package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import persistence.JsonIO;
import types.User;

public class GameTest {

    /**
     * Object of game.
     */
    private Game game;

    /**
     * User in the game.
     */
    private static User testUser;
    /**
     * Digit for the number of tests.
     */
    private static final int NUMBER_OF_TESTS = 10;
    /**
     * Wordlist used for testing.
     */
    private List<String> singleTestList = Arrays.asList("Test");
    /**
     * Wordlist used for testing with multiple strings.
     */
    private List<String> multipleTestList = Arrays.asList("Test", "Test1", "Test2", "Test3");

    /**
     * Sets up two instances of the Category class to be used in the tests.
     */
    @BeforeEach
    public void setUp() {
        testUser = new User("TestUser", "Password");
        testUser.addCustomCategories("Custom", Arrays.asList("1", "3", "4"));
        game = new Game(testUser.getUsername());
    }

    /**
     * Test constructors.
     */
    @Test
    @DisplayName("Test constructor")
    public void constructorTest() {
        Game testGame = new Game(testUser.getUsername());
        assertNotNull(testGame, "Game should not be null");
    }

    /**
     * Check that querying and setting the word list is correct.
     */
    @Test
    @DisplayName("Check correct get/set of word list")
    public void testWordList() {
        String fruitWord = "Apple";
        assertNull(game.getWordList());
        game.setWordList(singleTestList);
        assertEquals(game.getWordList(), singleTestList);
        game.setCategory("fruits");
        assertTrue(game.getWordList().contains(fruitWord.toUpperCase()),
                "The word " + fruitWord + " should be in the word list. Your list:" + game.getWordList());
        assertFalse(game.getWordList().contains(singleTestList.get(0)),
                " The word list should not contain the word " + singleTestList.get(0) + " anymore");
    }

    /**
     * Checks that a randomly generated substring from a randomly pulled word is
     * indeed recognized as a valid substring.
     */
    @Test
    @DisplayName("Check valid substring of word")
    public void testGetRandomSubstring() {
        game.setWordList(singleTestList);
        for (int i = 0; i < NUMBER_OF_TESTS; i++) {
            assertTrue("Test".contains(game.getWord()));
        }
    }

    /**
     * Test checking valid words.
     */
    @Test
    @DisplayName("Check that guesses are valid")
    public void testCheckValidWord() {
        game.setWordList(Collections.emptyList());
        assertFalse(game.checkValidWord("s", "Test"), "Failed for empty wordlist'");
        game.setWordList(multipleTestList);
        assertTrue(game.checkValidWord("s", "Test"), "Failed for 's' and 'Test'");
        assertTrue(game.checkValidWord("es", "Test"), "Failed for 'es' and 'Test'");
        assertTrue(game.checkValidWord("st2", "Test2"), "Failed for 'st2' and 'Test2'");
        assertFalse(game.checkValidWord("Tes", "Test4"), "Failed for 'Tes' and 'Test4'");
        assertFalse(game.checkValidWord("2", "Test"), "Failed for '2' and 'Test'");
        assertFalse(game.checkValidWord("es", "Test4"), "Failed for 'es' and 'Test4'");
        assertTrue(game.checkValidWord("", "Test2"), "Failed for '' and 'Test4'");
        assertFalse(game.checkValidWord("es", "Tes"), "Failed for word not in wordlist");
    }

    /**
     * Test getPlayerHighscore and savePlayerHighScore.
     */
    @Test
    @DisplayName("Check setting of high score")
    public void testHighscore() {
        assertEquals(0, game.getPlayerHighscore(), "High score should be 0 on start");
        game.savePlayerHighscore(300);
        assertEquals(300, game.getPlayerHighscore(), "High score should be 300, but was " + game.getPlayerHighscore());
        game.savePlayerHighscore(200);
        assertEquals(300, game.getPlayerHighscore(), "High score should be 300, but was " + game.getPlayerHighscore());
    }

    /**
     * Reset highscore after running tests.
     */
    @AfterAll
    public static void cleanUpAfterAllTests() {
        JsonIO jsonIO = new JsonIO("TestUser");
        try {
            jsonIO.updateCurrentUser(
                    (user) -> {
                        user.setHighscore(0);
                        return true;
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}