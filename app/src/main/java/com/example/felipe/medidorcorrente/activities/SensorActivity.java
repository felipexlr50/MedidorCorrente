package com.example.felipe.medidorcorrente.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.adapters.DeviceListAdapter;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.handlers.CustomMessage;
import com.example.felipe.medidorcorrente.handlers.InternetConnectionHandler;
import com.example.felipe.medidorcorrente.handlers.Session;
import com.example.felipe.medidorcorrente.model.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SensorActivity extends AppCompatActivity {

    private SensorDAO dao;
    private double valor;
    private ListView listView;
    private CustomMessage message;
    private DeviceListAdapter deviceListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefreshing;
    private View.OnClickListener mOnClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.testTable(SensorActivity.this);
            }
        });
        listView = (ListView) findViewById(R.id.list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        assert mSwipeRefreshLayout != null;
        message = new CustomMessage();
        message.setIpMessage(this);
        setSwipeListener();
        dao = new SensorDAO();
        getDBData();
    }


    private class DataFromDB extends AsyncTask<String, Void, ArrayList<Device>>{


        @Override
        protected ArrayList<Device> doInBackground(String... params) {

            dao = new SensorDAO();

            return dao.getSensorValue2("Sensor 1",SensorActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<Device> devices){
            deviceListAdapter = new DeviceListAdapter(devices,SensorActivity.this);
            listView.setAdapter(deviceListAdapter);

            mSwipeRefreshLayout.setRefreshing(false);

            if(isRefreshing){
                deviceListAdapter.notifyDataSetChanged();
                Toast.makeText(SensorActivity.this, "Lista atualizada!", Toast.LENGTH_SHORT).show();
            }
            isRefreshing = false;
        }
    }

    private void setSwipeListener(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                getDBData();
            }
        });
    }

    private void getDBData(){
        deviceListAdapter = new DeviceListAdapter(dao.getSensorValue2("Sensor 1",SensorActivity.this),SensorActivity.this);
        listView.setAdapter(deviceListAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
