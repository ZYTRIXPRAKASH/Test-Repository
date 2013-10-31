package com.flourish;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CustomDialog extends Activity 
{
	private static final int PICK_CONTACT = 1;
	private Button mFromPhone = null;
	private Button mManually = null;
	private Button mCancel = null;
	String   cNumber;
	String name ;
	
	
	private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
	private static final Uri PURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private static final Uri EURI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	private static final Uri AURI = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
	private static final String ID = ContactsContract.Contacts._ID;
	private static final String DNAME = ContactsContract.Contacts.DISPLAY_NAME;
	private static final String HPN = ContactsContract.Contacts.HAS_PHONE_NUMBER;
	private static final String LOOKY = ContactsContract.Contacts.LOOKUP_KEY;
	private static final String CID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	private static final String EID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
	private static final String AID = ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID;
	private static final String PNUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private static final String PHONETYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
	
	private static final String EMAIL = ContactsContract.CommonDataKinds.Email.DATA;
	private static final String EMAILTYPE = ContactsContract.CommonDataKinds.Email.TYPE;
	private static final String STREET = ContactsContract.CommonDataKinds.StructuredPostal.STREET;
	private static final String CITY = ContactsContract.CommonDataKinds.StructuredPostal.CITY;
	private static final String STATE = ContactsContract.CommonDataKinds.StructuredPostal.REGION;
	private static final String POSTCODE = ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE;
	private static final String COUNTRY = ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY;
	
	
	private String id=null;
	private String lookupKey=null;

	private String street=null;
	private String city=null;
	private String state=null;
	private String postcode=null;
	private String country=null;
	
	private String firstName=null;
	private String lastName=null;
	private String given=null;
	private String family=null;
	private String display=null;
	private  String email=null;
	
	   
	
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_contacts_dialog);

		mFromPhone = (Button)findViewById(R.id.from_phone);
		mFromPhone.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent intent = new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, PICK_CONTACT);
				
			}
		}); 

		mManually = (Button)findViewById(R.id.manually);
		mManually.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{            	
				startActivity(new Intent(CustomDialog.this, AddContact.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			}
		}); 

		mCancel = (Button)findViewById(R.id.cancel);
		mCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{            	
				finish();
			}
		}); 
	}	

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case PICK_CONTACT:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
			//ContentResolver mContentResolver=getContentResolver();
			/*//	Cursor c =  mContentResolver.managedQuery(contactData, null, null, null, null);
				
				Cursor c=mContentResolver.query(contactData,null, null, null, null);
				if (c.moveToFirst()) {
					
					for(int i=0; i<c.getColumnCount();i++){
						Log.v("TAG", i+" getColumnName= "+c.getColumnName(i));
					}
					String mContactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				
					String mContactNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME));
					
					
					Log.v("TAG", "mContactName"+mContactName);
					
					Log.v("TAG", "mContactNumber"+mContactNumber);*/
				
			//	 Uri contactData = data.getData();
				
				
			 /*    Cursor c =  managedQuery(contactData, null, null, null, null);
			     
			     for(int i=0; i<c.getColumnCount();i++){
						Log.v("TAG", i+" getColumnName= "+c.getColumnName(i));
					}*/
			     
			     
			     //cursor for phone number
			     if (c.moveToFirst()) {
			         String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
			         String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			           if (hasPhone.equalsIgnoreCase("1")) {
			          Cursor phones = getContentResolver().query( 
			                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
			                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
			                       null, null);
			           phones.moveToFirst();
			         cNumber = phones.getString(phones.getColumnIndex("data1"));
			          
			           }
			           
			         //cursor for email
			            final String EMAIL = ContactsContract.CommonDataKinds.Email.DATA;
			            final Uri EURI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
			            final String EID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
			            Cursor emailCur = getContentResolver().query(EURI, null, EID + " = ?",  new String[]{id}, null); 
	     	        	while (emailCur.moveToNext()) { 
	     	    	    email =  emailCur.getString(emailCur.getColumnIndex(EMAIL));
	     	    		
	     	    		Log.v("TAG", "email=="+email);
	     	    			
	     	    	 	   
	     	    	 	 } 
		 	    	 	 emailCur.close();
			           
			           //cursor for address
		 	    	 	 
		 	    	 	 
		 	    		Cursor addCur = getContentResolver().query(AURI, null, AID + " = ?",  new String[]{id}, null); 
	     	    		while (addCur.moveToNext()) { 
	     	    		    street = addCur.getString(addCur.getColumnIndex(STREET));
	     	    		    city = addCur.getString(addCur.getColumnIndex(CITY));
	     	    		    state = addCur.getString(addCur.getColumnIndex(STATE));
	     	    		    postcode = addCur.getString(addCur.getColumnIndex(POSTCODE));
	     	    		    country = addCur.getString(addCur.getColumnIndex(COUNTRY));
	     	    		    street = addCur.getString(addCur.getColumnIndex(STREET));
	     	    		    
	     	    		    Log.v("TAG", "City=="+city);
	     	    		    
	     	    		   
	     	    	 	 } 
	     	    		 addCur.close();
		 	    	 	 
		 	    	 	 
			           
			        // Fetch contact name with a specific ID
			           String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + id; 
			           String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
			           Cursor nameCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
			           while (nameCur.moveToNext()) {
			        	  
			        	   firstName = nameCur.getString(nameCur.getColumnIndex(Data.DATA2));
			               lastName = nameCur.getString(nameCur.getColumnIndex(Data.DATA3));
			               given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
			               family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
			               display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
			             
			               //Toast.makeText(this, "Name: " + given + " Family: " +  family + " Displayname: "  + display, Toast.LENGTH_LONG).show();
			           
			               Log.v("TAG", "First name"+firstName+"\n lastname"+lastName); 
			           
			           
			           }
			           nameCur.close();
			        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

	            Log.v("TAG", "mContactName"+name);
					
					Log.v("TAG", "mContactNumber"+cNumber);
			     }
			
					
					getSharedPreferences("DisplayName", 0).edit().putString("ContactName", name).commit();
				
					startActivity(new Intent(CustomDialog.this, AddContact.class)
					.putExtra("First_Name", firstName)
					.putExtra("Last_Name", lastName)
					.putExtra("Email", email)
					.putExtra("Phone_Number", cNumber)
					.putExtra("Street", street)
					.putExtra("City", city)
					.putExtra("State", state)
					.putExtra("Zipcode",postcode )
					.putExtra("Country", country)
					
				
							);
					
					
					finish();
					
					
					
					
					
					
				}
			
	
			
			break;
			}
	
		
		
		
		
		}
	}
