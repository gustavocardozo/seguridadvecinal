package com.social.seguridad.barbarus.seguridadvecinal;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PresentationActivity extends AppCompatActivity {

    public  static  final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                /* Aquí se mostrará la explicación al usuario de porqué es
                necesario el uso de un determinado permiso, pudiéndose mostrar de manera asíncrona, o lo que es lo mismo, desde un
                hilo secundario, sin bloquear el hilo principal, y a la espera de
                que el usuario concede el permiso necesario tras visualizar la explicación.*/
            } else {

                /* Se realiza la petición del permiso. En este caso permisos
                para la ubicacion.*/
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
        Button registrarseButton = (Button) findViewById(R.id.registrarseButton);
        registrarseButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){ lanaActivityRegistrar(v); }
        });

        Button ingresarButton = (Button) findViewById(R.id.ingresarButton);
        ingresarButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){ lanaActivityIngresar(v); }
        });



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                Intent servIntent = new Intent( getApplicationContext() , LocalizacionService.class);
                startService(servIntent);
            }

        }
    }

    public void lanaActivityRegistrar(View v){
        Intent intent = new Intent(v.getContext() , RegistrarseActivity.class);
        startActivityForResult(intent,0);
    }

    public void lanaActivityIngresar(View v){
        Intent intent = new Intent(v.getContext() , LoginActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
