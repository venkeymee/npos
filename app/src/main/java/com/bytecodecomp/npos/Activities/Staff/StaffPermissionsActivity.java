package com.bytecodecomp.npos.Activities.Staff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Data_Models.User_Permissions;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.Iterator;

public class StaffPermissionsActivity extends AppCompatActivity {

    CheckBox ch_user_login;CheckBox ch_user_change_password;CheckBox ch_user_change_email;CheckBox ch_user_change_profile;CheckBox ch_user_access_reports;CheckBox ch_user_dashboard;
    CheckBox ch_user_access_inventory;CheckBox ch_user_add_new_product;CheckBox ch_user_import_product;CheckBox ch_user_export_product;CheckBox ch_user_edit_product;CheckBox ch_user_print_label;
    CheckBox ch_user_restock;CheckBox ch_user_access_purchases;CheckBox ch_user_create_purchases;CheckBox ch_user_access_sales;CheckBox ch_user_view_sales;CheckBox ch_user_make_sales;
    CheckBox ch_user_access_cart;CheckBox ch_user_cash_checkout;CheckBox ch_user_paypal_checkout;CheckBox ch_user_credit_checkout;CheckBox ch_user_bank_checkout;CheckBox ch_user_access_customers;
    CheckBox ch_user_view_customers;CheckBox ch_user_create_customers;CheckBox ch_user_edit_customers;CheckBox ch_user_access_staff;CheckBox ch_user_view_staff;CheckBox ch_user_create_staff;
    CheckBox ch_user_set_staff_password;CheckBox ch_user_access_assets;CheckBox ch_user_view_assets;CheckBox ch_user_create_assets;CheckBox ch_user_edit_assets;CheckBox ch_user_access_exp_type;
    CheckBox ch_user_view_exp_type;CheckBox ch_user_add_exp_type;CheckBox ch_user_access_exp;CheckBox ch_user_create_exp;

    User_Permissions  user_permissions = new User_Permissions();
    LinearLayout lyt_permissions;

    Button btn_save;

    String staff_id;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference permissionsDatabaseReference = AppController.permissionsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_permissions);


        Bundle bundle = getIntent().getExtras();
        staff_id = bundle.getString("staff_id");
        Log.e("staff_id...", staff_id + " ");

        initToolbar();

        lyt_permissions = (LinearLayout) findViewById(R.id.lyt_permissions);

        load_staff_permissions(staff_id);

        ch_user_login = (CheckBox) findViewById(R.id.ch_user_login);
        ch_user_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_login(isChecked);

            }
        });

        ch_user_change_password = (CheckBox) findViewById(R.id.ch_user_change_password);
        ch_user_change_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_change_password(isChecked);

            }
        });

        ch_user_change_email = (CheckBox) findViewById(R.id.ch_user_change_email);
        ch_user_change_email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_change_email(isChecked);

            }
        });

        ch_user_change_profile = (CheckBox) findViewById(R.id.ch_user_change_profile);
        ch_user_change_profile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_change_profile(isChecked);

            }
        });

        ch_user_access_reports = (CheckBox) findViewById(R.id.ch_user_access_reports);
        ch_user_access_reports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_reports(isChecked);

            }
        });

        ch_user_dashboard = (CheckBox) findViewById(R.id.ch_user_dashboard);
        ch_user_dashboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_dashboard(isChecked);

            }
        });

        ch_user_access_inventory = (CheckBox) findViewById(R.id.ch_user_access_inventory);
        ch_user_access_inventory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_inventory(isChecked);

            }
        });

        ch_user_add_new_product = (CheckBox) findViewById(R.id.ch_user_add_new_product);
        ch_user_add_new_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_add_new_product(isChecked);

            }
        });

        ch_user_import_product = (CheckBox) findViewById(R.id.ch_user_import_product);
        ch_user_import_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_import_product(isChecked);

            }
        });

        ch_user_export_product = (CheckBox) findViewById(R.id.ch_user_export_product);
        ch_user_export_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_export_product(isChecked);

            }
        });


        ch_user_edit_product = (CheckBox) findViewById(R.id.ch_user_edit_product);
        ch_user_edit_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_edit_product(isChecked);

            }
        });

        ch_user_print_label = (CheckBox) findViewById(R.id.ch_user_print_label);
        ch_user_print_label.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_print_label(isChecked);

            }
        });

        ch_user_restock = (CheckBox) findViewById(R.id.ch_user_restock);
        ch_user_restock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_restock(isChecked);

            }
        });

        ch_user_access_purchases = (CheckBox) findViewById(R.id.ch_user_access_purchases);
        ch_user_access_purchases.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_purchases(isChecked);

            }
        });

        ch_user_create_purchases = (CheckBox) findViewById(R.id.ch_user_create_purchases);
        ch_user_create_purchases.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_create_purchases(isChecked);

            }
        });

        ch_user_access_sales = (CheckBox) findViewById(R.id.ch_user_access_sales);
        ch_user_access_sales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_sales(isChecked);

            }
        });

        ch_user_view_sales = (CheckBox) findViewById(R.id.ch_user_view_sales);
        ch_user_view_sales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_view_sales(isChecked);

            }
        });

        ch_user_make_sales = (CheckBox) findViewById(R.id.ch_user_make_sales);
        ch_user_make_sales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_make_sales(isChecked);

            }
        });

        ch_user_access_cart = (CheckBox) findViewById(R.id.ch_user_access_cart);
        ch_user_access_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_cart(isChecked);

            }
        });

        ch_user_cash_checkout = (CheckBox) findViewById(R.id.ch_user_cash_checkout);
        ch_user_cash_checkout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_cash_checkout(isChecked);

            }
        });

        ch_user_paypal_checkout = (CheckBox) findViewById(R.id.ch_user_paypal_checkout);
        ch_user_paypal_checkout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_paypal_checkout(isChecked);

            }
        });

        ch_user_credit_checkout = (CheckBox) findViewById(R.id.ch_user_credit_checkout);
        ch_user_credit_checkout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_credit_checkout(isChecked);

            }
        });

        ch_user_bank_checkout = (CheckBox) findViewById(R.id.ch_user_bank_checkout);
        ch_user_bank_checkout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_bank_checkout(isChecked);

            }
        });

        ch_user_access_customers = (CheckBox) findViewById(R.id.ch_user_access_customers);
        ch_user_access_customers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_customers(isChecked);

            }
        });

        ch_user_view_customers = (CheckBox) findViewById(R.id.ch_user_view_customers);
        ch_user_view_customers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_view_customers(isChecked);

            }
        });

        ch_user_create_customers = (CheckBox) findViewById(R.id.ch_user_create_customers);
        ch_user_create_customers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_create_customers(isChecked);

            }
        });

        ch_user_edit_customers = (CheckBox) findViewById(R.id.ch_user_edit_customers);
        ch_user_edit_customers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_edit_customers(isChecked);

            }
        });

        ch_user_access_staff = (CheckBox) findViewById(R.id.ch_user_access_staff);
        ch_user_access_staff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_staff(isChecked);

            }
        });

        ch_user_view_staff = (CheckBox) findViewById(R.id.ch_user_view_staff);
        ch_user_view_staff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_view_staff(isChecked);

            }
        });

        ch_user_create_staff = (CheckBox) findViewById(R.id.ch_user_create_staff);
        ch_user_create_staff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_create_staff(isChecked);

            }
        });

        ch_user_set_staff_password = (CheckBox) findViewById(R.id.ch_user_set_staff_password);
        ch_user_set_staff_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_set_staff_password(isChecked);

            }
        });

        ch_user_access_assets = (CheckBox) findViewById(R.id.ch_user_access_assets);
        ch_user_access_assets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_assets(isChecked);

            }
        });

        ch_user_view_assets = (CheckBox) findViewById(R.id.ch_user_view_assets);
        ch_user_view_assets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_view_assets(isChecked);

            }
        });

        ch_user_edit_assets = (CheckBox) findViewById(R.id.ch_user_edit_assets);
        ch_user_edit_assets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_edit_assets(isChecked);

            }
        });

        ch_user_access_exp_type = (CheckBox) findViewById(R.id.ch_user_access_exp_type);
        ch_user_access_exp_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_exp_type(isChecked);

            }
        });

        ch_user_view_exp_type = (CheckBox) findViewById(R.id.ch_user_view_exp_type);
        ch_user_view_exp_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_view_exp_type(isChecked);

            }
        });

        ch_user_add_exp_type = (CheckBox) findViewById(R.id.ch_user_add_exp_type);
        ch_user_add_exp_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_add_exp_type(isChecked);

            }
        });

        ch_user_access_exp = (CheckBox) findViewById(R.id.ch_user_access_exp);
        ch_user_access_exp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_access_exp(isChecked);

            }
        });

        ch_user_create_exp = (CheckBox) findViewById(R.id.ch_user_create_exp);
        ch_user_create_exp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_create_exp(isChecked);

            }
        });



        ch_user_create_assets = (CheckBox) findViewById(R.id.ch_user_create_assets);
        ch_user_create_assets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                user_permissions.setUser_create_assets(isChecked);

            }
        });

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_permisions();

            }
        });

    }


    public void save_permisions(){

        permissionsDatabaseReference.child(user.getUid()).child(staff_id).setValue(user_permissions);
        Toasty.success(StaffPermissionsActivity.this, "Permissions Saved").show();
        finish();

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Staff Permissions");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            //  home button
            case android.R.id.home:
                finish();
                return true;

        }

        return true;

    }


    public void load_staff_permissions(final String staff_id){

        permissionsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).child(staff_id).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while((iterator.hasNext())){

                    Log.e("user_permission..", String.valueOf(iterator.next().getValue()));
                    User_Permissions user_permission = iterator.next().getValue(User_Permissions.class);
                    lyt_permissions.setVisibility(View.VISIBLE);
                    ch_user_login.setChecked(user_permission.isUser_login());
                    ch_user_change_password.setChecked(user_permission.isUser_change_password());
                    ch_user_change_email.setChecked(user_permission.isUser_change_email());
                    ch_user_change_profile.setChecked(user_permission.isUser_change_profile());
                    ch_user_access_reports.setChecked(user_permission.isUser_access_reports());
                    ch_user_dashboard.setChecked(user_permission.isUser_dashboard());
                    ch_user_access_inventory.setChecked(user_permission.isUser_access_inventory());
                    ch_user_add_new_product.setChecked(user_permission.isUser_add_new_product());
                    ch_user_import_product.setChecked(user_permission.isUser_import_product());
                    ch_user_export_product.setChecked(user_permission.isUser_export_product());
                    ch_user_edit_product.setChecked(user_permission.isUser_edit_product());
                    ch_user_print_label.setChecked(user_permission.isUser_print_label());
                    ch_user_restock.setChecked(user_permission.isUser_restock());
                    ch_user_access_purchases.setChecked(user_permission.isUser_access_purchases());
                    ch_user_create_purchases.setChecked(user_permission.isUser_create_purchases());
                    ch_user_access_sales.setChecked(user_permission.isUser_access_sales());
                    ch_user_view_sales.setChecked(user_permission.isUser_view_sales());
                    ch_user_make_sales.setChecked(user_permission.isUser_make_sales());
                    ch_user_access_cart.setChecked(user_permission.isUser_access_cart());
                    ch_user_cash_checkout.setChecked(user_permission.isUser_cash_checkout());
                    ch_user_paypal_checkout.setChecked(user_permission.isUser_paypal_checkout());
                    ch_user_credit_checkout.setChecked(user_permission.isUser_credit_checkout());
                    ch_user_bank_checkout.setChecked(user_permission.isUser_bank_checkout());
                    ch_user_access_customers.setChecked(user_permission.isUser_access_customers());
                    ch_user_view_customers.setChecked(user_permission.isUser_view_customers());
                    ch_user_create_customers.setChecked(user_permission.isUser_create_customers());
                    ch_user_edit_customers.setChecked(user_permission.isUser_edit_customers());
                    ch_user_access_staff.setChecked(user_permission.isUser_access_staff());
                    ch_user_view_staff.setChecked(user_permission.isUser_view_staff());
                    ch_user_create_staff.setChecked(user_permission.isUser_create_staff());
                    ch_user_set_staff_password.setChecked(user_permission.isUser_set_staff_password());
                    ch_user_access_assets.setChecked(user_permission.isUser_access_assets());
                    ch_user_view_assets.setChecked(user_permission.isUser_view_assets());
                    ch_user_edit_assets.setChecked(user_permission.isUser_edit_assets());
                    ch_user_access_exp_type.setChecked(user_permission.isUser_access_exp_type());
                    ch_user_view_exp_type.setChecked(user_permission.isUser_view_exp_type());
                    ch_user_add_exp_type.setChecked(user_permission.isUser_add_exp_type());
                    ch_user_access_exp.setChecked(user_permission.isUser_access_exp());
                    ch_user_access_exp.setChecked(user_permission.isUser_create_exp());
                    ch_user_create_assets.setChecked(user_permission.isUser_create_assets());

                }


                lyt_permissions.setVisibility(View.VISIBLE);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
