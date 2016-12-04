package com.social.seguridad.barbarus.json;

/**
 * Created by braian on 04/12/2016.
 */
public class ResultJSONConfiguracion {


    private double latitud ;
    private double longitud ;
    private String status;
    private String message;

    public ResultJSONConfiguracion(double latitud, double longitud, String status, String message) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
