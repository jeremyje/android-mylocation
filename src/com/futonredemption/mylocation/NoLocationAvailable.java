package com.futonredemption.mylocation;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class NoLocationAvailable implements ILocationWidgetInfo {

	private Context _context;
	public NoLocationAvailable(final Context context) {
		_context = context;
	}
	
	public Intent getActionIntent() {
		return Intents.actionRefresh(_context);
	}

	public CharSequence getDescription() {
		return _context.getText(R.string.tap_here_to_fix_that);
	}

	public Intent getShareIntent() {
		return getViewIntent();
	}

	public CharSequence getTitle() {
		return _context.getText(R.string.location_not_found);
	}

	public Intent getViewIntent() {
		return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	}

	public int getWidgetState() {
		return Constants.WIDGETLAYOUTSTATE_NotAvailable;
	}

	public Intent getNotificationIntent() {
		return getViewIntent();
	}
}
