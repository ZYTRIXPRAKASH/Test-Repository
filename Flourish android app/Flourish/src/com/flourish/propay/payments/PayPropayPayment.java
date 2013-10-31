/**
 * 
 */
package com.flourish.propay.payments;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.flourish.AddPayments;
import com.flourish.ConnectionManager;
import com.flourish.CreateInvoice;
import com.flourish.CustomTextWatcher;
import com.flourish.FlourishHomeActivity;
import com.flourish.R;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PayPropayPayment extends Activity  {

	
	TextView textview=null;
	Intent mIntent=null;
	String mInvoiceId=null;
	String mCardExperiDate=null;
	String m16DigitsCardNumber=null;
	String mExpMonth=null;
	String mExpYear=null;
	String mLast4Digits=null;
	String mInvoiceNumber=null;
	ConnectionManager mConnectionManager=null;
	FlourishBaseApplication mFlourishBaseApplication=null;
	AppNetwork mAppNetwork=null;
	String mTaskUrl=null;
	JSONObject mCompleteJsonObjectBody=null;
	String mSessionId=null;
	String mResponse=null;
	String  CCN;
	Button mProcessPayment_btn=null;
	Button mActivityBack=null;
	
	TextView mPaymentType_tv=null;
	EditText mPaymentDate_et=null;
	EditText mPaymentCardNumber_et=null;
	EditText mPaymentCardExperiDate_et=null;
	EditText mZipCode_et=null;
	EditText mAmount_et=null;
	TextView mActivityTopbarTv=null;
	SharedPreferences mSharedPreferences=null;
	
	
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.pay_propay_payment);
	mAppNetwork=new AppNetwork();
	mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
	mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
	mIntent=getIntent();
	mInvoiceId=mIntent.getStringExtra("invoiceId");
	m16DigitsCardNumber=mIntent.getStringExtra("cardNumber");
	mLast4Digits=mIntent.getStringExtra("cardLast4Digits");
	mInvoiceNumber=mIntent.getStringExtra("invoiceNumber");
	
	mActivityTopbarTv=(TextView)findViewById(R.id.activity_topbar_tv);
	mActivityTopbarTv.setText(getResources().getString(R.string.invoice)+" #"+mInvoiceNumber);
	
	mPaymentType_tv=(TextView)findViewById(R.id.add_payment_type_tv);
	mPaymentDate_et=(EditText)findViewById(R.id.add_sales_payment_date_et);
	presentDate();
	mPaymentCardNumber_et=(EditText)findViewById(R.id.add_payment_Propay_CardNumber_et);
	mPaymentCardExperiDate_et=(EditText)findViewById(R.id.add_payment_Propay_CardExperiDate_et);
	mZipCode_et=(EditText)findViewById(R.id.add_payment_zipcode_et);
	mAmount_et=(EditText)findViewById(R.id.add_payment_amount_et);
	
	
	mProcessPayment_btn=(Button)findViewById(R.id.processPayment_btn);
	mActivityBack=(Button)findViewById(R.id.activity_topBar_left_btn);
	
	
	
	mProcessPayment_btn.setOnClickListener(buttonClickListner);
	mActivityBack.setOnClickListener(buttonClickListner);

	Log.v("TAG", "m16DigitsCardNumber====="+m16DigitsCardNumber);
	
// m16DigitsCardNumber=====;
//	4033980007009570=16101260000000000000?

	
	if(m16DigitsCardNumber!=null)
	{
	String  m16DigitsCardNumberArray[]=m16DigitsCardNumber.split(";");
	String mPart1=m16DigitsCardNumberArray[1];
	Log.v("TAG", "mPart1====="+mPart1);
    String mRequireCcn[]=mPart1.split("=");
    Log.v("TAG", "CCN NUMBER"+mRequireCcn[0]);
    CCN=mRequireCcn[0];

  //  mPaymentCardNumber_et.setText(CCN);
    mPaymentCardNumber_et.setText("************"+mLast4Digits);
	
	}
	
	mCardExperiDate=mIntent.getStringExtra("cardExperiDate");
	
	if (mCardExperiDate!=null&&mCardExperiDate.length()>=4) {
		
	
		Log.v("TAG", "mCardExperiDate=="+mCardExperiDate);
	mExpYear=mCardExperiDate.substring(0, 2);
	mExpMonth=mCardExperiDate.substring(2, 4);
	
	Log.v("TAG", "mInvoice id in PayPropayPayment=="+mInvoiceId);
	Log.v("TAG", "mExpYear=="+mExpYear);
	Log.v("TAG", "mExpMonth=="+mExpMonth);
	
	
	mPaymentCardExperiDate_et.setText(mExpMonth+"/"+mExpYear);
	mPaymentCardExperiDate_et.setEnabled(false);
	
	}

	
	

	//textview.setText(mInvoiceId);
	
	mSessionId = getSharedPreferences("LoginInfo", 0).getString(
			"sessionId", "nothing");
	Log.v("TAG", "Session ID:" + mSessionId);
	
	mPaymentCardExperiDate_et.addTextChangedListener(new CustomTextWatcher(mPaymentCardExperiDate_et));
	
	/*
	mPaymentCardExperiDate_et.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		
			
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
			if (s.length()>2) {
				mPaymentCardExperiDate_et.append(s+"/");
				
			}
			else
			{
				
			}
			
			
		}
	});
*/
}
	

 class Invoice_Payment_Propay extends AsyncTask<Void, Void, Void>
 {
	 @Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		 
		 FlourishProgressDialog.ShowProgressDialog(PayPropayPayment.this);
		super.onPreExecute();
		
		
	}
	 
	 
	 
	 @Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		 
		 mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.invoice_Payment_Propay;
		 propayPaymentJsonBody();
		 mConnectionManager=new ConnectionManager();
		mResponse= mConnectionManager.setUpHttpPut(mTaskUrl+mSessionId, mCompleteJsonObjectBody.toString());
		 
		 Log.v("TAG", "mResponse==="+mResponse);
		 
		 
		return null;
	}
	 
	 
	 
	 @Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		FlourishProgressDialog.Dismiss(PayPropayPayment.this);
		
		if (mResponse!=null) {
			
			FlourishProgressDialog.Dismiss(PayPropayPayment.this);
	
			
			// Toast.makeText(getApplicationContext(), "propay success", 500).show();
			
			SharedPreferences.Editor editor=mSharedPreferences.edit();
			  editor.putString("FragmentScreen", "Sales");
			 
			  //save payment invoice in shared 
			  
			  editor.putString("PAYMENT_INVOICE", mInvoiceId);
			  editor.commit();

			 Intent mIntentForDashboard = new Intent(PayPropayPayment.this, FlourishHomeActivity.class);
			 startActivity(mIntentForDashboard);
			 finish();
			
			try {
				
			
			
			JSONObject mCompleteJsonOnject=new JSONObject(mResponse);
			JSONObject AuthResponseObject=mCompleteJsonOnject.getJSONObject("AuthResponse");
			
			 String CallSuccess=AuthResponseObject.getString("CallSuccess");
			 if (CallSuccess.equalsIgnoreCase("True")) {
				 
			
				 
				/* Toast.makeText(getApplicationContext(), "propay success", 500).show();
				 SharedPreferences.Editor editor=mSharedPreferences.edit();
				  editor.putString("FragmentScreen", "Dashboard");
				  editor.commit();

				 Intent mIntentForDashboard = new Intent(PayPropayPayment.this, FlourishHomeActivity.class);
				 startActivity(mIntentForDashboard);
				 finish();
				*/
			}
			 else{
				 
				 Toast.makeText(getApplicationContext(), "Faied to pay payment", 500).show();
				 finish();
					
				 
			 }
			
			
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
			
		}
		
		
		
		
		
		
		
	}
	 
	 
	 
	 
	 
	 
	 
 }
	

	
	
	public void propayPaymentJsonBody()
	{
		try {
			
		
		mCompleteJsonObjectBody=new JSONObject();
		mCompleteJsonObjectBody.put("InvoiceId", mInvoiceId);
		mCompleteJsonObjectBody.put("Amount",mAmount_et.getText().toString().trim());
		mCompleteJsonObjectBody.put("Reference", "hi.....somthing");
		mCompleteJsonObjectBody.put("CreditCardNumber",CCN);
		mCompleteJsonObjectBody.put("ExpMonth", mExpMonth);	
		mCompleteJsonObjectBody.put("ExpYear", mExpYear);
	
		Log.v("TAG", "mCompleteJsonObjectBody=="+mCompleteJsonObjectBody);
		
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("TAG", "Exception=="+e.toString());
			
		}
		
		
	}
	
	
	
	View.OnClickListener buttonClickListner=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			switch (v.getId()) {
			case R.id.activity_topBar_left_btn:
				
				finish();
				
				break;
    case R.id.processPayment_btn:
    	
    	if (mZipCode_et.getText().length()==0) {
    		Toast.makeText(getApplicationContext(), "Please enter zip code", 500).show();
		}
    	else if (mAmount_et.getText().length()==0) {
			
    		Toast.makeText(getApplicationContext(), "Please enter amount", 500).show();
		}
			
    	
    	else if (mAppNetwork.isNetworkAvailable(PayPropayPayment.this)) {

    		new Invoice_Payment_Propay().execute();

		} else {

			mAppNetwork.getAlertDialog(PayPropayPayment.this,
					getString(R.string.alert_dialog_no_network));

		}
    	
    	
    	
    	
    	
				break;

			default:
				break;
			}
			
		}
	};
	
	
	
	public void presentDate() {

		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		mPaymentDate_et.setText(formattedDate);

	}

}
