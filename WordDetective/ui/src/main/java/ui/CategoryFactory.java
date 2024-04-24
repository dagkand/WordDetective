package ui;

import javafx.util.Callback;

public final class CategoryFactory implements Callback<Class<?>, Object> {

    /**
     * The user to store.
     */
    private final String username;

    /**
     * Constructor constructing the factory object.
     *
     * @param usernameParameter - the user to store for the game
     */
    public CategoryFactory(final String usernameParameter) {
        this.username = usernameParameter;
    }

    /**
     * Provides a reference to an instance of CategoryController with the correct
     * username.
     */
    @Override
    public Object call(final Class<?> type) {
        if (type == CategoryController.class) {
            return new CategoryController(username);
        }
        return null;
    }
}
