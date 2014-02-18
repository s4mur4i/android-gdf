package com.szamuraj.omega;

import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ListActivity{
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    
    protected LocationManager locationManager;
    
    protected Button retrieveLocationButton;
	
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 DatabaseHelper myDbHelper = new DatabaseHelper(getBaseContext());
		 myDbHelper = new DatabaseHelper(this);
		 try {
			myDbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 setContentView(R.layout.main);
		 ControllerEvent eventoperation = new ControllerEvent(this);
		 eventoperation.open();
		 
		 List values = eventoperation.getAllEvents();
		 ArrayAdapter adapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
		 setListAdapter(adapter);
			
			myDbHelper.close();
			
			retrieveLocationButton = (Button) findViewById(R.id.locbtn);
	        
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	        locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, 
	                MINIMUM_TIME_BETWEEN_UPDATES, 
	                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	                new MyLocationListener()
	        );
			retrieveLocationButton.setOnClickListener(new OnClickListener() {
		         @Override
		         public void onClick(View v) {
		             showCurrentLocation();
		         }
		 }); 
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	public void clearevents(View view) {
		 ArrayAdapter adapter = (ArrayAdapter) getListAdapter();
		 adapter.clear();
	}
	public void allevents(View view) {
		 ArrayAdapter adapter = (ArrayAdapter) getListAdapter();
		 ControllerEvent eventoperation = new ControllerEvent(this);
		 List values = eventoperation.getAllEvents();
		 for (  int i = 0; i < values.size(); i++ ) {
			 adapter.add(values.get(i));
		 }
		 setListAdapter(adapter);
	}

	           

 protected void showCurrentLocation() {

     Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

     if (location != null) {
         String message = String.format(
                 "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                 location.getLongitude(), location.getLatitude()
         );
         Toast.makeText(MainActivity.this, message,
                 Toast.LENGTH_LONG).show();
     }

 }
 private class MyLocationListener implements LocationListener {

     public void onLocationChanged(Location location) {
         String message = String.format(
                 "New Location \n Longitude: %1$s \n Latitude: %2$s",
                 location.getLongitude(), location.getLatitude()
         );
         Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
     }

     public void onStatusChanged(String s, int i, Bundle b) {
         Toast.makeText(MainActivity.this, "Provider status changed",
                 Toast.LENGTH_LONG).show();
     }

     public void onProviderDisabled(String s) {
         Toast.makeText(MainActivity.this,
                 "Provider disabled by the user. GPS turned off",
                 Toast.LENGTH_LONG).show();
     }

     public void onProviderEnabled(String s) {
         Toast.makeText(MainActivity.this,
                 "Provider enabled by the user. GPS turned on",
                 Toast.LENGTH_LONG).show();
     }

 }
}
