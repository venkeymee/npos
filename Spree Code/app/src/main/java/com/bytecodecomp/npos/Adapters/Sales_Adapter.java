package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;


public class Sales_Adapter extends BaseAdapter {

    private ArrayList<String> payment_date;
    private ArrayList<String> payment_id;
    private ArrayList<String> payment_money_id;
    private ArrayList<String> payment_method;
    private ArrayList<String> items;
    private ArrayList<String> user_id;
    private ArrayList<String> user_name;
    private ArrayList<String> payment_amount;
    private ArrayList<String> payment_user_staff;
    private ArrayList<String> customer_name;

    Activity activity;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;

    public Sales_Adapter(ArrayList<String> payment_date, ArrayList<String> payment_id, ArrayList<String> payment_money_id, ArrayList<String> payment_method, ArrayList<String> items, ArrayList<String> user_id, ArrayList<String> user_name, ArrayList<String> payment_amount, ArrayList<String> payment_user_staff, ArrayList<String> customer_name, Activity activity){

        this.payment_date=payment_date;
        this.payment_id=payment_id;
        this.payment_money_id=payment_money_id;
        this.payment_method=payment_method;
        this.items=items;
        this.user_id=user_id;
        this.user_name=user_name;
        this.payment_amount = payment_amount;
        this.payment_user_staff = payment_user_staff;
        this.customer_name = customer_name;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return payment_id.size();
    }

    @Override
    public Object getItem(int i) {
        return payment_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_sales,viewGroup,false);


        ((TextView)view.findViewById(R.id.txt_staff_name)).setText( String.valueOf(payment_user_staff.get(i)));
        ((TextView)view.findViewById(R.id.txt_sale_date)).setText("Date : " + String.valueOf(payment_date.get(i)));
        ((TextView)view.findViewById(R.id.txt_sale_id)).setText( "Sale Id : " + String.valueOf(payment_id.get(i)));

        ((MaterialRippleLayout)view.findViewById(R.id.lyt_parent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_payments_status(i);

            }
        });

        return view;
    }


    public void show_payments_status(final int i){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payments_invoice);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        String[] dates = payment_date.get(i).split(" ");

        TextView txt_trd_id = (TextView) dialog.findViewById(R.id.txt_trd_id);
        txt_trd_id.setText(payment_id.get(i));

        TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
        txt_date.setText(dates[0]);

        TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);
        txt_time.setText(dates[2] + " " + dates[3]);

        TextView txt_attendant = (TextView) dialog.findViewById(R.id.txt_attendant);
        txt_attendant.setText(payment_user_staff.get(i));

        TextView txt_customer = (TextView) dialog.findViewById(R.id.txt_customer);
        txt_customer.setText(customer_name.get(i));

        TextView txt_amount = (TextView) dialog.findViewById(R.id.txt_amount);
        txt_amount.setText( App_Settings.CURRENCY_TYPE + "  " + payment_amount.get(i));

        TextView txt_payment_gateway = (TextView) dialog.findViewById(R.id.txt_payment_gateway);
        txt_payment_gateway.setText(payment_method.get(i));

        TextView txt_payment = (TextView) dialog.findViewById(R.id.txt_payment);
        txt_payment.setVisibility(View.INVISIBLE);
        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String key = salesDatabaseReference.child(user.getUid()).child(payment_id.get(i)).getKey();
//
//
//
//                Log.e("KEY....", key);
//
//                salesDatabaseReference.child(user.getUid()).child(key).child("payment_method").setValue("Cash");
//                Toasty.success(activity, "Credit Paid").show();
//                dialog.dismiss();

            }
        });

        if (payment_method.get(i).equals("Credit")){

            LinearLayout lyt_credit = (LinearLayout) dialog.findViewById(R.id.lyt_credit);
            lyt_credit.setVisibility(View.VISIBLE);

            TextView txt_credit = (TextView) dialog.findViewById(R.id.txt_credit);
            txt_credit.setText( App_Settings.CURRENCY_TYPE + "  " + payment_amount.get(i));


        }


        ((FloatingActionButton) dialog.findViewById(R.id.fab_print)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intents = new Intent(activity, com.bytecodecomp.npos.Plugins.Printer.MainActivity.class);
                intents.putExtra("action", items.get(i));
                App_Settings.current_activity = "Sales_Adapter";
                activity.startActivity(intents);
                dialog.dismiss();

            }
        });


        ((FloatingActionButton) dialog.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



}