package com.flourish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.adapters.ProductData;
import com.flourish.adapters.ProductsListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class ChooseProduct extends BaseTimeOutActivity implements OnItemClickListener,OnClickListener,TextWatcher
{
	private ListView mProductsListView = null;
	private EditText mSearchProductEt = null;
	private Button mCancelBtn = null;

	private ProductsListAdapter mProductsListAdapter = null;
	public String mGetProductListResponseStr = null;
	private String mSessionIdStr = null;
	private String mSearchStr = null;
	public ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private ProductData mProductData;
	private List<ProductData> mAllProductList;
	FlourishBaseApplication mFlourishBaseApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_product);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		mAllProductList=new ArrayList<ProductData>();
		onSessionStarted(true);

		mSessionIdStr = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		initializeVariables();
		mCancelBtn=(Button)findViewById(R.id.choose_products_cancel);
		mCancelBtn.setOnClickListener(this);
		mSearchProductEt.addTextChangedListener(this);
		
/*		mSearchProductEt.setOnEditorActionListener(
		        new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
		        	
		        	
		        	
		        	
		        	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    		imm.hideSoftInputFromWindow(mSearchProductEt.getWindowToken(), 0);
		    	    mAllProductList=new ArrayList<ProductData>();
		    		mSearchStr = mSearchProductEt.getText().toString().trim();

		    		if(mAppNetwork.isNetworkAvailable(ChooseProduct.this)) 
		    		{
		    			new GetProductsListTask().execute();
		    		}
		    		else 
		    		{
		    			mAppNetwork.getAlertDialog(ChooseProduct.this, getResources().getString(R.string.alert_dialog_no_network));
		    		}
		        	
		
		          
		            return true;
		        }
		        return false;
		    }
		});*/
		
		
		
		
	}
	/**
	 * Method for initializing variables
	 */
	private void initializeVariables()
	{
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mProductsListView = (ListView) findViewById(R.id.choose_product_listview);
		mSearchProductEt = (EditText)findViewById(R.id.input_search_query);
		mProductsListView.setOnItemClickListener(this);
	}

	
	
	

	
	
	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		switch (arg0.getId()) 
		{
		case R.id.choose_product_listview:
			getSharedPreferences("ChooseProduct", 0).edit().putString("productName", mAllProductList.get(position).Name).commit();
			getSharedPreferences("ChooseProduct", 0).edit().putString("productId", mAllProductList.get(position).ProductId).commit();
			getSharedPreferences("ChooseProduct", 0).edit().putString("productQuantity", mAllProductList.get(position).OnHand).commit();
			finish();
			break;

		default:
			break;
		}
	}
	/**
	 * AsynTask for making Server request and response for getting products
	 */
	private class GetProductsListTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			//FlourishProgressDialog.ShowProgressDialog(ChooseProduct.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_product_list_url;
			mGetProductListResponseStr = mConnectionManager.setUpHttpGet(mTaskUrl+mSessionIdStr+"/"+mSearchStr);
//			mGetProductListResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_product_list_url)+mSessionIdStr+"/"+mSearchStr);
			Log.v("response", "==mGetProductListResponse=="+mGetProductListResponseStr);
			getProductsList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			//FlourishProgressDialog.Dismiss(ChooseProduct.this);
				if(mGetProductListResponseStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(ChooseProduct.this, LoginScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}
				else if(mGetProductListResponseStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(ChooseProduct.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mProductsListAdapter = new ProductsListAdapter(ChooseProduct.this, mAllProductList);
					mProductsListView.setAdapter(mProductsListAdapter);
				}
			
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for parsing the Server response for getting products
	 */
	public void getProductsList()
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mGetProductListResponseStr);
			JSONArray mJsonArray;
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				ProductData mProductData=new ProductData();
				
				mProductData.ProductId=mJsonArray.getJSONObject(i).getString("ProductId");
				mProductData.Name=mJsonArray.getJSONObject(i).getString("Name");
				mProductData.OnHand=mJsonArray.getJSONObject(i).getString("OnHand");
				mProductData.MostRecentPrice=mJsonArray.getJSONObject(i).getString("MostRecentWholesale");
				mAllProductList.add(mProductData);
				
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.hideSoftInputFromWindow(mSearchProductEt.getWindowToken(), 0);
	    mAllProductList=new ArrayList<ProductData>();
		mSearchStr = s.toString();

		if(mAppNetwork.isNetworkAvailable(ChooseProduct.this)) 
		{
			new GetProductsListTask().execute();
		}
		else 
		{
			mAppNetwork.getAlertDialog(ChooseProduct.this, getResources().getString(R.string.alert_dialog_no_network));
		}
	
	
	
	}

	@Override
	
	
	
	
	
	
	
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.choose_products_cancel:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}