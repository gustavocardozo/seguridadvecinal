package com.social.seguridad.barbarus.seguridadvecinal;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivityGoogle extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //ambulancia
        LatLng alert1 = new LatLng(-34.533849 ,  -58.788681);
        //policia
        LatLng alert2 = new LatLng(-34.5331491 ,   -58.7905443);
        //bombero
        LatLng alert3 = new LatLng(-34.5330151 ,   -58.7918482);

        mMap.addMarker(new MarkerOptions()
                .position(alert1)
                .title("Alerta ambulancia")
                .snippet("Fecha 12/02/2016 a las 08:00 hs")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance)));

        mMap.addMarker(new MarkerOptions()
                .position(alert2)
                .title("Alerta policia")
                .snippet("Fecha 12/02/2016 a las 21:00 hs")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.policecar)));


        mMap.addMarker(new MarkerOptions()
                .position(alert3)
                .title("Alerta bomberos")
                .snippet("Fecha 15/02/2016 a las 22:00 hs")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.firetruck)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(alert1));
    }
}
