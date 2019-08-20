package com.bytecodecomp.npos.Activities.Admin.Packages;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Subscriptions_Model;
import com.bytecodecomp.npos.MainActivity;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.bytecodecomp.npos.Activities.Sales.CheckoutActivity.PAYPAL_REQUEST_CODE;

public class PackagePaymentActivity extends AppCompatActivity {

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(App_Settings.PAYPAL_CLIENT_ID);

    String Currency = App_Settings.CURRENCY_TYPE;
    String Sale_Receipt = AppUtils.getRandomString(App_Settings.SALE_RECEIPT_ID);

    String amount, package_name, package_id, package_period;

    //Firebase user and database reference
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference storeDatabaseReference = AppController.storeDatabaseReference;
    DatabaseReference subscriptionsDatabaseReference = AppController.subscriptionsDatabaseReference;

    String current_date, exp_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_payment);
        Bundle bundle = getIntent().getExtras();
        amount = bundle.getString("amount", "");
        package_name = bundle.getString("package_name", "");
        package_id = bundle.getString("package_id", "");
        package_period = bundle.getString("package_period", "");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        current_date = df.format(c.getTime());
        c.add(Calendar.DATE, Integer.parseInt(package_period));  // number of days to add
        exp_date = df.format(c.getTime());


//        if (amount.length() >= 2){
//
//            do_package_payment(amount);
//
//        }
//
//
//        else {

                Map<String, Object> storeUpdates = new HashMap<>();
                storeUpdates.put("package_id", package_id);
                storeUpdates.put("package_expiry", exp_date);
                storeUpdates.put("package_name", package_name);
                storeUpdates.put("store_status", "active");

                storeDatabaseReference.child(user.getUid()).updateChildren(storeUpdates);

                do_subscription();

//        }


    }


    //start paypal payments
    public void do_package_payment(String amount){

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        getPayment();

    }


    //Pass required params to firebase
    private void getPayment() {

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), Currency  , Sale_Receipt,
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

                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);

                            if (jsonDetails.getString("PaymentAmount").equals(amount)){

                                Map<String, Object> storeUpdates = new HashMap<>();
                                storeUpdates.put("package_id", package_id);
                                storeUpdates.put("package_expiry", exp_date);
                                storeUpdates.put("package_name", package_name);
                                storeUpdates.put("store_status", "active");

                                storeDatabaseReference.child(user.getUid()).updateChildren(storeUpdates);

                                do_subscription();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

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


    // Create a subscription
    public void do_subscription(){

        String id = subscriptionsDatabaseReference.push().getKey();
        Subscriptions_Model subscriptions_model = new Subscriptions_Model(id, current_date, package_name, package_id, amount, App_Settings.store_name, user.getProviderId(), exp_date, Sale_Receipt, amount);
        subscriptionsDatabaseReference.child(id).setValue(subscriptions_model);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}
