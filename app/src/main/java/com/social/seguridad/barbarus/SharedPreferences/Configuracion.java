package com.social.seguridad.barbarus.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.widget.Toast;

/**
 * Created by braian on 17/08/2016.
 */
public class Configuracion {
    //Nombre de la configuracion
    private final String SHARED_PREFS_FILE = "HMPrefs";

    //
    private final String KEY_EMAIL = "email";
    private final String KEY_TOKEN = "token";

    //Ultima ubicacion encontrada por la aplicacion
    private final String KEY_LAST_KNOW_LATITUD  = "LAST_KNOW_LATITUD";
    private final String KEY_LAST_KNOW_LONGITUD = "LAST_KNOW_LONGITUD";
    private final String KEY_LAST_KNOW_ADDRESS  = "LAST_KNOW_ADDRESS";

    //ubicaciones guardadas por el usuario como predeterminadas
    private final String KEY_LATITUD    = "LATITUD";
    private final String KEY_LONGITUD   = "LONGITUD";
    private final String KEY_ADDRESS    = "ADDRESS";


    private Context context;

    public Configuracion(Context context){
        this.context = context;
    }


    private SharedPreferences getSettings(){
        return context.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String getUserEmail(){
        return getSettings().getString(KEY_EMAIL, null);
    }

    public void setUserEmail(String email){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_EMAIL, email );
        editor.commit();
    }

    public void deleteUserEmail(){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.remove(KEY_EMAIL);
        editor.apply();
    }


    public void setToken(String token){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_TOKEN, token );
        editor.commit();
    }

    public String getToken(){
        return getSettings().getString(KEY_TOKEN, null);
    }


    public void setLastKnowLatitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LAST_KNOW_LATITUD, String.valueOf(value) );
        editor.commit();
    }

    public double getLastKnowtLatitud(){
        String value = getSettings().getString(KEY_LAST_KNOW_LATITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }

    public void setLastKnowLongitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LAST_KNOW_LONGITUD, String.valueOf(value) );
        editor.commit();
    }

    public double getLastKnowLongitud(){
        String value = getSettings().getString(KEY_LAST_KNOW_LONGITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }


    public void setLastKnowAddresses(String addresses) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LAST_KNOW_ADDRESS,addresses );
        editor.commit();

    }

    public String getLastKnowAddresses(){
        return getSettings().getString(KEY_LAST_KNOW_ADDRESS , null);
    }


    public void setLatitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LATITUD, String.valueOf(value) );
        editor.commit();
    }

    public double getLatitud(){
        String value = getSettings().getString(KEY_LATITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }

    public void setLongitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LONGITUD, String.valueOf(value) );
        editor.commit();
    }

    public double getLongitud(){
        String value = getSettings().getString(KEY_LONGITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }


    public void setAddresses(String addresses) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_ADDRESS, addresses);
        editor.commit();
    }

    public String getAddresses(){
        return getSettings().getString(KEY_LAST_KNOW_ADDRESS , null);
    }
}

