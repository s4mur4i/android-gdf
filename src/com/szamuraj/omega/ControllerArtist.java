package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ControllerArtist {
	String LOG="OMEGA";
	private DatabaseHelper dbhelper;
	private String[] ARTIST_TABLE_COLUMNS = { "id","name", "url" };
	private SQLiteDatabase database;
	
	public ControllerArtist(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
	}
	
	public List<ModelArtist> getAllArtist() {
		List<ModelArtist> artists= new ArrayList<ModelArtist>();

		Cursor cursor = database.query("Artist",ARTIST_TABLE_COLUMNS,null,null,null,null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelArtist artist= parseArtist(cursor);
			artists.add(artist);
			cursor.moveToNext();
		}

		cursor.close();
		return artists;
	}
	
	public ModelArtist getArtistByID (int id ) {
		ModelArtist artist = new ModelArtist();

		Cursor cursor = database.query(false, "Arist",ARTIST_TABLE_COLUMNS,"id=" + id, null, null, null, null, null);
		artist = parseArtist(cursor);
		cursor.close();
		return artist;
	}
	
	public ModelArtist parseArtist (Cursor cursor) {
		ModelArtist artist = new ModelArtist();
		artist.setId(cursor.getInt(0));
		artist.setName(cursor.getString(1));
		artist.setUrl(cursor.getString(2));
		return artist;
	}
}
