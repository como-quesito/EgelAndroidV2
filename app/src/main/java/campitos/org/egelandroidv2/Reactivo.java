package campitos.org.egelandroidv2;

import java.util.ArrayList;

/**
 * Created by campitos on 21/08/15.
 */
public class Reactivo {

    String tema;
   String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String pregunta;
    String retroalimentacion;
    String urlimagen;
    boolean validado;
    Integer claveProfesor;

    public boolean isValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    public Integer getClaveProfesor() {
        return claveProfesor;
    }

    public void setClaveProfesor(Integer claveProfesor) {
        this.claveProfesor = claveProfesor;
    }

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
