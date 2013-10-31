package com.flourish;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/*
 * This ACtivity must be extended by all Activities of this application either
 * Directly or indirectly. 
 * This activity observes user interactions and sets Alarm Manager for all user inactiveness
 */
public abstract class BaseTimeOutActivity extends FragmentActivity {

	//	 Context context = null;
	AlertDialog sessionAlert = null;
	private boolean isGoingtoBkg = false;
	private boolean isLoginScreen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		isGoingtoBkg = false;
		
		
		
	}

	//This function will be called everytime from onCreate of each activity
	//This function may not be required but lets  keep it for now
	/*	 public void onSessionStarted(Context context)
	 {
		 onSessionStarted(context, false);

	 }*/

	//This is just a overloaded function

	public void onSessionStarted(/*Context context,*/ boolean notLoginScreen)
	{
		isLoginScreen = notLoginScreen;
		//		 this.context = context;
		//Start the alarm manager only if the user is not in login ACtivity
		if(isLoginScreen)
		{
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, SessionTimeOutService.class);
			intent.putExtra(SessionTimeOutService.ISWARNING, false);
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + SessionTimeOutService.TIMEOUT_WARNING, pendingIntent);
		}
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		if(isLoginScreen) // this is not login case !!!
		{
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, SessionTimeOutService.class);
			intent.putExtra(SessionTimeOutService.ISWARNING, false);
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+SessionTimeOutService.TIMEOUT_WARNING, pendingIntent);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 * Whenever any Activity Resumes, this function will get called.
	 * Check if the Session is Active or should be timed out. 
	 */
	@Override
	protected void onResume() 
	{
		super.onResume();
		if(getSharedPreferences(LoginScreen.PREFS_NAME, 0).getBoolean(SessionTimeOutService.ISSESSIONTIMEOUT, false)){
			SharedPreferences sp = getSharedPreferences(LoginScreen.PREFS_NAME, 0);
			SharedPreferences.Editor edit = sp.edit();
			edit.putBoolean(SessionTimeOutService.ISSESSIONTIMEOUT, false);
			edit.commit();
			Intent in_startActivity = new Intent(this, LoginScreen.class);
			in_startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in_startActivity.putExtra("SESSION_TIMEOUT", true);
			startActivity(in_startActivity);
		}
		else
		{}
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		flushAllCreatedObjects();
	}

	private void flushAllCreatedObjects()
	{
		sessionAlert = null;
	}
}