package api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import core.Game;

@RestController
public class GamePageController {

  /**
   * Session scoped Bean injected as dependecy from Springboot.
   */
  private Game game;

  /**
   * API endpoint for setup of the user's game instance.
   *
   * @param requestBody The requestbody cotaining the username and category chosen
   *                    by the user.
   */
  @RequestMapping(value = "/GamePageController/newGame", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void newGame(@RequestBody final String requestBody) {
    String[] components = requestBody.split("&");
    String username = components[0].split("=")[1];
    String category = components[1].split("=")[1];
    game = new Game(username);
    game.setCategory(category);
  }

  /**
   * API endpoint for fetching a randomly generated substring from a randomly
   * pulled word.
   *
   * @return A substring generated from a word randomly pulled from the current
   *         wordlist.
   */
  @RequestMapping(value = "/GamePageController/getWord", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public String getWord() {
    return game.getWord();
  }

  /**
   * API endpoint to check if the guessed word contains the substring and is
   * present in the wordlist.
   *
   * @param substring The substring provided to the user.
   * @param guess     The guess provided by the user.
   * @return Boolean indicating if guess was correct.
   */
  @RequestMapping(value = "/GamePageController/checkValidWord", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public boolean checkValidWord(final @RequestParam("substring") String substring,
      final @RequestParam("guess") String guess) {
    return game.checkValidWord(substring, guess);
  }

  /**
   * API endpoint for saving the player's highscore to file, if it's a new
   * highscore.
   *
   * @param highscore The score to potentially save to file.
   */
  @RequestMapping(value = "/GamePageController/savePlayerHighscore", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void savePlayerHighscore(@RequestBody final String highscore) {
    game.savePlayerHighscore(Integer.parseInt(highscore));
  }

  /**
   * API endpoint for retrieving the user's highscore.
   *
   * @return The user's highscore.
   */
  @RequestMapping(value = "/GamePageController/getPlayerHighscore", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public int getPlayerHighscore() {
    return game.getPlayerHighscore();
  }

}
