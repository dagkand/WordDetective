package persistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import types.User;

/**
 * This class is responsible for reading the wordlists from the files.
 * It also provides methods for querying the available categories.
 */
public final class JsonIO implements AbstractJsonIO {

    /**
     * Object used to represent the state of the current user's persistent json
     * file.
     */
    private User user;

    /**
     * Gson object user for seralizastion/deserialazation.
     */
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Static instance of path to resources to use with static methods.
     */
    private static String pathToResources = getAbsolutePathAsString();

    /**
     * Set containing the default categories, which are shared amongst all users.
     */
    private static final Set<String> DEFAULT_CATEGORY_NAMES = getPersistentFilenames("/default_categories");

    /**
     * Set containing teh custom categories, which are unique to each user.
     */
    private Set<String> customCategoryNames;

    /**
     * Type of a {@link List} of strings used for deserialitzing in gson.
     */
    private static final Type LIST_OF_STRINGS_TYPE = new TypeToken<List<String>>() {
    }.getType();

    /**
     * Constructor for instantiating the JsonIO class, which handles file related
     * tasks for an individual user.
     *
     * @param username
     */
    public JsonIO(final String username) {
        this.user = username.equals("guest") ? null : getUser(username);
        this.customCategoryNames = user != null ? user.getCustomCategories().keySet() : new HashSet<>();
    }

    @Override
    public HashMap<String, Set<String>> getAllCategories() {
        HashMap<String, Set<String>> categories = new HashMap<>();
        categories.put("default", DEFAULT_CATEGORY_NAMES);
        categories.put("custom", customCategoryNames);
        return categories;
    }

    @Override
    public <T> T getUserProperty(final Function<User, T> function) {
        return function.apply(user);
    }

    @Override
    public void updateCurrentUser(final Predicate<User> predicate) throws IOException {
        String userPath = pathToResources + "/users/" + user.getUsername() + ".json";
        if (predicate.test(user)) {
            if (new File(userPath).exists()) {
                FileWriter fw = new FileWriter(userPath, StandardCharsets.UTF_8);
                gson.toJson(user, fw);
                fw.close();
            } else {
                throw new IOException("User not found in " + pathToResources);
            }
        } else {
            throw new IOException("Error updating user");
        }
    }

    @Override
    public List<String> getCategoryWordlist(final String category) throws IOException, RuntimeException {
        if (DEFAULT_CATEGORY_NAMES.contains(category)) {
            return getDefaultCategory(category);
        }
        if (user != null && customCategoryNames.contains(category)) {
            return user.getCustomCategories().get(category);
        }
        throw new RuntimeException("Error fetching categories");
    }

    // STATIC UTILITY METHODS

    /**
     * Provides absolute path to current working directory.
     * Implemented in this fashion due to path differences in working files and test
     * files.
     *
     * @return absolute path to current working directory as {@link String}.
     */
    public static String getAbsolutePathAsString() {
        Path absolutePath = Paths.get("").toAbsolutePath();
        while (!absolutePath.endsWith("gr2325")) {
            absolutePath = absolutePath.getParent();
            if (absolutePath == null) {
                throw new IllegalStateException("Working directory not found.");
            }
        }
        return absolutePath + "/WordDetective/persistence/src/main/resources";
    }

    /**
     * Accessor method for the string holding the path.
     *
     * @return The string holding the path.
     */
    public static String getPathToResources() {
        return pathToResources;
    }

    /**
     * Retrieves all registered usernames.
     *
     * @return All registered usernames.
     */
    public static Collection<String> getAllUsernames() {
        return getPersistentFilenames("/users");
    }

    /**
     * Provides an object representing the state of a given user's persistent json
     * file.
     *
     * @param username The username of the user to represent.
     * @return A User object correlating to the state of the provided user's json
     *         file.
     */
    public static User getUser(final String username) {
        try {
            String jsonString = Files.readString(Paths.get(pathToResources + "/users/" + username + ".json"));
            return gson.fromJson(jsonString, User.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Attempts to persistently add a new user.
     *
     * @param user The user of which to persistently add.
     * @return Boolean indicating if the user was added successfully.
     */
    public static boolean addUser(final User user) throws IllegalArgumentException {
        if (getAllUsernames().contains(user.getUsername())) {
            throw new IllegalArgumentException("User already exists.");
        }
        try (FileWriter fw = new FileWriter(pathToResources + "/users/" + user.getUsername() + ".json",
                StandardCharsets.UTF_8)) {
            gson.toJson(user, fw);
            System.out.println("User " + user.getUsername() + " successfully created.");
            return true;
        } catch (IOException e) {
            System.out.println("Couldn't add user " + user.getUsername() + " because: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a user by removing the user's persistent json file.
     *
     * @param username The username of the user to delete.
     * @return A boolean indicating if the deletion was successful.
     */
    public static boolean deleteUser(final String username) {
        File userDel = new File(pathToResources + "/users/" + username + ".json");
        if (userDel.delete()) {
            System.out.println(username + " deleted successfully");
            return true;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't delete user because user does not exits or user has security measures that prevent deletion");
        }
    }

    /**
     * Checks if the provided username is registered with the provided password.
     *
     * @param username The username of which to check the password of.
     * @param password The password of which to check against the sotred password of
     *                 the username.
     * @return Boolean indicating if the username's stored password matches with the
     *         provided password.
     * @throws IOException If any issues are encountered during interaction with the
     *                     files.
     */
    public static boolean usernameAndPasswordMatch(final String username, final String password) throws IOException {
        String storedPassword;
        try {
            storedPassword = getPersistentProperty("password", pathToResources + "/users/" + username + ".json");
            if (storedPassword.equals(password)) {
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new IOException("Error reading property", e);
        }
    }

    /**
     * Retrieves the words belonging to the porvided category.
     *
     * @param category The category to retrieve the words of.
     * @return List of all words in the category.
     * @throws IOException If any issues are encountered during interaction with the
     *                     files.
     */
    public static List<String> getDefaultCategory(final String category) throws IOException {
        try {
            String answers = Files.readString(
                    Paths.get(pathToResources + "/default_categories/"
                            + category.replace(" ", "_") + ".json"));
            return gson.fromJson(answers, LIST_OF_STRINGS_TYPE);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't get category " + category + " because: " + e.getMessage());
        }
    }

    /**
     * Turns user-friendly category format into the format used for the files.
     *
     * @param category The category to access the file of.
     * @return The textual representation of the file's name.
     */
    public static String getCategoryFilename(final String category) {
        return "/" + category.replace(" ", "_") + ".json";
    }

    /**
     * Fetches a specific property from an specified file without the need to load
     * in all the file's content.
     *
     * @param propertyName The property to obtain the value of.
     * @param location     The file's absolute path location.
     * @return String representation of the requested value.
     * @throws IOException If any issues are encountered during interaction with the
     *                     files.
     */
    public static String getPersistentProperty(final String propertyName, final String location) throws IOException {
        try (JsonReader reader = new JsonReader(new FileReader(location, StandardCharsets.UTF_8))) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals(propertyName)) {
                    return reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            throw new IOException("Error reading property", e);
        }
        throw new IOException("Property not found");
    }

    /**
     * Provides the names of all files in a given directory.
     *
     * @param endpoint The final filepath to the directory of which to find the
     *                 filenames of.
     * @return Set<String> with all filenames in the directory,
     *         where the ".json" file extension removed, and all underscores
     *         converted to spaces.
     * @throws RuntimeException If the provided endpoint is not accessible.
     */
    public static Set<String> getPersistentFilenames(final String endpoint) throws RuntimeException {
        File[] nameFiles = new File(pathToResources + endpoint).listFiles();
        if (nameFiles != null) {
            Set<String> res = Arrays.stream(nameFiles).map(file -> {
                String name = file.getName();
                String stripJson = name.replace(".json", "");
                String formatSpace = stripJson.replace("_", " ");
                return formatSpace;
            }).collect(Collectors.toSet());
            return res;
        } else {
            throw new RuntimeException("Directory not present in " + pathToResources);
        }
    }
}
