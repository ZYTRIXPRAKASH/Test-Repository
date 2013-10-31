/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flourish.propay.payments;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.R;
import com.magtek.mobile.android.scra.ConfigParam;
import com.magtek.mobile.android.scra.MTSCRAException;
import com.magtek.mobile.android.scra.MagTekSCRA;
import com.magtek.mobile.android.scra.ProcessMessageResponse;
import com.magtek.mobile.android.scra.SCRAConfiguration;
import com.magtek.mobile.android.scra.SCRAConfigurationDeviceInfo;
import com.magtek.mobile.android.scra.SCRAConfigurationReaderType;
import com.magtek.mobile.android.scra.StatusCode;
//import com.magtek.mobile.android.scra.ArrayOfConfigParam;

/**
 * This is the main Activity that displays the current chat session.
 */
public class AddPropayPayment extends Activity {

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int STATUS_IDLE = 1;
	public static final int STATUS_PROCESSCARD = 2;
	private static final int MESSAGE_UPDATE_GUI = 6;
	public static final String CONFIGWS_URL = "https://deviceconfig.magensa.net/service.asmx";//Production URL
	

	private static final int CONFIGWS_READERTYPE = 0;
	private static final String CONFIGWS_USERNAME = "magtek";
	private static final String CONFIGWS_PASSWORD = "p@ssword";
	Intent mIntent=null;
	String mInvoiceId=null;
	private AudioManager mAudioMgr;	
	public static final String DEVICE_NAME = "device_name";
	public static final String CONFIG_FILE = "MTSCRADevConfig.cfg";
	public static final String TOAST = "toast";
	public static final String PARTIAL_AUTH_INDICATOR = "1";

	// Intent request codes;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	
	private MagTekSCRA mMTSCRA;
	private int miDeviceType=MagTekSCRA.DEVICE_TYPE_NONE;
	private Handler mSCRADataHandler = new Handler(new SCRAHandlerCallback());
	final headSetBroadCastReceiver mHeadsetReceiver = new headSetBroadCastReceiver();
	final NoisyAudioStreamReceiver mNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();

	private Button mSwipCardButton=null;
	private Button mkeyInButton=null;
	private Button mActivityBackButton=null;
	private String mInvoiceNumber=null;
	
	private TextView mActivityTopbarTextView=null;
	// Layout Views

	
	private LinearLayout mDeviceNotConnected_ll;
	private LinearLayout mDeviceConnected_ll;
	
	

	private int miReadCount=0;
	private String mStringDebugData;

	String mStringLocalConfig;
	private int mIntCurrentDeviceStatus;
	private RelativeLayout mTitleLayout;

	// =============================================================================================================
    //private Boolean mBooleanBTConnect;

	private boolean mbAudioConnected;

	private long mLongTimerInterval;

	private int mIntCurrentStatus;

	private int mIntCurrentVolume;
	
	// private String mRegisterScorePCodeResponse;
	// =============================================================================================================
	// private SensorManager mSensorMgr;
	// =============================================================================================================
	Handler GUIUpdateTimerHandler;

	final Handler mUIProcessCardHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		mIntent=getIntent();
		mInvoiceId=mIntent.getStringExtra("invoiceId");
		mInvoiceNumber=mIntent.getStringExtra("invoiceNumber");
         
		
		mSwipCardButton=(Button)findViewById(R.id.swipe_card_btn);
		mkeyInButton=(Button)findViewById(R.id.key_in_card_btn);
		mActivityBackButton=(Button)findViewById(R.id.activity_topBar_left_btn);
		mActivityTopbarTextView=(TextView)findViewById(R.id.activity_topbar_tv);
		mActivityTopbarTextView.setText(getResources().getString(R.string.invoice)+" #"+mInvoiceNumber);
		
		mDeviceNotConnected_ll=(LinearLayout)findViewById(R.id.deviceStatus_notConnected_ll);
		mDeviceConnected_ll=(LinearLayout)findViewById(R.id.deviceStatus_Connected_ll);
	
		mMTSCRA = new MagTekSCRA(mSCRADataHandler);
		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);			
		setStatus(R.string.status_default, Color.RED);
		InitializeData();
		
		mIntCurrentVolume = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);



mkeyInButton.setOnClickListener(buttonClickListner);
mActivityBackButton.setOnClickListener(buttonClickListner);



     mSwipCardButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				debugMsg("Android.Model=" + android.os.Build.MODEL);
				debugMsg("Android.Device=" + android.os.Build.DEVICE);
				debugMsg("Android.Product=" + android.os.Build.PRODUCT);
				debugMsg("Android.id=" + android.os.Build.ID);
				
				String mStringBody="";
				try
				{
					String strVersion = "";
					PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
					strVersion =  pInfo.versionName;
					mStringBody = "App.Version=" + strVersion + "\nSDK.Version=" + mMTSCRA.getSDKVersion() + "\n"; 
					
				}
				catch(Exception ex)
				{
					
				}		
				mStringBody += "Android.Model=" + android.os.Build.MODEL + "\n";
				mStringBody += "Android.Device=" + android.os.Build.DEVICE + "\n";
				mStringBody += "Android.Product=" + android.os.Build.PRODUCT + "\n";
				mStringBody += "Android.id=" + android.os.Build.ID + "\n";
				mStringBody += "Android.Display=" + android.os.Build.DISPLAY + "\n";
				mStringBody += "Android.radio=" + android.os.Build.RADIO + "\n";
				mStringBody += mStringDebugData;
			   Log.v("TAG", "Device details  "+mStringBody);
				//mCardDataEditText.setText(mStringBody);
				/*Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String[] recipients = new String[]{"softeng@magtek.com"};
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MagTek Audio Debug Info");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mStringBody);
                emailIntent.setType("text/plain");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));		*/
				
				if (mMTSCRA.isDeviceConnected()) {
					
					Toast.makeText(getApplicationContext(), "device connected"+mMTSCRA.isDeviceConnected(), 500).show();
					
				}
				else
				{
					mMTSCRA.isDeviceConnected();
					
					mMTSCRA.openDevice();
			
				}
	
				}
		});
		

		Timer tTimer = new Timer();

		TimerTask tTimerTask = new TimerTask() {
			public void run() {
				if (mMTSCRA.isDeviceConnected()) {
					if (mLongTimerInterval >= 2) {
						if (mMTSCRA.isDeviceConnected())
						{
							if (mIntCurrentStatus == STATUS_IDLE) 
							{
								setStatus(R.string.status_default, Color.GREEN);
							}
						}// if(mDeviceStatus==BluetoothChatService.STATE_CONNECTED)
						else
						{
							setStatus(R.string.status_default, Color.RED);
						}
						mLongTimerInterval = 0;
					}// if(mTimerInterval >= 2)

				}// if(mDeviceStatus==BluetoothChatService.STATE_CONNECTED)
				else 
				{

					if ((mIntCurrentStatus == STATUS_IDLE)&&(mIntCurrentDeviceStatus == MagTekSCRA.DEVICE_STATE_DISCONNECTED))
					{
						setStatus(R.string.status_default, Color.RED);
					}
				}
				mLongTimerInterval++;
			}
		};
		tTimer.scheduleAtFixedRate(tTimerTask, 0, 1000);
		displayInfo();
		

	}
	
View.OnClickListener buttonClickListner=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.activity_topBar_left_btn:
				finish();
				
				break;
				
				case R.id.key_in_card_btn:
				mIntent=new  Intent(AddPropayPayment.this, PayPropayPayment.class);
				mIntent.putExtra("invoiceNumber", mInvoiceNumber);
				startActivity(mIntent);
				finish();
				
				break;

			default:
				break;
			}
			
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	String getConfigurationLocal()
	{
		String strXMLConfig="";
		try
		{
			strXMLConfig = ReadSettings(getApplicationContext(),CONFIG_FILE);
			if(strXMLConfig==null)strXMLConfig="";
		}
		catch (Exception ex)
		{
		}
		
		return strXMLConfig;
	
	}
	void setConfigurationLocal(String lpstrConfig)
	{
		try
		{
			WriteSettings(getApplicationContext(),lpstrConfig,CONFIG_FILE);
		}
		catch (Exception ex)
		{
			
		}
		
	}
	void dumpWebConfigResponse(ProcessMessageResponse lpMessageResponse)
	{
		String strDisplay="";
		try
		{
            
				if(lpMessageResponse!=null)
				{
					if(lpMessageResponse.Payload!=null)
					{
						if(lpMessageResponse.Payload.StatusCode!= null)
						{
							if(lpMessageResponse.Payload.StatusCode.Number==0)
							{
								if(lpMessageResponse.Payload.SCRAConfigurations.size() > 0)
								{
									for (int i=0; i < lpMessageResponse.Payload.SCRAConfigurations.size();i++)
									{
										SCRAConfiguration tConfig = (SCRAConfiguration) lpMessageResponse.Payload.SCRAConfigurations.elementAt(i);
										strDisplay="********* Config:" + Integer.toString(i+1) + "***********\n";
										
										strDisplay+="DeviceInfo:Model:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_MODEL) + "\n";
										strDisplay+="DeviceInfo:Device:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_DEVICE) + "\n";
										strDisplay+="DeviceInfo:Firmware:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_FIRMWARE) + "\n";
										strDisplay+="DeviceInfo.Platform:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_PLATFORM) + "\n";
										strDisplay+="DeviceInfo:Product:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_PRODUCT) + "\n";
										strDisplay+="DeviceInfo:Release:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_RELEASE) + "\n";
										strDisplay+="DeviceInfo:SDK:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_SDK) + "\n";
										strDisplay+="DeviceInfo:Status:" + tConfig.DeviceInfo.getProperty(SCRAConfigurationDeviceInfo.PROP_STATUS)+ "\n";
										//Status = 0 Unknown
										//Status = 1 Tested and Passed 
										//Status = 2 Tested and Failed 
										strDisplay+="ReaderType.Name:" + tConfig.ReaderType.getProperty(SCRAConfigurationReaderType.PROP_NAME) + "\n";
										strDisplay+="ReaderType.Type:" + tConfig.ReaderType.getProperty(SCRAConfigurationReaderType.PROP_TYPE) + "\n";
										strDisplay+="ReaderType.Version:" + tConfig.ReaderType.getProperty(SCRAConfigurationReaderType.PROP_VERSION) + "\n";
										strDisplay+="ReaderType.SDK:" + tConfig.ReaderType.getProperty(SCRAConfigurationReaderType.PROP_SDK) + "\n";
										strDisplay+="StatusCode.Description:" + tConfig.StatusCode.Description + "\n";
										strDisplay+="StatusCode.Number:" + tConfig.StatusCode.Number + "\n";
										strDisplay+="StatusCode.Version:" + tConfig.StatusCode.Version + "\n";
										for (int j=0; j < tConfig.ConfigParams.size();j++)
										{
											strDisplay+="ConfigParam.Name:" + ((ConfigParam)tConfig.ConfigParams.elementAt(j)).Name + "\n";
											strDisplay+="ConfigParam.Type:" + ((ConfigParam)tConfig.ConfigParams.elementAt(j)).Type + "\n";
											strDisplay+="ConfigParam.Value:" + ((ConfigParam)tConfig.ConfigParams.elementAt(j)).Value + "\n";
										}//for (int j=0; j < tConfig.ConfigParams.size();j++)
										strDisplay+="*********  Config:" + Integer.toString(i+1) + "***********\n";
										debugMsg(strDisplay);
									}//for (int i=0; i < lpMessageResponse.Payload.SCRAConfigurations.size();i++)
									//debugMsg(strDisplay);
								}//if(lpMessageResponse.Payload.SCRAConfigurations.size() > 0)
								
							}//if(lpMessageResponse.Payload.StatusCode.Number==0)
							strDisplay= "Payload.StatusCode.Version:" + lpMessageResponse.Payload.StatusCode.getProperty(StatusCode.PROP_VERSION) + "\n";
							strDisplay+="Payload.StatusCode.Number:" + lpMessageResponse.Payload.StatusCode.getProperty(StatusCode.PROP_NUMBER) + "\n";
							strDisplay+="Payload.StatusCode.Description:" + lpMessageResponse.Payload.StatusCode.getProperty(StatusCode.PROP_DESCRIPTION) + "\n";
							debugMsg(strDisplay);
						}//if(lpMessageResponse.Payload.StatusCode!= null)
							
					}//if(lpMessageResponse.Payload!=null)
				}//if(lpMessageResponse!=null)
				else
				{
					debugMsg("Configuration Not Found");
				}
			
		}
		catch(Exception ex)
		{
			debugMsg("Exception:" + ex.getMessage());
		}
		
	}
	
	void dumpWebConfigResponse(String lpstrXML)
	{
		debugMsg(lpstrXML);
		
	}

	void setAudioConfigManual()throws MTSCRAException
	{
    	String model = android.os.Build.MODEL.toUpperCase();
		try
		{
	    	if(model.contains("DROID RAZR") || model.toUpperCase().contains("XT910"))
	        {
				  debugMsg("Found Setting for :"  + model); 
				   mMTSCRA.setConfigurationParams("INPUT_SAMPLE_RATE_IN_HZ=48000,");
	        }
	        else if ((model.equals("DROID PRO"))||
	        		 (model.equals("MB508"))||
	        		 (model.equals("DROIDX"))||
	        		 (model.equals("DROID2"))||
	        		 (model.equals("MB525")))
	        {
				  debugMsg("Found Setting for :"  + model); 
				  mMTSCRA.setConfigurationParams("INPUT_SAMPLE_RATE_IN_HZ=32000,");
	        }    	
	        else if ((model.equals("GT-I9300"))||//S3 GSM Unlocked
	        		 (model.equals("SPH-L710"))||//S3 Sprint
	        		 (model.equals("SGH-T999"))||//S3 T-Mobile
	        		 (model.equals("SCH-I535"))||//S3 Verizon
	        		 (model.equals("SCH-R530"))||//S3 US Cellular
	        		 (model.equals("SAMSUNG-SGH-I747"))||// S3 AT&T
	        		 (model.equals("M532"))||//Fujitsu
	        		 (model.equals("GT-N7100"))||//Notes 2 
	        		 (model.equals("GT-N7105"))||//Notes 2 
	        		 (model.equals("SAMSUNG-SGH-I317"))||// Notes 2
	        		 (model.equals("SCH-I605"))||// Notes 2
	        		 (model.equals("SCH-R950"))||// Notes 2
	        		 (model.equals("SGH-T889"))||// Notes 2
	        		 (model.equals("SPH-L900"))||// Notes 2
	        		 (model.equals("GT-P3113")))//Galaxy Tab 2, 7.0
	        		
	        {
				  debugMsg("Found Setting for :"  + model); 
	        	  mMTSCRA.setConfigurationParams("INPUT_AUDIO_SOURCE=VRECOG,");
	        }
	        else if ((model.equals("XT907")))
	        {
				  debugMsg("Found Setting for :"  + model); 
				  mMTSCRA.setConfigurationParams("INPUT_WAVE_FORM=0,");
	        }    	
	    	
	        else
	        {
				  debugMsg("Found Setting for :"  + model); 
	        	  mMTSCRA.setConfigurationParams("INPUT_AUDIO_SOURCE=VRECOG,");
	        }
			
		}
		catch(MTSCRAException ex)
		{
			debugMsg("Exception:" + ex.getMessage());
			throw new MTSCRAException(ex.getMessage());
		}
		
	}
	void setupAudioParameters()throws MTSCRAException
    {
		mStringLocalConfig="";
		
		try
		{
            
			//Option 1
			/*
			if (mGetConfigFromWeb.isChecked())
			{
			  debugMsg("Retrieve Configuration From Web....");
			  mMTSCRA.setConfiguration(CONFIGWS_READERTYPE,null,CONFIGWS_URL,10000);//Call Web Service to retrieve XML
			  return;

			}
			*/
			

			//Option 2
			/*
			if (mGetConfigFromWeb.isChecked())
			{
			  debugMsg("Retrieve Configuration From Web....");
			  
			  ProcessMessageResponse pResponse = mMTSCRA.getConfigurationResponse(CONFIGWS_USERNAME,CONFIGWS_PASSWORD,CONFIGWS_READERTYPE,null,CONFIGWS_URL,10000);
			  if(pResponse!=null)
			  {
				  dumpWebConfigResponse(pResponse);
				  mMTSCRA.setConfigurationResponse(pResponse); 
			  }
			  return;
			}
			*/
			
			
			//Option 3..
		
		}
		catch(Exception ex)
		{
			
		}
  
    
    }
		
	public void onStart() {
		super.onStart();
		// If BT is not on, request that it be enabled.
		
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		this.registerReceiver(mHeadsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		this.registerReceiver(mNoisyAudioStreamReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
	}


	@Override
	public synchronized void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		unregisterReceiver(mHeadsetReceiver);
		unregisterReceiver(mNoisyAudioStreamReceiver);
		if (mMTSCRA != null)
			closeDevice();
	
	}

	private void openDevice()
	{
		try
		{
			if(mMTSCRA.getDeviceType()==MagTekSCRA.DEVICE_TYPE_AUDIO)
			{
				setupAudioParameters();
				
			}
			mMTSCRA.openDevice();
			
		}
		catch(MTSCRAException ex)
		{
			
		}
	}
	private void closeDevice()
	{
		mMTSCRA.closeDevice();
	}

	private void ClearCardDataBuffer() {
		mMTSCRA.clearBuffers();

	}


	private void setStatus(int lpiStatus, int lpiColor) 
	{
		StatusTextUpdateHandler.sendEmptyMessage(lpiStatus);
		StatusColorUpdateHandler.sendEmptyMessage(lpiColor);
	}

	
	


	private void displayResponseData()
    {
    	
		String strDisplay="";

		
		String strResponse =  mMTSCRA.getResponseData();
		if(strResponse!=null)
		{
			strDisplay =  strDisplay + "Response.Length=" +strResponse.length()+ "\n";		
		}
		
		strDisplay =  strDisplay + "EncryptionStatus=" + mMTSCRA.getEncryptionStatus() + "\n";		
		strDisplay =  strDisplay + "SDK.Version=" + mMTSCRA.getSDKVersion() + "\n";
		strDisplay =  strDisplay + "Reader.Type=" + mMTSCRA.getDeviceType() + "\n";
		strDisplay =  strDisplay + "Track.Status=" + mMTSCRA.getTrackDecodeStatus() + "\n";
		strDisplay =  strDisplay + "KSN=" + mMTSCRA.getKSN()+ "\n";
		strDisplay =  strDisplay + "Track1.Masked=" + mMTSCRA.getTrack1Masked() + "\n";
		strDisplay =  strDisplay + "Track2.Masked=" + mMTSCRA.getTrack2Masked() + "\n";
		strDisplay =  strDisplay + "Track3.Masked=" + mMTSCRA.getTrack3Masked() + "\n";
		strDisplay =  strDisplay + "Track1.Encrypted=" + mMTSCRA.getTrack1() + "\n";
		strDisplay =  strDisplay + "Track2.Encrypted=" + mMTSCRA.getTrack2() + "\n";
		strDisplay =  strDisplay + "Track3.Encrypted=" + mMTSCRA.getTrack3() + "\n";  
		strDisplay =  strDisplay + "MagnePrint.Encrypted=" + mMTSCRA.getMagnePrint() + "\n";  
		strDisplay =  strDisplay + "MagnePrint.Status=" + mMTSCRA.getMagnePrintStatus() + "\n";  
		strDisplay =  strDisplay + "Card.IIN=" + mMTSCRA.getCardIIN() + "\n";
		strDisplay =  strDisplay + "Card.Name=" + mMTSCRA.getCardName() + "\n";
		strDisplay =  strDisplay + "Card.Last4=" + mMTSCRA.getCardLast4() + "\n";    	        	
		strDisplay =  strDisplay + "Card.ExpDate=" + mMTSCRA.getCardExpDate() + "\n";
		strDisplay =  strDisplay + "Card.SvcCode=" + mMTSCRA.getCardServiceCode() + "\n";
		strDisplay =  strDisplay + "Card.PANLength=" + mMTSCRA.getCardPANLength() + "\n";    
		strDisplay =  strDisplay + "Device.Serial=" + mMTSCRA.getDeviceSerial()+ "\n"; 
		strDisplay =  strDisplay  + "SessionID=" + mMTSCRA.getSessionID() + "\n";
		strDisplay =  strDisplay  + "CardLast4=" + mMTSCRA.getCardLast4()+ "\n";
		strDisplay =  strDisplay  + "CardCardIIN=" + mMTSCRA.getCardIIN()+ "\n";
		strDisplay =  strDisplay  + "CardServiceCode()=" + mMTSCRA.getCardServiceCode()+ "\n";
		strDisplay =  strDisplay  + "CardDataCRC=" + mMTSCRA.getCardDataCRC()+ "\n";
		
		Log.v("TAG", "strDisplay" +strDisplay);	
		
		
		
		
		
		switch(mMTSCRA.getDeviceType())
		{
		case MagTekSCRA.DEVICE_TYPE_AUDIO:
			strDisplay =  strDisplay  + "Card.Status=" + mMTSCRA.getCardStatus() + "\n";
			strDisplay =  strDisplay  + "Firmware.Partnumber=" + mMTSCRA.getFirmware()+ "\n";
			strDisplay =  strDisplay  + "MagTek.SN=" + mMTSCRA.getMagTekDeviceSerial()+ "\n";
			strDisplay =  strDisplay  + "TLV.Version=" + mMTSCRA.getTLVVersion()+ "\n";
			strDisplay =  strDisplay  + "HashCode=" + mMTSCRA.getHashCode()+ "\n";
			String tstrTkStatus = mMTSCRA.getTrackDecodeStatus();
			String tstrTk1Status="01";
			String tstrTk2Status="01";
			String tstrTk3Status="01";
			
			if(tstrTkStatus.length() >=6)
			{
				tstrTk1Status=tstrTkStatus.substring(0,2);
				tstrTk2Status=tstrTkStatus.substring(2,4);
				tstrTk3Status=tstrTkStatus.substring(4,6);
				debugMsg("Track1.Status=" + tstrTk1Status );
				debugMsg("Track2.Status=" + tstrTk2Status );
				debugMsg("Track3.Status=" + tstrTk3Status );
				if ((!tstrTk1Status.equalsIgnoreCase("01"))&&
					(!tstrTk2Status.equalsIgnoreCase("01"))&&	
					(!tstrTk3Status.equalsIgnoreCase("01")))
				{
					
					//closeDevice();

					
				}
			}
			else
			{
				closeDevice();
			}
			break;
		case MagTekSCRA.DEVICE_TYPE_BLUETOOTH:
			strDisplay =  strDisplay  + "CardDataCRC=" + mMTSCRA.getCardDataCRC() + "\n";
			
			break;
		default:
			break;
		
		};
		if(strResponse!=null)
		{
			strDisplay =  strDisplay + "Response.Raw=" + strResponse + "\n";		
		}
		
		mStringDebugData = strDisplay;
	
		//mCardDataEditText.setText(strDisplay);
		
		//Toast.makeText(getApplicationContext(), "Data swiped=="+strDisplay, 500).show();
		
		if (mMTSCRA.getTrack2Masked().length()!=0) {
			
		
		
                 Intent mIntent=new Intent(AddPropayPayment.this, PayPropayPayment.class);
                 mIntent.putExtra("invoiceId",mInvoiceId);
                 mIntent.putExtra("invoiceNumber",mInvoiceNumber);
                 mIntent.putExtra("cardNumber",mMTSCRA.getTrack2Masked());
                 mIntent.putExtra("cardExperiDate",mMTSCRA.getCardExpDate());
                 mIntent.putExtra("cardLast4Digits",mMTSCRA.getCardLast4());
                 Log.v("TAG", "CCN=="+mMTSCRA.getTrack2Masked());
            
                 startActivity(mIntent);
                 
                 finish();
         		
                 
		}
		else{
			Toast.makeText(getApplicationContext(), "Card swipe error please swipe again!", 500).show();
		}
		
		
		
		
    	
    }    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CONNECT_DEVICE: {
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) 
				{
				    String address = data.getExtras().getString(
							DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					mMTSCRA.setDeviceType(MagTekSCRA.DEVICE_TYPE_BLUETOOTH);
					// if you know the address, you can directly specify here
					// in that case you would not need a UI to show the list
					// of BT devices
					mMTSCRA.setDeviceID(address);
					openDevice();
	
	
				}
			}
				break;
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			return true;
		case R.id.bluetooth:
			// Launch the DeviceListActivity to see devices and do scan
            if(!mMTSCRA.isDeviceConnected())
    	    {
    			Intent serverIntent = new Intent(this, DeviceListActivity.class);
    			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    	    }
			return true;
		case R.id.audio:
  		 
           if(!mMTSCRA.isDeviceConnected())
    	   {
               if(mbAudioConnected)
               {
          		   mMTSCRA.setDeviceType(MagTekSCRA.DEVICE_TYPE_AUDIO);
            	   openDevice();
               }
    	   }
			
			return true;
		case R.id.disconn:
	  		  
	           if(mMTSCRA.isDeviceConnected())
	    	   {
					closeDevice();
	    	   }
				
				return true;

		case R.id.exit:
			// Ensure this device is discoverable by others
			// ensureDiscoverable();
			if (mMTSCRA != null)
				closeDevice();

			setResult(Activity.RESULT_OK);
			this.finish();
			return true;
		}
		return false;
	}

	private Handler StatusTextUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case R.string.status_default:
				if(mMTSCRA.isDeviceConnected())
				{
					//mAppStatusTextView.setText(R.string.title_connected);
					mDeviceConnected_ll.setVisibility(View.VISIBLE);
					mDeviceNotConnected_ll.setVisibility(View.GONE);
					
				}
				else
				{
					//mAppStatusTextView.setText(R.string.title_not_connected);
					mDeviceConnected_ll.setVisibility(View.GONE);
					mDeviceNotConnected_ll.setVisibility(View.VISIBLE);
				
					//Toast.makeText(getApplicationContext(), "StatusTextUpdateHandler  status_default", Toast.LENGTH_SHORT).show();
			
				
				}
				
				
				break;
			default:
				
				//mAppStatusTextView.setText(msg.what);
				
				break;
			}
			mLongTimerInterval = 0;

		}
	};
	private Handler StatusColorUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//mAppStatusTextView.setBackgroundColor(msg.what);
			
			mLongTimerInterval = 0;
		}
	};
	
    void ShowSoftKeyboard (EditText lpEditText)
    {
		InputMethodManager objInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// only will trigger it if no physical keyboard is open
		objInputManager.showSoftInput(lpEditText, InputMethodManager.SHOW_IMPLICIT);					

    }
    void HideSoftKeyboard (EditText lpEditText)
    {
		//Hide Keyboard
		InputMethodManager objInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		objInputManager.hideSoftInputFromWindow(lpEditText.getWindowToken(), 0);
    	
    }
	private void InitializeData() 
	{
	    mMTSCRA.clearBuffers();
		mLongTimerInterval = 0;
//		miReadCount=0;
		mbAudioConnected=false;
		mIntCurrentVolume=0;
		mIntCurrentStatus = STATUS_IDLE;
		mIntCurrentDeviceStatus = MagTekSCRA.DEVICE_STATE_DISCONNECTED;
		
		mStringDebugData ="";
		
	}
	private void debugMsg(String lpstrMessage)
	{
		Log.i("MagTekSCRA.Demo:",lpstrMessage);
		
	}

	private void clearAll() 
	{
		ClearCardDataBuffer();
	
		mIntCurrentStatus = STATUS_IDLE;
//		miReadCount = 0;
		mStringDebugData="";
		displayInfo();

	}
	private void displayInfo()
	{
		//ActivityManager tActivityManager =(ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		//MemoryInfo tMemoryInfo = new ActivityManager.MemoryInfo();
		//tActivityManager.getMemoryInfo(tMemoryInfo);		
		//String strLog = "SwipeCount=" + miReadCount + ",Memory=" + tMemoryInfo.availMem;
		String strVersion = "";
		
		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			strVersion =  pInfo.versionName;
			
		}
		catch(Exception ex)
		{
			
		}
		String strLog = "App.Version=" +strVersion + ",SDK.Version=" + mMTSCRA.getSDKVersion(); 
		//debugMsg(strLog);
		//mInfoTextView.setText(strLog);
		//tMemoryInfo=null;
		//tActivityManager=null;
		
	}
	private void maxVolume()
	{
		mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);	
			    	
	                
	}
	private void minVolume()
	{
		mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,mIntCurrentVolume, AudioManager.FLAG_SHOW_UI);
		
	}
	private class SCRAHandlerCallback implements Callback {
        public boolean handleMessage(Message msg) 
        {
        	
        	try
        	{
            	switch (msg.what) 
            	{
    			case MagTekSCRA.DEVICE_MESSAGE_STATE_CHANGE:
    				switch (msg.arg1) {
    				case MagTekSCRA.DEVICE_STATE_CONNECTED:
    					mIntCurrentStatus = STATUS_IDLE;
    					mIntCurrentDeviceStatus = MagTekSCRA.DEVICE_STATE_CONNECTED;    					
    					maxVolume();
    					setStatus(R.string.title_connected, Color.GREEN);
    					break;
    				case MagTekSCRA.DEVICE_STATE_CONNECTING:
    					mIntCurrentDeviceStatus = MagTekSCRA.DEVICE_STATE_CONNECTING;
    					setStatus(R.string.title_connecting, Color.YELLOW);
    					break;
    				case MagTekSCRA.DEVICE_STATE_DISCONNECTED:
    					mIntCurrentDeviceStatus = MagTekSCRA.DEVICE_STATE_DISCONNECTED;
    					setStatus(R.string.title_not_connected, Color.RED);
    					minVolume();
    					break;
    				}
    				break;
    			case MagTekSCRA.DEVICE_MESSAGE_DATA_START:
    	        	if (msg.obj != null) 
    	        	{
    	        		debugMsg("Transfer started");
    	        		//mCardDataEditText.setText("Card Swiped...");
    	        		Log.v("TAG", "Card swiped--");
    	                return true;
    	            }
    				break;  
    			case MagTekSCRA.DEVICE_MESSAGE_DATA_CHANGE:
    	        	if (msg.obj != null) 
    	        	{
    	        		debugMsg("Transfer ended");
//    	        		miReadCount++;
    	        		displayInfo();
    	        		displayResponseData();
    	        		msg.obj=null;
    	        		if(mStringLocalConfig.length() > 0)
    	        		{
    						setConfigurationLocal(mStringLocalConfig);//optional but can be useful to retrieve from locally and get it from server only certain times
    						mStringLocalConfig="";
    	        		}

    	                return true;
    	            }
    				break;  
    			case MagTekSCRA.DEVICE_MESSAGE_DATA_ERROR:
	        		Toast.makeText(getApplicationContext(), "Card Swipe Error... Please Swipe Again", Toast.LENGTH_SHORT);
	                return true;
    			default:
    	        	if (msg.obj != null) 
    	        	{
    	                return true;
    	            }
    				break;
            	};
        		
        	}
        	catch(Exception ex)
        	{
        		
        	}
        	
            return false;
        	
        	
        }
    }	
	
	public class NoisyAudioStreamReceiver extends BroadcastReceiver
    {
    	@Override
    	public void onReceive(Context context, Intent intent)
    	{
    		/* If the device is unplugged, this will immediately detect that action,
    		 * and close the device.
    		 */
    		if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction()))
    		{
            	mbAudioConnected=false;
            	if(mMTSCRA.getDeviceType()==MagTekSCRA.DEVICE_TYPE_AUDIO)
            	{
            		if(mMTSCRA.isDeviceConnected())
            		{
            			closeDevice();
            			//clearScreen();
            		}
            	}
    		}
    	}
    }
	
	public class headSetBroadCastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            // TODO Auto-generated method stub

        	try
        	{
                String action = intent.getAction();
                //Log.i("Broadcast Receiver", action);
                if( (action.compareTo(Intent.ACTION_HEADSET_PLUG))  == 0)   //if the action match a headset one
                {
                    int headSetState = intent.getIntExtra("state", 0);      //get the headset state property
                    int hasMicrophone = intent.getIntExtra("microphone", 0);//get the headset microphone property
  				    //mCardDataEditText.setText("Headset.Detected=" + headSetState + ",Microphone.Detected=" + hasMicrophone);

                    if( (headSetState == 1) && (hasMicrophone == 1))        //headset was unplugged & has no microphone
                    {
//                    	Toast.makeText(getApplicationContext(), "Device connected", Toast.LENGTH_SHORT).show();
                    	mbAudioConnected=true;
                    	openScreen();
                    	openDevice();
            			
                    
                    }
                    else 
                    {
                    	mbAudioConnected=false;
                    	if(mMTSCRA.getDeviceType()==MagTekSCRA.DEVICE_TYPE_AUDIO)
                    	{
                    		if(mMTSCRA.isDeviceConnected())
                    		{
                    			closeDevice();
                    			
                    		}
                    	}
                	
                    }

                }           
        		
        	}
        	catch(Exception ex)
        	{
        		
        	}

        }

    }	
	
	public static String ReadSettings(Context context, String file) throws IOException {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		String data = null;
		fis = context.openFileInput(file);
		isr = new InputStreamReader(fis);
		char[] inputBuffer = new char[fis.available()];
		isr.read(inputBuffer);
		data = new String(inputBuffer);
		isr.close();
		fis.close();
		return data;
	}
	
	public void openScreen() {
		mIntCurrentStatus = STATUS_IDLE;
		mIntCurrentDeviceStatus = MagTekSCRA.DEVICE_STATE_CONNECTED;    					
		maxVolume();
		setStatus(R.string.title_connected, Color.GREEN);
		
	}
	public static void WriteSettings(Context context, String data, String file) throws IOException {
		FileOutputStream fos= null;
		OutputStreamWriter osw = null;
		fos= context.openFileOutput(file,Context.MODE_PRIVATE);
		osw = new OutputStreamWriter(fos);
		osw.write(data);
		osw.close();
		fos.close();
	}
 
}
