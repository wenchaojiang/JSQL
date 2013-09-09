package com.wenchao.jsql;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JSQLite extends JSONObject{
	
	SQLiteDatabase database = null;
	private ArrayList<DBTableInfo> schema = new ArrayList<DBTableInfo>();
	
	private SQLHelper defaultHelper = new SQLHelper(){

		@Override
		public String getSQLClause(String table, String[] matchedKeys,JSONObject record, boolean recordExisting, String pkName) throws JSONException {
			if(matchedKeys.length == 0){
				return null;
			}
			if(recordExisting){
				//update
				String updates = "";
				for(int i = 0; i< matchedKeys.length; i++){
					//wrap everything into string. it is feasible because Column Affinity of sqlite
					//see http://sqlite.org/datatype3.html
					updates = updates + matchedKeys[i] + "='" + record.get(matchedKeys[i]) +"'";
					if(i != (matchedKeys.length-1)){
						updates = updates + ","  ;
					}
				}
				return "UPDATE " + table +  " SET "+ updates +" WHERE " + pkName + "=" + record.get(pkName);
			}
			else {
				//insert
				String columns = "(";
				String values ="(";
				for(int i = 0; i< matchedKeys.length; i++){
					columns = columns + matchedKeys[i]  ;
					//wrap everything into string, it is feasible because Column Affinity of sqlite
					//see http://sqlite.org/datatype3.html
					values = values + "'" + record.get(matchedKeys[i]) +"'" ;
					if(i != (matchedKeys.length-1)){
						columns = columns + ","  ;
						values = values + "," ;
					}
				}
				columns = columns + ")";
				values = values + ")";
				
				return "INSERT INTO " + table + columns + " VALUES " + values;
			}
		}

		@Override
		public JSONObject prepareData(JSONObject data) {
			return data;
		}
	};

	public JSQLite(JSONObject jsonObject,SQLiteDatabase db) throws JSONException {
		this(jsonObject.toString(),db);	
	}

	public JSQLite(String jsonString,SQLiteDatabase db) throws JSONException {
		super(jsonString);
		scanDatabase(db);
		database = db;
	}

	public void persist() throws JSONException {
			persist(defaultHelper);
	}
	
	public void persist(SQLHelper sh) throws JSONException {
		
		JSONObject data = sh.prepareData(this);
		Iterator<?> keys = data.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
            if( data.get(key) instanceof JSONArray ){

            	JSONArray rows = getJSONArray(key);
            	for(int i = 0; i<rows.length(); i++){
            		DBTableInfo table = getTable(key);
            		if(table != null){
            			String sql = sh.getSQLClause(
            						key, 
            						table.matchColumnValue(rows.getJSONObject(i)),
            						rows.getJSONObject(i), 
            						recordExists(table,rows.getJSONObject(i)),
            						table.getPK()
            					);
            			if(sql != null){
            				Log.i("sql statement", sql);
            			}else{
            				Log.i("sql statement", "no statement");
            			}
            			if(sql != null) database.execSQL(sql);
            		}
            	}
            	
            }
            else {
            	//fallback
            }
            
        }
		
	}
	//compare table
	private DBTableInfo getTable(String table){
		for(DBTableInfo t: schema){
			if(t.getName().equals(table)) return t;
		}
		return null;
	}
	
	
	//compare pk
	private boolean recordExists(DBTableInfo table, JSONObject record){
		//convert whatever objects to a string. 
		//case 1: double, float, int, long will be in it wrapper class, concat with "" will lead to correct string value
		//case 2: string is fine
		//case 3: Other json object or json Array, will lead to sql exception
		//case 4: empty, lead to jsonException
		String pk = null;
		try {
			pk = record.get(table.getPK())+ "";
		} catch (JSONException e) {
			return false;
		}
		
		String selection = "SELECT "+ table.getPK() + " FROM "+ table.getName() +" WHERE " + table.getPK() + " = '"+pk+"' ";
		Cursor c = database.rawQuery(selection,null);
		
		//if cursor is empty than return false
		if(!c.moveToFirst()) return false;
		
		return true;
	}
	
	
	private void scanDatabase(SQLiteDatabase db){
		
		String selection = "SELECT name FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence' ";
		Cursor c = db.rawQuery(selection,null);
		if(c.moveToFirst()) {;
			do {
				Log.i("schema",c.getString(0));
				scanColumns(db,c.getString(0));
			
			} while(c.moveToNext());
		}
		
		
		//show result
	}
	
	private void scanColumns(SQLiteDatabase db, String table){
		String selection = "PRAGMA table_info("+table+")";
		Cursor c = db.rawQuery(selection,null);
		
		String[] columns = new String[c.getCount()];
		String pk = null;
		String pkType = null;
		if(c.moveToFirst()) {;
			do {
				columns[c.getPosition()] = c.getString(1);
				if(c.getInt(5) == 1) pk = c.getString(1);
				if(c.getInt(5) == 1) pkType = c.getString(2);
			} while(c.moveToNext());
		}
		DBTableInfo tableInfo = new DBTableInfo(table,columns,pk,pkType);
		schema.add(tableInfo);
    }
	
	private class DBTableInfo{
		private String name;
		private String[] columns;
		private String pk;
		private String pkType;
		public DBTableInfo(String name,String[] columns,String pk, String pkType){
			this.name = name;
			this.columns = columns;
			this.pk = pk;
			this.pkType = pkType;
		}
		
		public String getName(){
			return name;
		}
		
		public String getPK(){
			return pk;
		}
		
		/*public String[] getColumns(){
			return columns;
		}*/
		
		public String getColumnStrings(){
			String result = "(";
			for (String c :columns){
				result = result + c;
			}
			return result +")";
		}
		
		public String[] matchColumnValue(JSONObject data){
			ArrayList<String> matched = new ArrayList<String>();
			Iterator<?> keys = data.keys();
			while(keys.hasNext()){
				String key = (String)keys.next();
				for (String c :columns){
					if(c.equals(key)){
						matched.add(c);
					}
					
				}
			}
			
			return matched.toArray(new String[matched.size()]);
		}
		
	}
	
}
