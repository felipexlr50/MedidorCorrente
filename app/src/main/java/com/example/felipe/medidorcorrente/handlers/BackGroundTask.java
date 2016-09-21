package com.example.felipe.medidorcorrente.handlers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.activities.SensorActivity;
import com.example.felipe.medidorcorrente.database.SensorDAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by desenvolvedor2 on 21/09/16.
 */

public class BackGroundTask {

    SensorDAO dao;

    public void GetData(final Context context){

        dao = new SensorDAO();
        final Activity activity = (Activity) context;

        class HttpAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                //boolean whileTest = false;
                Session.setScanRunning(false);
                ArrayList<Double> list;
                do {
                    final String aux = onJsonResponse(InternetConnectionHandler.getRequest(urls[0], context));
                    double valor = Double.valueOf(aux);
                    dao = new SensorDAO();
                    Session.setScanRunning(dao.insertSensor("Sensor 1", valor, context));
                    list = dao.getSensorValue("Sensor 1",context);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Received!", Toast.LENGTH_LONG).show();

                        }
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(Session.isScanRunning());
                return "";
            }

            public String onJsonResponse(String response){
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

        }
        new HttpAsyncTask().execute("http://"+Session.getIpAddress()+"/corrente");
    }




}
