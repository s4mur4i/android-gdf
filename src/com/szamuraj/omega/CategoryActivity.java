package com.szamuraj.omega;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

public class CategoryActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorylayout);
		Intent intent = getIntent();
		Bundle b = getIntent().getExtras();
		final int categoryid = b.getInt("id");
		updateLayout(categoryid);
		Spinner dropdown = (Spinner)findViewById(R.id.categoryspinner);
		String[] items = new String[]{"Place","Event","Artist"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(CategoryActivity.this,"Selected: " + parent.getItemAtPosition(position) , Toast.LENGTH_LONG).show();
				updateList(view, categoryid);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ListView eventlistview = (ListView) findViewById(R.id.categorylist);
		eventlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition(position);
				if (item instanceof ModelEvent) {
					Intent EventActivity = new Intent(CategoryActivity.this,EventActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelEvent) item).getId());
					Log.v("Omega", "id: " +  ((ModelEvent) item).getId());
					EventActivity.putExtras(b);
					startActivity(EventActivity);
					//finish();
				} else if (item instanceof ModelPlace) {
					Intent PlaceActivity = new Intent(CategoryActivity.this,PlaceActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelPlace) item).getId());
					PlaceActivity.putExtras(b);
					startActivity(PlaceActivity);
					//finish();
				} else if (item instanceof ModelArtist) {
					Intent ArtistActivity = new Intent(CategoryActivity.this,ArtistActivity.class);
					Bundle b= new Bundle();
					b.putInt("id", ((ModelArtist) item).getId());
					ArtistActivity.putExtras(b);
					startActivity(ArtistActivity);
					//finish();
				} else if ( item instanceof ModelCategory) {
					Intent CategoryActivity = new Intent(CategoryActivity.this,CategoryActivity.class);
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
		TextView nameview = (TextView) findViewById(R.id.categoryname);
		ControllerCategory categorycont = new ControllerCategory(this);
		categorycont.open();
		ModelCategory categoryobj = categorycont.getCategoryByID(id);
		nameview.setText(categoryobj.getName());
		categorycont.close();
	}
	
	public void updateList(View view, int id) {
		Spinner spinner = (Spinner) findViewById(R.id.categoryspinner);
		final ListView listview = (ListView) findViewById(R.id.categorylist);
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
		List placevalues = placeoperation.getCategory2Place(id);
		ArrayAdapter placeadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placevalues);
		listview.setAdapter(placeadapter);
		break;
	case 1:
		//Event
		ControllerEvent eventoperation = new ControllerEvent(this);
		eventoperation.open();
		List eventvalues = eventoperation.getCategory2Event(id);
		ArrayAdapter eventadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventvalues);
		listview.setAdapter(eventadapter);
		break;
	case 2:
		//Artist
		ControllerArtist artistoperation = new ControllerArtist(this);
		artistoperation.open();
		List values = artistoperation.getCategory2Artist(id);
		ArrayAdapter artistadapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
		listview.setAdapter(artistadapter);
		break;
	}
}

}
