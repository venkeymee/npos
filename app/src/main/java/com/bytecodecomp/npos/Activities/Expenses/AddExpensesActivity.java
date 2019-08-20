package com.bytecodecomp.npos.Activities.Expenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Data_Models.Expense_Model;
import com.bytecodecomp.npos.Data_Models.Expense_Type_Model;
import com.bytecodecomp.npos.Data_Models.Staff_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AddExpensesActivity extends AppCompatActivity {

    EditText et_expense_name, et_expense_value, et_expense_date;
    Spinner spn_expense_for, spn_expense_cat;
    Button btn_submit, btn_category;
    SwipeRefreshLayout mSwipeRefreshLayout;


    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference usersDatabaseReference = AppController.usersDatabaseReference;
    DatabaseReference expenseCatDatabaseReference = AppController.expenseCatDatabaseReference;
    DatabaseReference expenseDatabaseReference = AppController.expenseDatabaseReference;

    String expense_type, expense_for;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        initToolbar();

        et_expense_name = (EditText) findViewById(R.id.et_expense_name);
        et_expense_value = (EditText) findViewById(R.id.et_expense_value);
        et_expense_date = (EditText) findViewById(R.id.et_expense_date);
        et_expense_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpensesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                et_expense_date.setText(year + "/" + monthOfYear + "/" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //get staffs
                spinner_expense_for();
                //get expense type
                spinner_expense_cat();

            }
        });

        //get staffs
        spinner_expense_for();

        //get expense type
        spinner_expense_cat();


        spn_expense_for = (Spinner) findViewById(R.id.spn_expense_for);
        spn_expense_for.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense_for = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spn_expense_cat = (Spinner) findViewById(R.id.spn_expense_cat);
        spn_expense_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense_type = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_expense_name.getText().toString().length() <= 0 && et_expense_value.getText().toString().length() <= 0 && et_expense_date.getText().toString().length() <= 0 ){

                    Toasty.warning(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    do_add_expense(et_expense_name.getText().toString(), et_expense_value.getText().toString(), et_expense_date.getText().toString() );

                }

            }
        });


        btn_category = (Button) findViewById(R.id.btn_category);
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_add_exp_type();

            }
        });

    }



    //get expense for
    public void spinner_expense_for(){

        mSwipeRefreshLayout.setRefreshing(false);
        usersDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<String>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())) {
                    Staff_Model value = iterator.next().getValue(Staff_Model.class);
                    String consultaName = value.getStaff_name();
                    nomeConsulta.add(consultaName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddExpensesActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_expense_for.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //get expense type
    public void spinner_expense_cat(){

        mSwipeRefreshLayout.setRefreshing(false);
        expenseCatDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<String>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())) {
                    Expense_Type_Model value = iterator.next().getValue(Expense_Type_Model.class);
                    String consultaName = value.getExpenseType_name();
                    nomeConsulta.add(consultaName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddExpensesActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_expense_cat.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //create an expense
    public void do_add_expense(String expense_name, String expense_value, String expense_date){


        Toasty.success(getApplicationContext(), "Expense Added", Toast.LENGTH_SHORT, true).show();

        String id = expenseDatabaseReference.push().getKey();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String customer_add_date = sdf.format(new Date());
        String customer_update_date = customer_add_date;

        Expense_Model expense_model = new Expense_Model(id, expense_name, expense_value, expense_type, expense_date, expense_for, customer_update_date, customer_add_date);
        expenseDatabaseReference.child(user.getUid()).child(id).setValue(expense_model);

        //clear fields
        et_expense_name.setText("");
        et_expense_value.setText("");
        et_expense_date.setText("");

    }


    //dialog add expense type
    public void dialog_add_exp_type(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_expense);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText edt_exp_type = (EditText) dialog.findViewById(R.id.edt_exp_type);

        ((AppCompatButton) dialog.findViewById(R.id.bt_continue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (edt_exp_type.getText().toString().length() <= 0){

                    Toasty.warning(getApplicationContext(), "Enter Expense Name", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    Toasty.success(getApplicationContext(), "Expense type Created", Toast.LENGTH_SHORT, true).show();

                    String id = expenseCatDatabaseReference.push().getKey();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    String add_date = sdf.format(new Date());
                    String update_date = add_date;
                    Expense_Type_Model expense_model = new Expense_Type_Model(id, edt_exp_type.getText().toString(), update_date, add_date );
                    expenseCatDatabaseReference.child(user.getUid()).child(id).setValue(expense_model);

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

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Expense");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //back button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
