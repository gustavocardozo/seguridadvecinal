package com.social.seguridad.barbarus.seguridadvecinal;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Notificacion extends AppCompatActivity {

    private String numeroAlerta = null;

    private String COLOR_POLICIA= "Policia";
    private String COLOR_BOMBERO= "Bombero";
    private String COLOR_AMBULANCIA= "Ambulancia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            TextView textViewNombreApellido = (TextView) findViewById(R.id.textViewNombreApellido);
            textViewNombreApellido.setText(null != extras.getString("nombreApellido") ?
                    "Su vecino: " + extras.getString("nombreApellido") : "");

            TextView textViewDireccion = (TextView) findViewById(R.id.textViewDireccion);
            textViewDireccion.setText(null != extras.getString("direccion") ?
                    "En la calle: " + extras.getString("direccion") : "");

            TextView textViewNroTelefono = (TextView) findViewById(R.id.textViewNroTelefono);
            textViewNroTelefono.setText(null != extras.getString("nroTelefono") ?
                    "Numero de Telefono: " + extras.getString("nroTelefono") : "");

            TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
            textViewTitle.setText(null != extras.getString("alerta") ? extras.getString("alerta") : "");

            this.numeroAlerta = extras.getString("nroAlerta");

            ImageView fondo = (ImageView) findViewById(R.id.imageViewAlert);
            setColorToHeader(fondo , extras.getString("tipoAlerta"));

            Button ignorarButton = (Button) findViewById(R.id.ignorarButton);
            ignorarButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){
                    finish();
                }
            });

            Button callButton = (Button) findViewById(R.id.callButton);
            callButton.setText("Llamar a " + extras.getString("tipoAlerta"));
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){
                    if (ContextCompat.checkSelfPermission(getThis(), Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED && null != numeroAlerta) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + numeroAlerta));
                        startActivity(callIntent);
                    }
                }
            });

        }else{
            Intent mainIntent = new Intent().setClass(
                    Notificacion.this, MainActivity.class);
            startActivity(mainIntent);
        }


    }


    private Notificacion getThis(){
        return this;
    }


    private void setColorToHeader(ImageView imageView , String alerta){

        if(COLOR_POLICIA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaPolicia);
        }else if(COLOR_AMBULANCIA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaAmbulancia);
        }else if(COLOR_BOMBERO.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaBomberos);
        }
    }
}
