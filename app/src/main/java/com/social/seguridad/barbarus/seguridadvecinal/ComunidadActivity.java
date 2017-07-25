package com.social.seguridad.barbarus.seguridadvecinal;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Braian on 24/07/2017.
 */

public class ComunidadActivity extends AppCompatActivity implements Asynchtask {

    private Configuracion conf;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CONF
        conf = new Configuracion(this);

        // no more this
        // setContentView(R.layout.list_fruit);
        setContentView(R.layout.activity_comunidad);

        listView = (ListView) findViewById(R.id.listview);
        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);*/

        buildBottombar();
        obtenerMiComunidad();
    }

    private void buildBottombar(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarComunidad);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                accionBottombar(tabId);
            }
        });
    }

    private void accionBottombar(@IdRes int tabId){
        if(tabId == R.id.tab_home){
            Intent intent = new Intent(ComunidadActivity.this, MainActivity.class);
            startActivityForResult(intent, 0);
        }
    }




    public void obtenerMiComunidad(){
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", conf.getUserEmail());
        WebService wb = new WebService(
                URL.SERVER_URL + "/getUsers",
                datos,
                this,
                this,
                WebService.TYPE.GET,
                true
        );
        wb.execute("");

    }

    @Override
    public void processFinish(String result) {
        List<String> resultJSON = JSONParse.ParseJSONComunidad(result);

        if(null != resultJSON){

            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, resultJSON);
            listView.setAdapter(adapter);
        }else{
            Toast.makeText(this, "No se ha podido conectar con el servidor" , Toast.LENGTH_LONG).show();
        }
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


}

