package com.flourish.adapters;

import java.util.List;

import com.flourish.R;
import com.flourish.utils.BookingsData;
import com.flourish.utils.SalesData;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookingsListBaseAdapter  extends BaseAdapter{
	
	
	
private List<BookingsData>  mAllBookingsList;

	private Context mContext = null;


	
	public BookingsListBaseAdapter(Context context,	List<BookingsData> mAllBookingsList) {
	
		mContext = context;
		this.mAllBookingsList=mAllBookingsList;
	
		
	}


	
	@Override
	public int getCount() 
	{
		return mAllBookingsList.size();
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
          

		TextView mSubject ;
		TextView mBookingStartDate ;
		TextView mBookingType;
		

  
    }
	
	
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View mView=convertView;
		ViewHolder holder;
		if (convertView==null) {
			
			holder=new ViewHolder();
			mView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.bookings_list_row, null);
			
			
		

			holder.mSubject = (TextView)mView.findViewById(R.id.booking_subject);
			holder. mBookingStartDate = (TextView)mView.findViewById(R.id.booking_staring_date);
			holder. mBookingType = (TextView)mView.findViewById(R.id.booking_type_tv);
			mView.setTag(holder);
			
		}
		
			
		else{
			holder=(ViewHolder)mView.getTag();
	
			
		}


	
		
		holder.mBookingType.setText(mAllBookingsList.get(position).FirstName+" "+mAllBookingsList.get(position).LastName);
			
			
		holder.mSubject.setText(mAllBookingsList.get(position).BookingSubject);
			
			String dataString=mAllBookingsList.get(position).StartDate;
			
			
			String[] str_array = dataString.split("T");
			String date  = str_array[0]; 
		   
			
			holder.mBookingStartDate.setText(date);
			holder.mBookingType.setText(mAllBookingsList.get(position).BookingType);
			
		
		return mView;
	}

		
	
}


