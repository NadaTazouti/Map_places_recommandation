package com.example.map1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;


import java.util.ArrayList;

public class DB_Sqlit extends SQLiteOpenHelper {

    public static final String DBname = "data.db";


    public DB_Sqlit(Context context) {

        super(context, DBname, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table Place (name TEXT, latitude TEXT, longitude TEXT) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS Place");
        //onCreate(db);

    }

    public boolean insertData(String name, String latitude, String longitude) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        db.execSQL("create table IF NOT EXISTS Place (name TEXT , latitude TEXT,  longitude TEXT) ");

        content.put("name", name);
        content.put("latitude", latitude);
        content.put("longitude", longitude);

        long result = db.insert("Place", null, content);

        if (result == -1) return false;

        return true;
    }

    public void drop() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("drop table IF EXISTS Place ");


    }


    public ArrayList<Place> getAll() {

        ArrayList<Place> tab = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from Place", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String name = res.getString(0);
            String lat = res.getString(1);
            String lon = res.getString(2);

            tab.add(new Place(name, lat, lon));

            res.moveToNext();
        }
        return tab;
    }
}
