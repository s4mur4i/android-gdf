package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ControllerEventArtist {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] EVENTARTIST_TABLE_COLUMNS = { "event_id","artist_id" };
	private SQLiteDatabase database;
	
	public ControllerEventArtist(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
	}
	
	public List getEventByArtist( int id) {
		List eventartists= new ArrayList();

		Cursor cursor = database.query("EventArtist",EVENTARTIST_TABLE_COLUMNS,"event_id=" + id,null,null,null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelEventArtist eventartist= parseEventArtist(cursor);
			eventartists.add(eventartist);
			cursor.moveToNext();
		}

		cursor.close();
		return eventartists;
	}
	
	public List getEventArtistByEvent (int id ) {
		List eventartists= new ArrayList();

		Cursor cursor = database.query("EventArtist",EVENTARTIST_TABLE_COLUMNS,"artist_id=" + id,null,null,null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelEventArtist eventartist= parseEventArtist(cursor);
			eventartists.add(eventartist);
			cursor.moveToNext();
		}

		cursor.close();
		return eventartists;
	}
	
	public ModelEventArtist parseEventArtist (Cursor cursor) {
		ModelEventArtist eventartist = new ModelEventArtist();
		eventartist.setArtist_id(cursor.getInt(0));
		eventartist.setEvent_id(cursor.getInt(1));
		return eventartist;
	}
}
