package com.flourish;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {

	private EditText mEditText;

	boolean isDeleted = false, isRepeating = false;

	public CustomTextWatcher(EditText e) {
		mEditText = e;
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

		switch (mEditText.getId()) {
		case R.id.sale_type_quantity_et:

			// mEditText.setText("1");

			Log.v("TAG", "After textChange");
			if (s.length() == 0) {
				s.append("0");
			}

			break;

		case R.id.sale_type_quantity_et2:

			// mEditText.setText("1");

			Log.v("TAG", "After textChange");
			if (s.length()==0) {
				s.append("0");
			}
			

			break;

		case R.id.Dicount_et_sales_invoice_row_ET:

			// mEditText.setText("1");

			Log.v("TAG", "After textChange");
			if (s.length() == 0) {
				s.append("0");
			}
			
			mEditText.setSelection(mEditText.length());

		case R.id.invoice_discount1_value_et:

			// mEditText.setText("1");

			Log.v("TAG", "After textChange");
			if (s.length() == 0) {
				s.append("0");
			}

	
			
		case R.id.shipping_value:

			// mEditText.setText("1");

			Log.v("TAG", "After textChange");
			if (s.length() == 0) {
				s.append("0");
			}
			
		case R.id.add_payment_Propay_CardExperiDate_et:
			if (isDeleted) {
				isDeleted = false;
				return;
			}

			Log.v("TAG", "Length: " + s.length());
			if (s.length() == 2) {
				s.append("/");
				Log.v("TAG", "Length in if: " + s.length());

			}else{
				Log.v("TAG", "Length in else: " + s.length());		
				
			}

			break;
			
		

		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if(isRepeating){
			isRepeating = false;
			return;
		}
		if (count == 0)
			isDeleted = true;
		
		
	if(mEditText.getId() == R.id.Dicount_et_sales_invoice_row_ET
			|| mEditText.getId() == R.id.sale_type_quantity_et||mEditText.getId() == R.id.sale_type_quantity_et2||mEditText.getId() == R.id.shipping_value||mEditText.getId() == R.id.invoice_discount1_value_et){
		Log.v("TAG", "onTextChanged");
		String str = mEditText.getText().toString();
		if(str.startsWith("0")){
			Log.v("TAG", "onTextChanged startsWith 0");
			if(str.length() > 1){
				str = str.substring(1, str.length());
				Log.v("TAG", "onTextChanged startsWith 0 str.length() > 1 substring of str="+str);
				isRepeating = true;
				
				mEditText.setText(str);
				mEditText.setSelection(mEditText.length());
			}
		}
	}


		

	}

}
