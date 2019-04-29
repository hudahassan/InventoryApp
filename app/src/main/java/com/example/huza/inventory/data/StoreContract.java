package com.example.huza.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by huza on 4/17/2019.
 */

public class StoreContract {
    public StoreContract() {

    }

    public static final String AUTHORITY_NAME="com.example.huza.inventory";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY_NAME);
    public static final String PATH_inventory="inventory";



    public static final class stockEntry implements BaseColumns{

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_inventory);
        public static final String CONETNT_LIST_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" +AUTHORITY_NAME+ "/" +PATH_inventory;
        public static final String CONETNT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" +AUTHORITY_NAME+ "/" +PATH_inventory;

        public static final String TABLE_NAME="stocks";
        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME="productname";
        public static final String COLUMN_SUPPLIER_NAME="suppliername";
        public static final String COLUMN_SUPPLIER_PHONE="supplierphone";
        public static final String COLUMN_QUANTITY="quantity";
        public static final String COLUMN_PRICE="price";

        public static final String DELETE = " DROP TABLE " + TABLE_NAME;




    }
}
