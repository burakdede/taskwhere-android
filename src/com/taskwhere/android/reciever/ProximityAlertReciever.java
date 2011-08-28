package com.taskwhere.android.reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.taskwhere.android.activity.R;
import com.taskwhere.android.activity.TaskWhereActivity;

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
	private static final String ACTIVE_TASK_STATUS = "com.taskwhere.android.model.TaskStatus";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key,false);
		
		Bundle bundle = intent.getExtras();
		String taskLoc = bundle.getString(ACTIVE_TASK_LOC);
		String taskText = bundle.getString(ACTIVE_TASK_TEXT);
		int taskStatus = bundle.getInt(ACTIVE_TASK_STATUS, 0);
		
		if(entering){
			Log.d(TW, "Entered the proximity area");
			createNotification(context, "Task : \"" + taskText + "\""
					,"OH! Are you near in " + taskLoc + " ?");
		}else{
			Log.d(TW, "Quitted from proximity area");
			if(taskStatus == 0){			
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
		CharSequence tickerText = "Get A Life"; 
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
