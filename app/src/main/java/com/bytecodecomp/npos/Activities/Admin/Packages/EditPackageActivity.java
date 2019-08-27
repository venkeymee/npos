package com.bytecodecomp.npos.Activities.Admin.Packages;

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
import com.bytecodecomp.npos.Data_Models.Package_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

public class EditPackageActivity extends AppCompatActivity {

    EditText edt_package_name, edt_package_days, edt_package_value, edt_module_inventory, edt_module_purchase, edt_module_sales, edt_module_customers, edt_module_suppliers, edt_module_staff, edt_module_assets, edt_module_exp_type, edt_module_exp;
    Button btn_submit;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference packageDatabaseReference = AppController.packageDatabaseReference;

    String store_inventory_limit, store_purchase_daily_limit, store_sales_daily_limit, store_customer_limit, store_supplier_limit, store_staff_limit, store_assets_limit, store_exp_type_limit, store_exp_limit,
    package_name, package_exp, package_cost, package_id, package_period;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_package);

        Bundle bundle = getIntent().getExtras();
        store_inventory_limit = bundle.getString("store_inventory_limit");
        store_purchase_daily_limit = bundle.getString("store_purchase_daily_limit");
        store_sales_daily_limit = bundle.getString("store_sales_daily_limit");
        store_customer_limit = bundle.getString("store_customer_limit");
        store_supplier_limit = bundle.getString("store_supplier_limit");
        store_staff_limit = bundle.getString("store_staff_limit");
        store_assets_limit = bundle.getString("store_assets_limit");
        store_exp_type_limit = bundle.getString("store_exp_type_limit");
        store_exp_limit = bundle.getString("store_exp_limit");
        package_name = bundle.getString("package_name");
        package_exp = bundle.getString("package_exp");
        package_cost = bundle.getString("package_cost");
        package_id = bundle.getString("package_id");
        package_period = bundle.getString("package_period");

        initToolbar();

        edt_package_name = (EditText) findViewById(R.id.edt_package_name);
        edt_package_name.setText(package_name + "");
        edt_package_days = (EditText) findViewById(R.id.edt_package_days);
        edt_package_days.setText(package_period + "");
        edt_package_value = (EditText) findViewById(R.id.edt_package_value);
        edt_package_value.setText(package_cost);

        edt_module_inventory = (EditText) findViewById(R.id.edt_module_inventory);
        edt_module_inventory.setText(store_inventory_limit);
        edt_module_purchase = (EditText) findViewById(R.id.edt_module_purchase);
        edt_module_purchase.setText(store_purchase_daily_limit);
        edt_module_sales = (EditText) findViewById(R.id.edt_module_sales);
        edt_module_sales.setText(store_sales_daily_limit);
        edt_module_customers = (EditText) findViewById(R.id.edt_module_customers);
        edt_module_customers.setText(store_customer_limit);
        edt_module_suppliers = (EditText) findViewById(R.id.edt_module_suppliers);
        edt_module_suppliers.setText(store_supplier_limit);
        edt_module_staff = (EditText) findViewById(R.id.edt_module_staff);
        edt_module_staff.setText(store_staff_limit);
        edt_module_assets = (EditText) findViewById(R.id.edt_module_assets);
        edt_module_assets.setText(store_assets_limit);
        edt_module_exp_type = (EditText) findViewById(R.id.edt_module_exp_type);
        edt_module_exp_type.setText(store_exp_type_limit);
        edt_module_exp = (EditText) findViewById(R.id.edt_module_exp);
        edt_module_exp.setText(store_exp_limit);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_package_name.getText().toString().length() <= 1 || edt_package_days.getText().toString().length() <= 1){

                    Toasty.error(EditPackageActivity.this, "Missing Fields").show();

                }


                else {

                    do_update_package();

                }

            }
        });

    }


    //Update a package
    public void do_update_package(){

        Toasty.success(getApplicationContext(), "Package Edited", Toast.LENGTH_SHORT, true).show();


        Package_Model package_model = new Package_Model(
                get_module_status(edt_module_inventory.getText().toString()), Integer.parseInt(edt_module_inventory.getText().toString()),
                get_module_status(edt_module_purchase.getText().toString()), Integer.parseInt(edt_module_purchase.getText().toString()),
                get_module_status(edt_module_sales.getText().toString()), Integer.parseInt(edt_module_sales.getText().toString()),
                get_module_status(edt_module_customers.getText().toString()), Integer.parseInt(edt_module_customers.getText().toString()),
                get_module_status(edt_module_suppliers.getText().toString()), Integer.parseInt(edt_module_suppliers.getText().toString()),
                get_module_status(edt_module_staff.getText().toString()), Integer.parseInt(edt_module_staff.getText().toString()),
                get_module_status(edt_module_assets.getText().toString()), Integer.parseInt(edt_module_assets.getText().toString()),
                get_module_status(edt_module_exp_type.getText().toString()), Integer.parseInt(edt_module_exp_type.getText().toString()),
                get_module_status(edt_module_exp.getText().toString()), Integer.parseInt(edt_module_exp.getText().toString()),
                edt_package_name.getText().toString(), "", edt_package_value.getText().toString(),
                edt_package_days.getText().toString(), package_id);

        packageDatabaseReference.child(package_id).setValue(package_model);


        edt_module_inventory.setText("");
        edt_module_purchase.setText("");
        edt_module_sales.setText("");
        edt_module_customers.setText("");
        edt_module_suppliers.setText("");
        edt_module_staff.setText("");
        edt_module_assets.setText("");
        edt_module_exp_type.setText("");
        edt_module_exp.setText("");
        edt_package_name.setText("");
        edt_package_value.setText("");
        edt_package_days.setText("");

        finish();

    }


    //Check empty fields to disable module
    public boolean get_module_status(String value){

        boolean status = false;

        if (value.length() > 0){

            status = true;

        }

        return status;

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Package");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            // home button
            case android.R.id.home:
                finish();
                return true;


        }

        return true;

    }

}
