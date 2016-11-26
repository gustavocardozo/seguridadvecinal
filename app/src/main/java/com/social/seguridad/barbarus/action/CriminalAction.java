package com.social.seguridad.barbarus.action;

import android.util.Log;
import android.widget.Toast;

import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSON;
import com.social.seguridad.barbarus.seguridadvecinal.MainActivity;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by braian on 30/10/2016.
 */
public class CriminalAction implements Asynchtask {

    public static String ALERTA= "Robo";
    private static String NUMERO_ALERTA = "911";

    public static String ALERTA_TITLE = "Alerta " + ALERTA;

    private MainActivity mainActivity;

    public CriminalAction(MainActivity mainActivity){
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
                CriminalAction.this,
                WebService.TYPE.POST,
                true
        );
        wb.execute("");
    }

    @Override
    public void processFinish(String result) {
        ResultJSON resultJSON = JSONParse.ParseJSON(result);
        if(null != resultJSON && null != resultJSON.getMessage()){
            Toast.makeText(this.mainActivity, resultJSON.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}