package com.bytecodecomp.npos.Activities.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.UUID;

public class AccountSettingsActivity extends AppCompatActivity {

    EditText et_account_name;
    Button btn_submit, btn_profile;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference profileDatabaseReference = AppController.profileDatabaseReference;

    String TAG = "AccountSettingsActivity";
    Boolean value = false;

    String str_email, str_role;

    //Firebase storage
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    public String profile_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initToolbar();

        et_account_name = (EditText) findViewById(R.id.et_account_name);
        et_account_name.setText(user.getDisplayName());

        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });

        //Update / Create store details
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //if values are empty alert the user.
                if (et_account_name.getText().toString().length() < 1 ){

                    Toasty.warning(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT, true).show();

                }

                else {


                    if (filePath != null){

                        do_upload_profile();

                    }

                    else {

                        //call create staff
                        update_account(et_account_name.getText().toString(), "");

                    }


                }

        }
    });


    }




    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Settings");

    }


    public void update_account(String str_account_name, String str_avatar){

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(str_account_name)
                .setPhotoUri(Uri.parse(str_avatar))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toasty.success(AccountSettingsActivity.this, "User profile updated.").show();

                        }
                    }
                });

    }


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
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                user_image.setImageBitmap(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
        }
    }



    //upload user profile
    public void do_upload_profile() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image...");
            progressDialog.show();

            final String img_name = user.getUid() + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/admin_image/" + img_name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            profile_url = "https://firebasestorage.googleapis.com/v0/b/spree-254.appspot.com/o/images%2Fadmin_image%2F" + img_name + "?alt=media&token=";
                            Log.e("URL...", profile_url);

                            Toast.makeText(AccountSettingsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            update_account(et_account_name.getText().toString(), profile_url);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AccountSettingsActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            update_account(et_account_name.getText().toString(),  "");


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

            update_account(et_account_name.getText().toString(), "");

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {

            //  home button
            case android.R.id.home:
                finish();
                return true;
        }

        return true;

    }






}
