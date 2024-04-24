package core;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * The GameLogic class is responsible for the logic of the game.
 * It will delegate certain tasks to other objects,
 * who have more suitable functionality.
 */
public final class Game extends UserAccess implements AbstractGame {

    /**
     * Boolean indicating if the user is a guest user.
     * Used to avoid certain user-only methods.
     */
    private boolean isGuestUser;

    // /**
    // * The category chosen by the user.
    // */
    // private String chosenCategory;

    /**
     * List of answers for the chosen category.
     */
    private List<String> wordlist;

    /**
     * Random object used to provide random numbers.
     */
    private static Random random = new Random();

    /**
     * Initializes the Game object, which will control the logic of the game.
     * Certain tasks will be delegated to objects with better functionality.
     *
     * @param username The user's username, used to set up individualized games for
     *                 different users.
     */
    public Game(final String username) {
        super(username);
        this.isGuestUser = username.equals("guest");
    }

    /**
     * Delegates the task of fetching the wordlist of the requested category.
     */
    @Override
    public void setCategory(final String category) {
        try {
            this.wordlist = getJsonIO().getCategoryWordlist(category);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getWordList() {
        return this.wordlist;
    }

    @Override
    public void setWordList(final List<String> newWordList) {
        this.wordlist = newWordList;
    }

    @Override
    public String getWord() {
        return wordlist.get(random.nextInt(wordlist.size()));
    }

    @Override
    public boolean checkValidWord(final String substring, final String guess) {
        return guess.matches(".*" + substring + ".*") && wordlist.contains(guess);
    }

    /**
     * Delegates the task of retrieving the user's highscore.
     *
     * @return The user's highscore
     */
    public int getPlayerHighscore() {
        return isGuestUser ? 0 : getJsonIO().getUserProperty(user -> user.getHighScore());
    }

    @Override
    public void savePlayerHighscore(final int score) {
        if (!isGuestUser) {
            try {
                getJsonIO().updateCurrentUser(
                        (user) -> {
                            if (user.getHighScore() < score) {
                                user.setHighscore(score);
                                return true;
                            }
                            return false;
                        });
            } catch (IOException e) {
                // evt håndtere den
                e.printStackTrace();
            }
        }
    }
}
