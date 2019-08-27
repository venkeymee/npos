package com.bytecodecomp.npos.Activities.Assets;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;

public class AddAssetsActivity extends AppCompatActivity {

    EditText et_asset_name, et_asset_value, et_asset_date, et_asset_qnty;
    private RadioGroup radioAssetType;
    private RadioButton radioGroup;
    Button btn_submit;

    //dates values
    private int mYear, mMonth, mDay;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference assetsDatabaseReference = AppController.assetsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assets);

        //initialize toolbar
        initToolbar();

        et_asset_name = (EditText) findViewById(R.id.et_asset_name);
        et_asset_value = (EditText) findViewById(R.id.et_asset_value);
        et_asset_qnty = (EditText) findViewById(R.id.et_asset_qnty);
        et_asset_date = (EditText) findViewById(R.id.et_asset_date);
        et_asset_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAssetsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                et_asset_date.setText(year + "/" + monthOfYear + "/" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        radioAssetType =(RadioGroup)findViewById(R.id.radioGroup);

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

                    do_add_assets(et_asset_name.getText().toString(), et_asset_value.getText().toString(), et_asset_date.getText().toString(), radioGroup.getText().toString(), et_asset_qnty.getText().toString());

                }

            }
        });

    }


    //Method to add assets to firebase
    public void do_add_assets(String asset_name, String asset_value, String asset_date, String type, String qnty){

        Toasty.success(getApplicationContext(), "Asset Added", Toast.LENGTH_SHORT, true).show();

        String id = assetsDatabaseReference.push().getKey();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String customer_add_date = sdf.format(new Date());

        String customer_update_date = customer_add_date;

        Asset_Model asset_model = new Asset_Model(id, asset_name, asset_value, type, asset_date, qnty, customer_update_date, customer_add_date);
        assetsDatabaseReference.child(user.getUid()).child(id).setValue(asset_model);

        et_asset_name.setText("");
        et_asset_value.setText("");
        et_asset_date.setText("");
        et_asset_qnty.setText("");

    }


    //initialized toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Assets");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
