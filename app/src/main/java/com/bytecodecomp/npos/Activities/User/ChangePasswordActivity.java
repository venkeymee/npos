package com.bytecodecomp.npos.Activities.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_password;
    Button btn_reset_password, btn_back;

    private ProgressBar progressBar;

    //get current user
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        edt_password = (EditText) findViewById(R.id.edt_password);


        btn_reset_password = (Button) findViewById(R.id.btn_reset_password);
        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if field is empty
                if (edt_password.getText().toString().length() <= 0){

                    Toasty.warning(ChangePasswordActivity.this, "Password Field Empty", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    //call change password method
                    do_password_change(edt_password.getText().toString());

                }

            }
        });


        //backpress button
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }


    //change password method
    public void do_password_change(String password){

        progressBar.setVisibility(View.VISIBLE);
        if (user != null && !password.equals("")) {
            if (password.length() < 6) {

                Toasty.error(ChangePasswordActivity.this, "Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT, true).show();

                progressBar.setVisibility(View.GONE);
            } else {
                user.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toasty.success(ChangePasswordActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT, true).show();

                                    signOut();
                                    progressBar.setVisibility(View.GONE);
                                } else {

                                    Toasty.error(ChangePasswordActivity.this, "Failed to update password!", Toast.LENGTH_SHORT, true).show();

                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        } else if (password.equals("")) {

            Toasty.info(ChangePasswordActivity.this, "Enter password", Toast.LENGTH_SHORT, true).show();

            progressBar.setVisibility(View.GONE);
        }

    }


    //sign out method
    public void signOut() {
        auth.signOut();
    }



}
