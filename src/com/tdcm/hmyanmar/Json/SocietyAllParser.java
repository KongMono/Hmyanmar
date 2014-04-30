package com.tdcm.hmyanmar.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.EpisodeEntry;

import android.util.Log;

public class SocietyAllParser {
	private List<List<HashMap<String, String>>> arr;
	private List<HashMap<String, String>> desc;
	private JSONObject jObject;
	EpisodeEntry item;

	public SocietyAllParser() {
		arr = new Vector<List<HashMap<String,String>>>();
		desc = new Vector<HashMap<String,String>>();
	}

	public List<HashMap<String, String>> getDesc() {
		return desc;
	}
	
	public List<List<HashMap<String, String>>> getData(JSONObject jsonObject) {
		arr.clear();
		desc.clear();

		try {

			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			Object section = dataObject.get("section");
			
			HashMap<String, String> h = null;
			List<HashMap<String, String>> ls = null;
			
			
			if(section instanceof JSONArray){
				JSONArray jobs = (JSONArray) section;
				
				for (int i = 0; i < jobs.length(); i++) {
					
					ls = new Vector<HashMap<String,String>>();
					
					JSONObject js = jobs.getJSONObject(i);
					if(Integer.parseInt(String.valueOf(js.get("total")))>0){
						
						JSONObject items = js.getJSONObject("items");
						Object item = items.get("item");
					
						h = new HashMap<String, String>();
						h.put("channel_name", String.valueOf(js.get("channel_name")));
						h.put("total", String.valueOf(js.get("total")));
						desc.add(h);
						
						if(item instanceof JSONArray){
							
							JSONArray jobs2 = (JSONArray) item;
							for (int j = 0; j < jobs2.length(); j++) {
								JSONObject js2 = jobs2.getJSONObject(j);
								h = new HashMap<String, String>();
								
								for (int k = 0; k < js2.names().length(); k++) {
									h.put(js2.names().getString(k), js2.getString(js2.names().getString(k)));
								}
	
								ls.add(h);
							}
							
						}else{
							JSONObject js2 = (JSONObject) item;
							h = new HashMap<String, String>();
							
							for (int j = 0; j < js2.names().length(); j++) {
								h.put(js2.names().getString(j), js2.getString(js2.names().getString(j)));
							}
							
							ls.add(h);
						}
						
						
						Log.e("ls"+i, String.valueOf(ls.size()));
						
						arr.add(ls);
					}
				}
			}else{
//				JSONObject js = (JSONObject) entry;
//				h = new HashMap<String, String>();
//				
//				for (int j = 0; j < js.names().length(); j++) {
//					h.put(js.names().getString(j), js.getString(js.names().getString(j)));
//				}
//				
//				arr.add(h);
			}
			
		} catch (JSONException e) {
			Log.e("SocietyAllParser", "Error ", e);
		}
		return arr;
	}


}
