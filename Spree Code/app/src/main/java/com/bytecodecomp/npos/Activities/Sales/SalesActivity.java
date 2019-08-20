package com.bytecodecomp.npos.Activities.Sales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Sales_Adapter;
import com.bytecodecomp.npos.Data_Models.Sales_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;
import java.util.Iterator;

public class SalesActivity extends AppCompatActivity {

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        initToolbar();
        read_sales();

    }

    //READ SALES
    private void read_sales() {

        final ArrayList<String> payment_date = new ArrayList<>();
        final ArrayList<String> payment_id = new ArrayList<>();
        final ArrayList<String> payment_money_id = new ArrayList<>();
        final ArrayList<String> payment_method = new ArrayList<>();
        final ArrayList<String> items = new ArrayList<>();
        final ArrayList<String> user_id = new ArrayList<>();
        final ArrayList<String> user_name = new ArrayList<>();
        final ArrayList<String> payment_amount = new ArrayList<>();
        final ArrayList<String> payment_user_staff = new ArrayList<>();
        final ArrayList<String> customer_name = new ArrayList<>();

        salesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                payment_date.clear();
                payment_id.clear();
                payment_money_id.clear();
                payment_method.clear();
                items.clear();
                user_id.clear();
                user_name.clear();
                payment_amount.clear();
                payment_user_staff.clear();
                customer_name.clear();

                while((iterator.hasNext())){
                    Sales_Model value = iterator.next().getValue(Sales_Model.class);

                    payment_date.add(value.getPayment_date());
                    payment_id.add(value.getPayment_id());
                    payment_money_id.add(value.getPayment_money_id());
                    payment_method.add(value.getPayment_method());
                    items.add(value.getItems());
                    user_id.add(value.getUser_id());
                    user_name.add(value.getUser_name());
                    payment_amount.add(value.getPayment_amount());
                    payment_user_staff.add(value.getPayment_user_staff());
                    customer_name.add(value.getCustomer_name());

                    ((Sales_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Sales_Adapter(payment_date, payment_id, payment_money_id, payment_method, items, user_id, user_name, payment_amount, payment_user_staff, customer_name, this));
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Sales");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




}
