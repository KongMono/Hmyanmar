package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.ListHClusiveEntry;

import android.util.Log;

public class HClusiveParser {
	private ArrayList<ListHClusiveEntry> arr;
	private JSONObject jObject;
	private String str = "";
	ListHClusiveEntry item;

	public HClusiveParser() {
		arr = new ArrayList<ListHClusiveEntry>();
	}

	public ArrayList<ListHClusiveEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			JSONObject itemObject = dataObject.getJSONObject("items");
			JSONArray itemsjsonArray= itemObject.getJSONArray("item");
			
			for (int i = 0; i < itemsjsonArray.length(); i++) {
				item = new ListHClusiveEntry();
				jObject = itemsjsonArray.getJSONObject(i);
				item.setId(jObject.getInt("id"));
				item.setThumbnail(jObject.getString("thumbnail"));
				item.setTitle(jObject.getString("title"));
				arr.add(item);
			}
			
			
		} catch (JSONException e) {
			Log.e("NewsPaser", "Error ", e);
		}
		return arr;
	}

	public HashMap<String, String> getAllowOperator(JSONObject jsonObject){
		HashMap<String, String> allow = new HashMap<String, String>();
		try{
			
			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			JSONObject allowObject = dataObject.getJSONObject("allow");
			
			JSONArray jname = allowObject.names();
			for(int i=0;i<jname.length();i++){
				allow.put(jname.getString(i), allowObject.getString(jname.getString(i)));
			}
			
		} catch (JSONException e) {
			Log.e("NewsPaser", "Error ", e);
			allow = null;
		}
		return allow;
	}
}
