package com.bytecodecomp.npos.Utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.bytecodecomp.npos.Plugins.notification.MyNotificationOpenedHandler;
import com.bytecodecomp.npos.Plugins.notification.MyNotificationReceivedHandler;
import com.bytecodecomp.npos.R;

import io.fabric.sdk.android.Fabric;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class AppController extends Application {

	private static AppController mInstance;
	public static ValueEventListener listener;

	public ImageLoader imageLoader;


	public static DatabaseReference cartDatabaseReference, inventoryDatabaseReference, salesDatabaseReference, expenseDatabaseReference, subscriptionsDatabaseReference,
			expenseCatDatabaseReference, permissionsDatabaseReference, packageDatabaseReference, customerDatabaseReference, supplierDatabaseReference, deviceDatabaseReference,
			purchasesDatabaseReference, registerDatabaseReference, assetsDatabaseReference, usersDatabaseReference, storeDatabaseReference, profileDatabaseReference;

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());

		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		cartDatabaseReference = FirebaseDatabase.getInstance().getReference("Cart");
		salesDatabaseReference = FirebaseDatabase.getInstance().getReference("Sale");
		inventoryDatabaseReference = FirebaseDatabase.getInstance().getReference("Product");
		customerDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer");
		supplierDatabaseReference = FirebaseDatabase.getInstance().getReference("Supplier");
		purchasesDatabaseReference = FirebaseDatabase.getInstance().getReference("Purchase");
		registerDatabaseReference = FirebaseDatabase.getInstance().getReference("Register");
		assetsDatabaseReference = FirebaseDatabase.getInstance().getReference("Assets");
		expenseDatabaseReference = FirebaseDatabase.getInstance().getReference("Expense");
		expenseCatDatabaseReference = FirebaseDatabase.getInstance().getReference("Expense-Category");
		usersDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
		storeDatabaseReference = FirebaseDatabase.getInstance().getReference("Store");
		profileDatabaseReference = FirebaseDatabase.getInstance().getReference("Profile");
		permissionsDatabaseReference = FirebaseDatabase.getInstance().getReference("Permissions");
		packageDatabaseReference = FirebaseDatabase.getInstance().getReference("Packages");
		subscriptionsDatabaseReference = FirebaseDatabase.getInstance().getReference("Subscriptions");
		deviceDatabaseReference = FirebaseDatabase.getInstance().getReference("Devices");

		// initialize the AdMob app
		MobileAds.initialize(this, getString(R.string.admob_app_id));
		context = getApplicationContext();

		OneSignal.startInit(this)
				.setNotificationOpenedHandler(new MyNotificationOpenedHandler())
				.setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
				.init();

//		this.imageLoader = ImageLoader.getInstance();

		mInstance = this;

	}


	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public static int get_random_sku(){

		int min = 1000;
		int max = 10000;
		Random r = new Random();
		int sku = r.nextInt(max - min + 1) + min;

		return sku;

	}


	public static String timestamp(String str_date){

		String time_stamp;

		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dates = (Date)formatter.parse(str_date);
			time_stamp = String.valueOf(dates.getTime());
			} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

}