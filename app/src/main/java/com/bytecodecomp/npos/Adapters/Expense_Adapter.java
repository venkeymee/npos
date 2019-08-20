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
import com.bytecodecomp.npos.Activities.Expenses.ViewExpenseActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;


public class Expense_Adapter extends BaseAdapter {

    private ArrayList<String> expense_id;
    private ArrayList<String> expense_name;
    private ArrayList<String> expense_value;
    private ArrayList<String> expense_type;
    private ArrayList<String> expense_Date;
    private ArrayList<String> expense_for;
    private ArrayList<String> expense_update_date;
    private ArrayList<String> expense_add_date;
    Activity activity;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference expenseDatabaseReference = AppController.expenseDatabaseReference;

    public Expense_Adapter(ArrayList<String> expense_id, ArrayList<String> expense_name, ArrayList<String> expense_value, ArrayList<String> expense_type, ArrayList<String> expense_Date, ArrayList<String> expense_for, ArrayList<String> expense_update_date, ArrayList<String> expense_add_date, Activity activity){
        this.expense_id=expense_id;
        this.expense_name=expense_name;
        this.expense_value=expense_value;
        this.expense_type=expense_type;
        this.expense_Date=expense_Date;
        this.expense_for=expense_for;
        this.expense_update_date=expense_update_date;
        this.expense_add_date = expense_add_date;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return expense_id.size();
    }

    @Override
    public Object getItem(int i) {
        return expense_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_expence,viewGroup,false);

        int count = i + 1;
        ((TextView)view.findViewById(R.id.txt_Exp_count)).setText(count + " .");

        LinearLayout lyt_Exp = (LinearLayout) view.findViewById(R.id.lyt_Exp);

        if ( ( i % 2 ) == 0 ) {

            lyt_Exp.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_Exp.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }

        ((TextView)view.findViewById(R.id.txt_exp_name)).setText(String.valueOf(expense_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_exp_for)).setText(String.valueOf(expense_for.get(i)));
        ((TextView)view.findViewById(R.id.txt_exp_amount)).setText(String.valueOf(expense_value.get(i)));
        ((TextView)view.findViewById(R.id.txt_exp_type)).setText(String.valueOf(expense_type.get(i)));

        lyt_Exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(expense_name.get(i));
                alertDialogBuilder.setMessage("Expenses for " + expense_for.get(i) + ", worth " + expense_value.get(i) + ", on " + expense_Date.get(i));
                alertDialogBuilder.setPositiveButton("Print",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intents = new Intent(activity, com.bytecodecomp.npos.Plugins.Printer.MainActivity.class);
                                intents.putExtra("action", print_expense(i));
                                App_Settings.current_activity = "Expense_Adapter";
                                activity.startActivity(intents);

                            }
                        });

                alertDialogBuilder.setNeutralButton("Details",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(activity, ViewExpenseActivity.class);
                                intent.putExtra("expense_id", String.valueOf(expense_id.get(i)));
                                intent.putExtra("expense_name", String.valueOf(expense_name.get(i)));
                                intent.putExtra("expense_value", String.valueOf(expense_value.get(i)));
                                intent.putExtra("expense_type", String.valueOf(expense_type.get(i)));
                                intent.putExtra("expense_Date", String.valueOf(expense_Date.get(i)));
                                intent.putExtra("expense_for", String.valueOf(expense_for.get(i)));
                                intent.putExtra("expense_update_date", String.valueOf(expense_update_date.get(i)));
                                intent.putExtra("expense_add_date", String.valueOf(expense_add_date.get(i)));
                                activity.startActivity(intent);

                            }
                        });


                alertDialogBuilder.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                expenseDatabaseReference.child(user.getUid()).child(expense_id.get(i)).removeValue();
                                Toasty.error(activity, "Expense Deleted");

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        return view;
    }


    public String print_expense(int position){

        String expense =  "\n================================" ;
        expense = expense + "\n TITLE : " + expense_name.get(position);
        expense = expense + "\n EXP FOR : " + expense_for.get(position);
        expense = expense + "\n EXP TYPE : " + expense_type.get(position);
        expense = expense + "\n EXP VALUE : " + expense_value.get(position);
        expense = expense + "\n EXP DATE : " + expense_Date.get(position);
        expense = expense + "\n================================";

        return  expense;

    }

}