package com.bytecodecomp.npos.Activities.Inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Activities.User.DashboardActivity;
import com.bytecodecomp.npos.Data_Models.Product_Inventory;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProductActivity extends AppCompatActivity {

    //Edittext view
    EditText et_product_name, et_product_price, et_product_units, et_product_gtin, et_product_buying_price, et_product_vat;
    //Button
    Button btn_remove, btn_update;

    String org_product_id, org_product_name, org_product_value, org_product_units, org_product_add_date, org_product_update_date, org_product_gtin, org_product_buying_price, org_product_vat;

    Product_Inventory product_inventory = new Product_Inventory();

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        initToolbar();

        Bundle bundle = getIntent().getExtras();
        org_product_id = bundle.getString("product_id");
        org_product_name = bundle.getString("product_name");
        org_product_value = bundle.getString("product_value");
        org_product_units = bundle.getString("product_units");
        org_product_add_date = bundle.getString("product_add_date");
        org_product_update_date = bundle.getString("product_update_date");
        org_product_gtin = bundle.getString("product_gtin");
        org_product_buying_price = bundle.getString("product_buying_price");
        org_product_vat = bundle.getString("org_product_vat");


        product_inventory.setProduct_name(org_product_name);
        product_inventory.setProduct_add_date(org_product_add_date);
        product_inventory.setProduct_gtin(org_product_gtin);
        product_inventory.setProduct_id(org_product_id);
        product_inventory.setProduct_units(org_product_units);
        product_inventory.setProduct_value(org_product_value);
        product_inventory.setProduct_update_date(org_product_update_date);
        product_inventory.setProduct_vat(org_product_vat);


        et_product_name = (EditText) findViewById(R.id.et_product_name);
        et_product_name.setText(org_product_name);


        et_product_price = (EditText) findViewById(R.id.et_product_price);
        et_product_price.setText(org_product_value);


        et_product_units = (EditText) findViewById(R.id.et_product_units);
        et_product_units.setText(org_product_units);


        et_product_gtin = (EditText) findViewById(R.id.et_product_gtin);
        et_product_gtin.setText(org_product_gtin);


        et_product_buying_price = (EditText) findViewById(R.id.et_product_buying_price);
        et_product_buying_price.setText(org_product_buying_price);


        et_product_vat = (EditText) findViewById(R.id.et_product_vat);
        et_product_vat.setText(org_product_vat);


        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if all values are entered
                if (et_product_buying_price.getText().toString().length() > 1 &&  et_product_name.getText().toString().length() > 1 && et_product_price.getText().toString().length() > 1 && et_product_units.getText().toString().length() > 1 && et_product_gtin.getText().toString().length() >= 2 ){

                    //generate product update date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String product_update_date = sdf.format(new Date());

                    //call update product
                    do_update_product(org_product_id,et_product_name.getText().toString(), et_product_price.getText().toString(), et_product_units.getText().toString(), org_product_add_date, product_update_date, et_product_gtin.getText().toString(), et_product_buying_price.getText().toString());

                }

                //toast error on empty fields
                else {

                    Toasty.warning(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT, true).show();

                }

            }
        });



        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call delete product
                remove_product();

            }
        });




    }



    //update product on firebase
    private void do_update_product(String product_id, String product_name, String product_value, String product_units, String product_add_date, String product_update_date, String product_gtin, String product_buying_price){
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_id").setValue(product_id);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_name").setValue(product_name);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_value").setValue(product_value);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_units").setValue(product_units);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_add_date").setValue(product_add_date);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_update_date").setValue(product_update_date);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_gtin").setValue(product_gtin);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_buying_price").setValue(product_buying_price);
        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).child("product_vat").setValue(et_product_vat.getText().toString());

        finish();

    }


    //remove product on firebase
    private void remove_product(){

        inventoryDatabaseReference.child(product_inventory.getProduct_id()).removeValue();

        Toast.makeText(this, org_product_name + " Removed", Toast.LENGTH_SHORT).show();

        //on complete take user to dashboard
        Intent intent = new Intent(EditProductActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();

    }


    //On back click
    @Override
    public void onBackPressed() {
        finish();
    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Product");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //home button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
