package com.social.seguridad.barbarus.googleMapUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.social.seguridad.barbarus.action.AmbulanceAction;
import com.social.seguridad.barbarus.action.CriminalAction;
import com.social.seguridad.barbarus.action.FirefighterAction;
import com.social.seguridad.barbarus.action.PoliceAction;
import com.social.seguridad.barbarus.action.ViolenciaGeneroAction;

/**
 * Created by braian on 20/11/2016.
 */
public class GoogleMapUtil extends AppCompatActivity {

    public static  MarkerOptions buildMarkerOptions(Double latitud , Double longitud ,
                                            String tipoAlerta ,
                                            String fecha ,
                                            Boolean snippet,
                                            AppCompatActivity activity ){
        LatLng alert = new LatLng(latitud ,  longitud);

        if(PoliceAction.ALERTA.equals(tipoAlerta)){
            return new MarkerOptions()
                    .position(alert)
                    .title(PoliceAction.ALERTA_TITLE)
                    .snippet(snippet ? "Fecha " + fecha : "")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("police",40,50,activity)));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.police));
        }else if(AmbulanceAction.ALERTA.equals(tipoAlerta)){
            return new MarkerOptions()
                    .position(alert)
                    .title(AmbulanceAction.ALERTA_TITLE)
                    .snippet(snippet ? "Fecha " + fecha : "")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ambulance",40,50,activity)));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance));
        }else if(FirefighterAction.ALERTA.equals(tipoAlerta)){

            return new MarkerOptions()
                    .position(alert)
                    .title(FirefighterAction.ALERTA_TITLE)
                    .snippet(snippet ? "Fecha " + fecha : "")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("fire",40,50,activity)));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.fire));
        }else if (CriminalAction.ALERTA.equals(tipoAlerta)){
            return new MarkerOptions()
                    .position(alert)
                    .title(CriminalAction.ALERTA_TITLE)
                    .snippet(snippet ? "Fecha " + fecha : "")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("shooting",40,50,activity)));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.shooting));
        }else if (ViolenciaGeneroAction.ALERTA.equals(tipoAlerta)){
            return new MarkerOptions()
                    .position(alert)
                    .title(ViolenciaGeneroAction.ALERTA_TITLE)
                    .snippet(snippet ? "Fecha " + fecha : "")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("abduction",40,50,activity)));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.shooting));
        }

        return new MarkerOptions()
                .position(alert)
                .title("Alerta")
                .snippet(snippet ? "Fecha " + fecha : "")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pirates",40,50 , activity)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pirates));
    }

    private static Bitmap resizeMapIcons(String iconName, int width, int height ,AppCompatActivity activity){
        Bitmap imageBitmap = BitmapFactory.decodeResource(activity.getResources(),activity.getResources().getIdentifier(iconName, "drawable", activity.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}
