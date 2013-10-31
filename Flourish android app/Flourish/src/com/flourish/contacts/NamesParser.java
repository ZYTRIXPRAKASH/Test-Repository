package com.flourish.contacts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NamesParser 
{
	Items objItem;
	List<Items> listArray;

	public List<Items> getContacts(String mResponse) 
	{
		try
		{
			listArray = new ArrayList<Items>();
			JSONObject jsonObject = new JSONObject(mResponse);
			JSONArray jsonArray = new JSONArray();
			jsonArray = jsonObject.getJSONArray("data");
			for(int i=0; i<jsonArray.length(); i++)
			{
				objItem = new Items();
				JSONObject e = jsonArray.getJSONObject(i);
				Log.v("response", "==id=="+e.getString("Id")); 
				objItem.setId(e.getString("Id"));
				objItem.setBirthDate(e.getString("BirthDate"));
				objItem.setName(e.getString("FirstName") + " " + e.getString("LastName"));
				
			
				
				listArray.add(objItem);
				
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return listArray;
	}
}