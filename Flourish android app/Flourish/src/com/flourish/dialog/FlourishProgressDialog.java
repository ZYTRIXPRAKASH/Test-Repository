package com.flourish.dialog;

import com.flourish.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.ProgressBar;




public class FlourishProgressDialog implements DialogInterface {
	
	
	
	public static  Dialog dialog;
	
	public static Context context;

	public FlourishProgressDialog(Context context) {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public static  void ShowProgressDialog(final Context context){
		
		
		dialog = new  Dialog( context, R.style.ProgressDialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progressbar_style_layout);
//		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));				
		dialog.setCancelable(false);
		
		ProgressBar mProgressBar =(ProgressBar)dialog.findViewById(R.id.progress_bar);
		
		ProgressBar LoadingProgressBar =(ProgressBar)dialog.findViewById(R.id.data_loading_progress_bar);
		mProgressBar.setIndeterminate(true);
		LoadingProgressBar.setIndeterminate(true);
		
		mProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.progress_anim));
		mProgressBar.setClickable(false);
		
		
		LoadingProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.progress_anim_loading));
		LoadingProgressBar.setClickable(false);
		dialog.show();
		
		
	}
	
	
	
	
	
	
	public static void Dismiss(final Context context)
	{
		
	dialog.dismiss();
		
	}
	
	
	
public static  void MyDialogWithMessage(final Context context){
		
		
		dialog = new  Dialog( context, R.style.NewDialogWithMessage);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progressbar_style_layout);
//		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));				
		dialog.setCancelable(false);
		ProgressBar mLoadingProgressBar = (ProgressBar)dialog.findViewById(R.id.data_loading_progress_bar);
		ProgressBar mProgressBar =(ProgressBar)dialog.findViewById(R.id.progress_bar);
		mProgressBar.setIndeterminate(true);
		mProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.progress_anim));
		mProgressBar.setClickable(false);
		mLoadingProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.progress_anim));
		dialog.show();
		
		
	}
	
	public void MyDialogDismiss(){
		
		
		dialog.dismiss();
		
		
		
		
	}


	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		
		dialog.dismiss();
		
		
	}
	
	
	
	
	
	
	
}
