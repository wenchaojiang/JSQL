JSQL
====

A tool for Android: Persist JSON string and JSON Objects to your SQLite on Database.


Usage
-----

When you get a response from web services in JSON format, JSQLite can persist data to database with one line of code
  
 <code>  new JSQLite(yourJsonString, yourDB).persist();</code> 

Scenario
-----
import JSQLite  into your android project
```
    import com.wenchao.jsql.JSQLite
    import com.wenchao.jsql.SQLHelper
```

Prepare your SQLite database  
```
    //prepare your SQLdatabase  
    TestDBHelper dbHelper = new TestDBHelper(this);  
    SQLiteDatabase db = dbHelper.getWritableDatabase();  
```
Obtain a JSONString or JSONObject
```
    //obtain a JSONString or JSONObject
    String jsonString = 
		"{ \"contact\" :" +
				"[{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":\"0\"}," +
				"{\"name\": \"wenchao\",\"number\": 1234.567,\"_id\":10}," +
		    "\"address\" :" + 
				      "[{\"line\": 20,\"contact_id\": 15,\"_id\":15}]" +
		"}";
```
Initialize JSQLite Object 
```
    //initialize JSQLite Object 
    JSQLite jsql = new JSQLite(jsonString, db);
```
Persist to database
```
    //persist to database
    jsql.persist();
```
JSQLite JSON schema
-----

The default json schema that can be accepted by JSQLite:
```		
	//{
	//	table1:[{
	//			column1:value,
	//			column2:value,
	//			column3:value
	//			},  
	//		.....],
	//	table2:[.....],
	//	table3:[.....],
	//			......
	// }
```
Default behaviours
-----
Currently JSQLite do not create database schema for you, 
it scan your exisiting database schema, and matches tables and columns to keys and values in your json.

JSQLite will ignore unmatched tables and columns without complaining, so it is safe to put meta data into your json.
e.g. 
```
	{
		contact:[
				name:value,	 // matched column
				number:value,	 // matched column
				id:value,	 // matched column
				meta_synced:0 // unmatched column, ignored by JSQLite
				],
		table2:[column2:value2],	 // unmatched table and column name, ignored by JSQLite
		meta_table_count:0 //<------not even comply with JSQLite json schema, JSQLite ignore it as well.
	
	}

```
By default, JSQLite performs only "INSERT" and "UPDATE" operations based on primary key matching.
JSQLite will match exisiting records in your database to json entities by comparing their primary keys.  
 
 1. if json entities has a matching records in database, then update the existing records.
 2. if json entities do not have matching records in database, then insert new records.
 3. if primarily key is missing in json entitiy, then insert a new record.


Data  types
-----
If the values in the json entities violate data types in database schema, it will not break the system in most cases,
But in some cases it will UncaughtException, please check the SQLite Type affinity for more details, http://www.sqlite.org/datatype3.html

As a result of Type affinity, JSQLite handle all entities values as a string/text. 


Customization
-----
JSQLite also supplied a helper interface (SQLHelper)for you the customize behaviour of JSQLite.

You only need implement 2 methods in the interface.

```
public String getSQLClause(String table, String[] marchedKeys, JSONObject row, boolean recordExisting,String pkName) throws JSONException;
```




