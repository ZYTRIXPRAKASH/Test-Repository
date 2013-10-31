package com.flourish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InventoryDetails  extends Activity{
	Intent mIntent;
	TextView mOnHandValue_tv;
	TextView mOnHandQuantity_tv;
	TextView mNumber_tv;
	TextView mWholesale_tv;
	TextView mRetail_tv;
	TextView mTarget_tv;
	Button mBackInvetoryDetails;
	
	
	
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.inventory_details);
	mIntent=getIntent();
	
	mOnHandValue_tv=(TextView)findViewById(R.id.inventory_product_details_on_hand_value);
	mOnHandQuantity_tv=(TextView)findViewById(R.id.inventory_product_details_on_hand_quantity_value);
	mNumber_tv=(TextView)findViewById(R.id.inventory_product_details_number_val);
	mWholesale_tv=(TextView)findViewById(R.id.inventory_product_details_wholesale_val);
	mTarget_tv=(TextView)findViewById(R.id.inventory_product_details_target_val);
	mBackInvetoryDetails=(Button)findViewById(R.id.inventory_product_details_back);
	
	mOnHandValue_tv.setText(mIntent.getStringExtra("OnHandCost"));
	mOnHandQuantity_tv.setText(mIntent.getStringExtra("OnHandQuantity").replace("-", ""));
	mWholesale_tv.setText(mIntent.getStringExtra("Wholesale"));
	mOnHandValue_tv.setText(mIntent.getStringExtra("Retail"));
	mTarget_tv.setText(mIntent.getStringExtra("Target"));
	mNumber_tv.setText(mIntent.getStringExtra("Number"));
	
	mBackInvetoryDetails.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	});
	
	
			
	
}


}
