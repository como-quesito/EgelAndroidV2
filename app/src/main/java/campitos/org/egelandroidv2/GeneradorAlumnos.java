package campitos.org.egelandroidv2;

import java.util.ArrayList;

/**
 * Created by rapid on 11/07/2016.
 */
public class GeneradorAlumnos {
    public static ArrayList<Alumno> alumnos;

    public GeneradorAlumnos() {

    }

    public static ArrayList<Alumno> getTodos(){
        alumnos=new ArrayList<>();
        Alumno a1=new Alumno(10171298,"DANIEL AITZIN CASTELAZO");
        Alumno a2=new Alumno(13636571,"BRENDA CRUZ RAMIREZ");
        Alumno a3=new Alumno(9043943,"ALEJANDRO RODRIGUEZ GUALITO");
        Alumno a4=new Alumno(9142216,"MARTÍN DANIEL JIMENEZ ARTEAGA");
        Alumno a5=new Alumno(11229005,"JESSICA ELIZABETH HERNÁNDEZ AXOTLA");
        Alumno a6=new Alumno(12360966,"CLAUDIO BALBUENA SAMANO");
        Alumno a7=new Alumno(12390351,"LUIS FRANCISCO PINEA GARCIA");
        Alumno a8=new Alumno(12432617,"JOSE ALBERTO TREJO PÉREZ");
        Alumno a9=new Alumno(12454041,"REYNA SANTOS MORENO");
        Alumno a10=new Alumno(12528356,"RICARDO MENDOZA REYES");
        Alumno a11=new Alumno(13619262,"BRANDON MOISES HERNÁNDEZ MARTÍNEZ");
        Alumno a12=new Alumno(13645952,"CRISTIAN JESÚS VÁZQUEZ SALINAS");
        Alumno a13=new Alumno(13777292,"ALBERTO JARAMILLO CABRERA");
        Alumno a14=new Alumno(13792382,"FRANCISCO JAVIER MUÑIZ MARQUEZ");
        Alumno a15=new Alumno(33868,"Juan Carlos Campos");

        alumnos.add(a1);alumnos.add(a2);alumnos.add(a3);alumnos.add(a4);alumnos.add(a5);alumnos.add(a6);alumnos.add(a7);alumnos.add(a8);
        alumnos.add(a9);alumnos.add(a10);alumnos.add(a11);alumnos.add(a12);alumnos.add(a13); alumnos.add(a14);alumnos.add(a15);
return alumnos;
    }

    public static boolean isNip(int miNip){
        boolean autenticado=false;
       for( Alumno a:getTodos()){
            if(a.getNip()==miNip){
              autenticado=true;
                break;
            }
        }
        return autenticado;
    }
}
