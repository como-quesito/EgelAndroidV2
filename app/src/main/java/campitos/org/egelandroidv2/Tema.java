package campitos.org.egelandroidv2;

/**
 * Created by campitos on 18/01/16.
 */
public class Tema{
    String area;
    String titulo;

    public Tema(String area, String titulo) {
        this.area = area;
        this.titulo = titulo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
