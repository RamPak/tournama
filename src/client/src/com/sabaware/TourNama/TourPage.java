package com.sabaware.TourNama;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class TourPage  extends SherlockActivity
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
	
	Tour tour =new Tour();
	Resources res;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	static Handler handler;
	Context context = this;
	LoadTourPage loadTourPageThread;
	String TourID;
	
	SharedPreferences prefs;
	Boolean clickCredit = true;
	
	static Bitmap rowBMP;
	
    @Override 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourpage);
    	
        TourID = getIntent().getExtras().getString("TourID");
        	
    	setTitle("مشخصات تور");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.maintitle);
        ImageView titleImage = (ImageView)getSupportActionBar().getCustomView().findViewById(R.id.imageTitle);
        titleImage.setImageResource(R.drawable.titletourpage);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTheme(R.style.Theme_Sherlock_Light_ForceOverflow);

        res = getResources();
        
		face_BLotusBd = Typeface.createFromAsset(getAssets(), Setting.font_BLotusBd);
		face_dastnevis = Typeface.createFromAsset(getAssets(), Setting.font_dastnevis);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String date_ID = prefs.getString("date_"+TourID, "");
        if(!date_ID.equals(""))
        {
        	String date = date_ID.substring(0,date_ID.indexOf("_"));
        	if(date.equals(Setting.getPersianDate()))
        		clickCredit = false;
        }
        
        handler = new Handler();

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        getSupportActionBar().setBackgroundDrawable(bg);	
        
        loadTourPageThread = new LoadTourPage();
        loadTourPageThread.start();
    }
   
	class LoadTourPage extends Thread   
	{
		public void run() 
		{
			handler.post(new loadNoNetworkOffRunnable());
			
			handler.post(new setListLoadingIconOnRunnable());
			
			try
			{
				if(!Setting.isNetworkAvailable(context))
					throw new Exception("NoNetwork!");
				
				String Url = Setting.WebService + "table=tbl_tour" +
					"&select=ID,Name,StartPrice,Date,Description,Duration," +
					"(select logo from tbl_agency where tbl_tour.AgencyID = ID) AS AgencyLogo,"+
					"(select Name from tbl_agency where tbl_tour.AgencyID = ID) AS AgencyName,"+
					"(select Tel from tbl_agency where tbl_tour.AgencyID = ID) AS AgencyTel,"+
					"(select Address from tbl_agency where tbl_tour.AgencyID = ID) AS AgencyAddress,"+
					"(select Name from tbl_city where tbl_city.ID = SourceID) AS SourceCity,"+
					"(select Name from tbl_city where tbl_city.ID = DestinationID) AS DestinationCity,"+
					"(select Name from tbl_hotel where tbl_hotel.ID = HotelID) AS HotelName,"+
					"(select Name from tbl_country where tbl_country.ID = (select CountryID from tbl_city where tbl_city.ID = SourceID)) AS SourceCountry,"+
					"(select Name from tbl_country where tbl_country.ID = (select CountryID from tbl_city where tbl_city.ID = DestinationID)) AS DestinationCountry"+
					"&where=ID="+TourID;
				
				if(clickCredit)
					Url = Url + "&credit="+TourID;
				
				JSONArray jArray = Setting.connectWS(Url);
				
				JSONObject e = jArray.getJSONObject(0);
				String s = e.getString("post");
				JSONObject jObject = new JSONObject(s);

				tour.ID = Integer.parseInt(jObject.getString("ID"));
				tour.Name = jObject.getString("Name");
				tour.Duration = jObject.getString("Duration");
				tour.StartPrice = jObject.getDouble("StartPrice");
				tour.Date = jObject.getString("Date");
				tour.Description = jObject.getString("Description");
				tour.AgencyLogo = jObject.getString("AgencyLogo");
				tour.AgencyName = jObject.getString("AgencyName");
				tour.AgencyTel = jObject.getString("AgencyTel");
				tour.AgencyAddress = jObject.getString("AgencyAddress");
				tour.SourceCity = jObject.getString("SourceCity");
				tour.DestinationCity = jObject.getString("DestinationCity");
				tour.HotelName = jObject.getString("HotelName");
				tour.SourceCountry = jObject.getString("SourceCountry");
				tour.DestinationCountry = jObject.getString("DestinationCountry");				
				
				handler.post(new setListLoadingIconOffRunnable());
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
   
   public class setAdapterRunnable implements Runnable
   {
		public void run() 
		{
			fillLayout();
		}
	}
   
   public void fillLayout()
   {
	   prefs = PreferenceManager.getDefaultSharedPreferences(this);
       SharedPreferences.Editor editor = prefs.edit();
       editor.putString("date_"+TourID, Setting.getPersianDate() + "_" + tour.ID);
       editor.commit();
       
       //Load views
       
       TableLayout table = (TableLayout)findViewById(R.id.table);
       table.setBackgroundColor(Color.GRAY);
       
       View splitter1 = (View)findViewById(R.id.splitter1);
       splitter1.setBackgroundColor(Color.GRAY);
       View splitter2 = (View)findViewById(R.id.splitter2);
       splitter2.setBackgroundColor(Color.GRAY);
       View splitter3 = (View)findViewById(R.id.splitter3);
       splitter3.setBackgroundColor(Color.GRAY);
       View splitter4 = (View)findViewById(R.id.splitter4);
       splitter4.setBackgroundColor(Color.GRAY);
       View splitter5 = (View)findViewById(R.id.splitter5);
       splitter5.setBackgroundColor(Color.GRAY);
       View splitter6 = (View)findViewById(R.id.splitter6);
       splitter6.setBackgroundColor(Color.GRAY);
       View splitter7 = (View)findViewById(R.id.splitter7);
       splitter7.setBackgroundColor(Color.GRAY);
       View splitter8 = (View)findViewById(R.id.splitter8);
       splitter8.setBackgroundColor(Color.GRAY);
       View splitter9 = (View)findViewById(R.id.splitter9);
       splitter9.setBackgroundColor(Color.GRAY);
       View splitter10 = (View)findViewById(R.id.splitter10);
       splitter10.setBackgroundColor(Color.GRAY);
       View splitter11 = (View)findViewById(R.id.splitter11);
       splitter11.setBackgroundColor(Color.GRAY);
       
       TableRow tableRow0 = (TableRow)findViewById(R.id.tableRow0);
       tableRow0.setBackgroundColor(Color.WHITE);
       TableRow tableRow1 = (TableRow)findViewById(R.id.tableRow1);
       tableRow1.setBackgroundColor(Color.WHITE);
       TableRow tableRow2 = (TableRow)findViewById(R.id.tableRow2);
       tableRow2.setBackgroundColor(Color.WHITE);
       TableRow tableRow3 = (TableRow)findViewById(R.id.tableRow3);
       tableRow3.setBackgroundColor(Color.WHITE);
       TableRow tableRow4 = (TableRow)findViewById(R.id.tableRow4);
       tableRow4.setBackgroundColor(Color.WHITE);
       TableRow tableRow5 = (TableRow)findViewById(R.id.tableRow5);
       tableRow5.setBackgroundColor(Color.WHITE);
       TableRow tableRow6 = (TableRow)findViewById(R.id.tableRow6);
       tableRow6.setBackgroundColor(Color.WHITE);
       TableRow tableRow7 = (TableRow)findViewById(R.id.tableRow7);
       tableRow7.setBackgroundColor(Color.WHITE);
       TableRow tableRow8 = (TableRow)findViewById(R.id.tableRow8);
       tableRow8.setBackgroundColor(Color.WHITE);
       TableRow tableRow9 = (TableRow)findViewById(R.id.tableRow9);
       tableRow9.setBackgroundColor(Color.WHITE);
       TableRow tableRow10 = (TableRow)findViewById(R.id.tableRow10);
       tableRow10.setBackgroundColor(Color.WHITE);
       TableRow tableRow11 = (TableRow)findViewById(R.id.tableRow11);
       tableRow11.setBackgroundColor(Color.WHITE);
       
       TextView txtNameTitle = (TextView)findViewById(R.id.txtNameTitle);
       txtNameTitle.setTypeface(face_BLotusBd);
       txtNameTitle.setText(PersianReshape.reshape(this, "نام تور"));
       
       TextView txtNameValue = (TextView)findViewById(R.id.txtNameValue);
       txtNameValue.setTypeface(face_BLotusBd);
       txtNameValue.setText(PersianReshape.reshape(this, tour.Name));
       
       TextView txtDateTitle = (TextView)findViewById(R.id.txtDateTitle);
       txtDateTitle.setTypeface(face_BLotusBd);
       txtDateTitle.setText(PersianReshape.reshape(this, "تاریخ تور"));
       
       TextView txtDateValue = (TextView)findViewById(R.id.txtDateValue);
       txtDateValue.setTypeface(face_BLotusBd);
       txtDateValue.setText(PersianReshape.reshape(this, tour.Date));
       
       TextView txtDurationTitle = (TextView)findViewById(R.id.txtDurationTitle);
       txtDurationTitle.setTypeface(face_BLotusBd);
       txtDurationTitle.setText(PersianReshape.reshape(this, "مدت تور"));
       
       TextView txtDurationValue = (TextView)findViewById(R.id.txtDurationValue);
       txtDurationValue.setTypeface(face_BLotusBd);
       txtDurationValue.setText(PersianReshape.reshape(this, tour.Duration));
       
       TextView txtStartPriceTitle = (TextView)findViewById(R.id.txtStartPriceTitle);
       txtStartPriceTitle.setTypeface(face_BLotusBd);
       txtStartPriceTitle.setText(PersianReshape.reshape(this, "شروع قیمت"));
       
       TextView txtStartPriceValue = (TextView)findViewById(R.id.txtStartPriceValue);
       txtStartPriceValue.setTypeface(face_BLotusBd);
       txtStartPriceValue.setText(PersianReshape.reshape(this, Setting.setDecimalFormat(Double.parseDouble(Math.round(tour.StartPrice)+"")) + " (ریال)"));
       
       TextView txtSourceTitle = (TextView)findViewById(R.id.txtSourceTitle);
       txtSourceTitle.setTypeface(face_BLotusBd);
       txtSourceTitle.setText(PersianReshape.reshape(this, "مبدا"));
       
       TextView txtSourceValue = (TextView)findViewById(R.id.txtSourceValue);
       txtSourceValue.setTypeface(face_BLotusBd);
       txtSourceValue.setText(PersianReshape.reshape(this, tour.SourceCountry + " - " + tour.SourceCity));
       
       TextView txtDestinationTitle = (TextView)findViewById(R.id.txtDestinationTitle);
       txtDestinationTitle.setTypeface(face_BLotusBd);
       txtDestinationTitle.setText(PersianReshape.reshape(this, "مقصد"));
       
       TextView txtDestinationValue = (TextView)findViewById(R.id.txtDestinationValue);
       txtDestinationValue.setTypeface(face_BLotusBd);
       txtDestinationValue.setText(PersianReshape.reshape(this, tour.DestinationCountry + " - " + tour.DestinationCity));
       
       TextView txtHotelTitle = (TextView)findViewById(R.id.txtHotelTitle);
       txtHotelTitle.setTypeface(face_BLotusBd);
       txtHotelTitle.setText(PersianReshape.reshape(this, "نام هتل"));
       
       TextView txtHotelValue = (TextView)findViewById(R.id.txtHotelValue);
       txtHotelValue.setTypeface(face_BLotusBd);
       txtHotelValue.setText(PersianReshape.reshape(this, (tour.HotelName.equals("null") ? "" : tour.HotelName )));
       
       TextView txtAgencyNameTitle = (TextView)findViewById(R.id.txtAgencyNameTitle);
       txtAgencyNameTitle.setTypeface(face_BLotusBd);
       txtAgencyNameTitle.setText(PersianReshape.reshape(this, "نام آژانس"));
       
       TextView txtAgencyNameValue = (TextView)findViewById(R.id.txtAgencyNameValue);
       txtAgencyNameValue.setTypeface(face_BLotusBd);
       txtAgencyNameValue.setText(PersianReshape.reshape(this, tour.AgencyName));
       
       TextView txtAgencyTelTitle = (TextView)findViewById(R.id.txtAgencyTelTitle);
       txtAgencyTelTitle.setTypeface(face_BLotusBd);
       txtAgencyTelTitle.setText(PersianReshape.reshape(this, "تلفن"));
       
       TextView txtAgencyTelValue = (TextView)findViewById(R.id.txtAgencyTelValue);
       txtAgencyTelValue.setTypeface(face_BLotusBd);
       txtAgencyTelValue.setText(PersianReshape.reshape(this, tour.AgencyTel));

       TextView txtAgencyAddressTitle = (TextView)findViewById(R.id.txtAgencyAddressTitle);
       txtAgencyAddressTitle.setTypeface(face_BLotusBd);
       txtAgencyAddressTitle.setText(PersianReshape.reshape(this, "آدرس"));
       
       TextView txtAgencyAddressValue = (TextView)findViewById(R.id.txtAgencyAddressValue);
       txtAgencyAddressValue.setTypeface(face_BLotusBd);
       txtAgencyAddressValue.setText(PersianReshape.reshape(this, tour.AgencyAddress));
       
       TextView txtDescriptionTitle = (TextView)findViewById(R.id.txtDescriptionTitle);
       txtDescriptionTitle.setTypeface(face_BLotusBd);
       txtDescriptionTitle.setText(PersianReshape.reshape(this, "توضیحات"));
       
       TextView txtDescriptionValue = (TextView)findViewById(R.id.txtDescriptionValue);
       txtDescriptionValue.setTypeface(face_BLotusBd);
       txtDescriptionValue.setText(PersianReshape.reshape(this, tour.Description));
       
       LoadRowImage loadRowImageThread = new LoadRowImage();
       loadRowImageThread.start();
   }
   
   class LoadRowImage extends Thread   
	{
		public void run()
		{			
			try
			{
				if(tour.AgencyLogo.equals(""))
					throw new Exception();
				String urlImage = Setting.Server + tour.AgencyLogo;
				URL ulrn = new URL(urlImage);
				HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
				InputStream is = con.getInputStream();
				rowBMP = BitmapFactory.decodeStream(is);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				rowBMP = null;
			}
			
			handler.post(new setImage());
		}
	};

	public class setImage implements Runnable 
	{
		public void run() 
		{
			LoadIamge();
		}
	}
	
	public void LoadIamge()
	{
		if(rowBMP != null)
		{
			ImageView imgView = (ImageView)findViewById(R.id.AgencyLogo);
			imgView.setImageBitmap(rowBMP);
			imgView.setVisibility(View.VISIBLE);
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
				Intent tourList = new Intent(TourPage.this, TourList.class);
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
   
   private void setListLoadingIcon(boolean enable)
   {
  		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.TourPageLayout);
  		
   	if(enable)
   	{
        loadView = new LoadingView(this, R.drawable.loading);
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
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.TourPageLayout);
		
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
					loadTourPageThread = null;
					loadTourPageThread = new LoadTourPage();
					loadTourPageThread.start();
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
   
}
