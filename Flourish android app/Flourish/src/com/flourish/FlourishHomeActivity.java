 package com.flourish;

import java.util.Date;

import org.apache.http.client.methods.AbortableHttpRequest;

import com.example.menudrawer.MenuDrawer;
import com.example.menudrawer.Position;
import com.flourish.MoneyFragment.MoneyCLickListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FlourishHomeActivity extends FragmentActivity implements MoneyCLickListener{
	
	
	
	
	
	 private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.RightMenuSample.activePosition";
	    private static final String STATE_CONTENT_TEXT = "net.simonvt.menudrawer.samples.RightMenuSample.contentText";

	    private static final int MENU_OVERFLOW = 1;

	    private MenuDrawer mMenuDrawer;
	    private int mActivePosition = -1;

	    private String mContentText;
	    private TextView mContentTextView;
	    
//	    String mMenutype=null;
	    
		
	
	
	
	int mLayoutStatus = 0;
	Button mDashBoardButton = null;
	Button mContactsButton = null;
	Button mSalesButton = null;
	Button mInventoryButton = null;
	Button mMoneyButton = null;
	Button mSettingsButton=null;

	Button mSlideMenuButton = null;

	TextView mMainTopbarTextView = null;
	Button mMainTopBarRightButton = null;
	boolean mMenuSlideOut = false;

	// fragment

	FragmentManager mFragmentManager = null;
	FragmentTransaction mFragmentTransaction = null;

	Fragment mDashBoardFragment = null;
	Fragment mContactsFragment = null;
	Fragment mSalesFragment = null;
	Fragment mInventoryFragment = null;
	Fragment mMoneyFragment = null;
	Fragment mSettingsFragment = null;
	LinearLayout mMenuButtonLisstLinearLyout = null;
	LinearLayout mDetailsLisstLinearLyout = null;
	
	
	int mAddType = -1;
	public static final int CONTACTS = 0;
	public static final int SALES = 1;
	public static final int EXPENSES = 2;
	public static final int INCOME = 3;
	public static final int MILAGE = 4;
	public static final int SETTINGS=5;

	
	
	//checking fragment screen when pressing back button;
	
	String mFragmentScreenDashboard="Dashboard";   //default  dashboard screen
	String mFragmentScreenContacts="Contacts";     //Contacts screen 
	String mFragmentScreenSales="Sales";           //Sales fragment Screen 
	String mFragmentScreenInventory="Inventory";   //Inventory screen 
	String mFragmentScreenMoney="Money";                 //Money fragment Screen 
	
	String screenName;
	
	SharedPreferences mSharedPreferences;
    SharedPreferences.Editor backeditor;
	Intent mIntent;
	
	   View view ;

	 protected void onCreate(Bundle inState) {
	        super.onCreate(inState);

	        if (inState != null) {
	            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
	            mContentText = inState.getString(STATE_CONTENT_TEXT);
	            
	        }
	        
	        mSharedPreferences=getSharedPreferences("myfile", MODE_PRIVATE);
	    	mFragmentManager = getSupportFragmentManager();
	        
	     
	        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT);
	        mMenuDrawer.setContentView(R.layout.flourish_home);
	        
	        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.main_layout_id);
	        view = getLayoutInflater().inflate(R.layout.menu_row, mainLayout,false);
	        mMenuDrawer.setMenuView(view);
	        
	      
	    /*    mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT);
	    //    mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.RIGHT);
	        mMenuDrawer.setContentView(R.layout.flourish_home);
	        
	    
	        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.main_layout_id);
	 
	        //create a view to inflate the layout_item (the xml with the textView created before)
	        View view = getLayoutInflater().inflate(R.layout.menu_row, mainLayout,false);
	 
	          mMenuDrawer.setMenuView(view);
	      */
	   
		
	        
	    

		mDashBoardButton = (Button)mMenuDrawer.findViewById(R.id.DashBoard_btn);
		mContactsButton = (Button)mMenuDrawer. findViewById(R.id.Contacts_btn);
		mSalesButton = (Button)mMenuDrawer. findViewById(R.id.Sales_btn);
		mInventoryButton = (Button)mMenuDrawer. findViewById(R.id.Inventory_btn);
		mMoneyButton = (Button)mMenuDrawer. findViewById(R.id.Money_btn);
		mSettingsButton = (Button)mMenuDrawer. findViewById(R.id.Settings_btn);
		
		
		mSlideMenuButton = (Button) findViewById(R.id.MainTopbarMenuLeft_Btn);
		
		mMenuButtonLisstLinearLyout = (LinearLayout) findViewById(R.id.menuListLayout_ll);
		mMainTopbarTextView = (TextView) findViewById(R.id.MainTopBar_Tv);
		mMainTopBarRightButton = (Button) findViewById(R.id.MainTopbarMenuRight_Btn);

		//mMainTopbarTextView.setText("DashBoard");
	//	mMenuButtonLisstLinearLyout.setVisibility(View.GONE);
		
		mDetailsLisstLinearLyout=(LinearLayout)findViewById(R.id.mDetailsLisstLinearLyout);

		
		

        openFragmentScreen();
		
        
		 
		
		// onClick listener

		mSlideMenuButton.setOnClickListener(buttonClickListner);
		mDashBoardButton.setOnClickListener(buttonClickListner);
		mContactsButton.setOnClickListener(buttonClickListner);
		mSalesButton.setOnClickListener(buttonClickListner);
		mInventoryButton.setOnClickListener(buttonClickListner);
		mMoneyButton.setOnClickListener(buttonClickListner);
		mMainTopBarRightButton.setOnClickListener(buttonClickListner);
		mSettingsButton.setOnClickListener(buttonClickListner);
		
		 mDashBoardButton.setBackgroundResource(R.drawable.dashboard_hover);
	

	}

	View.OnClickListener buttonClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {

			case R.id.MainTopbarMenuLeft_Btn:

	
				
				
				 mMenuDrawer.toggleMenu();
			
				break;
			case R.id.DashBoard_btn:

				mDashBoardFragment = new DashBoardFragment();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				mFragmentTransaction.replace(R.id.MainDetailsFragment,
						mDashBoardFragment);
				// mFragmentTransaction.addToBackStack(null);

				mFragmentTransaction.commit();// Commit the transaction
				mMainTopbarTextView.setText("DashBoard");
				mMainTopBarRightButton.setVisibility(View.GONE);

				
				
				mMenuDrawer.setActiveView(v);
				mMenuDrawer.closeMenu();
				
				
				//Handling back
				 backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Dashboard");
				 backeditor.commit();
				
				 mDashBoardButton.setBackgroundResource(R.drawable.dashboard_hover);
				 mContactsButton.setBackgroundResource(R.drawable.contacts);
				 mSalesButton.setBackgroundResource(R.drawable.sales);
				 mInventoryButton.setBackgroundResource(R.drawable.inventory);
				 mMoneyButton.setBackgroundResource(R.drawable.money);
				 mSettingsButton.setBackgroundResource(R.drawable.settings);
				 
				
				
				
				
				break;

			case R.id.Contacts_btn:
				mContactsButton
						.setBackgroundResource(R.drawable.contacts_hover);

				mContactsFragment = new ContactsFragment();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				mFragmentTransaction.replace(R.id.MainDetailsFragment,
						mContactsFragment);
				// mFragmentTransaction.addToBackStack(null);

				mFragmentTransaction.commit();// Commit the transaction
				mMainTopbarTextView.setText("Contacts");
//				mMenuButtonLisstLinearLyout.setVisibility(View.GONE);
				mMainTopBarRightButton.setVisibility(View.VISIBLE);
				mMainTopBarRightButton.setText(getResources().getString(R.string.add));
				
				mMenuDrawer.setActiveView(v);
				mMenuDrawer.closeMenu();
				
				mAddType = CONTACTS;
				//Handling back
				 backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Contacts");
				 backeditor.commit();
				 mDashBoardButton.setBackgroundResource(R.drawable.dashboard);
				 mContactsButton.setBackgroundResource(R.drawable.contacts_hover);
				 mSalesButton.setBackgroundResource(R.drawable.sales);
				 mInventoryButton.setBackgroundResource(R.drawable.inventory);
				 mMoneyButton.setBackgroundResource(R.drawable.money);
				 mSettingsButton.setBackgroundResource(R.drawable.settings);
				
				

				break;

			case R.id.Sales_btn:
				
				mAddType = SALES;
				
				mSalesFragment = new SalesFragment();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				mFragmentTransaction.replace(R.id.MainDetailsFragment,
						mSalesFragment);
				// mFragmentTransaction.addToBackStack(null);

				mFragmentTransaction.commit();// Commit the transaction
				mMainTopbarTextView.setText("Sales");
//				mMenuButtonLisstLinearLyout.setVisibility(View.GONE);
				mMainTopBarRightButton.setVisibility(View.VISIBLE);
				
				mMainTopBarRightButton.setText(getResources().getString(R.string.add));
				
				mMenuDrawer.setActiveView(v);
				mMenuDrawer.closeMenu();
				
				
				//Handling back
				 backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Sales");
				 backeditor.commit();
				 mDashBoardButton.setBackgroundResource(R.drawable.dashboard);
				 mContactsButton.setBackgroundResource(R.drawable.contacts);
				 mSalesButton.setBackgroundResource(R.drawable.sales_hover);
				 mInventoryButton.setBackgroundResource(R.drawable.inventory);
				 mMoneyButton.setBackgroundResource(R.drawable.money);
				 mSettingsButton.setBackgroundResource(R.drawable.settings);
				break;

			case R.id.Inventory_btn:

				mInventoryFragment = new InventoryFragment();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				mFragmentTransaction.replace(R.id.MainDetailsFragment,
						mInventoryFragment);
				// mFragmentTransaction.addToBackStack(null);

				mFragmentTransaction.commit();// Commit the transaction
				mMainTopbarTextView.setText("Inventory");
//				mMenuButtonLisstLinearLyout.setVisibility(View.GONE);
				mMainTopBarRightButton.setVisibility(View.GONE);
				mMenuDrawer.setActiveView(v);
				mMenuDrawer.closeMenu();
				
				//Handling back
				 backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Inventory");
				 backeditor.commit();
				 mDashBoardButton.setBackgroundResource(R.drawable.dashboard);
				 mContactsButton.setBackgroundResource(R.drawable.contacts);
				 mSalesButton.setBackgroundResource(R.drawable.sales_hover);
				 mInventoryButton.setBackgroundResource(R.drawable.inventory_hover);
				 mMoneyButton.setBackgroundResource(R.drawable.money);
				 mSettingsButton.setBackgroundResource(R.drawable.settings);
				

				break;

			case R.id.Money_btn:
				
				mAddType = EXPENSES ;
				
				mMoneyFragment = new MoneyFragment();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				mFragmentTransaction.replace(R.id.MainDetailsFragment,
						mMoneyFragment);
				// mFragmentTransaction.addToBackStack(null);

				mFragmentTransaction.commit();// Commit the transaction
				mMainTopbarTextView.setText("Money");
				mMainTopBarRightButton.setVisibility(View.VISIBLE);
				mMainTopBarRightButton.setText(getResources().getString(
						R.string.add));
//				mMenuButtonLisstLinearLyout.setVisibility(View.GONE);
				mMenuDrawer.setActiveView(v);
				mMenuDrawer.closeMenu();
				
				mMainTopBarRightButton.setText(getResources().getString(R.string.add));
				
				//Handling back
				 backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Money");
				 backeditor.commit();
				
				 mDashBoardButton.setBackgroundResource(R.drawable.dashboard);
				 mContactsButton.setBackgroundResource(R.drawable.contacts);
				 mSalesButton.setBackgroundResource(R.drawable.sales_hover);
				 mInventoryButton.setBackgroundResource(R.drawable.inventory);
				 mMoneyButton.setBackgroundResource(R.drawable.money_hover);
				 mSettingsButton.setBackgroundResource(R.drawable.settings);
				
				 
				 
				 
				 

				break;
			case R.id.MainTopbarMenuRight_Btn:
				
				if(mAddType == EXPENSES){
//					startActivity(new Intent(FlourishHomeActivity.this, AddExpenses.class));
					startActivityForResult(new Intent(FlourishHomeActivity.this, AddExpenses.class), 0);
				}else if (mAddType == INCOME) {
//					startActivity(new Intent(FlourishHomeActivity.this, AddIncome.class));
					startActivityForResult(new Intent(FlourishHomeActivity.this, AddIncome.class), 1);
				}else if (mAddType == MILAGE) {
					//startActivity(new Intent(FlourishHomeActivity.this, AddMileage.class));
					startActivityForResult(new Intent(FlourishHomeActivity.this, AddMileage.class), 2);
					
					
				}else if (mAddType == CONTACTS) {
					startActivity(new Intent(FlourishHomeActivity.this, CustomDialog.class));
				}else if (mAddType == SALES) {
					 startActivity(new Intent(FlourishHomeActivity.this, CreateInvoice.class));
				}
				else if (mAddType == SETTINGS) {
					
					
					Log.v("TAG", "settings");
					if(mSettingsFragment != null){
						((SettingsFragment)mSettingsFragment).saveUserSettings();
					}
					// startActivity(new Intent(FlourishHomeActivity.this, CreateInvoice.class));
				}
				
			
			
				
				
				
				
				break;
				
				
				
				
			
				
				
				
				
			case R.id.Settings_btn:
				
				
				mAddType = SETTINGS;
				mSettingsFragment = new SettingsFragment();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				mFragmentTransaction.replace(R.id.MainDetailsFragment,mSettingsFragment);
				//mFragmentTransaction.addToBackStack(null);
				mFragmentTransaction.commit();// Commit the transaction
				mMainTopbarTextView.setText("Settings");
               
				mMainTopBarRightButton.setVisibility(View.VISIBLE);
				mMenuDrawer.setActiveView(v);
				mMenuDrawer.closeMenu();

				mMainTopBarRightButton.setText(getResources().getString(R.string.save));
				
				//Handling back
				 backeditor=mSharedPreferences.edit();
				 backeditor.putString("FragmentScreen", "Settings");
				 backeditor.commit();
				 
				 mDashBoardButton.setBackgroundResource(R.drawable.dashboard);
				 mContactsButton.setBackgroundResource(R.drawable.contacts);
				 mSalesButton.setBackgroundResource(R.drawable.sales);
				 mInventoryButton.setBackgroundResource(R.drawable.inventory);
				 mMoneyButton.setBackgroundResource(R.drawable.money_hover);
				 mSettingsButton.setBackgroundResource(R.drawable.settings_hover);
				 
				 
				
				
				break;	
				
				
				
				

			default:
				break;
			}

		}
	};
	/* @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	    }
	*/


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0: //Expences
			((MoneyFragment)mMoneyFragment).mExpensesBtn.performClick();
			
			break;
		case 1: //Income
//			mMoneyButton.performClick();
			((MoneyFragment)mMoneyFragment).mIncomeBtn.performClick();
			break;
		case 2: //Milage
			((MoneyFragment)mMoneyFragment).mMileageBtn.performClick();
			
			break;

		default:
			break;
		}
	};
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		/* SharedPreferences.Editor backeditor=mSharedPreferences.edit();
		 backeditor.putString("FragmentScreen", "Dashboard");
		 backeditor.commit();*/
		
		
		
		
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK )
		{
			// Custom Dialog for exiting the application
			 screenName=	mSharedPreferences.getString("FragmentScreen", null);
			
			
			if (screenName.equalsIgnoreCase("Dashboard"))
			
			{
			
	  
		
			final Dialog dialog = new Dialog(FlourishHomeActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.quit_dialog);

			Button mYes = (Button) dialog.findViewById(R.id.dialogButtonYES);
			Button mNo = (Button) dialog.findViewById(R.id.dialogButtonNO);

			mYes.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					getSharedPreferences("LoginInfo", 0).edit().putBoolean("Login", false).commit();
					startActivity(new Intent(FlourishHomeActivity.this, LoginScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}
			});
			mNo.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{  
					dialog.cancel();
				}
			});
			dialog.show();

			}
			
			
			else{
				SharedPreferences.Editor backeditor1=mSharedPreferences.edit();
				 backeditor1.putString("FragmentScreen", "Dashboard");
				 backeditor1.commit();
				 
				
				 String screenName1=	mSharedPreferences.getString("FragmentScreen", null);
					if (mFragmentScreenDashboard.equalsIgnoreCase(screenName1)) {
						mDashBoardFragment = new DashBoardFragment();
						mFragmentTransaction = mFragmentManager.beginTransaction();

						mFragmentTransaction.replace(R.id.MainDetailsFragment,
								mDashBoardFragment);
						// mFragmentTransaction.addToBackStack(null);

						mFragmentTransaction.commit();// Commit the transaction
						mMainTopbarTextView.setText("DashBoard");
						mMainTopBarRightButton.setVisibility(View.GONE);


						mMenuDrawer.closeMenu();
			
				
			}
			
			
			
			
			return true;
		}
		
		}
		return super.onKeyDown(keyCode, event);
		
		
	}




 


	public void addType(int type) {
		
		mAddType = type;
		
	}

	
	



	
	public void openFragmentScreen()
	{
		
		
	 screenName=	mSharedPreferences.getString("FragmentScreen", null);
		
		if (mFragmentScreenDashboard.equalsIgnoreCase(screenName)) {
			
		//	Toast.makeText(getApplicationContext(), "Dash board", 500).show();
			
			mDashBoardFragment = new DashBoardFragment();
			mFragmentTransaction = mFragmentManager.beginTransaction();

			mFragmentTransaction.replace(R.id.MainDetailsFragment,
					mDashBoardFragment);
			// mFragmentTransaction.addToBackStack(null);

			mFragmentTransaction.commit();// Commit the transaction
			mMainTopbarTextView.setText("DashBoard");
			mMainTopBarRightButton.setVisibility(View.GONE);


			mMenuDrawer.closeMenu();
	      
			
			
			
			
			
			
		}
		
		else if (mFragmentScreenContacts.equalsIgnoreCase(screenName)) {
		//	Toast.makeText(getApplicationContext(), "Contacts", 500).show();
		   
			mContactsButton.setBackgroundResource(R.drawable.contacts_hover);
		mIntent=getIntent();
		
		String mSelectedUserTag=mIntent.getStringExtra("SelectedUserTag");
			
		        
	mContactsFragment = new ContactsFragment();
	mFragmentTransaction = mFragmentManager.beginTransaction();

	mFragmentTransaction.replace(R.id.MainDetailsFragment,
			mContactsFragment);
	// mFragmentTransaction.addToBackStack(null);

	mFragmentTransaction.commit();// Commit the transaction
	mMainTopbarTextView.setText("Contacts");
//	mMenuButtonLisstLinearLyout.setVisibility(View.GONE);
	mMainTopBarRightButton.setVisibility(View.VISIBLE);
	mMainTopBarRightButton.setText(getResources().getString(R.string.add));
	

	mMenuDrawer.closeMenu();
	
	mAddType = CONTACTS;
	
	
	
	
		}
		
	else if (mFragmentScreenSales.equalsIgnoreCase(screenName)) {
		
		//Toast.makeText(getApplicationContext(), "Sales", 500).show();
		mAddType = SALES;
		mSalesFragment = new SalesFragment();
		mFragmentTransaction = mFragmentManager.beginTransaction();

		mFragmentTransaction.replace(R.id.MainDetailsFragment,
				mSalesFragment);
		// mFragmentTransaction.addToBackStack(null);

		mFragmentTransaction.commit();// Commit the transaction
		mMainTopbarTextView.setText("Sales");
		mMainTopBarRightButton.setVisibility(View.VISIBLE);
		mMainTopBarRightButton.setText(getResources().getString(R.string.add));


		mMenuDrawer.closeMenu();
		
		
		
		
			
		}
		
		
	else if (mFragmentScreenInventory.equalsIgnoreCase(screenName)) {
		
		
		
	}
	
		
		
	else if (mFragmentScreenMoney.equalsIgnoreCase(screenName)) {
		
		
		mAddType = EXPENSES ;
		
		mMoneyFragment = new MoneyFragment();
		mFragmentTransaction = mFragmentManager.beginTransaction();

		mFragmentTransaction.replace(R.id.MainDetailsFragment,
				mMoneyFragment);
		// mFragmentTransaction.addToBackStack(null);

		mFragmentTransaction.commit();// Commit the transaction
		mMainTopbarTextView.setText("Money");
		mMainTopBarRightButton.setVisibility(View.VISIBLE);
		mMainTopBarRightButton.setText(getResources().getString(
				R.string.add));
//		mMenuButtonLisstLinearLyout.setVisibility(View.GONE);

		mMenuDrawer.closeMenu();
		
		mMainTopBarRightButton.setText(getResources().getString(R.string.add));
		
		//Handling back
		 backeditor=mSharedPreferences.edit();
		 backeditor.putString("FragmentScreen", "Money");
		 backeditor.commit();
		
		
	}
	else
	{
		
		
		
		
		
		
	}
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
}
