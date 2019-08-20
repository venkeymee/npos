package com.bytecodecomp.npos.Activities.Expenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.Expense_Type_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseTypeActivity extends AppCompatActivity {

    EditText et_expense_type_name;
    Button btn_submit;

    DatabaseReference expenseCatDatabaseReference = AppController.expenseCatDatabaseReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_type);

        initToolbar();

        et_expense_type_name = (EditText) findViewById(R.id.et_expense_type_name);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_expense_type_name.getText().toString().length() <= 0){

                    Toast.makeText(ExpenseTypeActivity.this, "Enter Field", Toast.LENGTH_SHORT).show();

                }

                else {

                    do_expense_type(et_expense_type_name.getText().toString());

                }

            }
        });

    }


    //add expense type
    public void do_expense_type(String name){

        Toasty.success(getApplicationContext(), "Expense Created", Toast.LENGTH_SHORT, true).show();

        String id = expenseCatDatabaseReference.push().getKey();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String add_date = sdf.format(new Date());
        String update_date = add_date;

        //clear et_expense_type_name field
        et_expense_type_name.setText("");

        Expense_Type_Model expense_model = new Expense_Type_Model(id, name, update_date, add_date );
        expenseCatDatabaseReference.child(user.getUid()).child(id).setValue(expense_model);

    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Expense Type");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // home button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
