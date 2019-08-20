package com.bytecodecomp.npos.Activities.Staff;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bytecodecomp.npos.Data_Models.Staff_Model;
import com.bytecodecomp.npos.Plugins.Chips.ChipsAdapter;
import com.bytecodecomp.npos.Plugins.Chips.ChipsItem;
import com.bytecodecomp.npos.Plugins.Chips.ChipsMultiAutoCompleteTextview;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.Plugins.crypto.encrypt;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AddStaffActivity extends AppCompatActivity {

    EditText et_staff_name, et_staff_email, et_staff_phone_number, et_staff_commission, et_staff_password;
    Button btn_submit;
    ImageView staff_image;
    ChipsMultiAutoCompleteTextview et_staff_permissions;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference usersDatabaseReference = AppController.usersDatabaseReference;

    //Firebase storage
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    public String profile_url;
    public String permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initToolbar();

        staff_image = (ImageView) findViewById(R.id.staff_image);
        staff_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });


        et_staff_name = (EditText) findViewById(R.id.et_staff_name);
        et_staff_email = (EditText) findViewById(R.id.et_staff_email);
        et_staff_phone_number = (EditText) findViewById(R.id.et_staff_phone_number);
        et_staff_commission = (EditText) findViewById(R.id.et_staff_commission);
        et_staff_password = (EditText) findViewById(R.id.et_staff_password);

        et_staff_permissions = (ChipsMultiAutoCompleteTextview) findViewById(R.id.et_staff_permissions);

        String[] titles_array = getResources().getStringArray(R.array.permissions_title);
        TypedArray icon_array = getResources().obtainTypedArray(R.array.permission_icons);

        ArrayList<ChipsItem> arrPermissions = new ArrayList<ChipsItem>();

        for(int i=0;i<titles_array.length;i++){
            arrPermissions.add(new ChipsItem(titles_array[i], icon_array.getResourceId(i, -1) ));
            Log.i("Main Activity", arrPermissions.get(i).getTitle() +  " = " + arrPermissions.get(i).getImageid());
        }

        Log.i("MainActivity", "Array :" + arrPermissions.size());

        ChipsAdapter chipsAdapter = new ChipsAdapter(this, arrPermissions);
        et_staff_permissions.setAdapter(chipsAdapter);


        btn_submit  = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permissions = et_staff_permissions.getText().toString();
                Log.e("Permissions....", et_staff_permissions.getText().toString());

                //check if all fields are empty
                if (et_staff_name.getText().toString().length() <= 0 && et_staff_email.getText().toString().length() <= 0 && et_staff_phone_number.getText().toString().length() <= 0 && et_staff_commission.getText().toString().length() <= 0 && et_staff_password.getText().toString().length() <= 0){

                    Toasty.warning(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    if (filePath != null){

                        do_upload_profile();

                    }

                    else {

                        //call create staff
                        do_create_staff(et_staff_name.getText().toString(), et_staff_email.getText().toString(), et_staff_phone_number.getText().toString(), et_staff_commission.getText().toString(), et_staff_password.getText().toString());

                    }



                }


            }
        });

    }


    //create staff
    public void do_create_staff(String staff_name, String staff_email, String staff_phone, String staff_commission, String staff_passowrd){

        Toasty.success(getApplicationContext(), "Staff Added", Toast.LENGTH_SHORT, true).show();

        //generate staff id
        String id = usersDatabaseReference.push().getKey();

        //create add and update dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String add_date = sdf.format(new Date());
        String update_date = add_date;
        String password_encrypt =  encrypt.encryptIt(staff_passowrd);

        Staff_Model staff_model = new Staff_Model(id, staff_name, staff_email, staff_phone, update_date, add_date, Build.SERIAL, profile_url, permissions, password_encrypt, staff_commission);
        usersDatabaseReference.child(user.getUid()).child(id).setValue(staff_model);

//        dialog_add_staff_permision(id);

        //clear all fields
        et_staff_name.setText("");
        et_staff_email.setText("");
        et_staff_phone_number.setText("");
        et_staff_commission.setText("");
        et_staff_password.setText("");
        et_staff_permissions.setText("");

    }

    //Upload image
    public void do_upload_profile() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/staff_images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            profile_url = String.valueOf(taskSnapshot.getUploadSessionUri());
                            Log.e("URL...", profile_url);

                            Toast.makeText(AddStaffActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            if (et_staff_name.getText().toString().length() >= 0 && et_staff_email.getText().toString().length() >= 0 && et_staff_phone_number.getText().toString().length() >= 0 && et_staff_commission.getText().toString().length() >= 0 && et_staff_password.getText().toString().length() >= 0){

                                do_create_staff(et_staff_name.getText().toString(), et_staff_email.getText().toString(), et_staff_phone_number.getText().toString(), et_staff_commission.getText().toString(), et_staff_password.getText().toString());

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddStaffActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            if (et_staff_name.getText().toString().length() >= 0 && et_staff_email.getText().toString().length() >= 0 && et_staff_phone_number.getText().toString().length() >= 0 && et_staff_commission.getText().toString().length() >= 0 && et_staff_password.getText().toString().length() >=0){

                                do_create_staff(et_staff_name.getText().toString(), et_staff_email.getText().toString(), et_staff_phone_number.getText().toString(), et_staff_commission.getText().toString(), et_staff_password.getText().toString());

                            }

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

        }

        else {

            do_create_staff(et_staff_name.getText().toString(), et_staff_email.getText().toString(), et_staff_phone_number.getText().toString(), et_staff_commission.getText().toString(), et_staff_password.getText().toString());

        }

    }


    //pick image
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                staff_image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void dialog_add_staff_permision(final String user_id){

        new AlertDialog.Builder(AddStaffActivity.this)
                .setTitle("Permissions")
                .setMessage("Set staff permissions")
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(AddStaffActivity.this, StaffPermissionsActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })

                .show();

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Staff");

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
