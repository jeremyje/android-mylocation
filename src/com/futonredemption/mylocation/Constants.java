package com.futonredemption.mylocation;

import android.app.PendingIntent;

public class Constants
{
	public static final int DISTANCE_ReallyReallyFarAway = 100000;
	
	public static final int INTERVAL_Timeout = 60000;
	public static final int INTERVAL_HalfSecond = 1000;
	public static final int INTERVAL_OneSecond = 1000;
	public static final int INTERVAL_NotificationLed = 5000;
	
	public static final float ACCURACY_PreferredFine = 150; // About .1 miles (in meters)
	public static final float ACCURACY_PreferredCoarse = 1500; // About a mile (in meters)
	
	public static final String URL_FlickrPhotos = "http://m.flickr.com/nearby/%f,%f?show=thumb&fromfilter=1&by=everyone&taken=alltime&sort=mostrecent";
	public static final String URL_GmapsBase = "http://maps.google.com/maps?q=%f,+%f+(%s)&iwloc=A&hl=en&z=13";
	public static final String URL_GeoHack = "http://toolserver.org/~geohack/geohack.php?pagename=%s&language=en&params=%f;%f";
	public static final String URL_PanoramioPhotos = "http://www.panoramio.com/map/#lt=%f&ln=%f&z=0&k=2";
	public static final String URL_GoogleUpdates = "http://www.google.com/m/search?q=%s&tbs=mbl:1";
	

	public static final String TEXT_UsingProviderPleaseWait = "Finding with %s...";
	
	public static final String MIME_Email = "text/plain";
	
	public static final String ACTION = "action";
	public static final String ACTION_Refresh = "refresh";
	public static final String ACTION_Cancel = "cancel";
	
	public static final int NOTIFICATION_LocationUpdated = 1;
	public static final int NOTIFICATION_CustomMessage = 2;
	public static final int NOTIFICATION_ShareLocation = 3;
	
	public static final int WIDGETLAYOUTSTATE_NotAvailable = 1;
	public static final int WIDGETLAYOUTSTATE_Loading = 2;
	public static final int WIDGETLAYOUTSTATE_Default = 3;
	
	public static final int PENDINGINTENT_FLAG = PendingIntent.FLAG_UPDATE_CURRENT;
	
	public static final String NEWLINE = "\n";
}
