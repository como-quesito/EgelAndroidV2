package campitos.org.egelandroidv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActividadLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_login);
        findViewById(R.id.botonid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                 EditText edit= (EditText) findViewById(R.id.nip);
                    int minip=Integer.parseInt(edit.getText().toString());
                    if(GeneradorAlumnos.isNip(minip)){

                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        supportFinishAfterTransition();
                        finish();
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Clave incorrecta, intenta de nuevo", Toast.LENGTH_LONG) .show();
                        EditText edita= (EditText) findViewById(R.id.nip);
                        edita.setText(null);

                    }
                }catch(NumberFormatException  e ){
                   Toast.makeText(getApplicationContext(),"Introduce un numero en tu nip",Toast.LENGTH_LONG).show();
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"Introduce un valor",Toast.LENGTH_LONG).show();
                }




            }
        });
    }


}
