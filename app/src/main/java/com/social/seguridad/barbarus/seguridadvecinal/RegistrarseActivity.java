package com.social.seguridad.barbarus.seguridadvecinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
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
    @NotEmpty(message = "Complete nombre completo"  )
    @Length(min = 7, message = "Debe contener como mínimo 7 caracteres")
    EditText nameTXT;

    @Length(min = 7, message = "Debe contener como mínimo 7 caracteres")
    @NotEmpty(message = "Complete dni" )
    EditText dniTXT;

    @NotEmpty(message = "Complete nro. de teléfono" )
    EditText nroTelefonoTXT;

    @NotEmpty(message = "Complete el correo electrónico" )
    @Email(message = "Ingrese un correo electrónico válido")
    EditText emailAddressTXT;

    @NotEmpty(message = "Complete su contraseña" )
    @Password(message = "Debe contener letras y números" , scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText password1TXT;

    @Checked(message = "Debe aceptar los términos y condiciones"  )
    CheckBox terminosCbx;


    //validador
    Validator validator;

    private Configuracion conf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        buildInput();

        //CONF
        conf = new Configuracion(this);

        Button registrarmeButton = (Button) findViewById(R.id.configuracionButton);
        registrarmeButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){ lanaActivityLogin(v); }
        });
    }

    private void lanaActivityLogin(View v) {
        boolean isValidate = true;

        validator.validate();
    }


    private void buildInput(){
        nameTXT = (EditText)findViewById(R.id.nameTXT);
        dniTXT = (EditText)findViewById(R.id.dniTXT);
        nroTelefonoTXT = (EditText)findViewById(R.id.nroTelefonoTXT);
        emailAddressTXT = (EditText)findViewById(R.id.emailAddressTXT);
        password1TXT = (EditText)findViewById(R.id.password1TXT);
        terminosCbx = (CheckBox)findViewById(R.id.terminosCbx);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        this.registrar();
    }



    public void registrar(){
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("nombrecompleto", this.nameTXT.getText().toString());
        datos.put("cuit", this.dniTXT.getText().toString());
        datos.put("telefono", this.nroTelefonoTXT.getText().toString());
        datos.put("email", this.emailAddressTXT.getText().toString());
        datos.put("password", this.password1TXT.getText().toString());
        datos.put("token", conf.getToken());

        if(null != conf.getToken()){
            WebService wb = new WebService(
                    URL.SERVER_URL + "/addUser",
                    datos,
                    this,
                    this,
                    WebService.TYPE.POST,
                    true
            );
            wb.execute("");
        }else{
            Toast.makeText(this, "No se obtuvo identificador, por favor reinicie la aplicación" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String result) {
        ResultJSON resultJSON = JSONParse.ParseJSON(result);

        if(null != resultJSON){
            if(ResultJSON.STATUS_OK.equals(resultJSON.getStatus())){
                Toast.makeText(this, "Se registró correctamente" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrarseActivity.this , LoginActivity.class);
                startActivityForResult(intent,0);
            }else{
                Toast.makeText(this, resultJSON.getMessage() , Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, R.string.mensajeNoSalio , Toast.LENGTH_LONG).show();
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
