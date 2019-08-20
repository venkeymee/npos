package com.bytecodecomp.npos.Activities.Assets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.Asset_Model;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditAssetsActivity extends AppCompatActivity {

    EditText et_asset_name, et_asset_value, et_asset_date, et_asset_qnty;
    private RadioGroup radioAssetType;
    private RadioButton radioGroup;
    Button btn_submit;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference assetsDatabaseReference = AppController.assetsDatabaseReference;

    String asset_name, asset_qnty, asset_value,asset_date, asset_id;
    String asset_type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assets);

        initToolbar();

        Bundle bundle = getIntent().getExtras();
        asset_name = bundle.getString("asset_name");
        asset_qnty = bundle.getString("asset_qnty");
        asset_value = bundle.getString("asset_value");
        asset_date = bundle.getString("asset_date");
        asset_id = bundle.getString("asset_id");

        et_asset_name = (EditText) findViewById(R.id.et_asset_name);
        et_asset_name.setText(asset_name);

        et_asset_value = (EditText) findViewById(R.id.et_asset_value);
        et_asset_value.setText(asset_value);

        et_asset_qnty = (EditText) findViewById(R.id.et_asset_qnty);
        et_asset_qnty.setText(asset_qnty);

        et_asset_date = (EditText) findViewById(R.id.et_asset_date);
        et_asset_date.setText(asset_date);

        radioAssetType =(RadioGroup)findViewById(R.id.radioGroup);
        radioAssetType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
                asset_type = rb.getText().toString();


            }
        });


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioAssetType.getCheckedRadioButtonId();
                radioGroup =(RadioButton)findViewById(selectedId);


                if ( et_asset_name.getText().toString().length() <= 0 && et_asset_value.getText().toString().length() <= 0 && et_asset_date.getText().toString().length() <= 0 && et_asset_qnty.getText().toString().length() <= 0) {

                    Toasty.warning(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    if (asset_type.equals("0")){

                        Toasty.warning(getApplicationContext(), "Enter Asset Type", Toast.LENGTH_SHORT, true).show();

                    }

                    else {

                        do_edit_assets(et_asset_name.getText().toString(), et_asset_value.getText().toString(), et_asset_date.getText().toString(), asset_type, et_asset_qnty.getText().toString());

                    }


                }

            }
        });


    }


    //Add assets to firebase
    public void do_edit_assets(String asset_name, String asset_value, String asset_date, String type, String qnty){

        Toasty.success(getApplicationContext(), asset_name + " Edited Successfully", Toast.LENGTH_SHORT, true).show();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String customer_update_date = sdf.format(new Date());

        Asset_Model asset_model = new Asset_Model(asset_id, asset_name, asset_value, type, asset_date, qnty, customer_update_date, asset_date);
        assetsDatabaseReference.child(user.getUid()).child(asset_id).setValue(asset_model);
        finish();

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Assets");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            // home button
            case android.R.id.home:
                finish();
                return true;

        }

        return true;

    }



}
