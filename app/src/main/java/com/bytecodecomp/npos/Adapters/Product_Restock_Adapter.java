package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Permissions_Control;

import java.util.ArrayList;


public class Product_Restock_Adapter extends BaseAdapter {

    private ArrayList<String> product_id;
    private ArrayList<String> product_name;
    private ArrayList<String> product_value;
    private ArrayList<String> product_units;
    private ArrayList<String> product_add_date;
    private ArrayList<String> product_update_date;
    private ArrayList<String> product_gtin;
    private ArrayList<String> product_buying_price;


    private Activity activity;

    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public Product_Restock_Adapter(ArrayList<String> product_id, ArrayList<String> product_name, ArrayList<String> product_value, ArrayList<String> product_units, ArrayList<String> product_add_date, ArrayList<String> product_update_date, ArrayList<String> product_gtin, ArrayList<String> product_buying_price, Activity activity){
        this.product_id=product_id;
        this.product_name=product_name;
        this.product_value=product_value;
        this.product_units=product_units;
        this.product_add_date=product_add_date;
        this.product_update_date=product_update_date;
        this.product_gtin=product_gtin;
        this.product_buying_price=product_buying_price;


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

                if (Permissions_Control.check_perms("user_restock") == true) {

                    do_restock_dialog(product_name.get(i), product_units.get(i), product_id.get(i));

                }

                else
                {

                    Toasty.warning(activity, "Missing Access Restock Permission").show();

                }

            }
        });


        return view;
    }


    public void do_restock_dialog(final String product_name, final String product_units, final String product_id){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_restock);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        txt_title.setText("Restock " + product_name);

        final EditText edt_restock = (EditText) dialog.findViewById(R.id.edt_restock);

        ((AppCompatButton) dialog.findViewById(R.id.bt_continue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_restock.getText().toString().length() <= 0){

                    Toasty.warning(activity, "Enter Expense Name", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    int current = Integer.parseInt(product_units) + Integer.parseInt(edt_restock.getText().toString());
                    inventoryDatabaseReference.child(user.getUid()).child(product_id).child("product_units").setValue(current + "");
                    dialog.dismiss();

                }

            }
        });


        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

}