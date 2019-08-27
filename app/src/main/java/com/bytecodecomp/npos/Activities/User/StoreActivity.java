package com.bytecodecomp.npos.Activities.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Data_Models.Store_Model;
import com.bytecodecomp.npos.Plugins.Printer.MainActivity;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.HashMap;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {

    EditText et_store_name, et_store_location, et_store_address, et_store_contacts;
    Button btn_submit, btn_printer;

    private RadioGroup radioStoreType;
    private RadioButton radioGroup;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference storeDatabaseReference = AppController.storeDatabaseReference;

    String TAG = "StoreActivity";

    //boolean to check if there are existing values
    Boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        initToolbar();

        et_store_name = (EditText) findViewById(R.id.et_store_name);
        et_store_location = (EditText) findViewById(R.id.et_store_location);
        et_store_address = (EditText) findViewById(R.id.et_store_address);
        et_store_contacts = (EditText) findViewById(R.id.et_store_contacts);

        radioStoreType =(RadioGroup)findViewById(R.id.radioGroup);

        //Update / Create store details
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioStoreType.getCheckedRadioButtonId();
                radioGroup =(RadioButton)findViewById(selectedId);

                //check if there are empty values
                if (et_store_name.getText().toString().length() > 1 && et_store_location.getText().toString().length() > 1 && et_store_address.getText().toString().length() > 1 && et_store_contacts.getText().toString().length() > 1 ){

                    //if there is existing data update store
                    if (value == true){

                        do_update_store_details(et_store_name.getText().toString(), et_store_address.getText().toString(), et_store_location.getText().toString(), radioGroup.getText().toString(), et_store_contacts.getText().toString());

                    }

                    //if there is no store values, create one
                    if (value == false){

                        do_add_store_details(et_store_name.getText().toString(), et_store_address.getText().toString(), et_store_location.getText().toString(), radioGroup.getText().toString(), et_store_contacts.getText().toString());

                    }

                }

                //if values are empty alert the user.
                if (et_store_name.getText().toString().length() < 1 && et_store_location.getText().toString().length() < 1 && et_store_address.getText().toString().length() < 1 && et_store_contacts.getText().toString().length() < 1 ){

                    Toasty.warning(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT, true).show();

                }

            }
        });


        btn_printer = (Button) findViewById(R.id.btn_printer);
        btn_printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StoreActivity.this, MainActivity.class);
                startActivity(intent);

            }

    });



        //get store details
        do_populate();

    }


    //get store details
    public void do_populate(){

        // Read from the database
        storeDatabaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Store_Model store_model = dataSnapshot.getValue(Store_Model.class);

                if (!Store_Model.store_name.isEmpty()){

                    et_store_name.setText(store_model.getStore_name() + "");
                    et_store_location.setText(store_model.getStore_location() + "");
                    et_store_address.setText(store_model.getStore_address() + "");
                    et_store_contacts.setText(store_model.getStore_contacts() + "");

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    //create store details
    public void do_add_store_details(String store_name, String store_address, String store_location, String store_print, String store_contacts){

//        Store_Model store_model = new Store_Model( store_name, store_address, store_location, store_print, store_contacts);
//        storeDatabaseReference.child(user.getUid()).setValue(store_model).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//
//
//                    }
//                });

    }

    //update store details
    public void do_update_store_details(String store_name, String store_address, String store_location, String store_print, String store_contacts){

        Map<String, Object> storeUpdates = new HashMap<>();
        storeUpdates.put("store_name", store_name);
        storeUpdates.put("store_address", store_address);
        storeUpdates.put("store_location", store_location);
        storeUpdates.put("store_print", store_print);
        storeUpdates.put("store_contacts", store_contacts);

        storeDatabaseReference.child(user.getUid()).updateChildren(storeUpdates);

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Settings");

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
