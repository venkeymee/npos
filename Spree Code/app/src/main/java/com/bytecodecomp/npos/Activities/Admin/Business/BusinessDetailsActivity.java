package com.bytecodecomp.npos.Activities.Admin.Business;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bytecodecomp.npos.R;

public class BusinessDetailsActivity extends AppCompatActivity {

    TextView txt_sore_name, txt_package_name, txt_store_location, txt_customers, txt_staff, txt_inventory, txt_purchase, txt_assets, txt_expense;

    String store_name, store_address, store_location, store_contacts, package_name, store_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        Bundle bundle = getIntent().getExtras();
        store_name = bundle.getString("store_name");
        store_address = bundle.getString("store_address");
        store_location = bundle.getString("store_location");
        store_contacts = bundle.getString("store_contacts");
        package_name = bundle.getString("package_name");
        store_status = bundle.getString("store_status");

        initToolbar();

        txt_sore_name = (TextView) findViewById(R.id.txt_sore_name);
        txt_sore_name.setText(store_name);

        txt_package_name = (TextView) findViewById(R.id.txt_package_name);
        txt_package_name.setText(package_name);

        txt_store_location = (TextView) findViewById(R.id.txt_store_location);
        txt_store_location.setText(store_location);


        txt_customers = (TextView) findViewById(R.id.txt_customers);
        txt_staff = (TextView) findViewById(R.id.txt_staff);
        txt_inventory = (TextView) findViewById(R.id.txt_inventory);
        txt_purchase = (TextView) findViewById(R.id.txt_purchase);
        txt_assets = (TextView) findViewById(R.id.txt_assets);
        txt_expense = (TextView) findViewById(R.id.txt_expense);

    }

    //initialized toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Details");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
