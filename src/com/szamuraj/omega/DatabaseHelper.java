package com.szamuraj.omega;
import java.io.*;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper{
	private static String DB_PATH = "/data/data/com.szamuraj.omega/databases/";
	private static String DB_NAME = "database.sqlite";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}	
	public void createDataBase() throws IOException{
		this.getReadableDatabase();
		try {
			copyDataBase();
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}
	private void copyDataBase() throws IOException{
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	public void openDataBase() throws SQLException{
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
	@Override
	public synchronized void close() {
		if(myDataBase != null)
			myDataBase.close();
		super.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			this.copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}