package campitos.org.egelandroidv2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rapid on 11/07/2016.
 */
public class BaseDeDatos extends SQLiteOpenHelper {
    public static final int VERSION=1;
    public static final String TABLA_ALUMNOS="CREATE TABLE alumno (nip integer primary key, nombre TEXT, numExamenes integer, acceso DATE);";
    public BaseDeDatos(Context context) {
        super(context, "ceneval",null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL(TABLA_ALUMNOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
