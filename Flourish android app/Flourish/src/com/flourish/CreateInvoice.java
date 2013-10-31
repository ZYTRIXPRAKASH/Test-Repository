package com.flourish;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.adapters.MoneyListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class CreateInvoice extends BaseTimeOutActivity implements OnClickListener
{
	private static final int DATE_DIALOG_ID = 1;
	private static final int SELECT_CONTACT_ID=100;
	private static final int SELECT_BOOKINGG_ID=200;
	private EditText mCustomerEt = null;
	private EditText mDateEt = null;
	private TextView mInvoiceTypeTv = null;
	private Button mCalendarBtn = null;
	private Button mBackBtn = null;
	private Button mSaveBtn = null;
	private Button mCancelBtn = null;
	private DatePicker mInvoiceDatePicker = null;

	private String mCreateInvoiceResultStr = null;
	private String mSessionIdStr = null;
	private String mPersonIdStr = null;
	private String mCallResponseStr = null;
	private String mCustomerStr = null;
	private String mDateStr = null;
	private String mInvoiceTypeStr = null;
	private String mFromWhichScreen = null;
	String mBookingCalenderId=null;
	private ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private JSONObject mCreateInvoiceJsonObj = null;
	private VehicleListCustomDialog mCustomDialog = null;
	private ArrayList<String> mInvoiceTypeList = new ArrayList<String>();
	private MoneyListAdapter mInvoiceTypeListAdapter = null;
	private RelativeLayout mInvoiceType_rl = null;
	private RelativeLayout mAdd_invoice_date_rll=null;
	private EditText mCreate_invoice_date_et=null;
	private RelativeLayout mInvoiceBooking_rl = null;
	public static final String[] MONTHS = {"","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	String mPersonName=null;
	
	private String mInvoiceId = null;
	private String mPersonId = null;
	private String mInvoiceTypeId = null;
	private String mFirstName = null;
	private String mLastName = null;
	private String mInvoiceDate = null;
	private String mInvoiceNumber = null;
	private String mInvoiceTypeName = null;
	private String mInvoiceDiscount = null;
	private String mTaxAfterDiscount = null;
	private String mTaxShipping = null;
	private String mIsDelivered = null;
	private String mShipping = null;
	private String mTaxRate = null;
	private String mTaxableTotal = null;
	private String mTaxAmount = null;
	private String mTotalAmount = null;
	private String mInvoiceStatusId=null;
	
	private String mOutstanding=null;
	
	private String mDiscount=null;
	
	
	FlourishBaseApplication mFlourishBaseApplication;
	
	SharedPreferences mSharedPreferences;
	Intent mIntent;
	EditText add_invoice_booking_et;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_invoice);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		onSessionStarted(true);
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
		mIntent=getIntent();
	    mPersonIdStr=mIntent.getStringExtra("personId");
	    
		
	  
		/*putExtra("personId", mPersonIdStr)
		.putExtra("personName", mPersonName)*/

		initializeVariables();
		
		  mCustomerEt.setText(mIntent.getStringExtra("personName"));
		
		mFromWhichScreen = getSharedPreferences("WhichScreen", 0).getString("ScreenName", "nothing");
		mSessionIdStr = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		presentDate();
		
		
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables() 
	{
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mCustomDialog 	= new VehicleListCustomDialog(CreateInvoice.this);
		mCustomerEt = (EditText)findViewById(R.id.create_invoice_customer_et);
		mDateEt = (EditText)findViewById(R.id.create_invoice_date_et);
		mInvoiceTypeTv = (TextView)findViewById(R.id.create_invoice_type_et);
		mInvoiceDatePicker = (DatePicker) findViewById(R.id.dpResult);
		mCalendarBtn = (Button)findViewById(R.id.create_invoice_calendar_btn);
		mBackBtn = (Button)findViewById(R.id.create_invoice_back);
		mSaveBtn = (Button)findViewById(R.id.create_invoice_save);
		mCancelBtn = (Button)findViewById(R.id.create_invoice_cancel_btn);
		add_invoice_booking_et=(EditText)findViewById(R.id.add_invoice_booking_et);
		
		mInvoiceType_rl  = (RelativeLayout)findViewById(R.id.add_invoice_type_rl);
		mInvoiceBooking_rl  = (RelativeLayout)findViewById(R.id.add_invoice_booking_rl);
		mAdd_invoice_date_rll=(RelativeLayout)findViewById(R.id.add_invoice_date_rll);
		
		mCalendarBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mDateEt.setOnClickListener(this);
		mInvoiceTypeTv.setOnClickListener(this);
		mCustomerEt.setOnClickListener(this);
		add_invoice_booking_et.setOnClickListener(this);
		
		mInvoiceTypeList.add("Sale");
		mInvoiceTypeList.add("Booking");
		mInvoiceTypeList.add("Return");
		
		mCustomDialog.getHeaderTv().setText("Select an Account");
		mInvoiceTypeListAdapter = new MoneyListAdapter(CreateInvoice.this, R.layout.sales_list_row, mInvoiceTypeList);
		mCustomDialog.getListview().setAdapter(mInvoiceTypeListAdapter);
		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mInvoiceTypeStr = mInvoiceTypeList.get(position);
				
				mInvoiceTypeTv.setText(mInvoiceTypeList.get(position));
				if(mInvoiceTypeStr.equalsIgnoreCase("Booking")){
					mInvoiceType_rl.setBackgroundResource(R.drawable.login2);
					mInvoiceBooking_rl.setVisibility(View.VISIBLE);
				}else{
					mInvoiceType_rl.setBackgroundResource(R.drawable.login3);
					mInvoiceBooking_rl.setVisibility(View.GONE);
				}
				mCustomDialog.dismiss();
			}
		});
	}

	/**
	 * AsynTask for making Server request and response for creating invoice.
	 */
	private class CreateInvoiceTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() 
		{
			FlourishProgressDialog.ShowProgressDialog(CreateInvoice.this);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.create_invoice_url;
			putCreateInvoiceRequest();
			mCreateInvoiceResultStr = mConnectionManager.setUpHttpPut(mTaskUrl+mSessionIdStr, mCreateInvoiceJsonObj.toString());
			getCreateInvoiceResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result11)
		{
			
			FlourishProgressDialog.Dismiss(CreateInvoice.this);
				
				if(mCreateInvoiceResultStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(CreateInvoice.this, LoginScreen.class));
				}
				else if(mCreateInvoiceResultStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(CreateInvoice.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else if(mCallResponseStr != null)
				{
					FlourishProgressDialog.Dismiss(CreateInvoice.this);
					if(mCallResponseStr.equalsIgnoreCase("true"))
					{
						
						startActivity(new Intent(CreateInvoice.this, SalesInvoiceEditDetails.class)
						
						.putExtra("personName", mPersonName)
						.putExtra("invoiceId", ""+mInvoiceId)
						.putExtra("personId", ""+mPersonId)

						.putExtra("invoiceTypeId", ""+mInvoiceTypeId)
						.putExtra("firstName", ""+mFirstName)
						.putExtra("lastName",""+mLastName)
						.putExtra("invoiceDate", ""+mInvoiceDate)
						.putExtra("invoiceNumber", ""+mInvoiceNumber)
						.putExtra("invoiceTypeName", ""+mInvoiceTypeName)
						.putExtra("invoiceDiscount", ""+mInvoiceDiscount)
						.putExtra("taxAfterDiscount", ""+mTaxAfterDiscount)
						.putExtra("taxShipping", ""+mTaxShipping)
						.putExtra("markItemsDelivered", ""+mTaxAfterDiscount)
						.putExtra("shipping", ""+mShipping)
						.putExtra("invoiceTaxRate", ""+mTaxRate)
						.putExtra("invoiceTaxableTotal", ""+mTaxableTotal)
						.putExtra("invoiceTaxAmount", ""+mTaxAmount)
						.putExtra("total", ""+mTotalAmount)
						.putExtra("invoiceStatus",""+mInvoiceStatusId));
						
						
					
						
						Log.v("TAG", "invoiceStatus in Create Invoice ="+mInvoiceStatusId);
						Log.v("TAG", "invoiceDiscount in Create Invoice ="+mInvoiceDiscount);
						
						finish();
						
					
					/*	startActivity(new Intent(CreateInvoice.this, SalesInvoiceEditDetails.class)
						.putExtra("personId", mPersonIdStr)
						.putExtra("InvoiceNumber", mInvoiceNumber)
						.putExtra("invoiceTypeId", InvoiceTypeId)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						
						
						finish();*/
						
					
						
						
					
						/*final Dialog dialog = new Dialog(CreateInvoice.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.create_invoice_success_dialog);

						Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);

						mYes.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								dialog.cancel();
								if(mFromWhichScreen.equalsIgnoreCase("ContactsDetails"))
								{
									finish();
								}
								else
								{
									getSharedPreferences("WhichScreen", 0).edit().putString("ScreenName", "CreateInvoice").commit();
									startActivity(new Intent(CreateInvoice.this, FlourishHomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
									finish();
								}
							}
						});
						dialog.show();*/
					
					
					}
					else
					{
						mAppNetwork.getAlertDialog(CreateInvoice.this, getResources().getString(R.string.alert_dialog_create_invoice_unsuccess));
					}
					
					
					
				}
				else
				{
					FlourishProgressDialog.Dismiss(CreateInvoice.this);
					mAppNetwork.getAlertDialog(CreateInvoice.this, getResources().getString(R.string.alert_dialog_invoice_create_failed));
				}
			}
		}
	

	/**
	 * Method for making JSON Object Server request for creating invoice.
	 */
	public void putCreateInvoiceRequest() 
	{
		try 
		{
			mCreateInvoiceJsonObj = new JSONObject();
			
			mCreateInvoiceJsonObj.put("PersonId", mPersonIdStr);
			
			if(mInvoiceTypeStr.equalsIgnoreCase("Sale"))
			{
				mCreateInvoiceJsonObj.put("InvoiceTypeId", 7);
			
			}
			
			
			
			
			else if(mInvoiceTypeStr.equalsIgnoreCase("Booking"))
			{
				mCreateInvoiceJsonObj.put("InvoiceTypeId", 16);
			
			}
			else if(mInvoiceTypeStr.equalsIgnoreCase("Return"))
			{
				mCreateInvoiceJsonObj.put("InvoiceTypeId", 17);
				
			}
			mCreateInvoiceJsonObj.put("InvoiceDate", mDateStr);
			mCreateInvoiceJsonObj.put("BookingCalendarId", "0");
			//mCreateInvoiceJsonObj.put("BookingCalendarId", mBookingCalenderId);
			
			Log.v("TAG", "ofter json object  ");
			
		
			
			
			
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			
			Log.v("TAG", "exception in create invoice json object "+e.toString());
			
		}
	}

	/**
	 * Method for parsing Server response for creating invoice
	 */
	public void getCreateInvoiceResponse() 
	{
		try 
		{
			JSONObject mCompleJsonObject = new JSONObject(mCreateInvoiceResultStr);
			mCallResponseStr = mCompleJsonObject.getJSONObject("AuthResponse").getString("CallSuccess");
			
			if (mCreateInvoiceResultStr!=null) {
				
				JSONObject dataJsonObject=mCompleJsonObject.getJSONObject("data");
				
				
				mInvoiceTypeId=dataJsonObject.getString("InvoiceTypeId");
				mInvoiceId=dataJsonObject.getString("Id");
               mPersonId=dataJsonObject.getString("PersonId");
               mInvoiceNumber=dataJsonObject.getString("InvoiceNumber");
               mInvoiceDate=dataJsonObject.getString("InvoiceDate");
             //  mInvoiceTypeName=dataJsonObject.getString("InvoiceTypeName");
               mInvoiceDiscount=dataJsonObject.getString("Discount");
              mTaxAfterDiscount=dataJsonObject.getString("TaxAfterDiscount");
               mTaxShipping=dataJsonObject.getString("TaxShipping");
                
               mIsDelivered=dataJsonObject.getString("IsDelivered");
              // mOutstanding=dataJsonObject.getString("Outstanding");
               mShipping=dataJsonObject.getString("Shipping");
               mTaxRate=dataJsonObject.getString("TaxRate1");
               mTaxableTotal=dataJsonObject.getString("TaxableTotal1");
               mTaxAmount=dataJsonObject.getString("TaxAmount1");    
               mTotalAmount=dataJsonObject.getString("Total");
               mInvoiceStatusId=dataJsonObject.getString("InvoiceStatusId");
                
               Log.v("TAG", "InvoiceTypeId "+mInvoiceTypeId);
			
				
				
				Log.v("TAG", "mInvoiceStatusId "+mInvoiceStatusId);
				
				
			}
			
		
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
			
			
			Log.v("TAG", "exception in create invoice "+e.toString());
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.create_payment_back:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mInvoiceTypeTv.getWindowToken(), 0);
			if(mFromWhichScreen.equalsIgnoreCase("ContactsDetails"))
			{
				finish();
			}
			else
			{
				
				startActivity(new Intent(CreateInvoice.this, FlourishHomeActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			}
			break;
			
		case R.id.create_invoice_customer_et:

			SharedPreferences.Editor editor=mSharedPreferences.edit();
			  editor.putString("FragmentScreen", "Contacts");
			  editor.commit();

			 Intent mIntentForContacts = new Intent(CreateInvoice.this, FlourishHomeActivity.class);
			
			 
			 mFlourishBaseApplication.setmSelectedUserTag(1);//which is use to differentiate  to display user data from contacts  ,which is used in invoice
			
			 mFlourishBaseApplication.mPersonNameFromContact=2;//this is use to display the user name in save invoice edit activity 
			
			 startActivityForResult(mIntentForContacts, SELECT_CONTACT_ID);
			    Log.d("result", "intent back");
				break;

		case R.id.create_invoice_type_et:
			mCustomDialog.show();
			break;
		case R.id.create_invoice_calendar_btn:
			showDialog(DATE_DIALOG_ID);
			break;

		case R.id.create_invoice_date_et:
			showDialog(DATE_DIALOG_ID);
			break;
			
			
		case R.id.create_invoice_save:
			InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm1.hideSoftInputFromWindow(mDateEt.getWindowToken(), 0);
			mCustomerStr = mCustomerEt.getText().toString();

			if(mSessionIdStr.equalsIgnoreCase("nothing"))
			{
				Toast.makeText(CreateInvoice.this, "Unable to update the person details", Toast.LENGTH_LONG).show();
			}
			else if(mCustomerStr.trim().length()==0)
			{
				mAppNetwork.getAlertDialog(CreateInvoice.this, "Enter your information");
			}
			else if(mDateStr.trim().length()==0)
			{
				mAppNetwork.getAlertDialog(CreateInvoice.this, "Enter your information");
			}
			else if(mInvoiceTypeStr.trim().length()==0)
			{
				mAppNetwork.getAlertDialog(CreateInvoice.this, "Enter your information");
			}
			else if(mAppNetwork.isNetworkAvailable(CreateInvoice.this))
			{
				new CreateInvoiceTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(CreateInvoice.this, getString(R.string.alert_dialog_no_network));
			}
			break;
			
			
		case R.id.create_invoice_back:
			
			startActivity(new Intent(CreateInvoice.this, FlourishHomeActivity.class));
			
			 SharedPreferences.Editor backeditor=mSharedPreferences.edit();
			 backeditor.putString("FragmentScreen", "Dashboard");
			 backeditor.commit();
			
			
			
			finish();
			
			break;
			
		case R.id.create_invoice_cancel_btn:

			SharedPreferences.Editor editor1=mSharedPreferences.edit();
			  editor1.putString("FragmentScreen", "Dashboard");
			  editor1.commit();

			 Intent mIntentForDashboard = new Intent(CreateInvoice.this, FlourishHomeActivity.class);
			startActivity(mIntentForDashboard);
			
			  
					

		        Log.d("result", "intent back");
			
			
			
		
			
			break;
			
			
		case R.id.add_invoice_booking_et:
			
			Intent mIntent=new Intent(CreateInvoice.this, BookingsActivity.class);
			//startActivity(mIntent);
			startActivityForResult(mIntent, SELECT_BOOKINGG_ID);
			
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
				mDateStr = (new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear)).toString();
				mDateEt.setText(new StringBuilder().append(MONTHS[mMonth+1]).append(" ").append(mDay).append(", ").append(mYear));

				mInvoiceDatePicker.init(mYear, mMonth, mDay, null);
				
				
				
				
				
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(CreateInvoice.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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
		Date inputDate = new Date(mYear, mMonth, mDay+30);
		Long inputTime = inputDate.getTime();
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
			if(mFromWhichScreen.equalsIgnoreCase("ContactsDetails"))
			{
				finish();
			}
			else
			{
				getSharedPreferences("WhichScreen", 0).edit().putString("ScreenName", "CreateInvoice").commit();
				startActivity(new Intent(CreateInvoice.this, FlourishHomeActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.d("CheckStartActivity","onActivityResult and resultCode = "+resultCode);
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case SELECT_CONTACT_ID:
				  
				
				if(data!=null)
				{
				String datafromContacts=data.getAction();
				
				String[] str_array = datafromContacts.split(",");
				mPersonIdStr = str_array[0]; 
			   mPersonName = str_array[1];
				
				
				mCustomerEt.setText(mPersonName);
				
			   // Log.v("TAG", "Person Id  in Create invoice "+mPersonIdStr);
				
				
				
				}
		
				else
				{
				//Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
				} 
				
			
				break;
				
				
           case SELECT_BOOKINGG_ID:
				
				
				if(data!=null)
				{
					
				String mBookings=data.getAction();
				
				String[] str_array = mBookings.split(",");
				mBookingCalenderId = str_array[0]; 
			    String mBookingSubject = str_array[1];
			    Log.v("TAG", "mBookings  in Create invoice "+mBookings);
			    add_invoice_booking_et.setText(mBookingSubject);
			    
				}
		
				else
				{
				//Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
				} 
			
			
				break;
				
				

			default:
				break;
			}
			
			
			/*if(resultCode==RESULT_OK)
			{
				
		Toast.makeText(this, "Pass", Toast.LENGTH_LONG).show();
			if(data!=null)
			{
			String datafromContacts=data.getAction();
			
			String[] str_array = datafromContacts.split(",");
			mPersonIdStr = str_array[0]; 
		   mPersonName = str_array[1];
			
			
			mCustomerEt.setText(mPersonName);
			
		    Log.v("TAG", "Person Id  in Create invoice "+mPersonIdStr);
			
			
			
			}
			
			}
			
			else
			{
			Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
			} 
		

	*/
	
	}
	
	
	public void presentDate() {

		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("MMM dd-yyyy");
		String formattedDate = df.format(c.getTime());
		mDateEt.setText(formattedDate);
		mDateStr=formattedDate;

	}
	
	
	
	
}