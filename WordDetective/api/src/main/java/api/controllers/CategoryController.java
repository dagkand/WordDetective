package api.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import core.UserAccess;

@RestController
public class CategoryController {

  /**
   * UserAccess instance to provide access to persistently stored user
   * information.
   */
  private UserAccess userAccess;

  /**
   * API endpoint for fetching og categories related to certain user.
   *
   * @param username Username of the suer to fetch the categories of.
   * @return Set<String> with all category names.
   */
  @RequestMapping(value = "/CategoryController/getCategories", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public HashMap<String, Set<String>> getCategories(final @RequestParam("username") String username) {
    this.userAccess = new UserAccess(username);
    return userAccess.getAllCategories();
  }

  /**
   * API endpoint for enabling a user to add a new custom category.
   *
   * @param requestBody  Requestbody containing the category's name and
   *                     correlating
   *                     wordlist.
   * @param categoryName The name for the given category.
   */
  @RequestMapping(value = "/CategoryController/addCustomCategory/{categoryName}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void addCustomCategory(@PathVariable final String categoryName, @RequestBody final String requestBody) {
    List<String> wordList = Arrays.asList(
        requestBody
            .split("\"wordList\":")[1]
            .split("\\[")[1]
            .split("]")[0]
            .split(", "));
    try {
      userAccess.getJsonIO().updateCurrentUser(
          (user) -> {
            user.addCustomCategories(categoryName, wordList);
            return true;
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * API endpoint for enabling a user to add a new custom category.
   *
   * @param categoryName The category's name and correlating wordlist.
   */
  @RequestMapping(value = "/CategoryController/deleteCustomCategory/{categoryName}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void deleteCustomCategory(@PathVariable final String categoryName) {
    try {
      userAccess.getJsonIO().updateCurrentUser(
          (user) -> {
            user.deleteCustomCategories(categoryName);
            return true;
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
