//**************************************************************************************************
//**************************************************************************************************
//                                     Copyright (c) 2010 by Zytrix SoftLabs
//                                            ALL RIGHTS RESERVED
//**************************************************************************************************
//**************************************************************************************************
//
//        Project name                        : Essence
//        Class Name                          : AppUtils
//        Date                                : September 25th 2012
//        Author                              : Prashanth Kumar
//        Version                             : 1.0
//
//***************************************************************************************************
// Class Description:
// AppUtils class is for using the basic functionalities used in the application.
//
//***************************************************************************************************
//        Update history:
//        Date :                            Developer Name :     Modification Comments :
//
//***************************************************************************************************

package com.flourish.appnetwork;

import java.net.InetAddress;
import java.util.List;
import java.util.regex.Pattern;

import com.flourish.FlourishHomeActivity;
import com.flourish.LoginScreen;
import com.flourish.R;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AppNetwork 
{	
	public static String sLatitude = null;
	public static String sLongitude = null;
	public static String sCurrentLocation = null;
	public static MyLocationListener sLocationListener = null;
	public static LocationManager sLocationManager = null;
	public static Location sLocation = null;
	public final static Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{2,64}" + "(" + "\\."
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
	
	/**
	 * 	 This connectivity is an instance variable of type ConnectivityManager, this is for to hold the connectivity services 
	 *	 public static ConnectivityManager  connectivity;
	 */	
	ConnectivityManager connectivityManager;
	NetworkInfo wifiInfo, mobileInfo;

	/**
	 * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection using <code>isConnected()</code>
	 * Checks for generic Exceptions and writes them to logcat as <code>CheckConnectivity Exception</code>.
	 * Make sure AndroidManifest.xml has appropriate permissions.
	 * @param con Application context
	 * @return Boolean
	 */
	public Boolean isNetworkAvailable(Context con)
	{	 
		try
		{
			connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   

			if(wifiInfo.isConnected() || mobileInfo.isConnected())
			{
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Method for getting the Current Location
	 * @param context
	 * @return Current Location String
	 */
	public String getCurrentLocation(Context con)
	{    	
		try 
		{
			sLocationListener = new MyLocationListener();
			sLocationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
			sLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,sLocationListener);
			sLocation = sLocationManager.getLastKnownLocation("gps");

			if ( sLocation == null ) 
			{
				sLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10,sLocationListener);
				sLocation = sLocationManager.getLastKnownLocation("network");
			}

			if (sLocation != null && sLocation.getLongitude() != 0.0 && sLocation.getLatitude() !=0.0) 
			{
				sLatitude = String.valueOf(sLocation.getLatitude());
				sLongitude = String.valueOf(sLocation.getLongitude());
				sCurrentLocation = sLatitude+","+sLongitude;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return sCurrentLocation;
	}	

	/**
	 * Location Listener class for listening to any updates on the location. 
	 * @author Prashanth
	 *
	 */
	class MyLocationListener implements LocationListener
	{
		public void onLocationChanged(Location loc) 
		{
			if (loc != null)
			{
				sLatitude = String.valueOf(loc.getLatitude());
				sLongitude = String.valueOf(loc.getLongitude());
				//					Toast.makeText(AddKidSpot.context, " Location changed" , Toast.LENGTH_SHORT).show();
				//					sCurrentLocation = sLatitude+","+sLongitude;
			}
		}

		public void onProviderDisabled(String provider) 
		{
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider)
		{
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) 
		{
			// TODO Auto-generated method stub
		}
	}

	/**
	 * Method to turn off GPS once application is excited
	 */
	public static void removeGps() 
	{
		new Thread(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					sLocationManager.removeUpdates(sLocationListener);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				} 
				finally
				{
					sLocationListener = null;
					sLocationManager = null;
					sLocation = null;
				}
			}
		}).start();
		android.os.Process.killProcess(android.os.Process.myPid());
	} 

	/**
	 * Method for getting the device IP Address.
	 * @param context
	 * @return IP Address String
	 */
	public String getIPAddress(Context con)
	{
		String mIPAddress = null;
		try
		{
			InetAddress mLocalHost = InetAddress.getLocalHost();
			mIPAddress = mLocalHost.getHostAddress();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mIPAddress;
	}

	/**
	 * Method for getting the device ID.
	 * @param context
	 * @return Device ID String
	 */
	@SuppressWarnings("static-access")
	public String getDeviceId(Context con)
	{
		String device_id = null;
		TelephonyManager telemngr = (TelephonyManager)con.getSystemService(con.TELEPHONY_SERVICE);
		device_id=telemngr.getDeviceId();
		return device_id;
	}

	/**
	 * Method for getting the current activity running in foreground.
	 * @param context
	 * @return Activity name
	 */
	public String getCurrentTopActivity(Context con) 
	{

		ActivityManager mActivityManager = (ActivityManager) con.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
		ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
		return ar.topActivity.getClassName().toString();
	}
	
	/**
     * Tests an email address for proper syntax.
     *
     * @param email a <code>String</code> value
     * @return a <code>boolean</code> value
     */

    public static boolean isEmail(String email) {

        boolean valid = false;

        // email address must not contain space characters
        if ( email.indexOf(' ') >= 0 ) {
            return false;
        }      

        // email address contains @ character
        if ( email.indexOf('@') < 0 ) {
            return false;
        }      

        	          
        // email address pattern matching
        if(EMAIL_PATTERN.matcher(email).matches()) { 
         // email meets RFC requirements (ex. abc@aaa.com)
            valid = true;
        }
        
        return valid;
    }

	/**
	 * Method for displaying the Custom Dialog used in the application.
	 * @param context
	 * @param message
	 * @return Dialog
	 */
    
    private Dialog dialog = null;
	public void getAlertDialog(final Context con, final String message)
	{
		if(dialog !=null)
			return;
		dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		TextView title = (TextView) dialog.findViewById(R.id.custom_dialog_title);
		Button ok = (Button) dialog.findViewById(R.id.custom_dialog_ok);
		title.setText(message);
		ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(message.equalsIgnoreCase(con.getResources().getString(R.string.alert_dialog_delete_success)))
				{
					con.startActivity(new Intent(con, FlourishHomeActivity.class));
				}
				else if(message.equalsIgnoreCase(con.getResources().getString(R.string.alert_dialog_update_success)))
				{
					con.startActivity(new Intent(con, FlourishHomeActivity.class));
				}
				else if(message.equalsIgnoreCase(con.getResources().getString(R.string.alert_dialog_saved_success)))
				{
					con.startActivity(new Intent(con, FlourishHomeActivity.class));
				}
				else if(message.equalsIgnoreCase(con.getResources().getString(R.string.alert_dialog_create_user_success)))
				{
					
					con.startActivity(new Intent(con, LoginScreen.class));
				}
				dialog.dismiss();
				dialog = null;
			}
		});
		dialog.show();
	}
}