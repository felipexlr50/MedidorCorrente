package com.example.felipe.medidorcorrente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.felipe.medidorcorrente.handlers.Session;
import com.example.felipe.medidorcorrente.model.Device;
import com.github.mikephil.charting.data.Entry;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Felipe on 14/08/2016.
 */
public class SensorDAO {
    private SetDataBase dataBase;
    private SQLiteDatabase db;

    public boolean insertSensor(String nome, double valor, Context context){
        long i = 0;
        double amper = (40.0/1000.0);
        double time = 1.0/3600.0;
        double consumo = ((amper* valor)/1000)*time;
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(SetDataBase.NOME,nome);
        cv.put(SetDataBase.VALOR,consumo);
        i = db.insert(SetDataBase.TBL,null,cv);
        db.close();
        return i != -1;
    }

    public ArrayList<String> getSensorValue(String nome,Context context){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> datesList = new ArrayList<>();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMM/dd HH:mm");
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("SELECT "+SetDataBase.VALOR+", "+SetDataBase.DATA_IN+" FROM "+SetDataBase.TBL
                +" WHERE "+SetDataBase.NOME+" = '"+nome+"'"
                +" ORDER BY "+SetDataBase.DATA_IN+" ASC",null);

        DecimalFormat format = new DecimalFormat("0.0E0");
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getDouble(cursor.getColumnIndex(SetDataBase.VALOR))+"");
                try {
                    datesList.add(format2.format(format1.parse(cursor.getString(cursor.getColumnIndex(SetDataBase.DATA_IN)))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());
        }
        Session.getSelectedDevice().setValues(list);
        Session.getSelectedDevice().setDates(datesList);
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<Device> getSensorValue2(Context context){
        Log.d("teste","inicio");
        ArrayList<Device> devices = new ArrayList<>();
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("SELECT DISTINCT "+SetDataBase.NOME+" FROM "+SetDataBase.TBL
                +" ORDER BY "+SetDataBase.DATA_IN+" ASC",null);

        if(cursor.moveToFirst()){
            do{
                String nomeDevice = cursor.getString(cursor.getColumnIndex(SetDataBase.NOME));
                devices.add(new Device(nomeDevice));
                Log.d("teste",nomeDevice);
            }while(cursor.moveToNext());
        }
        Log.d("teste","final");
        cursor.close();
        db.close();

        return devices;
    }

    public ArrayList<Device> getSensorValueOrderBy(Context context){
        Log.d("teste","inicio");
        ArrayList<Device> devices = new ArrayList<>();
        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("SELECT "+SetDataBase.NOME+", SUM("+SetDataBase
                .VALOR+") as 'soma'  FROM "+SetDataBase.TBL
                +" GROUP BY "+SetDataBase.NOME+"  ORDER BY SUM("+SetDataBase.VALOR+") ASC",null);

        if(cursor.moveToFirst()){
            do{
                String nomeDevice = cursor.getString(cursor.getColumnIndex(SetDataBase.NOME));
                float valueSum = cursor.getFloat(cursor.getColumnIndex("soma"));
                Log.d("SUM",valueSum+"");
                devices.add(new Device(nomeDevice,valueSum));
                Log.d("teste",nomeDevice);
            }while(cursor.moveToNext());
        }
        Log.d("teste","final");
        cursor.close();
        db.close();

        return devices;
    }



    public String getConsumeSum(Context context, String nome){

        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("SELECT SUM("+SetDataBase.VALOR+") AS 'SOMA' FROM "+SetDataBase.TBL
                +" WHERE "+SetDataBase.NOME+" = '"+nome+"'",null);
        String result = "";
        if(cursor.moveToFirst()){
            do{
                result=cursor.getString(cursor.getColumnIndex("SOMA"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

}
