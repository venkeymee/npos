package com.bytecodecomp.npos.Activities.Supplier;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Iterator;

public class ViewSupplierActivity extends AppCompatActivity {

    TextView txt_supplier_name, txt_supplier_email;
    LinearLayout lyt_call, lyt_sms, lyt_email;

    String supplier_id, supplier_name, supplier_email, supplier_debit, supplier_business, supplier_phone_number, supplier_add_date;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference purchasesDatabaseReference = AppController.purchasesDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_supplier);

        initToolbar();

        Bundle bundle = getIntent().getExtras();
        supplier_id = bundle.getString("supplier_id");
        supplier_name = bundle.getString("supplier_name");
        supplier_email = bundle.getString("supplier_email");
        supplier_debit = bundle.getString("supplier_debit");
        supplier_business = bundle.getString("supplier_business");
        supplier_phone_number = bundle.getString("supplier_phone_number");
        supplier_add_date = bundle.getString("supplier_add_date");

        //set supplier name
        txt_supplier_name = (TextView) findViewById(R.id.txt_supplier_name);
        txt_supplier_name.setText(supplier_name);

        //set supplier email
        txt_supplier_email = (TextView) findViewById(R.id.txt_supplier_email);
        txt_supplier_email.setText(supplier_email);

        //make a call
        lyt_call = (LinearLayout) findViewById(R.id.lyt_call);
        lyt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", supplier_phone_number, null)));


            }
        });


        //send an sms
        lyt_sms = (LinearLayout) findViewById(R.id.lyt_sms);
        lyt_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = supplier_phone_number;  // The number on which you want to send SMS
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));


            }
        });

        //send email
        lyt_email = (LinearLayout) findViewById(R.id.lyt_email);
        lyt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + supplier_email));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));

            }
        });

        //call get recent purchases
        read_purchase();


    }


    //get recent purchases
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
                Iterator<DataSnapshot> iterators = snapshotIterator.iterator();

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

                    if (value.getPurchase_supplier().equals(supplier_name)){

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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Purchase_Adapter(purchase_id, purchase_qnty, purchase_date, purchase_item, purchase_buying_price, purchase_selling_price, purchase_status, purchase_supplier, this));
    }



    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Supplier");

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
