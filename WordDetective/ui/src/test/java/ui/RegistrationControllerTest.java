package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import types.RegistrationStatus;

public class RegistrationControllerTest extends ApplicationTest {

  /**
   * The root of the application is used as reference to the DOM.
   */
  private Parent root;
  /**
   * The label used to display error.
   */
  private Label errorDisplay;

  /**
   * The loader used for instantiation.
   */
  private FXMLLoader fxmlLoader;
  /**
   * The textfield used to provide username or password.
   */
  private TextField usernameField, passwordField;

  /**
   * Mock of the api.
   */
  @Mock
  private ApiConfig apiMock = mock(ApiConfig.class);

  /**
   * Controller that injects the mocks.
   */
  @InjectMocks
  private RegistrationController controller = new RegistrationController();

  /**
   * Instantiates the stage.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    fxmlLoader = new FXMLLoader(this.getClass().getResource("Registration.fxml"));
    root = fxmlLoader.load();
    controller = fxmlLoader.getController();
    controller.setApi(apiMock);
    stage.setScene(new Scene(root));
    stage.show();
    errorDisplay = (Label) root.lookup("#errorDisplay");
    usernameField = (TextField) root.lookup("#newUsername");
    passwordField = (TextField) root.lookup("#newPassword");
  }

  /**
   * Tests that a user can not be created if the fields are blank.
   */
  @Test
  public void testBlankFields() {
    clickOn("#signUp", MouseButton.PRIMARY);
    assertTrue(usernameField.getText().isBlank());
    assertTrue(passwordField.getText().isBlank());
    assertTrue(errorDisplay.getText().contains("blank"),
        "The error display should say something about blank fields, but was:" + errorDisplay.getText());
  }

  /**
   * Tests that a user can not be created if the username is taken.
   */
  @Test
  public void testUsernameTaken() {
    write("Test", usernameField);
    write("Password", passwordField);
    try {
      when(apiMock.registrationResult(usernameField.getText(), passwordField.getText()))
          .thenReturn(RegistrationStatus.USERNAME_TAKEN);
      clickOn("#signUp", MouseButton.PRIMARY);
      assertTrue(errorDisplay.getText().contains("taken"),
          "The error display should say something about username taken, but was:" + errorDisplay.getText());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests correct error message on wrong username.
   */
  @Test
  public void testWrongUsername() {
    write("I", usernameField);
    write("Password123", passwordField);
    try {
      when(apiMock.registrationResult(usernameField.getText(), passwordField.getText()))
          .thenReturn(RegistrationStatus.USERNAME_NOT_MATCH_REGEX);
      clickOn("#signUp", MouseButton.PRIMARY);
      assertTrue(errorDisplay.getText().contains("username needs"),
          "The error display should say something about username being wrong, but was:" + errorDisplay.getText());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests that a user can be created if the username is not taken.
   */
  @Test
  public void testWrongPassword() {
    write("ValidUsername", usernameField);
    write("wrongpassword", passwordField);
    try {
      when(apiMock.registrationResult(usernameField.getText(), passwordField.getText()))
          .thenReturn(RegistrationStatus.PASSWORD_NOT_MATCH_REGEX);

      clickOn("#signUp", MouseButton.PRIMARY);
      assertTrue(errorDisplay.getText().contains("password needs"),
          "The error display should say something about password being wrong, but was:" + errorDisplay.getText());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests that a user can be created if everything is correct.
   */
  @Test
  public void testCreateNewUser() {
    write("ValidUsername", usernameField);
    write("Validpwd123", passwordField);
    try {
      when(apiMock.registrationResult(usernameField.getText(), passwordField.getText()))
          .thenReturn(RegistrationStatus.SUCCESS);
      clickOn("#signUp", MouseButton.PRIMARY);

      assertEquals(0, errorDisplay.getOpacity());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * write a string to a textfield instant.
   *
   * @param string    - the string to write
   * @param textfield - the textfield to write to
   *
   */
  private void write(final String string, final TextField textfield) {
    textfield.setText(string);
  }

}
