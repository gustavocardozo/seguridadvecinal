package com.social.seguridad.barbarus.json;

import java.util.Date;

/**
 * Created by braian on 29/10/2016.
 */
public class ResultJSONMarker {


    private double latitud ;
    private double longitud ;
    private String lugar;
    private String fecha;
    private String tipoAlerta;
    private String message;

    public ResultJSONMarker(double latitud, double longitud, String lugar, String fecha, String tipoAlerta, String message) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
        this.fecha = fecha;
        this.tipoAlerta = tipoAlerta;
        this.message = message;
    }


    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
