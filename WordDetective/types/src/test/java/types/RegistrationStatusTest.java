package types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class RegistrationStatusTest {

  /**
   * Test correct value of "SUCCESS".
   */
  @Test
  public void testSuccess() {
    assertEquals(RegistrationStatus.SUCCESS, RegistrationStatus.valueOf("SUCCESS"));
  }

  /**
   * Test correct value of "USERNAME_TAKEN".
   */
  @Test
  public void testUsernameTaken() {
    assertEquals(RegistrationStatus.USERNAME_TAKEN, RegistrationStatus.valueOf("USERNAME_TAKEN"));
  }

  /**
   * Test correct value of "USERNAME_NOT_MATCH_REGEX".
   */
  @Test
  public void testUsernameNotMatchRegex() {
    assertEquals(RegistrationStatus.USERNAME_NOT_MATCH_REGEX, RegistrationStatus.valueOf("USERNAME_NOT_MATCH_REGEX"));
  }

  /**
   * Test correct value of "PASSWORD_NOT_MATCH_REGEX".
   */
  @Test
  public void testPasswordNotMatchRegex() {
    assertEquals(RegistrationStatus.PASSWORD_NOT_MATCH_REGEX, RegistrationStatus.valueOf("PASSWORD_NOT_MATCH_REGEX"));
  }

  /**
   * Test correct value of "UPLOAD_ERROR".
   */
  @Test
  public void testUploadError() {
    assertEquals(RegistrationStatus.UPLOAD_ERROR, RegistrationStatus.valueOf("UPLOAD_ERROR"));
  }

}
