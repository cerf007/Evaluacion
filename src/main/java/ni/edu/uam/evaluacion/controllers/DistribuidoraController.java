package ni.edu.uam.evaluacion.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.evaluacion.dao.DistribuidoraDAO;
import ni.edu.uam.evaluacion.interfaces.Crud;
import ni.edu.uam.evaluacion.models.Colaborador;
import ni.edu.uam.evaluacion.utils.AlertaUtil;

import java.time.LocalDate;

public class DistribuidoraController {

    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cmbCargo;
    @FXML private ListView<String> lvArea;
    @FXML private DatePicker dpFecha;
    @FXML private RadioButton rbFijo;
    @FXML private RadioButton rbTemporal;
    @FXML private CheckBox chkSeguro;
    @FXML private CheckBox chkTransporte;
    @FXML private CheckBox chkAlimentacion;

    @FXML private TableView<Colaborador> tablaColaboradores;
    @FXML private TableColumn<Colaborador, String> colNombre;
    @FXML private TableColumn<Colaborador, String> colCargo;
    @FXML private TableColumn<Colaborador, String> colArea;
    @FXML private TableColumn<Colaborador, LocalDate> colFecha;
    @FXML private TableColumn<Colaborador, String> colTipo;
    @FXML private TableColumn<Colaborador, String> colBeneficios;

    private ToggleGroup grupoContrato;
    private Colaborador colaboradorSeleccionado = null;


    private final Crud<Colaborador> dao = new DistribuidoraDAO();

    @FXML
    public void initialize() {
        cmbCargo.getItems().addAll("Gerente", "Supervisor", "Operario", "Asistente");
        lvArea.getItems().addAll("Administración", "Ventas", "Logística", "Recursos Humanos");

        grupoContrato = new ToggleGroup();
        rbFijo.setToggleGroup(grupoContrato);
        rbTemporal.setToggleGroup(grupoContrato);


        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombres() + " " + cell.getValue().getApellidos()));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));


        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(
                (cell.getValue().getTipoContrato() != null && cell.getValue().getTipoContrato()) ? "Fijo" : "Temporal"));

        colBeneficios.setCellValueFactory(cell -> new SimpleStringProperty(
                (cell.getValue().getBeneficios() != null && cell.getValue().getBeneficios()) ? "Sí" : "No"));


        tablaColaboradores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosEnFormulario(newSelection);
            }
        });

        refrescarTabla();
    }

    @FXML
    void registrar() {
        if (validarCampos()) {
            Colaborador nuevo = crearColaboradorDesdeFormulario();
            dao.agregar(nuevo); // Guardamos usando el DAO
            refrescarTabla();
            limpiar();
            AlertaUtil.mostrarInfo("Colaborador registrado exitosamente.");
        }
    }

    @FXML
    void actualizar() {
        if (colaboradorSeleccionado != null && validarCampos()) {
            int index = dao.obtenerRegistros().indexOf(colaboradorSeleccionado);
            if (index >= 0) {
                Colaborador actualizado = crearColaboradorDesdeFormulario();
                dao.actualizar(index, actualizado); // Actualizamos usando el DAO
                refrescarTabla();
                limpiar();
                AlertaUtil.mostrarInfo("Colaborador actualizado.");
            }
        } else if (colaboradorSeleccionado == null) {
            AlertaUtil.mostrarError("Seleccione un colaborador de la tabla para actualizar.");
        }
    }

    @FXML
    void eliminar() {
        if (colaboradorSeleccionado != null) {
            dao.eliminar(colaboradorSeleccionado); // Eliminamos usando el DAO
            refrescarTabla();
            limpiar();
            AlertaUtil.mostrarInfo("Colaborador eliminado.");
        } else {
            AlertaUtil.mostrarError("Seleccione un colaborador para eliminar.");
        }
    }

    @FXML
    void limpiar() {
        txtNombres.clear();
        txtApellidos.clear();
        txtUsuario.clear();
        txtContrasena.clear();
        cmbCargo.getSelectionModel().clearSelection();
        lvArea.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        if (grupoContrato.getSelectedToggle() != null) {
            grupoContrato.getSelectedToggle().setSelected(false);
        }
        chkSeguro.setSelected(false);
        chkTransporte.setSelected(false);
        chkAlimentacion.setSelected(false);
        colaboradorSeleccionado = null;
        tablaColaboradores.getSelectionModel().clearSelection();
    }


    private void refrescarTabla() {
        ObservableList<Colaborador> lista = FXCollections.observableArrayList(dao.obtenerRegistros());
        tablaColaboradores.setItems(lista);
    }

    private boolean validarCampos() {
        if (txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() ||
                txtUsuario.getText().isEmpty() || txtContrasena.getText().isEmpty()) {
            AlertaUtil.mostrarError("Los campos de texto no pueden estar vacíos.");
            return false;
        }
        if (txtUsuario.getText().length() < 5) {
            AlertaUtil.mostrarError("El usuario debe tener al menos 5 caracteres.");
            return false;
        }
        if (txtContrasena.getText().length() < 8) {
            AlertaUtil.mostrarError("La contraseña debe tener al menos 8 caracteres.");
            return false;
        }
        if (cmbCargo.getValue() == null) {
            AlertaUtil.mostrarError("Debe seleccionar un cargo.");
            return false;
        }
        if (lvArea.getSelectionModel().getSelectedItem() == null) {
            AlertaUtil.mostrarError("Debe seleccionar un área de trabajo.");
            return false;
        }
        if (dpFecha.getValue() == null || dpFecha.getValue().isAfter(LocalDate.now())) {
            AlertaUtil.mostrarError("La fecha no puede ser vacía ni posterior a la fecha actual.");
            return false;
        }
        if (grupoContrato.getSelectedToggle() == null) {
            AlertaUtil.mostrarError("Debe seleccionar un tipo de contrato.");
            return false;
        }
        if (!chkSeguro.isSelected() && !chkTransporte.isSelected() && !chkAlimentacion.isSelected()) {
            AlertaUtil.mostrarError("Debe seleccionar al menos un beneficio.");
            return false;
        }
        return true;
    }

    private Colaborador crearColaboradorDesdeFormulario() {

        boolean tipoContratoFijo = rbFijo.isSelected();
        boolean tieneBeneficios = chkSeguro.isSelected() || chkTransporte.isSelected() || chkAlimentacion.isSelected();


        return new Colaborador(
                txtNombres.getText(),
                txtApellidos.getText(),
                cmbCargo.getValue(),
                lvArea.getSelectionModel().getSelectedItem(),
                txtUsuario.getText(),
                txtContrasena.getText(),
                dpFecha.getValue(),
                tipoContratoFijo,
                tieneBeneficios,
                null // pathImagen
        );
    }

    private void cargarDatosEnFormulario(Colaborador col) {
        colaboradorSeleccionado = col;
        txtNombres.setText(col.getNombres());
        txtApellidos.setText(col.getApellidos());
        txtUsuario.setText(col.getUsuario());
        txtContrasena.setText(col.getContrasena());
        cmbCargo.setValue(col.getCargo());
        lvArea.getSelectionModel().select(col.getArea());
        dpFecha.setValue(col.getFechaContratacion());

        if (col.getTipoContrato() != null && col.getTipoContrato()) rbFijo.setSelected(true);
        else rbTemporal.setSelected(true);

        if (col.getBeneficios() != null && col.getBeneficios()) {
            chkSeguro.setSelected(true);
        } else {
            chkSeguro.setSelected(false);
            chkTransporte.setSelected(false);
            chkAlimentacion.setSelected(false);
        }
    }
}