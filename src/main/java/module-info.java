module com.example.portatest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.portatest to javafx.fxml;
    exports com.example.portatest;
}