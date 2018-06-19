package corp.carrizales.hefesto_002.modelo;

/**
 * Created by Luis on 27/10/2017.
 */

public class AceiteNorma {

    public String id;
    public String normaSAE;
    public String origen;
    public int temperaturaMax;
    public int temperaturaMin;

    public AceiteNorma(String id, String normaSAE, String origen, int temperaturaMax, int temperaturaMin){

        this.id = id;
        this.normaSAE = normaSAE;
        this.origen = origen;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
    }
}
