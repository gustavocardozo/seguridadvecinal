package com.social.seguridad.barbarus.models;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by braian on 26/11/2016.
 */
public class Localidad {

    private String name ;
    private double latitud;
    private double longitud;

    private List<Comuna> comunas = new ArrayList<>();

    public Localidad(String name , double latitud , double longitud){
        this.name = name;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getName(){
        return this.name;
    }
    public List<Comuna> getComunas(){
        return this.comunas;
    }

    public double getLatitud(){
        return this.latitud;
    }

    public double getLongitud(){
        return  this.longitud;
    }
}
