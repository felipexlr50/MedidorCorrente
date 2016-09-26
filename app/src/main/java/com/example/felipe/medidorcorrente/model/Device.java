package com.example.felipe.medidorcorrente.model;

import java.util.ArrayList;

public class Device {

    private String nome;

    private ArrayList<String> values;

    private ArrayList<String> dates;

    public Device(String nome) {

        this.nome = nome;
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
