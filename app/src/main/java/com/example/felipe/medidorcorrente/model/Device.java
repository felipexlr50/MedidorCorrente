package com.example.felipe.medidorcorrente.model;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class Device {

    private ArrayList<Entry> entries;

    private String nome;

    private ArrayList<String> labels;

    private LineDataSet lineDataSet;

    private LineData lineData;

    public Device(ArrayList<Entry> entries, String nome, ArrayList<String> labels) {
        this.entries = entries;
        this.nome = nome;
        this.labels = labels;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public LineDataSet getLineDataSet() {
        return lineDataSet;
    }

    public void setLineDataSet(LineDataSet lineDataSet) {
        this.lineDataSet = lineDataSet;
    }

    public LineData getLineData() {
        return lineData;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }
}
