package com.sabaware.TourNama;

import java.text.DecimalFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sabaware.util.DateFields;
import com.sabaware.util.PersianCalendar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Setting
{
	//public static String WebService = "http://192.168.1.101/TourNama/ws/webservice.php?";
	//public static String Server = "http://192.168.1.101/TourNama/";
	public static String WebService = "http://www.nojasystem.com/ws/webservice.php?";
	public static String Server = "http://www.nojasystem.com/";
	public static String font_BLotusBd = "font/BLotusBd.ttf"; //All
	public static String font_dastnevis = "font/danstevis.otf"; //Main About Contact NoNetwork
	
	public static boolean isNetworkAvailable(Context context)
	{
		try
		{
		    ConnectivityManager connectivityManager = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    if(activeNetworkInfo == null)
		    	return false;
		    return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	public static JSONArray connectWS(String url)
	{
		JSONArray jArray = null;
	    try 
	    {
	    	url = Setting.replace(url, " ", "%20");
	    	url = Setting.replace(url, ">", "_G_");
	    	url = Setting.replace(url, ">=", "_GE_");
	    	url = Setting.replace(url, "<", "_L_");
	    	url = Setting.replace(url, "<=", "_LE_");
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpget = new HttpGet(url); 
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
		    String responseBody = httpclient.execute(httpget,responseHandler);

	        if (!responseBody.equals(""))
	        {
				JSONObject json = new JSONObject(responseBody);
				jArray = json.getJSONArray("posts");
	        }
	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	    
        return jArray;
	}
	
	public static String replace(String text, String searchString, String replacementString)
	{
	    StringBuffer sBuffer = new StringBuffer();
	    int pos = 0;
	 
	    while ((pos = text.indexOf(searchString)) != -1)
	    {
	        sBuffer.append(text.substring(0, pos) + replacementString);
	        text = text.substring(pos + searchString.length());
	    }
	 
	    sBuffer.append(text);
	    return sBuffer.toString();
	}
	
	public static String setDecimalFormat(double number)
	{
		return new DecimalFormat("###,###,###,##0").format(number);
	}
	
	public static String getPersianDate()
	{
		PersianCalendar c = new PersianCalendar();
		DateFields df = c.getDateFields();
		return String.format("%04d/%02d/%02d", df.getYear(), df.getMonth()+1, df.getDay());
	}
}
