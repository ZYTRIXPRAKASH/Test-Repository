package com.flourish;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.flourish.utils.Constants;

public class AddContact extends BaseTimeOutActivity implements OnClickListener
{
	private static final int DATE_DIALOG_ID = 1;
	private LinearLayout mPhoneNumbersLl = null;
	private LinearLayout mEmailsLl = null;
	private RelativeLayout mPhoneNumberEtRl = null;
	private RelativeLayout mEmailEtRl = null;
	private EditText mEmailEt = null;
	private EditText mPhoneNumberEt = null;
	private EditText mFirstNameEt = null;
	private EditText mLastNameEt = null;
	private EditText mStreetEt = null;
	private EditText mCityEt = null;
	private EditText mStateEt = null;
	private EditText mPostalCodeEt = null;
	private EditText mCountryEt = null;
	private TextView mBirthDateTv = null;
	
	private Button mCalendarBtn = null;
	private Button mSaveBtn = null;
	private Button mBackBtn = null;
	private DatePicker mBirthDatePicker = null;
	private String mFirstNameStr = null;
	private String mLastNameStr = null;
	private String mBirthDateStr = null;
	public String mAddContactResponse = null;
	public String mSessionId = null;
	private String mAddContactCallResponse = null;
	public ConnectionManager mConnectionManager = null;
	private AppNetwork mAppUtils = null;
	private JSONObject mAddContactRequestObj = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Date minputDate = null;
	private LayoutInflater mLayoutInflater = null;
	Intent mIntent;
	Date inPutdate ;

	
	FlourishBaseApplication mFlourishBaseApplication;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_contact);
		onSessionStarted(true);

		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		
		initializeVariables();
		
		mIntent=getIntent();
		mFirstNameEt.setText(mIntent.getStringExtra("First_Name"));
		mLastNameEt.setText(mIntent.getStringExtra("Last_Name"));
		mEmailEt.setText(mIntent.getStringExtra("Email"));
		mPhoneNumberEt.setText(mIntent.getStringExtra("Phone_Number"));
		mCityEt.setText(mIntent.getStringExtra("City"));
		mStateEt.setText(mIntent.getStringExtra("State"));
		mPostalCodeEt.setText(mIntent.getStringExtra("Zipcode"));
		mCountryEt.setText(mIntent.getStringExtra("Country"));
		mStreetEt.setText(mIntent.getStringExtra("Street"));
		
		
		
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
	
	
	
		
         
		
		
		
		
		
		
		
		
		
		
		
		
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables() 
	{
		mConnectionManager = new ConnectionManager();
		mAppUtils = new AppNetwork();
		mEmailsLl = (LinearLayout)findViewById(R.id.add_contact_email_list);
		mPhoneNumbersLl = (LinearLayout)findViewById(R.id.add_contact_phone_list);
		mEmailEtRl = (RelativeLayout)findViewById(R.id.add_contact_email_et_rl);
		mPhoneNumberEtRl = (RelativeLayout)findViewById(R.id.add_contact_phone_et_rl);
		mLastNameEt = (EditText)findViewById(R.id.add_contact_last_name);
		mStreetEt = (EditText)findViewById(R.id.add_contact_address_street1);
		mCityEt = (EditText)findViewById(R.id.add_contact_address_city);
		mBirthDateTv = (TextView)findViewById(R.id.add_contact_birth_date_tv);
		mFirstNameEt = (EditText)findViewById(R.id.add_contact_first_name);
		mStateEt = (EditText)findViewById(R.id.add_contact_address_state);
		mPhoneNumberEt = (EditText)findViewById(R.id.add_contact_phone_et);
		mEmailEt = (EditText)findViewById(R.id.add_contact_email_et);
		mPostalCodeEt = (EditText)findViewById(R.id.add_contact_address_zipcode);
		mCountryEt = (EditText)findViewById(R.id.add_contact_address_country);
		mBackBtn = (Button)findViewById(R.id.add_contact_back);
		mSaveBtn = (Button)findViewById(R.id.add_contact_save);
		mCalendarBtn = (Button)findViewById(R.id.add_contact_birth_date_btn);
		mBirthDatePicker = (DatePicker) findViewById(R.id.add_contact_dpResult);
		mBackBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		mCalendarBtn.setOnClickListener(this);
		mBirthDateTv.setOnClickListener(this);

		if(getSharedPreferences("FROM_PHONE_CONTACTS", 0).getBoolean("FROM_PHONE", false))
		{
			setData();
			getSharedPreferences("FROM_PHONE_CONTACTS", 0).edit().putBoolean("FROM_PHONE", false).commit();
		}
	}

	/**
	 * Method for adding contacts data
	 */
	private void setData() 
	{
		String[] mSplittedStr = Constants.CONTACT_NAME.split(" ", Constants.CONTACT_NAME.length());
		Log.v("response", "==mSplittedStr=="+mSplittedStr.length);
		if(mSplittedStr.length==1)
		{
			mFirstNameEt.setText(mSplittedStr[0]);
		}
		if(mSplittedStr.length>1)
		{
			mFirstNameEt.setText(mSplittedStr[0]);
			mLastNameEt.setText(mSplittedStr[1]);
		}
		if(mSplittedStr.length>2)
		{
			mFirstNameEt.setText(mSplittedStr[0] + " " + mSplittedStr[1]);
			mLastNameEt.setText(mSplittedStr[2]);
		}
		getEmailDetails();
		getPhoneNumbersDetails();
		Log.v("TAG", "==STREET=="+Constants.STREET);
		Log.v("TAG", "==CITY=="+Constants.CITY);
		Log.v("TAG", "==STATE=="+Constants.STATE);
		Log.v("TAG", "==POSTAL_CODE=="+Constants.POSTAL_CODE);
		Log.v("TAG", "==COUNTRY=="+Constants.COUNTRY);
		mStreetEt.setText(Constants.STREET);
		mCityEt.setText(Constants.CITY);
		mStateEt.setText(Constants.STATE);
		mPostalCodeEt.setText(Constants.POSTAL_CODE);
		mCountryEt.setText(Constants.COUNTRY);
	}

	/**
	 * Method for designing phone layouts
	 */
	private void getEmailDetails()
	{
		if(Constants.EMAIl_ADDRESSES.size() == 0)
		{
			mEmailsLl.setVisibility(View.GONE);
			mEmailEtRl.setVisibility(View.VISIBLE);
		}
		else
		{
			mEmailsLl.setVisibility(View.VISIBLE);
			mEmailEtRl.setVisibility(View.GONE);
			for (int position = 0; position < Constants.EMAIl_ADDRESSES.size(); position++)
			{
				final View view;
				mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mLayoutInflater.inflate(R.layout.contacts_row, null);

				TextView text = (TextView) view.findViewById(R.id.contacts_row_text);
				View line = (View) view.findViewById(R.id.contacts_row_line);
				if(position == Constants.EMAIl_ADDRESSES.size()-1)
				{
					line.setVisibility(View.GONE);
				}
				if(Constants.EMAIl_ADDRESSES.get(position).trim().length() > 0)
				{
					if((!(Constants.EMAIl_ADDRESSES.get(position).equalsIgnoreCase("(null)"))))
					{
						text.setText(Constants.EMAIl_ADDRESSES.get(position));
					}
					mEmailsLl.addView(view);
				}
			}
		}
	}

	/**
	 * Method for designing phone layouts
	 */
	private void getPhoneNumbersDetails()
	{
		if(Constants.PHONE_NUMBERS.size() == 0)
		{
			mPhoneNumbersLl.setVisibility(View.GONE);
			mPhoneNumberEtRl.setVisibility(View.VISIBLE);
		}
		else
		{
			mPhoneNumbersLl.setVisibility(View.VISIBLE);
			mPhoneNumberEtRl.setVisibility(View.GONE);
			for (int position = 0; position < Constants.PHONE_NUMBERS.size(); position++)
			{
				final View view;
				mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mLayoutInflater.inflate(R.layout.contacts_row, null);

				TextView text = (TextView) view.findViewById(R.id.contacts_row_text);
				View line = (View) view.findViewById(R.id.contacts_row_line);
				if(position == Constants.PHONE_NUMBERS.size()-1)
				{
					line.setVisibility(View.GONE);
				}
				if(Constants.PHONE_NUMBERS.get(position).trim().length() > 0)
				{
					if((!(Constants.PHONE_NUMBERS.get(position).equalsIgnoreCase("(null)"))))
					{
						text.setText(Constants.PHONE_NUMBERS.get(position));
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
		case R.id.add_contact_back:
			startActivity(new Intent(AddContact.this, FlourishHomeActivity.class));
			finish();
			break;
		case R.id.add_contact_birth_date_btn:
			showDialog(DATE_DIALOG_ID);
			break;
			
		case R.id.add_contact_birth_date_tv:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.add_contact_save:
		/*	
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mBirthDateTv.getWindowToken(), 0);*/
		
			mFirstNameStr = mFirstNameEt.getText().toString().trim();
			mLastNameStr = mLastNameEt.getText().toString().trim();
			if(mSessionId.equalsIgnoreCase("nothing"))
			{
				Toast.makeText(AddContact.this, "Unable to update the person details", Toast.LENGTH_LONG).show();
			}
			else if(mFirstNameStr.trim().length()==0)
			{
				mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.enter_your_first_name));
			}
			else if(mLastNameStr.trim().length()==0)
			{
				mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.enter_your_last_name));
			}
			else if(mBirthDateTv.getText().toString().length()==0)
			{
				mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.enter_your_birth_date));
			}
			else if(mAppUtils.isNetworkAvailable(AddContact.this))
			{
				new AddContactTask().execute();
			}
			else 
			{
				mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.alert_dialog_no_network));
			}
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
				mBirthDateTv.setText(new StringBuilder().append(mMonth).append("/").append(mDay).append("/").append(mYear));

				
				
				
				
				
				
				mBirthDatePicker.init(mYear, mMonth, mDay, null);
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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
		minputDate = new Date(mMonth, mDay,mYear);
	
		
		Long inputTime = minputDate.getTime();
	
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

	/**
	 * AsyncTask for making Server request and response for adding a contact.
	 */
	private class AddContactTask extends AsyncTask<Void, Void, Void>
	{
		

		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(AddContact.this);
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl_create_contact_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.create_contact_url;
			
			putAddContactRequest();
			
			mAddContactResponse = mConnectionManager.setUpHttpPut(mTaskUrl_create_contact_url+mSessionId, "{\"putContact\" :"+mAddContactRequestObj.toString()+"}");
			getAddContactResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			
				if(mAddContactResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(AddContact.this, LoginScreen.class));
				}
				else if(mAddContactResponse.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(AddContact.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else if(mAddContactCallResponse != null)
				{
					FlourishProgressDialog.Dismiss(AddContact.this);
					if(mAddContactCallResponse.equalsIgnoreCase("Contact saved"))
					{
						mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.alert_dialog_saved_success));
					}
					else
					{
						mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.alert_dialog_saved_unsuccess));
					}

				}
				else
				{
					FlourishProgressDialog.Dismiss(AddContact.this);
					mAppUtils.getAlertDialog(AddContact.this, getResources().getString(R.string.alert_dialog_no_network));
				}

		
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for making JSON Object for adding contact request.
	 */
	private void putAddContactRequest()
	{
		StringBuilder mDateTime = new StringBuilder();
	
	
		Log.v("TAG", "InputDate="+minputDate);
		mDateTime = mDateTime.append("/Date").append("(").append(inPutdate.getTime()).append(")").append("/");
		mBirthDateStr = mDateTime.toString();
		
		Log.v("TAG", "mBirthDateStr"+mBirthDateStr);
		try 
	{
				
			mAddContactRequestObj = new JSONObject();
			mAddContactRequestObj.put("PersonID", 0);
			mAddContactRequestObj.put("FirstName", mFirstNameStr);
			mAddContactRequestObj.put("MiddleName", "");
			mAddContactRequestObj.put("LastName", mLastNameStr);
			mAddContactRequestObj.put("NickName", "");
			mAddContactRequestObj.put("BirthDate", mDateTime);
			mAddContactRequestObj.put("RecruitDate", null);
			mAddContactRequestObj.put("Employer", "");
			mAddContactRequestObj.put("DiscountPercent", 0);
			mAddContactRequestObj.put("TaxRate1", 0);
			mAddContactRequestObj.put("TaxRate2", 0);
			mAddContactRequestObj.put("SpouseName", "");
			mAddContactRequestObj.put("SpouseAnniversary", null);
			mAddContactRequestObj.put("SpouseBirthday", null);
			mAddContactRequestObj.put("PersonTypeId", 10);
			mAddContactRequestObj.put("Notepad", "");
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for parsing the Server response for adding contact.
	 */
	public void getAddContactResponse() 
	{
		try 
		{
			JSONObject json = new JSONObject(mAddContactResponse);
			mAddContactCallResponse = json.getJSONObject("AuthResponse").getString("CallResponse");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK )
		{
			startActivity(new Intent(AddContact.this, FlourishHomeActivity.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}