package com.social.seguridad.barbarus.action;

import android.util.Log;
import android.widget.Toast;

import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.seguridadvecinal.MainActivity;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by braian on 16/10/2016.
 */
public class FirefighterAction implements Asynchtask {

    private static String ALERTA= "Bombero";
    private static String NUMERO_ALERTA = "113";

    private MainActivity mainActivity;

    public FirefighterAction(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void send(String email , String token , String lugar , String latitud , String longitud){
        Log.d("Seguridad Vecinal", "Boton apretado");
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", email);
        datos.put("token", token);
        datos.put("lugar", lugar);
        datos.put("longitud", longitud);
        datos.put("latitud", latitud);
        datos.put("alerta", ALERTA);
        datos.put("nroalerta", NUMERO_ALERTA);


        WebService wb = new WebService(
                URL.SERVER_URL + "/sendNotification",
                datos,
                this.mainActivity,
                FirefighterAction.this,
                WebService.TYPE.POST
        );
        wb.execute("");
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(this.mainActivity, result , Toast.LENGTH_SHORT).show();
    }
}
