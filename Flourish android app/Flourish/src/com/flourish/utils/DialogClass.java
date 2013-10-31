//**************************************************************************************************
//**************************************************************************************************
//                                     Copyright (c) 2012 by Zytrix SoftLabs
//                                            ALL RIGHTS RESERVED
//**************************************************************************************************
//**************************************************************************************************
//
//        Project name                     : Amgonna
//        Class Name                       : DialogClass
//        Date                             : September 25th 2012
//        Author                           : Yerram Naveen Kumar
//        Version                          : 1.0
//
//***************************************************************************************************
//  Class Description:
//  DialogClass this class is to show the dialog
//
//***************************************************************************************************
//        Update history:
//        Date :                            Developer Name :     Modification Comments :
//
//***************************************************************************************************

package com.flourish.utils;

import com.flourish.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class DialogClass implements DialogInterface.OnClickListener {

	static Context context;

	public DialogClass(Context context) {
		super();
		DialogClass.context = context;
	}

	/*
	 * Create Alert Dialog is method with parameters message that message
	 * displays as dialog
	 */
	public static void createDAlertDialog(final Context context,
			final String message) {
		String setmessage = message;
		AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle(context.getString(R.string.app_name));
		alertbox.setMessage(setmessage);
		alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// context.finish();
			}
		});
		try {
			alertbox.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void createDAlertDialogActivityFinish(final Activity context,
			final String message) {
		String setmessage = message;
		AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle(context.getString(R.string.app_name));
		alertbox.setMessage(setmessage);
		alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				context.finish();
			}
		});
		try {
			alertbox.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void createDialoServerNoResponse(final Activity activity) {

		AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
		alertbox.setTitle(R.string.app_name);
		//alertbox.setMessage(R.string.alert_dialog_server_no_response);
		alertbox.setNeutralButton(R.string.alert_dialog_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						activity.finish();
					}
				});
		try {
			alertbox.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void createDialoServerNoResponseMessage(
			final Activity activity, String msg) {

		AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
		alertbox.setTitle(R.string.app_name);
		alertbox.setMessage(msg);
		alertbox.setNeutralButton(R.string.alert_dialog_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						activity.finish();
					}
				});
		try {
			alertbox.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
