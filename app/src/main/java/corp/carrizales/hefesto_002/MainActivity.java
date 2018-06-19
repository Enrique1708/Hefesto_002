package corp.carrizales.hefesto_002;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import corp.carrizales.hefesto_002.modelo.HistorialKilometraje;
import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

public class MainActivity extends AppCompatActivity {

    OperacionesBaseDatos datos;

    private String idModMotocicleta;
    private int nivelGasolina;
    private int temperaturaMotor;
    private int kilometrajeMoto;

    private int kilometrajeServidor;

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private Switch switchBluetooth;

    //////CODIGO BLUETOOTH////////////////////////////////////////
    BluetoothAdapter mBluetoothAdapter = null;                  //
    int ENABLE_BLEIntent = 1;                                   //
    int SOLICITA_CONEXION = 2;                                  //
    private BluetoothSocket btSocket = null;
    // String for MAC address
    private static String address = null;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //////////////////////////////////////////////////////////////

    private Button bt_chekEngine;

    private ViewPager mViewPager;

    private TextView txv_modeloMotocicleta;

    Bundle bundle = new Bundle();

    Motocicleta motocicleta = new Motocicleta();

    private static TabLayout tabLayout2;

    //VARIABLES PARA GENERAR HISTORIALKILOMETRAJE
    int kilometrajeInicial;
    int kilometrajeActual;
    int kilometrajeActualAxiliar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        motocicleta = (Motocicleta)getIntent().getExtras().getSerializable("motocicleta");
        idModMotocicleta = motocicleta.getId();
        kilometrajeMoto = motocicleta.getKilometraje();

        switchBluetooth = (Switch)findViewById(R.id.switchBluetooth);

        txv_modeloMotocicleta = (TextView)findViewById(R.id.txv_modeloMotocicleta);
        txv_modeloMotocicleta.setText(motocicleta.getModelo());

        Log.d(TAG, "onCreate: Starting.");

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

                    nivelGasolina = 50;
                    temperaturaMotor = 147;
                    kilometrajeServidor = 1400;
                    insertarHistorialKilometraje(kilometrajeServidor);
                    actualizarKilometrajeMotocicleta(motocicleta);
                    setupViewPager(mViewPager);
                    Panel_fragment.mostrarIconoSincronizado();
                    switchBluetooth.setText("Conectado");

                    Intent abreLista = new Intent(MainActivity.this, ListDispositivosActivity.class);
                    startActivityForResult(abreLista, SOLICITA_CONEXION);
                } else {
                    nivelGasolina = 0;
                    temperaturaMotor = 0;
                    kilometrajeServidor = 0;
                    setupViewPager(mViewPager);
                    switchBluetooth.setText("Desconectado");
                }
            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(ListDispositivosActivity.EXTRA_DEVICE_ADDRESS);

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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void encenderBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Este dispositivo no soporta Bluetooth
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Este dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT);
            toast1.show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBLEIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBLEIntent, ENABLE_BLEIntent);
        }
    }

    private void buscarDispositivosActivos(){

    }

    public static void activarCheckEngine(){
        tabLayout2.getTabAt(1).select();//PARA SELECCIONAR PESTAÑA DESDE CODIGO
    }
}
