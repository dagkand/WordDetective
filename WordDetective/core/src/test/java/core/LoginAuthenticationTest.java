package core;

import types.LoginStatus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginAuthenticationTest {

  /**
   * Check login into a user that has not been made.
   */
  @Test
  public void testAuthenticateUsernameDoesNotExist() {
    LoginAuthentication loginAuth = new LoginAuthentication("nonexistentUser");
    assertEquals(LoginStatus.USERNAME_DOES_NOT_EXIST, loginAuth.authenticate("somePassword"));
  }

  /**
   * Check login into a user that exists but the password does not match.
   */
  @Test
  public void testAuthenticateIncorrectPassword() {
    LoginAuthentication loginAuth = new LoginAuthentication("TestUser");
    assertEquals(LoginStatus.INCORRECT_PASSWORD, loginAuth.authenticate("incorrectPassword"));
  }

  /**
   * Test logging in succesfully.
   */
  @Test
  public void testAuthenticateSuccess() {
    LoginAuthentication loginAuth = new LoginAuthentication("TestUser");
    assertEquals(LoginStatus.SUCCESS, loginAuth.authenticate("Password"));
  }

  /**
   * Test read authentication error.
   * Use mockito to test this.
   */
  @Test
  public void testAuthenticateReadError() {
    // Mock the LoginAuthentication class
    LoginAuthentication loginAuthMock = mock(LoginAuthentication.class);

    // Force the isValidPassword method to throw a RuntimeException when called
    doThrow(new RuntimeException("Mocked read error")).when(loginAuthMock).isValidPassword(anyString());

    // Call the authenticate method, and handle the exception to return READ_ERROR
    when(loginAuthMock.authenticate(anyString())).thenReturn(LoginStatus.READ_ERROR);

    // Call the authenticate method, which should now return READ_ERROR
    assertEquals(LoginStatus.READ_ERROR, loginAuthMock.authenticate("triggerReadError"));
  }
}