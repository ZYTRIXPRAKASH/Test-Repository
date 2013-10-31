package com.flourish;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.flourish.adapters.MoneyExpenseBaseAdapter;
import com.flourish.adapters.MoneyIncomeBaseAdapter;
import com.flourish.adapters.MoneyMileageBaseAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.utils.MoneyExpensesData;
import com.flourish.utils.MoneyIncomeData;
import com.flourish.utils.MoneyMileageData;

@SuppressLint("ValidFragment")
public class MoneyFragment extends Fragment {
	
	
	Fragment mFragment;
	AppNetwork mAppNetwork;
	
	private ListView mMoneyExpensesListView = null;
	private ListView mMoneyIncomeListView = null;
	private ListView mMoneyMileageListView = null;
	
	
	private ConnectionManager mConnectionManager = null;
	SharedPreferences mSharedPreferences;
	private String mSessionId = null;
	Intent mIntent;
	
	
	
	String mMoneyMileageResponse=null;
	
	String mMoneyExpensesResponse=null;
	String mMoneyIncomeResponse=null;
	
	MoneyExpenseBaseAdapter mMoneyExpenseBaseAdapter=null;
	
	List<MoneyExpensesData> mAllMoneyExpensesList=null;
    MoneyExpensesData mMoneyExpensesData=null;
    
    List<MoneyIncomeData> mAllMoneyIncomeList=null;
    MoneyIncomeData mMoneyIncomeData=null;
	MoneyIncomeBaseAdapter mMoneyIncomeBaseAdapter=null;
	
	
	List<MoneyMileageData> mAllMoneyMilageList=null;
	MoneyMileageData mMoneyMileageData=null;
	MoneyMileageBaseAdapter mMoneyMileageBaseAdapter=null;
	LinearLayout mMoneyTopBarLl;
	FlourishBaseApplication mFlourishBaseApplication;
	
	
	
	
	

	
	public Button mExpensesBtn = null;
	public Button mMileageBtn = null;	
	public Button mIncomeBtn = null;
	Activity mContext;

	
	interface MoneyCLickListener {
		public void addType(int addType);
	}
	
	
	
	
	public MoneyFragment() {
	}
	
	public MoneyFragment(Activity context) {
		this.mContext = context;
	}
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		
		
		
		
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFlourishBaseApplication=(FlourishBaseApplication)getActivity().getApplicationContext();
		mAppNetwork=new  AppNetwork();
	
		
		
		mConnectionManager=new ConnectionManager();
		
		mSharedPreferences=getActivity().getSharedPreferences("LoginInfo", 0);
		mSessionId=mSharedPreferences.getString("sessionId", "nothing");
		Log.v("TAG", "mSessionId  "+mSessionId);
		
		View mView;

		mView=inflater.inflate(R.layout.money_fragment, container, false);
		
		
		mMoneyExpensesListView = (ListView)mView.findViewById(R.id.money_expenses_lv);
		mMoneyMileageListView = (ListView)mView.findViewById(R.id.money_mileage_lv);
		mMoneyIncomeListView = (ListView)mView.findViewById(R.id.money_income_lv);
		
		mExpensesBtn = (Button)mView.findViewById(R.id.money_expenses_btn);
		mIncomeBtn = (Button)mView.findViewById(R.id.money_income_btn);
		mMileageBtn = (Button)mView.findViewById(R.id.money_mileage_btn);
		mMoneyTopBarLl = (LinearLayout)mView.findViewById(R.id.money_top_bar_rl);
		
		mExpensesBtn.setOnClickListener(buttonClickListner);
		mIncomeBtn.setOnClickListener(buttonClickListner);
		mMileageBtn.setOnClickListener(buttonClickListner);
		
		
		
		
		
		
		mMoneyExpensesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				Log.v("TAG", "JournalId in list onclick"+mAllMoneyExpensesList.get(position).JournalId);
				
						mIntent=new  Intent(getActivity(),  ExpensesDetails.class);
						
						
						mIntent.putExtra("JournalId", mAllMoneyExpensesList.get(position).JournalId);
						
						mIntent.putExtra("Account_Id", mAllMoneyExpensesList.get(position).AccountId);
						mIntent.putExtra("Reference", mAllMoneyExpensesList.get(position).Reference);
						mIntent.putExtra("Description", mAllMoneyExpensesList.get(position).Description);
						mIntent.putExtra("Amount", mAllMoneyExpensesList.get(position).Amount);
						mIntent.putExtra("Transaction_Date", mAllMoneyExpensesList.get(position).TransactionDate);
						mIntent.putExtra("Account_Name", mAllMoneyExpensesList.get(position).AccountName);
						
						getActivity().startActivityForResult(mIntent, 0);
				
					
				
				
				
				
				
				
				
				
				
				
				
			}
		});
		
		
		
		

		mMoneyMileageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
			     	mIntent=new Intent(getActivity(), MileageDetails.class);
						
			        	mIntent.putExtra("Vehicle_Id", mAllMoneyMilageList.get(position).VehicleId);
						mIntent.putExtra("Vehicle_Trip_Id", mAllMoneyMilageList.get(position).VehicleTripId);
						mIntent.putExtra("Trip_Description", mAllMoneyMilageList.get(position).TripDescription);
						mIntent.putExtra("Trip_Miles", mAllMoneyMilageList.get(position).TripMiles);
						mIntent.putExtra("Mileage_Date", mAllMoneyMilageList.get(position).MileageDate);
						mIntent.putExtra("Vehicle_Description", mAllMoneyMilageList.get(position).VehicleDescription);
						getActivity().startActivityForResult(mIntent, 2);
						
			
				
				
				
				
				
				
				
				
			}
		});
		
		
		
		
		

		mMoneyIncomeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
			
						mIntent =new Intent(getActivity(), IncomeDetails.class);
						mIntent.putExtra("Account_Id", mAllMoneyIncomeList.get(position).AccountId);
						mIntent.putExtra("JournalId", mAllMoneyIncomeList.get(position).JournalId);
						mIntent.putExtra("Reference", mAllMoneyIncomeList.get(position).Reference);
						mIntent.putExtra("Description", mAllMoneyIncomeList.get(position).Description);
						mIntent.putExtra("Amount", mAllMoneyIncomeList.get(position).Amount);
						mIntent.putExtra("Transaction_Date", mAllMoneyIncomeList.get(position).TransactionDate);
						mIntent.putExtra("Account_Name", mAllMoneyIncomeList.get(position).AccountName);
						getActivity().startActivityForResult(mIntent, 1);
				
				
				
				
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		if (mAppNetwork.isNetworkAvailable(getActivity())) 
		{
			new GetMoneyExpensesTask().execute();
		}
		else 
		{
			mAppNetwork.getAlertDialog(getActivity(), getResources().getString(R.string.alert_dialog_no_network));
		}
	
		
		
		return mView ;
		
		
		
		
		
	}

	
	
	
	View.OnClickListener buttonClickListner=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.money_expenses_btn :
				
				mMoneyTopBarLl.setBackgroundResource(R.drawable.top_grey_first_selected);
				
				mExpensesBtn.setTextColor(getResources().getColor(R.color.white));
				mIncomeBtn.setTextColor(getResources().getColor(R.color.dark_gray));
				mMileageBtn.setTextColor(getResources().getColor(R.color.dark_gray));
				
				((FlourishHomeActivity)getActivity()).addType(FlourishHomeActivity.EXPENSES);
				
				new GetMoneyExpensesTask().execute();
				
				
				break;

			case R.id.money_mileage_btn:
				
				
				mMoneyTopBarLl.setBackgroundResource(R.drawable.top_grey_third_selected);
				mExpensesBtn.setTextColor(getResources().getColor(R.color.dark_gray));
				mIncomeBtn.setTextColor(getResources().getColor(R.color.dark_gray));
				mMileageBtn.setTextColor(getResources().getColor(R.color.white));
				
				((FlourishHomeActivity)getActivity()).addType(FlourishHomeActivity.MILAGE);
				
				new GetMoneyMileageTask().execute();
				break;

			case R.id.money_income_btn:
				mMoneyTopBarLl.setBackgroundResource(R.drawable.top_grey_second_selected);
				mExpensesBtn.setTextColor(getResources().getColor(R.color.dark_gray));
				mIncomeBtn.setTextColor(getResources().getColor(R.color.white));
				mMileageBtn.setTextColor(getResources().getColor(R.color.dark_gray));
				mMoneyExpensesListView.setVisibility(View.GONE);
				((FlourishHomeActivity)getActivity()).addType(FlourishHomeActivity.INCOME);
				new GetMoneyIncomeTask().execute();
				break;
				
				
			default:
				break;
			}
			
			
			
			
		}
	};
	
	

	
	
	/**
	 * Method for displaying money expenses AsyncTask
	 */
	private class GetMoneyExpensesTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
		FlourishProgressDialog.ShowProgressDialog(getActivity());
		
		mAllMoneyExpensesList=new ArrayList<MoneyExpensesData>();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)

		{   String mTaskUrl_get_expenses_list=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_expenses_list;
			mMoneyExpensesResponse = mConnectionManager.setUpHttpGet(mTaskUrl_get_expenses_list+mSessionId+"/-/-/-");
			Log.v("response", "==mMoneyExpensesResponse=="+mMoneyExpensesResponse);
			getMoneyExpenses();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(getActivity());
			
			if (mMoneyExpensesResponse!=null) {
				
		
				if(mMoneyExpensesResponse.contains("Login Key Not Valid"))
				{
					getActivity().getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(getActivity(), LoginScreen.class));
					//finish();
				}
				else if(mMoneyExpensesResponse.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(getActivity(), getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mMoneyIncomeListView.setVisibility(View.GONE);
					mMoneyMileageListView.setVisibility(View.GONE);
					mMoneyExpensesListView.setVisibility(View.VISIBLE);
				
					mMoneyExpenseBaseAdapter =new MoneyExpenseBaseAdapter(getActivity(),mAllMoneyExpensesList);
					mMoneyExpensesListView.setAdapter(mMoneyExpenseBaseAdapter);
					
				
				}
				
		
			super.onPostExecute(result);
		}
			
			
			else{
				
				mAppNetwork.getAlertDialog(getActivity(), getString(R.string.alert_dialog_no_network));
			}
	}
	}
	
	/**
	 * Method for parsing the response of money expenses from server
	 */
	public void getMoneyExpenses() 
	{
		
		
		try 
		{
			JSONObject mJsonObj = new JSONObject(mMoneyExpensesResponse);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				
				mMoneyExpensesData=new MoneyExpensesData();
				mMoneyExpensesData.ExpenceId=(mJsonArray.getJSONObject(i).getString("Id"));
				mMoneyExpensesData.AccountId=(mJsonArray.getJSONObject(i).getString("AccountId"));
				mMoneyExpensesData.Reference=(mJsonArray.getJSONObject(i).getString("Reference"));
				mMoneyExpensesData.Description=(mJsonArray.getJSONObject(i).getString("Description"));
				mMoneyExpensesData.Amount=(mJsonArray.getJSONObject(i).getString("Amount"));
				mMoneyExpensesData.TransactionDate=(mJsonArray.getJSONObject(i).getString("TransactionDate"));
				mMoneyExpensesData.AccountName=(mJsonArray.getJSONObject(i).getString("AccountName"));
				mMoneyExpensesData.JournalId=(mJsonArray.getJSONObject(i).getString("JournalId"));
				Log.v("TAG", "JournalId"+mMoneyExpensesData.JournalId);
				mAllMoneyExpensesList.add(mMoneyExpensesData);
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 *Method for displaying money income details AsyncTask
	 */
	private class GetMoneyIncomeTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
		
			FlourishProgressDialog.ShowProgressDialog(getActivity());
			super.onPreExecute();
			
			
			
			mAllMoneyIncomeList=new ArrayList<MoneyIncomeData>();
		
			
		}

		@Override
		protected Void doInBackground(Void... params)
		{   
			String mTaskUrl_get_income_list=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_income_list;
			mMoneyIncomeResponse = mConnectionManager.setUpHttpGet(mTaskUrl_get_income_list+mSessionId+"/-/-/-");
			Log.v("response", "==mMoneyIncomeResponse=="+mMoneyIncomeResponse);
			getMoneyIncome();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			
			FlourishProgressDialog.Dismiss(getActivity());
			
			if (mMoneyIncomeResponse!=null) {
			
			
				if(mMoneyIncomeResponse.contains("Login Key Not Valid"))
				{
					getActivity().getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(getActivity(), LoginScreen.class));
					//finish();
				}
				else if(mMoneyIncomeResponse.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(	getActivity(), getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mMoneyIncomeListView.setVisibility(View.VISIBLE);
					mMoneyMileageListView.setVisibility(View.GONE);
					mMoneyExpensesListView.setVisibility(View.GONE);
                    
					mMoneyIncomeBaseAdapter=new MoneyIncomeBaseAdapter(getActivity(),mAllMoneyIncomeList);
					mMoneyIncomeListView.setAdapter(mMoneyIncomeBaseAdapter);
					
					
					
				}
		
			super.onPostExecute(result);
		}
	}
	}
	
	
	
	
	public void getMoneyIncome() 
	{
		
		try 
		{
			JSONObject mJsonObj = new JSONObject(mMoneyIncomeResponse);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mMoneyIncomeData=new MoneyIncomeData();
				
				mMoneyIncomeData.AccountId=(mJsonArray.getJSONObject(i).getString("AccountId"));
					
				mMoneyIncomeData.JournalId=(mJsonArray.getJSONObject(i).getString("JournalId"));
				mMoneyIncomeData.Reference=(mJsonArray.getJSONObject(i).getString("Reference"));
				mMoneyIncomeData.Description=(mJsonArray.getJSONObject(i).getString("Description"));
				mMoneyIncomeData.Amount=(mJsonArray.getJSONObject(i).getString("Amount"));
				mMoneyIncomeData.TransactionDate=(mJsonArray.getJSONObject(i).getString("TransactionDate"));
				mMoneyIncomeData.AccountName=(mJsonArray.getJSONObject(i).getString("AccountName"));
                
				mAllMoneyIncomeList.add(mMoneyIncomeData);
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	

	
	
	
	/**
	 * Method for displaying money mileage list AsyncTask 
	 */
	private class GetMoneyMileageTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(getActivity());
			
		
			mAllMoneyMilageList=new ArrayList<MoneyMileageData>();
			
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{   
			String mTaskUrl_get_mileage_list=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_mileage_list;
			mMoneyMileageResponse = mConnectionManager.setUpHttpGet(mTaskUrl_get_mileage_list+mSessionId+"/-/-/-");
			Log.v("response", "==mMoneyMileageResponse=="+mMoneyMileageResponse);
			getMoneyMileage();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
		          
			FlourishProgressDialog.Dismiss(getActivity());
			
			if (mMoneyMileageResponse!=null) {
				
			
				if(mMoneyMileageResponse.contains("Login Key Not Valid"))
				{
					getActivity().getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(getActivity(), LoginScreen.class));
					//finish();
				}
				else if(mMoneyMileageResponse.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(getActivity(), getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mMoneyIncomeListView.setVisibility(View.GONE);
					mMoneyMileageListView.setVisibility(View.VISIBLE);
					mMoneyExpensesListView.setVisibility(View.GONE);
					
			
					mMoneyMileageBaseAdapter=new  MoneyMileageBaseAdapter(getActivity(),mAllMoneyMilageList);
					mMoneyMileageListView.setAdapter(mMoneyMileageBaseAdapter);
					
					
					
					
				
				
				}
		
			super.onPostExecute(result);
		}
	}
	}
	
	/**
	 * Method for parsing the response of money mileage from server
	 */

	public void getMoneyMileage() 
	{
	
		
		
		try 
		{
			JSONObject mJsonObj = new JSONObject(mMoneyMileageResponse);
			JSONArray mJsonArray;
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mMoneyMileageData=new MoneyMileageData();
				
				
				JSONObject json=mJsonArray.getJSONObject(i);
				
				mMoneyMileageData.VehicleId=json.getString("VehicleId");
				
				mMoneyMileageData.VehicleTripId=json.getString("VehicleTripId");
				mMoneyMileageData.TripDescription=json.getString("TripDescription");
				mMoneyMileageData.TripMiles=json.getString("TripMiles");
				mMoneyMileageData.MileageDate=json.getString("MileageDate");
				mMoneyMileageData.VehicleDescription=json.getString("VehicleDescription");
				
				mAllMoneyMilageList.add(mMoneyMileageData);
				
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
	
	//
	
	
	
	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.v("TAG", "onActivityResult  requestCode="+requestCode);
		switch (requestCode) {
		case 1:
			
			Log.v("TAG", "case 1");
			mExpensesBtn.performClick();
			
			
			
			break;
			
			 
	         case 2:
	        	 mIncomeBtn.performClick();
			
			
			break;
			
			
			
	        case 3:
	        	Log.v("TAG", "case 3");
	        	mMileageBtn.performClick();
	        	
		
		
		   break;
			
			

		default:
			break;
		}

		
		
		
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
