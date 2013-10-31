package com.flourish;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SessionTimeOutService extends IntentService 
{
	public static final long TIMEOUT_WARNING = 8*60*1000;
	//2*30
	public static final long TIMEOUT_SESSION_CLOSE = 2*60*1000;
	//1*30
	private static final int NOTIFICATION_ID = R.string.session_timeout_warning;
    private static final String SERVICE_NAME = "SessionTimeOutService";
    public static final String ISWARNING = "com.flourish.SessionTimeOutService.IsWarning";
    public static final String ISSESSIONTIMEOUT = "com.flourish.SessionTimeOutService.IsSessionTimeout";
	public SessionTimeOutService() {
		super(SERVICE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 * This function will get called when the Alarm Wakes up
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		if(!intent.getBooleanExtra(ISWARNING, false)) {
			Log.v("session_timeout","onHandle Intent shared prefs");
			//First Warning Arrived. Show the Alert and trigger a new alarm for Second Time Out
			NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
	        Notification notification = notificationBuilder.setContentTitle(getText(R.string.session_timeout_warning))
	            .setContentText(getText(R.string.session_timeout_warning_text))
	            .setSmallIcon(R.drawable.flourish_logo_57)
	            .setWhen(System.currentTimeMillis())
	            .build();
//	        nm.notify(NOTIFICATION_ID, notification);
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			 Intent in = new Intent(this, SessionTimeOutService.class);
			 in.putExtra(SessionTimeOutService.ISWARNING, true);
			 PendingIntent pendingIntent = PendingIntent.getService(this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
			 alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ TIMEOUT_SESSION_CLOSE, pendingIntent);
			 
			 SharedPreferences mPrefs = getSharedPreferences(LoginScreen.PREFS_NAME, 0);
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putBoolean(ISSESSIONTIMEOUT, false);
				editor.commit();
		}
		else
		{
			//Second timeout also happened. Now we should kill
			// Update a Shared Prefs Value that the Session is timed out. 
			Log.v("session_timeout","SessionTimeout before shared prefs");
			SharedPreferences mPrefs = getSharedPreferences(LoginScreen.PREFS_NAME, 0);
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putBoolean(ISSESSIONTIMEOUT, true);
			editor.commit();
			
			
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			 Intent in = new Intent(this, SessionTimeOutService.class);
			 in.putExtra(SessionTimeOutService.ISWARNING, true);
			 PendingIntent pendingIntent = PendingIntent.getService(this, 0, in, PendingIntent.FLAG_CANCEL_CURRENT);
			 //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ TIMEOUT_SESSION_CLOSE, pendingIntent);
			 alarmManager.cancel(pendingIntent);
			 Log.v("session_timeout","SessionTimeout shared prefs");
		}
	}
}