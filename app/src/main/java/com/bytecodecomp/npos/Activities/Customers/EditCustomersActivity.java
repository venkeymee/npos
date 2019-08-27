package com.bytecodecomp.npos.Activities.Customers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.Customer_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCustomersActivity extends AppCompatActivity {


    EditText et_customer_name, et_customer_email, et_customer_credit, et_customer_phone_number;
    Button btn_submit;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference customerDatabaseReference = AppController.customerDatabaseReference;

    String customer_id, customer_name, customer_email, customer_credit, customer_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customers);

        Bundle bundle = getIntent().getExtras();
        customer_id = bundle.getString("customer_id");
        customer_name = bundle.getString("customer_name");
        customer_email = bundle.getString("customer_email");
        customer_credit = bundle.getString("customer_credit");
        customer_phone_number = bundle.getString("customer_phone_number");

        initToolbar();

        et_customer_name = (EditText) findViewById(R.id.et_customer_name);
        et_customer_name.setText(customer_name);
        et_customer_email = (EditText) findViewById(R.id.et_customer_email);
        et_customer_email.setText(customer_email);
        et_customer_credit = (EditText) findViewById(R.id.et_customer_credit);
        et_customer_credit.setText(customer_credit);
        et_customer_phone_number = (EditText) findViewById(R.id.et_customer_phone_number);
        et_customer_phone_number.setText(customer_phone_number);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( et_customer_name.getText().toString().length() >= 0 && et_customer_email.getText().toString().length() >= 0 && et_customer_credit.getText().toString().length() >= 0 && et_customer_phone_number.getText().toString().length() >= 5 ){

                    do_update_customer(et_customer_name.getText().toString(), et_customer_email.getText().toString(), et_customer_credit.getText().toString(), et_customer_phone_number.getText().toString());

                }

                else {

                    Toasty.warning(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT, true).show();

                }

            }
        });

    }


    //update customer details
    public void do_update_customer(String str_customer_name, String str_customer_email, String str_customer_credit, String str_customer_phone_number){


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        String customer_add_date = sdf.format(new Date());
        String customer_update_date = customer_add_date;

        //update customer details to firebase
        update_customer(customer_id, str_customer_name, str_customer_email, str_customer_credit, str_customer_phone_number, customer_update_date, customer_add_date);

    }


    //update customer details to firebase
    private void update_customer(String customer_id, String customer_name, String customer_email, String customer_credit, String customer_phone_number, String customer_update_date, String customer_add_date){

        Toasty.success(getApplicationContext(), "Customer Added", Toast.LENGTH_SHORT, true).show();

        Customer_Model customer_model = new Customer_Model(customer_id, customer_name, customer_email, customer_credit, customer_phone_number, customer_update_date, customer_add_date);
        customerDatabaseReference.child(user.getUid()).child(customer_id).setValue(customer_model);

        //clear EditText values
        et_customer_name.setText("");
        et_customer_email.setText("");
        et_customer_credit.setText("");
        et_customer_phone_number.setText("");

    }




    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Customers");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            // home button
            case android.R.id.home:
                finish();
                return true;

            // home button
            case R.id.action_delete:
                customerDatabaseReference.child(user.getUid()).child(customer_id).removeValue();
                return true;

        }

        return true;

    }

}

