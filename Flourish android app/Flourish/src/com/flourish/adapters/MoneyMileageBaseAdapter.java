package com.flourish.adapters;

import java.util.List;

import com.flourish.R;
import com.flourish.adapters.MoneyExpenseBaseAdapter.ViewHolder;
import com.flourish.utils.MoneyMileageData;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

  public class MoneyMileageBaseAdapter  extends BaseAdapter{

	Context mContext=null;
	List<MoneyMileageData> mAllMoneyMileageList=null;
	
	
	
	
	public MoneyMileageBaseAdapter(Context context,List<MoneyMileageData> mAllMoneyMilageList) {
		// TODO Auto-generated constructor stub
		mContext=context;
		this.mAllMoneyMileageList=mAllMoneyMilageList;
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAllMoneyMileageList.size();
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
          
		TextView mTripDescription;
		TextView mTripMiles;
		TextView mMileageDate;
		TextView mVehicleDescription;
		
	    }
	
	
	
	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View mView=convertView;
		ViewHolder holder;
if (convertView==null) {
	
		
		holder=new ViewHolder();
		mView=((Activity)mContext).getLayoutInflater().inflate(R.layout.money_mileage_row, null);
		
		holder.mTripDescription = (TextView)mView.findViewById(R.id.money_mileage_trip_description);
		holder.mTripMiles = (TextView)mView.findViewById(R.id.money_mileage_trip_miles);
		holder.mMileageDate = (TextView)mView.findViewById(R.id.money_mileage_mileage_date);
		holder.mVehicleDescription = (TextView)mView.findViewById(R.id.money_mileage_vehicle_descptn);
		 /************  Set holder with LayoutInflater ************/
		mView.setTag( holder );
		
	
}
else{
	
	 holder=(ViewHolder)mView.getTag();

	
	
}	


if (mAllMoneyMileageList.get(position).TripDescription != null)
{
	holder.mTripDescription.setText(mAllMoneyMileageList.get(position).TripDescription);
}
if(mAllMoneyMileageList.get(position).TripMiles != null)
{
	holder.mTripMiles.setText(mAllMoneyMileageList.get(position).TripMiles+" miles");
}
if(mAllMoneyMileageList.get(position).MileageDate != null)
{
	String mMileageDateStr =mAllMoneyMileageList.get(position).MileageDate;
	String[] mPersonBirthDateArray = mMileageDateStr.split("-");
	mMileageDateStr = mPersonBirthDateArray[1]+"/"+mPersonBirthDateArray[2].substring(0, 2)+"/"+mPersonBirthDateArray[0];
	holder.mMileageDate.setText(mMileageDateStr);
}
if(mAllMoneyMileageList.get(position).VehicleDescription != null)
{

	holder.mVehicleDescription.setText(mAllMoneyMileageList.get(position).VehicleDescription);
}



		
		return mView;
	}

}
