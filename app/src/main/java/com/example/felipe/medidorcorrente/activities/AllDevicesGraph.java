package com.example.felipe.medidorcorrente.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.database.SensorDAO;
import com.example.felipe.medidorcorrente.model.Device;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AllDevicesGraph extends AppCompatActivity {

    private HorizontalBarChart barChart;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_devices_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarData data = new BarData(getXAxisValues(), getDataSet());
                barChart.setData(data);
                barChart.invalidate();
                barChart.animateXY(2000, 2500);
            }
        });

        barChart = (HorizontalBarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        barChart.setData(data);
        barChart.setDescription("Comparação entre dispositivos em kWh");
        barChart.animateXY(2000, 2500);
        barChart.invalidate();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<Device> devices;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        SensorDAO dao = new SensorDAO();
        devices = dao.getSensorValueOrderBy(this);
        for(int i = 0; i<devices.size();i++){
            valueSet1.add(new BarEntry(devices.get(i).getValueSum(),i));
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Dispositivos");
        barDataSet1.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        barDataSet1.setDrawValues(false);


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<Device> devices;
        SensorDAO dao = new SensorDAO();
        devices = dao.getSensorValueOrderBy(this);
        ArrayList<String> xAxis = new ArrayList<>();
        for(Device d:devices){
            xAxis.add(d.getNome());
        }
        return xAxis;
    }

}
