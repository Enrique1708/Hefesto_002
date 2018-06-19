package corp.carrizales.hefesto_002.modelo;

/**
 * Created by Luis on 27/10/2017.
 */

public class HistorialTemperatura {

    public String id;
    public int temperatura;
    public String fecha;
    public String id_IdMotocicleta;

    public HistorialTemperatura(String id, int temperatura, String fecha, String id_IdMotocicleta) {

        this.id = id;
        this.temperatura = temperatura;
        this.fecha = fecha;
        this.id_IdMotocicleta = id_IdMotocicleta;
    }

    public String getId() {
        return id;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public String getFecha() {
        return fecha;
    }

    public String getId_IdMotocicleta() {
        return id_IdMotocicleta;
    }
}
