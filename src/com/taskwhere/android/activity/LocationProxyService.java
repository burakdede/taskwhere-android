package com.taskwhere.android.activity;

import com.taskwhere.android.reciever.ProximityAlertReciever;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LocationProxyService extends Service{

	private ProximityAlertReciever proxReciever;
	private static final String ARRIVED_ACTION = "com.taskwhere.android.activity.ARRIVED_ACTION";
	private static final String TW = "TaskWhere";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TW, "LocationProxyService Service created");
		IntentFilter filter = new IntentFilter(ARRIVED_ACTION);
		proxReciever = new ProximityAlertReciever();
		registerReceiver(proxReciever, filter);
		Log.d(TW, "Registered ProximityIntentReciever succesfully");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
