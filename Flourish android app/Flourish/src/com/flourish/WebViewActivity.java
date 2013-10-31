package com.flourish;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.flourish.utils.Constants;

public class WebViewActivity extends BaseTimeOutActivity 
{
	private WebView mWebView;
	private int mFieldId = 0;

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		onSessionStarted(true);
		mWebView = (WebView) findViewById(R.id.webView1);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mFieldId = Integer.parseInt(getIntent().getStringExtra("fieldId"));

		getWebPage(mFieldId);
	}

	private void getWebPage(int fieldId) 
	{
		switch (fieldId) 
		{
		case Constants.HOW_DO_I_GET_STARTED_WITH_FLOURISH:
			mWebView.loadUrl("file:///android_asset/how_do_i_get_started_with_flourish.html");
			break;

		case Constants.SETTING_UP_YOUR_CONTACTS:
			mWebView.loadUrl("file:///android_asset/setting_up_your_contacts.html");
			break;

		case Constants.WHAT_IS_FLOURISH:
			mWebView.loadUrl("file:///android_asset/what_is_flourish.html");
			break;

		case Constants.IMPORTING_PRODUCTS_AND_CONTACTS:
			mWebView.loadUrl("file:///android_asset/importing_products_and_contacts.html");
			break;

		case Constants.WHAT_IS_A_PRIVACY_POLICY:
			mWebView.loadUrl("file:///android_asset/what_is_your_privacy_policy.html");
			break;

		case Constants.METHODS_OF_PAYMENT:
			mWebView.loadUrl("file:///android_asset/methods_of_payment.html");
			break;

		case Constants.CANCELLATION_AND_REFUND_POLICY:
			mWebView.loadUrl("file:///android_asset/cancellation_refund_policy.html");
			break;

		case Constants.WHY_USE_FLOURISH:
			mWebView.loadUrl("file:///android_asset/why_use_flourish.html");
			break;

		case Constants.MY_SETTINGS_AND_MY_DEFAULT_TAX_RATE:
			mWebView.loadUrl("file:///android_asset/my_settings_and_my_default_tax_rate.html");
			break;

		case Constants.HOW_DO_I_RESTORE_AN_INACTIVE_ACCOUNT:
			mWebView.loadUrl("file:///android_asset/how_do_I_restore_an_inactive_account.html");
			break;

		case Constants.HOW_DO_I_CANCEL_MY_ACCOUNT:
			mWebView.loadUrl("file:///android_asset/how_do_I_cancel_my_account.html");
			break;

		case Constants.HOW_DO_I_CHANGE_MY_TITLE:
			mWebView.loadUrl("file:///android_asset/how_do_I_change_my_title.html");
			break;

		case Constants.REFERRAL_PROGRAMS_TERMS_AND_CONDITIONS:
			mWebView.loadUrl("file:///android_asset/referral_program_terms _conditions.html");
			break;

		case Constants.WHAT_DOES_QUICK_ADD_MENU_DO:
			mWebView.loadUrl("file:///android_asset/what_does_quick_add_menu_do.html");
			break;

		case Constants.WHAT_IS_THE_DASHBOARD:
			mWebView.loadUrl("file:///android_asset/what_is_the_dashboard.html");
			break;

		case Constants.HOW_DO_I_ADD_A_RECURRING_APPOINTMENT:
			mWebView.loadUrl("file:///android_asset/how_do_I_add_a_recurring_appointment.html");
			break;

		case Constants.WHAT_DOES_ARCHIVING_A_ACCOUNT_DO:
			mWebView.loadUrl("file:///android_asset/what_does_archiving_a_contact_do.html");
			break;

		case Constants.HOW_DO_I_CREATE_A_CONTACT:
			mWebView.loadUrl("file:///android_asset/how_do_I_create_a_contact.html");
			break;

		case Constants.HOW_DO_I_SET_MY_INITIAL_PRODUCT_INVENTORY:
			mWebView.loadUrl("file:///android_asset/how_do_I_set_my_initial_product_inventory.html");
			break;

		case Constants.HOW_DO_I_ADD_INVENTORY:
			mWebView.loadUrl("file:///android_asset/how_do_I_add_inventory.html");
			break;

		case Constants.HOW_DO_I_CREATE_AN_INVENTORY_ORDER:
			mWebView.loadUrl("file:///android_asset/how_do_I_create_an_inventory_order.html");
			break;

		case Constants.HOW_DO_I_BORROW_AN_IEM_FROM_ANOTHER_CONSULTANT:
			mWebView.loadUrl("file:///android_asset/how_do_I_borrow_an_item_from_another_consultant.html");
			break;

		case Constants.HOW_DO_I_LOAN_AN_ITEM_TO_ANOTHER_CONSULTANT:
			mWebView.loadUrl("file:///android_asset/how_do_I_loan_an_item_to_another_consultant.html");
			break;

		case Constants.HOW_DO_I_ADD_INCOME_NOT_FROM_SALES:
			mWebView.loadUrl("file:///android_asset/how_do_I_add_income_not_from_sales.html");
			break;

		case Constants.HOW_DO_I_CREATE_AN_INVOICE:
			mWebView.loadUrl("file:///android_asset/how_do_I_create_an_invoice.html");
			break;

		case Constants.HOW_TO_MARK_A_PRODUCT_DELIEVERED:
			mWebView.loadUrl("file:///android_asset/how_to_mark_a_product_delivered.html");
			break;

		case Constants.HOW_DO_I_MARK_AN_INVOICE_AS_PAID:
			mWebView.loadUrl("file:///android_asset/how_do_I_mark_an_invoice_as_paid.html");
			break;

		case Constants.HOW_DO_I_ADJUST_THE_SHIPPING_ON_AN_INVOICE:
			mWebView.loadUrl("file:///android_asset/how_do_I_adjust_the_shipping_on_an_Invoice.html");
			break;

		case Constants.HOW_DO_I_ADD_DISCOUNTS_TO_AN_INVOICE:
			mWebView.loadUrl("file:///android_asset/how_do_I_add_discounts_to_an_Invoice.html");
			break;

		case Constants.ADDING_EXPENSES:
			mWebView.loadUrl("file:///android_asset/adding_expenses.html");
			break;

		case Constants.HOW_DO_I_ADD_AN_EXPENSE_CATEGORY:
			mWebView.loadUrl("file:///android_asset/how_do_I_add_an_expense_category.html");
			break;

		default:
			break;
		}
	}
}