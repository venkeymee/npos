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

public class ChangeEmailActivity extends AppCompatActivity {

    EditText et_email;
    Button btn_reset_email, btn_back;
    private ProgressBar progressBar;

    //get current user
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        auth = FirebaseAuth.getInstance();

        et_email = (EditText) findViewById(R.id.et_email);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btn_reset_email = (Button) findViewById(R.id.btn_reset_email);
        btn_reset_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if email field if empty
                if (et_email.getText().toString().length() <= 0){

                    Toasty.warning(ChangeEmailActivity.this, "Email Field Empty", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    //call change email method
                    do_email_change(et_email.getText().toString());

                }

            }
        });

        //backpress button
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChangeEmailActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }


    //change email method
    public void do_email_change(final String email){

        progressBar.setVisibility(View.VISIBLE);

        if (user != null && !email.equals("")) {
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toasty.success(ChangeEmailActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_SHORT, true).show();

                                signOut();
                                progressBar.setVisibility(View.GONE);
                            } else {

                                Toasty.error(ChangeEmailActivity.this, "Failed to update email!", Toast.LENGTH_SHORT, true).show();

                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        } else if (email.equals("")) {
            progressBar.setVisibility(View.GONE);
        }

    }


    //sign out method
    public void signOut() {
        auth.signOut();
    }



}
