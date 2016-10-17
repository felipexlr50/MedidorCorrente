package com.example.felipe.medidorcorrente.model;

import java.util.ArrayList;

public class Device {

    private String nome;

    private float valueSum;

    private ArrayList<String> values;

    private ArrayList<String> dates;



    public Device(String nome) {

        this.nome = nome;
    }

    public Device(String nome, float valueSum) {
        this.nome = nome;
        this.valueSum = valueSum;
    }





    public float getValueSum() {
        return valueSum;
    }

    public void setValueSum(float valueSum) {
        this.valueSum = valueSum;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }
}
