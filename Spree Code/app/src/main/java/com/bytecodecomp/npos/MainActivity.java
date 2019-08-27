package com.bytecodecomp.npos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.bytecodecomp.npos.Utils.ReceiptHTML;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.bytecodecomp.npos.Activities.Sales.CheckoutActivity;
import com.bytecodecomp.npos.Activities.Tablet.CheckoutPageActivity;
import com.bytecodecomp.npos.Activities.User.DashboardActivity;
import com.bytecodecomp.npos.Activities.User.LoginActivity;
import com.bytecodecomp.npos.Activities.User.ProfileActivity;
import com.bytecodecomp.npos.Adapters.Sales_Inventory_Adapter;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Data_Models.Constants;
import com.bytecodecomp.npos.Data_Models.Product_Cart_Details;
import com.bytecodecomp.npos.Fragments.InventoryFragment;
import com.bytecodecomp.npos.Fragments.SaleFragment;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.Utils.AppController;
import com.bytecodecomp.npos.Utils.AppUtils;
import com.bytecodecomp.npos.Utils.Permissions_Control;
import com.bytecodecomp.npos.Utils.Tools;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.print.*;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;

    SaleFragment saleFragment;
    InventoryFragment inventoryFragment;

    FragmentTransaction transaction;
    int value = 0;
    ListView list_view;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference cartDatabaseReference = AppController.cartDatabaseReference;
    ValueEventListener listener = AppController.listener;

//    AdView
    InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestCameraPermission();

        initToolbar();

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);

        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_spree_logo);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_menu_sale));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_menu_inventory));

        spaceNavigationView.shouldShowFullBadgeText(true);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.showIconOnly();

        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view,new SaleFragment());
        fragmentTransaction.commit();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                if (Permissions_Control.check_perms("user_dashboard") == true) {

                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);

                }

                else
                {

                    Toasty.warning(MainActivity.this, "Missing Access Dashboard Permission").show();

                }


            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                Log.e("Index", itemIndex + " int");

                saleFragment = new SaleFragment();
                inventoryFragment = new InventoryFragment();

                FragmentManager manager=getSupportFragmentManager();
                transaction = manager.beginTransaction();

                switch (itemIndex) {
                    case 0:
                    {

                        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.view,new SaleFragment());
                        fragmentTransaction.commit();
                        Log.e("onItemClick ", "" + itemIndex );



                        break;
                    }

                    case 1:
                    {

                        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.view,new InventoryFragment());
                        fragmentTransaction.commit();
                        Log.e("onItemClick ", "" + itemIndex );

                        break;
                    }

                }


            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
            }
        });

        spaceNavigationView.setSpaceOnLongClickListener(new SpaceOnLongClickListener() {
            @Override
            public void onCentreButtonLongClick() {
                Toast.makeText(MainActivity.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });


//        mInterstitialAd = new InterstitialAd(this);
//
//        // set the ad unit ID
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest);
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//        });


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                setUpViewPager();
            } else {
//                AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
            }

        }
    }


    @Override
    public void onBackPressed() {
        AppUtils.tapToExit(this);
    }


    //show cart dialog
    public void show_cart_dialog(final Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_cart);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final ArrayList<String> product_id = new ArrayList<>();
        final ArrayList<String> product_name = new ArrayList<>();
        final ArrayList<String> product_value = new ArrayList<>();
        final ArrayList<String> product_units = new ArrayList<>();
        final ArrayList<String> product_add_date = new ArrayList<>();
        final ArrayList<String> product_update_date = new ArrayList<>();
        final ArrayList<String> product_gtin = new ArrayList<>();


        listener = cartDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("Dialog cart", "Called");

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(user.getUid()).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())){
                    Product_Cart_Details value = iterator.next().getValue(Product_Cart_Details.class);

                    product_id.add(value.getProduct_id());
                    product_name.add(value.getProduct_name());
                    product_value.add(value.getProduct_value());
                    product_units.add(value.getProduct_units());
                    product_add_date.add(value.getProduct_add_date());
                    product_update_date.add(value.getProduct_update_date());
                    product_gtin.add(value.getProduct_gtin());

                    ((Sales_Inventory_Adapter)(((ListView)dialog.findViewById(R.id.list_view)).getAdapter())).notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        list_view = (ListView)dialog.findViewById(R.id.list_view);
        list_view.setAdapter(new Sales_Inventory_Adapter(product_id,product_name, product_value, product_units, product_add_date, product_update_date, product_gtin, dialog, activity));


        final AppCompatButton bt_checkout = (AppCompatButton) dialog.findViewById(R.id.bt_checkout);
        bt_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Permissions_Control.check_perms("user_make_sales") == true) {

                    cartDatabaseReference.removeEventListener(listener);
                    Intent intent = new Intent(activity, CheckoutActivity.class);
                    startActivity(intent);
                    close_dialog(dialog);

                }

                else
                {

                    Toasty.warning(activity, "Missing Access Checkout Permission").show();

                }

            }
        });

        final AppCompatButton bt_clear = (AppCompatButton) dialog.findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDatabaseReference.child(user.getUid()).removeValue();
                cartDatabaseReference.removeEventListener(listener);
                close_dialog(dialog);
            }
        });


        final AppCompatButton bt_close = (AppCompatButton) dialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_dialog(dialog);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    //close dialog
    public void close_dialog(Dialog dialog){
        dialog.dismiss();
    }



    /**
     * Requesting camera permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    //
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    //initialize toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_600));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // cart menu
        if (item.getTitle().toString().equals("CART")) {
            show_cart_dialog(this);
        }

        // cart full screen
        if (item.getTitle().toString().equals("CART FULL")) {
            Intent intent = new Intent(this, CheckoutPageActivity.class);
            startActivity(intent);
        }

        // profile menu
        if (item.getTitle().toString().equals("PROFILE")) {

            if (App_Settings.device_role.equals("merchant_admin")){

                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);

            }

            else {

                Toasty.warning(MainActivity.this, "Only Merchant Admin can Access").show();

            }


        }

        return super.onOptionsItemSelected(item);
    }



    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
