package corp.carrizales.hefesto_002;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;

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


    View popupView;
    LayoutInflater layoutInflaterListDispositivos;
    PopupWindow popupWindow_ListDispositivos;
    // EXTRA string to send on to mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter mPairedDevicesArrayAdapter;

    //////CODIGO BLUETOOTH////////////////////////////////////////
    BluetoothAdapter mBluetoothAdapter = null;                  //
    int ENABLE_BLEIntent = 1;                                   //
    int SOLICITA_CONEXION = 2;                                  //
    private BluetoothSocket btSocket = null;
    // String for MAC address
    private static String address = null;

    Handler bluetoothIn;
    final int handlerState = 0;
    private StringBuilder recDataString = new StringBuilder();


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    String txtString; /// mensaje que recive
    String txtStringLength; // largo del string

    private ConnectedThread mConnectedThread;
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


    @SuppressLint("HandlerLeak")
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

        bluetoothIn = new Handler(){
            public void handleMassage(Message msg){
                if (msg.what == handlerState) {          //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);              //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        txtString = ("Datos recibidos = " + dataInPrint);
                        int dataLength = dataInPrint.length();       //get length of data received
                        txtStringLength = ("Tamaño del String = " + String.valueOf(dataLength));

                        //recDataString.charAt(0);
                        recDataString.delete(0, recDataString.length());      //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        switchBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    encenderBluetooth();
                    if(mBluetoothAdapter.isEnabled()){
                        popupListaDispositivos();

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
                    apagarBluetooth();

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

    private void apagarBluetooth(){
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);         //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }

    public void recuperarAdress(){
        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        //address = intent.getStringExtra(ListDispositivosActivity.EXTRA_DEVICE_ADDRESS);
        //create device and set the MAC address
        //Log.i("ramiro", "adress : " + address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
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
                address = info.substring(info.length() - 17);
                popupWindow_ListDispositivos.dismiss();
            }
        });

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        //Set pairedDevices = mBtAdapter.getBondedDevices();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevices.size() > 0) {
            popupView.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device: pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "Ningun dispositivo pudo ser emparejado";
            mPairedDevicesArrayAdapter.add(noDevices);
        }
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
