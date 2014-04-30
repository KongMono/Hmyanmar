package com.tdcm.hmyanmar.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

public class UtilDatabase {

	public static List<HashMap<String, Object>> getDataListInDB(Context context){
		
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		list = DatabasePoolAdapter.getInstance(context).getMyFavoriteList("0");
		
		return list;
		
	}
	
	public static Boolean insert(Context context, String content_id, String user_id, String title, String thumb, String share_url, String view, String numorder) {
		
		if(!DatabasePoolAdapter.getInstance(context).isInDB(user_id, content_id)&&DatabasePoolAdapter.getInstance(context).getMyFavoriteList("0").size()<=20){
			
			HashMap<String, String> obj = new HashMap<String, String>();
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_ID, content_id);
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_USER_ID, user_id);
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_TITLE, title);
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_THUMB, thumb);
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_SHARE_URL, share_url);
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_VIEW, view);
	        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_ORDER, numorder);
	        return DatabasePoolAdapter.getInstance(context).insert(DatabasePoolAdapter.TABLE_MYFAVORITE_NAME, obj);
	        
		}
		
		return false;
		
	}
	
	public static Boolean deleteDataInDB (Context context, String user_id, String key){
			
		return DatabasePoolAdapter.getInstance(context).delete(DatabasePoolAdapter.TABLE_MYFAVORITE_NAME, user_id, key);

	}
	
	public static Boolean updateDataInDB(Context context, String content_id, String user_id, String numorder) {
		
		HashMap<String, String> obj = new HashMap<String, String>();
        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_ID, content_id);
        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_USER_ID, user_id);
        obj.put(DatabasePoolAdapter.KEY_TABLE_MYFAVORITE_ORDER, numorder);
		
		return DatabasePoolAdapter.getInstance(context).update(DatabasePoolAdapter.TABLE_MYFAVORITE_NAME, obj);
		
	}
	
	public static Boolean isInDataBase(Context context, String content_id, String user_id){
		return DatabasePoolAdapter.getInstance(context).isInDB(user_id, content_id);
	}
	
	public static int getSizeInDataBase(Context context){
		return DatabasePoolAdapter.getInstance(context).getMyFavoriteList("0").size();
	}

}
