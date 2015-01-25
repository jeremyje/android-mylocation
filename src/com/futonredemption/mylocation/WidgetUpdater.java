package com.futonredemption.mylocation;

import com.futonredemption.mylocation.appwidgets.AppWidgetProvider1x1;
import com.futonredemption.mylocation.appwidgets.AppWidgetProvider2x1;
import com.futonredemption.mylocation.appwidgets.AppWidgetProvider4x1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetUpdater {

	public static void updateAll(final Context context, final ILocationWidgetInfo widgetInfo) {
		int i;
		final AppWidgetManager widget_manager = AppWidgetManager.getInstance(context);
		final int[] ids4x1 = widget_manager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider4x1.class));

		final int len4x1 = ids4x1.length;
		for (i = 0; i < len4x1; i++) {
			AppWidgetProvider4x1.UpdateWidget(context, widget_manager, ids4x1[i], widgetInfo);
		}

		final int[] ids2x1 = widget_manager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider2x1.class));

		final int len2x1 = ids2x1.length;
		for (i = 0; i < len2x1; i++) {
			AppWidgetProvider2x1.UpdateWidget(context, widget_manager, ids2x1[i], widgetInfo);
		}

		final int[] ids1x1 = widget_manager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider1x1.class));

		final int len1x1 = ids1x1.length;
		for (i = 0; i < len1x1; i++) {
			AppWidgetProvider1x1.UpdateWidget(context, widget_manager, ids1x1[i], widgetInfo);
		}

		if (len2x1 > 0 || len1x1 > 0) {
			Notifications.viewMap(context, widgetInfo);
		} else {
			Notifications.cancelViewMap(context);
		}
	}

	public static void attachPendingServiceIntent(final Context context, final RemoteViews views, final Intent intent,
			int id) {
		if (intent != null) {
			final PendingIntent pIntent = Intents.createPendingService(context, intent);
			views.setOnClickPendingIntent(id, pIntent);
		}
	}

	public static void attachPendingActivityIntent(final Context context, final RemoteViews views, final Intent intent,
			int id) {
		if (intent != null) {
			final PendingIntent pIntent = Intents.createPendingActivity(context, intent);
			views.setOnClickPendingIntent(id, pIntent);
		}
	}

	public static void setInitialWidgetAppearance(final Context context) {
		ILocationWidgetInfo info = InitialStateMessage.create(context);
		WidgetUpdater.updateAll(context, info);
	}
}
