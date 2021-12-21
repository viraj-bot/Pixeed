module com.example.softablitz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires opencv;
    opens com.example.softablitz to javafx.fxml;
    exports com.example.softablitz;
}