package com.social.seguridad.barbarus.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by braian on 26/11/2016.
 */
public class ModelInit {

    private List<Provincia> provincias = new ArrayList<>();

    public ModelInit() {

        Provincia provinciaBuenosAires = new Provincia("Buenos Aires");

        /*Localidades*/
        addLocalidad(provinciaBuenosAires, "Jose C Paz" , false ,
        "El Salvador", "Monte Criollo", "Parque Pero", "Parque Golf Club", "Yei Porá", "Parque Alvear IV",
        "Parque Alvear III", "Sol y Verde","Rincón de Tortuguitas","20 de Junio","Sagrada Familia",
        "Urquiza", "San Atilio","Vucetich","Las Casitas", "Rooswel","Frino", "Lido","Zunino","Alberdi",
        "Provincias Unidas","Frino sur","Villa Hermosa","Piñero","San Adolfo","25 de Mayo","Los Prados",
        "Primavera","San Luis","San Ignacio","Arricau","El Ceibo","El Triángulo","La Paz","Gral. San Martín",
        "Argital","Aquinaga","Centenario","Alberdi Oeste","Santa Rita","Santa Paula","Villa Iglesias",
        "El Ombú","La Diagonal","El Cruce","Arquitectura","9 de Julio","Villa Germano","La Pilarica",
        "Antartida Argentina","Infico","Ideal","Villa altube", "Mariano Moreno", "Altos de José C. Paz",
        "Las Acacias","Parque Jardín San Miguel","San Gabriel","Parque Abascal","Las Heras",
        "Sarmiento Norte","Sarmiento","Mirador de Altube");

                provincias.add(provinciaBuenosAires);
    }



    public List<String> getProvincias(){
        List<String> p = new ArrayList<>();
        for (Provincia provincia : provincias  ) {
            p.add(provincia.getName());
        }

        return  p;
    }

    public List<String> getLocalidadesByProvincia(String provincia){
        List<String> l = new ArrayList<>();
        for (Provincia p : provincias  ) {
            if(p.getName().equals(provincia)){
                for (Localidad localidad:
                     p.getLocalidades()) {
                    l.add(localidad.getName());
                }
            }
        }

        return  l;
    }

    public List<String> getComunaByLocalidad(String provincia , String localidad){
        List<String> c = new ArrayList<>();
        for (Provincia p : provincias  ) {
            if(p.getName().equals(provincia)){
                for (Localidad l: p.getLocalidades()) {
                    if(l.getName().equals(localidad)){
                        for (Comuna comuna : l.getComunas()){
                            c.add(comuna.getName());
                        }
                    }
                }
            }
        }

        return  c;

    }

    public Localidad getLocalidadByKey(String provincia , String localidad){
        List<String> c = new ArrayList<>();
        for (Provincia p : provincias  ) {
            if(p.getName().equals(provincia)){
                for (Localidad l: p.getLocalidades()) {
                    if(l.getName().equals(localidad)){
                        return l;
                    }
                }
            }
        }

        return null;
    }


    private void addLocalidad(Provincia provincia ,
                              String localidadName ,
                              boolean especifico,
                              String ... comunas){
        Localidad localidad = new Localidad(localidadName , especifico);
        for (String comuna : comunas  ) {
            addComuna(comuna, localidad);
        }
        provincia.setLocalidad(localidad);
    }

    public void addComuna(String name , Localidad localidad) {
        Comuna comuna = new Comuna(name);
        localidad.getComunas().add(comuna);
    }
}
