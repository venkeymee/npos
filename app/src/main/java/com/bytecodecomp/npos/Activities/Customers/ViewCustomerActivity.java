package com.bytecodecomp.npos.Activities.Customers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.bytecodecomp.npos.Adapters.Sales_Adapter;
import com.bytecodecomp.npos.Data_Models.Sales_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class ViewCustomerActivity extends AppCompatActivity {

    TextView txt_customer_name, txt_customer_email, txt_customer_credit, txt_customer_limit;
    LinearLayout lyt_call, lyt_sms, lyt_email;

    String customer_id, customer_names, customer_email, customer_credit, customer_phone_number;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;

    //customer sales credit
    double credit_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        Bundle bundle = getIntent().getExtras();
        customer_id = bundle.getString("customer_id");
        customer_names = bundle.getString("customer_name");
        customer_email = bundle.getString("customer_email");
        customer_credit = bundle.getString("customer_credit");
        customer_phone_number = bundle.getString("customer_phone_number");


        txt_customer_credit = (TextView) findViewById(R.id.txt_customer_credit);

        txt_customer_limit = (TextView) findViewById(R.id.txt_customer_limit);
        txt_customer_limit.setText("Credit limit : " + customer_credit);


        txt_customer_name = (TextView) findViewById(R.id.txt_customer_name);
        txt_customer_name.setText(customer_names);

        txt_customer_email = (TextView) findViewById(R.id.txt_customer_email);
        txt_customer_email.setText(customer_email);


        //make a call
        lyt_call = (LinearLayout) findViewById(R.id.lyt_call);
        lyt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customer_phone_number, null)));


            }
        });


        //send an sms
        lyt_sms = (LinearLayout) findViewById(R.id.lyt_sms);
        lyt_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = customer_phone_number;  // The number on which you want to send SMS
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));


            }
        });

        //send email
        lyt_email = (LinearLayout) findViewById(R.id.lyt_email);
        lyt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + customer_email));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));

            }
        });


        initToolbar();
        read_sales();

    }


    //get sales
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

                //Clear ArrayList before a new value listener
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

                    if (value.getCustomer_name().equals(customer_names)){

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

                        if (value.getPayment_method().equals("credit")){

                            credit_value = credit_value + Double.parseDouble( value.getPayment_amount());
                            txt_customer_credit.setText("Current credit : " + credit_value);

                        }

                        ((Sales_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Sales_Adapter(payment_date, payment_id, payment_money_id, payment_method, items, user_id, user_name, payment_amount, payment_user_staff, customer_name, this));
    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Customer");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {

            //  home button
            case android.R.id.home:
                finish();
                return true;

            //  home button
            case R.id.action_edit:
                Intent intent = new Intent(this, EditCustomersActivity.class);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("customer_name", customer_names);
                intent.putExtra("customer_email", customer_email);
                intent.putExtra("customer_credit", customer_credit);
                intent.putExtra("customer_phone_number", customer_phone_number);
                startActivity(intent);
                return true;

        }

        return true;

    }

}
