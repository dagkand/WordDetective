package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import types.User;

public class JsonIOTest {

  /**
   * JsonIO object.
   */
  private JsonIO jsonIO;

  /**
   * User instance used for testing.
   */
  private User testUser;

  /**
   * Sets up required classes for testing.
   */
  @BeforeEach
  public void setup() {
    testUser = new User("TestUser", "Password");
  }

  /**
   * Test user added.
   */
  @Test
  public void testUserDeleteAndAdd() {
    JsonIO.deleteUser(testUser.getUsername());
    assertFalse(JsonIO.getAllUsernames().contains("TestUser"));
    JsonIO.addUser(testUser);
    assertTrue(JsonIO.getAllUsernames().contains("TestUser"));
  }

  /**
   * Test adding and deleting a user.
   */
  @Test
  public void testUserOccupied() {
    assertThrows(IllegalArgumentException.class, () -> JsonIO.addUser(testUser),
        "Should not be able to add an existing user");

    assertThrows(IllegalArgumentException.class, () -> JsonIO.deleteUser("NonexistingUser"),
        "Should not be able to delete a non existing user");
  }

  /**
   * Test getting a user from file.
   */
  @Test
  public void testGetUser() {
    assertEquals(testUser.getUsername(),
        JsonIO.getUser(testUser.getUsername()).getUsername());
    assertEquals(testUser.getPassword(),
        JsonIO.getUser(testUser.getUsername()).getPassword());
    assertEquals(testUser.getHighScore(),
        JsonIO.getUser(testUser.getUsername()).getHighScore());
    assertEquals(testUser.getCustomCategories(),
        JsonIO.getUser(testUser.getUsername()).getCustomCategories());
    assertThrows(RuntimeException.class, () -> JsonIO.getUser("Not existing user"));
  }

  /**
   * Test updating user.
   */
  @Test
  public void testUpdateUser() {
    assertEquals(testUser.getCustomCategories(),
        JsonIO.getUser(testUser.getUsername()).getCustomCategories(),
        "TestUser should have 0 custom categories");
    List<String> testCategory = Arrays.asList("Test", "Test2");
    try {
      jsonIO = new JsonIO(testUser.getUsername());
      jsonIO.updateCurrentUser(
          (user) -> {
            user.setHighscore(10);
            user.addCustomCategories("TestCategory", testCategory);
            return true;
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
    User retrivedUser = JsonIO.getUser(testUser.getUsername());
    assertEquals(1, retrivedUser.getCustomCategories().size());
    assertTrue(retrivedUser.getCustomCategories().containsValue(testCategory),
        "User should now have only TestCategory as a custom category, but has: "
            + retrivedUser.getCustomCategories().keySet());

    assertEquals(10, retrivedUser.getHighScore(), "Highscore should be 10 not "
        + retrivedUser.getHighScore());

    try {
      jsonIO = new JsonIO(testUser.getUsername());
      jsonIO.updateCurrentUser(
          (user) -> {
            user.setHighscore(0);
            user.deleteCustomCategories("TestCategory");
            return true;
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
    retrivedUser = JsonIO.getUser(testUser.getUsername());
    assertEquals(0, retrivedUser.getCustomCategories().size());
    assertFalse(retrivedUser.getCustomCategories().containsValue(testCategory),
        "User should not have any custom categories, but has: "
            + retrivedUser.getCustomCategories().keySet());

    assertEquals(0, retrivedUser.getHighScore(), "Highscore should be 0 not "
        + retrivedUser.getHighScore());
  }

  /**
   * Test getting default categories.
   */
  @Test
  public void testDefaultCategory() {
    Set<String> categories = new HashSet<>(Set.of("us states", "countries", "fruits"));
    assertTrue(JsonIO.getPersistentFilenames("/default_categories").containsAll(categories));

    // Adds a testCategory in the default_categories folder
    List<String> testCategory = Arrays.asList("Hei", "pa", "deg");
    try (FileWriter fw = new FileWriter(JsonIO.getPathToResources()
        + "/default_categories/testCategory.json", StandardCharsets.UTF_8)) {
      new Gson().toJson(testCategory, fw);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      assertEquals(testCategory, JsonIO.getDefaultCategory("testCategory"),
          "TestCategory should be equal to retrieved category, but was: "
              + JsonIO.getDefaultCategory("testCategory"));
    } catch (IOException e) {
      assertThrows(IOException.class, () -> JsonIO.getDefaultCategory("testCategory"));
    }
    Collection<String> defaultCategories = JsonIO.getPersistentFilenames("/default_categories");
    assertEquals(9, defaultCategories.size());
    assertTrue(defaultCategories.contains("testCategory"));
    assertFalse(defaultCategories.contains("Non existing"));
  }

  /**
   * Test get word list.
   */
  @Test
  public void testGetWordlist() {
    jsonIO = new JsonIO(testUser.getUsername());
    Set<String> states = new HashSet<>(Arrays.asList(
        "ALABAMA", "ALASKA", "ARIZONA", "ARKANSAS", "CALIFORNIA", "COLORADO", "CONNECTICUT", "DELAWARE",
        "FLORIDA", "GEORGIA", "HAWAII", "IDAHO", "ILLINOIS", "INDIANA", "IOWA", "KANSAS", "KENTUCKY",
        "LOUISIANA", "MAINE", "MARYLAND", "MASSACHUSETTS", "MICHIGAN", "MINNESOTA", "MISSISSIPPI", "MISSOURI",
        "MONTANA", "NEBRASKA", "NEVADA", "NEW HAMPSHIRE", "NEW JERSEY", "NEW MEXICO", "NEW YORK",
        "NORTH CAROLINA", "NORTH DAKOTA", "OHIO", "OKLAHOMA", "OREGON", "PENNSYLVANIA", "RHODE ISLAND",
        "SOUTH CAROLINA", "SOUTH DAKOTA", "TENNESSEE", "TEXAS", "UTAH", "VERMONT", "VIRGINIA", "WASHINGTON",
        "WEST VIRGINIA", "WISCONSIN", "WYOMING"));
    try {
      List<String> wordlist = jsonIO.getCategoryWordlist("us states");
      // Tests based on set equality: A ⊆ B and B ⊆ A <=> A = B
      assertTrue(states.containsAll(wordlist));
      assertTrue(wordlist.containsAll(states));
    } catch (IOException | RuntimeException e) {
      assertTrue(false, "Error fetching categories");
      e.printStackTrace();
    }

    assertThrows(RuntimeException.class, () -> {
      jsonIO.getCategoryWordlist("non existing category");
    });

  }

  /**
   * Tests getting all categories.
   */
  @Test
  public void testGetAllCategories() {
    jsonIO = new JsonIO(testUser.getUsername());
    Set<String> actualDefaultCategories = new HashSet<>(Arrays.asList(
        "chemical elements", "fruits", "major oceans and lakes", "us states", "countries",
        "programming languages", "colors", "100 most common carbrands"));
    Set<String> retrievedDefaultcategories = jsonIO.getAllCategories().get("default");
    // Tests based on set equality: A ⊆ B and B ⊆ A <=> A = B
    assertTrue(actualDefaultCategories.containsAll(retrievedDefaultcategories));
    assertTrue(retrievedDefaultcategories.containsAll(actualDefaultCategories));
  }

  /**
   * Test the getPersistentProperty method.
   * @throws IOException
   */
  @Test
  public void testGetPersistentProperty() throws IOException {
    // Create a temporary JSON file for testing
    File tempFile = File.createTempFile("test", ".json");
    try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
      writer.write("{\"property1\": \"value1\", \"property2\": \"value2\"}");
    }

    // Test case 1: Property found
    String propertyValue = JsonIO.getPersistentProperty("property1", tempFile.getAbsolutePath());
    assertEquals("value1", propertyValue);

    // Test case 2: Property not found
    IOException exception = assertThrows(IOException.class,
        () -> JsonIO.getPersistentProperty("nonexistentProperty", tempFile.getAbsolutePath()));
    assertEquals("Property not found", exception.getMessage());

    // Clean up the temporary file
    tempFile.delete();
  }

  /**
   * Test username and password match.
   * @throws IOException
   */
  @Test
    public void testUsernameAndPasswordMatch() throws IOException {
        // Test case 1: Matching username and password
        assertTrue(JsonIO.usernameAndPasswordMatch(testUser.getUsername(), testUser.getPassword()),
                "Should return true for matching username and password");

        // Test case 2: Non-matching password
        assertFalse(JsonIO.usernameAndPasswordMatch(testUser.getUsername(), "WrongPassword"),
                "Should return false for non-matching password");

        // Test case 3: IOException when reading property
        IOException exception = assertThrows(IOException.class,
                () -> JsonIO.usernameAndPasswordMatch("NonexistentUser", "somePassword"));
        assertEquals("Error reading property", exception.getMessage(),
                "Should throw IOException with the specified message");
    }
  /**
   * Delete newly created directories for clean up.
   */
  @AfterAll
  public static void deleteDirectories() {
    try {
      Files.deleteIfExists(Paths.get(JsonIO.getPathToResources()
          + "/default_categories/testCategory.json"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
