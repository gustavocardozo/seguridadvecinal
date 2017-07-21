package com.social.seguridad.barbarus.webservice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Esta clase se encarga de hacer el llamado al servicio web y retornar la informacion
 *
 */
public class WebService extends AsyncTask<String, Long, String> {


	public enum TYPE{
		POST , PUT , GET
	}

	//Variable con los datos para pasar al web service
	private Map<String, String> datos;
	//Url del servicio web
	private String url= "http://localhost:3000";
	
	//Actividad para mostrar el cuadro de progreso
	private Context actividad;
	
	//Resultado
	private String xml=null;
	
	//Clase a la cual se le retorna los datos dle ws
	private Asynchtask callback=null;

	private TYPE type;

	public Asynchtask getCallback() {
		return callback;
	}
	public void setCallback(Asynchtask callback) {
		this.callback = callback;
	}

	ProgressDialog progDailog;


	private boolean isMessage;

	/**
	 * Crea una estancia de la clase webService para hacer consultas a ws
	 * @param urlWebService Url del servicio web
	 * @param data Datos a enviar del servicios web
	 * @param activity Actividad de donde se llama el servicio web, para mostrar el cuadro de "Cargando"
	 * @param callback CLase a la que se le retornara los datos del servicio web
	 */
	public  WebService(String urlWebService,Map<String, String> data, Context activity, Asynchtask callback , TYPE type , boolean isMessage) {
		this.url=urlWebService;
		this.datos=data;
		this.actividad=activity;
		this.callback=callback;
		this.type=type;
		this.isMessage = isMessage;
	}

	public WebService() {
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
		if(this.isMessage) {
			progDailog = new ProgressDialog(this.actividad);
			progDailog.setMessage("Cargando...");
			progDailog.setIndeterminate(false);
			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDailog.setCancelable(false);
			progDailog.show();
		}
    }
	@Override
	protected String doInBackground(String... params) {
		try {
			String result = "";
			switch (this.type){
				case POST :
					result = HttpRequest.post(this.url).form(this.datos).body();
					break;
				case GET:
					result = HttpRequest.get(this.url).body();
					break;
				case PUT:
					Log.e("PUT ", "No implementado");
					break;
			}
			return result;
		} catch (HttpRequest.HttpRequestException exception) {
			Log.e("doInBackground", exception.getMessage());
			return "Error HttpRequestException";
		} catch (Exception e) {
			Log.e("doInBackground", e.getMessage());
			return "Error Exception";
		}
	}
	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);
        this.xml=response;
		if(isMessage){
			progDailog.dismiss();
		}
        //Retorno los datos
        callback.processFinish(this.xml);
    }
	public Map<String, String> getDatos() {
		return datos;
	}

	public void setDatos(Map<String, String> datos) {
		this.datos = datos;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Context getActividad() {
		return actividad;
	}

	public void setActividad(Context actividad) {
		this.actividad = actividad;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public ProgressDialog getProgDailog() {
		return progDailog;
	}

	public void setProgDailog(ProgressDialog progDailog) {
		this.progDailog = progDailog;
	}
}
