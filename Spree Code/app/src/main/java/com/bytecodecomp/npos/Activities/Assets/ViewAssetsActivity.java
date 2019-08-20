package com.bytecodecomp.npos.Activities.Assets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.Tools;

public class ViewAssetsActivity extends AppCompatActivity {

    TextView txt_asset_name, txt_asset_qnty, txt_asset_value, txt_asset_date;
    String asset_name, asset_qnty, asset_value,asset_date, asset_id;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assets);

        initToolbar();

        Bundle bundle = getIntent().getExtras();
        asset_name = bundle.getString("asset_name");
        asset_qnty = bundle.getString("asset_qnty");
        asset_value = bundle.getString("asset_value");
        asset_date = bundle.getString("asset_date");
        asset_id = bundle.getString("asset_id");

        txt_asset_name = (TextView) findViewById(R.id.txt_asset_name);
        txt_asset_name.setText(asset_name);

        txt_asset_qnty = (TextView) findViewById(R.id.txt_asset_qnty);
        txt_asset_qnty.setText(asset_qnty);

        txt_asset_value = (TextView) findViewById(R.id.txt_asset_value);
        txt_asset_value.setText(asset_value);

        txt_asset_date = (TextView) findViewById(R.id.txt_asset_date);
        txt_asset_date.setText(asset_date);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewAssetsActivity.this, EditAssetsActivity.class);
                intent.putExtra("asset_name", asset_name);
                intent.putExtra("asset_qnty", asset_qnty);
                intent.putExtra("asset_value", asset_value);
                intent.putExtra("asset_date", asset_date);
                intent.putExtra("asset_id", asset_id);
                startActivity(intent);
                finish();

            }
        });

    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Assets");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

                // edit button
            case R.id.action_edit:
                Intent intent = new Intent(this, EditAssetsActivity.class);
                intent.putExtra("asset_name", asset_name);
                intent.putExtra("asset_qnty", asset_qnty);
                intent.putExtra("asset_value", asset_value);
                intent.putExtra("asset_date", asset_date);
                intent.putExtra("asset_id", asset_id);
                startActivity(intent);
                return true;

        }

        return true;

    }

}
