module org.ayman.chatapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.ayman.chatapp to javafx.fxml;
    exports org.ayman.chatapp;
}