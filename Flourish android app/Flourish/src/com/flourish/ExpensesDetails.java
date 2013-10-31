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

public class ExpensesDetails extends Activity implements OnClickListener
{
	private TextView mExpensesAccountNameEt = null;
	private EditText mExpensesTransactionDateEt = null;
	private EditText mExpensesDescriptionEt = null;
	private EditText mExpensesReferenceEt = null;
	private EditText mExpensesAmountEt = null;
	private Button mSave = null;
	public Button mExpensesDeletebtn=null;
	private Button mBack = null;
	private Button mExpensesDateBtn = null;
	private DatePicker mExpensesDatePicker = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String mAccountIdStr = null;
	private String mReferenceStr = null;
	private String mSessionId = null;
	public String mExpensesUpdateStr = null;
	private String mExpensesDeleteStr=null;
	private String mExpensesDeleteResponse=null;
	private String mExpensesDateStr = null;
	String mExpensesUpdatedDateStr=null;
   String mExpensesUpdatedDateSendStr=null;
	private String mExpensesJournelId=null;

	
	private static final int DATE_DIALOG_ID = 1;
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private VehicleListCustomDialog mCustomDialog = null;
	private JSONObject mUpdateExpenceRequestObj = null;
	private ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expenses_details);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mExpensesDeleteStr=getIntent().getStringExtra("JournalId");
       Log.v("TAG", "JournalId==in oncreate"+mExpensesDeleteStr);
		initializeVariables();
		setExpensesData();

		mExpensesDateBtn.setOnClickListener(new OnClickListener()
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
		mExpensesAccountNameEt = (TextView)findViewById(R.id.expenses_details_account_name_et);
		mExpensesTransactionDateEt = (EditText)findViewById(R.id.expenses_details_transaction_date_et);
		mExpensesDescriptionEt = (EditText)findViewById(R.id.expenses_details_description_et);
		mExpensesReferenceEt = (EditText)findViewById(R.id.expenses_details_type_et);
		mExpensesAmountEt = (EditText)findViewById(R.id.expenses_details_amount_et);
		mExpensesDatePicker = (DatePicker) findViewById(R.id.expenses_dpResult);
		mExpensesDateBtn = (Button)findViewById(R.id.expenses_details_calendar_btn);
		mSave = (Button)findViewById(R.id.expenses_save);
		mBack = (Button)findViewById(R.id.expenses_details_back);
		mExpensesDeletebtn=(Button)findViewById(R.id.expenses_details_delete_btn);
		mCustomDialog 	= new VehicleListCustomDialog(ExpensesDetails.this);
		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();
		mBack.setOnClickListener(this);
		mExpensesDeletebtn.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mExpensesAccountNameEt.setOnClickListener(this);

		mCustomDialog.getHeaderTv().setText("Select an Account");
		ArrayAdapter<CharSequence> mAddVehicleAdapter = ArrayAdapter.createFromResource(this, R.array.expenses_account_name_array, android.R.layout.simple_spinner_item);
		mAddVehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCustomDialog.getListview().setAdapter(mAddVehicleAdapter);

		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mExpensesAccountNameEt.setText(mCustomDialog.getListview().getItemAtPosition(position).toString());
				mCustomDialog.dismiss();
			}
		});
	}

	/**
	 * Method for getting the expenses fields data
	 */
	private void setExpensesData() 
	{
		
	
		
		
		mExpensesJournelId=getIntent().getStringExtra("JournalId");
		mAccountIdStr = getIntent().getStringExtra("Account_Id");
		mReferenceStr = getIntent().getStringExtra("Reference");
		mExpensesAccountNameEt.setText(getIntent().getStringExtra("Account_Name"));
		mExpensesDescriptionEt.setText(getIntent().getStringExtra("Description"));
		mExpensesReferenceEt.setText(getIntent().getStringExtra("Reference"));
		mExpensesAmountEt.setText(getIntent().getStringExtra("Amount"));
		mExpensesDateStr = getIntent().getStringExtra("Transaction_Date");
		
		Log.v("TAG", "Dataformat=="+mExpensesDateStr);
		String[] dateArray=mExpensesDateStr.split("-");
		String ExpMonth=dateArray[1];
		
		String ExpYear=dateArray[0];
		String ExpDayArray=dateArray[2];
		
		String ExpDay[]=ExpDayArray.split("T");
		String day=ExpDay[0];
		
		mExpensesUpdatedDateSendStr=ExpMonth+"-"+day+"-"+ExpYear;
		Log.v("TAG", "mExpensesUpdatedDateSendStr "+mExpensesUpdatedDateSendStr);
		
		String[] mIncomeDateArray = mExpensesDateStr.split("-");
		int mMonth = 0;
		if(mIncomeDateArray[1].startsWith("0"))
		{
			mMonth = Integer.parseInt(mIncomeDateArray[1].substring(1));
		}
		else
		{
			mMonth = Integer.parseInt(mIncomeDateArray[1]);
		}
		 mExpensesUpdatedDateStr = MONTHS[mMonth-1]+" "+day+", "+ExpYear;
		mExpensesTransactionDateEt.setText(mExpensesUpdatedDateStr);
		
	
		
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.expenses_details_back:
		//	getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Expenses").commit();
			
			
			finish();
			break;

		case R.id.expenses_save:
			if(mExpensesAccountNameEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesTransactionDateEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesDescriptionEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesReferenceEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesAmountEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mAppNetwork.isNetworkAvailable(ExpensesDetails.this))
			{
				new GetUpdateExpensesTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_no_network));
			}
			break;
		case R.id.expenses_details_calendar_btn:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.expenses_details_account_name_et:
			mCustomDialog.show();
			break;
		case R.id.expenses_details_delete_btn:
			// Custom Dialog for asking whether to delete the expense or not
			final Dialog dialog = new Dialog(ExpensesDetails.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.expense_delete_dialog);

			Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);
			Button mNo = (Button) dialog.findViewById(R.id.dialogButtonNO);

			mYes.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					dialog.cancel();
					if (mAppNetwork.isNetworkAvailable(ExpensesDetails.this)) 
					{
						new GetDeleteExpensesTask().execute();
						
					}
					else 
					{
						mAppNetwork.getAlertDialog(ExpensesDetails.this, getResources().getString(R.string.alert_dialog_no_network));
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
	 * Method for saving the money expenses AsyncTask.
	 */
	private class GetUpdateExpensesTask extends AsyncTask<Void, Void, Void>
	{
	

		@Override
		protected void onPreExecute()
		{
			
			FlourishProgressDialog.ShowProgressDialog(ExpensesDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_expenses;
			putMileageData();
			mExpensesUpdateStr = mConnectionManager.setUpHttpPost(mTaskUrl+"/"+mSessionId, mUpdateExpenceRequestObj.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
		          FlourishProgressDialog.Dismiss(ExpensesDetails.this);
				if(mExpensesUpdateStr.contains("Account not found"))
				{
					mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_account_not_found));
				}
				else if(mExpensesUpdateStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(ExpensesDetails.this, LoginScreen.class));
				}
				else if(mExpensesUpdateStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				
				
				
				else
				{
					final Dialog dialog = new Dialog(ExpensesDetails.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.expense_success_dialog);

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
							  
//					          startActivity(new Intent(ExpensesDetails.this, FlourishHomeActivity.class));
							  
							  setResult(0);
					
						
								finish();
								
								
								
								
							
							
							
						}
					});
					dialog.show();
				}
			
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for passing the edit expenses data.
	 */
	private void putMileageData() 
	{
		try 
		{
	
			
			mUpdateExpenceRequestObj = new JSONObject();
			mUpdateExpenceRequestObj.put("JournalId", mExpensesJournelId);
			mUpdateExpenceRequestObj.put("AccountId", mAccountIdStr);
			mUpdateExpenceRequestObj.put("TransactionDate", mExpensesUpdatedDateSendStr);
			mUpdateExpenceRequestObj.put("Description", mExpensesDescriptionEt.getText().toString());
			mUpdateExpenceRequestObj.put("Reference", mExpensesReferenceEt.getText().toString());
			mUpdateExpenceRequestObj.put("Amount", mExpensesAmountEt.getText().toString());
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for Deleting the expenses AsyncTask
	 */
	private class GetDeleteExpensesTask extends AsyncTask<Void, Void, Void>
	{
	

		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(ExpensesDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl_delete_expenses=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.delete_expenses;
			mExpensesDeleteResponse = mConnectionManager.setUpHttpDelete(mTaskUrl_delete_expenses+mSessionId+"/"+mExpensesDeleteStr);
			Log.v("response","ExpensesDelete"+mExpensesDeleteResponse);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(ExpensesDetails.this);
				if(mExpensesDeleteResponse.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(ExpensesDetails.this, getString(R.string.alert_dialog_expense_not_deleted));
				}
				else if(mExpensesDeleteResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(ExpensesDetails.this, LoginScreen.class));
				}
				else
				{
					final Dialog dialog = new Dialog(ExpensesDetails.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.expense_delete_dialog_confirmed);

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

//							 Intent mIntentForMoney = new Intent(ExpensesDetails.this, FlourishHomeActivity.class);
//							 startActivity(mIntentForMoney);
							setResult(1);
							
							
							
							finish();
						}
					});
					
					dialog.show();
				}
			
			super.onPostExecute(result);
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
				mExpensesDateStr = (new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear)).toString();
				mExpensesTransactionDateEt.setText(new StringBuilder().append(MONTHS[mMonth]).append(" ").append(mDay).append(", ").append(mYear));
				
				mExpensesDatePicker.init(mYear, mMonth, mDay, null);
				
				mExpensesUpdatedDateSendStr=mMonth+1+"-"+mDay+"-"+mYear;
				
			}
			else 
			{
				mAppNetwork.getAlertDialog(ExpensesDetails.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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
		
		
		
	//	getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Expenses").commit();
		finish();
	
	
	
	}
}