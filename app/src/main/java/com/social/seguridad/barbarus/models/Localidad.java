package com.social.seguridad.barbarus.models;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by braian on 26/11/2016.
 */
public class Localidad {

    private String name ;
    //Si en la busqueda de latitud y longitud tiene que ser especifico
    private boolean especifico = false;

    private List<Comuna> comunas = new ArrayList<>();

    public Localidad(String name ){
        this.name = name;
    }

    public Localidad(String name , boolean especifico){
        this.name = name;
        this.especifico = especifico;
    }

    public String getName(){
        return this.name;
    }
    public List<Comuna> getComunas(){
        return this.comunas;
    }


    public boolean getEspecifico(){return  this.especifico; }
}
