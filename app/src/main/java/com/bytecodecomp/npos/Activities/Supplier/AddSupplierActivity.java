package com.bytecodecomp.npos.Activities.Supplier;

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
import com.bytecodecomp.npos.Data_Models.Supplier_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSupplierActivity extends AppCompatActivity {

    EditText et_supplier_name, et_supplier_business, et_supplier_email, et_supplier_debit, et_supplier_phone_number;
    Button btn_submit;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference supplierDatabaseReference = AppController.supplierDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);

        initToolbar();

        et_supplier_name = (EditText) findViewById(R.id.et_supplier_name);
        et_supplier_business = (EditText) findViewById(R.id.et_supplier_business_name);
        et_supplier_email = (EditText) findViewById(R.id.et_supplier_email);
        et_supplier_debit = (EditText) findViewById(R.id.et_supplier_debit);
        et_supplier_phone_number = (EditText) findViewById(R.id.et_supplier_phone_number);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if all fields are entered
                if ( et_supplier_name.getText().toString().length() >= 0 && et_supplier_business.getText().toString().length() >= 0 && et_supplier_email.getText().toString().length() >= 0 && et_supplier_debit.getText().toString().length() >= 0 && et_supplier_phone_number.getText().toString().length() >= 5 ){

                    do_add_supplier(et_supplier_name.getText().toString(), et_supplier_email.getText().toString(), et_supplier_debit.getText().toString(), et_supplier_phone_number.getText().toString(), et_supplier_business.getText().toString());

                }

                //show alert on missing values
                else {

                    Toasty.warning(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT, true).show();

                }

            }
        });

    }


    public void do_add_supplier(String str_supplier_name, String str_supplier_email, String str_supplier_debit, String str_supplier_phone_number, String str_business){

        //generate supplier id
        String id = supplierDatabaseReference.push().getKey();

        //create add and update dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        String supplier_add_date = sdf.format(new Date());
        String supplier_update_date = supplier_add_date;

        //call add supplier to firebase
        add_supplier(id, str_supplier_name, str_supplier_email, str_supplier_debit, str_supplier_phone_number, supplier_update_date, supplier_add_date, str_business);

    }


    //call add supplier to firebase
    private void add_supplier(String supplier_id, String supplier_name, String supplier_email, String supplier_debit, String supplier_phone_number, String supplier_update_date, String supplier_add_date, String str_business){

        Toasty.success(getApplicationContext(), "Supplier Added", Toast.LENGTH_SHORT, true).show();

        Supplier_Model supplier_model = new Supplier_Model(supplier_id, supplier_name, supplier_email, supplier_debit, supplier_phone_number, supplier_update_date, supplier_add_date, str_business);
        supplierDatabaseReference.child(user.getUid()).child(supplier_id).setValue(supplier_model);

        //clear all the fields
        et_supplier_name.setText("");
        et_supplier_business.setText("");
        et_supplier_email.setText("");
        et_supplier_debit.setText("");
        et_supplier_phone_number.setText("");

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
        getSupportActionBar().setTitle("Create Supplier");

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
