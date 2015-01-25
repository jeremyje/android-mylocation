package com.futonredemption.mylocation;

import android.content.Context;
import android.content.Intent;

public class InitialStateMessage extends CustomMessage {

	public InitialStateMessage(Context context, CharSequence title, CharSequence description, int state) {
		super(context, title, description, state);
	}

	public static CustomMessage create(Context context) {
		final CharSequence title = context.getText(R.string.my_location);
		final CharSequence description = context.getText(R.string.tap_to_get_location);
		
		return new InitialStateMessage(context, title, description, Constants.WIDGETLAYOUTSTATE_NotAvailable);
	}
	
	@Override
	public Intent getActionIntent() {
		return Intents.actionRefresh(getContext());
	}

	@Override
	public Intent getShareIntent() {
		return Intents.actionRefresh(getContext());
	}

	@Override
	public Intent getViewIntent() {
		return Intents.actionRefresh(getContext());
	}
	
	@Override
	public Intent getNotificationIntent() {
		return null;
	}
}
