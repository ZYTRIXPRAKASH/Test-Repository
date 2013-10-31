package com.flourish;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

public class LoginScreen extends BaseTimeOutActivity implements OnClickListener {
	private EditText mUsernameEt = null;
	private EditText mPasswordEt = null;
	private Button mLoginBtn = null;
	private TextView mSignUpTv = null;
	
	public String mResult = null;
	private String mResultvalue = null;
	private String mUsernameStr = null;
	private String mPasswordStr = null;
	private String mIsDataFound = null;
	private ConnectionManager mConnectionManager = null;
	private AppNetwork mAppNetwork = null;
	public static final String PREFS_NAME = "FLOURISH_SP";
	FlourishBaseApplication mFlourishBaseApplication;
	SharedPreferences mSharedPreferences;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_screen);
		// onSessionStarted(false);

mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		initializeVariables();
		mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);

		// Alert Dialog to be shown when the Login Key is not valid.
		mIsDataFound = getSharedPreferences("DataChecking", 0).getString(
				"Data", "nothing");
		if (mIsDataFound.equalsIgnoreCase("no_data")) {
			getSharedPreferences("DataChecking", 0).edit()
					.putString("Data", "nothing").commit();
			// Custom Dialog for exiting the application
			final Dialog dialog = new Dialog(LoginScreen.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.login_key_not_valid_dialog);

			Button mOk = (Button) dialog.findViewById(R.id.dialogButtonOK);

			mOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
		}

		// Alert Dialog to be shown when session is Timed Out.
		if (getIntent().getBooleanExtra("SESSION_TIMEOUT", false)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.alert_dialog_session_timed_out));
			builder.setMessage(getString(R.string.alert_dialog_logged_out_due_to_inactivity));
			builder.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							Log.v("session_timeout",
									"User Pressed OK so Kill Alarm Manager while in Login Screen");
							SharedPreferences mPrefs = getSharedPreferences(
									LoginScreen.PREFS_NAME, 0);
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putBoolean(
									SessionTimeOutService.ISSESSIONTIMEOUT,
									false);
							editor.commit();

							AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
							Intent in = new Intent(LoginScreen.this,
									SessionTimeOutService.class);
							in.putExtra(SessionTimeOutService.ISWARNING, true);
							PendingIntent pendingIntent = PendingIntent
									.getService(LoginScreen.this, 0, in,
											PendingIntent.FLAG_CANCEL_CURRENT);
							alarmManager.cancel(pendingIntent);
							Log.v("session_timeout",
									"SessionTimeout shared prefs");
						}
					});

			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables() {
		mUsernameEt = (EditText) findViewById(R.id.login_username_et);
		mPasswordEt = (EditText) findViewById(R.id.login_password_et);
		mLoginBtn = (Button) findViewById(R.id.login);
		mSignUpTv = (TextView) findViewById(R.id.get_an_account);
		mLoginBtn.setOnClickListener(this);
		mSignUpTv.setOnClickListener(this);
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();

		mUsernameEt.setText("API_FA");
		mPasswordEt.setText("test123fa");
		
	
	}

	public void onClick(View v) {
		mUsernameStr = mUsernameEt.getText().toString().trim();
		mPasswordStr = mPasswordEt.getText().toString().trim();

		switch (v.getId()) {
		case R.id.login:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mPasswordEt.getWindowToken(), 0);

			if (mUsernameStr.trim().length() == 0) {
				mAppNetwork.getAlertDialog(LoginScreen.this, getResources()
						.getString(R.string.enter_your_first_name));
			} else if (mPasswordStr.trim().length() == 0) {
				mAppNetwork.getAlertDialog(LoginScreen.this, getResources()
						.getString(R.string.enter_your_password));
			} else if (mUsernameEt.getText().toString().contains(" ")) {
				mAppNetwork.getAlertDialog(LoginScreen.this, getResources()
						.getString(R.string.enter_your_valid_username));
			} else if (mPasswordEt.getText().toString().contains(" ")) {
				mAppNetwork.getAlertDialog(LoginScreen.this, getResources()
						.getString(R.string.enter_your_valid_password));
			} else if (mAppNetwork.isNetworkAvailable(LoginScreen.this)) {
				new LoginTask().execute();
			} else {
				mAppNetwork.getAlertDialog(LoginScreen.this, getResources()
						.getString(R.string.alert_dialog_no_network));
			}
			break;

		case R.id.get_an_account:
			startActivity(new Intent(LoginScreen.this, CreateUser.class));
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * AsynTask for making Server Request and Response for Login Task
	 */
	private class LoginTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			FlourishProgressDialog.ShowProgressDialog(LoginScreen.this);
		
		
		
		}

		@Override
		protected Void doInBackground(Void... params) {
			

            String mTaskUrl_login_url=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.login_url;
			String mUserNameStr = mUsernameEt.getText().toString().trim();
			String mPasswordStr = mPasswordEt.getText().toString().trim();
			mResult = mConnectionManager.setUpHttpGet(mTaskUrl_login_url + mUserNameStr + "/" + mPasswordStr + "/true");
			
		//	mResult = mConnectionManager.setUpHttpGet(getResources().getString(R.string.login_url)+ "/" + mUserNameStr + "/" + mPasswordStr + "/true");
			return null;
		}

		@Override
		protected void onPostExecute(Void result11) {
		
				
				FlourishProgressDialog.Dismiss(LoginScreen.this);
				
				
				if (mResult != null) {

					getResponse();
					if (mResultvalue.contains("Success")) {
						getSharedPreferences("LoginInfo", 0).edit()
								.putBoolean("Login", true).commit();

						startActivity(new Intent(getApplicationContext(),
								FlourishHomeActivity.class));
						// startActivity(new Intent(getApplicationContext(),
						// Menu.class));
						
						  SharedPreferences.Editor editor=mSharedPreferences.edit();
						  editor.putString("FragmentScreen", "Dashboard");
						  editor.commit();

					

						finish();
					} else if (mResultvalue.contains("User Login Fail")) {
						mAppNetwork
								.getAlertDialog(
										LoginScreen.this,
										getResources()
												.getString(
														R.string.alert_dialog_user_login_failed));
					} else if (mResultvalue.contains("Incorrect Password")) {
						mAppNetwork
								.getAlertDialog(
										LoginScreen.this,
										getResources()
												.getString(
														R.string.alert_dialog_user_incorrect_password));
					} else if (mResultvalue.contains("Incorrect Login")) {
						mAppNetwork
								.getAlertDialog(
										LoginScreen.this,
										getResources()
												.getString(
														R.string.alert_dialog_user_incorrect_login));
					}
				} else {
					
					mAppNetwork.getAlertDialog(LoginScreen.this, getResources()
							.getString(R.string.alert_dialog_no_network));
				}
				if (mResultvalue == null) {
					mAppNetwork
							.getAlertDialog(
									LoginScreen.this,
									getResources()
											.getString(
													R.string.alert_dialog_enter_the_details));
				}
			}
		}
	

	/**
	 * Method for parsing the server response for Login
	 */
	public void getResponse() {
		try {
			JSONObject json = new JSONObject(mResult);
			mResultvalue = json.getJSONObject("AuthResponse").getString(
					"AuthResponse");
			getSharedPreferences("LoginInfo", 0).edit()
					.putString("sessionId", json.getString("data")).commit();

			Log.v("TAG", "SessionId  in Login " + json.getString("data"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			// Custom Dialog for exiting the application
			final Dialog dialog = new Dialog(LoginScreen.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.quit_application_dialog);

			Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);
			Button mNo = (Button) dialog.findViewById(R.id.dialogButtonNO);

			mYes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(startMain);
					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
			mNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.show();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}