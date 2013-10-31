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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flourish.adapters.MoneyListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class AddMileage extends Activity implements OnClickListener
{
	private static final int DATE_DIALOG_ID = 1;
	private Button mBack = null;
	private Button mSave = null;
	private RelativeLayout mAddVehicleRl = null;
	private EditText mTripDescriptionEt = null;
	private EditText mMileageDateEt = null;
	private Button mMileageDateBtn = null;
	private EditText mTripMilesEt = null;
	private DatePicker mMileageDatePicker = null;
	private TextView mAddVehicleTv = null;
	public String mMileageUpdateStr = null;
	public String mVehicleStr = null;
	private String mSessionId = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private AppNetwork mAppNetwork = null;
	private VehicleListCustomDialog mCustomDialog = null;
	private ConnectionManager mConnectionManager = null;
	private JSONObject mUpdateMileageRequestObj = null;
	private MoneyListAdapter mMileageListAdapter = null;
	private ArrayList<String> mMileageList = new ArrayList<String>();
	private ArrayList<String> mVehicleIdList = new ArrayList<String>();
	private String mVehicleIdStr = null;
	SharedPreferences mSharedPreferences;
	FlourishBaseApplication mFlourishBaseApplication;
	String mTaskUrl=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_mileage);


		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);

		initializeVariables();
		new GetVehicleIdTask().execute();
	}

	private void initializeVariables()
	{
		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();
		mCustomDialog 	= new VehicleListCustomDialog(AddMileage.this);
		mBack = (Button)findViewById(R.id.add_mileage_back);
		mSave = (Button)findViewById(R.id.mileage_save);
		mAddVehicleRl = (RelativeLayout)findViewById(R.id.add_mileage_vehicle_rl);
		mAddVehicleTv = (TextView)findViewById(R.id.add_mileage_vehicle_tv);
		mTripDescriptionEt = (EditText)findViewById(R.id.add_mileage_title_et);
		mMileageDateEt = (EditText)findViewById(R.id.add_mileage_date_et);
		mMileageDateBtn = (Button)findViewById(R.id.add_mileage_calendar_btn);
		mTripMilesEt = (EditText)findViewById(R.id.add_mileage_miles_et);
		mMileageDatePicker = (DatePicker) findViewById(R.id.mileage_dpResult);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mAddVehicleRl.setOnClickListener(this);
		mAddVehicleTv.setOnClickListener(this);
		mMileageDateBtn.setOnClickListener(this);
		mMileageDateEt.setOnClickListener(this);

		mMileageList.add("Ferrari 458");
		mMileageList.add("Audi R8 GT");
		mMileageList.add("Audi A4");
		mCustomDialog.getHeaderTv().setText("Select an Account");
		mMileageListAdapter = new MoneyListAdapter(AddMileage.this, R.layout.sales_list_row, mMileageList);
		mCustomDialog.getListview().setAdapter(mMileageListAdapter);
		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mVehicleIdStr = mVehicleIdList.get(position);
				mAddVehicleTv.setText(mMileageList.get(position));
				mCustomDialog.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.add_mileage_back:
			//getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Mileage").commit();
			//startActivity(new Intent(AddMileage.this, FlourishHomeActivity.class));
			
			finish();
			break;

		case R.id.mileage_save:
			if(mAddVehicleTv.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mTripMilesEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mMileageDateEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mTripDescriptionEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_enter_mileage_details_to_be_added));
			}
			else if(mAppNetwork.isNetworkAvailable(AddMileage.this))
			{
				new AddMileageTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_no_network));
			}
			break;

		case R.id.add_mileage_calendar_btn:
			showDialog(DATE_DIALOG_ID);
			break;

		case R.id.add_mileage_date_et:
			showDialog(DATE_DIALOG_ID);
			break;
			
			
		case R.id.add_mileage_vehicle_tv:
			mCustomDialog.show();
			break;

		default:
			break;
		}
	}

	private class GetVehicleIdTask extends AsyncTask<Void, Void, Void>
	{


		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(AddMileage.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			 mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_vehicles;
			mVehicleStr = mConnectionManager.setUpHttpGet(mTaskUrl+"/"+mSessionId);
			//mVehicleStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_vehicles)+"/"+mSessionId);
			getVehicleId();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(AddMileage.this);

			if (mVehicleStr!=null) {
				
		
			 if(mVehicleStr.contains("Login Key Not Valid"))
			{
				getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(AddMileage.this, LoginScreen.class));
			}
			else if(mVehicleStr.contains("Bad Parameters"))
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
			}
			super.onPostExecute(result);
		}
			
			
			else{
				
				mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_no_network));
			}
		}
	}

	private class AddMileageTask extends AsyncTask<Void, Void, Void>
	{
		
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(AddMileage.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			putMileageData();

            mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.add_mileage;
			mMileageUpdateStr = mConnectionManager.setUpHttpPut(mTaskUrl+"/"+mSessionId, mUpdateMileageRequestObj.toString());
		//	mMileageUpdateStr = mConnectionManager.setUpHttpPut(getResources().getString(R.string.add_mileage)+"/"+mSessionId, mUpdateMileageRequestObj.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(AddMileage.this);
				if(mMileageUpdateStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(AddMileage.this, LoginScreen.class));
				}
				else if(mMileageUpdateStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else if(mMileageUpdateStr.contains("Vehicle not found"))
				{
					mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_vehicle_not_found));
				}
				else
				{
					mAppNetwork.getAlertDialog(AddMileage.this, getString(R.string.alert_dialog_mileage_added_success));
					SharedPreferences.Editor editor=mSharedPreferences.edit();
					  editor.putString("FragmentScreen", "Money");
					  editor.commit();

					  setResult(2);
					  finish();
					  
					/* Intent mIntentForMoney = new Intent(AddMileage.this, FlourishHomeActivity.class);
					 startActivity(mIntentForMoney);*/
					
					
				}
			
			super.onPostExecute(result);
		}
	}

	private void putMileageData() 
	{
		try 
		{
			mUpdateMileageRequestObj = new JSONObject();
			mUpdateMileageRequestObj.put("VehicleId", mVehicleIdStr);
			mUpdateMileageRequestObj.put("Mileage", mTripMilesEt.getText().toString());
			mUpdateMileageRequestObj.put("MileageDate", mMileageDateEt.getText().toString());
			mUpdateMileageRequestObj.put("Description", mTripDescriptionEt.getText().toString());
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	public void getVehicleId() 
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mVehicleStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mVehicleIdList.add(mJsonArray.getJSONObject(i).getString("Id"));
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
				mMileageDateEt.setText(new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear));

				mMileageDatePicker.init(mYear, mMonth, mDay, null);
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(AddMileage.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
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