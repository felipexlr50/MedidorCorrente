package com.example.felipe.medidorcorrente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.adapters.DeviceListAdapter;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.handlers.CustomMessage;
import com.example.felipe.medidorcorrente.handlers.Session;
import com.example.felipe.medidorcorrente.model.Device;

import java.util.ArrayList;

public class SensorActivity extends AppCompatActivity {

    private SensorDAO dao;
    private ListView listView;
    private CustomMessage message;
    private DeviceListAdapter deviceListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefreshing;



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

        listView = (ListView) findViewById(R.id.listDevice);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        assert mSwipeRefreshLayout != null;

        message = new CustomMessage();
        message.setIpMessage(this);
        setSwipeListener();
        dao = new SensorDAO();
        getDeviceList();
    }

    private void getDeviceList(){
        dao = new SensorDAO();
        ArrayList<Device> devices;
        devices = dao.getSensorValue2("Sensor 1",SensorActivity.this);

        deviceListAdapter = new DeviceListAdapter(devices,SensorActivity.this);
        listView.setAdapter(deviceListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("selected","Selected: "+i);
                Device device = (Device) listView.getItemAtPosition(i);
                Session.setSelectedDevice(device);
                dao.getSensorValue(device.getNome(),SensorActivity.this);
                startActivity(new Intent(SensorActivity.this, DeviceActivity.class));
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
        deviceListAdapter.notifyDataSetChanged();
        if(isRefreshing){
            Toast.makeText(SensorActivity.this, "Lista atualizada!", Toast.LENGTH_SHORT).show();
        }
        isRefreshing = false;
    }

    private void setSwipeListener(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                getDeviceList();
            }
        });
    }



}
