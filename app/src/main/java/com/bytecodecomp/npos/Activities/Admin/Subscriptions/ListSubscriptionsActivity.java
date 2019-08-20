package com.bytecodecomp.npos.Activities.Admin.Subscriptions;

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
import com.bytecodecomp.npos.Adapters.Subscriptions_Adapter;
import com.bytecodecomp.npos.Data_Models.Subscriptions_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;
import java.util.Iterator;

public class ListSubscriptionsActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference subscriptionsDatabaseReference = AppController.subscriptionsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subscriptions);

        initToolbar();
        read_subscriptions();

    }

    //Get all subscriptions
    public void read_subscriptions(){

        final ArrayList<String> subscription_id = new ArrayList<>();
        final ArrayList<String> subscription_date = new ArrayList<>();
        final ArrayList<String> package_name = new ArrayList<>();
        final ArrayList<String> package_id = new ArrayList<>();
        final ArrayList<String> subscription_price = new ArrayList<>();
        final ArrayList<String> subscription_business_name = new ArrayList<>();
        final ArrayList<String> subscription_business_id = new ArrayList<>();
        final ArrayList<String> subscription_expiry = new ArrayList<>();
        final ArrayList<String> subscription_transaction_id = new ArrayList<>();
        final ArrayList<String> subscription_payment = new ArrayList<>();

        subscriptionsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList on addValueEventListener
                subscription_id.clear();
                subscription_date.clear();
                package_name.clear();
                package_id.clear();
                subscription_price.clear();
                subscription_business_name.clear();
                subscription_business_id.clear();
                subscription_expiry.clear();
                subscription_transaction_id.clear();
                subscription_payment.clear();

                while((iterator.hasNext())){
                    Subscriptions_Model value = iterator.next().getValue(Subscriptions_Model.class);
                    subscription_id.add(value.getSubscription_id());
                    subscription_date.add(value.getSubscription_date());
                    package_name.add(value.getPackage_name());
                    package_id.add(value.getPackage_id());
                    subscription_price.add(value.getSubscription_price());
                    subscription_business_name.add(value.getSubscription_business_name());
                    subscription_business_id.add(value.getSubscription_business_id());
                    subscription_expiry.add(value.getSubscription_expiry());
                    subscription_transaction_id.add(value.getSubscription_transaction_id());
                    subscription_payment.add(value.getSubscription_payment());
                    ((Subscriptions_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Subscriptions_Adapter(subscription_id, subscription_date, package_name, package_id, subscription_price, subscription_business_name, subscription_business_id, subscription_expiry, subscription_transaction_id, subscription_payment, this));

    }

    //Initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Package Subscription");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
