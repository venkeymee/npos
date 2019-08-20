package com.bytecodecomp.npos.Activities.Devices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bytecodecomp.npos.R;

public class ViewDeviceActivity extends AppCompatActivity {


    TextView et_device_name, et_device_serial, et_device_role;
    String device_name, device_id, device_serial, device_role;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_device);
        Bundle bundle = getIntent().getExtras();
        device_name = bundle.getString("device_name");
        device_id = bundle.getString("device_id");
        device_serial = bundle.getString("device_serial");
        device_role = bundle.getString("device_role");

        initToolbar();

        et_device_name = (TextView) findViewById(R.id.et_device_name);
        et_device_name.setText(device_name);
        et_device_serial = (TextView) findViewById(R.id.et_device_serial);
        et_device_serial.setText(device_serial);
        et_device_role = (TextView) findViewById(R.id.et_device_role);
        et_device_role.setText(device_role);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewDeviceActivity.this, EditDeviceActivity.class);
                intent.putExtra("device_name", device_name);
                intent.putExtra("device_id", device_id);
                intent.putExtra("device_serial", device_serial);
                intent.putExtra("device_role", device_role);
                startActivity(intent);

            }
        });


    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Device Details ");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
