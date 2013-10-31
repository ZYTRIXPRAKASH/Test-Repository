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
import com.flourish.parsers.CategoriesGettersSetters;
import com.flourish.parsers.CategoriesXMLHandler;

public class SupportScreen extends BaseTimeOutActivity implements OnClickListener
{
	public List<CategoriesGettersSetters> mFlourishArticleList = new ArrayList<CategoriesGettersSetters>();
	private ListView mFlourishArticleListView = null;
	private Button mBack = null;
	private CategoriesGettersSetters data = null;

	FlourishBaseApplication mFlourishBaseApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.support_main);
		mFlourishBaseApplication=(FlourishBaseApplication)getApplicationContext();
		onSessionStarted(true);
		
		initializeVariables();
		
		new GetArticlesListTask().execute();	
		
		
	/*	if(isNetworkAvailable())
		{
			
		}
		else
		{
			showToast("No Network Connection!!!");
			this.finish();
		}*/

		mFlourishArticleListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
			{
				Toast.makeText(getApplicationContext(), "items"+data.getId(), Toast.LENGTH_LONG).show();

				Intent in=new Intent(SupportScreen.this, SupportFieldsActivity.class);
				String AllCat=mFlourishArticleList.get(pos).id;
				
				in.putExtra("category_id","/folders/"+AllCat);

				startActivity(in);
			}
		});
	}

	/**
	 * Method for initialize variables
	 */
	private void initializeVariables() 
	{
		mBack = (Button)findViewById(R.id.dashboard_back);
		mFlourishArticleListView = (ListView)findViewById(R.id.dashboard_list); 
		mFlourishArticleListView.setCacheColorHint(Color.TRANSPARENT);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.dashboard_back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 *Articles list AsyncTask
	 */
	public class GetArticlesListTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			FlourishProgressDialog.ShowProgressDialog(SupportScreen.this);
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
			FlourishProgressDialog.Dismiss(SupportScreen.this);
				if(mFlourishArticleList.size() > 0)
				{
					FlourishProgressDialog.Dismiss(SupportScreen.this);
					mFlourishArticleListView.setAdapter(new MyArticlesListAdapter());
				}
				else
				{
					FlourishProgressDialog.Dismiss(SupportScreen.this);
					Toast.makeText(SupportScreen.this, "No data found", Toast.LENGTH_LONG).show();
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

String mTaskUrl_get_flourish_articles_list_url=mFlourishBaseApplication.get_flourish_articles_list_url;
			URL url = new URL(mTaskUrl_get_flourish_articles_list_url);

			/** 
			 * Create the Handler to handle each of the XML tags. 
			 **/
			CategoriesXMLHandler myXMLHandler = new CategoriesXMLHandler();
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(url.openStream()));

			mFlourishArticleList = myXMLHandler.getsDetails();
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}

	private class MyArticlesListAdapter extends BaseAdapter 
	{
		public int getCount() {
			return mFlourishArticleList.size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View v = convertView;
			if (v == null) 
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.support_list_row, null);
			}
			TextView name = (TextView) v.findViewById(R.id.dashboard_list_item);
			data  = mFlourishArticleList.get(position);
			name.setText(data.getName());
			return v;
		}
	}


/**
 * Checking Network connections
 * @return
 */

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