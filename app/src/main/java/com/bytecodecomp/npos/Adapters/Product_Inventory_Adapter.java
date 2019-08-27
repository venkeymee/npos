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

import com.bytecodecomp.npos.Activities.Inventory.ProductDetailsActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Product_Inventory_Adapter extends BaseAdapter {

    private ArrayList<String> product_id;
    private ArrayList<String> product_name;
    private ArrayList<String> product_value;
    private ArrayList<String> product_units;
    private ArrayList<String> product_add_date;
    private ArrayList<String> product_update_date;
    private ArrayList<String> product_gtin;
    private ArrayList<String> product_buying_price;
    private ArrayList<String> product_vat;


    private Activity activity;



    public Product_Inventory_Adapter(ArrayList<String> product_id, ArrayList<String> product_name, ArrayList<String> product_value, ArrayList<String> product_units, ArrayList<String> product_add_date, ArrayList<String> product_update_date, ArrayList<String> product_gtin, ArrayList<String> product_buying_price, ArrayList<String> product_vat, Activity activity){
        this.product_id=product_id;
        this.product_name=product_name;
        this.product_value=product_value;
        this.product_units=product_units;
        this.product_add_date=product_add_date;
        this.product_update_date=product_update_date;
        this.product_gtin=product_gtin;
        this.product_buying_price=product_buying_price;
        this.product_vat = product_vat;


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
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_product_inventory,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_Product_Unit)).setText(String.valueOf(product_units.get(i)));
        ((TextView)view.findViewById(R.id.txt_Product_Name)).setText(String.valueOf(product_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_Product_Price)).setText(String.valueOf(product_value.get(i)));
        ((TextView)view.findViewById(R.id.txt_Product_sku)).setText(String.valueOf(product_gtin.get(i)) );

        TextView txt_Product_count = (TextView) view.findViewById(R.id.txt_Product_count);
        int count = i + 1;
        txt_Product_count.setText( count +  " .");

        LinearLayout lyt_product = (LinearLayout) view.findViewById(R.id.lyt_product);

        if ( ( i % 2 ) == 0 ) {

            lyt_product.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_product.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }

        lyt_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendStuff = new Intent(activity, ProductDetailsActivity.class);
                sendStuff.putExtra("product_id", String.valueOf(product_id.get(i)));
                sendStuff.putExtra("product_name", String.valueOf(product_name.get(i)));
                sendStuff.putExtra("product_value", String.valueOf(product_value.get(i)));
                sendStuff.putExtra("product_units", String.valueOf(product_units.get(i)));
                sendStuff.putExtra("product_add_date", String.valueOf(product_add_date.get(i)));
                sendStuff.putExtra("product_update_date", String.valueOf(product_update_date.get(i)));
                sendStuff.putExtra("product_gtin", String.valueOf(product_gtin.get(i)));
                sendStuff.putExtra("product_vat", String.valueOf(product_vat.get(i)));
                sendStuff.putExtra("product_buying_price", String.valueOf(product_buying_price.get(i)));
                activity.startActivity(sendStuff);
                activity.finish();


            }
        });


        return view;
    }
}