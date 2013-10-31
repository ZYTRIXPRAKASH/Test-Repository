package com.flourish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.flourish.adapters.SalesListBaseAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.pulltorefresh.PullToRefresh;
import com.flourish.utils.SalesData;

public class SalesFragment extends Fragment implements PullToRefresh.ResetAsync {

	Fragment mFragment;
	private String mSessionId = null;
	String StatusDisplay = "null";
	PullToRefresh pullToRefresh = null;

	private int mCurrentPage = 1;
	private int mTotalPagesTag = 0;
	public int mSelectItem = 0;
	private String mTotalPages = null;
	private boolean isListLoaded = false;

	private String mGetSalesListResponse = null;

	private ConnectionManager mConnectionManager = null;
	SharedPreferences mSharedPreferences;
	List<SalesData> mAllSalesList = null;

	ListView sales_listview;
	FlourishBaseApplication mFlourishBaseApplication;

	AppNetwork mAppNetwork;
	SalesData mSalesData;

	GetSalesListAsyncTask task;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// onSessionStarted(true);
		mSharedPreferences = getActivity().getSharedPreferences("LoginInfo", 0);
		mSessionId = mSharedPreferences.getString("sessionId", "nothing");
		Log.v("TAG", "mSessionId  " + mSessionId);

		mAllSalesList = new ArrayList<SalesData>();

		mAppNetwork = new AppNetwork();

		if (mAppNetwork.isNetworkAvailable(getActivity())) {

			// task=new GetSalesListAsyncTask();

			new GetSalesListAsyncTask().execute();

		} else {
			mAppNetwork.getAlertDialog(getActivity(),
					getResources().getString(R.string.alert_dialog_no_network));
		}

		sales_listview.setOnScrollListener(new OnScrollListener() {

			public void onScroll(AbsListView view, int firstVisible,
					int visibleCount, int totalCount) {
				mSelectItem = firstVisible;
				boolean loadMore = firstVisible + visibleCount >= totalCount;
				if (loadMore && isListLoaded) {
					if (mTotalPages != null) {
						mTotalPagesTag = Integer.parseInt(mTotalPages);
						Log.v("response", "==mTotalPagesTag==" + mTotalPagesTag);
						if (mCurrentPage < mTotalPagesTag) {
							mCurrentPage += 1;
							isListLoaded = false;
							Log.v("response", "==load more==");
							new GetSalesListAsyncTask().execute();

						} else {
							Toast.makeText(getActivity(), "No data found",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}

			public void onScrollStateChanged(AbsListView v, int s) {

			}

		});

		

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFlourishBaseApplication = (FlourishBaseApplication) getActivity()
				.getApplicationContext();
		View mView;

		mView = inflater.inflate(R.layout.sales_fragment, container, false);

		sales_listview = (ListView) mView.findViewById(R.id.sales_listview);

		pullToRefresh = new PullToRefresh(SalesFragment.this,
				new GetSalesListAsyncTask(),
				mView.findViewById(R.id.pull_container));

		Log.v("TAG", "id" + mView.findViewById(R.id.pull_container));
		sales_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				mFlourishBaseApplication.mPersonNameFromContact = 1;//

				navigateToInvoice(mAllSalesList.get(position));
			}
		});

		return mView;

	}

	protected void navigateToInvoice(SalesData salesData) {
		if (salesData.StatusDisplay.equalsIgnoreCase("Draft")) {

			// Toast.makeText(getActivity(), "Draft", 500).show();

			startActivity(new Intent(getActivity(),
					SalesInvoiceEditDetails.class)

					.putExtra("invoiceId", "" + salesData.invoiceId)
					.putExtra("personId", "" + salesData.PersonId)

					.putExtra("invoiceTypeId", "" + salesData.InvoiceTypeId)
					.putExtra("firstName", "" + salesData.FirstName)
					.putExtra("lastName", "" + salesData.LastName)
					.putExtra("invoiceDate", "" + salesData.InvoiceDate)

					.putExtra("invoiceNumber", "" + salesData.InvoiceNumber)
					.putExtra("invoiceTypeName", "" + salesData.InvoiceTypeName)
					.putExtra("invoiceDiscount", "" + salesData.Discount)
					.putExtra("taxAfterDiscount",
							"" + salesData.TaxAfterDiscount)
					.putExtra("taxShipping", "" + salesData.TaxShipping)
					.putExtra("markItemsDelivered",
							"" + salesData.markItemsDelivered)
					.putExtra("shipping", "" + salesData.Shipping)
					.putExtra("invoiceTaxRate", "" + salesData.invoiceTaxRate)
					.putExtra("invoiceTaxableTotal",
							"" + salesData.TaxableTotal1)
					.putExtra("invoiceTaxAmount", "" + salesData.TaxAmount1)
					.putExtra("total", "" + salesData.Total)
					.putExtra("invoiceStatus", "" + salesData.InvoiceStatus));

			Log.v("TAG", "person id in Menu salesList" + "" + salesData);
			Log.v("TAG", "person id in  salesList" + "" + salesData.PersonId);

		}

		else {

			startActivity(new Intent(getActivity(), SalesDetails.class)

					.putExtra("StatusDisplay", "" + salesData.StatusDisplay)
					.putExtra("invoiceId", "" + salesData.invoiceId)
					.putExtra("personId", "" + salesData.PersonId)

					.putExtra("invoiceTypeId", "" + salesData.InvoiceTypeId)

					.putExtra("firstName", "" + salesData.FirstName)
					.putExtra("lastName", "" + salesData.LastName)
					.putExtra("invoiceDate", "" + salesData.InvoiceDate)
					.putExtra("invoiceNumber", "" + salesData.InvoiceNumber)
					.putExtra("invoiceTypeName", "" + salesData.InvoiceTypeName)
					.putExtra("invoiceDiscount", "" + salesData.Discount)
					.putExtra("taxAfterDiscount",
							"" + salesData.TaxAfterDiscount)
					.putExtra("taxShipping", "" + salesData.TaxShipping)
					.putExtra("markItemsDelivered",
							"" + salesData.markItemsDelivered)
					.putExtra("shipping", "" + salesData.Shipping)
					.putExtra("invoiceTaxRate", "" + salesData.invoiceTaxRate)
					.putExtra("invoiceTaxableTotal",
							"" + salesData.TaxableTotal1)
					.putExtra("invoiceTaxAmount", "" + salesData.TaxAmount1)
					.putExtra("total", "" + salesData.Total)
					.putExtra("invoiceStatus", "" + salesData.InvoiceStatus));

			Log.v("TAG", "person id in Menu salesList" + "" + salesData);
			Log.v("TAG", "person id in  salesList" + "" + salesData.PersonId);

			// getActivity().finish();

		}

	}

	class GetSalesListAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			/*
			 * // TODO Auto-generated method stub mProgressDialog =
			 * ProgressDialog.show(getActivity(),"Flourish", "Loading...",
			 * true); mProgressDialog.show();
			 */

			FlourishProgressDialog.ShowProgressDialog(getActivity());

			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			String mTaskUrl_get_sales_list_url = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.get_sales_list_url;
			mConnectionManager = new ConnectionManager();

			mGetSalesListResponse = mConnectionManager
					.setUpHttpGet(mTaskUrl_get_sales_list_url + mSessionId
							+ "/-/-/-/-/-/-/-/-/InvoiceNumber/DESC/"
							+ mCurrentPage + "/10");

			getSalesList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			FlourishProgressDialog.Dismiss(getActivity());

			if (mGetSalesListResponse != null) {

				FlourishProgressDialog.Dismiss(getActivity());

				if (mGetSalesListResponse.contains("Login Key Not Valid")) {
					getActivity().getSharedPreferences("DataChecking", 0)
							.edit().putString("Data", "no_data").commit();
					startActivity(new Intent(getActivity(), LoginScreen.class));
					getActivity().finish();

				} else if (mGetSalesListResponse.contains("Bad Parameters")) {
					mAppNetwork
							.getAlertDialog(
									getActivity(),
									getString(R.string.alert_dialog_update_invoice_unsuccess));
				} else {

					isListLoaded = true;

					SalesListBaseAdapter mSalesListBaseAdapter = new SalesListBaseAdapter(
							getActivity(), mAllSalesList);

					sales_listview.setAdapter(mSalesListBaseAdapter);
					sales_listview.setSelection(mSelectItem);
					
					String invoioceID = getActivity().getSharedPreferences("myfile", 0)
							.getString("PAYMENT_INVOICE", "");
					Log.v("TAG", "payment invoice"+invoioceID);
					if (invoioceID.length() > 0)
						for (SalesData salesData : mAllSalesList) {
							if (salesData.invoiceId.equals(invoioceID)) {
								navigateToInvoice(salesData);// Navigate to next screen
								break;
							}
						}

				}

				super.onPostExecute(result);

			}

		}

	}

	/**
	 * Method for parsing the Server response for getting sales
	 */
	public void getSalesList() {
		try {
			JSONObject mJsonObj = new JSONObject(mGetSalesListResponse);
			JSONArray mJsonArray;
			mTotalPages = (mJsonObj.getString("totalPages"));
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i = 0; i < mJsonArray.length(); i++) {
				SalesData mSalesData = new SalesData();

				JSONObject json = mJsonArray.getJSONObject(i);

				mSalesData.StatusDisplay = json.getString("StatusDisplay");

				mSalesData.InvoiceTypeId = json.getString("InvoiceTypeId");
				mSalesData.invoiceId = json.getString("Id");
				mSalesData.PersonId = json.getString("PersonId");
				mSalesData.FirstName = json.getString("FirstName");
				mSalesData.InvoiceNumber = json.getString("InvoiceNumber");
				mSalesData.LastName = json.getString("LastName");
				mSalesData.InvoiceDate = json.getString("InvoiceDate");
				mSalesData.InvoiceTypeName = json.getString("InvoiceTypeName");
				mSalesData.Discount = json.getString("Discount");
				mSalesData.TaxAfterDiscount = json
						.getString("TaxAfterDiscount");
				mSalesData.TaxShipping = json.getString("TaxShipping");

				mSalesData.IsDelivered = json.getString("IsDelivered");
				mSalesData.Outstanding = json.getString("Outstanding");
				mSalesData.Shipping = json.getString("Shipping");
				mSalesData.TaxRate1 = json.getString("TaxRate1");
				mSalesData.TaxableTotal1 = json.getString("TaxableTotal1");
				mSalesData.TaxAmount1 = json.getString("TaxAmount1");
				mSalesData.Total = json.getString("Total");
				mSalesData.InvoiceStatus = json.getString("InvoiceStatus");

				mAllSalesList.add(mSalesData);

			}
		} catch (Exception e) {
			e.printStackTrace();

			Log.v("TAG", "Exception  " + e.toString());

		}
	}

	public void onScroll(AbsListView view, int firstVisible, int visibleCount,
			int totalCount) {
		mSelectItem = firstVisible;
		boolean loadMore = firstVisible + visibleCount >= totalCount;
		if (loadMore && isListLoaded) {
			if (mTotalPages != null) {
				mTotalPagesTag = Integer.parseInt(mTotalPages);
				Log.v("response", "==mTotalPagesTag==" + mTotalPagesTag);
				if (mCurrentPage < mTotalPagesTag) {
					mCurrentPage += 1;
					isListLoaded = false;
					Log.v("response", "==load more==");
					new GetSalesListAsyncTask().execute();
				} else {
					Toast.makeText(getActivity(), "No data found",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	/*
	 * public void onScrollStateChanged(AbsListView v, int s) { }
	 * 
	 * void layoutApp(boolean menuOut) { System.out.println("layout [" +
	 * animParams.left + "," + animParams.top + "," + animParams.right + "," +
	 * animParams.bottom + "]"); mMainLayout.layout(animParams.left,
	 * animParams.top, animParams.right, animParams.bottom); //Now that we've
	 * set the app.layout property we can clear the animation, flicker avoided
	 * :) mMainLayout.clearAnimation();
	 * 
	 * }
	 */

	public void resetAsync() {
		pullToRefresh.setAsync(new GetSalesListAsyncTask());

	}

}
