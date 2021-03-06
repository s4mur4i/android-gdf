package com.szamuraj.omega;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
public class ControllerArtist {
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
	public List<ModelArtist> getEvent2Artist(int id) {
		List<ModelArtist> artists = new ArrayList<ModelArtist>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("EventArtist INNER JOIN Event ON EventArtist.event_id = Event.id LEFT JOIN Artist on EventArtist.artist_id = Artist.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Artist.id", "Artist.name", "Artist.url"}, 
				"Event.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelArtist artist= parseArtist(cursor);
			artists.add(artist);
			cursor.moveToNext();
		}
		return artists;
	}
	public List<ModelArtist> getCategory2Artist(int id) {
		List<ModelArtist> artists = new ArrayList<ModelArtist>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Artist INNER JOIN EventArtist ON Artist.id = EventArtist.artist_id LEFT JOIN Event on EventArtist.event_id = Event.id LEFT JOIN Category ON Event.category_id = Category.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Artist.id", "Artist.name", "Artist.url"}, 
				"Category.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelArtist artist= parseArtist(cursor);
			artists.add(artist);
			cursor.moveToNext();
		}
		return artists;
	}
	public List<ModelArtist> getPlace2Artist(int id) {
		List<ModelArtist> artists = new ArrayList<ModelArtist>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Artist INNER JOIN EventArtist ON Artist.id = EventArtist.artist_id LEFT JOIN Event on EventArtist.event_id = Event.id LEFT JOIN Place ON Event.place_id = Place.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Artist.id", "Artist.name", "Artist.url"}, 
				"Place.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelArtist artist= parseArtist(cursor);
			artists.add(artist);
			cursor.moveToNext();
		}
		return artists;
	}
	public ModelArtist getArtistByID (int id ) {
		ModelArtist artist = new ModelArtist();
		Cursor cursor = database.query("Artist",ARTIST_TABLE_COLUMNS,"id=" + id, null, null, null, null, null);
		cursor.moveToFirst();
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