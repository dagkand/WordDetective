package types;

public enum LoginStatus {

  /**
   * Returned if the login is successful.
   */
  SUCCESS,

  /**
   * Returned if the provided username for login does not exist.
   */
  USERNAME_DOES_NOT_EXIST,

  /**
   * Returned if the provided password for the login does not match the stored
   * password for the provided user.
   */
  INCORRECT_PASSWORD,

  /**
   * Retured if there occurs an error while attempting to read information from
   * the user's file.
   */
  READ_ERROR

}
