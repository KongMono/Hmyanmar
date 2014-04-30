package com.tdcm.hmyanmar;

import java.util.ArrayList;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Adapter.ListTVSocietyAdapter;
import com.tdcm.hmyanmar.Adapter.itemDialogAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Json.NewsParser;
import com.tdcm.hmyanmar.Util.PullAndLoadListView;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.Util.CustomsListView.OnRefreshListener;
import com.tdcm.hmyanmar.Util.PullAndLoadListView.OnLoadMoreListener;
import com.tdcm.hmyanmar.view.SocietyAllView;
import com.tdcm.hmyanmar.view.SocietyHomeView;
import com.tdcm.hmyanmar.view.SocietyHotShotView;
import com.tdcm.hmyanmar.view.SocietyNewsView;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TabTVSocietyActivity extends FragmentActivity implements OnItemClickListener{
	private Context context;
	private ListTVSocietyAdapter newsAdapter;
	private PullAndLoadListView list;
	private API api;
	private AQuery aq;
	private ProgressBar pgb;
	public static LinearLayout layout = null;
	private String catagory = null;
	private ListNewsEntry entry = new ListNewsEntry();
	private NewsParser newsPaser = new NewsParser();
	private String strApi;
	private ArrayList<ListNewsEntry> arrayList = new ArrayList<ListNewsEntry>();
	private Handler handler = new Handler();
	int offset = 0;
	int limit = 20;
	int count = 0;
	String [] Category;
	GoogleAnalyticsTracker tracker;
	
	public TabTVSocietyActivity(String cat, final LinearLayout layout,final ProgressBar pgb, final Context context) {
		this.pgb = pgb;
		this.layout = layout;
		this.catagory = cat;
		this.context = context;
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(context.getString(R.string.analytic), context);
		
//		init();
//		Category = context.getResources().getStringArray(R.array.CatDialogArray);
//		entry.setSubCategory(Category[0]);
//		
//		callFirstApi(entry.getSubCategory(),layout, pgb, context);
//		Util.refreshList.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				callFirstApi(entry.getSubCategory(),layout, pgb, context);
//				return false;
//			}
//		});
//		list.setOnLoadMoreListener(new OnLoadMoreListener() {
//			
//			@Override
//			public void onLoadMore() {
//				pgb.setVisibility(View.VISIBLE);
//				count++;
//				if (count >= 5) {
//					list.onLoadMoreComplete();
//					pgb.setVisibility(View.GONE);
//				}else{
//					new LoadDataTask().execute();
//				}
//				
//				
//			}
//		});
//		
//		list.setOnRefreshListener(new OnRefreshListener() {
//			
//			@Override
//			public void onRefresh() {
//				pgb.setVisibility(View.VISIBLE);
//				list.onRefreshComplete();
//				pgb.setVisibility(View.GONE);
//				new RefreshDataTask().execute();
//			}
//
//		});
//		if (arrayList.isEmpty()) {
//			Util.refreshList.setVisibility(View.VISIBLE);
//			Util.isError = true;
//		}else{
//			layout.addView(list);
//			Util.refreshList.setVisibility(View.GONE);
//			Util.isError = false;
//		}
		
//		layout.addView(new SocietyAllView(context));
//		layout.addView(new SocietyNewsView(cat, pgb, this , context));
//		toSocietyHotShotView();
		toSocietyHomView();
	}

	public void toSocietyHomView(){
		layout.removeAllViews();
		layout.addView(new SocietyHomeView(this , context));
		tracker.trackPageView(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt0)) + "A");
		Statistic.tracking(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt0)) + "A", context.getString(R.string.socity_bt0));
	}
	
	public void toSocietyNewsView(){
		layout.removeAllViews();
		layout.addView(new SocietyNewsView(catagory, pgb, this , context));
		tracker.trackPageView(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt1)) + "A");
		Statistic.tracking(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt1)) + "A", context.getString(R.string.socity_bt1));
	}
	
	public void toSocietyAllView(){
		layout.removeAllViews();
		layout.addView(new SocietyAllView(context,this));
		tracker.trackPageView(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt2)) + "A");
		Statistic.tracking(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt2)) + "A", context.getString(R.string.socity_bt2));
	}
	
	public void toSocietyHotShotView(){
		layout.removeAllViews();
		layout.addView(new SocietyHotShotView(this , context));
		tracker.trackPageView(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt3)) + "A");
		Statistic.tracking(String.format(context.getString(R.string.stat), context.getString(R.string.socity_bt3)) + "A", context.getString(R.string.socity_bt3));
	}
	
	private void callFirstApi(String Subcate,final LinearLayout layout, final ProgressBar pgb,final Context context) {
		strApi = api.getApiListTVSocietyData(entry.getSubCategory(),offset, limit);
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				ArrayList<ListNewsEntry> addlist = new ArrayList<ListNewsEntry>();
				try {
					arrayList.clear();
					addlist = newsPaser.getData(json);
					arrayList.addAll(addlist);
					newsAdapter = new ListTVSocietyAdapter(context, arrayList);
					newsAdapter.notifyDataSetChanged();
					list.setAdapter(newsAdapter);
					if (arrayList.isEmpty()) {
						Util.refreshList.setVisibility(View.VISIBLE);
						Util.isError = true;
					}else{
						layout.addView(list);
						Util.refreshList.setVisibility(View.GONE);
						Util.isError = false;
					}
					if (addlist.size() < limit) {
						list.onRefreshComplete();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					pgb.setVisibility(View.GONE);
				}
			}
		});
	}
	
	private class LoadDataTask extends AsyncTask<Void, Void, Void> {
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
				strApi = api.getApiListTVSocietyData(entry.getSubCategory(),offset+=20, limit);
				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
					
					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						try {
							addlist = newsPaser.getData(json);
							if (addlist.isEmpty()) {
								list.onLoadMoreComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										arrayList.addAll(addlist);
										newsAdapter.notifyDataSetChanged();
										pgb.setVisibility(View.GONE);
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
			list.onLoadMoreComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			list.onLoadMoreComplete();
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
				strApi = api.getApiListTVSocietyData(entry.getSubCategory(),0, limit);
				offset = 0;
				strApi = api.getApiListTVSocietyData(entry.getSubCategory(),offset, limit);
				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
					
					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						try {
							addlist = newsPaser.getData(json);
							if (addlist.isEmpty()) {
								list.onRefreshComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										arrayList.clear();
										arrayList.addAll(addlist);
										newsAdapter.notifyDataSetChanged();
										pgb.setVisibility(View.GONE);
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
			list.onRefreshComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			list.onRefreshComplete();
		}
	}

	private void init() {
		api = new API(context);
		list = new PullAndLoadListView(context);
		list.setSmoothScrollbarEnabled(true);
		list.setOnItemClickListener(this);
		aq = new AQuery(context);
		newsPaser = new NewsParser();
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
				showPopUp();
				return false;
			}
		});
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
		try {
			ListNewsEntry data = arrayList.get(position - 1);
			Intent intent = new Intent(this.context, TVSocietyDetailActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("thumbnail", data.getThumbnail());
			intent.putExtra("description", data.getDescription());
			intent.putExtra("title", data.getTitle());
			intent.putExtra("share_url", data.getShare_url());
		    this.context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showPopUp() {
		
		final Builder helpBuilder = new Builder(context);
		helpBuilder.setTitle("");

		final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_category_news,null);
		TextView txtheader = (TextView) view.findViewById(R.id.text);
		txtheader.setTypeface(Util.getPadaukBookFont(context));
		helpBuilder.setView(view);

		final AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();

		final ListView list = (ListView) view.findViewById(R.id.ls);
		final String[] values = context.getResources().getStringArray(R.array.CatDialogArray);

		list.setAdapter(new itemDialogAdapter(context,R.layout.item_category_dialog, values));
		list.setOnItemClickListener(new OnItemClickListener() {
			String category = null;
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,int position, long arg) {
				category = list.getItemAtPosition(position).toString();
				Util.titleCatagory.setText(category);
				entry.setSubCategory(category);
				callFirstApi(entry.getSubCategory(),layout, pgb, context);
				helpDialog.dismiss();
			}
		});
	}



}
