package com.flourish;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class VehicleListCustomDialog extends Dialog {

	private TextView mHeaderTv;
	private ListView mListview;
	private static Context mContext;
	private String selectedItem 	= null;
	private int selectedPosition 	= 0;
	private long selectedItemId 	= 0;

	public VehicleListCustomDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = context;

		setContentView(R.layout.spinner_dialog_layout);
		mHeaderTv = (TextView) findViewById(R.id.headerTv);
		mListview = (ListView) findViewById(R.id.mSpinner_listview);
	}

	public ListView getListview() {
		return mListview;
	}

	public TextView getHeaderTv() {
		return mHeaderTv;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public long getSelectedItemId() {
		return selectedItemId;
	}

	public void setSelectedItemId(long arg3) {
		this.selectedItemId = arg3;
	}

}
