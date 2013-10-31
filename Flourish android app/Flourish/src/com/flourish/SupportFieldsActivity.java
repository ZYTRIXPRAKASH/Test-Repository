package com.flourish;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flourish.baseapplication.FlourishBaseApplication;
import com.flourish.dialog.FlourishProgressDialog;
import com.flourish.parsers.FieldsGetterSetters;
import com.flourish.parsers.FieldsXMLHandler;

public class SupportFieldsActivity extends BaseTimeOutActivity implements OnClickListener
{
	private Button mBack = null;
	private ListView mFieldsListView = null;
	public List<FieldsGetterSetters> mFieldsList = new ArrayList<FieldsGetterSetters>();
	private ArrayList<String> mFieldIdsList = new ArrayList<String>();
	private String mCategoryId = null;

	private FieldsGetterSetters data = null;
	FlourishBaseApplication mFlourishBaseApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.support_fields);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		onSessionStarted(true);
		initializeVariables();
		if(isNetworkAvailable())
		{
			new GetFieldsListTask().execute();
		}
		else{

			showToast("No Network Connection!!!");
			this.finish();
		}
		mFieldsListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				startActivity(new Intent(SupportFieldsActivity.this, WebViewActivity.class).putExtra("fieldId", mFieldIdsList.get(position)));
			}
		});
	}

	private void initializeVariables() 
	{
		mBack = (Button)findViewById(R.id.dashboard_fields_back);
		mCategoryId = getIntent().getStringExtra("category_id");
		mFieldsListView = (ListView)findViewById(R.id.dashboard_fields_list); 
		mFieldsListView.setCacheColorHint(Color.TRANSPARENT);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.dashboard_fields_back:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * Method for getting the dashboard details AsyncTask
	 */
	public class GetFieldsListTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(SupportFieldsActivity.this);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			getData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			FlourishProgressDialog.Dismiss(SupportFieldsActivity.this);
				
				
				if(mFieldsList.size() > 0)
				{
					FlourishProgressDialog.Dismiss(SupportFieldsActivity.this);
					mFieldsListView.setAdapter(new MyFieldsListAdapter());
				}
				else
				{
					FlourishProgressDialog.Dismiss(SupportFieldsActivity.this);
					Toast.makeText(SupportFieldsActivity.this, "No data found", Toast.LENGTH_LONG).show();
				}
			
			super.onPostExecute(result);
		}
	}

	public void getData() 
	{
		try 
		{
			/**
			 * Create a new instance of the SAX parser
			 **/
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP 		   = saxPF.newSAXParser();
			XMLReader xmlR         = saxP.getXMLReader();
			
			String mTaskUr_get_flourish_articles_list_urll=mFlourishBaseApplication.mFlurishBaseUrl+mFlourishBaseApplication.get_flourish_articles_list_url;

			URL url = new URL(mTaskUr_get_flourish_articles_list_urll+mCategoryId+".xml");

			Log.v("TAG","===all categories=="+url);

			/** 
			 * Create the Handler to handle each of the XML tags. 
			 **/
			FieldsXMLHandler mFieldsHandler = new FieldsXMLHandler();
			xmlR.setContentHandler(mFieldsHandler);
			xmlR.parse(new InputSource(url.openStream()));

			mFieldsList = mFieldsHandler.getCategories();

		} catch (Exception e) {
			System.out.println(e);

			Log.v("TAG", "exeption  =--"+e.toString());
		}}

	private class MyFieldsListAdapter extends BaseAdapter 
	{

		public int getCount() {
			return mFieldsList.size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.support_fields_list_row, null);
			}

			TextView category_name = (TextView) v.findViewById(R.id.dashboard_fields_list_item);
			data  = mFieldsList.get(position);
			category_name.setText(data.getCatname());
			mFieldIdsList.add(data.getCatid());
			Log.v("response", "==data.getCatid()=="+data.getCatid());
			Log.v("response", "==data.getCatname()=="+data.getCatname());
			Log.v("response", "==mFieldIdsList=="+mFieldIdsList);

			return v;
		}
	}


	public boolean isNetworkAvailable()
	{
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		else 
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) 
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	private void showToast(String msg) 
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

}