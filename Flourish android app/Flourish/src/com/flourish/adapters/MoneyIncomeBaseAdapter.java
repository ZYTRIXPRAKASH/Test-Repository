package com.flourish.adapters;

import java.util.List;

import com.flourish.R;
import com.flourish.utils.MoneyIncomeData;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MoneyIncomeBaseAdapter extends BaseAdapter {
	public Context mContext;
	List<MoneyIncomeData> mAllMoneyIncomeList;

	public MoneyIncomeBaseAdapter(Context context,
			List<MoneyIncomeData> mAllMoneyIncomeList) {
		// TODO Auto-generated constructor stub

		mContext = context;
		this.mAllMoneyIncomeList = mAllMoneyIncomeList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAllMoneyIncomeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	  /********* Create a holder Class to contain inflated file elements *********/
    public static class ViewHolder{
          
    public	RelativeLayout mIncomeAmountRl;
	public  TextView mIncomeDescription;
	public 	TextView mIncomeAmount;
	public 	TextView mIncomeTransDateAccountName;

  
    }
     

	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View mView=convertView;
        ViewHolder holder;
	        if(convertView==null){
	            
	            /****** View Holder Object to contain tabitem.xml file elements ******/
	        	  holder = new ViewHolder();
	    		mView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.money_income_row, null);

	    		holder.mIncomeAmountRl = (RelativeLayout) mView.findViewById(R.id.money_income_amount_rl);
	    		holder.mIncomeDescription = (TextView) mView
	    				.findViewById(R.id.money_income_description);
	    		holder.mIncomeAmount = (TextView) mView.findViewById(R.id.money_income_amount);
	    		holder.mIncomeTransDateAccountName = (TextView) mView.findViewById(R.id.money_income_tdate_account_name_tv);
	     	 
		       /************  Set holder with LayoutInflater ************/
	    		mView.setTag( holder );
	        }
	        else
	        	
	        {
	            holder=(ViewHolder)mView.getTag();
	         


	        }
	        
	        

    		if (mAllMoneyIncomeList.get(position).Description != null) {
    			holder.mIncomeDescription
    					.setText(mAllMoneyIncomeList.get(position).Description);
    		}
    		if (mAllMoneyIncomeList.get(position).Amount != null) {
    			String mIncomeAmountStr = mAllMoneyIncomeList.get(position).Amount;
    			if (mIncomeAmountStr.contains("-")) {
    				mIncomeAmountStr = mIncomeAmountStr.substring(1,
    						mIncomeAmountStr.length());
    				holder.mIncomeAmountRl.setBackgroundResource(R.drawable.gray);
    				holder.mIncomeAmount.setTextColor(mContext.getResources().getColor(
    						R.color.dark_income_gray));
    			}
    			holder.mIncomeAmount.setText("$" + mIncomeAmountStr);
    		}
    		if (mAllMoneyIncomeList.get(position).TransactionDate != null
    				&& mAllMoneyIncomeList.get(position).AccountName != null) {
    			String mIncomeTransactionDateStr = mAllMoneyIncomeList
    					.get(position).TransactionDate;
    			String[] mPersonBirthDateArray = mIncomeTransactionDateStr
    					.split("-");
    			mIncomeTransactionDateStr = mPersonBirthDateArray[1] + "/"
    					+ mPersonBirthDateArray[2].substring(0, 2) + "/"
    					+ mPersonBirthDateArray[0];

    			holder.mIncomeTransDateAccountName.setText(mIncomeTransactionDateStr
    					+ " - " + mAllMoneyIncomeList.get(position).AccountName);
    		}     
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
			return mView;

	    }
		
	


}
