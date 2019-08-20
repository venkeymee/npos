package com.bytecodecomp.npos.Activities.Supplier;

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
import com.bytecodecomp.npos.Adapters.Supplier_Adapter;
import com.bytecodecomp.npos.Data_Models.Supplier_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class SupplierActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference supplierDatabaseReference = AppController.supplierDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        initToolbar();
        get_suppliers();

    }

    //get all suppliers
    public void get_suppliers(){

        final ArrayList<String> supplier_id = new ArrayList<>();
        final ArrayList<String> supplier_name = new ArrayList<>();
        final ArrayList<String> supplier_email = new ArrayList<>();
        final ArrayList<String> supplier_debit = new ArrayList<>();
        final ArrayList<String> supplier_business = new ArrayList<>();
        final ArrayList<String> supplier_phone_number = new ArrayList<>();
        final ArrayList<String> supplier_update_date = new ArrayList<>();
        final ArrayList<String> supplier_add_date = new ArrayList<>();

        supplierDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList on addValueEventListener
                supplier_id.clear();
                supplier_name.clear();
                supplier_email.clear();
                supplier_debit.clear();
                supplier_business.clear();
                supplier_phone_number.clear();
                supplier_update_date.clear();
                supplier_add_date.clear();

                while((iterator.hasNext())){
                    Supplier_Model value = iterator.next().getValue(Supplier_Model.class);
                    supplier_id.add(value.getSupplier_id());
                    supplier_name.add(value.getSupplier_name());
                    supplier_email.add(value.getSupplier_email());
                    supplier_debit.add(value.getSupplier_debit());
                    supplier_business.add(value.getSupplier_business());
                    supplier_phone_number.add(value.getSupplier_phone_number());
                    supplier_update_date.add(value.getSupplier_update_date());
                    supplier_add_date.add(value.getSupplier_add_date());
                    ((Supplier_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Supplier_Adapter(supplier_id, supplier_name, supplier_email, supplier_debit, supplier_business, supplier_phone_number, supplier_update_date, supplier_add_date, this));


    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Supplier");

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

            // home create
            case R.id.action_create:
                Intent intent = new Intent(this, AddSupplierActivity.class);
                startActivity(intent);
                return true;

        }

        return true;

    }


}
