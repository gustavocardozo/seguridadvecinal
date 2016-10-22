package com.social.seguridad.barbarus.seguridadvecinal;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.social.seguridad.barbarus.SharedPreferences.Configuracion;

import java.util.List;
import java.util.Locale;

/**
 * Created by braian on 23/09/2016.
 */
public class LocalizacionService extends Service implements LocationListener {
    LocationManager mLocationManager;

    private Configuracion conf;

    public IBinder onBind(Intent intent) {
        // This won't be a bound service, so simply return null
        return null;
    }

    @Override
    public void onCreate() {
        // This will be called when your Service is created for the first time
        // Just do any operations you need in this method.
        conf = new Configuracion(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {



            Toast.makeText(LocalizacionService.this, "No se puede obtener su ubicaciòn active permisos de localizacìòn", Toast.LENGTH_LONG).show();
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try{
            super.onDestroy();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }catch (Exception e){
            //Toast.makeText(LocalizacionService.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.v("WEAVER_","Service_Location Change");
        try {
            if(null != location){
                conf.setLastKnowLatitud(location.getLatitude());
                conf.setLastKnowLongitud(location.getLongitude());

                try {
                    Geocoder geocoder = new Geocoder(LocalizacionService.this, Locale.getDefault());
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(addresses != null && addresses.size() > 0){
                        String addressesValue =
                                addresses.get(0).getAdminArea() + " " +
                                addresses.get(0).getLocality() + " " +
                                addresses.get(0).getThoroughfare() + " " +
                                addresses.get(0).getFeatureName();

                        conf.setLastKnowAddresses( addressesValue);
                    }
                } catch (Exception ioException) {
                    Log.v("WEAVER_","Service Not Available");
                }
            }
        }catch (    Exception e ){  }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
