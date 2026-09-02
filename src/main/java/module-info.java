module ni.edu.uam.evaluacion {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.evaluacion to javafx.fxml;
    exports ni.edu.uam.evaluacion;
    exports ni.edu.uam.evaluacion.applications;
    opens ni.edu.uam.evaluacion.applications to javafx.fxml;
    exports ni.edu.uam.evaluacion.controllers;
    opens ni.edu.uam.evaluacion.controllers to javafx.fxml;
}