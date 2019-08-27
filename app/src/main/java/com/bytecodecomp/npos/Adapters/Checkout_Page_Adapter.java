package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;

import java.util.ArrayList;


public class Checkout_Page_Adapter extends BaseAdapter {

    private ArrayList<String> product_id;
    private ArrayList<String> product_name;
    private ArrayList<String> product_value;
    private ArrayList<String> product_units;
    private ArrayList<String> product_add_date;
    private ArrayList<String> product_update_date;
    private ArrayList<String> product_gtin;
    private ArrayList<String> product_inventory;
    private ArrayList<String> product_inventory_vat;
    Dialog dialog;

    DatabaseReference cartDatabaseReference;
    private LayoutInflater inflater;


    private Activity activity;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Checkout_Page_Adapter(ArrayList<String> product_id, ArrayList<String> product_name, ArrayList<String> product_value, ArrayList<String> product_units, ArrayList<String> product_add_date, ArrayList<String> product_update_date, ArrayList<String> product_gtin, Dialog dialog, Activity activity, DatabaseReference  cartDatabaseReference, ArrayList<String> product_inventory , ArrayList<String> product_inventory_vat){
        this.product_id=product_id;
        this.product_name=product_name;
        this.product_value=product_value;
        this.product_units=product_units;
        this.product_add_date=product_add_date;
        this.product_update_date=product_update_date;
        this.product_gtin=product_gtin;
        this.dialog = dialog;
        this.cartDatabaseReference = cartDatabaseReference;
        this.product_inventory = product_inventory;
        this.product_inventory_vat = product_inventory_vat;

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


        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) view = inflater.inflate(R.layout.item_product_page_checkout, null);
        {


            Log.e("Array...", product_inventory.size() + " Position..." + i);

            ImageView btn_remove = (ImageView) view.findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cartDatabaseReference.child(user.getUid()).child(product_id.get(i)).removeValue();
                    Toasty.error(activity, product_name.get(i) + " Deleted", Toast.LENGTH_SHORT, true).show();

                }
            });


            ImageView btn_subtract = (ImageView) view.findViewById(R.id.btn_subtract);
            btn_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (product_units.get(i) == "1") {

                        cartDatabaseReference.child(user.getUid()).child(product_id.get(i)).removeValue();
                        Toasty.error(activity, product_name.get(i) + " Deleted", Toast.LENGTH_SHORT, true).show();

                    } else {

                        int int_current = Integer.parseInt(product_units.get(i)) - 1;
                        String value = String.valueOf(int_current);
                        cartDatabaseReference.child(user.getUid()).child(product_id.get(i)).child("product_units").setValue(value);

                    }


                }
            });


            ImageView btn_add = (ImageView) view.findViewById(R.id.btn_add);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String int_count = product_units.get(i);

                    int int_current = Integer.parseInt(int_count) + 1;
                    String value = String.valueOf(int_current);

                    int inventory = Integer.parseInt(product_inventory.get(i));

                    if (inventory >= int_current) {

                        cartDatabaseReference.child(user.getUid()).child(product_id.get(i)).child("product_units").setValue(value);

                    } else {

                        Toasty.error(activity, "Product Inventory Empty", Toast.LENGTH_SHORT, true).show();

                    }


                }
            });

            double subtotal = Double.parseDouble(product_value.get(i));
            double units = Double.parseDouble(product_units.get(i));
            double total = subtotal * units;


            ((TextView) view.findViewById(R.id.txt_Product_Name)).setText(String.valueOf(product_name.get(i)));
            ((TextView) view.findViewById(R.id.txt_Product_unit)).setText(String.valueOf("Units : " + product_units.get(i)));
            ((TextView) view.findViewById(R.id.txt_Product_Price)).setText(String.valueOf(product_value.get(i)) + "  =  " + App_Settings.CURRENCY_TYPE + " " + total);


            Log.e("Count...", product_name.get(i));

        }

        return view;
    }
}