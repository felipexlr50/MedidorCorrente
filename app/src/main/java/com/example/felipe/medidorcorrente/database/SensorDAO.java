package com.example.felipe.medidorcorrente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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


}
