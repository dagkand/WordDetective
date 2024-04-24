package types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

  /**
   * Valid user used for testing.
   */
  private User validUser;

  /**
   * Initialize before each test.
   */
  @BeforeEach
  public void init() {
    validUser = new User("Testuser", "Correct123");
  }

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    User guest = new User();
    assertEquals(guest.getUsername(), "guest",
        "Username should be guest on empty constructor not: " + guest.getUsername());
    assertEquals(null, guest.getPassword(), "Password should be null on guest, not:" + guest.getPassword());
    assertEquals(guest.getHighScore(), 0, "Higscore should be 0, not:" + guest.getHighScore());

    User user = new User("Username", "Password123");
    assertEquals(user.getUsername(), "Username", "Username should be 'Username' not: " + user.getUsername());
    assertEquals(user.getPassword(), "Password123",
        "Password should be 'Password123' on user, not:" + user.getPassword());
    assertEquals(user.getHighScore(), 0, "Higscore should be 0, not:" + user.getHighScore());
  }

  /**
   * Test highscore setting/getting.
   */
  @Test
  public void highscoreTest() {
    assertEquals(validUser.getHighScore(), 0, "Highscore should be 0, not:" + validUser.getHighScore());
    validUser.setHighscore(20);
    assertEquals(validUser.getHighScore(), 20, "Highscore should be 20, not:" + validUser.getHighScore());
    validUser.setHighscore(50);
    assertEquals(validUser.getHighScore(), 50, "Highscore should be 20, not:" + validUser.getHighScore());
  }

  /**
   * Test set/get custom categories.
   */
  @Test
  public void customCategoriesTest() {
    HashMap<String, List<String>> hashmap = new HashMap<>();
    assertEquals(validUser.getCustomCategories(), hashmap, "Custom categories should be empty");

    hashmap.put("TestCategory", Arrays.asList("Test", "Test", "Test"));
    validUser.addCustomCategories("TestCategory", Arrays.asList("Test", "Test", "Test"));
    assertEquals(validUser.getCustomCategories(), hashmap, "Custom categories should contain " + hashmap.toString());

    hashmap.put("TestCategory2", Arrays.asList("Test", "Test", "Test"));
    hashmap.put("TestCategory3", Arrays.asList("Test", "Test", "Test"));
    validUser.setCustomCategories(hashmap);
    assertEquals(validUser.getCustomCategories(), hashmap, "Custom categories should be same as hashmap");

    validUser.deleteCustomCategories("TestCategory");
    assertNull(validUser.getCustomCategories().get("TestCategory"), "TestCategory should have been deleted");

    assertThrows(RuntimeException.class, () -> {
      validUser.deleteCustomCategories("nonEistingCategory");
    });

  }

}
