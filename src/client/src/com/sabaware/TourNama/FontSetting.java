package com.sabaware.TourNama;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FontSetting extends SherlockActivity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fontsetting);
        
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.maintitle);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        ImageView titleImage = (ImageView)getSupportActionBar().getCustomView().findViewById(R.id.imageTitle);
        android.view.ViewGroup.LayoutParams params = titleImage.getLayoutParams();
        params.height = 45;
        titleImage.setLayoutParams(params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        
        setTheme(R.style.Theme_Sherlock_Light_ForceOverflow);
        
        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        getSupportActionBar().setBackgroundDrawable(bg);
        
        Typeface face = Typeface.createFromAsset(getAssets(), Setting.font_BLotusBd);
        
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setTypeface(face);
        button1.setText("متن آزمایشی - پارسی 1391");
        
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setTypeface(face);
        button2.setText(PersianReshape.reshape_normal("متن آزمایشی - پارسی 1391"));
        
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setTypeface(face);
        button3.setText(new StringBuffer("متن آزمایشی - پارسی 1391").reverse().toString());
                     
        Button button4 = (Button)findViewById(R.id.button4); 
        button4.setTypeface(face);
        button4.setText(PersianReshape.reshape_reverse("متن آزمایشی - پارسی 1391"));
        
        Button button5 = (Button)findViewById(R.id.button5);
        button5.setTypeface(face);
        button5.setText(PersianReshape.reshape_reverse(new StringBuffer("متن آزمایشی - پارسی 1391").reverse().toString()));
        
    }
    
    public void onClick(View v)
    {
    	int ResharpMode = 0;
    	switch(v.getId())
    	{
    		case R.id.button1:
    			ResharpMode = 1;
    			break;
    		case R.id.button2:
    			ResharpMode = 2;
    			break;
    		case R.id.button3:
    			ResharpMode = 3;
    			break;
    		case R.id.button4:
    			ResharpMode = 4;
    			break;
    		case R.id.button5:
    			ResharpMode = 5;
    			break;
    	}
    	
 	   	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
 	   	SharedPreferences.Editor editor = prefs.edit();
 	   	editor.putInt("ResharpMode", ResharpMode);
 	   	editor.commit();
    	
		Intent main = new Intent(FontSetting.this, Main.class);
		startActivity(main);  			
    	finish();
    }

    
}
