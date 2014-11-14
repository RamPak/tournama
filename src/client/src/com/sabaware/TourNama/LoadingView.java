package com.sabaware.TourNama;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.view.View;

public class LoadingView extends View 
{
    private Movie mMovie;
    private long mMovieStart;
    
    public LoadingView(Context context, int GifLogoID) 
    {
        super(context);
        setFocusable(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        InputStream is = context.getResources().openRawResource(GifLogoID);
        mMovie = Movie.decodeStream(is);
    }
    
    @Override protected void onDraw(Canvas canvas) 
    {
    	if(mMovie != null)
    	{
            long now = android.os.SystemClock.uptimeMillis();
            if (mMovieStart == 0) 
            {   // first time
                mMovieStart = now;
            }
            if (mMovie != null) 
            {
                int dur = mMovie.duration();
                if (dur == 0) 
                    dur = 1000;
                int relTime = (int)((now - mMovieStart) % dur);
                mMovie.setTime(relTime);
                mMovie.draw(canvas, getWidth()/2 - mMovie.width()/2, getHeight()/2- mMovie.height()/2);
                invalidate();
            }      
    	}
    	else
    		super.onDraw(canvas);

        
    }
}
