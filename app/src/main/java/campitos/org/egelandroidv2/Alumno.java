package campitos.org.egelandroidv2;

import java.util.Date;

/**
 * Created by rapid on 11/07/2016.
 */
public class Alumno {
    private int nip;
    private String nombre;
    private Date fecha;

    public Alumno() {
    }

    public Alumno(int nip, String nombre) {
        this.nip = nip;
        this.nombre = nombre;
    }

    public int getNip() {
        return nip;
    }

    public void setNip(int nip) {
        this.nip = nip;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
