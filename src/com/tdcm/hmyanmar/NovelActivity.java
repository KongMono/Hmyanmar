package com.tdcm.hmyanmar;

import java.util.ArrayList;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Adapter.ListChaptersAdapter;
import com.tdcm.hmyanmar.Adapter.NovelPagerAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.NovelEntry;
import com.tdcm.hmyanmar.Json.NovelParser;
import com.tdcm.hmyanmar.Util.PopoverView;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.Util.PopoverView.PopoverViewDelegate;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NovelActivity extends CoreActivity implements OnClickListener,PopoverViewDelegate,OnItemClickListener{
	private Bundle bundle;
	ViewPager viewPager;
	TextView text_cen,text_left,text_right;
	TextView header_title;
	NovelPagerAdapter novelPagerAdapter;
	ArrayList<NovelEntry> listAllData = new ArrayList<NovelEntry>();
	private GoogleAnalyticsTracker tracker;
	Handler mHandler = new Handler();
	NovelEntry entry;
	NovelParser novelParser = new NovelParser();
	private AQuery aq;
	private API api;
	int Firstpage = 1;
	int id;
	int totalPage,selectedIndex,pageChanged,lastPage;
	String strApi;
	String title = "";
	ListView listSub;
	PopoverView popoverView;
	Boolean isChangeByPageNum = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.web_novel_detail);
//		getActionBar().hide();

		if(Boolean.parseBoolean(getString(R.string.istablet))){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
		
		init();
		bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		title = bundle.getString("title");
		strApi = api.getApiNovelInfo(id, Firstpage);
		
		 novelPagerAdapter = new NovelPagerAdapter(getSupportFragmentManager());
		 
		 aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
				@Override
				public void callback(String url, JSONObject json, AjaxStatus status) {
					 try {
						 totalPage = novelParser.getTotalPage(json);
						 text_cen.setText(Firstpage +"/"+ totalPage);
						 novelPagerAdapter.setData(NovelActivity.this,id,totalPage);
						 selectedIndex = Firstpage;
						 pageChanged = Firstpage;
						 viewPager.setAdapter(novelPagerAdapter);
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				}
		 });
		 
		 viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int page) {
				if (isChangeByPageNum) {
					selectedIndex = page + 1;
				}else{
					if(lastPage > page) {
						selectedIndex--;
					}else if(lastPage < page){ 
				    	selectedIndex++;
				    }
				}
				
				lastPage = page;
				isChangeByPageNum = false;
				Log.d("selectedIndex","lastPage: " +String.valueOf(lastPage));
			}
			
			@Override
			public void onPageScrolled(int page, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int page) {
				
				if (selectedIndex == 1) {
					text_left.setVisibility(View.INVISIBLE);
					text_cen.setText(String.valueOf("1" + "/" + totalPage));
					
				}else if(selectedIndex == totalPage){
					text_right.setVisibility(View.INVISIBLE);
					text_cen.setText(String.valueOf(totalPage + "/" + totalPage));
					
				}else if(selectedIndex != 1 && selectedIndex != totalPage){
					text_left.setVisibility(View.VISIBLE);
					text_right.setVisibility(View.VISIBLE);
					text_cen.setText(String.valueOf(selectedIndex) + "/" + totalPage);
				}
			}
		});
		 
		 
		text_cen.setOnClickListener(this);
		text_left.setOnClickListener(this);
		text_right.setOnClickListener(this);
		listSub.setOnItemClickListener(this);
		listSub.setVerticalScrollBarEnabled(false);
		listSub.setDividerHeight(0);
		 
		int required_width = this.getResources().getDisplayMetrics().widthPixels - 40;
		 
		header_title.setText(title);
		header_title.setTypeface(Util.getPadaukBookFont(this));
		Util.autoScaleTextViewTextToHeight(this, header_title, required_width);
		 
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),title) + "D");
		Statistic.tracking(String.format(getString(R.string.stat),title) + "D", title);
	}
	
	private void init() {
		
		aq = new AQuery(this);
		api = new API(this);
		listSub = new ListView(this);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		text_cen = (TextView) findViewById(R.id.text_cen);
		text_left = (TextView) findViewById(R.id.text_left);
		text_right = (TextView) findViewById(R.id.text_right);
		header_title = (TextView) findViewById(R.id.header_title);
		
		text_cen.setTypeface(Util.getPadaukFont(this));
		text_left.setTypeface(Util.getPadaukFont(this));
		text_right.setTypeface(Util.getPadaukFont(this));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_cen:
			
			
			final ArrayList<String> arrayList = new ArrayList<String>();
		    for (int i = 1; i <= totalPage; ++i) {
		    	arrayList.add(String.valueOf(i));
		    }
		    String[] array = arrayList.toArray(new String[arrayList.size()]);
		    listSub.setAdapter(new ListChaptersAdapter(this,R.layout.item_category_dialog, array));
			
			RelativeLayout rootView = (RelativeLayout) findViewById(R.id.Frame);

			int required_height = NovelActivity.this.getResources().getDisplayMetrics().heightPixels/2;
			
			popoverView = new PopoverView(NovelActivity.this,listSub);
			popoverView.setContentSizeForViewInPopover(new Point(100, required_height));
			popoverView.setDelegate(this);
			popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(text_cen), PopoverView.PopoverArrowDirectionDown, true);
			break;
			
		case R.id.text_left:
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
			break;
		case R.id.text_right:
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		if (viewPager.getCurrentItem() == 0) {
			text_left.setVisibility(View.INVISIBLE);
		}
		super.onResume();
	}
    
	@Override
	public void popoverViewWillShow(PopoverView view) {
		
		Log.i("POPOVER", "Will show");
	}

	@Override
	public void popoverViewDidShow(PopoverView view) {
		Log.i("POPOVER", "Did show");
	}

	@Override
	public void popoverViewWillDismiss(PopoverView view) {
		Log.i("POPOVER", "Will dismiss");
	}

	@Override
	public void popoverViewDidDismiss(PopoverView view) {
		Log.i("POPOVER", "Did dismiss");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view,int position, long arg) {
		isChangeByPageNum = true;
		String chapter = listSub.getItemAtPosition(position).toString();
		int pagechange = lastPage - Integer.parseInt(chapter);
		viewPager.setCurrentItem((viewPager.getCurrentItem() - pagechange) - 1);
		popoverView.dissmissPopover(true);
	}

}
