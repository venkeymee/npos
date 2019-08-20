package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.content.DialogInterface;
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
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;


public class Expense_Type_Adapter extends BaseAdapter {

    private ArrayList<String> expenseType_id;
    private ArrayList<String> expenseType_name;
    private ArrayList<String> expenseType_update_date;
    private ArrayList<String> expenseType_add_date;
    Activity activity;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference expenseCatDatabaseReference = AppController.expenseCatDatabaseReference;

    public Expense_Type_Adapter(ArrayList<String> expenseType_id, ArrayList<String> expenseType_name, ArrayList<String> expenseType_update_date, ArrayList<String> expenseType_add_date, Activity activity){

        this.expenseType_id=expenseType_id;
        this.expenseType_name=expenseType_name;
        this.expenseType_update_date=expenseType_update_date;
        this.expenseType_add_date=expenseType_add_date;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return expenseType_id.size();
    }

    @Override
    public Object getItem(int i) {
        return expenseType_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_expense_type,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_expense_type_name)).setText(String.valueOf(expenseType_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_expense_type_date)).setText(String.valueOf(expenseType_add_date.get(i)));

        LinearLayout card_exp_type = (LinearLayout) view.findViewById(R.id.card_exp_type);
        card_exp_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(expenseType_name.get(i));
                alertDialogBuilder.setMessage("Expenses Type add Date  " + expenseType_add_date.get(i));
                alertDialogBuilder.setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });


                alertDialogBuilder.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                expenseCatDatabaseReference.child(user.getUid()).child(expenseType_id.get(i)).removeValue();
                                Toasty.error(activity, "Expense Type Deleted");

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        return view;
    }
}