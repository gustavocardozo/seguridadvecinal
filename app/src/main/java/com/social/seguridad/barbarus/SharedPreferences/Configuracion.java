package com.social.seguridad.barbarus.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private final String KEY_LAST_KNOW_DATE     = "LAST_KNOW_DATE";

    //ubicaciones guardadas por el usuario como predeterminadas
    private final String USAR_POSICION_GUARDADA = "USAR_POSICION_GUARDADA";
    private final String KEY_LATITUD    = "LATITUD";
    private final String KEY_LONGITUD   = "LONGITUD";
    private final String KEY_ADDRESS    = "ADDRESS";

    //configuraciones guardadas por el usuario
    private final String PROVINCIA    = "PROVINCIA";
    private final String LOCALIDAD   = "LOCALIDAD";
    private final String BARRIO    = "BARRIO";

    private final String EN_SESSION = "EN_SESSION";


    //TEST
    // Otro test


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

    public Double getLastKnowtLatitud(){
        String value = getSettings().getString(KEY_LAST_KNOW_LATITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }


    public void setLastKnowLongitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LAST_KNOW_LONGITUD, String.valueOf(value) );
        editor.commit();
    }

    public Double getLastKnowLongitud(){
        String value = getSettings().getString(KEY_LAST_KNOW_LONGITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }

    public void setLastKnowAddresses(String addresses) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LAST_KNOW_ADDRESS,addresses );
        editor.commit();
    }

    public String getLastKnowAddresses(){
        return getSettings().getString(KEY_LAST_KNOW_ADDRESS , "Ninguna");
    }


    public void setLastKnowDate(Date date) {
        SharedPreferences.Editor editor = getSettings().edit();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        editor.putString(KEY_LAST_KNOW_DATE, format );
        editor.commit();
    }

    public Date getLastKnowDate(){
        String date =  getSettings().getString(KEY_LAST_KNOW_DATE , null);
        if(date != null){
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date format = formatter.parse(date);
            } catch (ParseException e) {
                return  null;
            }
        }
        return null;
    }


    public void setUsarPosicionGuardada(boolean b){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(USAR_POSICION_GUARDADA , b );
        editor.commit();
    }

    public boolean getUsarPosicionGuardad(){
        return getSettings().getBoolean(USAR_POSICION_GUARDADA, false);
    }

    public void setLatitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LATITUD, String.valueOf(value) );
        editor.commit();
    }

    public Double getLatitud(){
        String value = getSettings().getString(KEY_LATITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }


    public void setLongitud(double value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_LONGITUD, String.valueOf(value) );
        editor.commit();
    }

    public Double getLongitud(){
        String value = getSettings().getString(KEY_LONGITUD, null);
        return null != value && value != "" ? Double.parseDouble(value) : null;
    }

    public void setAddresses(String addresses) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_ADDRESS, addresses);
        editor.commit();
    }

    public String getAddresses(){
        return getSettings().getString(KEY_LAST_KNOW_ADDRESS , "Ninguna");
    }








    public void setProvincia(String provincia){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(PROVINCIA, provincia);
        editor.commit();
    }
    public String getProvincia(){
        return getSettings().getString(PROVINCIA ,"");
    }

    public void setLocalidad(String localidad){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(LOCALIDAD, localidad);
        editor.commit();
    }
    public String getLocalidad(){
        return getSettings().getString(LOCALIDAD ,"");
    }

    public void setBarrio(String barrio){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(BARRIO, barrio);
        editor.commit();
    }
    public String getBarrio(){
        return getSettings().getString(BARRIO ,"");
    }

    public void setEnSession(boolean en_session){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(EN_SESSION, en_session);
        editor.commit();
    }
    public boolean getEnSession(){
        return getSettings().getBoolean(EN_SESSION ,false);
    }

    public void cleanUserPreferences(){
        this.setEnSession(false);
    }

}

