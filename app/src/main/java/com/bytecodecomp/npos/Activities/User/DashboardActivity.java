package com.bytecodecomp.npos.Activities.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Activities.Assets.AssetsActivity;
import com.bytecodecomp.npos.Activities.Customers.CustomerActivity;
import com.bytecodecomp.npos.Activities.Devices.DevicesActivity;
import com.bytecodecomp.npos.Activities.Expenses.ExpenseActivity;
import com.bytecodecomp.npos.Activities.Expenses.ViewExpenseTypeActivity;
import com.bytecodecomp.npos.Activities.Inventory.InventoryActivity;
import com.bytecodecomp.npos.Activities.Purchase.PurchaseActivity;
import com.bytecodecomp.npos.Activities.Reports.ReportsActivity;
import com.bytecodecomp.npos.Activities.Sales.SalesActivity;
import com.bytecodecomp.npos.Activities.Staff.StaffActivity;
import com.bytecodecomp.npos.Activities.Supplier.SupplierActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.MainActivity;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Permissions_Control;
import com.bytecodecomp.npos.Utils.Tools;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout card_inventory, card_purchase, card_sales, card_customers, card_exp_type, card_expense,
            card_suppliers, card_staff, card_assets, lyt_reports, card_devices, card_reports, card_notification;

    TextView txt_total_sales, txt_inventory, txt_customer, txt_assets, txt_purchase, txt_users;

    //Firebase user and database reference
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;
    DatabaseReference assetsDatabaseReference = AppController.assetsDatabaseReference;
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;
    DatabaseReference usersDatabaseReference = AppController.usersDatabaseReference;
    DatabaseReference customerDatabaseReference = AppController.customerDatabaseReference;
    DatabaseReference purchasesDatabaseReference = AppController.purchasesDatabaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ValueEventListener listener = AppController.listener;

    int stock_value = 0;

    //    AdView
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initToolbar();


        //adview setuo
//        mAdView = (AdView) findViewById(R.id.adView);
//
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdClosed() {
////                Toast.makeText(DashboardActivity.this, "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
////                Toast.makeText(DashboardActivity.this, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
////                Toast.makeText(DashboardActivity.this, "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });
//        mAdView.loadAd(adRequest);

        txt_total_sales = (TextView) findViewById(R.id.txt_total_sales);

        txt_inventory = (TextView) findViewById(R.id.txt_inventory);
        txt_customer = (TextView) findViewById(R.id.txt_customer);
        txt_assets = (TextView) findViewById(R.id.txt_assets);
        txt_purchase = (TextView) findViewById(R.id.txt_purchase);
        txt_users = (TextView) findViewById(R.id.txt_users);

        do_inventory_database();
        do_assets_database();
        do_sales_database();
        do_staff_database();
        do_customers_database();
        do_purchase_database();

        lyt_reports = (LinearLayout) findViewById(R.id.lyt_reports);

        //assets card
        card_assets = (LinearLayout) findViewById(R.id.card_assets);
        card_assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_assets") == true) {
                    Intent intent = new Intent(DashboardActivity.this, AssetsActivity.class);
                    startActivity(intent);
                }

                else {
                    Toasty.warning(DashboardActivity.this, "Missing Access Assets Permission").show();
                }

            }
        });

        //inventory card
        card_inventory = (LinearLayout) findViewById(R.id.card_inventory);
        card_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_inventory") == true) {
                    Intent intent = new Intent(DashboardActivity.this, InventoryActivity.class);
                    startActivity(intent);
                }

                else {
                    Toasty.warning(DashboardActivity.this, "Missing Access Inventory Permission").show();
                }


            }
        });

        //purchase card
        card_purchase = (LinearLayout) findViewById(R.id.card_purchase);
        card_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_purchases") == true) {
                    Intent intent = new Intent(DashboardActivity.this, PurchaseActivity.class);
                    startActivity(intent);
                }

                else {
                    Toasty.warning(DashboardActivity.this, "Missing Access Purchase Permission").show();
                }

            }
        });


        //sales card
        card_sales = (LinearLayout) findViewById(R.id.card_sales);
        card_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_sales") == true) {
                    Intent intent = new Intent(DashboardActivity.this, SalesActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Sales Permission").show();
                }

            }
        });

        //customers card
        card_customers = (LinearLayout) findViewById(R.id.card_customers);
        card_customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_customers") == true) {
                    Intent intent = new Intent(DashboardActivity.this, CustomerActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Customer Permission").show();
                }

            }
        });


        //suppliers card
        card_suppliers = (LinearLayout) findViewById(R.id.card_suppliers);
        card_suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_inventory") == true) {
                    Intent intent = new Intent(DashboardActivity.this, SupplierActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Expense Type Permission").show();
                }

            }
        });


        //staff card
        card_staff = (LinearLayout) findViewById(R.id.card_staff);
        card_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_staff") == true) {
                    Intent intent = new Intent(DashboardActivity.this, StaffActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Staff Permission").show();
                }


            }
        });

        //exp type card
        card_exp_type = (LinearLayout) findViewById(R.id.card_exp_type);
        card_exp_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_exp_type") == true) {
                    Intent intent = new Intent(DashboardActivity.this, ViewExpenseTypeActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Expense Type Permission").show();
                }

            }
        });

        //exp card
        card_expense = (LinearLayout) findViewById(R.id.card_expense);
        card_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_exp") == true) {
                    Intent intent = new Intent(DashboardActivity.this, ExpenseActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Expense Permission").show();
                }

            }
        });

        //device card
        card_devices = (LinearLayout) findViewById(R.id.card_devices);
        card_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_dashboard") == true) {
                    Intent intent = new Intent(DashboardActivity.this, DevicesActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Device Permission").show();
                }

            }
        });

        //reports card
        card_reports = (LinearLayout) findViewById(R.id.card_reports);
        card_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                if (Permissions_Control.check_perms("user_access_reports") == true) {
                    Intent intent = new Intent(DashboardActivity.this, ReportsActivity.class);
                    startActivity(intent);
                }

                else
                {
                    Toasty.warning(DashboardActivity.this, "Missing Access Reports Permission").show();
                }

            }
        });


        //reports card
        card_notification = (LinearLayout) findViewById(R.id.card_notification);
        card_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for user permissions
                Toasty.warning(DashboardActivity.this, "Coming soon").show();

            }
        });


    }


    //get inventory count
    public void do_inventory_database(){

        listener = inventoryDatabaseReference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        txt_inventory.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Unit/s" );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }


    //get sales count
    public void do_sales_database(){

        listener = salesDatabaseReference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        txt_total_sales.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Sale/s" );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }


    //get staff count
    public void do_staff_database(){

        listener = usersDatabaseReference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        txt_users.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Staff/s"  );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }


    //get assets count
    public void do_assets_database(){

        listener = assetsDatabaseReference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        txt_assets.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Asset/s"  );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }


    //get customers count
    public void do_customers_database(){

        listener = customerDatabaseReference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        txt_customer.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Customer/s"  );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }


    //get purchase count
    public void do_purchase_database(){

        listener = purchasesDatabaseReference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        txt_purchase.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Purchase/s"  );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dashboard");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {

            // home
            case android.R.id.home:
                finish();
                return true;

                //profile
            case R.id.action_profile:


                if (App_Settings.device_role.equals("merchant_admin")){

                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);

                }

                else {

                    Toasty.warning(this, "Only Merchant Admin can Access").show();

                }

                return true;

        }

        return true;

    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }



}
