package corp.carrizales.hefesto_002;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import corp.carrizales.hefesto_002.modelo.HistorialKilometraje;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;


/**
 * Created by Luis on 23/10/2017.
 */

public class Aceite_Fragment extends Fragment{

    private static final String TAG = "Aceite_fragment";

    private MCalendarView calendarioCambioAceite;

    private TextView txb_KilometrosDia;

    private String idModMotocicleta;

    private Calendar calendar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aceite_fragment,container,false);

        if (getArguments() !=null){
            idModMotocicleta = getArguments().getString("idModMotocicleta","000000");
            //porcentajeGasolina = getArguments().getInt("gasolina",0);
            //temperaturaMotor = getArguments().getInt("temperatura",0);
        }

        calendarioCambioAceite = (MCalendarView) view.findViewById(R.id.clendarioCambioAceite);

        txb_KilometrosDia = (TextView)view.findViewById(R.id.txb_KilometrosDia);

        calcularKilometrosDia();

        return view;
    }

    public void seleccionarFechaCambioAceite(int diasContados, int color){
        int auxAño;
        int auxMes;
        int auxDia;

        calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime()); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, diasContados);  // numero de días a añadir, o restar en caso de días<0

        auxAño = calendar.get(Calendar.YEAR);
        auxMes = calendar.get(Calendar.MONTH)+1; ////ESTA FUNCION DEVUELVE EL MES DEL 0-11 Y EL API TRABJA CON MESES DEL 1-12
        auxDia = calendar.get(Calendar.DAY_OF_MONTH);

        calendarioCambioAceite.markDate(new DateData(auxAño,auxMes,auxDia).setMarkStyle(new MarkStyle(MarkStyle.RIGHTSIDEBAR, color)));

                /// EJEMPLO COLOCAR EN EL CALENDARIO
                //calendarioCambioAceite.markDate(new DateData(2018, 6, 1).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.GREEN)));
                //calendarioCambioAceite.markDate(2018,6,2);
    }
    public void calcularKilometrosDia(){
        int punteroA = 0;
        int punteroB = 0;
        int promedio = 0;

        int AuxcontadorDias = 0;
        int AuxcontadorDias1 = 0;
        int AuxcontadorDias2 = 0;

        int kilometrajeActual = ListMotocicleta.obtenerMotocicleta(idModMotocicleta).getKilometraje();
        int kilometrajeMeta = ListHistorialNotificacion.obtenerUltimoHistorialNotificacion_Kilometraje(idModMotocicleta);

        ArrayList<HistorialKilometraje> historialKilometrajes;
        ArrayList<Integer> listaKilometrosDia = new ArrayList<>();

        historialKilometrajes = ListHistorialKilometraje.obtenerHistorialKilometraje_KilometrosDia(idModMotocicleta);

        /*
        HistorialKilometraje historialKilometraje = new HistorialKilometraje(null,1000,null,null);
        HistorialKilometraje historialKilometraje01 = new HistorialKilometraje(null,1100,null,null);
        HistorialKilometraje historialKilometraje02 = new HistorialKilometraje(null,1200,null,null);
        HistorialKilometraje historialKilometraje03 = new HistorialKilometraje(null,1300,null,null);
        HistorialKilometraje historialKilometraje04 = new HistorialKilometraje(null,1400,null,null);
        HistorialKilometraje historialKilometraje05 = new HistorialKilometraje(null,1500,null,null);

        historialKilometrajes.add(historialKilometraje);
        historialKilometrajes.add(historialKilometraje01);
        historialKilometrajes.add(historialKilometraje02);
        historialKilometrajes.add(historialKilometraje03);
        historialKilometrajes.add(historialKilometraje04);
        historialKilometrajes.add(historialKilometraje05);
        */

        for(int i = 0; historialKilometrajes.size()>i; i++){
            if (i+1<historialKilometrajes.size()){
                punteroA = historialKilometrajes.get(i).getKilometraje();
                punteroB = historialKilometrajes.get(i+1).getKilometraje();
            }

            listaKilometrosDia.add(punteroB-punteroA);
        }

        for (int i =0; listaKilometrosDia.size()>i; i++){
            promedio = promedio + listaKilometrosDia.get(i);
        }
        //promedio = promedio/listaKilometrosDia.size();
        promedio =75;

        AuxcontadorDias = contadorDiasCambiosAceite(kilometrajeActual, kilometrajeMeta, promedio);
        AuxcontadorDias1 = contadorDiasCambiosAceite(kilometrajeActual, kilometrajeMeta+500, promedio);
        AuxcontadorDias2 = contadorDiasCambiosAceite(kilometrajeActual, kilometrajeMeta+1000, promedio);

        seleccionarFechaCambioAceite(AuxcontadorDias, Color.YELLOW);
        seleccionarFechaCambioAceite(AuxcontadorDias1, Color.YELLOW);
        seleccionarFechaCambioAceite(AuxcontadorDias2, Color.YELLOW);

        txb_KilometrosDia.setText("Km/dia(aprox): "+promedio);


    }
    public int contadorDiasCambiosAceite(int kilometrajeActual, int kilometrajeMeta, int promedioKMDia){
        int contadorDias = 0;
        while (kilometrajeActual<kilometrajeMeta){
            contadorDias = contadorDias+1;
            kilometrajeActual = kilometrajeActual+promedioKMDia;
        }
        return contadorDias;
    }
}
