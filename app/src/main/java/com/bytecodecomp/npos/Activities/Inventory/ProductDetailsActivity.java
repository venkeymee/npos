package com.bytecodecomp.npos.Activities.Inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Activities.User.DashboardActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Product_Inventory;
import com.bytecodecomp.npos.Plugins.Printer.MainActivity;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;




public class ProductDetailsActivity extends AppCompatActivity {

    //Text views
    TextView et_product_name, et_product_price, et_product_gtin, et_product_buying_price, et_product_units, et_product_vat;

    String org_product_id, org_product_name, org_product_value, org_product_units, org_product_add_date, org_product_update_date, org_product_gtin, org_product_buying_price, org_product_vat;

    Product_Inventory product_inventory = new Product_Inventory();

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle bundle = getIntent().getExtras();
        org_product_id = bundle.getString("product_id");
        org_product_name = bundle.getString("product_name");
        org_product_value = bundle.getString("product_value");
        org_product_units = bundle.getString("product_units");
        org_product_add_date = bundle.getString("product_add_date");
        org_product_update_date = bundle.getString("product_update_date");
        org_product_gtin = bundle.getString("product_gtin");
        org_product_buying_price = bundle.getString("product_buying_price");
        org_product_vat = bundle.getString("product_vat");

        initToolbar();


        product_inventory.setProduct_name(org_product_name);
        product_inventory.setProduct_add_date(org_product_add_date);
        product_inventory.setProduct_gtin(org_product_gtin);
        product_inventory.setProduct_id(org_product_id);
        product_inventory.setProduct_units(org_product_units);
        product_inventory.setProduct_value(org_product_value);
        product_inventory.setProduct_update_date(org_product_update_date);
        product_inventory.setProduct_vat(org_product_vat);



        et_product_name = (TextView) findViewById(R.id.et_product_name);
        et_product_name.setText(org_product_name);

        et_product_price = (TextView) findViewById(R.id.et_product_price);
        et_product_price.setText(org_product_value + " " + App_Settings.CURRENCY_TYPE);

        et_product_gtin = (TextView) findViewById(R.id.et_product_gtin);
        et_product_gtin.setText(org_product_gtin);

        et_product_buying_price = (TextView) findViewById(R.id.et_product_buying_price);
        et_product_buying_price.setText(org_product_buying_price);

        et_product_units = (TextView) findViewById(R.id.et_product_units);
        et_product_units.setText(org_product_units + " Unit/s");

        et_product_vat = (TextView) findViewById(R.id.et_product_vat);
        et_product_vat.setText(org_product_vat + " " + " % ");


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
        getSupportActionBar().setTitle("View Product");

    }

    //choose menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_product, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int itemId = item.getItemId();

        switch(itemId) {

            //  home button
            case android.R.id.home:
                finish();
                return true;

            //  print label button
            case R.id.action_print:

                String content = "\n================================";
                content = content +  "\nNAME : " +  org_product_name + " \n" + "SKU : " + org_product_gtin + " \n" + "PRICE : " + org_product_value;
                content = content + "\n================================";

                Intent intents = new Intent(this, MainActivity.class);
                intents.putExtra("action",  content);
                App_Settings.current_activity = "ProductDetailsActivity";
                startActivity(intents);


                return true;

            //  edit button
            case R.id.action_edit:
                Intent intent = new Intent(this, EditProductActivity.class);
                intent.putExtra("product_id", org_product_id);
                intent.putExtra("product_name", org_product_name);
                intent.putExtra("product_value", org_product_value);
                intent.putExtra("product_units", org_product_units);
                intent.putExtra("product_add_date", org_product_add_date);
                intent.putExtra("product_update_date", org_product_update_date);
                intent.putExtra("product_gtin", org_product_gtin);
                intent.putExtra("product_vat", org_product_vat);
                intent.putExtra("product_buying_price", org_product_buying_price);
                startActivity(intent);
                finish();
                return true;


            //  remove product button
            case R.id.action_remove:

                delete_product();

                return true;

        }

        return true;


    }


    //Delete product function
    public void delete_product(){

        Toasty.warning(this, "Product deleted");

        inventoryDatabaseReference.child(user.getUid()).child(org_product_id).removeValue();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();

    }




}



