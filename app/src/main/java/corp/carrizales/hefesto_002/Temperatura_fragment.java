package corp.carrizales.hefesto_002;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sccomponents.gauges.ScArcGauge;
import com.sccomponents.gauges.ScGauge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import corp.carrizales.hefesto_002.modelo.HistorialTemperatura;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 23/10/2017.
 */

public class Temperatura_fragment extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private static final String TAG = "Temperatura_fragment";

    OperacionesBaseDatos datos;

    private String idModMotocicleta;

    private String auxAñoSelecccionado;
    private String auxMesSeleccionado;

    private LineChart mChart;

    Spinner spnr_tipoAceite;
    Spinner spnr_aceitesSeleccionados;

    Spinner spnr_añosLineChart;
    Spinner spnr_mesesLineChart;

    ScArcGauge gauge;
    ImageView indicator;
    TextView counter;

    Button btn_analizarTemperatura;

    TextView txb_descripcionAceiteSAE;

    ArrayAdapter<String> comboAdapter;

    String auxTipoAceiteSelecccionado;

    private ArrayList<String> dias_LineChart;
    private ArrayList<Float> temperaturas_LineChart;

    private int temperaturaMotor = 0;

    private int tempMax_Aceite;
    private String aceiteSeleccionado;

    // <editor-fold desc="COLORES INDICADOR TEMPERATURA">
    String verde_azulado = "#00FF80";
    String verde_fosforecente = "#00FF40";
    String amarillo_patito = "#FFFF00";
    String rojo_peligro = "#F51319";
    String naraja_peligro = "#FE9A2E";

    private int[] col_aceite_5W_30AP = new int[]{   Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(amarillo_patito),
                                                Color.parseColor(amarillo_patito), Color.parseColor(rojo_peligro),
                                                Color.parseColor(rojo_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_10W_40AP = new int[]{  Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(amarillo_patito), Color.parseColor(amarillo_patito),
                                                Color.parseColor(rojo_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_20W_50AP = new int[]{  Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(amarillo_patito),
                                                Color.parseColor(naraja_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_0W_30SS = new int[]{   Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(amarillo_patito),
                                                Color.parseColor(amarillo_patito), Color.parseColor(rojo_peligro),
                                                Color.parseColor(rojo_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_5W_40SS = new int[]{   Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(amarillo_patito), Color.parseColor(amarillo_patito),
                                                Color.parseColor(naraja_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_10W_50SS = new int[]{  Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(amarillo_patito),
                                                Color.parseColor(amarillo_patito), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_20W_50SS = new int[]{  Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(amarillo_patito), Color.parseColor(amarillo_patito),
                                                Color.parseColor(rojo_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_0W_40FS = new int[]{   Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(amarillo_patito), Color.parseColor(amarillo_patito),
                                                Color.parseColor(naraja_peligro), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_15W_50FS = new int[]{  Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(amarillo_patito),
                                                Color.parseColor(amarillo_patito), Color.parseColor(rojo_peligro)};

    private int[] col_aceite_15W_30FS = new int[]{  Color.parseColor(verde_azulado), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(verde_fosforecente), Color.parseColor(verde_fosforecente),
                                                Color.parseColor(amarillo_patito), Color.parseColor(amarillo_patito),
                                                Color.parseColor(amarillo_patito), Color.parseColor(rojo_peligro),
                                                Color.parseColor(rojo_peligro), Color.parseColor(rojo_peligro)};
    // </editor-fold>

    float porcentajeTemperatura;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temperatura_fragment,container,false);

        datos = OperacionesBaseDatos.obtenerInstancia(getActivity().getApplicationContext());

        if (getArguments() !=null){
            idModMotocicleta = getArguments().getString("idModMotocicleta","000000");
            //porcentajeGasolina = getArguments().getInt("gasolina",0);
            temperaturaMotor = getArguments().getInt("temperatura",0);
        }
        mChart = (LineChart)view.findViewById(R.id.lineChart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        gauge = (ScArcGauge) view.findViewById(R.id.gauge1);
        indicator = (ImageView) view.findViewById(R.id.indicator1);
        counter = (TextView) view.findViewById(R.id.counter1);

        vistaTemperatura();

        spnr_tipoAceite = (Spinner)view.findViewById(R.id.spnr_tipos_aceite);
        cargarSpinner_TiposAceites();

        spnr_aceitesSeleccionados = (Spinner) view.findViewById(R.id.spnr_aceites_seleccionados);
        spnr_aceitesSeleccionadosListener(); ///varios aceites


        txb_descripcionAceiteSAE = (TextView)view.findViewById(R.id.txb_descricicon_aceiteSAE);

        btn_analizarTemperatura = (Button)view.findViewById(R.id.btn_realizar_analizar_temperatura);
        btn_analizarTemperatura(btn_analizarTemperatura);

        spnr_añosLineChart = (Spinner)view.findViewById(R.id.spnr_añosLineChartTemp);
        spnr_mesesLineChart = (Spinner)view.findViewById(R.id.spnr_mesesLineChartTemp);
        cargarSpinner_LineCharTemp_Años();
        listenerSpinner_LineCharTemp_Años();
        listenerSpinner_LineChartTemp_Meses();


        return view;
    }

    public void vistaTemperatura(){

        porcentajeTemperatura = (float) temperaturaMotor/235*100; //REGULAR EL ANGULO DEL INDICADOR
        gauge.setHighValue(porcentajeTemperatura);
        if (porcentajeTemperatura == 0){
            gauge.setHighValue(1);
        }else {
            gauge.setHighValue(porcentajeTemperatura);
        }
        //gauge.destroyDrawingCache();//BORRAR EL CACHE DE COLORES
        //gauge.setStrokeColors(listaColors);
        //gauge.setStrokeColorsMode(ScFeature.ColorsMode.GRADIENT);

        // Each time I will change the value I must write it inside the counter text.
        gauge.setOnEventListener(new ScGauge.OnEventListener() {
            @Override
            public void onValueChange(float lowValue, float highValue) {
                // Convert the percentage value in an angle

                float angle = gauge.percentageToAngle(highValue);
                indicator.setRotation(angle);

                // Write the value
                counter.setText((int)temperaturaMotor + "");
            }
        });
    }

    private Chart getSameChart(Chart chart, String description, int textcolor, int background, int animatey){
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textcolor);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animatey);

        return chart;
    }
    private void legend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i< dias_LineChart.size(); i++){
            LegendEntry entry = new LegendEntry();
            //entry.formColor = colors[i];
            entry.label = dias_LineChart.get(i);
            entries.add(entry);
        }
        legend.setCustom(entries);
    }
    private ArrayList<Entry>getLineEntries(){
        ArrayList<Entry>entries = new ArrayList<>();
        for (int i = 0; i< temperaturas_LineChart.size(); i++){
            entries.add(new Entry(i, temperaturas_LineChart.get(i)));
        }
        return entries;
    }
    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(dias_LineChart));
    }
    private void axisLeft(YAxis axis){
        axis.setAxisMinimum(0);
        axis.setAxisMaximum(190);


        //////////////ARREGLAR DESPUES EL LIMITE DE TEMPERATURA SEGUN EL ACEITE COMO PLUS
        LimitLine upper_limit = new LimitLine(190, "Temperatura Maxima");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        axis.removeAllLimitLines();
        axis.addLimitLine(upper_limit);
        axis.setAxisMaxValue(220f);
        axis.setAxisMinValue(-50f);
        axis.enableGridDashedLine(10f, 10f, 0f);
        axis.setDrawZeroLine(false);

        axis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);
        ///////////////////////////////////////////////////////////////////////////////////////
    }
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }
    public void createCharts(String descripcion){
        mChart = (LineChart)getSameChart(mChart,descripcion,Color.BLACK, Color.WHITE, 3000);
        mChart.setDrawGridBackground(false);
        mChart.setData(getLineData());

        axisX(mChart.getXAxis());
        axisLeft(mChart.getAxisLeft());
        axisRight(mChart.getAxisRight());
    }
    private LineDataSet getData(LineDataSet dataSet){
        dataSet.setColors(Color.RED);//COLOR DE LAS LINEAS DE LA GRAFICA
        //dataSet.setValueTextSize(Color.WHITE);
        //dataSet.setValueTextSize(10);

        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawFilled(false);//PINAR CON COLOR DEBAJO DE LAS LINEAS TRAZADAS

        return dataSet;
    }
    private LineData getLineData(){
        LineDataSet lineDataSet = getData(new LineDataSet(getLineEntries(),"Temperauras del motor"));

        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
    }

    public void cargarSpinner_TiposAceites(){
        //final List<String> listAños = Arrays.asList(getResources().getStringArray(R.array.tiposAceite));

        final List<String>listAceitesMineral = Arrays.asList(getResources().getStringArray(R.array.aceites_mineral));
        final List<String>listAceitesSemiSintetico = Arrays.asList(getResources().getStringArray(R.array.aceites_semi_sintetico));
        final List<String>listAceitesFullSintetico = Arrays.asList(getResources().getStringArray(R.array.aceites_full_sintetico));

        spnr_tipoAceite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auxTipoAceiteSelecccionado = (String) parent.getItemAtPosition(position);
                switch (auxTipoAceiteSelecccionado) {
                    case "Mineral-AP":
                        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listAceitesMineral);
                        spnr_aceitesSeleccionados.setAdapter(comboAdapter);//Cargo el spinner con los datos
                        break;
                    case "SemiSintetico-SS":
                        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listAceitesSemiSintetico);
                        spnr_aceitesSeleccionados.setAdapter(comboAdapter);//Cargo el spinner con los datos
                        break;
                    case "FullSintetico-FS":
                        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listAceitesFullSintetico);
                        spnr_aceitesSeleccionados.setAdapter(comboAdapter);//Cargo el spinner con los datos
                        break;
                    default:
                        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listAceitesMineral);
                        spnr_aceitesSeleccionados.setAdapter(comboAdapter);//Cargo el spinner con los datos
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void spnr_aceitesSeleccionadosListener(){
        spnr_aceitesSeleccionados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gauge.destroyDrawingCache();
                String auxAceiteSelecccionado = (String) parent.getItemAtPosition(position);
                if (auxTipoAceiteSelecccionado.equals("Mineral-AP"))
                    switch (auxAceiteSelecccionado) {
                        case "SAE 20W-50 AP":
                            aceiteSeleccionado = "SAE 20W-50 AP";
                            gauge.setStrokeColors(col_aceite_20W_50AP);
                            tempMax_Aceite = 176;
                            break;
                        case "SAE 10W-40 AP":
                            aceiteSeleccionado = "SAE 10W-40 AP";
                            gauge.setStrokeColors(col_aceite_10W_40AP);
                            tempMax_Aceite = 160;
                            break;
                        case "SAE 5W-30 AP":
                            aceiteSeleccionado = "SAE 5W-30 AP";
                            gauge.setStrokeColors(col_aceite_5W_30AP);
                            tempMax_Aceite = 147;
                            break;
                    }
                else {
                    if(auxTipoAceiteSelecccionado.equals("SemiSintetico-SS")){
                        switch (auxAceiteSelecccionado){
                            case "SAE 20W-50 SS":
                                aceiteSeleccionado = "SAE 20W-50 SS";
                                tempMax_Aceite = 163;
                                gauge.setStrokeColors(col_aceite_20W_50SS);
                                break;
                            case "SAE 10W-50 SS":
                                aceiteSeleccionado = "SAE 10W-50 SS";
                                tempMax_Aceite = 174;
                                gauge.setStrokeColors(col_aceite_10W_50SS);
                                gauge.buildDrawingCache();
                                break;
                            case "SAE 5W-40 SS":
                                aceiteSeleccionado = "SAE 5W-40 SS";
                                tempMax_Aceite = 159;
                                gauge.setStrokeColors(col_aceite_5W_40SS);
                                break;
                            case "SAE 0W-30 SS":
                                aceiteSeleccionado = "SAE 0W-30 SS";
                                tempMax_Aceite = 142;
                                gauge.setStrokeColors(col_aceite_0W_30SS);
                                break;
                        }
                    }
                    else {
                        if(auxTipoAceiteSelecccionado.equals("FullSintetico-FS")){
                            switch (auxAceiteSelecccionado){
                                case "SAE 15W-50 FS":
                                    aceiteSeleccionado = "SAE 15W-50 FS";
                                    tempMax_Aceite = 181;
                                    gauge.setStrokeColors(col_aceite_15W_30FS);
                                    break;
                                case "SAE 15W-30 FS":
                                    aceiteSeleccionado = "SAE 15W-30 FS";
                                    tempMax_Aceite = 140;
                                    gauge.setStrokeColors(col_aceite_15W_30FS);
                                    break;
                                case "SAE 0W-40 FS":
                                    aceiteSeleccionado = "SAE 0W-40 FS";
                                    tempMax_Aceite = 168;
                                    gauge.setStrokeColors(col_aceite_0W_40FS);
                                    break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void btn_analizarTemperatura(Button btn_analizarTemperatura){
        btn_analizarTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*datos.getDb().beginTransaction();
                HistorialTemperatura historialTemperatura = new HistorialTemperatura(null, 33, "2017-08-04",idModMotocicleta);
                HistorialTemperatura historialTemperatura01 = new HistorialTemperatura(null, 80, "2017-08-05",idModMotocicleta);
                HistorialTemperatura historialTemperatura02 = new HistorialTemperatura(null, 100, "2017-08-06",idModMotocicleta);
                HistorialTemperatura historialTemperatura03 = new HistorialTemperatura(null, 150, "2017-08-07",idModMotocicleta);
                HistorialTemperatura historialTemperatura04 = new HistorialTemperatura(null, 190, "2017-09-04",idModMotocicleta);
                HistorialTemperatura historialTemperatura05 = new HistorialTemperatura(null, 77, "2018-09-05",idModMotocicleta);
                HistorialTemperatura historialTemperatura06 = new HistorialTemperatura(null, 69, "2018-10-04",idModMotocicleta);
                HistorialTemperatura historialTemperatura07 = new HistorialTemperatura(null, 53, "2018-11-05",idModMotocicleta);
                HistorialTemperatura historialTemperatura08 = new HistorialTemperatura(null, 75, "2018-11-06",idModMotocicleta);
                HistorialTemperatura historialTemperatura09 = new HistorialTemperatura(null, 79, "2018-11-07",idModMotocicleta);

                datos.insertarHistorialTemperatura(historialTemperatura);
                datos.insertarHistorialTemperatura(historialTemperatura01);
                datos.insertarHistorialTemperatura(historialTemperatura02);
                datos.insertarHistorialTemperatura(historialTemperatura03);
                datos.insertarHistorialTemperatura(historialTemperatura04);
                datos.insertarHistorialTemperatura(historialTemperatura05);
                datos.insertarHistorialTemperatura(historialTemperatura06);
                datos.insertarHistorialTemperatura(historialTemperatura07);
                datos.insertarHistorialTemperatura(historialTemperatura08);
                datos.insertarHistorialTemperatura(historialTemperatura09);
                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();*/
                if(temperaturaMotor>tempMax_Aceite){
                    txb_descripcionAceiteSAE.setText("La temperatura maxima recomendada en "+aceiteSeleccionado+" es de: "+tempMax_Aceite+"°c *Sobre paso la temperatura recomendad, deje enfriar el motor.*");
                }
                else{
                    if (temperaturaMotor <= tempMax_Aceite && temperaturaMotor>0){
                        txb_descripcionAceiteSAE.setText("La temperatura maxima recomendada en "+aceiteSeleccionado+" es de: "+tempMax_Aceite+"°c *La temperatura es optima.*");
                    }
                    else {
                        txb_descripcionAceiteSAE.setText("La temperatura maxima recomendada en "+aceiteSeleccionado+"es de: "+tempMax_Aceite+"°c *Se recomienda dejar encendido el motor por unos minutos antes de partir.*");
                    }
                }
            }
        });
    }
    /////PARA CAMBIAR EL INDICADOR A UNA NUEVA TEMPERATURA
                        /*temperaturaMotor = 190;
                        porcentajeTemperatura = (float) 190/235*100;
                        gauge.setHighValue(porcentajeTemperatura);
                        //counter.setText("190");
                        */
    public void cargarSpinner_LineCharTemp_Años(){
        List<String>listAños = new ArrayList<>();
        listAños.addAll(ListHistorialTemperatura.listaHistorialTemperatura_años(idModMotocicleta));
        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listAños);
        spnr_añosLineChart.setAdapter(comboAdapter);//Cargo el spinner con los datos
    }
    public void listenerSpinner_LineCharTemp_Años(){
        spnr_añosLineChart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auxAñoSelecccionado = (String) parent.getItemAtPosition(position);

                List<String>listmeses = new ArrayList<>();
                listmeses.addAll(ListHistorialTemperatura.listaHistorialNotificacion_meses(idModMotocicleta,auxAñoSelecccionado));
                comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listmeses);
                spnr_mesesLineChart.setAdapter(comboAdapter);//Cargo el spinner con los datos
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public  void listenerSpinner_LineChartTemp_Meses(){
        spnr_mesesLineChart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auxMesSeleccionado = (String) parent.getItemAtPosition(position);
                if(auxAñoSelecccionado != null && auxMesSeleccionado != null){
                    cargarListasLineChart(ListHistorialTemperatura.listaHistorialTemperaturas(idModMotocicleta, auxAñoSelecccionado, auxMesSeleccionado));

                    createCharts("Temperaturas maximas diarias");
                }
                else {
                    dias_LineChart = new ArrayList<>();
                    temperaturas_LineChart = new ArrayList<>();

                    dias_LineChart.add("");
                    temperaturas_LineChart.add((float)0);

                    createCharts("No existen datos");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cargarListasLineChart(ArrayList<HistorialTemperatura> historialTemperaturas){
        ArrayList<String> auxdias_LineChart = new ArrayList<>();
        ArrayList<Float> auxtemperaturas_LineChart = new ArrayList<>();
        for (int i = 0;i<historialTemperaturas.size();i++){
            auxdias_LineChart.add(historialTemperaturas.get(i).getFecha());
            auxtemperaturas_LineChart.add((float)historialTemperaturas.get(i).getTemperatura());///CAMBIAR A FLOAT EN LA BD
        }
        dias_LineChart = auxdias_LineChart;
        temperaturas_LineChart = auxtemperaturas_LineChart;
    }
}
