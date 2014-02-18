package com.szamuraj.omega;

import java.io.IOException;
import java.util.List;


import android.app.ListActivity;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity{
	
	
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 DatabaseHelper myDbHelper = new DatabaseHelper(getBaseContext());
		 myDbHelper = new DatabaseHelper(this);
		 try {
			myDbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 setContentView(R.layout.main);
		 ControllerEvent eventoperation = new ControllerEvent(this);
		 eventoperation.open();
		 
		 List values = eventoperation.getAllEvents();
		 ArrayAdapter adapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
		 setListAdapter(adapter);
			
			myDbHelper.close();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}


}
