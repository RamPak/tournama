package com.sabaware.TourNama;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.sabaware.animation.ActivitySwitcher;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class Main extends SherlockActivity
{
	public static final int MENU_Search = Menu.FIRST;
	public static final int MENU_AdvanveSearch = Menu.FIRST+1;
	public static final int MENU_Setting = Menu.FIRST+2;
	public static final int MENU_Contact = Menu.FIRST+3;
	public static final int MENU_About = Menu.FIRST+4;
	
	Typeface face_BLotusBd,face_dastnevis;
	EditText txtSearch;
	View netView,loadView;
	Boolean IsHome = true;
	DatabaseHelper db;
	
	MenuItem searchMenuItem;
	
	private List<TourGroup> tourgroups=new ArrayList<TourGroup>();
	String[] groupItems;
	ArrayAdapter<String> adaptor; 
	GridView GroupGrid;
	Resources res;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds 
	static Handler handler;
	Context context = this;
	loadTourGroup loadgroupThread;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new DatabaseHelper(this);
        
        //Check Font Preference
  		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
  		if(prefs.getInt("ResharpMode", 0) == 0)
  		{
			Intent fontSetting = new Intent(Main.this, FontSetting.class);
			startActivity(fontSetting);  			
  			finish();
  		}
  		
  			
/*        //Title Display
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Custom View Display
        getSupportActionBar().setCustomView(R.layout.custom_view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(false);

        //Home Action Item Display
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);*/
        
        
        tourgroups.clear();
        groupItems = null;
        
		if(getIntent().getExtras() != null)
		{
			if(getIntent().getExtras().getString("SpecialTour").equals("true"))
				IsHome = false;
		}
		
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.maintitle);
        ImageView titleImage = (ImageView)getSupportActionBar().getCustomView().findViewById(R.id.imageTitle);
        
		if(!IsHome)
		{
	    	setTitle("تورهای ویژه");
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        LinearLayout footerLayout = (LinearLayout)findViewById(R.id.footer);
	        footerLayout.setVisibility(View.GONE);
	        titleImage.setImageResource(R.drawable.titlespecialtour);
		}
		else
		{
	    	setTitle("نوژا تور");
	        getSupportActionBar().setDisplayShowHomeEnabled(false);
	        android.view.ViewGroup.LayoutParams params = titleImage.getLayoutParams();
	        params.height = 45;
	        titleImage.setLayoutParams(params);
		}
		
        getSupportActionBar().setDisplayShowCustomEnabled(true);		
        setTheme(R.style.Theme_Sherlock_Light_ForceOverflow);
        res = getResources();
		
        handler = new Handler();
        
        checkLock.start();
      
        //This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047
        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
        //{
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);
        //}		
            
		face_BLotusBd = Typeface.createFromAsset(getAssets(), Setting.font_BLotusBd);
		face_dastnevis = Typeface.createFromAsset(getAssets(), Setting.font_dastnevis);
		//TextView lbl_main=(TextView)findViewById(R.id.lbl_main);
	    //lbl_main.setTypeface(face);
	    //String str_lbl = (String) lbl_main.getText().toString();
	    //lbl_main.setText(PersianReshape.reshape(str_lbl));
              
		Button btnAbout = (Button)findViewById(R.id.buttonAbout);
		btnAbout.setTypeface(face_dastnevis);
		btnAbout.setTextSize(20);
		btnAbout.setText(PersianReshape.reshape(this, "درباره"));
		
		Button btnContact = (Button)findViewById(R.id.buttonContact);
		btnContact.setTypeface(face_dastnevis);
		btnContact.setTextSize(20);
		btnContact.setText(PersianReshape.reshape(this, "تماس با ما"));
		
		GroupGrid = (GridView)findViewById(R.id.gridGroup);
		GroupGrid.setCacheColorHint(Color.TRANSPARENT);
		GroupGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) 
			{
				if(position == 0 && IsHome) //تور ویژه
				{
	    			Intent main = new Intent(Main.this, Main.class);
	    			main.putExtra("SpecialTour", "true");
	    			startActivity(main);
				}
				else
				{
	    			final Intent cityList = new Intent(Main.this, CityList.class);
	    			if(IsHome)
		    			cityList.putExtra("SpecialTour", "false");
	    			else
		    			cityList.putExtra("SpecialTour", "true");
	    			cityList.putExtra("GroupID", tourgroups.get(position).ID+"");
	    			//Add Animation
	    			cityList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    			ActivitySwitcher.animationOut(findViewById(R.id.mainLayout), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener()
	    			{
	    				public void onAnimationFinished() 
	    				{
	    					startActivity(cityList);
	    				}
	    			});
	    			//startActivity(cityList);
				}

			}
		});
        
		loadgroupThread = new loadTourGroup();
		loadgroupThread.start();
    }
    
	@Override 
	protected void onResume() 
	{
		ActivitySwitcher.animationIn(findViewById(R.id.mainLayout), getWindowManager());
		super.onResume();
	}
    
	class loadTourGroup extends Thread   
	{
		public void run() 
		{
			handler.post(new loadNoNetworkOffRunnable());
			
			handler.post(new setListLoadingIconOnRunnable());
			
			try
			{
				if(!Setting.isNetworkAvailable(context))
					throw new Exception("NoNetwork!");
				String Url = Setting.WebService + "table=tbl_tourgroup&select=ID,Name&order=ID";
				JSONArray jArray = Setting.connectWS(Url);

				tourgroups.clear();
				
				TourGroup newGroup = new TourGroup();
				if(IsHome)
				{
					newGroup.ID = -1;
					newGroup.Name = "تورهای ویژه";
					tourgroups.add(newGroup);
				}
				
				for (int i = 0; i < jArray.length(); i++) 
				{
					JSONObject e = jArray.getJSONObject(i);
					String s = e.getString("post");
					JSONObject jObject = new JSONObject(s);

					newGroup = new TourGroup();
					newGroup.ID = Integer.parseInt(jObject.getString("ID"));
					newGroup.Name = jObject.getString("Name");
					tourgroups.add(newGroup);
				}
							
				groupItems = new String[tourgroups.size()];
				for(int i = 0; i< tourgroups.size(); i++)
					groupItems[i] = tourgroups.get(i).Name;
							
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
			GroupGrid.setAdapter(adaptor);
		}
	}
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   	
        menu.add(0, MENU_Search, 0, "Search")
        .setIcon(R.drawable.actionsearch)
        .setActionView(R.layout.collapsible_edittext)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add(0, MENU_AdvanveSearch, 0, "Advanced Search")
        .setIcon(R.drawable.actionsettings2)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        menu.add(0, MENU_Setting, 0, "Font Setting")
        //.setIcon(R.drawable.actionsettings2)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        
/*        SubMenu sub = menu.addSubMenu(0 ,MENU_Setting ,0, "تنظیمات");
        sub.setIcon(R.drawable.ratingimportant);
        sub.add(0, MENU_Contact, 0, "تماس با ما");
        sub.add(0, MENU_About, 0, "درباره");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
               
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
				Intent tourList = new Intent(Main.this, TourList.class);
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
    			
    		case MENU_Setting:
    			Intent fontSetting = new Intent(Main.this, FontSetting.class);
    			startActivity(fontSetting);  			
      			finish();
    			break;
    			
/*    		case MENU_Contact:
           		mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
    			final View contact = getLayoutInflater().inflate(R.layout.contact, null);
    			
    			TextView txtTelTitle =(TextView)contact.findViewById(R.id.txtTelTitle);
    			txtTelTitle.setTypeface(face);
    			txtTelTitle.setText(PersianReshape.reshape(res.getString(R.string.txtTelTitle)));
    			
    			TextView txtTel =(TextView)contact.findViewById(R.id.txtTel);
    			txtTel.setTypeface(face);
    			txtTel.setText(PersianReshape.reshape(res.getString(R.string.txtTel)));
    			
    			TextView txtEmailTitle =(TextView)contact.findViewById(R.id.txtEmailTitle);
    			txtEmailTitle.setTypeface(face);
    			txtEmailTitle.setText(PersianReshape.reshape(res.getString(R.string.txtEmailTitle)));
    			
    			TextView txtEmail =(TextView)contact.findViewById(R.id.txtEmail);
    			txtEmail.setTypeface(face);
    			txtEmail.setText(PersianReshape.reshape(res.getString(R.string.txtEmail)));
    			
           		new AlertDialog.Builder(this)
    				.setTitle("تماس با ما")
    				.setView(contact)
    				.show();
           		break;
           		
           	case MENU_About:
           		mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
    			final View about = getLayoutInflater().inflate(R.layout.about, null);
    			
    			TextView txtSoftwareName =(TextView)about.findViewById(R.id.txtSoftwareName);
    			txtSoftwareName.setTypeface(face);
    			txtSoftwareName.setText(PersianReshape.reshape(res.getString(R.string.txtSoftwareName)));
    			
    			TextView txtVersion =(TextView)about.findViewById(R.id.txtVersion);
    			txtVersion.setTypeface(face);
    			txtVersion.setText(PersianReshape.reshape(res.getString(R.string.txtVersion)));
    			
    			TextView txtDevelopTitle =(TextView)about.findViewById(R.id.txtDevelopTitle);
    			txtDevelopTitle.setTypeface(face);
    			txtDevelopTitle.setText(PersianReshape.reshape(res.getString(R.string.txtDevelopTitle))); 
    			
    			TextView txtCompanyUrl =(TextView)about.findViewById(R.id.txtCompanyUrl);
    			txtCompanyUrl.setTypeface(face);
    			txtCompanyUrl.setText(PersianReshape.reshape(res.getString(R.string.txtCompanyUrl)));
    			   		
           		new AlertDialog.Builder(this)
    				.setTitle("درباره")
    				.setView(about)
    				.show();
           		break;*/
    			
        	case android.R.id.home:
        		//For example, consider a task consisting of the activities: 
        		//A, B, C, D. If D calls startActivity() with an Intent that resolves 
        		//to the component of activity B, then C and D will be finished and B receive
        		//the given Intent, resulting in the stack now being: A, B. 
/*    			Intent main = new Intent().setClass(this, Main.class);
    			main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(main);*/
        		finish();
        		break;
        }

        return true;
    }
    
    public void onClick(View v)
    {
    	InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	switch (v.getId())
    	{
    		case R.id.buttonAbout :
        		mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
				final View about = getLayoutInflater().inflate(R.layout.about, null); 
				
				TextView txtSoftwareName =(TextView)about.findViewById(R.id.txtSoftwareName);
				txtSoftwareName.setTypeface(face_dastnevis);
				txtSoftwareName.setText(PersianReshape.reshape(this, res.getString(R.string.txtSoftwareName)));
				
				try
				{
					TextView txtVersion =(TextView)about.findViewById(R.id.txtVersion);
					txtVersion.setTypeface(face_dastnevis);
					txtVersion.setText(PersianReshape.reshape(this, "ویرایش: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
				}catch(Exception ex){}
				
				TextView txtDevelopTitle =(TextView)about.findViewById(R.id.txtDevelopTitle);
				txtDevelopTitle.setTypeface(face_dastnevis);
				txtDevelopTitle.setText(PersianReshape.reshape(this, res.getString(R.string.txtDevelopTitle))); 
				
				TextView txtCompanyUrl =(TextView)about.findViewById(R.id.txtCompanyUrl);
				txtCompanyUrl.setTypeface(face_dastnevis);
				txtCompanyUrl.setText(PersianReshape.reshape(this, res.getString(R.string.txtCompanyUrl)));
				   		
        		new AlertDialog.Builder(this)
				.setTitle("About")
				.setView(about)
				.show();
    			break;
    			
    		case R.id.buttonContact :
        		mgr.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
				final View contact = getLayoutInflater().inflate(R.layout.contact, null);    
				
				TextView txtTelTitle =(TextView)contact.findViewById(R.id.txtTelTitle);
				txtTelTitle.setTypeface(face_dastnevis);
				txtTelTitle.setText(PersianReshape.reshape(this, res.getString(R.string.txtTelTitle)));
				
				TextView txtTel =(TextView)contact.findViewById(R.id.txtTel);
				txtTel.setTypeface(face_dastnevis);
				txtTel.setText(PersianReshape.reshape(this, res.getString(R.string.txtTel)));
				
				TextView txtEmailTitle =(TextView)contact.findViewById(R.id.txtEmailTitle);
				txtEmailTitle.setTypeface(face_dastnevis);
				txtEmailTitle.setText(PersianReshape.reshape(this, res.getString(R.string.txtEmailTitle)));
				
				TextView txtEmail =(TextView)contact.findViewById(R.id.txtEmail);
				txtEmail.setTypeface(face_dastnevis);
				txtEmail.setText(PersianReshape.reshape(this, res.getString(R.string.txtEmail)));
				   		
        		new AlertDialog.Builder(this)
				.setTitle("Contact Us")
				.setView(contact)
				.show();
        		break;
    	}
    }
    
    private void setListLoadingIcon(boolean enable)
    {
   		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
   		
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
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
		
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
					loadgroupThread = null;
					loadgroupThread = new loadTourGroup();
					loadgroupThread.start();
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
        
	Thread checkLock = new Thread(new Runnable() 
	{
		public void run() 
		{
	    	try
	    	{
	    		HttpClient client=new DefaultHttpClient();
	    		HttpGet getMethod=new HttpGet(
	    				"http://sabaware.com/ws/lockservice.php?system=tournama");
	    		ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    		String responseBody=client.execute(getMethod, responseHandler);
	    		if(responseBody.replace("\"", "").trim().equals("false"))
	    			finish();
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    	}
		}
	});
    
	class IconicAdapter extends ArrayAdapter<String> 
	{
		private LayoutInflater mInflater;

		IconicAdapter() 
		{
			super(Main.this, R.layout.rowtourgroup, R.id.groupName, groupItems);
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
	                convertView = mInflater.inflate(R.layout.rowtourgroup, null);
	                holder.txt = (TextView) convertView.findViewById(R.id.groupName);
	                holder.img = (ImageView) convertView.findViewById(R.id.imageViewGroup);
	                convertView.setTag(holder);
	            } else 
	            {
	                holder = (ViewHolder) convertView.getTag();
	            }

	            holder.txt.setTypeface(face_dastnevis);
	            holder.txt.setText(PersianReshape.reshape(Main.this, tourgroups.get(position).Name));
	            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2)
	            //	holder.txt.setGravity(Gravity.LEFT | Gravity.CENTER);
	            holder.txt.setId(position);	
	            String urlImage = "/data/data/com.sabaware.TourNama/picture/g" + tourgroups.get(position).ID + ".png";
			    InputStream is = new FileInputStream(urlImage);
			    Bitmap bmp = BitmapFactory.decodeStream(is);
			    holder.img.setImageBitmap(bmp);
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
    	ImageView img;
    }
    
}