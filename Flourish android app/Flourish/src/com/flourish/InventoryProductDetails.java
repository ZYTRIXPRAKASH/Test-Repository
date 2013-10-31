package com.flourish;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class InventoryProductDetails extends BaseTimeOutActivity
{
	private Button mBackBtn = null;
	private TextView mProductNameTv = null;
	private TextView mOnHandValueTv = null;
	private TextView mOnHandQuantityTv = null;
	private TextView mNumberTv = null;
	private TextView mWholeSaleTv = null;
	private TextView mRetailTv = null;
	private TextView mTargetTv = null;
	
	public ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	public String mInventoryProductsResponseStr = null;
	private String mSessionIdStr = null;
	private String mProductIdStr = null;
	private String mProductNameStr = null;
	private String mOnHandValueStr = null;
	private String mOnHandQuantityStr = null;
	private String mNumberStr = null;
	private String mWholeSaleStr = null;
	private String mRetailStr = null;
	private String mTargetStr = null;
	FlourishBaseApplication mFlourishBaseApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inventory_details);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		onSessionStarted(true);
		

		mSessionIdStr = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		mProductIdStr = getIntent().getStringExtra("productId");
		Log.v("response", "==mProductIdStr=="+mProductIdStr);
		initializeVariables();

		if(mAppNetwork.isNetworkAvailable(InventoryProductDetails.this))
		{
			new GetInventoryProductsTask().execute();
		}
		else
		{
			mAppNetwork.getAlertDialog(InventoryProductDetails.this, getString(R.string.alert_dialog_no_network));
		}
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables()
	{
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mBackBtn = (Button)findViewById(R.id.inventory_product_details_back);
		mProductNameTv = (TextView)findViewById(R.id.inventory_product_details_name_tv);
		mOnHandValueTv = (TextView)findViewById(R.id.inventory_product_details_on_hand_value);
		mOnHandQuantityTv = (TextView)findViewById(R.id.inventory_product_details_on_hand_quantity_value);
		mNumberTv = (TextView)findViewById(R.id.inventory_product_details_number_val);
		mWholeSaleTv = (TextView)findViewById(R.id.inventory_product_details_wholesale_val);
		mRetailTv = (TextView)findViewById(R.id.inventory_product_details_retail_val);
		mTargetTv = (TextView)findViewById(R.id.inventory_product_details_target_val);

		mBackBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				finish();
			}
		});
	}

	/**
	 * AsynTask for making Server request and response for getting Inventory Product details
	 */
	public class GetInventoryProductsTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(InventoryProductDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			String mTaskUrl_inventory_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.inventory_url;
			mInventoryProductsResponseStr = mConnectionManager.setUpHttpGet(mTaskUrl_inventory_url+mSessionIdStr+"/"+mProductIdStr);
			Log.v("response", "==mSalesInvoiceItemsResponse=="+mInventoryProductsResponseStr);
			getInventoryProductData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(InventoryProductDetails.this);
			{
				if(mInventoryProductsResponseStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(InventoryProductDetails.this, LoginScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}

				else if(mInventoryProductsResponseStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(InventoryProductDetails.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					setInventoryProductData();
				}
			}
			
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for parsing Server response for getting Inventory Product details
	 */
	public void getInventoryProductData()
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mInventoryProductsResponseStr);
			JSONObject mJsonArray;
			mJsonArray = mJsonObj.getJSONObject("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mProductIdStr= mJsonArray.getString("ProductId");
				mProductNameStr = mJsonArray.getString("Name");
				mOnHandValueStr = mJsonArray.getString("OnHandCost");
				mOnHandQuantityStr = mJsonArray.getString("OnHand");
				mNumberStr = mJsonArray.getString("Number");
				mWholeSaleStr = mJsonArray.getString("MostRecentWholesale");
				mRetailStr = mJsonArray.getString("MostRecentPrice");
				mTargetStr = mJsonArray.getString("TargetLevel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for setting the parsed response to text fields
	 */
	private void setInventoryProductData()
	{
		if(mProductNameStr != null)
		{
			mProductNameTv.setText(mProductNameStr);
		}
		else
		{
			mProductNameTv.setText("");
		}
		
		if(mOnHandValueStr != null && !(mOnHandQuantityStr.equalsIgnoreCase("null")) && !(mRetailStr.equalsIgnoreCase("null")))
		{
			float mOnHandValue = (Float.parseFloat(mOnHandQuantityStr) * Float.parseFloat(mRetailStr));
			String mOnHandValueStr = String.valueOf(mOnHandValue);
			if(mOnHandValueStr.contains("-"))
			{
				mOnHandValueStr = mOnHandValueStr.substring(1, mOnHandValueStr.length());
			}
			mOnHandValueTv.setText("$("+mOnHandValueStr+"0)");
		}
		else
		{
			mOnHandValueTv.setText("$0.00");
		}
		if(mOnHandQuantityStr != null)
		{
			if(mOnHandQuantityStr.contains("-"))
			{
				mOnHandQuantityStr = mOnHandQuantityStr.substring(1, mOnHandQuantityStr.length());
				mOnHandQuantityTv.setText("("+mOnHandQuantityStr+")");
			}
			if(mOnHandQuantityStr.equalsIgnoreCase("null"))
			{
				mOnHandQuantityTv.setText("(0)");
			}
		}
		else
		{
			mOnHandQuantityTv.setText("0");
		}
		if(mNumberStr != null)
		{
			mNumberTv.setText(mNumberStr);
		}
		else
		{
			mNumberTv.setText("0");
		}
		if(mWholeSaleStr != null)
		{
			mWholeSaleTv.setText("$"+mWholeSaleStr+"0");
		}
		else
		{
			mWholeSaleTv.setText("0");
		}
		if(mRetailStr != null)
		{
			mRetailTv.setText("$"+mRetailStr+"0");
		}
		else
		{
			mRetailTv.setText("0");
		}
		if(mTargetStr != null)
		{
			if(mTargetStr.equalsIgnoreCase("null"))
			{
				mTargetTv.setText("0");
			}
			else
			{
				mTargetTv.setText(mTargetStr);
			}
		}
		else
		{
			mTargetTv.setText("0");
		}
	}
}