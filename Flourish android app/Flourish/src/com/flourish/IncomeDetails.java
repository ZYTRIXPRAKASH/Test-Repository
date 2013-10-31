package com.flourish;

import java.sql.Date;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class IncomeDetails extends Activity implements OnClickListener
{
	private TextView mIncomeAccountNameEt = null;
	private EditText mIncomeTransactionDateEt = null;
	public Button mIncomeDeletebtn=null;
	private Button mIncomeDateBtn = null;
	private Button mBack = null;
	private Button mSave = null;
	private EditText mIncomeDescriptionEt = null;
	private EditText mIncomeReferenceEt = null;
	private EditText mIncomeAmountEt = null;
	private DatePicker mIncomeDatePicker = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String mSessionId = null;
	private String mAccountIdStr = null;
	private String mReferenceStr = null;
	public String mIncomeUpdateStr = null;
	private String mIncomeDeleteResponse=null;
//	private String mIncomeDeleteStr=null;
	private String mJournalId=null;
	private String mIncomeDateStr = null;
	private JSONObject mUpdateIncomeRequestObj = null;
	private ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private VehicleListCustomDialog mCustomDialog = null;
	private static final int DATE_DIALOG_ID = 1;
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;
	
	String mIncomeUpdatedDateSendStr=null;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.income_details);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");

		initializeVariables();
		setIncomeData();

		mIncomeDateBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				showDialog(DATE_DIALOG_ID);
			}
		});
	}
	/**
	 * Method for initializing variables
	 */
	private void initializeVariables()
	{
		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();
		mCustomDialog 	= new VehicleListCustomDialog(IncomeDetails.this);
		mIncomeAccountNameEt = (TextView)findViewById(R.id.income_details_account_name_et);
		mIncomeTransactionDateEt = (EditText)findViewById(R.id.income_details_transaction_date_et);
		mIncomeDescriptionEt = (EditText)findViewById(R.id.income_details_description_et);
		mIncomeReferenceEt = (EditText)findViewById(R.id.income_details_type_et);
		mIncomeAmountEt = (EditText)findViewById(R.id.income_details_amount_et);
		mIncomeDatePicker = (DatePicker) findViewById(R.id.payment_dpResult);
		mBack = (Button)findViewById(R.id.income_details_back);
		mIncomeDateBtn = (Button)findViewById(R.id.income_details_calendar_btn);
		mIncomeDeletebtn=(Button)findViewById(R.id.income_details_delete_btn);
		mSave = (Button)findViewById(R.id.payment_save);
		mIncomeDeletebtn.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mIncomeTransactionDateEt.setOnClickListener(this);
		mIncomeAccountNameEt.setOnClickListener(this);

		mCustomDialog.getHeaderTv().setText("Select an Account");
		ArrayAdapter<CharSequence> mAddVehicleAdapter = ArrayAdapter.createFromResource(this, R.array.income_account_name_array, android.R.layout.simple_spinner_item);
		mAddVehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCustomDialog.getListview().setAdapter(mAddVehicleAdapter);

		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mIncomeAccountNameEt.setText(mCustomDialog.getListview().getItemAtPosition(position).toString());
				mCustomDialog.dismiss();
			}
		});
	}

	private void setIncomeData() 
	{
		mJournalId=getIntent().getStringExtra("JournalId");
		
		//mIncomeDeleteStr = getIntent().getStringExtra("JournalId");
		mAccountIdStr = getIntent().getStringExtra("Account_Id");
		mReferenceStr = getIntent().getStringExtra("Reference");
		mIncomeAccountNameEt.setText(getIntent().getStringExtra("Account_Name"));
		mIncomeDescriptionEt.setText(getIntent().getStringExtra("Description"));
		mIncomeReferenceEt.setText(getIntent().getStringExtra("Reference"));
		mIncomeAmountEt.setText(getIntent().getStringExtra("Amount").replaceAll("-", ""));
		mIncomeDateStr = getIntent().getStringExtra("Transaction_Date");
		String[] mIncomeDateArray = mIncomeDateStr.split("-");
	
		String ExpMonth=mIncomeDateArray[1];
		
		String ExpYear=mIncomeDateArray[0];
		String ExpDayArray=mIncomeDateArray[2];
		
		String ExpDay[]=ExpDayArray.split("T");
		String day=ExpDay[0];
		
		mIncomeUpdatedDateSendStr=ExpMonth+"-"+day+"-"+ExpYear;
		
		
		
		int mMonth = 0;
		if(mIncomeDateArray[1].startsWith("0"))
		{
			mMonth = Integer.parseInt(mIncomeDateArray[1].substring(1));
		}
		else
		{
			mMonth = Integer.parseInt(mIncomeDateArray[1]);
		}
		String mIncomeUpdatedDateStr = MONTHS[mMonth-1]+" "+mIncomeDateArray[2].substring(0, 2)+", "+mIncomeDateArray[0];
		mIncomeTransactionDateEt.setText(mIncomeUpdatedDateStr);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.income_details_back:
			getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Income").commit();
			finish();
			break;
		case R.id.payment_save:
			if(mIncomeAccountNameEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeTransactionDateEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeDescriptionEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeReferenceEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeAmountEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mAppNetwork.isNetworkAvailable(IncomeDetails.this))
			{
				new GetUpdateIncomeTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_no_network));
			}
			break;
		case R.id.income_details_calendar_btn:
			showDialog(DATE_DIALOG_ID);
			break;
			
			
			
			
		case R.id.income_details_transaction_date_et:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.income_details_account_name_et:
			mCustomDialog.show();
			break;
		case R.id.income_details_delete_btn:
			// Custom Dialog for asking whether to delete the income or not
			final Dialog dialog = new Dialog(IncomeDetails.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.income_delete_dialog);

			Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);
			Button mNo = (Button) dialog.findViewById(R.id.dialogButtonNO);

			mYes.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					dialog.cancel();
					if (mAppNetwork.isNetworkAvailable(IncomeDetails.this)) 
					{
						new GetDeleteIncomeTask().execute();
					}
					else 
					{
						mAppNetwork.getAlertDialog(IncomeDetails.this, getResources().getString(R.string.alert_dialog_no_network));
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

	/**
	 * Method for edit income details AsyncTask
	 */
	private class GetUpdateIncomeTask extends AsyncTask<Void, Void, Void>
	{
		

		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(IncomeDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{

            String mTaskUrl_update_income=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_income;
			putMileageData();
			mIncomeUpdateStr = mConnectionManager.setUpHttpPost(mTaskUrl_update_income+"/"+mSessionId, mUpdateIncomeRequestObj.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{	FlourishProgressDialog.Dismiss(IncomeDetails.this);
			{
				
				if(mIncomeUpdateStr.contains("Account not found"))
				{
					mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_account_not_found));
				}
				else if(mIncomeUpdateStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(IncomeDetails.this, LoginScreen.class));
				}
				else if(mIncomeUpdateStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					final Dialog dialog = new Dialog(IncomeDetails.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.income_success_dialog);

					Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);

					mYes.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							dialog.cancel();
							getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Expenses").commit();
							SharedPreferences.Editor editor=mSharedPreferences.edit();
							  editor.putString("FragmentScreen", "Money");
							  editor.commit();

							/* Intent mIntentForMoney = new Intent(IncomeDetails.this, FlourishHomeActivity.class);
							 startActivity(mIntentForMoney);*/
							  
							  setResult(1);
							finish();
						}
					});
					dialog.show();

				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for deleting the income details AsyncTask
	 */
	public class GetDeleteIncomeTask extends AsyncTask<Void, Void, Void>
	{
		

		@Override
		protected void onPreExecute()
		{
		FlourishProgressDialog.ShowProgressDialog(IncomeDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{

          String mTaskUrl_delete_expenses=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.delete_expenses;
			mIncomeDeleteResponse = mConnectionManager.setUpHttpDelete(mTaskUrl_delete_expenses+mSessionId+"/"+mJournalId);
			Log.v("response","IncomeDelete"+mIncomeDeleteResponse);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(IncomeDetails.this);
				if(mIncomeDeleteResponse.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(IncomeDetails.this, getString(R.string.alert_dialog_income_not_deleted));
				}
				else if(mIncomeDeleteResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(IncomeDetails.this, LoginScreen.class));
				}
				else
				{
					final Dialog dialog = new Dialog(IncomeDetails.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.income_delete_success_dialog);

					Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);

					mYes.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							dialog.cancel();
							SharedPreferences.Editor editor=mSharedPreferences.edit();
							  editor.putString("FragmentScreen", "Money");
							  editor.commit();

						//	 Intent mIntentForMoney = new Intent(IncomeDetails.this, FlourishHomeActivity.class);
							// startActivity(mIntentForMoney);
							  
							  setResult(1);
							 finish();
						}
					});
					dialog.show();

				
				}
		
			super.onPostExecute(result);
		}
	}

	private void putMileageData() 
	{
		try 
		{
			
		
			
			
			
			mUpdateIncomeRequestObj = new JSONObject();
			mUpdateIncomeRequestObj.put("JournalId", mJournalId);
			mUpdateIncomeRequestObj.put("AccountId", mAccountIdStr);
			mUpdateIncomeRequestObj.put("TransactionDate", mIncomeUpdatedDateSendStr);
			mUpdateIncomeRequestObj.put("Description", mIncomeDescriptionEt.getText().toString());
			mUpdateIncomeRequestObj.put("Reference", mIncomeReferenceEt.getText().toString());
			mUpdateIncomeRequestObj.put("Amount", mIncomeAmountEt.getText().toString());
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
				mIncomeDateStr = (new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear)).toString();
				mIncomeTransactionDateEt.setText(new StringBuilder().append(MONTHS[mMonth]).append(" ").append(mDay).append(", ").append(mYear));
				mIncomeDatePicker.init(mYear, mMonth, mDay, null);
				
				mIncomeUpdatedDateSendStr=mMonth+1+"-"+mDay+"-"+mYear;
				
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(IncomeDetails.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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

	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Income").commit();
	}
}