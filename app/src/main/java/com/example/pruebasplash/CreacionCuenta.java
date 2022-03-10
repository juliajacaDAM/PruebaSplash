package com.example.pruebasplash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CreacionCuenta extends AppCompatActivity {
    private DBHandler dbHandler;
    private EditText etNombre, etPassCaja, etPassCaja2;
    private Button btnCrear;
    private String nombre, contraseña, contraseña2;
    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_cuenta);
        contexto = this;

        etNombre = findViewById(R.id.nombreCaja);
        etPassCaja = findViewById(R.id.passCaja);
        etPassCaja2 = findViewById(R.id.passCaja2);
        btnCrear = findViewById(R.id.boton);
        dbHandler = new DBHandler(CreacionCuenta.this);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // on below line we are calling a method to add new course to sqlite data and pass all our values to it.
                // below line is to get data from all edit text fields.
                nombre = etNombre.getText().toString();
                contraseña = etPassCaja.getText().toString();
                contraseña2 =  etPassCaja2.getText().toString();

                // validating if the text fields are empty or not.
                if(isCamposValidos(nombre, contraseña, contraseña2)){
                    JugadorModel jugador  = new JugadorModel(nombre, contraseña);
                    dbHandler.añadirJugador(jugador);
                    mostrarMensaje("correcto");
                    Intent i = new Intent(CreacionCuenta.this, Menu.class);
                    i.putExtra("usuario", nombre);
                    startActivity(i);
                    Config.LOGGED_USER = nombre;
                }
            }
    } );

    }

    private boolean contraseñasCoinciden(String contraseña, String contraseña2) {
        boolean coinciden = false;
        if(contraseña.equals(contraseña2)){
            coinciden = true;
        }
        return coinciden;
    }

    private boolean isCamposValidos(String nombre, String pass1, String pass2){
        boolean isValido = false;
        if ((nombre.isEmpty()|| nombre.length() == 0 || nombre.equals("") || nombre == null ) ||
                (  contraseña.isEmpty() || contraseña.length() == 0 || contraseña.equals("") || contraseña == null) ||
                (  contraseña2.isEmpty() || contraseña2.length() == 0 || contraseña2.equals("") || contraseña2 == null)) {
            mostrarMensaje("camposVacios");
        }else if (!dbHandler.isNombreUnico(nombre)){
            mostrarMensaje("nombreEnUso");
        } else if (!contraseñasCoinciden(contraseña, contraseña2)){
            mostrarMensaje("contraseñasNoCoinciden");
        }else{
            isValido = true;
        }
        return isValido;
    }

    private void mostrarMensaje(String campoError){
        Toasty.Config.getInstance().setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 0, Config.CARD_WIDTH*12).apply(); //
        switch (campoError){
            case "camposVacios":
                Toasty.info(CreacionCuenta.this, "Por favor rellena todos los datos", Toast.LENGTH_SHORT,true).show();
                break;
            case "nombreEnUso":
                Toasty.error(CreacionCuenta.this, "Ese nombre ya está registrado.", Toast.LENGTH_SHORT,true).show();
                break;
            case "contraseñasNoCoinciden":
                Toasty.error(CreacionCuenta.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT, true).show();
                break;
            case "correcto":
                Toasty.success(CreacionCuenta.this, "Correcto", Toast.LENGTH_SHORT, true).show();
                break;
            default:
                Toasty.info(CreacionCuenta.this, "Error no controlado", Toast.LENGTH_SHORT,true).show();
                break;
        }
    }
}