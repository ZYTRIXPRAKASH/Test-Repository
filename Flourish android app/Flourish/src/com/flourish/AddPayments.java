/**
 * 
 */
package com.flourish;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.propay.payments.AddPropayPayment;


public class AddPayments extends Activity implements OnClickListener {

	private RelativeLayout mPaymentTopBar = null;
	private Button mPaymentBackBtn = null;
	private TextView mInvoicePaymentNumberTv = null;
	private Button mPaymentSaveBtn = null;
	private RelativeLayout mAddPaymentFields_rl = null;
	private TextView mPaymentTypeTv = null;
	private EditText mPaymentDate_et = null;
	private Button mPaymentDateCalanderBtn = null;
	private EditText mPaymentMemo_et = null;
	private EditText mPaymentAmount_et = null;
	private DatePicker mPaymentDatePicker = null;
	private String mSessionId = null;
	private ConnectionManager mConnectionManager = null;
	private String mAddPaymentResponse = null;
	private RelativeLayout mPaymentType_rl = null;
	private String mInvoiceId = null;
	private String mPaymentType = null;
	private String mAmount=null;
	private String mInvoiceNumber=null;

	private int mYear;
	private int mMonth;
	private int mDay;
	private String addPaymentJSONString = null;
	private AppNetwork mAppNetwork = null;
	FlourishBaseApplication mFlourishBaseApplication;
	String paymentType;
	SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_payments);
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
		mFlourishBaseApplication = (FlourishBaseApplication) getApplicationContext();

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mSessionId = getSharedPreferences("LoginInfo", 0).getString(
				"sessionId", "nothing");
		Log.v("TAG", "Session ID:" + mSessionId);

		mInvoiceId = getIntent().getStringExtra("invoiceId");
		mAmount=getIntent().getStringExtra("amount").replaceAll("\\$", "");
		mInvoiceNumber=getIntent().getStringExtra("invoiceNumber");
		Log.v("TAG", "invoice number in add payments "+mInvoiceNumber);
		
		Log.v("TAG", "Invoice ID:" + mInvoiceId);
		Log.v("TAG", "amount in add payment:" + mAmount);
		mPaymentType = getIntent().getStringExtra("PAYMENT_TYPE");
		Log.v("TAG", "Payment Type:" + mPaymentType);

		intializeVariables();

		presentDate();

	}

	private void intializeVariables() {

		mAppNetwork = new AppNetwork();
		mConnectionManager = new ConnectionManager();

		mPaymentType_rl = (RelativeLayout) findViewById(R.id.add_payment_type_rl);
		mPaymentTopBar = (RelativeLayout) findViewById(R.id.create_deliveries_top_bar);
		mPaymentBackBtn = (Button) findViewById(R.id.create_payment_back);
		mInvoicePaymentNumberTv = (TextView) findViewById(R.id.invoice_payment_number_tv);
		mInvoicePaymentNumberTv.setText("Invoice #"+mInvoiceNumber);
		
		mPaymentSaveBtn = (Button) findViewById(R.id.payment_save);
		mAddPaymentFields_rl = (RelativeLayout) findViewById(R.id.add_payment_fields);
		mPaymentTypeTv = (TextView) findViewById(R.id.add_payment_type_tv);
		mPaymentDate_et = (EditText) findViewById(R.id.add_sales_payment_date_et);
		mPaymentDateCalanderBtn = (Button) findViewById(R.id.payment_date_calander_btn);
		mPaymentMemo_et = (EditText) findViewById(R.id.add_payment_memo_et);
		mPaymentAmount_et = (EditText) findViewById(R.id.add_payment_amount_et);
		mPaymentAmount_et.setText("0.00");
		
		mPaymentDatePicker = (DatePicker) findViewById(R.id.payment_dpResult);
		mPaymentSaveBtn.setOnClickListener(this);
		mPaymentTypeTv.setOnClickListener(this);
		mPaymentDateCalanderBtn.setOnClickListener(this);
		mPaymentBackBtn.setOnClickListener(this);

		mPaymentTypeTv.setText(mPaymentType);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.create_payment_back:
			finish();
			break;
		case R.id.add_payment_type_tv:
			showDialog(R.id.add_payment_type_tv);
			break;

		case R.id.payment_save:

			if (mPaymentDate_et.getText().toString().trim().length() == 0) {

				mAppNetwork.getAlertDialog(AddPayments.this,
						getString(R.string.alert_dialog_enter_payment_date));

			} else if (mPaymentMemo_et.getText().toString().trim().length() == 0) {

				mAppNetwork.getAlertDialog(AddPayments.this,
						getString(R.string.alert_dialog_enter_payment_memo));

			} else if (mPaymentAmount_et.getText().toString().trim().length() == 0) {

				mAppNetwork.getAlertDialog(AddPayments.this,
						getString(R.string.alert_dialog_enter_payment_amount));

			} else if (mAppNetwork.isNetworkAvailable(AddPayments.this)) {

				new PostPaymentDetailsTask().execute();

			} else {

				mAppNetwork.getAlertDialog(AddPayments.this,
						getString(R.string.alert_dialog_no_network));

			}

			break;

		case R.id.payment_date_calander_btn:

			showDialog(R.id.payment_date_calander_btn);

			break;

		default:
			break;
		}

	}

	private void putAddPaymentData() {

		// {"InvoiceId":"377025","Reference":"ggg","Amount":"100","PaymentDate":"09-25-2013","Method":"Cash"}

		JSONObject addPaymentDetailsJSON = new JSONObject();
		try {
			addPaymentDetailsJSON.put("InvoiceId", mInvoiceId);
			addPaymentDetailsJSON.put("Amount", mPaymentAmount_et.getText()
					.toString());
			addPaymentDetailsJSON.put("Method", mPaymentTypeTv.getText()
					.toString());
			addPaymentDetailsJSON.put("Reference", mPaymentMemo_et.getText()
					.toString());
			addPaymentDetailsJSON.put("PaymentDate", mPaymentDate_et.getText()
					.toString());

		} catch (JSONException e) {
			Log.e("response",
					"JSON Exception in Add Payments: " + e.getMessage());
		}

		addPaymentJSONString = addPaymentDetailsJSON.toString();

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;
			if (comparingDate().equalsIgnoreCase("valid")) {
				mPaymentDate_et.setText(new StringBuilder().append(mMonth + 1)
						.append("-").append(mDay).append("-").append(mYear));

				mPaymentDatePicker.init(mYear, mMonth, mDay, null);
			} else if (comparingDate().equalsIgnoreCase("invalid")) {
				mAppNetwork.getAlertDialog(AddPayments.this, getResources()
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

	private class PostPaymentDetailsTask extends AsyncTask<Void, Void, Void> {


		@Override
		protected void onPreExecute() {
			FlourishProgressDialog.ShowProgressDialog(AddPayments.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String mTaskUrl_add_payment_request = mFlourishBaseApplication.mFlurishBaseUrl
					+ mFlourishBaseApplication.add_payment_request;
			putAddPaymentData();
			mAddPaymentResponse = mConnectionManager.setUpHttpPut(
					mTaskUrl_add_payment_request + mSessionId,
					addPaymentJSONString);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		
			FlourishProgressDialog.Dismiss(AddPayments.this);

				

				if (mAddPaymentResponse
						.equalsIgnoreCase("Invoice not available for payment")
						|| mAddPaymentResponse
								.equalsIgnoreCase("Payment Not found")) {

					mAppNetwork.getAlertDialog(AddPayments.this,
							getString(R.string.alert_dialog_account_not_found));

				} 
				
				else {
					
				
					if (mFlourishBaseApplication.getmPaymentsTab()==1) {
						Intent i = new Intent();
						
						setResult(RESULT_OK, i);
						finish();
						
					}
					

					// startActivity(new Intent(AddPayments.this,SalesPayments.class));
					
					
					else{
						
						
						
						//startActivity(new Intent(AddPayments.this, SalesDetails.class));
						
						SharedPreferences.Editor editor=mSharedPreferences.edit();
						  editor.putString("FragmentScreen", "Dashboard");
						  editor.commit();

						 Intent mIntentForDashboard = new Intent(AddPayments.this, FlourishHomeActivity.class);
						startActivity(mIntentForDashboard);
						
						
						
						
					}
					
				
				
				
				}
		
			super.onPostExecute(result);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Dialog dialog;
		AlertDialog.Builder builder;
		switch (id) {
		case R.id.add_payment_type_tv:

			final String paymentTypes[] = getResources().getStringArray(
					R.array.payment_type_array);
			builder = new AlertDialog.Builder(AddPayments.this);
			builder.setTitle(R.string.select_payment_type).setItems(
					paymentTypes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int position) {

							paymentType = paymentTypes[position];
							if (paymentType.equalsIgnoreCase(getResources()
									.getString(R.string.cash))) {
								mPaymentTypeTv.setText(getResources()
										.getString(R.string.cash));

							} else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.cheque))) {

								mPaymentTypeTv.setText(getResources()
										.getString(R.string.cheque));

							} 
							
					else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.credit_OR_card))) {

								mPaymentTypeTv.setText(getResources()
										.getString(R.string.credit_OR_card));

//								startActivity(new Intent(getApplicationContext(),AddPropayPayment.class));
								startActivity(new Intent(getApplicationContext(),AddPropayPayment.class)
								.putExtra("invoiceId", mInvoiceId)
								.putExtra("invoiceNumber", mInvoiceNumber)
								
								
							
										);
								
								finish();

								Log.v("TAG", "invoiceId===="+mInvoiceId);
								
							} 
							
							
							else if (paymentType
									.equalsIgnoreCase(getResources().getString(
											R.string.other))) {

								mPaymentTypeTv.setText(getResources()
										.getString(R.string.other));
								
								
								
								
							}
							
					
						}
					});
			dialog = builder.create();
			break;

		case R.id.payment_date_calander_btn: {

			// set date picker as current date
			return new DatePickerDialog(AddPayments.this, datePickerListener,
					mYear, mMonth, mDay);
		}

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
		mPaymentDate_et.setText(formattedDate);

	}

}
