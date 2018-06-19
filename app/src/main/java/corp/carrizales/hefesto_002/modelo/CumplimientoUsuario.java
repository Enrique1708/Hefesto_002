package corp.carrizales.hefesto_002.modelo;

/**
 * Created by Luis on 23/04/2018.
 */

public class CumplimientoUsuario {
    private int kilometraje;
    private double porcentaje;

    public CumplimientoUsuario(int kilometraje, double porcentaje) {
        this.kilometraje = kilometraje;
        this.porcentaje = porcentaje;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

}
