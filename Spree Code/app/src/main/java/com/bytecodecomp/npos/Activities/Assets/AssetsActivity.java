package com.bytecodecomp.npos.Activities.Assets;

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
import com.bytecodecomp.npos.Adapters.Assets_Adapter;
import com.bytecodecomp.npos.Data_Models.Asset_Model;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class AssetsActivity extends AppCompatActivity {

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference assetsDatabaseReference = AppController.assetsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        initToolbar();
        read_assets();

    }


    //Get assets to listview
    private void read_assets(){

        final ArrayList<String> asset_id = new ArrayList<>();
        final ArrayList<String> asset_name = new ArrayList<>();
        final ArrayList<String> asset_value = new ArrayList<>();
        final ArrayList<String> asset_type = new ArrayList<>();
        final ArrayList<String> asset_qnty = new ArrayList<>();
        final ArrayList<String> asset_purchase_Date = new ArrayList<>();
        final ArrayList<String> asset_update_date = new ArrayList<>();
        final ArrayList<String> asset_add_date = new ArrayList<>();

        assetsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //Clear ArrayList before a new value listener
                asset_id.clear();
                asset_name.clear();
                asset_value.clear();
                asset_type.clear();
                asset_qnty.clear();
                asset_purchase_Date.clear();
                asset_update_date.clear();
                asset_add_date.clear();

                while((iterator.hasNext())){
                    Asset_Model value = iterator.next().getValue(Asset_Model.class);
                    asset_id.add(value.getAsset_id());
                    asset_name.add(value.getAsset_name());
                    asset_value.add(value.getAsset_value());
                    asset_type.add(value.getAsset_type());
                    asset_qnty.add(value.getAsset_qnty());
                    asset_purchase_Date.add(value.getAsset_purchase_Date());
                    asset_update_date.add(value.getAsset_update_date());
                    asset_add_date.add(value.getAsset_add_date());
                    ((Assets_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Assets_Adapter(asset_id, asset_name, asset_value, asset_type, asset_qnty, asset_purchase_Date, asset_update_date, asset_add_date, this));
    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Assets");

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

                Intent intent = new Intent(this, AddAssetsActivity.class);
                startActivity(intent);

                return true;

        }

        return true;

    }


}
