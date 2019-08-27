package com.bytecodecomp.npos.Activities.Admin.Business;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Business_Adapter;
import com.bytecodecomp.npos.Data_Models.Store_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;
import java.util.Iterator;

public class ListBusinessActivity extends AppCompatActivity {

    //Database reference
    DatabaseReference storeDatabaseReference = AppController.storeDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_business);

        initToolbar();
        read_business();

    }

        //list all business on the system
        public void read_business(){

            final ArrayList<String> store_name = new ArrayList<>();
            final ArrayList<String> store_address = new ArrayList<>();
            final ArrayList<String> store_location = new ArrayList<>();
            final ArrayList<String> store_print = new ArrayList<>();
            final ArrayList<String> store_contacts = new ArrayList<>();
            final ArrayList<String> package_id = new ArrayList<>();
            final ArrayList<String> package_expiry = new ArrayList<>();
            final ArrayList<String> package_name = new ArrayList<>();
            final ArrayList<String> store_status = new ArrayList<>();

            storeDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    //Clear ArrayList on addValueEventListener
                    store_name.clear();
                    store_address.clear();
                    store_location.clear();
                    store_print.clear();
                    store_contacts.clear();
                    package_id.clear();
                    package_expiry.clear();
                    package_name.clear();
                    store_status.clear();

                    while((iterator.hasNext())){
                        Store_Model value = iterator.next().getValue(Store_Model.class);
                        store_name.add(value.getStore_name());
                        store_address.add(value.getStore_address());
                        store_location.add(value.getStore_location());
                        store_print.add(value.getStore_print());
                        store_contacts.add(value.getStore_contacts());
                        package_id.add(value.getPackage_id());
                        package_expiry.add(value.getPackage_expiry());
                        package_name.add(value.getPackage_name());
                        store_status.add(value.getStore_status());
                        ((Business_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            ((ListView)findViewById(R.id.list_view)).setAdapter(new Business_Adapter(store_name, store_address, store_location, store_print, store_contacts, package_id, package_expiry, package_name, store_status, this));

        }

        //Toolbar
        private void initToolbar() {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Business");

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if (item.getItemId() == android.R.id.home) {
                finish();
            }

            return super.onOptionsItemSelected(item);
        }


    }
