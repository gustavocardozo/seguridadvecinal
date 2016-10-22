package com.social.seguridad.barbarus.seguridadvecinal;

import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;

/**
 * Created by retconadmin on 11/07/16.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService{

    private String token;
    private Configuracion conf;

    @Override
    public void onTokenRefresh() {
        this.conf = new Configuracion(this);
        this.token = FirebaseInstanceId.getInstance().getToken();
        this.conf.setToken(this.token);
    }


    public String getToken(){
        return this.token;
    }
}
