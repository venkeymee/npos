package com.bytecodecomp.npos.Activities.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.bytecodecomp.npos.Activities.Admin.AdminActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.CircularImageView;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout lyt_logout, lyt_delete, lyt_change_email, lyt_change_password, lyt_store, lyt_profile, lyt_admin;
    CircularImageView circularImageView;

    TextView txt_username, txt_email;

    //Firebase user and database reference
    private FirebaseAuth auth;
    DatabaseReference cartDatabaseReference  = AppController.cartDatabaseReference,
            inventoryDatabaseReference = AppController.inventoryDatabaseReference,
            salesDatabaseReference = AppController.salesDatabaseReference,
            expenseDatabaseReference = AppController.expenseDatabaseReference,
            expenseCatDatabaseReference = AppController.expenseCatDatabaseReference,
            customerDatabaseReference = AppController.customerDatabaseReference,
            supplierDatabaseReference = AppController.supplierDatabaseReference,
            purchasesDatabaseReference = AppController.purchasesDatabaseReference,
            registerDatabaseReference = AppController.registerDatabaseReference,
            assetsDatabaseReference = AppController.assetsDatabaseReference,
            usersDatabaseReference = AppController.usersDatabaseReference,
            storeDatabaseReference = AppController.storeDatabaseReference;

    //get current user
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar();
        auth = FirebaseAuth.getInstance();


        user_id = user.getUid();

        //set user name
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_username.setText(user.getDisplayName());

        //set user email
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setText(user.getEmail());

        circularImageView = (CircularImageView) findViewById(R.id.circularImageView);
        Log.e("getPhotoUrl..", String.valueOf(user.getPhotoUrl()) + FirebaseInstanceId.getInstance().getToken());
        Glide.with(getApplicationContext()).load(String.valueOf(user.getPhotoUrl()) + FirebaseInstanceId.getInstance().getToken()).into(circularImageView);

        //System admin
        lyt_admin = (LinearLayout) findViewById(R.id.lyt_admin);

        lyt_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, AdminActivity.class);
                startActivity(intent);

            }
        });


        //layout store
        lyt_store = (LinearLayout) findViewById(R.id.lyt_store);
        lyt_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, StoreActivity.class);
                startActivity(intent);

            }
        });


        //layout profile
        lyt_profile = (LinearLayout) findViewById(R.id.lyt_profile);
        lyt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, AccountSettingsActivity.class);
                startActivity(intent);

            }
        });


        //layout change password
        lyt_change_password = (LinearLayout) findViewById(R.id.lyt_change_password);
        lyt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);

            }
        });

        //layout change email
        lyt_change_email = (LinearLayout) findViewById(R.id.lyt_change_email);
        lyt_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ChangeEmailActivity.class);
                startActivity(intent);

            }
        });

        //layout delete account
        lyt_delete = (LinearLayout) findViewById(R.id.lyt_delete);
        lyt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Delete Account")
                        .setMessage("Are you sure you want to remove Account.")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();

                            }
                        })
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                                remove_account();

                            }
                        }).show();

            }
        });

        //layout logout
        lyt_logout = (LinearLayout) findViewById(R.id.lyt_logout);
        lyt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

            }
        });


        if (!App_Settings.admin_uid.equals(auth.getUid())){

            lyt_admin.setVisibility(View.GONE);
            lyt_delete.setVisibility(View.GONE);

        }

//        adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
//                .build();
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdClosed() {
//
////                Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
////                Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
////                Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });
//        adView.loadAd(adRequest);

    }


    //sign out method
    public void signOut() {
        auth.signOut();

        startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
        finish();

    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //remove user account
    public void remove_account(){

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


//                                cartDatabaseReference.child(user_id).removeValue();
//                                inventoryDatabaseReference.child(user_id).removeValue();
//                                customerDatabaseReference.child(user_id).removeValue();
//                                supplierDatabaseReference.child(user_id).removeValue();
//                                salesDatabaseReference.child(user_id).removeValue();
//                                purchasesDatabaseReference.child(user_id).removeValue();
//                                expenseDatabaseReference.child(user_id).removeValue();
//                                expenseCatDatabaseReference.child(user_id).removeValue();
//                                assetsDatabaseReference.child(user_id).removeValue();
//                                registerDatabaseReference.child(user_id).removeValue();
//                                usersDatabaseReference.child(user_id).removeValue();
//                                storeDatabaseReference.child(user_id).removeValue();


                                Toasty.warning(getApplicationContext(), "Your profile is deleted:( Create a new account", Toast.LENGTH_SHORT, true).show();
                                startActivity(new Intent(ProfileActivity.this, SignUpActivity.class));
                                finish();

                            } else {
                                Log.e("task",  task.getResult().toString());
                                Toasty.warning(getApplicationContext(), "Failed to delete your account!", Toast.LENGTH_SHORT, true).show();

                            }

                        }
                    });
        }

    }


}
