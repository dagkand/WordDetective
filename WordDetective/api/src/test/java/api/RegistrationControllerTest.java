package api;

import persistence.JsonIO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackerrank.test.utility.Order;
import com.hackerrank.test.utility.OrderedTestRunner;
import com.hackerrank.test.utility.TestWatchman;
import types.RegistrationStatus;
import static org.junit.Assert.assertSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.lang.reflect.Type;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(OrderedTestRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

  /**
   * Integer used to specify the last executed test.
   */
  private static final int FINALTEST = 4;

  /**
   * Classrule used for testing purposes.
   */
  @ClassRule
  public static final SpringClassRule SPRINGCLASSRULE = new SpringClassRule();

  /**
   * Rule definition used for testing purposes.
   */
  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();

  /**
   * Rule definition used for testing purposes.
   */
  @Rule
  public TestWatcher watchman = TestWatchman.watchman;

  /**
   * Setup of the Springboot server.
   */
  @BeforeClass
  public static void setUpClass() {
    TestWatchman.watchman.registerClass(LoginControllerTest.class);
  }

  /**
   * Shutdown of the Springboot server.
   */
  @AfterClass
  public static void tearDownClass() {
    TestWatchman.watchman.createReport(LoginControllerTest.class);
  }

  /**
   * Mock instance used to retrieve information from API without setting up the
   * server.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Template to reduce redundant code.
   *
   * @param username The new username provided by the user.
   * @param password The new password provided by the user.
   * @return RegistrationStatus indicating the result of the registration.
   * @throws Exception If any errors are encountered while contacting the API.
   */
  private RegistrationStatus testTemplate(final String username, final String password) throws Exception {
    String response = mockMvc.perform(get("/RegistrationController/registrationResult")
        .param("username", username)
        .param("password", password)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();
    Type type = new TypeToken<RegistrationStatus>() {
    }.getType();
    return new Gson().fromJson(response, type);
  }

  /**
   * Test successful registration with valid credentials.
   *
   * @throws Exception If any errors are encountered while contacting the API
   */
  @Test
  @Order(1)
  public void testRegistrationSuccess() throws Exception {
    RegistrationStatus result = testTemplate("NewUser123", "Password123!");
    assertSame(RegistrationStatus.SUCCESS, result);
    // endre tilgang for bedre modularisering.
    JsonIO.deleteUser("NewUser123");
  }

  /**
   * Test correct error when username is taken.
   *
   * @throws Exception If any errors are encountered while contacting the API
   */
  @Test
  @Order(2)
  public void testRegistrationUsernameTaken() throws Exception {
    RegistrationStatus result = testTemplate("TestUser", "NewPassword123!");
    assertSame(RegistrationStatus.USERNAME_TAKEN, result);
  }

  /**
   * Test correct error when username does not match the set criteria.
   *
   * @throws Exception If any errors are encountered while contacting the API
   */
  @Test
  @Order(3)
  public void testRegistrationUsernameNotMatchRegex() throws Exception {
    RegistrationStatus result = testTemplate("guest", "NewPassword123!");
    assertSame(RegistrationStatus.USERNAME_NOT_MATCH_REGEX, result);
  }

  /**
   * Test correct error when password does not match the set criteria.
   *
   * @throws Exception If any errors are encountered while contacting the API
   */
  @Test
  @Order(FINALTEST)
  public void testRegistrationPasswordNotMatchRegex() throws Exception {
    RegistrationStatus result = testTemplate("NewUser123", "notvalidpassword");
    assertSame(RegistrationStatus.PASSWORD_NOT_MATCH_REGEX, result);
  }

}
