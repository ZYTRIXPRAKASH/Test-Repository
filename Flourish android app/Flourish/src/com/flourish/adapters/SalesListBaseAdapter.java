package com.flourish.adapters;

import java.util.List;

import com.flourish.R;
import com.flourish.utils.SalesData;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SalesListBaseAdapter  extends BaseAdapter{
	
	
	
private List<SalesData>  mAllSalesList;

	private Context mContext = null;


	
	public SalesListBaseAdapter(Context context,	List<SalesData> mAllSalesList) {
	
		mContext = context;
		this.mAllSalesList=mAllSalesList;
	
		
	}


	
	@Override
	public int getCount() 
	{
		return mAllSalesList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;

	
	
	}
	
	

	public static class ViewHolder{
          

		
		
		TextView mPersonName ;
		TextView mTotalAmount ;
		TextView mInvoiceNumber ;
		TextView mInvoiceDate ;
		TextView mStatusdisplay ;
	    }
	
	
	
	
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View mView=convertView;
		ViewHolder holder;
		
			if (convertView==null) {
				
				holder=new  ViewHolder();
		
		
				mView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.sales_list_row, null);
	
			
			holder.mPersonName = (TextView)mView.findViewById(R.id.sales_list_row_person_name);
			holder.mTotalAmount = (TextView)mView.findViewById(R.id.sales_list_row_total_value);
			holder.mInvoiceNumber = (TextView)mView.findViewById(R.id.sales_list_row_invoice_value);
			holder.mInvoiceDate = (TextView)mView.findViewById(R.id.sales_list_row_date_value);
			holder.mStatusdisplay = (TextView)mView.findViewById(R.id.sales_list_row_statusdisplay);
	
			holder.mPersonName.setText(mAllSalesList.get(position).FirstName+" "+mAllSalesList.get(position).LastName);
			
			 /************  Set holder with LayoutInflater ************/
			mView.setTag( holder );
			
			
			}
			
			else{
				

				holder=(ViewHolder)mView.getTag();
			}
			
			

			
			

			
	holder.mStatusdisplay.setText(mAllSalesList.get(position).StatusDisplay);
	if (mAllSalesList != null)
	{
		if(mAllSalesList.get(position).Total.trim().length() > 0)
		{
			String totalAmountStr = mAllSalesList.get(position).Total;
			if(totalAmountStr.contains("-"))
			{
				totalAmountStr = totalAmountStr.substring(1, mAllSalesList.get(position).Total.length());
			}
			holder.mTotalAmount.setText("$"+totalAmountStr);
		}
	}
	
	
	
	if (mAllSalesList.get(position) != null)
	{
		if(mAllSalesList.get(position).InvoiceNumber.trim().length() > 0)
		{
			holder.mInvoiceNumber.setText("#"+mAllSalesList.get(position).InvoiceNumber + " " + "-" + " ");
		}
	}
	if (mAllSalesList.get(position).InvoiceDate != null)
	{
		if(mAllSalesList.get(position).InvoiceDate.trim().length() > 0)
		{
			String mInvoiceDateStr = mAllSalesList.get(position).InvoiceDate;
			String[] mPersonBirthDateArray = mInvoiceDateStr.split("-");
			mInvoiceDateStr = mPersonBirthDateArray[1]+"/"+mPersonBirthDateArray[2].substring(0, 2)+"/"+mPersonBirthDateArray[0];
			holder.mInvoiceDate.setText(mInvoiceDateStr);
		}
	}
	if (mAllSalesList.get(position).Outstanding != null)
	{
		if(mAllSalesList.get(position).Outstanding.trim().length() > 0);
		{
			String outstandingStr = mAllSalesList.get(position).Outstanding;
			if(outstandingStr.contains("-"))
			{
				outstandingStr = outstandingStr.substring(1, mAllSalesList.get(position).Outstanding.length());
			}
			
		}
	}
	
	

	
	
			
			
			
			
			
			
			
			
			
			
			
			
		return mView;
	}

	

		

}


