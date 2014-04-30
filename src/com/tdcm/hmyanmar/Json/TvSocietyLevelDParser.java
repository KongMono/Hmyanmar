package com.tdcm.hmyanmar.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TvSocietyLevelDParser {
	
	public HashMap<String, Object> getInfo(JSONObject json) {
		
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("code", "200");
		info.put("description", "success");
		try{
			
			JSONArray jname = json.names();
			for(int i=0;i<jname.length();i++){
				info.put(jname.getString(i), json.get(jname.getString(i)));
			}
			
		}catch(JSONException e){
			info = new HashMap<String, Object>();
			info.put("code", "500");
			info.put("description", e.getMessage());
		}
		return info;
		
	}
	
	public List<HashMap<String, Object>> getItemsList(JSONArray json){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
//		try{
			for(int i=0;i<json.length();i++){
				try{
					JSONObject jItem = json.getJSONObject(i);
					HashMap<String, Object> item = new HashMap<String, Object>();
					JSONArray jname = jItem.names();
					for(int j=0;j<jname.length();j++){
						item.put(jname.getString(j), jItem.get(jname.getString(j)));
					}
					list.add(item);
				}catch(JSONException e){
					
				}
			}
//		}catch(JSONException e){
//			list = null;
//		}
		return list;
	}

}
