package com.example.felipe.medidorcorrente.handlers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.activities.SensorActivity;
import com.example.felipe.medidorcorrente.database.SensorDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by desenvolvedor2 on 21/09/16.
 */

public class BackGroundTask {

    SensorDAO dao;

    public void getData(final Context context){

        dao = new SensorDAO();
        final Activity activity = (Activity) context;

        class HttpAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                //boolean whileTest = false;
                Session.setScanRunning(false);
                ArrayList<Double> list;

                    final ArrayList<String> aux = onJsonResponse(InternetConnectionHandler.getRequest(urls[0], context));
                    if(aux!=null){
                        dao = new SensorDAO();
                        for (String s:aux) {
                            String[] parts = s.split(":");
                            Session.setScanRunning(dao.insertSensor(parts[0], Double.parseDouble(parts[1]), context));
                        }

                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Sem dados! desconectando...", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                return "";
            }

            public String onJsonResponseOld(String response){
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject data = json.getJSONObject("sensor 1");
                    String value = data.getString("corrente");
                    Log.d("testeValue",value);
                    return value;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "";
            }

            private ArrayList<String> onJsonResponse(String response){
                Log.d("json",response);
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray jarray = json.getJSONArray("sensores");
                    ArrayList<String> value = new ArrayList<>();
                    for(int i=0;i<jarray.length();i++){
                        String sensorValue = jarray.getString(i);
                        value.add("Sensor "+(i+1)+":"+sensorValue);
                    }
                    return value;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }
        new HttpAsyncTask().execute("http://"+Session.getIpAddress()+"/corrente");
    }

    public void testGetData(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                    getData(context);

                    try {
                        Thread.sleep(1000*60*2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (Session.isScanRunning());
            }
        }).start();
    }

    public static void SetON(final Context context, String name){
        name = name.replaceAll("\\s+","");
        Log.d("TesteNome",name);
        class SensorOn extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                Log.d("testeCall","OK");
                return InternetConnectionHandler.getRequest(urls[0],context);
            }
            @Override
            protected void onPostExecute(String response){
                Log.d("ResponseTest",response);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(Session.getIpAddress()).append("/");
        builder.append(name);
        builder.append("On");
        String url = builder.toString();
        new SensorOn().execute(url);
    }

    public static void SetOFF(final Context context, String name){
        name = name.replaceAll("\\s+","");
        Log.d("TesteNome",name);
        class SensorOff extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                Log.d("testeCall","OK");
                return InternetConnectionHandler.getRequest(urls[0],context);
            }
            @Override
            protected void onPostExecute(String response){
                Log.d("ResponseTest",response);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(Session.getIpAddress()).append("/");
        builder.append(name);
        builder.append("Off");
        String url = builder.toString();
        new SensorOff().execute(url);
    }



}
