package campitos.org.egelandroidv2;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String url="http://unitec.elasticbeanstalk.com/reactivo";
    public String areaReactivos="";
    Typeface tf;
    ArrayList<Reactivo> reactivos;
    ArrayList<Reactivo> reactivosAleatorios;
    boolean evaluar=false;
static int numeroDeReactivoActual=0;
   static float totalCalificacion;
    Button botonEvaluar;
    Button botonReactivos;
    TextView textoResultadoExamensito;
    public LinearLayout layout_reactivos;
    static float calificacion;

    Reactivo reactivo;
    String respuestaReactivo="";
    String status="No";
    String datosGuias="nada";
    AlertDialog.Builder dialogoCargado;
    Button botonChecarRespuesta;
    AlertDialog dialogito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogoCargado =new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);
        layout_reactivos= (LinearLayout) findViewById(R.id.linear_layout_reactivos);
 botonEvaluar=new Button(getApplicationContext());
 botonReactivos = (Button) findViewById(R.id.cargarReactivos);
 botonChecarRespuesta= (Button) findViewById(R.id.botonChecarRespuesta);
 textoResultadoExamensito= (TextView) findViewById(R.id.textoResultadoExamensito);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


       // setSupportActionBar(toolbar);
        //Personalizamos el titulo de la inicial
       TextView titulo= (TextView) findViewById(R.id.titulo);
        tf=   Typeface.createFromAsset(getAssets(), "RobotoSlab-Light.ttf");
        titulo.setTypeface(tf);


        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       //Despues de mostrar la navegacion ocultamos  y solo mostramos el principal
                     ocultarTodo();
        ScrollView principal= (ScrollView) findViewById(R.id.contenido_principal);
       principal.setVisibility(View.VISIBLE);


        /*
        EVENTO DE CARGADO DE REACTIVO SEGUN EL TEMA SELECCINADO
         */

        //////////////////////////////////////////////////////////////////////////////
        //BOTON EVALUAR
        botonEvaluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocultarTodo();
                evaluar=false;
                numeroDeReactivoActual=0;
               totalCalificacion=(calificacion/5)*10;
                ScrollView principal= (ScrollView) findViewById(R.id.contenido_evaluacion);
                principal.setVisibility(View.VISIBLE);

                botonChecarRespuesta.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(),"Tu calificacion es:"+totalCalificacion,Toast.LENGTH_LONG).show();
                layout_reactivos.removeView(botonEvaluar);
                textoResultadoExamensito.setText("Calificación obtenida "+totalCalificacion);
                //La siguiente es el tema
               // totalCalificacion+"tema:"+areaReactivos+" el nip es "+Constantes.nip

            }
        });

        botonReactivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             RadioGroup grupo= (RadioGroup) findViewById(R.id.radioSeleccionado);
                grupo.clearCheck();

               if(reactivos.size()>=1&& numeroDeReactivoActual<reactivos.size()) {
                   mostrarReactivo(numeroDeReactivoActual);
                   //para evitar que el chamaco vaya a otro reactivo sin contestar este
                   botonReactivos.setEnabled(false);
                   numeroDeReactivoActual++;
                   botonReactivos.setText("Reactivo  " +numeroDeReactivoActual+" de "+reactivos.size() );
               }

            }
        });

        //El siguiente dialoguito es para que cheques alli la respuesta si es correcta o no
       final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //EL BOTON DE EVALUAR REACTIVO MUY IMPORTANTE

        botonChecarRespuesta.setOnClickListener(new View.OnClickListener() {







            @Override
            public void onClick(View v) {

                botonReactivos.setEnabled(true);
//Checamos si el numero de reactivo es == 5 para dar el boton de evaluar

                botonEvaluar.setText("Evaluar");
                if(numeroDeReactivoActual==5){

                   layout_reactivos.addView(botonEvaluar);
                   botonReactivos.setVisibility(View.INVISIBLE);
                    botonChecarRespuesta.setVisibility(View.INVISIBLE);
                    TextView titulo= (TextView) findViewById(R.id.textoTituloPregunta);
                             titulo.setText("");
                    RadioGroup grupo= (RadioGroup) findViewById((R.id.radioSeleccionado));
                    titulo.setVisibility(View.INVISIBLE);
                    grupo.setVisibility(View.INVISIBLE);


                    layout_reactivos.refreshDrawableState();

                }else{
                    layout_reactivos.removeView(botonEvaluar);
                }

                //LOgica de la evaluacion de los reactivos

                RadioGroup tuOpcion = (RadioGroup) findViewById(R.id.radioSeleccionado);
                  int miId=      tuOpcion.getCheckedRadioButtonId();
                System.out.println("El id es...."+miId);
                RadioButton radioSEleccionado = (RadioButton) findViewById(tuOpcion.getCheckedRadioButtonId());
                if (miId!=-1) {
                    String titulo = radioSEleccionado.getText().toString();
                    String elCorrecto = "Nada";



                    for (int i = 0; i < 4; i++) {
                        if (reactivo.getOpciones().get(i).isAcierto()) {

                            elCorrecto = reactivo.getOpciones().get(i).getTitulo();
                        }
                        if (titulo.equals(elCorrecto))
                        {
                            status = "Correcto";
                           // calificacion++;

                        }
                        else status = "Falso";
                    }
                    ///CALIFICACION AQUIIIIIIII
                    if(status.equals("Correcto"))calificacion++;

                    Log.i("RATA",""+calificacion);
                    builder.setMessage(respuestaReactivo)
                            .setTitle(status);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button




                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //Aqui navegamos a la siguiente pregunta limpiamos tambien los radio buttons
                    RadioGroup grupo= (RadioGroup) findViewById(R.id.radioSeleccionado);
                    grupo.clearCheck();
                    if(reactivos.size()>=1&& numeroDeReactivoActual<reactivos.size()) {
                        mostrarReactivo(numeroDeReactivoActual);
                        //para evitar que el chamaco vaya a otro reactivo sin contestar este
                        botonReactivos.setEnabled(false);
                        numeroDeReactivoActual++;

                        botonReactivos.setText("Reactivo  " +numeroDeReactivoActual+" de "+reactivos.size() );
                    }

                    /*
                    if(evaluar){
                      builder.setMessage("Felicidades acabas de terminar tu primer exa-MENSITO").setTitle("Terminado").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                          }
                      }).show();

                    }*/


                }else{
                    builder.setMessage("Debes de seleccionar una opción antes de checar la respuesta")
                            .setTitle("Selecciona una opción");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
        //////////////////////////////////////////////////////
        //// TERMINA BOTONCHECARRESPUESTA
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/***********************************************************
                      NAVEGACION
 ***********************************************************/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.introduccion) {
            // INTRODUCCION
            ocultarTodo();
            evaluar=false;
            numeroDeReactivoActual=0;

            ScrollView principal= (ScrollView) findViewById(R.id.contenido_principal);
            principal.setVisibility(View.VISIBLE);

        } else if (id == R.id.temario) {

                evaluar=false;
                numeroDeReactivoActual=0;
                WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

                WebSettings settings = textoGuias.getSettings();

                settings.setDefaultTextEncodingName("utf-8");
                settings.setAllowContentAccess(true);
                settings.setAllowFileAccess(true);
                settings.setGeolocationEnabled(true);
                settings.setLoadsImagesAutomatically(true);
                //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
                textoGuias.loadUrl("file:///android_asset/temario.html");
                ocultarTodo();
                ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
                principal.setVisibility(View.VISIBLE);


        } else if (id == R.id.reactivos_a1) {
            /*
            invocamos el método de los reactivos
             */
             limpiarScrollViewReactivos();
            evaluar=false;
            calificacion=0;
            layout_reactivos.removeView(botonEvaluar);
            previoABuscarReactivos();
            areaReactivos="A1";
            numeroDeReactivoActual=0;

            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        } else if (id == R.id.reactivos_a2) {
                  /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="A2";
            calificacion=0;
            //Este lo ponemos para evitar que el boton quede hasta abajo junto con el ScrollView para que se ajusyte a como estab,
            // pues de otra maneras si la pregunta es muy grande ya no se veria el boton  VER PREGUNTAS
           limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;

            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();





            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);


        }else if (id == R.id.reactivos_b1) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="B1";
limpiarScrollViewReactivos();
            numeroDeReactivoActual=0;
            evaluar=false;

            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }
        else if (id == R.id.reactivos_b2) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="B2";
limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;
            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }
        else if (id == R.id.reactivos_b3) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="B3";
limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;
            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }
        else if (id == R.id.reactivos_b4) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="B4";

limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;
            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }else if (id == R.id.reactivos_c1) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="C1";
limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;

            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }else if (id == R.id.reactivos_c2) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="C2";
            evaluar=false;
            numeroDeReactivoActual=0;
limpiarScrollViewReactivos();
            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }else if (id == R.id.reactivos_d1) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="D1";
            numeroDeReactivoActual=0;

limpiarScrollViewReactivos();
            evaluar=false;
            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }else if (id == R.id.reactivos_d2) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="D2";
limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;

            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }else if (id == R.id.reactivos_d3) {
            /*
            invocamos el método de los reactivos
             */
            previoABuscarReactivos();
            areaReactivos="D3";
limpiarScrollViewReactivos();
            evaluar=false;
            numeroDeReactivoActual=0;
            TareaAsyncronicaGetReactivosArea area=
                    new TareaAsyncronicaGetReactivosArea();
            area.execute(null, null, null);
            ocultarTodo();
            ocultarOpciones();
            ScrollView layout=(ScrollView)findViewById(R.id.layout_reactivos);
            layout.setVisibility(View.VISIBLE);




        }
        else if(id==R.id.guia_a1){
            evaluar=false;
            //El siguiente aunque es bueno , te vconviene usar el de loadUrl, si no no se ven las imagenes html
           // datosGuias = ServicioLeerGuias.servicioLeerGuias(getApplicationContext());
            //Texto guias con html
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);
            // textoGuias.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
            // textoGuias.setText(Html.fromHtml(datosGuias));
            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
         //         textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
         textoGuias.loadUrl("file:///android_asset/a1.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);
            numeroDeReactivoActual=0;


        }else if(id==R.id.guia_a2){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/a2.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_b1){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/b1.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_b2){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/b2.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_b3){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/b3.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }

        else if(id==R.id.guia_b4){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/b4.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_c1){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/c1.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_c2){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/c2.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_d1){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/d1.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }

        else if(id==R.id.guia_d2){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/d2.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }
        else if(id==R.id.guia_d3){
            evaluar=false;
            numeroDeReactivoActual=0;
            WebView textoGuias = (WebView) findViewById(R.id.textoGuias);

            WebSettings settings = textoGuias.getSettings();

            settings.setDefaultTextEncodingName("utf-8");
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setGeolocationEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            //   textoGuias.loadUrl(datosGuias, "text/html; charset=utf-8", "UTF-8");
            textoGuias.loadUrl("file:///android_asset/d3.html");
            ocultarTodo();
            ScrollView principal= (ScrollView) findViewById(R.id.layout_guias);
            principal.setVisibility(View.VISIBLE);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Metodo previo a la bisuqueda de reactivos se debe invocar antes
    public void previoABuscarReactivos(){
        numeroDeReactivoActual=0;
calificacion=0;
        totalCalificacion=0;



            //Ajustamos el texto de boton si no se queda guardaod lo de las preguntas
            TextView textoSinReactivo= (TextView) findViewById(R.id.textoSinReactivos);
            textoSinReactivo.setVisibility(View.INVISIBLE);


      TextView titulo= (TextView) findViewById(R.id.textoTituloPregunta);
       RadioGroup grupo= (RadioGroup) findViewById((R.id.radioSeleccionado));
        titulo.setVisibility(View.VISIBLE);
        grupo.setVisibility(View.VISIBLE);


            botonReactivos.setEnabled(true);
            botonReactivos.setVisibility(View.VISIBLE);
            botonReactivos.setText("Ver preguntas");
        layout_reactivos.refreshDrawableState();


        evaluar=false;

    }

    /*******************************************************************************
     HACEMOS EL POST PARA AUTENTICAR
     **********************************************************************************/
    class TareaAsincronicaGuardarUsuario extends AsyncTask<String, Integer, Integer> {

        String sinConexion="";
        String login="nada";
        @Override
        protected Integer doInBackground(String... params) {
            try {
                login=  autenticar().getAutoridad();
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<SE autentico BIEN: "+login);

            } catch (Exception e) {
                System.out.println("Hubo problemas "+e.getMessage());

            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer i){

            try {

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<Holaaa SI SALIO BIEN");



            }catch (Exception e){
                System.out.println(">>>>>>>>>>>>>>>>Algo malo sucedio "+e.getMessage());

            }
        }

        /*
Metodo para enviar un json a un controller que acxepte dicho json
 */
        public Profesor autenticar()throws Exception{
// Creamos un objeto simple

            Profesor p=new Profesor();
            p.setLogin("juanito");
            p.setPassword("33868");
// Ajustamos el content type acorde, en ese caso json
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application","json"));
            HttpEntity<Profesor> requestEntity = new HttpEntity<Profesor>(p, requestHeaders);

// Creamos una nueva instancia de RestTemplate
            RestTemplate restTemplate = new RestTemplate();

// Agregamos los conertidores de jackson
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

// Hacemos el metodo post y obtenemos nuestra respuesta
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String respuesta = responseEntity.getBody();
            ObjectMapper mapper=new ObjectMapper();
            Profesor profesor=mapper.readValue(respuesta, new TypeReference<Profesor>() {
            });

            return profesor;
        }
    }
/***************************************************************************
    GET REACTIVOS BASADO EN EL AREA SELECCIONADA
 ****************************************************************************/
class TareaAsyncronicaGetReactivosArea extends AsyncTask<String,Integer,Integer>{
    boolean sinConexion=false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialogoCargado.setMessage("Buscando los reactivos...")
                .setTitle("Cargando reactivos");
        dialogito = dialogoCargado.create();
        dialogito.show();


    }

    @Override
    protected Integer doInBackground(String... params){
        try {
         reactivos=   obtenerReactivos();
            System.out.println("<<<<<<<<<<<SI SALIO BIENEEEN, tamaño:"+reactivos.size());


        } catch (Exception e) {
           System.out.println("<<<<<<<ALGO SALIO MAL"+e.getMessage());
          sinConexion=true;



        }
        return 1;
    }


    protected void onPostExecute(Integer integer) {
        try {
            if(sinConexion){
            TextView texto= (TextView) findViewById(R.id.textoSinReactivos);
                texto.setVisibility(View.VISIBLE);
                texto.setText("En este momento no tienes conexion a internet, no podemos mostrar los reactivos");
             Button boton= (Button) findViewById(R.id.cargarReactivos);
                boton.setVisibility(View.INVISIBLE);
             //Quitamos  el dialoguito de busqueda
                if (dialogito.isShowing()) {
                    dialogito.dismiss();
                }

            }else {
                TextView texto= (TextView) findViewById(R.id.textoSinReactivos);
                texto.setVisibility(View.INVISIBLE);

                super.onPostExecute(integer);
                if (reactivos.size() == 0) {
                    Button botonReactivos = (Button) findViewById(R.id.cargarReactivos);
                    TextView textoSinReactivos = (TextView) findViewById(R.id.textoSinReactivos);
                    botonReactivos.setVisibility(View.INVISIBLE);
                    textoSinReactivos.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getApplicationContext(), "Encontrados:" + reactivos.size(),
                        Toast.LENGTH_LONG).show();



/**********************************************************************************************************
     Checamos si las pregunats son más de 10 para hacer el acomodado al azar
 **********************************************************************************************************/
            if(reactivos.size()>5) {
                Random rng = new Random(); //
// El LinkedHashSet es para mantener el orden
                Set<Integer> generated = new LinkedHashSet<Integer>();
                //Aqui va el numero de reactivos a mostrarse o sea 10 preguntas
                while (generated.size() < 5) {
                    // Aqui va el numero total de reactivos en los que debe hacerse la asignacion aleatorio
                    Integer next = rng.nextInt(reactivos.size()) + 1;
                    // Agregamos
                    generated.add(next);
                }
                //Acomodamos los reactivos de acuerdo a los indices generados
              reactivosAleatorios=new ArrayList<>();
                for (int i : generated) {
                    Log.i("XXXX", "Valor:" + i);
                    reactivosAleatorios.add(reactivos.get(i-1));
                }

               //Reasignamos el arraylist de reactivos a sólo los 10.
                reactivos=reactivosAleatorios;


            }//***************************** AQUI TERMINA EL ALGORITMO SI ES QUE NO SON MAS DE 10
                if (dialogito.isShowing()) {
                    dialogito.dismiss();
                }

            /*
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_reactivos);
            TextView[] titulos = new TextView[tama];
            RadioGroup[] radios = new RadioGroup[tama];
            for (int i = 0; i < tama; i++) {
                titulos[i] = new TextView(getApplicationContext());
                titulos[i].setText(reactivos.get(i).getPregunta());

                RadioButton b1 = new RadioButton(getApplicationContext());
                b1.setTextColor(Color.BLACK);
                int textColor = Color.parseColor("#000000");
                b1.setButtonTintMode(PorterDuff.Mode.DARKEN);
                b1.setText(reactivos.get(i).getOpciones().get(0).getTitulo());
                RadioButton b2 = new RadioButton(getApplicationContext());
                b2.setTextColor(Color.BLACK);
                b2.setText(reactivos.get(i).getOpciones().get(1).getTitulo());
                RadioButton b3 = new RadioButton(getApplicationContext());
                b3.setText(reactivos.get(i).getOpciones().get(2).getTitulo());
                b3.setTextColor(Color.BLACK);
                RadioButton b4 = new RadioButton(getApplicationContext());
                b4.setTextColor(Color.BLACK);
                b4.setText(reactivos.get(i).getOpciones().get(3).getTitulo());
                radios[i] = new RadioGroup(getApplicationContext());
                radios[i].addView(b1);
                radios[i].addView(b2);
                radios[i].addView(b3);
                radios[i].addView(b4);
                titulos[i].setTextColor(Color.BLACK);
                titulos[i].setPadding(0, 5, 0, 5);
                titulos[i].setTypeface(tf);
                layout.addView(titulos[i]);
                layout.addView(radios[i]);

            }*/
            }//termina el esle
        }catch (Exception e){


            Toast.makeText(getApplicationContext(),
                    "NO se pudieron cargar los reactivos, revisa tu conexion wi-fi",Toast.LENGTH_LONG);

        }


    }

    public ArrayList<Reactivo> obtenerReactivos()throws Exception{

        //Ajustamos headers y entidad
        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Collections.singletonList(new MediaType("application", "json")));

        HttpEntity<?> entity=new HttpEntity<Object>(headers);
        //Creamos el resttemplate
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        //Creamos la entidad de respuesta
        ResponseEntity<String> responseEntity=restTemplate.exchange(url+"/"+areaReactivos,HttpMethod.GET,entity,String.class);
        String respuesta=responseEntity.getBody();

        //Convertimos en objeto este json que llego
        ObjectMapper maper=new ObjectMapper();
        reactivos=maper.readValue(respuesta, new TypeReference<ArrayList<Reactivo>>() {
        });

        return reactivos;

    }
}

    public void ocultarTodo(){
        ScrollView reactivos=(ScrollView)findViewById(R.id.layout_reactivos);
        ScrollView guias=(ScrollView)findViewById(R.id.layout_guias);
        ScrollView principal= (ScrollView) findViewById(R.id.contenido_principal);
        ScrollView evalauciones=(ScrollView)findViewById(R.id.contenido_evaluacion);
        reactivos.setVisibility(View.INVISIBLE);
        guias.setVisibility(View.INVISIBLE);
        principal.setVisibility(View.INVISIBLE);
        evalauciones.setVisibility(View.INVISIBLE);
    }

    public void mostrarReactivo(int indice){
        mostrarOpciones();
   TextView textoTituloPregunta= (TextView) findViewById(R.id.textoTituloPregunta);
  //      textoTituloPregunta.setTypeface(tf);
        RadioButton radioOpcion1= (RadioButton) findViewById(R.id.radioOpcion1);
        RadioButton radioOpcion2= (RadioButton) findViewById(R.id.radioOpcion2);
        RadioButton radioOpcion3= (RadioButton) findViewById(R.id.radioOpcion3);
        RadioButton radioOpcion4= (RadioButton) findViewById(R.id.radioOpcion4);
        //aJUSTAMOS LA PREGUNTA
        reactivo=reactivos.get(indice);
        textoTituloPregunta.setText(reactivo.getPregunta());
        radioOpcion1.setText(reactivo.getOpciones().get(0).getTitulo());
        radioOpcion2.setText(reactivo.getOpciones().get(1).getTitulo());
        radioOpcion3.setText(reactivo.getOpciones().get(2).getTitulo());
        radioOpcion4.setText(reactivo.getOpciones().get(3).getTitulo());



        respuestaReactivo=reactivo.getRetroalimentacion();



    }
    public void ocultarOpciones(){
        TextView textoTituloPregunta= (TextView) findViewById(R.id.textoTituloPregunta);
        RadioButton radioOpcion1= (RadioButton) findViewById(R.id.radioOpcion1);
        RadioButton radioOpcion2= (RadioButton) findViewById(R.id.radioOpcion2);
        RadioButton radioOpcion3= (RadioButton) findViewById(R.id.radioOpcion3);
        RadioButton radioOpcion4= (RadioButton) findViewById(R.id.radioOpcion4);
        Button botonChecarRespuesta= (Button) findViewById(R.id.botonChecarRespuesta);
        textoTituloPregunta.setVisibility(View.INVISIBLE);
        radioOpcion1.setVisibility(View.INVISIBLE);
        radioOpcion2.setVisibility(View.INVISIBLE);
        radioOpcion3.setVisibility(View.INVISIBLE);
        radioOpcion4.setVisibility(View.INVISIBLE);
        botonChecarRespuesta.setVisibility(View.INVISIBLE);

    }
    public void mostrarOpciones(){
        TextView textoTituloPregunta= (TextView) findViewById(R.id.textoTituloPregunta);
        RadioButton radioOpcion1= (RadioButton) findViewById(R.id.radioOpcion1);
        RadioButton radioOpcion2= (RadioButton) findViewById(R.id.radioOpcion2);
        RadioButton radioOpcion3= (RadioButton) findViewById(R.id.radioOpcion3);
        RadioButton radioOpcion4= (RadioButton) findViewById(R.id.radioOpcion4);
        Button botonChecarRespuesta= (Button) findViewById(R.id.botonChecarRespuesta);
        textoTituloPregunta.setVisibility(View.VISIBLE);
        radioOpcion1.setVisibility(View.VISIBLE);
        radioOpcion2.setVisibility(View.VISIBLE);
        radioOpcion3.setVisibility(View.VISIBLE);
        radioOpcion4.setVisibility(View.VISIBLE);
        botonChecarRespuesta.setVisibility(View.VISIBLE);


    }
    /*
    ESTE METODO SE INVOCA CADA QUE SE SELECCIONE UNA OPCION DEL MENU OFF CANVAS PARA EVITAR QUE EL BOTON QUEDE HASTA ABAJO
     */
public void limpiarScrollViewReactivos(){
    TextView tv= (TextView) findViewById(R.id.textoTituloPregunta);
    tv.setText("");
    ScrollView sc= (ScrollView) findViewById(R.id.layout_reactivos);
    sc.refreshDrawableState();

}

}
