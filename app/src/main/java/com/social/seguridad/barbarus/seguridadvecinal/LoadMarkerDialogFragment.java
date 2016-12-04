package com.social.seguridad.barbarus.seguridadvecinal;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.social.seguridad.barbarus.SharedPreferences.Configuracion;
import com.social.seguridad.barbarus.action.Action;
import com.social.seguridad.barbarus.action.MarcadorAction;

import java.util.List;

/**
 * Created by braian on 02/12/2016.
 */
public class LoadMarkerDialogFragment extends DialogFragment  implements  Validator.ValidationListener{

    //inputs
    @NotEmpty(message = "Complete la descripción" )
    @Length(min = 30, message = "Debe contener como mínimo 30 caracteres")
    EditText descripcion;

    //validador
    Validator validator;

    //PARA GUARDAR CONFIGURACIONES
    private Configuracion conf;

    private double latitud;
    private double longitud;

    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().setTitle("Agregar marcador");
        conf = new Configuracion(getActivity());

        this.latitud = getArguments().getDouble("latitude");
        this.longitud = getArguments().getDouble("longitude");

        descripcion = (EditText)rootView.findViewById(R.id.descripcionText);
        validator = new Validator(this);
        validator.setValidationListener(this);

        this.spinner = (Spinner) rootView.findViewById(R.id.delitosSpinner);

        Button cancelarButton = (Button) rootView.findViewById(R.id.cancelarButton);
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button agregarButton = (Button) rootView.findViewById(R.id.agregarButton);
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        return rootView;
    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onValidationSucceeded() {
        if(esValidoParaEnviarAlerta()){
            MarcadorAction marcadorAction = new MarcadorAction(getActivity());
            marcadorAction.enviar(conf.getUserEmail(), conf.getToken() , descripcion.getText().toString() ,
                        this.spinner.getSelectedItem().toString(),
                        String.valueOf(this.latitud), String.valueOf(this.longitud));
            dismiss();
        }
    }


    private boolean esValidoParaEnviarAlerta(){
        if(conf.getUserEmail() != null
                && conf.getToken() != null){
            return true;
        }

        Toast.makeText(getActivity(),
                "Por el momento usted no puede agregar marcadores",
                Toast.LENGTH_LONG).show();
        return false;
    }


}
