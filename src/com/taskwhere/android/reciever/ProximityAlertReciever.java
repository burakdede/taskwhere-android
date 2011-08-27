package com.taskwhere.android.reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.taskwhere.android.activity.R;

/**
 * 
 * @author burak
 * @data 27 Aug 2011
 * 
 * Broadcast reciever to fire notifications
 * when user enters the proximity area
 */
public class ProximityAlertReciever extends BroadcastReceiver{

	private static final String TW = "TaskWhere";
	private int SIMPLE_NOTFICATION_ID;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(TW, "Proximity alert recieved");
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;		
		Boolean entering = intent.getBooleanExtra(key, false);
		
		if(entering){
			Log.d(TW, "Entered the proximity area");
			createNotification(context, intent, "Opppsss look like you have task in near");
			
		}else{
			Log.d(TW, "Quitted from proximity area");
			createNotification(context, intent, "Opppsss look like you have task in near");
		}

	}
	
	public void createNotification(Context context, Intent intent, String contentText){
		
		NotificationManager mgr = 
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE) ; 

		int icon = R.drawable.notf_icon;        
		CharSequence tickerText = "Get A Life"; 
		long when = System.currentTimeMillis(); 
		CharSequence contentTitle = "TaskWhere | You have a Task !!!";  
		
		Intent notificationIntent = new Intent();
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		
		Notification notificationEnter = new Notification(icon, tickerText, when);
		notificationEnter.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrateEnter = {0,100,200,300};
		notificationEnter.vibrate = vibrateEnter;
		notificationEnter.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mgr.notify(SIMPLE_NOTFICATION_ID, notificationEnter);
		
	}
}
