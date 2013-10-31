/*package com.flourish;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.adapters.MoneyExpensesAdapter;
import com.flourish.adapters.MoneyIncomeAdapter;
import com.flourish.adapters.MoneyMileageAdapter;
import com.flourish.adapters.ProductsListAdapter;
import com.flourish.adapters.SalesListAdapter;
import com.flourish.contacts.Item;
import com.flourish.contacts.Items;
import com.flourish.contacts.ItemsSections;
import com.flourish.contacts.NamesAdapter;
import com.flourish.contacts.NamesParser;
import com.flourish.utils.Constants;

public class Menu extends BaseTimeOutActivity implements OnClickListener, OnItemClickListener, TextWatcher, OnScrollListener, AnimationListener
{
	private View mSideLayout = null;
	private View mMainLayout = null;
	private RelativeLayout mContactRl = null;
	private RelativeLayout mSalesRl = null;
	private RelativeLayout mInventoryRl = null;
	private RelativeLayout mSettingsRl = null;
	private RelativeLayout mMoneyLayout = null;
	private RelativeLayout mAddContactLayout = null;
	private RelativeLayout mMoneyTopBarRl = null;
	private TextView mTopText = null;
	private ListView mContactsListView = null;
	private ListView mSalesListView = null;
	private ListView mInventoryProductsListView = null;
	
	
	private ListView mMoneyIncomeListView = null;
	private ListView mMoneyExpensesListView = null;
	private ListView mMoneyMileageListView = null;
	
	
	
	private MoneyIncomeAdapter mMoneyIncomeAdapter = null;
	private List<Items> mItems = null;
	private List<Items> filterArray = new ArrayList<Items>();
	private ArrayList<Item> itemsSection = new ArrayList<Item>();
	private ArrayList<String> mInvoiceTypeIdList = new ArrayList<String>();
	private ArrayList<String> mInvoiceIdList = new ArrayList<String>();
	private ArrayList<String> mPersonIdList = new ArrayList<String>();
	private ArrayList<String> mInvoiceNumberList = new ArrayList<String>();
	private ArrayList<String> mTaxAfterDiscountList = new ArrayList<String>();
	private ArrayList<String> mTaxShippingList = new ArrayList<String>();
	private ArrayList<String> mIsDelieveredList = new ArrayList<String>();
	private ArrayList<String> mInvoiceTypeNameList = new ArrayList<String>();
	private ArrayList<String> mInvoiceDateList = new ArrayList<String>();
	private ArrayList<String> mTotalAmountList = new ArrayList<String>();
	private ArrayList<String> mFirstNameList = new ArrayList<String>();
	private ArrayList<String> mLastNameList = new ArrayList<String>();
	private ArrayList<String> mOutstandingList = new ArrayList<String>();
	private ArrayList<String> mInvoiceDiscountList = new ArrayList<String>();
	private ArrayList<String> mShippingList = new ArrayList<String>();
	private ArrayList<String> mTaxRateList = new ArrayList<String>();
	private ArrayList<String> mTaxableTotalList = new ArrayList<String>();
	private ArrayList<String> mTaxAmountList = new ArrayList<String>();
	private ArrayList<String> mProductIdList = new ArrayList<String>();
	private ArrayList<String> mProductNameList = new ArrayList<String>();
	private ArrayList<String> mOnHandValueList = new ArrayList<String>();
	private ArrayList<String> mOnHandQuantityList = new ArrayList<String>();
	private ArrayList<String> mWholeSaleList = new ArrayList<String>();
	private ArrayList<String> mNumberList = new ArrayList<String>();
	private ArrayList<String> mRetailList = new ArrayList<String>();
	private ArrayList<String> mTargetList = new ArrayList<String>();
	private ArrayList<String> mVehicleIdList = new ArrayList<String>();
	private ArrayList<String> mVehicleTripIdList = new ArrayList<String>();
	private ArrayList<String> mTripDescriptionList = new ArrayList<String>();
	private ArrayList<String> mTripMilesList = new ArrayList<String>();
	private ArrayList<String> mMileageDateList = new ArrayList<String>();
	private ArrayList<String> mVehileDescriptionList = new ArrayList<String>();
	private ArrayList<String> mIncomeAmountList = new ArrayList<String>();
	private ArrayList<String> mIncomeTransactionDateList = new ArrayList<String>();
	private ArrayList<String> mIncomeAccountNameList = new ArrayList<String>();
	private ArrayList<String> mIncomeDescriptionList = new ArrayList<String>();
	private ArrayList<String> mIncomeAccountIdList = new ArrayList<String>();
	private ArrayList<String> mIncomeReferenceList = new ArrayList<String>();
	private ArrayList<String> mExpensesAmountList = new ArrayList<String>();
	private ArrayList<String> mExpensesTransactionDateList = new ArrayList<String>();
	private ArrayList<String> mExpensesAccountNameList = new ArrayList<String>();
	private ArrayList<String> mExpensesDescriptionList = new ArrayList<String>();
	private ArrayList<String> mExpensesAccountIdList = new ArrayList<String>();
	private ArrayList<String> mExpensesReferenceList = new ArrayList<String>();
	private ArrayList<String> mExpensesJournalList = new ArrayList<String>();
	private ArrayList<String> mIncomeJournalList = new ArrayList<String>();
	private ArrayList<String> mInvoiceStatusList = new ArrayList<String>(); 
	
	
	private Button mContactsBtn = null;
	private Button mSalesBtn = null;
	private Button mInventoryBtn = null;
	//private Button mSettingsBtn = null;
	private Button mMoneyBtn = null;
	private Button mDashboardBtn = null;
	private Button mMenuSlideOutBtn = null;
	private Button mTopBarBtn = null;
	private Button mFromPhone = null;
	private Button mManually = null;
	private Button mCancel = null;
	private Button mOk = null;
	private Button mNo = null;
	
	private Button mExpensesBtn = null;
	private Button mMileageBtn = null;	
	private Button mIncomeBtn = null;
	
	
	private EditText mSearchContacts = null;
	private EditText mSearchInventory = null;
	private Button mLogoutButton = null;

	private ProgressDialog mProgressDialog = null;
	private ConnectionManager mConnectionManager = null;
	private AppUtils mAppUtils = null;
	private AlertDialog mAlert = null;
	private static final int PICK_CONTACT = 1;
	private NamesAdapter mNamesAdapterObj = null;
	private SalesListAdapter mSalesListAdapter = null;
	private ProductsListAdapter mProductsListAdapter = null;
	private MoneyMileageAdapter mMoneyMileageAdapter = null;
	private MoneyExpensesAdapter mMoneyExpenseAdapter = null;
	private String mSessionId = null;
	private String mWhichScreen = null;
	private String mSearchContactsStr = null;
	private String mGetContactsResponse = null;
	private String mGetSalesListResponse = null;
	private String mGetProductListResponseStr = null;
	private String mSearchStr = null;
	private String mCallResponse = null;
	private String mTotalPages = null;
	private String mMoneyExpensesResponse;
	private String mMoneyIncomeResponse;
	private String mCheckMoneyType="Expenses";
	public String mMoneyMileageResponse;
	private int mCurrentPage = 1;
	private int mTotalPagesTag = 0;
	public int mSelectItem=0;
	private boolean mInventorySearch = false;
	private boolean isListLoaded = false;
	private boolean mSoftKeyboardVisibility = true;
	boolean mMenuSlideOut = false;
	AnimParams animParams = new AnimParams();
	private RelativeLayout mDashboardRl = null;
	private Button mCreateContactButton = null;
	private Button mCreateInvoiceButton = null;
	private Button mAddExpensesButton = null;
	private Button mAddMileageButton = null;
	private Button mNeedHelpButton = null;
	SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_screen);
		onSessionStarted(true);
mSharedPreferences=getPreferences(MODE_PRIVATE);
		
		mSessionId = getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		
		mSessionId=mSharedPreferences.getString("sessionId", null);
		
		Log.v("TAG", "Session id  from login "+mSessionId);
		
		mWhichScreen = getSharedPreferences("WhichScreen", 0).getString("ScreenName", "nothing");

		initializeVariables();

		if (mAppUtils.isNetworkAvailable(Menu.this)) 
		{
			if(mWhichScreen.equalsIgnoreCase("SalesDetails"))
			{
				salesView();
				getSharedPreferences("WhichScreen", 0).edit().putString("ScreenName", "nothing").commit();
			}
			else
			{
				getSharedPreferences("WhichScreen", 0).edit().putString("ScreenName", "dashboard").commit();
				dashboardView();
			}
		}
		else 
		{
			mAppUtils.getAlertDialog(Menu.this, getResources().getString(R.string.alert_dialog_no_network));
		}

		if(mTopText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.contacts)))
		{
			mContactsBtn.setBackgroundResource(R.drawable.contacts_hover);
			mTopBarBtn.setVisibility(View.VISIBLE);
			mTopBarBtn.setText(getResources().getString(R.string.add_contact));
		}
		
	}

	*//**
	 * Method for initializing variables
	 *//*
	private void initializeVariables() 
	{
		mAppUtils = new AppUtils();
		mConnectionManager = new ConnectionManager();
		
		mContactRl = (RelativeLayout)findViewById(R.id.contacts_include_rl);
		mAddContactLayout = (RelativeLayout)findViewById(R.id.add_contact_include_rl);
		mSalesRl = (RelativeLayout)findViewById(R.id.sales_include_rl);
		mInventoryRl = (RelativeLayout)findViewById(R.id.inventory_include_rl);
		mSettingsRl = (RelativeLayout)findViewById(R.id.settings_include_rl);
		mMoneyLayout=(RelativeLayout)findViewById(R.id.money_include_rl);
		mMoneyTopBarRl = (RelativeLayout)findViewById(R.id.money_top_bar_rl);
		mTopText = (TextView)findViewById(R.id.top_bar_tv);
		mSideLayout = findViewById(R.id.side_layout_rl);
		mMainLayout = findViewById(R.id.main_layout_rl);
		mContactsBtn = (Button)findViewById(R.id.contacts);
		mSalesBtn = (Button)findViewById(R.id.sales);
		mInventoryBtn = (Button)findViewById(R.id.inventory);
		//mSettingsBtn = (Button)findViewById(R.id.settings);
		mFromPhone = (Button)findViewById(R.id.from_phone);
		mManually = (Button)findViewById(R.id.manually);
		mCancel = (Button)findViewById(R.id.cancel);
		mExpensesBtn = (Button)findViewById(R.id.money_expenses_btn);
		mContactsBtn.setOnClickListener(this);
		mMenuSlideOutBtn = (Button)findViewById(R.id.menu_slideout);
		mLogoutButton = (Button)findViewById(R.id.logout_user);
		mMoneyBtn = (Button)findViewById(R.id.money);
		mDashboardBtn = (Button)findViewById(R.id.dashboard);
		mExpensesBtn = (Button)findViewById(R.id.money_expenses_btn);
		mIncomeBtn = (Button)findViewById(R.id.money_income_btn);
		mMileageBtn = (Button)findViewById(R.id.money_mileage_btn);
		mTopBarBtn = (Button)findViewById(R.id.top_bar_btn);
		mContactsListView = (ListView) findViewById(R.id.contacts_listview);
		mSalesListView = (ListView) findViewById(R.id.sales_listview);
		mInventoryProductsListView = (ListView) findViewById(R.id.favourites_list);
		mMoneyExpensesListView = (ListView)findViewById(R.id.money_expenses_lv);
		mMoneyMileageListView = (ListView)findViewById(R.id.money_mileage_lv);
		mMoneyIncomeListView = (ListView)findViewById(R.id.money_income_lv);
		mSearchInventory = (EditText) findViewById(R.id.inventory_search_query);
		
		mSearchContacts = (EditText) findViewById(R.id.input_search_query);
		
		mDashboardRl  = (RelativeLayout)findViewById(R.id.dashboard_main_rl);
		mCreateContactButton = (Button)findViewById(R.id.create_contact_button);
		mCreateInvoiceButton = (Button)findViewById(R.id.create_invoice_button);
		mAddExpensesButton = (Button)findViewById(R.id.add_expenses_button);
		mAddMileageButton = (Button)findViewById(R.id.add_mileage_button);
		mNeedHelpButton = (Button)findViewById(R.id.need_help_button);
		
		
		mSalesBtn.setOnClickListener(this);
		mInventoryBtn.setOnClickListener(this);
		mDashboardBtn.setOnClickListener(this);
		//mSettingsBtn.setOnClickListener(this);
		mMenuSlideOutBtn.setOnClickListener(this);
		mTopBarBtn.setOnClickListener(this);
		mContactsListView.setOnItemClickListener(this);
		mSalesListView.setOnItemClickListener(this);
		
		
		
		mInventoryProductsListView.setOnItemClickListener(this);
		mSalesListView.setOnScrollListener(this);
		
		mSearchContacts.setOnClickListener(this);
		mSearchContacts.addTextChangedListener(this);
		
		
		mSearchInventory.setOnClickListener(this);
		mSearchInventory.addTextChangedListener(this);
		
		
		mLogoutButton.setOnClickListener(this);
		mFromPhone.setOnClickListener(this);
		mManually.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		mMoneyBtn.setOnClickListener(this);
		mMoneyMileageListView.setOnItemClickListener(this);
		mMoneyExpensesListView.setOnItemClickListener(this);
		mMoneyIncomeListView.setOnItemClickListener(this);
		mExpensesBtn.setOnClickListener(this);
		mIncomeBtn.setOnClickListener(this);
		mMileageBtn.setOnClickListener(this);
		
		mCreateContactButton.setOnClickListener(this);
		mCreateInvoiceButton.setOnClickListener(this);
		mAddExpensesButton.setOnClickListener(this);
		mAddMileageButton.setOnClickListener(this);
		mNeedHelpButton.setOnClickListener(this);
	}

	*//**
	 * Method for menu animation slideout
	 *//*
	private void getAnimation()
	{
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();  // deprecated
		int height = display.getHeight();

		System.out.println("onClick " + new Date());
		Menu me = Menu.this;
		Context context = me;
		Animation anim;

		int w = mMainLayout.getMeasuredWidth();
		int h = mMainLayout.getMeasuredHeight();
		int left = 0;
		if(width > 600)
		{
			left = (int) (width * 0.24);
		}
		else
		{
			left = (int) (width * 0.28);
		}


		if (!mMenuSlideOut) {
			Log.v("response", "==menuOut=="+mMenuSlideOut);
			anim = new TranslateAnimation(0, left, 0, 0);
			mSideLayout.setVisibility(View.VISIBLE);
			animParams.init(left, 0, left + w, h);
		} else {
			Log.v("response", "==menuOut=="+mMenuSlideOut);
			anim = new TranslateAnimation(0, -left, 0, 0);
			animParams.init(0, 0, w, h);
		}
		anim.setDuration(400);
		anim.setAnimationListener(me);
		//Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
		anim.setFillAfter(true);
		mMainLayout.startAnimation(anim);
	}
	
	
	
	
	

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.contacts:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
			
			contactsView();
			break;

		case R.id.sales:
			salesView();
			break;

		case R.id.inventory:
			inventoryView();
			break;

		case R.id.settings:
			settingsView();
			break;

		case R.id.money:
			MoneyView();
			break;
			
		case R.id.dashboard:
			dashboardView();
			break;
		
		case R.id.create_contact_button:
			startActivity(new Intent(Menu.this,AddContact.class));
			break;
		
		case R.id.create_invoice_button:
			startActivity(new Intent(Menu.this,CreateInvoice.class));
			break;
			
		case R.id.add_expenses_button:
			startActivity(new Intent(Menu.this, AddExpenses.class));
			break;
			
		case R.id.add_mileage_button:
			startActivity(new Intent(Menu.this, AddMileage.class));
			break;
			
			
		case R.id.money_expenses_btn:
			mCheckMoneyType = "Expenses";
			
			mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_first_selected);
			
			mExpensesBtn.setTextColor(getResources().getColor(R.color.white));
			mIncomeBtn.setTextColor(getResources().getColor(R.color.dark_gray));
			mMileageBtn.setTextColor(getResources().getColor(R.color.dark_gray));
			
			
			new GetMoneyExpensesTask().execute();
			break;

		case R.id.money_mileage_btn:
			mCheckMoneyType = "Mileage";
			
			
			mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_third_selected);
			mExpensesBtn.setTextColor(getResources().getColor(R.color.dark_gray));
			mIncomeBtn.setTextColor(getResources().getColor(R.color.dark_gray));
			mMileageBtn.setTextColor(getResources().getColor(R.color.white));
			
			
			new GetMoneyMileageTask().execute();
			break;

		case R.id.money_income_btn:
			mCheckMoneyType = "Income";
			mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_second_selected);
			mExpensesBtn.setTextColor(getResources().getColor(R.color.dark_gray));
			mIncomeBtn.setTextColor(getResources().getColor(R.color.white));
			mMileageBtn.setTextColor(getResources().getColor(R.color.dark_gray));
			mMoneyExpensesListView.setVisibility(View.GONE);
			new GetMoneyIncomeTask().execute();
			break;

		case R.id.input_search_query:
			if(mSideLayout.getVisibility() == View.VISIBLE)
			{
				mMenuSlideOut = false;	
				mMainLayout.clearAnimation();
				mSideLayout.setVisibility(View.GONE);
				mSearchContacts.setFocusable(true);
				mSearchContacts.setFocusableInTouchMode(true);
				mSearchContacts.setClickable(true);
				
				
			}
			break;

		case R.id.inventory_search_query:
			if(mSideLayout.getVisibility() == View.VISIBLE)
			{
				mMenuSlideOut = false;
				mMainLayout.clearAnimation();
				mSideLayout.setVisibility(View.GONE);
				mSearchInventory.setFocusable(true);
				mSearchInventory.setFocusableInTouchMode(true);
				mSearchInventory.setClickable(true);
			}
			break;

		case R.id.menu_slideout:
			InputMethodManager imm11 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm11.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
			getAnimation();
			break;

		case R.id.top_bar_btn:
			if(mTopText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.contacts)))
			{
				InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
				TranslateAnimation moveRighttoLeft = new TranslateAnimation(0, 0, 200, 0);
				moveRighttoLeft.setDuration(1000);
				moveRighttoLeft.setFillAfter(true);
				mAddContactLayout.startAnimation(moveRighttoLeft);
				mAddContactLayout.setVisibility(View.VISIBLE);
				mSearchContacts.setFocusable(false);
				mSearchContacts.setFocusableInTouchMode(false);
				mSearchContacts.setClickable(false);
			}
			else if(mTopText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.sales)))
			{
					startActivity(new Intent(Menu.this, CreateInvoice.class).putExtra("personId", "")
							.putExtra("fromWhichScreen", "Menu"));
					finish();
			}
			else if(mTopText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.money)))
			{
				if(mCheckMoneyType.equalsIgnoreCase("Mileage"))
				{
					startActivity(new Intent(Menu.this, AddMileage.class));
				}
				else if(mCheckMoneyType.equalsIgnoreCase("Income"))
				{
					startActivity(new Intent(Menu.this,AddIncome.class));
				}
				else if(mCheckMoneyType.equalsIgnoreCase("Expenses"))
				{
					startActivity(new Intent(Menu.this, AddExpenses.class));
				}
			}
			break;

		case R.id.logout_user:

			if(mLogoutButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.logout)))
			{
				getSharedPreferences("LoginInfo", 0).edit().putBoolean("Login", false).commit();
				// Custom Dialog for logging out the application
				final Dialog logoutDialog = new Dialog(Menu.this);
				logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				logoutDialog.setContentView(R.layout.logout_dialog);

				mOk = (Button) logoutDialog.findViewById(R.id.logout_dialog_yes);
				mNo=(Button)logoutDialog.findViewById(R.id.logout_dialog_no);

				mOk.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						startActivity(new Intent(Menu.this, LoginScreen.class));
						finish();
					}
				});

				logoutDialog.show();
				mNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						logoutDialog.cancel();
					}
				});
			}
			break;

		case R.id.from_phone:
			Intent intent = new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, PICK_CONTACT);
			break;

		case R.id.manually:
			startActivity(new Intent(Menu.this, AddContact.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			finish();
			break;

		case R.id.cancel:

			TranslateAnimation moveRighttoLeft = new TranslateAnimation(0, 0, 0, 200);
			moveRighttoLeft.setDuration(1000);
			mAddContactLayout.startAnimation(moveRighttoLeft);
			mAddContactLayout.setVisibility(View.GONE);
			mSearchContacts.setFocusable(true);
			mSearchContacts.setFocusableInTouchMode(true);
			mSearchContacts.setClickable(true);
			break;
			
		default:
			break;
		}
	}

	*//**
	 * method for picking up the contacts from phone
	 *//*
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) 
	{
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) 
		{
		case (PICK_CONTACT) :
			if (resultCode == Activity.RESULT_OK)
			{
				Uri contactData = data.getData();
				ContentResolver cr = getContentResolver();
				Cursor c =  managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
					Constants.CONTACT_NAME = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					Constants.EMAIl_ADDRESSES = new ArrayList<String>();
					Constants.PHONE_NUMBERS = new ArrayList<String>();
					Cursor emailCur = cr.query( 
							ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
							new String[]{id}, null); 
					while (emailCur.moveToNext()) { 
						// This would allow you get several email addresses
						// if the email addresses were stored in an array
						Constants.EMAIl_ADDRESSES.add(emailCur.getString(
								emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
						String emailType = emailCur.getString(
								emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)); 
					} 
					emailCur.close();
					if (Integer.parseInt(c.getString(
							c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
					{
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
						while (pCur.moveToNext()) {
							// Do something with phones
							Constants.PHONE_NUMBERS.add(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
							Log.v("response", "==Phone=="+pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
						} 
						pCur.close();
						// Address
						String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"; 
						String[] addrWhereParams = new String[]{id, 
								ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE}; 
						Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, 
								null, addrWhere, addrWhereParams, null); 
						while(addrCur.moveToNext()) {
							String poBox = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
							Constants.STREET = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
							Constants.CITY = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
							Constants.STATE = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
							Constants.POSTAL_CODE = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
							Constants.COUNTRY = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
							String type = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
						} 
						addrCur.close();
					}
					getSharedPreferences("FROM_PHONE_CONTACTS", 0).edit().putBoolean("FROM_PHONE", true).commit();
					startActivity(new Intent(Menu.this, AddContact.class));
					finish();
				}
			}
		break;
		}
	}
	

	*//**
	 * Method for Dashboard Display
	 *//*
	private void dashboardView() {
		
		mMenuSlideOut = false;
		mMainLayout.clearAnimation();
		mSideLayout.setVisibility(View.GONE);
		mContactRl.setVisibility(View.GONE);
		mSalesRl.setVisibility(View.GONE);
		mInventoryRl.setVisibility(View.GONE);
		mSettingsRl.setVisibility(View.GONE);
		mMoneyLayout.setVisibility(View.GONE);
		mContactsBtn.setBackgroundResource(R.drawable.contacts);
		mSalesBtn.setBackgroundResource(R.drawable.sales);
		mInventoryBtn.setBackgroundResource(R.drawable.inventory);
		//mSettingsBtn.setBackgroundResource(R.drawable.settings);
		mMoneyBtn.setBackgroundResource(R.drawable.money);
		mDashboardBtn.setBackgroundResource(R.drawable.dashboard_hover);
		
		mTopText.setText(getResources().getString(R.string.dashboard));
		mTopBarBtn.setVisibility(View.GONE);
		mDashboardRl.setVisibility(View.VISIBLE);
	}

	*//**
	 * Method for menu from contacts
	 *//*
	private void contactsView()
	{
		mSearchContacts.setFocusable(true);
		mSearchContacts.setFocusableInTouchMode(true);
		mSearchContacts.setClickable(true);
		mSideLayout.setVisibility(View.GONE);
		mMenuSlideOut = false;
		mMainLayout.clearAnimation();
		mContactRl.setVisibility(View.VISIBLE);
		mSalesRl.setVisibility(View.GONE);
		mInventoryRl.setVisibility(View.GONE);
		mSettingsRl.setVisibility(View.GONE);
		mMoneyLayout.setVisibility(View.GONE);
		mDashboardRl.setVisibility(View.GONE);
		mContactsBtn.setBackgroundResource(R.drawable.contacts_hover);
		mSalesBtn.setBackgroundResource(R.drawable.sales);
		mInventoryBtn.setBackgroundResource(R.drawable.inventory);
		//mSettingsBtn.setBackgroundResource(R.drawable.settings);
		mMoneyBtn.setBackgroundResource(R.drawable.money);
		mDashboardBtn.setBackgroundResource(R.drawable.dashboard);
		mTopText.setText(getResources().getString(R.string.contacts));
		mTopBarBtn.setVisibility(View.VISIBLE);
		mTopBarBtn.setText(getResources().getString(R.string.add_contact));

		if (mAppUtils.isNetworkAvailable(Menu.this)) 
		{
			new GetContactsTask().execute();
		}
		else 
		{
			mAppUtils.getAlertDialog(Menu.this, getResources().getString(R.string.alert_dialog_no_network));
		}
	}


	*//**
	 * Method for sales view from side menu
	 *//*

	private void salesView()
	{
		mSearchInventory.setFocusable(true);
		mSearchInventory.setFocusableInTouchMode(true);
		mSearchInventory.setClickable(true);
		mMenuSlideOut = false;
		mMainLayout.clearAnimation();
		mSideLayout.setVisibility(View.GONE);
		mContactRl.setVisibility(View.GONE);
		mSalesRl.setVisibility(View.VISIBLE);
		mInventoryRl.setVisibility(View.GONE);
		mSettingsRl.setVisibility(View.GONE);
		mMoneyLayout.setVisibility(View.GONE);
		mDashboardRl.setVisibility(View.GONE);
		mContactsBtn.setBackgroundResource(R.drawable.contacts);
		mSalesBtn.setBackgroundResource(R.drawable.sales_hover);
		mInventoryBtn.setBackgroundResource(R.drawable.inventory);
		mDashboardBtn.setBackgroundResource(R.drawable.dashboard);
		//mSettingsBtn.setBackgroundResource(R.drawable.settings);
		mMoneyBtn.setBackgroundResource(R.drawable.money);
		mTopText.setText(getResources().getString(R.string.sales));
		mTopBarBtn.setVisibility(View.VISIBLE);
		mTopBarBtn.setText(getResources().getString(R.string.add_contact));
		
		if (mAppUtils.isNetworkAvailable(Menu.this)) 
		{
			new GetSalesListTask().execute();
		}
		else 
		{
			mAppUtils.getAlertDialog(Menu.this, getResources().getString(R.string.alert_dialog_no_network));
		}

	
	
	}


	*//**
	 * Method for Inventory View from side Menu
	 *//*

	private void inventoryView()
	{
		mMenuSlideOut = false;
		mMainLayout.clearAnimation();
		mSideLayout.setVisibility(View.GONE);
		mContactRl.setVisibility(View.GONE);
		mSalesRl.setVisibility(View.GONE);
		mInventoryRl.setVisibility(View.VISIBLE);
		mSettingsRl.setVisibility(View.GONE);
		mMoneyLayout.setVisibility(View.GONE);
		mDashboardRl.setVisibility(View.GONE);
		mContactsBtn.setBackgroundResource(R.drawable.contacts);
		mSalesBtn.setBackgroundResource(R.drawable.sales);
		mInventoryBtn.setBackgroundResource(R.drawable.inventory_hover);
		mDashboardBtn.setBackgroundResource(R.drawable.dashboard);
		//mSettingsBtn.setBackgroundResource(R.drawable.settings);
		mMoneyBtn.setBackgroundResource(R.drawable.money);
		mTopText.setText(getResources().getString(R.string.inventory));
		mTopBarBtn.setVisibility(View.GONE);

		if (mAppUtils.isNetworkAvailable(Menu.this)) 
		{
			new InventoryTask().execute();
		}
		else 
		{
			mAppUtils.getAlertDialog(Menu.this, getResources().getString(R.string.alert_dialog_no_network));
		}
	
	
	
	}


	*//**
	 * Method for Money view from side Menu
	 *//*

	private void MoneyView()
	{
		
		mCheckMoneyType = "Expenses";
		mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_first_selected);
		mMenuSlideOut = false;
		mMainLayout.clearAnimation();
		mSideLayout.setVisibility(View.GONE);
		mContactRl.setVisibility(View.GONE);
		mSalesRl.setVisibility(View.GONE);
		mInventoryRl.setVisibility(View.GONE);
		mSettingsRl.setVisibility(View.GONE);
		mDashboardRl.setVisibility(View.GONE);
		mMoneyLayout.setVisibility(View.VISIBLE);
		mMoneyBtn.setBackgroundResource(R.drawable.money_hover);
		mContactsBtn.setBackgroundResource(R.drawable.contacts);
		mSalesBtn.setBackgroundResource(R.drawable.sales);
		mInventoryBtn.setBackgroundResource(R.drawable.inventory);
		mDashboardBtn.setBackgroundResource(R.drawable.dashboard);
		//mSettingsBtn.setBackgroundResource(R.drawable.settings);
		mTopText.setText(getResources().getString(R.string.money));
		mTopBarBtn.setVisibility(View.VISIBLE);
		mTopBarBtn.setText(getResources().getString(R.string.add_contact));

		if (mAppUtils.isNetworkAvailable(Menu.this)) 
		{
			new GetMoneyExpensesTask().execute();
		}
		else 
		{
			mAppUtils.getAlertDialog(Menu.this, getResources().getString(R.string.alert_dialog_no_network));
		}
	
	
	
	}

	*//**
	 * Method for setting view from side menu
	 *//*
	private void settingsView()
	{
		mMenuSlideOut=false;
		mMainLayout.clearAnimation();
		mSideLayout.setVisibility(View.GONE);
		mContactRl.setVisibility(View.GONE);
		mSalesRl.setVisibility(View.GONE);
		mInventoryRl.setVisibility(View.GONE);
		mSettingsRl.setVisibility(View.VISIBLE);
		mDashboardRl.setVisibility(View.GONE);
		mMoneyLayout.setVisibility(View.GONE);
		mContactsBtn.setBackgroundResource(R.drawable.contacts);
		mSalesBtn.setBackgroundResource(R.drawable.sales);
		mInventoryBtn.setBackgroundResource(R.drawable.inventory);
		mDashboardBtn.setBackgroundResource(R.drawable.dashboard);
		//mSettingsBtn.setBackgroundResource(R.drawable.settings_hover);
		mTopText.setText(getResources().getString(R.string.settings));
		mLogoutButton.setText(getResources().getString(R.string.logout));
		mMoneyBtn.setBackgroundResource(R.drawable.money);
		mTopBarBtn.setVisibility(View.GONE);
	
	
	}

	*//**
	 * Method for displaying the contacts AsyncTask
	 *//*
	private class GetContactsTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(Menu.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			mGetContactsResponse = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_contacts_url)+mSessionId);
			Log.v("response", "==mGetContactsResponse=="+mGetContactsResponse);
			mItems = new NamesParser().getContacts(mGetContactsResponse);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mGetContactsResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this, LoginScreen.class));
				}
				else if(mGetContactsResponse.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(Menu.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else if (null == mItems || mItems.size() == 0)
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this,LoginScreen.class));
					finish();
				}
				else
				{
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
					setAdapterToListview(mItems);
					
					
				}
			}
			else
			{
				mProgressDialog.dismiss();
			}
			super.onPostExecute(result);
		}
	}

	public class InventoryTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(Menu.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			mProductIdList = new ArrayList<String>();
			mProductNameList = new ArrayList<String>();
			mOnHandValueList = new ArrayList<String>();
			mOnHandQuantityList = new ArrayList<String>();
			mNumberList = new ArrayList<String>();
			mWholeSaleList = new ArrayList<String>();
			mRetailList = new ArrayList<String>();
			mTargetList = new ArrayList<String>();

			if(mInventorySearch)
			{
				mProductIdList = new ArrayList<String>();
				mGetProductListResponseStr = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_product_list_url)+mSessionId+"/"+mSearchStr);
				Log.v("TAG", "==mGetProductListResponse=="+mGetProductListResponseStr);
				getProductsList();
				mInventorySearch = false;
			}
			else
			{
				getInventoryProductsData();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mInventorySearch && mGetProductListResponseStr.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this, LoginScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}
				else if(mInventorySearch && mGetProductListResponseStr.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(Menu.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mProductsListAdapter = new ProductsListAdapter(Menu.this, R.layout.products_list_row, mProductNameList, mOnHandQuantityList, mWholeSaleList);
					mInventoryProductsListView.setAdapter(mProductsListAdapter);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	
	
	*//**
	 * Method for displaying list of sales AsyncTask
	 *
	 *//*
	public class GetSalesListTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(Menu.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		
		
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			mGetSalesListResponse = mConnectionManager.setUpHttpGet(getResources().getString(R.string.get_sales_list_url)+mSessionId+"/-/-/-/-/-/-/-/-/InvoiceNumber/DESC/"+mCurrentPage+"/20");
			getSalesList();
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mGetSalesListResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this, LoginScreen.class));
					finish();
				}
				else if(mGetSalesListResponse.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(Menu.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else 
				{
				
					isListLoaded = true;
					mSalesListAdapter = new SalesListAdapter(Menu.this, R.layout.sales_list_row, mInvoiceNumberList, mInvoiceDateList, mTotalAmountList, mFirstNameList, mLastNameList, mOutstandingList, mInvoiceStatusList);
					mSalesListView.setAdapter(mSalesListAdapter);
					mSalesListView.setSelection(mSelectItem);
				}
			}
			else
			{
				mProgressDialog.dismiss();
			}
			super.onPostExecute(result);
		}
	}
	*//**
	 * Method for displaying money expenses AsyncTask
	 *//*
	private class GetMoneyExpensesTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(Menu.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			mMoneyExpensesResponse = mConnectionManager.setUpHttpGet(getString(R.string.get_expenses_list)+mSessionId+"/-/-/-");
			Log.v("response", "==mMoneyExpensesResponse=="+mMoneyExpensesResponse);
			getMoneyExpenses();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mMoneyExpensesResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this, LoginScreen.class));
					finish();
				}
				else if(mMoneyExpensesResponse.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(Menu.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mMoneyIncomeListView.setVisibility(View.GONE);
					mMoneyMileageListView.setVisibility(View.GONE);
					mMoneyExpensesListView.setVisibility(View.VISIBLE);
					mMoneyExpenseAdapter = new MoneyExpensesAdapter(Menu.this, R.layout.money_income_row,mExpensesDescriptionList,mExpensesAmountList,mExpensesTransactionDateList,mExpensesAccountNameList);
					mMoneyExpensesListView.setAdapter(mMoneyExpenseAdapter);
				}
			}
			else
			{
				mProgressDialog.dismiss();
			}
			super.onPostExecute(result);
		}
	}
	
	
	
	*//**
	 *Method for displaying money income details AsyncTask
	 *//*
	private class GetMoneyIncomeTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(Menu.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			mMoneyIncomeResponse = mConnectionManager.setUpHttpGet(getString(R.string.get_income_list)+mSessionId+"/-/-/-");
			Log.v("response", "==mMoneyIncomeResponse=="+mMoneyIncomeResponse);
			getMoneyIncome();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mMoneyIncomeResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this, LoginScreen.class));
					finish();
				}
				else if(mMoneyIncomeResponse.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(Menu.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mMoneyIncomeListView.setVisibility(View.VISIBLE);
					mMoneyMileageListView.setVisibility(View.GONE);
					mMoneyExpensesListView.setVisibility(View.GONE);
					mMoneyIncomeAdapter = new MoneyIncomeAdapter(Menu.this, R.layout.money_income_row,mIncomeDescriptionList,mIncomeAmountList,mIncomeTransactionDateList,mIncomeAccountNameList);
					mMoneyIncomeListView.setAdapter(mMoneyIncomeAdapter);
				}
			}
			else
			{
				mProgressDialog.dismiss();
			}
			super.onPostExecute(result);
		}
	}

	*//**
	 * Method for displaying money mileage list AsyncTask 
	 *//*
	private class GetMoneyMileageTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(Menu.this,"Flourish", "Loading...", true);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			mMoneyMileageResponse = mConnectionManager.setUpHttpGet(getString(R.string.get_mileage_list)+mSessionId+"/-/-/-");
			Log.v("response", "==mMoneyMileageResponse=="+mMoneyMileageResponse);
			getMoneyMileage();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if (null != mProgressDialog && mProgressDialog.isShowing()) 
			{
				mProgressDialog.dismiss();
				if(mMoneyMileageResponse.contains("Login Key Not Valid"))
				{
					getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
					startActivity(new Intent(Menu.this, LoginScreen.class));
					finish();
				}
				else if(mMoneyMileageResponse.contains("Bad Parameters"))
				{
					mAppUtils.getAlertDialog(Menu.this, getString(R.string.alert_dialog_update_invoice_unsuccess));
				}
				else
				{
					mMoneyIncomeListView.setVisibility(View.GONE);
					mMoneyMileageListView.setVisibility(View.VISIBLE);
					mMoneyExpensesListView.setVisibility(View.GONE);
					mMoneyMileageAdapter = new MoneyMileageAdapter(Menu.this, R.layout.money_mileage_row,mTripDescriptionList,mTripMilesList,mMileageDateList,mVehileDescriptionList);
					mMoneyMileageListView.setAdapter(mMoneyMileageAdapter);
				}
			}
			else
			{
				mProgressDialog.dismiss();
			}
			super.onPostExecute(result);
		}
	}

	*//**
	 * Method for displaying inventory products listview
	 *//*
	public void getInventoryProductsData() 
	{
		mProductIdList.add("131379");
		mProductNameList.add("Face Cream");
		mWholeSaleList.add("$16.44");
		mOnHandQuantityList.add("On Hand: 24");
		mOnHandValueList.add("0.00");
		mProductIdList.add("131378");
		mProductNameList.add("Lip Balm");
		mWholeSaleList.add("$245.23");
		mOnHandQuantityList.add("On Hand: 5");
		mOnHandValueList.add("0.00");
		mProductIdList.add("131377");
		mProductNameList.add("Face Cream");
		mWholeSaleList.add("$245.23");
		mOnHandQuantityList.add("On Hand: 5");
		mOnHandValueList.add("0.00");
	}

	
	
	*//**
	 * Method for parsing the Server response for getting products
	 *//*
	public void getProductsList()
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mGetProductListResponseStr);
			JSONArray mJsonArray;
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mProductIdList.add(mJsonArray.getJSONObject(i).getString("ProductId"));
				mProductNameList.add(mJsonArray.getJSONObject(i).getString("Name"));
				mOnHandValueList.add(mJsonArray.getJSONObject(i).getString("OnHandCost"));
				mOnHandQuantityList.add(mJsonArray.getJSONObject(i).getString("OnHand"));
				mNumberList.add(mJsonArray.getJSONObject(i).getString("Number"));
//				mWholeSaleList.add(mJsonArray.getJSONObject(i).getString("MostRecentWholesale"));
				mWholeSaleList.add("$0.00");
				mRetailList.add(mJsonArray.getJSONObject(i).getString("MostRecentPrice"));
				mTargetList.add(mJsonArray.getJSONObject(i).getString("TargetLevel"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	*//**
	 * Method for parsing the Server response for getting sales
	 *//*
	public void getSalesList() 
	{
		try 
		{
			JSONObject mJsonObj = new JSONObject(mGetSalesListResponse);
			JSONArray mJsonArray;
			mTotalPages = (mJsonObj.getString("totalPages"));
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mInvoiceTypeIdList.add(mJsonArray.getJSONObject(i).getString("InvoiceTypeId"));
				mInvoiceIdList.add(mJsonArray.getJSONObject(i).getString("Id"));
				mPersonIdList.add(mJsonArray.getJSONObject(i).getString("PersonId"));
				
				Log.v("TAG", "Person Id  "+mJsonArray.getJSONObject(i).getString("PersonId"));
				
				mInvoiceNumberList.add(mJsonArray.getJSONObject(i).getString("InvoiceNumber"));
				mFirstNameList.add(mJsonArray.getJSONObject(i).getString("FirstName"));
				mLastNameList.add(mJsonArray.getJSONObject(i).getString("LastName"));
				mInvoiceDateList.add(mJsonArray.getJSONObject(i).getString("InvoiceDate"));
				mInvoiceTypeNameList.add(mJsonArray.getJSONObject(i).getString("InvoiceTypeName"));
				mInvoiceDiscountList.add(mJsonArray.getJSONObject(i).getString("Discount"));
				mTaxAfterDiscountList.add(mJsonArray.getJSONObject(i).getString("TaxAfterDiscount"));
				mTaxShippingList.add(mJsonArray.getJSONObject(i).getString("TaxShipping"));
				mIsDelieveredList.add(mJsonArray.getJSONObject(i).getString("IsDelivered"));
				mOutstandingList.add(mJsonArray.getJSONObject(i).getString("Outstanding"));
				mShippingList.add(mJsonArray.getJSONObject(i).getString("Shipping"));
				mTaxRateList.add(mJsonArray.getJSONObject(i).getString("TaxRate1"));
				mTaxableTotalList.add(mJsonArray.getJSONObject(i).getString("TaxableTotal1"));
				mTaxAmountList.add(mJsonArray.getJSONObject(i).getString("TaxAmount1"));
				mTotalAmountList.add(mJsonArray.getJSONObject(i).getString("Total"));
				mInvoiceStatusList.add(mJsonArray.getJSONObject(i).getString("InvoiceStatus"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	*//**
	 * Method for parsing the response of money expenses from server
	 *//*
	public void getMoneyExpenses() 
	{
		mExpensesAccountIdList = new ArrayList<String>();
		mExpensesReferenceList=new ArrayList<String>();
		mExpensesDescriptionList=new ArrayList<String>();
		mExpensesAmountList=new ArrayList<String>();
		mExpensesTransactionDateList=new ArrayList<String>();
		mExpensesAccountNameList=new ArrayList<String>();
		
		try 
		{
			JSONObject mJsonObj = new JSONObject(mMoneyExpensesResponse);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mExpensesAccountIdList.add(mJsonArray.getJSONObject(i).getString("AccountId"));
				mExpensesReferenceList.add(mJsonArray.getJSONObject(i).getString("Reference"));
				mExpensesDescriptionList.add(mJsonArray.getJSONObject(i).getString("Description"));
				mExpensesAmountList.add(mJsonArray.getJSONObject(i).getString("Amount"));
				mExpensesTransactionDateList.add(mJsonArray.getJSONObject(i).getString("TransactionDate"));
				mExpensesAccountNameList.add(mJsonArray.getJSONObject(i).getString("AccountName"));
				mExpensesJournalList.add(mJsonArray.getJSONObject(i).getString("JournalId"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	*//**
	 * Method for parsing the response of money mileage from server
	 *//*

	public void getMoneyMileage() 
	{
		mVehicleIdList=new ArrayList<String>();
		mVehicleTripIdList=new ArrayList<String>();
		mTripDescriptionList=new ArrayList<String>();
		mTripMilesList=new ArrayList<String>();
		mMileageDateList=new ArrayList<String>();
		mVehileDescriptionList=new ArrayList<String>();
		try 
		{
			JSONObject mJsonObj = new JSONObject(mMoneyMileageResponse);
			JSONArray mJsonArray;
			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mVehicleIdList.add(mJsonArray.getJSONObject(i).getString("VehicleId"));
				mVehicleTripIdList.add(mJsonArray.getJSONObject(i).getString("VehicleTripId"));
				mTripDescriptionList.add(mJsonArray.getJSONObject(i).getString("TripDescription"));
				mTripMilesList.add(mJsonArray.getJSONObject(i).getString("TripMiles"));
				mMileageDateList.add(mJsonArray.getJSONObject(i).getString("MileageDate"));
				mVehileDescriptionList.add(mJsonArray.getJSONObject(i).getString("VehicleDescription"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	*//**
	 * Method for parsing the response of money Income from server
	 *//*

	public void getMoneyIncome() 
	{
		mIncomeAccountIdList=new ArrayList<String>();
		mIncomeReferenceList=new ArrayList<String>();
		mIncomeDescriptionList=new ArrayList<String>();
		mIncomeAmountList=new ArrayList<String>();
		mIncomeTransactionDateList=new ArrayList<String>();
		mIncomeAccountNameList=new ArrayList<String>();
		try 
		{
			JSONObject mJsonObj = new JSONObject(mMoneyIncomeResponse);
			JSONArray mJsonArray;

			mJsonArray = mJsonObj.getJSONArray("data");
			for (int i=0; i<mJsonArray.length(); i++)
			{
				mIncomeAccountIdList.add(mJsonArray.getJSONObject(i).getString("AccountId"));
				mIncomeJournalList.add(mJsonArray.getJSONObject(i).getString("JournalId"));
				mIncomeReferenceList.add(mJsonArray.getJSONObject(i).getString("Reference"));
				mIncomeDescriptionList.add(mJsonArray.getJSONObject(i).getString("Description"));
				mIncomeAmountList.add(mJsonArray.getJSONObject(i).getString("Amount"));
				mIncomeTransactionDateList.add(mJsonArray.getJSONObject(i).getString("TransactionDate"));
				mIncomeAccountNameList.add(mJsonArray.getJSONObject(i).getString("AccountName"));

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	

	// Adapter for List of items

	public void setAdapterToListview(List<Items> listForAdapter) 
	{
		itemsSection.clear();

		if (null != listForAdapter && listForAdapter.size() != 0)
		{
			Collections.sort(listForAdapter);

			char checkChar = ' ';

			for (int index = 0; index < listForAdapter.size(); index++) 
			{
				Items objItem = (Items) listForAdapter.get(index);

				String mContactName = objItem.getName();

				String[] mContactNameSplittedStr = mContactName.split(" ", mContactName.length());
				if(mContactNameSplittedStr.length>1)
				{
					mContactName = mContactNameSplittedStr[1];
				}
				else if(mContactNameSplittedStr.length == 1)
				{
					mContactName = objItem.getName();
				}
				char firstChar = mContactName.charAt(0);
				if (' ' != checkChar) 
				{
					Log.v("response", "==checkChar=="+checkChar+"==firstChar=="+firstChar);
					if (checkChar != firstChar) 
					{
						ItemsSections objSectionItem = new ItemsSections();
						objSectionItem.setSectionLetter(firstChar);
						itemsSection.add(objSectionItem);
					}
				} 
				else
				{
					ItemsSections objSectionItem = new ItemsSections();
					objSectionItem.setSectionLetter(firstChar);
					itemsSection.add(objSectionItem);
				}
				checkChar = firstChar;
				itemsSection.add(objItem);
			}
		} 
		else 
		{
			showAlertView();
		}
		if (null == mNamesAdapterObj)
		{
			mNamesAdapterObj = new NamesAdapter(Menu.this, itemsSection);
			mContactsListView.setAdapter(mNamesAdapterObj);
		} 
		else
		{
			mNamesAdapterObj.notifyDataSetChanged();
		}
	}

	private void showAlertView()
	{
		if (null == mAlert)
			mAlert = new AlertDialog.Builder(this).create();
		if (mAlert.isShowing()) 
		{
			return;
		}
		mAlert.setTitle("Not Found!!!");
		mAlert.setMessage("Cannot find name Like '" + mSearchContactsStr + "'");
		mAlert.setButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		mAlert.show();
	}

	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) 
	{
		switch (arg0.getId()) 
		{
		case R.id.contacts_listview:
			
			Item item = itemsSection.get(position);

			if (view.getTag().getClass().getSimpleName().equals("ViewHolderName")) 
			{
				Items objItems = (Items) item;
				startActivity(new Intent(Menu.this, ContactsDetails.class).putExtra("personId", objItems.getId())
						.putExtra("personName", objItems.getName())
						.putExtra("personBirthDate", objItems.getBirthDate()));
				finish();
			}
			else 
			{
				ItemsSections objSectionsName = (ItemsSections) item;
			}
			break;
			
			
			
		case R.id.sales_listview:
			
			
			 startActivity(new Intent(Menu.this, SalesDetails.class)
			.putExtra("invoiceId", mInvoiceIdList.get(position))
			.putExtra("personId", mPersonIdList.get(position))
		
			.putExtra("invoiceTypeId", mInvoiceTypeIdList.get(position))
			.putExtra("firstName", mFirstNameList.get(position))
			.putExtra("lastName", mLastNameList.get(position))
			.putExtra("invoiceDate", mInvoiceDateList.get(position))
			.putExtra("invoiceNumber", mInvoiceNumberList.get(position))
			.putExtra("invoiceTypeName", mInvoiceTypeNameList.get(position))
			.putExtra("invoiceDiscount", mInvoiceDiscountList.get(position))
			.putExtra("taxAfterDiscount", mTaxAfterDiscountList.get(position))
			.putExtra("taxShipping", mTaxShippingList.get(position))
			.putExtra("markItemsDelivered", mIsDelieveredList.get(position))
			.putExtra("shipping", mShippingList.get(position))
			.putExtra("invoiceTaxRate", mTaxRateList.get(position))
			.putExtra("invoiceTaxableTotal", mTaxableTotalList.get(position))
			.putExtra("invoiceTaxAmount", mTaxAmountList.get(position))
			.putExtra("total", mTotalAmountList.get(position))
			.putExtra("invoiceStatus",mInvoiceStatusList.get(position)));
			 
				Log.v("TAG", "person id in Menu salesList"+mPersonIdList.get(position));
				
			finish();
			
			
			break;
		case R.id.favourites_list:
			startActivity(new Intent(Menu.this, InventoryProductDetails.class)
			.putExtra("productId", mProductIdList.get(position))
			.putExtra("OnHandCost",mOnHandValueList.get(position)));
			break;
			
			
		case R.id.money_expenses_lv:
			startActivity(new Intent(Menu.this, ExpensesDetails.class)
			.putExtra("JournalId", mExpensesJournalList.get(position))
			.putExtra("Account_Id", mExpensesAccountIdList.get(position))
			.putExtra("Reference", mExpensesReferenceList.get(position))
			.putExtra("Description", mExpensesDescriptionList.get(position))
			.putExtra("Amount", mExpensesAmountList.get(position))
			.putExtra("Transaction_Date", mExpensesTransactionDateList.get(position))
			.putExtra("Account_Name", mExpensesAccountNameList.get(position)));
			break;
		case R.id.money_mileage_lv:
			startActivity(new Intent(Menu.this, MileageDetails.class)
			.putExtra("Vehicle_Id", mVehicleIdList.get(position))
			.putExtra("Vehicle_Trip_Id", mVehicleTripIdList.get(position))
			.putExtra("Trip_Description", mTripDescriptionList.get(position))
			.putExtra("Trip_Miles", mTripMilesList.get(position))
			.putExtra("Mileage_Date", mMileageDateList.get(position))
			.putExtra("Vehicle_Description", mVehileDescriptionList.get(position)));
			break;
		case R.id.money_income_lv:
			startActivity(new Intent(Menu.this, IncomeDetails.class)
			.putExtra("Account_Id", mIncomeAccountIdList.get(position))
			.putExtra("JournalId", mIncomeJournalList.get(position))
			.putExtra("Reference", mIncomeReferenceList.get(position))
			.putExtra("Description", mIncomeDescriptionList.get(position))
			.putExtra("Amount", mIncomeAmountList.get(position))
			.putExtra("Transaction_Date", mIncomeTransactionDateList.get(position))
			.putExtra("Account_Name", mIncomeAccountNameList.get(position)));
			break;
			
			
			
		default:
			break;
		}
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		Log.v("response", "==on resume==");
		if(getSharedPreferences("SalesUpdate", 0).getBoolean("invoiceDeleted", false))
		{
			mInvoiceNumberList = new ArrayList<String>();
			mInvoiceDateList = new ArrayList<String>();
			mTotalAmountList = new ArrayList<String>();
			mFirstNameList = new ArrayList<String>();
			mLastNameList = new ArrayList<String>();
			mOutstandingList = new ArrayList<String>();
			new GetSalesListTask().execute();
			getSharedPreferences("SalesUpdate", 0).edit().putBoolean("invoiceDeleted", false).commit();
		}
		else if(getSharedPreferences("MoneyList", 0).getString("UpdatedList", "nothing").equalsIgnoreCase("Income"))
		{
			mCheckMoneyType = "Income";
			mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_second_selected);
			new GetMoneyIncomeTask().execute();
			getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "nothing").commit();
		}
		else if(getSharedPreferences("MoneyList", 0).getString("UpdatedList", "nothing").equalsIgnoreCase("Expenses"))
		{
			mCheckMoneyType = "Expenses";
			mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_first_selected);
			new GetMoneyExpensesTask().execute();
			getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "nothing").commit();
		}
		else if(getSharedPreferences("MoneyList", 0).getString("UpdatedList", "nothing").equalsIgnoreCase("Mileage"))
		{
			mCheckMoneyType = "Mileage";
			mMoneyTopBarRl.setBackgroundResource(R.drawable.top_grey_third_selected);
			new GetMoneyMileageTask().execute();
			getSharedPreferences("MoneyList", 0).edit().putString("UpdatedList", "nothing").commit();
		}
	}

	public void afterTextChanged(Editable s)
	{
		if(mTopText.getText().toString().equalsIgnoreCase("Contacts"))
		{
			filterArray.clear();
			mSearchContactsStr = mSearchContacts.getText().toString().trim()
					.replaceAll("\\s", "");

			if (mItems != null && mItems.size() > 0 && mSearchContactsStr.length() > 0)
			{
				for (Items name : mItems)
				{
					if (name.getName().toLowerCase()
							.startsWith(mSearchContactsStr.toLowerCase())) 
					{
						filterArray.add(name);
					}
				}
				setAdapterToListview(filterArray);
			} 
			else 
			{
				filterArray.clear();
				setAdapterToListview(mItems);
			}
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	{
		// TODO Auto-generated method stub
	}
	
	

	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		if(mTopText.getText().toString().equalsIgnoreCase("Inventory"))
		{
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mSearchInventory.getWindowToken(), 0);
			if(s.length() > 0)
			{
				mProductNameList = new ArrayList<String>();
				mOnHandQuantityList = new ArrayList<String>();
				mWholeSaleList = new ArrayList<String>();
				mSearchStr = s.toString();
				mInventorySearch = true;

				if(mAppUtils.isNetworkAvailable(Menu.this)) 
				{
					new InventoryTask().execute();
				}
				else 
				{
					mAppUtils.getAlertDialog(Menu.this, getResources().getString(R.string.alert_dialog_no_network));
				}
			}
		}
	}

	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK )
		{
			// Custom Dialog for exiting the application
			final Dialog dialog = new Dialog(Menu.this);
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
					startActivity(new Intent(Menu.this, LoginScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) 
	{
		mSelectItem=firstVisible;
		boolean loadMore = firstVisible + visibleCount >= totalCount;
		if(loadMore && isListLoaded) 
		{
			if(mTotalPages != null)
			{
				mTotalPagesTag = Integer.parseInt(mTotalPages);
				Log.v("response", "==mTotalPagesTag=="+mTotalPagesTag);
				if(mCurrentPage < mTotalPagesTag)
				{
					mCurrentPage+=1;
					isListLoaded = false;
					Log.v("response", "==load more==");
					new GetSalesListTask().execute();
				}
				else
				{
					Toast.makeText(Menu.this, "No data found", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public void onScrollStateChanged(AbsListView v, int s) { }   

	void layoutApp(boolean menuOut) {
		System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
				+ animParams.bottom + "]");
		mMainLayout.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
		//Now that we've set the app.layout property we can clear the animation, flicker avoided :)
		mMainLayout.clearAnimation();

	}

	
	
	
	
	
	
	@Override
	public void onAnimationEnd(Animation animation) {
		System.out.println("onAnimationEnd");
		//        ViewUtils.printView("menu", menu);
		//        ViewUtils.printView("app", app);
		Log.v("response", "==Animation End==");
		mMenuSlideOut = !mMenuSlideOut;
		if (!mMenuSlideOut) {
			mSideLayout.setVisibility(View.INVISIBLE);
		}
		layoutApp(mMenuSlideOut);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		System.out.println("onAnimationRepeat");
	}

	@Override
	public void onAnimationStart(Animation animation) {
		System.out.println("onAnimationStart");
	}

	static class AnimParams {
		int left, right, top, bottom;

		void init(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}




}*/