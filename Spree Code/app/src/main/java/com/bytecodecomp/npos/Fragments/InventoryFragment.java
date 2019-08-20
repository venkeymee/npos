package com.bytecodecomp.npos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.bytecodecomp.npos.Adapters.Product_Restock_Adapter;
import com.bytecodecomp.npos.Data_Models.Product_Inventory_Details;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by kenzy on 08/10/2018.
 */

public class InventoryFragment extends Fragment {

    View view;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;

    LinearLayout lyt_empty;

    //    AdView
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory, null);
        setHasOptionsMenu(true);

        MobileAds.initialize(getActivity(), getString(R.string.admob_app_id));


        //adview setuo
//        mAdView = (AdView) view.findViewById(R.id.adView);
////        mAdView.setAdSize(AdSize.BANNER);
////        mAdView.setAdUnitId(getString(R.string.banner_home_footer));
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
//                .build();
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdClosed() {
//
////                Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
////                Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
////                Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });
//        mAdView.loadAd(adRequest);

        lyt_empty = (LinearLayout) view.findViewById(R.id.lyt_empty);

        //call get inventory
        read_inventory();


        return view;
    }


    //read inventory function
    private void read_inventory(){

        final ArrayList<String> product_id = new ArrayList<>();
        final ArrayList<String> product_name = new ArrayList<>();
        final ArrayList<String> product_value = new ArrayList<>();
        final ArrayList<String> product_units = new ArrayList<>();
        final ArrayList<String> product_add_date = new ArrayList<>();
        final ArrayList<String> product_update_date = new ArrayList<>();
        final ArrayList<String> product_gtin = new ArrayList<>();
        final ArrayList<String> product_buying_price = new ArrayList<>();


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

                while((iterator.hasNext())){

                    Product_Inventory_Details value = iterator.next().getValue(Product_Inventory_Details.class);

                    product_id.add(value.getProduct_id());
                    product_name.add(value.getProduct_name());
                    product_value.add(value.getProduct_value());
                    product_units.add(value.getProduct_units());
                    product_add_date.add(value.getProduct_add_date());
                    product_update_date.add(value.getProduct_update_date());
                    product_gtin.add(value.getProduct_gtin());
                    product_buying_price.add(value.getProduct_buying_price());


                    if (product_id.size() > 1){

                        lyt_empty.setVisibility(View.GONE);

                    }

                    ((Product_Restock_Adapter)(((ListView)view.findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((ListView)view.findViewById(R.id.list_view)).setAdapter(new Product_Restock_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, getActivity()));

    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }





}