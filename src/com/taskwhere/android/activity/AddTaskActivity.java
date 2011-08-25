package com.taskwhere.android.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class AddTaskActivity extends MapActivity{

	private final static String TW = "TaskWhere";
	private MapView locMapView;
	private MapController mapController;
	private MyLocationOverlay me=null;
	private GeoPoint point;
	private Drawable marker;
	private LocationManager locationManager;
	private ProgressDialog loadingDialog;
	
	boolean gpsEnabled;
	boolean wirelessEnabled;
	Location location;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, TaskWhereActivity.createIntent(this),R.drawable.home));
		
		final Action infoAction = new IntentAction(this, SearchAddressActivity.createIntent(this), R.drawable.search_magnifier);
        actionBar.addAction(infoAction);
        
        final Action addAction = new IntentAction(this, AddTaskActivity.createIntent(this), R.drawable.accept_item);
        actionBar.addAction(addAction);
		
		showDialog(1);
		
		locMapView = (MapView) findViewById(R.id.locMapView);
		locMapView.setBuiltInZoomControls(true);
		mapController = locMapView.getController();
		
		locMapView.setStreetView(true);
		mapController.setZoom(15);
		
		marker = getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),marker.getIntrinsicHeight());
		
		String contenxt = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(contenxt);
		
		gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		wirelessEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if(!gpsEnabled && !wirelessEnabled){
			
			Toast disableToast = Toast.makeText(getApplicationContext(), "Its seems both your GPS and WIFI is disabled", Toast.LENGTH_LONG);
			disableToast.show();
		}
		
		if (gpsEnabled) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
		}
		
		if(wirelessEnabled){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networklocationListener);
		}
		
		Timer timerLoc = new Timer();
		timerLoc.schedule(new LocationTaks(), 10000);
		
		me=new MyLocationOverlay(this, locMapView);
		locMapView.getOverlays().add(me);
	}
	
    public static Intent createIntent(Context context) {
		
		Intent i = new Intent(context, AddTaskActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
	}
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	
		loadingDialog = ProgressDialog.show(AddTaskActivity.this, "", "Getting your current location.Please wait...",true);
    	loadingDialog.setCancelable(true);
    	
		return loadingDialog;
    }
    
    class LocationTaks extends TimerTask{
		
		@Override
		public void run() {

			Location gpsLoc = null,networkLoc = null;
			locationManager.removeUpdates(gpsLocationListener);
			locationManager.removeUpdates(networklocationListener);
			
			if(gpsEnabled){
				gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Log.d(TW, "Last known gps location is : " + gpsLoc);
			}
			
			if(wirelessEnabled){
				networkLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				Log.d(TW, "Last known network location is : " + networkLoc);
			}
			
			if(gpsLoc != null && networkLoc != null){
				
				if(gpsLoc.getTime() > networkLoc.getTime()){
					location = gpsLoc;
				}else{
					location = networkLoc;
				}
			}
			
			if(gpsLoc != null){
				location = gpsLoc;
			}
			
			if(networkLoc != null){
				location = networkLoc;
			}
			
			updateWithNewLocation(location,marker);
		}
	}
    
    private void updateWithNewLocation(Location location,Drawable marker){
		
		if(location != null){
		
			loadingDialog.dismiss();
			locMapView.getOverlays().clear();
			Double lat = location.getLatitude() * 1E6;
			Double lon = location.getLongitude()* 1E6;
			
			Log.d(TW,"New updated location : " + "Latitude => " + lat.intValue() +" | Longitude => "+ lon.intValue());
			point = new GeoPoint(lat.intValue(), lon.intValue());
			mapController.animateTo(point);
			locMapView.getOverlays().add(new SitesOverlay(marker,location));

		}
	}

	private final LocationListener gpsLocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d(TW, "There are some updates from GSP listener");
			updateWithNewLocation(location,marker);
		}
	};

	private final LocationListener networklocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d(TW, "There are some updates from Network listener");
			updateWithNewLocation(location,marker);
		}
	};
    
	private GeoPoint getPoint(double lat, double lon) {
    
		return(new GeoPoint((int)(lat*1000000.0),(int)(lon*1000000.0)));
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}
	
	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		
		public List<OverlayItem> items=new ArrayList<OverlayItem>();
		private Drawable marker=null;
		private OverlayItem inDrag=null;
		private ImageView dragImage=null;
		private int xDragImageOffset=0;
		private int yDragImageOffset=0;
		private int xDragTouchOffset=0;
		private int yDragTouchOffset=0;

		public SitesOverlay(Drawable marker,Location location) {
			
			super(marker);
			this.marker=marker;
			dragImage=(ImageView)findViewById(R.id.drag);
			xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
			yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();
			items.add(new OverlayItem(getPoint(location.getLatitude(),location.getLongitude()), "Current Location", "You are here"));
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return(items.get(i));
		}

		@Override
		public void draw(Canvas canvas, MapView mapView,
											boolean shadow) {
			
			if(!shadow){
				
				super.draw(canvas, mapView, shadow);
			}
			boundCenterBottom(marker);
		}
 		
		@Override
		public int size() {
			return(items.size());
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			
			final int action=event.getAction();
			final int x=(int)event.getX();
			final int y=(int)event.getY();
			boolean result=false;

			if (action == MotionEvent.ACTION_DOWN) {
				for (OverlayItem item : items) {
					Point p=new Point(0,0);

					locMapView.getProjection().toPixels(item.getPoint(), p);

					if (hitTest(item, marker, x-p.x, y-p.y)) {
						result=true;
						inDrag=item;
						items.remove(inDrag);
						populate();

						xDragTouchOffset=0;
						yDragTouchOffset=0;

						setDragImagePosition(p.x, p.y);
						dragImage.setVisibility(View.VISIBLE);

						xDragTouchOffset=x-p.x;
						yDragTouchOffset=y-p.y;

						break;
					}
				}
			}
			else if (action == MotionEvent.ACTION_MOVE && inDrag!=null) {
				setDragImagePosition(x, y);
				result=true;
			}
			else if (action==MotionEvent.ACTION_UP && inDrag!=null) {
				
				Log.d(TW, "Dragging pin setting visibility gone");
				dragImage.setVisibility(View.GONE);

				Log.d(TW, "Getting new projection geopoint from map");
				GeoPoint pt=locMapView.getProjection().fromPixels(x-xDragTouchOffset,y-yDragTouchOffset);
				Log.d(TW, "Dragged Location is : " +" Lattitude => "+ pt.getLatitudeE6() +" | Longitude =>" + pt.getLongitudeE6());
				OverlayItem toDrop=new OverlayItem(pt, inDrag.getTitle(),inDrag.getSnippet());
				Log.d(TW, "Adding new point to items and animate to that location");
				point = pt;
				mapController.animateTo(pt);
				items.clear();
				items.add(toDrop);
				populate();
				Log.d(TW, "Populating map again...");

				inDrag=null;
				result=true;
			}

			return(result || super.onTouchEvent(event, mapView));
		}

		private void setDragImagePosition(int x, int y) {
			LinearLayout.LayoutParams lp=
				(LinearLayout.LayoutParams)dragImage.getLayoutParams();

			lp.setMargins(x-xDragImageOffset-xDragTouchOffset,y-yDragImageOffset-yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}
}
