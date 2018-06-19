package corp.carrizales.hefesto_002;

import android.database.Cursor;

import java.util.ArrayList;

import corp.carrizales.hefesto_002.modelo.CumplimientoUsuario;
import corp.carrizales.hefesto_002.modelo.HistorialNotificacion;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 21/03/2018.
 */

public class ListHistorialNotificacion {
    public static ArrayList<HistorialNotificacion> listaHistorialNotificacion(String idModMotocicleta) {
        ArrayList<HistorialNotificacion> historialNotificacion = new ArrayList<>();


        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion(idModMotocicleta);

        if (cursor.moveToFirst()) {
            do {
                Boolean boolAux;
                if(cursor.getString(5) == null) {
                    boolAux = false;
                }
                else {
                    boolAux = false;
                }
                historialNotificacion.add(new HistorialNotificacion(cursor.getString(0), cursor.getString(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), boolAux, cursor.getString(6), cursor.getString(7)));
            } while (cursor.moveToNext());
        }
        return historialNotificacion;
    } //Se usa en la interface Notificacion

    public static ArrayList<HistorialNotificacion> listaHistorialNotificaciones_CompletoRealizados(String idModMotocicleta, int kilometraje) {
        ArrayList<HistorialNotificacion> historialNotificacion = new ArrayList<>();


        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificaciones_ListaCompletaRealizadas(idModMotocicleta, kilometraje);

        if (cursor.moveToFirst()) {
            do {
                Boolean boolAux;
                if(cursor.getString(4) == null) {
                    boolAux = false;
                }
                else {
                    boolAux = false;
                }
                historialNotificacion.add(new HistorialNotificacion(null, cursor.getString(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getString(3), boolAux, null, null));
            } while (cursor.moveToNext());
        }
        return historialNotificacion;
    }
    public static ArrayList<HistorialNotificacion> listaHistorialNotificaciones_CompletoNoRealizados(String idModMotocicleta, int kilometraje) {
        ArrayList<HistorialNotificacion> historialNotificacion = new ArrayList<>();


        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificaciones_ListaCompletaNoRealizadas(idModMotocicleta, kilometraje);

        if (cursor.moveToFirst()) {
            do {
                Boolean boolAux;
                if(cursor.getString(4) == null) {
                    boolAux = false;
                }
                else {
                    boolAux = false;
                }
                historialNotificacion.add(new HistorialNotificacion(null, cursor.getString(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getString(3), boolAux, null, null));
            } while (cursor.moveToNext());
        }
        return historialNotificacion;
    }

    public static ArrayList<String> listaNotificacion_kilometrajes(String idModMotocicleta) {
        ArrayList<String> kilometros = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_Kilometrajes(idModMotocicleta);

        if (cursor.moveToFirst()) {
            do {
                kilometros.add("" + cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return kilometros;
    }
    public static Integer listaNotificacion_porcentajes(String idModMotocicleta, String kilometraje) {
        ArrayList<Integer> porcentajes = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_porcentajeCumplimiento(idModMotocicleta, kilometraje);

        if (cursor.moveToFirst()) {
            do {
                porcentajes.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return porcentajes.get(0);
    }

    public static ArrayList<String> listaNotificacion_kilometrajes_año(String idModMotocicleta, String año) {
        ArrayList<String> kilometros = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_kilometrajes_Año(idModMotocicleta, año);

        if (cursor.moveToFirst()) {
            do {
                kilometros.add("" + cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return kilometros;
    }
    public static ArrayList<String> listaNotificacion_kilometrajes_año_mes(String idModMotocicleta, String año, String mes) {
        ArrayList<String> kilometros = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_kilometrajes_Año_Mes(idModMotocicleta, año, mes);

        if (cursor.moveToFirst()) {
            do {
                kilometros.add("" + cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return kilometros;
    }

    //FUNCIONES PARA CARGAR LOS ESPINERS DE HISTORIAL NOTIFICACION
    public static ArrayList<String> listaHistorialNotificacion_años(String idModMotocicleta) {
        ArrayList<String> años = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_ListAños(idModMotocicleta);

        if (cursor.moveToFirst()) {
            do {
                años.add("" + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return años;
    }
    public static ArrayList<String> listaHistorialNotificacion_meses(String idModMotocicleta, String año) {
        ArrayList<String> años = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_ListMeses(idModMotocicleta, año);

        if (cursor.moveToFirst()) {
            do {
                años.add(cursor.getString(0));
                /*String auxTipoMes = "" + cursor.getString(0);
                switch (auxTipoMes) {
                    case "01":
                        años.add("Enero");
                        break;
                    case "02":
                        años.add("Febrero");
                        break;
                    case "03":
                        años.add("Marzo");
                        break;
                    case "04":
                        años.add("Abril");
                        break;
                    case "05":
                        años.add("Mayo");
                        break;
                    case "06":
                        años.add("Junio");
                        break;
                    case "07":
                        años.add("Julio");
                        break;
                    case "08":
                        años.add("Agosto");
                        break;
                    case "09":
                        años.add("Septiembre");
                        break;
                    case "10":
                        años.add("Octubre");
                        break;
                    case "11":
                        años.add("Noviembre");
                        break;
                    case "12":
                        años.add("Diciembre");
                        break;
                    default:
                        años.add("Vacio");
                }
                */
            } while (cursor.moveToNext());
        }
        return años;
    }

    public static int verificarExistenciaNotificacionHistorial(String idMotocicleta, int kilometraje){
        int ultimoKilometraje = 0;
        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacionRegistroUnico(idMotocicleta, kilometraje);
        if (cursor.moveToFirst()) {
            ultimoKilometraje = cursor.getInt(0);
        }
        return ultimoKilometraje;
    }

    public static boolean obtenerHistorialNotificacion_SinRealizar(String idMotocicleta){
        boolean boleano = false;
        Cursor cursor = OperacionesBaseDatos.obtenerHistorialNotificacion_SinRealizar(idMotocicleta);
        if (cursor.moveToFirst()) {
            if(cursor.getString(0)!=null){
                boleano = true;
            }
        }
        return boleano;
    }

    public static int obtenerUltimoHistorialNotificacion_Kilometraje(String idMotocicleta){
        int ultimoKilometraje = 0;
        Cursor cursor = OperacionesBaseDatos.obtenerUltimoHistorialNotificacion_Kilometraje(idMotocicleta);
        if (cursor.moveToFirst()) {
            ultimoKilometraje = cursor.getInt(0);
        }
        return ultimoKilometraje;
    }
}
