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



    public static List<ResultJSONMarker> ParseJSONMarker(String json){
        List<ResultJSONMarker> resultJSONMarkers = new ArrayList<>();

        try {
            JSONArray markers = new JSONArray(json);
            for (int i = 0; i < markers.length(); i++) {
                JSONObject c = markers.getJSONObject(i);

                resultJSONMarkers.add(new ResultJSONMarker(c.getDouble("latitud") , c.getDouble("longitud"),
                        c.getString("lugar") , c.getString("fecha") , c.getString("tipoAlerta"), c.getString("mensaje")));
            }

        }catch (Exception e){
            //ERROR EN EL RESPONSE DEL SERVICIO
        }

        return resultJSONMarkers;
    }

}
