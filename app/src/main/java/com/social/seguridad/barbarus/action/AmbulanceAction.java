package com.social.seguridad.barbarus.action;

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
 * Created by braian on 16/10/2016.
 */
public class AmbulanceAction extends  Action implements Asynchtask {

    public static String ALERTA= "Ambulancia";
    private static String NUMERO_ALERTA = "100";
    public static String ALERTA_TITLE = "Alerta " + ALERTA;

    private MainActivity mainActivity;
    private ResultJSONMarker resultJSONMarker;

    public AmbulanceAction(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void processFinish(String result) {
        ResultJSON resultJSON = JSONParse.ParseJSON(result);
        if(null != resultJSON && null != resultJSON.getMessage()){
            if("OK".equals(resultJSON.getStatus())){
                this.mainActivity.addMarker(this.resultJSONMarker);
            }

            Toast.makeText(this.mainActivity, resultJSON.getMessage() , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.mainActivity, R.string.mensajeAlertaNoEnviada , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void enviar(String email, String token, String lugar, String latitud, String longitud) {
        Log.d("Seguridad Vecinal", "Boton apretado");
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", email);
        datos.put("token", token);
        datos.put("lugar", lugar);
        datos.put("longitud", longitud);
        datos.put("latitud", latitud);
        datos.put("alerta", ALERTA);
        datos.put("nroalerta", NUMERO_ALERTA);

        buildMarker(lugar, latitud , longitud);

        WebService wb = new WebService(
                URL.SERVER_URL + "/sendNotification",
                datos,
                this.mainActivity,
                AmbulanceAction.this,
                WebService.TYPE.POST,
                true
        );
        wb.execute("");
    }


    private void buildMarker(String lugar, String latitud, String longitud){
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);

            this.resultJSONMarker =
                    new ResultJSONMarker(Double.valueOf(latitud) , Double.valueOf(longitud) , lugar , strDate , ALERTA , ""  );

        }catch (Exception e){

        }
    }
}
