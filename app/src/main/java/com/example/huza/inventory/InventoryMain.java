package com.example.huza.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huza.inventory.data.DBConnection;
import com.example.huza.inventory.data.StoreContract.stockEntry;
import java.util.ArrayList;

public class InventoryMain extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LOADE_INVENTORY=0;

    FloatingActionButton fab;
    DBConnection dbConnection;
    ListView displaylist;


    myCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_main);
        displaylist=(ListView)findViewById(R.id.listview);


        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InventoryMain.this,ProductDetail.class);
                startActivity(intent);
            }
        });

        dbConnection=new DBConnection(this);
        adapter=new myCursorAdapter(this,null);
        displaylist.setAdapter(adapter);

        View emptyView = findViewById(R.id.empty_view);
        displaylist.setEmptyView(emptyView);

        displaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryMain.this, ProductDetail.class);

                Uri content_uri= ContentUris.withAppendedId(stockEntry.CONTENT_URI,id);
                intent.setData(content_uri);
                startActivity(intent);

            }});

        TextView empty = new TextView(this);
        empty.setHeight(200);
        displaylist.addFooterView(empty, 0, false);


        getLoaderManager().initLoader(LOADE_INVENTORY,null,this);


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.delete:
                showDeleteConfimationDialog();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={stockEntry._ID,stockEntry.COLUMN_PRODUCT_NAME,stockEntry.COLUMN_QUANTITY,stockEntry.COLUMN_PRICE};
        return new CursorLoader(this,stockEntry.CONTENT_URI,projection,null,null,null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }


    public void deleteallproduct(){
        int rowsDeleted=getContentResolver().delete(stockEntry.CONTENT_URI,null,null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");

    }
    public void showDeleteConfimationDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are You sure For Deleting All Pets? ");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteallproduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
