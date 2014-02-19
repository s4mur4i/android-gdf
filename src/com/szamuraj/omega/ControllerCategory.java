package com.szamuraj.omega;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ControllerCategory {
	String LOG="OMEGA";
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
	
	public List getAllCategory() {
		List categories= new ArrayList();

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
	
	public ModelCategory getCategoryByID (int id ) {
		ModelCategory category = new ModelCategory();

		Cursor cursor = database.query(false, "Category",CATEGORY_TABLE_COLUMNS,"id=" + id, null, null, null, null, null);
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
