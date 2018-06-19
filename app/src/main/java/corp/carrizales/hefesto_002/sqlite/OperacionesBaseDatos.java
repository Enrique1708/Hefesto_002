package corp.carrizales.hefesto_002.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import corp.carrizales.hefesto_002.modelo.HistorialTemperatura;
import corp.carrizales.hefesto_002.sqlite.BaseDatosHefesto.Tablas;
import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.modelo.HistorialNotificacion;
import corp.carrizales.hefesto_002.modelo.HistorialKilometraje;


/**
 * Clase auxiliar que implementa a {@link BaseDatosHefesto} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */
public final class OperacionesBaseDatos {

    private static BaseDatosHefesto baseDatos;

    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();

    private OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new BaseDatosHefesto(contexto);
        }
        return instancia;
    }

    /////////////////[OPERACIONES MOTOCICLETA]////////////////////////////////
    public static Cursor ObtenerMotocicletas(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT id, modelo, contraseña, kilometraje FROM %s", Tablas.MOTOCICLETA);

        return db.rawQuery(sql, null);
    }
    public static void insertarMotocicleta(Motocicleta motocicleta){

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        //generar PK
        String idMotocicleta = InformacionMantenimiento.Motocicletas.generarIdMotocicleta();
        valores.put(InformacionMantenimiento.Motocicleta.ID, idMotocicleta);
        valores.put(InformacionMantenimiento.Motocicleta.MODELO, motocicleta.modelo);
        valores.put(InformacionMantenimiento.Motocicleta.CONTRASEÑA, motocicleta.contraseña);
        valores.put(InformacionMantenimiento.Motocicleta.KILOMETRAJE, motocicleta.kilometraje);

        db.insertOrThrow(Tablas.MOTOCICLETA, null, valores);
    }

    public static void actualizarKilometrajeMotocicleta(int kilometraje, String idMotocicleta){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InformacionMantenimiento.Motocicleta.KILOMETRAJE, kilometraje);

        String whereClause = String.format("%s=?", InformacionMantenimiento.Motocicleta.ID);
        String[] whereArgs = {idMotocicleta};

       db.update(Tablas.MOTOCICLETA, valores, whereClause, whereArgs);
    }
    //////////////////////////////////////////////////////////////////////////

    /////////////[OPERACIONES HISTORIAL NOTIFICACION/////////////////////////////////////////////////////
    public static Cursor obtenerHistorialNotificacion_Kilometrajes(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT DISTINCT(kilometraje) FROM %s WHERE %s=?",

                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }
    public static Cursor obtenerHistorialNotificacion_porcentajeCumplimiento(String idModMotocicleta, String kilometraje){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT COUNT(oportuno), 1 FROM %s WHERE %s='%s' AND oportuno=1 AND kilometraje=?",
                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA, idModMotocicleta);
        String [] selectionArgs = {kilometraje};
        return db.rawQuery(sql, selectionArgs);
    }
    public static Cursor obtenerHistorialNotificacion_ListAños(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT DISTINCT(strftime(%s,fechaCreacion)) FROM %s WHERE %s='%s'",
            "'%Y'",Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA, idModMotocicleta);
        return db.rawQuery(sql, null);
    }
    public static Cursor obtenerHistorialNotificacion_ListMeses(String idModMotocicleta, String año){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT DISTINCT(strftime(%s,fechaCreacion)) FROM %s WHERE %s='%s' AND strftime(%s,fechaCreacion) LIKE '%s'",
                "'%m'",Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA, idModMotocicleta, "'%Y'", año);
        return db.rawQuery(sql, null);
    }

    public static Cursor obtenerHistorialNotificacion_kilometrajes_Año(String idModMotocicleta, String año){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String aux = "%Y";

        String sql = String.format("SELECT DISTINCT(kilometraje) FROM %s WHERE %s='%s' AND strftime('%s',%s)='%s'",
                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA, idModMotocicleta,
                aux,InformacionMantenimiento.HistorialNotificacion.FECHACREACION, año);

        return db.rawQuery(sql, null);
    }
    public static Cursor obtenerHistorialNotificacion_kilometrajes_Año_Mes(String idModMotocicleta, String año, String mes){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = ("SELECT DISTINCT(kilometraje) FROM HISTORIALNOTIFICACION WHERE id_IdMotocicleta ='"+idModMotocicleta+"' AND strftime('%Y', fechaCreacion) = '"+año+"'"+"AND strftime('%m', fechaCreacion) = '"+mes+"'");

        return db.rawQuery(sql, null);
    }

    public static Cursor obtenerHistorialNotificacion(String idModMotocicleta){ //PARA LA LISTA DE NOTIFICACIONES
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT id, nombre, kilometraje, fechaCreacion, fechaRealizacion, oportuno, descripcion, id_IdMotocicleta FROM %s WHERE oportuno is NULL and %s=?",
                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        /*String sql = String.format("SELECT id, nombre, kilometraje, fechaCreacion, fechaRealizacion, oportuno, descripcion, id_IdMotocicleta FROM %s WHERE %s=?",
                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);*/
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static Cursor obtenerHistorialNotificaciones_ListaCompletaRealizadas(String idModMotocicleta, int kilometraje){ //PARA LA LISTA DE NOTIFICACIONES
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT nombre, kilometraje, fechaCreacion, fechaRealizacion, oportuno FROM %s WHERE fechaRealizacion IS NOT NULL AND kilometraje = %s  AND %s=?",
                Tablas.HISTORIALNOTIFICACION, kilometraje,InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }
    public static Cursor obtenerHistorialNotificaciones_ListaCompletaNoRealizadas(String idModMotocicleta, int kilometraje){ //PARA LA LISTA DE NOTIFICACIONES
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT nombre, kilometraje, fechaCreacion, fechaRealizacion, oportuno FROM %s WHERE fechaRealizacion IS NULL AND kilometraje = %s  AND %s=?",
                Tablas.HISTORIALNOTIFICACION, kilometraje,InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static Cursor obtenerUltimoHistorialNotificacion_Kilometraje(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT kilometraje FROM %s WHERE %s=? AND fechaRealizacion IS NULL ORDER BY kilometraje DESC",
                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static Cursor obtenerHistorialNotificacionRegistroUnico(String idModMotocicleta, int kilometraje){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT kilometraje FROM %s WHERE kilometraje = %s AND %s=? ORDER BY kilometraje DESC",
                Tablas.HISTORIALNOTIFICACION,kilometraje, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static Cursor obtenerHistorialNotificacion_SinRealizar(String idModMotocicleta){ //PARA LA LISTA DE NOTIFICACIONES
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT id FROM %s WHERE oportuno is NULL and %s=?",
                Tablas.HISTORIALNOTIFICACION, InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static void insertarHistorialNotificacion(HistorialNotificacion historialNotificacion){

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        //generar PK
        String idHistNotific = InformacionMantenimiento.HistorialNotificaciones.generarIdHistorialNotificaciones();
        valores.put(InformacionMantenimiento.HistorialNotificacion.ID, idHistNotific);
        valores.put(InformacionMantenimiento.HistorialNotificacion.NOMBRE, historialNotificacion.nombre);
        valores.put(InformacionMantenimiento.HistorialNotificacion.KILOMETRAJE, historialNotificacion.kilometraje);
        valores.put(InformacionMantenimiento.HistorialNotificacion.FECHACREACION, historialNotificacion.fechaCreacion);
        valores.put(InformacionMantenimiento.HistorialNotificacion.FECHAREALIZACION, historialNotificacion.fechaRealizacion);
        valores.put(InformacionMantenimiento.HistorialNotificacion.OPORTUNO, historialNotificacion.oportuno);
        valores.put(InformacionMantenimiento.HistorialNotificacion.DESCRIPCION, historialNotificacion.descripcion);
        valores.put(InformacionMantenimiento.HistorialNotificacion.ID_IDMOTOCICLETA, historialNotificacion.id_IdMotocicleta);

        db.insertOrThrow(Tablas.HISTORIALNOTIFICACION, null, valores);
    }
    public static void mantenimientoRealizado(HistorialNotificacion historialNotificacion){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String boalOportuno;

        ContentValues valores = new ContentValues();
        valores.put(InformacionMantenimiento.HistorialNotificacion.FECHAREALIZACION, historialNotificacion.fechaRealizacion);

        valores.put(InformacionMantenimiento.HistorialNotificacion.OPORTUNO, historialNotificacion.oportuno);

        String whereClause = String.format("%s=?", InformacionMantenimiento.HistorialNotificacion.ID);
        String[] whereArgs = {historialNotificacion.getId()};

        int resultado = db.update(Tablas.HISTORIALNOTIFICACION, valores, whereClause, whereArgs);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////[OPERACIONES HISTORIAL kILOMETRAJE]/////////////////////////////////////////////////////

    public static Cursor obtenerUltimoHistorialKilometraje(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT id, kilometraje, fecha, id_IdMotocicleta FROM %s WHERE %s=? ORDER BY kilometraje DESC",
                Tablas.HISTORIALKILOMETRAJE, InformacionMantenimiento.HistorialKilometraje.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }
    public static Cursor obtenerPrimerHistorialKilometraje(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT id, kilometraje, fecha, id_IdMotocicleta FROM %s WHERE %s=? ORDER BY kilometraje",
                Tablas.HISTORIALKILOMETRAJE, InformacionMantenimiento.HistorialKilometraje.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static Cursor obtenerHistorialKilometraje_Kilometrajes(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT kilometraje FROM %s WHERE %s=?",
                Tablas.HISTORIALKILOMETRAJE, InformacionMantenimiento.HistorialKilometraje.ID_IDMOTOCICLETA);
        String [] selectionArgs = {idModMotocicleta};
        return db.rawQuery(sql, selectionArgs);
    }

    public static void insertarHistorialKilometraje(HistorialKilometraje historialKilometraje){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        //generar PK
        String idHistKilometraje = InformacionMantenimiento.HistorialKilometrajes.generarIdHistorialKilometraje();
        valores.put(InformacionMantenimiento.HistorialKilometraje.ID, idHistKilometraje);
        valores.put(InformacionMantenimiento.HistorialKilometraje.KILOMETRAJE, historialKilometraje.kilometraje);
        valores.put(InformacionMantenimiento.HistorialKilometraje.FECHA, historialKilometraje.fecha);
        valores.put(InformacionMantenimiento.HistorialKilometraje.ID_IDMOTOCICLETA, historialKilometraje.id_IdMotocicleta);

        db.insertOrThrow(Tablas.HISTORIALKILOMETRAJE, null, valores);

    }

    public static void actualizarHistorialKilometraje(int kilometraje, String idMotocicleta){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InformacionMantenimiento.HistorialKilometraje.KILOMETRAJE, kilometraje);

        String whereClause = String.format("%s=?", InformacionMantenimiento.Motocicleta.ID);
        String[] whereArgs = {idMotocicleta};

        db.update(Tablas.HISTORIALKILOMETRAJE, valores, whereClause, whereArgs);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////[OPERACIONES HISTORIAL TEMPERATURA]/////////////////////////////////////////////////////
    public static void insertarHistorialTemperatura(HistorialTemperatura historialTemperatura){

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        //generar PK
        String idTempNotific = InformacionMantenimiento.HistorialTemperaturas.generarIdHistorialTemperatura();
        valores.put(InformacionMantenimiento.HistorialTemperatura.ID, idTempNotific);
        valores.put(InformacionMantenimiento.HistorialTemperatura.TEMPERATURA, historialTemperatura.temperatura);
        valores.put(InformacionMantenimiento.HistorialTemperatura.FECHA, historialTemperatura.fecha);
        valores.put(InformacionMantenimiento.HistorialTemperatura.ID_IDMOTOCICLETA, historialTemperatura.id_IdMotocicleta);

        db.insertOrThrow(Tablas.HISTORIALTEMPERATURA, null, valores);
    }

    public static Cursor obtenerHistorialTemperatura_List_AñoMes(String idModMotocicleta, String año, String mes){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = ("SELECT strftime('%d',fecha), temperatura FROM HISTORIALTEMPERATURA WHERE id_IdMotocicleta ='"+idModMotocicleta+"' AND strftime('%Y', fecha) = '"+año+"'"+"AND strftime('%m', fecha) = '"+mes+"'");

        return db.rawQuery(sql, null);
    }

    public static Cursor obtenerHistorialTemperatura_ListAños(String idModMotocicleta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT DISTINCT(strftime(%s,fecha)) FROM %s WHERE %s='%s' ORDER BY fecha DESC",
                "'%Y'",Tablas.HISTORIALTEMPERATURA, InformacionMantenimiento.HistorialTemperatura.ID_IDMOTOCICLETA, idModMotocicleta);
        return db.rawQuery(sql, null);
    }
    public static Cursor obtenerHistorialTemperatura_ListMeses(String idModMotocicleta, String año){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT DISTINCT(strftime(%s,fecha)) FROM %s WHERE %s='%s' AND strftime(%s,fecha) LIKE '%s' ORDER BY fecha DESC",
                "'%m'",Tablas.HISTORIALTEMPERATURA, InformacionMantenimiento.HistorialTemperatura.ID_IDMOTOCICLETA, idModMotocicleta, "'%Y'", año);
        return db.rawQuery(sql, null);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }
}
