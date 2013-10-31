package com.flourish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.Window;

public class Sales extends BaseTimeOutActivity implements OnClickListener
{
	private ViewGroup mTopBar;
	private ImageView mMenuSlide;
	private View mSalesView;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		inflater = LayoutInflater.from(this);
		mSalesView = inflater.inflate(R.layout.sales, null);
		onSessionStarted(true);
		setContentView(mSalesView);
		
		
		mTopBar = (ViewGroup) mSalesView.findViewById(R.id.tabBar);
		mMenuSlide = (ImageView) mTopBar.findViewById(R.id.menu_slide);
		mMenuSlide.setOnClickListener(this);
	}
 
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.menu_slide:
			startActivity(new Intent(Sales.this, FlourishHomeActivity.class));
			finish();
			break;
		default:
			break;
		}
	}
	

}
