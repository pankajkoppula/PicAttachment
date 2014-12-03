package org.jaaps.example.picattachment.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ConversionUtil {

	public static int convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    int dp = (int) (px / (metrics.densityDpi / 160f));
	    return dp;
	}
	
	public static int convertDpToPixels(int dp){
	    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}
	
}
