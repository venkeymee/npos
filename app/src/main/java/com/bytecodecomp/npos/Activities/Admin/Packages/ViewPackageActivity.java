package com.bytecodecomp.npos.Activities.Admin.Packages;

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

public class ViewPackageActivity extends AppCompatActivity {

    TextView txt_package_name, txt_package_days, txt_package_value, txt_module_inventory, txt_module_purchase, txt_module_sales, txt_module_customers, txt_module_suppliers, txt_module_staff, txt_module_assets, txt_module_exp_type, txt_module_exp;
    Button btn_submit;

    String store_inventory_limit, store_purchase_daily_limit, store_sales_daily_limit, store_customer_limit, store_supplier_limit, store_staff_limit, store_assets_limit, store_exp_type_limit, store_exp_limit,
            package_name, package_exp, package_cost, package_id, package_period;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package);

        Bundle bundle = getIntent().getExtras();
        store_inventory_limit = bundle.getString("store_inventory_limit");
        store_purchase_daily_limit = bundle.getString("store_purchase_daily_limit");
        store_sales_daily_limit = bundle.getString("store_sales_daily_limit");
        store_customer_limit = bundle.getString("store_customer_limit");
        store_supplier_limit = bundle.getString("store_supplier_limit");
        store_staff_limit = bundle.getString("store_staff_limit");
        store_assets_limit = bundle.getString("store_assets_limit");
        store_exp_type_limit = bundle.getString("store_exp_type_limit");
        store_exp_limit = bundle.getString("store_exp_limit");
        package_name = bundle.getString("package_name");
        package_exp = bundle.getString("package_exp");
        package_cost = bundle.getString("package_cost");
        package_id = bundle.getString("package_id");
        package_period = bundle.getString("package_period");

        initToolbar();

        txt_package_name = (TextView) findViewById(R.id.txt_package_name);
        txt_package_name.setText(package_name + "");
        txt_package_days = (TextView) findViewById(R.id.txt_package_days);
        txt_package_days.setText(package_period + "");
        txt_package_value = (TextView) findViewById(R.id.txt_package_value);
        txt_package_value.setText(package_cost);

        txt_module_inventory = (TextView) findViewById(R.id.txt_module_inventory);
        txt_module_inventory.setText(store_inventory_limit);
        txt_module_purchase = (TextView) findViewById(R.id.txt_module_purchase);
        txt_module_purchase.setText(store_purchase_daily_limit);
        txt_module_sales = (TextView) findViewById(R.id.txt_module_sales);
        txt_module_sales.setText(store_sales_daily_limit);
        txt_module_customers = (TextView) findViewById(R.id.txt_module_customers);
        txt_module_customers.setText(store_customer_limit);
        txt_module_suppliers = (TextView) findViewById(R.id.txt_module_suppliers);
        txt_module_suppliers.setText(store_supplier_limit);
        txt_module_staff = (TextView) findViewById(R.id.txt_module_staff);
        txt_module_staff.setText(store_staff_limit);
        txt_module_assets = (TextView) findViewById(R.id.txt_module_assets);
        txt_module_assets.setText(store_assets_limit);
        txt_module_exp_type = (TextView) findViewById(R.id.txt_module_exp_type);
        txt_module_exp_type.setText(store_exp_type_limit);
        txt_module_exp = (TextView) findViewById(R.id.txt_module_exp);
        txt_module_exp.setText(store_exp_limit);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewPackageActivity.this, EditPackageActivity.class);
                intent.putExtra("store_inventory_limit", store_inventory_limit);
                intent.putExtra("store_purchase_daily_limit", store_purchase_daily_limit);
                intent.putExtra("store_sales_daily_limit", store_sales_daily_limit);
                intent.putExtra("store_customer_limit", store_customer_limit);
                intent.putExtra("store_supplier_limit", store_supplier_limit);
                intent.putExtra("store_staff_limit", store_staff_limit);
                intent.putExtra("store_assets_limit", store_assets_limit);
                intent.putExtra("store_exp_type_limit", store_exp_type_limit);
                intent.putExtra("store_exp_limit", store_exp_limit);
                intent.putExtra("package_name", package_name);
                intent.putExtra("package_exp", package_exp);
                intent.putExtra("package_cost", package_cost);
                intent.putExtra("package_id", package_id);
                intent.putExtra("package_period", package_period);
                startActivity(intent);

            }
        });

    }



    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Package");

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
                Intent intent = new Intent(ViewPackageActivity.this, EditPackageActivity.class);
                intent.putExtra("store_inventory_limit", store_inventory_limit);
                intent.putExtra("store_purchase_daily_limit", store_purchase_daily_limit);
                intent.putExtra("store_sales_daily_limit", store_sales_daily_limit);
                intent.putExtra("store_customer_limit", store_customer_limit);
                intent.putExtra("store_supplier_limit", store_supplier_limit);
                intent.putExtra("store_staff_limit", store_staff_limit);
                intent.putExtra("store_assets_limit", store_assets_limit);
                intent.putExtra("store_exp_type_limit", store_exp_type_limit);
                intent.putExtra("store_exp_limit", store_exp_limit);
                intent.putExtra("package_name", package_name);
                intent.putExtra("package_exp", package_exp);
                intent.putExtra("package_cost", package_cost);
                intent.putExtra("package_id", package_id);
                intent.putExtra("package_period", package_period);
                startActivity(intent);
                return true;

        }

        return true;

    }


}
