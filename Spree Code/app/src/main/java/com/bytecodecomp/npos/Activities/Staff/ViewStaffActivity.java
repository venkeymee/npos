package com.bytecodecomp.npos.Activities.Staff;

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

import com.android.volley.toolbox.ImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Sales_Adapter;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Sales_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.CustomNetworkImageView;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class ViewStaffActivity extends AppCompatActivity {

    TextView txt_staff_name, txt_staff_email, txt_staff_sales, txt_staff_commission;
    LinearLayout lyt_call, lyt_sms, lyt_email;
    CustomNetworkImageView customNetworkImageView;


    String staff_id, staff_names, staff_email, staff_phone, staff_add_date, staff_device_id, staff_profile_photo, staff_docs, staff_password, staff_commision;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;

//    ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader()


    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    double total_sales = 0;
    double total_commision = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_staff);
        initToolbar();

        Bundle bundle = getIntent().getExtras();
        staff_id = bundle.getString("staff_id");
        staff_names = bundle.getString("staff_name");
        staff_email = bundle.getString("staff_email");
        staff_phone = bundle.getString("staff_phone");
        staff_add_date = bundle.getString("staff_add_date");
        staff_device_id = bundle.getString("staff_device_id");
        staff_profile_photo = bundle.getString("staff_profile_photo");
        staff_docs = bundle.getString("staff_docs");
        staff_password = bundle.getString("staff_password");
        staff_commision = bundle.getString("staff_commision");

//        customNetworkImageView = (CustomNetworkImageView) findViewById(R.id.customNetworkImageView);
//        customNetworkImageView.setImageUrl(staff_profile_photo, mImageLoader);
//


        txt_staff_sales = (TextView) findViewById(R.id.txt_staff_sales);
        txt_staff_commission = (TextView) findViewById(R.id.txt_staff_commission);


        //set staff name
        txt_staff_name = (TextView) findViewById(R.id.txt_staff_name);
        txt_staff_name.setText(staff_names);

        //set staff email
        txt_staff_email = (TextView) findViewById(R.id.txt_staff_email);
        txt_staff_email.setText(staff_email);


        //make a call
        lyt_call = (LinearLayout) findViewById(R.id.lyt_call);
        lyt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", staff_phone, null)));


            }
        });


        //send an sms
        lyt_sms = (LinearLayout) findViewById(R.id.lyt_sms);
        lyt_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = staff_phone;  // The number on which you want to send SMS
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));


            }
        });

        //send email
        lyt_email = (LinearLayout) findViewById(R.id.lyt_email);
        lyt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + staff_email));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));

            }
        });

        //call get all staff sales report
        read_sales();


    }

    //get all staff sales report
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

                //clear ArrayList on addValueEventListener
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
                total_sales = 0;
                total_commision = 0;


                while((iterator.hasNext())){
                    Sales_Model value = iterator.next().getValue(Sales_Model.class);

                    if (value.getPayment_user_staff().equals(staff_names)){

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

                        double payments = Double.parseDouble(value.getPayment_amount());

                        total_sales = total_sales +  payments;

                        if (!staff_commision.equals("null")){
                            total_commision = total_sales / Integer.parseInt(staff_commision);
                        }


                        txt_staff_commission.setText("Commission " + App_Settings.CURRENCY_TYPE + " " + total_commision);
                        txt_staff_sales.setText("Sales " + App_Settings.CURRENCY_TYPE + " " + total_sales);

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
            getSupportActionBar().setTitle("View Staff");

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_staff, menu);
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

            case R.id.action_set_perms:

                Intent intents = new Intent(this, StaffPermissionsActivity.class);
                intents.putExtra("staff_id", staff_id);
                startActivity(intents);

                return true;

            //  create button
            case R.id.action_edit:

                Intent intent = new Intent(this, EditStaffActivity.class);
                intent.putExtra("staff_id", staff_id);
                intent.putExtra("staff_name", staff_names);
                intent.putExtra("staff_email", staff_email);
                intent.putExtra("staff_phone", staff_phone);
                intent.putExtra("staff_add_date", staff_add_date);
                intent.putExtra("staff_device_id", staff_device_id);
                intent.putExtra("staff_profile_photo", staff_profile_photo);
                intent.putExtra("staff_docs", staff_docs);
                intent.putExtra("staff_password", staff_password);
                intent.putExtra("staff_commision", staff_commision);
                startActivity(intent);

                return true;

        }

        return true;

    }


}
