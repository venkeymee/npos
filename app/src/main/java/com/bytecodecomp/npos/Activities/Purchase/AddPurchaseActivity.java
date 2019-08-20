package com.bytecodecomp.npos.Activities.Purchase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Data_Models.Product_Inventory;
import com.bytecodecomp.npos.Data_Models.Purchase_Model;
import com.bytecodecomp.npos.Data_Models.Supplier_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AddPurchaseActivity extends AppCompatActivity {

    Spinner spn_supplier_business, spn_item;
    EditText et_purchase_qnty, et_product_price, et_product_selling;
    Button btn_submit;

    String business_name;
    final List<String> product_ids = new ArrayList<String>();
    int product_position;
    String suppliers_name, product;
    boolean units_updated = false;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference supplierDatabaseReference = AppController.supplierDatabaseReference;
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;
    DatabaseReference purchasesDatabaseReference = AppController.purchasesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        initToolbar();

        spn_supplier_business = (Spinner) findViewById(R.id.spn_supplier_business);
        spn_supplier_business.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                suppliers_name = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spn_item = (Spinner) findViewById(R.id.spn_item);
        spn_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                product = parent.getItemAtPosition(position).toString();
                product_position = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        do_supplier_business();
        do_products();

        et_purchase_qnty = (EditText) findViewById(R.id.et_purchase_qnty);
        et_product_price = (EditText) findViewById(R.id.et_product_price);
        et_product_selling = (EditText) findViewById(R.id.et_product_selling);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if there are missing fields
                if (et_purchase_qnty.getText().toString().length() <= 1 && et_product_price.getText().toString().length() <= 1 && et_product_selling.getText().toString().length() <= 1 && business_name.length() <= 0){

                    Toasty.warning(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT, true).show();

                }

                //toast error in missing fields found
                else {

                    do_post_purchase(et_purchase_qnty.getText().toString(), et_product_price.getText().toString(), et_product_selling.getText().toString());

                }

            }
        });

    }


    //get store suppliers list
    public void do_supplier_business(){

        supplierDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<String>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())) {
                    Supplier_Model value = iterator.next().getValue(Supplier_Model.class);
                    String supplierName = value.getSupplier_name();
                    nomeConsulta.add(supplierName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddPurchaseActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_supplier_business.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    //get store products list
    public void do_products(){

        inventoryDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<String>();

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())) {
                    Product_Inventory value = iterator.next().getValue(Product_Inventory.class);
                    String product_name = value.getProduct_name();
                    String product_id = value.getProduct_id();
                    nomeConsulta.add(product_name);
                    product_ids.add(product_id);

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddPurchaseActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_item.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //create a purchase
    public void do_post_purchase(String purchase_qnty, String purchase_price, String purchase_selling){

        Toasty.success(getApplicationContext(), "Purchase Added", Toast.LENGTH_SHORT, true).show();

        //generate purchase id
        String id = purchasesDatabaseReference.push().getKey();

        //get purchase add and updates date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String purchase_add_date = sdf.format(new Date());

        Purchase_Model purchase_model = new Purchase_Model(id, purchase_qnty, purchase_add_date, product, purchase_price, purchase_selling, "received", suppliers_name);
        purchasesDatabaseReference.child(user.getUid()).child(id).setValue(purchase_model);

        //clear EditText values
        et_purchase_qnty.setText("");
        et_product_price.setText("");
        et_product_selling.setText("");

        //call update store inventory on firestore
        set_inventory_update(purchase_qnty, purchase_price, purchase_selling);

    }

    //update store inventory on firestore
    public void set_inventory_update(final String purchase_qnty, String purchase_price, String purchase_selling){

        //update buying price
        inventoryDatabaseReference.child(user.getUid()).child(product_ids.get(product_position)).child("product_buying_price").setValue(purchase_price);

        //updating product selling price
        inventoryDatabaseReference.child(user.getUid()).child(product_ids.get(product_position)).child("product_value").setValue(purchase_selling);

        //uodating product units
        inventoryDatabaseReference.child(user.getUid()).child(product_ids.get(product_position)).child("product_units").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //use units_updated to check if item is updated or not.
                if (units_updated == false){

                    String current_unit = String.valueOf(dataSnapshot.getValue());
                    Log.e("current_unit", current_unit);
                    int updated_unit = Integer.parseInt(current_unit) + Integer.parseInt(purchase_qnty);
                    inventoryDatabaseReference.child(user.getUid()).child(product_ids.get(product_position)).child("product_units").setValue(updated_unit + "");

                    units_updated = true;

                }

                else {

                    finish();

                }



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Purchases");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
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
