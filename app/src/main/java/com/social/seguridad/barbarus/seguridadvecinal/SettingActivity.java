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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.models.ModelInit;
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

    ArrayAdapter<String> adapterProvincias;
    ArrayAdapter<String> adapterLocalidad;
    ArrayAdapter<String> adapterBarrios;

    //Comobo usar configuracion actual
    private SwitchButton switchButton;
    private boolean switchValue = false;

    private String lugarConfigurado;

    //Carga de valores para el combo
    private ModelInit init = new ModelInit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //CONF
        conf = new Configuracion(this);

        this.actualizarLocalizacionAVLoadingIndicatorView =
                (AVLoadingIndicatorView) findViewById(R.id.loadingIndicatorView);
        this.actualizarLocalizacionAVLoadingIndicatorView.hide();


        this.lugarConfigurado = conf.getAddresses();

        this.localizacion = (TextView) findViewById(R.id.localizaciontextView);
        this.localizacion.setText(this.lugarConfigurado);

        switchButton = (SwitchButton) findViewById(R.id.switch1);
        switchValue = conf.getUsarPosicionGuardad();
        switchButton.setChecked( switchValue );
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!lugarConfigurado.equals("Ninguna")){
                    switchValue = isChecked;
                }else{
                    Toast.makeText(getActivity(), "Primero actualize su ubicacion", Toast.LENGTH_SHORT).show();
                    switchButton.setChecked( false );
                }
            }
        });

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


    private SettingActivity getActivity() {
        return this;
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
            if(!conf.getEnSession()){
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            }else {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        }
    }


    private void cargarCombos() {

        List<String> provinciaList = init.getProvincias();
        provincia = provinciaList.get(0);
        adapterProvincias = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, init.getProvincias());
        adapterProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_provincias.setAdapter(adapterProvincias);
        this.spinner_provincias.setOnItemSelectedListener(this);

        List<String> localidadesList = init.getLocalidadesByProvincia(provincia);
        localidad = localidadesList.get(0);
        adapterLocalidad = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, localidadesList);
        adapterLocalidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_localidades.setAdapter(adapterLocalidad);
        this.spinner_localidades.setOnItemSelectedListener(this);
        List<String> comunaList = init.getComunaByLocalidad(provincia , localidad);
        barrio = comunaList.get(0);

        adapterBarrios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,comunaList );
        adapterBarrios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner_barrios.setAdapter(adapterBarrios);
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
                lugarConfigurado = conf.getLastKnowAddresses();
                localizacion.setText(lugarConfigurado);
                actualizarLocalizacionAVLoadingIndicatorView.hide();
                runActualizacion = false;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinProvincias:

                provincia = parent.getItemAtPosition(position).toString();
                List<String> localidades =init.getLocalidadesByProvincia(provincia);
                localidad = localidades.get(0);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, localidades    );
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.spinner_localidades.setAdapter(dataAdapter);


                break;
            case R.id.spinLocalidades:

                this.localidad = parent.getItemAtPosition(position).toString();
                List<String> comunaList = init.getComunaByLocalidad(provincia , localidad);
                barrio = comunaList.get(0);
                adapterBarrios = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item,comunaList );
                adapterBarrios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.spinner_barrios.setAdapter(adapterBarrios);

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
                //Configuracion de los conbos
                conf.setLocalidad(localidad);
                conf.setProvincia(provincia);
                conf.setBarrio(barrio);
                //Guardar si usar la posicion actual
                //Y los campos que se guardaron ubicacion , latitud , longitud
                conf.setUsarPosicionGuardada(switchValue);
                if(switchValue){
                    conf.setAddresses(lugarConfigurado);
                    conf.setLatitud(conf.getLastKnowtLatitud());
                    conf.setLongitud(conf.getLastKnowLongitud());
                }else{
                    conf.setAddresses(null);
                    conf.setLatitud(0);
                    conf.setLongitud(0);
                }

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
