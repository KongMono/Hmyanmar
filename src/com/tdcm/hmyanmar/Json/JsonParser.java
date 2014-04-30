package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
	
	public HashMap<String, Object> parserList(String data) {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			
			JSONObject jObject = new JSONObject(data);
			
			result.put("code", "200");
			result.put("description", "success");
			
			List<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
			
			JSONArray jentryarr = jObject.getJSONObject("contents").getJSONArray("entry");
			
			for(int i=0;i<jentryarr.length();i++){
				
				HashMap<String, Object> entry = new HashMap<String, Object>();
				JSONObject jentry = jentryarr.getJSONObject(i);
				JSONArray jname = jentry.names();
				for(int j=0;j<jname.length();j++){
					entry.put(jname.getString(j), jentry.get(jname.getString(j)));
				}
				datalist.add(entry);
				
			}
			result.put("data", datalist);
			
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			result = new HashMap<String, Object>();
			result.put("code", "500");
			result.put("description", e.getMessage());
		}
		
		return result;
		
	}
	
	public HashMap<String, Object> parserListCate(String data){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try{
			
			JSONObject jObject = new JSONObject(data);
			JSONArray jArrayitem = jObject.getJSONArray("item");
			
			result.put("code", "200");
			result.put("description", "success");
			
			List<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
			
			for(int i=0;i<jArrayitem.length();i++){
				JSONObject jitem = jArrayitem.getJSONObject(i);
				JSONArray jname = jitem.names();
				HashMap<String, Object> item = new HashMap<String, Object>();
				for(int j=0;j<jname.length();j++){
					item.put(jname.getString(j), jitem.get(jname.getString(j)));
				}
				datalist.add(item);
			}
			
			result.put("menu", datalist);
			
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			result = new HashMap<String, Object>();
			result.put("code", "500");
			result.put("description", e.getMessage());
		}
		
		return result;
		
	}
	
	public HashMap<String, Object> parserListAudio(String data) {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			
			JSONObject jObject = new JSONObject(data);
			
			result.put("code", "200");
			result.put("description", "success");
			
			List<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
			
			JSONArray jentryarr = jObject.getJSONObject("contents").getJSONArray("entry");
			
			for(int i=0;i<jentryarr.length();i++){
				
				HashMap<String, Object> entry = new HashMap<String, Object>();
				JSONObject jentry = jentryarr.getJSONObject(i);
				JSONArray jname = jentry.names();
				for(int j=0;j<jname.length();j++){
					Log.e("parser", jname.getString(j));
					if(jname.getString(j).equals("id")){
						entry.put("content_id", jentry.get(jname.getString(j)));
					}else if(jname.getString(j).equals("title")){
						entry.put("channel_name", jentry.get(jname.getString(j)));
					}else if(jname.getString(j).equals("thumbnail")){
						entry.put("thumbnail_app", jentry.get(jname.getString(j)));
						entry.put("thumbnail", jentry.get(jname.getString(j)));
					}else if(jname.getString(j).equals("categories")){
						entry.put("category", "audio");
					}else{
						entry.put(jname.getString(j), jentry.get(jname.getString(j)));
					}
				}
				datalist.add(entry);
				
			}
			result.put("data", datalist);
			
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			result = new HashMap<String, Object>();
			result.put("code", "500");
			result.put("description", e.getMessage());
		}
		
		return result;
		
	}
	
	public HashMap<String, Object> parserInfo (String data){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			
			JSONObject jObject = new JSONObject(data);
			JSONObject jentry = jObject.getJSONObject("content").getJSONObject("entry");
			JSONArray jname = jentry.names();
			
			result.put("code", "200");
			result.put("description", "success");
			
			for(int i=0;i<jname.length();i++){
				if(jname.getString(i).equals("stream")){
					
					JSONObject jstream = jentry.getJSONObject(jname.getString(i));
					JSONArray jnamestream = jstream.names();
					for(int j=0;j<jnamestream.length();j++){
						result.put("stream_"+jnamestream.getString(j), jstream.get(jnamestream.getString(j)));
					}
					
				} else if(jname.getString(i).equals("stream_live")){
					
					JSONObject jstream = jentry.getJSONObject(jname.getString(i));
					JSONArray jnamestream = jstream.names();
					for(int j=0;j<jnamestream.length();j++){
						result.put("live_"+jnamestream.getString(j), jstream.get(jnamestream.getString(j)));
					}
					
				} else {
					result.put(jname.getString(i), jentry.get(jname.getString(i)));
				}
			}
			
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			result = new HashMap<String, Object>();
			result.put("code", "500");
			result.put("description", e.getMessage());
		}
		
		return result;
		
	}

}
