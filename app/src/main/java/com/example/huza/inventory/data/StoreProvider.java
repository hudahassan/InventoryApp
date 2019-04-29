package com.example.huza.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.huza.inventory.data.StoreContract.stockEntry;
/**
 * Created by huza on 4/17/2019.
 */

public class StoreProvider extends ContentProvider {
    public static final String LOG_TAG=ContentProvider.class.getSimpleName();

    public static final int INVENTORY=100;
    public static final int INVENTORY_ID=101;

    static UriMatcher suriMatcher ;
    static {
        suriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        suriMatcher.addURI(StoreContract.AUTHORITY_NAME,StoreContract.PATH_inventory,INVENTORY);
        suriMatcher.addURI(StoreContract.AUTHORITY_NAME,StoreContract.PATH_inventory+"/#",INVENTORY_ID);

    }

    DBConnection dbConnection;
    @Override
    public boolean onCreate() {
        dbConnection=new DBConnection(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        dbConnection=new DBConnection(getContext());
        SQLiteDatabase database=dbConnection.getWritableDatabase();
        int match = suriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case INVENTORY:
                cursor= database.query(stockEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case INVENTORY_ID:
                selection=stockEntry._ID+ "?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor= database.query(stockEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match=suriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return stockEntry.CONETNT_LIST_TYPE ;

            case INVENTORY_ID:
                return stockEntry.CONETNT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match=suriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return INsert_data(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }}

    //insert
    public Uri INsert_data(Uri uri,ContentValues contentValues){
        String product_name=contentValues.getAsString(stockEntry.COLUMN_PRODUCT_NAME);
        if(product_name==null){
            throw new IllegalArgumentException("Insertion require product's name" );
        }
        String supplier_name=contentValues.getAsString(stockEntry.COLUMN_SUPPLIER_NAME);
        if(supplier_name==null){
            throw new IllegalArgumentException("Insertion require supplier's name" );
        }
        String supplier_phone=contentValues.getAsString(stockEntry.COLUMN_SUPPLIER_PHONE);
        if(supplier_phone==null){
            throw new IllegalArgumentException("Insertion require supplier's phone" );
        }
        Integer price=contentValues.getAsInteger(stockEntry.COLUMN_PRICE);
        if(price==null){
            throw new IllegalArgumentException("Insertion require price" );
        }
        Integer quantity=contentValues.getAsInteger(stockEntry.COLUMN_QUANTITY);
        if(quantity==null || quantity<0){
            throw new IllegalArgumentException("Insertion require quantity" );
        }
        SQLiteDatabase database=dbConnection.getWritableDatabase();
        long id=database.insert(stockEntry.TABLE_NAME,null,contentValues);

        if(id==-1){
            Log.e(LOG_TAG,"failed to insert"+uri);
            return null;
        }
        Toast.makeText(getContext(), "successful insert", Toast.LENGTH_LONG).show();
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        dbConnection=new DBConnection(getContext());
        SQLiteDatabase database = dbConnection.getWritableDatabase();

        int rowsDeleted;

        final int match = suriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                rowsDeleted = database.delete(stockEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                selection = stockEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(stockEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;


    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match=suriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return updatestock(uri,values,selection,selectionArgs);

            case INVENTORY_ID:
                selection=stockEntry._ID+ "=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatestock(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }


    }
    public int updatestock(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs){
        if(contentValues.containsKey(stockEntry.COLUMN_PRODUCT_NAME)){
            String product_name=contentValues.getAsString(stockEntry.COLUMN_PRODUCT_NAME) ;
            if (product_name == null) {
                throw new IllegalArgumentException("require name");
            }
        }
        if(contentValues.containsKey(stockEntry.COLUMN_SUPPLIER_NAME)){
            String supplier_name=contentValues.getAsString(stockEntry.COLUMN_SUPPLIER_NAME);
            if(supplier_name==null) {
                throw new IllegalArgumentException("require supplier_name");
            }}

        if(contentValues.containsKey(stockEntry.COLUMN_SUPPLIER_PHONE)){
            String supplier_phone=contentValues.getAsString(stockEntry.COLUMN_SUPPLIER_PHONE);
            if(supplier_phone==null) {
                throw new IllegalArgumentException("require supplier_phone");
            }}

        if(contentValues.containsKey(stockEntry.COLUMN_QUANTITY)){
            Integer quantity=contentValues.getAsInteger(stockEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Product requires valid quantity");
            }
        }

        if(contentValues.containsKey(stockEntry.COLUMN_PRICE)){
            Integer price=contentValues.getAsInteger(stockEntry.COLUMN_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Product requires valid price");
            }}

        if (contentValues.size() == 0) {
            return 0;
        }
        dbConnection=new DBConnection(getContext());
        SQLiteDatabase database = dbConnection.getWritableDatabase();
        int rowsUpdated = database.update(stockEntry.TABLE_NAME, contentValues, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}



