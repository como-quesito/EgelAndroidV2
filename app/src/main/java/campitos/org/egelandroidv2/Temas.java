package campitos.org.egelandroidv2;

import java.util.ArrayList;

/**
 * Created by campitos on 18/01/16.
 */
public class Temas {
    public final static String A1="A1";
    public final static String A2="A2";

    public final static String B1="B1";
    public final static String B2="B2";
    public final static String B3="B3";
    public final static String B4="B4";

    public final static String C1="C1";
    public final static String C2="C2";

    public final static String D1="D1";
    public final static String D2="D2";
    public final static String D3="D3";

   public static  ArrayList<Tema> temas;
/*
inicializamos todos los temas en el array
 */
    public Temas(){
temas=new ArrayList<Tema>();
        Tema a1=new Tema(A1,"Diagnóstico del problema");
        temas.add(a1);
        Tema a2=new Tema(A2,"Modelado de los requerimientos");
        temas.add(a2);
        Tema b1=new Tema(B1,"Diseño de la solución del problema de TI");
        temas.add(b1);
        Tema b2=new Tema(B2,"Desarrollo de sistemas");
        temas.add(b2);
        Tema b3=new Tema(B3,"Implantación de sistemas");
        temas.add(b3);
        Tema b4=new Tema(B4,"Aplicación de modelos matemáticos");
        temas.add(b4);
        Tema c1=new Tema(C1,"Administración de proyectos de TI");
        temas.add(c1);
        Tema c2=new Tema(C2,"Control de calidad de proyectos de TI");
        temas.add(c2);
        Tema d1=new Tema(D1,"Gestión de redes de datos");
        temas.add(d1);
        Tema d2=new Tema(D2,"Gestión de bases de datos");
        temas.add(d2);
        Tema d3=new Tema(D3,"Gestión de sistemas operativos o lenguajes de prog.");
        temas.add(d2);

    }

    public ArrayList<Tema> obtenerTemas(){
        return temas;
    }

}


