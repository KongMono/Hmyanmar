package com.tdcm.hmyanmar.Database;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import android.os.Environment;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tdcm.hmyanmar.R;
import com.truelife.mobile.android.util.file.CacheObj;

public class DatabasePoolAdapter {

	private final Context ctx;
	private CacheObj cache = null;
	private String fObject_config_path = "";
	private SQLiteDatabase sqlconnection = null;
	
	private String DATABASE_LOCATION = "";
	private static final int DATABASE_VERSION = 7;
	
	public static final String ORDER_BY_ASC = "ASC";
	public static final String ORDER_BY_DESC = "DESC";
	
	public static final String TABLE_MYFAVORITE_NAME = "table_myfavorite";
	public static final String KEY_TABLE_MYFAVORITE_ID = "content_id";
	public static final String KEY_TABLE_MYFAVORITE_USER_ID = "userid";
	public static final String KEY_TABLE_MYFAVORITE_TITLE = "channel_name";
	public static final String KEY_TABLE_MYFAVORITE_THUMB = "thumbnail";
	public static final String KEY_TABLE_MYFAVORITE_SHARE_URL = "share_url";
	public static final String KEY_TABLE_MYFAVORITE_VIEW = "view";
	public static final String KEY_TABLE_MYFAVORITE_ORDER = "number";
	
	static private DatabasePoolAdapter instance;
	
	static public DatabasePoolAdapter getInstance(Context ctx) {
		
		if(instance == null) {
			instance = new DatabasePoolAdapter(ctx);
			instance.open();
		} else {
			if(!instance.isOpen()) {
				instance.open();
			}
		}
		return instance;
	}
	
	public DatabasePoolAdapter(Context ctx) {
		
		this.ctx = ctx;
	}
	
	public void close() {
		sqlconnection.close();
	}
	
	private DatabasePoolAdapter open() throws SQLException {
		
		cache = new CacheObj(ctx, ctx.getString(R.string.cache_name));
		fObject_config_path = Environment.getExternalStorageDirectory() + "/" + cache.getcacheId() + "/cache/database";
		File f = new File(fObject_config_path);
		if(!f.isDirectory()) {
			f.mkdirs();
		}
		
		DATABASE_LOCATION = fObject_config_path + File.separator + ctx.getString(R.string.database_name);
		f= new File(DATABASE_LOCATION);
		sqlconnection = SQLiteDatabase.openOrCreateDatabase(f, null);
		
		sqlconnection.execSQL(
				"CREATE TABLE IF NOT EXISTS " + TABLE_MYFAVORITE_NAME + " ("
				+ KEY_TABLE_MYFAVORITE_ID + " varchar(20),"
				+ KEY_TABLE_MYFAVORITE_USER_ID + " varchar(20),"
				+ KEY_TABLE_MYFAVORITE_TITLE + " text,"
				+ KEY_TABLE_MYFAVORITE_THUMB + " text,"
				+ KEY_TABLE_MYFAVORITE_SHARE_URL + " text,"
				+ KEY_TABLE_MYFAVORITE_VIEW + " text,"
				+ KEY_TABLE_MYFAVORITE_ORDER + " INTEGER,"
				+ " PRIMARY KEY (" + KEY_TABLE_MYFAVORITE_ID + ", " + KEY_TABLE_MYFAVORITE_USER_ID + ")"
				+ ");"
			);
		
		return this;
	}
	
	public boolean isOpen() {
		return sqlconnection.isOpen();
	}
	
	public boolean insert(String table_name, HashMap<String, String> value) {
		
		ContentValues initialValues = new ContentValues();
		if(table_name.equalsIgnoreCase(TABLE_MYFAVORITE_NAME)) {
			
			initialValues.put(KEY_TABLE_MYFAVORITE_ID, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_ID)));
			initialValues.put(KEY_TABLE_MYFAVORITE_USER_ID, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_USER_ID)));
			initialValues.put(KEY_TABLE_MYFAVORITE_TITLE, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_TITLE)));
			initialValues.put(KEY_TABLE_MYFAVORITE_THUMB, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_THUMB)));
			initialValues.put(KEY_TABLE_MYFAVORITE_SHARE_URL, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_SHARE_URL)));
			initialValues.put(KEY_TABLE_MYFAVORITE_VIEW, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_VIEW)));
			initialValues.put(KEY_TABLE_MYFAVORITE_ORDER, Integer.valueOf(String.valueOf(value.get(KEY_TABLE_MYFAVORITE_ORDER))));
			return sqlconnection.insert(TABLE_MYFAVORITE_NAME, null, initialValues) > 0;
		}
		return false;
	}
	
	public Boolean isInDB(String userid,String key){
    	String SQL = "";
        String ORDER_BY = "";
        Cursor c = null;
        SQL = "SELECT * FROM " + TABLE_MYFAVORITE_NAME + " WHERE " + KEY_TABLE_MYFAVORITE_USER_ID + "='" + userid + "' AND " + KEY_TABLE_MYFAVORITE_ID + "='" + key +"'";
        c = sqlconnection.rawQuery(SQL, null);
        return c.getCount()>0;
    }

	
	public boolean update(String table_name, HashMap<String, String> value) {
		
		ContentValues initialValues = new ContentValues();
		if(table_name.equalsIgnoreCase(TABLE_MYFAVORITE_NAME)) {
			
//			initialValues.put(KEY_TABLE_MYFAVORITE_ID, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_ID)));
//			initialValues.put(KEY_TABLE_MYFAVORITE_USER_ID, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_USER_ID)));
//			initialValues.put(KEY_TABLE_MYFAVORITE_TITLE, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_TITLE)));
//			initialValues.put(KEY_TABLE_MYFAVORITE_THUMB, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_THUMB)));
//			initialValues.put(KEY_TABLE_MYFAVORITE_SHARE_URL, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_SHARE_URL)));
//			initialValues.put(KEY_TABLE_MYFAVORITE_VIEW, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_VIEW)));
			initialValues.put(KEY_TABLE_MYFAVORITE_ORDER, String.valueOf(value.get(KEY_TABLE_MYFAVORITE_ORDER)));
			return sqlconnection.update(TABLE_MYFAVORITE_NAME, initialValues, KEY_TABLE_MYFAVORITE_ID + " = ? AND "+KEY_TABLE_MYFAVORITE_USER_ID+" = ? ", new String[]{String.valueOf(value.get(KEY_TABLE_MYFAVORITE_ID)) , String.valueOf(value.get(KEY_TABLE_MYFAVORITE_USER_ID))}) > 0;
		}
		return false;
	}
	
	public boolean delete(String table_name, String userid, String key) {
		
		if(key != null) {
			
			if(table_name.equalsIgnoreCase(TABLE_MYFAVORITE_NAME)) {
				return sqlconnection.delete(TABLE_MYFAVORITE_NAME, KEY_TABLE_MYFAVORITE_ID + "='" + key + "' AND " + KEY_TABLE_MYFAVORITE_USER_ID + "='" + userid + "'", null) > 0;
			}
		} else if(userid != null){
			if(table_name.equalsIgnoreCase(TABLE_MYFAVORITE_NAME)) {
				return sqlconnection.delete(TABLE_MYFAVORITE_NAME, KEY_TABLE_MYFAVORITE_USER_ID + "='" + userid + "'", null) > 0;
			}
		}else {
			
			if(table_name.equalsIgnoreCase(TABLE_MYFAVORITE_NAME)) {
				return sqlconnection.delete(TABLE_MYFAVORITE_NAME, null, null) > 0;
			}
		}
		return false;
	}
	
	public List<HashMap<String, Object>> getMyFavoriteList(String userid) {
		
		String SQL = "";
		String ORDER_BY = ORDER_BY_ASC;
		
		Cursor c = null;
		String table_name = TABLE_MYFAVORITE_NAME;
		List<HashMap<String, Object>> result = null;
		
		SQL = "SELECT * FROM " + table_name + " WHERE "+ KEY_TABLE_MYFAVORITE_USER_ID + "='" + userid + "'";
//		ORDER BY " + KEY_TABLE_MYFAVORITE_ORDER + " " + ORDER_BY;
		c = sqlconnection.rawQuery(SQL, null);
		result = (List<HashMap<String, Object>>) convertCursor2List(c);
		c.close();
		
		if(result == null) {
			return (new ArrayList<HashMap<String, Object>>());
		} else {
			return result;
		}
	}
	
	private List<HashMap<String, Object>> convertCursor2List(Cursor c) {
		
		List<HashMap<String, Object>> result = null;
		if(c != null) {
			
			result = new ArrayList<HashMap<String, Object>>();
			if(c.moveToFirst()) {
				
				while(!c.isAfterLast()) {
					
					String[] columnName = c.getColumnNames();
					HashMap<String, Object> hm = new HashMap<String, Object>();
					for(int j=0; j<columnName.length; j++) {
						hm.put(columnName[j], c.getString(c.getColumnIndexOrThrow(columnName[j])));
					}

					result.add(hm);
					c.moveToNext();
				}
			}
		}
		return result;
	}
	
}
