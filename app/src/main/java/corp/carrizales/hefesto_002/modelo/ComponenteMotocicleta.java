package corp.carrizales.hefesto_002.modelo;

/**
 * Created by Luis on 27/10/2017.
 */

public class ComponenteMotocicleta {

    public String id;
    public String nombre;
    public int soporteKilometros;
    public String descripcion;

    public ComponenteMotocicleta(String id, String nombre, int soporteKilometros, String descripcion){

        this.id = id;
        this.nombre = nombre;
        this.soporteKilometros = soporteKilometros;
        this.descripcion = descripcion;
    }
}
