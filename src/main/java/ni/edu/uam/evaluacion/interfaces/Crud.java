package ni.edu.uam.evaluacion.interfaces;

import java.util.List;

public interface Crud<T> {
    void agregar(T entidad);
    List<T> obtenerRegistros();
    void actualizar(int index, T entidad);
    void eliminar(T entidad);
}