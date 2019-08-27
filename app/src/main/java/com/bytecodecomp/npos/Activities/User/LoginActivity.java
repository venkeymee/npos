package com.bytecodecomp.npos.Activities.User;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Activities.Admin.Business.CreateStoreActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Device_Model;
import com.bytecodecomp.npos.Data_Models.Staff_Model;
import com.bytecodecomp.npos.Data_Models.Store_Model;
import com.bytecodecomp.npos.MainActivity;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.Plugins.crypto.decrypt;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Permissions_Control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    //Input field for password and email
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;

    //Progressbar
    private ProgressBar progressBar;

    //Buttons
    private Button btnSignup, btnLogin, btnReset;

    private RewardedVideoAd mRewardedVideoAd;
    DatabaseReference storeDatabaseReference = AppController.storeDatabaseReference;
    DatabaseReference deviceDatabaseReference = AppController.deviceDatabaseReference;
    DatabaseReference usersDatabaseReference = AppController.usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            //If phone is authenticated to store
            check_store_details();

        }


        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if registration is activated
                if (App_Settings.allow_user_registration == 1 ){

                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

                }

                else {

                    Toasty.error(LoginActivity.this, "Registration deactivated.").show();

                }


            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });





        //Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {

                    Toasty.warning(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT, true).show();

                    return;
                }

                if (TextUtils.isEmpty(password)) {

                    Toasty.warning(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT, true).show();

                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {

                                        Toasty.error(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_SHORT, true).show();
//                                        Log.e("Error...", task.getResult().toString());

                                    }
                                }

                                else {

                                    check_store_details();

                                }
                            }
                        });
            }
        });



        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

            @Override
            public void onRewarded(RewardItem rewardItem) {
//                Toast.makeText(LoginActivity.this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
//                        rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
//                Toast.makeText(LoginActivity.this, "onRewardedVideoAdLeftApplication",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
//                Toast.makeText(LoginActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
//                Toast.makeText(LoginActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {

            }


            @Override
            public void onRewardedVideoAdLoaded() {
//                Toast.makeText(LoginActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                loadRewardedVideoAd();
            }

            @Override
            public void onRewardedVideoAdOpened() {
//                Toast.makeText(LoginActivity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
//                Toast.makeText(LoginActivity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }
        });

    }



    //Staff login dialog
    public void dialog_staff_login(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_staff_login);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText edt_email = (EditText) dialog.findViewById(R.id.edt_email);
        edt_email.setText(inputEmail.getText().toString() + "");
        final EditText edt_password = (EditText) dialog.findViewById(R.id.edt_password);


        ((MaterialRippleLayout) dialog.findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_email.getText().toString().length() <= 0 && edt_password.getText().toString().length() <= 0){

                    Toasty.error(LoginActivity.this,"Missing Fields").show();

                }

                else {

                    do_staff_login(edt_email.getText().toString(), edt_password.getText().toString());

                }



            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    //firebase db login
    public void do_staff_login(final String email, final String password){

        final ArrayList<String> staff_uid_list = new ArrayList<>();
        final ArrayList<String> staff_perms_list = new ArrayList<>();

        usersDatabaseReference.child(auth.getUid()).orderByChild("staff_email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();

                Log.e("Count...", size + "");

                if (size == 0){

                    Toasty.warning(LoginActivity.this, "No user with that account found " + email).show();

                }

                else if (size == 1){

                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while((iterator.hasNext())){

                        Staff_Model value = iterator.next().getValue(Staff_Model.class);
                        staff_uid_list.add(value.getStaff_id());
                        staff_perms_list.add(value.getStaff_docs());

                        Log.e("One...", password + " DB..." + decrypt.decryptIt(value.getStaff_password()));

                        if (decrypt.decryptIt(value.getStaff_password()).equals(password)){

                            App_Settings.staff_uid = staff_uid_list.get(0);
                            App_Settings.staff_permissions = staff_perms_list.get(0);
                            Log.e("staff_uid...", App_Settings.staff_uid);
                            Log.e("staff_permissions...", App_Settings.staff_permissions);

                            if (Permissions_Control.check_perms("user_login") == true){

                                Intent intents = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intents);
                                finish();

                            }

                            else {

                                Toasty.error(LoginActivity.this, "Missing login permission").show();

                            }



                        }

                        else {

                            Toasty.warning(LoginActivity.this, "Invalid password for user : " + email).show();

                        }

                    }


                }

                else {

                    Toasty.error(LoginActivity.this, "Duplicate mail for user : " + email).show();

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_video), new AdRequest.Builder().build());

        // showing the ad to user
        showRewardedVideo();
    }

    private void showRewardedVideo() {
        // make sure the ad is loaded completely before showing it
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }


    //check store status
    public void check_store_details(){

        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        final int current_timestamp = Integer.parseInt(timeStamp);


        storeDatabaseReference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            Store_Model store_model = dataSnapshot.getValue(Store_Model.class);

                if (Store_Model.store_name.isEmpty()){

                    Toasty.error(LoginActivity.this, "No store associated with you").show();
                    dialog_create_store();

                }

                else {

                    Log.e("getPackage_expiry...", store_model.getPackage_expiry()+ " - value - " + auth.getUid());

                    if (store_model.getStore_status().equals("active")){

                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date strDate = sdf.parse(store_model.getPackage_expiry());

                            if (!new Date().after(strDate)) {

                                App_Settings.store_name = store_model.getStore_name();
                                App_Settings.store_address = store_model.getStore_address();
                                App_Settings.store_location = store_model.getStore_location();
                                App_Settings.store_print = store_model.getStore_print();
                                App_Settings.store_contacts = store_model.getStore_contacts();
                                App_Settings.package_id = store_model.getPackage_id();
                                App_Settings.package_name = store_model.getPackage_name();
                                App_Settings.package_expiry = store_model.getPackage_expiry();
                                App_Settings.store_status = store_model.getStore_status();
                                App_Settings.store_uid = dataSnapshot.getKey();

                                Log.e("UID...", App_Settings.store_uid);

                                Toasty.success(LoginActivity.this, store_model.getStore_name()).show();

                                check_device();

                            }

                            else {

                                Toasty.error(LoginActivity.this, "Package Expired").show();

                                Intent intent = new Intent(LoginActivity.this, PackageActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                    else {

                        Intent intent = new Intent(LoginActivity.this, PackageActivity.class);
                        startActivity(intent);
                        finish();

                        Toasty.warning(LoginActivity.this, "Your store is not activated").show();

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Store...", "Failed to read value.", error.toException());
            }
        });

    }

    //func to check device is asigned to who
    public void check_device(){

        final ArrayList<String> device_role_list = new ArrayList<>();

        deviceDatabaseReference.child(App_Settings.store_uid).orderByChild("device_serial").equalTo(Build.SERIAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
                Log.e("device_serial...", " " + size);

                if (size == 0){

                    Intent intents = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intents);
                    finish();

                }

                else {

                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while((iterator.hasNext())){

                        Device_Model value = iterator.next().getValue(Device_Model.class);
                        device_role_list.add(value.getDevice_role());
                        App_Settings.device_role = device_role_list.get(0);
                        Log.e("Device_Role...", App_Settings.device_role);

                        confirm_device_role();

                    }


                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    //func to check device role
    public void confirm_device_role(){

        if (App_Settings.device_role.equals("merchant_admin")){

            Toasty.success(LoginActivity.this, "Welcome Back").show();
            Intent intents = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intents);
            finish();

        }


        else if (App_Settings.device_role.equals("staff")){

       Toasty.info(LoginActivity.this, "Session ended, Kindly Login again").show();
       dialog_staff_login();

        }

    }

    //dialog show create store
    public void dialog_create_store() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Create your store");
        alertDialogBuilder.setMessage("You don't have an active store");
        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(LoginActivity.this, CreateStoreActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}