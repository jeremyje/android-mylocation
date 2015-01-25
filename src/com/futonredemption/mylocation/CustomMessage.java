package com.futonredemption.mylocation;

import android.content.Context;
import android.content.Intent;

public class CustomMessage implements ILocationWidgetInfo {

	private final Context _context;
	private final CharSequence _title;
	private final CharSequence _description;
	private final int _state;
	
	public CustomMessage(Context context, CharSequence title, CharSequence description) {
		_context = context;
		_title = title;
		_description = description;
		_state = Constants.WIDGETLAYOUTSTATE_Loading;
	}

	public CustomMessage(Context context, CharSequence title, CharSequence description, int state) {
		_context = context;
		_title = title;
		_description = description;
		_state = state;
	}
	
	public static CustomMessage create(final Context context, final CharSequence title, final CharSequence description) {
		final CustomMessage message = new CustomMessage(context, title, description);
		return message;
	}
	
	public static CustomMessage create(final Context context, final CharSequence title, final CharSequence description, final int state) {
		final CustomMessage message = new CustomMessage(context, title, description, state);
		return message;
	}
	
	public int getWidgetState() {
		return _state;
	}
	
	protected Context getContext() {
		return _context;
	}
	
	public Intent getActionIntent() {
		return Intents.actionCancel(_context);
	}
	
	public CharSequence getTitle() {
		return _title;
	}

	public CharSequence getDescription() {
		return _description;
	}

	public Intent getShareIntent() {
		return null;
	}

	public Intent getViewIntent() {
		return null;
	}
	
	public Intent getNotificationIntent() {
		return getViewIntent();
	}
}
