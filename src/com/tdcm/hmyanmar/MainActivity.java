package com.tdcm.hmyanmar;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;
import com.truelife.mobile.android.lib.DataUsage;
import com.viewpagerindicator.TitlePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends CoreFragment implements OnClickListener {

	private static LinearLayout streamingLayout;
	private LinearLayout tvSocietyLayout;
	private LinearLayout hclusiveLayout;
	private static LinearLayout headerbuttonframe;
	private static RelativeLayout headerLayout;
	public static Button btnStreaming, btnTVSociety, btnHClusive;
	private String[] tabs;
	private ImageView toggle;
	private static ImageView headerLayout_line;
	String title;
	LayoutInflater inflater;
	LayoutParams relativeParam;
	ProgressBar pgb;
	TitlePageIndicator indicator;
	GoogleAnalyticsTracker tracker;
	static Boolean isFullScreen = false;
	static Boolean island = false;
	static Boolean isTablet = false;
	public static Handler handler = new Handler();
	private TabStreamingActivity_New tabStreamingActivity_New;

	public MainActivity() {

	}

	public MainActivity(String t) {
		this.title = t;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		if (Boolean.parseBoolean(getActivity().getString(R.string.istablet))) {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//		} else {
//			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
			
		View view = inflater.inflate(R.layout.activity_main, container, false);

		tabs = getResources().getStringArray(R.array.tabArray);
		isTablet = Boolean.parseBoolean(getActivity().getString(R.string.istablet));

		init(view);

		btnStreaming.setSelected(true);
		btnTVSociety.setSelected(false);
		btnHClusive.setSelected(false);

		try {
			tabStreamingActivity_New = new TabStreamingActivity_New(tabs[0], streamingLayout, pgb, getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), getActivity());
		tracker.trackPageView(String.format(getString(R.string.stat), tabs[0])+ "A");
		Statistic.startStatistic(getActivity(), getString(R.string.cache_name),	"", getString(R.string.app_id),	getString(R.string.app_version));
		Statistic.tracking(String.format(getString(R.string.stat), tabs[0])	+ "A", tabs[0]);

		return view;
	}

	private void init(View view) {

		streamingLayout = (LinearLayout) view.findViewById(R.id.streamingLayout);
		tvSocietyLayout = (LinearLayout) view.findViewById(R.id.tvsocietyLayout);
		hclusiveLayout = (LinearLayout) view.findViewById(R.id.hclusiveLayout);
		headerbuttonframe = (LinearLayout) view.findViewById(R.id.headerbuttonframe);

		headerLayout = (RelativeLayout) view.findViewById(R.id.headerLayout);
		headerLayout_line = (ImageView) view.findViewById(R.id.headerLayout_line);

		btnStreaming = (Button) view.findViewById(R.id.btn1);
		btnTVSociety = (Button) view.findViewById(R.id.btn2);
		btnHClusive = (Button) view.findViewById(R.id.btn3);

		Util.titleCatagory = (TextView) view.findViewById(R.id.titleCatagory);
		Util.logo_right = (ImageView) view.findViewById(R.id.logo_right);
		Util.catagory = (ImageView) view.findViewById(R.id.imgmore);
		Util.refreshList = (ImageView) view.findViewById(R.id.imgRelist);
		toggle = (ImageView) view.findViewById(R.id.logo_left);
		Util.logo_cen = (ImageView) view.findViewById(R.id.logo_cen);
		Util.layout = (LinearLayout) view.findViewById(R.id.layout_right);

		btnStreaming.setTypeface(Util.getPadaukBookFont(getActivity()));
		btnTVSociety.setTypeface(Util.getPadaukBookFont(getActivity()));
		btnHClusive.setTypeface(Util.getPadaukBookFont(getActivity()));
		
		float required_width = getActivity().getResources().getDisplayMetrics().widthPixels / 3;
		Util.autoScalebuttonToHeight(getActivity(), btnStreaming,
				required_width, getString(R.string.tab1));
		Util.autoScalebuttonToHeight(getActivity(), btnTVSociety,
				required_width, getString(R.string.tab2));
		Util.autoScalebuttonToHeight(getActivity(), btnHClusive,
				required_width, getString(R.string.tab3));

		btnStreaming.setOnClickListener(this);
		btnTVSociety.setOnClickListener(this);
		btnHClusive.setOnClickListener(this);

		toggle.setBackgroundResource(R.drawable.btn_mainmenu);
		toggle.setVisibility(View.VISIBLE);
		toggle.setOnClickListener(this);

		relativeParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		pgb = (ProgressBar) view.findViewById(R.id.progressBar);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn1:
//			if (Boolean.parseBoolean(getActivity().getString(R.string.istablet))) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//			} else {
//				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			}
			if (streamingLayout.getChildCount() == 0) {
				try {
					tabStreamingActivity_New = new TabStreamingActivity_New(tabs[0], streamingLayout, pgb, getActivity());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			btnStreaming.setSelected(true);
			btnTVSociety.setSelected(false);
			btnHClusive.setSelected(false);
			streamingLayout.setVisibility(View.VISIBLE);
			tvSocietyLayout.setVisibility(View.GONE);
			hclusiveLayout.setVisibility(View.GONE);
			Util.refreshList.setVisibility(View.GONE);
			Util.layout.setVisibility(View.GONE);
			tabStreamingActivity_New.isVisibleLogoRight();
			tabStreamingActivity_New.playVideo();
//			TabStreamingActivity_New.isVisibleLogoRight();
			// Util.logo_right.setVisibility(View.VISIBLE);
//			TabStreamingActivity_New.playVideo();
			tracker.trackPageView(String.format(getString(R.string.stat),
					tabs[0]) + "A");
			Statistic.tracking(String.format(getString(R.string.stat), tabs[0])
					+ "A", tabs[0]);
			break;

		case R.id.btn2:
			if (isTablet) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
			} else {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			if (tvSocietyLayout.getChildCount() <= 1) {
				new TabTVSocietyActivity(tabs[1], tvSocietyLayout, pgb,	getActivity());
			}
			btnStreaming.setSelected(false);
			btnTVSociety.setSelected(true);
			btnHClusive.setSelected(false);
			streamingLayout.setVisibility(View.GONE);
			tvSocietyLayout.setVisibility(View.VISIBLE);
			hclusiveLayout.setVisibility(View.GONE);
			if (Util.isError) {
				Util.refreshList.setVisibility(View.VISIBLE);
			}
			Util.logo_right.setVisibility(View.GONE);
			tabStreamingActivity_New.puaseVideo();
//			TabstreamingActivity.puaseVideo();
			// tracker.trackPageView(String.format(getString(R.string.stat),
			// tabs[1]) + "A");
			// Statistic.tracking(String.format(getString(R.string.stat),
			// tabs[1]) + "A", tabs[1]);
			break;
		case R.id.btn3:
			if (isTablet) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
			} else {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			if (hclusiveLayout.getChildCount() == 0) {
				new TabHClusiveActivity(tabs[2], hclusiveLayout, pgb,
						getActivity());
			}
			btnStreaming.setSelected(false);
			btnTVSociety.setSelected(false);
			btnHClusive.setSelected(true);
			streamingLayout.setVisibility(View.GONE);
			tvSocietyLayout.setVisibility(View.GONE);
			hclusiveLayout.setVisibility(View.VISIBLE);
			if (Util.isError) {
				Util.refreshList.setVisibility(View.VISIBLE);
			}
			Util.logo_right.setVisibility(View.GONE);
			Util.layout.setVisibility(View.GONE);
//			TabstreamingActivity.puaseVideo();
			tabStreamingActivity_New.puaseVideo();

			tracker.trackPageView(String.format(getString(R.string.stat),
					tabs[2]) + "A");
			Statistic.tracking(String.format(getString(R.string.stat), tabs[2])
					+ "A", tabs[2]);
			break;

		case R.id.logo_left:
			toggleLeftMenu();
			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		streamingLayout.removeAllViewsInLayout();
		tvSocietyLayout.removeAllViewsInLayout();
		hclusiveLayout.removeAllViewsInLayout();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			island = true;
			if (isTablet) {
				if(!isFullScreen){
					TabStreamingActivity_New.landScreen();
				}
			} else {
				fullScreen(getActivity());
				TabStreamingActivity_New.fullScreen();
			}
		} else {
			island = false;
			if(isTablet){
				if(!isFullScreen){
					TabStreamingActivity_New.portScreen();
				}
			}else{
				exitFullScreen(getActivity());		
				TabStreamingActivity_New.notFullScreen();
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (tabStreamingActivity_New!=null && streamingLayout.getVisibility() == View.VISIBLE) {
			tabStreamingActivity_New.refreshList(tabStreamingActivity_New.categoryNum, tabStreamingActivity_New.titleCategory);
			tabStreamingActivity_New.playVideo();
//			TabstreamingActivity.refreshList();
//			TabstreamingActivity.playVideo();
		}
	}

	public static void fullScreen(final Context mContext) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFullScreen = true;
				((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				((View) headerLayout.getParent()).setPadding(0, 0, 0, 0);
				((View) headerLayout.getParent()).setBackgroundResource(R.color.black);
				headerbuttonframe.setVisibility(View.GONE);
				headerLayout.setVisibility(View.GONE);
				headerLayout_line.setVisibility(View.GONE);
			}
		});
	}

	public static void exitFullScreen(final Context mContext) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFullScreen = false;
				((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				((View) headerLayout.getParent()).setPadding(4, 0, 4, 0);
				((View) headerLayout.getParent()).setBackgroundResource(R.color.white);
				headerbuttonframe.setVisibility(View.VISIBLE);
				headerLayout.setVisibility(View.VISIBLE);
				headerLayout_line.setVisibility(View.VISIBLE);
			}
		});
		
	}

}
