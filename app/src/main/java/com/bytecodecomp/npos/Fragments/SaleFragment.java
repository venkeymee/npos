package com.bytecodecomp.npos.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.bytecodecomp.npos.Activities.Sales.CheckoutActivity;
import com.bytecodecomp.npos.Adapters.Product_Sale_Adapter;
import com.bytecodecomp.npos.Data_Models.Amount_Model;
import com.bytecodecomp.npos.Data_Models.Product_Cart;
import com.bytecodecomp.npos.Data_Models.Product_Cart_Details;
import com.bytecodecomp.npos.Data_Models.Product_Inventory_Details;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.AppPreference;
import com.bytecodecomp.npos.Utils.AppUtils;
import com.bytecodecomp.npos.Utils.Permissions_Control;
import com.bytecodecomp.npos.Utils.PrefKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * Created by kenzy on 08/06/2017.
 */

public class SaleFragment extends Fragment {


    private Activity mActivity;
    private Context mContext;

    private ViewGroup contentFrame;
    private ZXingScannerView zXingScannerView;
    private ArrayList<Integer> mSelectedIndices;

    private boolean isFlash, isAutoFocus;
    private int camId, frontCamId, rearCamId;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ImageView btn_search;
    EditText et_product_gtin;
    private FloatingActionButton flash, focus, camera;

    private DatabaseReference inventoryDatabaseReference = AppController.inventoryDatabaseReference;
    private DatabaseReference cartDatabaseReference = AppController.cartDatabaseReference;
    private ValueEventListener listener = AppController.listener;

    ImageView btn_cart;

    View view;


    //    AdView
    private AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        zXingScannerView = new ZXingScannerView(mActivity);

        setupFormats();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sale, null);

        et_product_gtin = (EditText) view.findViewById(R.id.search_text);

        btn_search = (ImageView) view.findViewById(R.id.img_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String search = et_product_gtin.getText().toString();

                if (search.length() > 1){

                    do_add_items(search);

                }

                else {

                    Toasty.warning(getActivity(), "Enter search value").show();

                }

            }
        });


        //adview setuo
//        mAdView = (AdView) view.findViewById(R.id.adView);
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner_home_footer));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("LC74F1001088")
                .build();

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

        initView(view);
        initListener();

        return view;
    }

    private void initVar() {
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();

        isFlash = AppPreference.getInstance(mContext).getBoolean(PrefKey.FLASH, false); // flash off by default
        isAutoFocus = AppPreference.getInstance(mContext).getBoolean(PrefKey.FOCUS, true); // auto focus on by default
        camId = AppPreference.getInstance(mContext).getInteger(PrefKey.CAM_ID); // back camera by default
        if(camId == -1) {
            camId = rearCamId;
        }

        loadCams();
    }

    private void initView(View rootView) {

        btn_cart = (ImageView) rootView.findViewById(R.id.btn_cart);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Permissions_Control.check_perms("user_make_sales") == true) {

                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

                else
                {

                    Toasty.warning(getActivity(), "Missing Access Checkout Permission").show();

                }


            }
        });

        contentFrame = (ViewGroup) rootView.findViewById(R.id.content_frame);

        flash = (FloatingActionButton) rootView.findViewById(R.id.flash);
        focus = (FloatingActionButton) rootView.findViewById(R.id.focus);
        camera = (FloatingActionButton) rootView.findViewById(R.id.camera);
        initConfigs();
        do_list();

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

                    ((Product_Sale_Adapter)(((ListView)view.findViewById(R.id.list_search)).getAdapter())).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((ListView)view.findViewById(R.id.list_search)).setAdapter(new Product_Sale_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, product_buying_price, product_vat, getActivity()));


    }



    private void initListener() {

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFlash();
            }
        });

        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFocus();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCamera();
            }
        });

            zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                @Override
                public void handleResult(final Result result) {


                    dialog_add(result.toString());

                }
            });

    }

    //start scanner
    private void activateScanner() {
        if(zXingScannerView != null) {

            if(zXingScannerView.getParent()!=null) {
                ((ViewGroup) zXingScannerView.getParent()).removeView(zXingScannerView); // to prevent crush on re adding view
            }
            contentFrame.addView(zXingScannerView);

            if(zXingScannerView.isActivated()) {
                zXingScannerView.stopCamera();
            }

            zXingScannerView.startCamera(camId);
//            zXingScannerView.setFlash(isFlash);
            zXingScannerView.setAutoFocus(isAutoFocus);
        }
    }


    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(zXingScannerView != null) {
            zXingScannerView.setFormats(formats);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

        activateScanner();
    }

    @Override
    public void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();
        if(zXingScannerView != null) {
            zXingScannerView.stopCamera();
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if(zXingScannerView != null) {
            if (visible) {
                zXingScannerView.setFlash(isFlash);
            } else {
                zXingScannerView.setFlash(false);
            }
        }
    }


    //toggle flash
    private void toggleFlash() {
        if (isFlash) {
            isFlash = false;
            flash.setImageResource(R.drawable.ic_flash_on);
        } else {
            isFlash = true;
            flash.setImageResource(R.drawable.ic_flash_off);
        }
        AppPreference.getInstance(mContext).setBoolean(PrefKey.FLASH, isFlash);
        zXingScannerView.setFlash(isFlash);
    }

    //change camera focus
    private void toggleFocus() {
        if (isAutoFocus) {
            isAutoFocus = false;
            focus.setImageResource(R.drawable.ic_focus_on);
            AppUtils.showToast(mContext, getString(R.string.autofocus_off));
        } else {
            isAutoFocus = true;
            focus.setImageResource(R.drawable.ic_focus_off);
            AppUtils.showToast(mContext, getString(R.string.autofocus_on));
        }
        AppPreference.getInstance(mContext).setBoolean(PrefKey.FOCUS, isAutoFocus);
        zXingScannerView.setFocusable(isAutoFocus);
    }

    //toggle camera change
    private void toggleCamera() {

        if (camId == rearCamId) {
            camId = frontCamId;
        } else {
            camId = rearCamId;
        }
        AppPreference.getInstance(mContext).setInteger(PrefKey.CAM_ID, camId);
        zXingScannerView.stopCamera();
        zXingScannerView.startCamera(camId);
    }

    //change flash
    private void initConfigs() {
        if (isFlash) {
            flash.setImageResource(R.drawable.ic_flash_off);
        } else {
            flash.setImageResource(R.drawable.ic_flash_on);
        }
        if (isAutoFocus) {
            focus.setImageResource(R.drawable.ic_focus_off);
        } else {
            focus.setImageResource(R.drawable.ic_focus_on);
        }
    }

    //change camera front
    private void loadCams() {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCamId = i;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                rearCamId = i;
            }
        }
        AppPreference.getInstance(mContext).setInteger(PrefKey.CAM_ID, rearCamId);

    }


    private void add_product(final String product_id, String product_name, String product_value, String product_add_date, String product_update_date, String product_gtin, String product_unit, String product_vat){

        final Product_Cart_Details product_cart_details = new Product_Cart_Details(product_id,product_name, product_value, "1", product_add_date, product_update_date, product_gtin, product_unit, product_vat);
        cartDatabaseReference.child(user.getUid()).child(product_id).setValue(product_cart_details);

    }


    //Add product on cart
    public void do_add_items(String result){

        int total_products = Amount_Model.total_products + 1;
        Amount_Model.total_products = total_products;

        Query query = inventoryDatabaseReference.child(user.getUid()).orderByChild("product_gtin").equalTo(String.valueOf(result));
        listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){

                    Product_Cart product_cart = data.getValue(Product_Cart.class);

                    if (product_cart.getProduct_gtin() != null){

                        String product_add_date = product_cart.getProduct_add_date();
                        String product_gtin = product_cart.getProduct_gtin();
                        String product_id = product_cart.getProduct_id();
                        String product_name = product_cart.getProduct_name();
                        String product_update_date = product_cart.getProduct_update_date();
                        String product_value = product_cart.getProduct_value();
                        String product_unit = product_cart.getProduct_units();
                        String product_vat = product_cart.getProduct_vat();


                        if (product_vat == null){

                            product_vat = "1";

                        }

                        int amount = Amount_Model.amount + Integer.parseInt(product_cart.getProduct_value());
                        Amount_Model.amount = amount;
                        add_product(product_id, product_name, product_value, product_add_date, product_update_date, product_gtin, product_unit, product_vat);
                        inventoryDatabaseReference.removeEventListener(listener);

                        Toasty.success(getActivity(), "Item Added to cart").show();
                        et_product_gtin.setText("");

                    }

                    else {

                        Toasty.warning(getActivity(), "Item not Found").show();

                    }



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //Dialog add product to cart
    public void dialog_add(final String result){

        zXingScannerView.stopCamera();

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText("Product with GTIN  :  " + result);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_add_items(result);
                dialog.dismiss();

                zXingScannerView.startCamera(camId);

                //clear field after product added
                et_product_gtin.setText("");

            }
        });


        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                zXingScannerView.startCamera(camId);

                //clear field after product canceled
                et_product_gtin.setText("");

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}