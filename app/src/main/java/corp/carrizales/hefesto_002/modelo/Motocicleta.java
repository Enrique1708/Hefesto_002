package corp.carrizales.hefesto_002.modelo;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luis on 25/10/2017.
 */

public class Motocicleta implements Serializable{

    public String id;
    public String modelo;
    public String contraseña;
    public int kilometraje;

    public Motocicleta(){

    }

    public Motocicleta(String id, String modelo, String contraseña, int kilometraje){

        this.id = id;
        this.modelo = modelo;
        this.contraseña = contraseña;
        this.kilometraje = kilometraje;
    }

    public String getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public int getKilometraje() {
        return kilometraje;
    }
}
