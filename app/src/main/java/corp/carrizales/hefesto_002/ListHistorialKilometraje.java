package corp.carrizales.hefesto_002;

import android.database.Cursor;

import java.util.ArrayList;

import corp.carrizales.hefesto_002.modelo.HistorialKilometraje;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 12/03/2018.
 */

public class ListHistorialKilometraje {
    public static ArrayList<HistorialKilometraje> listaHistorialKilometrajeUltimo(String idModMotocicleta){
        ArrayList<HistorialKilometraje> historialKilometrajes = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerUltimoHistorialKilometraje(idModMotocicleta);

        if (cursor.moveToFirst()){
            do {
                historialKilometrajes.add(new HistorialKilometraje(cursor.getString(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return historialKilometrajes;
    }
    public static ArrayList<HistorialKilometraje> listaHistorialKilometrajePrimer(String idModMotocicleta){
        ArrayList<HistorialKilometraje> historialKilometrajes = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerPrimerHistorialKilometraje(idModMotocicleta);

        if (cursor.moveToFirst()){
            do {
                historialKilometrajes.add(new HistorialKilometraje(cursor.getString(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return historialKilometrajes;
    }


    public static int obtenerUltimoKilometraje(String idModMotocicleta){

        ArrayList<HistorialKilometraje> historialKilometrajes = new ArrayList<>();
        historialKilometrajes = listaHistorialKilometrajeUltimo(idModMotocicleta);
        HistorialKilometraje historialKilometraje = new HistorialKilometraje();

        historialKilometraje = historialKilometrajes.get(0);

        return historialKilometraje.getKilometraje();
    }
    public static String obtenerUltimoKilometraje_fecha(String idModMotocicleta){

        ArrayList<HistorialKilometraje> historialKilometrajes = new ArrayList<>();
        historialKilometrajes = listaHistorialKilometrajeUltimo(idModMotocicleta);
        HistorialKilometraje historialKilometraje = new HistorialKilometraje();

        historialKilometraje = historialKilometrajes.get(0);

        return historialKilometraje.getFecha();
    }

    public static int obtenerPrimerKilometraje(String idModMotocicleta){

        ArrayList<HistorialKilometraje> historialKilometrajes = new ArrayList<>();
        historialKilometrajes = listaHistorialKilometrajePrimer(idModMotocicleta);
        HistorialKilometraje historialKilometraje = new HistorialKilometraje();

        historialKilometraje = historialKilometrajes.get(0);

        return historialKilometraje.getKilometraje();
    }

    public static ArrayList<HistorialKilometraje> obtenerHistorialKilometraje_KilometrosDia(String idModMotocicleta){

        ArrayList<HistorialKilometraje> historialKilometrajes = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialKilometraje_Kilometrajes(idModMotocicleta);

        if (cursor.moveToFirst()){
            do {
                historialKilometrajes.add(new HistorialKilometraje(null, cursor.getInt(0), null,
                       null));
            }while (cursor.moveToNext());
        }
        return historialKilometrajes;
    }
}
