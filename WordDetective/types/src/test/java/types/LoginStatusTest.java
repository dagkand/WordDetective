package types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class LoginStatusTest {

  /**
   * Test correct value of "SUCCESS".
   */
  @Test
  public void testSuccess() {
    assertEquals(LoginStatus.SUCCESS, LoginStatus.valueOf("SUCCESS"));
  }

  /**
   * Test correct value of "USERNAME_DOES_NOT_EXIST".
   */
  @Test
  public void testUsernameDoesNotExist() {
    assertEquals(LoginStatus.USERNAME_DOES_NOT_EXIST, LoginStatus.valueOf("USERNAME_DOES_NOT_EXIST"));
  }

  /**
   * Test correct value of "INCORRECT_PASSWORD".
   */
  @Test
  public void testIncorrectPassword() {
    assertEquals(LoginStatus.INCORRECT_PASSWORD, LoginStatus.valueOf("INCORRECT_PASSWORD"));
  }

  /**
   * Test correct value of "READ_ERROR".
   */
  @Test
  public void testReadError() {
    assertEquals(LoginStatus.READ_ERROR, LoginStatus.valueOf("READ_ERROR"));
  }

}
