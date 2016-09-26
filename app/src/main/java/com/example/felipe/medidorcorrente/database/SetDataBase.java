package com.example.felipe.medidorcorrente.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SetDataBase extends SQLiteOpenHelper{
    public static final String TBL = "sensor";
    public static final String COD = "cod_pos";
    public static final String NOME = "nome";
    public static final String VALOR = "valor";
    public static final String DATA_IN = "data_inserida";


    private static final String DATABASE_NAME = "Corrente.db";
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_TABLE = "CREATE TABLE "+TBL+"(\n" +
            COD+" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            NOME+" TEXT,\n" +
            VALOR+" REAL,\n" +
            DATA_IN+" DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
            ")";

    public SetDataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL);
        onCreate(db);
    }
}
