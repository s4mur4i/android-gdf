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

public class ControllerPlace {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] PLACE_TABLE_COLUMNS = { "id","name", "url", "lat", "lon", "addrr", "email", "tel" };
	private SQLiteDatabase database;
	protected LocationManager locationManager;
	float distance = 10;
	public ControllerPlace(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
	}
	
	public List getAllPlaces() {
		List places= new ArrayList();

		Cursor cursor = database.query("Place",PLACE_TABLE_COLUMNS,null,null,null,null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelPlace place= parsePlace(cursor);
			places.add(place);
			cursor.moveToNext();
		}

		cursor.close();
		return places;
	}
	
	public List getPlacesNearMe(double lat, double lon) {
		GeoLocation myLocation = GeoLocation.fromDegrees(lat, lon);
		List places= new ArrayList();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Place");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),PLACE_TABLE_COLUMNS, null, null, null, null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Location pos_me = new Location("pont A");
			pos_me.setLatitude(myLocation.getLatitudeInDegrees());
			pos_me.setLongitude(myLocation.getLongitudeInDegrees());
			Location pos_event = new Location("point B");
			pos_event.setLatitude(cursor.getDouble(3));
			pos_event.setLongitude(cursor.getDouble(4));
			float dist = pos_me.distanceTo(pos_event)/1000;
			
			if ( dist<= distance) {
				ModelPlace place= parsePlace(cursor);
				places.add(place);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return places;
		
	}
	
	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public ModelPlace getPlaceByID (int id ) {
		ModelPlace place = new ModelPlace();

		Cursor cursor = database.query(false, "Place",PLACE_TABLE_COLUMNS,"id=" + id, null, null, null, null, null);
		place = parsePlace(cursor);
		cursor.close();
		return place;
	}
	
	public ModelPlace parsePlace (Cursor cursor) {
		ModelPlace place = new ModelPlace();
		place.setId(cursor.getInt(0));
		place.setName(cursor.getString(1));
		place.setUrl(cursor.getString(2));
		place.setLat(cursor.getLong(3));
		place.setLon(cursor.getLong(4));
		place.setAddrr(cursor.getString(5));
		place.setEmail(cursor.getString(6));
		place.setTel(cursor.getString(7));
		return place;
	}
}
