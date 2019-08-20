package com.bytecodecomp.npos.Activities.Staff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;

public class SetStaffPassword extends AppCompatActivity {

    TextView txt_staff_name;
    EditText et_password;
    Button btn_submit;

    String staff_id, staff_names, staff_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_staff_password);

        Bundle bundle = getIntent().getExtras();
        staff_id = bundle.getString("staff_id");
        staff_names = bundle.getString("staff_name");
        staff_email = bundle.getString("staff_email");

        txt_staff_name = (TextView) findViewById(R.id.txt_staff_name);

        et_password = (EditText) findViewById(R.id.et_password);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_password.getText().toString().length() <= 1){

                    Toasty.error(SetStaffPassword.this, "Pass cant be empty / less than 5 values");

                }

                else {

                    do_password_change(et_password.getText().toString());

                }

            }
        });


    }



    public void do_password_change(String password){



    }


}
