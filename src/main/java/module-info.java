module AA {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires json.simple;

    exports model;

    exports view;
    opens view to javafx.fxml;
}