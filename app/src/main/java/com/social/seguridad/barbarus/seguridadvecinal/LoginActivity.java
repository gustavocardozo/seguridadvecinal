package com.social.seguridad.barbarus.seguridadvecinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSON;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by braian on 05/08/2016.
 */
public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener , Asynchtask {

    private Configuracion conf;


    //inputs
    @NotEmpty(message = "Complete el correo electronico" )
    @Email(message = "Ingrese un correo electronico valido")
    EditText emailTxt;

    @NotEmpty(message = "Complete su contraseña" )
    @Password(message = "Debe contener letras , numeros , simbolos" , scheme = Password.Scheme.ALPHA_NUMERIC_SYMBOLS)
    EditText passwordTxt;

    //validador
    Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //CONF
        conf = new Configuracion(this);

        emailTxt = (EditText)findViewById(R.id.emailText);
        passwordTxt = (EditText)findViewById(R.id.passwordText);

        validator = new Validator(this);
        validator.setValidationListener(this);

        Toast.makeText(this, conf.getToken() != null ? conf.getToken() : "no token" , Toast.LENGTH_SHORT).show();

        Button ingresarButton = (Button) findViewById(R.id.ingresarButton);
        ingresarButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){ lanaActivityIngresar(v); }
        });
    }

    public void lanaActivityIngresar(View v){
        validator.validate();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onValidationSucceeded() {
        conf.setUserEmail(emailTxt.getText().toString());
        login();
        //Intent intent = new Intent(LoginActivity.this , MainActivity.class);
        //startActivityForResult(intent,0);
    }

    public void login(){
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", this.emailTxt.getText().toString());
        datos.put("password", this.passwordTxt.getText().toString());
        datos.put("token", conf.getToken());

        if(null != conf.getToken()){
            WebService wb = new WebService(
                    URL.SERVER_URL + "/login",
                    datos,
                    this,
                    this,
                    WebService.TYPE.POST
            );
            wb.execute("");
        }else{
            Toast.makeText(this, "No se obtuvo identificador, por favor reinicie la aplicaciòn" , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void processFinish(String result) {
        Toast.makeText(this, result , Toast.LENGTH_SHORT).show();

        ResultJSON resultJSON = JSONParse.ParseJSON(result);

        if(null != resultJSON){
            if(ResultJSON.STATUS_OK.equals(resultJSON.getStatus())){
                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                startActivityForResult(intent,0);
            }else{
                Toast.makeText(this, resultJSON.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }else{
            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
            startActivityForResult(intent,0);
        }
    }
}






/*
*

    */