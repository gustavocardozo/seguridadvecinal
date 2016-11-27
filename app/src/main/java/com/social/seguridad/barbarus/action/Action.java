package com.social.seguridad.barbarus.action;

/**
 * Created by braian on 27/11/2016.
 */
public abstract class Action {

    public abstract void enviar(String email , String token , String lugar , String latitud , String longitud);
}
