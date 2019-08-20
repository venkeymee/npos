package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Subscriptions_Adapter extends BaseAdapter {

    private ArrayList<String> subscription_id;
    private ArrayList<String> subscription_date;
    private ArrayList<String> package_name;
    private ArrayList<String> package_id;
    private ArrayList<String> subscription_price;
    private ArrayList<String> subscription_business_name;
    private ArrayList<String> subscription_business_id;
    private ArrayList<String> subscription_expiry;
    private ArrayList<String> subscription_transaction_id;
    private ArrayList<String> subscription_payment;

    Activity activity;

    public Subscriptions_Adapter(ArrayList<String> subscription_id, ArrayList<String> subscription_date, ArrayList<String> package_name, ArrayList<String> subscription_price, ArrayList<String> subscription_business_name, ArrayList<String> subscription_business_id, ArrayList<String> subscription_expiry, ArrayList<String> subscription_transaction_id, ArrayList<String> subscription_payment, ArrayList<String> package_id, Activity activity){

        this.subscription_id=subscription_id;
        this.subscription_date=subscription_date;
        this.package_name=package_name;
        this.subscription_price=subscription_price;
        this.subscription_business_name=subscription_business_name;
        this.subscription_business_id=subscription_business_id;
        this.subscription_expiry=subscription_expiry;
        this.subscription_transaction_id = subscription_transaction_id;
        this.subscription_payment = subscription_payment;
        this.package_id = package_id;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return subscription_id.size();
    }

    @Override
    public Object getItem(int i) {
        return subscription_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_subscriptions,viewGroup,false);

        int count = i + 1;

        LinearLayout lyt_purchase = (LinearLayout) view.findViewById(R.id.lyt_purchase);

        if ( ( i % 2 ) == 0 ) {

            lyt_purchase.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_purchase.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }

        ((TextView)view.findViewById(R.id.txt_purchase_count)).setText( count + " .");
        ((TextView)view.findViewById(R.id.txt_sub_business)).setText(String.valueOf(subscription_business_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_sub_package_name)).setText(String.valueOf(package_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_sub_cost)).setText(String.valueOf(subscription_price.get(i)));
        ((TextView)view.findViewById(R.id.txt_sub_status)).setText(String.valueOf(subscription_expiry.get(i)));

        return view;
    }
}