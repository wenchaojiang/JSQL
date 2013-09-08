package com.example.jsqlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TestDBHelper extends  SQLiteOpenHelper{
	 public static final String TABLE_CONTACT = "contact";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_NUMBER = "number";

	  private static final String DATABASE_NAME = "contact.db";
	  private static final int DATABASE_VERSION = 13;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_CONTACT + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_NAME
	      + " text, " + COLUMN_NUMBER
	      + " integer  " 
	      + ");";
	  private static final String DATABASE_CREATE2 = "create table "
		      + "address" + 
			  "(_id" 
		      + " integer primary key autoincrement, " + 
		      "line" 
		      + " text, " + 
		      "contact_id"
		      + " integer  " 
		      + ");";

	  public TestDBHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	    database.execSQL(DATABASE_CREATE2);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(TestDBHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
	    onCreate(db);
	  }
}
