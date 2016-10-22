package com.social.seguridad.barbarus.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by braian on 24/07/2016.
 */
public class JSONParse {

    public static ResultJSON ParseJSON(String json) {
        ResultJSON resultJSON = null;

        if (json != null) {
            try {
                JSONArray arr = new JSONArray(json);
                JSONObject obj = arr.getJSONObject(0);
                resultJSON = new ResultJSON(obj.getString("status"), obj.getString("message"));

            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }

        return resultJSON;
    }


}
