package com.example.felipe.medidorcorrente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        db.close();
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

    public ArrayList<Device> getSensorValue2(String nome, Context context){
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

    public void testTable(Context context){

        dataBase = new SetDataBase(context);
        db = dataBase.getWritableDatabase();
        Cursor cursor = dataBase.getReadableDatabase().rawQuery("Select DISTINCT "+SetDataBase.NOME+" from "+SetDataBase.TBL,null);
        String result="";
        if(cursor.moveToFirst()){
            do{
                result+=cursor.getString(cursor.getColumnIndex(SetDataBase.NOME));

            }while (cursor.moveToNext());
            Log.d("tabela",result);
        }
        db.close();
    }


}
