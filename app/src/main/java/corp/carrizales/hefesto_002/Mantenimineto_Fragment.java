package corp.carrizales.hefesto_002;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import corp.carrizales.hefesto_002.modelo.DetalleComponenteMotocicleta;
import corp.carrizales.hefesto_002.modelo.HistorialNotificacion;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Luis on 09/12/2017.
 */

public class Mantenimineto_Fragment extends Fragment  implements OnChartGestureListener, OnChartValueSelectedListener{

    View view;
    View popupView;

    PopupWindow popupWindow_Componete;

    PopupWindow popupWindow_PieChart;

    private LineChart mChart;

    String[] gruposNotificaciones = new String[] {"Realizados", "No Realizados"}; //COMPONENTE DEL PIECHART


    Button btn_Cerrar;//COMPONENTE UTILIZADO POR AMBOS POPUP

    Spinner spnrOpcionesAños;
    Spinner spnrOpcionesMeses;
    String spinerAuxAño;
    String spinerAuxMes = "Seleccionar";

    ArrayAdapter<String> comboAdapter;

    private static final String TAG = "Mantenimineto_Fragment";
    private String idModMotocicleta;

    int kilometrajeSeleccionado; //RECUPERA KILOMETRAJE SELECCIONADO DEL LINE CHART DEL MANTENIMEINTO FRAGMENT

    private float numeroMantenimientos; //RECUPERA EL NUMERO DE COMPONENTES SOMETIDOS A MANTENIMIENTO

    private Context ctx;

    AnimationDrawable frameanimation;

    // <editor-fold desc="BUTTONS MANTENIMIENTO FRAGMENT">
    Button btn_bujia;
    Button btn_ruedas;
    Button btn_aceite_motor;
    Button btn_pernos_chasis;
    Button btn_cadena;
    Button btn_pernos_escape;
    Button btn_filtro_aire;
    // </editor-fold>

    // <editor-fold desc="IMAGEVIEWS MANTENIMIENTO FRAGMENT">
    private ImageView img_mantenimieto;

    ImageView img_PointToBujia;
    ImageView img_PointToRueda;
    ImageView img_pointToceiteMotor;
    ImageView img_pointToPernosChasis;
    ImageView img_pointToFiltro_aire;
    ImageView img_pointToPernosEscape;
    ImageView img_pointToCadena;
    // </editor-fold>

    // <editor-fold desc="POPUP COMPONENTE MOTOCICLETA">
    private boolean auxpopupabirto_componenteM = true;

    List<DetalleComponenteMotocicleta> listdetCompMotocicleta; //CARGA EL POPUP CON IMGANEES Y DESCRIPCIONES

    LayoutInflater layoutInflater_componenteM;

    ImageView imgComponente;

    TextView txb_tituloComponente;
    TextView txb_descripcionComponente;
    // </editor-fold>

    // <editor-fold desc="LISTAS LINECHART">
    private ArrayList<String> listKilometrajes;
    private ArrayList<Float> listporcentajes;
    // </editor-fold>

    // <editor-fold desc="POPUP NOTIFICACION">
    boolean auxpopupabirtoGraficaMantenimiento = true;


    private int[] colors_pieChart = new int[] {Color.GREEN, Color.YELLOW};                //COMPONENTE DEL PIECHART

    LayoutInflater layoutInflaterGraficaMantenimeinto;
    // </editor-fold>

    // <editor-fold desc="LIST POPUP NOTIFICACION">
    private List<HistorialNotificacion> listdetHistorialNotificacion;

    private ListView listView;

    ArrayAdapter<HistorialNotificacion> adapter;
    // </editor-fold>


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() !=null){
            idModMotocicleta = getArguments().getString("idModMotocicleta","000000");
            //porcentajeGasolina = getArguments().getInt("gasolina",0);
            //temperaturaMotor = getArguments().getInt("temperatura",0);
        }



        view = inflater.inflate(R.layout.mantenimiento_fragment, container, false);

        mChart = (LineChart)view.findViewById(R.id.lineChart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        spnrOpcionesAños = (Spinner)view.findViewById(R.id.spnr_año);
        spnrOpcionesMeses = (Spinner)view.findViewById(R.id.spnr_meses);

        numeroMantenimientos = Arrays.asList(getResources().getStringArray(R.array.listaComponentesMantenimiento)).size();

        cargarSpinnerAño();
        spnrOpcionesAños.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String descripcion;
                spinerAuxAño = (String) parent.getItemAtPosition(position);
                if ("Todo".equals(spinerAuxAño)) {
                    cargarlistasGraficaMantenimiento();
                    if (listKilometrajes.size()==0){
                        listKilometrajes.add("");
                        listporcentajes.add((float)0);
                        descripcion = "No existen datos aún";
                    }
                    else {
                        descripcion = "Mantenimientos realizados";
                        if(spinerAuxMes.equals("Seleccionar") != true && spinerAuxAño.equals("Todo") != true){
                            cargarlistasGraficaMantenimiento_Año_Mes(spinerAuxAño, spinerAuxMes);
                        }
                    }
                    createCharts_LineChart(descripcion);
                }
                else {
                    cargarlistasGraficaMantenimiento_Año(spinerAuxAño);
                    cargarSpinnerMeses(spinerAuxAño);
                    if (listKilometrajes.size()==0){
                        listKilometrajes.add("");
                        listporcentajes.add((float)0);
                        Toast.makeText(parent.getContext(),
                                "No existe datos del "+spinerAuxAño, Toast.LENGTH_SHORT).show();
                    }
                    createCharts_LineChart("Mantenimientos realizados");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnrOpcionesMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinerAuxMes = (String) parent.getItemAtPosition(position);
                if (spinerAuxAño.equals("Todo") != true && spinerAuxMes.equals("Seleccionar") != true){
                    cargarlistasGraficaMantenimiento_Año_Mes(spinerAuxAño, spinerAuxMes);
                    createCharts_LineChart("Mantenimientos realizados");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_bujia = (Button)view.findViewById(R.id.btn_bujia);
        btn_ruedas = (Button)view.findViewById(R.id.btn_ruedas);
        btn_aceite_motor = (Button)view.findViewById(R.id.btn_aceite_motor);
        btn_pernos_chasis = (Button)view.findViewById(R.id.btn_pernos_chasis);
        btn_cadena = (Button)view.findViewById(R.id.btn_cadena);
        btn_pernos_escape = (Button)view.findViewById(R.id.btn_pernos_escape);
        btn_filtro_aire = (Button)view.findViewById(R.id.btn_filtro_aire);

        img_mantenimieto = (ImageView)view.findViewById(R.id.img_mantenimieto);

        img_PointToBujia = (ImageView)view.findViewById(R.id.img_point_to_bujia);
        img_PointToRueda = (ImageView)view.findViewById(R.id.img_point_to_rueda1);
        img_pointToceiteMotor = (ImageView)view.findViewById(R.id.img_point_to_aceite_motor);
        img_pointToPernosChasis = (ImageView)view.findViewById(R.id.img_point_to_pernos_chasis);
        img_pointToFiltro_aire = (ImageView)view.findViewById(R.id.img_point_to_filtro_aire);
        img_pointToPernosEscape = (ImageView)view.findViewById(R.id.img_point_to_pernos_escape);
        img_pointToCadena = (ImageView)view.findViewById(R.id.img_point_to_cadena);
        esuchar_btn_bujia(btn_bujia,img_PointToBujia);
        esuchar_btn_ruedas(btn_ruedas,img_PointToRueda);
        esuchar_btn_aceite_motor(btn_aceite_motor,img_pointToceiteMotor);
        esuchar_btn_pernos_chasis(btn_pernos_chasis,img_pointToPernosChasis);
        esuchar_btn_cadena(btn_cadena,img_pointToCadena);
        esuchar_btn_pernos_escape(btn_pernos_escape,img_pointToPernosEscape);
        esuchar_btn_filtro_aire(btn_filtro_aire,img_pointToFiltro_aire);

        return view;
    }

    private List<DetalleComponenteMotocicleta> getListDetCompMotocicleta(){
        listdetCompMotocicleta = new ArrayList<DetalleComponenteMotocicleta>();

        List<String> titulos = Arrays.asList(getResources().getStringArray(R.array.listaTitulosComponentesMantenimiento));

        List<String> descripciones = Arrays.asList(getResources().getStringArray(R.array.listaComponentesMantenimiento));


        int [] imagenes = new int[]{
                R.drawable.bujia
                , R.drawable.aceite
                , R.drawable.filtro
                , R.drawable.cadena
                , R.drawable.rueda1
                , R.drawable.t_1
                , R.drawable.t2
        };

        for (int i = 0; i<imagenes.length; i++){
            listdetCompMotocicleta.add(getDetCompMotocicleta(titulos.get(i), descripciones.get(i),imagenes[i]));
        }

        return listdetCompMotocicleta;
    }

    private DetalleComponenteMotocicleta getDetCompMotocicleta(String titulo, String descripcion, int imagen){
        return new DetalleComponenteMotocicleta(titulo, descripcion, imagen);
    }

    private Chart getSameChart_LineChart(Chart chart, String description, int textcolor, int background, int animatey){
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textcolor);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animatey);

        return chart;
    }
    private ArrayList<Entry>getLineEntries(){
        ArrayList<Entry>entries = new ArrayList<>();
        for (int i=0; i<listporcentajes.size(); i++){
            entries.add(new Entry(i, listporcentajes.get(i)));
        }
        return entries;
    }
    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(listKilometrajes));
    }
    private void axisLeft(YAxis axis){
        axis.setAxisMinimum(0);
        axis.setAxisMaximum(100);
    }
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }
    public void createCharts_LineChart(String descripcion){
        mChart = (LineChart) getSameChart_LineChart(mChart,descripcion,Color.RED, Color.WHITE, 3000);
        mChart.setDrawGridBackground(true);
        mChart.setData(getLineData());
        //mChart.setGridBackgroundColor(Color.GRAY); //COLOR FONDO DE LAS GRAFICAS
        axisX(mChart.getXAxis());
        axisLeft(mChart.getAxisLeft());
        axisRight(mChart.getAxisRight());
    }
    private LineDataSet getData_LineChart(LineDataSet dataSet){

        dataSet.setFillColor(Color.GREEN); //COLOR DE LAS BARRAS

        dataSet.setColor(Color.BLACK);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawFilled(true);

        return dataSet;
    }

    private LineData getLineData(){
        LineDataSet lineDataSet = getData_LineChart(new LineDataSet(getLineEntries(),"Porcentajes de cumplimiento de mantenimientos"));

        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: "+ me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture:" + lastPerformedGesture);
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP){
            mChart.highlightValue(null);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress","Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
        //un toque
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: "
                + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        /*Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX()
                + ", high: " + mChart.getHighestVisibleX());

        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin()
                + ", xmax: " + mChart.getXChartMax()
                + ", ymin: " + mChart.getYChartMin()
                + ", ymax: " + mChart.getYChartMax());
         */

        if(auxpopupabirtoGraficaMantenimiento){
            layoutInflaterGraficaMantenimeinto = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflaterGraficaMantenimeinto.inflate(R.layout.popud_grafica_mantenimiento, null);
            PieChart pieChart =(PieChart)popupView.findViewById(R.id.pieChart);
            pieChart.setOnChartValueSelectedListener(this);
            pieChart.setOnChartGestureListener(this);

            popupWindow_PieChart = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            listView = (ListView)popupView.findViewById(R.id.listView_Componetes_PieChart);
            btn_Cerrar = (Button) popupView.findViewById(R.id.id_cerrar);

            kilometrajeSeleccionado = Integer.parseInt(listKilometrajes.get(Math.round(e.getX())));
            float aux_procentaje = 100/numeroMantenimientos;
            ArrayList<Float> arrayList_PorcentajesPieChart = new ArrayList<>() ;                  //COMPONENTE DEL PIECHART
            arrayList_PorcentajesPieChart.add((float)getListdetHistorialNotificaciones_CompletaRealizadas(kilometrajeSeleccionado).size()*(aux_procentaje));
            arrayList_PorcentajesPieChart.add((float)getListdetHistorialNotificaciones_CompletaNoRealizadas(kilometrajeSeleccionado).size()*(aux_procentaje));

            createChartPie(arrayList_PorcentajesPieChart, pieChart);
            btn_Cerrar.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auxpopupabirtoGraficaMantenimiento = true;

                    popupWindow_PieChart.dismiss();
                }});

            popupWindow_PieChart.showAsDropDown(img_mantenimieto, 50, -460);
            auxpopupabirtoGraficaMantenimiento = false;

            pieChartListener(pieChart, arrayList_PorcentajesPieChart);
        }


        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                "Eje Y: "+e.getY()+" Eje X: "+ listKilometrajes.get(Math.round(e.getX())), Toast.LENGTH_SHORT);

        toast1.show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    //CREAR CHAR PIE
    private Chart getSameChart_PieChart(Chart chart, String description, int textcolor, int background, int animatey){
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textcolor);
        chart.getDescription().setTextSize(10);
        chart.setBackgroundColor(background);
        chart.animateY(animatey);
        legend(chart);
        return chart;
    }
    private void legend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry>entries = new ArrayList<>();
        for (int i=0; i<gruposNotificaciones.length; i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors_pieChart[i];
            entry.label = gruposNotificaciones[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }
    private ArrayList<PieEntry>getPieEntries(ArrayList<Float> arrayList){
        ArrayList<PieEntry>entries = new ArrayList<>();
        for (int i=0; i<arrayList.size(); i++){
            entries.add(new PieEntry(arrayList.get(i)));
        }
        return entries;
    }
    public void createChartPie(ArrayList<Float> arrayList, PieChart pieChart){
        pieChart = (PieChart) getSameChart_PieChart(pieChart,"",Color.GRAY,Color.WHITE,3000);
        pieChart.setHoleRadius(10);
        //pieChart.setTransparentCircleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(12);
        pieChart.setData(getPieData(arrayList));
        pieChart.setDrawHoleEnabled(false);//CIRCULO CENTRAL DEL PIECHART
    }
    private DataSet getData_PieChart(DataSet dataSet){
        dataSet.setColors(colors_pieChart);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }
    private PieData getPieData(ArrayList<Float> arrayList){
        PieDataSet pieDataSet = (PieDataSet)getData_PieChart(new PieDataSet(getPieEntries(arrayList),""));
        pieDataSet.setSliceSpace(4);
        pieDataSet.setValueFormatter(new PercentFormatter());
        return new PieData(pieDataSet);
    }

    public void cargarlistasGraficaMantenimiento(){
        float aux;
        numeroMantenimientos = Arrays.asList(getResources().getStringArray(R.array.listaComponentesMantenimiento)).size();
        listKilometrajes = new ArrayList<>();
        listKilometrajes = ListHistorialNotificacion.listaNotificacion_kilometrajes(idModMotocicleta);
        listporcentajes = new ArrayList<>();
        for (int i = 0; i<listKilometrajes.size(); i++){
            aux = ListHistorialNotificacion.listaNotificacion_porcentajes(idModMotocicleta, listKilometrajes.get(i));
            listporcentajes.add(aux*(100/numeroMantenimientos));
        }
    }
    public void cargarlistasGraficaMantenimiento_Año(String año){
        float aux;
        listKilometrajes = new ArrayList<>();
        listKilometrajes = ListHistorialNotificacion.listaNotificacion_kilometrajes_año(idModMotocicleta, año);
        listporcentajes = new ArrayList<>();
        for (int i = 0; i<listKilometrajes.size(); i++){
            aux = ListHistorialNotificacion.listaNotificacion_porcentajes(idModMotocicleta, listKilometrajes.get(i));
            listporcentajes.add(aux*(100/numeroMantenimientos));
        }
    }
    public void cargarlistasGraficaMantenimiento_Año_Mes(String año, String mes){
        float aux;
        listKilometrajes = new ArrayList<>();
        listKilometrajes = ListHistorialNotificacion.listaNotificacion_kilometrajes_año_mes(idModMotocicleta, año, mes);
        listporcentajes = new ArrayList<>();
        for (int i = 0; i<listKilometrajes.size(); i++){
            aux = ListHistorialNotificacion.listaNotificacion_porcentajes(idModMotocicleta, listKilometrajes.get(i));
            listporcentajes.add(aux*(100/numeroMantenimientos));
        }
    }

    ///CARGAR LIST ADAPTER PARA DETALLE DEL PIECHART
    private List<HistorialNotificacion> getListdetHistorialNotificaciones_CompletaRealizadas(int kilometraje){
        listdetHistorialNotificacion = new ArrayList<HistorialNotificacion>();

        listdetHistorialNotificacion = ListHistorialNotificacion.listaHistorialNotificaciones_CompletoRealizados(idModMotocicleta,kilometraje);

        return listdetHistorialNotificacion;
    }
    private List<HistorialNotificacion> getListdetHistorialNotificaciones_CompletaNoRealizadas(int kilometraje){
        listdetHistorialNotificacion = new ArrayList<HistorialNotificacion>();

        listdetHistorialNotificacion = ListHistorialNotificacion.listaHistorialNotificaciones_CompletoNoRealizados(idModMotocicleta,kilometraje);

        return listdetHistorialNotificacion;
    }

    public void esuchar_btn_bujia(Button btn_bujia, final ImageView imgview){
        btn_bujia.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(0, imgview);
            }});
    }
    public void esuchar_btn_ruedas(Button btn_ruedas, final ImageView imgview){
        btn_ruedas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(4, imgview);
            }
        });
    }
    public void esuchar_btn_aceite_motor(Button btn_aceite_motor, final ImageView imgview){
        btn_aceite_motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(1, imgview);
            }
        });
    }
    public void esuchar_btn_pernos_chasis(Button btn_pernos_chasis, final ImageView imgview){
        btn_pernos_chasis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(5, imgview);
            }
        });
    }
    public void esuchar_btn_cadena(Button btn_cadena, final ImageView imgview){
        btn_cadena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(3, imgview);
            }
        });
    }
    public void esuchar_btn_pernos_escape(Button btn_pernos_escape, final ImageView imgview){
        btn_pernos_escape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(6, imgview);
            }
        });
    }
    public void esuchar_btn_filtro_aire(Button btn_filtro_aire, final ImageView imgview){
        btn_filtro_aire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPopup(2,imgview);
            }
        });

    }

    public void cargarSpinnerAño(){
        List<String> listAños = new ArrayList<>();
        listAños.add("Todo");
        listAños.addAll(ListHistorialNotificacion.listaHistorialNotificacion_años(idModMotocicleta));
        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listAños);
        spnrOpcionesAños.setAdapter(comboAdapter);
    }
    public void cargarSpinnerMeses(String año){
        List<String> listMeses = new ArrayList<>();
        listMeses.add("Seleccionar");
        listMeses.addAll(ListHistorialNotificacion.listaHistorialNotificacion_meses(idModMotocicleta, año));
        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listMeses);
        spnrOpcionesMeses.setAdapter(comboAdapter);
    }

    public void generarPopup(int numeroComponente, final ImageView imageView){
        if (auxpopupabirto_componenteM ){
            animacionParpadeo(imageView);
            layoutInflater_componenteM = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater_componenteM.inflate(R.layout.popud_componentes_motocicleta, null);
            popupWindow_Componete = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);

            txb_tituloComponente = (TextView)popupView.findViewById(R.id.txb_titulo_componente);
            imgComponente = (ImageView)popupView.findViewById(R.id.imgComponente);
            txb_descripcionComponente = (TextView)popupView.findViewById(R.id.txb_descripcion_componete);
            btn_Cerrar = (Button) popupView.findViewById(R.id.id_cerrar);

            txb_tituloComponente.setText(getListDetCompMotocicleta().get(numeroComponente).getTitulo());
            imgComponente.setImageResource(getListDetCompMotocicleta().get(numeroComponente).getId_imagen());
            txb_descripcionComponente.setText(getListDetCompMotocicleta().get(numeroComponente).getDescripcion());

            btn_Cerrar.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auxpopupabirto_componenteM = true;

                    ((AnimationDrawable)(imageView.getBackground())).stop();
                    imageView.setBackgroundDrawable(null);

                    popupWindow_Componete.dismiss();
                }});
            popupWindow_Componete.showAsDropDown(img_mantenimieto, 90, -0);
            auxpopupabirto_componenteM = false;
        }
    }
    private void animacionParpadeo(ImageView imageView){
        imageView.setBackgroundResource(R.drawable.loading_cricle);
        frameanimation = (AnimationDrawable)imageView.getBackground();
        frameanimation.start();
    }

    private void pieChartListener(PieChart pieChart, final ArrayList<Float> arrayList_PorcentajesPieChart){
        pieChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast toast2 = Toast.makeText(getActivity().getApplicationContext(),
                        "Eje Y: "+e.getY(), Toast.LENGTH_SHORT);
                toast2.show();

                if (arrayList_PorcentajesPieChart.get(0)==e.getY()){
                    adapter = new ListAdapterDetCompMot(getActivity().getApplicationContext(), getListdetHistorialNotificaciones_CompletaRealizadas(kilometrajeSeleccionado));
                    listView.setAdapter(adapter);
                }
                else
                {
                    adapter = new ListAdapterDetCompMot(getActivity().getApplicationContext(), getListdetHistorialNotificaciones_CompletaNoRealizadas(kilometrajeSeleccionado));
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}
