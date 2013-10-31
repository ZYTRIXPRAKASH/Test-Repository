package com.flourish;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.flourish.adapters.InventoryProductListBaseAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.utils.InVentoryData;

public class InventoryFragment extends Fragment {
	
	
	Fragment mFragment;
	                                    
	AppNetwork mAppNetwork;

	InVentoryData mInVentoryData;              
	List<InVentoryData> mAllInventoryList;
	private boolean mInventorySearch = false;
	private String mGetProductListResponseStr = null;
	private String mSearchStr = "a";
	ConnectionManager mConnectionManager;
	 ArrayList<String> mProductIdList ;
	 String mSessionId;
	 ListView mInventoryProductsListView;
      InventoryProductListBaseAdapter mInventoryProductListBaseAdapter;
      FlourishBaseApplication mFlourishBaseApplication;
      String mInventoryProductListTaskUrl;
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
//		mFlourishBaseApplication=(FlourishBaseApplication)getActivity().getApplicationContext();
//		
	
		Log.v("TAG", "session id in Inventory Fragment "+mSessionId);
		
		//new InventoryAsyncTask().execute();
		
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mAppNetwork=new AppNetwork();
		mAllInventoryList=new ArrayList<InVentoryData>();
		
		mFlourishBaseApplication=(FlourishBaseApplication)getActivity().getApplicationContext();
		Log.v("TAG", "baseUrl "+mFlourishBaseApplication.mFlurishBaseUrl);
		
		mSessionId = getActivity().getSharedPreferences("LoginInfo", 0).getString(
				"sessionId", "nothing");
		
		
		
		View mView;

		mView=inflater.inflate(R.layout.inventory_fragment, container, false);
		
		mInventoryProductsListView=(ListView)mView.findViewById(R.id.favourites_list);
		if (mAppNetwork.isNetworkAvailable(getActivity())) 
		{
			new InventoryAsyncTask().execute();
		}
		else 
		{
			mAppNetwork.getAlertDialog(getActivity(), getResources().getString(R.string.alert_dialog_no_network));
		}
	
		mInventoryProductsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				
				
				startActivity(new Intent(getActivity(), InventoryDetails.class)
				.putExtra("OnHandCost", mAllInventoryList.get(position).OnHandCost)
				.putExtra("OnHandQuantity", mAllInventoryList.get(position).OnHand)
				.putExtra("Wholesale", mAllInventoryList.get(position).MostRecentWholesale)
				.putExtra("Retail", mAllInventoryList.get(position).MostRecentPrice)
				.putExtra("Target", mAllInventoryList.get(position).TargetLevel)
				.putExtra("Number", mAllInventoryList.get(position).Number)
						
						);
				
				
				
				
				
			}
		});
		
		
		
		
		
		return mView ;
		
		
		
	
		
		
		
		
		
	}
	
	
	
	
	public class InventoryAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{	FlourishProgressDialog.ShowProgressDialog(getActivity());
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			/*mProductIdList = new ArrayList<String>();
			mProductNameList = new ArrayList<String>();
			mOnHandValueList = new ArrayList<String>();
			mOnHandQuantityList = new ArrayList<String>();
			mNumberList = new ArrayList<String>();
			mWholeSaleList = new ArrayList<String>();
			mRetailList = new ArrayList<String>();
			mTargetList = new ArrayList<String>();
			*/
			mConnectionManager=new ConnectionManager();
			
			mInventoryProductListTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.inventory_product_list_url_Get;
			//mGetProductListResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_product_list_url)+mSessionId+"/"+mSearchStr);
			mGetProductListResponseStr = mConnectionManager.setUpHttpGet(mInventoryProductListTaskUrl+mSessionId+"/"+mSearchStr);
			getProductsList();
			
		
			if(mInventorySearch)
			{
				mProductIdList = new ArrayList<String>();
				
				mGetProductListResponseStr = mConnectionManager.setUpHttpGet(mInventoryProductListTaskUrl+mSessionId+"/"+mSearchStr);
				
				Log.v("TAG", "==mGetProductListResponse=="+mGetProductListResponseStr);
				getProductsList();
				mInventorySearch = false;
			}
			else
			{
				getInventoryProductsData();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(getActivity());
			{
				
				

				mInventoryProductListBaseAdapter=new InventoryProductListBaseAdapter(getActivity(),mAllInventoryList);
				
				mInventoryProductsListView.setAdapter(mInventoryProductListBaseAdapter);
				
				
				
				if(mInventorySearch && mGetProductListResponseStr.contains("Login Key Not Valid"))
				{
					getActivity().getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(getActivity(), LoginScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					getActivity().finish();
				}
				else if(mInventorySearch && mGetProductListResponseStr.contains("Bad Parameters"))
				{
					mAppNetwork.getAlertDialog(getActivity(), getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mInventoryProductListBaseAdapter=new InventoryProductListBaseAdapter(getActivity(),mAllInventoryList);
				mInventoryProductsListView.setAdapter(mInventoryProductListBaseAdapter);
					
					
					
				}
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
				JSONObject json=mJsonArray.getJSONObject(i);
				mInVentoryData=new  InVentoryData();
				
				
				mInVentoryData.ProductId=json.getString("ProductId");
				mInVentoryData.Name=json.getString("Name");
				mInVentoryData.OnHandCost=json.getString("OnHandCost");
				mInVentoryData.OnHand=json.getString("OnHand");
				mInVentoryData.Number=json.getString("Number");
			
				mInVentoryData.MostRecentWholesale=json.getString("MostRecentWholesale");
				mInVentoryData.MostRecentPrice=json.getString("MostRecentPrice");
				mInVentoryData.TargetLevel=json.getString("TargetLevel");
				mAllInventoryList.add(mInVentoryData);
				
			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

		
	
	public void getInventoryProductsData() 
	{
		/*mProductIdList.add("131379");
		mProductNameList.add("Face Cream");
		mWholeSaleList.add("$16.44");
		mOnHandQuantityList.add("On Hand: 24");
		mOnHandValueList.add("0.00");
		mProductIdList.add("131378");
		mProductNameList.add("Lip Balm");
		mWholeSaleList.add("$245.23");
		mOnHandQuantityList.add("On Hand: 5");
		mOnHandValueList.add("0.00");
		mProductIdList.add("131377");
		mProductNameList.add("Face Cream");
		mWholeSaleList.add("$245.23");
		mOnHandQuantityList.add("On Hand: 5");
		mOnHandValueList.add("0.00");*/
	}

	
	

}
