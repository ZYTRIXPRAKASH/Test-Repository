/**
 * 
 */
package com.flourish;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.appnetwork.DialogClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 
 *
 */
public class Dashboard extends Activity implements OnClickListener{
	
	private Button mCreateContactButton = null;
	private Button mCreateInvoiceButton = null;
	private Button mAddExpensesButton = null;
	private Button mAddMileageButton = null;
	private Button mNeedHelpButton = null;
	AppNetwork mAppNetwork=null;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		
		intializeVariables();
	}

	private void intializeVariables() {
		
		mAppNetwork=new AppNetwork();
		mCreateContactButton = (Button)findViewById(R.id.create_contact_button);
		mCreateInvoiceButton = (Button)findViewById(R.id.create_invoice_button);
		mAddExpensesButton = (Button)findViewById(R.id.add_expenses_button);
		mAddMileageButton = (Button)findViewById(R.id.add_mileage_button);
		mNeedHelpButton  = (Button)findViewById(R.id.need_help_button);
		
		mCreateContactButton.setOnClickListener(this);
		mCreateInvoiceButton.setOnClickListener(this);
		mAddExpensesButton.setOnClickListener(this);
		mAddMileageButton.setOnClickListener(this);
		mNeedHelpButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_contact_button:
			
			
			if (mAppNetwork.isNetworkAvailable(Dashboard.this)) {
				startActivity(new Intent(Dashboard.this, AddContact.class));
				
			}
			else{
				
				mAppNetwork.getAlertDialog(Dashboard.this, getString(R.string.alert_dialog_no_network));
				
			}

			break;

		case R.id.create_invoice_button:
			
			if (mAppNetwork.isNetworkAvailable(Dashboard.this)) {
				startActivity(new Intent(Dashboard.this, CreateInvoice.class));
				
			}
			else{
				
				mAppNetwork.getAlertDialog(Dashboard.this, getString(R.string.alert_dialog_no_network));
				
			}
			
			
			
			
			
			
			break;
			
		case R.id.add_expenses_button:
			
			
			if (mAppNetwork.isNetworkAvailable(Dashboard.this)) {
				startActivity(new Intent(Dashboard.this, AddExpenses.class));
				
			}
			else{
				
				mAppNetwork.getAlertDialog(Dashboard.this, getString(R.string.alert_dialog_no_network));
				
			}
			
			break;
			
		case R.id.add_mileage_button:
			
			
			if (mAppNetwork.isNetworkAvailable(Dashboard.this)) {
				startActivity(new Intent(Dashboard.this, AddMileage.class));
				break;
			}
			else{
				
				mAppNetwork.getAlertDialog(Dashboard.this, getString(R.string.alert_dialog_no_network));
				
			}
			
			
		default:
			break;
		}
		
	}
	
	

}
