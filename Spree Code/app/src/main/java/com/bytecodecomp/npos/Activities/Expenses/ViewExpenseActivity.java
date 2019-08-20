package com.bytecodecomp.npos.Activities.Expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

public class ViewExpenseActivity extends AppCompatActivity {

    EditText et_expense_name, et_expense_value, et_expense_date;
    TextView txt_exp_for, txt_exp_type;
    Button btn_edit, btn_delete;

    String expense_id, expense_name, expense_value, expense_type, expense_Date, expense_for, expense_update_date, expense_add_date;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference expenseDatabaseReference = AppController.expenseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);

        Bundle bundle = getIntent().getExtras();
        expense_id = bundle.getString("expense_id");
        expense_name = bundle.getString("expense_name");
        expense_value = bundle.getString("expense_value");
        expense_type = bundle.getString("expense_type");
        expense_Date = bundle.getString("expense_Date");
        expense_for = bundle.getString("expense_for");
        expense_update_date = bundle.getString("expense_update_date");
        expense_add_date = bundle.getString("expense_add_date");

        et_expense_name = (EditText) findViewById(R.id.et_expense_name);
        et_expense_name.setText(expense_name);

        et_expense_value = (EditText) findViewById(R.id.et_expense_value);
        et_expense_value.setText(expense_value);

        et_expense_date = (EditText) findViewById(R.id.et_expense_date);
        et_expense_date.setText(expense_Date);

        txt_exp_for = (TextView) findViewById(R.id.txt_exp_for);
        txt_exp_for.setText(expense_for);

        txt_exp_type = (TextView) findViewById(R.id.txt_exp_type);
        txt_exp_type.setText(expense_type);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewExpenseActivity.this, EditExpenseActivity.class);
                intent.putExtra("expense_id", String.valueOf(expense_id));
                intent.putExtra("expense_name", String.valueOf(expense_name));
                intent.putExtra("expense_value", String.valueOf(expense_value));
                intent.putExtra("expense_type", String.valueOf(expense_type));
                intent.putExtra("expense_Date", String.valueOf(expense_Date));
                intent.putExtra("expense_for", String.valueOf(expense_for));
                intent.putExtra("expense_update_date", String.valueOf(expense_update_date));
                intent.putExtra("expense_add_date", String.valueOf(expense_add_date));
                ViewExpenseActivity.this.startActivity(intent);
                finish();

            }
        });


        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseDatabaseReference.child(user.getUid()).child(expense_id).removeValue();
                Toasty.error(ViewExpenseActivity.this, "Expense Deleted");
                finish();

            }
        });



        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Expense");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
