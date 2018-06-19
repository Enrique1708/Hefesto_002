package corp.carrizales.hefesto_002;

import android.database.Cursor;

import java.util.ArrayList;

import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 04/11/2017.
 */

public class ListMotocicleta{


    public static ArrayList<Motocicleta> listaMotocicletas(){

        ArrayList<Motocicleta> motocicletas = new ArrayList<>();

        Cursor cursor = OperacionesBaseDatos.ObtenerMotocicletas();

        if (cursor.moveToFirst()){
            do {

                motocicletas.add(new Motocicleta(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3)));
                //motocicletas.add(new Motocicleta(cursor.getString(0), cursor.getString(2), cursor.getString(3),
                //                    cursor.getInt(4)));
            }while (cursor.moveToNext());
        }

        return motocicletas;
    }

    public static boolean verificarExistenciaMotocicleta(String nombreModelo){

        ArrayList<Motocicleta> motocicletas= new ArrayList<>();
        motocicletas = listaMotocicletas();
        Motocicleta motocicleta = new Motocicleta();

        for (int i = 0; i<motocicletas.toArray().length; i++) {
            if (motocicletas.get(i).getModelo().equals(nombreModelo)) {
                return true;
            }
        }
        return false;
    }

    public static Motocicleta obtenerMotocicleta(String idModMotocicleta){

        ArrayList<Motocicleta> motocicletas= new ArrayList<>();
        motocicletas = listaMotocicletas();
        Motocicleta motocicleta = new Motocicleta();

        for (int i = 0; i<motocicletas.toArray().length; i++) {
            if (motocicletas.get(i).getId().equals(idModMotocicleta)) {
                motocicleta = motocicletas.get(i);
            }
        }
        return motocicleta;
    }

    public static Motocicleta verificarMotocicleta(String nombre, String contraseña){

        ArrayList<Motocicleta> motocicletas= new ArrayList<>();
        motocicletas = listaMotocicletas();
        Motocicleta motocicleta = new Motocicleta();

        for (int i = 0; i<motocicletas.toArray().length; i++) {
            if (motocicletas.get(i).getModelo().equals(nombre) && motocicletas.get(i).getContraseña().equals(contraseña)) {
                motocicleta = motocicletas.get(i);
            }
        }
        return motocicleta;
    }
}
