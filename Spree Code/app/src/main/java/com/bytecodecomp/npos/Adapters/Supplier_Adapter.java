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

import com.bytecodecomp.npos.Activities.Supplier.ViewSupplierActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Supplier_Adapter extends BaseAdapter {

    private ArrayList<String> supplier_id;
    private ArrayList<String> supplier_name;
    private ArrayList<String> supplier_email;
    private ArrayList<String> supplier_debit;
    private ArrayList<String> supplier_business;
    private ArrayList<String> supplier_phone_number;
    private ArrayList<String> supplier_update_date;
    private ArrayList<String> supplier_add_date;



    Activity activity;

    public Supplier_Adapter(ArrayList<String> supplier_id, ArrayList<String> supplier_name, ArrayList<String> supplier_email, ArrayList<String> supplier_debit, ArrayList<String> supplier_business, ArrayList<String> supplier_phone_number, ArrayList<String> supplier_update_date, ArrayList<String> supplier_add_date, Activity activity){
        this.supplier_id=supplier_id;
        this.supplier_name=supplier_name;
        this.supplier_email=supplier_email;
        this.supplier_debit=supplier_debit;
        this.supplier_business=supplier_business;
        this.supplier_phone_number=supplier_phone_number;
        this.supplier_update_date=supplier_update_date;
        this.supplier_add_date=supplier_add_date;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return supplier_id.size();
    }

    @Override
    public Object getItem(int i) {
        return supplier_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_supplier,viewGroup,false);

        ((TextView)view.findViewById(R.id.supplier_name)).setText(String.valueOf(supplier_name.get(i)));
        ((TextView)view.findViewById(R.id.supplier_email)).setText(String.valueOf(supplier_email.get(i)));
        ((TextView)view.findViewById(R.id.supplier_business)).setText(String.valueOf(supplier_business.get(i)));

        ((LinearLayout)view.findViewById(R.id.lyt_parent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ViewSupplierActivity.class);
                intent.putExtra("supplier_id", String.valueOf(supplier_id.get(i)));
                intent.putExtra("supplier_name", String.valueOf(supplier_name.get(i)));
                intent.putExtra("supplier_email", String.valueOf(supplier_email.get(i)));
                intent.putExtra("supplier_debit", String.valueOf(supplier_debit.get(i)));
                intent.putExtra("supplier_business", String.valueOf(supplier_business.get(i)));
                intent.putExtra("supplier_phone_number", String.valueOf(supplier_phone_number.get(i)));
                intent.putExtra("supplier_add_date", String.valueOf(supplier_add_date.get(i)));
                activity.startActivity(intent);

            }
        });


        return view;
    }
}