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
import com.bytecodecomp.npos.Adapters.Expense_Type_Adapter;
import com.bytecodecomp.npos.Data_Models.Expense_Type_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class ViewExpenseTypeActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference expenseCatDatabaseReference = AppController.expenseCatDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense_type);

        initToolbar();
        read_expence_type();

    }

    // get expense type
    public void read_expence_type(){

        final ArrayList<String> expenseType_id = new ArrayList<>();
        final ArrayList<String> expenseType_name = new ArrayList<>();
        final ArrayList<String> expenseType_update_date = new ArrayList<>();
        final ArrayList<String> expenseType_add_date = new ArrayList<>();

        expenseCatDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                // clear ArrayList on addValueEventListener
                expenseType_id.clear();
                expenseType_name.clear();
                expenseType_update_date.clear();
                expenseType_add_date.clear();

                while((iterator.hasNext())){
                    Expense_Type_Model value = iterator.next().getValue(Expense_Type_Model.class);
                    expenseType_id.add(value.getExpenseType_id());
                    expenseType_name.add(value.getExpenseType_name());
                    expenseType_update_date.add(value.getExpenseType_update_date());
                    expenseType_add_date.add(value.getExpenseType_add_date());
                    ((Expense_Type_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Expense_Type_Adapter(expenseType_id, expenseType_name, expenseType_update_date, expenseType_add_date, this));


    }


    // initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Expense Type");

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
                Intent intent = new Intent(this, ExpenseTypeActivity.class);
                startActivity(intent);
                return true;

        }

        return true;

    }

}
