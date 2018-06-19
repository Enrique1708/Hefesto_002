package corp.carrizales.hefesto_002.modelo;

/**
 * Created by Luis on 27/10/2017.
 */

public class HistorialKilometraje {

    public String id;
    public int kilometraje;
    public String fecha;
    public String id_IdMotocicleta;

    public HistorialKilometraje() {
    }

    public  HistorialKilometraje(String id, int kilometraje, String fecha, String id_IdMotocicleta){

        this.id = id;
        this.kilometraje = kilometraje;
        this.fecha = fecha;
        this.id_IdMotocicleta = id_IdMotocicleta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId_IdMotocicleta() {
        return id_IdMotocicleta;
    }

    public void setId_IdMotocicleta(String id_IdMotocicleta) {
        this.id_IdMotocicleta = id_IdMotocicleta;
    }
}
