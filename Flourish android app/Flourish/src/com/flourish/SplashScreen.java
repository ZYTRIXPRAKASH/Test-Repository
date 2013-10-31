package com.flourish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity
{
	private Runnable mRunnable = null;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
     	setContentView(R.layout.splash_screen);

		/*
		 * Runnable thread for holding the screen for 1200ms open Display
		 * activity.a
		 */
		mRunnable = new Runnable()
		{
			public void run()
			{
				try 
				{
					Thread.sleep(2000);
				Intent in = new Intent(SplashScreen.this, LoginScreen.class);
			
				    startActivity(in);
					finish();
				} 
				catch (Exception e) 
				{ 
					e.printStackTrace();
				}
			}
		};
		try
		{
			Thread t = new Thread(null, mRunnable);
			t.start();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}