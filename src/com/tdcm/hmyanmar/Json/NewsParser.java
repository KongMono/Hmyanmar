package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tdcm.hmyanmar.Dataset.ListNewsEntry;

import android.util.Log;

public class NewsParser {
	private ArrayList<ListNewsEntry> arr;
	private JSONObject jObject;
	private String str = "";
	ListNewsEntry item;

	public NewsParser() {
		arr = new ArrayList<ListNewsEntry>();
	}

	public ArrayList<ListNewsEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			JSONObject itemObject = dataObject.getJSONObject("items");
			JSONArray itemsjsonArray= itemObject.getJSONArray("item");
			
			for (int i = 0; i < itemsjsonArray.length(); i++) {
				item = new ListNewsEntry();
				jObject = itemsjsonArray.getJSONObject(i);
				item.setId(jObject.getInt("id"));
				item.setThumbnail(jObject.getString("thumbnail"));
				item.setTitle(jObject.getString("title"));
				item.setDescription(jObject.getString("description"));
				item.setShare_url(jObject.getString("share_url"));
				arr.add(item);
			}
			
			
		} catch (JSONException e) {
			Log.e("NewsPaser", "Error ", e);
		}
		return arr;
	}


}
