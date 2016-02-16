package campitos.org.egelandroidv2;

import java.util.ArrayList;

/**
 * Created by campitos on 2/09/15.
 */
public class Profesor extends Usuario {


    ArrayList<Reactivo> reactivos;

    public ArrayList<Reactivo> getReactivos() {
        return reactivos;
    }


    public void setReactivos(ArrayList<Reactivo> reactivos) {
        this.reactivos = reactivos;
    }

    public Profesor() {
    }
}
