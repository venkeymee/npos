package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytecodecomp.npos.Activities.Devices.ViewDeviceActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Device_Adapter extends BaseAdapter {

    private ArrayList<String> device_name;
    private ArrayList<String> device_id;
    private ArrayList<String> device_serial;
    private ArrayList<String> device_role;

    Activity activity;

    public Device_Adapter(ArrayList<String> device_name, ArrayList<String> device_id, ArrayList<String> device_serial, ArrayList<String> device_role, Activity activity){
        this.device_name = device_name;
        this.device_id = device_id;
        this.device_serial = device_serial;
        this.device_role = device_role;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return device_id.size();
    }

    @Override
    public Object getItem(int i) {
        return device_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_device,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_device_name)).setText(String.valueOf(device_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_device_serial)).setText(String.valueOf(device_serial.get(i)));

        ((LinearLayout)view.findViewById(R.id.card_device)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ViewDeviceActivity.class);
                intent.putExtra("device_name", String.valueOf(device_name.get(i)));
                intent.putExtra("device_id", String.valueOf(device_id.get(i)));
                intent.putExtra("device_serial", String.valueOf(device_serial.get(i)));
                intent.putExtra("device_role", String.valueOf(device_role.get(i)));
                activity.startActivity(intent);

            }
        });



        return view;
    }
}