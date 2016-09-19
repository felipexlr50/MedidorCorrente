package com.example.felipe.medidorcorrente.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.adapters.DeviceListAdapter;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.handlers.InternetConnectionHandler;
import com.example.felipe.medidorcorrente.handlers.Session;
import com.example.felipe.medidorcorrente.model.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SensorActivity extends Activity{

    private SensorDAO dao;
    private double valor;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        new DataFromDB().execute();

        dao = new SensorDAO();
        dao.testTable(this);
        //new HttpAsyncTask().execute("http://192.168.1.107/corrente");

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //boolean whileTest = false;
            Session.setScanRunning(false);
            ArrayList<Double> list;
            do {
            final String aux = onJsonResponse(InternetConnectionHandler.getRequest(urls[0], SensorActivity.this));
            valor = Double.valueOf(aux);
            dao = new SensorDAO();
            Session.setScanRunning(dao.insertSensor("Sensor 1", valor, SensorActivity.this));
            list = dao.getSensorValue("Sensor 1",SensorActivity.this);

                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

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
    }

    private class DataFromDB extends AsyncTask<String, Void, ArrayList<Device>>{


        @Override
        protected ArrayList<Device> doInBackground(String... params) {

            dao = new SensorDAO();

            return dao.getSensorValue2("Sensor 1",SensorActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<Device> devices){
            DeviceListAdapter deviceListAdapter = new DeviceListAdapter(devices,SensorActivity.this);
            listView.setAdapter(deviceListAdapter);
            deviceListAdapter.notifyDataSetChanged();
        }
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
