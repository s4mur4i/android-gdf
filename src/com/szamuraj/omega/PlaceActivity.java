package com.szamuraj.omega;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
public class PlaceActivity extends Activity{
	protected LocationManager locationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placelayout);
		Bundle b = getIntent().getExtras();
		final int placeid = b.getInt("id");
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		updateLayout(placeid);
		Spinner dropdown = (Spinner)findViewById(R.id.placespinner);
		String[] items = new String[]{"Event","Artist","Category"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(PlaceActivity.this,"Selected: " + parent.getItemAtPosition(position) , Toast.LENGTH_LONG).show();
				updateList(view, placeid);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {	}
		});
		ListView eventlistview = (ListView) findViewById(R.id.placelist);
		eventlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getItemAtPosition(position);
				if (item instanceof ModelEvent) {
					Intent EventActivity = new Intent(PlaceActivity.this,EventActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelEvent) item).getId());
					Log.v("Omega", "id: " +  ((ModelEvent) item).getId());
					EventActivity.putExtras(b);
					startActivity(EventActivity);
					//finish();
				} else if (item instanceof ModelPlace) {
					Intent PlaceActivity = new Intent(PlaceActivity.this,PlaceActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelPlace) item).getId());
					PlaceActivity.putExtras(b);
					startActivity(PlaceActivity);
					//finish();
				} else if (item instanceof ModelArtist) {
					Intent ArtistActivity = new Intent(PlaceActivity.this,ArtistActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelArtist) item).getId());
					ArtistActivity.putExtras(b);
					startActivity(ArtistActivity);
					//finish();
				} else if ( item instanceof ModelCategory) {
					Intent CategoryActivity = new Intent(PlaceActivity.this,CategoryActivity.class);
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
		TextView nameview = (TextView) findViewById(R.id.placename);
		TextView distview = (TextView) findViewById(R.id.placedist);
		TextView telview = (TextView) findViewById(R.id.placetel);
		TextView addrrview = (TextView) findViewById(R.id.placeaddrr);
		TextView emailview = (TextView) findViewById(R.id.placeemail);
		ControllerPlace placecont = new ControllerPlace(this);
		placecont.open();
		ModelPlace placeobj = placecont.getPlaceByID(id);
		nameview.setText(placeobj.getName());
		telview.setText(placeobj.getTel());
		addrrview.setText(placeobj.getAddrr());
		emailview.setText(placeobj.getEmail());
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		distview.setText( placecont.PlaceDist(id, location.getLatitude(),location.getLongitude()) + "Km");
		placecont.close();
	}
	public void updateList(View view, int id) {
		Spinner spinner = (Spinner) findViewById(R.id.placespinner);
		final ListView listview = (ListView) findViewById(R.id.placelist);
		int pos;
		try {
			pos = spinner.getSelectedItemPosition();
		} catch (NullPointerException e) {
			pos = 0;
		}
		switch (pos) {
		case 0:
			//Event
			ControllerEvent eventoperation = new ControllerEvent(this);
			eventoperation.open();
			List<?> eventvalues = eventoperation.getPlace2Event(id);
			ArrayAdapter eventadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventvalues);
			listview.setAdapter(eventadapter);
			eventoperation.close();
			break;
		case 1:
			//Artist
			ControllerArtist artistoperation = new ControllerArtist(this);
			artistoperation.open();
			List<?> artistvalues = artistoperation.getPlace2Artist(id);
			ArrayAdapter artistadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, artistvalues);
			listview.setAdapter(artistadapter);
			artistoperation.close();
			break;
		case 2:
			//Category
			ControllerCategory categoryoperation = new ControllerCategory(this);
			categoryoperation.open();
			List<?> categoryvalues = categoryoperation.getPlace2Category(id);
			ArrayAdapter categoryadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryvalues);
			listview.setAdapter(categoryadapter);
			categoryoperation.close();
			break;
		}
	}
}