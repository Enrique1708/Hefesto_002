package corp.carrizales.hefesto_002.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import corp.carrizales.hefesto_002.sqlite.InformacionMantenimiento.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.InformacionMantenimiento.HistorialTemperatura;
import corp.carrizales.hefesto_002.sqlite.InformacionMantenimiento.HistorialNotificacion;
import corp.carrizales.hefesto_002.sqlite.InformacionMantenimiento.HistorialKilometraje;


/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */

public class BaseDatosHefesto extends SQLiteOpenHelper{

    private static final String NOMBRE_BASE_DATOS = "hefesto.db";

    private static final int VERSION_ACTUAL = 1;


    private SQLiteDatabase db;

    interface Tablas{
        String MOTOCICLETA = "motocicleta";
        String HISTORIALNOTIFICACION = "historialNotificacion";
        String HISTORIALTEMPERATURA = "historialTemperatura";
        String HISTORIALKILOMETRAJE = "historialKilometraje";

        //String ACEITENORMA = "aceiteNorma";
        //String COMPONENTEMOTOCICLETA = "componenteMotocicleta";
    }

    public BaseDatosHefesto(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        //contexto.deleteDatabase(NOMBRE_BASE_DATOS);//THIS IS FOR DROP DATABASE
        db = this.getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");

            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL)",
                Tablas.MOTOCICLETA, BaseColumns._ID,
                Motocicleta.ID, Motocicleta.MODELO,
                Motocicleta.CONTRASEÑA, Motocicleta.KILOMETRAJE));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "%s TEXT NOT NULL UNIQUE, %s TEXT NOT NULL," +
                        "%s INTEGER NOT NULL, %s DATE NOT NULL,"+
                        "%s DATE, %s BOOLEAN, %s TEXT NOT NULL, %s TEXT NOT NULL)",
                        Tablas.HISTORIALNOTIFICACION, BaseColumns._ID,
                        HistorialNotificacion.ID, HistorialNotificacion.NOMBRE,
                        HistorialNotificacion.KILOMETRAJE, HistorialNotificacion.FECHACREACION,
                        HistorialNotificacion.FECHAREALIZACION, HistorialNotificacion.OPORTUNO,
                        HistorialNotificacion.DESCRIPCION, HistorialNotificacion.ID_IDMOTOCICLETA));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE, %s INTEGER NOT NULL, %s DATE NOT NULL,"+
                        "%s TEXT NOT NULL)",
                Tablas.HISTORIALTEMPERATURA, BaseColumns._ID,
                HistorialTemperatura.ID, HistorialTemperatura.TEMPERATURA,
                HistorialTemperatura.FECHA, HistorialTemperatura.ID_IDMOTOCICLETA));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE, %s INTEGER NOT NULL, %s DATE NOT NULL,"+
                        "%s TEXT NOT NULL)",
                Tablas.HISTORIALKILOMETRAJE, BaseColumns._ID,
                HistorialKilometraje.ID, HistorialKilometraje.KILOMETRAJE,
                HistorialKilometraje.FECHA, HistorialKilometraje.ID_IDMOTOCICLETA));
        /*
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL," +
                        "%s INTEGER NOT NULL, %s INTEGER NOT NULL)",
                Tablas.ACEITENORMA, BaseColumns._ID,
                AceiteNorma.ID, AceiteNorma.NORMASAE,
                AceiteNorma.ORIGEN, AceiteNorma.TEMPERATURAMAX,
                AceiteNorma.TEMPERATURAMIN));
                */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MOTOCICLETA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HISTORIALNOTIFICACION);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HISTORIALTEMPERATURA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HISTORIALKILOMETRAJE);
        //db.execSQL("DROP TABLE IF EXISTS " + Tablas.ACEITENORMA);
        //db.execSQL("DROP TABLE IF EXISTS " + Tablas.COMPONENTEMOTOCICLETA);

        onCreate(db);
    }
}