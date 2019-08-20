package com.bytecodecomp.npos.Activities.Admin.Business;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Activities.User.PackageActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Store_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

public class CreateStoreActivity extends AppCompatActivity {

    EditText et_store_name, et_store_location, et_store_address, et_store_contacts;
    Button btn_submit;

    private RadioGroup radioStoreType;
    private RadioButton radioGroup;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference storeDatabaseReference = AppController.storeDatabaseReference;

    String TAG = "StoreActivity";

    //boolean to check if there are existing values
    Boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);

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

                    do_add_store_details(et_store_name.getText().toString(), et_store_address.getText().toString(), et_store_location.getText().toString(), radioGroup.getText().toString(), et_store_contacts.getText().toString());


                }

                //if values are empty alert the user.
                if (et_store_name.getText().toString().length() < 1 && et_store_location.getText().toString().length() < 1 && et_store_address.getText().toString().length() < 1 && et_store_contacts.getText().toString().length() < 1 ){

                    Toasty.warning(getApplicationContext(), "Enter all values or Field size error", Toast.LENGTH_SHORT, true).show();

                }

            }
        });




    }


    //create store details
    public void do_add_store_details(final String store_name, final String store_address, final String store_location, final String store_print, final String store_contacts){

        Store_Model store_model = new Store_Model( store_name, store_address, store_location, store_print, store_contacts, "none", "none","none", "offline" );
        storeDatabaseReference.child(user.getUid()).setValue(store_model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                App_Settings.store_name = store_name;
                App_Settings.store_address = store_address;
                App_Settings.store_location = store_location;
                App_Settings.store_contacts = store_contacts;
                App_Settings.store_status = "";


                Toasty.success(getApplicationContext(), "Store Added Successful, Choose a package", Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(CreateStoreActivity.this ,PackageActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {



                    }
                });

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
