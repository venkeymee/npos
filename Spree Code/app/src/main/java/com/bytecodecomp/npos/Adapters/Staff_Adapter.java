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

import com.bytecodecomp.npos.Activities.Staff.ViewStaffActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Staff_Adapter extends BaseAdapter {

    private ArrayList<String> staff_id;
    private ArrayList<String> staff_name;
    private ArrayList<String> staff_email;
    private ArrayList<String> staff_phone;
    private ArrayList<String> staff_update_date;
    private ArrayList<String> staff_add_date;
    private ArrayList<String> staff_device_id;
    private ArrayList<String> staff_profile_photo;
    private ArrayList<String> staff_docs;
    private ArrayList<String> staff_password;
    private ArrayList<String> staff_commision;

    Activity activity;

    public Staff_Adapter(ArrayList<String> staff_id, ArrayList<String> staff_name, ArrayList<String> staff_email, ArrayList<String> staff_phone, ArrayList<String> staff_update_date, ArrayList<String> staff_add_date,     ArrayList<String> staff_device_id, ArrayList<String> staff_profile_photo, ArrayList<String> staff_docs, ArrayList<String> staff_password, ArrayList<String> staff_commision, Activity activity){
        this.staff_id=staff_id;
        this.staff_name=staff_name;
        this.staff_email=staff_email;
        this.staff_phone=staff_phone;
        this.staff_update_date=staff_update_date;
        this.staff_add_date=staff_add_date;
        this.staff_device_id=staff_device_id;
        this.staff_profile_photo=staff_profile_photo;
        this.staff_docs=staff_docs;
        this.staff_password=staff_password;
        this.staff_commision=staff_commision;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return staff_id.size();
    }

    @Override
    public Object getItem(int i) {
        return staff_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_staff,viewGroup,false);

        ((TextView)view.findViewById(R.id.staff_name)).setText(String.valueOf(staff_name.get(i)));
        ((TextView)view.findViewById(R.id.staff_email)).setText(String.valueOf(staff_email.get(i)));

        ((LinearLayout)view.findViewById(R.id.lyt_parent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ViewStaffActivity.class);
                intent.putExtra("staff_id", String.valueOf(staff_id.get(i)));
                intent.putExtra("staff_name", String.valueOf(staff_name.get(i)));
                intent.putExtra("staff_email", String.valueOf(staff_email.get(i)));
                intent.putExtra("staff_phone", String.valueOf(staff_phone.get(i)));
                intent.putExtra("staff_add_date", String.valueOf(staff_add_date.get(i)));
                intent.putExtra("staff_device_id", String.valueOf(staff_device_id.get(i)));
                intent.putExtra("staff_profile_photo", String.valueOf(staff_profile_photo.get(i)));
                intent.putExtra("staff_docs", String.valueOf(staff_docs.get(i)));
                intent.putExtra("staff_password", String.valueOf(staff_password.get(i)));
                intent.putExtra("staff_commision", String.valueOf(staff_commision.get(i)));

                activity.startActivity(intent);

            }
        });


        return view;
    }
}