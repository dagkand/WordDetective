package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryControllerTest extends ApplicationTest {

    /**
     * The root of the application is used as reference to the DOM.
     */
    private Parent root;

    /**
     * Vbox containing categories.
     */
    @FXML
    private VBox vbox;

    /**
     * Test hashmap of categories.
     */
    private HashMap<String, Set<String>> categories = new HashMap<>();
    /**
     * Mock of the api.
     */
    @Mock
    private ApiConfig apiMock = mock(ApiConfig.class);

    /**
     * Controller that injects the mocks.
     */
    @InjectMocks
    private CategoryController controller;

    /**
     * The label used to display error.
     */
    private Label errorDisplay;

    /**
     * Button to upload custom categories.
     */
    private Button uploadButton;

    /**
     * Pane to upload custom categories.
     */
    private Pane uploadPane;

    /**
     * Instantiates the stage.
     */
    @Override
    public void start(final Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Category.fxml"));
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setApi(apiMock);
        stage.setScene(new Scene(root));
        stage.show();
        uploadButton = (Button) root.lookup("#showCustomCatBtn");
        uploadPane = (Pane) root.lookup("#addCategoryPane");
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertDoesNotThrow(() -> new CategoryController());
        assertDoesNotThrow(() -> new CategoryController("guest"));
        assertThrows(NullPointerException.class, () -> new CategoryController(null));
    }

    /**
     * Test closing explanation.
     */
    @Test
    public void testCloseExplanation() {
        clickOn("#categoryInformationButton");
        assertFalse(root.lookup("#categoryInformationPane").isVisible());
    }

    /**
     * Test showing upload.
     */
    @Test
    public void testShowUpload() {
        uploadButton.setVisible(true);
        assertFalse(uploadPane.isVisible());
        clickOn(uploadButton);
        assertTrue(uploadPane.isVisible());

    }

    /**
     * Test rendering categories.
     */
    @Test
    public void testRenderCatgories() {
        vbox = (VBox) root.lookup("#vbox");
        try {
            when(apiMock.getCategories("guest")).thenReturn(categories);
            assertEquals(1, vbox.getChildren().size(),
                    "The number of categories should be 1 not " + vbox.getChildren().size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test uploading categories.
     */
    @Test
    public void testUploadCategory() {
        errorDisplay = (Label) root.lookup("#uploadErrorDisplay");
        uploadButton.setVisible(true);
        clickOn(uploadButton);
        Button actuallyUpload = (Button) root.lookup("#upload");
        TextArea nameArea = (TextArea) root.lookup("#categoryName");
        TextArea wordsArea = (TextArea) root.lookup("#categoryWords");
        clickOn(actuallyUpload);
        assertTrue(errorDisplay.getText().contains("blank fields"),
                "The error display should say something about blank fields, but was:" + errorDisplay.getText());

        clickOn(nameArea);
        write("Valid");
        clickOn(actuallyUpload);
        assertTrue(errorDisplay.getText().contains("blank fields"),
                "The error display should say something about blank fields, but was:" + errorDisplay.getText());

        clickOn(wordsArea);
        write("notvalidformat");
        clickOn(actuallyUpload);
        assertTrue(errorDisplay.getText().contains("format"),
                "The error display should say something about wrong format, but was:" + errorDisplay.getText());

        wordsArea.clear();
        clickOn(wordsArea);
        write("Valid,Words,Here");
        try {
            doNothing().when(apiMock).addCustomCategory(Mockito.any(String.class), Mockito.any(String[].class));
            clickOn(actuallyUpload);
            assertFalse(uploadPane.isVisible());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
