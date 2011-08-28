package com.taskwhere.android.activity;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.taskwhere.android.reciever.ProximityAlertReciever;

public class LocationProxyService extends Service{

	private ProximityAlertReciever proxReciever;
	private static final String ARRIVED_ACTION = "com.taskwhere.android.ARRIVED_ACTION";
	private static final String TW = "TaskWhere";
	
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
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TW, "Service on start command called");
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
