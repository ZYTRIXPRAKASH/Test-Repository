package com.flourish;

import java.io.Flushable;

import org.json.JSONException;
import org.json.JSONObject;

import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	private EditText mUsernameEt = null;
	private EditText mPasswordEt = null;
	private Button mLoginBtn = null;
	private TextView mSignUpTv = null;

	public String mResult = null;
	private String mResultvalue = null;
	private String mUsernameStr = null;
	private String mPasswordStr = null;
	private String mIsDataFound = null;
	ConnectionManager mConnectionManager=null;
	String mLoginResponse=null;
	FlourishBaseApplication mFlourishBaseApplication;
	String mLoginTaskUrl;
	String mAuthResponse;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		initializeVariables();
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		
		mLoginBtn.setOnClickListener(buttonClickListener);
		
		
		
		
		
	}

	
	private void initializeVariables() {
		mUsernameEt = (EditText)findViewById(R.id.login_username_et);
		mPasswordEt = (EditText)findViewById(R.id.login_password_et);
		mLoginBtn = (Button)findViewById(R.id.login);
		mSignUpTv = (TextView)findViewById(R.id.get_an_account);
	
		

		mUsernameEt.setText("API_FA");
		mPasswordEt.setText("test123fa");
		
		
		
		
		
	}


	View.OnClickListener buttonClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			mUsernameStr = mUsernameEt.getText().toString().trim();
			mPasswordStr = mPasswordEt.getText().toString().trim();

			
			switch (v.getId()) {
			case R.id.login:
				
				
				new LoginTask().execute();
				
				
				
				
			
				break;
				
				
				
			

			default:
				break;
			}
			
			
			
		}
	};
	
	

	/**
	 * AsynTask for making Server Request and Response for Login Task
	 */
	private class LoginTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() 
		{
			FlourishProgressDialog.ShowProgressDialog(LoginActivity.this);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			String mUserNameStr = mUsernameEt.getText().toString().trim();
			String mPasswordStr = mPasswordEt.getText().toString().trim();
			
			mConnectionManager=new ConnectionManager();
			
			mLoginTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.mLoginUrl_get;
			
			
			
			mLoginResponse=mConnectionManager.setUpHttpGet(mLoginTaskUrl+ "/"+mUserNameStr+"/"+mPasswordStr+"/true");
			
			Log.v("TAG", "Login Response in Login ---"+mLoginResponse);
			
			Log.v("TAG", "mLoginTaskUrl ---"+mLoginTaskUrl);
			
			
			
			
		try {
			JSONObject mCompleteJsonObject=new JSONObject(mLoginResponse);
			
			mAuthResponse=mCompleteJsonObject.getString("AuthResponse");
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
			
			
			
			


			return null;
		}

		@Override
		protected void onPostExecute(Void result11)
		{

			FlourishProgressDialog.Dismiss(LoginActivity.this);
			
			
			if (mLoginResponse!=null) {
				
				
				if (mAuthResponse.contains("Success")) {
					
					
				startActivity(new  Intent(getApplicationContext(), FlourishHomeActivity.class));
				
					
				}
				
				
				
			}
			
			
			
			
		
		
		}


	
	
	}}
