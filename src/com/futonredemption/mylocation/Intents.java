package com.futonredemption.mylocation;

import org.beryl.app.IntentChooser;

import com.futonredemption.mylocation.services.WidgetUpdateService;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Intents
{
	public static final Intent actionRefresh(final Context context) {
		final Intent intent = new Intent(context, WidgetUpdateService.class);
		intent.putExtra(Constants.ACTION, Constants.ACTION_Refresh);
		return intent;
	}

	public static final Intent actionCancel(final Context context) {
		final Intent intent = new Intent(context, WidgetUpdateService.class);
		intent.putExtra(Constants.ACTION, Constants.ACTION_Cancel);
		return intent;
	}

	public static Intent createShareLocationIntent(final Context context, final Bundle locationInfo) {
		final Intent chooserIntent = new Intent(context, IntentChooser.class);
		chooserIntent.putExtra("test", locationInfo);
		return chooserIntent;
	}
	
	// FIXME: Hash codes are a hack here since it wastes pendingintents.
	public static PendingIntent createPendingActivity(final Context context, final Intent intent) {
		return PendingIntent.getActivity(context, intent.hashCode(), intent, Constants.PENDINGINTENT_FLAG);
	}
	
	public static PendingIntent createPendingService(final Context context, final Intent intent) {
		return PendingIntent.getService(context, intent.hashCode(), intent, Constants.PENDINGINTENT_FLAG);
	}
	
	public static Intent viewWebsite(final String url) {
		final Intent viewUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		viewUrl.addCategory(Intent.CATEGORY_DEFAULT);
		return viewUrl;
	}
	
	public static Intent createSend(final CharSequence subject, final CharSequence content) {
		
		final Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType(Constants.MIME_Email);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendIntent.putExtra(Intent.EXTRA_TEXT, content);
		return sendIntent;
	}
}
