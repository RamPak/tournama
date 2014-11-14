package com.sabaware.TourNama;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class AdvancedSearch extends SherlockActivity  
{
	Typeface face;
	
	EditText txtFromDateValueYear,
			 txtFromDateValueMonth,
			 txtFromDateValueDay,
			
			 txtToDateValueYear,
			 txtToDateValueMonth,
			 txtToDateValueDay,

			 txtStartPriceFromValue,
			 txtStartPriceToValue,

			 txtSourceValue,
			 txtDestinationValue,
			 txtHotelValue;
	
    @Override 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advancedsearch); 
    	setTitle("جستجوی پیشرفته");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.maintitle);
        ImageView titleImage = (ImageView)getSupportActionBar().getCustomView().findViewById(R.id.imageTitle);
        titleImage.setImageResource(R.drawable.titleadvancedsearch); 
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTheme(R.style.Theme_Sherlock_Light_ForceOverflow);
        
        //This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047
        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
        //{
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);
        //}
        
		face = Typeface.createFromAsset(getAssets(), Setting.font_BLotusBd);
		fillLayout();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch(item.getItemId())
        {
        	case android.R.id.home:
        		finish();
        		break;
        }


        return true;
    }
    
    public void onClick(View v)
    {
    	switch(v.getId())
    	{
    		case R.id.BtnAdvSearch :
	        	
				try
				{
					Intent tourList = new Intent(AdvancedSearch.this, TourList.class);
					tourList.putExtra("Filter", "(" +
						"(select Name from tbl_city where tbl_city.ID=DestinationID) like '" + URLEncoder.encode("%" + txtDestinationValue.getText().toString().trim() + "%","UTF-8") +"' or " +  															//DestinationCity
    					"(select Name from tbl_country where tbl_country.ID=(select CountryID from tbl_city where tbl_city.ID=DestinationID)) like '" + URLEncoder.encode("%" + txtDestinationValue.getText().toString().trim() + "%","UTF-8") +"'" +       //DestinationCountry
    					") and (" + 
    					"(select Name from tbl_city where tbl_city.ID=SourceID) like '" + URLEncoder.encode("%" + txtSourceValue.getText().toString().trim() + "%","UTF-8") +"' or " +  															//DestinationCity
    					"(select Name from tbl_country where tbl_country.ID=(select CountryID from tbl_city where tbl_city.ID=SourceID)) like '" + URLEncoder.encode("%" + txtSourceValue.getText().toString().trim() + "%","UTF-8") +"'" +       //DestinationCountry
    					") and " + 
    					"(select Name from tbl_hotel where tbl_hotel.ID=HotelID) like '" + URLEncoder.encode("%" + txtHotelValue.getText().toString().trim() + "%","UTF-8") +"' " +											//Hotel
    					
    					(txtStartPriceFromValue.getText().toString().trim().equals("") ? "" : " and StartPrice >=" + Setting.replace(txtStartPriceFromValue.getText().toString().trim(), ",", ""))  +
    					(txtStartPriceToValue.getText().toString().trim().equals("") ? "" : " and StartPrice <=" + Setting.replace(txtStartPriceToValue.getText().toString().trim(), ",", "")) +
    					
    					" and Date>='" + txtFromDateValueYear.getText().toString().trim()  + "/" + txtFromDateValueMonth.getText().toString().trim() + "/" + txtFromDateValueDay.getText().toString().trim() + "'" + 
    					" and Date<='" + txtToDateValueYear.getText().toString().trim()  + "/" + txtToDateValueMonth.getText().toString().trim() + "/" + txtToDateValueDay.getText().toString().trim() + "'"
    					);
					startActivity(tourList);
					finish();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
    			break;
    	}
    }
	
    public void fillLayout()
    {        
        //Load views
    	String todayDate = Setting.getPersianDate();
        
        TextView txtFromDateTitle = (TextView)findViewById(R.id.txtFromDateTitle);
        txtFromDateTitle.setTypeface(face);
        txtFromDateTitle.setText(PersianReshape.reshape(this, "از تاریخ"));
        
        txtFromDateValueYear = (EditText)findViewById(R.id.txtFromDateValueYear);
        txtFromDateValueYear.setTypeface(face);
        txtFromDateValueYear.setText(PersianReshape.reshape(this, todayDate.substring(0, 4)));
        
        txtFromDateValueMonth = (EditText)findViewById(R.id.txtFromDateValueMonth);
        txtFromDateValueMonth.setTypeface(face);
        txtFromDateValueMonth.setText(PersianReshape.reshape(this, todayDate.substring(5, 7)));
        
        txtFromDateValueDay = (EditText)findViewById(R.id.txtFromDateValueDay);
        txtFromDateValueDay.setTypeface(face);
        txtFromDateValueDay.setText(PersianReshape.reshape(this, todayDate.substring(8, 10)));
        
        TextView txtToDateTitle = (TextView)findViewById(R.id.txtToDateTitle);
        txtToDateTitle.setTypeface(face);
        txtToDateTitle.setText(PersianReshape.reshape(this, "تا تاریخ"));
        
        txtToDateValueYear = (EditText)findViewById(R.id.txtToDateValueYear);
        txtToDateValueYear.setTypeface(face);
        txtToDateValueYear.setText(PersianReshape.reshape(this, todayDate.substring(0, 4)));
        
        txtToDateValueMonth = (EditText)findViewById(R.id.txtToDateValueMonth);
        txtToDateValueMonth.setTypeface(face);
        txtToDateValueMonth.setText(PersianReshape.reshape(this, todayDate.substring(5, 7)));
        
        txtToDateValueDay = (EditText)findViewById(R.id.txtToDateValueDay);
        txtToDateValueDay.setTypeface(face);
        txtToDateValueDay.setText(PersianReshape.reshape(this, todayDate.substring(8, 10)));
        
        TextView txtStartPriceFromTitle = (TextView)findViewById(R.id.txtStartPriceFromTitle);
        txtStartPriceFromTitle.setTypeface(face);
        txtStartPriceFromTitle.setText(PersianReshape.reshape(this, "از قیمت"));
        
        TextView txtStartPriceFromRial = (TextView)findViewById(R.id.txtStartPriceFromRial);
        txtStartPriceFromRial.setTypeface(face);
        txtStartPriceFromRial.setText(PersianReshape.reshape(this, "ریال"));
        
        txtStartPriceFromValue = (EditText)findViewById(R.id.txtStartPriceFromValue);
        txtStartPriceFromValue.setTypeface(face);
        txtStartPriceFromValue.setText(PersianReshape.reshape(this, ""));
        txtStartPriceFromValue.addTextChangedListener( new TextWatcher() 
        {
            boolean isEdiging;
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void afterTextChanged(Editable s) 
            {
            	if(!s.toString().equals(""))
            	{
            		if(isEdiging) return;
                	isEdiging = true;

                	String str = s.toString().replaceAll( "[^\\d]", "" );
                	double s1 = Double.parseDouble(str);

                	NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                	((DecimalFormat)nf2).applyPattern("###,###");
                	s.replace(0, s.length(), nf2.format(s1));

                	isEdiging = false;
            	}
            }
        });
        
        TextView txtStartPriceToTitle = (TextView)findViewById(R.id.txtStartPriceToTitle);
        txtStartPriceToTitle.setTypeface(face);
        txtStartPriceToTitle.setText(PersianReshape.reshape(this, "تا قیمت"));
        
        TextView txtStartPriceToRial = (TextView)findViewById(R.id.txtStartPriceToRial);
        txtStartPriceToRial.setTypeface(face);
        txtStartPriceToRial.setText(PersianReshape.reshape(this, "ریال"));
        
        txtStartPriceToValue = (EditText)findViewById(R.id.txtStartPriceToValue);
        txtStartPriceToValue.setTypeface(face);
        txtStartPriceToValue.setText(PersianReshape.reshape(this, ""));
        txtStartPriceToValue.addTextChangedListener( new TextWatcher() 
        {
            boolean isEdiging;
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void afterTextChanged(Editable s) 
            {
            	if(!s.toString().equals(""))
            	{
            		if(isEdiging) return;
                	isEdiging = true;

                	String str = s.toString().replaceAll( "[^\\d]", "" );
                	double s1 = Double.parseDouble(str);

                	NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                	((DecimalFormat)nf2).applyPattern("###,###");
                	s.replace(0, s.length(), nf2.format(s1));

                	isEdiging = false;
            	}
            }
        });
        
        TextView txtSourceTitle = (TextView)findViewById(R.id.txtSourceTitle);
        txtSourceTitle.setTypeface(face);
        txtSourceTitle.setText(PersianReshape.reshape(this, "مبدا"));
        
        txtSourceValue = (EditText)findViewById(R.id.txtSourceValue);
        txtSourceValue.setTypeface(face);
        txtSourceValue.setText(PersianReshape.reshape(this, ""));
        
        TextView txtDestinationTitle = (TextView)findViewById(R.id.txtDestinationTitle);
        txtDestinationTitle.setTypeface(face);
        txtDestinationTitle.setText(PersianReshape.reshape(this, "مقصد"));
        
        txtDestinationValue = (EditText)findViewById(R.id.txtDestinationValue);
        txtDestinationValue.setTypeface(face);
        txtDestinationValue.setText(PersianReshape.reshape(this, ""));
        
        TextView txtHotelTitle = (TextView)findViewById(R.id.txtHotelTitle);
        txtHotelTitle.setTypeface(face);
        txtHotelTitle.setText(PersianReshape.reshape(this, "نام هتل"));
        
        txtHotelValue = (EditText)findViewById(R.id.txtHotelValue);
        txtHotelValue.setTypeface(face);
        txtHotelValue.setText(PersianReshape.reshape(this, ""));

        Button BtnAdvSearch = (Button)findViewById(R.id.BtnAdvSearch);
        BtnAdvSearch.setTypeface(face);
        BtnAdvSearch.setText(PersianReshape.reshape(this, "جستجو"));

    }
}
