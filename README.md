JSQL
====

A tool for Android: Persist JSON string and JSON Objects to your SQLite on Database.


Usage
====

When you get a response from web services in JSON format, JSQLite can persist data to database with one line of code
  
  new JSQLite(yourJsonString, yourDB).persist();

Scenario
====
  import com.wenchao.jsql.JSQLite

  //prepare your SQLdatabase 
  TestDBHelper dbHelper = new TestDBHelper(this);
  SQLiteDatabase db = dbHelper.getWritableDatabase();

  //obtain a JSONString or JSONObject
  String jsonString = 
		"{ \"contact\" :" +
				"[{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":\"0\"}," +
				"{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":10}," +
		    "\"address\" :" + 
				      "[{\"line\": 20,\"contact_id\": 15,\"_id\":15}]" +
		"}";

  //initialize JSQLite Object 
  JSQLite jsql = new JSQLite(yourJsonString, yourDB);

  //persist to database
  jsql.persist();
