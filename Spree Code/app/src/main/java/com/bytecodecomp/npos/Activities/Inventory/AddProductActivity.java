package com.bytecodecomp.npos.Activities.Inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.Product_Inventory_Details;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddProductActivity extends AppCompatActivity {

    EditText et_product_name, et_product_price, et_product_units, et_product_gtin, et_product_buying_price, et_product_vat;
    Button btn_submit;

    int vat_value = 0;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initToolbar();

        et_product_name = (EditText) findViewById(R.id.et_product_name);
        et_product_price = (EditText) findViewById(R.id.et_product_price);
        et_product_units = (EditText) findViewById(R.id.et_product_units);
        et_product_gtin = (EditText) findViewById(R.id.et_product_gtin);
        et_product_buying_price = (EditText) findViewById(R.id.et_product_buying_price);
        et_product_vat = (EditText) findViewById(R.id.et_product_vat);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_product_gtin.getText().toString().length() <= 4){

                    String sku = String.valueOf(AppController.get_random_sku());
                    Log.e("sku....", sku);
                    et_product_gtin.setText(sku);

                    new AlertDialog.Builder(AddProductActivity.this)
                            .setTitle("SKU field is empty")
                            .setMessage("Click ENTER to auto generate and CANCEL to enter manually")
                            .setCancelable(false)
                            .setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ( et_product_buying_price.getText().toString().length() > 0 &&  et_product_name.getText().toString().length() > 0 && et_product_price.getText().toString().length() > 0 && et_product_units.getText().toString().length() > 0 && et_product_gtin.getText().toString().length() >= 2 ){

                                        vat_value = Integer.parseInt(et_product_vat.getText().toString()) + 0;
                                        do_add_product(et_product_name.getText().toString(), et_product_price.getText().toString(), et_product_units.getText().toString(), et_product_gtin.getText().toString(), et_product_buying_price.getText().toString());

                                    }

                                    else {

                                        Toasty.warning(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT, true).show();

                                    }

                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                   dialog.dismiss();

                                }
                            }).show();

                }

                else {

                    if ( et_product_buying_price.getText().toString().length() > 0 &&  et_product_name.getText().toString().length() > 0 && et_product_price.getText().toString().length() > 0 && et_product_units.getText().toString().length() > 0 && et_product_gtin.getText().toString().length() >= 2 && et_product_vat.getText().toString().length() >= 1 ){

                        vat_value = Integer.parseInt(et_product_vat.getText().toString()) + 0;
                        do_add_product(et_product_name.getText().toString(), et_product_price.getText().toString(), et_product_units.getText().toString(), et_product_gtin.getText().toString(), et_product_buying_price.getText().toString());

                    }

                    else {

                        Toasty.warning(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT, true).show();

                    }

                }



            }
        });

    }


    //create a new product
    public void do_add_product(String str_product_name, String str_product_price, String str_product_units, String str_product_gtin, String str_product_buying_price){

        //generate product id
        String id = inventoryDatabaseReference.push().getKey();

        // product add and update dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        String product_add_date = sdf.format(new Date());
        String product_update_date = product_add_date;

        //create a new product on firebase
        add_product(id, str_product_name, str_product_price, str_product_units, product_add_date, product_update_date, str_product_gtin, str_product_buying_price);

    }


    //create a new product on firebase
    private void add_product(String product_id, String product_name, String product_value, String product_units, String product_add_date, String product_update_date, String product_gtin, String product_buying_price){

        Toasty.success(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT, true).show();
        String vat = String.valueOf(vat_value);

        Product_Inventory_Details product_inventory_details = new Product_Inventory_Details(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, vat);
        inventoryDatabaseReference.child(user.getUid()).child(product_id).setValue(product_inventory_details);

        // Clear edit fields
        et_product_name.setText("");
        et_product_price.setText("");
        et_product_units.setText("");
        et_product_gtin.setText("");
        et_product_buying_price.setText("");
        et_product_vat.setText("");

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Product");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // home button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
