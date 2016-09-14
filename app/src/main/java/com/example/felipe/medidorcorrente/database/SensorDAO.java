package com.example.felipe.medidorcorrente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.felipe.medidorcorrente.model.Device;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by Felipe on 14/08/2016.
 */
public class SensorDAO {
    private SetDataBase dataBase;
    private SQLiteDatabase db;

    public boolean insertSensor(String nome, double valor, Context context){
        long i = 0;
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(SetDataBase.NOME,nome);
        cv.put(SetDataBase.VALOR,valor);
        i = db.insert(SetDataBase.TBL,null,cv);
        if(i==-1) return false;
        else return true;
    }

    public ArrayList<Double> getSensorValue(String nome,Context context){
        ArrayList<Double> list = new ArrayList<>();
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("SELECT "+SetDataBase.VALOR+" FROM "+SetDataBase.TBL
                +" WHERE "+SetDataBase.NOME+" = '"+nome+"'"
                +" ORDER BY "+SetDataBase.DATA_IN+" ASC",null);

        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getDouble(cursor.getColumnIndex(SetDataBase.VALOR)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public Device getSensorValue2(String nome, Context context){
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("SELECT "+SetDataBase.VALOR+" FROM "+SetDataBase.TBL
                +" WHERE "+SetDataBase.NOME+" = '"+nome+"'"
                +" ORDER BY "+SetDataBase.DATA_IN+" ASC",null);

        if(cursor.moveToFirst()){
            do{
                entries.add(new Entry((float) cursor.getDouble(cursor.getColumnIndex(SetDataBase.VALOR)),cursor.getPosition()));
                labels.add(cursor.getString(cursor.getColumnIndex(SetDataBase.DATA_IN)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return new Device(entries,"Aparelho 1", labels);
    }


}
