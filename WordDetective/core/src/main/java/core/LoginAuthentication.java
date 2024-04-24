package core;

import java.io.IOException;
import persistence.JsonIO;
import types.LoginStatus;

public class LoginAuthentication extends AbstractAuthentication {

  /**
   * Username of the current user.
   * Used for methods dependent of the users persistently stored information.
   */
  private String username;

  /**
   * Instantiates a new instance of LoginAuthentication.
   *
   * @param providedUsername The provided username of the user to authenticate the
   *                         login of.
   */
  public LoginAuthentication(final String providedUsername) {
    this.username = providedUsername;
  }

  /**
   * Checks if the password provided by the user matches the password stored for
   * the given username.
   *
   * @param password The password provided by the user.
   * @return SUCCESS, USERNAME_DOES_NOT_EXIST, INCORRECT_PASSWORD, or READ_ERROR,
   *         respectively.
   */
  public LoginStatus authenticate(final String password) {
    if (!usernameExists(username)) {
      return LoginStatus.USERNAME_DOES_NOT_EXIST;
    }
    try {
      if (isValidPassword(password)) {
        return LoginStatus.SUCCESS;
      }
      return LoginStatus.INCORRECT_PASSWORD;
    } catch (RuntimeException e) {
      return LoginStatus.READ_ERROR;
    }
  }

  @Override
  public final boolean isValidPassword(final String password) throws RuntimeException {
    try {
      if (JsonIO.usernameAndPasswordMatch(username, password)) {
        return true;
      }
      return false;
    } catch (IOException e) {
      throw new RuntimeException(
          "Could not find given user.");
    }
  }

}
