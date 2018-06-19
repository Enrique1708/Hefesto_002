package corp.carrizales.hefesto_002;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import corp.carrizales.hefesto_002.modelo.HistorialKilometraje;
import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.BaseDatosHefesto;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

public class FormularioRegistroActivity extends AppCompatActivity {

    OperacionesBaseDatos datos;

    private Button btn_registro;

    private EditText txb_modeloMoto;
    private EditText txb_contraseña;
    private EditText txb_contraseñaConfirm;
    private EditText txb_kilometraje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro);

        //[DEFINICION BASE DE DATOS]
        BaseDatosHefesto bd = new BaseDatosHefesto(this);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        //[DEFINICION DE ELEMENTOS EditText]
        txb_modeloMoto = (EditText)findViewById(R.id.txb_modeloMoto);
        txb_contraseña = (EditText)findViewById(R.id.txb_contraseña);
        txb_contraseñaConfirm = (EditText)findViewById(R.id.txb_contraseñaConfirm);
        txb_kilometraje = (EditText)findViewById(R.id.txb_kilometraje);

        //[DEFINICION DE ELEMENTOS Button]
        btn_registro = (Button) findViewById(R.id.btn_registro);

        verificacionModeloMotocicleta();
        verificacionContraseña();
        registroMotocicleta();

    }

    public void verificacionModeloMotocicleta(){

        txb_modeloMoto.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasFocus) {
                datos.getDb().beginTransaction();
                if (!hasFocus){
                    if(ListMotocicleta.verificarExistenciaMotocicleta(txb_modeloMoto.getText().toString()) != false){
                        txb_modeloMoto.setText("");
                        Toast tostada = Toast.makeText(getApplicationContext(), "Motocicleta existente", Toast.LENGTH_LONG);
                        tostada.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                        tostada.show();
                    }
                }
                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();
            }
        });
    }

    public void verificacionContraseña(){
        txb_contraseñaConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(!hasfocus){
                    if (!txb_contraseña.getText().toString().equals(txb_contraseñaConfirm.getText().toString())){
                        txb_contraseña.setText("");
                        txb_contraseñaConfirm.setText("");
                    }
                }
            }
        });
    }

    public boolean verificarVacios(){
        if (txb_modeloMoto.getText().toString().equals("") || txb_contraseña.getText().toString().equals("") || txb_kilometraje.getText().toString().equals("")){
            Toast tostada = Toast.makeText(getApplicationContext(), "Llenar espacios vacios", Toast.LENGTH_LONG);
            tostada.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            tostada.show();
            return false;
        }else {
            return true;
        }
    }

    public void registroMotocicleta(){

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                datos.getDb().beginTransaction();

                if (verificarVacios()){
                    Motocicleta motocicleta = new Motocicleta(null, txb_modeloMoto.getText().toString(), txb_contraseña.getText().toString(), Integer.parseInt(txb_kilometraje.getText().toString()));
                    datos.insertarMotocicleta(motocicleta);


                    //Envio de el objeto Motocicleta
                    Motocicleta motocicleta1 = new Motocicleta();
                    motocicleta1 = ListMotocicleta.verificarMotocicleta(txb_modeloMoto.getText().toString(), txb_contraseña.getText().toString());

                    //Registro del primer kilometraje en HistorialKilometraje
                    HistorialKilometraje historialKilometraje = new HistorialKilometraje(null,Integer.parseInt(txb_kilometraje.getText().toString()),getDateTime(),motocicleta1.getId());
                    datos.insertarHistorialKilometraje(historialKilometraje);

                    datos.getDb().setTransactionSuccessful();
                    datos.getDb().endTransaction();

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("motocicleta", motocicleta1);
                    startActivity(intent);
                }
            }
        });
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
