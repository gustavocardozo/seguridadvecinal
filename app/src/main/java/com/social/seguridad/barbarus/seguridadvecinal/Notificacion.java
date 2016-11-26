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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.social.seguridad.barbarus.action.AmbulanceAction;
import com.social.seguridad.barbarus.action.CriminalAction;
import com.social.seguridad.barbarus.action.FirefighterAction;
import com.social.seguridad.barbarus.action.PoliceAction;
import com.social.seguridad.barbarus.action.ViolenciaGeneroAction;
import com.social.seguridad.barbarus.googleMapUtil.GoogleMapUtil;

import java.util.Date;

public class Notificacion extends AppCompatActivity implements OnMapReadyCallback {

    private String numeroAlerta = null;
    private GoogleMap mMap;

    //Para el mapa
    private double latitud;
    private double longitud;
    private String alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            String nombreApellido = extras.getString("nombreApellido");
            String direccion = extras.getString("direccion");
            String nroTelefono =  extras.getString("nroTelefono");
            String alerta = extras.getString("alerta");
            this.numeroAlerta = extras.getString("nroAlerta");
            String tipoAlerta = CriminalAction.ALERTA.equals(extras.getString("tipoAlerta")) ?
                                    "Policia" : extras.getString("tipoAlerta");

            this.alerta = extras.getString("tipoAlerta");
            this.latitud  =  Double.valueOf(extras.getString("latitud"));
            this.longitud = Double.valueOf(extras.getString("longitud"));

            TextView textViewNombreApellido = (TextView) findViewById(R.id.textViewNombreApellido);
            textViewNombreApellido.setText(null != nombreApellido ?
                    "Su vecino: " + nombreApellido : "");

            TextView textViewDireccion = (TextView) findViewById(R.id.textViewDireccion);
            textViewDireccion.setText(null != direccion ?
                    "En la calle: " + direccion : "");

            TextView textViewNroTelefono = (TextView) findViewById(R.id.textViewNroTelefono);
            textViewNroTelefono.setText(null != nroTelefono ?
                    "Numero de Telefono: " + nroTelefono : "");

            TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
            textViewTitle.setText(null != alerta ? alerta : "");

            ImageView fondo = (ImageView) findViewById(R.id.imageViewAlert);
            setColorToHeader(fondo , tipoAlerta);

            Button callButton = (Button) findViewById(R.id.callButton);
            callButton.setText("Llamar a " + tipoAlerta);

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

        if(PoliceAction.ALERTA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaPolicia);
        }else if(AmbulanceAction.ALERTA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaAmbulancia);
        }else if(FirefighterAction.ALERTA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaBomberos);
        }else if (CriminalAction.ALERTA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaRobo);
        }else if (ViolenciaGeneroAction.ALERTA.equals(alerta)){
            imageView.setBackgroundResource(R.color.alertaViolenciaGenero);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng central = new LatLng(this.latitud,  this.longitud);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(central, 14));
        GoogleMapUtil googleMapUtil = new GoogleMapUtil();
        mMap.addMarker(googleMapUtil.buildMarkerOptions(this.latitud , this.longitud , this.alerta , "" , false, getThis()));
    }
}
