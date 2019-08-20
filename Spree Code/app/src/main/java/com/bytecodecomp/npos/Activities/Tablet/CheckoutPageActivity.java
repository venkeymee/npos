package com.bytecodecomp.npos.Activities.Tablet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.bytecodecomp.npos.Adapters.Checkout_Page_Adapter;
import com.bytecodecomp.npos.Adapters.Checkout_Inventory_Adapter;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Customer_Model;
import com.bytecodecomp.npos.Data_Models.Product_Cart_Details;
import com.bytecodecomp.npos.Data_Models.Product_Inventory_Details;
import com.bytecodecomp.npos.Data_Models.Sales_Model;
import com.bytecodecomp.npos.Data_Models.Staff_Model;
import com.bytecodecomp.npos.MainActivity;
import com.bytecodecomp.npos.Plugins.Printer.util.DateUtil;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.AppUtils;
import com.bytecodecomp.npos.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CheckoutPageActivity extends AppCompatActivity {

    Dialog dialog;

    GridView grd_items;
    ListView list_view;
    CardView card_cash, card_paypal, card_credit, card_bank;

    public TextView txt_total_amount, txt_total_items, txt_total_vat, txt_subtotal_vat, txt_customer_discount;
    TextView txt_staff_name, txt_customer_name;
    

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference cartDatabaseReference = AppController.cartDatabaseReference;
    DatabaseReference salesDatabaseReference = AppController.salesDatabaseReference;
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;
    DatabaseReference usersDatabaseReference = AppController.usersDatabaseReference;
    DatabaseReference customerDatabaseReference = AppController.customerDatabaseReference;

    ValueEventListener cart_listener = AppController.listener;
    
    ImageView user_menu;
    String staff_name = "Not Set";
    String payment_gateway = "Not Set";
    String customer_name = "Not Set";
    int customer_discount = 0;
    double vat_total = 0;
    double vat_sub_total = 0;
    double customer_credit_limit = 0;
    double paymentAmount = 0, item_count = 0;
    public static boolean onsale = false;
    public static String saved_receipt = "";

    String Sale_Receipt = AppUtils.getRandomString(App_Settings.SALE_RECEIPT_ID);

    JSONArray jsonArray = new JSONArray();

    final ArrayList<String> product_id_cart = new ArrayList<>();
    final ArrayList<String> product_name_cart = new ArrayList<>();
    final ArrayList<String> product_value_cart = new ArrayList<>();
    final ArrayList<String> product_units_cart = new ArrayList<>();
    final ArrayList<String> product_add_date_cart = new ArrayList<>();
    final ArrayList<String> product_update_date_cart = new ArrayList<>();
    final ArrayList<String> product_gtin_cart = new ArrayList<>();
    final ArrayList<String> product_inventory_cart = new ArrayList<>();
    final ArrayList<String> product_inventory_vat = new ArrayList<>();

    public static ArrayList<String> ids_list = new ArrayList<String>();
    public static ArrayList<String> count_list = new ArrayList<String>();
    public static ArrayList<String> unit_list = new ArrayList<String>();

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(App_Settings.PAYPAL_CLIENT_ID);


    String  date;
    String Currency = App_Settings.CURRENCY_TYPE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initToolbar();

        txt_total_amount = (TextView) findViewById(R.id.txt_total_amount);
        txt_total_items = (TextView) findViewById(R.id.txt_total_items);
        txt_total_vat = (TextView) findViewById(R.id.txt_total_vat);
        txt_subtotal_vat = (TextView) findViewById(R.id.txt_subtotal_vat);
        txt_customer_discount = (TextView) findViewById(R.id.txt_customer_discount);

        txt_staff_name = (TextView) findViewById(R.id.txt_staff_name);
        txt_staff_name.setText(staff_name);
        txt_customer_name = (TextView) findViewById(R.id.txt_customer_name);
        txt_customer_name.setText(customer_name);
        txt_customer_discount.setText(customer_discount + " " + App_Settings.CURRENCY_TYPE);

        grd_items = (GridView) findViewById(R.id.grd_items);
        list_view = (ListView) findViewById(R.id.list_view);
        txt_total_items = (TextView) findViewById(R.id.txt_total_items);


        card_cash = (CardView) findViewById(R.id.card_cash);
        card_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App_Settings.require_staff_on_checkout == 0){

                    date = new SimpleDateFormat("yyyy-MM-dd / h:mm a", Locale.getDefault()).format(new Date());
                    show_payments_status_cash();

                }

                else {

                    if (staff_name.equals("Not Set")){

                        Toasty.warning(CheckoutPageActivity.this, "Choose Staff").show();


                    }

                    else {

                        date = new SimpleDateFormat("yyyy-MM-dd / h:mm a", Locale.getDefault()).format(new Date());
                        show_payments_status_cash();

                    }

                }



            }
        });


        card_paypal = (CardView) findViewById(R.id.card_paypal);
        card_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_up_paypal("");

            }
        });


        card_credit = (CardView) findViewById(R.id.card_credit);
        card_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //If customer has not been chosen
                if (staff_name.equals("Not Set")){

                    Toasty.warning(CheckoutPageActivity.this, "Choose Customer").show();

                }

                else {


                    //If customer credit is more or equal to that of sub total
                    if (customer_credit_limit >= paymentAmount){

                        if (App_Settings.require_staff_on_checkout == 0){

                            date = new SimpleDateFormat("yyyy-MM-dd / h:mm a", Locale.getDefault()).format(new Date());
                            show_payments_status_credit();

                        }

                        else {

                            if (staff_name.equals("Not Set")){

                                Toasty.warning(CheckoutPageActivity.this, "Choose Staff").show();

                            }


                            else {

                                date = new SimpleDateFormat("yyyy-MM-dd / h:mm a", Locale.getDefault()).format(new Date());
                                show_payments_status_credit();

                            }

                        }

                    }


                    //If customer credit is less than that of sub total
                    else {

                        Toasty.warning(CheckoutPageActivity.this, "Customer Credit limit is " + customer_credit_limit).show();

                    }



                }


            }
        });

        card_bank = (CardView) findViewById(R.id.card_bank);
        card_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App_Settings.require_staff_on_checkout == 0){

                    date = new SimpleDateFormat("yyyy-MM-dd / h:mm a", Locale.getDefault()).format(new Date());
                    do_bank_number_dialog();

                }

                else {

                    if (staff_name.equals("Not Set")){

                        Toasty.warning(CheckoutPageActivity.this, "Choose Staff").show();

                    }


                    else {

                        date = new SimpleDateFormat("yyyy-MM-dd / h:mm a", Locale.getDefault()).format(new Date());
                        do_bank_number_dialog();

                    }



                }



            }
        });

        list_view = (ListView) findViewById(R.id.list_view);

        do_list();
        do_cart_list();

    }


    public void do_list(){

        final ArrayList<String> product_id = new ArrayList<>();
        final ArrayList<String> product_name = new ArrayList<>();
        final ArrayList<String> product_value = new ArrayList<>();
        final ArrayList<String> product_units = new ArrayList<>();
        final ArrayList<String> product_add_date = new ArrayList<>();
        final ArrayList<String> product_update_date = new ArrayList<>();
        final ArrayList<String> product_gtin = new ArrayList<>();
        final ArrayList<String> product_buying_price = new ArrayList<>();
        final ArrayList<String> product_vat = new ArrayList<>();


        inventoryDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                //clear ArrayList on addValueEventListener
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

                    Product_Inventory_Details value = iterator.next().getValue(Product_Inventory_Details.class);

                    Log.e("VAT no", value.getProduct_vat() + " ");

                    product_id.add(value.getProduct_id());
                    product_name.add(value.getProduct_name());
                    product_value.add(value.getProduct_value());
                    product_units.add(value.getProduct_units());
                    product_add_date.add(value.getProduct_add_date());
                    product_update_date.add(value.getProduct_update_date());
                    product_gtin.add(value.getProduct_gtin());
                    product_buying_price.add(value.getProduct_buying_price());
                    product_vat.add(value.getProduct_vat());

                    ((Checkout_Inventory_Adapter)(((GridView)findViewById(R.id.grd_items)).getAdapter())).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((GridView)findViewById(R.id.grd_items)).setAdapter(new Checkout_Inventory_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, product_vat, this));

    }

    public void do_cart_list(){

        cart_listener = cartDatabaseReference.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("Cart_Count", " " + dataSnapshot.getChildrenCount());

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                if (!iterator.hasNext()){

                    if (onsale == true){

                        Log.e("JsonArray...", jsonArray.toString());
                        do_receipt_print(saved_receipt);

                    }

                    else {

                        do_items_clear();

                    }

                    txt_total_items.setText("Total Items : " + 0);
                    ((Checkout_Page_Adapter) (list_view.getAdapter())).notifyDataSetChanged();
                    Toasty.warning(getApplicationContext(), "Cart Empty", Toast.LENGTH_SHORT, true).show();




                }

                else {

                    do_items_clear();

                    while((iterator.hasNext())) {
                        Product_Cart_Details value = iterator.next().getValue(Product_Cart_Details.class);


                        try {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("gtin", value.getProduct_gtin());
                            jsonObject.put("value", value.getProduct_value());
                            jsonObject.put("name", value.getProduct_name());
                            jsonObject.put("units", value.getProduct_units());
                            jsonArray.put(jsonObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        };

                        ids_list.add(value.getProduct_id());
                        count_list.add(value.getProduct_units());
                        unit_list.add(value.getProduct_inventory());

                        product_id_cart.add(value.getProduct_id());
                        product_name_cart.add(value.getProduct_name());
                        product_value_cart.add(value.getProduct_value());
                        product_units_cart.add(value.getProduct_units());
                        product_add_date_cart.add(value.getProduct_add_date());
                        product_update_date_cart.add(value.getProduct_update_date());
                        product_gtin_cart.add(value.getProduct_gtin());
                        product_inventory_cart.add(value.getProduct_inventory());
                        product_inventory_vat.add(value.getProduct_vat());



                        double subtotal = Double.parseDouble(value.getProduct_value());
                        double units = Double.parseDouble(value.getProduct_units());
                        double total = subtotal * units;

                        String str_vat = value.getProduct_vat() + " ";

                        if (!str_vat.equals("null ")  && str_vat.length() < 1){

//                            Log.e("VAT...", str_vat);
                            double vat = Double.parseDouble(value.getProduct_vat().replace(" ", "")) / 100;
                            double item_vat = total * vat;

                            vat_total = vat_total + item_vat;

                        }

                        paymentAmount = paymentAmount + total;
                        item_count = item_count + units;

                        vat_sub_total = paymentAmount - vat_total;

                        if (customer_discount != 0){

                            paymentAmount = paymentAmount - customer_discount;
                            vat_sub_total = vat_sub_total - customer_discount;
                            txt_total_amount.setText(App_Settings.CURRENCY_TYPE + " " + paymentAmount);
                            txt_subtotal_vat.setText(App_Settings.CURRENCY_TYPE + " " + vat_sub_total);

                        }

                        else {

                            txt_total_amount.setText(App_Settings.CURRENCY_TYPE + " " + paymentAmount);
                            txt_subtotal_vat.setText(App_Settings.CURRENCY_TYPE + " " + vat_sub_total);

                        }

                        txt_total_items.setText("Total Items : " + item_count);
                        txt_total_vat.setText(App_Settings.CURRENCY_TYPE + " " + vat_total);


                        ((Checkout_Page_Adapter) (list_view.getAdapter())).notifyDataSetChanged();

                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        list_view.setAdapter(new Checkout_Page_Adapter(product_id_cart,product_name_cart, product_value_cart, product_units_cart, product_add_date_cart, product_update_date_cart, product_gtin_cart, dialog, this, cartDatabaseReference, product_inventory_cart, product_inventory_vat));


    }

    public void do_items_clear(){

        product_id_cart.clear();
        product_name_cart.clear();
        product_value_cart.clear();
        product_units_cart.clear();
        product_add_date_cart.clear();
        product_update_date_cart.clear();
        product_gtin_cart.clear();

        jsonArray = new JSONArray(new ArrayList<String>());

        ids_list.clear();
        unit_list.clear();
        count_list.clear();

        paymentAmount = 0;
        item_count = 0;
        vat_total = 0;
        vat_sub_total = 0;

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checkout");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            // Android home
            case android.R.id.home:

                Intent intent = new Intent(CheckoutPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                return true;

            //Enter Discount dialog
            case R.id.action_discount:

                final Dialog dialog_discount = new Dialog(CheckoutPageActivity.this);
                dialog_discount.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog_discount.setContentView(R.layout.dialog_discount);
                dialog_discount.setCancelable(false);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog_discount.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                TextView discount_txt = (TextView) dialog_discount.findViewById(R.id.title_txt);
                discount_txt.setText("Discount Value : " + customer_discount);

                final EditText edt_discount_value = (EditText) dialog_discount.findViewById(R.id.edt_discount_value);

                AppCompatButton btn_close = (AppCompatButton) dialog_discount.findViewById(R.id.bt_close);
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (edt_discount_value.getText().toString().length() <= 0){

                            dialog_discount.dismiss();

                        }

                        else {

                            customer_discount = Integer.parseInt(edt_discount_value.getText().toString());
                            txt_customer_discount.setText(customer_discount + " " + App_Settings.CURRENCY_TYPE);
                            dialog_discount.dismiss();

                        }

                    }
                });


                dialog_discount.show();
                dialog_discount.getWindow().setAttributes(lp);

                return true;

            //Choose customer dialog
            case R.id.action_customer:

                final List<String> customer_name_list = new ArrayList<String>();
                final List<String> customer_credit_list = new ArrayList<String>();

                final Dialog dialogs = new Dialog(CheckoutPageActivity.this);
                dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialogs.setContentView(R.layout.dialog_staff);
                dialogs.setCancelable(false);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialogs.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                TextView title_txt = (TextView) dialogs.findViewById(R.id.title_txt);
                title_txt.setText("Choose Customer " + customer_name);

                final Spinner spinner = (Spinner) dialogs.findViewById(R.id.spn_staff);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        customer_name = parent.getItemAtPosition(position).toString();
                        customer_credit_limit = Double.parseDouble(customer_credit_list.get(position));
                        Log.e("Credit...", customer_credit_limit + " ");
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                customerDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                        while((iterator.hasNext())) {
                            Customer_Model value = iterator.next().getValue(Customer_Model.class);
                            String name = value.getCustomer_name();
                            String credit = value.getCustomer_credit();
                            customer_name_list.add(name);
                            customer_credit_list.add(credit);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CheckoutPageActivity.this, android.R.layout.simple_spinner_item, customer_name_list);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                AppCompatButton bt_close = (AppCompatButton) dialogs.findViewById(R.id.bt_close);
                bt_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("Clicked", customer_name);
                        txt_customer_name.setText(customer_name);
                        dialogs.dismiss();

                    }
                });

                dialogs.show();
                dialogs.getWindow().setAttributes(layoutParams);

                return true;

            //choose staff dialog
            case R.id.action_staff:

                final Dialog dialog = new Dialog(CheckoutPageActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_staff);
                dialog.setCancelable(false);

                WindowManager.LayoutParams lps = new WindowManager.LayoutParams();
                lps.copyFrom(dialog.getWindow().getAttributes());
                lps.width = WindowManager.LayoutParams.MATCH_PARENT;
                lps.height = WindowManager.LayoutParams.WRAP_CONTENT;

                TextView staff_txt = (TextView) dialog.findViewById(R.id.title_txt);
                staff_txt.setText("Choose Staff " + staff_name);

                final Spinner spinners = (Spinner) dialog.findViewById(R.id.spn_staff);
                spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        staff_name = parent.getItemAtPosition(position).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                usersDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<String> nomeConsulta = new ArrayList<String>();
                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                        while((iterator.hasNext())) {
                            Staff_Model value = iterator.next().getValue(Staff_Model.class);
                            String consultaName = value.getStaff_name();
                            nomeConsulta.add(consultaName);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CheckoutPageActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinners.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                AppCompatButton bt_closes = (AppCompatButton) dialog.findViewById(R.id.bt_close);
                bt_closes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("Clicked", staff_name);
                        txt_staff_name.setText(staff_name);
                        dialog.dismiss();

                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lps);

                return true;

        }

        return true;
    }



    public void set_up_paypal(String amount){

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        getPayment();

    }



    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void getPayment() {

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), Currency  , Sale_Receipt,
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        show_payments_status_paypal(paymentDetails, paymentAmount + "");

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    //Complete paypal checkout func
    public void show_payments_status_paypal(String PaymentDetails, String paymentAmount){

        try {
            JSONObject jsonDetails = new JSONObject(PaymentDetails);

            onsale = true;
            do_product_update();
            payment_gateway = "Paypal";
            saved_receipt = print_receipt();

            String id = salesDatabaseReference.push().getKey();
            Sales_Model sales_model = new Sales_Model( date,  Sale_Receipt,  "paypal_" + Sale_Receipt, "paypal", print_receipt(), user.getUid(), user.getEmail(), paymentAmount + "" , staff_name, customer_name, customer_discount + "");
            salesDatabaseReference.child(user.getUid()).child(id).setValue(sales_model);
            cartDatabaseReference.child(user.getUid()).removeValue();


        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //Complete cash checkout func
    public void show_payments_status_cash(){

        onsale = true;
        do_product_update();
        payment_gateway = "Cash";
        saved_receipt = print_receipt();

        String id = salesDatabaseReference.push().getKey();
        Sales_Model sales_model = new Sales_Model( date,  Sale_Receipt,  "cash_" + Sale_Receipt, payment_gateway, print_receipt(), user.getUid(), user.getEmail(), paymentAmount + "" , staff_name, customer_name, customer_discount + "");
        salesDatabaseReference.child(user.getUid()).child(id).setValue(sales_model);
        cartDatabaseReference.child(user.getUid()).removeValue();


    }



    //Complete credit checkout func
    public void show_payments_status_credit(){

        onsale = true;
        do_product_update();
        payment_gateway = "Credit";
        saved_receipt = print_receipt();

        String id = salesDatabaseReference.push().getKey();
        Sales_Model sales_model = new Sales_Model( date,  Sale_Receipt,  "credit_" + Sale_Receipt, payment_gateway, print_receipt(), user.getUid(), user.getEmail(), paymentAmount + "" , staff_name, customer_name, customer_discount+ "");
        salesDatabaseReference.child(user.getUid()).child(id).setValue(sales_model);
        cartDatabaseReference.child(user.getUid()).removeValue();

    }


    //Complete bank checkout func
    public void show_payments_status_bank(final String slip_id){

        onsale = true;
        do_product_update();
        payment_gateway = "Bank";
        saved_receipt = print_receipt();

        Sales_Model sales_model = new Sales_Model( date,  Sale_Receipt,  "bank_" + Sale_Receipt, payment_gateway, print_receipt(), user.getUid(), user.getEmail(), paymentAmount + "" , staff_name, customer_name, customer_discount+ "");
        salesDatabaseReference.child(user.getUid()).child(slip_id).setValue(sales_model);
        cartDatabaseReference.child(user.getUid()).removeValue();

    }


    //Enter bank slip dialog
    public void do_bank_number_dialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_bank_number);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText edt_bank = (EditText) dialog.findViewById(R.id.edt_bank);

        ((AppCompatButton) dialog.findViewById(R.id.bt_continue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_bank.getText().toString().length() <= 0){

                    Toasty.warning(CheckoutPageActivity.this, "Enter Bank Slip Number", Toast.LENGTH_SHORT, true).show();

                }

                else {

                    String slip_id = edt_bank.getText().toString();
                    show_payments_status_bank(slip_id);
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


    //Update product inventory
    public void do_product_update(){

        Iterator iter = ids_list.iterator();
        while (iter.hasNext())
        {

            int inventory_stock = Integer.parseInt(unit_list.get(0)) - Integer.parseInt(count_list.get(0));
            String value = String.valueOf(inventory_stock);
            Log.e("value,,,", value);
            inventoryDatabaseReference.child(user.getUid()).child(String.valueOf(iter.next())).child("product_units").setValue(value);

        }


    }


    //Checks if store if print is enabled
    public void do_receipt_print(String BILL){

        if (App_Settings.store_print.equals("YES")){

            Intent intents = new Intent(this, com.bytecodecomp.npos.Plugins.Printer.MainActivity.class);
            intents.putExtra("action", BILL);
            App_Settings.current_activity = "CheckoutPageActivity";
            onsale = false;
            startActivity(intents);
            finish();

        }



    }

    //print receipt
    public String print_receipt(){

        long milis		= System.currentTimeMillis();
        String date		= DateUtil.timeMilisToString(milis, "MMM-dd-yyyy");

        String BILL = "" + "\n";

        BILL =  BILL + App_Settings.store_name + "\n"
                + "TEL :: " + App_Settings.store_contacts + "\n"
                + "TIME :: "  + date + "\n"
                + "TRANSACTION ID :: "  + Sale_Receipt + "\n";


        BILL = BILL + "--------------------------------\n";


        BILL = BILL + String.format("%1$2s %2$5s %3$5s %4$5s", " # ", "Item/s", "Units", " S-Total");
        BILL = BILL + "\n================================" ;

        String BILL_ITEMS = "\n";


        for (int i = 0; i <= jsonArray.length(); i ++){

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                BILL_ITEMS = BILL_ITEMS + "\n " + String.format("%1$2s %2$5s %3$5s %4$5s",i , jsonObject.get("name"), jsonObject.get("units"), jsonObject.get("value"));
                BILL_ITEMS = BILL_ITEMS + "\n" + "--------------------------------";

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        BILL = BILL + BILL_ITEMS + "\n\n";

        BILL = BILL + "================================\n\n";

        BILL = BILL + "Total Item/s  ::  " + item_count + "  Units" + "\n";
        BILL = BILL + "Total VAT Value ::  " + vat_sub_total + " " + App_Settings.CURRENCY_TYPE + "\n";
        BILL = BILL + "Sub Total Value ::  " + paymentAmount + " " + App_Settings.CURRENCY_TYPE + "\n";
        BILL = BILL + "Discounts Value ::  " + customer_discount + " " + App_Settings.CURRENCY_TYPE + "\n";

        BILL = BILL + "\n================================\n\n";

        BILL = BILL + "Payment Gateway :: " + payment_gateway + "\n";
        BILL = BILL + "Customer Name ::  " + txt_customer_name.getText().toString() + "\n";
        BILL = BILL + "You were served by :: " + txt_customer_name.getText().toString() + "\n";

        BILL = BILL + "================================\n\n";

        BILL = BILL + "Thank you for shopping with us  Provided by POS" + "\n";

        BILL = BILL + "================================\n\n\n";

        Log.e("BILL", BILL);

        return BILL;

    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }




}
