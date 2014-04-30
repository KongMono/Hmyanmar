package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import com.tdcm.hmyanmar.Dataset.NovelEntry;

import android.util.Log;

public class NovelParser {
	private ArrayList<NovelEntry> arr;
	private JSONObject jObject;
	private String str = "";
	NovelEntry item;

	public NovelParser() {
		arr = new ArrayList<NovelEntry>();
	}

	public ArrayList<NovelEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {
			item = new NovelEntry();
			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			
			item.setSection(dataObject.getString("section"));
			item.setTotalPage(dataObject.getInt("total_page"));
			item.setNowPage(dataObject.getInt("now_page"));
			
			JSONObject itemObject = dataObject.getJSONObject("items");
			item.setDescription(itemObject.getString("description"));
			item.setView(itemObject.getInt("views"));
			
			arr.add(item);
			
		} catch (JSONException e) {
			Log.e("NewsPaser", "Error ", e);
		}
		return arr;
	}
	
	public int getTotalPage(JSONObject jsonObject) {
		int total = 0;
		try {
			jObject = jsonObject;
			
			JSONObject responseObject = jObject.getJSONObject("response");
			JSONObject dataObject = responseObject.getJSONObject("data");
			total = dataObject.getInt("total_page");
		} catch (JSONException e) {
			Log.e("NewsPaser", "Error ", e);
		}
		return total;
	}



}
