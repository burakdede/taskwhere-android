package com.taskwhere.android.reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.taskwhere.android.activity.LocationProxyService;
import com.taskwhere.android.activity.R;
import com.taskwhere.android.activity.TaskWhereActivity;
import com.taskwhere.android.activity.R.drawable;

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
	private int NOTIFICATION_ID = 0;
	private static final String ACTIVE_TASK_LOC = "com.taskwhere.android.model.TaskLoc";
	private static final String ACTIVE_TASK_TEXT = "com.taskwhere.android.model.TaskText";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			   
			ComponentName comp = new ComponentName(context.getPackageName(), LocationProxyService.class.getName());
			ComponentName service = context.startService(new Intent().setComponent(comp));
			
			
			if (null == service){
			   // something really wrong here
			   Log.e(TW, "Could not start service " + comp.toString());
			}else{
				Log.d(TW, "Service started succesfully via ProximityAlertReciever");
			}
		}else{
			
			String key = LocationManager.KEY_PROXIMITY_ENTERING;
			Boolean entering = intent.getBooleanExtra(key,false);
			
			Bundle bundle = intent.getExtras();
			String taskLoc = bundle.getString(ACTIVE_TASK_LOC);
			String taskText = bundle.getString(ACTIVE_TASK_TEXT);

			if(entering){
					Log.d(TW, "Entered the proximity area");
					createNotification(context, "Task : \"" + taskText + "\""
							,"OH! Near in " + taskLoc + " ?");
			}else{
					Log.d(TW, "Quitted from proximity area");
					Log.d(TW, "Task still did not accomplished");
					createNotification(context, "Task : \"" + taskText + "\""
							,"SNAP! Leaving " + taskLoc + " already ?");
			}
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param intent
	 * @param contentText
	 * 
	 * Create notification based on the action taken 
	 * by user entering | quitting
	 */
	public void createNotification(Context context, String contentText, String contentTitle){
		
		NotificationManager mgr = 
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE) ; 

		int icon = R.drawable.notf_icon;        
		CharSequence tickerText = "TaskWhere"; 
		long when = System.currentTimeMillis();   
		
		Intent notificationIntent = new Intent(context,TaskWhereActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		
		Notification notificationEnter = new Notification(icon, tickerText, when);
		notificationEnter.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrateEnter = {0,100,200,300};
		notificationEnter.vibrate = vibrateEnter;
		notificationEnter.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mgr.notify(NOTIFICATION_ID, notificationEnter);
	}
}
