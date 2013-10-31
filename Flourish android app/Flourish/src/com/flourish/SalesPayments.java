/**
 * 
 */
package com.flourish;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

/**
 * @author aditya
 *
 */
public class SalesPayments extends Activity{
	
	private String mSessionId  = null;
	private String mInvoiceId = null;
	private String mSalesPaymentItemsResponse = null;

	private AppNetwork mAppNetwork = null;
	private ConnectionManager mConnectionManager = null;
	
	private RelativeLayout mTopBarSalesPayment = null;
	FlourishBaseApplication mFlourishBaseApplication;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sales_topbar);

mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		
		mTopBarSalesPayment  = (RelativeLayout)findViewById(R.id.sales_top_bar_rl);
		mTopBarSalesPayment.setBackgroundResource(R.drawable.top_grey_second_selected);
		
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mInvoiceId = getIntent().getStringExtra("invoiceId");
		
		initializeVariables();
		
		if(mAppNetwork.isNetworkAvailable(SalesPayments.this))
		{
			new GetSalesPaymentItems().execute();
			setSalesPaymentItemsData();
		}
		else
		{
			mAppNetwork.getAlertDialog(SalesPayments.this, getString(R.string.alert_dialog_no_network));
		}
	}

	private void setSalesPaymentItemsData() {
		
	}

	private void initializeVariables() {
		
		
	}
	
	/**
	 * AsynTask for making Server request and response for getting Sales Payment Items
	 */
	private class GetSalesPaymentItems extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(SalesPayments.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{

String mTaskUrl_get_sales_invoice_items_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_sales_invoice_items_url;
			mSalesPaymentItemsResponse  = mConnectionManager.setUpHttpGet(mTaskUrl_get_sales_invoice_items_url+mSessionId+"/"+mInvoiceId);
			Log.v("TAG", "==mSalesInvoiceItemsResponse=="+mSalesPaymentItemsResponse);
			getSalesPaymentItems();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(SalesPayments.this);
				if(mSalesPaymentItemsResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(SalesPayments.this, LoginScreen.class));
				}
				else if(mSalesPaymentItemsResponse.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(SalesPayments.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else 
				{
					setSalesPaymentLineItems();

				}
			
			super.onPostExecute(result);
		}
	}

	public void getSalesPaymentItems() {
		// TODO Auto-generated method stub
		
	}

	public void setSalesPaymentLineItems() {
		// TODO Auto-generated method stub
		
	}

}
