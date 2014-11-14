package com.sabaware.TourNama;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

public class RandLogo  extends Activity
{
	Boolean AppCloesed = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.randlogo);
        
        Random randomGenerator = new Random();
        int randomNum = randomGenerator.nextInt(6);
        
        String mDrawableName = "rand" + randomNum;
        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        
        ImageView imageRand = (ImageView)findViewById(R.id.imageViewRand);
        imageRand.setImageResource(resID);

		timer.start();        
    }
    
	 CountDownTimer timer = new CountDownTimer(3000, 3000) 
	 {
	      @Override
	       public void onTick(long l) 
	       {}

	       @Override
	       public void onFinish() 
	       {
	    	   if(!AppCloesed)
	    	   {
	   				Intent main = new Intent(RandLogo.this, Main.class);
	   				startActivity(main);
	   				finish();
	    	   }
	       };
	 };
	 
		@Override
	    public void onDestroy()
		{
			super.onDestroy();
			
			AppCloesed = true;
		}
	 
	 
}
