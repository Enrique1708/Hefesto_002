package corp.carrizales.hefesto_002;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.race604.drawable.wave.WaveDrawable;
import com.sccomponents.gauges.ScArcGauge;
import com.sccomponents.gauges.ScFeature;
import com.sccomponents.gauges.ScGauge;

import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;
import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by Luis on 23/10/2017.
 */

public class Panel_fragment extends Fragment{

    private static final String TAG = "Panel_fragment";

    private String idModMotocicleta;
    Motocicleta motocicleta = new Motocicleta();

    TextView textView0;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;

    WaveDrawable waveDrawable;

    static ImageView iconoSincr;

    private static int porcentajeGasolina;
    private static int temperaturaMotor = 0;
    private static float porcentajeTemperatura;

    private String kilometraje;
    private char[] kilometrajeChars;
    int logitudkilometrajeChars;


    private Button btn_chekEngine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,  Bundle savedInstanceState) {

        if (getArguments() !=null){
            idModMotocicleta = getArguments().getString("idModMotocicleta","000000");
            porcentajeGasolina = getArguments().getInt("gasolina",0);
            temperaturaMotor = getArguments().getInt("temperatura",0);
        }

        //Obtener kilometraje de la motocicleta
        motocicleta = ListMotocicleta.obtenerMotocicleta(idModMotocicleta);
        kilometraje = ListHistorialKilometraje.obtenerUltimoKilometraje(idModMotocicleta)+"";

        View view = inflater.inflate(R.layout.panel_fragment,container,false);

        btn_chekEngine = (Button)view.findViewById(R.id.btn_checkEngine);
        iconoSincr = (ImageView)view.findViewById(R.id.icono_datoSincr);

        iconoSincr.setImageResource(R.drawable.sync_icon1);
        if (porcentajeGasolina != 0 && temperaturaMotor !=0){
            iconoSincr.setImageResource(R.drawable.sync_icon);
        }

        vistaKilometraje(view);
        vistaGasolina(view);
        vistaTemperatura(view);
        escucharBtn_CheckEngien(btn_chekEngine);

        if(ListHistorialNotificacion.obtenerHistorialNotificacion_SinRealizar(idModMotocicleta)){
            animacionParpadeo().start();
        }


        return view;
    }
    public void vistaKilometraje(View view){

        textView0 = (TextView)view.findViewById(R.id.textView);
        textView1 = (TextView)view.findViewById(R.id.textView1);
        textView2 = (TextView)view.findViewById(R.id.textView2);
        textView3 = (TextView)view.findViewById(R.id.textView3);
        textView4 = (TextView)view.findViewById(R.id.textView4);
        textView5 = (TextView)view.findViewById(R.id.textView5);

        kilometrajeChars = kilometraje.toCharArray();

        logitudkilometrajeChars = kilometrajeChars.length;

        switch (logitudkilometrajeChars) {

            case 6:
                textView5.setText(kilometrajeChars[5]+"");
                textView4.setText(kilometrajeChars[4]+"");
                textView3.setText(kilometrajeChars[3]+"");
                textView2.setText(kilometrajeChars[2]+"");
                textView1.setText(kilometrajeChars[1]+"");
                textView0.setText(kilometrajeChars[0]+"");
                break;
            case 5:
                textView5.setText(kilometrajeChars[4]+"");
                textView4.setText(kilometrajeChars[3]+"");
                textView3.setText(kilometrajeChars[2]+"");
                textView2.setText(kilometrajeChars[1]+"");
                textView1.setText(kilometrajeChars[0]+"");
                break;

            case 4:
                textView5.setText(kilometrajeChars[3]+"");
                textView4.setText(kilometrajeChars[2]+"");
                textView3.setText(kilometrajeChars[1]+"");
                textView2.setText(kilometrajeChars[0]+"");
                break;

            case 3:
                textView5.setText(kilometrajeChars[2]+"");
                textView4.setText(kilometrajeChars[1]+"");
                textView3.setText(kilometrajeChars[0]+"");
                break;
            case 2:
                textView1.setText(kilometrajeChars[1]+"");
                textView0.setText(kilometrajeChars[0]+"");
                break;
            default:
                //no se hace nada
                break;

        }
    }
    public void vistaGasolina(View view){
        ImageView imageView = (ImageView)view.findViewById(R.id.img_galongasolina);
        waveDrawable = new WaveDrawable(getActivity().getApplicationContext(), R.drawable.galon);
        imageView.setImageDrawable(waveDrawable);
        waveDrawable.setLevel(porcentajeGasolina*(8000/100));
        waveDrawable.setWaveAmplitude(200);
        waveDrawable.setWaveLength(1000);
        waveDrawable.setWaveSpeed(10);

        TextView textView = (TextView)view.findViewById(R.id.txv_porcentajeGasolina);
        textView.setText(""+porcentajeGasolina+"%");
    }
    public void vistaTemperatura(View view){

        // Find the components

        final ScArcGauge gauge = (ScArcGauge) view.findViewById(R.id.gauge);
        assert gauge != null;

        final ImageView indicator = (ImageView) view.findViewById(R.id.indicator);
        assert indicator != null;

        final TextView counter = (TextView) view.findViewById(R.id.counter);
        assert counter != null;

        // If you set the value from the xml that not produce an event so I will change the
        // value from code.

        porcentajeTemperatura = (float) temperaturaMotor/180*100;

        if (porcentajeTemperatura == 0){
            gauge.setHighValue(1);
        }else {
            gauge.setHighValue(porcentajeTemperatura);
        }

        // Set the base colors feature
        gauge.setStrokeColors(new int[] {
                Color.parseColor("#97B329"), Color.parseColor("#A9CB2A"),
                Color.parseColor("#D4E935"), Color.parseColor("#F1DD31"),
                Color.parseColor("#FBCB2E"), Color.parseColor("#F3A328"),
                Color.parseColor("#F18C23"), Color.parseColor("#F18C23"),
                Color.parseColor("#F51319"), Color.parseColor("#F51319")}
        );
        gauge.setStrokeColorsMode(ScFeature.ColorsMode.SOLID);

        // Each time I will change the value I must write it inside the counter text.
        gauge.setOnEventListener(new ScGauge.OnEventListener() {
            @Override
            public void onValueChange(float lowValue, float highValue) {
                // Convert the percentage value in an angle
                float angle = gauge.percentageToAngle(highValue);
                //indicator.setRotation(angle);
                rotarImagen(indicator,angle);

                // Write the value
                //counter.setText((int) highValue + "");
                counter.setText((int)temperaturaMotor + "°c");
            }
        });
    }
    public static void mostrarIconoSincronizado(){
        if(porcentajeGasolina != 0 && temperaturaMotor !=0){
            rotarImagen360(iconoSincr);
            iconoSincr.setImageResource(R.drawable.sync_icon);
        }
    }

    private static void rotarImagen360(View view){
        RotateAnimation animation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(2000);
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }
    private void rotarImagen(View view, float angulo){

        view.setRotation(angulo);
        if(angulo>182){
            RotateAnimation animation = new RotateAnimation(-90, angulo-270,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            animation.setDuration(2000);
            view.startAnimation(animation);
        }
    }

    private void escucharBtn_CheckEngien(Button btn_chekEngine){
        btn_chekEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.activarCheckEngine();
            }
        });
    }
    private void animacionTransicion(Button btn_chekEngine){
        Drawable backgrounds[] = new Drawable[2];
        Resources res = getResources();
        backgrounds[0] = res.getDrawable(R.drawable.check_engine);
        backgrounds[1] = res.getDrawable(R.drawable.check_engine1);

        TransitionDrawable crossfader = new TransitionDrawable(backgrounds);
        btn_chekEngine.setBackgroundDrawable(crossfader);

        crossfader.startTransition(500);
        crossfader.startTransition(500);
        crossfader.startTransition(500);
    }

    private AnimationDrawable animacionParpadeo(){
        btn_chekEngine.setBackgroundResource(R.drawable.loading);
        AnimationDrawable frameanimation = (AnimationDrawable)btn_chekEngine.getBackground();
        return frameanimation;
        //frameanimation.start();
    }
}

