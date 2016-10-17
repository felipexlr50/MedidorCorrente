package com.example.felipe.medidorcorrente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.adapters.DeviceListAdapter;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.handlers.BackGroundTask;
import com.example.felipe.medidorcorrente.handlers.CustomMessage;
import com.example.felipe.medidorcorrente.handlers.Session;
import com.example.felipe.medidorcorrente.model.Device;

import java.text.DecimalFormat;
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
                startActivity(new Intent(SensorActivity.this,AllDevicesGraph.class));
            }
        });

        listView = (ListView) findViewById(R.id.listDevice);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        assert mSwipeRefreshLayout != null;

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        message = new CustomMessage();
        message.setIpMessage(this);
        setSwipeListener();
        dao = new SensorDAO();
        getDeviceList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_sync){
            if(Session.isScanRunning()){
                Toast.makeText(SensorActivity.this, "Ja esta pegando dados!", Toast.LENGTH_SHORT).show();
            }
            else{
                BackGroundTask backGroundTask = new BackGroundTask();
                Toast.makeText(SensorActivity.this, "Pegando dados...", Toast.LENGTH_SHORT).show();
                backGroundTask.testGetData(SensorActivity.this);
            }
            return true;
        }

        if(id == R.id.action_stop_sync){
            if(Session.isScanRunning()){
                Session.setScanRunning(false);
            }
            else {
                Toast.makeText(this, "Já esta desconectado", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if(id == R.id.action_resetIP){
            message = new CustomMessage();
            message.setIpMessage(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getDeviceList(){
        dao = new SensorDAO();
        ArrayList<Device> devices;
        devices = dao.getSensorValue2(SensorActivity.this);

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
