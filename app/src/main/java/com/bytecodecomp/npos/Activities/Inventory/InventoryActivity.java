package com.bytecodecomp.npos.Activities.Inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Product_Inventory_Adapter;
import com.bytecodecomp.npos.Data_Models.Product_Inventory_Details;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class InventoryActivity extends AppCompatActivity {

    //Firebase user and dtabase reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;

    //empty layout view
    LinearLayout lyt_empty;

    final Context context = this;
    public static final int requestcode = 1;

    //Product items list
    final ArrayList<String> product_id = new ArrayList<>();
    final ArrayList<String> product_name = new ArrayList<>();
    final ArrayList<String> product_value = new ArrayList<>();
    final ArrayList<String> product_units = new ArrayList<>();
    final ArrayList<String> product_add_date = new ArrayList<>();
    final ArrayList<String> product_update_date = new ArrayList<>();
    final ArrayList<String> product_gtin = new ArrayList<>();
    final ArrayList<String> product_buying_price = new ArrayList<>();
    final ArrayList<String> product_vat = new ArrayList<>();

    String PathHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        lyt_empty = (LinearLayout) findViewById(R.id.lyt_empty);

        initToolbar();
        read_products();

    }



    //get inventory
    private void read_products(){

        inventoryDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                // clear ArrayList on addValueEventListener
                product_id.clear();
                product_name.clear();
                product_value.clear();
                product_units.clear();
                product_add_date.clear();
                product_update_date.clear();
                product_gtin.clear();
                product_buying_price.clear();
                product_vat.clear();

                while((iterator.hasNext())){

                    lyt_empty.setVisibility(View.GONE);


                    Product_Inventory_Details value = iterator.next().getValue(Product_Inventory_Details.class);
                    product_id.add(value.getProduct_id());
                    product_name.add(value.getProduct_name());
                    product_value.add(value.getProduct_value());
                    product_units.add(value.getProduct_units());
                    product_add_date.add(value.getProduct_add_date());
                    product_update_date.add(value.getProduct_update_date());
                    product_gtin.add(value.getProduct_gtin());
                    product_buying_price.add(value.getProduct_buying_price());
                    product_vat.add(value.getProduct_vat());




                    ((Product_Inventory_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView)findViewById(R.id.list_view)).setAdapter(new Product_Inventory_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, product_vat, this));
    }

    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Inventory");

    }


    //Set menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }


    //Toolbar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            //  home Button
            case android.R.id.home:
                finish();
                return true;

            //  create Button
            case R.id.action_create:
                Intent intent = new Intent(this, AddProductActivity.class);
                startActivity(intent);
                return true;

                //case import
            case R.id.action_import:
               dialog_choose();
                return true;

//                //case search
//            case R.id.action_search:
//                search_dialog();
//                return true;

        }

        return true;

        }



        //Dialog Export / Import items
        public void dialog_choose(){

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Import or Export Inventory");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Export",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            import_inventory();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Import",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            open_file_chooser();
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }


        //Export product list from firebase
        public String get_json_inventory_from_firebase(String json){

        json = "[ ";
        for (int i = 0; i <= product_id.size(); i++){

            if (i < product_id.size()){

                json = json + "{ " + "\"product_name\" : \"" + product_name.get(i) + "\"," + "\"product_gtin\" : \"" + product_gtin.get(i) + "\","  + "\"product_units\" : \"" + product_units.get(i) + "\"," +  "\"product_value\" : \"" + product_value.get(i) + "\"," +  "\"product_vat\" : \"" + product_vat.get(i) + "\","  + "\"product_buying_price\" : \"" + product_buying_price.get(i) + "\" } ,";
                Log.e("json...", i + " : " + json);

            }

        }

//        Log.e("Json response...", json);
        return  json.replaceFirst(".$","]");

        }


        //Import product list from user to firebase
        public void import_inventory(){

            try {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File myFile = new File(path, "inventory.json");
                FileOutputStream fOut = new FileOutputStream(myFile,true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(get_json_inventory_from_firebase(""));
                myOutWriter.close();
                fOut.close();

                Toasty.success(this, "JSON file Exported, find it at Downloads ").show();

            }

            catch (java.io.IOException e) {

                Toasty.error(this, "Error file couldn't be Exported, Check Permissions").show();

            }

        }


        //Choose file to import
        public void get_import_file(){

//            Log.e("file...", PathHolder);
            String imported_items = "";


            ///document/raw:
            try {
                FileInputStream fileInputStream = new FileInputStream(PathHolder);
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    imported_items = imported_items + strLine;
                }
//                Log.e("Text file", imported_items);
                do_import_json_product(imported_items);
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        //Import products
        public void do_import_json_product(final String items){

            try {
                JSONArray jsonArray = new JSONArray(items);
                for (int i = 0; i <= jsonArray.length(); i ++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String str_product_name = jsonObject.getString("product_name");
                    String str_product_gtin = jsonObject.getString("product_gtin");
                    String str_product_units = jsonObject.getString("product_units");
                    String str_product_value = jsonObject.getString("product_value");
                    String str_product_buying_price = jsonObject.getString("product_buying_price");
                    String str_product_vat = jsonObject.getString("product_vat");


                    String product_id = inventoryDatabaseReference.push().getKey();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                    String product_add_date = sdf.format(new Date());
                    String product_update_date = product_add_date;

                    Product_Inventory_Details product_inventory_details = new Product_Inventory_Details(product_id, str_product_name, str_product_value, str_product_units, product_add_date, product_update_date, str_product_gtin, str_product_buying_price, str_product_vat);
                    inventoryDatabaseReference.child(user.getUid()).child(product_id).setValue(product_inventory_details);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        public void search_product(String value){

            inventoryDatabaseReference.child(user.getUid()).orderByChild("product_gtin").equalTo(value);
            inventoryDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    // clear ArrayList on addValueEventListener
                    product_id.clear();
                    product_name.clear();
                    product_value.clear();
                    product_units.clear();
                    product_add_date.clear();
                    product_update_date.clear();
                    product_gtin.clear();
                    product_buying_price.clear();
                    product_vat.clear();

                    while((iterator.hasNext())){

                        lyt_empty.setVisibility(View.GONE);


                        Product_Inventory_Details value = iterator.next().getValue(Product_Inventory_Details.class);
                        product_id.add(value.getProduct_id());
                        product_name.add(value.getProduct_name());
                        product_value.add(value.getProduct_value());
                        product_units.add(value.getProduct_units());
                        product_add_date.add(value.getProduct_add_date());
                        product_update_date.add(value.getProduct_update_date());
                        product_gtin.add(value.getProduct_gtin());
                        product_buying_price.add(value.getProduct_buying_price());
                        product_vat.add(value.getProduct_vat());




                        ((Product_Inventory_Adapter)(((ListView)findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            ((ListView)findViewById(R.id.list_view)).setAdapter(new Product_Inventory_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, product_vat, this));


        }


        public void search_dialog(){

            final Dialog dialog = new Dialog(InventoryActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_search_inventory);
            dialog.setCancelable(false);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
            txt_title.setText("Enter Product SKU " + product_name);

            final EditText edt_search = (EditText) dialog.findViewById(R.id.edt_search);

            ((AppCompatButton) dialog.findViewById(R.id.bt_continue)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (edt_search.getText().toString().length() <= 0){

                        Toasty.warning(InventoryActivity.this , "Enter Expense Name", Toast.LENGTH_SHORT, true).show();

                    }

                    else {

                        search_product(edt_search.getText().toString());
                        dialog.dismiss();

                    }

                }
            });


            ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

        }


        //Open file choose to select file
        public void open_file_chooser(){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, 7);

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){

            case 7:
                if(resultCode==RESULT_OK){
                    PathHolder = data.getData().getPath().toString().replace("/document/raw:", "");
                    get_import_file();
                }
                break;
        }
    }




}
