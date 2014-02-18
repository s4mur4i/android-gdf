package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ControllerEvent {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] EVENT_TABLE_COLUMNS = { "id","name", "place_id", "pic", "url", "date", "category_id", "besorolas" };
	private SQLiteDatabase database;
	
	public ControllerEvent(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
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
