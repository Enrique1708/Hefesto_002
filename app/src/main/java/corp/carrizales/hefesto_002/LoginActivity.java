package corp.carrizales.hefesto_002;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import corp.carrizales.hefesto_002.modelo.Motocicleta;
import corp.carrizales.hefesto_002.sqlite.BaseDatosHefesto;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;


public class LoginActivity extends AppCompatActivity {


    OperacionesBaseDatos datos;

    private TextView txtRegistrar;

    private Button btn_IniciarS;

    private EditText txb_Motocicleta;
    private EditText txb_contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BaseDatosHefesto bd = new BaseDatosHefesto(this);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());


        btn_IniciarS = (Button) findViewById(R.id.btn_IniciarS);

        txtRegistrar = (TextView)findViewById(R.id.txv_Registro);

        txb_Motocicleta = (EditText)findViewById(R.id.txb_MotocicletaModelLogin);
        txb_contraseña = (EditText)findViewById(R.id.txb_contraseñaLogin);

        botonIniciarSecion();
        botonRegistrar();

    }

    public void botonIniciarSecion(){

        btn_IniciarS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                datos.getDb().beginTransaction();

                Motocicleta motocicleta = new Motocicleta();
                motocicleta = ListMotocicleta.verificarMotocicleta(txb_Motocicleta.getText().toString(), txb_contraseña.getText().toString());

                if (motocicleta.getId() != null){
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("motocicleta", motocicleta);
                    startActivity(intent);
                }else {
                    txb_Motocicleta.setText("");
                    txb_contraseña.setText("");
                    Toast tostada = Toast.makeText(getApplicationContext(), "Error al iniciar sesion", Toast.LENGTH_LONG);
                    tostada.setGravity(Gravity.CENTER_HORIZONTAL|0,0,0);
                    tostada.show();
                }

                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();
            }
        });
    }

    public void botonRegistrar(){
        txtRegistrar.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Aqui el codigo que queremos que ejecute al ser pulsado
                Intent intent = new Intent (v.getContext(), FormularioRegistroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
            }
        });
    }
}

