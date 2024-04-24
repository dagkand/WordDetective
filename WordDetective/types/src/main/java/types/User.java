package types;

import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public final class User {

    /**
     * The username of the given player.
     */
    @SerializedName("username")
    private String name;

    /**
     * The password of the given player.
     */
    @SerializedName("password")
    private String pwd;

    /**
     * The highscore of the given player.
     */
    @SerializedName("highscore")
    private int highscore;

    /**
     * The user's custom categories.
     */
    @SerializedName("customCategories")
    private HashMap<String, List<String>> customCategories;

    /**
     * Constructor setting up a new User object to be injected into correlating.
     * json file.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     */
    public User(final String username, final String password) {
        this.highscore = 0;
        this.customCategories = new HashMap<String, List<String>>();
        this.name = username;
        this.pwd = password;
    }

    /**
     * Empty constructor for setting up a User as guest.
     */
    public User() {
        this.name = "guest";
    }

    /**
     * Get user's highscore.
     *
     * @return The highscore of the given user as an Integer.
     */
    public int getHighScore() {
        return highscore;
    }

    /**
     * Get user's username.
     *
     * @return The username of the given user as String.
     */
    public String getUsername() {
        return name;
    }

    /**
     * Get user's password.
     *
     * @return The password of the given user as a string.
     */
    public String getPassword() {
        return pwd;
    }

    /**
     * Set the user's custom categories.
     *
     * @param newCustomCategories - a HashMap with all the categories
     */
    public void setCustomCategories(final HashMap<String, List<String>> newCustomCategories) {
        this.customCategories = newCustomCategories;
    }

    /**
     * Get user's custom categories.
     *
     * @return a HashMap containing the user's custom categories
     */
    public HashMap<String, List<String>> getCustomCategories() {
        return customCategories;
    }

    /**
     * Set the user's highscore.
     *
     * @param score - The highscore to set
     */
    public void setHighscore(final int score) {
        this.highscore = score;
    }

    /**
     * Add a custom category to user.
     *
     * @param categoryName   - Name of category
     * @param customCategory - List of all answers in category
     */
    public void addCustomCategories(final String categoryName, final List<String> customCategory) {
        this.customCategories.put(categoryName, customCategory);
    }

    /**
     * Delete a custom category from user's custom categories.
     *
     * @param category - The category to delete
     */
    public void deleteCustomCategories(final String category) {
        if (this.customCategories.remove(category) == null) {
            throw new RuntimeException("Category" + category + "does not exist");
        }
    }

}
