package corp.carrizales.hefesto_002;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;

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
    TextView txv_modeloMotocicleta;

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

    //////CODIGO BLUETOOTH////////////////////////////////////////
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;

    private ConnectionThread mBluetoothConnection = null;

    private String data;
    private boolean mServerMode;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int SELECT_SERVER = 1;
    public static final int DATA_RECEIVED = 3;
    public static final int SOCKET_CONNECTED = 4;

    int ENABLE_BLEIntent = 1;
    // SPP UUID service - this should work for most devices
    public static final UUID APP_UUID = UUID
            .fromString("aeb9f938-a1a3-4947-ace2-9ebd0c67adf1");
    private ArrayAdapter mPairedDevicesArrayAdapter;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        motocicleta = (Motocicleta)getIntent().getExtras().getSerializable("motocicleta");
        idModMotocicleta = motocicleta.getId();
        //kilometrajeMoto = motocicleta.getKilometraje();

        switchBluetooth = (Switch)findViewById(R.id.switchBluetooth);
        txv_modeloMotocicleta = (TextView)findViewById(R.id.txv_modeloMotocicleta);
        txv_modeloMotocicleta.setText(motocicleta.getModelo());

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
                    encenderBluetooth();
                    if(mBluetoothAdapter.isEnabled()){
                        //popupListaDispositivos();
                        selectServer();

                        nivelGasolina = 50;
                        temperaturaMotor = 147;
                        kilometrajeServidor = 1400;
                        insertarHistorialKilometraje(kilometrajeServidor);
                        actualizarKilometrajeMotocicleta(motocicleta);
                        setupViewPager(mViewPager);
                        Panel_fragment.mostrarIconoSincronizado();
                        switchBluetooth.setText("Conectado");
                    }else {
                        switchBluetooth.setChecked(false);
                    }
                } else {
                    //apagarBluetooth();

                    nivelGasolina = 0;
                    temperaturaMotor = 0;
                    kilometrajeServidor = 0;
                    setupViewPager(mViewPager);
                    switchBluetooth.setText("Desconectado");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            //setButtonsEnabled(true);
        } else if (requestCode == SELECT_SERVER
                && resultCode == RESULT_OK) {
            BluetoothDevice device = data
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            connectToBluetoothServer(device.getAddress());
        }
    }

    private void connectToBluetoothServer(String id) {
        //tv.setText("Connecting to Server...");
        new ConnectThread(id, mHandler).start();
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SOCKET_CONNECTED: {
                    mBluetoothConnection = (ConnectionThread) msg.obj;
                    if (!mServerMode)
                        mBluetoothConnection.write("this is a message".getBytes());
                    break;
                }
                case DATA_RECEIVED: {
                    data = (String) msg.obj;
                    /* tv.setText(data); */
                    if (mServerMode)
                        mBluetoothConnection.write(data.getBytes());
                }
                default:
                    break;
            }
        }
    };

    private void selectServer() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                .getBondedDevices();
        ArrayList<String> pairedDeviceStrings = new ArrayList<String>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceStrings.add(device.getName() + "\n"
                        + device.getAddress());
            }
        }
        Intent showDevicesIntent = new Intent(this, ShowDevices.class);
        showDevicesIntent.putStringArrayListExtra("devices", pairedDeviceStrings);
        startActivityForResult(showDevicesIntent, SELECT_SERVER);
    }

    private void encenderBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Este dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT);
            toast1.show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBLEIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBLEIntent, ENABLE_BLEIntent);
        }
    }

    private void apagarBluetooth(){
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    private void popupListaDispositivos(){
        layoutInflaterListDispositivos = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflaterListDispositivos.inflate(R.layout.activity_list_dispositivos, null);

        popupWindow_ListDispositivos = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);


        popupWindow_ListDispositivos.showAsDropDown(switchBluetooth, 0, 0);

        // Initialize array adapter for paired devices
        mPairedDevicesArrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) popupView.findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) view).getText().toString();
                //address = info.substring(info.length() - 17);
                mBluetoothAdapter.cancelDiscovery();
                //recuperarAdress();
                popupWindow_ListDispositivos.dismiss();
            }
        });

        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        //Set pairedDevices = mBtAdapter.getBondedDevices();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevices.size() > 0) {
            popupView.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device: pairedDevices) {
                mBluetoothAdapter.startDiscovery();
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            buscarDispositivos();
        } else {
            String noDevices = "Ningun dispositivo pudo ser emparejado";
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private void buscarDispositivos(){
        mBluetoothAdapter.startDiscovery();
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
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
}
