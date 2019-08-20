package com.bytecodecomp.npos.Activities.Customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Customer_Adapter;
import com.bytecodecomp.npos.Data_Models.Customer_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class CustomerActivity extends AppCompatActivity {

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference customerDatabaseReference = AppController.customerDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initToolbar();
        read_customers();

    }


    //get customers from firebase
    public void read_customers(){

        final ArrayList<String> customer_id = new ArrayList<>();
        final ArrayList<String> customer_name = new ArrayList<>();
        final ArrayList<String> customer_email = new ArrayList<>();
        final ArrayList<String> customer_credit = new ArrayList<>();
        final ArrayList<String> customer_phone_number = new ArrayList<>();
        final ArrayList<String> customer_update_date = new ArrayList<>();
        final ArrayList<String> customer_add_date = new ArrayList<>();

        customerDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList before a new value listener
                customer_id.clear();
                customer_name.clear();
                customer_email.clear();
                customer_credit.clear();
                customer_phone_number.clear();
                customer_update_date.clear();
                customer_add_date.clear();

                while((iterator.hasNext())){

                    Customer_Model value = iterator.next().getValue(Customer_Model.class);
                    customer_id.add(value.getCustomer_id());
                    customer_name.add(value.getCustomer_name());
                    customer_email.add(value.getCustomer_email());
                    customer_credit.add(value.getCustomer_credit());
                    customer_phone_number.add(value.getCustomer_phone_number());
                    customer_update_date.add(value.getCustomer_update_date());
                    customer_add_date.add(value.getCustomer_add_date());

                    ((Customer_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Customer_Adapter(customer_id, customer_name, customer_email, customer_credit, customer_phone_number, customer_update_date, customer_add_date, this));


    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Customers");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
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

            // create button
            case R.id.action_create:

                Intent intent = new Intent(this, AddCustomerActivity.class);
                startActivity(intent);

                return true;

        }

        return true;

    }


}
