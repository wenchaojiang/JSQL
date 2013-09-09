package com.example.jsqlitetest;

import org.json.JSONException;
import org.json.JSONObject;

import com.wenchao.jsql.JSQLite;


import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TestDBHelper dbHelper = new TestDBHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String jsonString = 
		"{ \"contact\" :" +
				"[{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":\"0\"}," +
				"{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":10}," +
				"{\"name\": \"wenchao\",\"number\": 1234.567}]," +
		    "\"address\" :" + 
				
				"[{\"line\": 20,\"contact_id\": \"fda\",\"_id\":15}]" +
		"}";
		//The default json schema
		
		// {
		//	table1:[
		//			column1:value,
		//			column2:value,
		//			column3:value
		//			],
		//	table2:[.....],
		//	table3:[.....],
		//			......
		// }
					
		
		//default behaviour
		
		
		//TODO test
		//case 1 normal case
		//case 2 invalid JSON structure
		//case 3 invalid JSON structure (JSONObject nested in json array) will crash : uncaught exception
		/*
		String jsonString = 
				"{ \"contact\" :" +
						"[{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":\"0\"}," +
						"{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":10}," +
						"{\"name\": \"wenchao\",\"number\": 1234.567}]," +
				    "\"address\" :" + 
						"[{\"line\": \"wenchao\",\"contact_id\": 1234.567}," +
						"{\"line\": \"wenchao\",\"contact_id\": 1234.567,\"_id\":{\"illegal\":\"json\"}}]" +
				"}";*/
		
		
		/*
		 * Any column in an SQLite version 3 database, except an INTEGER PRIMARY KEY column, may be used to store a value of any storage class.
		*/
		//case 4 data mismatch
		/*
		String jsonString = 
				"{ 
				    "\"address\" :" + 
						"{\"line\": \"wenchao\",\"contact_id\": \"fdsaf\",\"_id\":0}]" +
				"}";*/
		
		try {
			JSQLite o2 = new JSQLite(jsonString,db);
			o2.persist();		
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		showTable(db,"contact");
		showTable(db,"address");
		
	}
	
	private void showTable(SQLiteDatabase db,String table){
		
		//String selection = "SELECT * FROM sqlite_master";
		String selection = "SELECT * from "+ table;
		Cursor c = db.rawQuery(selection,null);
		if(c.moveToFirst()) {;
			do {
				Log.i("table:" + table,c.getString(0)+","+c.getString(1)+"," + c.getString(2));
			} while(c.moveToNext());
		}
		//show result
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
