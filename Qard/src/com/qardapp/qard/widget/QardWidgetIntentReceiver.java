package com.qardapp.qard.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.qardapp.qard.R;


public class QardWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("pl.looksok.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context, "CHANGE_PICTURE");
		}
		/*
		if(intent.getAction().equals("pl.looksok.intent.action.CAMERA")){
			cameraWidgetButtonListener(context, "CAMERA");
		}
		*/
		

		
	}

	private void updateWidgetPictureAndButtonListener(Context context, String widgetAction) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());

		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.widget_button, QardWidgetProvider.buildButtonPendingIntent(context));

		QardWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	/*
	private void cameraWidgetButtonListener(Context context, String widgetAction){
		//Log.v("toggle_widget","Enabled is being called"); 

        AppWidgetManager mgr = AppWidgetManager.getInstance(context); 
        //retrieve a ref to the manager so we can pass a view update 

        Intent i = new Intent(); 
        i.setClassName("com.qardapp.qard.widget", "com.qardapp.qard.MainActivity"); 
        PendingIntent myPI = PendingIntent.getService(context, 0, i, 0); 
        //intent to start service 

      // Get the layout for the App Widget 
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_main); 

      //attach the click listener for the service start command intent 
      views.setOnClickPendingIntent(R.id.menu_camera, myPI); 

      //define the componenet for self 
      ComponentName comp = new ComponentName(context.getPackageName(), QardWidgetIntentReceiver.class.getName()); 

      //tell the manager to update all instances of the toggle widget with the click listener 
      mgr.updateAppWidget(comp, views); 
	}
	*/
	

	private int getImageToSet() {
		clickCount++;
		return clickCount % 2 == 0 ? R.drawable.nexcircle : R.drawable.service_facebook;
	}
}