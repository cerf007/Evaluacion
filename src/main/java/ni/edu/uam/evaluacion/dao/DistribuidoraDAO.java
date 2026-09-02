package ni.edu.uam.evaluacion.dao;

import ni.edu.uam.evaluacion.interfaces.Crud;
import ni.edu.uam.evaluacion.models.Colaborador;

import java.util.ArrayList;
import java.util.List;

public class DistribuidoraDAO implements Crud<Colaborador> {

    private static final List<Colaborador> COLABORADORS = new ArrayList<>();


    @Override
    public void agregar(Colaborador entidad) {
        COLABORADORS.add(entidad);
    }

    @Override
    public List<Colaborador> obtenerRegistros() {
        return COLABORADORS;
    }

    @Override
    public void actualizar(int index, Colaborador entidad) {

    }

    @Override
    public void eliminar(Colaborador entidad) {

    }

}
