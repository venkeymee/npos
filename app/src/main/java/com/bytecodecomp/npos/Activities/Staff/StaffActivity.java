package com.bytecodecomp.npos.Activities.Staff;

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
import com.bytecodecomp.npos.Adapters.Staff_Adapter;
import com.bytecodecomp.npos.Data_Models.Staff_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class StaffActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference usersDatabaseReference = AppController.usersDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        initToolbar();
        do_staff();

    }

    //get all staffs
    public void do_staff(){

        final ArrayList<String> staff_id = new ArrayList<>();
        final ArrayList<String> staff_name = new ArrayList<>();
        final ArrayList<String> staff_email = new ArrayList<>();
        final ArrayList<String> staff_phone = new ArrayList<>();
        final ArrayList<String> staff_update_date = new ArrayList<>();
        final ArrayList<String> staff_add_date = new ArrayList<>();
        final ArrayList<String> staff_device_id = new ArrayList<>();
        final ArrayList<String> staff_profile_photo = new ArrayList<>();
        final ArrayList<String> staff_docs = new ArrayList<>();
        final ArrayList<String> staff_password = new ArrayList<>();
        final ArrayList<String> staff_commision = new ArrayList<>();


        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList on addValueEventListener
                staff_id.clear();
                staff_name.clear();
                staff_email.clear();
                staff_phone.clear();
                staff_update_date.clear();
                staff_add_date.clear();
                staff_device_id.clear();
                staff_profile_photo.clear();
                staff_docs.clear();
                staff_password.clear();
                staff_commision.clear();

                while((iterator.hasNext())){
                    Staff_Model value = iterator.next().getValue(Staff_Model.class);
                    staff_id.add(value.getStaff_id());
                    staff_name.add(value.getStaff_name());
                    staff_email.add(value.getStaff_email());
                    staff_phone.add(value.getStaff_phone());
                    staff_update_date.add(value.getStaff_update_date());
                    staff_add_date.add(value.getStaff_add_date());
                    staff_device_id.add(value.getStaff_device_id());
                    staff_profile_photo.add(value.getStaff_profile_photo());
                    staff_docs.add(value.getStaff_docs());
                    staff_password.add(value.getStaff_password());
                    staff_commision.add(value.getStaff_commision());

                    ((Staff_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Staff_Adapter(staff_id, staff_name, staff_email, staff_phone, staff_update_date, staff_add_date,   staff_device_id, staff_profile_photo, staff_docs, staff_password, staff_commision, this));


    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Staff");

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

            //  home button
            case android.R.id.home:
                finish();
                return true;

            //  create button
            case R.id.action_create:
                Intent intent = new Intent(this, AddStaffActivity.class);
                startActivity(intent);
                return true;

        }

        return true;

    }


}
