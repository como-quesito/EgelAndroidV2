package campitos.org.egelandroidv2;

import java.util.ArrayList;

/**
 * Created by campitos on 21/08/15.
 */
public class Reactivo {

    String tema;

    String pregunta;
    String retroalimentacion;
    String urlimagen;
    ArrayList<Opcion> opciones;

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }

    public ArrayList<Opcion> getOpciones() {
        return opciones;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }





    public void setOpciones(ArrayList<Opcion> opciones) {
        this.opciones = opciones;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public void setRetroalimentacion(String retroalimentacion) {
        this.retroalimentacion = retroalimentacion;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Reactivo() {
    }
}
