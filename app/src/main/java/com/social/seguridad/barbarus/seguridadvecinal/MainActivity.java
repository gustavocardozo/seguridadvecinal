package com.social.seguridad.barbarus.seguridadvecinal;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.action.PoliceAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    private static final int TIME_RUNTIME = 100;

    private GoogleMap mMap;

    //PARA GUARDAR CONFIGURACIONES
    //EJEMPLO EMAIL DEL LOGUEADO
    private Configuracion conf;

    private FloatingActionsMenu mFAB;
    private DonutProgress donutProgress;
    private GoogleApiClient client;
    private Map<String, Timer> buttonsThreads = new HashMap<String, Timer>();


    private Location mLastLocation;
    public LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CONF
        conf = new Configuracion(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFAB = (FloatingActionsMenu) findViewById(R.id.fab);
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // GPS
        //Se inicia el servicio de GPS
        Intent servIntent = new Intent( getApplicationContext() , LocalizacionService.class);
        startService(servIntent);

        buildButton();


        Toast.makeText(this, conf.getToken() != null ? conf.getToken() : "no token" , Toast.LENGTH_SHORT).show();
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
        }, 500, 50);
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
     */
    private void buildButton() {
        buttonsThreads.put("accion_policeman", null);
        buttonsThreads.put("accion_doctor", null);
        buttonsThreads.put("accion_firefighter", null);

        //Policia
        findViewById(R.id.accion_policeman).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                                policeAction.send(conf.getUserEmail(), conf.getToken(),
                                        conf.getLastKnowAddresses(), String.valueOf(conf.getLastKnowtLatitud()),
                                                            String.valueOf(conf.getLastKnowLongitud()));
                            }catch (Exception e){
                                Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        restarProgressBar();
                        break;
                }
                return false;
            }
        });

        //Doctor
        findViewById(R.id.accion_doctor).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        buildTimer("accion_doctor");
                        return true;
                    case MotionEvent.ACTION_UP:
                        removeTimer("accion_doctor");
                        if (donutProgress.getProgress() == 100) {
                            //envio notificacion
                            Toast.makeText(MainActivity.this, "Enviando notificacion", Toast.LENGTH_SHORT).show();
                        }
                        restarProgressBar();
                        break;
                }
                return false;
            }
        });

        //Bombero
        findViewById(R.id.accion_firefighter).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        buildTimer("accion_firefighter");
                        return true;
                    case MotionEvent.ACTION_UP:
                        removeTimer("accion_firefighter");
                        if (donutProgress.getProgress() == 100) {
                            //envio notificacion
                            Toast.makeText(MainActivity.this, "Enviando notificacion", Toast.LENGTH_SHORT).show();
                        }
                        restarProgressBar();
                        break;
                }
                return false;
            }
        });
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ClipData clip = ClipData.newPlainText("text", conf.getToken());
            ClipboardManager clipboard = (ClipboardManager)this.getSystemService(CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clip);
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, 0);
        }
        if (id == R.id.action_cerrar_sesion) {
            conf.deleteUserEmail();
            Intent intent = new Intent(MainActivity.this, PresentationActivity.class);
            startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
//            mMap.setMyLocationEnabled(true);
        }

        //Location location = mMap.getMyLocation();

        // Add a marker in Sydney and move the camera
        //ambulancia
        LatLng alert1 = new LatLng(-34.533849 ,  -58.788681);
        //policia
        LatLng alert2 = new LatLng(-34.5331491 ,   -58.7905443);
        //bombero
        LatLng alert3 = new LatLng(-34.5330151 ,   -58.7918482);

        mMap.addMarker(new MarkerOptions()
                .position(alert1)
                .title("Alerta ambulancia")
                .snippet("Fecha 12/02/2016 a las 08:00 hs")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.am)));

        mMap.addMarker(new MarkerOptions()
                .position(alert2)
                .title("Alerta policia")
                .snippet("Fecha 12/02/2016 a las 21:00 hs")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pol)));


        mMap.addMarker(new MarkerOptions()
                .position(alert3)
                .title("Alerta bomberos")
                .snippet("Fecha 15/02/2016 a las 22:00 hs")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bom)));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alert1, 14));
    }
}