package com.example.felipe.medidorcorrente.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.handlers.BackGroundTask;
import com.example.felipe.medidorcorrente.handlers.Session;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;

public class DeviceActivity extends AppCompatActivity {

    private LineChart lineChart;
    private BackGroundTask backGroundTask;
    private SensorDAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dao = new SensorDAO();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.getSensorValue(Session.getSelectedDevice().getNome(),DeviceActivity.this);
                setGraph();
            }
        });


        Button btnOn = (Button) findViewById(R.id.btnOn);
        assert btnOn != null;
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   setOn();
            }
        });

        Button btnOff = (Button) findViewById(R.id.btnOff);
        assert btnOff != null;
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOff();
            }
        });
        TextView title = (TextView) findViewById(R.id.txtDeviceNome);
        assert title != null;
        title.setText(Session.getSelectedDevice().getNome());
        lineChart = (LineChart) findViewById(R.id.chart);

        TextView txtConsumo = (TextView) findViewById(R.id.txtConsumo);
        String aux = txtConsumo != null ? txtConsumo.getText().toString() : "";
        aux+= dao.getConsumeSum(this,Session.getSelectedDevice().getNome());
        txtConsumo.setText(aux);

        setGraph();
        backGroundTask=new BackGroundTask();
    }

    private void setGraph(){

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = Session.getSelectedDevice().getDates();
        int i =0;
        for(String s:Session.getSelectedDevice().getValues()){
            entries.add(new Entry((float) Double.parseDouble(s), i));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Consomo em kWh");
        LineData data = new LineData(labels, dataSet);

        dataSet.setDrawCubic(true);
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSet.setDrawCircles(false);

        lineChart.setData(data);
        lineChart.animateY(3000);
        lineChart.invalidate();

    }

    public void setOn(){
        BackGroundTask.SetON(this,Session.getSelectedDevice().getNome());

    }

    public void setOff(){
        BackGroundTask.SetOFF(this,Session.getSelectedDevice().getNome());

    }


}
