package com.bytecodecomp.npos.Activities.Expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Expense_Adapter;
import com.bytecodecomp.npos.Data_Models.Expense_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class ExpenseActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference expenseDatabaseReference = AppController.expenseDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        initToolbar();
        read_expence();

    }

    //get expense
    private void read_expence(){

        final ArrayList<String> expense_id = new ArrayList<>();
        final ArrayList<String> expense_name = new ArrayList<>();
        final ArrayList<String> expense_value = new ArrayList<>();
        final ArrayList<String> expense_type = new ArrayList<>();
        final ArrayList<String> expense_Date = new ArrayList<>();
        final ArrayList<String> expense_for = new ArrayList<>();
        final ArrayList<String> expense_update_date = new ArrayList<>();
        final ArrayList<String> expense_add_date = new ArrayList<>();

        expenseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //clear ArrayList on addValueEventListener
                expense_id.clear();
                expense_name.clear();
                expense_value.clear();
                expense_type.clear();
                expense_Date.clear();
                expense_for.clear();
                expense_update_date.clear();
                expense_add_date.clear();

                while((iterator.hasNext())){
                    Expense_Model value = iterator.next().getValue(Expense_Model.class);
                    expense_id.add(value.getExpense_id());
                    expense_name.add(value.getExpense_name());
                    expense_value.add(value.getExpense_value());
                    expense_type.add(value.getExpense_type());
                    expense_Date.add(value.getExpense_purchase_Date());
                    expense_for.add(value.getExpenset_for());
                    expense_update_date.add(value.getExpense_update_date());
                    expense_add_date.add(value.getExpense_add_date());
                    ((Expense_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Expense_Adapter(expense_id, expense_name, expense_value, expense_type, expense_Date, expense_for, expense_update_date, expense_add_date, this));
    }


    // initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Expense");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {

            // home button
            case android.R.id.home:
                finish();
                return true;

            // create button
            case R.id.action_create:
                Intent intent = new Intent(this, AddExpensesActivity.class);
                startActivity(intent);
                return true;

        }

        return true;

    }



}
