package com.flourish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class DashBoardFragment extends Fragment {
	
	
	Fragment mFragment;
	
	Button mCreateContactButton=null;
	Button mCreateInvoiceButton=null;
	Button mAddExpensesButton=null;
	Button mAddMileageButton=null;
	Button mNeedHelpButton=null;
	
	

	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
		
		
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View mView;

		mView=inflater.inflate(R.layout.dashboard_fragment, container, false);
		
		
		mCreateContactButton=(Button)mView.findViewById(R.id.create_contact_button);
		mCreateInvoiceButton=(Button)mView.findViewById(R.id.create_invoice_button);
		
		mAddExpensesButton=(Button)mView.findViewById(R.id.add_expenses_button);
		mAddMileageButton=(Button)mView.findViewById(R.id.add_mileage_button);
		mNeedHelpButton=(Button)mView.findViewById(R.id.need_help_button);
		
		
		
		
		
		

		mCreateContactButton.setOnClickListener(buttonClickListener);
		mCreateInvoiceButton.setOnClickListener(buttonClickListener);
		mAddExpensesButton.setOnClickListener(buttonClickListener);
		mAddMileageButton.setOnClickListener(buttonClickListener);
		mNeedHelpButton.setOnClickListener(buttonClickListener);
		
		
		
		
		return mView ;
		
		
		
		
		
	}
	
	
	View.OnClickListener buttonClickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			switch (v.getId()) {
			case R.id.create_contact_button:
				startActivity(new Intent(getActivity(),CustomDialog.class));
				break;
			
			case R.id.create_invoice_button:
				startActivity(new Intent(getActivity(),CreateInvoice.class));
				break;
				
			case R.id.add_expenses_button:
				startActivity(new Intent(getActivity(), AddExpenses.class));
				break;
				
			case R.id.add_mileage_button:
				startActivity(new Intent(getActivity(), AddMileage.class));
				break;
			
		
		case R.id.need_help_button:
			
			startActivity(new Intent(getActivity().getApplicationContext(), SupportScreen.class));
			
			break;
				
			default:
				break;
			}
			
			
			
		}
	};
			
	
	

}
