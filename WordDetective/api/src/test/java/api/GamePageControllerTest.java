package api;

import com.hackerrank.test.utility.Order;
import com.hackerrank.test.utility.OrderedTestRunner;
import com.hackerrank.test.utility.TestWatchman;

import persistence.JsonIO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.nio.charset.StandardCharsets;
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
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(OrderedTestRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GamePageControllerTest {

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
   * @param username Username of the current user.
   * @param category Category selected by the current user.
   * @throws Exception If there is an error while contacting the API.
   */
  private void templateNewGame(final String username, final String category) throws Exception {
    String requestBody = "username=" + username + "&category=" + category;
    mockMvc.perform(post("/GamePageController/newGame")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  /**
   * Template to reduce redundant code.
   *
   * @param endpoint  Specifies where to sent the request.
   * @param mediaType Specifies the type of the content sent to the API.
   * @return String containing the response from the API
   * @throws Exception If there is an error while contacting the API.
   */
  private String templateGetRequest(final String endpoint, final MediaType mediaType) throws Exception {
    return mockMvc.perform(get(endpoint)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType))
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  /**
   * Tests correct setup of new game instance.
   *
   * @throws Exception If there is an error while contacting the API.
   */
  @Test
  @Order(1)
  public void testNewGame() throws Exception {
    templateNewGame("TestUser", "colors");
  }

  /**
   * Tests that the API returns a substring when requested.
   *
   * @throws Exception If there is an error while contacting the API.
   */
  @Test
  @Order(2)
  public void testGetWord() throws Exception {
    templateNewGame("TestUser", "colors");
    String response = templateGetRequest("/GamePageController/getWord",
        new MediaType("text", "plain", StandardCharsets.UTF_8));
    assertNotNull(response);
  }

  /**
   * Tests correct retrieval of user highscore.
   *
   * @throws Exception If there is an error while contacting the API.
   */
  @Test
  @Order(3)
  public void testGetHighscore() throws Exception {
    templateNewGame("TestUser", "colors");
    String response = templateGetRequest("/GamePageController/getPlayerHighscore", MediaType.APPLICATION_JSON);
    Integer highscore = Integer.parseInt(response);
    Integer actualHighscore = 0;
    assertEquals(actualHighscore, highscore);
  }

  /**
   * Tests that the highscore get set correcty.
   *
   * @throws Exception If there is an error while contacting the API.
   */
  @Test
  @Order(FINALTEST)
  public void testSetHighscore() throws Exception {
    templateNewGame("TestUser", "colors");
    mockMvc.perform(put("/GamePageController/savePlayerHighscore")
        .content("100")
        .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());

    String response = templateGetRequest("/GamePageController/getPlayerHighscore", MediaType.APPLICATION_JSON);
    Integer highscore = Integer.parseInt(response);
    Integer actualHighscore = 100;
    assertEquals(actualHighscore, highscore);

    mockMvc.perform(put("/GamePageController/savePlayerHighscore")
        .content("200")
        .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());

    response = templateGetRequest("/GamePageController/getPlayerHighscore", MediaType.APPLICATION_JSON);
    highscore = Integer.parseInt(response);
    // Only write if highscore is higher
    actualHighscore = 200;
    assertEquals(actualHighscore, highscore);

    // cleanup
    JsonIO jsonIO = new JsonIO("TestUser");
    jsonIO.updateCurrentUser(
        (user) -> {
          user.setHighscore(0);
          return true;
        });

  }

}
