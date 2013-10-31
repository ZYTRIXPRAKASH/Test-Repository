/**
 * 
 */
package com.flourish;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.adapters.UndeliveredItemsListAdapter;
import com.flourish.appnetwork.AppNetwork;
import com.flourish.appnetwork.DialogClass;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

import com.flourish.utils.Deliveries;
import com.flourish.utils.SelectedDeliveries;
import com.flourish.utils.UndeliveredItem;

/**
 *
 *
 */

public class AddDeliveries extends Activity implements OnClickListener {

	private String mSessionId = null;
	private String mInvoiceId = null;
	private TextView mDeliveryTypeTv = null;
	private TextView mDeliveryDateTv = null;
	private TextView mTopbarInvoiceNumber = null;
	private Button mCalanderButton = null;
	private EditText mDeliveryTracking_et = null;
	private Button mDeliverAllProductsButton = null;
	private ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	private String mGetDeliveryTypesResponse = null;
	private String mGetUndeliveredItemsResponse = null;
	private JSONObject mDeliveryTypesJSON;
	private int mYear;
	private int mMonth;
	private int mDay;
	private DatePicker mDeliveryDatePicker = null;
	private String mDeliveriesAllResponse = null;
	private String mDeliveredQuantityResponse;
	private SelectedDeliveries mSelectedDeliveries;
	List<SelectedDeliveries> mAllSelectedDeliveriesList = null;
	ArrayList<Boolean> isCheckedArrayList = new ArrayList<Boolean>();

	ArrayList<String> checkedList = new ArrayList<String>();
	private ArrayList<String> msubsicid = new ArrayList<String>();

	private ArrayList<Integer> mCheckedPosition = new ArrayList<Integer>();

	TextView add_sales_delivery_date_tv;

	private String[] deliveryTypes = null;
	private JSONArray mDeleiveryTypesDataJSONArray = null;
	private JSONArray mUndeliveredItemsJSONArray = null;
	private ArrayList<String> mItemIdArrayList = null;

	private ListView mUndeliveredListView = null;
	private Button mDeliveriesSaveButton = null;
	private Button mDeliveriesBackButton = null;
	private String addDeliveryToInvoiceString = null;
	private String mInvoiceNumber = null;

	private UndeliveredItemsListAdapter mUndeliveredItemsAdapter;
	UndeliveredItem mUndeliveredItem;
	List<UndeliveredItem> mAllUndeliveredItemsList;

	String mAddDeliveryToInvoiceResponse;
	FlourishBaseApplication mFlourishBaseApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sales_deliveries_add_edit);
		mFlourishBaseApplication = (FlourishBaseApplication) getApplicationContext();

		mAllUndeliveredItemsList = new ArrayList<UndeliveredItem>();
		mAllSelectedDeliveriesList = new ArrayList<SelectedDeliveries>();

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mSessionId = getSharedPreferences("LoginInfo", 0).getString(
				"sessionId", "nothing");
		Log.v("TAG", "Session ID:" + mSessionId);

		mInvoiceId = getIntent().getStringExtra("invoiceId");
		Log.v("TAG", "Invoice ID:" + mInvoiceId);
		mInvoiceNumber = getIntent().getStringExtra("invoiceNumber");

		intializeVariables();
		presentDate();

		if (mAppNetwork.isNetworkAvailable(AddDeliveries.this)) {
			new GetDeliveryTypestask().execute();
		} else {
			mAppNetwork.getAlertDialog(AddDeliveries.this,
					getString(R.string.alert_dialog_no_network));
		}
	}

	private void intializeVariables() {

		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();

		mDeliveryTypeTv = (TextView) findViewById(R.id.add_delivery_type_tv);
		mDeliveryDateTv = (TextView) findViewById(R.id.add_sales_delivery_date_tv);
		add_sales_delivery_date_tv = (TextView) findViewById(R.id.add_sales_delivery_date_tv);
		mCalanderButton = (Button) findViewById(R.id.delivery_date_calander_btn);

		mDeliveryTracking_et = (EditText) findViewById(R.id.delivery_tracking_et);
		mDeliveryDatePicker = (DatePicker) findViewById(R.id.delivery_dpResult);
		mDeliverAllProductsButton = (Button) findViewById(R.id.deliver_all_products_button);
		mUndeliveredListView = (ListView) findViewById(R.id.undelivered_list_view);
		mDeliveriesBackButton = (Button) findViewById(R.id.create_deliveries_back);
		mDeliveriesSaveButton = (Button) findViewById(R.id.deliveries_save);

		mTopbarInvoiceNumber = (TextView) findViewById(R.id.invoice_deliveries_Number);
		mTopbarInvoiceNumber.setText("Invoice #" + mInvoiceNumber);

		mDeliveryTypeTv.setOnClickListener(this);
		mCalanderButton.setOnClickListener(this);
		add_sales_delivery_date_tv.setOnClickListener(this);
		mDeliveriesSaveButton.setOnClickListener(this);
		mDeliverAllProductsButton.setOnClickListener(this);
		mDeliveriesBackButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_sales_delivery_date_tv:

			showDialog(R.id.delivery_date_calander_btn);
			break;

		case R.id.delivery_date_calander_btn:

			showDialog(R.id.delivery_date_calander_btn);
			break;

		case R.id.deliver_all_products_button:

			if (mAllUndeliveredItemsList.size() == 0) {
				Toast.makeText(getApplicationContext(),
						"No Undelivered Items  ", 500).show();

			}

			else if (mDeliveryTypeTv.length() == 0) {

				Toast.makeText(getApplicationContext(),
						"Select the Delivery Type ", 500).show();

			} else if (mDeliveryTracking_et.length() == 0) {
				Toast.makeText(getApplicationContext(),
						"Enter the Tracking number ", 500).show();

			} else {

				new InvoiceDeliverAlliesAsyncTask().execute();

			}

			break;

		case R.id.add_delivery_type_tv:

			showDialog(R.id.add_delivery_type_tv);

			break;

		case R.id.deliveries_save:

			// putInvoiceDeliveryData();
			new MakeADeliveryOnInvoiceTask().execute();

			break;

		case R.id.create_deliveries_back:

			finish();

			break;

		default:
			break;
		}
	}

	/**
	 * Collects all the data from the input and uploads to server
	 */
	private void putInvoiceDeliveryData() {

		JSONObject mPutInvoiceDeliveryJSON = new JSONObject();
		String mShipMethod = mDeliveryTypeTv.getText().toString();
		String mShipDate = mDeliveryDateTv.getText().toString();
		String mTrackingNumber = mDeliveryTracking_et.getText().toString();

		try {
			mPutInvoiceDeliveryJSON.put("InvoiceId", mInvoiceId);
			mPutInvoiceDeliveryJSON.put("ShipMethod", mShipMethod);
			mPutInvoiceDeliveryJSON.put("ShipDate", mShipDate);
			mPutInvoiceDeliveryJSON.put("TrackingNumber", mTrackingNumber);

		} catch (JSONException e) {
			Log.e("TAG",
					"Exception in AddDeliveries from putInvoiceDeliveryData : "
							+ e.getMessage());
		}

		addDeliveryToInvoiceString = mPutInvoiceDeliveryJSON.toString();
	}

	/**
	 * Gets all the delivery types from the server
	 */
	private class GetDeliveryTypestask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			FlourishProgressDialog.ShowProgressDialog(AddDeliveries.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String mTaskurl_Deliyverytypes = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.get_delivery_types;

			mGetDeliveryTypesResponse = mConnectionManager
					.setUpHttpGet(mTaskurl_Deliyverytypes + mSessionId);
			getDeliveryTypes();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			FlourishProgressDialog.Dismiss(AddDeliveries.this);
			new GetUndeliveredItemsTask().execute();

			super.onPostExecute(result);
		}
	}

	private class GetUndeliveredItemsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			FlourishProgressDialog.ShowProgressDialog(AddDeliveries.this);

		}

		@Override
		protected Void doInBackground(Void... params) {
			String mTaskUrl_get_all_undelivered_items = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.get_all_undelivered_items;
			mGetUndeliveredItemsResponse = mConnectionManager
					.setUpHttpGet(mTaskUrl_get_all_undelivered_items
							+ mSessionId + "/" + mInvoiceId);
			getUndeliveredItems();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			FlourishProgressDialog.Dismiss(AddDeliveries.this);

			Log.v("TAG", "UnDelivery list--" + mAllUndeliveredItemsList.size());
			if (mAllUndeliveredItemsList.size() != 0) {
				mUndeliveredListView
						.setAdapter(new UndeliveredListBaseAdapter());

			}

			else {

				Toast.makeText(getApplicationContext(), "No Undelivery Items ",
						500).show();

			}

			super.onPostExecute(result);
		}
	}

	/**
	 * Parses the response to get delivery types from the server
	 */
	public void getDeliveryTypes() {

		mDeliveryTypesJSON = new JSONObject();
		mDeleiveryTypesDataJSONArray = new JSONArray();

		try {
			mDeliveryTypesJSON = new JSONObject(mGetDeliveryTypesResponse);
			mDeleiveryTypesDataJSONArray = mDeliveryTypesJSON
					.getJSONArray("data");
			deliveryTypes = new String[mDeleiveryTypesDataJSONArray.length()];

			for (int i = 0; i < mDeleiveryTypesDataJSONArray.length(); i++) {

				deliveryTypes[i] = mDeleiveryTypesDataJSONArray.get(i)
						.toString();
			}

		} catch (JSONException e) {
			Log.e("TAG", "Exception in Add Deliveries from getDeliveryTypes : "
					+ e.getMessage());
		}
	}

	/**
	 * Parses the response from server to obtain undelivered items on an Invoice
	 */
	public void getUndeliveredItems() {

		mUndeliveredItemsJSONArray = new JSONArray();

		/*
		 * mItemIdArrayList = new ArrayList<String>(); mProductIdArrayList = new
		 * ArrayList<String>(); mNameArrayList = new ArrayList<String>();
		 * mQuantityArrayList = new ArrayList<String>();
		 * mDeliveredQuantityArrayList = new ArrayList<String>();
		 * mRetailArrayList = new ArrayList<String>(); mPriceArrayList = new
		 * ArrayList<String>(); mItemTypeNameArrayList = new
		 * ArrayList<String>(); mInvoiceItemTypeIdArrayList = new
		 * ArrayList<String>(); mSubTotalArrayList= new ArrayList<String>();
		 */

		try {
			JSONObject mUndeliveredItemsJSONObj = new JSONObject(
					mGetUndeliveredItemsResponse);
			mUndeliveredItemsJSONArray = mUndeliveredItemsJSONObj
					.getJSONArray("data");
			for (int position = 0; position < mUndeliveredItemsJSONArray
					.length(); position++) {
				JSONObject json = mUndeliveredItemsJSONArray
						.getJSONObject(position);

				mUndeliveredItem = new UndeliveredItem();
				mUndeliveredItem.UndeliverdItemId = json.getString("Id");
				mUndeliveredItem.ProductId = json.getString("ProductId");
				mUndeliveredItem.Name = json.getString("Name");
				mUndeliveredItem.Quantity = json.getString("Quantity");
				mUndeliveredItem.DeliveredQty = json.getString("DeliveredQty");
				mUndeliveredItem.Retail = json.getString("Retail");
				mUndeliveredItem.Price = json.getString("Price");
				mUndeliveredItem.ItemTypeName = json.getString("ItemTypeName");
				mUndeliveredItem.InvoiceItemTypeId = json
						.getString("InvoiceItemTypeId");
				mUndeliveredItem.SubTotal = json.getString("SubTotal");

				mAllUndeliveredItemsList.add(mUndeliveredItem);

				isCheckedArrayList.add(false);

			}

		} catch (JSONException e) {
			Log.e("TAG",
					"Exception in AddDeliveries from getUndeliveredItems : "
							+ e.toString());
		}
	}

	class UndeliveredListBaseAdapter extends BaseAdapter

	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAllUndeliveredItemsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View mView = null;
			mSelectedDeliveries = new SelectedDeliveries();

			LayoutInflater mLayoutInflater = getLayoutInflater();
			mView = mLayoutInflater.inflate(R.layout.undelivered_items_row,
					null);

			TextView mUndeliveredItemName = (TextView) mView
					.findViewById(R.id.sales_deliveries_undelivered_item_tv);

			EditText mUndeliveredItemQty = (EditText) mView
					.findViewById(R.id.sales_deliveries_undelivered_item_quantity_et);
			final CheckBox mCheckBox = (CheckBox) mView
					.findViewById(R.id.sales_undelivered_check_button);

			mUndeliveredItemName
					.setText(mAllUndeliveredItemsList.get(position).Name);

			mUndeliveredItemQty
					.setText(mAllUndeliveredItemsList.get(position).Quantity);
			mCheckBox.setTag(position);

			mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub

					if (isCheckedArrayList.get(position)) {
						isCheckedArrayList.remove(position);
						isCheckedArrayList.add(position, false);
					
						if (mCheckedPosition.size() > position) {

							mCheckedPosition.remove(position);
						}

						Log.v("TAG", "mCheckedPosition ==" + mCheckedPosition);

						Log.v("TAG", "if");

					} else {

						isCheckedArrayList.remove(position);
				
						isCheckedArrayList.add(position, true);
						Log.v("TAG", "else");

						mCheckedPosition.add(position);
						Log.v("TAG", "mCheckedPosition ==" + mCheckedPosition);

					}

				}
			});

			/*
			 * mCheckBox.setOnCheckedChangeListener(new
			 * OnCheckedChangeListener() {
			 * 
			 * public void onCheckedChanged(CompoundButton buttonView, boolean
			 * isChecked) { final int position = mUndeliveredListView
			 * .getPositionForView(buttonView);
			 * 
			 * if (position != ListView.INVALID_POSITION && isChecked) {
			 * 
			 * checkedList.add(msubsicid.get(position));
			 * 
			 * Log.v("TAG", "checkedList==" + checkedList);
			 * 
			 * } else {
			 * 
			 * // mallsubsicid.remove(msubsicid.get(position));
			 * checkedList.remove(msubsicid.get(position)); Log.v("TAG",
			 * " Remove checkedList==" + checkedList);
			 * 
			 * }
			 * 
			 * }
			 * 
			 * 
			 * });
			 */

			/*
			 * mCheckBox.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * if(mCheckBox.isChecked()){ mCheckedPosition.add(position); }
			 * else{ mCheckedPosition.remove(position); } } });
			 */

			/*
			 * //setmCheckedPosition(mCheckedPosition);
			 * 
			 * mCheckBox.setOnCheckedChangeListener(new
			 * OnCheckedChangeListener() {
			 * 
			 * 
			 * 
			 * @Override public void onCheckedChanged(CompoundButton buttonView,
			 * boolean isChecked) { // TODO Auto-generated method stub final int
			 * position = mUndeliveredListView.getPositionForView(buttonView);
			 * 
			 * if (position != ListView.INVALID_POSITION && isChecked) {
			 * //mStarStates[position] = isChecked;
			 * 
			 * checkedList.add(msubsicid.get(position));
			 * 
			 * Utils.sSelectedSicList.add(msubsicid.get(position));
			 * mSelectedDeliveries=new SelectedDeliveries(); Log.v("TAG",
			 * "Name  "+mAllUndeliveredItemsList.get(position).Name);
			 * mSelectedDeliveries
			 * .Quantity=mAllUndeliveredItemsList.get(position).Quantity;
			 * mAllSelectedDeliveriesList.add(mSelectedDeliveries); Log.v("TAG",
			 * "Selected list "+mAllSelectedDeliveriesList);
			 * 
			 * 
			 * 
			 * } else {
			 * 
			 * mallsubsicid.remove(msubsicid.get(position));
			 * checkedList.remove(msubsicid.get(position));
			 * Utils.sSelectedSicList.remove(msubsicid.get(position));
			 * 
			 * 
			 * mAllSelectedDeliveriesList.remove(mSelectedDeliveries);
			 * Log.v("TAG", "Selected list "+mAllSelectedDeliveriesList);
			 * 
			 * 
			 * }
			 * 
			 * } });
			 */

			return mView;
		}

	}

	/**
	 * Adds a Delivery to an Invoice
	 */
	private class MakeADeliveryOnInvoiceTask extends
			AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			FlourishProgressDialog.ShowProgressDialog(AddDeliveries.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject mCompleteJsobBody = new JSONObject();
			String TaskUrladd_a_delivery_to_an_invoice = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.add_a_delivery_to_an_invoice;
			String TaskUrl_Add_an_item_to_a_delivery = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.add_an_item_to_a_delivery;

			Log.v("PRK1", "mAddDeliveryToInvoiceResponse  "
					+ mAddDeliveryToInvoiceResponse);
			for (int index = 0; index < mCheckedPosition.size(); index++) {

				int position = mCheckedPosition.get(index);

				// if (isCheckedArrayList.get(index)) {

				try {
					putInvoiceDeliveryData();
					mAddDeliveryToInvoiceResponse = mConnectionManager
							.setUpHttpPut(TaskUrladd_a_delivery_to_an_invoice
									+ mSessionId, addDeliveryToInvoiceString);
					Log.v("PRK1", "add delivery to invoice response"
							+ mAddDeliveryToInvoiceResponse);

					mCompleteJsobBody.put("InvoiceId", mInvoiceId);
					mCompleteJsobBody.put("DeliveryId",
							mAllUndeliveredItemsList.get(position).ProductId);
					mCompleteJsobBody
							.put("InvoiceItemId",
									mAllUndeliveredItemsList.get(position).UndeliverdItemId);
					mCompleteJsobBody.put("Quantity",
							mAllUndeliveredItemsList.get(position).Quantity);

					String Add_an_item_to_a_deliveryResponse = mConnectionManager
							.setUpHttpPut(TaskUrl_Add_an_item_to_a_delivery
									+ mSessionId, mCompleteJsobBody.toString());

					Log.v("PRK", "ADD TO DELIVERY RESPONSE=="
							+ Add_an_item_to_a_deliveryResponse);
					String mDeliveredId = null;
					if (mAddDeliveryToInvoiceResponse != null) {
						JSONObject mCompleteJsonObject = new JSONObject(
								mAddDeliveryToInvoiceResponse);
						JSONObject mDataJsonObject = mCompleteJsonObject
								.getJSONObject("data");
						mDeliveredId = mDataJsonObject.getString("Id");
					}

					String finallizedTaskUrl = mFlourishBaseApplication.mFlurishBaseUrl
							+ mFlourishBaseApplication.Finalizes_a_delivery;

					String mFinalizeDeliveryResponse = mConnectionManager
							.setUpHttpGet(finallizedTaskUrl + mSessionId + "/"
									+ mDeliveredId);

					Log.v("TAG", "mFinalizeDeliveryResponse "
							+ mFinalizeDeliveryResponse);

					Log.v("TAG", "Delivered response==="
							+ mAddDeliveryToInvoiceResponse);

				} catch (Exception e) {
					// TODO: handle exception
				}

				// }
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			FlourishProgressDialog.Dismiss(AddDeliveries.this);
			super.onPostExecute(result);

			Intent i = new Intent();
			// i.setAction(mBookingCalenederId+","+mBookingSubject);

			setResult(RESULT_OK, i);// navigating to delivery list;
			finish();

		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;
			if (comparingDate().equalsIgnoreCase("valid")) {
				mDeliveryDateTv.setText(new StringBuilder().append(mMonth + 1)
						.append("-").append(mDay).append("-").append(mYear));

				mDeliveryDatePicker.init(mYear, mMonth, mDay, null);
			} else if (comparingDate().equalsIgnoreCase("invalid")) {
				mAppNetwork.getAlertDialog(AddDeliveries.this, getResources()
						.getString(R.string.alert_dialog_enter_proper_date));
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
		Date inputDate = new Date(mYear, mMonth, mDay + 30);
		Long inputTime = inputDate.getTime();
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
	 * Forms the body required to add an item to delivery
	 */
	public String putItemToDelivery(int position) {

		JSONObject mJsonObject = new JSONObject();
		try {
			mJsonObject.put("InvoiceId", mInvoiceId);
			mJsonObject.put("DeliveryId", mItemIdArrayList.get(position)
					.toString());
			mJsonObject.put("InvoiceItemId",
					mAllUndeliveredItemsList.get(position).InvoiceItemTypeId);
			mJsonObject.put("Quantity",
					mAllUndeliveredItemsList.get(position).Quantity);
		} catch (JSONException e) {
			Log.e("TAG",
					"Exception in AddDelivereries from putItemToDelivery : "
							+ e.getMessage());
		}
		return mJsonObject.toString();
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder;
		switch (id) {
		case R.id.delivery_date_calander_btn: {

			// set date picker as current date
			return new DatePickerDialog(AddDeliveries.this, datePickerListener,
					mYear, mMonth, mDay);
		}

		case R.id.add_delivery_type_tv:

			builder = new AlertDialog.Builder(AddDeliveries.this);
			builder.setTitle(R.string.select_delivery_type).setItems(
					deliveryTypes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int position) {

							String mDeliveryType = deliveryTypes[position];
							mDeliveryTypeTv.setText(mDeliveryType);
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

	public void presentDate() {

		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		String formattedDate = df.format(c.getTime());
		mDeliveryDateTv.setText(formattedDate);

	}

	class InvoiceDeliverAlliesAsyncTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String mTaskUrl_DeliveriesAll = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.invoice_Deliver_All;
			mConnectionManager = new ConnectionManager();

			try {

				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("InvoiceId", mInvoiceId);
				mJsonObject.put("ShipMethod", mDeliveryTypeTv.getText()
						.toString());
				mJsonObject.put("ShipDate", mDeliveryDateTv.getText()
						.toString());
				mJsonObject.put("TrackingNumber", mDeliveryTracking_et
						.getText().toString());

				mDeliveriesAllResponse = mConnectionManager.setUpHttpPut(
						mTaskUrl_DeliveriesAll + mSessionId, "" + mJsonObject);

				Log.v("TAG", "mDeliveriesAllResponse" + mDeliveriesAllResponse);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (mDeliveriesAllResponse != null) {

				Intent i = new Intent();

				setResult(RESULT_OK, i);
				finish();

			}

		}

	}

}
