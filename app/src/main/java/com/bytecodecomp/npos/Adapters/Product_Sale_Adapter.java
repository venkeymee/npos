package com.bytecodecomp.npos.Adapters;

/**
 * Created by limon on 21/02/2018.
 */

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.bytecodecomp.npos.Data_Models.Amount_Model;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Product_Cart_Details;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;


public class Product_Sale_Adapter extends BaseAdapter {

    private ArrayList<String> product_id;
    private ArrayList<String> product_name;
    private ArrayList<String> product_value;
    private ArrayList<String> product_units;
    private ArrayList<String> product_add_date;
    private ArrayList<String> product_update_date;
    private ArrayList<String> product_gtin;
    private ArrayList<String> product_buying_price;
    private ArrayList<String> product_vat;


    private Activity activity;

//    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;
    private DatabaseReference cartDatabaseReference = AppController.cartDatabaseReference;
//    private ValueEventListener listener = AppController.listener;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public Product_Sale_Adapter(ArrayList<String> product_id, ArrayList<String> product_name, ArrayList<String> product_value, ArrayList<String> product_units, ArrayList<String> product_add_date, ArrayList<String> product_update_date, ArrayList<String> product_gtin, ArrayList<String> product_buying_price, ArrayList<String> product_vat,Activity activity){
        this.product_id=product_id;
        this.product_name=product_name;
        this.product_value=product_value;
        this.product_units=product_units;
        this.product_add_date=product_add_date;
        this.product_update_date=product_update_date;
        this.product_gtin=product_gtin;
        this.product_buying_price=product_buying_price;
        this.product_vat=product_vat;


        this.activity=activity;
    }

    @Override
    public int getCount() {
        return product_id.size();
    }

    @Override
    public Object getItem(int i) {
        return product_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_products,viewGroup,false);

        ((TextView)view.findViewById(R.id.txt_product_title)).setText(String.valueOf(product_name.get(i)));
        ((TextView)view.findViewById(R.id.txt_product_price)).setText( "SKU : " + String.valueOf(product_gtin.get(i))  + " / PRICE : " + App_Settings.CURRENCY_TYPE + " " +  String.valueOf(product_value.get(i)));

        LinearLayout lyt_product = (LinearLayout) view.findViewById(R.id.lyt_item);

        if ( ( i % 2 ) == 0 ) {

            lyt_product.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        } else {

            lyt_product.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_200));

        }


        lyt_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int total_products = Amount_Model.total_products + 1;
                Amount_Model.total_products = total_products;

                Toasty.info(activity, "Item Added").show();

                final Product_Cart_Details product_cart_details = new Product_Cart_Details(product_id.get(i),product_name.get(i), product_value.get(i), "1", product_add_date.get(i), product_update_date.get(i), product_gtin.get(i), product_units.get(i), product_vat.get(i));
                cartDatabaseReference.child(user.getUid()).child(product_id.get(i)).setValue(product_cart_details);




            }
        });


        return view;
    }

//
//    public void do_add_to_cart(final String product_gtin){
//
//        int total_products = Amount_Model.total_products + 1;
//        Amount_Model.total_products = total_products;
//
//
//        Query query = inventoryDatabaseReference.child(user.getUid()).orderByChild("product_gtin").equalTo(String.valueOf(product_gtin));
//        listener = query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot data:dataSnapshot.getChildren()){
//
//                    Product_Cart product_cart = data.getValue(Product_Cart.class);
//
//                    if (product_cart.getProduct_gtin() != null){
//
//                        String product_add_date = product_cart.getProduct_add_date();
//                        String product_gtin = product_cart.getProduct_gtin();
//                        String product_id = product_cart.getProduct_id();
//                        String product_name = product_cart.getProduct_name();
//                        String product_update_date = product_cart.getProduct_update_date();
//                        String product_value = product_cart.getProduct_value();
//                        String product_unit = product_cart.getProduct_units();
//                        String product_vat = product_cart.getProduct_vat();
//
//
//                        if (product_vat == null){
//
//                            product_vat = "1";
//
//                        }
//
//                        int amount = Amount_Model.amount + Integer.parseInt(product_cart.getProduct_value());
//                        Amount_Model.amount = amount;
//                        add_product(product_id, product_name, product_value, product_add_date, product_update_date, product_gtin, product_unit, product_vat);
//
//                    }
//
//                    else {
//
//                        Toasty.error(activity, "Item not Found").show();
//
//                    }
//
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//
//    private void add_product(final String product_id, String product_name, String product_value, String product_add_date, String product_update_date, String product_gtin, String product_unit, String product_vat){
//
//        Toasty.info(activity, "Item Added").show();
//
//        inventoryDatabaseReference.removeEventListener(listener);
//        final Product_Cart_Details product_cart_details = new Product_Cart_Details(product_id,product_name, product_value, "1", product_add_date, product_update_date, product_gtin, product_unit, product_vat);
//        cartDatabaseReference.child(user.getUid()).child(product_id).setValue(product_cart_details);
//
//
//    }


}