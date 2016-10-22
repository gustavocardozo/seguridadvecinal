package com.social.seguridad.barbarus.json;

/**
 * Created by braian on 16/10/2016.
 */
public class ResultJSON {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    private String status;
    private String message;


    public ResultJSON(String status, String message) {
        this.status = status;
        this.message = message;
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
