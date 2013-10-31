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

public class MileageDetails extends Activity implements OnClickListener
{
	private static final int DATE_DIALOG_ID = 1;
	private Button mBack = null;
	private Button mSave = null;
	private EditText mTripDescriptionEt = null;
	private EditText mMileageDateEt = null;
	private Button mMileageDateBtn = null;
	private TextView mVehicleDescriptionEt = null;
	private EditText mTripMilesEt = null;
	private DatePicker mMileageDatePicker = null;
	private String mVehicleIdStr = null;
	private String mVehicleTripIdStr = null;
	private String mTripDescriptionStr = null;
	private String mMileageDateStr = null;
	private String mVehicleDescriptionStr = null;
	private String mTripMilesStr = null;
	public String mMileageUpdateStr = null;
	private String mSessionId = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private AppNetwork mAppNetwork = null;
	private ConnectionManager mConnectionManager = null;
	private VehicleListCustomDialog mCustomDialog = null;
	private JSONObject mUpdateMileageRequestObj = null;
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	FlourishBaseApplication mFlourishBaseApplication;

	SharedPreferences mSharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mileage_details);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");

		initializeVariables();
		setMileageData();
	}

	
	private void initializeVariables()
	{
		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();
		mCustomDialog 	= new VehicleListCustomDialog(MileageDetails.this);
		mBack = (Button)findViewById(R.id.mileage_details_back);
		mSave = (Button)findViewById(R.id.mileage_save);
		mTripDescriptionEt = (EditText)findViewById(R.id.mileage_details_title_et);
		mMileageDateEt = (EditText)findViewById(R.id.mileage_details_date_et);
		mMileageDateBtn = (Button)findViewById(R.id.mileage_details_calendar_btn);
		mVehicleDescriptionEt = (TextView)findViewById(R.id.mileage_details_type_et);
		mTripMilesEt = (EditText)findViewById(R.id.mileage_details_miles_et);
		mMileageDatePicker = (DatePicker) findViewById(R.id.mileage_dpResult);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		
		mVehicleDescriptionEt.setOnClickListener(this);
		mMileageDateBtn.setOnClickListener(this);
		
		mVehicleIdStr = getIntent().getStringExtra("Vehicle_Id");;
		mVehicleTripIdStr = getIntent().getStringExtra("Vehicle_Trip_Id");
		
		Log.v("TAG", "mVehicleTripIdStr"+mVehicleTripIdStr);
		
		mTripDescriptionStr = getIntent().getStringExtra("Trip_Description");
		mVehicleDescriptionStr = getIntent().getStringExtra("Vehicle_Description");
		mTripMilesStr = getIntent().getStringExtra("Trip_Miles");
		mCustomDialog.getHeaderTv().setText("Select an Account");
		ArrayAdapter<CharSequence> mAddVehicleAdapter = ArrayAdapter.createFromResource(this, R.array.vehicles_array, android.R.layout.simple_spinner_item);
		mAddVehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCustomDialog.getListview().setAdapter(mAddVehicleAdapter);

		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mVehicleDescriptionEt.setText(mCustomDialog.getListview().getItemAtPosition(position).toString());
				mCustomDialog.dismiss();
			}
		});
	}

	private void setMileageData() 
	{
		mTripDescriptionEt.setText(mTripDescriptionStr);
		mVehicleDescriptionEt.setText(mVehicleDescriptionStr);
		mTripMilesEt.setText(mTripMilesStr);
		mMileageDateStr = getIntent().getStringExtra("Mileage_Date");
		String[] mMileageDateArray = mMileageDateStr.split("-");
		int mMonth = 0;
		if(mMileageDateArray[1].startsWith("0"))
		{
			mMonth = Integer.parseInt(mMileageDateArray[1].substring(1));
		}
		else
		{
			mMonth = Integer.parseInt(mMileageDateArray[1]);
		}
		String mMileageDateUpdatedStr = MONTHS[mMonth-1]+" "+mMileageDateArray[2].substring(0, 2)+", "+mMileageDateArray[0];;
		mMileageDateEt.setText(mMileageDateUpdatedStr);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.mileage_details_back:
			getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Mileage").commit();
			finish();
			break;

		case R.id.mileage_save:
			
	    if(mTripMilesEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(MileageDetails.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mMileageDateEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(MileageDetails.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mTripDescriptionEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(MileageDetails.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mAppNetwork.isNetworkAvailable(MileageDetails.this))
			{
				new GetUpdateMileageTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(MileageDetails.this, getString(R.string.alert_dialog_no_network));
			}
			break;
			
	

		case R.id.mileage_details_calendar_btn:
			showDialog(DATE_DIALOG_ID);
			break;

		case R.id.mileage_details_type_et:
			mCustomDialog.show();
			break;

		default:
			break;
		}
	}

	private class GetUpdateMileageTask extends AsyncTask<Void, Void, Void>
	{
		

		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(MileageDetails.this);
		
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl_update_mileage=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_mileage;
			putMileageData();
			mMileageUpdateStr = mConnectionManager.setUpHttpPost(mTaskUrl_update_mileage+mSessionId, mUpdateMileageRequestObj.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
		       FlourishProgressDialog.Dismiss(MileageDetails.this);
	
				if(mMileageUpdateStr.contains("Vehicle Trip not found"))
				{
					mAppNetwork.getAlertDialog(MileageDetails.this, getString(R.string.alert_dialog_vehicle_trip_not_found));
				}
				else if(mMileageUpdateStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(MileageDetails.this, LoginScreen.class));
				}
				else if(mMileageUpdateStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(MileageDetails.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					final Dialog dialog = new Dialog(MileageDetails.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.mileage_success_dialog);

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

							// Intent mIntentForMoney = new Intent(MileageDetails.this, FlourishHomeActivity.class);
							// startActivity(mIntentForMoney);
							 setResult(2);
	
							
							
							
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
			mUpdateMileageRequestObj = new JSONObject();
			mUpdateMileageRequestObj.put("MileageId", mVehicleTripIdStr);
			mUpdateMileageRequestObj.put("VehicleId", mVehicleIdStr);
			mUpdateMileageRequestObj.put("Mileage", mTripMilesEt.getText().toString());
			mUpdateMileageRequestObj.put("MileageDate", mMileageDateStr);
			mUpdateMileageRequestObj.put("Description", mTripDescriptionEt.getText().toString());
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
				mMileageDateStr = (new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear)).toString();
				mMileageDateEt.setText(new StringBuilder().append(MONTHS[mMonth]).append(" ").append(mDay).append(", ").append(mYear));

				mMileageDatePicker.init(mYear, mMonth, mDay, null);
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(MileageDetails.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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
		getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Mileage").commit();
	}
}