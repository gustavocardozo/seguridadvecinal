package com.social.seguridad.barbarus.seguridadvecinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by braian on 12/08/2016.
 */
public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 6500;

    private Configuracion conf;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CONF
        conf = new Configuracion(this);


        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);

        //Se inicia el servicio
        /*Intent servIntent = new Intent( getApplicationContext() , LocalizacionService.class);
        startService(servIntent);*/

        try {
            FirebaseInstanceId.getInstance().getToken();
        }catch (Exception e){
            Toast.makeText(SplashScreenActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        long delay = SPLASH_SCREEN_DELAY;
        if(null != conf.getToken()){
            delay = 2000;
        }


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent= null;
                //
                //Log.d("App" , conf.getUserEmail());
                if(conf.getUserEmail() == null) {
                    // Start the next activity presentacion
                    mainIntent = new Intent().setClass(
                            SplashScreenActivity.this, PresentationActivity.class);
                }else{
                    if(conf.getSessionUbicacion() == null){
                        //si esta logueado pero no tiene ubicacion va direccto a la configuracion
                        mainIntent = new Intent().setClass(
                                SplashScreenActivity.this, ConfiguracionInicialActivity.class);
                    }else {
                        //si esta logueado va directamente al principal
                        mainIntent = new Intent().setClass(
                                SplashScreenActivity.this, MainActivity.class);
                    }
                }
                startActivity(mainIntent);
                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };
        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, delay);
    }
}
