package com.flourish;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;

public class CreateUser extends BaseTimeOutActivity implements OnClickListener
{
	private EditText mUsernameEt = null;
	private EditText mPasswordEt = null;
	private EditText mConfirmPasswordEt = null;
	private EditText mFirstNameEt = null;
	private EditText mLastNameEt = null;
	private EditText mEmailEt = null;
	private EditText mCompanyEt = null;
	private CheckBox mTermsAndConditionsCheckbox = null;
	private Button mSubmitBtn = null;
	private Button mCancelBtn = null;
	private ConnectionManager mConnectionManager = null;
	private ProgressDialog mProgressDialog = null;
	private AppNetwork mAppNetwork = null;
	private JSONObject mCreateUserJsonObj = null;
	private String mUsernameStr = null;
	private String mCreateUserResultStr = null;
	private String mPasswordStr = null;
	private String mResultStr = null;
	private String mFirstNameStr = null;
	private String mLastNameStr = null;
	private String mEmailStr = null;
	private String mCompanyStr=null;
	private String mConfirmPassStr=null;
	FlourishBaseApplication mFlourishBaseApplication;

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_user);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		onSessionStarted(true);
		
		initializeVariables();
	}

	/**
	 * Method for initializing variables
	 */
	private void initializeVariables() 
	{
		mUsernameEt = (EditText)findViewById(R.id.create_user_username_et);
		mPasswordEt = (EditText)findViewById(R.id.create_user_password_et);
		mConfirmPasswordEt=(EditText)findViewById(R.id.create_user_confirm_password_et);
		mFirstNameEt = (EditText)findViewById(R.id.first_name_et);
		mLastNameEt = (EditText)findViewById(R.id.last_name_et);
		mEmailEt = (EditText)findViewById(R.id.email_et);
		mCompanyEt=(EditText)findViewById(R.id.company_et);
		mTermsAndConditionsCheckbox=(CheckBox)findViewById(R.id.checkbox_check);
		mSubmitBtn = (Button)findViewById(R.id.create_user_submit);
		mCancelBtn=(Button)findViewById(R.id.create_user_cancel);
		mSubmitBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mConnectionManager = new ConnectionManager();
		mAppNetwork = new AppNetwork();
	}
	/**
	 * AsynTask for making Server request and response for creating user
	 */

	private class CreateUserTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() 
		{
			mProgressDialog = ProgressDialog.show(CreateUser.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params)
		{   String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.create_user_url;
			putCreateUserJsonRequest();
			mResultStr = mConnectionManager.setUpHttpPut(mTaskUrl, mCreateUserJsonObj.toString());
			//mResultStr = mConnectionManager.setUpHttpPut(getResources().getString(R.string.create_user_url), mCreateUserJsonObj.toString());
			getCreateUserResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result11)
		{
			if(mProgressDialog.isShowing())
			{
				if(mResultStr != null)
				{
					mProgressDialog.dismiss();

					if(mCreateUserResultStr.equalsIgnoreCase("Success"))
					{
						mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_create_user_success));
					}
					else if(mCreateUserResultStr.equalsIgnoreCase("Username not available"))
					{
						mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_username_not_available));
					}
					else if(mCreateUserResultStr.equalsIgnoreCase("Invalid Password"))
					{
						mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_invalid_password));
					}
					else if(mCreateUserResultStr.equalsIgnoreCase(""))
					{
						mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_enter_the_details));	
					}

					else if(mResultStr.contains("Login Key Not Valid"))
					{
						getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
						startActivity(new Intent(CreateUser.this, LoginScreen.class));
						finish();
					}
					else if(mResultStr.contains("Bad Parameters"))
					{
						mAppNetwork.getAlertDialog(CreateUser.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
					}
				}
				else
				{
					mProgressDialog.dismiss();
					mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_no_network));
				}
				if(mResultStr==null)
				{
					mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_enter_the_details));	
				}
			}
		}
	}

	/**
	 * Method for making JSON Server request for creating the user
	 */
	private void putCreateUserJsonRequest() 
	{
		try 
		{
			mCreateUserJsonObj = new JSONObject();
			mCreateUserJsonObj.put("UserName", mUsernameEt.getText().toString().trim());
			mCreateUserJsonObj.put("UserPass", mPasswordEt.getText().toString().trim());
			mCreateUserJsonObj.put("FirstName", mFirstNameEt.getText().toString().trim());
			mCreateUserJsonObj.put("LastName", mLastNameEt.getText().toString().trim());
			mCreateUserJsonObj.put("Email", mEmailEt.getText().toString().trim());
			mCreateUserJsonObj.put("dsCompany",mCompanyEt.getText().toString().trim());
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for parsing Server response for creating user
	 */
	private void getCreateUserResponse()
	{
		try 
		{
			JSONObject json = new JSONObject(mResultStr);
			mCreateUserResultStr = json.getJSONObject("AuthResponse").getString("CallResponse");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void onClick(View v)
	{
		mUsernameStr = mUsernameEt.getText().toString().trim();
		mPasswordStr = mPasswordEt.getText().toString().trim();
		mFirstNameStr = mFirstNameEt.getText().toString().trim();
		mLastNameStr = mLastNameEt.getText().toString().trim();
		mEmailStr = mEmailEt.getText().toString().trim();
		mConfirmPassStr=mConfirmPasswordEt.getText().toString().trim();
		mCompanyStr=mCompanyEt.getText().toString().trim();

		switch (v.getId()) 
		{
		case R.id.create_user_submit:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mCompanyEt.getWindowToken(), 0);

			if(mFirstNameStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_first_name));
			}
			else if(mLastNameStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_last_name));
			}
			else if(mUsernameStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_username));
			}
			else if(mPasswordStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_password));
			}
			else if(mPasswordStr.trim().length() < 6)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_invalid_password));
			}
			else if(mConfirmPassStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.confirm_your_password));
			}
			else if(mConfirmPassStr.trim().length() < 6)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_invalid_password));
			}
			else if(!(mPasswordStr.equalsIgnoreCase(mConfirmPassStr)))
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.alert_dialog_password_doesnot_match));
			}
			else if(mEmailStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_email));
			}
			else if (!mAppNetwork.isEmail(mEmailStr))
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_valid_email));
			}
			else if(mCompanyStr.trim().length() == 0)
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.enter_your_company));
			}
			else if(!(mTermsAndConditionsCheckbox.isChecked()))
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getResources().getString(R.string.accept_terms_conditions));
			}
			else if(mAppNetwork.isNetworkAvailable(CreateUser.this))
			{

				new CreateUserTask().execute();
			}
			else
			{
				mAppNetwork.getAlertDialog(CreateUser.this, getString(R.string.alert_dialog_no_network));
			}
			break;

		case R.id.create_user_cancel:
			startActivity(new Intent(CreateUser.this,LoginScreen.class));
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
			startActivity(new Intent(CreateUser.this, LoginScreen.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}