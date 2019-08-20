package com.bytecodecomp.npos.Activities.Admin.Packages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Package_Admin_Adapter;
import com.bytecodecomp.npos.Data_Models.Package_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class ListPackagesActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference packageDatabaseReference = AppController.packageDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_packages);

        initToolbar();
        read_packages();

    }

    //Get packages to listview
    private void read_packages(){

        final ArrayList<Boolean> store_inventory = new ArrayList<>();
        final ArrayList<Integer> store_inventory_limit = new ArrayList<>();
        final ArrayList<Boolean> store_purchase = new ArrayList<>();
        final ArrayList<Integer> store_purchase_daily_limit = new ArrayList<>();
        final ArrayList<Boolean> store_sales = new ArrayList<>();
        final ArrayList<Integer> store_sales_daily_limit = new ArrayList<>();
        final ArrayList<Boolean> store_customer = new ArrayList<>();
        final ArrayList<Integer> store_customer_limit = new ArrayList<>();
        final ArrayList<Boolean> store_supplier = new ArrayList<>();
        final ArrayList<Integer> store_supplier_limit = new ArrayList<>();
        final ArrayList<Boolean> store_staff = new ArrayList<>();
        final ArrayList<Integer> store_staff_limit = new ArrayList<>();
        final ArrayList<Boolean> store_assets = new ArrayList<>();
        final ArrayList<Integer> store_assets_limit = new ArrayList<>();
        final ArrayList<Boolean> store_exp_type = new ArrayList<>();
        final ArrayList<Integer> store_exp_type_limit = new ArrayList<>();
        final ArrayList<Boolean> store_exp = new ArrayList<>();
        final ArrayList<Integer> store_exp_limit = new ArrayList<>();
        final ArrayList<String> package_name = new ArrayList<>();
        final ArrayList<String> package_exp = new ArrayList<>();
        final ArrayList<String> package_cost = new ArrayList<>();
        final ArrayList<String> package_period = new ArrayList<>();
        final ArrayList<String> package_id = new ArrayList<>();

        packageDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList before a new value listener
                store_inventory.clear();
                store_inventory_limit.clear();
                store_purchase.clear();
                store_purchase_daily_limit.clear();
                store_sales.clear();
                store_sales_daily_limit.clear();
                store_customer.clear();
                store_customer_limit.clear();
                store_supplier.clear();
                store_supplier_limit.clear();
                store_staff.clear();
                store_staff_limit.clear();
                store_assets.clear();
                store_assets_limit.clear();
                store_exp_type.clear();
                store_exp_type_limit.clear();
                store_exp.clear();
                store_exp_limit.clear();
                package_exp.clear();
                package_name.clear();
                package_cost.clear();
                package_period.clear();
                package_id.clear();


                while((iterator.hasNext())){
                    Package_Model value = iterator.next().getValue(Package_Model.class);

                    store_inventory.add(value.isStore_inventory());
                    store_inventory_limit.add(value.getStore_inventory_limit());
                    store_purchase.add(value.isStore_purchase());
                    store_purchase_daily_limit.add(value.getStore_purchase_daily_limit());
                    store_sales.add(value.isStore_inventory());
                    store_sales_daily_limit.add(value.getStore_sales_daily_limit());
                    store_customer.add(value.isStore_customer());
                    store_customer_limit.add(value.getStore_customer_limit());
                    store_supplier.add(value.isStore_supplier());
                    store_supplier_limit.add(value.getStore_supplier_limit());
                    store_staff.add(value.isStore_staff());
                    store_staff_limit.add(value.getStore_staff_limit());
                    store_assets.add(value.isStore_assets());
                    store_assets_limit.add(value.getStore_assets_limit());
                    store_exp_type.add(value.isStore_exp_type());
                    store_exp_type_limit.add(value.getStore_exp_type_limit());
                    store_exp.add(value.isStore_exp());
                    store_exp_limit.add(value.getStore_exp_limit());
                    package_exp.add(value.getPackage_exp());
                    package_name.add(value.getPackage_name());
                    package_cost.add(value.getPackage_cost());
                    package_period.add(value.getPackage_period());
                    package_id.add(value.getPackage_id());

                    ((Package_Admin_Adapter)(((GridView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((GridView)findViewById(R.id.list_view)).setAdapter(new Package_Admin_Adapter( store_inventory, store_inventory_limit, store_purchase, store_purchase_daily_limit, store_sales, store_sales_daily_limit, store_customer, store_customer_limit, store_supplier, store_supplier_limit, store_staff, store_staff_limit, store_assets, store_assets_limit, store_exp_type, store_exp_type_limit, store_exp, store_exp_limit, package_name, package_exp, package_cost, package_period, package_id,  this));
    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Choose Package");

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

                Intent intent = new Intent(this, AddPackageActivity.class);
                startActivity(intent);

                return true;

        }

        return true;

    }



}
