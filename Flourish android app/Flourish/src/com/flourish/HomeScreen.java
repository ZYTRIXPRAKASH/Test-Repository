package com.flourish;

import com.flourish.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class HomeScreen extends BaseTimeOutActivity implements OnClickListener
{
	private Button mCreateUser = null;
	private Button mAlreadyHaveAccount = null;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_screen);
		onSessionStarted(true);
		initializeVariables();
	}
	/**
	 * Method for initialize variables
	 */
	private void initializeVariables() 
	{
		mCreateUser = (Button)findViewById(R.id.create_user);
		mAlreadyHaveAccount = (Button)findViewById(R.id.already_have_account);
		mCreateUser.setOnClickListener(this);
		mAlreadyHaveAccount.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_payments_menu, menu);
		return true;
	}

	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.create_user:
			startActivity(new Intent(HomeScreen.this, CreateUser.class));
			break;
		case R.id.already_have_account:
			startActivity(new Intent(HomeScreen.this, LoginScreen.class));
			break;

		default:
			break;
		}
	}
}