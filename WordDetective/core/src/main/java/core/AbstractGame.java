package core;

import java.io.IOException;
import java.util.List;

public interface AbstractGame {

  /**
   * Set the chosen category.
   *
   * @param category The category chosen by the user.
   * @throws IllegalArgumentException - If category does not exist
   */
  void setCategory(String category) throws IllegalArgumentException;

  /**
   * Set new wordlist.
   *
   * @param newWordList - a {@link List} containing answers
   */
  void setWordList(List<String> newWordList);

  /**
   * Get the wordlist containing answers.
   *
   * @return {@link List} of strings containing answers
   */
  List<String> getWordList();

  /**
   * Get a random word from the wordlist.
   *
   * @return A random word from the wordlist as {@link String}.
   */
  String getWord();

  /**
   * Checks if the guess is present in wordlist and wether the substring is
   * present.
   *
   * @param substring The substring used to make a guess.
   * @param guess     The guess provided by the user. Should contain substring,
   *                  and be part of the wordlist.
   * @return True if the guess is valid, else false.
   */
  boolean checkValidWord(String substring, String guess);

  /**
   * Save the current players highscore to database.
   * Will not save if player is guest.
   *
   * @param highscore - The highscore to save
   *
   */
  void savePlayerHighscore(int highscore) throws IOException;

}
