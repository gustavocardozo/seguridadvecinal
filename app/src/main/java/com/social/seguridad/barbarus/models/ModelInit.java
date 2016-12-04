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
        addLocalidad(provinciaBuenosAires, "Jose C Paz" , -34.524165019503684,  -58.77218160000001 ,"Alberdi","El Salvador","Maximo" ,"Parque Alvear","Primavera" ,"Santa Rita", "San Luis","Trujui");
        addLocalidad(provinciaBuenosAires ,"San miguel" ,-34.54615442105987 , -58.71619853344117 , "Mitre" ,"Otro");
        addLocalidad(provinciaBuenosAires, "Tortuguitas",-34.49596349999999,-58.7882658,"Parque Alvear II","Tortuguitas");
        addLocalidad(provinciaBuenosAires, "Capital Federal",-34.6036844,-58.3815591,"Agronomia","Almagro","Balvanera","Barracas","Belgrano","Boedo","Chacarita","Colegiales","Constitucion","Caballito","Microcentro","Monserrat","Nunez","Nueva Pompeya","Palermo","Parque Chacabuco","Puerto Madero","Flores","Floresta","Recoleta","Retiro","San Cristobal","San Nicolas","San Telmo","Villa Deboto","Villa Crespo");

        addLocalidad(provinciaSantaFe ,"San Francisco" , -31.42902780716834  , -62.08063277568971 , "25 de Mayo");
        addLocalidad(provinciaEntreRios ,"Colón" ,-32.22866547279823  , -58.148557275201426 , "Colón" );

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
                              double latitud,
                              double longitud,
                              String ... comunas){
        Localidad localidad = new Localidad(localidadName , latitud , longitud);
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
