package com.example.huza.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huza.inventory.data.StoreContract;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by huza on 4/17/2019.
 */

public class myCursorAdapter extends CursorAdapter {

    public myCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }




    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView Productname_text=(TextView)view.findViewById(R.id.product_name);


        TextView quantity_text=(TextView)view.findViewById(R.id.quantity);


        TextView price_text=(TextView)view.findViewById(R.id.price);

         Button sale=(Button) view.findViewById(R.id.sale);

        int id_index=cursor.getColumnIndex(StoreContract.stockEntry._ID);
        int name_index=cursor.getColumnIndex(StoreContract.stockEntry.COLUMN_PRODUCT_NAME);
        int quantity_index=cursor.getColumnIndex(StoreContract.stockEntry.COLUMN_QUANTITY);
        int price_index=cursor.getColumnIndex(StoreContract.stockEntry.COLUMN_PRICE);


        int id=cursor.getInt(id_index);
        String name=cursor.getString(name_index);
        final String quantity=cursor.getString(quantity_index);
        String price=cursor.getString(price_index);

        NumberFormat formattt=NumberFormat.getCurrencyInstance();
        price=formattt.format(Double.parseDouble(price)/100);

        Productname_text.setText(name);
        quantity_text.setText(String.valueOf(quantity));
        price_text.setText(String.valueOf(price));

        final Uri currentProductUri = ContentUris.withAppendedId(StoreContract.stockEntry.CONTENT_URI, id);


        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int QuantityInteger = Integer.parseInt(quantity);

                //Decrease the quantity of the product by 1
                if (QuantityInteger > 0) {
                    QuantityInteger -= 1;
                } else {
                    QuantityInteger = 0;
                }
                ContentValues values = new ContentValues();
                values.put(StoreContract.stockEntry.COLUMN_QUANTITY, QuantityInteger);

                //Pass the Content Resolver the updated product quantity
                int rowsAffected = context.getContentResolver()
                        .update(currentProductUri, values, null, null);
            }
        });


    }
}
