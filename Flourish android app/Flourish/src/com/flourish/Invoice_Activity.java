package com.flourish;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Invoice_Activity extends Activity {

	TextView mTopbarTextView=null;
	Button   mTopbarRightButton=null;
	Button   mTopbarLeftButton=null;
	String mPersonIdString=null;
	Intent mIntent=null;
	String mInvoiceNumber=null;

	
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice);
		mIntent=getIntent();
		mPersonIdString=mIntent.getStringExtra("PersonId");
		 mInvoiceNumber=mIntent.getStringExtra("InvoiceNumber");
		
		mTopbarRightButton=(Button)findViewById(R.id.activity_topbar_right_btn);
		
		mTopbarLeftButton=(Button)findViewById(R.id.activity_topBar_left_btn);
		
		mTopbarTextView=(TextView)findViewById(R.id.activity_topbar_tv);
		mTopbarTextView.setText("Invoice #"+mInvoiceNumber);
		mTopbarLeftButton.setText("Save");
		mTopbarLeftButton.setVisibility(View.VISIBLE);
		
		mTopbarRightButton.setOnClickListener(btnClickListner);
		mTopbarLeftButton.setOnClickListener(btnClickListner);
		
		
		
		
	}

View.OnClickListener btnClickListner=new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.activity_topbar_right_btn:
			
		
			
			
			break;
			
	case R.id.activity_topBar_left_btn:
		Dialog mDialog=new  Dialog(Invoice_Activity.this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.invoice_saving_types_dialog);
		
		mDialog.getWindow().setGravity(Gravity.CENTER);
		
	
		mDialog.show();
		
		
			
			break;
			
			
			

		default:
			break;
		}
		
	}
};




















}
