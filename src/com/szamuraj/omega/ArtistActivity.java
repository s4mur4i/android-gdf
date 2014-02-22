package com.szamuraj.omega;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artistlayout);
		Bundle b = getIntent().getExtras();
		final int artistid = b.getInt("id");
		updateLayout(artistid);
		Spinner dropdown = (Spinner)findViewById(R.id.artistspinner);
		String[] items = new String[]{"Place","Event","Category"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(ArtistActivity.this,"Selected: " + parent.getItemAtPosition(position) , Toast.LENGTH_LONG).show();
				updateList(view, artistid);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		ListView listview = (ListView) findViewById(R.id.artistlist);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getItemAtPosition(position);
				if (item instanceof ModelEvent) {
					Intent EventActivity = new Intent(ArtistActivity.this,EventActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelEvent) item).getId());
					EventActivity.putExtras(b);
					startActivity(EventActivity);
					//finish();
				} else if (item instanceof ModelPlace) {
					Intent PlaceActivity = new Intent(ArtistActivity.this,PlaceActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelPlace) item).getId());
					PlaceActivity.putExtras(b);
					startActivity(PlaceActivity);
					//finish();
				} else if (item instanceof ModelArtist) {
					Intent ArtistActivity = new Intent(ArtistActivity.this,ArtistActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelArtist) item).getId());
					ArtistActivity.putExtras(b);
					startActivity(ArtistActivity);
					//finish();
				} else if ( item instanceof ModelCategory) {
					Intent CategoryActivity = new Intent(ArtistActivity.this,CategoryActivity.class);
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
			List<?> placevalues = placeoperation.getArtist2Place(id);
			ArrayAdapter placeadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placevalues);
			listview.setAdapter(placeadapter);
			placeoperation.close();
			break;
		case 1:
			//Event
			ControllerEvent eventoperation = new ControllerEvent(this);
			eventoperation.open();
			List<?> eventvalues = eventoperation.getArtist2Event(id);
			ArrayAdapter eventadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventvalues);
			listview.setAdapter(eventadapter);
			eventoperation.close();
			break;
		case 2:
			//Category
			ControllerCategory categoryoperation = new ControllerCategory(this);
			categoryoperation.open();
			List<?> categoryvalues = categoryoperation.getArtist2Category(id);
			ArrayAdapter categoryadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryvalues);
			listview.setAdapter(categoryadapter);
			categoryoperation.close();
			break;
		}
	}
}