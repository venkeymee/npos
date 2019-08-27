package com.bytecodecomp.npos.Activities.Devices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.Device_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

public class CreateDeviceActivity extends AppCompatActivity {

    EditText et_device_name, et_device_serial, et_device_role;
    Button btn_submit;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference deviceDatabaseReference = AppController.deviceDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        et_device_name = (EditText) findViewById(R.id.et_device_name);
        et_device_serial = (EditText) findViewById(R.id.et_device_serial);
        et_device_role = (EditText) findViewById(R.id.et_device_role);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_device_name.getText().toString().length() <= 0 || et_device_serial.getText().toString().length() <= 0 || et_device_role.getText().toString().length() <= 0 ){

                    Toasty.warning(CreateDeviceActivity.this, "Enter all values").show();

                }

                else {

                    if (et_device_role.getText().toString().equals("merchant_admin") || et_device_role.getText().toString().equals("staff")){

                        create_device(et_device_name.getText().toString(), et_device_serial.getText().toString(), et_device_role.getText().toString());

                    }

                    else {

                        Toasty.warning(CreateDeviceActivity.this, "Enter valid device roles").show();

                    }

                }

            }
        });

    }

    public void create_device(String device_name, String device_serial, String device_role){

        String id = deviceDatabaseReference.push().getKey();
        Device_Model device_model = new Device_Model(device_name, id, device_serial, device_role);
        deviceDatabaseReference.child(user.getUid()).child(id).setValue(device_model);

        Toasty.success(getApplicationContext(), "Device Added", Toast.LENGTH_SHORT, true).show();

        et_device_name.setText("");
        et_device_serial.setText("");
        et_device_role.setText("");


    }

}
