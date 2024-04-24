package ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public final class GamePageController extends AbstractController implements Initializable {

    /**
     * The profileCircle is the circle that represents the player.
     */

    @FXML
    private Circle profileCircle, profileCircle1, profileCircle2, profileCircle3;

    /**
     * This is the anchor pane of game page.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * The lettersCircle is the pane that contains the letters.
     */

    @FXML
    private Pane lettersCircle;
    /**
     * The window is the pane that contains the game.
     */

    @FXML
    private Pane innerWindow;

    /**
     * The gameOverPage is the pane that comes after game end.
     */
    @FXML
    private Pane gameOverPage;
    /**
     * The playerInputField is the textfield where the player writes the word.
     */

    @FXML
    private TextField playerInputField;
    /**
     * Labels on the game page.
     */

    @FXML
    private Label letters, points, categoryDisplay, highScore;

    /**
     * Outputfield of what the player writes.
     */
    @FXML
    private TextFlow outputField;

    /**
     * The HowToPlay window.
     */
    @FXML
    private Pane howToPlay;

    /**
     * Buttons for opening and closing HowToPlay window.
     */
    @FXML
    private Button closeHTPBtn, openHTPBtn;

    /**
     * Button for going back to Category page.
     */
    @FXML
    private Button backArrowBtn;

    /**
     * Imageview of back arrow png.
     */
    @FXML
    private ImageView backArrowImg;

    /**
     * Image for background.
     */
    @FXML
    private ImageView imageGame;

    /**
     * Buttons on game over pane to return or restart.
     */
    @FXML
    private Button returnBtn, restartBtn;
    /**
     * Labels to show previous highscore and score for this game.
     */
    @FXML
    private Label gameOverHighScore, gameOverScore;

    /**
     * The current word used for showing at game over.
     */
    @FXML
    private Label currentWordLabel;
    /**
     * The substring is the letters that the player has to use.
     */
    private String substring = "";
    /**
     * The current user.
     */
    private String username;
    /**
     * The layoutX is the X position of the lettersCircle.
     */
    private final int layoutX = 470;
    /**
     * The layoutY is the Y position of the lettersCircle.
     */
    private final int layoutY = 70;
    /**
     * The letterVelocity is the velocity of the moving letter circle.
     */
    private double letterVelocity = 30;
    /**
     * Variable holding current animation.
     */
    private TranslateTransition currentAnimation;
    /**
     * The layoutCenter is the center of the game.
     */
    private final int layoutCenter = 30;

    /**
     * The radius is the radius of the player circle.
     */
    private final int radius = 30;

    /**
     * The centerY is the default Y position of the player circle.
     */
    private final int centerY = 425; // Default Y position

    /**
     * The playerCenterX is the X position of the player circle.
     */
    private final double centerX = 500.0; // Default X is center
    // /**
    // * The bots is the number of bots in the game.
    // */
    // private final int botsMultiplier = 5;

    /**
     * Number for moving node in X-direction on shake animation.
     */
    private final int shakeXMovment = 4;

    /**
     * Duration of shake animation in milliseconds.
     */
    private final int shakeDuration = 250;

    /**
     * Boolean that determines if HowToPlay window should be opened or closed.
     * Start value is true because of automatic pop-up on screen on game start.
     */
    private boolean showHowToPlay = true;

    /**
     * Boolean that determines if yoy have already seen how to play.
     */
    private boolean hasShownHowToPlay = false;

    /**
     * Variable holding the category of the given game.
     */
    private String currentCategory = "";

    /**
     * The current word used to obtain the substring.
     */
    private String currentWord = "";

    /**
     * The player circle display on screen.
     */
    private Circle playerCircle = new Circle();

    /**
     * Random object used to provide substrings.
     */
    private Random random;

    /**
     * Api object used for calling backend application.
     */
    private ApiConfig api;

    /**
     * Constructor initializing the object.
     *
     * @param usernameParameter username.
     * @param categoryParameter category of the given game.
     */
    public GamePageController(final String usernameParameter, final String categoryParameter) {
        if (usernameParameter == null || categoryParameter == null) {
            throw new NullPointerException("Username or category cannot be null");
        }
        this.username = usernameParameter;
        this.currentCategory = categoryParameter;
        random = new Random();
        api = new ApiConfig();
    }

    /**
     * Empty Constuctor for initialising controller.
     *
     * @param category The category chosen by the user.
     */
    public GamePageController(final String category) {
        this("guest", category);
        api = new ApiConfig();
    }

    /**
     * Empty Constuctor for initialising controller.
     */
    public GamePageController() {
        this("guest", "");
        api = new ApiConfig();
    }

    /**
     * Sets the api object.
     *
     * @param newApi - The api object to set.
     */
    public void setApi(final ApiConfig newApi) {
        this.api = newApi;
    }

    /**
     * Move the game (The letters) to a chosen location. Resets to original
     * posistion after animation.
     *
     * @param targetX  - X position of target
     * @param targetY  - Y position of target
     * @param duration - Duration of animation in seconds
     */
    public void moveLettersTo(final double targetX, final double targetY, final double duration) {
        if (currentAnimation != null) {
            currentAnimation.stop();
        }
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(lettersCircle);
        translate.setDuration(Duration.seconds(duration));
        translate.setByY(targetY - lettersCircle.getLayoutY() - layoutCenter); // -30 so the reference point is the
                                                                               // center
        translate.setByX(targetX - lettersCircle.getLayoutX() - layoutCenter);
        translate.play();

        currentAnimation = translate;
        translate.setOnFinished((event) -> {
            gameOverPage.setVisible(true);
            playerInputField.setText("");
            try {

                api.savePlayerHighscore(points.getText());
                gameOverHighScore.setText(String.valueOf(api.getHighScore()));
                gameOverScore.setText(points.getText());
                currentWordLabel.setText("The word the substring was taken from was: " + currentWord.toUpperCase());
                if (username.equals("guest")) {
                    gameOverHighScore.setText(highScore.getText());
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Reset the letter circle to original position and starts transaltion again.
     */
    public void resetLettersPos() {
        if (currentAnimation != null) {
            currentAnimation.stop(); // Stop any ongoing animation
        }

        lettersCircle.setLayoutX(layoutX);
        lettersCircle.setLayoutY(layoutY);
        lettersCircle.setTranslateX(0);
        lettersCircle.setTranslateY(0);
        letterVelocity = letterVelocity * 0.95;
        moveLettersTo(playerCircle.getCenterX(), playerCircle.getCenterY(), letterVelocity);
    }

    /**
     * game checks if player written word is correct. If right add 1 point
     * else shake game.
     *
     * @param ke - KeyEvent
     */
    public void checkWrittenWord(final KeyEvent ke) {
        colorCorrectLetters(playerInputField, outputField);
        if (ke.getCode().equals(KeyCode.ENTER)) { // If pressed Enter, then check word
            String playerGuess = playerInputField.getText();
            try {
                if (api.checkValidWord(substring, playerGuess)) {
                    int newPoints = Integer.parseInt(points.getText()) + 1;
                    points.setText(String.valueOf(newPoints));
                    playerInputField.setText("");
                    rndwordMasterLetters();
                    resetLettersPos();
                } else {
                    // Shake inputfield
                    TranslateTransition shake = new TranslateTransition();
                    shake.setDuration(Duration.millis(shakeDuration));
                    shake.setNode(outputField);
                    shake.setFromX(-shakeXMovment);
                    shake.setToX(shakeXMovment);
                    shake.play();
                }
            } catch (NumberFormatException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Color the letters in the guessed word that corresponds
     * with the Wordmaster letters in green.
     *
     * @param playerInput - The string the player has written
     * @param textFlow    - where to place the output string
     */
    public void colorCorrectLetters(final TextField playerInput, final TextFlow textFlow) {
        textFlow.getChildren().clear();
        boolean isFrstUsed = false, isScndUsed = false, isThirdUsed = false;
        String playerString = playerInput.getText();
        char[] correctLetters = substring.toCharArray();

        for (int i = 0; i < playerString.length(); i++) {
            char[] inputArray = playerString.toCharArray();
            Text coloredLetter = new Text(String.valueOf(inputArray[i]));
            try {
                if (inputArray[i] == correctLetters[0] && !isFrstUsed) {
                    coloredLetter.setFill(Color.GREEN);
                    isFrstUsed = true;
                } else if (inputArray[i - 1] == correctLetters[0] && inputArray[i] == correctLetters[1]
                        && !isScndUsed) {
                    coloredLetter.setFill(Color.GREEN);
                    isScndUsed = true;
                } else if (inputArray[i - 2] == correctLetters[0]
                        && inputArray[i - 1] == correctLetters[1]
                        && inputArray[i] == correctLetters[2]
                        && !isThirdUsed) {
                    coloredLetter.setFill(Color.GREEN);
                    isThirdUsed = true;
                } else {
                    coloredLetter.setFill(Color.WHITE);
                }
            } catch (Exception e) {
                coloredLetter.setFill(Color.WHITE);
            }
            textFlow.getChildren().add(coloredLetter);
        }
    }

    /**
     * Displays a random set of letters from the category answers.
     * The length of the letters is either 2 or 3.
     */
    public void rndwordMasterLetters() {
        substring = getSubstring();
        letters.setText(substring.toUpperCase());
    }

    /**
     * Gets a random substring from the current word.
     *
     * @return The substring
     *
     */
    public String getSubstring() {
        try {
            currentWord = api.getWord();

            do {
                int wordLength = currentWord.length();
                int startIndexSubstring = Math.max(random.nextInt(wordLength) - 2, 0);
                int endIndexSubstring = startIndexSubstring + 2 + random.nextInt(2);
                substring = currentWord.substring(startIndexSubstring, endIndexSubstring);
            } while (substring.contains(" "));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get substring because of: " + e.getMessage());
        }

        return substring;
    }

    /**
     * Opens or closes the HowToPlay window.
     */
    @FXML
    public void howToPlay() {
        if (hasShownHowToPlay && showHowToPlay) {
            howToPlay.setVisible(false);
            showHowToPlay = false;
        } else if (showHowToPlay) {
            howToPlay.setVisible(false);
            showHowToPlay = false;
            hasShownHowToPlay = true;
            moveLettersTo(playerCircle.getCenterX(), playerCircle.getCenterY(), 30);
        } else {
            howToPlay.setVisible(true);
            showHowToPlay = true;
        }
    }

    /**
     * Changes the current scene back to category page.
     */
    public void backToCategories() {
        changeSceneTo("Category.fxml", backArrowBtn, new CategoryFactory(username));
    }

    /**
     * Restarts the game in the current category.
     */
    public void restartGame() {
        try {
            if (!username.equals("guest")) {
                highScore.setText(String.valueOf(api.getHighScore()));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        if (username.equals("guest")) { // To check and update highscore if player is guest
            int currentHighScore = Integer.parseInt(gameOverHighScore.getText());
            int currentPoints = Integer.parseInt(points.getText());
            System.out.println("Highscore " + currentHighScore);
            System.out.println("Points " + currentPoints);

            if (currentPoints > currentHighScore) {
                highScore.setText(String.valueOf(currentPoints));
                gameOverHighScore.setText(String.valueOf(currentPoints));
                System.out.println("This runs");
            } else {
                highScore.setText(String.valueOf(currentHighScore));

            }
        }
        playerInputField.setText("");
        rndwordMasterLetters();
        letterVelocity = 30;
        gameOverPage.setVisible(false);
        resetLettersPos();
        points.setText("0");

    }

    /**
     * This formats categories that appear in game page.
     * @param input - category names.
     * @return - formatted string.
     */
    public String formatString(final String input) {
        String[] words = input.split(" ");
        StringBuilder formattedString = new StringBuilder();

        for (String word : words) {
            if (word.length() > 1) {
                formattedString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            } else {
                formattedString.append(word.toUpperCase());
            }

            formattedString.append(" ");
        }
        return formattedString.toString().trim();
    }

    @Override // Runs on start of the application
    public void initialize(final URL location, final ResourceBundle resources) {
        setBackArrowImg(backArrowImg);
        try {
            imageGame.setImage(
                    new Image(new FileInputStream(
                            Paths.get("runtime/assets").toAbsolutePath() + "/images/gamepagenew.png")));
            api.newGame(username, currentCategory);
            rndwordMasterLetters();
            playerCircle = new Circle(centerX, centerY, radius,
                    new ImagePattern(new Image(new FileInputStream("./runtime/assets/images/Brage.png"))));

            innerWindow.getChildren().addAll(playerCircle);
            outputField.setStyle("-fx-font: 24 arial;");
            // Change textfield format till uppercase
            playerInputField.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;
            }));
            playerInputField.requestFocus();
            String formattedCategory = formatString(currentCategory);
            categoryDisplay.setText("Category: " + formattedCategory);
            highScore.setText(String.valueOf(api.getHighScore()));

            // Add shutdownhook that updates user highscore when closing application
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    if (!username.equals("guest")) {
                        try {
                            api.savePlayerHighscore(points.getText());
                        } catch (NumberFormatException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, "Shutdown-thread"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}