package types;

public enum RegistrationStatus {

  /**
   * Returned if the registration of a new user is successful.
   */
  SUCCESS,

  /**
   * Returned if the provided username for a registration is taken.
   */
  USERNAME_TAKEN,

  /**
   * Returned if the provided username for a registration does not match the set
   * username regular expression.
   */
  USERNAME_NOT_MATCH_REGEX,

  /**
   * Returned if the provided password for a registration does not match the set
   * username regular expression.
   */
  PASSWORD_NOT_MATCH_REGEX,

  /**
   * Retured if there occurs an error while attempting to create the new user's
   * file.
   */
  UPLOAD_ERROR

}
