package com.flourish;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class ContactsDetails extends BaseTimeOutActivity implements OnClickListener
{
	private LinearLayout mEmailsLl = null;
	private LinearLayout mPhonesLl = null;
	private RelativeLayout mCreateInvoiceRl = null;
	private TextView mPersonNameTv = null;
	private TextView mRevenueTv = null;
	private TextView mOutstandingTv = null;
	private Button mBackBtn = null;
	private Button mEditBtn = null;
	private EditText mBirthDateEt = null;
	private EditText mStreet1Et = null;
	private EditText mStreet2Et = null;
	private EditText mCityEt = null;
	private EditText mStateEt = null;
	private EditText mPostalCodeEt = null;
	private LayoutInflater mLayoutInflater = null;
	
	private ArrayList<String> mEmailList = new ArrayList<String>();
	private ArrayList<String> mPhoneNumberList = new ArrayList<String>();
	private ArrayList<String> mStreet1List = new ArrayList<String>();
	private ArrayList<String> mStreet2List = new ArrayList<String>();
	private ArrayList<String> mCityList = new ArrayList<String>();
	private ArrayList<String> mStateList = new ArrayList<String>();
	private ArrayList<String> mPostalCodeList = new ArrayList<String>();
	private ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private String mEmailAddressResponseStr = null;
	private String mPhoneNumbersResponseStr = null;
	private String mSalesByContactResponseStr = null;
	private String mSessionIdStr = null;
	private String mPersonIdStr = null;
	private String mPersonName = null;
	private String mPersonBirthDate = null;
	private String mOutstandingValueStr = null;
	private String mRevenueValueStr = null;
	private String mBirthDateStr = null;
	private String mPostalAddressResponseStr = null;
	FlourishBaseApplication mFlourishBaseApplication;



	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_details);
		
		onSessionStarted(true);

		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();

		
		mSessionIdStr = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mPersonIdStr = getIntent().getStringExtra("personId");
		mPersonName = getIntent().getStringExtra("personName");
		mPersonBirthDate = getIntent().getStringExtra("personBirthDate");
		mBirthDateStr = getIntent().getStringExtra("personBirthDate");
		Log.v("TAG", "person date of birth "+mBirthDateStr);
		Log.v("TAG", "mBirthDateStr"+mBirthDateStr);
		
		initializeVariables();
		setDateFormat();
		
		if(mAppNetwork.isNetworkAvailable(ContactsDetails.this))
		{
			new MyTask().execute();
		}
		else
		{
			mAppNetwork.getAlertDialog(ContactsDetails.this, getString(R.string.alert_dialog_no_network));
		}
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables() 
	{
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mBackBtn = (Button)findViewById(R.id.contact_details_back);
		mEditBtn = (Button)findViewById(R.id.contact_edit);
		mPersonNameTv = (TextView)findViewById(R.id.contact_details_name);
		mRevenueTv = (TextView)findViewById(R.id.payment_sales_value);
		mOutstandingTv = (TextView)findViewById(R.id.payment_remaining_balance_value);
		mEmailsLl = (LinearLayout)findViewById(R.id.email_list);
		mPhonesLl = (LinearLayout)findViewById(R.id.phone_list);
		mCreateInvoiceRl = (RelativeLayout)findViewById(R.id.create_invoice_rl);
		mBirthDateEt = (EditText)findViewById(R.id.contact_details_birth_date_et);
		mStreet1Et = (EditText)findViewById(R.id.contact_details_address_street1);
		mStreet2Et = (EditText)findViewById(R.id.contact_details_address_street2);
		mCityEt = (EditText)findViewById(R.id.contact_details_address_city);
		mStateEt = (EditText)findViewById(R.id.contact_details_address_state);
		mPostalCodeEt = (EditText)findViewById(R.id.contact_details_address_zipcode);
		mPersonNameTv.setText(mPersonName);
		mBackBtn.setOnClickListener(this);
		mEditBtn.setOnClickListener(this);
		mCreateInvoiceRl.setOnClickListener(this);
	}
	
	private void setDateFormat() 
	{
		String[] mPersonBirthDateArray = mBirthDateStr.split("-");
		mBirthDateStr = mPersonBirthDateArray[1]+"/"+mPersonBirthDateArray[2].substring(0, 2)+"/"+mPersonBirthDateArray[0];
		mBirthDateEt.setText(mBirthDateStr);
	}

	/**
	 * AsynTask for making Server request and response for getting contact details
	 */
	private class MyTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(ContactsDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			if(mSessionIdStr.equalsIgnoreCase("nothing"))
			{
				Toast.makeText(ContactsDetails.this, "Unable to get the person details", Toast.LENGTH_LONG).show();
			}
			else
			{

               String get_contacts_email_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_email_url;
               String get_contacts_phone_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_phone_url;
               String sales_by_contact_totals_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.sales_by_contact_totals_url;
               String get_contacts_address_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_address_url;
				
				
				
				
				mEmailAddressResponseStr = mConnectionManager.setUpHttpGet(get_contacts_email_url+mSessionIdStr+"/"+mPersonIdStr);
				getEmailAddressList();
				mPhoneNumbersResponseStr = mConnectionManager.setUpHttpGet(get_contacts_phone_url+mSessionIdStr+"/"+mPersonIdStr);
				getPhoneNumberList();
				mSalesByContactResponseStr = mConnectionManager.setUpHttpGet(sales_by_contact_totals_url+mSessionIdStr+"/"+mPersonIdStr);
				getSalesValues();
				mPostalAddressResponseStr = mConnectionManager.setUpHttpGet(get_contacts_address_url+mSessionIdStr+"/"+mPersonIdStr);
				getPostalAddressList();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(ContactsDetails.this);
				getEmailDetails();
				getPhoneNumbersDetails();
				setPostalAddressesDetails();
				
				if (mRevenueValueStr==null) {
					 mRevenueTv.setText("$0.00");
				}
				else{
					 mRevenueTv.setText("$"+mRevenueValueStr);
				}
				
				
				if (mOutstandingValueStr==null) {
					mOutstandingTv.setText("$0.00");
				}
			      
				else
				{
					mOutstandingTv.setText("$"+mOutstandingValueStr);
				}
		
			if(mEmailAddressResponseStr.contains("Login Key Not Valid")||mPhoneNumbersResponseStr.contains("Login Key Not Valid")||mSalesByContactResponseStr.contains("Login Key Not Valid"))
			{
				getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(ContactsDetails.this, LoginScreen.class));
			}
			else if(mEmailAddressResponseStr.contains("Bad Parameters")||mPhoneNumbersResponseStr.contains("Bad Parameters")||mSalesByContactResponseStr.contains("Bad Parameters"))
			{
				mAppNetwork.getAlertDialog(ContactsDetails.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for parsing Server response for getting contact email details
	 */
	private void getEmailAddressList()
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mEmailAddressResponseStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mEmailList.add(mJsonArray.getJSONObject(i).getString("Value"));
				Log.v("TAG", "mEmailList"+mEmailList);
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
			
			Log.v("TAG", "email list exception "+e.toString());
		}
	}

	/**
	 * Method for parsing Server response for getting contact sales details
	 */
	public void getSalesValues() 
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mSalesByContactResponseStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mRevenueValueStr = mJsonArray.getJSONObject(i).getString("SumTotal").replaceAll("-", "");
				mOutstandingValueStr = mJsonArray.getJSONObject(i).getString("Outstanding").replaceAll("-", "");
		
				
				
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
			
			Log.v("TAG", "getSalesValues exception "+e.toString());
		}
	}

	/**
	 * Method for parsing Server response for getting contact phone number details
	 */
	private void getPhoneNumberList() 
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mPhoneNumbersResponseStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mPhoneNumberList.add(mJsonArray.getJSONObject(i).getString("Value"));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
			Log.v("TAG", "getPhoneNumberList exception "+e.toString());
			
		
		}
	}
	
	/**
	 * Method for parsing Server response for getting contact Postal Address details
	 */
	private void getPostalAddressList()
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mPostalAddressResponseStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mStreet1List.add(mJsonArray.getJSONObject(i).getString("Street1"));
				mStreet2List.add(mJsonArray.getJSONObject(i).getString("Street2"));
				mCityList.add(mJsonArray.getJSONObject(i).getString("City"));
				mStateList.add(mJsonArray.getJSONObject(i).getString("GeographicSubDivisionCd"));
				mPostalCodeList.add(mJsonArray.getJSONObject(i).getString("PostalCode"));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void setPostalAddressesDetails()
	{
		if(mStreet1List.size() > 0)
		{
			mStreet1Et.setText(mStreet1List.get(0));
			mStreet2Et.setText(mStreet2List.get(0));
			mCityEt.setText(mCityList.get(0));
			mStateEt.setText(mStateList.get(0));
			mPostalCodeEt.setText(mPostalCodeList.get(0));
		}
	}

	/**
	 * Method for designing email layouts
	 */
	private void getEmailDetails()
	{
		if(mEmailList.size() == 0)
		{
			final View view;
			mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mLayoutInflater.inflate(R.layout.contacts_row, null);

			TextView text = (TextView) view.findViewById(R.id.contacts_row_text);
			View line = (View) view.findViewById(R.id.contacts_row_line);
			line.setVisibility(View.GONE);
			text.setText("No emails found");
			mEmailsLl.addView(view);
		}
		else
		{
			for (int position = 0; position < mEmailList.size(); position++)
			{
				final View view;
				mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mLayoutInflater.inflate(R.layout.contacts_row, null);

				TextView text = (TextView) view.findViewById(R.id.contacts_row_text);
				View line = (View) view.findViewById(R.id.contacts_row_line);
				if(position == mEmailList.size()-1)
				{
					line.setVisibility(View.GONE);
				}
				if(mEmailList.get(position).trim().length() > 0)
				{
					if((!(mEmailList.get(position).equalsIgnoreCase("(null)"))))
					{
						text.setText(mEmailList.get(position));
					}
					mEmailsLl.addView(view);
				}
			}
		}
	}

	/**
	 * Method for designing phone number layouts
	 */
	private void getPhoneNumbersDetails()
	{
		if(mPhoneNumberList.size() == 0)
		{
			final View view;
			mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mLayoutInflater.inflate(R.layout.contacts_row, null);

			TextView text = (TextView) view.findViewById(R.id.contacts_row_text);
			View line = (View) view.findViewById(R.id.contacts_row_line);
			line.setVisibility(View.GONE);
			text.setText("No phone numbers found");
			mPhonesLl.addView(view);
		}
		else
		{
			for (int position = 0; position < mPhoneNumberList.size(); position++)
			{
				final View view;
				mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mLayoutInflater.inflate(R.layout.contacts_row, null);

				TextView text = (TextView) view.findViewById(R.id.contacts_row_text);
				View line = (View) view.findViewById(R.id.contacts_row_line);
				if(position == mPhoneNumberList.size()-1)
				{
					line.setVisibility(View.GONE);
				}
				if(mPhoneNumberList.get(position).trim().length() > 0)
				{
					if((!(mPhoneNumberList.get(position).equalsIgnoreCase("(null)"))))
					{
						text.setText(mPhoneNumberList.get(position));
					}
					mPhonesLl.addView(view);
				}
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.contact_details_back:
			//startActivity(new Intent(ContactsDetails.this, FlourishHomeActivity.class));
			finish();
			break;

		case R.id.contact_edit:
			startActivity(new Intent(ContactsDetails.this, ContactEdit.class).putExtra("personName", mPersonName)
					.putExtra("personId", mPersonIdStr)
					.putExtra("personBirthDate", mPersonBirthDate));
			break;

		case R.id.create_invoice_rl:
			startActivity(new Intent(ContactsDetails.this, CreateInvoice.class).putExtra("personId", mPersonIdStr)
					.putExtra("personName", mPersonName));
					
			
			//pick the person name from contact 
			mFlourishBaseApplication.mPersonNameFromContact=1;
			
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
		{
			//startActivity(new Intent(ContactsDetails.this, FlourishHomeActivity.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}