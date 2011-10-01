package com.taskwhere.android.activity;

import com.taskwhere.android.adapter.TaskListDbAdapter;
import com.taskwhere.android.reciever.ProximityAlertReciever;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;


public class LocationProxyService extends Service{

	private ProximityAlertReciever proxReciever;
	private static final String ARRIVED_ACTION = "com.taskwhere.android.ARRIVED_ACTION";
	private static final String ACTIVE_TASK_LOC = "com.taskwhere.android.model.TaskLoc";
	private static final String ACTIVE_TASK_TEXT = "com.taskwhere.android.model.TaskText";
	private static final String TW = "TaskWhere";
	private static boolean registered = true;
	
	
	private TaskListDbAdapter dbAdapter;
	private Cursor taskItemCursor;
	private LocationManager locationManager;
	
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		Log.d(TW, "Service on create called");
		IntentFilter filter = new IntentFilter(ARRIVED_ACTION);
		proxReciever = new ProximityAlertReciever();
		registerReceiver(proxReciever, filter);
		Log.d(TW, "Registered ProximityIntentReciever succesfully");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d(TW, "Service on start command called");
		//its one time job, only do this when bootup recieved
		if(registered){
			reregisterAlertsBack();
			Log.d(TW, "Reregistered proximity alerts back via LocationProxyService");
			registered = false;
		}
		Log.d(TW, "Now reregister value of the service is : " + registered);
	}
	
/*	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TW, "Service on start command called");
		//its one time job, only do this when bootup recieved
		if(registered){
			reregisterAlertsBack();
			Log.d(TW, "Reregistered proximity alerts back via LocationProxyService");
			registered = false;
		}
		Log.d(TW, "Now reregister value of the service is : " + registered);
		
		return START_STICKY;
	}*/
	
	
	/*
	 * reregister proximity alerts that wiped 
	 * before device is booted up
	 */
	public void reregisterAlertsBack(){

		Log.d(TW, "Now registering all proximity alerts back");
		String contenxt = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(contenxt);
		
		dbAdapter = new TaskListDbAdapter(this);        
	    dbAdapter.open();
	    
	    taskItemCursor = dbAdapter.getAllTasks();
	    taskItemCursor.requery();
	    
	    if(taskItemCursor.getCount() != 0){
	    	taskItemCursor.moveToFirst();
	    	
	    	do{
	    		
	    		Intent anIntent = new Intent(ARRIVED_ACTION);
				anIntent.putExtra(ACTIVE_TASK_LOC, taskItemCursor.getString(2));
				anIntent.putExtra(ACTIVE_TASK_TEXT, taskItemCursor.getString(1));
				
				PendingIntent operation = PendingIntent.getBroadcast(getApplicationContext(), taskItemCursor.getInt(5), anIntent, 0);
				locationManager.addProximityAlert(taskItemCursor.getDouble(3), taskItemCursor.getDouble(4), taskItemCursor.getInt(7), -1, operation);
			
				Log.d(TW, "======================= TASK ITEM ====================");
				Log.d(TW, "Reregistered task loc : " + taskItemCursor.getString(2));
				Log.d(TW, "Reregistered task text : " + taskItemCursor.getString(1));
				Log.d(TW, "Reregistered task latitude : " + taskItemCursor.getString(3));
				Log.d(TW, "Reregistered task longitude : " + taskItemCursor.getString(4) + "\n");
				
	    	}while(taskItemCursor.moveToNext());
	    }
	    taskItemCursor.close();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
