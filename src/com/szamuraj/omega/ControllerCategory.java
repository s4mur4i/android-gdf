package com.szamuraj.omega;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
public class ControllerCategory {
	private DatabaseHelper dbhelper;
	private String[] CATEGORY_TABLE_COLUMNS = { "id","name" };
	private SQLiteDatabase database;
	public ControllerCategory(Context context) {
		dbhelper = new DatabaseHelper(context);
	}
	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}
	public void close() {
		dbhelper.close();
	}
	public List<ModelCategory> getAllCategory() {
		List<ModelCategory> categories= new ArrayList<ModelCategory>();
		Cursor cursor = database.query("Category",CATEGORY_TABLE_COLUMNS,null,null,null,null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelCategory category= parseCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return categories;
	}
	public List<ModelCategory> getEvent2Category(int id) {
		List<ModelCategory> categories = new ArrayList<ModelCategory>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Category INNER JOIN Event ON Event.place_id=Category.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Category.id","Category.name"}, 
				"Event.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelCategory category= parseCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		return categories;
	}
	public List<ModelCategory> getPlace2Category(int id) {
		List<ModelCategory> categories = new ArrayList<ModelCategory>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Category INNER JOIN Event ON Event.place_id=Category.id LEFT JOIN Place ON Event.place_id=Place.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Category.id","Category.name"}, 
				"Place.id = " + id, null, "Category.name", null,null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelCategory category= parseCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		return categories;
	}
	public List<ModelCategory> getArtist2Category(int id) {
		List<ModelCategory> categories = new ArrayList<ModelCategory>();
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables("Event INNER JOIN Category ON Event.category_id=Category.id LEFT JOIN EventArtist on Event.id = EventArtist.event_id LEFT JOIN Artist ON EventArtist.artist_id = Artist.id");
		Cursor cursor = _QB.query(dbhelper.getReadableDatabase(),
				new String[]{"Category.id", "Category.name"}, 
				"Artist.id = " + id, null, null, null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ModelCategory category= parseCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		return categories;
	}
	public ModelCategory getCategoryByID (int id ) {
		ModelCategory category = new ModelCategory();
		Cursor cursor = database.query(false, "Category",CATEGORY_TABLE_COLUMNS,"id=" + id, null, null, null, null, null);
		cursor.moveToFirst();
		category = parseCategory(cursor);
		cursor.close();
		return category;
	}
	public ModelCategory parseCategory (Cursor cursor) {
		ModelCategory category = new ModelCategory();
		category.setId(cursor.getInt(0));
		category.setName(cursor.getString(1));
		return category;
	}
}