package com.flourish;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SaveInvoiceDialogActivity extends Activity {

	
	Button mSaveAsDraftButton;
	Button mCreateInvoiceButton;
	Button mCreteAndPaymentButton;
	Button mCreateAndDeleveryButton;
	Button mCancelSaveingInvoiceButton;
	Intent mIntent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invoice_saving_types_dialog);
		mSaveAsDraftButton=(Button)findViewById(R.id.save_as_draft_btn);
		mCreateInvoiceButton=(Button)findViewById(R.id.create_invoice_btn);
		mCreteAndPaymentButton=(Button)findViewById(R.id.create_and_payment_btn);
		mCreateAndDeleveryButton=(Button)findViewById(R.id.create_and_add_delevery_btn);
		mCancelSaveingInvoiceButton=(Button)findViewById(R.id.cancel_saveing_incoice_btn);
		mIntent=new Intent();
		
		mSaveAsDraftButton.setOnClickListener(btnClickListner);
		mCreateInvoiceButton.setOnClickListener(btnClickListner);
		mCreteAndPaymentButton.setOnClickListener(btnClickListner);
		mCreateAndDeleveryButton.setOnClickListener(btnClickListner);
		mCancelSaveingInvoiceButton.setOnClickListener(btnClickListner);
		
		
		
	}
	
	
	View.OnClickListener btnClickListner=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.save_as_draft_btn:
				
				
				mIntent.setAction("Save as draft");
				setResult(RESULT_OK, mIntent);
				finish();
				
				
				
				
				break;
			case R.id.create_invoice_btn:
				
				mIntent.setAction("create invoice");
				setResult(RESULT_OK, mIntent);
				finish();
				
				
				break;
				
           case R.id.create_and_payment_btn:
				
        	   mIntent.setAction("create and payment");
				setResult(RESULT_OK, mIntent);
				finish();
				
        	   
				break;
			case R.id.create_and_add_delevery_btn:
				mIntent.setAction("Create and add delevery");
				setResult(RESULT_OK, mIntent);
				finish();
				
				
				break;	
				
          case R.id.cancel_saveing_incoice_btn:
				
        	  mIntent.setAction("Cancel");
				setResult(RESULT_OK, mIntent);
				finish();
				
        	  
				break;
			

			default:
				break;
			}
			
		}
	};

	

}
