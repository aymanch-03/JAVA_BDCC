module org.ayman.gestionconsultation {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    opens org.ayman.gestionconsultation to javafx.fxml;
    opens org.ayman.gestionconsultation.controllers to javafx.fxml;
    opens org.ayman.gestionconsultation.entities to javafx.base;

    exports org.ayman.gestionconsultation;
}