package com.bytecodecomp.npos.Activities.Purchase;

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
import com.bytecodecomp.npos.Adapters.Purchase_Adapter;
import com.bytecodecomp.npos.Data_Models.Purchase_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class PurchaseActivity extends AppCompatActivity {

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference purchasesDatabaseReference = AppController.purchasesDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        initToolbar();
        read_purchase();

    }

    //get purchases
    private void read_purchase(){

        final ArrayList<String> purchase_id = new ArrayList<>();
        final ArrayList<String> purchase_qnty = new ArrayList<>();
        final ArrayList<String> purchase_date = new ArrayList<>();
        final ArrayList<String> purchase_item = new ArrayList<>();
        final ArrayList<String> purchase_buying_price = new ArrayList<>();
        final ArrayList<String> purchase_selling_price = new ArrayList<>();
        final ArrayList<String> purchase_status = new ArrayList<>();
        final ArrayList<String> purchase_supplier = new ArrayList<>();

        purchasesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList on addValueEventListener
                purchase_id.clear();
                purchase_qnty.clear();
                purchase_date.clear();
                purchase_item.clear();
                purchase_buying_price.clear();
                purchase_selling_price.clear();
                purchase_status.clear();
                purchase_supplier.clear();

                while((iterator.hasNext())){
                    Purchase_Model value = iterator.next().getValue(Purchase_Model.class);
                    purchase_id.add(value.getPurchase_id());
                    purchase_qnty.add(value.getPurchase_qnty());
                    purchase_date.add(value.getPurchase_date());
                    purchase_item.add(value.getPurchase_item());
                    purchase_buying_price.add(value.getPurchase_buying_price());
                    purchase_selling_price.add(value.getPurchase_selling_price());
                    purchase_status.add(value.getPurchase_status());
                    purchase_supplier.add(value.getPurchase_supplier());
                    ((Purchase_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Purchase_Adapter(purchase_id, purchase_qnty, purchase_date, purchase_item, purchase_buying_price, purchase_selling_price, purchase_status, purchase_supplier, this));
    }


    //initial toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Purchases");

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
            // Android home
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_create:

                Intent intent = new Intent(this, AddPurchaseActivity.class);
                startActivity(intent);

                return true;

        }

        return true;

    }

}
