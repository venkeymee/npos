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

import com.bytecodecomp.npos.Activities.Customers.ViewCustomerActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Customer_Adapter extends BaseAdapter {

    private ArrayList<String> customer_id;
    private ArrayList<String> customer_name;
    private ArrayList<String> customer_email;
    private ArrayList<String> customer_credit;
    private ArrayList<String> customer_phone_number;
    private ArrayList<String> customer_update_date;
    private ArrayList<String> customer_add_date;
    Activity activity;

    public Customer_Adapter(ArrayList<String> customer_id, ArrayList<String> customer_name, ArrayList<String> customer_email, ArrayList<String> customer_credit, ArrayList<String> customer_phone_number, ArrayList<String> customer_update_date, ArrayList<String> customer_add_date, Activity activity){
        this.customer_id=customer_id;
        this.customer_name=customer_name;
        this.customer_email=customer_email;
        this.customer_credit=customer_credit;
        this.customer_phone_number=customer_phone_number;
        this.customer_update_date=customer_update_date;
        this.customer_add_date=customer_add_date;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return customer_id.size();
    }

    @Override
    public Object getItem(int i) {
        return customer_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_customer,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_customer_name)).setText(String.valueOf(customer_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_customer_email)).setText(String.valueOf(customer_email.get(i)));

        ((LinearLayout)view.findViewById(R.id.card_customer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ViewCustomerActivity.class);
                intent.putExtra("customer_id", String.valueOf(customer_id.get(i)));
                intent.putExtra("customer_name", String.valueOf(customer_name.get(i)));
                intent.putExtra("customer_email", String.valueOf(customer_email.get(i)));
                intent.putExtra("customer_credit", String.valueOf(customer_credit.get(i)));
                intent.putExtra("customer_phone_number", String.valueOf(customer_phone_number.get(i)));
                activity.startActivity(intent);

            }
        });



        return view;
    }
}