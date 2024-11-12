module com.example.oop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens oop to javafx.fxml;
    exports oop;
    exports security;
    opens security to javafx.fxml;
}