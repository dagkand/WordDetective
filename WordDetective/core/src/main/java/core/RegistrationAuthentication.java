package core;

import persistence.JsonIO;
import types.RegistrationStatus;
import types.User;

public final class RegistrationAuthentication extends AbstractAuthentication {

  /**
   * Attemps to registrate the new user.
   *
   * @param newUsername The new username provided by the user.
   * @param newPassword The new password provided by the user.
   * @return SUCCESS, USERNAME_TAKEN, USERNAME_NOT_MATCH_REGEX,
   *         PASSWORD_NOT_MATCH_REGEX, or UPLOAD_ERROR, respectively.
   */
  public RegistrationStatus registrationResult(final String newUsername, final String newPassword) {
    if (usernameExists(newUsername)) {
      return RegistrationStatus.USERNAME_TAKEN;
    }
    if (!isValidUsername(newUsername)) {
      return RegistrationStatus.USERNAME_NOT_MATCH_REGEX;
    }
    if (!isValidPassword(newPassword)) {
      return RegistrationStatus.PASSWORD_NOT_MATCH_REGEX;
    }
    if (JsonIO.addUser(new User(newUsername, newPassword))) {
      return RegistrationStatus.SUCCESS;
    }
    return RegistrationStatus.UPLOAD_ERROR;
  }

  /**
   * check if password is correct according to set regex.
   *
   * @return {@link Boolean} Indicating if the password matches the set regular
   *         expression.
   */
  @Override
  protected boolean isValidPassword(final String password) {
    return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{5,}$");
  }

  /**
   * check if username is correct according to set regex.
   *
   * @param newUsername The new username provided by the user
   * @return {@link Boolean} Indicating if the username matches the set regular
   *         expression.
   */
  public boolean isValidUsername(final String newUsername) {
    return newUsername.matches("^(?!guest)[a-zA-Z0-9_ ]{2,}$");
  }

}
