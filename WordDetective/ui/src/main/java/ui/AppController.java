package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public final class AppController extends AbstractController implements Initializable {

    /**
     * The root pane of the App.fxml.
     */
    @FXML
    private AnchorPane mainPage;

    /**
     * Buttons on the Front,Login page.
     */
    @FXML
    private Button appLogInBtn, appGuestBtn, undoButton;

    /**
     * Textfield on the Front,Login page.
     */
    @FXML
    private TextField usernameTF, passwordTF;

    /**
     * Reference to the given loader.
     */
    @FXML
    private FXMLLoader fxmlLoader;

    /**
     * Send user to Login page.
     */
    @FXML
    public void toLogIn() {
        changeSceneTo("LoginPage.fxml", appLogInBtn);
    }

    /**
     * Send user to Category selection as guest.
     */
    @FXML
    void toSelectCategory() {
        changeSceneTo("Category.fxml", appGuestBtn, new CategoryFactory("guest"));
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        startBGVideo(mainPage);
    }

}
