package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public final class LoginController extends AbstractController implements Initializable {
    /**
     * Anchor pane of login.fxml.
     */
    @FXML
    private AnchorPane loginPage;
    /**
     * Label for marking of incorrect password.
     */
    @FXML
    private Label errorDisplay;

    /**
     * FXML component for enabling user to provide username.
     */
    @FXML
    private TextField usernameField;

    /**
     * FXML component for enabling user to provide password.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * FXML buttons providing access to respectively "performLogin" and
     * "registerNewUser".
     */
    @FXML
    private Button login, registerUser, backArrowbtn;

    /**
     * Imageview of backbutton.
     */
    @FXML
    private ImageView backArrowImg;

    /**
     * Api object used for calling backend application.
     */
    private ApiConfig api;

    /**
     * Constructor for LoginController.
     */
    public LoginController() {
        api = new ApiConfig();
    }

    /**
     * Method fired when pressing the "login" button. Loads the category window.
     */
    @FXML
    public void performLogin() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isBlank() || password.isBlank()) {
                displayError("Cannot have blank fields.", errorDisplay);
                return;
            }
            switch (api.performLogin(username, password)) {
                case SUCCESS:
                    changeSceneTo("Category.fxml", login, new CategoryFactory(username));
                    break;
                case USERNAME_DOES_NOT_EXIST:
                    displayError("Username does not exist.", errorDisplay);
                    break;
                case INCORRECT_PASSWORD:
                    displayError("Incorrect password.", errorDisplay);
                    break;
                case READ_ERROR:
                    displayError("Error during extraction of password.", errorDisplay);
                    break;
                default:
                    displayError("Unknown error occured.", errorDisplay);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method fired when pressing the "registerUser" button. Loads the registration
     * window.
     */
    @FXML
    public void registerNewUser() {
        changeSceneTo("Registration.fxml", registerUser);
    }

    /**
     * Getter for api.
     *
     * @param newApi - the api to use.
     */
    public void setApi(final ApiConfig newApi) {
        this.api = newApi;
    }

    /**
     * Change scene back to main page.
     */
    @FXML
    public void backToMainPage() {
        changeSceneTo("App.fxml", backArrowbtn);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        setBackArrowImg(backArrowImg);
        startBGVideo(loginPage);
    }

}
