package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytecodecomp.npos.Activities.Admin.Business.BusinessDetailsActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Business_Adapter extends BaseAdapter {

    private ArrayList<String> store_name;
    private ArrayList<String> store_address;
    private ArrayList<String> store_location;
    private ArrayList<String> store_print;
    private ArrayList<String> store_contacts;
    private ArrayList<String> package_id;
    private ArrayList<String> package_expiry;
    private ArrayList<String> package_name;
    private ArrayList<String> store_status;

    Activity activity;

    public Business_Adapter(ArrayList<String> store_name, ArrayList<String> store_address, ArrayList<String> store_location, ArrayList<String> store_print, ArrayList<String> store_contacts, ArrayList<String> package_id, ArrayList<String> package_expiry, ArrayList<String> package_name, ArrayList<String> store_status, Activity activity){

        this.store_name=store_name;
        this.store_address=store_address;
        this.store_location=store_location;
        this.store_print=store_print;
        this.store_contacts=store_contacts;
        this.package_id=package_id;
        this.package_expiry=package_expiry;
        this.package_name = package_name;
        this.store_status = store_status;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return store_name.size();
    }

    @Override
    public Object getItem(int i) {
        return store_name.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_business,viewGroup,false);

        int count = i + 1;

        LinearLayout lyt_purchase = (LinearLayout) view.findViewById(R.id.lyt_purchase);

        if ( ( i % 2 ) == 0 ) {

            lyt_purchase.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_purchase.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }

        ((TextView)view.findViewById(R.id.txt_purchase_count)).setText( count + " .");
        ((TextView)view.findViewById(R.id.txt_sub_business)).setText(String.valueOf(store_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_sub_package_name)).setText(String.valueOf(package_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_sub_status)).setText(String.valueOf(store_status.get(i)));

        ((LinearLayout)view.findViewById(R.id.lyt_business)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, BusinessDetailsActivity.class);
                intent.putExtra("store_name", store_name.get(i));
                intent.putExtra("store_address", store_address.get(i));
                intent.putExtra("store_location", store_location.get(i));
                intent.putExtra("store_contacts", store_contacts.get(i));
                intent.putExtra("package_name", package_name.get(i));
                intent.putExtra("store_status", store_status.get(i));
                activity.startActivity(intent);

            }
        });

        return view;
    }
}