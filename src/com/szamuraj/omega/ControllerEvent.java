package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.util.Log;

public class ControllerEvent {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] EVENT_TABLE_COLUMNS = { "id","name", "place_id", "pic", "url", "date", "category_id", "besorolas" };
	private SQLiteDatabase database;
	float distance = 10;
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

	public List<ModelEvent> getEventsNearMe(double lat, double lon) {
		GeoLocation myLocation = GeoLocation.fromDegrees(lat, lon);
		List<ModelEvent> events= new ArrayList<ModelEvent>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN Place ON Event.place_id = Place.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Event.id","Event.name", "Event.place_id", "Event.pic", "Event.url", "Event.date", "Event.category_id", "Event.besorolas", "Place.lat","Place.lon"}, 
				null, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
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
	
	public float EventDist(int id, double lat, double lon) {
		float dist;
		GeoLocation myLocation = GeoLocation.fromDegrees(lat, lon);
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN Place ON Event.place_id = Place.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Place.lat","Place.lon"}, 
				"Event.id=" + id, null, null, null,null);
		cursor.moveToFirst();
		Location pos_me = new Location("pont A");
		pos_me.setLatitude(myLocation.getLatitudeInDegrees());
		pos_me.setLongitude(myLocation.getLongitudeInDegrees());
		Location pos_event = new Location("point B");
		pos_event.setLatitude(cursor.getDouble(0));
		pos_event.setLongitude(cursor.getDouble(1));
		dist = pos_me.distanceTo(pos_event)/1000;
		cursor.close();
		return dist;
	}
	
	public List<ModelEvent> getArtist2Event(int id) {
		List events = new ArrayList();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN EventArtist ON Event.id = EventArtist.event_id LEFT JOIN Artist ON EventArtist.artist_id = Artist.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Event.id","Event.name", "Event.place_id", "Event.pic", "Event.url", "Event.date", "Event.category_id", "Event.besorolas"}, 
				"Artist.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelEvent event= parseEvent(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		return events;
	}
	
	public List<ModelEvent> getPlace2Event(int id) {
		List events = new ArrayList();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN Place ON Event.place_id = Place.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Event.id","Event.name", "Event.place_id", "Event.pic", "Event.url", "Event.date", "Event.category_id", "Event.besorolas"}, 
				"Place.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelEvent event= parseEvent(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		return events;
	}
	
	public List<ModelEvent> getCategory2Event(int id) {
		List events = new ArrayList();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN Category ON Event.category_id = Category.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Event.id","Event.name", "Event.place_id", "Event.pic", "Event.url", "Event.date", "Event.category_id", "Event.besorolas"}, 
				"Category.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelEvent event= parseEvent(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		return events;
	}
	
	public ModelEvent getEventByID(int id) {
		ModelEvent event = new ModelEvent();
		Log.v(LOG, "id:" + id);
		Cursor cursor = database.query( "Event",EVENT_TABLE_COLUMNS,"id= " + id,null,null,null,null );
		cursor.moveToFirst();
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