package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.location.LocationManager;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class ControllerEvent {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] EVENT_TABLE_COLUMNS = { "id","name", "place_id", "pic", "url", "date", "category_id", "besorolas" };
	private SQLiteDatabase database;
	float distance = 10;
	EditText mEdit;
	public ControllerEvent(Context context) {
		dbhelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public void close() {
		dbhelper.close();
	}

	public List getAllEvents() {
		List events = new ArrayList();

		Cursor cursor = database.query("Event",EVENT_TABLE_COLUMNS,null,null,null,null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelEvent event= parseEvent(cursor);
			events.add(event);
			cursor.moveToNext();
		}

		cursor.close();
		return events;

	}

	public List getEventsNearMe(double lat, double lon) {
		GeoLocation myLocation = GeoLocation.fromDegrees(lat, lon);
		//double earthRadius = 6371.01;
		List events= new ArrayList();
		//GeoLocation[] boundingCoordinates =	myLocation.boundingCoordinates(distance, earthRadius);
		//boolean meridian180WithinDistance = boundingCoordinates[0].getLongitudeInRadians() > boundingCoordinates[1].getLongitudeInRadians();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN Place ON Event.place_id = Place.id");
		//Log.v(LOG, "Min: " + boundingCoordinates[0].getLatitudeInDegrees() + " Max: " + boundingCoordinates[1].getLatitudeInDegrees());
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Event.id","Event.name", "Event.place_id", "Event.pic", "Event.url", "Event.date", "Event.category_id", "Event.besorolas", "Place.lat","Place.lon"}, 
				null, null, null, null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			//	double dist = myLocation.distance(, , , );
			Location pos_me = new Location("pont A");

			pos_me.setLatitude(myLocation.getLatitudeInDegrees());
			pos_me.setLongitude(myLocation.getLongitudeInDegrees());

			Location pos_event = new Location("point B");

			pos_event.setLatitude(cursor.getDouble(8));
			pos_event.setLongitude(cursor.getDouble(9));

			float dist = pos_me.distanceTo(pos_event)/1000;
			
			if ( dist<= distance) {
				ModelEvent event= parseEvent(cursor);
				events.add(event);
			}
			cursor.moveToNext();
		}

		cursor.close();
		return events;
	}

	public ModelEvent getEventByID(int id) {
		ModelEvent event = new ModelEvent();

		Cursor cursor = database.query(false, "Event",EVENT_TABLE_COLUMNS,"id=" + id, null, null, null, null, null);
		event = parseEvent(cursor);
		cursor.close();
		return event;
	}

	public ModelEvent parseEvent(Cursor cursor) {
		ModelEvent event = new ModelEvent();
		event.setId((cursor.getInt(0)));
		event.setName(cursor.getString(1));
		event.setPlace_id(cursor.getInt(2));
		event.setPic(cursor.getString(3));
		event.setUrl(cursor.getString(4));
		event.setDate(cursor.getString(5));
		event.setCategory_id(cursor.getInt(6));
		event.setBesorolas(cursor.getInt(7));
		return event;
	}
}
//"(Place.lat >= " + boundingCoordinates[0].getLatitudeInDegrees() +
//" AND Place.lat <= " +  boundingCoordinates[1].getLatitudeInDegrees() +
//" ) AND (Place.lon >= " + boundingCoordinates[0].getLongitudeInDegrees() +
//(meridian180WithinDistance ? " OR " : " AND ") +
//" Place.lon <= " + boundingCoordinates[1].getLongitudeInDegrees() + " ) AND " + 
//"acos(sin(" + myLocation.getLatitudeInDegrees() + ") * sin(Place.lat) + cos("+ myLocation.getLatitudeInDegrees()+
//") * cos(Place.lat) * cos( Place.lon - " + myLocation.getLongitudeInDegrees() + ")) <= " + distance / earthRadius