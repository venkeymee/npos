package com.bytecodecomp.npos.Data_Models;

import org.json.JSONObject;

public class App_Settings {

    //Choose your store currency, Make sure the currency is supported by paypal
    public static String CURRENCY_TYPE = "USD";

    public static String CURRENCY_SYMBOL = "$";

    //enter you paypal client id
    public static final String PAYPAL_CLIENT_ID = "AfSqTGEYye4h-C_yopiC1btOuN7MKm4lfzx-C_fiPEFMYeOuMvCMZOeMQlknYP0P60twwqaFZYV5UqW9";

    //enter sale receipt id
    public static final int SALE_RECEIPT_ID = 8;

    public static final int require_staff_on_checkout = 1;
    public static final int require_customer_on_credit_checkout = 1;

    public static final int allow_user_registration = 1;

    //set to true if saas is enabled and false if not
    public static final boolean allow_saas = true;

    //set encrypt
    public static String crypts = "com.pos.spree";

    //set Admin uid
    public static String admin_uid = "kAyN4SBJQ3ZalYH78oin5Ak6uUB3";

    public static String store_uid  = "";

    public static String device_role = "" ;

    public static String staff_uid = "0";

    public static String staff_permissions = "0";

    public static String current_activity = "";

    public static String store_name, store_address, store_location, store_print, store_contacts, package_id, package_expiry, package_name, store_status;

    JSONObject jsonObject = new JSONObject();

    //Note, you will need to change this to match the name of your device
    public static String default_printer = "BlueTooth Printer";


    public static String json_user_roles = "{\"Roles\":[{\"role\":\"owner\"},{\"role\":\"cashier\"},{\"role\":\"stock_manager\"},{\"role\":\"accounts\"},{\"role\":\"manager\"}]}";
    public static String user_role = "";

}
