package corp.carrizales.hefesto_002;


import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import corp.carrizales.hefesto_002.modelo.HistorialKilometraje;
import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

public class MainActivity extends AppCompatActivity {

    // <editor-fold desc="COMPONENTES DE LOS NAVEGADORES DEL MENU (TABS)">
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    Bundle bundle = new Bundle();
    // </editor-fold>
    // <editor-fold desc="COMPONENTES DE LA CLASE">
    OperacionesBaseDatos datos;

    private Switch switchBluetooth;
    TextView txb_modeloMotocicleta, txb_macAddress;

    private static TabLayout tabLayout2;

    private String idModMotocicleta;

    Motocicleta motocicleta = new Motocicleta();
    // </editor-fold>
    // <editor-fold desc="VARIABLES DE LA MOTOCICLETA-SERVIDOR">
    private int nivelGasolina;
    private int temperaturaMotor;
    private int kilometrajeServidor;
    // </editor-fold>
    // <editor-fold desc="POPUP LISTA DE DISPOSITIVOS">
    View popupView;
    LayoutInflater layoutInflaterListDispositivos;
    PopupWindow popupWindow_ListDispositivos;
    // </editor-fold>
    // <editor-fold desc="VARIABLES PARA GENERAR HISTORIALKILOMETRAJE">
    int kilometrajeInicial;
    int kilometrajeActual;
    int kilometrajeActualAxiliar;
    // </editor-fold>

    //private int kilometrajeMoto;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refMot = database.getReference("motocicleta");
    DatabaseReference refkilometraje, refNivel_gasolina, refTemperatura;

    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refkilometraje = refMot.child("kilometraje");
        refNivel_gasolina = refMot.child("nivel_gasolina");
        refTemperatura = refMot.child("temperatura");


        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        motocicleta = (Motocicleta)getIntent().getExtras().getSerializable("motocicleta");
        idModMotocicleta = motocicleta.getId();

        switchBluetooth = (Switch)findViewById(R.id.switchBluetooth);
        txb_modeloMotocicleta = (TextView)findViewById(R.id.txv_modeloMotocicleta);
        txb_modeloMotocicleta.setText(motocicleta.getModelo());

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(Color.WHITE,Color.BLACK);

        tabLayout2 = tabLayout;

        actualizarKilometrajeMotocicleta(motocicleta);

        switchBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    //refNivel_gasolina.setValue(50);
                    leerDato(refNivel_gasolina);
                    //nivelGasolina = 60
                    //temperaturaMotor = 147;
                    //kilometrajeServidor = 1400;
                        /*insertarHistorialKilometraje(kilometrajeServidor);
                        actualizarKilometrajeMotocicleta(motocicleta);
                        setupViewPager(mViewPager);
                        Panel_fragment.mostrarIconoSincronizado();
                        */
                } else {
                    //apagarBluetooth();
                    nivelGasolina = 0;
                    temperaturaMotor = 0;
                    kilometrajeServidor = 0;
                    //setupViewPager(mViewPager);
                    switchBluetooth.setText("Sincronizado");
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        //Drawable imagen;
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        Panel_fragment panel_fragment = new Panel_fragment();
        bundle.putString("idModMotocicleta", idModMotocicleta);
        bundle.putInt("gasolina", nivelGasolina);
        bundle.putInt("temperatura", temperaturaMotor);
        panel_fragment.setArguments(bundle);
        adapter.addFragment(panel_fragment,"Panel Principal");

        Notificacion_fragment notificacion_fragment = new Notificacion_fragment();
        notificacion_fragment.setArguments(bundle);
        adapter.addFragment(notificacion_fragment, "Notificación");

        Mantenimineto_Fragment mantenimineto_fragment = new Mantenimineto_Fragment();
        mantenimineto_fragment.setArguments(bundle);
        adapter.addFragment(mantenimineto_fragment,"Mantenimineto");


        Temperatura_fragment temperatura_fragment = new Temperatura_fragment();
        temperatura_fragment.setArguments(bundle);
        adapter.addFragment(temperatura_fragment, "Temperatura");

        Aceite_Fragment aceite_fragment = new Aceite_Fragment();
        aceite_fragment.setArguments(bundle);
        adapter.addFragment(aceite_fragment, "Aceite");

        viewPager.setAdapter(adapter);
    }

    ///ACTUALIZAR KILOMETRAJE DE LA MOTOCICLETA
    private void actualizarKilometrajeMotocicleta(Motocicleta moto){
        int kilometrajeFianl = ListHistorialKilometraje.obtenerUltimoKilometraje(moto.getId());
        if(moto.getKilometraje() < kilometrajeFianl){
            datos.getDb().beginTransaction();
            datos.actualizarKilometrajeMotocicleta(kilometrajeFianl, moto.getId());
            datos.getDb().setTransactionSuccessful();
            datos.getDb().endTransaction();
        }
    }
    private  void insertarHistorialKilometraje(int kilometrajeServidorAux){
        kilometrajeInicial = ListHistorialKilometraje.obtenerPrimerKilometraje(idModMotocicleta);
        kilometrajeActual = ListMotocicleta.obtenerMotocicleta(idModMotocicleta).getKilometraje();
        kilometrajeActualAxiliar = kilometrajeInicial + kilometrajeServidorAux;
        String fechaUltimoHistorialKilometraje = ListHistorialKilometraje.obtenerUltimoKilometraje_fecha(idModMotocicleta);

        if(kilometrajeActualAxiliar > kilometrajeActual){
            if(fechaUltimoHistorialKilometraje.equals(getDateTime())){
                datos.getDb().beginTransaction();
                datos.actualizarHistorialKilometraje(kilometrajeActualAxiliar, idModMotocicleta);
                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();
            }
            else {
                datos.getDb().beginTransaction();
                HistorialKilometraje historialKilometraje = new HistorialKilometraje(null,kilometrajeActualAxiliar,getDateTime(),idModMotocicleta);
                datos.insertarHistorialKilometraje(historialKilometraje);
                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();
            }
        }
    }

    public static void activarCheckEngine(){
        tabLayout2.getTabAt(1).select();//PARA SELECCIONAR PESTAÑA DESDE CODIGO
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void leerDato(final DatabaseReference refDato) {

        refDato.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ///NOTA: NO OLVIDAR QUE AL LEER DE LA BASE DE DATOS NO RELACIONAL SE DEBE ESPECIFICAR DE MANERA EXACTA EL TIPO DE CLASE AL CUAL PERTENECE
                // YA SEA: String.class/Integer.class/Bolean.classs snapshot.getValue(Integer.class);
                int nivelGasolinas = snapshot.getValue(Integer.class);
                Toast tostada = Toast.makeText(getApplicationContext(), "El dato es"+nivelGasolinas, Toast.LENGTH_LONG);
                tostada.setGravity(Gravity.CENTER_HORIZONTAL|0,0,0);
                tostada.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
