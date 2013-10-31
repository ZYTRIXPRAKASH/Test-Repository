package com.flourish.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flourish.R;
import com.flourish.utils.InVentoryData;

public class InventoryProductListBaseAdapter  extends BaseAdapter{
	
	
	public Context mContext;
	public List<InVentoryData> mAllInvetoryProductList;



	public InventoryProductListBaseAdapter(Context context, List<InVentoryData> mAllInventoryList) {
		
		mContext=context;
		this.mAllInvetoryProductList=mAllInventoryList;
		
		
	}
	

	

	@Override
	public int getCount() 
	
	
	{
		
		
		Log.v("TAG", "size "+mAllInvetoryProductList.size());
		return mAllInvetoryProductList.size();
		
		
		
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
	
	  /********* Create a holder Class to contain inflated file elements *********/
    public static class ViewHolder{
          
    	TextView mProductName;
		 TextView mMostRecentPrice;
		 TextView   mOnHand;
  
    }
     
	
	
	
	
	
	
	
	
	
	
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View mView = convertView;
		ViewHolder holder;
		 if (convertView==null) {
			 holder=new  ViewHolder();
			 mView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.products_list_row, null);
				
			 holder.mProductName = (TextView)mView.findViewById(R.id.product_list_row_name);
			 holder.mMostRecentPrice = (TextView)mView.findViewById(R.id.product_list_row_most_recent_price);
			 holder. mOnHand = (TextView)mView.findViewById(R.id.product_list_row_on_hand);
		          mView.setTag(holder);
		
			
		}
		 
		 else{
			 
			 holder=(ViewHolder)mView.getTag();
  
		
  
  
		 }

		 
		 holder.mProductName.setText(mAllInvetoryProductList.get(position).Name);
	        //Removing negative value
	        String mOnHandSring=mAllInvetoryProductList.get(position).OnHand;
	        int mOnHandInt= Integer.parseInt(mOnHandSring.replaceAll("-", ""));
	        
	        holder.mOnHand.setText("OnHand "+Math.abs(mOnHandInt));//removing - value
	        holder. mMostRecentPrice.setText(mAllInvetoryProductList.get(position).MostRecentPrice);
		
		
	     	Log.v("TAG", "Product name  "+mAllInvetoryProductList.get(position).Name);
		
		
		return mView;
	}

}