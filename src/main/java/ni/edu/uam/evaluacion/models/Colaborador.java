package ni.edu.uam.evaluacion.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Colaborador {
    private String nombres;
    private String apellidos;
    private String cargo;
    private String area;
    private String usuario;
    private String contrasena;
    private LocalDate fechaContratacion;
    private Boolean tipoContrato;
    private Boolean beneficios;
    private String pathImagen;
}