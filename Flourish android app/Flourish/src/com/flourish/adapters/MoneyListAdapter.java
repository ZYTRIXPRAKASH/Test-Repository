package com.flourish.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flourish.R;

public class MoneyListAdapter extends BaseAdapter
{
	private ArrayList<String> text = null;
	private ViewHolder116 mHolder = null;
	private Context mContext = null;

	public MoneyListAdapter(Context context, int textViewResourceId, ArrayList<String> text)
	{
		mContext = context;
		this.text = text;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.custom_row, null);
			mHolder = new ViewHolder116();
			mHolder.mText = (TextView)convertView.findViewById(R.id.custom_row_text);
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder116) convertView.getTag();
		}
		if (text!=null)
		{
			if(text.get(position).trim().length() > 0)
			{
				if((!(text.get(position).equalsIgnoreCase("(null)"))))
				{
					mHolder.mText.setText(text.get(position));
				}
			}
			else
			{}
		}
		return convertView;
	}

	@Override
	public int getCount() 
	{
		return text.size();
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

class ViewHolder116
{
	TextView mText;
}