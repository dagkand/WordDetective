package persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import types.User;

public interface AbstractJsonIO {

  /**
   * Fetches the names of all the categories available to the current user.
   * Custom and default categories are separated.
   *
   * @return a {@link Hashmap} containing all categories available to the current
   *         user.
   */
  HashMap<String, Set<String>> getAllCategories();

  /**
   * Provides absolute path to current working directory.
   * Retrieves a certain property from the current user.
   *
   * @param <T>      Specification of return type.
   * @param function Functional interface to access the desired property.
   * @return The retrieved property.
   */
  <T> T getUserProperty(Function<User, T> function);

  /**
   * Update user and store new data in database.
   *
   * @param predicate A predicate which potentially changes the instance of the
   *                  user.
   *                  The predicate returns a boolean indicating if changes were
   *                  made, which is used to persistenty store the potential
   *                  changes.
   */
  void updateCurrentUser(Predicate<User> predicate) throws IOException;

  /**
   * Fetches the words contained in the wordlist of the given category.
   *
   * @param category The category to fetch the wordlist of.
   * @return List<String> containing all the words in the categories wordlist.
   * @throws IOException      If any issues are encountered during interaction
   *                          with the files.
   * @throws RuntimeException If the given category is present neither among the
   *                          default nor the custom categories.
   */
  List<String> getCategoryWordlist(String category) throws IOException, RuntimeException;

}
