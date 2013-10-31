package com.flourish;

import com.flourish.R;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SideLayout extends RelativeLayout
{
	private Activity mContext = null;
	private View inflatedView = null;
	private Button mContacts = null;
	private Button mSales = null;
	private Button mInventory = null;

	public SideLayout(Activity context, String callFrom)
	{
		super(context);
		mContext = context;
		inflatedView = View.inflate(mContext, R.layout.side_layout, null);
		this.addView(inflatedView);

		mContacts = (Button) inflatedView.findViewById(R.id.contacts);
		mSales = (Button) inflatedView.findViewById(R.id.sales);
		mInventory = (Button) inflatedView.findViewById(R.id.inventory);
		mContacts.setOnClickListener(DisplayListener);
		mSales.setOnClickListener(DisplayListener);
		mInventory.setOnClickListener(DisplayListener);
		
	}

	private OnClickListener DisplayListener=new OnClickListener ()
	{
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.contacts:
				RelativeLayout layout = (RelativeLayout) findViewById(R.id.contacts_main_rl);
				Animation a = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_to_left);
//				a=setFillAfter(true); 
				layout.setLayoutAnimation(new LayoutAnimationController(a));
				layout.startLayoutAnimation();
				break;
			default:
				break;
			}
		}
	};
}