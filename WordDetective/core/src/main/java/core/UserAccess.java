package core;

import java.util.HashMap;
import java.util.Set;
import persistence.JsonIO;

public class UserAccess {

  /**
   * Instance of JsonIO used to gain access to persistently stored information of
   * a ceratin user.
   */
  private JsonIO jsonIO;

  /**
   * Instantiates a new instance of UserAccess, enabling access to persistently
   * stored information about a certain user.
   *
   * @param username The username of the user to access the persistent information
   *                 of.
   */
  public UserAccess(final String username) {
    this.jsonIO = new JsonIO(username);
  }

  /**
   * Provides access to the JsonIO.
   *
   * @return The instances JsonIO.
   */
  public JsonIO getJsonIO() {
    return jsonIO;
  }

  /**
   * Get all categories available to the current user.
   * Custom and default categories are separated.
   *
   * @return a {@link Hashmap} containing all categories available to the current
   */
  // kan brukes videre av Game for velging av ny kategori underveis i en
  // spill-økt.
  public HashMap<String, Set<String>> getAllCategories() {
    return jsonIO.getAllCategories();
  }

}
