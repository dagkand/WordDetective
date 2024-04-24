package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * An abstract parent class for controllers containing the common methods for
 * controllers.
 */
public abstract class AbstractController {
  /**
   * The controls of the video playing in bg.
   */
  private MediaPlayer videoMP;
  /**
   * The constrols of the music in application.
   */
  private MediaPlayer soundMP;

  /**
   * Change the scene to a new fxml scene.
   *
   * @param scene         - The fxml scene to change to. For example
   *                      {@code App.fxml}
   * @param buttonPressed - The button that is clicked when you want to switch
   *                      scene
   */
  public void changeSceneTo(final String scene, final Button buttonPressed) {
    try {
      if (soundMP != null) {
        soundMP.stop();
        videoMP.stop();
      }
      FXMLLoader fxmlLoader = new FXMLLoader(AbstractController.class.getResource(scene));
      Stage stage = (Stage) buttonPressed.getScene().getWindow();
      Parent parent = fxmlLoader.load();
      stage.setScene(new Scene(parent));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the scene to a new fxml scene.
   *
   * @param scene         - The fxml scene to change to. For example
   *                      {@code App.fxml}
   * @param buttonPressed - The button that is clicked when you want to switch
   *                      scene
   * @param factory       - The factory to use if you need to pass parameters
   *                      between scenes
   */
  public void changeSceneTo(
      final String scene,
      final Button buttonPressed,
      final Callback<Class<?>, Object> factory) {
    try {
      if (soundMP != null) {
        soundMP.stop();
      }
      FXMLLoader fxmlLoader = new FXMLLoader(AbstractController.class.getResource(scene));
      fxmlLoader.setControllerFactory(factory);
      Stage stage = (Stage) buttonPressed.getScene().getWindow();
      Parent parent = fxmlLoader.load();
      stage.setScene(new Scene(parent));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Start the background video on your application.
   *
   * @param background - The {@link AnchorPane} to add the video to
   *
   */
  public void startBGVideo(final AnchorPane background) {
    File video = new File(Paths.get("runtime/assets").toAbsolutePath()
        + "/video/WordDetectiveBackgroundVideo.mp4");
    File sound = new File(Paths.get("runtime/assets").toAbsolutePath()
        + "/music/WordDetectiveMusic.L.wav");
    videoMP = new MediaPlayer(new Media(video.toURI().toString()));
    soundMP = new MediaPlayer(new Media(sound.toURI().toString()));
    MediaView videoMV = new MediaView(videoMP);
    MediaView soundMV = new MediaView(soundMP);

    DoubleProperty mvw = videoMV.fitWidthProperty();
    DoubleProperty mvh = videoMV.fitHeightProperty();

    mvw.bind(Bindings.selectDouble(videoMV.sceneProperty(), "width"));
    mvh.bind(Bindings.selectDouble(videoMV.sceneProperty(), "height"));
    videoMV.setPreserveRatio(false);

    background.getChildren().add(0, soundMV);
    background.getChildren().add(0, videoMV);
    videoMP.setCycleCount(Timeline.INDEFINITE);
    soundMP.setCycleCount(Timeline.INDEFINITE);
    videoMP.play();
    soundMP.play();
    videoMP.seek(null);
  }

  /**
   * Render in the image of the back arrow.
   *
   * @param imageview - The {@link ImageView} to place the back arrow image in
   */
  public void setBackArrowImg(final ImageView imageview) {
    Image backArrow;
    try {
      backArrow = new Image(
          new FileInputStream(new File(Paths.get("runtime/assets").toAbsolutePath() + "/images/backArrow.png")));
    } catch (Exception e) {
      backArrow = null;
      System.out.println("Couldn't find image because: " + e.getMessage());
    }
    imageview.setImage(backArrow);
  }

  /**
   * Display an error message.
   *
   * @param message      - The message to display
   * @param errorDisplay - The {@link
   *
   *
   *
   *                     Label} to display the message in
   */
  public void displayError(final String message, final Label errorDisplay) {
    errorDisplay.setStyle("-fx-text-fill: #b51c1c;");
    errorDisplay.setText(message);
    errorDisplay.setOpacity(1);
  }
}
