package com.futonredemption.mylocation.appwidgets;

import com.futonredemption.mylocation.Constants;
import com.futonredemption.mylocation.ILocationWidgetInfo;
import com.futonredemption.mylocation.R;
import com.futonredemption.mylocation.WidgetUpdater;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class AppWidgetProvider2x1 extends AppWidgetProvider
{
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		WidgetUpdater.setInitialWidgetAppearance(context);
	}
	
	public static final int ConvertToLayoutId2x1(final int layoutstate_id)
	{
		switch(layoutstate_id)
		{
			case Constants.WIDGETLAYOUTSTATE_NotAvailable: { return R.layout.appwidget_2x1_notavailable; }
			case Constants.WIDGETLAYOUTSTATE_Loading: { return R.layout.appwidget_2x1_loading; }
			case Constants.WIDGETLAYOUTSTATE_Default: { return R.layout.appwidget_2x1_default; }
		}
		
		return R.layout.appwidget_2x1_notavailable;
	}
	
	public static void UpdateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, final ILocationWidgetInfo widgetInfo)
	{
		RemoteViews views = null;

		final int layout_id = ConvertToLayoutId2x1(widgetInfo.getWidgetState());

		views = new RemoteViews(context.getPackageName(), layout_id);

		WidgetUpdater.attachPendingServiceIntent(context, views, widgetInfo.getActionIntent(), R.id.btnAction);
		WidgetUpdater.attachPendingActivityIntent(context, views, widgetInfo.getShareIntent(), R.id.btnShare);
		WidgetUpdater.attachPendingActivityIntent(context, views, widgetInfo.getViewIntent(), R.id.panelInformation);
		
		if(layout_id == R.layout.appwidget_2x1_notavailable || layout_id == R.layout.appwidget_2x1_loading)
		{
			views.setTextViewText(R.id.panelInformation, widgetInfo.getDescription());
		}

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
