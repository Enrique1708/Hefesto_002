package corp.carrizales.hefesto_002.sqlite;

import java.sql.Struct;
import java.util.UUID;

/**
 * Clase que establece nombres de la base de datos
 */

public class InformacionMantenimiento {

    interface Motocicleta{
        String ID = "id";
        String MODELO = "modelo";
        String CONTRASEÑA = "contraseña";
        String KILOMETRAJE = "kilometraje";
    }

    /*interface ComponenteMotocicleta {
        String ID = "id";
        String NOMBRE = "nombre";
        String SOPORTEKILOMETROS = "soportekilometros";
        String DESCRIPCION = "descripcion";
    }*/

    interface  HistorialTemperatura{
        String ID = "id";
        String TEMPERATURA= "temperatura";
        String FECHA = "fecha";
        String ID_IDMOTOCICLETA = "id_IdMotocicleta";
    }

    interface HistorialKilometraje{
        String ID = "id";
        String KILOMETRAJE = "kilometraje";
        String FECHA = "fecha";
        String ID_IDMOTOCICLETA = "id_IdMotocicleta";
    }

    interface HistorialNotificacion{
        String ID = "id";
        String NOMBRE = "nombre";
        String KILOMETRAJE = "kilometraje";
        String FECHACREACION = "fechaCreacion";
        String FECHAREALIZACION = "fechaRealizacion";
        String OPORTUNO = "oportuno";
        String DESCRIPCION = "descripcion";
        String ID_IDMOTOCICLETA = "id_IdMotocicleta";
    }
    //[GENERADOR DE ID UNICOS]
    public static class Motocicletas implements Motocicleta {
        public static String generarIdMotocicleta() {
            return UUID.randomUUID().toString();
        }
    }

    public static class HistorialNotificaciones implements HistorialNotificacion{
        public static String generarIdHistorialNotificaciones(){return UUID.randomUUID().toString();}
    }

    public static class HistorialKilometrajes implements HistorialKilometraje{
        public static String generarIdHistorialKilometraje(){return UUID.randomUUID().toString();}
    }

    public static class HistorialTemperaturas implements HistorialTemperatura{
        public static String generarIdHistorialTemperatura(){return UUID.randomUUID().toString();}
    }

    private InformacionMantenimiento(){

    }
}
