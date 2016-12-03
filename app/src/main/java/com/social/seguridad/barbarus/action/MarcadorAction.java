package com.social.seguridad.barbarus.action;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSON;
import com.social.seguridad.barbarus.json.ResultJSONMarker;
import com.social.seguridad.barbarus.seguridadvecinal.MainActivity;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by braian on 02/12/2016.
 */
public class MarcadorAction implements Asynchtask {

    public static String ALERTA= "AlertaMarker";

    private Activity activity;

    private ResultJSONMarker resultJSONMarker;

    public MarcadorAction(Activity activity){
        this.activity = activity;
    }

    public void enviar(String email , String token , String mensaje, String latitud , String longitud){
        Log.d("Seguridad Vecinal", "add Marker");
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", email);
        datos.put("token", token);
        datos.put("longitud", longitud);
        datos.put("latitud", latitud);
        datos.put("alerta", ALERTA);
        datos.put("mensaje", mensaje );


        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);

        this.resultJSONMarker =
                new ResultJSONMarker(Double.valueOf(latitud) , Double.valueOf(longitud) , "", strDate , ALERTA , mensaje  );

        WebService wb = new WebService(
                URL.SERVER_URL + "/addMarker",
                datos,
                this.activity,
                MarcadorAction.this,
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
                activity.addMarker(this.resultJSONMarker);
            }
            Toast.makeText(this.activity, resultJSON.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}
