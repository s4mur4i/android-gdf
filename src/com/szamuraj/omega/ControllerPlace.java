package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;

public class ControllerPlace {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] PLACE_TABLE_COLUMNS = { "id","name", "url", "lat", "lon", "addr", "email", "tel" };
	private SQLiteDatabase database;
	protected LocationManager locationManager;

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
	
	public List getPlacesNearMe() {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		GeoLocation myLocation = GeoLocation.fromDegrees(location.getLatitude(), location.getLongitude());
		double earthRadius = 6371.01;
		double distance = 1000;
		List places= new ArrayList();
		GeoLocation[] boundingCoordinates =	myLocation.boundingCoordinates(distance, earthRadius);
		boolean meridian180WithinDistance = boundingCoordinates[0].getLongitudeInRadians() > boundingCoordinates[1].getLongitudeInRadians();
		
		Cursor cursor = database.query("Place",PLACE_TABLE_COLUMNS, "(lat >= " + boundingCoordinates[0].getLatitudeInRadians() + 
				" AND lat <= " +  boundingCoordinates[1].getLatitudeInRadians() + 
				" ) AND (lon >= ? " + (meridian180WithinDistance ? "OR" : "AND") +
				" lon <= " + boundingCoordinates[1].getLongitudeInRadians() + 
				" ) AND " + "acos(sin(" + myLocation.getLatitudeInRadians() + 
				" ) * sin(lat) + cos( " + myLocation.getLatitudeInRadians() + 
				" ) * cos(lat) * cos(lon -" + myLocation.getLongitudeInRadians() + 
				")) <= " + distance / earthRadius,null,null,null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelPlace place= parsePlace(cursor);
			places.add(place);
			cursor.moveToNext();
		}

		cursor.close();
		return places;
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
