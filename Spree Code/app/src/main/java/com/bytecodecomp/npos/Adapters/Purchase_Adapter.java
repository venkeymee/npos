package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;


public class Purchase_Adapter extends BaseAdapter {

    private ArrayList<String> purchase_id;
    private ArrayList<String> purchase_qnty;
    private ArrayList<String> purchase_date;
    private ArrayList<String> purchase_item;
    private ArrayList<String> purchase_buying_price;
    private ArrayList<String> purchase_selling_price;
    private ArrayList<String> purchase_status;
    private ArrayList<String> purchase_supplier;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference purchasesDatabaseReference = AppController.purchasesDatabaseReference;

    Activity activity;

    public Purchase_Adapter(ArrayList<String> purchase_id, ArrayList<String> purchase_qnty, ArrayList<String> purchase_date, ArrayList<String> purchase_item,   ArrayList<String> purchase_buying_price, ArrayList<String> purchase_selling_price, ArrayList<String> purchase_status, ArrayList<String> purchase_supplier, Activity activity){

        this.purchase_id=purchase_id;
        this.purchase_qnty=purchase_qnty;
        this.purchase_date=purchase_date;
        this.purchase_item=purchase_item;
        this.purchase_buying_price=purchase_buying_price;
        this.purchase_selling_price=purchase_selling_price;
        this.purchase_status=purchase_status;
        this.purchase_supplier = purchase_supplier;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return purchase_id.size();
    }

    @Override
    public Object getItem(int i) {
        return purchase_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_purchase,viewGroup,false);

        int count = i + 1;

        LinearLayout lyt_purchase = (LinearLayout) view.findViewById(R.id.lyt_purchase);

        if ( ( i % 2 ) == 0 ) {

            lyt_purchase.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_purchase.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }

        ((TextView)view.findViewById(R.id.txt_purchase_count)).setText( count + " .");
        ((TextView)view.findViewById(R.id.txt_purchase_business)).setText(String.valueOf(purchase_supplier.get(i)));
        ((TextView)view.findViewById(R.id.txt_purchase_product)).setText(String.valueOf(purchase_item.get(i)));
        ((TextView)view.findViewById(R.id.txt_purchase_sale)).setText(String.valueOf(purchase_buying_price.get(i)));
        ((TextView)view.findViewById(R.id.txt_purchase_status)).setText(String.valueOf(purchase_status.get(i)));

        lyt_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(purchase_supplier.get(i));
                alertDialogBuilder.setMessage("Purchase for " + purchase_qnty.get(i) + " units of " + purchase_item.get(i) + " worth " + purchase_buying_price.get(i) + " " + App_Settings.CURRENCY_TYPE + " per unit");
                alertDialogBuilder.setPositiveButton("Print",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intents = new Intent(activity, com.bytecodecomp.npos.Plugins.Printer.MainActivity.class);
                                intents.putExtra("action", print_purchase(i));
                                App_Settings.current_activity = "Purchase_Adapter";
                                activity.startActivity(intents);

                            }
                        });

                alertDialogBuilder.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                purchasesDatabaseReference.child(user.getUid()).child(purchase_id.get(i)).removeValue();
                                Toasty.error(activity, "Expense Deleted");

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        return view;
    }

    public String print_purchase(int position){

        String purchase =  "\n================================" ;
        purchase = purchase + "\nSUPPLIER : " + purchase_supplier.get(position);
        purchase = purchase + "\nPRODUCT : " + purchase_item.get(position);
        purchase = purchase + "\nCOST : " + purchase_buying_price.get(position) + " " + App_Settings.CURRENCY_TYPE;
        purchase = purchase + "\nUNITS : " + purchase_qnty.get(position);
        purchase = purchase + "\nDATE : " + purchase_date.get(position);
        purchase = purchase + "\n================================";

        return  purchase;

    }


}