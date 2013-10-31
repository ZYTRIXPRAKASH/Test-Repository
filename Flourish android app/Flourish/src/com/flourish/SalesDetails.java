/**
 * 
 */
package com.flourish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.propay.payments.AddPropayPayment;
import com.flourish.pulltorefresh.PullToRefresh;
import com.flourish.utils.Deliveries;
import com.flourish.utils.SalesData;

/**
 * @author aditya
 * 
 */
public class SalesDetails extends Activity implements OnClickListener {
	
	private int INVOICE_TAB_ID=100;
	private int PAYMENTS_TAB_ID=200;
	private int DELIVERIES_TAB_ID=00;

	
	private LinearLayout mLineItemsLl = null;
	
	private LayoutInflater mLayoutInflater = null;
	private TextView mSalesInvoiceNumber = null;
	private TextView mSalesPersonName = null;
	private TextView mSalesInvoiceDate = null;
	private TextView mSalesInvoiceType = null;               
	private TextView mSalesSubTotalValue = null;
	private TextView mSalesInvoiceDiscount = null;
	private TextView mSalesInvoiceShipping = null;
	private TextView mSalesInvoiceTaxValue = null;
	private TextView mSalesInvoiceTaxAmount = null;
	private TextView mSalesInvoiceTotalAmount = null;
	private TextView mDeliveredQuantity_tv=null;
	PullToRefresh mPullToRefresh=null;

	
	
	
	private ArrayList<String> mLineItemList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemIdList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemNameList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemSaleTypeList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemRetailList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemQuantityList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemDiscountList = new ArrayList<String>();
	private ArrayList<String> mInvoiceItemTotalList = new ArrayList<String>();
	
	
	
	

	private String mSessionId = null;
	private String mInvoiceId = null;
	private String mFirstName = null;
	private String mLastName = null;
	private String mInvoiceType = null;
	public  String mSalesInvoiceItemsResponse = null;
	private String mDeliveriesItemsResponse = null;
	private String mTaxableTotal = null;
	private String mTaxRate = null;
	private String mTaxAmount = null;
	private String mTotalAmount = null;
	private AppNetwork mAppNetwork = null;
	private String mPaymentsOnInvoiceStr = null;
	private ConnectionManager mConnectionManager = null;
	private String mResponse = null;
	private String mTaskUrl = null;

	private Button mPaymentsButton = null;
	private Button mDeliveriesButton = null;
	private Button mInvoiceButton = null;
	private String mCheckSalesType = "Invoice";
	private Button mTopAddBarBtn = null;
	private LinearLayout mSalesTopBar_rl = null;
	private Button mSalesBackButton = null;
	private String mPersonId = null;
	private String mInvoiceTypeId = null;
	private String mInvoiceDate = null;
	private String mInvoiceNumber = null;
	private String mInvoiceDiscount = null;
	private String mTaxShipping = null;
	private String mShipping = null;
	private String mInvoiceStatus = null;
	private String mTaxAfterDiscount = null;
	private String mInvoiceTypeName = null;
	private String mIsDelivered = null;
	private String mStatusDisplay=null;
	private ImageView mPaid_status_ImageView;

	JSONArray mSalesInvoiceLineItemsJSONArray = new JSONArray();

	private TextView mInvoiceTitle = null;
	private ArrayList<String> mInvoiceItemSubtotalList = new ArrayList<String>();
	private RelativeLayout mSalesInvoiceLayout_rl = null;
	private RelativeLayout mSalesPaymentLayout_rl = null;
	private RelativeLayout mSalesDeliveriesLayout_rl = null;

	private ArrayList<String> mPaymentDatesArrayList;
	private ArrayList<String> mPaymentMethodArrayList;
	private ArrayList<String> mPaymentAmountArrayList;
	private ArrayList<String> mPaymentMemoArrayList;

	private JSONArray mAllPaymentsDataResponse;
	private LinearLayout mPaymentLineItemsLl = null;
	private TextView mPaidAmount = null;
	private TextView mRemainingBalance = null;

	Deliveries mDeliveries;
	List<Deliveries> mAllDeliveryItemsList;
	ListView mDeliveriesListView;
	FlourishBaseApplication mFlourishBaseApplication;
	String mDeliveredQuantityResponse=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sales_topbar);

		
        mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();

		mAllDeliveryItemsList = new ArrayList<Deliveries>();

		mSessionId = getSharedPreferences("LoginInfo", 0).getString(
				"sessionId", "nothing");


	//	StatusDisplay
		mStatusDisplay=getIntent().getStringExtra("StatusDisplay");
		
		
		mInvoiceId = getIntent().getStringExtra("invoiceId");
		mPersonId = getIntent().getStringExtra("personId");
		Log.v("TAG", "Invoice  id  in Sales Details " + mPersonId);
		Log.v("TAG", "Person id  in Sales Details " + mPersonId);
		mInvoiceTypeId = getIntent().getStringExtra("invoiceTypeId");
		mFirstName = getIntent().getStringExtra("firstName");
		mLastName = getIntent().getStringExtra("lastName");
		mInvoiceDate = getIntent().getStringExtra("invoiceDate");
		mInvoiceNumber = getIntent().getStringExtra("invoiceNumber");
		mInvoiceDiscount = getIntent().getStringExtra("invoiceDiscount");
		mTaxAfterDiscount = getIntent().getStringExtra("taxAfterDiscount");
		mTaxShipping = getIntent().getStringExtra("taxShipping");
		mIsDelivered = getIntent().getStringExtra("markItemsDelivered");
		mShipping = getIntent().getStringExtra("shipping");
		mTaxRate = getIntent().getStringExtra("invoiceTaxRate");
		
		mTaxableTotal = getIntent().getStringExtra("invoiceTaxableTotal");

		Log.v("TAG", "sales  details----mTaxableTotal= "+mTaxableTotal);
		
		mTaxAmount = getIntent().getStringExtra("invoiceTaxAmount");
		mTotalAmount = getIntent().getStringExtra("total");
		mInvoiceStatus = getIntent().getStringExtra("invoiceStatus");
		mInvoiceTypeName = getIntent().getStringExtra("invoiceTypeName");

		Log.v("TAG", "first name " + mFirstName);
		Log.v("TAG", "mTaxableTotal  " + mTaxableTotal);
		Log.v("TAG", " mTaxAmount " + mTaxAmount);
		Log.v("TAG", "invoiceTypeName  " + mInvoiceTypeName);

		initializeVariables();
		setPaidStatus();

		if (mAppNetwork.isNetworkAvailable(SalesDetails.this)) {
		    new DeliverediItemsQuantityAsyncTask().execute();
			new GetSalesInvoiceItems().execute();
         	setSalesInvoiceItemsData();

		} else {
			mAppNetwork.getAlertDialog(SalesDetails.this,
					getString(R.string.alert_dialog_no_network));
			Log.v("TAG", "in onCreate");
			
		}
	}

	
	private void setPaidStatus() {
		// TODO Auto-generated method stub
		
		if (mStatusDisplay.equalsIgnoreCase("Active")) {
			
			mPaid_status_ImageView.setImageResource(R.drawable.unpaid);
			
			
			
		}
		else if (mStatusDisplay.equalsIgnoreCase("UpPaid")) {
			mPaid_status_ImageView.setImageResource(R.drawable.unpaid);
		}
		
    else if (mStatusDisplay.equalsIgnoreCase("Paid")) {
	
    	mPaid_status_ImageView.setImageResource(R.drawable.paid);
			
		}
		
else if(mStatusDisplay.equalsIgnoreCase("Delivered")) {
	mPaid_status_ImageView.setImageResource(R.drawable.paid);
}
		

		
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.v("TAG", "onResume=="+"onResume");
		
		mSessionId = getSharedPreferences("LoginInfo", 0).getString(
				"sessionId", "nothing");
		
		String invoioceID = getSharedPreferences("myfile", 0).getString("PAYMENT_INVOICE", "");
		if(invoioceID.length() > 0)
			
		 mPaymentsButton.performClick();
		getSharedPreferences("myfile", 0).edit().putString("PAYMENT_INVOICE", "").commit();
		
		
		
		
		
	}
	private void initializeVariables() {

		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();

		mLineItemsLl = (LinearLayout) findViewById(R.id.sales_invoice_line_list);
		mSalesInvoiceNumber = (TextView) findViewById(R.id.sales_invoice_value);
        mPaid_status_ImageView=(ImageView)findViewById(R.id.sales_invoice_paid_status);
		mSalesPersonName = (TextView) findViewById(R.id.sales_invoice_person_name);
		mSalesInvoiceDate = (TextView) findViewById(R.id.sales_invoice_date_value);
		mSalesInvoiceType = (TextView) findViewById(R.id.sales_invoice_type_value);
		mSalesSubTotalValue = (TextView) findViewById(R.id.subtotal_value);
		mSalesInvoiceDiscount = (TextView) findViewById(R.id.invoice_discount_value);
		mSalesInvoiceShipping = (TextView) findViewById(R.id.shipping_value);
		mSalesInvoiceTotalAmount = (TextView) findViewById(R.id.total_value);
		mSalesTopBar_rl = (LinearLayout) findViewById(R.id.sales_top_bar_rl);
		mTopAddBarBtn = (Button) findViewById(R.id.add_sales_button);
		mSalesBackButton = (Button) findViewById(R.id.sales_back_button);
		mSalesInvoiceTaxValue = (TextView) findViewById(R.id.sales_tax_calc_value);
		mSalesInvoiceTaxAmount = (TextView) findViewById(R.id.sales_tax_value);
		mInvoiceTitle = (TextView) findViewById(R.id.sales_name);
		mDeliveredQuantity_tv=(TextView)findViewById(R.id.deliveredItems_tv);

		mSalesInvoiceLayout_rl = (RelativeLayout) findViewById(R.id.sales_invoice_include_rl);
		mSalesPaymentLayout_rl = (RelativeLayout) findViewById(R.id.sales_payments_include_rl);
		mSalesDeliveriesLayout_rl = (RelativeLayout) findViewById(R.id.sales_deliveries_include_rl);

		mPaymentLineItemsLl = (LinearLayout) findViewById(R.id.sales_payment_line_list);
		mPaidAmount = (TextView) findViewById(R.id.payment_sales_value);
		mRemainingBalance = (TextView) findViewById(R.id.payment_remaining_balance_value);

		mInvoiceButton = (Button) findViewById(R.id.sales_invoice_btn);
		mPaymentsButton = (Button) findViewById(R.id.sales_payments_btn);
		mDeliveriesButton = (Button) findViewById(R.id.sales_deliveries_btn);
		mDeliveriesListView = (ListView) findViewById(R.id.sales_deliveries_lv);

		mInvoiceButton.setOnClickListener(this);
		mPaymentsButton.setOnClickListener(this);
		mDeliveriesButton.setOnClickListener(this);
		mTopAddBarBtn.setOnClickListener(this);
		mSalesBackButton.setOnClickListener(this);

	}

	/**
	 * AsynTask for making Server request and response for getting Sales Invoice
	 * Items
	 */
	private class GetSalesInvoiceItems extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		FlourishProgressDialog.ShowProgressDialog(SalesDetails.this);
			mLineItemsLl.removeAllViews();
			mPaymentLineItemsLl.removeAllViews();
			
			mAllDeliveryItemsList=new ArrayList<Deliveries>();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{   
			String mTaskUrl_get_sales_invoice_items_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_sales_invoice_items_url;
			mSalesInvoiceItemsResponse = mConnectionManager.setUpHttpGet(mTaskUrl_get_sales_invoice_items_url+ mSessionId + "/" + mInvoiceId);
			Log.v("TAG", "==mSalesInvoiceItemsResponse=="
					+ mSalesInvoiceItemsResponse);
			getSalesInvoiceItems();

			String mTaskUrl_get_all_payments=  mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_all_payments;
			mPaymentsOnInvoiceStr = mConnectionManager.setUpHttpGet(mTaskUrl_get_all_payments+ mSessionId + "/" + mInvoiceId);
			
			getAllPaymentsResponse();

			getAllDeliveryResponse();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			FlourishProgressDialog.Dismiss(SalesDetails.this);
			
			if (mSalesInvoiceItemsResponse!=null) {
				
		
				if (mSalesInvoiceItemsResponse.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
							.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesDetails.this,
							LoginScreen.class));
				} else if (mSalesInvoiceItemsResponse
						.contains("Bad Parameters")) {mAppNetwork.getAlertDialog(SalesDetails.this,getString(R.string.alert_dialog_update_invoice_unsuccess));
				
				Log.v("TAG", "service invoice");
				
				} else {
					setSalesInvoiceLineItems();
					setPaymentLineItems();
					mDeliveriesListView.setAdapter(new SalesDeliveryListBaseApplication());
					
					
					
					
					
				}
			
			}
			
			else {
				
				mAppNetwork.getAlertDialog(SalesDetails.this, getResources().getString(R.string.alert_dialog_no_network));
			}
			
			
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for parsing Server response for getting Sales Invoice Items
	 */
	private void getSalesInvoiceItems() {
		try {
			if (mSalesInvoiceItemsResponse!=null) {
				
			
			
			
			JSONObject mSalesInvoiceLineItemsJSON = new JSONObject(
					mSalesInvoiceItemsResponse);

			mSalesInvoiceLineItemsJSONArray = mSalesInvoiceLineItemsJSON
					.getJSONArray("data");

			for (int i = 0; i < mSalesInvoiceLineItemsJSONArray.length(); i++) {

				mInvoiceItemIdList.add(mSalesInvoiceLineItemsJSONArray
						.getJSONObject(i).getString("Number"));
				mInvoiceItemNameList.add(mSalesInvoiceLineItemsJSONArray.getJSONObject(i).getString("Name"));
				
				Log.v("TAG", "name list======="+mSalesInvoiceLineItemsJSONArray.getJSONObject(i).getString("Name"));
				mInvoiceItemSaleTypeList.add(mSalesInvoiceLineItemsJSONArray
						.getJSONObject(i).getString("ItemTypeName"));
				mInvoiceItemRetailList.add(mSalesInvoiceLineItemsJSONArray
						.getJSONObject(i).getString("Retail"));

				if (mSalesInvoiceLineItemsJSONArray.getJSONObject(i)
						.getString("Quantity").contains("-")) {
					mInvoiceItemQuantityList
							.add(mSalesInvoiceLineItemsJSONArray
									.getJSONObject(i)
									.getString("Quantity")
									.substring(
											1,
											mSalesInvoiceLineItemsJSONArray
													.getJSONObject(i)
													.getString("Quantity")
													.length()));
				} else {
					mInvoiceItemQuantityList
							.add(mSalesInvoiceLineItemsJSONArray.getJSONObject(
									i).getString("Quantity"));
				}

				if (mSalesInvoiceLineItemsJSONArray.getJSONObject(i)
						.getString("DiscountAmount").contains("-")) {
					mInvoiceItemDiscountList
							.add(mSalesInvoiceLineItemsJSONArray
									.getJSONObject(i)
									.getString("DiscountAmount")
									.substring(
											1,
											mSalesInvoiceLineItemsJSONArray
													.getJSONObject(i)
													.getString("DiscountAmount")
													.length()));
				} else {
					mInvoiceItemDiscountList
							.add(mSalesInvoiceLineItemsJSONArray.getJSONObject(
									i).getString("DiscountAmount"));
				}

				if (mSalesInvoiceLineItemsJSONArray.getJSONObject(i)
						.getString("SubTotal").contains("-")) {
					mInvoiceItemSubtotalList
							.add(mSalesInvoiceLineItemsJSONArray
									.getJSONObject(i)
									.getString("SubTotal")
									.substring(
											1,
											mSalesInvoiceLineItemsJSONArray
													.getJSONObject(i)
													.getString("SubTotal")
													.length()));
				} else {
					mInvoiceItemSubtotalList
							.add(mSalesInvoiceLineItemsJSONArray.getJSONObject(
									i).getString("SubTotal"));
				}
			}
			
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses the response from server and displays the list of deliveries
	 */

	public void getAllDeliveryResponse() {

		JSONArray dataJsonArry;
		String mTaskUrl_get_all_delivered_items=  mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_all_delivered_items;

	//	mDeliveriesItemsResponse = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_all_delivered_items)+ mSessionId + "/" + mInvoiceId);
		mDeliveriesItemsResponse = mConnectionManager.setUpHttpGet(mTaskUrl_get_all_delivered_items+ mSessionId + "/" + mInvoiceId);
		Log.v("PRK", "All Deivery list--->"+mDeliveriesItemsResponse);
		if (mDeliveriesItemsResponse != null) {

			try {

				JSONObject mCompleteJsonObject = new JSONObject(
						mDeliveriesItemsResponse);
				dataJsonArry = mCompleteJsonObject.getJSONArray("data");

				Log.v("PRK", "delivery array size" + dataJsonArry.length());

				if (dataJsonArry.length() != 0) {

					for (int i = 0; i < dataJsonArry.length(); i++) {

						mDeliveries = new Deliveries();
						
						JSONObject json = dataJsonArry.getJSONObject(i);

						mDeliveries.Id = json.getString("Id");
						mDeliveries.SalesInvoiceId = json
								.getString("SalesInvoiceId");
						mDeliveries.ShipDate = json.getString("ShipDate");
						mDeliveries.ShipMethod = json.getString("ShipMethod");
						mDeliveries.TrackingNumber = json
								.getString("TrackingNumber");
						mDeliveries.IsExecuted = json.getString("IsExecuted");
						mDeliveries.QuantitySum = json.getString("QuantitySum");
						mDeliveries.Tag = json.getString("Tag");
						mDeliveries.SuppressEntityEvents = json
								.getString("SuppressEntityEvents");
						mDeliveries.IsDeleted = json.getString("IsDeleted");
						mDeliveries.IsDirty = json.getString("IsDirty");
						mDeliveries.IsNew = json.getString("IsNew");

						mDeliveries.ViewName = json.getString("ViewName");
						mDeliveries.TrackingNumber=json.getString("TrackingNumber");

						mAllDeliveryItemsList.add(mDeliveries);
					

						Log.v("PRK", "Delivery id   " + mDeliveries.Id);

					}
				}

				else {

					

					Log.v("PRK", "List size " + mAllDeliveryItemsList.size());
					Log.v("PRK", "delivery List===Response "+mPaymentsOnInvoiceStr);

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				Log.v("prakash",
						"Exception in Delivery Response" + e.toString());

			}

		}

	}
	
	
	
	
	
	
	
	
	

	private void setSalesInvoiceItemsData() {
		/*
		 * double mTaxableTotalValue = Double.parseDouble(mTaxableTotal);
		 * 
		 * float mTaxRateValue = Float.parseFloat(mTaxRate);
		 * mSalesInvoiceTaxValue.setText("($" + String.format("%.2f",
		 * mTaxableTotalValue) + " x " + (mTaxRateValue * 100) + "%)");
		 */

		mSalesPersonName.setText(mFirstName + " " + mLastName);
		Log.v("TAG", "mInvoiceNumber " + mInvoiceNumber);
		mSalesInvoiceType.setText(mInvoiceTypeName);
		mSalesInvoiceTaxAmount.setText(": $" + mTaxAmount.replaceAll("-", ""));
		mSalesInvoiceNumber.setText(mInvoiceNumber);
		mSalesInvoiceShipping.setText("$" + mShipping);
		mInvoiceTitle.setText("Invoice #" + mInvoiceNumber);

		String mInvoiceDateStr = mInvoiceDate;
		String[] mPersonBirthDateArray = mInvoiceDateStr.split("-");
		mInvoiceDateStr = mPersonBirthDateArray[1] + "/"
				+ mPersonBirthDateArray[2].substring(0, 2) + "/"
				+ mPersonBirthDateArray[0];

		mSalesInvoiceDate.setText(mInvoiceDateStr);

		

		if (mTotalAmount.contains("-")) {
			mSalesInvoiceDiscount.setText("$"
					+ mTotalAmount.substring(1, mTotalAmount.length()));
		} else {
			mSalesInvoiceTotalAmount.setText("$" + mTotalAmount);
		}
		
		mSalesInvoiceTotalAmount.setText("$" + mTotalAmount.replaceAll("-", ""));
		mSalesInvoiceDiscount.setText("$ "+mInvoiceDiscount.replaceAll("-", ""));
		
		Log.v("TAG", "TOTAL==="+mTotalAmount);
		
		
		
	}

	/**
	 * Method for designing email layouts
	 */
	private void setSalesInvoiceLineItems() {
		float subTotal = 0;
		if (mSalesInvoiceLineItemsJSONArray.length() > 0) {
			for (int position = 0; position < mSalesInvoiceLineItemsJSONArray
					.length(); position++) {
				final View view;
				mLayoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mLayoutInflater.inflate(
						R.layout.sales_invoice_line_item_row, null);

				TextView mLineItemTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_tv);
				TextView mLineItemTypeTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_type_tv);
				TextView mLineItemInvoiceNumberTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_invoice_number_tv);
				TextView mLineItemTaxTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_type_tax_tv);
				TextView mLineItemTotalTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_total_tv);
				TextView mLineItemQuantityTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_type_quantity);
				TextView mLineItemDiscountTv = (TextView) view
						.findViewById(R.id.sales_invoice_line_item_discount);

				View line = (View) view
						.findViewById(R.id.sales_invoice_line_item_view);

				if (position == mSalesInvoiceLineItemsJSONArray.length() - 1) {
					line.setVisibility(View.GONE);
				}

				if (mSalesInvoiceLineItemsJSONArray.length() > 0 && mInvoiceItemNameList.size()!=0) {
					mLineItemTv.setText(mInvoiceItemNameList.get(position).toString());
					mLineItemInvoiceNumberTv.setText("#"+ mInvoiceItemIdList.get(position).toString());
					mLineItemTaxTv.setText("$"
							+ mInvoiceItemRetailList.get(position).toString());
					mLineItemTaxTv.setTextColor(Color.parseColor("#44923a"));
					mLineItemQuantityTv
							.setText(" "
									+ "x"
									+ " "
									+ mInvoiceItemQuantityList.get(position)
											.toString());
					mLineItemTypeTv.setText(mInvoiceItemSaleTypeList.get(
							position).toString());
					
                         Log.v("TAG", "position =="+position);
                   
                         if (mInvoiceItemDiscountList.size()!=0) {
                        	 
                        	 if (!(mInvoiceItemDiscountList.get(position).toString().equals("0.0"))) {
         						mLineItemDiscountTv.setText("-"+ mInvoiceItemDiscountList.get(position)
         										.toString());
         						mLineItemDiscountTv.setVisibility(View.VISIBLE);
         					}

							
						}
                         
                         if (mInvoiceItemSubtotalList.size()!=0) {
                        	 
                        	 mLineItemTotalTv
 							.setText("$"
 						+ mInvoiceItemSubtotalList.get(position)
 											.toString());
 					subTotal = subTotal
 							+ Float.parseFloat(mInvoiceItemSubtotalList.get(
 									position).toString());
							
						}
					
					

					mLineItemsLl.addView(view);
				}
			}
			
			mSalesSubTotalValue.setText("$" + String.valueOf(subTotal));
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.sales_invoice_btn:

			mCheckSalesType = "Invoice";

			mTopAddBarBtn.setText(getResources().getString(R.string.edit));
			mSalesTopBar_rl
					.setBackgroundResource(R.drawable.top_grey_first_selected);
			mInvoiceButton.setTextColor(getResources().getColor(R.color.white));
			mDeliveriesButton.setTextColor(getResources().getColor(
					R.color.dark_gray));
			mPaymentsButton.setTextColor(getResources().getColor(
					R.color.dark_gray));

			mSalesInvoiceLayout_rl.setVisibility(View.VISIBLE);
			mSalesPaymentLayout_rl.setVisibility(View.GONE);
			mSalesDeliveriesLayout_rl.setVisibility(View.GONE);
			
			

			break;

		case R.id.sales_payments_btn:

			mCheckSalesType = "Payments";

			mTopAddBarBtn.setText(getResources()
					.getString(R.string.add_contact));
			mSalesTopBar_rl
					.setBackgroundResource(R.drawable.top_grey_second_selected);
			mInvoiceButton.setTextColor(getResources().getColor(
					R.color.dark_gray));
			mDeliveriesButton.setTextColor(getResources().getColor(
					R.color.dark_gray));
			mPaymentsButton
					.setTextColor(getResources().getColor(R.color.white));

			mSalesInvoiceLayout_rl.setVisibility(View.GONE);
			mSalesDeliveriesLayout_rl.setVisibility(View.GONE);
			mSalesPaymentLayout_rl.setVisibility(View.VISIBLE);

			break;

		case R.id.sales_deliveries_btn:

			mCheckSalesType = "Deliveries";

			mTopAddBarBtn.setText(getResources()
					.getString(R.string.add_contact));
			mSalesTopBar_rl
					.setBackgroundResource(R.drawable.top_grey_third_selected);
			mInvoiceButton.setTextColor(getResources().getColor(
					R.color.dark_gray));
			mDeliveriesButton.setTextColor(getResources().getColor(
					R.color.white));
			mPaymentsButton.setTextColor(getResources().getColor(
					R.color.dark_gray));

			mSalesInvoiceLayout_rl.setVisibility(View.GONE);
			mSalesPaymentLayout_rl.setVisibility(View.GONE);
			
			//
			mSalesDeliveriesLayout_rl.setVisibility(View.VISIBLE);

			break;

		case R.id.add_sales_button:

			if (mCheckSalesType.equalsIgnoreCase("Invoice")) {

				Intent mIntent=new Intent(SalesDetails.this, SalesInvoiceEditDetails.class);
				
				
				mIntent.putExtra("invoiceId", mInvoiceId);
				mIntent		.putExtra("personId", mPersonId);
						mIntent.putExtra("invoiceTypeId", mInvoiceTypeId);
						mIntent.putExtra("firstName", mFirstName);
						mIntent.putExtra("lastName", mLastName);
						mIntent.putExtra("invoiceDate", mInvoiceDate);
						mIntent.putExtra("invoiceNumber", mInvoiceNumber);
						mIntent.putExtra("invoiceTypeName", mInvoiceTypeName);
						mIntent.putExtra("invoiceDiscount", mInvoiceDiscount);
						mIntent.putExtra("taxAfterDiscount", mTaxAfterDiscount);
						mIntent.putExtra("taxShipping", mTaxShipping);
						mIntent.putExtra("markItemsDelivered", mIsDelivered);
						mIntent.putExtra("shipping", mShipping);
						mIntent.putExtra("invoiceTaxRate", mTaxRate);
						mIntent.putExtra("invoiceTaxableTotal", mTaxableTotal);
						mIntent.putExtra("invoiceTaxAmount", mTaxAmount);
						mIntent.putExtra("total", mTotalAmount);
						mIntent.putExtra("invoiceStatus", mInvoiceStatus);  

		               startActivityForResult(mIntent, 100);

			} else if (mCheckSalesType.equalsIgnoreCase("Payments")) {
               mFlourishBaseApplication.setmPaymentsTab(1);
                
				Intent mIntent=new Intent(getApplicationContext(), AddPayments.class);
				mIntent.putExtra("invoiceId", mInvoiceId);
				mIntent.putExtra("invoiceNumber", mInvoiceNumber);
				mIntent.putExtra("amount", mInvoiceNumber);
				mIntent.putExtra("amount", mSalesInvoiceTotalAmount.getText().toString().trim());
			
				Log.v("TAG", "mInvoiceNumber==="+mInvoiceNumber);
				startActivityForResult(mIntent, 200);
				
				
				//showDialog(R.id.sales_payments_btn);
				

			} else if (mCheckSalesType.equalsIgnoreCase("Deliveries")) {

				/*startActivity(new Intent(SalesDetails.this, AddDeliveries.class)
						.putExtra("InvoiceId", mInvoiceId));*/
				
				
			
				Intent mIntent=new Intent(getApplicationContext(), AddDeliveries.class);
				mIntent.putExtra("invoiceId", mInvoiceId);
				mIntent.putExtra("invoiceNumber", mInvoiceNumber);
				startActivityForResult(mIntent, 300);
				
			}

			break;
			
			

		case R.id.sales_back_button:

		
			
			startActivity(new Intent(SalesDetails.this, FlourishHomeActivity.class));
			finish();

			break;

		default:
			break;
		}
	}

	/**
	 * Gets all the Payments from Server on a particular Invoice
	 */
	public void getAllPaymentsResponse() {

		mPaymentDatesArrayList = new ArrayList<String>();
		mPaymentMethodArrayList = new ArrayList<String>();
		mPaymentAmountArrayList = new ArrayList<String>();
		mPaymentMemoArrayList = new ArrayList<String>();
		mAllPaymentsDataResponse = new JSONArray();

		try {
			JSONObject mAllPaymentsResponseJSON = new JSONObject(
					mPaymentsOnInvoiceStr);
			mAllPaymentsDataResponse = mAllPaymentsResponseJSON
					.getJSONArray("data");

			for (int i = 0; i < mAllPaymentsDataResponse.length(); i++) {

				mPaymentDatesArrayList.add(mAllPaymentsDataResponse
						.getJSONObject(i).getString("PaymentDate"));
				mPaymentMethodArrayList.add(mAllPaymentsDataResponse
						.getJSONObject(i).getString("Method"));
				mPaymentAmountArrayList.add(mAllPaymentsDataResponse
						.getJSONObject(i).getString("Amount"));
				mPaymentMemoArrayList.add(mAllPaymentsDataResponse
						.getJSONObject(i).getString("Reference"));
			}

		} catch (JSONException e) {
			Log.e("TAG",
					"Exception in SalesDetails at getAllPaymentsResponse : "
							+ e.getMessage());
		}
	}

	/**
	 * Method to set Payment Line Items
	 */
	public void setPaymentLineItems() {

		float totalAmount = 0;
		float remainingAmount = 0;

		
		
		
		
		
		if (mAllPaymentsDataResponse.length() > 0) {
		
			
			
			for (int position = 0; position < mAllPaymentsDataResponse.length(); position++) {
				final View view;
				mLayoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = mLayoutInflater.inflate(
						R.layout.sales_payment_line_item_row, null);

				TextView paymentDateTv = (TextView) view
						.findViewById(R.id.sales_payment_date_line_item_tv);
				TextView paymentMethod = (TextView) view
						.findViewById(R.id.sales_payment_type_line_item);
				TextView paymentAmount = (TextView) view
						.findViewById(R.id.sales_payment_amount_line_item_type_tv);
				TextView paymentMemo = (TextView) view
						.findViewById(R.id.sales_payment_line_item_payment_memo_tv);

				View line = (View) view
						.findViewById(R.id.sales_payment_line_item_view);

				if (position == mAllPaymentsDataResponse.length() - 1) {
					line.setVisibility(View.GONE);
				}

				String date = mPaymentDatesArrayList.get(position).toString();
				String paymentDate = date.substring(0, date.indexOf("T"));

				paymentDateTv.setText(paymentDate);
				paymentMethod.setText(" - "
						+ mPaymentMethodArrayList.get(position).toString());
				paymentAmount.setText("$"
						+ mPaymentAmountArrayList.get(position).toString());
				paymentMemo.setText(mPaymentMemoArrayList.get(position)
						.toString());

				mPaymentLineItemsLl.addView(view);
				totalAmount = totalAmount
						+ Float.parseFloat(mPaymentAmountArrayList
								.get(position).toString());
				remainingAmount = Float.parseFloat(mTotalAmount) - totalAmount;
			}

			mPaidAmount.setText("$" + String.valueOf(totalAmount).replaceAll("-", ""));
			mRemainingBalance.setText("$" + String.valueOf(remainingAmount).replaceAll("-", ""));
		}
		
		else
		{
			
			
			//DialogClass.createDAlertDialogActivityFinish(this, ""+R.string.no_deliveries);
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Dialog dialog;
		AlertDialog.Builder builder;
		switch (id) {
		case R.id.sales_payments_btn:

			final String paymentTypes[] = getResources().getStringArray(
					R.array.payment_type_array);
			builder = new AlertDialog.Builder(SalesDetails.this);
			builder.setTitle(R.string.select_payment_type).setItems(
					paymentTypes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int position) {

							String paymentType = paymentTypes[position];
							if (paymentType.equalsIgnoreCase(getResources()
									.getString(R.string.cash))) {

								startActivity(new Intent(SalesDetails.this,
										AddPayments.class).putExtra(
										"PAYMENT_TYPE", "Cash").putExtra(
										"invoiceId", mInvoiceId));

							} else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.cheque))) {

								startActivity(new Intent(SalesDetails.this,
										AddPayments.class).putExtra(
										"PAYMENT_TYPE", "Cheque").putExtra(
										"invoiceId", mInvoiceId));

							} else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.credit_OR_card))) {

								startActivity(new Intent(SalesDetails.this,
										AddPayments.class).putExtra(
										"PAYMENT_TYPE", "Credit Card")
										.putExtra("invoiceId", mInvoiceId));

							} else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.other))) {

								startActivity(new Intent(SalesDetails.this,
										AddPayments.class).putExtra(
										"PAYMENT_TYPE", "Others").putExtra(
										"invoiceId", mInvoiceId));

							} else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.propay))) {

								startActivity(new Intent(SalesDetails.this,
										AddPropayPayment.class).putExtra(
										"invoiceId", mInvoiceId));
							}
						}
					});
			dialog = builder.create();
			break;
		default:
			dialog = null;
			break;
		}
		return dialog;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			finish();

		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	

	class SalesDeliveryListBaseApplication extends BaseAdapter

	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("TAG", "List size" + mAllDeliveryItemsList.size());
			return mAllDeliveryItemsList.size();

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v;
			LayoutInflater mLayoutInflater = getLayoutInflater();

			v = mLayoutInflater.inflate(R.layout.sales_invoice_deliveries_list_row, null);

			TextView mPementTypeTv = (TextView) v.findViewById(R.id.deliveryType_tv);
			TextView mDeliveryItesSize = (TextView) v.findViewById(R.id.deliveryItemsSize_tv);
			TextView mDeliveryNumber = (TextView) v.findViewById(R.id.deliveryNumberTv);

			String dateString = mAllDeliveryItemsList.get(position).ShipDate;

			String[] parts = dateString.split("T");
			String part1Date = parts[0]; // 004

			String[] part2 = part1Date.split("-");
			String myear = part2[0]; // 2013
			String mMonth = part2[1]; // 07
			String mDay = part2[2];// 20

			String requiredDateFormate = mDay + "/" + mMonth + "/" + myear;

			mPementTypeTv.setText("" + requiredDateFormate + " -"
					+ mAllDeliveryItemsList.get(position).ShipMethod);
			
			
			String deliveryItems=mAllDeliveryItemsList.get(position).QuantitySum;
			if (deliveryItems.contains("null")) {
				
				mDeliveryNumber.setText("");
				
			}
			else if (deliveryItems.contains("-")) {
				mDeliveryNumber.setText(""
						+ mAllDeliveryItemsList.get(position).SalesInvoiceId.replaceAll("-", "")+" items");
				
			}
			else
			{
				mDeliveryItesSize.setText(""+ mAllDeliveryItemsList.get(position).QuantitySum+ " items");
				
			}
			
			

			mDeliveryNumber.setText(""+ mAllDeliveryItemsList.get(position).TrackingNumber);

			Log.v("TAG", "Base adapter in DeliveryList");

			return v;
		}

	}

	
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
	
		switch (requestCode) {
		
		case 100:
			
			
			if(resultCode == RESULT_OK)
			{
		          Toast.makeText(getApplicationContext(), "Invoicereturned OK", Toast.LENGTH_LONG).show();
			}
		       break;
		       
		       
			
		case 200:
			if(resultCode == RESULT_OK)
			{
				new GetSalesInvoiceItems().execute();
				
				
				mCheckSalesType = "Payments";

				mTopAddBarBtn.setText(getResources()
						.getString(R.string.add_contact));
				mSalesTopBar_rl
						.setBackgroundResource(R.drawable.top_grey_second_selected);
				mInvoiceButton.setTextColor(getResources().getColor(
						R.color.dark_gray));
				mDeliveriesButton.setTextColor(getResources().getColor(
						R.color.dark_gray));
				mPaymentsButton
						.setTextColor(getResources().getColor(R.color.white));

				mSalesInvoiceLayout_rl.setVisibility(View.GONE);
				mSalesDeliveriesLayout_rl.setVisibility(View.GONE);
				mSalesPaymentLayout_rl.setVisibility(View.VISIBLE);
				
				
				
			}
		       break;
		       
		case 300:
			
			if(resultCode == RESULT_OK)
			{
		          //Toast.makeText(getApplicationContext(), "Deliveries OK", Toast.LENGTH_LONG).show();
		          
		          new GetSalesInvoiceItems().execute();
		          new DeliverediItemsQuantityAsyncTask().execute();
		      
		          
		          
			}
		       break;

		default:
			break;
		}
		

		
	}
	

	
	
	
class DeliverediItemsQuantityAsyncTask extends AsyncTask<Object, Void, String>
{
	
protected void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();
}



@Override
	protected String doInBackground(Object... params) {
	
	String mTaskUrl_deliveredQuantity=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.invoice_Items_DeliveredQty;
	   mConnectionManager=new ConnectionManager();
	   mDeliveredQuantityResponse= mConnectionManager.setUpHttpGet(mTaskUrl_deliveredQuantity+mSessionId+"/"+mInvoiceId);
	
	
			return mDeliveredQuantityResponse;
	}





@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
//		Log.v("TAG","Delivered Quantity Response==="+result);
		
		
		if (result!=null) {
			
			try {
				
		
			JSONObject mCompleteJsonObject=new JSONObject(mDeliveredQuantityResponse);
			
			JSONArray dataArray=mCompleteJsonObject.getJSONArray("data");
			
			int mQuantitySum=0;
			int mDeliveredQuty=0;
			
			if ((dataArray.length())!=0) {
				
				
			for (int i = 0; i < dataArray.length(); i++) {
				
				JSONObject json=dataArray.getJSONObject(i);
				int Quantity =Integer.parseInt(json.getString("Quantity"));
				int DeliveredQty =Integer.parseInt(json.getString("DeliveredQty"));
				mQuantitySum=mQuantitySum+Quantity;
				mDeliveredQuty=mDeliveredQuty+DeliveredQty;
				
				
				mDeliveredQuantity_tv.setText(mDeliveredQuty+" of "+mQuantitySum );
				
				//Log.v("TAF", "mDeliveredQuantity_tv  "+mDeliveredQuantity_tv);
				
				
			}	
				
				
				
				
				
			}
			{
			//Log.v("TAG", "data array is empty ");
				
			}
			
			
			
			} catch (Exception e) {
				// TODO: handle exception
				
				Log.v("TAG", "Exception "+e.toString());
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}






}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
