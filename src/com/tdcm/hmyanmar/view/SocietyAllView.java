package com.tdcm.hmyanmar.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tdcm.hmyanmar.Adapter.ListAllTVSocietyAdapter;
import com.tdcm.hmyanmar.Adapter.ListTVSocietyAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Json.SocietyAllParser;
import com.tdcm.hmyanmar.Json.SocietyHomeParser;
import com.tdcm.hmyanmar.Util.PullAndLoadListView;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.Util.CustomsListView.OnRefreshListener;
import com.tdcm.hmyanmar.Util.PullAndLoadListView.OnLoadMoreListener;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.TabTVSocietyActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SocietyAllView extends FrameLayout{

	private API api;
	private AQuery aq;
	private String strApi;
	private List<List<HashMap<String, String>>> arrayList = new Vector<List<HashMap<String,String>>>();
	private LinearLayout layout;
	private PullAndLoadListView list;
	private SocietyAllParser parser;
	private ListAllTVSocietyAdapter homeAdapter;
	private View socity_bottom;
	private Button socity_bt1,socity_bt2,socity_bt3;
	private ImageView socity_btnews,socity_btall,socity_bthot;
	private Activity activity;
	private Context context;
	private Handler handler = new Handler();
	private TextView horizontal_scroll_list_title;
	
	public SocietyAllView(final Context context,final Activity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.activity = activity;
		setLayoutParams(new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = (View) vi.inflate(R.layout.society_home, null);
		addView(v);

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
			socity_btall.setBackgroundResource(R.drawable.btn_tvsociety02_inactive);
			socity_bthot.setBackgroundResource(R.drawable.btn_tvsociety03);
			
			socity_btnews.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyNewsView();
				}
			});
	
			socity_bthot.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyHotShotView();
				}
			});
			Util.layout.setVisibility(View.VISIBLE);
			Util.layout.removeAllViews();
			Util.layout.addView(socity_btnews);
			Util.layout.addView(socity_btall);
			Util.layout.addView(socity_bthot);
			
		}else{
			socity_bottom.setVisibility(View.VISIBLE);
			
			socity_bt1 = (Button) socity_bottom.findViewById(R.id.socity_bt11);
			socity_bt2 = (Button) socity_bottom.findViewById(R.id.socity_bt22);
			socity_bt3 = (Button) socity_bottom.findViewById(R.id.socity_bt33);
			
			socity_bt1.setTypeface(Util.getPadaukFont(context));
			socity_bt2.setTypeface(Util.getPadaukFont(context));
			socity_bt3.setTypeface(Util.getPadaukFont(context));
			
			socity_bt2.setEnabled(false);
			socity_bt2.setBackgroundResource(R.drawable.bg_button_base_organge);
			socity_bt2.setTextColor(context.getResources().getColor(R.color.white));
			
			socity_bt1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyNewsView();
				}
			});
	
			socity_bt3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyHotShotView();
				}
			});
		}
		
		arrayList = new Vector<List<HashMap<String,String>>>();
		parser = new SocietyAllParser();
		api = new API(context);
		list = new PullAndLoadListView(context);
		list.setSmoothScrollbarEnabled(true);
//		list.setOnItemClickListener(this);
		aq = new AQuery(context);
		
		
		callFirstApi(0 ,20 ,layout, null, context);
		
		Util.refreshList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				callFirstApi(0 ,20 ,layout, null, context);
				return false;
			}
		});
		
		list.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				list.onLoadMoreComplete();
				list.getLoadMoreView().setVisibility(GONE);
			}
		});
		
		list.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				list.onRefreshComplete();
				new RefreshDataTask().execute();
			}

		});
		
		if (arrayList.isEmpty()) {
			Util.refreshList.setVisibility(View.VISIBLE);
			Util.isError = true;
		}else{
			layout.addView(list);
			Util.refreshList.setVisibility(View.GONE);
			Util.isError = false;
		}
	}
	
	private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<ListNewsEntry> addlist = new ArrayList<ListNewsEntry>();
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
				strApi = api.getAllTVSocietyData(0 , 20);
				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
					
					List<List<HashMap<String, String>>> addlist = new Vector<List<HashMap<String,String>>>();
					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						try {
							addlist = parser.getData(json);
							if (addlist.isEmpty()) {
								list.onRefreshComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										arrayList.clear();
										arrayList.addAll(addlist);
										homeAdapter.notifyDataSetChanged();
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
			homeAdapter.notifyDataSetChanged();
			list.onRefreshComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			list.onRefreshComplete();
		}
	}
	
	private void callFirstApi(int offset, int limit , final LinearLayout layout, final ProgressBar pgb,final Context context) {
		strApi = api.getAllTVSocietyData(offset , limit);
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				List<List<HashMap<String, String>>> addlist = new Vector<List<HashMap<String,String>>>();
				try {
					arrayList.clear();
					addlist = parser.getData(json);
					arrayList.addAll(addlist);
					
					Log.e("arrayList", String.valueOf(arrayList.size()));
					
					homeAdapter = new ListAllTVSocietyAdapter(context, arrayList , parser.getDesc());
					homeAdapter.notifyDataSetChanged();
					list.setAdapter(homeAdapter);
					if (arrayList.isEmpty()) {
						Util.refreshList.setVisibility(View.VISIBLE);
						Util.isError = true;
					}else{
						layout.addView(list);
						Util.refreshList.setVisibility(View.GONE);
						Util.isError = false;
					}
					
					list.onRefreshComplete();
					if(Boolean.valueOf(context.getString(R.string.istablet))){
						horizontal_scroll_list_title.setVisibility(View.VISIBLE);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
