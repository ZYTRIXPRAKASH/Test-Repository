package com.flourish;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.flourish.adapters.MoneyListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class AddIncome extends Activity implements OnClickListener
{
	private ArrayList<String> mIncomeList = new ArrayList<String>();
	private ArrayList<String> mIncomeAccountIdList = new ArrayList<String>();
	private MoneyListAdapter mIncomeListAdapter = null;
	private TextView mIncomeAccountNameTv = null;
	private EditText mIncomeTransactionDateEt = null;
	private EditText mIncomeDescriptionEt = null;
	private EditText mIncomeAmountEt = null;
	private EditText mIncomeReferenceEt=null;
	private Button mBack = null;
	private Button mSave = null;
	private Button mIncomeDateBtn = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private AppNetwork mAppNetwork = null;
	private ConnectionManager mConnectionManager = null;
	private VehicleListCustomDialog mCustomDialog = null;
	private String mSessionId = null;
	private String mIncomeAccountIdStr = null;
	public String mIncomeUpdateStr = null;
	private String mIncomeAccountIdResponseStr = null;
	private DatePicker mIncomeDatePicker = null;
	private JSONObject mUpdateMileageRequestObj = null;
	private static final int DATE_DIALOG_ID = 1;
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_income);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");

		initializeVariables();
		new GetIncomeAccountIdTask().execute();

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
	 * Method for initialize variables
	 */

	private void initializeVariables()
	{
		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();
		mCustomDialog 	= new VehicleListCustomDialog(AddIncome.this);
		mIncomeAccountNameTv = (TextView)findViewById(R.id.add_payment_type_tv);
		mIncomeTransactionDateEt = (EditText)findViewById(R.id.add_sales_payment_date_et);
		mIncomeDescriptionEt = (EditText)findViewById(R.id.add_payment_memo_et);
		mIncomeAmountEt = (EditText)findViewById(R.id.add_payment_amount_et);
		mIncomeReferenceEt=(EditText)findViewById(R.id.add_income_reference_et);
		mBack = (Button)findViewById(R.id.add_income_back);
		mSave = (Button)findViewById(R.id.payment_save);
		mIncomeDateBtn = (Button)findViewById(R.id.payment_date_calander_btn);
		mIncomeDatePicker = (DatePicker) findViewById(R.id.payment_dpResult);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mIncomeTransactionDateEt.setOnClickListener(this);
		
		mIncomeAccountNameTv.setOnClickListener(this);

		mIncomeList.add("Comissions");
		mIncomeList.add("Misc");
		mCustomDialog.getHeaderTv().setText("Select an Account");
		mIncomeListAdapter = new MoneyListAdapter(AddIncome.this, R.layout.sales_list_row, mIncomeList);
		mCustomDialog.getListview().setAdapter(mIncomeListAdapter);
		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mIncomeAccountIdStr = mIncomeAccountIdList.get(position);
				mIncomeAccountNameTv.setText(mIncomeList.get(position));
				mCustomDialog.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.add_income_back:
			getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Income").commit();
			finish();
			break;

		case R.id.payment_save:
			if(mIncomeAccountNameTv.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeTransactionDateEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeDescriptionEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeReferenceEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mIncomeAmountEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_enter_income_details_to_be_added));
			}
			else if(mAppNetwork.isNetworkAvailable(AddIncome.this))
			{
				new GetUpdateIncomeTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_no_network));
			}
			break;

		case R.id.payment_date_calander_btn:
			showDialog(DATE_DIALOG_ID);
			break;

		case R.id.add_sales_payment_date_et:
			showDialog(DATE_DIALOG_ID);
			
			break;
		case R.id.add_payment_type_tv:
			mCustomDialog.show();
			break;

		default:
			break;
		}
	}

	/*
	 * Income Account Id Async Task
	 */
	private class GetIncomeAccountIdTask extends AsyncTask<Void, Void, Void>
	{
	

		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(AddIncome.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_income;
			mIncomeAccountIdResponseStr = mConnectionManager.setUpHttpGet(mTaskUrl+"/"+mSessionId);
			//mIncomeAccountIdResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_income)+"/"+mSessionId);
			getIncomeAccountId();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{	
			FlourishProgressDialog.Dismiss(AddIncome.this);
		
if (mIncomeAccountIdResponseStr!=null) {
	

			
			
         if(mIncomeAccountIdResponseStr.contains("Login Key Not Valid"))
			{
				getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(AddIncome.this, LoginScreen.class));
			}
			else if(mIncomeAccountIdResponseStr.contains("Bad Parameters"))
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
			}
			else
			{
				
			}
         
			super.onPostExecute(result);
		}
		}
	}

	/*
	 * Save Income AsyncTask
	 */
	private class GetUpdateIncomeTask extends AsyncTask<Void, Void, Void>
	{
		

		@Override
		protected void onPreExecute()
		{
		FlourishProgressDialog.ShowProgressDialog(AddIncome.this);
			
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			putMileageData();
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_income;
			mIncomeUpdateStr = mConnectionManager.setUpHttpPut(mTaskUrl+"/"+mSessionId, mUpdateMileageRequestObj.toString());
			//mIncomeUpdateStr = mConnectionManager.setUpHttpPut(getResources().getString(R.string.update_income)+"/"+mSessionId, mUpdateMileageRequestObj.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(AddIncome.this);
				if(mIncomeUpdateStr.contains("Account not found"))
				{
					mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_account_not_found));
				}
				else
				{
					mAppNetwork.getAlertDialog(AddIncome.this, getString(R.string.alert_dialog_income_added_success));
					
					SharedPreferences.Editor editor=mSharedPreferences.edit();
					  editor.putString("FragmentScreen", "Money");
					  editor.commit();
					  setResult(1);
					  finish();
//					 Intent mIntent = new Intent(AddIncome.this, FlourishHomeActivity.class);
				
//					startActivity(mIntent);
					
					
					
				}
			
			super.onPostExecute(result);
		}
	}

	private void putMileageData() 
	{
		try 
		{
			mUpdateMileageRequestObj = new JSONObject();
			mUpdateMileageRequestObj.put("AccountId", mIncomeAccountIdStr);
			mUpdateMileageRequestObj.put("TransactionDate", mIncomeTransactionDateEt.getText().toString());
			mUpdateMileageRequestObj.put("Description", mIncomeDescriptionEt.getText().toString());
			mUpdateMileageRequestObj.put("Reference",mIncomeReferenceEt.getText().toString());
			mUpdateMileageRequestObj.put("Amount", mIncomeAmountEt.getText().toString());
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for parsing the Server response for getting the income Account Id.
	 */
	public void getIncomeAccountId() 
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mIncomeAccountIdResponseStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mIncomeAccountIdList.add(mJsonArray.getJSONObject(i).getString("Id"));
			}
		}
		catch(Exception e)
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
				mIncomeTransactionDateEt.setText(new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear));

				mIncomeDatePicker.init(mYear, mMonth, mDay, null);
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(AddIncome.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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
}