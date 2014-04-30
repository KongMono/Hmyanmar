package com.tdcm.hmyanmar;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tdcm.hmyanmar.Adapter.AddFavoriteAdapter;
import com.tdcm.hmyanmar.Adapter.ChannelTVAdapter;
import com.tdcm.hmyanmar.Database.UtilDatabase;
import com.tdcm.hmyanmar.Json.JsonParser;
import com.tdcm.hmyanmar.Util.Util;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.TextView;

public class AddFavoriteActivity extends CoreActivity{
	
	AQuery aq;
	TextView header, addfavorite_text, addfavorite_textnum, header_title_right;
	GridView addfavorite_gridview;
//	List<HashMap<String, Object>> favoriteList;
	List<HashMap<String, Object>> itemsList;
	
	JsonParser json;
	int refresh_count = 3;
	
	AddFavoriteAdapter adapter;
	String api;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_addfavorite);
		
		init();
		
		if(Boolean.valueOf(getString(R.string.istablet))){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
		
		api = getString(R.string.api_getlist) + "1";
		aq.ajax(api, JSONObject.class, AddFavoriteActivity.this, "getListCallback");
		
	}
	
	private void init() {
		
		aq = new AQuery(this);
		json = new JsonParser();
		
		header = (TextView) findViewById(R.id.header_title);
		header_title_right = (TextView) findViewById(R.id.header_title_right);
		addfavorite_text = (TextView) findViewById(R.id.addfavorite_text);
		addfavorite_textnum = (TextView) findViewById(R.id.addfavorite_textnum);
		addfavorite_gridview = (GridView) findViewById(R.id.addfavorite_gridview);
		
		header.setText(getString(R.string.addfavoriteheader));
		header.setTypeface(Util.getPadaukBookFont(this));
		header_title_right.setVisibility(View.VISIBLE);
		header_title_right.setText(getString(R.string.done));
		header_title_right.setTextColor(Color.parseColor(getString(R.color.orange)));
		header_title_right.setTypeface(Util.getPadaukBookFont(this));
		addfavorite_text.setTypeface(Util.getPadaukFont(this));
		addfavorite_textnum.setTypeface(Util.getPadaukFont(this));
		
		header_title_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(adapter != null){
					List<HashMap<String, Object>> favoriteList = adapter.getNewFavoriteList();
					List<HashMap<String, Object>> deleteList = adapter.getDeleteList();
//					UtilDatabase.deleteDataInDB(AddFavoriteActivity.this, "0", null);
					for(int i=0;i<deleteList.size();i++){
						HashMap<String, Object> item = deleteList.get(i);
						UtilDatabase.deleteDataInDB(AddFavoriteActivity.this, "0", String.valueOf(item.get("content_id")));
					}
					for(int i=0;i<favoriteList.size();i++){
						HashMap<String, Object> item = favoriteList.get(i);
						UtilDatabase.insert(AddFavoriteActivity.this, String.valueOf(item.get("content_id")), "0", String.valueOf(item.get("channel_name")), String.valueOf(item.get("thumbnail")), String.valueOf(item.get("share_url")), String.valueOf(item.get("view")), "0");
					}
				}
				onClickExit(v);
			}
		});
	}
	
	public void getListCallback(String url, JSONObject json, AjaxStatus status){
		if(json != null){
			refresh_count = 3;		
			try {
				JSONObject response = json.getJSONObject("response");
				HashMap<String, Object> result = this.json.parserList(response.getString("data"));
				if(result.get("code").equals("200")){
					itemsList = (List<HashMap<String, Object>>) result.get("data");
					adapter = new AddFavoriteAdapter(this, itemsList, addfavorite_textnum);
					addfavorite_gridview.setAdapter(adapter);
					Util.refreshList.setVisibility(View.GONE);
					Util.isError = false;
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(refresh_count>0){
			refresh_count -= 1;
			Log.e("addFavorite", "addFavorite "+refresh_count);
			aq.ajax(api, JSONObject.class, AddFavoriteActivity.this, "getListCallback");
		}else{
			refresh_count = 3;
		}
	}

}
