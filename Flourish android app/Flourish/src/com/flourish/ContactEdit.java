package com.flourish;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class ContactEdit extends BaseTimeOutActivity implements OnClickListener
{
	private static final int DATE_DIALOG_ID = 1;
	private Button mBackBtn = null;
	private Button mSaveBtn = null;
	private Button mCalendarBtn = null;
	private Button mDeleteContactBtn = null;
	private LinearLayout mPhoneNumbersLl = null;
	private LinearLayout mEmailsLl = null;
	private RelativeLayout mEmailEtRl = null;
	private EditText mEmailEt = null;
	private RelativeLayout mPhoneNumberEtRl = null;
	private EditText mPhoneNumberEt = null;
	private EditText mFirstNameEt = null;
	private EditText mLastNameEt = null;
	private EditText mBirthDateEt = null;
	private EditText mStreet1Et = null;
	private EditText mStreet2Et = null;
	private EditText mCityEt = null;
	private EditText mStateEt = null;
	private EditText mPostalCodeEt = null;
	private DatePicker mBirthDatePicker = null;
	private String mFirstNameStr = null;
	private String mLastNameStr = null;
	private String mBirthDateStr = null;
	private String mPersonNameStr = null;
	private String mPersonIdStr = null;
	private String mPersonBirthDateStr = null;
	public String mSessionIdStr = null;
	public String mUpdateContactResponseStr = null;
	public String mDeleteContactResponseStr = null;
	private String mUpdateContactCallResponseStr = null;
	private String mAddContactPhoneNumberResponse = null;
	private String mAddContactEmailAddressResponse = null;
	private String mAddContactAddressResponse = null;
	private JSONObject mAddContactPhoneNumberRequestObj = null;
	private JSONObject mAddContactEmailAddressRequestObj = null;
	private JSONObject mAddContactAddressRequestObj = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Date mInputDate = null;
	public ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private JSONObject mUpdateContactRequestObj = null;
	private LayoutInflater mLayoutInflater = null;
	private String mEmailAddressResponseStr = null;
	private String mPhoneNumbersResponseStr = null;
	private String mPostalAddressResponseStr = null;
	Date inPutdate=null;
	
	
	private ArrayList<String> mEmailList = new ArrayList<String>();
	private ArrayList<String> mPhoneNumberList = new ArrayList<String>();
	private ArrayList<String> mStreet1List = new ArrayList<String>();
	private ArrayList<String> mStreet2List = new ArrayList<String>();
	private ArrayList<String> mCityList = new ArrayList<String>();
	private ArrayList<String> mStateList = new ArrayList<String>();
	private ArrayList<String> mPostalCodeList = new ArrayList<String>();
	
	FlourishBaseApplication mFlourishBaseApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_edit);
		onSessionStarted(true);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();

		initializeVariables();
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		mSessionIdStr = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
	
		
		mPersonNameStr = getIntent().getStringExtra("personName");
		mPersonIdStr = getIntent().getStringExtra("personId");
		mBirthDateStr = getIntent().getStringExtra("personBirthDate");
		String[] mPersonNameArray = mPersonNameStr.split(" ");
		mFirstNameStr = mPersonNameArray[0];
		mLastNameStr = mPersonNameArray[1];

		mFirstNameEt.setText(mFirstNameStr);
		mLastNameEt.setText(mLastNameStr);
		setDateFormat();
		if(mAppNetwork.isNetworkAvailable(ContactEdit.this))
		{
			new GetContactDetailsTask().execute();
		}
		else
		{
			mAppNetwork.getAlertDialog(ContactEdit.this, getString(R.string.alert_dialog_no_network));
		}
	}

	/**
	 * Method for initializing Variables
	 */
	private void initializeVariables() 
	{
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mBackBtn = (Button)findViewById(R.id.contact_edit_back);
		mSaveBtn = (Button)findViewById(R.id.contact_save);
		mCalendarBtn = (Button)findViewById(R.id.contact_edit_birth_date_btn);
		mDeleteContactBtn = (Button)findViewById(R.id.contact_delete);
		mFirstNameEt = (EditText)findViewById(R.id.contact_edit_first_name);
		mLastNameEt = (EditText)findViewById(R.id.contact_edit_last_name);
		mBirthDateEt = (EditText)findViewById(R.id.contact_edit_birth_date_et);
		mStreet1Et = (EditText)findViewById(R.id.contact_edit_address_street1);
		mStreet2Et = (EditText)findViewById(R.id.contact_edit_address_street2);
		mCityEt = (EditText)findViewById(R.id.contact_edit_address_city);
		mStateEt = (EditText)findViewById(R.id.contact_edit_address_state);
		mPostalCodeEt = (EditText)findViewById(R.id.contact_edit_address_zipcode);
		mBirthDatePicker = (DatePicker) findViewById(R.id.contact_edit_dpResult);
		mEmailsLl = (LinearLayout)findViewById(R.id.contact_edit_email_list);
		mPhoneNumbersLl = (LinearLayout)findViewById(R.id.contact_edit_phone_list);
		mEmailEtRl = (RelativeLayout)findViewById(R.id.contact_edit_email_et_rl);
		mEmailEt = (EditText)findViewById(R.id.contact_edit_email_et);
		mPhoneNumberEtRl = (RelativeLayout)findViewById(R.id.contact_edit_phone_et_rl);
		mPhoneNumberEt = (EditText)findViewById(R.id.contact_edit_phone_et);
		mBackBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		mCalendarBtn.setOnClickListener(this);
		mDeleteContactBtn.setOnClickListener(this);
		mBirthDateEt.setOnClickListener(this);
		
	}

	private void setDateFormat() 
	{
		String[] mPersonBirthDateArray = mBirthDateStr.split("-");
		mPersonBirthDateStr = mPersonBirthDateArray[1]+"/"+mPersonBirthDateArray[2].substring(0, 2)+"/"+mPersonBirthDateArray[0];
		mBirthDateEt.setText(mPersonBirthDateStr);
	}

	/**
	 * AsynTask for making Server request and response for getting contact details
	 */
	private class GetContactDetailsTask extends AsyncTask<Void, Void, Void>
	{
		
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(ContactEdit.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			if(mSessionIdStr.equalsIgnoreCase("nothing"))
			{
				Toast.makeText(ContactEdit.this, "Unable to get the person details", Toast.LENGTH_LONG).show();
			}
			else
			{
				String mTaskEmaiUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_email_url;
				String mTaskPhoneNumberUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_phone_url;
				String mTaskPostalAdressUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_address_url;
				
				mEmailAddressResponseStr = mConnectionManager.setUpHttpGet(mTaskEmaiUrl+mSessionIdStr+"/"+mPersonIdStr);
				getEmailAddressList();
				mPhoneNumbersResponseStr = mConnectionManager.setUpHttpGet(mTaskPhoneNumberUrl+mSessionIdStr+"/"+mPersonIdStr);
				getPhoneNumberList();
				mPostalAddressResponseStr = mConnectionManager.setUpHttpGet(mTaskPostalAdressUrl+mSessionIdStr+"/"+mPersonIdStr);
				getPostalAddressList();
				
				/*mEmailAddressResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_contacts_email_url)+"/"+mSessionIdStr+"/"+mPersonIdStr);
				getEmailAddressList();
				mPhoneNumbersResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_contacts_phone_url)+"/"+mSessionIdStr+"/"+mPersonIdStr);
				getPhoneNumberList();
				mPostalAddressResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_contacts_address_url)+"/"+mSessionIdStr+"/"+mPersonIdStr);
				getPostalAddressList();
				*/
				
				
				
				
				
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(ContactEdit.this);
				setEmailDetails();
				setPhoneNumbersDetails();
				setPostalAddressesDetails();
		
			/*else if(mEmailAddressResponseStr.contains("Login Key Not Valid")||mPhoneNumbersResponseStr.contains("Login Key Not Valid")||mPostalAddressResponseStr.contains("Login Key Not Valid"))
			{
				getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(ContactEdit.this, LoginScreen.class));
			}
			else if(mEmailAddressResponseStr.contains("Bad Parameters")||mPhoneNumbersResponseStr.contains("Bad Parameters")||mPostalAddressResponseStr.contains("Bad Parameters"))
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
			}*/
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
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
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
		}
	}

	/**
	 * Method for parsing Server response for getting contact email details
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
	 * Method for designing phone layouts
	 */
	private void setEmailDetails()
	{
		Log.v("response", "==mEmailList=="+mEmailList.size());
		if(mEmailList.size() == 0)
		{
			mEmailsLl.setVisibility(View.GONE);
			mEmailEtRl.setVisibility(View.VISIBLE);
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
	 * Method for designing phone layouts
	 */
	private void setPhoneNumbersDetails()
	{
		if(mPhoneNumberList.size() == 0)
		{
			mPhoneNumbersLl.setVisibility(View.GONE);
			mPhoneNumberEtRl.setVisibility(View.VISIBLE);
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
					mPhoneNumbersLl.addView(view);
				}
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.contact_edit_back:
			finish();
			break;

		case R.id.contact_edit_birth_date_btn:
			showDialog(DATE_DIALOG_ID);
			break;
			
			
			
		case R.id.contact_edit_birth_date_et:
			showDialog(DATE_DIALOG_ID);
			break;
			

		case R.id.contact_save:
			mFirstNameStr = mFirstNameEt.getText().toString();
			mLastNameStr = mLastNameEt.getText().toString();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mLastNameEt.getWindowToken(), 0);
			if(mSessionIdStr.equalsIgnoreCase("nothing"))
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_contact_not_updated));
			}
			else if(mFirstNameStr.trim().length()==0)
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.enter_your_first_name));
			}
			else if(mLastNameStr.trim().length()==0)
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.enter_your_last_name));
			}
			else if(mBirthDateEt.getText().toString().length()==0)
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.enter_your_birth_date));
			}
			else if(mAppNetwork.isNetworkAvailable(ContactEdit.this))
			{
				new UpdateContactTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_no_network));
			}
			break;

		case R.id.contact_delete:
			// Custom Dialog for asking whether to delete the contact or not
			final Dialog dialog = new Dialog(ContactEdit.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.contact_delete_dialog);

			Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);
			Button mNo = (Button) dialog.findViewById(R.id.dialogButtonNO);

			mYes.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					dialog.cancel();
					if(mSessionIdStr.equalsIgnoreCase("nothing"))
					{
						mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_contact_not_deleted));
					}

					else if (mAppNetwork.isNetworkAvailable(ContactEdit.this)) 
					{
						new DeleteContactTask().execute();
					}
					else 
					{
						mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_no_network));
					}
				}
			});
			mNo.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					dialog.cancel();
				}
			});
			dialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id)
		{
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay);
		}
		return null;
	}

	/**
	 * AsynTask for making Server request and response for updating the contact
	 */
	private class UpdateContactTask extends AsyncTask<Void, Void, Void>
	{
	

		@Override
		protected void onPreExecute()
		{
		FlourishProgressDialog.ShowProgressDialog(ContactEdit.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{

			String add_contact_phone_number_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.add_contact_phone_number_url;

			String add_contact_email_address_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.add_contact_email_address_url;

			String add_contact_address_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.add_contact_address_url;
			String update_contact_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_contact_url;
			
			putAddContactPhoneNumberRequest();
			mAddContactPhoneNumberResponse = mConnectionManager.setUpHttpPost(add_contact_phone_number_url+"/"+mSessionIdStr+"/"+mPersonIdStr, "{\"postedPhone\" :"+mAddContactPhoneNumberRequestObj.toString()+"}");
			putAddContactEmailAddressRequest();
			mAddContactEmailAddressResponse = mConnectionManager.setUpHttpPost(add_contact_email_address_url+"/"+mSessionIdStr+"/"+mPersonIdStr, "{\"postedInternetAddress\" :"+mAddContactEmailAddressRequestObj.toString()+"}");
			putAddContactAddressRequest();
			mAddContactAddressResponse = mConnectionManager.setUpHttpPost(add_contact_address_url+"/"+mSessionIdStr+"/"+mPersonIdStr, "{\"postedAddress\" :"+mAddContactAddressRequestObj.toString()+"}");
			putUpdateContactRequest();
			mUpdateContactResponseStr = mConnectionManager.setUpHttpPost(update_contact_url+"/"+mSessionIdStr+"/"+mPersonIdStr, "{\"postedContact\" :"+mUpdateContactRequestObj.toString()+"}");
			Log.v("response", "==update contact response=="+mUpdateContactResponseStr);
			getUpdateContactResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			
				/*if(mAddContactPhoneNumberResponse.contains("Login Key Not Valid")||mAddContactEmailAddressResponse.contains("Login Key Not Valid")||mAddContactAddressResponse.contains("Login Key Not Valid")||mUpdateContactResponseStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(ContactEdit.this, LoginScreen.class));
				}
				else if(mAddContactPhoneNumberResponse.contains("Bad Parameters")||mAddContactEmailAddressResponse.contains("Bad Parameters")||mAddContactAddressResponse.contains("Bad Parameters")||mUpdateContactResponseStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(ContactEdit.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}*/
				if(mUpdateContactCallResponseStr != null)
				{
					FlourishProgressDialog.Dismiss(ContactEdit.this);
					if(mUpdateContactCallResponseStr.equalsIgnoreCase("Contact saved"))
					{
						mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_update_success));
					}
					else
					{
						mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_update_unsuccess));
					}
				}
				 
				else
				{
					
					mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_no_network));
					FlourishProgressDialog.Dismiss(ContactEdit.this);
				}
			
			super.onPostExecute(result);
		}
		
		
	}

	private void putAddContactPhoneNumberRequest()
	{
		try 
		{
			mAddContactPhoneNumberRequestObj = new JSONObject();
			mAddContactPhoneNumberRequestObj.put("PersonID", mPersonIdStr);
			mAddContactPhoneNumberRequestObj.put("AddressId", 0);
			mAddContactPhoneNumberRequestObj.put("Value", mPhoneNumberEt.getText().toString());
			mAddContactPhoneNumberRequestObj.put("DoNotCall", true);
			mAddContactPhoneNumberRequestObj.put("BestTimeToCall", "Weekdays");
			mAddContactPhoneNumberRequestObj.put("AddressTypeId", 1);
			mAddContactPhoneNumberRequestObj.put("IsPrimary", true);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	private void putAddContactEmailAddressRequest()
	{
		try 
		{
			mAddContactEmailAddressRequestObj = new JSONObject();
			mAddContactEmailAddressRequestObj.put("PersonID", mPersonIdStr);
			mAddContactEmailAddressRequestObj.put("AddressId", 0);
			mAddContactEmailAddressRequestObj.put("Value", mEmailEt.getText().toString());
			mAddContactEmailAddressRequestObj.put("AddressTypeId", 1);
			mAddContactEmailAddressRequestObj.put("IsPrimary", true);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	private void putAddContactAddressRequest()
	{
		try 
		{
			mAddContactAddressRequestObj = new JSONObject();
			mAddContactAddressRequestObj.put("PersonID", mPersonIdStr);
			mAddContactAddressRequestObj.put("AddressId", 0);
			mAddContactAddressRequestObj.put("Street1", mStreet1Et.getText().toString());
			mAddContactAddressRequestObj.put("Street2", mStreet2Et.getText().toString());
			mAddContactAddressRequestObj.put("City", mCityEt.getText().toString());
			mAddContactAddressRequestObj.put("GeographicSubDivisionCd", mStateEt.getText().toString());
			mAddContactAddressRequestObj.put("PostalCode", mPostalCodeEt.getText().toString());
			mAddContactAddressRequestObj.put("AddressTypeId", 1);
			mAddContactAddressRequestObj.put("IsPrimary", true);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}


	/**
	 * Method for making JSON Object Server request for updating the contact
	 */
	public void putUpdateContactRequest()
	{
		StringBuilder mDateTime = new StringBuilder();
		if(inPutdate != null)
		{
			mDateTime = mDateTime.append("/Date").append("(").append(inPutdate.getTime()).append(")").append("/");
		}
		else
		{
			mDateTime = mDateTime.append("/Date").append("(").append(inPutdate).append(")").append("/");
		}
		mBirthDateStr = mDateTime.toString();

		try 
		{
			mUpdateContactRequestObj = new JSONObject();
			mUpdateContactRequestObj.put("PersonID", mPersonIdStr);
			mUpdateContactRequestObj.put("FirstName", mFirstNameStr);
			mUpdateContactRequestObj.put("MiddleName", "");
			mUpdateContactRequestObj.put("LastName", mLastNameStr);
			mUpdateContactRequestObj.put("NickName", "");
			mUpdateContactRequestObj.put("BirthDate", mBirthDateStr);
			mUpdateContactRequestObj.put("RecruitDate", null);
			mUpdateContactRequestObj.put("Employer", "");
			mUpdateContactRequestObj.put("DiscountPercent", 0);
			mUpdateContactRequestObj.put("TaxRate1", 0);
			mUpdateContactRequestObj.put("TaxRate2", 0);
			mUpdateContactRequestObj.put("SpouseName", "");
			mUpdateContactRequestObj.put("SpouseAnniversary", null);
			mUpdateContactRequestObj.put("SpouseBirthday", null);
			mUpdateContactRequestObj.put("PersonTypeId", 10);
			mUpdateContactRequestObj.put("Notepad", "");
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for parsing the server response for updating the contact
	 */
	public void getUpdateContactResponse() 
	{
		try 
		{
			JSONObject json = new JSONObject(mUpdateContactResponseStr);
			mUpdateContactCallResponseStr = json.getJSONObject("AuthResponse").getString("CallResponse");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * AsynTask for making Server request and response for deleting a contact
	 */
	private class DeleteContactTask extends AsyncTask<Void, Void, Void>
	{
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(ContactEdit.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			
			String delete_contact_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.delete_contact_url;
			
			mDeleteContactResponseStr = mConnectionManager.setUpHttpDelete(delete_contact_url+"/"+mSessionIdStr+"/"+mPersonIdStr);
			getDeleteResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mUpdateContactCallResponseStr.equalsIgnoreCase("true"))
				{
					mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_delete_success));
				}
				else
				{
					mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_delete_unsuccess));
				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for parsing the Server response for deleting a contact
	 */
	public void getDeleteResponse() 
	{
		try 
		{
			JSONObject json = new JSONObject(mDeleteContactResponseStr);
			mUpdateContactCallResponseStr = json.getJSONObject("AuthResponse").getString("CallSuccess");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	{
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) 
		{
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;
			if(comparingDate().equalsIgnoreCase("valid"))
			{
				mBirthDateEt.setText(new StringBuilder().append(mMonth).append("/").append(mDay).append("/").append(mYear));
				mBirthDatePicker.init(mYear, mMonth, mDay, null);
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(ContactEdit.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
			}
		}
	};

	/**
	 * Method for comparing two dates
	 * @return String (Valid/Invalid)
	 */
	private String comparingDate()
	{
		String mValidation;
		mInputDate = new Date(mYear, mMonth, mDay+30);
		
		try {
			
			mMonth=mMonth+1;
			String inputTimeStr=mDay+"-"+mMonth+"-"+mYear;
			
			Log.v("TAG", "Month"+mMonth);
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		   inPutdate = (Date)formatter.parse(inputTimeStr); 
			System.out.println("Today is " +inPutdate.getTime());
	                Log.v("TAG", "TimeStamp"+inPutdate.getTime());
		
			
			} catch (Exception e) {
				// TODO: handle exception
			}
		
		
		
    	Long inputTime = mInputDate.getTime();
		Calendar calendar = Calendar.getInstance();
		Date validDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH)+30));
		Long validTime = validDate.getTime();
		if(validTime >= inputTime)
		{
			mValidation = "valid";
		}
		else
		{
			mValidation = "invalid";
		}
		return mValidation;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}