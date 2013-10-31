package com.flourish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;

public class ConnectionManager
{
	
	
	
	private static final int TIME_OUT_CONNECTION=5000;
	private static final int TIME_OUT_SOCKET=3000;
	private DefaultHttpClient mHttpClient = null;
	private HttpPost mHttpPost = null;
	private HttpGet mHttpGet = null;
	private HttpPut mHttpPut = null;
	private HttpDelete mHttpDelete = null;

	InputStream iStream;
	String jsonResponse;

	public String setUpHttpPost(String url, JSONObject string){
		Log.v("ConnectionManager", "URl : "+url+"   body  : "+string);
		return setUpHttpPost(url, string.toString());
	}
	public String setUpHttpPost(String url, String string)
	{
		String response = null;
		try 
		{

			Log.v("ConnectionManager", "URL: " + url);
			Log.v("ConnectionManager", "string ; " + string);
			/*StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy1); 
			
			
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = 
				        new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				}*/
		
			HttpParams httpParameters = new BasicHttpParams();
			Log.v("ConnectionManager", "httpParameters ; " + httpParameters);
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = TIME_OUT_CONNECTION;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = TIME_OUT_SOCKET;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// Log.v("TAG", "TIMedOUT 1");

			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(url);
			Log.v("ConnectionManager", "mHttpPost ; " + mHttpPost);
			mHttpPost.setHeader("Content-type", "application/json");

			mHttpPost.setEntity(new StringEntity(string, "UTF-8"));
			HttpResponse httpResponse = mHttpClient.execute(mHttpPost);
			Log.v("ConnectionManager", "httpResponse ; " + httpResponse);

			HttpEntity entity = httpResponse.getEntity();
			Log.v("ConnectionManager", "entity ; " + entity);
			iStream = entity.getContent();
			response = ConvertStreamString(iStream);
			
			

//			response = EntityUtils.toString(httpResponse.getEntity());
			Log.v("TAG", "URL: " + url);
			Log.v("TAG", "REQUEST: " + string);
			Log.v("TAG", "RESPONSE: " + response);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	public String setUpHttpPut(String url, String body)
	{
		String response = null;
		try 
		{
			mHttpClient = new DefaultHttpClient();
			mHttpPut = new HttpPut(url);

			mHttpPut.setHeader("Content-type", "application/json");

			mHttpPut.setEntity(new StringEntity(body, "UTF-8"));
			HttpResponse httpResponse = mHttpClient.execute(mHttpPut);

			response = EntityUtils.toString(httpResponse.getEntity());
			Log.v("flourish", "URL: " + url);
			Log.v("flourish", "REQUEST: " + body);
			Log.v("flourish", "RESPONSE: " + response);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return response;

	}

	public String setUpHttpGet(String uri) 
	{
		Log.v("flourish", "REQUEST URL: " + uri);
		try 
		{
			StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy1); 
			
			
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = 
				        new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				}
		
			
			
			
			
			
			
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = TIME_OUT_CONNECTION;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = TIME_OUT_SOCKET;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// Log.v("TAG", "TIMedOUT 1");

			mHttpClient = new DefaultHttpClient();
			mHttpGet = new HttpGet(uri);

			HttpResponse response = mHttpClient.execute(mHttpGet);
			HttpEntity entity = response.getEntity();

			iStream = entity.getContent();
			jsonResponse = ConvertStreamString(iStream);

			Log.v("flourish", "RESPONSE: " + jsonResponse);
			return jsonResponse;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String setUpHttpDelete(String uri) 
	{
		try 
		{
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = TIME_OUT_CONNECTION;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = TIME_OUT_SOCKET;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// Log.v("TAG", "TIMedOUT 1");

			mHttpClient = new DefaultHttpClient();
			mHttpDelete = new HttpDelete(uri);

			HttpResponse response = mHttpClient.execute(mHttpDelete);
			HttpEntity entity = response.getEntity();

			iStream = entity.getContent();
			jsonResponse = ConvertStreamString(iStream);

			Log.v("flourish", "REQUEST: " + uri);
			Log.v("flourish", "RESPONSE: " + jsonResponse);
			return jsonResponse;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String ConvertStreamString(InputStream is) 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		String line = null;
		try 
		{
			while ((line = reader.readLine()) != null) 
			{
				if (line!=null) {
					sb.append(line + "\n");
				}
				
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				is.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}