package com.tdcm.hmyanmar.view;

import java.util.ArrayList;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tdcm.hmyanmar.Adapter.ListTVSocietyAdapter;
import com.tdcm.hmyanmar.Adapter.itemDialogAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Json.NewsParser;
import com.tdcm.hmyanmar.Util.PullAndLoadListView;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.Util.CustomsListView.OnRefreshListener;
import com.tdcm.hmyanmar.Util.PullAndLoadListView.OnLoadMoreListener;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.TVSocietyDetailActivity;
import com.tdcm.hmyanmar.TabTVSocietyActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SocietyNewsView extends FrameLayout  implements OnItemClickListener{

	private Activity activity;
	private Context context;
	private ListTVSocietyAdapter newsAdapter;
//	private PullAndLoadListView list;
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
	private View socity_bottom;
	private Button socity_bt1,socity_bt2,socity_bt3;
	private ImageView socity_btnews,socity_btall,socity_bthot;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private TextView horizontal_scroll_list_title;
	
	public SocietyNewsView(String cat , final ProgressBar pgb, final Activity activity , final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setLayoutParams(new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pgb = pgb;
//		this.layout = layout;
		this.catagory = cat;
		this.context = context;
		
		View v = (View) vi.inflate(R.layout.society_news, null);
		addView(v);
		
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
			
			socity_btnews.setBackgroundResource(R.drawable.btn_tvsociety01_inactive);
			socity_btall.setBackgroundResource(R.drawable.btn_tvsociety02);
			socity_bthot.setBackgroundResource(R.drawable.btn_tvsociety03);
			
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
			socity_bt1 = (Button) socity_bottom.findViewById(R.id.socity_bt11);
			socity_bt2 = (Button) socity_bottom.findViewById(R.id.socity_bt22);
			socity_bt3 = (Button) socity_bottom.findViewById(R.id.socity_bt33);
			
			socity_bt1.setTypeface(Util.getPadaukFont(context));
			socity_bt2.setTypeface(Util.getPadaukFont(context));
			socity_bt3.setTypeface(Util.getPadaukFont(context));
			
			socity_bt1.setEnabled(false);
			socity_bt1.setBackgroundResource(R.drawable.bg_button_base_organge);
			socity_bt1.setTextColor(context.getResources().getColor(R.color.white));
			
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
		
		init();
		Category = context.getResources().getStringArray(R.array.CatDialogArray);
		entry.setSubCategory(Category[0]);
		
		callFirstApi(layout, pgb, context);
		Util.refreshList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				callFirstApi(layout, pgb, context);
				return false;
			}
		});
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
		if (arrayList.isEmpty()) {
			Util.refreshList.setVisibility(View.VISIBLE);
			Util.isError = true;
		}else{
//			layout.addView(list);
			Util.refreshList.setVisibility(View.GONE);
			Util.isError = false;
		}
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//				Toast.makeText(context, "Pull Down!", Toast.LENGTH_SHORT).show();
//				new GetDataTask().execute();
//				limit = 20;
//				callFirstApi(layout, pgb, context);
				pgb.setVisibility(View.VISIBLE);
//				list.onRefreshComplete();
				mPullRefreshGridView.onRefreshComplete();
				pgb.setVisibility(View.GONE);
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//				Toast.makeText(PullToRefreshGridActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
				new LoadDataTask().execute();
//				mPullRefreshGridView.onRefreshComplete();
			}

		});
	}
	
	private void callFirstApi(final LinearLayout layout, final ProgressBar pgb,final Context context) {
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
					mGridView.setAdapter(newsAdapter);
//					list.setAdapter(newsAdapter);
					if (arrayList.isEmpty()) {
						Util.refreshList.setVisibility(View.VISIBLE);
						Util.isError = true;
					}else{
//						layout.addView(list);
						Util.refreshList.setVisibility(View.GONE);
						Util.isError = false;
					}
					if (addlist.size() < limit) {
//						list.onRefreshComplete();
						mPullRefreshGridView.onRefreshComplete();
					}
					
					if(Boolean.valueOf(context.getString(R.string.istablet))){
						horizontal_scroll_list_title.setVisibility(View.VISIBLE);
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
								mPullRefreshGridView.onRefreshComplete();
//								list.onLoadMoreComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										for(int i=0;i<addlist.size();i++){
											arrayList.add(addlist.get(i));
										}
//										arrayList.addAll(addlist);
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
//			list.onLoadMoreComplete();
			mPullRefreshGridView.onRefreshComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
//			list.onLoadMoreComplete();
			mPullRefreshGridView.onRefreshComplete();
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
//				strApi = api.getApiListTVSocietyData(entry.getSubCategory(),0, limit);
				offset = 0;
				strApi = api.getApiListTVSocietyData(entry.getSubCategory(),offset, limit);
				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
					
					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						try {
							addlist = newsPaser.getData(json);
							if (addlist.isEmpty()) {
//								list.onRefreshComplete();
								mPullRefreshGridView.onRefreshComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										arrayList.clear();
										for(int i=0;i<addlist.size();i++){
											arrayList.add(addlist.get(i));
										}
//										arrayList.addAll(addlist);
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
//			list.onRefreshComplete();
			mPullRefreshGridView.onRefreshComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
//			list.onRefreshComplete();
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	private void init() {
		api = new API(context);
//		list = new PullAndLoadListView(context);
//		list.setSmoothScrollbarEnabled(true);
//		list.setOnItemClickListener(this);
		mGridView.setOnItemClickListener(this);
		mGridView.setSmoothScrollbarEnabled(true);
		aq = new AQuery(context);
		newsPaser = new NewsParser();
		Util.logo_cen.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				list.smoothScrollToPosition(0);
				mGridView.smoothScrollToPosition(0);
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
	public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
		try {
			ListNewsEntry data = arrayList.get(position);
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
				callFirstApi(layout, pgb, context);
				helpDialog.dismiss();
			}
		});
	}
	
}
