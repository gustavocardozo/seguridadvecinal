package com.social.seguridad.barbarus.json;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by braian on 24/07/2016.
 */
public class JSONParse {

    public static ResultJSON ParseJSON(String json) {
        ResultJSON resultJSON = null;

        if (json != null) {
            try {
                JSONObject jObj = new JSONObject(json);
                resultJSON = new ResultJSON(jObj.getString("status"), jObj.getString("message"));

            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }

        return resultJSON;
    }


    public static ResultJSONConfiguracion ParseJSONConfiguracion(String json) {
        ResultJSONConfiguracion resultJSON = null;

        if (json != null) {
            try {
                JSONObject jObj = new JSONObject(json);
                resultJSON = new ResultJSONConfiguracion(
                        jObj.getDouble("latitud"),
                        jObj.getDouble("longitud"),
                        jObj.getString("status"),
                        jObj.getString("message"));

            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }

        return resultJSON;
    }




    public static List<ResultJSONMarker> ParseJSONMarker(String json){
        List<ResultJSONMarker> resultJSONMarkers = new ArrayList<>();

        try {
            JSONArray markers = new JSONArray(json);
            for (int i = 0; i < markers.length(); i++) {
                try {
                    JSONObject c = markers.getJSONObject(i);
                    resultJSONMarkers.add(new ResultJSONMarker(c.getDouble("latitud") , c.getDouble("longitud"),
                            c.getString("lugar") , c.getString("fecha") , c.getString("tipoAlerta"),
                            c.getString("mensaje"), c.getString("titulo")));
                }catch (Exception e){
                    //ERROR AL OBTENER LOS VALORES
                    String a = e.getMessage();
                }
            }

        }catch (Exception e){
            //ERROR EN EL RESPONSE DEL SERVICIO
        }

        return resultJSONMarkers;
    }


    public static List<String>  ParseJSONComunidad(String json) {
        ResultJSONConfiguracion resultJSON = null;
        List<String> resultado = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray jsonarray = new JSONArray(json);
                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    resultado.add(jsonobject.getString("nombrecompleto"));
                }
            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }

        return resultado;
    }


}
