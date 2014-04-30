package com.tdcm.hmyanmar.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tdcm.hmyanmar.Adapter.ListHotShotAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Json.HotShotParser;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.TabTVSocietyActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SocietyHotShotView extends FrameLayout  implements OnItemClickListener{

	private Activity activity;
	private Context context;
	private ListHotShotAdapter newsAdapter;
	private API api;
	private AQuery aq;
	public static LinearLayout layout = null;
	private ListNewsEntry entry = new ListNewsEntry();
	private HotShotParser hotPaser = new HotShotParser();
	private String strApi;
	private List<HashMap<String, String>> arrayList = new Vector<HashMap<String,String>>();
	private Handler handler = new Handler();
	int offset = 0;
	int limit = 20;
	int count = 0;
	private View socity_bottom;
	private Button socity_bt1,socity_bt2,socity_bt3;
	private ImageView socity_btnews,socity_btall,socity_bthot;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private TextView horizontal_scroll_list_title;
	
	public SocietyHotShotView(final Activity activity , final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setLayoutParams(new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		
		View v = (View) vi.inflate(R.layout.society_hotshot, null);
		addView(v);
		
		api = new API(context);
		aq = new AQuery(context);

		mPullRefreshGridView = (PullToRefreshGridView) v.findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		
		socity_bottom = (View) v.findViewById(R.id.socity_bottom);
		layout = (LinearLayout) v.findViewById(R.id.socity_home_l);
		horizontal_scroll_list_title = (TextView) v.findViewById(R.id.horizontal_scroll_list_title);
		horizontal_scroll_list_title.setVisibility(View.GONE);
		
		if(Boolean.valueOf(context.getString(R.string.istablet))){
			socity_bottom.setVisibility(View.GONE);
			
			socity_btnews = new ImageView(context);
			socity_btall = new ImageView(context);
			socity_bthot = new ImageView(context);
			
			socity_btnews.setBackgroundResource(R.drawable.btn_tvsociety01);
			socity_btall.setBackgroundResource(R.drawable.btn_tvsociety02);
			socity_bthot.setBackgroundResource(R.drawable.btn_tvsociety03_inactive);
			
			socity_btnews.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyNewsView();
				}
			});
			
			socity_btall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyAllView();
				}
			});
	
			Util.layout.setVisibility(View.VISIBLE);
			Util.layout.removeAllViews();
			Util.layout.addView(socity_btnews);
			Util.layout.addView(socity_btall);
			Util.layout.addView(socity_bthot);
			
		}else{
			
			mGridView.setNumColumns(2);
			mGridView.setVerticalSpacing(2);
			mGridView.setHorizontalSpacing(2);
			
			socity_bt1 = (Button) socity_bottom.findViewById(R.id.socity_bt11);
			socity_bt2 = (Button) socity_bottom.findViewById(R.id.socity_bt22);
			socity_bt3 = (Button) socity_bottom.findViewById(R.id.socity_bt33);
			
			socity_bt1.setTypeface(Util.getPadaukFont(context));
			socity_bt2.setTypeface(Util.getPadaukFont(context));
			socity_bt3.setTypeface(Util.getPadaukFont(context));
			
			socity_bt3.setEnabled(false);
			socity_bt3.setBackgroundResource(R.drawable.bg_button_base_organge);
			socity_bt3.setTextColor(context.getResources().getColor(R.color.white));
			
			socity_bt1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyNewsView();
				}
			});
	
			socity_bt2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyAllView();
				}
			});
		}
		
		callFirstApi(layout, context);
		
		if (arrayList.isEmpty()) {
			Util.refreshList.setVisibility(View.VISIBLE);
			Util.isError = true;
		}else{
			Util.refreshList.setVisibility(View.GONE);
			Util.isError = false;
		}
		
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//				Toast.makeText(context, "Pull Down!", Toast.LENGTH_SHORT).show();
//				new GetDataTask().execute();
				callFirstApi(layout, context);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//				Toast.makeText(PullToRefreshGridActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
//				mPullRefreshGridView.onRefreshComplete();
			}

		});
		
	}
	
	private void callFirstApi(final LinearLayout layout,final Context context) {
		limit = 20;
		strApi = api.getHotShotSocietyData(limit);
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				List<HashMap<String, String>> addlist = new Vector<HashMap<String,String>>();
				try {
					arrayList.clear();
					addlist = hotPaser.getData(json);
					arrayList.addAll(addlist);
					newsAdapter = new ListHotShotAdapter(context, arrayList ,  R.layout.horizontal_list_items);
					newsAdapter.notifyDataSetChanged();
					mGridView.setAdapter(newsAdapter);
					if (arrayList.isEmpty()) {
						Util.refreshList.setVisibility(View.VISIBLE);
						Util.isError = true;
					}else{
						Util.refreshList.setVisibility(View.GONE);
						Util.isError = false;
					}
					mPullRefreshGridView.onRefreshComplete();
					
					if(Boolean.valueOf(context.getString(R.string.istablet))){
						horizontal_scroll_list_title.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
				}
			}
		});
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, Void> {
		List<HashMap<String, String>> addlist = new Vector<HashMap<String,String>>();
		@Override
		protected Void doInBackground(Void... params) {
		
			if (isCancelled()) {
				return null;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				limit+=20;
				strApi = api.getHotShotSocietyData(limit);
				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
					
					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						try {
							addlist = hotPaser.getData(json);
							if (addlist.isEmpty()) {
								mPullRefreshGridView.onRefreshComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										arrayList.clear();
										for(int i=0;i<addlist.size();i++){
											arrayList.add(addlist.get(i));
										}
//										for (HashMap<String, String> a : arrayList) {
//											addlist.remove(a);
//										}
										
//										arrayList.addAll(addlist);
										newsAdapter.notifyDataSetChanged();
									}
								});
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				});
			} catch (Exception e) {
				e.getMessage();
			}
		
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			newsAdapter.notifyDataSetChanged();
			mPullRefreshGridView.onRefreshComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
