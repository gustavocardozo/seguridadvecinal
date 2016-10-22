package com.social.seguridad.barbarus.seguridadvecinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by braian on 13/08/2016.
 */
public class SettingActivity extends AppCompatActivity {

    AVLoadingIndicatorView actualizarLocalizacionAVLoadingIndicatorView;
    private Configuracion conf;

    private boolean runActualizacion = false;

    TextView localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //CONF
        conf = new Configuracion(this);

        this.actualizarLocalizacionAVLoadingIndicatorView =
                (AVLoadingIndicatorView) findViewById(R.id.loadingIndicatorView);

        this.localizacion = (TextView) findViewById(R.id.localizaciontextView);
        this.localizacion.setText(null != conf.getToken() ? conf.getToken() : "Ninguna");
        this.actualizarLocalizacionAVLoadingIndicatorView.hide();

        Button actualizarLocalizacionButton = (Button) findViewById(R.id.actualizarLocalizacionButton);
        actualizarLocalizacionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                actualizarLocalizacionAccion(v);
            }
        });
    }

    private void actualizarLocalizacionAccion(View v) {
        if(!this.runActualizacion){
            this.runActualizacion = true;
            this.actualizarLocalizacionAVLoadingIndicatorView.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        closeLoader();
                    }catch (Exception e){
                        closeLoader();
                    }
                }
            }).start();
        }
    }


    private void closeLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SettingActivity.this, "Addreses " +     conf.getLastKnowAddresses() , Toast.LENGTH_SHORT).show();
                localizacion.setText(null != conf.getLastKnowAddresses() ? conf.getLastKnowAddresses() : "Ninguna");
                actualizarLocalizacionAVLoadingIndicatorView.hide();
                runActualizacion = false;
            }
        });
    }
}
