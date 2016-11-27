package com.social.seguridad.barbarus.seguridadvecinal;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PresentationActivity extends AppCompatActivity {

    public  static  final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

       /* if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Aquí se mostrará la explicación al usuario de porqué es
                necesario el uso de un determinado permiso, pudiéndose mostrar de manera asíncrona, o lo que es lo mismo, desde un
                hilo secundario, sin bloquear el hilo principal, y a la espera de
                que el usuario concede el permiso necesario tras visualizar la explicación.
            } else {
                Se realiza la petición del permiso. En este caso permisos
                para la ubicacion.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }*/

        boolean check = true;
        while(check){
            if(checkPermissions()){
                Intent servIntent = new Intent( getApplicationContext() , LocalizacionService.class);
                startService(servIntent);
                check = true;
            }else{
                Toast.makeText(this, "Se necesita aceptar los permisos para el funcionamiento basico de la aplicacion",
                                Toast.LENGTH_LONG).show();
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

        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
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

        }*/
    }

    private  boolean checkPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);

            return  false;
        }

        return true;
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
