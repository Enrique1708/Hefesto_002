package corp.carrizales.hefesto_002.modelo;

import java.util.Date;

/**
 * Created by Luis on 27/10/2017.
 */

public class HistorialNotificacion {

    public String id;
    public String nombre;
    public int kilometraje;
    public String fechaCreacion;
    public String fechaRealizacion;
    public Boolean oportuno;
    public String descripcion;
    public String id_IdMotocicleta;

    public HistorialNotificacion(String id, String nombre, int kilometraje, String fechaCreacion, String fechaRealizacion,
                                 Boolean oportuno, String descripcion, String id_IdMotocicleta){
        this.id = id;
        this.nombre = nombre;
        this.kilometraje = kilometraje;
        this.fechaCreacion = fechaCreacion;
        this.fechaRealizacion = fechaRealizacion;
        this.oportuno = oportuno;
        this.descripcion = descripcion;
        this.id_IdMotocicleta = id_IdMotocicleta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(String fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Boolean getOportuno() {
        return oportuno;
    }

    public void setOportuno(Boolean oportuno) {
        this.oportuno = oportuno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId_IdMotocicleta() {
        return id_IdMotocicleta;
    }

    public void setId_IdMotocicleta(String id_IdMotocicleta) {
        this.id_IdMotocicleta = id_IdMotocicleta;
    }
}
