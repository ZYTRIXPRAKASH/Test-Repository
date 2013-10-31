package com.flourish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.flourish.appnetwork.AppNetwork;
import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.contacts.Item;
import com.flourish.contacts.Items;
import com.flourish.contacts.ItemsSections;
import com.flourish.contacts.NamesAdapter;
import com.flourish.contacts.NamesParser;
import com.flourish.dialog.FlourishProgressDialog;

public class ContactsFragment extends Fragment  implements TextWatcher{

Fragment mFragment;
String mGetContactsResponse=null;
ConnectionManager mConnectionManager;
private List<Items> mItems = null;
private ArrayList<Item> itemsSection = new ArrayList<Item>();
public String mSearchContactsStr = null;
EditText mSearchContacts=null;
private NamesAdapter mNamesAdapterObj = null;
String mSessionId=null;
private AlertDialog mAlert = null;
private List<Items> filterArray = new ArrayList<Items>();
	
AppNetwork mAppNetwork;
ListView mContactsListView;
FlourishBaseApplication mFlourishBaseApplication;	

int customerNameSelected=0;//this is used to display the user data in different activities
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState); 
		
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mAppNetwork=new AppNetwork();
        mFlourishBaseApplication=(FlourishBaseApplication)getActivity().getApplicationContext();
        customerNameSelected=mFlourishBaseApplication.getmSelectedUserTag();
        
		mSessionId = getActivity().getSharedPreferences("LoginInfo", 0).getString("sessionId", "nothing");
		View mView;
		mView=inflater.inflate(R.layout.contacts_fragment, container, false);
		mContactsListView = (ListView)mView. findViewById(R.id.contacts_listview);
		if (mAppNetwork.isNetworkAvailable(getActivity().getApplicationContext())) 
		{
			new GetContactsAsyncTask().execute();
		}
		else 
		{
			mAppNetwork.getAlertDialog(getActivity(), getResources().getString(R.string.alert_dialog_no_network));
		}
		mSearchContacts = (EditText)mView.findViewById(R.id.input_search_query);
		
		mSearchContacts.setOnClickListener(buttonClickListener);
		mSearchContacts.addTextChangedListener(this);
		
		mContactsListView.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {Item item = itemsSection.get(position);
		Items objItems = (Items) item;
		
		
		if (customerNameSelected==1) {
			
			/*startActivity(new Intent(getActivity(), ContactsDetails.class).putExtra("personId", objItems.getId())
					.putExtra("personName", objItems.getName())
				    .putExtra("personBirthDate", objItems.getBirthDate()));
					
					Log.v("TAG", "Person Id "+objItems.getId());*/
					
				
			       Log.v("TAG", "Person Id in Contacts  "+objItems.getId());
					Intent i=new Intent();
					i.setAction((String)objItems.getId()+ ","+objItems.getName());
					getActivity().setResult(getActivity().RESULT_OK, i);
					getActivity().finish();
				
					//hide the keyboard after done
					InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
					
				
			
		}
		
		else{
			
			startActivity(new Intent(getActivity(), ContactsDetails.class).putExtra("personId", objItems.getId())
			.putExtra("personName", objItems.getName())
		    .putExtra("personBirthDate", objItems.getBirthDate()));
			Log.v("TAG", "Person Id "+objItems.getId());
			InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
			
			
			
			
		}
		
		
		
				
			}
		});
		
		
		return mView ;
		}

	
       View.OnClickListener buttonClickListener=new View.OnClickListener() {
	   public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_contact_button:
		break;
    	case R.id.create_invoice_button:
		break;
	    case R.id.add_expenses_button:
		break;
    	case R.id.add_mileage_button:
		break;
     	case R.id.need_help_button:
		break;
    	case R.id.input_search_query:
	    mSearchContacts.setFocusable(true);
		mSearchContacts.setFocusableInTouchMode(true);
		mSearchContacts.setClickable(true);
		break;
		default:
		break;
		}
	}
};
		

/**
 * Method for displaying the contacts AsyncTask
 */
private class GetContactsAsyncTask extends AsyncTask<Void, Void, Void>
{
	@Override
	protected void onPreExecute()
	{
		FlourishProgressDialog.ShowProgressDialog(getActivity());
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params)
	{   
		String mTaskUrl=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_contacts_url;
		mConnectionManager=new ConnectionManager();
		mGetContactsResponse = mConnectionManager.setUpHttpGet(mTaskUrl+mSessionId);
		Log.v("response", "==mGetContactsResponse=="+mGetContactsResponse);
		Log.v("response", "==mSessionId in contacts=="+mSessionId);
		mItems = new NamesParser().getContacts(mGetContactsResponse);
		return null;
	}
	@Override
	protected void onPostExecute(Void result) 
	{
		FlourishProgressDialog.Dismiss(getActivity());
			//
			
			if(mGetContactsResponse.contains("Login Key Not Valid"))
			{
				getActivity().getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(getActivity(), LoginScreen.class));
			}
			else if(mGetContactsResponse.contains("Bad Parameters"))
			{
				mAppNetwork.getAlertDialog(getActivity(), getString(R.string.alert_dialog_update_invoice_unsuccess));
			}
			else if (null == mItems || mItems.size() == 0)
			{
				getActivity().getSharedPreferences("DataChecking", 0).edit().putString("Data", "no_data").commit();
				startActivity(new Intent(getActivity(),LoginScreen.class));
				getActivity().finish();
			}
			else
			{
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mSearchContacts.getWindowToken(), 0);
				setAdapterToListview(mItems);
			}
		
		
		super.onPostExecute(result);
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
			char firstChar = 0;
			if(mContactName.length()>0){
			
				 firstChar = mContactName.charAt(0);
			}
			
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
		mNamesAdapterObj = new NamesAdapter(getActivity(), itemsSection);
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
		mAlert = new AlertDialog.Builder(getActivity()).create();
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


@SuppressLint("DefaultLocale")
public void afterTextChanged(Editable s)
{
		filterArray.clear();
		mSearchContactsStr = mSearchContacts.getText().toString().trim()
				.replaceAll("\\s", "");

		if (mItems != null && mItems.size() > 0 && mSearchContactsStr.length() > 0)
		{
			for (Items name : mItems)
			{
				if (name.getName().toLowerCase().startsWith(mSearchContactsStr.toLowerCase())) 
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

public void beforeTextChanged(CharSequence s, int start, int count, int after) 
{
	// TODO Auto-generated method stub
}



@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	
	
	
	
}


	
}
