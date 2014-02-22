package com.szamuraj.omega;

import java.util.EventListener;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EventActivity extends Activity{
	protected LocationManager locationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlayout);
		Intent intent = getIntent();
		Bundle b = getIntent().getExtras();
		final int eventid = b.getInt("id");
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		updateLayout(eventid);
		Spinner dropdown = (Spinner)findViewById(R.id.eventmodeltype);
		String[] items = new String[]{"Artist","Place","Category"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(EventActivity.this,"Selected: " + parent.getItemAtPosition(position) , Toast.LENGTH_LONG).show();
				updateList(view, eventid);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ListView eventlistview = (ListView) findViewById(R.id.list);
		eventlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition(position);
				if (item instanceof ModelEvent) {
					Intent EventActivity = new Intent(EventActivity.this,EventActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelEvent) item).getId());
					Log.v("Omega", "id: " +  ((ModelEvent) item).getId());
					EventActivity.putExtras(b);
					startActivity(EventActivity);
					//finish();
				} else if (item instanceof ModelPlace) {
					Intent PlaceActivity = new Intent(EventActivity.this,PlaceActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelPlace) item).getId());
					PlaceActivity.putExtras(b);
					startActivity(PlaceActivity);
					//finish();
				} else if (item instanceof ModelArtist) {
					Intent ArtistActivity = new Intent(EventActivity.this,ArtistActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelArtist) item).getId());
					ArtistActivity.putExtras(b);
					startActivity(ArtistActivity);
					//finish();
				} else if ( item instanceof ModelCategory) {
					Intent CategoryActivity = new Intent(EventActivity.this,CategoryActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	public void updateLayout(int id) {
		TextView nameview = (TextView) findViewById(R.id.eventname);
		TextView disview = (TextView) findViewById(R.id.eventdistance);
		TextView dateview = (TextView) findViewById(R.id.eventdate);
		TextView urlview = (TextView) findViewById(R.id.eventurl);
		ControllerEvent eventcont = new ControllerEvent(this);
		eventcont.open();
		ModelEvent eventobj = eventcont.getEventByID(id);
		nameview.setText(eventobj.getName());
		dateview.setText("Date: " + eventobj.getDate());
		urlview.setText("Url: " + eventobj.getUrl());
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		disview.setText( eventcont.EventDist(id, location.getLatitude(),location.getLongitude()) + "Km");
		eventcont.close();
	}
	
	public void updateList(View view, int id) {
		Spinner spinner = (Spinner) findViewById(R.id.eventmodeltype);
		final ListView listview = (ListView) findViewById(R.id.list);
		int pos;
		try {
			pos = spinner.getSelectedItemPosition();
		} catch (NullPointerException e) {
			pos = 0;
		}
	switch (pos) {
	case 0:
		ControllerArtist artistoperation = new ControllerArtist(this);
		artistoperation.open();
		List artistvalues = artistoperation.getEvent2Artist(id);
		ArrayAdapter artistadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, artistvalues);
		listview.setAdapter(artistadapter);
		break;
	case 1:
		//Place
		ControllerPlace placeoperation = new ControllerPlace(this);
		placeoperation.open();
		List placevalues = placeoperation.getEvent2Place(id);
		ArrayAdapter placeadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placevalues);
		listview.setAdapter(placeadapter);
		break;
	case 2:
		//Category
		ControllerCategory categoryoperation = new ControllerCategory(this);
		categoryoperation.open();
		List categoryvalues = categoryoperation.getEvent2Category(id);
		ArrayAdapter categoryadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryvalues);
		listview.setAdapter(categoryadapter);
		break;
	}
}
}
