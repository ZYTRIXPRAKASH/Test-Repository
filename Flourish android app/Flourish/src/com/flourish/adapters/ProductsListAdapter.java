package com.flourish.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flourish.R;

public class ProductsListAdapter extends BaseAdapter
{
	
	private Activity mContext = null;
	private List<ProductData> mAllProductList;

	public ProductsListAdapter(Activity context, List<ProductData> mAllProductList)
	{
		mContext = context;
		this.mAllProductList = mAllProductList;
	
	}

	
	
	
	public static class ViewHolder{
          
		TextView mProductName;
		TextView mOnHand;
		TextView mMostRecentPrice;
		
	    }
	
	
	
	
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View mView=convertView;
		ViewHolder holder;
		if (convertView==null) {
			
	   holder=new ViewHolder();
		
		
		
	
			mView = mContext.getLayoutInflater().inflate(R.layout.products_list_row, null);
			
			holder.mProductName = (TextView)mView.findViewById(R.id.product_list_row_name);
			holder.mMostRecentPrice = (TextView)mView.findViewById(R.id.product_list_row_most_recent_price);
			holder.mOnHand = (TextView)mView.findViewById(R.id.product_list_row_on_hand);
			mView.setTag(holder);
			
			
		}
		
		else
		{
			holder=(ViewHolder)mView.getTag();
	
		}
		
		

		  holder.mProductName.setText(mAllProductList.get(position).Name);
			
			String ProductQuantityString=mAllProductList.get(position).OnHand;
			 
			
			if (ProductQuantityString.contains("null")) {
				
				holder.mOnHand.setText("OnHand "+0);
			}
			else
			{
				holder.mOnHand.setText("OnHand "+mAllProductList.get(position).OnHand.replaceAll("-", ""));
			}
			
			holder.mMostRecentPrice.setText(mAllProductList.get(position).MostRecentPrice);
		
		
		
		
		
		return mView;
	}
	
	
	

	@Override
	public int getCount() 
	{
		return mAllProductList.size();
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
	
	
	
	
	
}

