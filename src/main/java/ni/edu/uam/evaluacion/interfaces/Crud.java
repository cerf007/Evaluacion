package ni.edu.uam.evaluacion.interfaces;

import java.util.List;

public interface Crud <T>{
    public void  agregar(T entidad);

    public List<T> obtenerRegistros();


}
