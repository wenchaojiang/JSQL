JSQL
====

A tool for Android: Persist JSON string and JSON Objects to your SQLite on Database.


Usage
-----

When you get a response from web services in JSON format, JSQLite can persist data to database with one line of code
  
 <code>  new JSQLite(yourJsonString, yourDB).persist();</code> 

Scenario
-----
import JSQLite  
<code>import com.wenchao.jsql.JSQLite</code> 

Prepare your SQLdatabase  
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
    JSQLite jsql = new JSQLite(yourJsonString, yourDB);
```
Persist to database
```
    //persist to database
    jsql.persist();
```
Default JSON schema
-----

The default json schema that can be accepted by JSQLite
```		
	//{
	//	table1:[
	//			column1:value,
	//			column2:value,
	//			column3:value
	//			],
	//	table2:[.....],
	//	table3:[.....],
	//			......
	// }
```
Default behaviours
-----
Customization
-----
