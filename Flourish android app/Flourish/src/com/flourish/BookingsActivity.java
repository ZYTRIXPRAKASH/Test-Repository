package com.flourish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flourish.adapters.BookingsListBaseAdapter;
import com.flourish.adapters.SalesListBaseAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.contacts.Item;
import com.flourish.contacts.Items;
import com.flourish.contacts.NamesAdapter;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.utils.BookingsData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BookingsActivity extends BaseTimeOutActivity {

	Button mCancelButton;
	ListView mBookingListViwe;
	
	
	String mGetBookingListResponse=null;
	ConnectionManager mConnectionManager;
	private List<BookingsData> mAllBookingsList = null;
	BookingsData mBookingsData;

	AppNetwork mAppNetwork;
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;
	private String mSessionIdStr = null;
	
	
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookings_activity);
		
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		onSessionStarted(true);
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
		mSessionIdStr = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mAllBookingsList=new ArrayList<BookingsData>();
		Log.v("TAG", "session id in bookings"+mSessionIdStr);
		mBookingListViwe=(ListView)findViewById(R.id.booking_listview);
		mCancelButton=(Button)findViewById(R.id.cancel_btn_bookings);
		
		
		if (mAppNetwork.isNetworkAvailable(BookingsActivity.this)) {
		
			new BookingListAsyncTask().execute();
			
			
		}
	      
		
		else
		{
			
			mAppNetwork.getAlertDialog(BookingsActivity.this, getResources().getString(R.string.alert_dialog_no_network));	
		}
		
		
		
	mBookingListViwe.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			String mBookingSubject=mAllBookingsList.get(position).BookingSubject;
			String mBookingCalenederId=mAllBookingsList.get(position).CalendarId;
			
			Intent i=new Intent();
			i.setAction(mBookingCalenederId+","+mBookingSubject);
	
			setResult(RESULT_OK, i);
			finish();
			
			
			
			
			
		}
	});	
		
		
		
	mCancelButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			finish();
			
		}
	})	;
		
		
		
		
		
		
		
		
	}

	
	
	
	class BookingListAsyncTask extends AsyncTask<Void, Void, Void>
	{
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			FlourishProgressDialog.Dismiss(BookingsActivity.this);
			
			
			
		}
		
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.bookingList;
			
			mGetBookingListResponse = mConnectionManager.setUpHttpGet(mTaskUrl+mSessionIdStr);
			getBookingLIstResponse();
			
			
			return null;
		}
		
		
		
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			FlourishProgressDialog.Dismiss(BookingsActivity.this);
			
			
			BookingsListBaseAdapter mBookingsListBaseAdapter=new BookingsListBaseAdapter(BookingsActivity.this, mAllBookingsList);
			

		  	   mBookingListViwe.setAdapter(mBookingsListBaseAdapter);
			
			
			
			
			
		}


		private void getBookingLIstResponse() {
			
			if (mGetBookingListResponse!=null) {
				
				
				
			
				try {
					JSONObject mCompleteJsonObject=new JSONObject(mGetBookingListResponse);
					
					JSONArray mDataJsonArray = mCompleteJsonObject.getJSONArray("data");
					
					for (int i = 0; i < mDataJsonArray.length(); i++) {
						JSONObject json=mDataJsonArray.getJSONObject(i);
						
						mBookingsData =new BookingsData();
						
						mBookingsData.CalendarId=json.getString("CalendarId");
						mBookingsData.BookingSubject=json.getString("BookingSubject");
						mBookingsData.StartDate=json.getString("StartDate");
						mBookingsData.BookingType=json.getString("BookingType");
						mBookingsData.BookingTypeId=json.getString("BookingTypeId");
						mAllBookingsList.add(mBookingsData);
						
						//Log.v("TAG", " list size =="+mAllBookingsList.size());
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					
				//	Log.v("TAG", " exeption in booking service =="+e.toString());
					
					
				}
				
				
				
				
				
			}
			
			
			
			
		}
		
		
		
		
		
		
		
	}
	

}
