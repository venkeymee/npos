package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Sales_Inventory_Adapter extends BaseAdapter {

    private ArrayList<String> product_id;
    private ArrayList<String> product_name;
    private ArrayList<String> product_value;
    private ArrayList<String> product_units;
    private ArrayList<String> product_add_date;
    private ArrayList<String> product_update_date;
    private ArrayList<String> product_gtin;
    Dialog dialog;


    private Activity activity;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Sales_Inventory_Adapter(ArrayList<String> product_id, ArrayList<String> product_name, ArrayList<String> product_value, ArrayList<String> product_units, ArrayList<String> product_add_date, ArrayList<String> product_update_date, ArrayList<String> product_gtin, Dialog dialog, Activity activity){
        this.product_id=product_id;
        this.product_name=product_name;
        this.product_value=product_value;
        this.product_units=product_units;
        this.product_add_date=product_add_date;
        this.product_update_date=product_update_date;
        this.product_gtin=product_gtin;
        this.dialog = dialog;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return product_id.size();
    }

    @Override
    public Object getItem(int i) {
        return product_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_product_cart,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_Product_Name)).setText(String.valueOf(product_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_Product_Price)).setText( App_Settings.CURRENCY_TYPE + " " + String.valueOf(product_value.get(i)));

        return view;
    }
}