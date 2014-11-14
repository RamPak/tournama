package com.sabaware.TourNama;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
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


public class TourList extends SherlockActivity
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
	
	private List<Tour> tours=new ArrayList<Tour>();
	String[] tourItems;
	ArrayAdapter<String> adaptor; 
	ListView TourList;
	Resources res;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	static Handler handler;
	Context context = this;
	LoadTourList loadTourListThread;
	String CityID, On_Sale, GroupID, Filter, enable;
	
	Advertise advertise;
	Bitmap AdvBMP;
	//Bitmap rowBMP;
	//static int imageCounter;
	
    @Override 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourlist);
        
        tours.clear();
        tourItems = null;
        
        if(getIntent().getExtras().getString("Filter") != null)
        {
			enable = "1";
        	Filter = "&where=enable="+enable+" and Date>'" + "_TODAYDATE_" + "' and " + getIntent().getExtras().getString("Filter");
        }
        else
        {
            On_Sale = getIntent().getExtras().getString("OnSale");
            GroupID = getIntent().getExtras().getString("GroupID");
            CityID = getIntent().getExtras().getString("CityID");
			enable = "1";
            Filter = "&where=GroupID="+GroupID+" and DestinationID="+CityID+" and enable="+enable+" and Date>'" + "_TODAYDATE_" + "' and On_Sale="+On_Sale;
        }

        //if(getIntent().getExtras().getString("OtherCity") != null)
        //{
        //	if(getIntent().getExtras().getString("OtherCity").equals("true"))
        //		IsHome = false;
        //}
        
        //imageCounter = 0;
        	
    	setTitle("تورها");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.maintitle);
        ImageView titleImage = (ImageView)getSupportActionBar().getCustomView().findViewById(R.id.imageTitle);
        titleImage.setImageResource(R.drawable.titletourlist);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTheme(R.style.Theme_Sherlock_Light_ForceOverflow);

        res = getResources();
        
		face_BLotusBd = Typeface.createFromAsset(getAssets(), Setting.font_BLotusBd);
		face_dastnevis = Typeface.createFromAsset(getAssets(), Setting.font_dastnevis);
		
        TextView priceTitle = (TextView)findViewById(R.id.priceTitle);
        priceTitle.setTypeface(face_BLotusBd);
        priceTitle.setText(PersianReshape.reshape(this, "شروع قیمت(ریال)"));
        
        TextView tourTitle = (TextView)findViewById(R.id.tourTitle);
        tourTitle.setTypeface(face_BLotusBd);
        tourTitle.setText(PersianReshape.reshape(this, "مشخصات تور"));
        
        handler = new Handler();

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        getSupportActionBar().setBackgroundDrawable(bg);	
                          
		TourList = (ListView)findViewById(R.id.TourList);
		TourList.setCacheColorHint(Color.TRANSPARENT);
		TourList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) 
			{
    			Intent tourpage = new Intent(TourList.this, TourPage.class);
    			tourpage.putExtra("TourID", tours.get(position).ID+"");
    			startActivity(tourpage);
			}
		});
        
        loadTourListThread = new LoadTourList();
        loadTourListThread.start();
        
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
            );
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        TourList.setLayoutAnimation(controller);
    }
   
	class LoadTourList extends Thread   
	{
		public void run() 
		{
			handler.post(new loadNoNetworkOffRunnable());
			
			handler.post(new setListLoadingIconOnRunnable());
			
			try
			{
				if(!Setting.isNetworkAvailable(context))
					throw new Exception("NoNetwork!");
				
				String Url = Setting.WebService + "table=tbl_tour&order=Credit desc,ID" +
					"&select=ID,Name,StartPrice,Duration,(select logo from tbl_agency where tbl_agency.ID=AgencyID) AS logo" +
					Filter;		
				JSONArray jArray = Setting.connectWS(Url);

				tours.clear();
				Tour newTour;
				
				for (int i = 0; i < jArray.length(); i++)
				{
					JSONObject e = jArray.getJSONObject(i);
					String s = e.getString("post");
					JSONObject jObject = new JSONObject(s);

					newTour = new Tour();
					newTour.ID = Integer.parseInt(jObject.getString("ID"));
					newTour.Name = jObject.getString("Name");
					newTour.AgencyLogo = jObject.getString("logo");
					newTour.Duration = jObject.getString("Duration");
					newTour.StartPrice = jObject.getDouble("StartPrice");
					tours.add(newTour);
				}
				
				tourItems = new String[tours.size()];
				for(int i = 0; i< tours.size(); i++)
					tourItems[i] = tours.get(i).Name;
				
				LoadAdImage loadAdImageThread = new LoadAdImage();
				loadAdImageThread.start();
				
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
			TourList.setAdapter(adaptor);
		}
	}
   
   class LoadAdImage extends Thread   
	{
		public void run()
		{			
			try
			{
				String UrlAd = Setting.WebService + "table=tbl_advertise&select=image,link&order=rand() limit 1" +
						"&where=startdate<='" + "_TODAYDATE_" + "' and enddate>='" + "_TODAYDATE_" + "'";
				JSONArray jArrayAd = Setting.connectWS(UrlAd);
				
				if(jArrayAd.length() > 0)
				{				
					JSONObject e = jArrayAd.getJSONObject(0);
					String s = e.getString("post");
					JSONObject jObject = new JSONObject(s);
	
					advertise = new Advertise();
					advertise.Link = jObject.getString("link");
					advertise.Image = jObject.getString("image");
					
					String urlImage = Setting.Server + advertise.Image;
					URL ulrn = new URL(urlImage);
					HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
					InputStream is = con.getInputStream();
					AdvBMP = BitmapFactory.decodeStream(is);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				AdvBMP = null;
			}
			
			handler.post(new setAdvImage());
		}
	}; 
   
   public class setAdvImage implements Runnable 
   {
 		public void run() 
 		{
 			loadAdvImage();
 		}
   }
   
   private void loadAdvImage()
   {
		if(AdvBMP != null)
		{
			ImageView imageViewAdvertise = (ImageView)findViewById(R.id.imageViewAdvertise);
			imageViewAdvertise.setImageBitmap(AdvBMP);
			imageViewAdvertise.setVisibility(View.VISIBLE);
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
				Intent tourList = new Intent(TourList.this, TourList.class);
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
        	case MENU_Search:
       		
       		break;
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
   
   public void onClick(View v)
   {
	   switch (v.getId()) 
	   {
	   		case R.id.imageViewAdvertise:
	   		   Intent i = new Intent(Intent.ACTION_VIEW);
	   		   i.setData(Uri.parse(advertise.Link));
	   		   startActivity(i);
	   		   break;
	   }
   }
   
   private void setListLoadingIcon(boolean enable)
   {
  		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.TourListLayout);
  		
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
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.TourListLayout);
		
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
					loadTourListThread = null;
					loadTourListThread = new LoadTourList();
					loadTourListThread.start();
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
			super(TourList.this, R.layout.rowtourlist, R.id.tourName, tourItems);
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
	                convertView = mInflater.inflate(R.layout.rowtourlist, null);
	                holder.txtName = (TextView) convertView.findViewById(R.id.tourName);
	                holder.txtDuratrion = (TextView) convertView.findViewById(R.id.Duration);
	                holder.txtStartPrice = (TextView) convertView.findViewById(R.id.SatrtPrice);
	                //holder.img = (ImageView)convertView.findViewById(R.id.AgencyImage);
	                convertView.setTag(holder);
	            } 
	            else
	            {
	                holder = (ViewHolder) convertView.getTag();
	            }
	            //Fill EditText with the value you have in data source
	            holder.txtName.setTypeface(face_BLotusBd);
	            holder.txtName.setText(PersianReshape.reshape(TourList.this, tours.get(position).Name));
	            holder.txtName.setId(position);	
	            
	            holder.txtStartPrice.setTypeface(face_BLotusBd);
	            holder.txtStartPrice.setText(PersianReshape.reshape(TourList.this, Setting.setDecimalFormat(Double.parseDouble(Math.round(tours.get(position).StartPrice)+""))));
	            holder.txtStartPrice.setTextColor(Color.GRAY);
	            holder.txtStartPrice.setId(position);	
	            
	            holder.txtDuratrion.setTypeface(face_BLotusBd);
	            holder.txtDuratrion.setText(PersianReshape.reshape(TourList.this, tours.get(position).Duration));
	            holder.txtDuratrion.setTextColor(Color.GRAY);
	            holder.txtDuratrion.setId(position);	
	            
	            //tours.get(position).logoView = holder.img;
	            //LoadRowImage loadRowImageThread = new LoadRowImage();
	            //loadRowImageThread.start();
	            //holder.img.setId(position);	
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
   	   TextView txtName;
   	   TextView txtStartPrice;
   	   TextView txtDuratrion;
   	   ImageView img;
   }
   
   
/*	class LoadRowImage extends Thread   
	{
		public void run() 
		{			
			try
			{
				synchronized (this) 
				{
					if(imageCounter == tours.size()-1)
						imageCounter = 0;
					String urlImage = Setting.Server + tours.get(imageCounter++).logo;
					URL ulrn = new URL(urlImage);
					HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
					InputStream is = con.getInputStream();
					rowBMP = BitmapFactory.decodeStream(is);
					handler.post(new setRowImage());
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	};

  public class setRowImage implements Runnable 
  {
		public void run() 
		{
			if(imageCounter == tours.size()-1)
				imageCounter = 0;
			tours.get(imageCounter++).logoView.setImageBitmap(rowBMP);
		}
  }*/
}
