package com.szamuraj.omega;

import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity{
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	public final static String EXTRA_MESSAGE = "com.szamuraj.omega.MESSAGE";

	protected LocationManager locationManager;

	protected Button retrieveLocationButton;
	EditText mEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHelper myDbHelper = new DatabaseHelper(getBaseContext());
		myDbHelper = new DatabaseHelper(this);
		try {
			myDbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentView(R.layout.main);
		allevents(getListView());

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

		Spinner dropdown = (Spinner)findViewById(R.id.type);
		String[] items = new String[]{"Event","Artist","Place", "Category"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,"Selected: " + parent.getItemAtPosition(position) , Toast.LENGTH_LONG).show();
				allevents(view);
				changeSum();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		ListView listview = getListView();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition(position);
				if (item instanceof ModelEvent) {
					Intent EventActivity = new Intent(MainActivity.this,EventActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelEvent) item).getId());
					EventActivity.putExtras(b);
					startActivity(EventActivity);
					//finish();
				} else if (item instanceof ModelPlace) {
					Intent PlaceActivity = new Intent(MainActivity.this,PlaceActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelPlace) item).getId());
					PlaceActivity.putExtras(b);
					startActivity(PlaceActivity);
					//finish();
				} else if (item instanceof ModelArtist) {
					Intent ArtistActivity = new Intent(MainActivity.this,ArtistActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelArtist) item).getId());
					ArtistActivity.putExtras(b);
					startActivity(ArtistActivity);
					//finish();
				} else if ( item instanceof ModelCategory) {
					Intent CategoryActivity = new Intent(MainActivity.this,CategoryActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelCategory) item).getId());
					CategoryActivity.putExtras(b);
					startActivity(CategoryActivity);
					//finish();
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

	}


	public void near(View view) {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		ArrayAdapter<?> adapter = (ArrayAdapter<?>) getListAdapter();
		adapter.clear();
		Spinner spinner = (Spinner) findViewById(R.id.type);
		int pos;
		try {
			pos = spinner.getSelectedItemPosition();
		} catch (NullPointerException e) {
			pos = 0;
		}

		switch (pos) {
		case 2:
			ControllerPlace placeoperation = new ControllerPlace(this);
			placeoperation.open();
			String pstr;
			mEdit = (EditText) findViewById(R.id.reqdist);
			try {
				pstr = mEdit.getText().toString();
			} catch (Exception e) {
				pstr = "10";
			}
			if ( pstr.isEmpty()) {
				pstr = "10";
			}

			Float f = Float.parseFloat(pstr);
			placeoperation.setDistance(f);

			List values = placeoperation.getPlacesNearMe(location.getLatitude(), location.getLongitude());
			ArrayAdapter nearplaceadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
			setListAdapter(nearplaceadapter);
			break;
		default:
			ControllerEvent eventoperation = new ControllerEvent(this);
			eventoperation.open();
			String str;
			mEdit = (EditText) findViewById(R.id.reqdist);
			try {
				str = mEdit.getText().toString();
			} catch (Exception e) {
				str = "10";
			}
			if ( str.isEmpty()) {
				str = "10";
			}

			Float fl = Float.parseFloat(str);
			eventoperation.setDistance(fl);

			List<?> eventvalues = eventoperation.getEventsNearMe(location.getLatitude(), location.getLongitude());
			ArrayAdapter neareventadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventvalues);
			setListAdapter(neareventadapter);
			break;
		}
		changeSum();
	}

	public void clearevents(View view) {
		ArrayAdapter adapter = (ArrayAdapter) getListAdapter();
		adapter.clear();
	}

	public void allevents(View view) {
		Spinner spinner = (Spinner) findViewById(R.id.type);
		int pos;
		try {
			pos = spinner.getSelectedItemPosition();
		} catch (NullPointerException e) {
			pos = 0;
		}
		switch (pos) {
		case 0:
			ControllerEvent eventoperation = new ControllerEvent(this);
			eventoperation.open();

			List values = eventoperation.getAllEvents();
			ArrayAdapter adapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
			setListAdapter(adapter);
			break;
		case 1:
			ControllerArtist artist = new ControllerArtist(this);
			artist.open();

			List<?> artistvalues = artist.getAllArtist();
			ArrayAdapter artistadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, artistvalues);
			setListAdapter(artistadapter);
			break;
		case 2:
			ControllerPlace place = new ControllerPlace(this);
			place.open();
			List<?> placevalues = place.getAllPlaces();
			ArrayAdapter placeadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placevalues);
			setListAdapter(placeadapter);
			break;
		case 3:
			ControllerCategory category = new ControllerCategory(this);
			category.open();
			List<?> categoryvalues = category.getAllCategory();
			ArrayAdapter categoryadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryvalues);
			setListAdapter(categoryadapter);
			break;
		}
		//changeSum();
	}

	protected void changeSum() {
		TextView mTextView = (TextView) findViewById(R.id.sum);
		ArrayAdapter<?> adapter = (ArrayAdapter<?>) getListAdapter();
		mTextView.setText( "Sum: " + adapter.getCount());
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
