package com.social.seguridad.barbarus.seguridadvecinal;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSON;
import com.social.seguridad.barbarus.json.ResultJSONConfiguracion;
import com.social.seguridad.barbarus.models.Localidad;
import com.social.seguridad.barbarus.models.ModelInit;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by braian on 18/11/2016.
 */
public class ConfiguracionInicialActivity extends AppCompatActivity implements Asynchtask,AdapterView.OnItemSelectedListener {

    private Configuracion conf;

    String provincia = "";
    String localidad = "";
    String barrio = "";

    Spinner spinner_provincias;
    Spinner spinner_localidades;
    Spinner spinner_barrios;

    ArrayAdapter<String> adapterProvincias;
    ArrayAdapter<String> adapterLocalidad;
    ArrayAdapter<String> adapterBarrios;

    private ModelInit init = new ModelInit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_inicial);

        //CONF
        conf = new Configuracion(this);
        if(!conf.getEnSession()) {
            Intent intent = new Intent(ConfiguracionInicialActivity.this,
                    PresentationActivity.class);
            startActivityForResult(intent, 0);
        }

        // Combos
        this.spinner_provincias = (Spinner) findViewById(R.id.spinProvincias);
        this.spinner_localidades = (Spinner) findViewById(R.id.spinLocalidades);
        this.spinner_barrios = (Spinner) findViewById(R.id.spinBarrios);
        cargarCombos();

        Button configuracionButton = (Button) findViewById(R.id.configuracionButton);
        configuracionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                guardarConfiguracion();
            }
        });
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SplashScreenActivity.class);

        if(Build.VERSION.SDK_INT >= 11) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        intent.putExtra("EXIT", true);
        startActivity(intent);
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

        Localidad localidadObj = init.getLocalidadByKey(provincia, localidad);
        datos.put("especifico", String.valueOf(localidadObj != null ? localidadObj.getEspecifico() : false));

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
        ResultJSONConfiguracion resultJSON = JSONParse.ParseJSONConfiguracion(result);

        if(null != resultJSON){
            if(ResultJSON.STATUS_OK.equals(resultJSON.getStatus())){
                conf.setLocalidad(localidad);
                conf.setProvincia(provincia);
                conf.setBarrio(barrio);
                conf.setLatitudMap(resultJSON.getLatitud());
                conf.setLongitudMap(resultJSON.getLongitud());

                Toast.makeText(this, "Sus datos fueron guardados correctamente" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ConfiguracionInicialActivity.this , MainActivity.class);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            conf.deleteUserEmail();
            onBackPressed();
            return true;
        }
        return false;
    }
}
