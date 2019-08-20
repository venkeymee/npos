package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/11/2018.
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

import com.bytecodecomp.npos.Activities.Assets.ViewAssetsActivity;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Assets_Adapter extends BaseAdapter {

    private ArrayList<String> asset_id;
    private ArrayList<String> asset_name;
    private ArrayList<String> asset_value;
    private ArrayList<String> asset_type;
    private ArrayList<String> asset_qnty;
    private ArrayList<String> asset_purchase_Date;
    private ArrayList<String> asset_update_date;
    private ArrayList<String> asset_add_date;
    Activity activity;

    public Assets_Adapter(ArrayList<String> asset_id, ArrayList<String> asset_name, ArrayList<String> asset_value, ArrayList<String> asset_type, ArrayList<String> asset_qnty, ArrayList<String> asset_purchase_Date, ArrayList<String> asset_update_date, ArrayList<String> asset_add_date, Activity activity){
        this.asset_id=asset_id;
        this.asset_name=asset_name;
        this.asset_value=asset_value;
        this.asset_type=asset_type;
        this.asset_qnty=asset_qnty;
        this.asset_purchase_Date=asset_purchase_Date;
        this.asset_update_date=asset_update_date;
        this.asset_add_date = asset_add_date;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return asset_id.size();
    }

    @Override
    public Object getItem(int i) {
        return asset_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_assets,viewGroup,false);

        int count = i + 1;
        ((TextView)view.findViewById(R.id.txt_asset_count)).setText(count + " .");

        ((TextView)view.findViewById(R.id.txt_asset_type)).setText(String.valueOf(asset_type.get(i)));
        ((TextView)view.findViewById(R.id.txt_asset_units)).setText(String.valueOf(asset_qnty.get(i)));
        ((TextView)view.findViewById(R.id.txt_asset_name)).setText(String.valueOf(asset_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_asset_cost)).setText(String.valueOf(asset_value.get(i)));

        LinearLayout lyt_assets = (LinearLayout) view.findViewById(R.id.lyt_assets);

        if ( ( i % 2 ) == 0 ) {

            lyt_assets.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_assets.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }


        ((LinearLayout)view.findViewById(R.id.lyt_assets)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewAssetsActivity.class);
                intent.putExtra("asset_name", String.valueOf(asset_name.get(i)));
                intent.putExtra("asset_qnty", String.valueOf(asset_qnty.get(i)));
                intent.putExtra("asset_value", String.valueOf(asset_value.get(i)));
                intent.putExtra("asset_date", String.valueOf(asset_add_date.get(i)));
                intent.putExtra("asset_id", String.valueOf(asset_id.get(i)));
                activity.startActivity(intent);
            }
        });


        return view;
    }
}