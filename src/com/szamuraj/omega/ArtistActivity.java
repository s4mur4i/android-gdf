package com.szamuraj.omega;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ArtistActivity extends Activity{
	public final static String EXTRA_MESSAGE = "com.szamuraj.omega.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artistlayout);
		final Intent intent = getIntent();
		final int artistid = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
		updateLayout(artistid);
		Spinner dropdown = (Spinner)findViewById(R.id.artistspinner);
		String[] items = new String[]{"Place","Event","Category"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(ArtistActivity.this,"Selected: " + parent.getItemAtPosition(position) , Toast.LENGTH_LONG).show();
				updateList(view, artistid);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
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
		TextView nameview = (TextView) findViewById(R.id.artistname);
		TextView urlview = (TextView) findViewById(R.id.artisturl);
		ControllerArtist artistcont = new ControllerArtist(this);
		artistcont.open();
		ModelArtist artistobj = artistcont.getArtistByID(id);
		nameview.setText(artistobj.getName());
		urlview.setText("Url: " + artistobj.getUrl());
		artistcont.close();
	}
	
	public void updateList(View view, int id) {
		Spinner spinner = (Spinner) findViewById(R.id.artistspinner);
		final ListView listview = (ListView) findViewById(R.id.artistlist);
		int pos;
		try {
			pos = spinner.getSelectedItemPosition();
		} catch (NullPointerException e) {
			pos = 0;
		}
	switch (pos) {
	case 0:
		//Place
		ControllerPlace placeoperation = new ControllerPlace(this);
		placeoperation.open();
		List placevalues = placeoperation.getArtist2Place(id);
		ArrayAdapter placeadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placevalues);
		listview.setAdapter(placeadapter);
		break;
	case 1:
		//Event
		ControllerEvent eventoperation = new ControllerEvent(this);
		eventoperation.open();
		List eventvalues = eventoperation.getArtist2Event(id);
		ArrayAdapter eventadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventvalues);
		listview.setAdapter(eventadapter);
		break;
	case 2:
		//Category
		ControllerCategory categoryoperation = new ControllerCategory(this);
		categoryoperation.open();
		List categoryvalues = categoryoperation.getArtist2Category(id);
		ArrayAdapter categoryadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryvalues);
		listview.setAdapter(categoryadapter);
		break;
	}
}
}
