package com.social.seguridad.barbarus.seguridadvecinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
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
 * Created by braian on 03/08/2016.
 */
public class RegistrarseActivity extends AppCompatActivity implements Validator.ValidationListener , Asynchtask {


    //inputs
    @NotEmpty(message = "Complete nombre"  )
    EditText nombreCompletoTXT;

    @NotEmpty(message = "Complete DNI" )
    EditText dniTXT;

    @NotEmpty(message = "Complete nro de telefono" )
    EditText telefonoTXT;

    @NotEmpty(message = "Complete el correo electronico" )
    @Email(message = "Ingrese un correo electronico valido")
    EditText emailTXT;

    @NotEmpty(message = "Complete su contraseña" )
    @Password(message = "Debe contener letras , numeros , simbolos" , scheme = Password.Scheme.ALPHA_NUMERIC_SYMBOLS)
    EditText passwordTXT;


    //validador
    Validator validator;


    Spinner spinner;
    TabHost tabHost;
    String[] provincias = {"Buenos Aires" , "La pampa"};
    String[] buenosAires_localidades = {"Jose C Paz", "San Miguel"};
    String[] joseCPaz_barrios = {"El salvador", "Abascal"};
    ArrayAdapter<String> adapterProvincias;
    ArrayAdapter<String> adapterLocalidad;
    ArrayAdapter<String> adapterBarrios;


    private Configuracion conf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        buildInput();

        //CONF
        conf = new Configuracion(this);

        Toast.makeText(this, conf.getToken() != null ? conf.getToken() : "no token" , Toast.LENGTH_SHORT).show();

        Button registrarmeButton = (Button) findViewById(R.id.registrarmeButton);
        registrarmeButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){ lanaActivityLogin(v); }
        });
    }

    private void lanaActivityLogin(View v) {
        boolean isValidate = true;
        if(!(nombreCompletoTXT.getText().toString().length() > 4)){
            isValidate = false;
            nombreCompletoTXT.requestFocus();
            nombreCompletoTXT.setError("Debe tener al menos 4 letras");
            return;
        }

        if(!(dniTXT.getText().toString().length() > 6)){
            isValidate = false;
            dniTXT.requestFocus();
            dniTXT.setError("Debe tener al menos 7 caracteres");
            return;
        }

        validator.validate();
    }


    private void buildInput(){
        nombreCompletoTXT = (EditText)findViewById(R.id.nombreCompletoTXT);
        dniTXT = (EditText)findViewById(R.id.dniTXT);
        telefonoTXT = (EditText)findViewById(R.id.telefonoTXT);
        emailTXT = (EditText)findViewById(R.id.emailTXT);
        passwordTXT = (EditText)findViewById(R.id.passwordTXT);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        this.registrar();
    }



    public void registrar(){
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("nombrecompleto", this.nombreCompletoTXT.getText().toString());
        datos.put("cuit", this.dniTXT.getText().toString());
        datos.put("telefono", this.telefonoTXT.getText().toString());
        datos.put("email", this.emailTXT.getText().toString());
        datos.put("password", this.passwordTXT.getText().toString());
        datos.put("token", conf.getToken());

        if(null != conf.getToken()){
            WebService wb = new WebService(
                    URL.SERVER_URL + "/addUser",
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
        ResultJSON resultJSON = JSONParse.ParseJSON(result);

        if(null != resultJSON){
            if(ResultJSON.STATUS_OK.equals(resultJSON.getStatus())){
                Intent intent = new Intent(RegistrarseActivity.this , LoginActivity.class);
                startActivityForResult(intent,0);
            }else{
                Toast.makeText(this, resultJSON.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) { ((EditText) view).setError(message); }
            else { Toast.makeText(this, message, Toast.LENGTH_LONG).show(); }
        }
    }
}
