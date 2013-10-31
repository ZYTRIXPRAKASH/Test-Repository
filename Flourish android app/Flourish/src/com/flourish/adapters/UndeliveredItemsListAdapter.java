package com.flourish.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.flourish.R;

public class UndeliveredItemsListAdapter extends BaseAdapter
{
	private ArrayList<String> mItemName = null;
	private ArrayList<String> mItemQty = null;
	private ArrayList<Integer> mCheckedPosition = new ArrayList<Integer>();
	private RowHolder mHolder = null;
	private Context mContext = null;

	public UndeliveredItemsListAdapter(Context context, int undeliveredItemsRow, ArrayList<String> itemName, ArrayList<String> itemQty)
	{
		mContext = context;
		this.mItemName = itemName;
		this.mItemQty = itemQty;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		
		
		if (convertView == null)
		{
			convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.undelivered_items_row, null);
			mHolder = new RowHolder();
			
			mHolder.mUndeliveredItemName = (TextView)convertView.findViewById(R.id.sales_deliveries_undelivered_item_tv);
			mHolder.mUndeliveredItemQty = (EditText)convertView.findViewById(R.id.sales_deliveries_undelivered_item_quantity_et);
			mHolder.mCheckBox = (CheckBox)convertView.findViewById(R.id.sales_undelivered_check_button);
			
		
			
			mHolder.mUndeliveredItemQty.setOnEditorActionListener(
			        new EditText.OnEditorActionListener() {
			    @Override
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			        if ( actionId == EditorInfo.IME_ACTION_DONE ) {
			        	
			        
			     	//new UpdateLineItemInvoiceDiscountAsyncTask().execute();
			      
			        	//hide the keyboard after done
			        	//InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
						//imm.hideSoftInputFromWindow(mHolder.mUndeliveredItemQty.getWindowToken(), 0);
			        	
			          
			            return true;
			        }
			        return false;
			    }
			});
			

			
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (RowHolder) convertView.getTag();

	
		}
		
		
		if (mItemName!=null)
		{
			if(mItemName.get(position).trim().length() > 0)
			{
				if((!(mItemName.get(position).equalsIgnoreCase("(null)"))))
				{
					mHolder.mUndeliveredItemName.setText(mItemName.get(position));
					mHolder.mUndeliveredItemQty.setText(mItemQty.get(position));
				}
			}
			else
			{
				
			}
			
		}
		
		
		
		
		
		
		
		
		
		
		
		mHolder.mCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mHolder.mCheckBox.isChecked()){
					mCheckedPosition.add(position);
				}
			}
		});
		
		
		
		
		
		
		setmCheckedPosition(mCheckedPosition);
		return convertView;
	}

	@Override
	public int getCount() 
	{
		return mItemName.size();
	}

	@Override
	public String getItem(int position) 
	{
		return mItemName.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	public ArrayList<Integer> getmCheckedPosition() {
		return mCheckedPosition;
	}

	public void setmCheckedPosition(ArrayList<Integer> mCheckedPosition) {
		this.mCheckedPosition = mCheckedPosition;
	}
}

class RowHolder
{
	TextView mUndeliveredItemName;
	TextView mUndeliveredItemQty ;
	CheckBox mCheckBox;
	
}