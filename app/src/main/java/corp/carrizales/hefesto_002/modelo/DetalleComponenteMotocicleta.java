package corp.carrizales.hefesto_002.modelo;

/**
 * Created by Luis on 21/02/2018.
 */

public class DetalleComponenteMotocicleta {
    private String titulo;
    private String Descripcion;
    private int id_imagen;

    public DetalleComponenteMotocicleta() {
    }

    public DetalleComponenteMotocicleta(String titulo, String descripcion, int id_imagen) {
        this.titulo = titulo;
        Descripcion = descripcion;
        this.id_imagen = id_imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getId_imagen() {
        return id_imagen;
    }

    public void setId_imagen(int id_imagen) {
        this.id_imagen = id_imagen;
    }
}
