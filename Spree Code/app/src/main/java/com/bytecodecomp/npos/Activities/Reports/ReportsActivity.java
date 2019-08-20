package com.bytecodecomp.npos.Activities.Reports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Data_Models.Expense_Model;
import com.bytecodecomp.npos.Data_Models.Purchase_Model;
import com.bytecodecomp.npos.Data_Models.Sales_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import im.dacer.androidcharts.BarView;

public class ReportsActivity extends AppCompatActivity {

    BarView bar_sales, bar_purchase, bar_expense;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;
    DatabaseReference purchasesDatabaseReference = AppController.purchasesDatabaseReference;
    DatabaseReference expenseDatabaseReference = AppController.expenseDatabaseReference;

    ArrayList<String> dates = new ArrayList<String>();
    final ArrayList<String> date_list = new ArrayList<String>();

    final ArrayList<Integer> sales_data_list = new ArrayList<Integer>();
    final ArrayList<Integer> purchase_data_list = new ArrayList<Integer>();
    final ArrayList<Integer> expense_data_list = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        bar_sales = (BarView) findViewById(R.id.bar_sales);

        bar_purchase = (BarView) findViewById(R.id.bar_purchase);

        bar_expense = (BarView) findViewById(R.id.bar_expense);


        load_last_seven_days();
        initToolbar();

        load_last_seven_days_sales();
        load_last_seven_days_purchase();
        load_last_seven_days_expense();

    }


    //This func gets last seven days
    public void load_last_seven_days(){

        for (int i = 0; i <= 7; i ++){

            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -i);
            Date daysBeforeDate = cal.getTime();

            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Today's date is "+dateFormat.format(calendar.getTime()));

            calendar.add(Calendar.DATE, -i);
            System.out.println("Yesterday's date was "+dateFormat.format(calendar.getTime()));


            Log.e("Date...", dateFormat.format(calendar.getTime()));
            date_list.add(dateFormat.format(calendar.getTime()));

            SimpleDateFormat postFormater = new SimpleDateFormat("EEE");
            dates.add(postFormater.format(daysBeforeDate));

            sales_data_list.add(0);
            purchase_data_list.add(0);
            expense_data_list.add(0);

        }

    }


    //load last sales for seven days
    public void load_last_seven_days_sales(){


            salesDatabaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();


                while((iterator.hasNext())){
                    Sales_Model value = iterator.next().getValue(Sales_Model.class);

                    String date = value.getPayment_date();
                    String sub_date =date.substring(0,10);

                    if (date_list.contains(sub_date)){

                        Log.e("Date 2 ...", sub_date);
                        int postition = date_list.indexOf(sub_date);
                        double total_sales = Double.parseDouble(value.getPayment_amount());
                        double total = sales_data_list.get(postition) + total_sales;

                        int int_total = (int) total;

                        sales_data_list.add(postition, int_total);

                    }

                }

                Log.e("sales_data_list...", String.valueOf(sales_data_list));

                int totals = 0;
                for (int i : sales_data_list)
                    totals += i;

                totals = totals / 7;

                bar_sales.setBottomTextList(dates);
                bar_sales.setDataList(sales_data_list, totals);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    //load last PURCHASES for seven days
    public void load_last_seven_days_purchase(){


        purchasesDatabaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();


                while((iterator.hasNext())){
                    Purchase_Model value = iterator.next().getValue(Purchase_Model.class);

                    String date = value.getPurchase_date().replace("/", "-");
                    String sub_date =date.substring(0,10);

                    if (date_list.contains(sub_date)){

                        int postition = date_list.indexOf(sub_date);
                        double total_sales = Double.parseDouble(value.getPurchase_buying_price()) * Double.parseDouble(value.getPurchase_qnty());
                        double total = purchase_data_list.get(postition) + total_sales;

                        int int_total = (int) total;

                        purchase_data_list.add(postition, int_total);

                    }

                }

                Log.e("purchase_data_list...", String.valueOf(purchase_data_list));

                int totals = 0;
                for (int i : purchase_data_list)
                    totals += i;

                totals = totals / 7;

                bar_purchase.setBottomTextList(dates);
                bar_purchase.setDataList(purchase_data_list, totals);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });





    }


    //load last EXPENSES for seven days
    public void load_last_seven_days_expense(){


        expenseDatabaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();


                while((iterator.hasNext())){
                    Expense_Model value = iterator.next().getValue(Expense_Model.class);

                    String date = value.getExpense_add_date().replace("/", "-");
                    String sub_date =date.substring(0,10);

                    if (date_list.contains(sub_date)){

                        int postition = date_list.indexOf(sub_date);
                        double total_sales = Double.parseDouble(value.getExpense_value());
                        double total = expense_data_list.get(postition) + total_sales;

                        int int_total = (int) total;

                        expense_data_list.add(postition, int_total);

                    }

                }

                Log.e("expense_data_list...", String.valueOf(expense_data_list));

                int totals = 0;
                for (int i : expense_data_list)
                    totals += i;

                totals = totals / 7;

                bar_expense.setBottomTextList(dates);
                bar_expense.setDataList(expense_data_list, totals);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });





    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Reports");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }






}
