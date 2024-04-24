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
import types.LoginStatus;

public final class LoginControllerTest extends ApplicationTest {

  /**
   * The root of the application is used as reference to the DOM.
   */
  private Parent root;

  /**
   * The loader used for instantiation.
   */
  private FXMLLoader fxmlLoader;

  /**
   * The label used to display error.
   */
  private Label errorDisplay;

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
  private LoginController controller;

  /**
   * Instantiates the stage.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    fxmlLoader = new FXMLLoader(this.getClass().getResource("LoginPage.fxml"));
    root = fxmlLoader.load();
    controller = fxmlLoader.getController();
    controller.setApi(apiMock);
    errorDisplay = (Label) root.lookup("#errorDisplay");
    usernameField = (TextField) root.lookup("#usernameField");
    passwordField = (TextField) root.lookup("#passwordField");
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Tests that a user cannot login if the fields are blank.
   */
  @Test
  public void testBlankFields() {
    clickOn("#login", MouseButton.PRIMARY);
    assertTrue(usernameField.getText().isBlank());
    assertTrue(passwordField.getText().isBlank());
    assertTrue(errorDisplay.getText().contains("blank"),
        "The error display should say something about blank fields, but was:" + errorDisplay.getText());
  }

  /**
   * Tests that a user can not login if wrong username.
   */
  public void testWrongUsername() {
    write("wrongUsername", usernameField);
    write("password", passwordField);

    try {
      when(apiMock.performLogin(usernameField.getText(), passwordField.getText()))
          .thenReturn(LoginStatus.USERNAME_DOES_NOT_EXIST);
      clickOn("#login", MouseButton.PRIMARY);
      assertTrue(errorDisplay.getText().contains("exist"),
          "The error display should say something about username not existing, but was:" + errorDisplay.getText());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

  }

  /**
   * Tests that a user can not login if wrong password.
   */
  public void testWringPassword() {
    write("ExistingUser", usernameField);
    write("WrongPassword", passwordField);

    try {
      when(apiMock.performLogin(usernameField.getText(), passwordField.getText()))
          .thenReturn(LoginStatus.INCORRECT_PASSWORD);
      clickOn("#login", MouseButton.PRIMARY);
      assertTrue(errorDisplay.getText().contains("incorrect"),
          "The error display should say something about incorrect password, but was:" + errorDisplay.getText());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

  }

  /**
   * Tests that a user can login if correct username and password.
   */
  public void testSuccessfulLogin() {
    write("ExistingUser", usernameField);
    write("CorrectPassword", passwordField);

    try {
      when(apiMock.performLogin(usernameField.getText(), passwordField.getText()))
          .thenReturn(LoginStatus.SUCCESS);
      clickOn("#login", MouseButton.PRIMARY);
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
