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
        Provincia provinciaSantaFe = new Provincia("Santa Fe");
        Provincia provinciaEntreRios = new Provincia("Entre Rios");

        /*Localidades*/
        addLocalidad(provinciaBuenosAires, "Jose C Paz" , false , "Alberdi","El Salvador","Maximo" ,"Parque Alvear","Primavera" ,"Santa Rita", "San Luis","Trujui");
        addLocalidad(provinciaBuenosAires ,"San miguel" , false , "Mitre" ,"Otro");
        addLocalidad(provinciaBuenosAires, "Tortuguitas", false ,"Parque Alvear II","Tortuguitas");
        addLocalidad(provinciaBuenosAires, "Capital Federal", true ,"Agronomia","Almagro","Balvanera","Barracas","Belgrano","Boedo","Chacarita","Colegiales","Constitucion","Caballito","Microcentro","Monserrat","Nunez","Nueva Pompeya","Palermo","Parque Chacabuco","Puerto Madero","Flores","Floresta","Recoleta","Retiro","San Cristobal","San Nicolas","San Telmo","Villa Deboto","Villa Crespo");

        addLocalidad(provinciaSantaFe ,"San Francisco" , false , "25 de Mayo");
        addLocalidad(provinciaEntreRios ,"Colón" , false , "Colón" );

        provincias.add(provinciaBuenosAires);
        provincias.add(provinciaSantaFe);
        provincias.add(provinciaEntreRios);

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
