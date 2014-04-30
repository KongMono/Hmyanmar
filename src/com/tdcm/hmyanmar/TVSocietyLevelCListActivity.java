package com.tdcm.hmyanmar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Adapter.TvSocietyLevelDListAdapter;
import com.tdcm.hmyanmar.Json.TvSocietyLevelDParser;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TVSocietyLevelCListActivity extends CoreActivity{
	
	private TextView header_title;
	private ListView list;
	
	private Bundle bundle;
	private String url,header,content_id;
	private List<HashMap<String,Object>> items;
	private GoogleAnalyticsTracker tracker;
	
	private AQuery aq;
	private Handler handler = new Handler();
	private TvSocietyLevelDListAdapter adapter;
	private TvSocietyLevelDParser jsonParser = new TvSocietyLevelDParser();
	int refresh_count = 3;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activty_list_episode);
		
		bundle =  getIntent().getExtras();
		url = bundle.getString("url");
		header = bundle.getString("header");
		content_id = bundle.getString("content_id");
		
		init();
		callAPI();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),header) + "C");
		Statistic.tracking(String.format(getString(R.string.stat),header) + "C", header);
		
	}
	
	private void init(){
		
		aq = new AQuery(this);
		
		header_title = (TextView) findViewById(R.id.header_title);
		list = (ListView) findViewById(R.id.list);
		
		header_title.setTypeface(Util.getPadaukBookFont(this));
		list.setDividerHeight(0);
		
		header_title.setText(header);
		
	}
	
	private void callAPI(){
		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
			@Override
			public void callback(String url, JSONObject object,	AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				if(object!=null){
					try {
						JSONObject content = object.getJSONObject("content");
						JSONObject album = content.getJSONObject("album");
						
						Object item = album.get("item");
						if(item instanceof JSONArray){
							items = jsonParser.getItemsList((JSONArray)item);
						}else{
							HashMap<String, Object> temp = jsonParser.getInfo((JSONObject)item);
							items = new ArrayList<HashMap<String,Object>>();
							items.add(temp);
						}
						
						if(items!=null && items.size()>0){
							adapter = new TvSocietyLevelDListAdapter(TVSocietyLevelCListActivity.this, items, header,false);
							list.setAdapter(adapter);
							list.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									HashMap<String, Object> item = adapter.getItem(arg2);
									Intent intent = new Intent(TVSocietyLevelCListActivity.this, TVSocietyLevelDGalleryActivity.class);
									intent.putExtra("url", String.format(getString(R.string.api_getdramagallery), content_id)+String.valueOf(item.get("album_id")));
									intent.putExtra("header", header);
									startActivity(intent);
								}
							});
						}else{
							showDialodAndExit();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						showDialodAndExit();
					}
				}else{
					if(refresh_count>0){
						refresh_count -= 1;
						callAPI();
					}
				}
			}
		});
	}
	
	private void showDialodAndExit(){
		Builder builder = new Builder(this);
		builder.setTitle("");
		builder.setMessage(R.string.alerttextandexit);
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onClickExit(getCurrentFocus());
			}
		});
		builder.show();
	}

}
