package com.futonredemption.mylocation;

import java.util.ArrayList;
import java.util.Locale;

import org.beryl.app.ChoosableIntent;
import org.beryl.app.IntentChooser;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.Uri;

public class MyLocation implements ILocationWidgetInfo {

	private final Location _location;
	private final Context _context;
	private Address _address = null;

	public MyLocation(final Context context, final Location location) {
		_location = location;
		_context = context;
	}

	public double getLatitude() {
		return _location.getLatitude();
	}
	
	public double getLongitude() {
		return _location.getLongitude();
	}

	public CharSequence getTitle() {
		CharSequence title = null;
		
		if(_address != null) {
			if(_address.getMaxAddressLineIndex() > 0) {
				title = _address.getAddressLine(0);
			}
		}
		else {
			title = _context.getText(R.string.coordinates);
		}
		
		return title;
	}
	
	public CharSequence getDescription() {
		CharSequence description = null;
		
		if(_address != null) {
			if(_address.getMaxAddressLineIndex() > 1) {
				description = _address.getAddressLine(1);
			}
			else {
				description = _address.getFeatureName();
			}
		}
		else {
			description = getOneLineCoordinates();
		}
		
		return description;
	}
	
	public void attachAddress(Address address) {
		_address = address;
	}
	
	public Intent getActionIntent() {
		return Intents.actionRefresh(_context);
	}

	private Intent getShareCoordinatesIntent() {
		final Intent coordinateShare = Intents.createSend(_context.getText(R.string.my_location), getOneLineCoordinates());
		return Intent.createChooser(coordinateShare, _context.getText(R.string.coordinates));
	}

	private Intent getShareAddressIntent() {
		final Intent addressShare = Intents.createSend(_context.getText(R.string.my_location), getOneLineAddress());
		return Intent.createChooser(addressShare, _context.getText(R.string.address));
	}
	
	private Intent getShareMapsLinkIntent() {
		final Intent addressShare = Intents.createSend(_context.getText(R.string.my_location), getGoogleMapsUrl(getOneLineAddress()));
		return Intent.createChooser(addressShare, _context.getText(R.string.maps_link));
	}
	
	private Intent getShareAddressWithMapLinkIntent() {
		final String yourFriendIsHere = _context.getString(R.string.your_friend_is_here);
		final StringBuilder message = new StringBuilder();
		message.append(getOneLineAddress());
		message.append(Constants.NEWLINE);
		message.append(getGoogleMapsUrl(yourFriendIsHere));
		
		final Intent addressShare = Intents.createSend(_context.getText(R.string.my_location), message);
		return Intent.createChooser(addressShare, _context.getText(R.string.address_with_link));
	}
	
	public Intent getShareIntent() {
		final ArrayList<ChoosableIntent> intents = new ArrayList<ChoosableIntent>();
		if(_address != null) {
			intents.add(createChoosable(R.string.address, getShareAddressIntent()));
		}
		intents.add(createChoosable(R.string.coordinates, getShareCoordinatesIntent()));
		intents.add(createChoosable(R.string.maps_link, getShareMapsLinkIntent()));
		if(_address != null) {
			intents.add(createChoosable(R.string.address_with_link, getShareAddressWithMapLinkIntent()));
		}
		
		final CharSequence title = _context.getText(R.string.share_location);
		
		return IntentChooser.createChooserIntent(_context, title, intents);
	}

	private ChoosableIntent createChoosable(final int stringId, final Intent intent) {
		final CharSequence title = _context.getText(stringId);
		return new ChoosableIntent(title, intent);
	}
	
	private String getOneLineAddress() {

		final StringBuilder sb = new StringBuilder();
		sb.append(getTitle());
		sb.append(" ");
		sb.append(getDescription());
		return sb.toString();
	}
	
	private CharSequence getOneLineCoordinates() {
		return String.format(Locale.ENGLISH, "Lat: %s Long: %s", getLatitude(), getLongitude());
	}
	
	public Intent getViewIntent() {
		final ArrayList<ChoosableIntent> intents = new ArrayList<ChoosableIntent>();
		intents.add(createChoosable(R.string.nolocal_google_maps, Intents.viewWebsite(getGoogleMapsUrl(getOneLineAddress()))));
		
		final String googleUpdatesUrl = getGoogleUpdatesUrl();
		if(googleUpdatesUrl != null) {
			intents.add(createChoosable(R.string.nolocal_updates_from_google, Intents.viewWebsite(googleUpdatesUrl)));
		}
		
		intents.add(createChoosable(R.string.nolocal_flickr_photos, Intents.viewWebsite(getFlickrPhotosUrl())));
		intents.add(createChoosable(R.string.nolocal_panoramio_photos, Intents.viewWebsite(getPanoramioPhotosUrl())));
		intents.add(createChoosable(R.string.nolocal_info_from_geohack, Intents.viewWebsite(getGeoHackUrl())));

		final CharSequence title = _context.getText(R.string.view_location);
		return IntentChooser.createChooserIntent(_context, title, intents);
	}

	public int getWidgetState() {
		return Constants.WIDGETLAYOUTSTATE_Default;
	}

	private String getFlickrPhotosUrl() {
		return String.format(Locale.ENGLISH, Constants.URL_FlickrPhotos, getLatitude(), getLongitude());
	}
	private String getGoogleMapsUrl(String message) {
		return String.format(Locale.ENGLISH, Constants.URL_GmapsBase, getLatitude(), getLongitude(), Uri.encode(message));
	}

	
	private String getGeoHackUrl() {
		String pagename = "My_Location";
		
		if (_address != null) {
			pagename = getDescription().toString().replaceAll(" ", "_");
		}
		
		return String.format(Locale.ENGLISH, Constants.URL_GeoHack, Uri.encode(pagename), getLatitude(), getLongitude());
	}
	
	private String getPanoramioPhotosUrl() {
		return String.format(Locale.ENGLISH, Constants.URL_PanoramioPhotos, getLatitude(), getLongitude());
	}
	
	private String getLocationName() {
		String name = null;

		if(_address != null) {
			final String adminArea = _address.getAdminArea();
			final String locality = _address.getLocality();
			if(adminArea != null && locality != null) {
				name = String.format(Locale.ENGLISH, "%s %s", locality, adminArea);
			}
		}
		
		return name;
	}
	
	private String getGoogleUpdatesUrl() {
		String result = null;
		final String locationName = getLocationName();
		
		if(locationName != null) {
			result = String.format(Locale.ENGLISH, Constants.URL_GoogleUpdates, Utility.formatGoogleUrlParameter(locationName));
		}
		
		return result;
	}
	
	public Intent getNotificationIntent() {
		return getViewIntent();
	}
}
