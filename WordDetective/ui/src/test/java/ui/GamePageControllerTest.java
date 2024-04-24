package ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
// import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class GamePageControllerTest extends ApplicationTest {

  /**
   * The root of the application is used as reference to the DOM.
   */
  private Parent root;

  /**
   * FXML components used for testing.
   */
  @FXML
  private Label score, highScore;

  /**
   * Mock of the api.
   */
  @Mock
  private ApiConfig apiMock = mock(ApiConfig.class);

  /**
   * Controller that injects the mocks.
   */
  @InjectMocks
  private GamePageController controller;

  /**
   * Textfield where the user writes.
   */
  private TextField playerInputField;

  /**
   * get the RootNode.
   *
   * @return Parent node
   */
  public Parent getRootNode() {
    return root;
  }

  /**
   * Start application.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    try {
      doNothing().when(apiMock).newGame("guest", null);
      doNothing().when(apiMock).savePlayerHighscore(Mockito.anyString());
      when(apiMock.getWord()).thenReturn("TESTWORD");
      when(apiMock.getHighScore()).thenReturn(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("GamePage.fxml"));
    root = fxmlLoader.load();
    controller = fxmlLoader.getController();
    controller.setApi(apiMock);
    playerInputField = (TextField) root.lookup("#playerInputField");
    score = (Label) root.lookup("#points");
    highScore = (Label) root.lookup("#highScore");
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Setup before each test.
   */
  @BeforeEach
  public void setUp() {
    closeHowToPlay();
  }

  /**
   * Test constructors.
   */
  @Test
  public void testConstructor() {
    assertDoesNotThrow(() -> new GamePageController());
    assertDoesNotThrow(() -> new GamePageController("guest"));
    assertDoesNotThrow(() -> new GamePageController("guest", "TESTWORD"));
    assertThrows(NullPointerException.class, () -> new GamePageController(null));
    assertThrows(NullPointerException.class, () -> new GamePageController(null, null));
  }

  /**
   * Tests if closing and opening howToPlay works.
   */
  @Test
  public void testHowToPlay() {
    Pane htp = (Pane) root.lookup("#howToPlay");
    assertFalse(htp.isVisible());
    clickOn("#openHTPBtn");
    assertTrue(htp.isVisible());
  }

  /**
   * Test if restart game works.
   */
  @Test
  public void restartGame() {
    testGuess("TESTWORD", true);
    Pane gameOverPane = (Pane) root.lookup("#gameOverPage");
    gameOverPane.setVisible(true);
    try {
      when(apiMock.getHighScore()).thenReturn(1);
      clickOn("#restartBtn");
      assertEquals(1, Integer.parseInt(highScore.getText()));
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests if go back to category page.
   */
  @Test
  public void goBack() {
    clickOn("#backArrowBtn");
    Node elementPresentInCategory = lookup("#categoryPage").query();
    assertNotNull(elementPresentInCategory);

  }

  /**
   * Tests if the input from the user is displayed correctly.
   */
  @Test
  public void testWriteInput() {
    assertTrue("TESTWORD".contains(controller.getSubstring()));
    write("TESTWORD", playerInputField);
    assertTrue(getInput().equals("TESTWORD"));
  }

  /**
   * Tests guesses gets right feedback.
   *
   * @param guess     - Guess
   * @param isCorrect - Is the guess correct
   */
  @ParameterizedTest
  @MethodSource("testWriteWords")
  public void testGuess(final String guess, final boolean isCorrect) {
    try {
      write(guess, playerInputField);
      when(apiMock.getWord()).thenReturn("TESTWORD");
      when(apiMock.checkValidWord(Mockito.anyString(), Mockito.anyString())).thenReturn(isCorrect);
      press(KeyCode.ENTER);
      if (isCorrect) {
        assertTrue(playerInputField.getText().isBlank());
      } else {
        assertTrue(playerInputField.getText().equals(guess));
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Stream of arguments for testing used in {@code testGuess}.
   *
   * @return - Stream of Arguments
   */
  private static Stream<Arguments> testWriteWords() {
    return Stream.of(
        Arguments.of("TESTWORD", true),
        Arguments.of("TESTWORD", true),
        Arguments.of("WRONGWORD", false));
  }

  /**
   * Helper method to get current input in textfield.
   *
   * @return String in input field
   */
  private String getInput() {
    return ((TextField) root.lookup("#playerInputField")).getText();

  }

  /**
   * Helper method to close the HowToPlay popup window.
   */
  private void closeHowToPlay() {
    clickOn(LabeledMatchers.hasText("Close"));
  }

  /**
   * Helper method to write input instant.
   *
   * @param string    - String to write
   * @param textfield - Textfield to write to
   */
  private void write(final String string, final TextField textfield) {
    textfield.setText(string);
  }

}
