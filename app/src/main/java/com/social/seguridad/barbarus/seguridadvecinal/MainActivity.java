package com.social.seguridad.barbarus.seguridadvecinal;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.social.seguridad.barbarus.Estados.EstadosMarkers;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.URL.URL;
import com.social.seguridad.barbarus.action.Action;
import com.social.seguridad.barbarus.action.AmbulanceAction;
import com.social.seguridad.barbarus.action.CerrarSesion;
import com.social.seguridad.barbarus.action.CriminalAction;
import com.social.seguridad.barbarus.action.FirefighterAction;
import com.social.seguridad.barbarus.action.PoliceAction;
import com.social.seguridad.barbarus.action.ViolenciaGeneroAction;
import com.social.seguridad.barbarus.googleMapUtil.GoogleMapUtil;
import com.social.seguridad.barbarus.json.JSONParse;
import com.social.seguridad.barbarus.json.ResultJSONMarker;
import com.social.seguridad.barbarus.models.Localidad;
import com.social.seguridad.barbarus.models.ModelInit;
import com.social.seguridad.barbarus.webservice.Asynchtask;
import com.social.seguridad.barbarus.webservice.WebService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback , Asynchtask , GoogleMap.OnMapLongClickListener {

    private static final int TIME_RUNTIME = 100;

    private GoogleMap mMap;

    //PARA GUARDAR CONFIGURACIONES
    private Configuracion conf;

    private FloatingActionsMenu mFAB;
    private DonutProgress donutProgress;
    private GoogleApiClient client;
    private Map<String, Timer> buttonsThreads = new HashMap<String, Timer>();

    private Location mLastLocation;
    public LocationManager mLocationManager;

    private List<ResultJSONMarker> markers;
    private EstadosMarkers.ESTADO estadoMarkers = EstadosMarkers.ESTADO.LOCAL;

    private  ModelInit model = new ModelInit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CONF
        conf = new Configuracion(this);
        if(!conf.getEnSession()) {
            Intent intent = new Intent(MainActivity.this,
                    PresentationActivity.class);
            startActivityForResult(intent, 0);
        }
        //Floating y donut progress
        mFAB = (FloatingActionsMenu) findViewById(R.id.fab);
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setVisibility(View.INVISIBLE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Generacion de acciones de los botones
        buildButton();
        // GPS
        //Se inicia el servicio de GPS
        Intent servIntent = new Intent( getApplicationContext() , LocalizacionService.class);
        startService(servIntent);

        buildBottombar();
    }



    private void buildBottombar(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            int i = 0;
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (this.i != 0) {
                    accionBottombar(tabId);
                }
                this.i++;
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            int i = 0;
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (this.i != 0) {
                    accionBottombar(tabId);
                }
                this.i++;
            }
        });
    }


    private void accionBottombar(@IdRes int tabId){
        if (tabId == R.id.tab_inicio) {
        }else if(tabId == R.id.tab_configuracion){
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, 0);
        }else if(tabId == R.id.tab_comunidad){
        }else if(tabId == R.id.tab_salir){
            salir();
        }
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

    //Sale de la aplicacion
    private void salir(){
        new AlertDialog.Builder(this)
                .setMessage("¿Seguro que desea cerrar su sesión?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CerrarSesion cerrarSesion = new CerrarSesion(getThis());
                        cerrarSesion.cerrar(conf.getUserEmail(), conf.getToken());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void cerrarSesion(){
        conf.cleanUserPreferences();
        Intent intent = new Intent(MainActivity.this, PresentationActivity.class);
        if(Build.VERSION.SDK_INT >= 11) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivityForResult(intent, 0);
    }

    /**
     * Arma el progress por x camtidad de tiempo
     *
     * @param key
     */
    private void buildTimer(String key) {
        donutProgress.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        buttonsThreads.put(key, timer);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(buildTimerTask());
            }
        }, 500, 30);
    }

    /**
     * Detiene y remueve el timer
     *
     * @param key
     */
    private void removeTimer(String key) {
        Timer buttonThread_up = buttonsThreads.get(key);
        buttonThread_up.cancel();
        buttonThread_up.purge();
        buttonsThreads.put(key, null);
    }

    /**
     * Resetea el progress y cierra el tool de los botones
     */
    private void restarProgressBar() {
        //restar progressBar
        donutProgress.setVisibility(View.INVISIBLE);
        donutProgress.setProgress(0);
        mFAB.collapse();
    }


    /**
     * Se parar en un metodo generico
     * usar herencia para enviar notificaciones
     * TODO: serapar en otra clase solo de las acciones de los botones
     */
    private void buildButton() {
        buttonsThreads.put("accion_policeman", null);
        buttonsThreads.put("accion_doctor", null);
        buttonsThreads.put("accion_firefighter", null);
        buttonsThreads.put("accion_criminal" , null);
        buttonsThreads.put("accion_venus" , null);

        //Violencia de genero
        findViewById(R.id.accion_venus).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return  alertVenus(v,event);
            }
        });

        //Policia
        findViewById(R.id.accion_policeman).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return  alertPolicia(v,event);
            }
        });

        //Doctor
        findViewById(R.id.accion_doctor).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return alertAmbulancia(v,event);
            }
        });

        //Bombero
        findViewById(R.id.accion_firefighter).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return alertBombero(v, event);
            }
        });

        //Criminal
        findViewById(R.id.accion_criminal).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return  alertCriminal(v, event);
            }
        });
    }

    //Alertas
    private boolean alertVenus(View v, MotionEvent event){
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                buildTimer("accion_venus");
                return true;
            case MotionEvent.ACTION_UP:
                removeTimer("accion_venus");
                if (donutProgress.getProgress() == 100) {
                    //envio notificacion
                    try {
                        ViolenciaGeneroAction violenciaGeneroAction = new ViolenciaGeneroAction(MainActivity.this);
                        enviarAlerta(violenciaGeneroAction);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
                restarProgressBar();
                break;
        }
        return false;
    }

    private boolean alertPolicia(View v, MotionEvent event){
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                buildTimer("accion_policeman");
                return true;
            case MotionEvent.ACTION_UP:
                removeTimer("accion_policeman");
                if (donutProgress.getProgress() == 100) {
                    //envio notificacion
                    try {
                        PoliceAction policeAction = new PoliceAction(MainActivity.this);
                        enviarAlerta(policeAction);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
                restarProgressBar();
                break;
        }
        return false;
    }

    private boolean alertCriminal(View v, MotionEvent event){
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                buildTimer("accion_criminal");
                return true;
            case MotionEvent.ACTION_UP:
                removeTimer("accion_criminal");
                if (donutProgress.getProgress() == 100) {
                    //envio notificacion
                    try {
                        CriminalAction criminalAction = new CriminalAction(MainActivity.this);
                        enviarAlerta(criminalAction);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
                restarProgressBar();
                break;
        }
        return false;
    }

    private boolean alertBombero(View v, MotionEvent event){
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                buildTimer("accion_firefighter");
                return true;
            case MotionEvent.ACTION_UP:
                removeTimer("accion_firefighter");
                if (donutProgress.getProgress() == 100) {
                    //envio notificacion
                    try {
                        FirefighterAction firefighterAction = new FirefighterAction(MainActivity.this);
                        enviarAlerta(firefighterAction);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
                restarProgressBar();
                break;
        }
        return false;
    }

    private boolean alertAmbulancia(View v, MotionEvent event){
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                buildTimer("accion_doctor");
                return true;
            case MotionEvent.ACTION_UP:
                removeTimer("accion_doctor");
                if (donutProgress.getProgress() == 100) {
                    //envio notificacion
                    try {
                        AmbulanceAction ambulanceAction = new AmbulanceAction(MainActivity.this);
                        enviarAlerta(ambulanceAction);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
                restarProgressBar();
                break;
        }
        return false;
    }


    public void enviarAlerta(Action action){
        if(conf.getUsarPosicionGuardad()){
            if(esValidoParaEnviarAlertaConfigurado()){
                action.enviar(conf.getUserEmail(), conf.getToken(),
                        conf.getAddresses(), String.valueOf(conf.getLatitud()),
                        String.valueOf(conf.getLongitud()));
            }
        }else
            if(esValidoParaEnviarAlerta()){
                action.enviar(conf.getUserEmail(), conf.getToken(),
                        conf.getLastKnowAddresses(), String.valueOf(conf.getLastKnowtLatitud()),
                        String.valueOf(conf.getLastKnowLongitud()));
        }
    }

    private boolean esValidoParaEnviarAlertaConfigurado(){
        if(conf.getUserEmail() != null
                && conf.getToken() != null
                && conf.getAddresses() != null
                && conf.getLongitud() != null
                && conf.getLatitud() != null){
            return true;
        }
        Toast.makeText(MainActivity.this,
                "Debe tener una ubicación configurada para poder enviar alertas a su comunidad",
                Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean esValidoParaEnviarAlerta(){
        if(conf.getUserEmail() != null
                && conf.getToken() != null
                && conf.getLastKnowAddresses() != null
                && conf.getLastKnowLongitud() != null
                && conf.getLastKnowtLatitud() != null
                && esValidoUltimaFechadeAddresses(conf.getLastKnowDate())){
            return true;
        }

        Toast.makeText(MainActivity.this,
                "Debe tener una ubicación para poder enviar alertas a su comunidad por " +
                        "favor active su gps o configure una por defecto desde configuraciones",
                Toast.LENGTH_LONG).show();
        return false;
    }


    private  boolean esValidoUltimaFechadeAddresses(Date date){
        if(date == null){
            return  false;
        }

        if(getDateDiff(date , new Date() , TimeUnit.HOURS) < 24){
            return true;
        }

        return  false;
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


    /**
     * Progreso hasta 100
     *
     * @return
     */
    private TimerTask buildTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (donutProgress.getProgress() < 100) {
                            donutProgress.setProgress(donutProgress.getProgress() + 1);
                        }
                    }
                });
            }
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(this);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.
                LinearLayout info = new LinearLayout(context);
                info.setPadding(10, 0 ,10 ,0);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        //Por DEFECTO argentina
        double latitud = -40.4336595504857;
        double longitud = -63.59892055;
        //Si encuentra la localidad
        if(null != conf.getLatitudMap()
                && null != conf.getLongitudMap()){
            latitud = conf.getLatitudMap();
            longitud = conf.getLongitudMap();
        }
        LatLng central = new LatLng(latitud , longitud);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(central, 13));

        loadMarkers("/getMarkers");
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        FragmentManager fm = getFragmentManager();
        LoadMarkerDialogFragment dialogFragment = new LoadMarkerDialogFragment ();

        Bundle args = new Bundle();
        args.putDouble("latitude", latLng.latitude);
        args.putDouble("longitude", latLng.longitude);
        dialogFragment.setArguments(args);

        dialogFragment.show(fm, "Sample Fragment");
    }

    private void loadMarkers(String metodo){
        //Llama el servicio para poder cargar los datos
        //Ahora sera de la comuna del usuaario
        //TODO: que cada x tiempo vaya a buscar apartir de un horario nuevas alertas
        Map<String , String> datos = new HashMap<String, String>();
        datos.put("email", conf.getUserEmail());

        WebService wb = new WebService(
                URL.SERVER_URL + metodo,
                datos,
                this,
                this,
                WebService.TYPE.POST,
                false
        );
        wb.execute("");
    }

    private MainActivity getThis(){
        return this;
    }


    public void addMarker(ResultJSONMarker resultJSONMarker){
        try{
            mMap.addMarker(GoogleMapUtil.buildMarkerOptions(resultJSONMarker.getLatitud(),
                    resultJSONMarker.getLongitud(), resultJSONMarker.getTipoAlerta() ,
                    resultJSONMarker.getFecha(),
                    resultJSONMarker.getMessage(),
                    resultJSONMarker.getTitulo(),
                    true ,
                    Integer.valueOf(getResources().getString(R.string.imageAlto)),
                    Integer.valueOf(getResources().getString(R.string.imageAncho)),
                    getThis()));
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Ocurrió un error inesperado. Intente nuevamente mas tarde", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void processFinish(String result) {
        this.markers = JSONParse.ParseJSONMarker(result);
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    for(ResultJSONMarker resultJSONMarker : markers){
                        try {
                            mMap.addMarker(GoogleMapUtil.buildMarkerOptions(resultJSONMarker.getLatitud(),
                                    resultJSONMarker.getLongitud(), resultJSONMarker.getTipoAlerta() ,
                                    resultJSONMarker.getFecha(),
                                    resultJSONMarker.getMessage(),
                                    resultJSONMarker.getTitulo(),
                                    true ,
                                    Integer.valueOf(getResources().getString(R.string.imageAlto)),
                                    Integer.valueOf(getResources().getString(R.string.imageAncho)),
                                    getThis()));
                        }catch (Exception e){
                            String a = e.getMessage();
                        }
                    }
                    /*if(EstadosMarkers.ESTADO.LOCAL.equals(estadoMarkers)){
                        estadoMarkers = EstadosMarkers.ESTADO.PROVINCIAL;
                        loadMarkers("getRestsMarkers");

                    }
                    else if(EstadosMarkers.ESTADO.PROVINCIAL.equals(estadoMarkers)){
                        estadoMarkers = EstadosMarkers.ESTADO.NACIONAL;
                        loadMarkers("getRestsProMarkers");
                    }*/
                }catch (Exception e){
                    String a = e.getMessage();
                }
            }
        });
    }
}