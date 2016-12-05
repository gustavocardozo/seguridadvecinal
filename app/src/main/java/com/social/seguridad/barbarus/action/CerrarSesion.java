package com.social.seguridad.barbarus.action;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSON;
import com.social.seguridad.barbarus.json.ResultJSONMarker;
import com.social.seguridad.barbarus.seguridadvecinal.MainActivity;
import com.social.seguridad.barbarus.seguridadvecinal.R;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by braian on 04/12/2016.
 */
public class CerrarSesion implements Asynchtask {

    private Activity activity;
    private ResultJSON resultJSON;

    public CerrarSesion(Activity activity){
        this.activity = activity;
    }

    public void cerrar(String email , String token){
        Log.d("Seguridad Vecinal", "Cerrar sesion");
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", email);
        datos.put("token", token);

        WebService wb = new WebService(
                URL.SERVER_URL + "/closeSessionUser",
                datos,
                this.activity,
                CerrarSesion.this,
                WebService.TYPE.POST,
                true
        );
        wb.execute("");
    }

    @Override
    public void processFinish(String result) {
        ResultJSON resultJSON = JSONParse.ParseJSON(result);
        if(null != resultJSON && null != resultJSON.getMessage()){
            if("OK".equals(resultJSON.getStatus())){
                MainActivity activity = (MainActivity)this.activity;
                activity.cerrarSesion();
            }else {
                Toast.makeText(this.activity, resultJSON.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this.activity, R.string.mensajeAlertaNoEnviada , Toast.LENGTH_SHORT).show();
        }
    }
}

