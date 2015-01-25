package com.futonredemption.mylocation.services;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.beryl.location.LocationMonitor;
import org.beryl.location.PreferGpsLocationMonitorController;

import com.futonredemption.mylocation.Constants;
import com.futonredemption.mylocation.CustomMessage;
import com.futonredemption.mylocation.ILocationWidgetInfo;
import com.futonredemption.mylocation.MyLocation;
import com.futonredemption.mylocation.NoLocationAvailable;
import com.futonredemption.mylocation.Notifications;
import com.futonredemption.mylocation.R;
import com.futonredemption.mylocation.WidgetUpdater;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

public class WidgetUpdateService extends Service {
	public static final String TAG = "WidgetUpdateService";

	private final WidgetLocationListener _listener = new WidgetLocationListener();
	private Timer _watchdog = null;
	private LocationMonitor _monitor;

	private TimerTask _watchexec = new TimerTask() {
		@Override
		public void run() {
			final WidgetLocationListener listener = _listener;
			Location location = null;
			cancelNotification();

			if (listener != null) {
				location = listener.getLocation();
			}
			endService(location);
		}
	};

	public void cancelNotification() {
		Notifications.cancelCustomMessage(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setName(TAG);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		boolean lockacquired = false;

		final String action = intent.getStringExtra(Constants.ACTION);
		if (action.compareToIgnoreCase(Constants.ACTION_Refresh) == 0) {
			if (_monitor == null) {
				_monitor = new LocationMonitor(this);
				lockacquired = true;
			}

			if (lockacquired) {
				_watchdog = new Timer();
				_watchdog.schedule(_watchexec, Constants.INTERVAL_Timeout);
				showBeginFindingLocationNotification();
				getLocationInformation();
			}
		} else if (action.compareToIgnoreCase(Constants.ACTION_Cancel) == 0) {
			endService(null);
		} else {
			throw new RuntimeException("Clunched");
		}
	}

	private void showBeginFindingLocationNotification() {
		final CharSequence title = getText(R.string.finding_location);
		final CharSequence description = getText(R.string.tap_to_cancel);
		Notifications.customMessage(this, title, description, title);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void endService(Location location) {
		boolean allow_refresh = false;

		synchronized (this) {
			if (_watchdog != null) {
				_watchdog.purge();
				_watchdog.cancel();
				_watchdog = null;
				allow_refresh = true;
			}

			if (_monitor != null) {
				_monitor.stopListening();
			}
		}

		// Sometimes multiple location updates come through. Only accept the
		// first one which can be determined by the killing of the watchdog.
		if (allow_refresh) {
			
			if(location == null) {
				location = _monitor.getBestStaleLocation();
			}
			
			final MyLocation myLocation = convertLocation(location);
			
			if (myLocation == null) {
				WidgetUpdater.updateAll(this, new NoLocationAvailable(this));
			} else {
				final CharSequence title = this.getText(R.string.finding_location);
				final CharSequence description = this.getText(R.string.determining_address);
				final ILocationWidgetInfo widgetInfo = CustomMessage.create(this, title, description);
				WidgetUpdater.updateAll(this, widgetInfo);

				resolveAddress(myLocation, 3);
				WidgetUpdater.updateAll(this, myLocation);
			}
		}

		System.gc();
		_monitor = null;
		Notifications.cancelCustomMessage(this);
		this.stopSelf();
	}

	private void getLocationInformation() {
		String provider = null;
		
		provider = getBestProvider();
		
		if(provider != null && ! provider.equals("")) {
			displayInitialRetrieveMessage(provider);
			beginAcquireLocation();
		} else {
			onLocationAcquired(null);
		}
	}

	private void displayInitialRetrieveMessage(final String provider) {
		showUpdatingMessage(provider);
	}

	private void showUpdatingMessage(final String provider) {
		final CharSequence title = this.getText(R.string.finding_location);
		final CharSequence description = String.format(Locale.ENGLISH, Constants.TEXT_UsingProviderPleaseWait,
				provider);
		showCustomMessageOnWidget(title, description);
	}

	private void showCustomMessageOnWidget(final CharSequence title, final CharSequence description) {
		final ILocationWidgetInfo widgetInfo = CustomMessage.create(this, title, description);
		WidgetUpdater.updateAll(this, widgetInfo);
	}
	private String getBestProvider() {
		return _monitor.getBestEnabledProvider();
	}

	private void beginAcquireLocation() {
		setupListener();
		
		final PreferGpsLocationMonitorController preferGps = new PreferGpsLocationMonitorController(_monitor);
		preferGps.setLocationTimeAndDistanceIntervals(Constants.INTERVAL_HalfSecond, Constants.DISTANCE_ReallyReallyFarAway);
		preferGps.startMonitor();
	}
	
	private void setupListener() {
		if(_monitor.isGpsEnabled()) {
			_listener.setPreferredAccuracy(Constants.ACCURACY_PreferredFine);
		}
		else {
			_listener.setPreferredAccuracy(Constants.ACCURACY_PreferredCoarse);
		}
			
		_monitor.addListener(_listener);
	}

	public class WidgetLocationListener implements LocationListener {
		private float _preferredAccuracy;
		private Location _bestLocation = null;
		private final Object _lockMonitor = new Object();
		
		public WidgetLocationListener() {
		}

		public boolean hasLocation() {
			return _bestLocation != null;
		}

		public void setPreferredAccuracy(final float preferredAccuracy) {
			_preferredAccuracy = preferredAccuracy;
		}

		public void onLocationChanged(final Location location) {
			final float accuracy = location.getAccuracy();
			final float preferredAccuracy = _preferredAccuracy;
			boolean shouldStopListening = false;
			
			synchronized(_lockMonitor) {
				if(_bestLocation == null || accuracy <= _bestLocation.getAccuracy()) {
					_bestLocation = location;
					
					if(accuracy <= preferredAccuracy) {
						shouldStopListening = true;
					}
				}
			}
			
			if(shouldStopListening) {
				WidgetUpdateService.this.onLocationAcquired(getLocation());
			}
		}

		public Location getLocation() {
			return _bestLocation;
		}
		
		public void onProviderDisabled(String provider) {
			if(! _monitor.isAnyProviderEnabled()) {
				WidgetUpdateService.this.onLocationAcquired(getLocation());
			}
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public void resolveAddress(MyLocation location, int attempts) {
		
		final double latitude = location.getLatitude();
		final double longitude = location.getLongitude();

		Address address = null;
		
		// Limit 1 - 5 attempts
		if(attempts <= 0) {
			attempts = 1;
		}
		else if(attempts > 5) {
			attempts = 5;
		}
		
		while (attempts > 0 && address == null) {
			address = tryFindAddress(latitude, longitude);
			attempts--;
		}

		if (address != null) {
			location.attachAddress(address);
		}
	}
	
	public MyLocation convertLocation(final Location location) {
		if (location == null)
			return null;
		
		return new MyLocation(this, location);
	}
	
	private Address tryFindAddress(double latitude, double longitude) {
		final Geocoder coder = new Geocoder(this);
		Address address = null;
		try {
			
			final List<Address> addresses = coder.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) {
				address = addresses.get(0);
			}
		} catch (Exception e) {
		}
		
		return address;
	}

	public void onLocationAcquired(final Location location) {
		endService(location);
	}
}
