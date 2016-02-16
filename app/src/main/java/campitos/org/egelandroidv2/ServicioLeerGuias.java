/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campitos.org.egelandroidv2;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author campitos
 */

public class ServicioLeerGuias {
  public static  String servicioLeerGuias(Context ctx){
       String mensaje="nada";
       boolean encontrada=false;
           int lineas=0;
           int valorLinea=0;


       String datos="nada";
       try{
           AssetManager manager = ctx.getAssets();
           // URL url = new URL("http://www.weatherlink.com/user/sierraguadalupe/index.php?view=summary&headers=0");
          // HttpURLConnection con = (HttpURLConnection) url.openConnection();
           InputStream input=manager.open("a1.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String inputLine;
        StringBuilder builder=new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
        datos=inputLine;
        lineas++;
        builder.append(inputLine);

        }
           datos=new String(builder);

        }catch(Exception e){

        }

     return datos;
    }






}
