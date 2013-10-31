package com.flourish;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.flourish.adapters.MoneyListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class AddExpenses extends FragmentActivity implements OnClickListener
{    private Uri mUri;
private Bitmap mPhoto;
	private ArrayList<String> mActiveExpensesAccountIdList = new ArrayList<String>();
	private ArrayList<String> mExpensesList = new ArrayList<String>();
	
	private MoneyListAdapter mExpensesListAdapter = null;
	private TextView mExpensesAccountNameTv = null;
	private static EditText mExpensesTransactionDateEt = null;
	private EditText mExpensesAmountEt = null;
	private EditText mExpensesDescriptionEt = null;
	private EditText mExpensesReferenceEt=null;
	private Button mBack = null;
	private Button mSave = null;
	private Button mExpensesDateBtn = null;
	//private static DatePicker mExpensesDatePicker = null;
	private String mSessionId = null;
	public String mExpensesUpdateStr = null;
	public String mActiveExpenseStr;
	private static int mYear;
	private static int mMonth;
	private static int mDay;
	private static AppNetwork mAppNetwork = null;
	private ConnectionManager mConnectionManager = null;
	private JSONObject mUpdateMileageRequestObj = null;
	private VehicleListCustomDialog mCustomDialog = null;
	private Button mTakeReceiptPhotoButton = null;
	private ImageView mExpensesCaptureImageView = null;
	private static final int DATE_DIALOG_ID = 1;
	private final int CAMERA_CAPTURE = 1;
//	private final int PICTURE_CROP = 2;
	private Button mDeleteExpensesImageViewButton = null;
	private String encodedImage = null;
	private String mExpensesReceiptStr;
	private JSONObject mUpdateExpensesReceiptObj = null;
	private String journalId = null;
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_expenses);

mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();

		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);

		initializeVariables();
		new GetVehicleIdTask().execute();

		mExpensesDateBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				//showDialog(DATE_DIALOG_ID);
				
				 DialogFragment newFragment = new DatePickerFragment();
				    newFragment.show(getSupportFragmentManager(), "datePicker");

				
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
		mCustomDialog 	= new VehicleListCustomDialog(AddExpenses.this);
		mBack = (Button)findViewById(R.id.add_expenses_back);
		mSave = (Button)findViewById(R.id.expenses_save);
		mExpensesAccountNameTv = (TextView)findViewById(R.id.add_expenses_account_name_tv);
		mExpensesTransactionDateEt = (EditText)findViewById(R.id.add_expenses_transaction_date_et);
		mExpensesDateBtn = (Button)findViewById(R.id.add_expenses_calendar_btn);
		mExpensesDescriptionEt = (EditText)findViewById(R.id.add_expenses_description_et);
		mExpensesAmountEt = (EditText)findViewById(R.id.add_expenses_amount_et);
		mExpensesReferenceEt = (EditText)findViewById(R.id.add_expenses_reference_et);
	//	mExpensesDatePicker = (DatePicker) findViewById(R.id.expenses_dpResult);
		mTakeReceiptPhotoButton  = (Button)findViewById(R.id.expense_capture_button);
		mExpensesCaptureImageView  = (ImageView)findViewById(R.id.expense_imageview);
		mDeleteExpensesImageViewButton  = (Button)findViewById(R.id.delete_expenses_imageview);
		
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mTakeReceiptPhotoButton.setOnClickListener(this);
		mExpensesAccountNameTv.setOnClickListener(this);
		mExpensesTransactionDateEt.setOnClickListener(this);
		
		mDeleteExpensesImageViewButton.setOnClickListener(this);
		
		mDeleteExpensesImageViewButton.setOnClickListener(this);
		mExpensesList.add("Computers");
		mExpensesList.add("Food & Entertainment");
		mExpensesList.add("Office Supplies");
		mCustomDialog.getHeaderTv().setText("Select an Account");
		mExpensesListAdapter = new MoneyListAdapter(AddExpenses.this, R.layout.sales_list_row, mExpensesList);
		mCustomDialog.getListview().setAdapter(mExpensesListAdapter);
		mCustomDialog.getListview().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View listItem, int position, long position1) 
			{
				mActiveExpenseStr = mActiveExpensesAccountIdList.get(position);
				mExpensesAccountNameTv.setText(mExpensesList.get(position));
				mCustomDialog.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.add_expenses_back:
			//getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "Expenses").commit();
			
			//startActivity(new Intent(AddExpenses.this, FlourishHomeActivity.class));
		
			finish();
			break;

		case R.id.expenses_save:
			if(mExpensesAccountNameTv.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesTransactionDateEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesDescriptionEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesReferenceEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mExpensesAmountEt.getText().toString().trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_enter_expenses_details_to_be_added));
			}
			else if(mAppNetwork.isNetworkAvailable(AddExpenses.this))
			{
				new GetUpdateexpensesTask().execute();
			}   
			else
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_no_network));
			}
			break;

		case R.id.add_expenses_calendar_btn:
			 DialogFragment newFragment = new DatePickerFragment();
			   newFragment.show(getSupportFragmentManager(), "datePicker");

			
			break;
			
		case R.id.add_expenses_transaction_date_et:
			//showDialog(DATE_DIALOG_ID);
			 DialogFragment newFragment2 = new DatePickerFragment();
			   newFragment2.show(getSupportFragmentManager(), "datePicker");
			break;

		case R.id.add_expenses_account_name_tv:
			mCustomDialog.show();
			break;
			  
		case R.id.expense_capture_button:
			
			
			
			
			captureExpenses();
			break;
			
		case R.id.delete_expenses_imageview:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenses.this);
			builder.setMessage("Do you want to remove ?");
			builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mExpensesCaptureImageView.setImageResource(0);
					mExpensesCaptureImageView.setVisibility(View.GONE);
					mDeleteExpensesImageViewButton.setVisibility(View.GONE);
					mTakeReceiptPhotoButton.setVisibility(View.VISIBLE);
				}
			});
			
			builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create();
			builder.show();
			break;

		default:
			break;
		}
	}
	
	/**
	 * Captures the expenses using camera and uploads to server
	 */
	private void captureExpenses() {
		
		try {
			
	
		    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(captureIntent, CAMERA_CAPTURE);
		
			/*
			Intent captureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
	        File f = new File(Environment.getExternalStorageDirectory(),  "photo.jpg");
	        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	        mUri = Uri.fromFile(f);
	        startActivityForResult(captureIntent, CAMERA_CAPTURE);*/
			
			
		}catch(ActivityNotFoundException exception){
		    Toast.makeText(AddExpenses.this, "Your device doesn't support any Image Capturing", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK){
			if(requestCode == CAMERA_CAPTURE){
				
		
		
		
              /*  getContentResolver().notifyChange(mUri, null);
                ContentResolver cr = getContentResolver();
                try {
                    mPhoto = android.provider.MediaStore.Images.Media.getBitmap(cr, mUri);
                   
                    if (mPhoto!=null) {
        				Matrix matrix = new Matrix();
        	            matrix.postRotate(90);
        	            
        	            Log.v("TAT", "width=="+mPhoto.getWidth()/2);
        	            Log.v("TAT", "height=="+mPhoto.getHeight()/2);
        	            
        	            mPhoto = Bitmap.createBitmap(mPhoto , 0, 0, mPhoto.getWidth(), mPhoto.getHeight(), matrix, true);
        	            mExpensesCaptureImageView.setVisibility(View.VISIBLE);
        				mDeleteExpensesImageViewButton.setVisibility(View.VISIBLE);
        				mTakeReceiptPhotoButton.setVisibility(View.GONE);
        	            mExpensesCaptureImageView.setImageBitmap(mPhoto);
        	            bitMapToString(mPhoto);
        	            
        			}
                    
                    
                    
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            */
		
		
       
				
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				mExpensesCaptureImageView.setVisibility(View.VISIBLE);
				mDeleteExpensesImageViewButton.setVisibility(View.VISIBLE);


				
				
				
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = dm.widthPixels; 
			
				
				if (width<=239) {
					Bitmap resized = Bitmap.createScaledBitmap(bmp, 90, 100, true);
					mExpensesCaptureImageView.setImageBitmap(resized);
					
				}
				
				
				if (width<=320) {
					Bitmap resized = Bitmap.createScaledBitmap(bmp, 300, 400, true);
					mExpensesCaptureImageView.setImageBitmap(resized);
					
				}
				
				
				if (width>=480) {
					Bitmap resized = Bitmap.createScaledBitmap(bmp, 500, 700, true);
					mExpensesCaptureImageView.setImageBitmap(resized);
					
					
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				bitMapToString(bmp);
				
				mTakeReceiptPhotoButton.setVisibility(View.GONE);
				
				//performCrop();
			
				
			
			}/*else if(requestCode == PICTURE_CROP){
				
				Bundle bundleExtras = data.getExtras();
				Bitmap expensesPicture = bundleExtras.getParcelable("data");
				mExpensesCaptureImageView.setVisibility(View.VISIBLE);
				mExpensesCaptureImageView.setImageBitmap(expensesPicture);
			}*/
		
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public String bitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		
	
		byte[] b = baos.toByteArray();
		String temp = null;
		try {
			System.gc();
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			b = baos.toByteArray();
			temp = Base64.encodeToString(b, Base64.DEFAULT);
			Log.e("EWN", "Out of memory error catched");
		}
		return temp;
	}
	/**
	 * Helper Class to perform cropping of an image
	 
	private void performCrop() {
		
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			cropIntent.setDataAndType(mExpensesPictureURI, "image/*");
			cropIntent.putExtra("crop", "true");
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			cropIntent.putExtra("return-data", true);
			startActivityForResult(cropIntent, PICTURE_CROP);
		} catch (ActivityNotFoundException exception) {
			Toast.makeText(AddExpenses.this,"Your device doesn't support any Image Capturing", Toast.LENGTH_LONG).show();
		}
	}
	*/
	

	/**
	 * Get the vehicle id AsyncTask
	 *
	 */

	private class GetVehicleIdTask extends AsyncTask<Void, Void, Void>
	{
	
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(AddExpenses.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_expenses;
//			mActiveExpenseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_expenses)+"/"+mSessionId);
			
			mActiveExpenseStr = mConnectionManager.setUpHttpGet(mTaskUrl+"/"+mSessionId);
			getExpenseAccountId();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(AddExpenses.this);
			
			if (mActiveExpenseStr!=null) {
		
			
			 if(mActiveExpenseStr.contains("Login Key Not Valid"))
			{
				getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(AddExpenses.this, LoginScreen.class));
			}
			else if(mActiveExpenseStr.contains("Bad Parameters"))
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
			}
			super.onPostExecute(result);
			
			}
		}
	}

	/**
	 * Save Money Expenses AsyncTask
	 *
	 */
	private class GetUpdateexpensesTask extends AsyncTask<Void, Void, Void>
	{
		
		@Override
		protected void onPreExecute()
		{
			
			FlourishProgressDialog.ShowProgressDialog(AddExpenses.this);
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			putMileageData();
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_expenses;
			mExpensesUpdateStr = mConnectionManager.setUpHttpPut(mTaskUrl+"/"+mSessionId, mUpdateMileageRequestObj.toString());
		//	mExpensesUpdateStr = mConnectionManager.setUpHttpPut(getResources().getString(R.string.update_expenses)+"/"+mSessionId, mUpdateMileageRequestObj.toString());
			if (mExpensesUpdateStr!=null) {
				getExpenseData();
			}
		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			
			FlourishProgressDialog.Dismiss(AddExpenses.this);
			//mExpensesUpdateStr
		
			if (mExpensesUpdateStr!=null) {
				new UpdateExpensesPhotoTask().execute();
				
				
			}
			
				//
				
			/*	SharedPreferences.Editor editor=mSharedPreferences.edit();
				  editor.putString("FragmentScreen", "Money");
				  editor.commit();*/
				  

				 // setResult(0);
				 // finish();
				  
				  
				/* Intent mIntent = new Intent(AddExpenses.this, FlourishHomeActivity.class);
			
				startActivity(mIntent);*/
				
				
				
				
		
			super.onPostExecute(result);
		}
	}

	private class UpdateExpensesPhotoTask extends AsyncTask<Void, Void, Void>
	{
	

		@Override
		protected void onPreExecute() 
		{
			
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String mTaskUrl_update_expenses_photo=  mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.update_expenses_photo;
			putExpensesReceipt();
			mExpensesReceiptStr = mConnectionManager.setUpHttpPut(mTaskUrl_update_expenses_photo+mSessionId, mUpdateExpensesReceiptObj.toString());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
		
				
			if (mExpensesReceiptStr!=null) {
				
		
				
				if(mExpensesReceiptStr.contains("Account not found"))
				{
					mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_account_not_found));
				}
				else
				{
					//mAppNetwork.getAlertDialog(AddExpenses.this, getString(R.string.alert_dialog_expenses_added_success));
				
					finish();
				}
		
			super.onPostExecute(result);
			}
		}
	}
	
	private void putMileageData() 
	{
		try 
		{
			mUpdateMileageRequestObj = new JSONObject();
			mUpdateMileageRequestObj.put("AccountId", mActiveExpenseStr);
			mUpdateMileageRequestObj.put("TransactionDate", mExpensesTransactionDateEt.getText().toString());
			mUpdateMileageRequestObj.put("Description", mExpensesDescriptionEt.getText().toString());
			mUpdateMileageRequestObj.put("Reference", mExpensesReferenceEt.getText().toString());
			mUpdateMileageRequestObj.put("Amount", mExpensesAmountEt.getText().toString());
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	public void getExpenseData() {
		
		try {
			JSONObject mAddExpensesResponse = new JSONObject(mExpensesUpdateStr);
			JSONObject mAddExpensesResponseData = mAddExpensesResponse.getJSONObject("data");
			journalId  = mAddExpensesResponseData.getString("Id").toString();
		} catch (JSONException e) {
			Log.e("TAG","Exception in Add Expenses getExpensesData : "+e.getMessage()+"  mExpensesUpdateStr="+mExpensesUpdateStr);
			e.printStackTrace();
		}
	}

	/**
	 * Adds the receipt image along with other details for uploading to server
	 */
	public void putExpensesReceipt() {
		
		String todaysDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(System.currentTimeMillis()));
		
		try {
			mUpdateExpensesReceiptObj  = new JSONObject();
			mUpdateExpensesReceiptObj.put("JournalId", journalId);
			mUpdateExpensesReceiptObj.put("FileExtension","jpeg");
			mUpdateExpensesReceiptObj.put("Caption", "Captured on "+todaysDate+" by mobile app");
			mUpdateExpensesReceiptObj.put("EncodedImage", encodedImage);
		} catch (JSONException e) {
			Log.e("TAG","Exception in Add Expenses putExpensesReceipt : "+e.getMessage());
		}
	}

	/**
	 * Method for parsing the Server response for adding expenses.
	 */
	public void getExpenseAccountId() 
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mActiveExpenseStr);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mActiveExpensesAccountIdList.add(mJsonArray.getJSONObject(i).getString("Id"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/*private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	{
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) 
		{
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;
			if(comparingDate().equalsIgnoreCase("valid"))
			{
				mExpensesTransactionDateEt.setText(new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear));

				mExpensesDatePicker.init(mYear, mMonth, mDay, null);
				
			}
			else if(comparingDate().equalsIgnoreCase("invalid"))
			{
				mAppNetwork.getAlertDialog(AddExpenses.this, getResources().getString(R.string.alert_dialog_enter_proper_date));
			}
		}
	};
*/
	
	
	
	public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
final Calendar c = Calendar.getInstance();
int year = c.get(Calendar.YEAR);
int month = c.get(Calendar.MONTH);
int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
return new DatePickerDialog(getActivity(), this, year, month, day);
}

public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) 
{
	mYear = selectedYear;
	mMonth = selectedMonth;
	mDay = selectedDay;
	if(comparingDate().equalsIgnoreCase("valid"))
	{
		mExpensesTransactionDateEt.setText(new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear));

		//mExpensesDatePicker.init(mYear, mMonth, mDay, null);
	
	}
	else if(comparingDate().equalsIgnoreCase("invalid"))
	{
		mAppNetwork.getAlertDialog(getActivity(), getResources().getString(R.string.alert_dialog_enter_proper_date));
	}
}


}
	
	
	
	
	/**
	 * Method for comparing two dates
	 * @return String (Valid/Invalid)
	 */
	private static String comparingDate()
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


	/*protected Dialog onCreateDialog(int id) 
	{
		switch (id)
		{
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay);
		}
		return null;
	}*/
}