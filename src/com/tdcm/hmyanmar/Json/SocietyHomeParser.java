package com.tdcm.hmyanmar.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.EpisodeEntry;

import android.util.Log;

public class SocietyHomeParser {
	private List<HashMap<String, String>> arr;
	private JSONObject jObject;
	EpisodeEntry item;

	public SocietyHomeParser() {
		arr = new Vector<HashMap<String,String>>();
	}

	public List<HashMap<String, String>> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			JSONObject contents = dataObject.getJSONObject("contents");
			Object entry= contents.get("entry");
			
			HashMap<String, String> h = null;
			
			
			if(entry instanceof JSONArray){
				JSONArray jobs = (JSONArray) entry;
				
				for (int i = 0; i < jobs.length(); i++) {
					JSONObject js = jobs.getJSONObject(i);
					h = new HashMap<String, String>();
					
					for (int j = 0; j < js.names().length(); j++) {
						h.put(js.names().getString(j), js.getString(js.names().getString(j)));
					}
					
					arr.add(h);
				}
			}else{
				JSONObject js = (JSONObject) entry;
				h = new HashMap<String, String>();
				
				for (int j = 0; j < js.names().length(); j++) {
					h.put(js.names().getString(j), js.getString(js.names().getString(j)));
				}
				
				arr.add(h);
			}
			
		} catch (JSONException e) {
			Log.e("SocietyHomeParser", "Error ", e);
		}
		return arr;
	}


}
