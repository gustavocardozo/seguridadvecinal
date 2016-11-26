package com.social.seguridad.barbarus.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by braian on 26/11/2016.
 */
public class Localidad {

    private String name ;
    private List<Comuna> comunas = new ArrayList<>();

    public Localidad(String name ){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    public List<Comuna> getComunas(){
        return this.comunas;
    }
}
