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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tdcm.hmyanmar.Adapter.ListTVSocietyAdapter;
import com.tdcm.hmyanmar.Adapter.ListTvSocietyHomeAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Json.SocietyHomeParser;
import com.tdcm.hmyanmar.Util.PullAndLoadListView;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.TabTVSocietyActivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SocietyHomeView extends FrameLayout{

	private Activity activity;
	private API api;
	private AQuery aq;
	private ProgressBar pgb;
	private String strApi;
	private List<HashMap<String, String>> arrayList;
	private LinearLayout layout;
	private SocietyHomeParser parser;
	private ListTvSocietyHomeAdapter homeAdapter;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private View socity_bottom;
	private Button socity_bt1,socity_bt2,socity_bt3;
	private ImageView socity_btnews,socity_btall,socity_bthot;
	private TextView horizontal_scroll_list_title;
	
	public SocietyHomeView(final Activity activity,final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setLayoutParams(new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = (View) vi.inflate(R.layout.society_home_all, null);
		addView(v);
		
		layout = (LinearLayout) v.findViewById(R.id.socity_home_l);
		socity_bottom = (View) v.findViewById(R.id.socity_bottom);
		horizontal_scroll_list_title = (TextView) v.findViewById(R.id.horizontal_scroll_list_title);
		horizontal_scroll_list_title.setVisibility(View.INVISIBLE);
		
		arrayList = new Vector<HashMap<String,String>>();
		parser = new SocietyHomeParser();
		api = new API(context);
		mPullRefreshGridView = (PullToRefreshGridView) v.findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		aq = new AQuery(context);
		
		if(Boolean.valueOf(context.getString(R.string.istablet))){
			socity_bottom.setVisibility(View.GONE);
			
			socity_btnews = new ImageView(context);
			socity_btall = new ImageView(context);
			socity_bthot = new ImageView(context);
			
			socity_btnews.setBackgroundResource(R.drawable.btn_tvsociety01);
			socity_btall.setBackgroundResource(R.drawable.btn_tvsociety02);
			socity_bthot.setBackgroundResource(R.drawable.btn_tvsociety03);
			
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
			
			mGridView.setNumColumns(2);
			mGridView.setVerticalSpacing(2);
			mGridView.setHorizontalSpacing(2);
			
			socity_bt1 = (Button) socity_bottom.findViewById(R.id.socity_bt11);
			socity_bt2 = (Button) socity_bottom.findViewById(R.id.socity_bt22);
			socity_bt3 = (Button) socity_bottom.findViewById(R.id.socity_bt33);
			
			socity_bt1.setTypeface(Util.getPadaukFont(context));
			socity_bt2.setTypeface(Util.getPadaukFont(context));
			socity_bt3.setTypeface(Util.getPadaukFont(context));
	
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
	
			socity_bt3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TabTVSocietyActivity)activity).toSocietyHotShotView();
				}
			});
		}
		
		callFirstApi(layout, pgb, context);
		
		if (arrayList.isEmpty()) {
			Util.refreshList.setVisibility(View.VISIBLE);
			Util.isError = true;
		}else{
			Util.refreshList.setVisibility(View.GONE);
			Util.isError = false;
		}
		
		mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
		
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				callFirstApi(layout, pgb, context);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				mPullRefreshGridView.onRefreshComplete();
			}

		});
	}
	
	private void callFirstApi(final LinearLayout layout, final ProgressBar pgb,final Context context) {
		strApi = api.getHomeTVSocietyData();
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				List<HashMap<String, String>> addlist = new Vector<HashMap<String,String>>();
				try {
					arrayList = new Vector<HashMap<String,String>>();
					addlist = parser.getData(json);
					arrayList.addAll(addlist);
					
					Log.e("arrayList", String.valueOf(arrayList.size()));
					
					homeAdapter = new ListTvSocietyHomeAdapter(context, arrayList , R.layout.horizontal_list_items);
					homeAdapter.notifyDataSetChanged();
					mGridView.setAdapter(homeAdapter);
					if (arrayList.isEmpty()) {
						Util.refreshList.setVisibility(View.VISIBLE);
						Util.isError = true;
					}else{
						Util.refreshList.setVisibility(View.GONE);
						Util.isError = false;
					}
					
					mPullRefreshGridView.onRefreshComplete();
					
					horizontal_scroll_list_title.setVisibility(View.VISIBLE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					pgb.setVisibility(View.GONE);
				}
			}
		});
	}
	
}
