package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.SBEntry;

import android.util.Log;



public class SpringBoardParser {
	private ArrayList<SBEntry> arr;
	private JSONObject jObject;
	private String TAG = getClass().getSimpleName();
	SBEntry item;

	public SpringBoardParser() {
		arr = new ArrayList<SBEntry>();
	}

	public ArrayList<SBEntry> getData(JSONObject jsonObject) throws Exception {
		arr.clear();
		try {

			jObject = jsonObject;
			
			JSONObject pagesObject = jObject.getJSONObject("pages");
			JSONObject dataObject = pagesObject.getJSONObject("Pages");
			JSONArray itemsjsonArray = dataObject.getJSONArray("item");
			
			for (int i = 0; i < itemsjsonArray.length(); i++) {
				item = new SBEntry();
				jObject = itemsjsonArray.getJSONObject(i);
				item.setTitle(jObject.getString("title"));
				item.setIcon(jObject.getString("icon"));
				item.setIcon_active(jObject.getString("icon_active"));
				item.setTemplate(jObject.getString("template"));
				item.setType(jObject.getString("type"));
				arr.add(item);
			}
			
		} catch (JSONException e) {
			Log.e(TAG,e.getMessage(), e);
		}
		return arr;
	}
	

}
