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

public final class RegistrationController extends AbstractController implements Initializable {

    /**
     * Anchorpane of page.
     */
    @FXML
    private AnchorPane registrationPage;
    /**
     * FXML component used to display error.
     */
    @FXML
    private Label errorDisplay;

    /**
     * FXML component used for providing new username.
     */
    @FXML
    private TextField newUsername;

    /**
     * FXML component for providing new password.
     */
    @FXML
    private PasswordField newPassword;

    /**
     * FXML button used for signing up.
     * FXML button for backArrow
     */
    @FXML
    private Button signUp, backArrowbtn;

    /**
     * ImageView of back arrow.
     */
    @FXML
    private ImageView backArrowImg;

    /**
     * Api object used for calling backend application.
     */
    private ApiConfig api;

    /**
     * Constructor for RegistrationController.
     */
    public RegistrationController() {
        api = new ApiConfig();
    }

    /**
     * Method fired when "signUp" is pressed. Launches category selection if
     * username is not taken.
     *
     */
    public void fireSignUp() {
        try {
            String username = newUsername.getText();
            String password = newPassword.getText();
            if (username.isBlank() || password.isBlank()) {
                displayError("Cannot have blank fields.", errorDisplay);
                return;
            }
            switch (api.registrationResult(username, password)) {
                case SUCCESS:
                    changeSceneTo("Category.fxml", signUp, new CategoryFactory(username));
                    break;
                case USERNAME_TAKEN:
                    displayError("The username \"" + username + "\" is already taken.", errorDisplay);
                    break;
                case USERNAME_NOT_MATCH_REGEX:
                    displayError("The username needs to be minimum 2 characters and not be 'guest'", errorDisplay);
                    break;
                case PASSWORD_NOT_MATCH_REGEX:
                    displayError("The password needs to be more then 4 characters, contain at least 1 number,"
                            + " 1 lowercase letter and 1 uppercase letter", errorDisplay);
                    break;
                case UPLOAD_ERROR:
                    displayError("Error during instantiation of new user.", errorDisplay);
                    break;
                default:
                    displayError("Unknown error occured.", errorDisplay);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the api object.
     *
     * @param newApi - The api object to set.
     */
    public void setApi(final ApiConfig newApi) {
        this.api = newApi;
    }

    /**
     * Change scene back to login page.
     */
    public void toLoginPage() {
        changeSceneTo("LoginPage.fxml", backArrowbtn);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        setBackArrowImg(backArrowImg);
        startBGVideo(registrationPage);
    }

}
