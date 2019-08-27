package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytecodecomp.npos.Activities.Admin.Packages.PackagePaymentActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Package_Adapter extends BaseAdapter {

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

    public Package_Adapter(ArrayList<Boolean> store_inventory, ArrayList<Integer> store_inventory_limit, ArrayList<Boolean> store_purchase, ArrayList<Integer> store_purchase_daily_limit, ArrayList<Boolean> store_sales, ArrayList<Integer> store_sales_daily_limit, ArrayList<Boolean> store_customer, ArrayList<Integer> store_customer_limit, ArrayList<Boolean> store_supplier, ArrayList<Integer> store_supplier_limit, ArrayList<Boolean> store_staff, ArrayList<Integer> store_staff_limit, ArrayList<Boolean> store_assets, ArrayList<Integer> store_assets_limit, ArrayList<Boolean> store_exp_type, ArrayList<Integer> store_exp_type_limit, ArrayList<Boolean> store_exp, ArrayList<Integer> store_exp_limit, ArrayList<String> package_name, ArrayList<String> package_exp, ArrayList<String> package_cost, ArrayList<String> package_period, ArrayList<String> package_id, Activity activity) {
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
        ((TextView)view.findViewById(R.id.txt_package_period)).setText(String.valueOf(package_period.get(i)));

        ((LinearLayout)view.findViewById(R.id.lyt_parent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_package_info(i);

            }
        });


        return view;
    }


    public void dialog_package_info(final int position){

        String package_infos = " Package Duration : " + package_period.get(position) + " Days" + " \n";
        package_infos = package_infos +  " Package Cost : " + package_cost.get(position) + " \n";
        package_infos = package_infos +  " Manage Inventory : " + store_inventory.get(position) + " \n";
        package_infos = package_infos +  " Inventory Count : " + store_inventory_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Purchases : " + store_purchase.get(position) + " \n";
        package_infos = package_infos +  " Daily purchase limit : " + store_purchase_daily_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Sales : " + store_sales.get(position) + " \n";
        package_infos = package_infos +  " Daily Sales Limit : " + store_sales_daily_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Customer : " + store_customer.get(position) + " \n";
        package_infos = package_infos +  " Customer Limit : " + store_customer_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Staff : " + store_staff.get(position) + " \n";
        package_infos = package_infos +  " Staff Limit : " + store_staff_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Assets : " + store_assets.get(position) + " \n";
        package_infos = package_infos +  " Assets Limit : " + store_assets_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Expense Type : " + store_exp_type.get(position) + " \n";
        package_infos = package_infos +  " Expense Type Limit : " + store_exp_type_limit.get(position) + " \n";
        package_infos = package_infos +  " Manage Store : " + store_exp.get(position) + " \n";
        package_infos = package_infos +  " Store Limit : " + store_exp_limit.get(position) + " \n";


        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_package);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView package_title = (TextView) dialog.findViewById(R.id.package_title);
        package_title.setText(package_name.get(position));

        TextView package_info = (TextView) dialog.findViewById(R.id.package_info);
        package_info.setText(package_infos);


        final AppCompatButton bt_checkout = (AppCompatButton) dialog.findViewById(R.id.bt_checkout);
        bt_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, PackagePaymentActivity.class);
                intent.putExtra("amount", package_cost.get(position));
                intent.putExtra("package_name", package_name.get(position));
                intent.putExtra("package_id", package_id.get(position));
                intent.putExtra("package_period", package_period.get(position));
                activity.startActivity(intent);
                activity.finish();

            }
        });

        final AppCompatButton bt_close = (AppCompatButton) dialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }




}