package ni.edu.uam.evaluacion.dao;

import ni.edu.uam.evaluacion.interfaces.Crud;
import ni.edu.uam.evaluacion.models.Distribuidora;

import java.util.ArrayList;
import java.util.List;

public class DistribuidoraDAO implements Crud<Distribuidora> {

    private static final List<Distribuidora> DISTRIBUIDORAS = new ArrayList<>();


    @Override
    public void agregar(Distribuidora entidad) {
        DISTRIBUIDORAS.add(entidad);
    }

    @Override
    public List<Distribuidora> obtenerRegistros() {
        return DISTRIBUIDORAS;
    }
    
}
