package com.social.seguridad.barbarus.seguridadvecinal;

import android.content.Intent;
import android.content.res.TypedArray;
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
import com.social.seguridad.barbarus.models.ModelInit;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
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

    ArrayAdapter<CharSequence> adapterProvincias;
    ArrayAdapter<CharSequence> adapterLocalidad;
    ArrayAdapter<CharSequence> adapterBarrios;


    private int provinciaPosicion   = 0;
    private int localidadPosicion   = 0;
    private int comunaPosicion      = 0;
    private ESTADOS_COMBO estados_combo = ESTADOS_COMBO.CARGA_INICIAl;

    private ModelInit init = new ModelInit();

    private enum  ESTADOS_COMBO{
            CARGA_INICIAl, CARGA_COMPLETADA
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_inicial);

        //CONF
        conf = new Configuracion(this);

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

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        //adapterProvincias = ArrayAdapter.createFromResource(
          //      this, R.array.provincias, android.R.layout.simple_spinner_item);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, init.getProvincias());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        //adapterProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //this.spinner_provincias.setAdapter(adapterProvincias);
        this.spinner_provincias.setAdapter(dataAdapter);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinProvincias:
                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.provincias_x_localidades);
                CharSequence[] localidades = arrayLocalidades.getTextArray(position);
                arrayLocalidades.recycle();


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item,
                        init.getLocalidadesByProvincia(parent.getItemAtPosition(position).toString()));

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Specify the layout to use when the list of choices appears
                //adapterProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                //this.spinner_provincias.setAdapter(adapterProvincias);
                //this.spinner_provincias.setAdapter(dataAdapter);
                // This activity implements the AdapterView.OnItemSelectedListener
                //this.spinner_provincias.setOnItemSelectedListener(this);


                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                /*ArrayAdapter<CharSequence> adapter_localidades = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, localidades);

                // Specify the layout to use when the list of choices appears
                adapter_localidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)*/

                // Apply the adapter to the spinner
                this.spinner_localidades.setAdapter(dataAdapter);


                this.provincia =  parent.getItemAtPosition(position).toString();
                this.provinciaPosicion = position;



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


    private void spinProvincia(){

    }
    private void spinLocalidad(){

    }
    private void spinBarrio(AdapterView<?> parent,int position){
        // Retrieves an arr
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
                conf.setBarrio(barrio);
                conf.setProvincia(provincia);
                conf.setLocalidad(localidad);
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
            finish();
            return true;
        }
        return false;
    }
}
