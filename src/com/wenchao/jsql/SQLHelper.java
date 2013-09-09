package com.wenchao.jsql;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public interface SQLHelper {
	public String getSQLClause(String table, String[] marchedKeys, JSONObject row, boolean recordExisting,String pkName) throws JSONException;
	public JSONObject prepareData(JSONObject json);
}
