package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.EpisodeEntry;

import android.util.Log;

public class EpisodeParser {
	private ArrayList<EpisodeEntry> arr;
	private JSONObject jObject;
	private String str = "";
	EpisodeEntry item;

	public EpisodeParser() {
		arr = new ArrayList<EpisodeEntry>();
	}

	public ArrayList<EpisodeEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			JSONObject itemObject = dataObject.getJSONObject("items");
			Object itemObj = itemObject.get("item");
			if(itemObj instanceof JSONArray){
				
				JSONArray itemsjsonArray= (JSONArray) itemObj;
				
				for (int i = 0; i < itemsjsonArray.length(); i++) {
					item = new EpisodeEntry();
					jObject = itemsjsonArray.getJSONObject(i);
					item.setId(jObject.getInt("id"));
					item.setThumbnail(jObject.getString("thumbnail"));
					item.setTitle(jObject.getString("title"));
					arr.add(item);
				}
			}else{
				jObject = (JSONObject) itemObj;
				item = new EpisodeEntry();
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


}
