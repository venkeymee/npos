package com.bytecodecomp.npos.Plugins.Printer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import com.bytecodecomp.npos.Activities.Sales.CheckoutActivity;
import com.bytecodecomp.npos.Activities.Tablet.CheckoutPageActivity;
import com.bytecodecomp.npos.Data_Models.App_Settings;
import com.bytecodecomp.npos.Plugins.Printer.pockdata.PocketPos;
import com.bytecodecomp.npos.Plugins.Printer.util.DataConstants;
import com.bytecodecomp.npos.Plugins.Printer.util.DateUtil;
import com.bytecodecomp.npos.Plugins.Printer.util.FontDefine;
import com.bytecodecomp.npos.Plugins.Printer.util.Printer;
import com.bytecodecomp.npos.Plugins.Printer.util.StringUtil;
import com.bytecodecomp.npos.Plugins.Printer.util.Util;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Demo Blue Bamboo P25 Thermal Printer.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class MainActivity extends Activity {

	
	private ProgressDialog mProgressDlg;
	private ProgressDialog mConnectingDlg;
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private P25Connector mConnector;
	
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	String action;

	TextView btn_bluetooth, btn_btp;
	ImageView img_cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer);
		Bundle bundle = getIntent().getExtras();
		action = bundle.getString("action");
		
		mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
				
		if (mBluetoothAdapter == null) {
//			showUnsupported();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
//				showDisabled();
			} else {
//				showEnabled();
				
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				
				if (pairedDevices != null) {
					mDeviceList.addAll(pairedDevices);
					
//					updateDeviceList();
				}
			}
			
			mProgressDlg 	= new ProgressDialog(this);
			
			mProgressDlg.setMessage("Scanning...");
			mProgressDlg.setCancelable(false);
			mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.dismiss();
			        
			        mBluetoothAdapter.cancelDiscovery();
			    }
			});
			
			mConnectingDlg 	= new ProgressDialog(this);
			
			mConnectingDlg.setMessage("Connecting...");
			mConnectingDlg.setCancelable(false);
			
			mConnector 		= new P25Connector(new P25Connector.P25ConnectionListener() {
				
				@Override
				public void onStartConnecting() {
					mConnectingDlg.show();
				}
				
				@Override
				public void onConnectionSuccess() {
					mConnectingDlg.dismiss();

					printText(action);

				}
				
				@Override
				public void onConnectionFailed(String error) {
					mConnectingDlg.dismiss();
				}
				
				@Override
				public void onConnectionCancelled() {
					mConnectingDlg.dismiss();
				}
				
				@Override
				public void onDisconnected() {
					showToast("Disconnected");
				}
			});


			btn_bluetooth = (TextView) findViewById(R.id.btn_bluetooth);
			btn_bluetooth.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					connect();

				}
			});

			btn_btp = (TextView) findViewById(R.id.btn_btp);
			btn_btp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					String textToPrint = action; // plain or base64
					Intent intent = new Intent("ru.a402d.rawbtprinter.action.PRINT_RAWBT"); // action
					intent.putExtra("ru.a402d.rawbtprinter.extra.DATA",textToPrint); // extra
					intent.setPackage("ru.a402d.rawbtprinter");
					startService(intent);

				}
			});

			img_cancel = (ImageView) findViewById(R.id.img_cancel);
			img_cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				finish();

				}
			});

			connect();

		}
		
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		
		registerReceiver(mReceiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_printer, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_scan) {
			mBluetoothAdapter.startDiscovery();
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}			
		}
		
		if (mConnector != null) {
			try {
				mConnector.disconnect();
			} catch (P25ConnectionException e) {
				e.printStackTrace();
			}
		}
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
	}
	
	private String[] getArray(ArrayList<BluetoothDevice> data) {
		String[] list = new String[0];
		
		if (data == null) return list;
		
		int size	= data.size();
		list		= new String[size];
		
		for (int i = 0; i < size; i++) {
			list[i] = data.get(i).getName();
		}
		
		return list;	
	}
	
	private void showToast(String message) {

		Toasty.info(getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();

		}

	private void connect() {
		if (mDeviceList == null || mDeviceList.size() == 0) {
			return;
		}

		if (mDeviceList.size() > 0) {
			for (BluetoothDevice device : mDeviceList) {
				if (device.getName().equals(App_Settings.default_printer)) //Note, you will need to change this to match the name of your device
//				if (device.getName().equals("Mobile Printer")) //Note, you will need to change this to match the name of your device

				{

					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						try {
							createBond(device);
						} catch (Exception e) {
							showToast("Failed to pair device");
							return;
						}
					}

					try {
						if (!mConnector.isConnected()) {
							mConnector.connect(device);
						} else {
							mConnector.disconnect();

							showToast("Disconnected");
						}
					} catch (P25ConnectionException e) {
						e.printStackTrace();
					}

					break;

				}


			}

		}
	}



	
	private void createBond(BluetoothDevice device) throws Exception { 
        
        try {
            Class<?> cl 	= Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par 	= {};
            
            Method method 	= cl.getMethod("createBond", par);
            
            method.invoke(device);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            throw e;
        }
    }
	
	private void sendData(byte[] bytes) {
		try {

			int response = mConnector.sendData(bytes);

			if (response == 2){

				if (App_Settings.current_activity == "CheckoutActivity"){

					Intent intent = new Intent(this, CheckoutActivity.class);
					startActivity(intent);
					finish();


				}

				if (App_Settings.current_activity == "CheckoutPageActivity"){

					Intent intent = new Intent(this, CheckoutPageActivity.class);
					startActivity(intent);
					finish();


				}

				else if (App_Settings.current_activity == "ProductDetailsActivity"){

					finish();

				}

				else if (App_Settings.current_activity == "Sales_Adapter"){

					finish();

				}

				else {

					finish();

				}



			}

			else {

				showToast("Print Error, kindly try again.");
				finish();

			}

		} catch (P25ConnectionException e) {

			Log.e("here", "....");
			e.printStackTrace();
		}
	}

	
	private void printDemoContent(){
		   
		/*********** print head*******/
		String receiptHead = "************************" 
				+ "   P25/M Test Print"+"\n"
				+ "************************"
				+ "\n";
		
		long milis		= System.currentTimeMillis();
		
		String date		= DateUtil.timeMilisToString(milis, "MMM dd, yyyy");
		String time		= DateUtil.timeMilisToString(milis, "hh:mm a");
		
		String hwDevice	= Build.MANUFACTURER;
		String hwModel	= Build.MODEL;
		String osVer	= Build.VERSION.RELEASE;
		String sdkVer	= String.valueOf(Build.VERSION.SDK_INT);
		
		StringBuffer receiptHeadBuffer = new StringBuffer(100);
		
		receiptHeadBuffer.append(receiptHead);
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify(date, time, DataConstants.RECEIPT_WIDTH) + "\n");
		
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("Device:", hwDevice, DataConstants.RECEIPT_WIDTH) + "\n");
		
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("Model:",  hwModel, DataConstants.RECEIPT_WIDTH) + "\n");
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("OS ver:", osVer, DataConstants.RECEIPT_WIDTH) + "\n");
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("SDK:", sdkVer, DataConstants.RECEIPT_WIDTH));
		receiptHead = receiptHeadBuffer.toString();
		
		byte[] header = Printer.printfont(receiptHead + "\n", FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
			
		/*********** print English text*******/
		StringBuffer sb = new StringBuffer();
		for(int i=1; i<128; i++)
			sb.append((char)i);
		String content = sb.toString().trim();
		
		byte[] englishchartext24 			= Printer.printfont(content + "\n",FontDefine.FONT_24PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		byte[] englishchartext32			= Printer.printfont(content + "\n",FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		byte[] englishchartext24underline	= Printer.printfont(content + "\n",FontDefine.FONT_24PX_UNDERLINE,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
		//2D Bar Code
		byte[] barcode = StringUtil.hexStringToBytes("1d 6b 02 0d 36 39 30 31 32 33 34 35 36 37 38 39 32");
		
		
		/*********** print Tail*******/
		String receiptTail =  "Test Completed" + "\n"
				+ "************************" + "\n";
		
		String receiptWeb =  "** www.londatiga.net ** " + "\n\n\n";
		
		byte[] foot = Printer.printfont(receiptTail,FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		byte[] web	= Printer.printfont(receiptWeb,FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
		byte[] totladata =  new byte[header.length + englishchartext24.length + englishchartext32.length + englishchartext24underline.length + 
		                              + barcode.length
		                             + foot.length + web.length
		                             ];
	 	int offset = 0;
		System.arraycopy(header, 0, totladata, offset, header.length);
		offset += header.length;
		
		System.arraycopy(englishchartext24, 0, totladata, offset, englishchartext24.length);
		offset+= englishchartext24.length;
		
		System.arraycopy(englishchartext32, 0, totladata, offset, englishchartext32.length);
		offset+=englishchartext32.length;
		
		System.arraycopy(englishchartext24underline, 0, totladata, offset, englishchartext24underline.length);
		offset+=englishchartext24underline.length;
		
		System.arraycopy(barcode, 0, totladata, offset, barcode.length);
		offset+=barcode.length;

		System.arraycopy(foot, 0, totladata, offset, foot.length);
		offset+=foot.length;
		
		System.arraycopy(web, 0, totladata, offset, web.length);
		offset+=web.length;
		
		byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totladata, 0, totladata.length);

		sendData(senddata);		
	}
	
	private void printText(String text) {
		byte[] line 	= Printer.printfont(text + "\n\n", FontDefine.FONT_32PX, FontDefine.Align_CENTER, (byte) 0x1A, PocketPos.LANGUAGE_ENGLISH);

		text = text + "\n\n";
		byte[] bytes = null;
		byte[] senddata = null;
		try {
			bytes = text.getBytes("UTF-8");
			senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, line, 0, line.length);

			Log.e("Values...1", new String(bytes, "UTF-8"));
			Log.e("Values...2", new String(senddata, "UTF-8"));
			Log.e("Values...3", StringUtil.byteToString(senddata));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		sendData(bytes);
	}


	private void print1DBarcode() {
		String content	= "6901234567892";
	
		//1D barcode format (hex): 1d 6b 02 0d + barcode data
		
		byte[] formats	= {(byte) 0x1d, (byte) 0x6b, (byte) 0x02, (byte) 0x0d};
		byte[] contents	= content.getBytes();
		
		byte[] bytes	= new byte[formats.length + contents.length];
		
		System.arraycopy(formats, 0, bytes, 0, formats.length);
		System.arraycopy(contents, 0, bytes, formats.length, contents.length);
		
		sendData(bytes);
		
		byte[] newline 	= Printer.printfont("\n\n",FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
		sendData(newline);
	}
	
	private void print2DBarcode() {
		String content 	= "Lorenz Blog - www.londatiga.net";
		
		//2D barcode format (hex) : 1d 6b 10 00 00 00 00 00 1f + barcode data
		
		byte[] formats	= {(byte) 0x1d, (byte) 0x6b, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
						   (byte) 0x00, (byte) 0x1f};
		byte[] contents	= content.getBytes();
		byte[] bytes	= new byte[formats.length + contents.length];
		
		System.arraycopy(formats, 0, bytes, 0, formats.length);
		System.arraycopy(contents, 0, bytes, formats.length, contents.length);
		
		sendData(bytes);
		
		byte[] newline 	= Printer.printfont("\n\n",FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
		sendData(newline);
	}
	
	private void printImage() {
		try {		
			//image must be in monochrome bitmap
			//format: 1B 58 31 0B 30 + image data
			//where = 1B 58 31 = image format
			//   0B 30 = width x height (tes.bmp: 84x48 pixel)
			//   0B = image width/8 -> 84/8 = 11 (in decimal) -> 0B (in hex)
			//   30 = image height = 48 (in decimal) -> 30 in hexa
			//see: http://bluebamboo.helpserve.com/index.php?/Knowledgebase/Article/View/48
			
			//byte[] formats	= {(byte) 0x1B, (byte) 0x58, (byte) 0x34, (byte) 0x35, (byte) 0x118	};
			//byte[] image 	= FileOperation.getBytesFromAssets(MainActivity.this, "tes.bmp");
			//byte[] image	= StringUtil.hexStringToBytes("00 00 00 00 FF FF FF 00 FF FF FF FF FE FF FF FF FF FF FF FF FF 80 00 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 01 01 0F FF FF FE FE FF FE FE FE FE FF FF FE FF FE FE FF F0 00 01 00 FF FE FE FF FF FE FF FE FF FE FE FE FF 81 01 00 00 7F FE FF FF FE FF FE FF FE FE FE FF FF FE FE FF 80 01 1E FE FF FF 7F FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 00 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF F8 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF F8 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF E3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF C7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF C7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF C3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF E3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 87 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 87 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 87 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 9F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 9F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF BF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF CF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF EF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F9 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FD FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF F9 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF E1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 01 FF FF FF FF FF FF FF FF FF FF FF FF C1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF 81 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 01 FF FF FF FF FF FF FF FF FF FE 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 3F FF FF FF F1 FF FF FF FF FC 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 03 FF FF FF 80 7F FF FF FF FC 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 7F FF FE 00 3F FF FF FF F8 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 0F FF FC 00 3F FF FF FF F0 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 01 FF E0 00 1F FF FF FF E0 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 1F 00 00 0F FF FF FF C0 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF 80 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF 80 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FE 00 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 01 FF FF FE 00 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 01 FF FF FC 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 FF FF F8 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 7F FF E0 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF C0 00 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF 80 00 00 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FE 00 00 00 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 07 F0 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 03 FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 0F FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 1F FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 7F FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 7F FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F0 07 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 01 FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 FE 07 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF F8 3F 80 00 00 00 00 00 00 00 01 FC 1F FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF F0 7F C0 00 00 00 00 00 00 00 03 F8 07 FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF F1 FF E0 00 00 00 00 00 00 00 07 F0 03 FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF E3 FF F0 00 00 00 00 00 00 00 07 E0 01 FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C7 FF F0 00 00 00 00 00 00 00 0F E0 00 FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 8F FF F8 00 00 00 00 00 00 00 0F C0 00 FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 0F FF F8 00 00 00 00 00 00 00 1F C0 00 7F FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FE 1F FF FC 00 00 00 00 00 00 00 3F C0 00 7F FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FF C7 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FC 7F FF FE 00 00 00 00 00 00 00 7F E0 00 7F FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F FE 03 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF F8 FF FF FE 00 00 00 00 00 00 00 7F FE 01 FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F FC 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF F1 FF FF FF 00 00 00 00 00 00 01 FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F F8 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 01 FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F F0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 03 FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 07 FF FF FF FF FF FF FF FF F0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF C0 00 00 03 FF 80 0F FF FF FF FF FF FF FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 93 FF FF C0 00 00 0F FF E0 1F FF FF FF FF FF FF FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 3F FF E0 00 00 7F FF F0 3F FF FF FF FF FF FF FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 0F FF E0 00 01 FF FF FC 3F FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 07 FF E0 00 03 FF FF FE 7F FF FF FF FF FF FF FF FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F E0 03 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 07 FF C0 00 07 FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F F0 03 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 07 FF 80 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F F0 03 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 03 FF 00 00 7F FF FF FF FF FF FF FF FF FF 80 3F FF FF F8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F F0 03 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 03 FF 00 1F FF FF FF FF FF FF FF FF FF FF 00 1F FF FF FC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 F8 01 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 03 FE 00 7F FF FF FF FF FF FF FF FF FF FE 00 0F FF FF FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 F8 00 FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 01 FE 01 FF FF FF FF FF FF F7 FF FF FF FC 00 07 FF FF FF 00 00 00 00 00 00 7F FF FC 00 00 00 00 00 03 F8 00 7F FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 01 FC 03 FF FF FF FF FF FF E3 FF FF FF F8 00 07 FF FF FF 80 00 00 00 00 01 FF FF FF 80 00 00 00 00 03 FC 00 1F FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 03 FE 07 FF FF FF FF FF FF C1 FF FF FF F0 00 03 FF FF FF C0 00 00 00 00 03 FF FF FF F8 00 00 00 00 07 FC 00 0F FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 03 FF FF FF FF FF FF FF FF 81 FF FF FF E0 00 03 FF FF FF E0 00 00 00 00 0F FF FF FF FF 00 00 00 00 1F FC 00 03 FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 07 FF FF FF FF FF FF FF FF 80 FF FF FF E0 00 03 FF FF FF E0 00 00 00 00 1F FF FF FF FF E0 00 00 00 FF FC 00 00 FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 80 07 FF FF FF FF FF FF FF FF 80 FF FF FF C0 00 03 FF FF FF F0 00 00 00 00 3F FF FF FF FF FE 00 00 03 FF FC 00 00 7F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 80 0F FF FF FF FF FF FF F7 FF 00 7F FF FF C0 00 03 FF FF FF F0 00 00 3E 00 7F FF FF FF FF FF E0 00 0F FF F8 00 00 1F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 80 1F FF FF FF FF FF FF F3 FF 00 3F FF FF 80 00 03 FF FF FF F8 00 00 FE 00 FF FF FF FF FF FF FF 00 3F FF F8 00 00 1F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 80 3F FF FF FF FF FF FF E3 FF 00 3F FF FF 80 00 03 FF FF FF F8 00 01 FF 01 FF FF FF FF FF FF FF C0 3F FF F0 00 00 0F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 80 7F FF FF FF FF FF FF E3 FF 00 1F FF FF 00 00 03 FF FF FF F8 00 03 FF 03 FF FF FF FF FF FF FF E0 7F FF E0 00 00 0F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C1 FF FF FF FF FF FF FF E7 FF 00 1F FF FF 00 00 07 FF FF FF F8 00 0F FF 07 FF FF FF FF FF FF FF F0 FF FF C0 00 00 0F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C1 FF FF FF FF FF FF FF E7 FF 00 1F FF FF 00 00 07 FF FF FF FC 00 0F FE 07 FF FF FF FF FF FF FF F0 FF FF 80 00 00 0F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C3 FF FF FF FF FF FF FF E7 FF 00 0F FF FF 00 00 0F FF FF FF FC 00 3F FE 0F FF FF FF FF FF FF FF F0 FF FF 80 00 00 0F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C7 FF FF FF FF FF FF FF EF FF 80 0F FF FF 00 00 0F FF FF FF FC 00 7F FE 1F FF FF FF FF FF FF FF F0 FF FF 00 00 00 0F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C7 FF FF FF FF FF FF FF FF FF 80 0F FF FF 00 00 1F FF FF FF FC 00 7F FE 3F FF FF FF FF FF FF FF F0 FF FF 00 00 00 07 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF 80 0F FF FF 00 00 3F FF FF FF FC 00 FF FC 7F FF FF FF FF FF FF FF F8 7F FF 00 00 00 07 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 8F FF FF FF FF FF FF FF FF FF C0 0F FF FF 80 00 7F FF FF FF FC 01 FF FC 7F FF FF FF FF FF FF FF F8 7F FF C0 00 00 03 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 03 FF FF FF FF FF FF FF FF FF C0 0F FF FF 80 00 FF FF FF FF FC 03 FF F8 FF FF FF FF FF FF FF FF F8 7F FF F0 00 00 03 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 FF FF FF FF FF FF FF FF FF E0 0F FF FF C0 03 FF FF FF FF FC 03 FF F1 FF FF FF FF FF FF FF FF F8 3F FF FC 00 00 00 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 FF FF EF FF FF FF FF FF FF F0 0F FF FF E0 0F FF FF FF FF FC 07 FF F1 FF FF FF FF FF FF FF FF FC 0F FF FF 80 00 00 7F FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 00 3F FF 8F FF FF FF FF FF FF F8 1F FF FF F0 1F FF FF FF FF FC 0F FF F3 FF FF FF FF FF FF FF FF FE 03 FF FF E0 00 00 0F FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 38 3F FE 1F FF FF FF FF F3 FF FC 1F FF FF F8 7F FF FF FF FF FE 0F FF E7 FF FF FF FF FF FF FF FF FF 00 7F FF FC 00 00 01 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 38 3F FC 3F CF FF FF FF F1 FF FE 1F FF FF FF FF FF FF FF FF FE 1F FF EF FF FF FF FF FF FF FF FF FF 80 1F FF FF 00 00 00 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 9F FF F8 7F 9F FF FF FF F1 FF FF BF FF FF FF FF FF FF FF FF FE 3F FF FF FF FF FF FF FF FF FF FF FF C0 03 FF FF E0 00 00 1F FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 9F FF F0 FE 3F FF FF FF F1 FF FF FF FF FF FF FF FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF F0 00 FF FF FE 00 00 0F FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF CF FF E1 FE 7F FF FF FF F9 FF FF FF FF FF FF FF FF FF FF FF FE FF FF FF FF FF FF FF FF FF FF FF FF FC 00 3F FF FF 80 00 07 FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF CF FF C3 F8 FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 03 FF FF FC 00 01 FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF C7 FF 87 F1 FF FF FF FF F8 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 00 7F FF FF 80 00 FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF E7 FF 0F E3 FF FF FF FF FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 0F FF FF E0 00 FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF E7 FE 1F C7 FF FF FF FF FC 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 80 03 FF FF FC 00 7F FF FF FF F0 00 00 00 FF FF FF FF FF FF FF CF FC 3F 9F FF FF FF FF FC 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 7F FF FF F0 3F FF FF FF F0 00 00 00 FF FF FF FF FF FF FF CF F8 7F 1F FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 0F FF FF FF 3F FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 9F F0 7E 7F FF FF FF FF FE 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 03 FF FF FF 9F FF FF FF F0 00 00 00 FF FF FF FF FF FF FF 3F E1 FE FF FF FF FF FF FF 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FE 00 3F FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FE 3F C3 F9 FF FF FF FF FF FF 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 03 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FC 7F 83 F9 FF FF FF FF FF FF 9F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 01 FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF F8 FF 07 F7 FF FF FF FF FF FF CF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 3F FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF F1 FE 0F CF FF FF FF FF FF FF C7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 07 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF E3 FC 1F 9F FF FF FF FF FF FF F3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 07 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF C7 F8 3F 3F FF FF FF FF FF FF FB FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 03 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF CF E0 3E 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C1 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF 9F C0 7C FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF 3F 80 F9 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 3F FF FF FF FF F0 00 00 00 FF FF FF FF FF FE 3F 81 F3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 03 FF FF FF FF F0 00 00 00 FF FF FF FF FF FE 7F 03 C7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 03 FF FF FF FF F0 00 00 00 FF FF FF FF FF FC 7E 07 CF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FC 07 FF FF FF FF F0 00 00 00 FF FF FF FF FF F8 FC 0F 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF F0 FC 3E 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF F0 FC FC FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF E0 FD F9 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF E0 FF F1 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF E0 7F E3 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF E0 7F C7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF F0 7F 87 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF F8 7F 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FC 3C 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FC 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FE 00 1F FF FF FF FF FF FF FF FF FF F1 FF FF FF FF FF FF FF E0 01 FF E7 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF 80 3F FF FF FF FF FF FF FF FF FF F0 FF FF FF FF FF FF FF C0 00 7F CF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF F0 FF FF FF FF FF FF FF FF FF FF E0 7F FF FF FF FF FF FF 80 00 1F 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 3F FF FF FF FF FF FF 80 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 1F FF FF FF FF FF FF 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 1F FF FF FF FF FF FF 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 0F FF FF FF FF FF FE 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 0F FF FF FF FF FF FF 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 07 FF FF FF FF FF FF 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 03 FF FF FF FF FF FF 80 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 03 FF FF FF FF FF FF 80 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 01 FF FF FF FF FF FF C0 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 01 FF FF FF FF FF FF C0 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 7F FF FF FF FF FF E0 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 7F FF FF FF FF FF E0 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 3F FF FF FF FF FF C0 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 1F FF FF FF FF FF 80 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 0F FF FF FF FF FE 00 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 03 FF FF FF FF FC 0F 00 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF C0 00 00 00 07 FF FF F0 7F C0 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 00 FF 00 FF FF C1 FF F0 00 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 07 FF F8 7F FF 87 FF F8 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF E0 00 1F FF FF FF FF 1F FF FC 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 7F FF FF FF FF FF FF FF 00 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 FF FF FF FF FF FF FF FF 80 7F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 03 FF FF FF FF FF FF FF FF E0 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F8 1F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00");
			//byte[] bytes	= new byte[formats.length + image.length];

			//System.arraycopy(formats, 0, bytes, 0, formats.length);
			//System.arraycopy(image, 0, bytes, formats.length, image.length);
			
			//bluebamboo logo
			byte[] bytes = {(byte)0x1B,(byte)0x58,(byte)0x31,(byte)0x24,(byte)0x2D,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x1B,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x39,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x38,(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x7C,(byte)0x40,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0F,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x7E,(byte)0x20,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x10,(byte)0xC0,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x3F,(byte)0x10,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x37,(byte)0x40,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x06,(byte)0x9F,(byte)0x88,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x25,(byte)0x40,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0C,(byte)0x4F,(byte)0xF0,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x27,(byte)0x40,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x1E,(byte)0x27,(byte)0xE6,(byte)0x00,(byte)0x03,(byte)0xFF,(byte)0xFC,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xF0,(byte)0x07,(byte)0xFF,(byte)0xF8,(byte)0x7F,(byte)0xFF,(byte)0x1E,(byte)0x00,(byte)0x7D,(byte)0xFF,(byte)0xFE,(byte)0x0F,(byte)0xFF,(byte)0xC1,(byte)0xFF,(byte)0xF8,(byte)0x25,(byte)0x40,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x3F,(byte)0x93,(byte)0xCD,(byte)0x00,(byte)0x03,(byte)0xFF,(byte)0xFE,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xF0,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0x7F,(byte)0xFF,(byte)0x9F,(byte)0x00,(byte)0x7D,(byte)0xFF,(byte)0xFF,(byte)0x1F,(byte)0xFF,(byte)0xE3,(byte)0xFF,(byte)0xFC,(byte)0x10,(byte)0xC0,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x1F,(byte)0xC9,(byte)0x98,(byte)0x80,(byte)0x03,(byte)0xFF,(byte)0xFF,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xF0,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0xFF,(byte)0xFF,(byte)0x9F,(byte)0x00,(byte)0xFD,(byte)0xFF,(byte)0xFF,(byte)0x3F,(byte)0xFF,(byte)0xE3,(byte)0xFF,(byte)0xFE,(byte)0x0F,(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xCF,(byte)0xE4,(byte)0x3C,(byte)0x60,(byte)0x03,(byte)0xC0,(byte)0x0F,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0xFF,(byte)0xFF,(byte)0x9F,(byte)0x80,(byte)0xFD,(byte)0xFF,(byte)0xFF,(byte)0xBF,(byte)0xFF,(byte)0xF7,(byte)0xFF,(byte)0xFE,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0xA7,(byte)0xF2,(byte)0x3F,(byte)0x30,(byte)0x03,(byte)0x80,(byte)0x07,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x00,(byte)0x1C,(byte)0xE0,(byte)0x03,(byte)0x9F,(byte)0x81,(byte)0xFD,(byte)0xC0,(byte)0x07,(byte)0xB8,(byte)0x00,(byte)0xF7,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x93,(byte)0xFC,(byte)0x3F,(byte)0x98,(byte)0x03,(byte)0x80,(byte)0x07,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x00,(byte)0x1C,(byte)0xE0,(byte)0x03,(byte)0x9F,(byte)0xC3,(byte)0xFD,(byte)0xC0,(byte)0x07,(byte)0xB8,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0xC9,(byte)0xF9,(byte)0x9F,(byte)0xCC,(byte)0x03,(byte)0xFF,(byte)0xFE,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xE0,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0xE0,(byte)0x03,(byte)0x9F,(byte)0xC3,(byte)0xFD,(byte)0xFF,(byte)0xFF,(byte)0x38,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0xE4,(byte)0x73,(byte)0x4F,(byte)0xE4,(byte)0x03,(byte)0xFF,(byte)0xFE,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xE0,(byte)0x07,(byte)0xFF,(byte)0xF8,(byte)0xE7,(byte)0xFF,(byte)0x9D,(byte)0xE7,(byte)0xBD,(byte)0xFF,(byte)0xFF,(byte)0x38,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0xE2,(byte)0x72,(byte)0x27,(byte)0xFC,(byte)0x03,(byte)0xFF,(byte)0xFE,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xE0,(byte)0x07,(byte)0xFF,(byte)0xF8,(byte)0xE7,(byte)0xFF,(byte)0x9D,(byte)0xE7,(byte)0xBD,(byte)0xFF,(byte)0xFF,(byte)0x38,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0xF1,(byte)0x07,(byte)0x13,(byte)0xF8,(byte)0x03,(byte)0xFF,(byte)0xFF,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0xFF,(byte)0xE0,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0xE7,(byte)0xFF,(byte)0x9C,(byte)0xFF,(byte)0x3D,(byte)0xFF,(byte)0xFF,(byte)0x38,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0xF9,(byte)0x8F,(byte)0x89,(byte)0xF0,(byte)0x03,(byte)0xC0,(byte)0x07,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x00,(byte)0x1E,(byte)0xE7,(byte)0xFF,(byte)0x9C,(byte)0xFF,(byte)0x3D,(byte)0xC0,(byte)0x07,(byte)0xB8,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xFF,(byte)0x8F,(byte)0xC4,(byte)0xE0,(byte)0x03,(byte)0x80,(byte)0x07,(byte)0x78,(byte)0x00,(byte)0x70,(byte)0x00,(byte)0xEF,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x00,(byte)0x1E,(byte)0xE0,(byte)0x03,(byte)0x9C,(byte)0x7E,(byte)0x3D,(byte)0xC0,(byte)0x03,(byte)0xB8,(byte)0x00,(byte)0x77,(byte)0x80,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x7E,(byte)0x27,(byte)0xE2,(byte)0x00,(byte)0x03,(byte)0xC0,(byte)0x07,(byte)0x78,(byte)0x00,(byte)0x78,(byte)0x01,(byte)0xEF,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x00,(byte)0x1E,(byte)0xE0,(byte)0x03,(byte)0x9C,(byte)0x3E,(byte)0x3D,(byte)0xE0,(byte)0x07,(byte)0xBC,(byte)0x00,(byte)0xF7,(byte)0xC0,(byte)0x1E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x3C,(byte)0xD3,(byte)0xF1,(byte)0x00,(byte)0x03,(byte)0xFF,(byte)0xFF,(byte)0x7F,(byte)0xFF,(byte)0x3F,(byte)0xFF,(byte)0xEF,(byte)0xFF,(byte)0xF0,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0xE0,(byte)0x03,(byte)0x9C,(byte)0x3C,(byte)0x3D,(byte)0xFF,(byte)0xFF,(byte)0xBF,(byte)0xFF,(byte)0xF3,(byte)0xFF,(byte)0xFE,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x19,(byte)0xC9,(byte)0xFA,(byte)0x00,(byte)0x03,(byte)0xFF,(byte)0xFF,(byte)0x7F,(byte)0xFF,(byte)0x3F,(byte)0xFF,(byte)0xCF,(byte)0xFF,(byte)0xF0,(byte)0x07,(byte)0xFF,(byte)0xFC,(byte)0xE0,(byte)0x03,(byte)0x9C,(byte)0x18,(byte)0x3D,(byte)0xFF,(byte)0xFF,(byte)0x1F,(byte)0xFF,(byte)0xE3,(byte)0xFF,(byte)0xFC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0xE4,(byte)0xFC,(byte)0x00,(byte)0x03,(byte)0xFF,(byte)0xFE,(byte)0x7F,(byte)0xFF,(byte)0x1F,(byte)0xFF,(byte)0x8F,(byte)0xFF,(byte)0xF0,(byte)0x07,(byte)0xFF,(byte)0xF8,(byte)0xE0,(byte)0x03,(byte)0x9C,(byte)0x18,(byte)0x3D,(byte)0xFF,(byte)0xFF,(byte)0x0F,(byte)0xFF,(byte)0xC1,(byte)0xFF,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0xF2,(byte)0x78,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0xFF,(byte)0xC0,(byte)0xC0,(byte)0x01,(byte)0x9C,(byte)0x00,(byte)0x19,(byte)0xFF,(byte)0xF8,(byte)0x03,(byte)0xFF,(byte)0x00,(byte)0x3F,(byte)0xE0,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0xF9,(byte)0x30,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0xFC,(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xFE,(byte)0x40,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x3F,(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x3F,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,};
			
			sendData(bytes);
			
			byte[] newline 	= Printer.printfont("\n\n",FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
			
			sendData(newline);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void printBarcode() { 
		String[] types = {"1D Barcode", "2D Barcode"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Choose Barcode")
	           .setItems(types, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
		               if (which == 0) {
		            	   print1DBarcode();
		               } else {
		            	   print2DBarcode();
		               }
	           }
	    });
	    
	    builder.create().show();
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {	    	
	        String action = intent.getAction();
	        
	        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
	        	final int state 	= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
	        	
	        	if (state == BluetoothAdapter.STATE_ON) {
//	        		showEnabled();
	        	} else if (state == BluetoothAdapter.STATE_OFF) {
//		        	showDisabled();
	        	}
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        	mDeviceList = new ArrayList<BluetoothDevice>();
				
				mProgressDlg.show();
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	mProgressDlg.dismiss();
	        	
//	        	updateDeviceList();
	        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		        
	        	mDeviceList.add(device);
	        	
	        	showToast("Found device " + device.getName());
	        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
	        	 final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
	        	 
	        	 if (state == BluetoothDevice.BOND_BONDED) {
	        		 showToast("Paired");
	        		 
	        		 connect();
	        	 }
	        }
	    }
	};
	
}