package corp.carrizales.hefesto_002;

import android.database.Cursor;

import java.util.ArrayList;

import corp.carrizales.hefesto_002.modelo.HistorialTemperatura;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 11/06/2018.
 */

public class ListHistorialTemperatura {
    public static ArrayList<HistorialTemperatura> listaHistorialTemperaturas(String idModMotocicleta, String año, String mes) {
        ArrayList<HistorialTemperatura> historialTemperaturas = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialTemperatura_List_AñoMes(idModMotocicleta, año, mes);

        if (cursor.moveToFirst()) {
            do {
                historialTemperaturas.add(new HistorialTemperatura(null, cursor.getInt(1), cursor.getString(0),
                       null));
            } while (cursor.moveToNext());
        }
        return historialTemperaturas;
    }

    public static ArrayList<String> listaHistorialTemperatura_años(String idModMotocicleta) {
        ArrayList<String> años = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialTemperatura_ListAños(idModMotocicleta);

        if (cursor.moveToFirst()) {
            do {
                años.add("" + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return años;
    }
    public static ArrayList<String> listaHistorialNotificacion_meses(String idModMotocicleta, String año) {
        ArrayList<String> años = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialTemperatura_ListMeses(idModMotocicleta, año);

        if (cursor.moveToFirst()) {
            do {
                años.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return años;
    }
}
