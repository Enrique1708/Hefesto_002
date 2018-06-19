package corp.carrizales.hefesto_002;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import corp.carrizales.hefesto_002.modelo.HistorialNotificacion;
import corp.carrizales.hefesto_002.sqlite.BaseDatosHefesto;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 06/03/2018.
 */

public class Notificacion_fragment extends Fragment {

    private static final String TAG = "Notificacion_fragment";

    private ListView listView;

    OperacionesBaseDatos datos;

    private List<HistorialNotificacion> listdetHistorialNotificacion;
    ArrayAdapter<HistorialNotificacion> adapter;

    private String idModMotocicleta;

    //VARIABLES PARA GENERAR NOTIFICACIONES
    int ultimoHistKilometraje;
    int kilometrajeActual;
    int kilometrajeInicial;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() !=null){
            idModMotocicleta = getArguments().getString("idModMotocicleta","000");
        }
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity().getApplicationContext());

        generarNotificaciones();

        View view = inflater.inflate(R.layout.notificacion_fragment,container,false);

        listView = (ListView)view.findViewById(R.id.listView);

        adapter = new ListAdapterHistNotificacion(getActivity().getApplicationContext(), getListdetHistorialNotificacion());
        listView.setAdapter(adapter);



        return view;
    }

    private List<HistorialNotificacion> getListdetHistorialNotificacion(){
        listdetHistorialNotificacion = new ArrayList<HistorialNotificacion>();

        listdetHistorialNotificacion = ListHistorialNotificacion.listaHistorialNotificacion(idModMotocicleta);

        return listdetHistorialNotificacion;
    }

    private void generarNotificaciones(){
        //ultimoHistKilometraje = ListHistorialKilometraje.obtenerPrimerKilometraje(idModMotocicleta);
        //int i = 2;
        int auxKilometraje  = 500;
        kilometrajeActual = ListMotocicleta.obtenerMotocicleta(idModMotocicleta).getKilometraje();
        kilometrajeInicial = ListHistorialKilometraje.obtenerPrimerKilometraje(idModMotocicleta);
        while (kilometrajeActual+100>=auxKilometraje){
            existenciaNotificacion(kilometrajeInicial, auxKilometraje);
            auxKilometraje = auxKilometraje + 500;
            //i = i + 1;
            //ultimoHistKilometraje = ListHistorialNotificacion.ultimoHistorialNotificacion_kilometraje(idModMotocicleta);
        }
    }

    private void existenciaNotificacion(int kilometrajeIni, int auxKil){
        /*if(ListHistorialNotificacion.ultimoHistorialNotificacion_kilometraje(idModMotocicleta) != kilometrajeIni+auxKil && kilometrajeActual+100 >= kilometrajeIni+auxKil && ListHistorialNotificacion.ultimoHistorialNotificacion_kilometraje(idModMotocicleta) < kilometrajeActual ){
            crearNotificacion(kilometrajeIni+auxKil);
        }
        */
        if (ListHistorialNotificacion.verificarExistenciaNotificacionHistorial(idModMotocicleta, auxKil) != auxKil && auxKil>kilometrajeIni){
            crearNotificacion(auxKil);
        }
    }
    private void crearNotificacion(int kilometrajeMantenimiento){
        //[DEFINICION BASE DE DATOS]
        datos.getDb().beginTransaction();
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity().getApplicationContext());

        List<String> titulos = Arrays.asList(getResources().getStringArray(R.array.listaTitulosComponentesMantenimiento));
        List<String> descripciones = Arrays.asList(getResources().getStringArray(R.array.listaComponentesMantenimiento));
        for (int i=0; descripciones.size()>i;i++){
            HistorialNotificacion historialNotificacion = new HistorialNotificacion(null,titulos.get(i),kilometrajeMantenimiento, getDateTime(),null,null,descripciones.get(i), idModMotocicleta);
            datos.insertarHistorialNotificacion(historialNotificacion);
        }
        datos.getDb().setTransactionSuccessful();
        datos.getDb().endTransaction();
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
