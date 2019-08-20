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

import com.bytecodecomp.npos.Activities.Admin.Packages.ViewPackageActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Package_Admin_Adapter extends BaseAdapter {

    final ArrayList<Boolean> store_inventory;
    final ArrayList<Integer> store_inventory_limit;
    final ArrayList<Boolean> store_purchase;
    final ArrayList<Integer> store_purchase_daily_limit;
    final ArrayList<Boolean> store_sales;
    final ArrayList<Integer> store_sales_daily_limit;
    final ArrayList<Boolean> store_customer;
    final ArrayList<Integer> store_customer_limit;
    final ArrayList<Boolean> store_supplier;
    final ArrayList<Integer> store_supplier_limit;
    final ArrayList<Boolean> store_staff;
    final ArrayList<Integer> store_staff_limit;
    final ArrayList<Boolean> store_assets;
    final ArrayList<Integer> store_assets_limit;
    final ArrayList<Boolean> store_exp_type;
    final ArrayList<Integer> store_exp_type_limit;
    final ArrayList<Boolean> store_exp;
    final ArrayList<Integer> store_exp_limit;
    final ArrayList<String> package_name;
    final ArrayList<String> package_exp;
    final ArrayList<String> package_cost;
    final ArrayList<String> package_period;
    final ArrayList<String> package_id;

    Activity activity;

    public Package_Admin_Adapter(ArrayList<Boolean> store_inventory, ArrayList<Integer> store_inventory_limit, ArrayList<Boolean> store_purchase, ArrayList<Integer> store_purchase_daily_limit, ArrayList<Boolean> store_sales, ArrayList<Integer> store_sales_daily_limit, ArrayList<Boolean> store_customer, ArrayList<Integer> store_customer_limit, ArrayList<Boolean> store_supplier, ArrayList<Integer> store_supplier_limit, ArrayList<Boolean> store_staff, ArrayList<Integer> store_staff_limit, ArrayList<Boolean> store_assets, ArrayList<Integer> store_assets_limit, ArrayList<Boolean> store_exp_type, ArrayList<Integer> store_exp_type_limit, ArrayList<Boolean> store_exp, ArrayList<Integer> store_exp_limit, ArrayList<String> package_name, ArrayList<String> package_exp, ArrayList<String> package_cost, ArrayList<String> package_period, ArrayList<String> package_id, Activity activity) {
        this.store_inventory = store_inventory;
        this.store_inventory_limit = store_inventory_limit;
        this.store_purchase = store_purchase;
        this.store_purchase_daily_limit = store_purchase_daily_limit;
        this.store_sales = store_sales;
        this.store_sales_daily_limit = store_sales_daily_limit;
        this.store_customer = store_customer;
        this.store_customer_limit = store_customer_limit;
        this.store_supplier = store_supplier;
        this.store_supplier_limit = store_supplier_limit;
        this.store_staff = store_staff;
        this.store_staff_limit = store_staff_limit;
        this.store_assets = store_assets;
        this.store_assets_limit = store_assets_limit;
        this.store_exp_type = store_exp_type;
        this.store_exp_type_limit = store_exp_type_limit;
        this.store_exp = store_exp;
        this.store_exp_limit = store_exp_limit;
        this.package_name = package_name;
        this.package_exp = package_exp;
        this.package_cost = package_cost;
        this.package_period = package_period;
        this.package_id = package_id;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return package_id.size();
    }

    @Override
    public Object getItem(int i) {
        return package_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_package,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_package_title)).setText(String.valueOf(package_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_package_period)).setText(String.valueOf(package_period.get(i)) + " Days");

        ((LinearLayout)view.findViewById(R.id.lyt_parent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ViewPackageActivity.class);
                intent.putExtra("store_inventory_limit", String.valueOf(store_inventory_limit.get(i)));
                intent.putExtra("store_purchase_daily_limit", String.valueOf(store_purchase_daily_limit.get(i)));
                intent.putExtra("store_sales_daily_limit", String.valueOf(store_sales_daily_limit.get(i)));
                intent.putExtra("store_customer_limit", String.valueOf(store_customer_limit.get(i)));
                intent.putExtra("store_supplier_limit", String.valueOf(store_supplier_limit.get(i)));
                intent.putExtra("store_staff_limit", String.valueOf(store_staff_limit.get(i)));
                intent.putExtra("store_assets_limit", String.valueOf(store_assets_limit.get(i)));
                intent.putExtra("store_exp_type_limit", String.valueOf(store_exp_type_limit.get(i)));
                intent.putExtra("store_exp_limit", String.valueOf(store_exp_limit.get(i)));
                intent.putExtra("package_name", String.valueOf(package_name.get(i)));
                intent.putExtra("package_exp", String.valueOf(package_exp.get(i)));
                intent.putExtra("package_cost", String.valueOf(package_cost.get(i)));
                intent.putExtra("package_id", String.valueOf(package_id.get(i)));
                intent.putExtra("package_period", String.valueOf(package_period.get(i)));
                activity.startActivity(intent);

            }
        });


        return view;
    }


}