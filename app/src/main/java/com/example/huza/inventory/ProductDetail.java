package com.example.huza.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.huza.inventory.data.StoreContract.stockEntry;

import java.text.NumberFormat;

public class ProductDetail extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PRODUCT_LOADER= 0;


    EditText edit_product,edit_supplier,edit_phone,edit_price;
    EditText text_quantity;
    Button button_increase,button_decrease;
    ImageButton imagecall;
    int init_quantity=0;
    Uri mContent_uri;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent=getIntent();
        mContent_uri=intent.getData();
        if(mContent_uri==null){
            setTitle("Add Product");
            invalidateOptionsMenu();
        }else {
            setTitle("Product Details");
            //getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER,null,this);
        }



        edit_product=(EditText)findViewById(R.id.edit_product);
        edit_supplier=(EditText)findViewById(R.id.edit_supplier);
        edit_phone=(EditText)findViewById(R.id.edit_phonesupplier);
        edit_price=(EditText)findViewById(R.id.edit_price);
        text_quantity=(EditText) findViewById(R.id.edit_quantity);

        button_decrease=(Button)findViewById(R.id.minus_quantity);
        button_increase=(Button)findViewById(R.id.plus_quantity);
        imagecall=(ImageButton) findViewById(R.id.call);

        edit_product.setOnTouchListener(mTouchListener);
        edit_supplier.setOnTouchListener(mTouchListener);
        edit_phone.setOnTouchListener(mTouchListener);
        edit_price.setOnTouchListener(mTouchListener);
        text_quantity.setOnTouchListener(mTouchListener);

        button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });
        button_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });

        imagecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplier_phone=edit_supplier.getText().toString().trim();
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",supplier_phone,null));
                startActivity(intent);

            }
        });


    }




    public void increase(){
        ++ init_quantity;
        displayquantity(init_quantity);
    }
    public void decrease() {
        if (init_quantity == 0) {
            return;
        }
        --init_quantity;
        displayquantity(init_quantity);

    }
    public void displayquantity(int number){
        text_quantity.setText(String.valueOf(number));
        // edit_price.setText(String.valueOf(5*number));
    }


    public void savedata() {

        String PRODUCT_NAME = edit_product.getText().toString().trim();
        String SUPPLIRE_NAME = edit_supplier.getText().toString().trim();
        String QUANTITY_string = text_quantity.getText().toString().trim();
        String PHONE = edit_phone.getText().toString().trim();
        int QUANTITY = Integer.parseInt(QUANTITY_string);
        String PRICE_string = edit_price.getText().toString().trim();
        int PRICE = Integer.parseInt(PRICE_string);

        if (mContent_uri==null && TextUtils.isEmpty(PRODUCT_NAME) && TextUtils.isEmpty(SUPPLIRE_NAME) &&
                TextUtils.isEmpty(QUANTITY_string) && TextUtils.isEmpty(PHONE) && TextUtils.isEmpty(PRICE_string)){
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(stockEntry.COLUMN_PRODUCT_NAME, PRODUCT_NAME);
        contentValues.put(stockEntry.COLUMN_SUPPLIER_NAME, SUPPLIRE_NAME);
        contentValues.put(stockEntry.COLUMN_SUPPLIER_PHONE, PHONE);
        contentValues.put(stockEntry.COLUMN_QUANTITY, QUANTITY);
        contentValues.put(stockEntry.COLUMN_PRICE, PRICE);


        if (mContent_uri == null) {
            Uri new_uri = getContentResolver().insert(stockEntry.CONTENT_URI, contentValues);

            if (new_uri == null) {
                Toast.makeText(this, "failed insert",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "successful insert",
                        Toast.LENGTH_SHORT).show();


            }
        }else {
            int row_updated=getContentResolver().update(mContent_uri, contentValues,null,null);
            if (row_updated == 0) {
                Toast.makeText(this, "failing update", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "successfully update", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void deletestock(){
        if(mContent_uri!=null){
            int row_deleted=getContentResolver().delete(mContent_uri,null,null);
            if (row_deleted == 0) {
                Toast.makeText(this, "failing delete", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "successfully delete", Toast.LENGTH_LONG).show();

            }
        }

        finish();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                if(!mProductHasChanged) {// if there isn't any change
                    NavUtils.navigateUpFromSameTask(ProductDetail.this);
                    return true;
                }
                DialogInterface.OnClickListener dicardButton=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(ProductDetail.this);
                    }
                };
                showunsavedchangeDialog(dicardButton);
                return true;
            case R.id.save:

                savedata();
                finish();
                return true;

            case R.id.delete:
                showDeleteConfimationDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mContent_uri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={stockEntry._ID,stockEntry.COLUMN_PRODUCT_NAME,stockEntry.COLUMN_SUPPLIER_NAME,stockEntry.COLUMN_SUPPLIER_PHONE
                ,stockEntry.COLUMN_QUANTITY,stockEntry.COLUMN_PRICE};

        return new CursorLoader(this,mContent_uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            //Set Cursor to the 0th position
            cursor.moveToFirst();

            //Find the columns of the product attributes
            int productNameColumnIndex = cursor.getColumnIndex(stockEntry.COLUMN_PRODUCT_NAME);
            int supplierNameColumnIndex = cursor.getColumnIndex(stockEntry.COLUMN_SUPPLIER_NAME);
            int suppliePhoneNumberColumnIndex = cursor.getColumnIndex(stockEntry.COLUMN_SUPPLIER_PHONE);
            int productQuantityColumnIndex = cursor.getColumnIndex(stockEntry.COLUMN_QUANTITY);
            int productPriceColumnIndex = cursor.getColumnIndex(stockEntry.COLUMN_PRICE);

            //Read the product attributes from the Cursor for the current product
            String productName = cursor.getString(productNameColumnIndex);
            String productPrice = cursor.getString(productPriceColumnIndex);
            String productQuantity = cursor.getString(productQuantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhoneNumber = cursor.getString(suppliePhoneNumberColumnIndex);


            //Format the price
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            productPrice = formatter.format(Double.parseDouble(productPrice) / 100);

            //Populate views with extracted data
            edit_product.setText(productName);
            edit_price.setText(productPrice);
            text_quantity.setText(productQuantity);
            edit_supplier.setText(supplierName);
            edit_phone.setText(supplierPhoneNumber);
        }
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        edit_product.setText("");
        edit_supplier.setText("");
        edit_phone.setText("");
        text_quantity.setText("");
        edit_price.setText("");
    }


    //used ib backhome
    public void showunsavedchangeDialog(DialogInterface.OnClickListener dicardButtonclickListner){

        android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Discard your change and quit editing");
        builder.setPositiveButton("discard",dicardButtonclickListner);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    //used in delete
    public void showDeleteConfimationDialog(){

        android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Delete this Product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletestock();
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
        android.support.v7.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}




