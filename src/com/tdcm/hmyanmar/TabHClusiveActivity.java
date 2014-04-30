package com.tdcm.hmyanmar;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tdcm.hmyanmar.Adapter.EpisodeAdapter;
import com.tdcm.hmyanmar.Adapter.ListHClusiveAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.EpisodeEntry;
import com.tdcm.hmyanmar.Dataset.ListHClusiveEntry;
import com.tdcm.hmyanmar.Json.EpisodeParser;
import com.tdcm.hmyanmar.Json.HClusiveParser;
import com.tdcm.hmyanmar.Util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TabHClusiveActivity extends FragmentActivity implements OnItemClickListener{
	private Context context;
	private ListHClusiveAdapter clusiveAdapter;
//	private LoadMoreListView list;
	private API api;
	private AQuery aq;
	private ProgressBar pgb;
	public  LinearLayout layout = null;
	private String catagory = null;
	private HClusiveParser clusiveParser = new HClusiveParser();
	private String strApi;
	private ArrayList<ListHClusiveEntry> arrayList = new ArrayList<ListHClusiveEntry>();
	private Handler handler = new Handler();
	int offset = 0;
	int limit = 20;
	int count = 0;
	private HashMap<String, String> checkOperator;
	
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	AlertDialog helpDialog;
	ProgressDialog progressDialog;
	
	private ArrayList<EpisodeEntry> episodeList = new ArrayList<EpisodeEntry>();
	EpisodeParser episodeParser = new EpisodeParser();
	EpisodeAdapter episodeAdapter;
	ListView list;
	TextView header_title;
	
	public TabHClusiveActivity(String cat, final LinearLayout l,final ProgressBar pgb, final Context context) {
		this.pgb = pgb;
		this.layout = l;
		this.catagory = cat;
		this.context = context;
		
		init();
		
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
//				pgb.setVisibility(View.GONE);
//				new LoadDataTask().execute();
//			}
//		});
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//				Toast.makeText(context, "Pull Down!", Toast.LENGTH_SHORT).show();
//				new GetDataTask().execute();
//				limit = 20;
				callFirstApi(layout, pgb, context);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//				Toast.makeText(PullToRefreshGridActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
				new LoadDataTask().execute();
//				mPullRefreshGridView.onRefreshComplete();
			}

		});
		
		
		if (arrayList.isEmpty()) {
			Util.refreshList.setVisibility(View.VISIBLE);
			Util.isError = true;
		}else{
			Util.refreshList.setVisibility(View.GONE);
			Util.isError = false;
		}
	}

	private void callFirstApi(final LinearLayout layout, final ProgressBar pgb,final Context context) {
		offset = 0;
		strApi = api.getApiListHClusiveData(offset, limit);
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				ArrayList<ListHClusiveEntry> addlist = new ArrayList<ListHClusiveEntry>();
				try {
					arrayList.clear();
					addlist = clusiveParser.getData(json);
					checkOperator = clusiveParser.getAllowOperator(json);
					arrayList.addAll(addlist);
					clusiveAdapter = new ListHClusiveAdapter(context, arrayList);
					clusiveAdapter.notifyDataSetChanged();
					mGridView.setAdapter(clusiveAdapter);
//					list.setAdapter(clusiveAdapter);
					if (arrayList.isEmpty()) {
						Util.refreshList.setVisibility(View.VISIBLE);
						Util.isError = true;
					}else{
						Util.refreshList.setVisibility(View.GONE);
						Util.isError = false;
					}
					pgb.setVisibility(View.GONE);
					mPullRefreshGridView.onRefreshComplete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private class LoadDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<ListHClusiveEntry> addlist = new ArrayList<ListHClusiveEntry>();
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
				strApi = api.getApiListHClusiveData(offset+=20, limit);
				aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
					
					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						try {
							addlist = clusiveParser.getData(json);
							if (addlist.isEmpty()) {
//								list.onLoadMoreComplete();
								mPullRefreshGridView.onRefreshComplete();
							}else{
								handler.post(new Runnable() {
									@Override
									public void run() {
										for(int i=0;i<addlist.size();i++){
											arrayList.add(addlist.get(i));
										}
//										arrayList.addAll(addlist);
										clusiveAdapter.notifyDataSetChanged();
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
			clusiveAdapter.notifyDataSetChanged();
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
	
	private void init() {
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Loading...");

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.hclusive_activity, null);
		
		view.setLayoutParams(new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		
		mPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		
		api = new API(context);
//		list = new LoadMoreListView(context);
//		list.setSmoothScrollbarEnabled(true);
//		list.setOnItemClickListener(this);
		mGridView.setOnItemClickListener(this);
		mGridView.setSmoothScrollbarEnabled(true);
		aq = new AQuery(context);
		clusiveParser = new HClusiveParser();
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
				return false;
			}
		});
		this.layout.addView(view);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
		try {
			if(checkOperator!=null&&String.valueOf(checkOperator.get("status")).toLowerCase().equals("yes")){
				ListHClusiveEntry data = arrayList.get(position);
				
				if(Boolean.parseBoolean(context.getString(R.string.istablet))){
					openDialogListForTablet(data.getId(), data.getTitle());
				}else{
					Intent intent = new Intent(this.context, EpisodeActivity.class);
					intent.putExtra("id", data.getId());
					intent.putExtra("title", data.getTitle());
					this.context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
				}
				
			}else{
			    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				View viewdialog = inflater.inflate(R.layout.dialog_truemoveonly, null);
				Button cancel = (Button) viewdialog.findViewById(R.id.dialog_truemove_button);
				TextView textView = (TextView) viewdialog.findViewById(R.id.dialog_truemove_text);
				
				if(checkOperator!=null)
					textView.setText(String.valueOf(checkOperator.get("message")));

				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(viewdialog);
				dialog.show();
				
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void openDialogListForTablet(int id, String title){
		
		progressDialog.show();
		
		if((helpDialog == null)||(helpDialog!=null&&!helpDialog.isShowing())){
			Builder helpBuilder = new Builder(context);
			helpBuilder.setTitle("");
	
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View view = inflater.inflate(R.layout.activty_list_episode,null);
			helpBuilder.setView(view);
			
			helpDialog = helpBuilder.create();
	
			list = (ListView) view.findViewById(R.id.list);
			header_title = (TextView) view.findViewById(R.id.header_title);
			
			header_title.setTypeface(Util.getPadaukBookFont(context));
			header_title.setText(title);
			
			list.setDividerHeight(0);
			
			String strApigetlist = api.getApiEpisodeData(id);
			aq.ajax(strApigetlist, JSONObject.class, new AjaxCallback<JSONObject>() {
				
				@Override
				public void callback(String url, JSONObject json, AjaxStatus status) {
					
					try {
						episodeList.clear();
						ArrayList<EpisodeEntry> addlistep = new ArrayList<EpisodeEntry>();
						addlistep = episodeParser.getData(json);
						episodeList.addAll(addlistep);
						episodeAdapter = new EpisodeAdapter(context, episodeList);
						episodeAdapter.notifyDataSetChanged();
						list.setAdapter(episodeAdapter);
						list.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
								try {
									EpisodeEntry data = episodeList.get(position);
									Intent intent = new Intent(context, NovelActivity.class);
									intent.putExtra("id", data.getId());
									intent.putExtra("title", data.getTitle());
									context.startActivity(intent);
									((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
						});
						
						helpDialog.show();
				
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						progressDialog.dismiss();
					}
				}
			});
			
		}
	}

}
