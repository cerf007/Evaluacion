module ni.edu.uam.evaluacion {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.evaluacion to javafx.fxml;
    exports ni.edu.uam.evaluacion;
}