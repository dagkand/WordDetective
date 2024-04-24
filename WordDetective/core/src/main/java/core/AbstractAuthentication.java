package core;

import persistence.JsonIO;

public abstract class AbstractAuthentication {

  /**
   * Implemented method for checking if provided username already exists.
   *
   * @param username The username to for existence.
   * @return Boolean indicating if a user with the given username already exists.
   */
  protected boolean usernameExists(final String username) {
    return JsonIO.getPersistentFilenames("/users").contains(username);
  }

  /**
   * Abstract method implemented by subclasses to check if the provided password
   * matches their requirements.
   *
   * @param password The password provided by the user.
   * @return A boolean indicating if the subclass accepts the password.
   */
  protected abstract boolean isValidPassword(String password);

}
