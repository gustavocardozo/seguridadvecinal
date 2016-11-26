package com.social.seguridad.barbarus.seguridadvecinal;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;
import com.wang.avi.AVLoadingIndicatorView;
import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSON;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by braian on 13/08/2016.
 */
public class SettingActivity extends AppCompatActivity implements Asynchtask,AdapterView.OnItemSelectedListener {

    AVLoadingIndicatorView actualizarLocalizacionAVLoadingIndicatorView;
    private Configuracion conf;

    private boolean runActualizacion = false;

    TextView localizacion;

    String provincia = "";
    String localidad = "";
    String barrio = "";

    Spinner spinner_provincias;
    Spinner spinner_localidades;
    Spinner spinner_barrios;

    ArrayAdapter<CharSequence> adapterProvincias;
    ArrayAdapter<CharSequence> adapterLocalidad;
    ArrayAdapter<CharSequence> adapterBarrios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //CONF
        conf = new Configuracion(this);

        this.actualizarLocalizacionAVLoadingIndicatorView =
                (AVLoadingIndicatorView) findViewById(R.id.loadingIndicatorView);

        this.localizacion = (TextView) findViewById(R.id.localizaciontextView);
        //this.localizacion.setText(null != conf.getToken() ? conf.getToken() : "Ninguna");
        this.localizacion.setText("Ninguna");
        this.actualizarLocalizacionAVLoadingIndicatorView.hide();

        // Combos
        this.spinner_provincias = (Spinner) findViewById(R.id.spinProvincias);
        this.spinner_localidades = (Spinner) findViewById(R.id.spinLocalidades);
        this.spinner_barrios = (Spinner) findViewById(R.id.spinBarrios);
        cargarCombos();

        Button actualizarLocalizacionButton = (Button) findViewById(R.id.actualizarLocalizacionButton);
        actualizarLocalizacionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                actualizarLocalizacionAccion(v);
            }
        });

        buildBottombar();
    }


    private void buildBottombar(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarSettings);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                accionBottombar(tabId);
            }
        });
    }


    private void accionBottombar(@IdRes int tabId){
        if(tabId == R.id.tab_check){
            guardarConfiguracion();

        }else if(tabId == R.id.tab_cancelar){

            if(null == conf.getSessionUbicacion()){
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            }else {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        }
    }


    private void cargarCombos() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        adapterProvincias = ArrayAdapter.createFromResource(
                this, R.array.provincias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spinner_provincias.setAdapter(adapterProvincias);
        // This activity implements the AdapterView.OnItemSelectedListener
        this.spinner_provincias.setOnItemSelectedListener(this);



        adapterLocalidad = ArrayAdapter.createFromResource(
                this, R.array.localidades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterLocalidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spinner_localidades.setAdapter(adapterLocalidad);
        // This activity implements the AdapterView.OnItemSelectedListener
        this.spinner_localidades.setOnItemSelectedListener(this);




        adapterBarrios = ArrayAdapter.createFromResource(
                this, R.array.barrios, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBarrios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spinner_barrios.setAdapter(adapterBarrios);
        // This activity implements the AdapterView.OnItemSelectedListener
        this.spinner_barrios.setOnItemSelectedListener(this);

    }

    private void actualizarLocalizacionAccion(View v) {
        if(!this.runActualizacion){
            this.runActualizacion = true;
            this.actualizarLocalizacionAVLoadingIndicatorView.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        closeLoader();
                    }catch (Exception e){
                        closeLoader();
                    }
                }
            }).start();
        }
    }


    private void closeLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                localizacion.setText(null != conf.getLastKnowAddresses() ? conf.getLastKnowAddresses() : "Ninguna");
                actualizarLocalizacionAVLoadingIndicatorView.hide();
                runActualizacion = false;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinProvincias:
                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.provincias_x_localidades);
                CharSequence[] localidades = arrayLocalidades.getTextArray(position);
                arrayLocalidades.recycle();

                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter_localidades = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, localidades);

                // Specify the layout to use when the list of choices appears
                adapter_localidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                this.spinner_localidades.setAdapter(adapter_localidades);


                this.provincia =  parent.getItemAtPosition(position).toString();


                break;
            case R.id.spinLocalidades:
                // Retrieves an array
                TypedArray arrayBarrios = getResources().obtainTypedArray(
                        R.array.barrios_x_localidades);
                CharSequence[] barrios = arrayBarrios.getTextArray(position);
                arrayBarrios.recycle();

                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter_barrios = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, barrios);

                // Specify the layout to use when the list of choices appears
                adapter_barrios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                this.spinner_barrios.setAdapter(adapter_barrios);

                this.localidad = parent.getItemAtPosition(position).toString();

                break;
            case R.id.spinBarrios:
                this.barrio = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void guardarConfiguracion(){
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("provincia", provincia);
        datos.put("localidad", localidad);
        datos.put("comuna", barrio);
        datos.put("email", conf.getUserEmail());

        if(validarDatos()) {
                WebService wb = new WebService(
                        URL.SERVER_URL + "/updateLocationUser",
                        datos,
                        this,
                        this,
                        WebService.TYPE.POST,
                        true
                );
                wb.execute("");
        }else{
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String result) {
        ResultJSON resultJSON = JSONParse.ParseJSON(result);

        if(null != resultJSON){
            if(ResultJSON.STATUS_OK.equals(resultJSON.getStatus())){
                Toast.makeText(this, "Sus datos fueron guardados correctamente" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SettingActivity.this , MainActivity.class);
                startActivityForResult(intent,0);

            }else{
                Toast.makeText(this, resultJSON.getMessage() , Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "No se ha podido conectar con el servidor" , Toast.LENGTH_LONG).show();
        }
    }


    private boolean validarDatos() {
        if(this.provincia == "")
            return false;
        if(this.localidad == "")
            return false;
        if(this.barrio == "")
            return false;

        return true;

    }
}
