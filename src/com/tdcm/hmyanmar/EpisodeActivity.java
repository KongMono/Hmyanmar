package com.tdcm.hmyanmar;

import java.util.ArrayList;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Adapter.EpisodeAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.EpisodeEntry;
import com.tdcm.hmyanmar.Json.EpisodeParser;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;
import com.truelife.mobile.android.lib.DataUsage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EpisodeActivity extends FragmentActivity implements OnItemClickListener {
	
	private EpisodeAdapter episodeAdapter;
	private ListView list;
	private API api;
	private AQuery aq;
	private ProgressBar pgb;
	public static LinearLayout layout = null;
	private Bundle bundle;
	private EpisodeParser episodeParser = new EpisodeParser();
	private String strApi;
	private ArrayList<EpisodeEntry> arrayList = new ArrayList<EpisodeEntry>();
	int id = 0;
	String title = "";
	private TextView header_title;
	private GoogleAnalyticsTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activty_list_episode);
//		getActionBar().hide();

		init();
		bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		title = bundle.getString("title");
		
		header_title.setText(title);
		header_title.setTypeface(Util.getPadaukBookFont(this));
		
		callFirstApi(id,layout, pgb);
		Util.refreshList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				callFirstApi(id,layout, pgb);
				return false;
			}
		});
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),title) + "C");
		Statistic.tracking(String.format(getString(R.string.stat),title) + "C", title);
		
	}

	private void callFirstApi(int id,final LinearLayout layout, final ProgressBar pgb) {
		strApi = api.getApiEpisodeData(id);
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				ArrayList<EpisodeEntry> addlist = new ArrayList<EpisodeEntry>();
				try {
					arrayList.clear();
					addlist = episodeParser.getData(json);
					arrayList.addAll(addlist);
					episodeAdapter = new EpisodeAdapter(EpisodeActivity.this, arrayList);
					episodeAdapter.notifyDataSetChanged();
					list.setAdapter(episodeAdapter);
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					pgb.setVisibility(View.GONE);
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
//	private class LoadDataTask extends AsyncTask<Void, Void, Void> {
//		ArrayList<EpisodeEntry> addlist = new ArrayList<EpisodeEntry>();
//		@Override
//		protected Void doInBackground(Void... params) {
//		
//			if (isCancelled()) {
//				return null;
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			try {
//				strApi = api.getApiEpisodeData(id);
//				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
//					
//					@Override
//					public void callback(String url, JSONObject json, AjaxStatus status) {
//						try {
//							addlist = episodeParser.getData(json);
//							if (addlist.isEmpty()) {
//								list.onLoadMoreComplete();
//							}else{
//								handler.post(new Runnable() {
//									@Override
//									public void run() {
//										arrayList.addAll(addlist);
//										episodeAdapter.notifyDataSetChanged();
//										pgb.setVisibility(View.GONE);
//									}
//								});
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					
//				});
//			} catch (Exception e) {
//				e.getMessage();
//			}
//		
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(Void result) {
//			episodeAdapter.notifyDataSetChanged();
//			list.onLoadMoreComplete();
//			super.onPostExecute(result);
//		}
//		
//		@Override
//		protected void onCancelled() {
//			list.onLoadMoreComplete();
//		}
//	}
	
	

	private void init() {
		api = new API(this);
		list = (ListView)findViewById(R.id.list);
		list.setSmoothScrollbarEnabled(true);
		list.setDividerHeight(0);
		list.setOnItemClickListener(this);
		aq = new AQuery(this);
		Util.logo_cen.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				list.smoothScrollToPosition(0);
				return false;
			}
		});
		Util.catagory.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		
		pgb = new ProgressBar(this);
		
		header_title = (TextView) findViewById(R.id.header_title);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
		try {
			EpisodeEntry data = arrayList.get(position);
			Intent intent = new Intent(this, NovelActivity.class);
			intent.putExtra("id", data.getId());
			intent.putExtra("title", data.getTitle());
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DataUsage.checkOnResumeCheckDataUsage(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DataUsage.setActivityNameForOnPauseOnDestroyed(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		DataUsage.setActivityNameForOnPauseOnDestroyed(this);
	}

}
