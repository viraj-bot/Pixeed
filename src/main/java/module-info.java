module com.example.softablitz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.softablitz to javafx.fxml;
    exports com.example.softablitz;
}