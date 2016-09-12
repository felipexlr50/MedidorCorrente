package com.example.felipe.medidorcorrente.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.handlers.InternetConnectionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SensorActivity extends Activity{

    private Button btLedOn;
    private Button btLedOff;
    private EditText ipURL;
    private TextView resultado;
    private SensorDAO dao;
    private double valor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLedOn = (Button) findViewById(R.id.buttonOn);
        resultado = (TextView) findViewById(R.id.textView);
        btLedOff = (Button) findViewById(R.id.buttonOff);
        ipURL = (EditText) findViewById(R.id.editText);


        btLedOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // new HttpAsyncTask().execute("http://"+ipURL.getText().toString()+"/corrente");
            }
        });

        btLedOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // new HttpAsyncTask().execute("http://"+ipURL.getText().toString()+"/ledOff");
            }
        });

        new HttpAsyncTask().execute("http://192.168.1.107/corrente");

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            boolean whileTest = false;
            ArrayList<Double> list;
            do {
            final String aux = onJsonResponse(InternetConnectionHandler.getRequest(urls[0], SensorActivity.this));
            valor = Double.valueOf(aux);
            dao = new SensorDAO();
            whileTest = dao.insertSensor("Sensor 1", valor, SensorActivity.this);
            list = dao.getSensorValue("Sensor 1",SensorActivity.this);
                final ArrayList<Double> finalList = list;
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
                    resultado.setText(finalList.toString());
                }
            });
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(whileTest);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

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
