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
        addLocalidad(provinciaBuenosAires, "Jose C Paz" , "El Salvador" ," Primavera" , "San Luis");
        addLocalidad(provinciaBuenosAires ,"San migule" , "Mitre" ,"Otro" );

        addLocalidad(provinciaSantaFe ,"San juan" , "Pepe" ,"Memo" );
        addLocalidad(provinciaEntreRios ,"San juan" , "Pepe" ,"Memo" );


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


    private void addLocalidad(Provincia provincia , String localidadName , String ... comunas){
        Localidad localidad = new Localidad(localidadName);
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
