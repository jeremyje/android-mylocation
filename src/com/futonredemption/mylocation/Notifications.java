package com.futonredemption.mylocation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Notifications {
	
	public static void viewMap(final Context context, final ILocationWidgetInfo widgetInfo) {
		
		final Intent notifyIntent = widgetInfo.getNotificationIntent(); 
		if (notifyIntent != null) {
			final PendingIntent pIntent = PendingIntent.getActivity(context, 0, notifyIntent, Constants.PENDINGINTENT_FLAG);
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notifier = null;
			notifier = new Notification();
			notifier.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
			notifier.icon = R.drawable.ic_stat_findlocation;
			notifier.ledOffMS = Constants.INTERVAL_NotificationLed;
			notifier.ledOnMS = Constants.INTERVAL_NotificationLed;
			notifier.ledARGB = 0xff0000ff;

			notifier.tickerText = widgetInfo.getTitle() + " " + widgetInfo.getDescription();
			notifier.setLatestEventInfo(context, widgetInfo.getTitle(), widgetInfo.getDescription(), pIntent);
			nm.notify(Constants.NOTIFICATION_LocationUpdated, notifier);
		} else {
			cancelViewMap(context);
		}
	}
	
	public static void customMessage(final Context context, final CharSequence title, final CharSequence description, final CharSequence ticker_text)
	{
		final NotificationManager nm = getNotificationManager(context);
		final Notification notifier = new Notification();
		notifier.flags = Notification.FLAG_ONGOING_EVENT;
		notifier.icon = R.drawable.ic_stat_findlocation;
		if(ticker_text != null)
		{
			notifier.tickerText = ticker_text;
		}
		
		PendingIntent pintent = Intents.createPendingService(context, Intents.actionCancel(context));
		notifier.setLatestEventInfo(context, title, description, pintent);
		nm.notify(Constants.NOTIFICATION_CustomMessage, notifier);
	}
	
	public static void cancelViewMap(final Context context) {
		cancel(context, Constants.NOTIFICATION_LocationUpdated);
	}

	public static void cancelCustomMessage(final Context context) {
		cancel(context, Constants.NOTIFICATION_CustomMessage);
	}
	
	private static NotificationManager getNotificationManager(final Context context) {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	private static void cancel(final Context context, int id) {
		final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(id);
	}
}
