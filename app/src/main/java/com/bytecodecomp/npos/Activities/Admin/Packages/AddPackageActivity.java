package com.bytecodecomp.npos.Activities.Admin.Packages;

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
import com.bytecodecomp.npos.Data_Models.Package_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

public class AddPackageActivity extends AppCompatActivity {

    EditText edt_package_name, edt_package_days, edt_package_value, edt_module_inventory, edt_module_purchase, edt_module_sales, edt_module_customers, edt_module_suppliers, edt_module_staff, edt_module_assets, edt_module_exp_type, edt_module_exp;
    Button btn_submit;

    //Firebase user and Database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference packageDatabaseReference = AppController.packageDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        initToolbar();

        edt_package_name = (EditText) findViewById(R.id.edt_package_name);
        edt_package_days = (EditText) findViewById(R.id.edt_package_days);
        edt_package_value = (EditText) findViewById(R.id.edt_package_value);

        edt_module_inventory = (EditText) findViewById(R.id.edt_module_inventory);
        edt_module_purchase = (EditText) findViewById(R.id.edt_module_purchase);
        edt_module_sales = (EditText) findViewById(R.id.edt_module_sales);
        edt_module_customers = (EditText) findViewById(R.id.edt_module_customers);
        edt_module_suppliers = (EditText) findViewById(R.id.edt_module_suppliers);
        edt_module_staff = (EditText) findViewById(R.id.edt_module_staff);
        edt_module_assets = (EditText) findViewById(R.id.edt_module_assets);
        edt_module_exp_type = (EditText) findViewById(R.id.edt_module_exp_type);
        edt_module_exp = (EditText) findViewById(R.id.edt_module_exp);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Values...", edt_package_name.getText().toString() + " : " + edt_package_days.getText().toString() + " : " + edt_package_value.getText().toString());

                if (edt_package_name.getText().toString().length() <= 1 || edt_package_days.getText().toString().length() <= 1){

                    Toasty.error(AddPackageActivity.this, "Missing Fields").show();

                }


                else {

                    do_add_package();

                }

            }
        });

    }

    //Create new packages
    public void do_add_package(){

        Toasty.success(getApplicationContext(), "Package Added", Toast.LENGTH_SHORT, true).show();

        String id = packageDatabaseReference.push().getKey();

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
                edt_package_days.getText().toString(), id);

        Log.e("Values", String.valueOf(package_model));

        packageDatabaseReference.child(id).setValue(package_model);


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

    }


    //Check if there are empty fields to disable module
    public boolean get_module_status(String value){

        boolean status = false;

        if (value.length() > 0){

            status = true;

        }

        return status;

    }


    //initialized toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Package");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
