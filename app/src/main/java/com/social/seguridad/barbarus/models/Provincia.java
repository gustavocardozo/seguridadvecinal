package com.social.seguridad.barbarus.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by braian on 26/11/2016.
 */
public class Provincia {

    private String name ;
    private List<Localidad> localidades = new ArrayList<>();


    public Provincia(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    public List<Localidad> getLocalidades(){
        return this.localidades;
    }


    public  void setLocalidad(Localidad localidad){
        this.localidades.add(localidad);
    }
}
