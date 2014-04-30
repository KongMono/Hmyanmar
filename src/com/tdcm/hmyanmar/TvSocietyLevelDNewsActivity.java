package com.tdcm.hmyanmar;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Json.TvSocietyLevelDParser;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TvSocietyLevelDNewsActivity extends CoreActivity{
	
	private TextView header_title, leveld_news_title, leveld_news_des;
	private ImageView leveld_news_image;
	
	private AQuery aq;
	private Bundle bundle;
	private TvSocietyLevelDParser jsonParser;
	private GoogleAnalyticsTracker tracker;
	
	private String header, url, thumbnail;
	private HashMap<String, Object> item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tvsociety_leveld_news);
		
		if(MainActivity.isTablet){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
		
		bundle = getIntent().getExtras();
		header = bundle.getString("header");
		url = bundle.getString("url");
		thumbnail = bundle.getString("thumbnail");
		
		init();
		
		header_title.setText(header);
		aq.id(leveld_news_image).image(thumbnail);
		
		callAPI();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),header) + "D");
		Statistic.tracking(String.format(getString(R.string.stat),header) + "D", header);
	}
	
	private void init(){
		
		aq = new AQuery(this);
		jsonParser = new TvSocietyLevelDParser();
		
		header_title = (TextView) findViewById(R.id.header_title);
		leveld_news_title = (TextView) findViewById(R.id.leveld_news_title);
		leveld_news_image = (ImageView) findViewById(R.id.leveld_news_image);
		leveld_news_des = (TextView) findViewById(R.id.leveld_news_des);
		
		header_title.setTypeface(Util.getPadaukBookFont(this));
		leveld_news_title.setTypeface(Util.getPadaukFont(this));
		leveld_news_des.setTypeface(Util.getPadaukFont(this));
		
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
						item = jsonParser.getInfo(content.getJSONObject("entry"));
						if(String.valueOf(item.get("code")).equals("200")){
							leveld_news_title.setText(String.valueOf(item.get("content_title")));
							leveld_news_des.setText(String.valueOf(item.get("description")).replaceAll("&nbsp;|&ndash;|&ldquo;|&rdquo;", ""));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
	}

}
