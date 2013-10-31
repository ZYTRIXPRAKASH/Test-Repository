package com.flourish.adapters;

import java.util.List;

import com.flourish.R;
import com.flourish.adapters.MoneyIncomeBaseAdapter.ViewHolder;
import com.flourish.utils.MoneyExpensesData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MoneyExpenseBaseAdapter  extends BaseAdapter{

	
	
	public Context mContext;
	List<MoneyExpensesData>mAllMoneyExpensesList;

	public MoneyExpenseBaseAdapter(Context context,List<MoneyExpensesData> mAllMoneyExpensesList) {
		
		mContext=context;
		this.mAllMoneyExpensesList=mAllMoneyExpensesList;
		
	
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mAllMoneyExpensesList.size();
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

	
	public static class ViewHolder{
          
		TextView mExpensesDescription;
		TextView mExpensesAmount;
		TextView mExpensesTransDateAccountName;
	    }
	
	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View mView=convertView;
		ViewHolder holder;
		
		if (convertView==null) {
		 holder=new ViewHolder();
			mView=((Activity)mContext).getLayoutInflater().inflate(R.layout.money_expenses_row, null);
			
			holder.mExpensesDescription = (TextView)mView.findViewById(R.id.money_expenses_description);
			holder.mExpensesAmount = (TextView)mView.findViewById(R.id.money_expenses_amount);
			holder.mExpensesTransDateAccountName = (TextView)mView.findViewById(R.id.money_expenses_tdate_account_name_tv);

		       /************  Set holder with LayoutInflater ************/
	    		mView.setTag( holder );
			
		}
	
		
	
		else{
			
	 holder=(ViewHolder)mView.getTag();


		}
		
		if (mAllMoneyExpensesList.get(position).Description != null)
		{
			holder.mExpensesDescription.setText(mAllMoneyExpensesList.get(position).Description);
		}
		if(mAllMoneyExpensesList.get(position).Amount != null)
		{
			holder.mExpensesAmount.setText("$"+mAllMoneyExpensesList.get(position).Amount);
		}
		if(mAllMoneyExpensesList.get(position).TransactionDate != null && mAllMoneyExpensesList.get(position).AccountName != null)
		{
			String mExpensesTransactionDateStr = mAllMoneyExpensesList.get(position).TransactionDate;
			String[] mPersonBirthDateArray = mExpensesTransactionDateStr.split("-");
			mExpensesTransactionDateStr = mPersonBirthDateArray[1]+"/"+mPersonBirthDateArray[2].substring(0, 2)+"/"+mPersonBirthDateArray[0];
		
			holder.mExpensesTransDateAccountName.setText(mExpensesTransactionDateStr+" - "+mAllMoneyExpensesList.get(position).AccountName);
		}
		
		
		
		
		
		
		
		return mView;
	}
	

	

}
