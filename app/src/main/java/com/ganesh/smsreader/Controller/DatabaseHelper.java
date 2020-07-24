package com.ganesh.smsreader.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME_TRANSACTION = "TRANSACTION_DETAILS";
    private static final String COL_1_TRANSACTION = "TRANSACTION_ID";
    private static final String COL_2_TRANSACTION = "TAG";

    private static final String DB_NAME = "SMS_DB";


    private SQLiteDatabase database;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
        String createTable = "CREATE TABLE " + TABLE_NAME_TRANSACTION + "(" + COL_1_TRANSACTION + " INTEGER PRIMARY KEY UNIQUE," + COL_2_TRANSACTION + " TEXT)";
        Log.d("db: createTable", createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANSACTION);
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addDataToTransaction(int id, String tag) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_TRANSACTION, id);
        contentValues.put(COL_2_TRANSACTION, tag);
        Log.d("DB :Adding," + id+" "+ tag + " to", TABLE_NAME_TRANSACTION);
        long result = database.insert(TABLE_NAME_TRANSACTION, null, contentValues);
        return result != -1;
    }

    public String getTagById(int id) {
        database = this.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME_TRANSACTION + " WHERE " + COL_1_TRANSACTION + " = " + id ;
        Cursor c = getReadableDatabase().rawQuery(selectString, new String[]{});
        String tags = null;
        if (c.moveToFirst()) {
            do {
                 tags = c.getString(1);
            } while (c.moveToNext());
        }
        c.close();
        return tags;
    }

    public void updateTagById(int id, String tag) {
        database = this.getWritableDatabase();
        String updateString = "UPDATE " + TABLE_NAME_TRANSACTION + " SET " + COL_2_TRANSACTION + "=" + tag + " WHERE ID =" + id;
        database.execSQL(updateString);
        Log.d("db: updateString", updateString);
    }




}
