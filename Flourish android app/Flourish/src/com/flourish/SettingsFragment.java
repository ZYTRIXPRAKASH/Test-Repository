package com.flourish;



import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class SettingsFragment extends Fragment {
	
	Fragment mFragment;
	AppNetwork mAppNetwork;
	
    FlourishBaseApplication mFlourishBaseApplication;
    String mInventoryProductListTaskUrl;
	ConnectionManager mConnectionManager=null;
	String  mTaskUrl=null;
	String mResponse=null;
	public String mSessionId = null;
	private String mSaveUserSettingsResponse;

	Button mSettingBackButton=null;
	Button mSaveSettingButton=null;
	Button mLogoutButton=null;
	
	public String mProPayEmail =null;
	public String mTaxRate ="8.5";
	public String mAlwaysDeliverInvoices =null;
	public String mTaxShipping =null;
	
	EditText mProPayEmail_EditText=null;
	TextView mTaxRate_TextView=null;
	Button mAlwaysDeliverInvoices_btn=null;
	Button mTaxShipping_btn=null;
	SharedPreferences mSharedPreferences;
	
	
		
	
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.v("TAG", "session id in Inventory Fragment "+mSessionId);
		
		
		
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stubS

		//mMenuDrawer =MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT);
       // mMenuDrawer.setContentView(R.layout.flourish_home);
		
		
		mAppNetwork=new AppNetwork();
        mFlourishBaseApplication=(FlourishBaseApplication)getActivity().getApplicationContext();
		Log.v("TAG", "baseUrl "+mFlourishBaseApplication.mFlurishBaseUrl);
		mSessionId = getActivity().getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		//mSharedPreferences=getSharedPreferences("myfile",MODE_PRIVATE);
	
		View mView;
		
		mView=inflater.inflate(R.layout.settings_fragment, container, false);
		
		mProPayEmail_EditText=(EditText)mView.findViewById(R.id.propay_email_et);
		mTaxRate_TextView=(TextView)mView.findViewById(R.id.tax_rate_tv);
		mAlwaysDeliverInvoices_btn=(Button)mView.findViewById(R.id.AlwaysDeliverInvoices_tbtn);
		mTaxShipping_btn=(Button)mView.findViewById(R.id.Tax_Shaipping_tbtn_settings);
		
		
		//mSettingBackButton=(Button)mView.findViewById(R.id.SettinsTopbarBack_Btn);
		//mSaveSettingButton=(Button)mView.findViewById(R.id.settingTopbarSave_settings_Btn);
		mLogoutButton=(Button)mView.findViewById(R.id.logout_btn);
		
		
		mAppNetwork=new AppNetwork();
		mSessionId = getActivity().getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		
	
		
		
		
		if (mAppNetwork.isNetworkAvailable(getActivity())) 
		{
			new getUserSettingAsynkTask().execute();
		}
		else 
		{
			mAppNetwork.getAlertDialog(getActivity(), getResources().getString(R.string.alert_dialog_no_network));
		}
		
		
		
		
		
		
		mAlwaysDeliverInvoices_btn.setOnClickListener(buttonClickListener);
		mTaxShipping_btn.setOnClickListener(buttonClickListener);
		//mSettingBackButton.setOnClickListener(buttonClickListener);
		//mSaveSettingButton.setOnClickListener(buttonClickListener);
		mLogoutButton.setOnClickListener(buttonClickListener);
		
		
		return mView ;
	}
	
	
	
	
	
View.OnClickListener buttonClickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.SettinsTopbarBack_Btn:
				
				
				/*startActivity(new Intent(getActivity(), FlourishHomeActivity.class));
				SharedPreferences.Editor backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Dashboard");
				 backeditor.commit();
				 //finish();
				  * 
*/				
				

			
				
				break;
             case R.id.settingTopbarSave_settings_Btn:
            	 
            		if (mAppNetwork.isNetworkAvailable(getActivity())) 
            		{
            			saveUserSettings();
            		}
            		else 
            		{
            			mAppNetwork.getAlertDialog(getActivity(), getResources().getString(R.string.alert_dialog_no_network));
            		}
				
				break;
             case R.id.AlwaysDeliverInvoices_tbtn:
            	
            	 if (mAlwaysDeliverInvoices.equals("true"))
            	 {  
            		
            		 mAlwaysDeliverInvoices="false";
            		 Log.v("TAG", "mAlwaysDeliverInvoices "+mAlwaysDeliverInvoices);
            		 mAlwaysDeliverInvoices_btn.setBackgroundResource(R.drawable.off);
					
				}
            	 else {
            		 
            		 mAlwaysDeliverInvoices="true";
            		 Log.v("TAG", "mAlwaysDeliverInvoices "+mAlwaysDeliverInvoices);
            		 mAlwaysDeliverInvoices_btn.setBackgroundResource(R.drawable.on);
				}
            		 
				
				break;
             case R.id.Tax_Shaipping_tbtn_settings:
            	 
            	 if (mTaxShipping.equalsIgnoreCase("True")) {
            	
				
            	
            		 mTaxShipping="False";
            		 Log.v("TAG", "mTaxShipping "+mTaxShipping);
            		 mTaxShipping_btn.setBackgroundResource(R.drawable.off);
				}
            	 else {
            		 mTaxShipping="True";
            		 Log.v("TAG", "mTaxShipping "+mTaxShipping);
            		 mTaxShipping_btn.setBackgroundResource(R.drawable.on );
					
				}
            	 
            
            	 
				break;
             case R.id.logout_btn:
            	 
            	 getActivity().finish();
            	 
            	 
            	 
            	 
            	     
            	 
            	 break;

			default:
				break;
			}
			
		}
	};
	
	
	
	
	
	
	class getUserSettingAsynkTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			FlourishProgressDialog.ShowProgressDialog(getActivity());
			super.onPreExecute();
			
			
			
		}
		
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
	mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_user_settings;
	mConnectionManager=new ConnectionManager();
	mResponse=mConnectionManager.setUpHttpGet(mTaskUrl+mSessionId);
	
			if (mResponse!=null) {
				
				
	              Log.v("TAG", "Get User Setting "+mResponse);
	              
	              try {
	            	  
	            	  JSONObject mCompleteJsonObject=new JSONObject(mResponse);
	            	  
	            	  JSONObject dataJsonObject=mCompleteJsonObject.getJSONObject("data");
	            	  
	            	  mProPayEmail=dataJsonObject.getString("ProPayEmail");
	            	  mTaxRate=dataJsonObject.getString("TaxRate1");
	            	  mAlwaysDeliverInvoices=dataJsonObject.getString("AlwaysDeliverInvoices");
	            	  mTaxShipping=dataJsonObject.getString("TaxShipping");
	            	 
	            	  
	            	  
		              
					
				} catch (Exception e) {
					// TODO: handle exception
					Log.v("TAG", "Exception  "+e.toString());
				}
	              
	              
	              
	              
	              
				
				
				
			}
			
			
			
			
			return null;
		}
		
	
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			FlourishProgressDialog.Dismiss(getActivity());
			
			
			 mProPayEmail_EditText.setText(mProPayEmail);
			 mTaxRate_TextView.setText(mTaxRate);
			
			 
			/* mAlwaysDeliverInvoices_btn.setText(mAlwaysDeliverInvoices);
			
			 mTaxShipping_btn.setText(mTaxShipping);*/
			 
			// changing the back gorup for Always Deliver invoice  
			 
			 if (mAlwaysDeliverInvoices.equalsIgnoreCase("True")) {
				
			
				
				 mAlwaysDeliverInvoices_btn.setBackgroundResource(R.drawable.on);
			
			}
			 else {
				
				 mAlwaysDeliverInvoices_btn.setBackgroundResource(R.drawable.off);
				 
				 
			}
			
			
			 
			 //
			 
			 
			 if (mTaxShipping.equalsIgnoreCase("true")) {
				
				
				 mTaxShipping_btn.setBackgroundResource(R.drawable.on);
			}
			 else {
				
				 mTaxShipping_btn.setBackgroundResource(R.drawable.off);
			}
			
			
		
			
		}
		
		
		
		
		
		
	}
	
	
	
	//save user setting method
	
	
	public void saveUserSettings()
	{
		String mTaskurl_saveUserSettings=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.save_user_setting;
		
		mConnectionManager=new ConnectionManager();   
		
	
		
		try {
			
		
		JSONObject saveSettingJsonObject=new JSONObject();
		saveSettingJsonObject.put("ProPayEmail", mProPayEmail_EditText.getText().toString().trim());
		saveSettingJsonObject.put("TaxRate", mTaxRate);
		saveSettingJsonObject.put("AlwaysDeliver", mAlwaysDeliverInvoices);
		saveSettingJsonObject.put("TaxShipping", mTaxShipping);
		Log.v("TAG", "Request  "+saveSettingJsonObject.toString());
		mSaveUserSettingsResponse=mConnectionManager.setUpHttpPut(mTaskurl_saveUserSettings+"/"+mSessionId, saveSettingJsonObject.toString());
		
	
	
		
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	if (mSaveUserSettingsResponse!=null) {
			
		Log.v("TAG", "mSAVED Response"+mSaveUserSettingsResponse);
		
		startActivity(new Intent(getActivity(), FlourishHomeActivity.class));
		
		
		}
		
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
}
