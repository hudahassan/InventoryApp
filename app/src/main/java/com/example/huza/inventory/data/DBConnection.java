package com.example.huza.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.example.huza.inventory.data.StoreContract.stockEntry;
/**
 * Created by huza on 4/17/2019.
 */

public class DBConnection extends SQLiteOpenHelper{



    public static final int DB_Version=1;
    public static final String DB_NAME="product.db";

    public DBConnection(Context context) {
        super(context, DB_NAME, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TALE= "CREATE TABLE "+stockEntry.TABLE_NAME+ " ("
                +stockEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +stockEntry.COLUMN_PRODUCT_NAME+" TEXT NOT NULL, "
                +stockEntry.COLUMN_SUPPLIER_NAME+" TEXT NOT NULL, "
                +stockEntry.COLUMN_SUPPLIER_PHONE+" TEXT NOT NULL, "
                +stockEntry.COLUMN_QUANTITY+" INTEGER NOT NULL, "
                +stockEntry.COLUMN_PRICE+" INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_PETS_TALE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(stockEntry.DELETE);
        onCreate(db);
    }
}


