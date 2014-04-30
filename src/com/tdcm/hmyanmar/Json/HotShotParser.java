package com.tdcm.hmyanmar.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.EpisodeEntry;

import android.util.Log;

public class HotShotParser {
	private List<HashMap<String, String>> arr;
	private List<HashMap<String, String>> desc;
	private JSONObject jObject;
	EpisodeEntry item;

	public HotShotParser() {
		arr = new Vector<HashMap<String,String>>();
	}
	
	public List<HashMap<String, String>> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			JSONObject contents = dataObject.getJSONObject("contents");
			Object entry = contents.get("entry");
			
			HashMap<String, String> h = null;
			
			if(entry instanceof JSONArray){
				
				JSONArray jobs = (JSONArray) entry;
				for (int i = 0; i < jobs.length(); i++) {
					JSONObject js = jobs.getJSONObject(i);
					h = new HashMap<String, String>();
					
					for (int k = 0; k < js.names().length(); k++) {
						h.put(js.names().getString(k), js.getString(js.names().getString(k)));
					}
					
					arr.add(h);
				}
				
			}else{
				JSONObject js2 = (JSONObject) entry;
				h = new HashMap<String, String>();
				
				for (int j = 0; j < js2.names().length(); j++) {
					h.put(js2.names().getString(j), js2.getString(js2.names().getString(j)));
				}
				
				arr.add(h);
			}
			
		} catch (JSONException e) {
			Log.e("SocietyAllParser", "Error ", e);
		}
		return arr;
	}


}
