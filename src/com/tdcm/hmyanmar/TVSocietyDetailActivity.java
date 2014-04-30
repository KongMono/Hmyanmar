package com.tdcm.hmyanmar;

import java.io.InputStream;
import java.util.List;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;
import com.truelife.mobile.android.lib.DataUsage;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class TVSocietyDetailActivity extends FragmentActivity implements OnTouchListener {
	private WebView webView;
	private Bundle bundle;
	private WebSettings settings;
	private TextView textShare;
	String urlToShare, description, title, thumbnail;
	private GoogleAnalyticsTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activty_tv_society_detail);
		
		if(Boolean.parseBoolean(getString(R.string.istablet))){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}

		init();

		bundle = getIntent().getExtras();
		description = bundle.getString("description");
		title = bundle.getString("title");
		thumbnail = bundle.getString("thumbnail");
		urlToShare = bundle.getString("share_url");
		String tag = getResources().getString(R.string.codeHeader1);
		String tag2 = getResources().getString(R.string.codeHeader2);
		title = new StringBuilder(tag).append(title).append(tag2).toString();
		description = new StringBuilder(title).append(description).toString();

		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);

		settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebChromeClient(new WebChromeClient());
//		webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8",null);

		try {
			InputStream input = getAssets().open("theme.html");
			int size = input.available();
	        byte[] buffer = new byte[size];
	        input.read(buffer);
	        input.close();
	        String htmlData = new String(buffer);

	        description = description.replaceAll("(width=\").*?[0-9]{3}(\")", "width=100%");
	        description = description.replaceAll("(height=\").*?[0-9]{3}(\")", "height=auto");
	        
	        htmlData = htmlData.replaceAll("<!--client_generated-->", description);

			webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8","");
			
			webView.setWebViewClient(new WebViewClient(){

		        @Override
		        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
		            super.onReceivedError(view, errorCode, description, failingUrl);
		            webView.loadDataWithBaseURL("file:///android_asset/error.html", null, "text/html", "utf-8", null);
		        }

		    }); 
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),getString(R.string.socity_bt1)) + "D");
		Statistic.tracking(String.format(getString(R.string.stat),getString(R.string.socity_bt1)) + "D", title);

	}

	private void init() {
		textShare = (TextView) findViewById(R.id.textShare);
		textShare.setTypeface(Util.getPadaukBookFont(this));
		textShare.setTextSize(20f);
		webView = (WebView) findViewById(R.id.webview);
		textShare.setOnTouchListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.textShare:

			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, description);
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,urlToShare);
			PackageManager packManager = getPackageManager();
			List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(sharingIntent,PackageManager.MATCH_DEFAULT_ONLY);

			boolean resolved = false;
			for (ResolveInfo resolveInfo : resolvedInfoList) {
				if (resolveInfo.activityInfo.packageName.startsWith("com.facebook.katana")) {
					sharingIntent.setClassName(
							resolveInfo.activityInfo.packageName,
							resolveInfo.activityInfo.name);
					resolved = true;
					break;
				}
			}

			if (resolved) {
				startActivity(Intent.createChooser(sharingIntent, description));
			} else {
				Builder alert = new AlertDialog.Builder(TVSocietyDetailActivity.this);
				alert.setTitle("Warning");
				alert.setMessage("Facebook App not found");
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("text/plain");
								share.putExtra(Intent.EXTRA_TEXT, urlToShare);
								startActivity(Intent.createChooser(share, "share"));
								dialog.dismiss();

							}
						});
				alert.show();
			}

		default:
			break;
		}

		return false;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DataUsage.checkOnResumeCheckDataUsage(this);
		callHiddenWebViewMethod("onResume");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DataUsage.setActivityNameForOnPauseOnDestroyed(this);
		callHiddenWebViewMethod("onDestroy");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		DataUsage.setActivityNameForOnPauseOnDestroyed(this);
		callHiddenWebViewMethod("onPause");
	}
	
	private void callHiddenWebViewMethod(String name){
	    if( webView != null ){
	    	try {
		        Class.forName("android.webkit.WebView").getMethod(name, (Class[]) null).invoke(webView, (Object[]) null);
		    } catch(ClassNotFoundException cnfe) {
		        
		    } catch(NoSuchMethodException nsme) {
		        
		    } catch(InvocationTargetException ite) {
		        
		    } catch (IllegalAccessException iae) {
		        
		    }
	    }
	}

}
