package ui;

// import java.io.IOException;
// import org.junit.BeforeClass;
import org.testfx.framework.junit5.ApplicationTest;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.stage.Stage;

public class AppControllerTest extends ApplicationTest {

  // /**
  // * The root of the application is used as reference to the DOM.
  // */
  // private Parent root;

  // /**
  // * Reference to the programs loader.
  // */
  // private FXMLLoader fxmlLoader;

  // /**
  // * Properties t0 get the gitlab CI pipeline to run headless for Integration
  // * tests.
  // */
  // @BeforeClass
  // public static void headless() {
  // System.setProperty("prism.verbose", "true");
  // System.setProperty("java.awt.headless", "true");
  // System.setProperty("testfx.robot", "glass");
  // System.setProperty("testfx.headless", "true");
  // System.setProperty("glass.platform", "Monocle");
  // System.setProperty("monocle.platform", "Headless");
  // System.setProperty("prism.order", "sw");
  // System.setProperty("prism.text", "t2k");
  // System.setProperty("testfx.setup.timeout", "2500");
  // }

  // /**
  // * Instantiates the stage.
  // */
  // @Override
  // public void start(final Stage stage) throws IOException {
  // fxmlLoader = new FXMLLoader(this.getClass().getResource("App.fxml"));
  // root = fxmlLoader.load();
  // stage.setScene(new Scene(root));
  // stage.show();
  // }

  // /**
  // * Tests correct load of category page for guest users.
  // */
  // @Test
  // public void isGuest() {
  // clickOn("#appGuestBtn", MouseButton.PRIMARY);
  // Node elementPresentInCategory = lookup("#categoryPage").query();
  // Assert.assertNotNull(elementPresentInCategory);
  // }

  // /**
  // * Tests correct load of loginpage.
  // */
  // @Test
  // public void isRegisteredUser() {
  // clickOn("#appLogInBtn", MouseButton.PRIMARY);
  // Node elementPresentInCategory = lookup("#loginPage").query();
  // Assert.assertNotNull(elementPresentInCategory);
  // }

}
