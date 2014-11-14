package com.sabaware.TourNama;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class CityList  extends SherlockActivity
{
	public static final int MENU_Search = Menu.FIRST;
	public static final int MENU_AdvanveSearch = Menu.FIRST+1;
	public static final int MENU_Setting = Menu.FIRST+2;
	public static final int MENU_Contact = Menu.FIRST+3;
	public static final int MENU_About = Menu.FIRST+4;
	
	Typeface face_BLotusBd, face_dastnevis;
	EditText txtSearch;
	View netView,loadView;
	
	MenuItem searchMenuItem;
	
	private List<City> cities=new ArrayList<City>();
	String[] cityItems;
	ArrayAdapter<String> adaptor; 
	ListView CityList;
	Resources res;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	static Handler handler;
	Context context = this;
	LoadCityList loadCityListThread;
	String GroupID, On_Sale, showinmenu;
	Boolean IsHome = true;
	
   @Override 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylist);
        
        cities.clear();
        cityItems = null;
        
        On_Sale = (getIntent().getExtras().getString("SpecialTour").equals("true") ? "1" : "0");
        GroupID = getIntent().getExtras().getString("GroupID");
        if(getIntent().getExtras().getString("OtherCity") != null)
        {
        	if(getIntent().getExtras().getString("OtherCity").equals("true"))
        		IsHome = false;
        }
        	
    	setTitle("شهرها");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.maintitle);
        ImageView titleImage = (ImageView)getSupportActionBar().getCustomView().findViewById(R.id.imageTitle);
        titleImage.setImageResource(R.drawable.titlecitylist);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTheme(R.style.Theme_Sherlock_Light_ForceOverflow);

        res = getResources();
        
        handler = new Handler();

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        getSupportActionBar().setBackgroundDrawable(bg);	
            
		face_BLotusBd = Typeface.createFromAsset(getAssets(), Setting.font_BLotusBd);
		face_dastnevis = Typeface.createFromAsset(getAssets(), Setting.font_dastnevis);
		
		CityList = (ListView)findViewById(R.id.CityList);
		CityList.setCacheColorHint(Color.TRANSPARENT);
		CityList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) 
			{
				if(cities.get(position).ID == -1) //Other Cities
				{
	    			Intent citylist = new Intent(CityList.this, CityList.class);
	    			citylist.putExtra("OtherCity", "true");
	    			citylist.putExtra("SpecialTour", (On_Sale.equals("1") ? "true" : "false"));
	    			citylist.putExtra("GroupID", GroupID);
	    			startActivity(citylist);
	    			finish();
				}
				else
				{
	    			Intent tourList = new Intent(CityList.this, TourList.class);
	    			tourList.putExtra("OnSale", On_Sale);
	    			tourList.putExtra("GroupID", GroupID);
	    			tourList.putExtra("CityID", cities.get(position).ID+"");
	    			startActivity(tourList);
				}
			}
		});
        
        loadCityListThread = new LoadCityList();
        loadCityListThread.start();
        
    }
   
	class LoadCityList extends Thread   
	{
		public void run() 
		{
			handler.post(new loadNoNetworkOffRunnable());
			
			handler.post(new setListLoadingIconOnRunnable());
			
			try
			{
				if(!Setting.isNetworkAvailable(context))
					throw new Exception("NoNetwork!");
				
				String enable = "1";
				showinmenu = (IsHome ? "1" : "0");
				String Url = Setting.WebService + "table=tbl_city&select=ID,Name&order=ID" +
					"&where=showinmenu="+showinmenu+" and ID IN (SELECT tbl_city.ID FROM tbl_city INNER JOIN tbl_tour ON(tbl_city.ID=tbl_tour.DestinationID) " + 
					"where enable="+enable+
					" and On_Sale="+On_Sale+
					" and GroupID="+GroupID+
					" and Date>'" + "_TODAYDATE_" + "')";
				JSONArray jArray = Setting.connectWS(Url);

				cities.clear();
				City newCity;
				
				for (int i = 0; i < jArray.length(); i++) 
				{
					JSONObject e = jArray.getJSONObject(i);
					String s = e.getString("post");
					JSONObject jObject = new JSONObject(s);

					newCity = new City();
					newCity.ID = Integer.parseInt(jObject.getString("ID"));
					newCity.Name = jObject.getString("Name");
					cities.add(newCity);
				}
				
				if(IsHome)
				{
					newCity = new City();
					newCity.ID = -1;
					newCity.Name = "دیگر";
					cities.add(newCity);
				}
				
				cityItems = new String[cities.size()];
				for(int i = 0; i< cities.size(); i++)
					cityItems[i] = cities.get(i).Name;
				
				handler.post(new setListLoadingIconOffRunnable());
				
				adaptor = new IconicAdapter();
				handler.post(new setAdapterRunnable());
				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				
				handler.post(new setListLoadingIconOffRunnable());
				handler.post(new loadNoNetworkOnRunnable());
			}
		}
	};
 
   public class setListLoadingIconOffRunnable implements Runnable {
		public void run() {
			setListLoadingIcon(false);
		}
	}
   
   public class setListLoadingIconOnRunnable implements Runnable {
		public void run() {
			setListLoadingIcon(true);
		}
	}
	
   public class loadNoNetworkOffRunnable implements Runnable {
		public void run() {
	    	loadNoNetwork(false);
		}
	}
   
   public class loadNoNetworkOnRunnable implements Runnable {
		public void run() {
	    	loadNoNetwork(true);
		}
	}
   
   public class setAdapterRunnable implements Runnable {
		public void run() {
			CityList.setAdapter(adaptor);
		}
	}
   
	@Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
  	
       menu.add(0, MENU_Search, 0, "جستجو")
       .setIcon(R.drawable.actionsearch)
       .setActionView(R.layout.collapsible_edittext)
       .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

       menu.add(0, MENU_AdvanveSearch, 0, "جستجوی پیشرفته")
       .setIcon(R.drawable.actionsettings2)
       .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
       
       searchMenuItem = menu.getItem(0);
	   txtSearch = (EditText)searchMenuItem.getActionView().findViewById(R.id.txtSearch);
	   txtSearch.setTypeface(face_BLotusBd);
	   txtSearch.setTextSize(25);
	   txtSearch.setHint(PersianReshape.reshape(this, "جستجو")); 
   	
   		ImageButton btnQuickSearch = (ImageButton)searchMenuItem.getActionView().findViewById(R.id.btnQuickSearch);
   		btnQuickSearch.setOnClickListener(new View.OnClickListener() 
   		{
			public void onClick(View v) 
			{
				doQuickSearch();
				txtSearch.setText("");
			}
		});
   	
	   searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() 
	   {
			
			public boolean onMenuItemActionExpand(MenuItem item)
			{
				Handler mHandler = new Handler();
				mHandler.post(new Runnable()
				{
				    public void run() 
				    {
						txtSearch.requestFocus();
						InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.showSoftInput(txtSearch, InputMethodManager.SHOW_FORCED);
				    }
				});

				//invalidateOptionsMenu();
				return true;
			}
			
			public boolean onMenuItemActionCollapse(MenuItem item) 
			{
				//Close Keyboard
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
				txtSearch.setText("");
				//invalidateOptionsMenu();
				return true;
			}
		});

   		txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() 
   		{
			public boolean onEditorAction(TextView v, int actionId,	KeyEvent event) 
			{
				
	   	        if (actionId == EditorInfo.IME_ACTION_SEARCH)
	   	        {
	   	        	doQuickSearch();    	        	
	   	        	v.setText("");
	   	            return true;
	   	        }
				return false;
			}
   		});
       
       return true;
   }
	
	public void doQuickSearch()
	{
		String searchText = txtSearch.getText().toString().trim();
		
    	if(!searchText.equals(""))
    	{
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);

			try
			{
				Intent tourList = new Intent(CityList.this, TourList.class);
				tourList.putExtra("Filter", "(Name like '" + URLEncoder.encode("%" + searchText + "%","UTF-8") +"' or " +      //Name
					"(select Name from tbl_city where tbl_city.ID=DestinationID) like '" + URLEncoder.encode("%" + searchText + "%","UTF-8") +"' or " +  //DestinationCity
					"(select Name from tbl_country where tbl_country.ID=(select CountryID from tbl_city where tbl_city.ID=DestinationID)) like '" + URLEncoder.encode("%" + searchText + "%","UTF-8") +"'" +       //DestinationCountry
					")");
				startActivity(tourList);
				searchMenuItem.collapseActionView();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
    	}
	}
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {   	
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
       switch(item.getItemId())
       {
/*        	case MENU_Search:
       		
       		break;*/
   		case MENU_AdvanveSearch:
   			mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
   			Intent advSearch = new Intent().setClass(this, AdvancedSearch.class);
   			startActivity(advSearch);
   			break;
   			
    	case android.R.id.home:
    		finish();
    		break;
       }

       return true;
   }
   
   private void setListLoadingIcon(boolean enable)
   {
  		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.CityListLayout);
  		
   	if(enable)
   	{
           //Loading Logo
           loadView = new LoadingView(this, R.drawable.loading);
           //loadView.bringToFront();
   		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
   		//		RelativeLayout.LayoutParams.MATCH_PARENT);
   		//params.addRule(RelativeLayout.CENTER_IN_PARENT);
   		//relativeLayout.addView(loadView,params);
   		relativeLayout.addView(loadView);
   	}
   	else
   	{
   		relativeLayout.removeView(loadView);
   		loadView = null;
   	}
   }
   
	private void loadNoNetwork(boolean enable) 
	{		
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.CityListLayout);
		
		if(enable)
		{
			LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			netView = mInflater.inflate(R.layout.nonetworkview, null);
			TextView txtError = (TextView)netView.findViewById(R.id.txtNoNetwork);
			txtError.setTypeface(face_dastnevis);
			txtError.setText(PersianReshape.reshape(this, "عدم دسترسی به اینترنت !"));
			Button BtnRetry =(Button)netView.findViewById(R.id.buttonRetry);
			BtnRetry.setTypeface(face_dastnevis);
			BtnRetry.setText(PersianReshape.reshape(this, "سعی مجدد"));
			BtnRetry.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					loadCityListThread = null;
					loadCityListThread = new LoadCityList();
					loadCityListThread.start();
				}
			});
			Button BtnWiFi =(Button)netView.findViewById(R.id.buttonWiFi);
			BtnWiFi.setTypeface(face_dastnevis);
			BtnWiFi.setText(PersianReshape.reshape(this, "روشن کردن شبکه بی سیم"));
			BtnWiFi.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				}
			});
			Button BtnData =(Button)netView.findViewById(R.id.buttonData);
			BtnData.setTypeface(face_dastnevis);
			BtnData.setText(PersianReshape.reshape(this, "روشن کردن شبکه موبایل"));
			BtnData.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					final  Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					 final ComponentName cn = new ComponentName("com.android.phone","com.android.phone.Settings");
					intent.setComponent(cn);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			relativeLayout.addView(netView,params);
		}
		else
		{
   		relativeLayout.removeView(netView);
   		netView = null;
		}
	}
   
	class IconicAdapter extends ArrayAdapter<String> 
	{
		private LayoutInflater mInflater;

		IconicAdapter() 
		{
			super(CityList.this, R.layout.rowcitylist, R.id.cityName, cityItems);
			mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}
		
		public View getView(int position, View convertView, ViewGroup parent)
		{
			try
			{
	            ViewHolder holder;
	            if (convertView == null) 
	            {
	                holder = new ViewHolder();
	                convertView = mInflater.inflate(R.layout.rowcitylist, null);
	                holder.txt = (TextView) convertView.findViewById(R.id.cityName);
	                convertView.setTag(holder);
	            } else 
	            {
	                holder = (ViewHolder) convertView.getTag();
	            }

	            holder.txt.setTypeface(face_BLotusBd);
	            holder.txt.setText(PersianReshape.reshape(CityList.this, cities.get(position).Name));
	            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2)
	            //	holder.txt.setGravity(Gravity.LEFT | Gravity.CENTER);
	            holder.txt.setId(position);	            
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
           return convertView;
		}
	}
   
   class ViewHolder 
   {
   	TextView txt;  	
   }
}
