module project.ui {

    requires project.types;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens ui to javafx.graphics, javafx.fxml, javafx.media;
}
