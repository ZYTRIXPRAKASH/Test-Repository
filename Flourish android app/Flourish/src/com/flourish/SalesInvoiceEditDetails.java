package com.flourish;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.flourish.adapters.MoneyListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.utils.InvoiceLineItem;

public class SalesInvoiceEditDetails extends BaseTimeOutActivity implements
		OnClickListener {
	private static final int DATE_DIALOG_ID = 1;
	private static final int SELECT_BOOKINGG_ID = 200;
	String mBookingCalenderId;

	private String SaveInvoiceType = null;

	private EditText mCustomerNameEt = null;
	private TextView mInvoiceNameTv = null;
	private TextView mInvoiceDateTv = null;
	private TextView mInvoiceTypeNameTv = null;
	private EditText mInvoiceDiscount1EditText = null;
	private TextView mInvoiceDiscount2Tv = null;
	private EditText add_invoice_booking_et = null;
	public  EditText mShipping_et = null;
	private TextView mTaxTv = null;
	private TextView mTaxAmountTv = null;
	private TextView mTotalAmountTv = null;
	private DatePicker mBirthDatePicker = null;
	private Button mCalendarBtn = null;
	private Button mSaveBtn = null;
	private Button mAddLineItemBtn = null;
	private EditText mAddLineItemEt = null;
	private Button mDeleteInvoiceBtn = null;
	private LinearLayout mInvoiceItemRl = null;
	private RelativeLayout mAddLineItemRl = null;
	private Button mTaxAfterDiscountBtn = null;
	private Button mTaxShippingBtn = null;
	private Button mMarkItemsDeliveredBtn = null;

	private Button mSalesDetailsBackButton = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Date mInputDate = null;
	private String mPersonName = null;
	private String mInvoiceId = null;
	private String mPersonId = null;
	private String mInvoiceTypeId = null;
	private String mFirstName = null;
	private String mLastName = null;
	private String mInvoiceDate = null;
	private String mInvoiceNumber = null;
	private String mInvoiceTypeName = null;
	private String mInvoiceDiscount = null;
	private String mTaxAfterDiscount = null;
	private String mTaxShipping = null;
	private String mIsDelivered = null;
	private String mShipping = null;
	private String mTaxRate = null;
	private String mTaxableTotal = null;
	private String mTaxAmount = null;
	private String mTotalAmount = null;
	private String mShippingValueString=null;

	private String mSessionId = null;
	private String mSalesInvoiceItemsResponse = null;
	private String mAddLineItemResponse = null;
	private String mUpdateInvoiceResponse = null;
	private String mDeleteLineItemResponse = null;
	private String mDeleteInvoiceResponse = null;
	private JSONObject mAddLineItemRequestObj = null;
	private JSONObject mUpdateInvoiceRequestObj = null;
	private String mAddLineItemAuthResponse = null;
	private String mDeleteLineItemAuthResponse = null;
	public String mProductId = null;
	public String mProductQuantity = null;
	public String mInvoiceItemId = null;
	public ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private LayoutInflater mInflater = null;
	
	private float mAllItemsDiscountTotal=0;

	List<InvoiceLineItem> mAllInvoiceLIstItemsList;
	InvoiceLineItem mInvoiceLineItem;

	ArrayList<Boolean> callServiceListItemEditText = new ArrayList<Boolean>();
	ArrayList<Boolean> callServiceEditText = new ArrayList<Boolean>();

	private String mSelectedProduct = null;
	private int mPosition = 0;
	private boolean mIsChecked = false;
	private boolean mIsTaxAfterDiscountChecked = false;
	
	private boolean mIsTaxShippingChecked = false;
	private boolean mIsMarkItemsDeliveredChecked = false;
	private VehicleListCustomDialog mCustomDialog = null;
	private ArrayList<String> mInvoiceTypeList = new ArrayList<String>();
	private MoneyListAdapter mInvoiceTypeListAdapter = null;
	protected String mInvoiceTypeStr = "Sale";
	private ToggleButton mDollarPrecentageToggleButton = null;
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;
	JSONObject mpostedItemJsonObject;
	String mTaskUrlFinalizeInvoce;
	String mFinalizeInvoiceResponse;
	JSONObject mJsonObj =null;
	TextView invoice_discount1_value_tv=null;

	// line item
	int mLineItemQuantityInteger;
	int mLineItemDiscount;

	String mQuantityString;
	String mDiscounted = null;
	float subtotalfloal = 0;
	String mInvoiceDisStr;
	float mTotalAmountValue;

	public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sales_invoice_edit_details);

		mFlourishBaseApplication = (FlourishBaseApplication) getApplicationContext();
		onSessionStarted(true);
		mAllInvoiceLIstItemsList = new ArrayList<InvoiceLineItem>();

		mSessionId = getSharedPreferences("LoginInfo", 0).getString(
				"sessionId", "nothing");
		Log.v("TAG", "mSessionId  :  " + mSessionId);
		mSharedPreferences = getSharedPreferences("myfile", MODE_PRIVATE);

		mInvoiceId = getIntent().getStringExtra("invoiceId");
		mInvoiceTypeId = getIntent().getStringExtra("invoiceTypeId");
		mPersonId = getIntent().getStringExtra("personId");

		
		/*
		 * mIntent=getIntent();
		 * mPersonIdString=mIntent.getStringExtra("PersonId");
		 * mInvoiceNumber=mIntent.getStringExtra("InvoiceNumber");
		 */

		Log.v("PRK", "Person id in SalesInvoiceEditDetails  " + mPersonId);
		Log.v("PRK", "mInvoiceId id in SalesInvoiceEditDetails = " + mInvoiceId);
		Log.v("PRK", "invoiceTypeId in SalesInvoiceEditDetails = "
				+ mInvoiceTypeId);

		initializeVariables();
		// taxofter discount

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		if (mAppNetwork.isNetworkAvailable(SalesInvoiceEditDetails.this)) {
			new GetSalesInvoiceItems().execute();
			
			setSalesInvoiceItemsData();
			new UpdateInvoiceTask().execute();
			
		} else {
			mAppNetwork.getAlertDialog(SalesInvoiceEditDetails.this,
					getString(R.string.alert_dialog_no_network));
		}
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables() {
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
		mCustomDialog = new VehicleListCustomDialog(
				SalesInvoiceEditDetails.this);

		mCustomerNameEt = (EditText) findViewById(R.id.sales_details_customer_et);
		mInvoiceNameTv = (TextView) findViewById(R.id.sales_details_invoice_name);
		mInvoiceDateTv = (EditText) findViewById(R.id.sales_details_date_et);
		add_invoice_booking_et = (EditText) findViewById(R.id.add_invoice_booking_et);
		add_invoice_booking_et.setText("TEST");
		mInvoiceTypeNameTv = (TextView) findViewById(R.id.sales_details_invoice_type_et);
		invoice_discount1_value_tv=(TextView)findViewById(R.id.invoice_discount1_value_tv);
	
		mInvoiceDiscount1EditText = (EditText) findViewById(R.id.invoice_discount1_value_et);
		mInvoiceDiscount1EditText.addTextChangedListener(new CustomTextWatcher(mInvoiceDiscount1EditText));
		mInvoiceDiscount1EditText.setSelection(mInvoiceDiscount1EditText.getText().length());
	
		Log.v("TAG", "EditText lenth "+mInvoiceDiscount1EditText.getText().length());
		
	//	mInvoiceDiscount1EditText.addTextChangedListener(new CustomTextWatcher(mInvoiceDiscount1EditText));
		
		mInvoiceDiscount2Tv = (TextView) findViewById(R.id.invoice_discount2_value);
		mShipping_et = (EditText) findViewById(R.id.shipping_value);
		mShipping_et.setSelection(mShipping_et.length());
		mShipping_et.addTextChangedListener(new CustomTextWatcher(mShipping_et));
		
		mTaxTv = (TextView) findViewById(R.id.tax_tv);
		mTaxAmountTv = (TextView) findViewById(R.id.tax_value);
		mTotalAmountTv = (TextView) findViewById(R.id.total_value);
		mInvoiceItemRl = (LinearLayout) findViewById(R.id.sales_invoice_item_list);
		mAddLineItemRl = (RelativeLayout) findViewById(R.id.sales_details_add_line_item_fields);
		mSaveBtn = (Button) findViewById(R.id.sales_details_save);
		mAddLineItemBtn = (Button) findViewById(R.id.sales_details_add_line_item_btn);
		mAddLineItemEt = (EditText) findViewById(R.id.sales_details_add_line_item_et);
		mCalendarBtn = (Button) findViewById(R.id.sales_details_calendar_btn);
		mDeleteInvoiceBtn = (Button) findViewById(R.id.sales_details_delete_btn);
		mSalesDetailsBackButton = (Button) findViewById(R.id.sales_details_back);
		mBirthDatePicker = (DatePicker) findViewById(R.id.sales_details_dpResult);

		mTaxAfterDiscountBtn = (Button) findViewById(R.id.tax_after_discount_btn);
		mTaxShippingBtn = (Button) findViewById(R.id.tax_shipping_btn);
		mMarkItemsDeliveredBtn = (Button) findViewById(R.id.mark_items_delivered_btn);
		mDollarPrecentageToggleButton = (ToggleButton) findViewById(R.id.dollar_percentage_toggle_button);

		mTaxAfterDiscountBtn.setOnClickListener(this);
		mTaxShippingBtn.setOnClickListener(this);
		mMarkItemsDeliveredBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		mAddLineItemRl.setOnClickListener(this);
		mAddLineItemBtn.setOnClickListener(this);
		mDeleteInvoiceBtn.setOnClickListener(this);
		mSalesDetailsBackButton.setOnClickListener(this);
		mCalendarBtn.setOnClickListener(this);
		mInvoiceTypeNameTv.setOnClickListener(this);
		add_invoice_booking_et.setOnClickListener(this);
		mDollarPrecentageToggleButton.setOnClickListener(this);

		mFirstName = getIntent().getStringExtra("firstName");
		mLastName = getIntent().getStringExtra("lastName");
		
	
		
		
		Log.v("TAG", "mInvoiceDate"+mInvoiceDate);
		mInvoiceNumber = getIntent().getStringExtra("invoiceNumber");
	
		mInvoiceTypeName = getIntent().getStringExtra("invoiceTypeName");
		mInvoiceDiscount = getIntent().getStringExtra("invoiceDiscount");
		mTaxAfterDiscount = getIntent().getStringExtra("taxAfterDiscount");
		mTaxShipping = getIntent().getStringExtra("taxShipping");
		mIsDelivered = getIntent().getStringExtra("markItemsDelivered");
		mShipping = getIntent().getStringExtra("shipping");
		mTaxRate = getIntent().getStringExtra("invoiceTaxRate");
		mTaxableTotal = getIntent().getStringExtra("invoiceTaxableTotal");
		mTaxAmount = getIntent().getStringExtra("invoiceTaxAmount");
		mTotalAmount = getIntent().getStringExtra("total");

		Log.v("TAG", "mTaxableTotal in sales invoice edit " + mTaxableTotal);
		mPersonName = getIntent().getStringExtra("personName");

		if (mTaxableTotal.contains("-")) {
			mTaxableTotal = mTaxableTotal.substring(1, mTaxableTotal.length());
		}
		if (mTaxAmount.contains("-")) {
			mTaxAmount = mTaxAmount.substring(1, mTaxAmount.length());
		}
		if (mTotalAmount.contains("-")) {
			mTotalAmount = mTotalAmount.substring(1, mTotalAmount.length());
			Log.v("TAG", "mTotalAmount " + mTotalAmount);
		

		}

		mInvoiceTypeList.add("Sale");
		mInvoiceTypeList.add("Booking");
		mInvoiceTypeList.add("Return");

		mCustomDialog.getHeaderTv().setText("Select an Account");
		mInvoiceTypeListAdapter = new MoneyListAdapter(
				SalesInvoiceEditDetails.this, R.layout.sales_list_row,
				mInvoiceTypeList);
		mCustomDialog.getListview().setAdapter(mInvoiceTypeListAdapter);
	
		mCustomDialog.getListview().setOnItemClickListener(
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> listView,
							View listItem, int position, long position1) {
						mInvoiceTypeStr = mInvoiceTypeList.get(position);
						mInvoiceTypeNameTv.setText(mInvoiceTypeList.get(position));

						mCustomDialog.dismiss();
					}
				});
	
	
		
		
		
		mShipping_et.setOnEditorActionListener(
		        new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
		        	
		        	mShippingValueString=mShipping_et.getText().toString().trim();
					Sales_Invoice_SetShipping();
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mShipping_et.getWindowToken(), 0);
		        	
		        	
		          
		            return true;
		        }
		        return false;
		    }
		});
		
		
		
		
		
		
		mInvoiceDiscount1EditText.setOnEditorActionListener(
		        new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
		        	
		        String enteredDiscountStr=mInvoiceDiscount1EditText.getText().toString();
		        float enteredInvoiceDiscoutFloat=Float.parseFloat(enteredDiscountStr);
		        
		        if (enteredInvoiceDiscoutFloat>mTotalAmountValue) {
		        	
		        	Toast.makeText(SalesInvoiceEditDetails.this, "Invoice Discount exiceed", 500).show();
					
				}
		        	
		        	
		        	
		        	
		        	
		        	
		        else{
		        	
		        	
		        	
		        	new DollarDiscount().execute();
		      
		        	//hide the keyboard after done
		        	/*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mInvoiceDiscount1EditText.getWindowToken(), 0);*/
					mInvoiceDiscount1EditText.setVisibility(View.GONE);
					invoice_discount1_value_tv.setVisibility(View.VISIBLE);
					 mInvoiceDisStr=mInvoiceDiscount1EditText.getText().toString();
					if (mInvoiceDisStr.contains(".")) {
						
						invoice_discount1_value_tv.setText("$"+mInvoiceDiscount1EditText.getText().toString());
						
					}
					else{
						invoice_discount1_value_tv.setText("$"+mInvoiceDiscount1EditText.getText().toString()+".00");
					}
					
					//hide the keyboard after done
					InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mInvoiceDiscount1EditText.getWindowToken(), 0);
					
		        }
		        
		        
		            return true;
		        }
		        return false;
		    }
		});
		

	
		
		
	}

	
	
	
	
	
	
	/**
	 * Method for setting Sales Invoice Items Data
	 */
	private void setSalesInvoiceItemsData() {
		
		/*
		String mYear = mInvoiceDate.substring(0, 4);
		int mMonth = Integer.parseInt(mInvoiceDate.substring(6, 7));
		String mDate = mInvoiceDate.substring(8, 10);

		mInvoiceDate = mDate + "-" + (mInvoiceDate.substring(6, 7)) + "-"
				+ mYear;
		*/
		
		mInvoiceDate = getIntent().getStringExtra("invoiceDate");
		Log.v("TAG", "Invoice date"+mInvoiceDate);
	    
		String[] dateString =mInvoiceDate.split("T");
		String part1=dateString[0];
		Log.v("TAG", " part1"+part1);
		
		String[] dateformateArray=part1.split("-");
		String mYear=dateformateArray[0];
		String mDate=dateformateArray[2];
		String month=dateformateArray[1];
		int mMonth = Integer.parseInt(month);
		
		mInvoiceDate=month+"-"+ mDate+"-"+mYear;
		
		
		Log.v("TAG", " mInvoiceDate"+mInvoiceDate);
		
		

		float mInvoiceDiscountValue = Float.parseFloat(mInvoiceDiscount);
		float mShippingValue = Float.parseFloat(mShipping);
		float mTaxableTotalValue = Float.parseFloat(mTaxableTotal);

		// float mTaxRateValue = Float.parseFloat(mTaxRate);

		// Log.v("TAG", "mTaxRateValue---- = "+mTaxRateValue);

		float mTaxAmountValue = Float.parseFloat(mTaxAmount);
	   mTotalAmountValue = Float.parseFloat(mTotalAmount);
		mInvoiceNameTv.setText("Invoice #" + mInvoiceNumber);

		// here one is for selection the person name from contact create invoice

		if (mFlourishBaseApplication.mPersonNameFromContact == 1) {
			mCustomerNameEt.setText(mFirstName + " " + mLastName);
		} else {
			mCustomerNameEt.setText(mPersonName);
			// this name is from create invoice activity
		}

		mInvoiceDateTv.setText(MONTHS[mMonth-1 ] + " " + mDate + "," + mYear);
		
		
		mInvoiceTypeNameTv.setText(mInvoiceTypeName);
		
		mInvoiceTypeNameTv.setText("Booking");

		mInvoiceDiscount1EditText.setText(String.format("%.2f", mInvoiceDiscountValue));
		invoice_discount1_value_tv.setText(String.format("$"+"%.2f", mInvoiceDiscountValue));
		invoice_discount1_value_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mInvoiceDiscount1EditText.setVisibility(View.VISIBLE);
				invoice_discount1_value_tv.setVisibility(View.GONE);
				
			}
		});
		
		
		
		mInvoiceDiscount1EditText.setSelection(mInvoiceDiscount1EditText.getText().length());
		

		mAllItemsDiscountTotal=mAllItemsDiscountTotal+mInvoiceDiscountValue;
		Log.v("PRK", "mAllItemsDiscountTotal2   ==="+mAllItemsDiscountTotal);
		
		mInvoiceDiscount2Tv.setText( String.format("%.2f", mAllItemsDiscountTotal));
		
		
		
		mShipping_et.setText(" "+ String.format("%.2f", mShippingValue));
		
		mShipping_et.setSelection(mShipping_et.getText().length());

		// mTaxTv.setText("Tax: ($"+String.format("%.2f",
		// mTaxableTotalValue)+" * "+(mTaxRateValue*100)+"%)");

		mTaxAmountTv.setText("$" + String.format("%.2f", mTaxAmountValue));
		mTotalAmountTv.setText("$" + String.format("%.2f", mTotalAmountValue));
		
		if (mTaxAfterDiscount.equalsIgnoreCase("true")) {
			mTaxAfterDiscountBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.on));
			mIsTaxAfterDiscountChecked = true;
		} else {
			mTaxAfterDiscountBtn.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.off));
			mIsTaxAfterDiscountChecked = false;
		}

		if (mTaxShipping.equalsIgnoreCase("true")) {
			mTaxShippingBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.on));
		} else {
			mTaxShippingBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.off));
		}

		if (mIsDelivered.equalsIgnoreCase("true")) {
			mMarkItemsDeliveredBtn.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.on));
		} else {
			mMarkItemsDeliveredBtn.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.off));
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, mYear,
					mMonth, mDay);

		}

		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;
			if (comparingDate().equalsIgnoreCase("valid")) {
				mInvoiceDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
				mInvoiceDateTv.setText(new StringBuilder()
						.append(MONTHS[mMonth]).append(" ").append(mDay)
						.append(",").append(mYear));

				mBirthDatePicker.init(mYear, mMonth, mDay, null);
			} else if (comparingDate().equalsIgnoreCase("invalid")) {
				mAppNetwork.getAlertDialog(
						SalesInvoiceEditDetails.this,
						getResources().getString(
								R.string.alert_dialog_enter_proper_date));
			}
		}
	};

	/**
	 * Method for comparing two dates
	 * 
	 * @return String (Valid/Invalid)
	 */
	private String comparingDate() {
		String mValidation;
		mInputDate = new Date(mYear, mMonth, mDay + 30);
		Long inputTime = mInputDate.getTime();
		Calendar calendar = Calendar.getInstance();
		Date validDate = new Date(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				(calendar.get(Calendar.DAY_OF_MONTH) + 30));
		Long validTime = validDate.getTime();
		if (validTime >= inputTime) {
			mValidation = "valid";
		} else {
			mValidation = "invalid";
		}
		return mValidation;
	}

	
	/**
	 * AsynTask for making Server request and response for getting Sales Invoice
	 * Items
	 */
	private class GetSalesInvoiceItems extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			
			mInvoiceItemRl.removeAllViews();
		FlourishProgressDialog.ShowProgressDialog(SalesInvoiceEditDetails.this);
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String mTaskUrl_get_sales_invoice_items_url = mFlourishBaseApplication.mFlurishBaseUrl
			+ mFlourishBaseApplication.get_sales_invoice_items_url;
			mSalesInvoiceItemsResponse = mConnectionManager.setUpHttpGet(mTaskUrl_get_sales_invoice_items_url+ mSessionId + "/" + mInvoiceId);
			Log.v("TAG", "==mSalesInvoiceItemsResponse=="+ mSalesInvoiceItemsResponse);
			getSalesInvoiceItems();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			FlourishProgressDialog.Dismiss(SalesInvoiceEditDetails.this);
			if (mSalesInvoiceItemsResponse!=null) {
				if (mSalesInvoiceItemsResponse.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
					.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesInvoiceEditDetails.this,LoginScreen.class));
				} 
				else if (mSalesInvoiceItemsResponse.contains("Bad Parameters"))
				
				{
					mAppNetwork.getAlertDialog(SalesInvoiceEditDetails.this,getString(R.string.alert_dialog_update_invoice_unsuccess));
				} 
				
				else 
				{
					setSalesInvoiceItems();
					mAddLineItemEt.setText("");
					getSharedPreferences("ChooseProduct", 0).edit()
					.putString("productName", "nothing").commit();
					getSharedPreferences("ChooseProduct", 0).edit()
				    .putString("productId", "nothing").commit();
					getSharedPreferences("ChooseProduct", 0).edit()
					.putString("productQuantity", "nothing").commit();

					//TaxAfterDiscount();
					new UpdateInvoiceTask().execute();

				}
				
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
			 mJsonObj = new JSONObject(mSalesInvoiceItemsResponse);
			JSONArray mJsonArray;
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i = 0; i < mJsonArray.length(); i++) {
				mInvoiceLineItem = new InvoiceLineItem();
				mInvoiceLineItem.InvoiceLineItemId = mJsonArray
				.getJSONObject(i).getString("Id");
				mInvoiceLineItem.InvoiceItemTypeId = mJsonArray.getJSONObject(i).getString("InvoiceItemTypeId");
				Log.v("PRK", "Invoice line item Id "+ mInvoiceLineItem.InvoiceLineItemId);
				Log.v("PRK", "Invoice line item product Id "+ mJsonArray.getJSONObject(i).getString("ProductId"));

				mInvoiceLineItem.Name = mJsonArray.getJSONObject(i).getString("Name");
				mInvoiceLineItem.ItemTypeName = mJsonArray.getJSONObject(i).getString("ItemTypeName");
				mInvoiceLineItem.Retail = mJsonArray.getJSONObject(i).getString("Retail");
				mInvoiceLineItem.OnHand = mJsonArray.getJSONObject(i).getString("OnHand").replaceAll("-", "");
				
				Log.v("PRK", "mInvoiceLineItem.OnHand "+mInvoiceLineItem.OnHand );
				
				mInvoiceLineItem.DiscountAmount = mJsonArray.getJSONObject(i).getString("DiscountAmount").replaceAll("-", "");

				float  lineItemDiscount=Float.parseFloat(mInvoiceLineItem.DiscountAmount);
				
				mAllItemsDiscountTotal=lineItemDiscount+mAllItemsDiscountTotal;
				Log.v("PRK", "mAllItemsDiscountTotal"+mAllItemsDiscountTotal);
				
				
				
				
				if (mJsonArray.getJSONObject(i).getString("Quantity").contains("-")) {
					mInvoiceLineItem.Quantity = mJsonArray.getJSONObject(i).getString("Quantity").replaceAll("-", "");
					Log.v("PRK", "mInvoiceLineItem.Quantity---"+mInvoiceLineItem.Quantity);
				} else {
					mInvoiceLineItem.Quantity = mJsonArray.getJSONObject(i).getString("Quantity");
					Log.v("PRK", "mInvoiceLineItem.Quantity"+mInvoiceLineItem.Quantity);
				}

				if (mJsonArray.getJSONObject(i).getString("Discount")
						.contains("-")) {mInvoiceLineItem.Discount = mJsonArray.getJSONObject(i).getString("Discount")
							.substring(1,mJsonArray.getJSONObject(i).getString("Discount").length());
						
						
						
				} else {
					mInvoiceLineItem.Discount = mJsonArray.getJSONObject(i).getString("Discount");
				}

				mInvoiceLineItem.SubTotal = mJsonArray.getJSONObject(i)
						.getString("SubTotal");

				mAllInvoiceLIstItemsList.add(mInvoiceLineItem);
				callServiceListItemEditText.add(true);
				callServiceEditText.add(true);
			}

			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.v("TAG", "exception in ----" + e.toString());
		}
	
		
		}

	/**
	 * Method for setting added Sales Invoice Items
	 */
	
	
//	getView
	private void setSalesInvoiceItems() {

		Log.v("TAG", "Array list size()1 " + mAllInvoiceLIstItemsList.size());

		if ((mAllInvoiceLIstItemsList.size() != 0)) {

			Log.v("TAG",
					"Array list size()2 " + mAllInvoiceLIstItemsList.size());
			int mPosition = 0;
			
			
			for (mPosition=0; mPosition < mAllInvoiceLIstItemsList.size(); mPosition++) {
				Log.v("TAG",
						"Array list size() 3" + mAllInvoiceLIstItemsList.size());

				float retailfloat;

				int quantityInt = 0;
				float mDiscountFloat;
				final String mInviceItemTypeId;
				final String mInvoiceItemId;
				final View view;
			
			
				// final String mInvoiceItemId ;
				mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mInflater.inflate(R.layout.sales_invoice_items_row, null);
				
				view.setTag(mPosition);

				LinearLayout msales_invoice_item_ll = (LinearLayout) view.findViewById(R.id.sales_invoice_item_ll);
				TextView invoiceItemName = (TextView) view.findViewById(R.id.sales_invoice_item_name_tv);
				TextView retail = (TextView) view.findViewById(R.id.sale_type_value);
				TextView retail_dublicate = (TextView) view.findViewById(R.id.sale_type_value2);

				final EditText mInvoiceItemQuantity_EditText1 = (EditText)view.findViewById(R.id.sale_type_quantity_et);
				mInvoiceItemQuantity_EditText1.setSelection(mInvoiceItemQuantity_EditText1.length());
			
				
				final EditText mInvoiceItemQuantity_EditText2 = (EditText) view.findViewById(R.id.sale_type_quantity_et2);
				mInvoiceItemQuantity_EditText1.addTextChangedListener(new CustomTextWatcher(mInvoiceItemQuantity_EditText1));
				mInvoiceItemQuantity_EditText2.addTextChangedListener(new CustomTextWatcher(mInvoiceItemQuantity_EditText2));
				mInvoiceItemQuantity_EditText2.setSelection(mInvoiceItemQuantity_EditText2.length());
				
				final TextView mInvoiceItemTotal_TextView = (TextView) view.findViewById(R.id.sale_type_total_amount);
				final TextView mInvoiceItemTotal2_TextView = (TextView) view.findViewById(R.id.sale_type_total_amount2);
				final EditText mInvoiceItemDiscount_EditText = (EditText) view.findViewById(R.id.Dicount_et_sales_invoice_row_ET);
				mInvoiceItemDiscount_EditText.setSelection(mInvoiceItemDiscount_EditText.length());
				
				mInvoiceItemDiscount_EditText.addTextChangedListener(new CustomTextWatcher(mInvoiceItemDiscount_EditText));
				
				final LinearLayout salas_invoice_item_details_ll = (LinearLayout) view.findViewById(R.id.salas_invoice_item_details_ll);
				final LinearLayout simple_display_View_linear_layout = (LinearLayout) view.findViewById(R.id.simple_display_View_linear_layout);
				final Button invoiceItemBtn = (Button) view.findViewById(R.id.sales_invoice_items_minus);
				final Button invoiceItemDeleteBtn = (Button) view.findViewById(R.id.sales_invoice_item_delete_btn);

				String mSubtotalString = mAllInvoiceLIstItemsList.get(mPosition).SubTotal;
				String retailStr = mAllInvoiceLIstItemsList.get(mPosition).Retail;
				String discountStr = mAllInvoiceLIstItemsList.get(mPosition).DiscountAmount.replaceAll("-", "");
				mInvoiceItemId = mAllInvoiceLIstItemsList.get(mPosition).InvoiceLineItemId;
				mInviceItemTypeId = mAllInvoiceLIstItemsList.get(mPosition).InvoiceItemTypeId;
			   String onHandString=mAllInvoiceLIstItemsList.get(mPosition).OnHand;
				final int   mOnHand=Integer.parseInt(onHandString);
	
				Log.v("TAG", "ONHand VALUE=="+mOnHand);
				if (retailStr.contains("null")) {
					retailfloat = 0;
					retail.setText("$ " + 0);
					retail_dublicate.setText("$ " + 0);

				} 
				else {

					retailfloat = Float.parseFloat(retailStr);
					retail.setText("$ " + String.format("%.2f", retailfloat));
					retail_dublicate.setText("$ "+ String.format("%.2f", retailfloat).replaceAll("-", ""));

				}

				if (mSubtotalString.contains("null")) {

					subtotalfloal = 0;
					mInvoiceItemTotal_TextView.setText("$ " + subtotalfloal);
					mInvoiceItemTotal2_TextView.setText("$ " + subtotalfloal);
				} else {

					subtotalfloal = Float.parseFloat(mSubtotalString);
					mInvoiceItemTotal_TextView.setText("$"+ String.format("%.2f", subtotalfloal).replace("-",""));
					mInvoiceItemTotal2_TextView.setText("$"+ String.format("%.2f", subtotalfloal).replace("-",""));

				}
				invoiceItemName.setText(mAllInvoiceLIstItemsList.get(mPosition).Name);
				String mInvoiceItemSaleType = mAllInvoiceLIstItemsList.get(mPosition).InvoiceItemTypeId;
				String mInvoiceItemQuantity = mAllInvoiceLIstItemsList.get(mPosition).Quantity.replaceAll("-", "");

				if (mInvoiceItemQuantity != null) {

					mInvoiceItemQuantity_EditText1.setText("" + mAllInvoiceLIstItemsList.get(mPosition).Quantity.replaceAll("-", ""));
					mInvoiceItemQuantity_EditText2.setText("" + mAllInvoiceLIstItemsList.get(mPosition).Quantity.replaceAll("-", ""));
					quantityInt = Integer.parseInt(mInvoiceItemQuantity);
				} 
				else {
					mInvoiceItemQuantity_EditText1.setText("0");
					mInvoiceItemQuantity_EditText2.setText("0");
				}
				
				if (discountStr.contains("null")) {
					mDiscountFloat = 0;
					mInvoiceItemDiscount_EditText.setText("$ " + mDiscountFloat);
				}

				else {

					mDiscountFloat = Float.parseFloat(discountStr);
					mInvoiceItemDiscount_EditText.setText("" + String.format("%.2f", mDiscountFloat));

				}
				
				

				
				mInvoiceItemRl.addView(view);
			
				invoiceItemBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						TranslateAnimation moveRighttoLeft = new TranslateAnimation(
								200, 0, 0, 0);
						moveRighttoLeft.setDuration(1000);
						moveRighttoLeft.setFillAfter(true);

						TranslateAnimation moveLefttoRight = new TranslateAnimation(
								0, 200, 0, 0);
						moveLefttoRight.setDuration(1000);
						moveLefttoRight.setFillAfter(true);

						if (mIsChecked) {
							invoiceItemDeleteBtn
									.startAnimation(moveLefttoRight);
							invoiceItemDeleteBtn.setVisibility(View.GONE);
							mIsChecked = false;
						} else {
							invoiceItemDeleteBtn
									.startAnimation(moveRighttoLeft);
							invoiceItemDeleteBtn.setVisibility(View.VISIBLE);
							mIsChecked = true;
						}
					}
				});

				invoiceItemDeleteBtn.setOnClickListener(new OnClickListener() {

				
					
					public void onClick(View v) {
						

						int pos=(Integer)view.getTag();
						
						Log.v("TAG", "pos---"+pos);
						Log.v("TAG", "Invoice item id ---"+mAllInvoiceLIstItemsList.get(pos).InvoiceLineItemId);
						// mInvoiceItemId = mAllInvoiceLIstItemsList.get(pos).InvoiceItemTypeId;

						if (mAppNetwork
								.isNetworkAvailable(SalesInvoiceEditDetails.this)) {
							
							new DeleteLineItemTask().execute(mAllInvoiceLIstItemsList.get(pos).InvoiceLineItemId);
						} else {
							mAppNetwork
									.getAlertDialog(
											SalesInvoiceEditDetails.this,
											getString(R.string.alert_dialog_no_network));
						}
					}
				});

				// click listener on layout
				

				msales_invoice_item_ll
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								if (salas_invoice_item_details_ll
										.getVisibility() == View.VISIBLE) {

									salas_invoice_item_details_ll
											.setVisibility(View.GONE);
									simple_display_View_linear_layout
											.setVisibility(View.VISIBLE);

								}

								else {

									salas_invoice_item_details_ll
											.setVisibility(View.VISIBLE);
									simple_display_View_linear_layout
											.setVisibility(View.GONE);
								}

							}
						});
				
				mInvoiceItemQuantity_EditText1.setOnEditorActionListener(
				        new EditText.OnEditorActionListener() {
				    @Override
				    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
				        	
				        	//Toast.makeText(SalesInvoiceEditDetails.this, "checking event", Toast.LENGTH_LONG).show();
				        	
				        	mInvoiceItemQuantity_EditText1.addTextChangedListener(new CustomTextWatcher(mInvoiceItemQuantity_EditText1));
				         String edittextQuantity_String=mInvoiceItemQuantity_EditText1.getText().toString().trim();
				        	
				         String enteredvalueStr=mInvoiceItemQuantity_EditText1.getText().toString().trim();
				         int enteredValueInt=Integer.parseInt(enteredvalueStr);
				        	if (mOnHand<enteredValueInt) {
								
				        		Toast.makeText(SalesInvoiceEditDetails.this, "Qunatity is exiceed...!", 500).show();
							}
				         
				        	else{
				         
				        		
				        		
				        		if (mIsChecked) {
									
								}
				        		else
				        		{
				        			
				        		}
				        		
							new UpdateLineItemInvoiceAsyncTask()
									.execute(
											mInvoiceItemId,
											mInviceItemTypeId,
											mInvoiceItemDiscount_EditText.getEditableText().toString().trim(),
											edittextQuantity_String,
											
											
											mInvoiceItemQuantity_EditText1,
											mInvoiceItemQuantity_EditText2,
											mInvoiceItemDiscount_EditText,
											mInvoiceItemTotal_TextView,
											mInvoiceItemTotal2_TextView,
											(Integer) view.getTag());
				        	

				        	//hide the keyboard after done
							InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(mInvoiceItemQuantity_EditText2.getWindowToken(), 0);
				        	
				          
				            return true;
				            
				            
				        	}
				        	
				        	
				        	
				        }
				        return false;
				    }
				});
				
				
				

				mInvoiceItemQuantity_EditText2.setOnEditorActionListener(
				        new EditText.OnEditorActionListener() {
				    @Override
				    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
				        	
				        	//Toast.makeText(SalesInvoiceEditDetails.this, "checking event", Toast.LENGTH_LONG).show();
				        	
				         String edittextQuantity_String=mInvoiceItemQuantity_EditText2.getText().toString().trim();
				       
				      
				         int enteredValueInt=Integer.parseInt(edittextQuantity_String);
				        	if (mOnHand<enteredValueInt) {
								
				        		Toast.makeText(SalesInvoiceEditDetails.this, "Qunatity is exiceed...!", 500).show();
							}
				         
				         
				         
				        	else{
				        	
							new UpdateLineItemInvoiceAsyncTask()
									.execute(
											mInvoiceItemId,
											mInviceItemTypeId,
											mInvoiceItemDiscount_EditText.getEditableText().toString().trim(),
											edittextQuantity_String,
											
											
											mInvoiceItemQuantity_EditText1,
											mInvoiceItemQuantity_EditText2,
											mInvoiceItemDiscount_EditText,
											mInvoiceItemTotal_TextView,
											mInvoiceItemTotal2_TextView,
											(Integer) view.getTag());
				        	

				        	//hide the keyboard after done
							InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(mInvoiceItemQuantity_EditText2.getWindowToken(), 0);
				        }
				          
				            return true;
				        }
				        return false;
				    }
				});
				
			

				// Discount text change listener
			
				
				mInvoiceItemDiscount_EditText.setOnEditorActionListener(
				        new EditText.OnEditorActionListener() {
				    @Override
				    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
				        	
				        	//Toast.makeText(SalesInvoiceEditDetails.this, "checking event", Toast.LENGTH_LONG).show();
				        	
				         String edittextQuantity=	mInvoiceItemQuantity_EditText1.getText().toString().trim();
				        	
				        	mQuantityString = mInvoiceItemQuantity_EditText1.getText().toString().trim();
                             
				        	String mEnteredAmount=mInvoiceItemDiscount_EditText.getText().toString();
				        	float mEnteredAmountFlout=Float.parseFloat(mEnteredAmount);
				        	
				        	
				        	Log.v("TAG", "subtotalfloal==     ="+subtotalfloal);
				        	Log.v("TAG", "mEnteredAmountFlout=="+mEnteredAmountFlout);
				        	
							if (subtotalfloal>mEnteredAmountFlout) {
						
				        	new UpdateLineItemInvoiceDiscountAsyncTask()
							.execute(
									mInvoiceItemId,
									mInviceItemTypeId,
									mInvoiceItemDiscount_EditText.getEditableText()
											.toString().trim(),
									edittextQuantity,
									mInvoiceItemQuantity_EditText1,
									mInvoiceItemDiscount_EditText,
									mInvoiceItemTotal_TextView,
									mInvoiceItemTotal2_TextView,
									(Integer) view.getTag());
				      
				        	//hide the keyboard after done
				        	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(mInvoiceItemQuantity_EditText2.getWindowToken(), 0);
				        	
							}
							else{
								
								Toast.makeText(SalesInvoiceEditDetails.this, "Discount exiceed", 500).show();
								
							}
							
							
							
							
							
							
							
							
							
							
							
				          
				            return true;
				        }
				        return false;
				    }
				});
				

			}

			Log.v("TAG", "Array list size()" + mAllInvoiceLIstItemsList.size());

		}
	}

	/**
	 * AsynTask for making Server request and response for adding Sales Invoice
	 * Line Items
	 */
	public class AddLineItemTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		
			
			
			FlourishProgressDialog.ShowProgressDialog(SalesInvoiceEditDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String mTaskUrl_create_line_item_for_invoice_url = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.create_line_item_for_invoice_url;
			putAddLineItemRequest();
			mAddLineItemResponse = mConnectionManager.setUpHttpPut(
					mTaskUrl_create_line_item_for_invoice_url + mSessionId,
					mAddLineItemRequestObj.toString());
			Log.v("response", "==mCreateInvoiceItemResponse=="
					+ mAddLineItemResponse);
			getAddLineItemResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			FlourishProgressDialog.Dismiss(SalesInvoiceEditDetails.this);

				/*
				 * mInvoiceItemIdList = new ArrayList<String>();
				 * mInvoiceItemNameList = new ArrayList<String>();
				 * mInvoiceItemSaleTypeList = new ArrayList<String>();
				 * mInvoiceItemRetailList = new ArrayList<String>();
				 * mInvoiceItemQuantityList = new ArrayList<String>();
				 * mInvoiceItemDiscountList = new ArrayList<String>();
				 * mInvoiceItemTotalList = new ArrayList<String>();
				 */

				// mInvoiceItemRl.removeAllViews();
				mAllInvoiceLIstItemsList = new ArrayList<InvoiceLineItem>();

				if (mAddLineItemResponse.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
							.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesInvoiceEditDetails.this,
							LoginScreen.class));
				} else if (mAddLineItemResponse.contains("Bad Parameters")) {
					mAppNetwork
							.getAlertDialog(
									SalesInvoiceEditDetails.this,
									getString(R.string.alert_dialog_update_invoice_unsuccess));
				}

				else if (mAppNetwork
						.isNetworkAvailable(SalesInvoiceEditDetails.this)) {
					new GetSalesInvoiceItems().execute();
				} else {
					mAppNetwork.getAlertDialog(SalesInvoiceEditDetails.this,
							getString(R.string.alert_dialog_no_network));
				}
				// }
				super.onPostExecute(result);
			}
		}
	

	/**
	 * Method for making JSON Object Server request for adding Sales Invoice
	 * Line Items
	 */
	private void putAddLineItemRequest() {
		try {
			mAddLineItemRequestObj = new JSONObject();
			mAddLineItemRequestObj.put("InvoiceId", mInvoiceId);
			mAddLineItemRequestObj.put("ProductId", mProductId);
			//mAddLineItemRequestObj.put("Quantity", mProductQuantity);
			mAddLineItemRequestObj.put("Quantity", ""+1);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for parsing Server response for adding Sales Invoice Line Items
	 */
	private void getAddLineItemResponse() {
		try {
			JSONObject mJsonObj = new JSONObject(mAddLineItemResponse);
			mAddLineItemAuthResponse = String.valueOf(mJsonObj.getJSONArray(
					"data").length());
			Log.v("response", "==mAddLineItemAuthResponse=="
					+ mAddLineItemAuthResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * AsynTask for making Server request and response for updating invoice
	 */
	public class UpdateInvoiceTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String mTaskUrl_update_invoice_url = mFlourishBaseApplication.mFlurishBaseUrl+ mFlourishBaseApplication.updates_an_invoices;
			putUpdateInvoiceRequest();
			mUpdateInvoiceResponse = mConnectionManager.setUpHttpPost(mTaskUrl_update_invoice_url + mSessionId+"/" + mInvoiceId,mUpdateInvoiceRequestObj.toString());
			Log.v("response", "==mUpdateInvoiceResponse=="+ mUpdateInvoiceResponse);
			return null;
			
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if (mUpdateInvoiceResponse!=null) {
				
			
			
				if (mUpdateInvoiceResponse.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
							.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesInvoiceEditDetails.this,LoginScreen.class));
					
				} else if (mUpdateInvoiceResponse.contains("Bad Parameters")) {
					mAppNetwork
							.getAlertDialog(
									SalesInvoiceEditDetails.this,
									getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				
				else {
					
					//
				try {
					
				
					
				JSONObject mCompleteJsonObject=new JSONObject(mUpdateInvoiceResponse);
                   
				JSONObject json=mCompleteJsonObject.getJSONObject("data");
				
				String mTotal=json.getString("Total");
				
				Log.v("TAG", "Total---=="+mTotal);
				
				
				mTotalAmountTv.setText("$"+json.getString("Total").replaceAll("-", ""));
				mTaxAmountTv.setText("$"+json.getString("TaxAmount1").replaceAll("-", "")); 
				
				String TaxRate1=json.getString("TaxRate1");
				String TaxableTotal1 =json.getString("TaxableTotal1");
				
		
			
				mTaxTv.setText("Tax: ($"+TaxableTotal1+" x "+TaxRate1+"%)");
				
				  mTotalAmountValue = Float.parseFloat(mTotal);
				  
				
				
				
				} catch (Exception e) {
					// TODO: handle exception
				}
					
				
					
					
					
					
					
					
					
				}
			
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for making JSON Object Server request for adding Sales Invoice
	 * Line Item
	 */
	private void putUpdateInvoiceRequest() {
		try {
			mUpdateInvoiceRequestObj = new JSONObject();
			mUpdateInvoiceRequestObj.put("PersonId", mPersonId);
			mUpdateInvoiceRequestObj.put("InvoiceTypeId", mInvoiceTypeId);
			mUpdateInvoiceRequestObj.put("InvoiceDate", mInvoiceDate);
			mUpdateInvoiceRequestObj.put("BookingCalendarId", "0");
			
		

			/*if (mInvoiceTypeStr.equalsIgnoreCase("Sale")) {
				mUpdateInvoiceRequestObj.put("InvoiceTypeId", "7");
			} else if (mInvoiceTypeStr.equalsIgnoreCase("Booking")) {
				mUpdateInvoiceRequestObj.put("InvoiceTypeId", "16");
			} else if (mInvoiceTypeStr.equalsIgnoreCase("Return")) {
				mUpdateInvoiceRequestObj.put("InvoiceTypeId", "17");
			}
		
			
		
		*/
		
		
		
		
		} catch (JSONException e) {
			e.printStackTrace();
			
			Log.v("TAG", "exception "+e.toString());
			
		}
	}

	/**
	 * AsynTask for making Server request and response for deleting Sales
	 * Invoice Line Item
	 */
	public class DeleteLineItemTask extends AsyncTask<Object, Void, String> {
		
		
		
		String invoiceItemId = null;
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
		}

		protected String doInBackground(Object... params) {
			
			
			
			invoiceItemId = (String)params[0];
			
			String mTaskUrl_create_line_item_for_invoice_url = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.create_line_item_for_invoice_url;
			mDeleteLineItemResponse = mConnectionManager
					.setUpHttpDelete(mTaskUrl_create_line_item_for_invoice_url
							+ mSessionId + "/" + invoiceItemId);
			Log.v("response", "==mDeleteLineItemResponse=="
					+ mDeleteLineItemResponse);
			getDeleteLineItemResponse();
			return mDeleteLineItemResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			
			
			FlourishProgressDialog.Dismiss(SalesInvoiceEditDetails.this);
			
				Log.v("response", "==mDeleteLineItemAuthResponse=="
						+ mDeleteLineItemAuthResponse);
			

				mAllInvoiceLIstItemsList = new ArrayList<InvoiceLineItem>();
				mInvoiceItemRl.removeAllViews();

				if (result.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
							.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesInvoiceEditDetails.this,
							LoginScreen.class));
				} else if (result.contains("Bad Parameters")) {
					mAppNetwork
							.getAlertDialog(
									SalesInvoiceEditDetails.this,
									getString(R.string.alert_dialog_update_invoice_unsuccess));
				}

				else if (mAppNetwork
						.isNetworkAvailable(SalesInvoiceEditDetails.this)) {
					new GetSalesInvoiceItems().execute();
					
				} else {
					mAppNetwork.getAlertDialog(SalesInvoiceEditDetails.this,
							getString(R.string.alert_dialog_no_network));
				}
		
			super.onPostExecute(result);
		}
	}

	/**
	 * Method for parsing Server response for deleting Invoice Line Item
	 */
	private void getDeleteLineItemResponse() {
		try {
			JSONObject mJsonObj = new JSONObject(mDeleteLineItemResponse);
			mDeleteLineItemAuthResponse = (mJsonObj.getString("AuthSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * AsynTask for making Server request and response for deleting Sales
	 * Invoice Item
	 */
	public class DeleteInvoiceTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			FlourishProgressDialog.ShowProgressDialog(SalesInvoiceEditDetails.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String mTaskUrl_delete_invoice_url = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.delete_invoice_url;
			mDeleteInvoiceResponse = mConnectionManager
					.setUpHttpDelete(mTaskUrl_delete_invoice_url + mSessionId
							+ "/" + mInvoiceId);
			Log.v("response", "==mDeleteLineItemResponse=="
					+ mDeleteInvoiceResponse);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			FlourishProgressDialog.Dismiss(SalesInvoiceEditDetails.this);

				if (mDeleteInvoiceResponse.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
							.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesInvoiceEditDetails.this,
							LoginScreen.class));
				} else if (mDeleteInvoiceResponse.contains("Bad Parameters")) {
					mAppNetwork
							.getAlertDialog(
									SalesInvoiceEditDetails.this,
									getString(R.string.alert_dialog_update_invoice_unsuccess));
				} else {
					final Dialog dialog = new Dialog(
							SalesInvoiceEditDetails.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.invoice_deleted_success_dialog);

					Button mYes = (Button) dialog
							.findViewById(R.id.dialogButtonYES);

					mYes.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.cancel();

							startActivity(new Intent(
									SalesInvoiceEditDetails.this,
									FlourishHomeActivity.class));

							SharedPreferences.Editor editor = mSharedPreferences
									.edit();
							editor.putString("FragmentScreen", "Dashboard");
							editor.commit();

							finish();
						}
					});
					dialog.show();
				}
		
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSelectedProduct = getSharedPreferences("ChooseProduct", 0).getString(
				"productName", "nothing");
		if (!(mSelectedProduct.equalsIgnoreCase("nothing"))) {
			mAddLineItemEt.setText(mSelectedProduct);
			mProductId = getSharedPreferences("ChooseProduct", 0).getString(
					"productId", "nothing");
			mProductQuantity = getSharedPreferences("ChooseProduct", 0)
					.getString("productQuantity", "nothing");
			if (mAppNetwork.isNetworkAvailable(SalesInvoiceEditDetails.this)) {
				new AddLineItemTask().execute();
			} else {
				mAppNetwork.getAlertDialog(SalesInvoiceEditDetails.this,
						getString(R.string.alert_dialog_no_network));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.dollar_percentage_toggle_button:
			
			
			if (mDollarPrecentageToggleButton.isChecked()) {
			//	Toast.makeText(this, ""+true, 500).show();
				
				
				new PercentDiscount().execute();
				 mInvoiceDisStr=mInvoiceDiscount1EditText.getText().toString();
				if (mInvoiceDisStr.contains(".")) {
					
					invoice_discount1_value_tv.setText("%"+mInvoiceDiscount1EditText.getText().toString());
					
				}
				else{
					invoice_discount1_value_tv.setText("%"+mInvoiceDiscount1EditText.getText().toString()+".00");
				}
				
				
			}
			
			else
			{
				new DollarDiscount().execute();
				//Toast.makeText(this, ""+false, 500).show();
				
				if (mInvoiceDisStr.contains(".")) {
					
					invoice_discount1_value_tv.setText("$"+mInvoiceDiscount1EditText.getText().toString());
					
				}
				else{
					invoice_discount1_value_tv.setText("$"+mInvoiceDiscount1EditText.getText().toString()+".00");
				}
				
				
				
			}
			
			break;
		
		
		case R.id.sales_details_add_line_item_fields:
			startActivity(new Intent(SalesInvoiceEditDetails.this,
					ChooseProduct.class));
			break;

		case R.id.sales_details_add_line_item_btn:
			startActivity(new Intent(SalesInvoiceEditDetails.this,
					ChooseProduct.class));
			break;

		case R.id.sales_details_invoice_type_et:
			mCustomDialog.show();
			break;

		case R.id.add_invoice_booking_et:

			Intent bookingIntent = new Intent(SalesInvoiceEditDetails.this,BookingsActivity.class);
			// startActivity(mIntent);
			startActivityForResult(bookingIntent, SELECT_BOOKINGG_ID);

			break;

		case R.id.tax_after_discount_btn:
			if (mIsTaxAfterDiscountChecked) {
				mTaxAfterDiscountBtn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.on));
				mIsTaxAfterDiscountChecked=false;
				//TaxAfterDiscount();
				
				
				
			} else {
				mTaxAfterDiscountBtn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.off));
				mIsTaxAfterDiscountChecked=true;
				//TaxAfterDiscount();
				
			}
			break;
		
			
			
		case R.id.tax_shipping_btn:
			if (mIsTaxShippingChecked) {
				mTaxShippingBtn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.on));
				mIsTaxShippingChecked = false;
				//TaxShiping();
				
				
				
			} else {
				mTaxShippingBtn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.off));
				mIsTaxShippingChecked = true;
				//TaxShiping();
				
			}
			break;

		case R.id.mark_items_delivered_btn:
			if (mIsMarkItemsDeliveredChecked) {

				mMarkItemsDeliveredBtn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.on));
				mIsMarkItemsDeliveredChecked = false;
			} else {
				mMarkItemsDeliveredBtn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.off));
				mIsMarkItemsDeliveredChecked = true;
			}
			break;

		case R.id.sales_details_delete_btn:
			// Custom Dialog for exiting the application
			final Dialog dialog = new Dialog(SalesInvoiceEditDetails.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.delete_invoice_dialog);

			Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);
			Button mNo = (Button) dialog.findViewById(R.id.dialogButtonNO);

			mYes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (mAppNetwork
							.isNetworkAvailable(SalesInvoiceEditDetails.this)) {
						new DeleteInvoiceTask().execute();
					} else {
						mAppNetwork.getAlertDialog(
								SalesInvoiceEditDetails.this,
								getString(R.string.alert_dialog_no_network));
					}
				}

			});
			mNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			break;

		case R.id.sales_details_back:
/*
			SharedPreferences.Editor backeditor = mSharedPreferences.edit();
			backeditor.putString("FragmentScreen", "Sales");
			backeditor.commit();

			startActivity(new Intent(SalesInvoiceEditDetails.this,
					FlourishHomeActivity.class));
			*/
			
			finish();
			break;

		case R.id.sales_details_calendar_btn:
			showDialog(DATE_DIALOG_ID);
			break;

		case R.id.sales_details_save:

			

			Intent mIntent = new Intent(SalesInvoiceEditDetails.this,
					SaveInvoiceDialogActivity.class);

			startActivityForResult(mIntent, 100);

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			/*
			 * startActivity(new Intent(SalesInvoiceEditDetails.this,
			 * FlourishHomeActivity.class)); finish();
			 */
			
			finish();
			
			
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case SELECT_BOOKINGG_ID:
			if ((resultCode == RESULT_OK)) {

				if (data != null) {

					String mBookings = data.getAction();

					String[] str_array = mBookings.split(",");
					 mBookingCalenderId = str_array[0];
					String mBookingSubject = str_array[1];
					Log.v("TAG", "mBookings  in Create invoice " + mBookings);
					add_invoice_booking_et.setText(mBookingSubject);

				}

				else {
					// Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
				}

			}

			break;

		case 100:

			if (resultCode == RESULT_OK) {
		 String amount=mTotalAmountTv.getText().toString().replaceAll("$", "");
				if (data != null) {

					SaveInvoiceType = data.getAction().toString();
				//	Toast.makeText(SalesInvoiceEditDetails.this,"" + SaveInvoiceType, 500).show();

					if (SaveInvoiceType.equalsIgnoreCase("Save as Draft")) {

						SharedPreferences.Editor backeditor = mSharedPreferences.edit();
						backeditor.putString("FragmentScreen", "Sales");
						backeditor.commit();

						startActivity(new Intent(SalesInvoiceEditDetails.this,
								FlourishHomeActivity.class));
						finish();

					}

					else if (SaveInvoiceType.equalsIgnoreCase("Create Invoice")) {

						SharedPreferences.Editor backeditor = mSharedPreferences
								.edit();
						backeditor.putString("FragmentScreen", "Sales");
						backeditor.commit();

						finalizeInvoice();
						//

						if (mFinalizeInvoiceResponse != null) {

							startActivity(new Intent(
									SalesInvoiceEditDetails.this,
									FlourishHomeActivity.class));
							finish();

						}
						

						else {
							//Toast.makeText(SalesInvoiceEditDetails.this,"Invoice Not Created", 500).show();
							startActivity(new Intent(
									SalesInvoiceEditDetails.this,
									FlourishHomeActivity.class));
							finish();

						}

					}

					else if (SaveInvoiceType
							.equalsIgnoreCase("create and payment")) {

						finalizeInvoice();
                     
						startActivity(new Intent(SalesInvoiceEditDetails.this,AddPayments.class)
						.putExtra("invoiceId",mInvoiceId)
						.putExtra("amount","" + amount)
						.putExtra("invoiceNumber","" + mInvoiceNumber));

						finish();
					}

					
					else if (SaveInvoiceType.equalsIgnoreCase("Create and add delevery")) {
						
						finalizeInvoice();
						startActivity(new Intent(SalesInvoiceEditDetails.this,AddDeliveries.class)
						.putExtra("invoiceId",mInvoiceId)
						.putExtra("amount","" + amount)
						.putExtra("invoiceNumber","" + mInvoiceNumber));
					
						finish();

					
					
					}
			
				}

				else {
					Toast.makeText(SalesInvoiceEditDetails.this, "No data ",
							500).show();

				}
			}
		default:
			break;
		}

	}

	private void finalizeInvoice() {
		// TODO Auto-generated method stub

		mTaskUrlFinalizeInvoce = mFlourishBaseApplication.mFlurishBaseUrl
				+ mFlourishBaseApplication.Sales_Invoice_Finalize;

		ConnectionManager mConnectionManager = new ConnectionManager();

		mFinalizeInvoiceResponse = mConnectionManager
				.setUpHttpGet(mTaskUrlFinalizeInvoce + mSessionId + "/"
						+ mInvoiceId);

		Log.v("TAG", "mFinalizeInvoiceResponse----" + mFinalizeInvoiceResponse);

	}

	private class UpdateLineItemInvoiceMainAsyncTask extends
			AsyncTask<Object, Void, String> {

		// String invoiceItemId_string = null, invoiceItemTypeId_string = null,
		// discount_string = null, quantity_string = null;

		EditText sale_type_quantity_et = null;
		EditText Dicount_et_sales_invoice_row_ET = null;
		TextView invoiceItemTotal_TextView = null;
		TextView invoiceItemTotal2TextView = null;

		int position = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			FlourishProgressDialog.ShowProgressDialog(SalesInvoiceEditDetails.this);
		
		
		}

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub

			sale_type_quantity_et = (EditText) params[4];
			Dicount_et_sales_invoice_row_ET = (EditText) params[5];
			invoiceItemTotal_TextView = (TextView) params[6];
			invoiceItemTotal2TextView = (TextView) params[7];

			position = (Integer) params[8];
			String reponse = updatesAlineItemOnInvoice((String) params[0],
					(String) params[1], (String) params[2], (String) params[3]);
			return reponse;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			FlourishProgressDialog.Dismiss(SalesInvoiceEditDetails.this);
				JSONObject mCompleteJsonObject;

				if (result != null) {
					try {

						mCompleteJsonObject = new JSONObject(result);
						JSONObject datajsonObject = mCompleteJsonObject
								.getJSONObject("data");

						invoiceItemTotal_TextView.setText(datajsonObject
								.getString("SubTotal"));
						invoiceItemTotal2TextView.setText(datajsonObject
								.getString("SubTotal"));

						// sale_type_quantity_et.setText(datajsonObject
						// .getString("Quantity"));

						callServiceListItemEditText.remove(position);
						callServiceListItemEditText.add(position, false);

						sale_type_quantity_et.setText(datajsonObject
								.getString("Quantity"));
						new UpdateInvoiceTask().execute();

					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				Log.v("TAG", "Response----->" + result);
		

			// Log.v("TAG", "result  "+result.toString());

		}
	}
	
	

	
	
	

	private class UpdateLineItemInvoiceAsyncTask extends
			AsyncTask<Object, Void, String> {

		// String invoiceItemId_string = null, invoiceItemTypeId_string = null,
		// discount_string = null, quantity_string = null;

		EditText mInvoiceItemQuantity_et1 = null;
		EditText mInvoiceItemQuantity_et2 = null;
		EditText mInvoiceItemDicount_et = null;
		TextView mInvoiceItemTotal1_TextView = null;
		TextView mInvoiceItemTotal2_TextView = null;

		int position = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			
		}

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub

			mInvoiceItemQuantity_et1 = (EditText) params[4];
			mInvoiceItemQuantity_et2 = (EditText) params[5];
			mInvoiceItemDicount_et = (EditText) params[6];
			mInvoiceItemTotal1_TextView = (TextView) params[7];
			mInvoiceItemTotal2_TextView = (TextView) params[8];
			
			
			

			position = (Integer) params[9];
			String reponse = updatesAlineItemOnInvoice((String) params[0],
					(String) params[1], (String) params[2], (String) params[3]);
			return reponse;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		

				JSONObject mCompleteJsonObject;

				if (result.contains("Login Key Not Valid")) {
					getSharedPreferences("DataChecking", 0).edit()
					.putString("Data", "no_data").commit();
					startActivity(new Intent(SalesInvoiceEditDetails.this,LoginScreen.class));
				} 
				
				
				
				
				else if (result != null) {
					try {

						mCompleteJsonObject = new JSONObject(result);
						JSONObject datajsonObject = mCompleteJsonObject.getJSONObject("data");
						mInvoiceItemTotal1_TextView.setText("$"+datajsonObject.getString("SubTotal"));
						mInvoiceItemTotal2_TextView.setText("$"+datajsonObject.getString("SubTotal"));
					///	this is for discount comparison 
						String subtotalStr=datajsonObject.getString("SubTotal").replaceAll("-", "");;
						subtotalfloal=Float.parseFloat(subtotalStr);
						
						mInvoiceItemQuantity_et1.setText(datajsonObject .getString("Quantity"));
						mInvoiceItemQuantity_et2.setText(datajsonObject.getString("Quantity"));
						//mInvoiceItemDicount_et.setText(datajsonObject.getString("Discount"));
						mInvoiceItemQuantity_et1.setSelection(mInvoiceItemQuantity_et1.getText().length());
						mInvoiceItemQuantity_et2.setSelection(mInvoiceItemQuantity_et2.getText().length());
						
						//updatign invoice
						new UpdateInvoiceTask().execute();
						
						

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
 
				
				
				Log.v("TAG", "Response----->" + result);
			}

			// Log.v("TAG", "result  "+result.toString());

		
	}

	
	
	
	//Discount AsyncTask
	
	
	private class UpdateLineItemInvoiceDiscountAsyncTask extends
	AsyncTask<Object, Void, String> {

// String invoiceItemId_string = null, invoiceItemTypeId_string = null,
// discount_string = null, quantity_string = null;

EditText sale_type_quantity_et = null;
EditText Dicount_et_sales_invoice_row_ET = null;
TextView invoiceItemTotal_TextView = null;
TextView invoiceItemTotal2TextView = null;

int position = 0;

@Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();


}

@Override
protected String doInBackground(Object... params) {
	// TODO Auto-generated method stub

	sale_type_quantity_et = (EditText) params[4];
	Dicount_et_sales_invoice_row_ET = (EditText) params[5];
	invoiceItemTotal_TextView = (TextView) params[6];
	invoiceItemTotal2TextView = (TextView) params[7];

	position = (Integer) params[8];
	String reponse = updatesAlineItemOnInvoice((String) params[0],
			(String) params[1], (String) params[2], (String) params[3]);
	return reponse;
}

@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	super.onPostExecute(result);

	

		JSONObject mCompleteJsonObject;

		if (result != null) {
			try {

				mCompleteJsonObject = new JSONObject(result);
				JSONObject datajsonObject = mCompleteJsonObject
						.getJSONObject("data");

				invoiceItemTotal_TextView.setText("$"+datajsonObject.getString("SubTotal"));
				invoiceItemTotal2TextView.setText("$"+datajsonObject.getString("SubTotal"));
				
				///	this is for discount comparison 
				String subtotalStr=datajsonObject.getString("SubTotal").replaceAll("-", "");;
				subtotalfloal=Float.parseFloat(subtotalStr);
				
				sale_type_quantity_et.setText(datajsonObject.getString("Quantity"));


				/*sale_type_quantity_et.setText(datajsonObject
						.getString("Quantity"));*/
				new UpdateInvoiceTask().execute();
				

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		Log.v("TAG", "Response----->" + result);
	}

	// Log.v("TAG", "result  "+result.toString());

}


	
	public String updatesAlineItemOnInvoice(String invoiceItemId,
			String invoiceItemTypeId, String disCount, String mQuantityString) {

		String response = null;
		try {

			String mTaskUrlUpdatesAlineItemUrl = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.updatesAlineItemOnInvoice;
			mTaskUrlUpdatesAlineItemUrl = mTaskUrlUpdatesAlineItemUrl
					+ mSessionId;

			Log.v("TAG", "mTaskUrlUpdatesAlineItemUrl----"
					+ mTaskUrlUpdatesAlineItemUrl + " \n " + "mSessionId  ;  "
					+ mSessionId);

			JSONObject body = mLIneItemBody(invoiceItemId, invoiceItemTypeId,
					disCount, mQuantityString);

			response = mConnectionManager.setUpHttpPost(
					mTaskUrlUpdatesAlineItemUrl, body);

			Log.v("TAG", "mLineItemResponse----" + response);
		} catch (Exception e) {

			Log.v("TAG", "exception----" + e);

		}

		return response;

	}

	private JSONObject mLIneItemBody(String mInviceItemId,
			String invoiceItemTypeId, String itemDiscount,
			String mQuantityString) {
		// TODO Auto-generated method stub
		JSONObject mJsonObject;
		JSONObject mpostedItemJsonObject = new JSONObject();
		try {

			mJsonObject = new JSONObject();

			// {"postedItem":{"ItemDiscount":"10","InvoiceItemId":"1034605"
			// ,"InvoiceItemTypeId" : "16","Quantity":"20"}}

			mJsonObject.put("ItemDiscount", itemDiscount);
			mJsonObject.put("InvoiceItemId", mInviceItemId);
			mJsonObject.put("InvoiceItemTypeId", invoiceItemTypeId);
			mJsonObject.put("Quantity", mQuantityString);

			Log.v("TAG", "mJsonObject--" + mJsonObject);

			mpostedItemJsonObject.put("postedItem", mJsonObject);

			Log.v("TAG", "mLIneItemBody mpostedItemJsonObject--"
					+ mpostedItemJsonObject);

		} catch (Exception e) {
			// TODO: handle exception
			Log.v("TAG", "exception--" + e.toString());
		}

		return mpostedItemJsonObject;

	}

	public void TaxAfterDiscount() {
		String mTaskUrlAfterDiscount = null;
		String mResponseAfterDiscount = null;
		ConnectionManager mConnectionManager = new ConnectionManager();

		mTaskUrlAfterDiscount = mFlourishBaseApplication.mFlurishBaseUrl
				+ mFlourishBaseApplication.Sales_Invoice_TaxAfterDisc;
		//
		String url = (mTaskUrlAfterDiscount + mSessionId + "/" + mInvoiceId+ "/" + mIsTaxAfterDiscountChecked).toString();

		mResponseAfterDiscount = mConnectionManager.setUpHttpGet(url);

		if (mResponseAfterDiscount != null) {

			try {

				JSONObject mComplejson = new JSONObject(mResponseAfterDiscount);

				JSONObject mAfterDiscountObject = mComplejson.getJSONObject("data");

				

				mIsTaxAfterDiscountChecked = mAfterDiscountObject.getBoolean("Discounted");
				
				Log.v("TAG", "mAfterDiscountObject   ---"+ mAfterDiscountObject);
				Log.v("TAG", "DiscountBoolean-"+ mIsTaxAfterDiscountChecked);
		
				

			} catch (Exception e) {
				// TODO: handle exception

				Log.v("TAG", "Exception----" + e.toString());
			}

		}

		else {
			Log.v("TAG", "mResponseAfterDiscount----" + mResponseAfterDiscount);
		}

	}
	

	
	
	//Tax TaxShipping Method
	
	public void TaxShiping()
	{
		
		String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.Sales_Invoice_TaxShipping;
		
		
		
		String mTaxShippingResponse=mConnectionManager.setUpHttpGet(mTaskUrl+mSessionId+"/"+mInvoiceId+"/"+mIsTaxShippingChecked);
		
		
		
		if (mTaxShippingResponse!=null) {
			
			
			Log.v("TAG", "mTaxShippingResponse=="+mTaxShippingResponse);
			
			
			
		}
		else
		{
			Log.v("TAG", "Response empty");
			
			
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	
	//Sales_Invoice_SetShipping
	

	public void Sales_Invoice_SetShipping()
	{
		String  setShippingUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.Sales_Invoice_SetShipping;
		
		mConnectionManager=new ConnectionManager();
	 String mResponse=mConnectionManager.setUpHttpGet(setShippingUrl+mSessionId+"/"+mInvoiceId+"/"+mShippingValueString);
	 

	 if (mResponse!=null) {
		 
		 Log.v("TAG", "Response---"+mResponse);
		 
		 try {
		 JSONObject mComPleteJsonObject=new  JSONObject(mResponse);
		 
		 JSONObject dataJsonOject=mComPleteJsonObject.getJSONObject("data");
		 String total=dataJsonOject.getString("Total");
		 String tax=dataJsonOject.getString("TaxAmount1");
		 String discountAmount=dataJsonOject.getString("DiscountAmount").replace("-","");
		 mTaxAmountTv.setText("$"+tax.replaceAll("-", ""));
		 mTotalAmountTv.setText("$"+total.replaceAll("-", ""));
		 
		
		 
		 mTotalAmountValue = Float.parseFloat(total);
		 
		 Log.v("TAG", "total in setshipping=="+mTotalAmountValue);
		// Toast.makeText(getApplicationContext(), "Total=="+mTotalAmountValue, 500).show();
		 mInvoiceDiscount2Tv.setText( String.format("%.2f", discountAmount));
			
			} catch (Exception e) {
				// TODO: handle exception
				 Log.v("TAG", "Exception---"+e.toString());
				
			}
			 
		 
		 
		 
		 
		 
		
	}
		
		
		
		
		
	}
	
	
	
	class PercentDiscount extends AsyncTask<Void, Void, Void>
	{
		String Response=null;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			
			super.onPreExecute();
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			mConnectionManager=new ConnectionManager();
		
			String TaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.Sales_Invoice_ApplyPercentDiscount;
		
//			/{LoginKey}/{SalesInvoiceId}/{Discount}
			Response=mConnectionManager.setUpHttpGet(TaskUrl+mSessionId+"/"+mInvoiceId+"/"+mInvoiceDiscount1EditText.getText().toString());
			
			
			
			
			
			
			
			
			return null;
		}
		
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
              if (Response!=null) {
				
				Log.v("TAG", "PercentDiscount Response=="+Response);
				
				new UpdateInvoiceTask().execute();
				
			}
			
			
			
			
		}
		
		
		
		
		
		
	}
	
	
	
	class DollarDiscount extends AsyncTask<Void, Void, Void>
	{
		
		String Response=null;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			mConnectionManager=new ConnectionManager();
		
			String TaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.Sales_Invoice_Apply_DollarDiscount;
		
//			/{LoginKey}/{SalesInvoiceId}/{Discount}
			Response=mConnectionManager.setUpHttpGet(TaskUrl+mSessionId+"/"+mInvoiceId+"/"+mInvoiceDiscount1EditText.getText().toString());
			
				return null;
		}
		
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (Response!=null) {

				Log.v("TAG", "Dolar Discount Response===="+Response);
				try {
				
				JSONObject mCoJsonObject=new JSONObject(Response);
				JSONObject dataJsonObject=mCoJsonObject.getJSONObject("data");
		
				String discountString =dataJsonObject.getString("DiscountAmount");
				float discountFloat=Float.parseFloat(discountString);
			 
				
				mInvoiceDiscount2Tv.setText(String.format("%.2f", discountFloat));
				Log.v("PRK", "Discount value =="+discountString);
				
				Log.v("PRK", "Discount Amount "+dataJsonObject.getString("DiscountAmount"));
               String mTotal=dataJsonObject.getString("Total");
				Log.v("TAG", "Total---=="+mTotal);
				mTotalAmountTv.setText("$"+dataJsonObject.getString("Total").replaceAll("-", ""));
				mTaxAmountTv.setText("$"+dataJsonObject.getString("TaxAmount1").replaceAll("-", "")); 
		
			
				
				} catch (Exception e) {
					// TODO: handle exception
					Log.v("PRK", "exception =="+e.toString());
				}
				
				
				new UpdateInvoiceTask().execute();
				
			}
			
		
			
			
		}
		
		
		
		
		
		
	}


}