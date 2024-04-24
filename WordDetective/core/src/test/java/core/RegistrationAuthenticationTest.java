package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import persistence.JsonIO;
import types.RegistrationStatus;

public class RegistrationAuthenticationTest {

  /**
   * registrationAuthentication variable for testing.
   */
  private RegistrationAuthentication registrationAuthentication;

  /**
   * Make new registrationAuthentication for each test run.
   */
  @BeforeEach
  public void setUp() {
    registrationAuthentication = new RegistrationAuthentication();
  }

  /**
   * Test valid username and password.
   */
  @Test
  public void testRegistrationSuccess() {
    RegistrationStatus result = registrationAuthentication.registrationResult("newUser", "Password123");
    assertEquals(RegistrationStatus.SUCCESS, result);
  }

  /**
   * Valid password but username is already in use.
   */
  @Test
  public void testRegistrationUsernameTaken() {
    RegistrationStatus result = registrationAuthentication.registrationResult("newUser", "Password123");
    assertEquals(RegistrationStatus.USERNAME_TAKEN, result);
  }

  /**
   * Username does not fit regex.
   */
  @Test
  public void testRegistrationInvalidUsername() {
    RegistrationStatus result = registrationAuthentication.registrationResult("invalidUsername$", "Password123");
    assertEquals(RegistrationStatus.USERNAME_NOT_MATCH_REGEX, result);
  }

  /**
   * Password does not fulfill the criteria.
   */
  @Test
  public void testRegistrationInvalidPassword() {
    RegistrationStatus result = registrationAuthentication.registrationResult("bigBossMan", "weak");
    assertEquals(RegistrationStatus.PASSWORD_NOT_MATCH_REGEX, result);
  }

  /**
   * more tests for password regex.
   */
  @Test
  public void testIsValidPassword() {
    assertTrue(registrationAuthentication.isValidPassword("StrongPassword123"));
    assertFalse(registrationAuthentication.isValidPassword("weak"));
  }

  /**
   * more tests for username regex.
   */
  @Test
  public void testIsValidUsername() {
    assertTrue(registrationAuthentication.isValidUsername("validUsername123"));
    assertFalse(registrationAuthentication.isValidUsername("invalidUsername$"));
    assertFalse(registrationAuthentication.isValidUsername("guestUsername"));
  }

  /**
   * After all test delete the test user.
   */
  @AfterAll
  public static void cleanUpAfterAllTests() {
    JsonIO.deleteUser("newUser");
  }

}