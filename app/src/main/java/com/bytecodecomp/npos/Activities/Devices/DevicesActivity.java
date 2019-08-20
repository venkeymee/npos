package com.bytecodecomp.npos.Activities.Devices;

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
import com.bytecodecomp.npos.Adapters.Device_Adapter;
import com.bytecodecomp.npos.Data_Models.Device_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class DevicesActivity extends AppCompatActivity {

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference deviceDatabaseReference = AppController.deviceDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        initToolbar();
        read_devices();

    }


    //get devices from firebase
    public void read_devices(){

        final ArrayList<String> device_name = new ArrayList<>();
        final ArrayList<String> device_id = new ArrayList<>();
        final ArrayList<String> device_serial = new ArrayList<>();
        final ArrayList<String> device_role = new ArrayList<>();

        deviceDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList before a new value listener
                device_name.clear();
                device_id.clear();
                device_serial.clear();

                while((iterator.hasNext())){

                    Device_Model value = iterator.next().getValue(Device_Model.class);
                    device_name.add(value.getDevice_name());
                    device_id.add(value.getDevice_id());
                    device_serial.add(value.getDevice_serial());
                    device_role.add(value.getDevice_role());

                    ((Device_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Device_Adapter(device_name, device_id, device_serial, device_role, this));


    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Devices");

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

                Intent intent = new Intent(this, CreateDeviceActivity.class);
                startActivity(intent);

                return true;

        }

        return true;

    }

}
