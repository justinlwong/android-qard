package com.qardapp.qard.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;

public class QardWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		//change images
		remoteViews.setOnClickPendingIntent(R.id.widget_button, buildButtonPendingIntent(context));
		pushWidgetUpdate(context, remoteViews);
		
		//Camera
	    Intent configIntent = new Intent(context, MainActivity.class);
	    configIntent.putExtra("widgetAction", "Scan");
	    configIntent.putExtra("widgetAction", "QR");
	    PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
	    remoteViews.setOnClickPendingIntent(R.id.widget_camera, configPendingIntent);
	    configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 1);
	    remoteViews.setOnClickPendingIntent(R.id.widget_qr, configPendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	    
	    
	}

	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("pl.looksok.intent.action.CHANGE_PICTURE");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, QardWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
}