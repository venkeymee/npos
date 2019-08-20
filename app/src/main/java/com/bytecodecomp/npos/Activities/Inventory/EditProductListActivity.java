package com.bytecodecomp.npos.Activities.Inventory;

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
import com.bytecodecomp.npos.Adapters.Product_Edit_Adapter;
import com.bytecodecomp.npos.Data_Models.Product_Inventory_Details;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class EditProductListActivity extends AppCompatActivity {

    //Firebase user and database refenece
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_list);

        initToolbar();
        read_products();

    }


    //get inventory
    private void read_products(){
        final ArrayList<String> product_id = new ArrayList<>();
        final ArrayList<String> product_name = new ArrayList<>();
        final ArrayList<String> product_value = new ArrayList<>();
        final ArrayList<String> product_units = new ArrayList<>();
        final ArrayList<String> product_add_date = new ArrayList<>();
        final ArrayList<String> product_update_date = new ArrayList<>();
        final ArrayList<String> product_gtin = new ArrayList<>();
        final ArrayList<String> product_buying_price = new ArrayList<>();

        inventoryDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //clear ArrayList on addValueEventListener
                product_id.clear();
                product_name.clear();
                product_value.clear();
                product_units.clear();
                product_add_date.clear();
                product_update_date.clear();
                product_gtin.clear();
                product_buying_price.clear();

                while((iterator.hasNext())){

                    Product_Inventory_Details value = iterator.next().getValue(Product_Inventory_Details.class);
                    product_id.add(value.getProduct_id());
                    product_name.add(value.getProduct_name());
                    product_value.add(value.getProduct_value());
                    product_units.add(value.getProduct_units());
                    product_add_date.add(value.getProduct_add_date());
                    product_update_date.add(value.getProduct_update_date());
                    product_gtin.add(value.getProduct_gtin());
                    product_buying_price.add(value.getProduct_buying_price());

                    ((Product_Edit_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Product_Edit_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, this));
    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Inventory");

    }


    //Choose menu file item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // home button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }



        return super.onOptionsItemSelected(item);
    }




}
