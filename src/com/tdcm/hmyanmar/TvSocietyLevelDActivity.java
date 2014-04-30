package com.tdcm.hmyanmar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Json.TvSocietyLevelDParser;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.R.bool;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

public class TvSocietyLevelDActivity extends CoreActivity {

	TextView header_title;
	WebView webview;

	Bundle bundle;
	AQuery aq;
	TvSocietyLevelDParser jsonParser;
	Handler handler;
	GoogleAnalyticsTracker tracker;

	String url, header;
	HashMap<String, Object> data;
	WebSettings settings;
	int refresh_count = 3;
	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tvsociety_leveld);

		if(Boolean.parseBoolean(getString(R.string.istablet))){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
		
		bundle = getIntent().getExtras();
		header = bundle.getString("header");
		url = bundle.getString("url");

		init();
		aq = new AQuery(this);
		jsonParser = new TvSocietyLevelDParser();
		handler = new Handler();
		data = new HashMap<String, Object>();

		header_title.setText(header);
		callAPI();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),header) + "D");
		Statistic.tracking(String.format(getString(R.string.stat),header) + "D", header);
	}

	private void init() {
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");

		header_title = (TextView) findViewById(R.id.header_title);
		webview = (WebView) findViewById(R.id.webview);
		header_title.setTypeface(Util.getPadaukBookFont(this));

	}

	private void callAPI() {
		if (url != null) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					progressDialog.show();
				}
			});
			aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
				@Override
				public void callback(String url, JSONObject object,
						AjaxStatus status) {
					// TODO Auto-generated method stub
					super.callback(url, object, status);
					if (object != null) {
						progressDialog.dismiss();
						try {
							JSONObject content = object
									.getJSONObject("content");
							if (content.getString("status_code").equals("200")) {
								data = jsonParser.getInfo(content
										.getJSONObject("entry"));
								if (String.valueOf(data.get("code")).equals(
										"200")) {
									showData();
								} else {
									showDialodAndExit();
								}
							}else{
								if(refresh_count>0){
									refresh_count -= 1;
									callAPI();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							showDialodAndExit();
						}
					}else{
						if(refresh_count>0){
							refresh_count -= 1;
							callAPI();
						}else{
							progressDialog.dismiss();
						}
					}
				}
			});
		}
	}

	private void showData() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!String.valueOf(data.get("description")).equals("")) {
					String title = "";
					if (!String.valueOf(data.get("content_title")).equals(
							"null")) {
						String tag = getResources().getString(
								R.string.codeHeader1);
						String tag2 = getResources().getString(
								R.string.codeHeader2);
						title = new StringBuilder(tag)
								.append(String.valueOf(data
										.get("content_title"))).append(tag2)
								.toString();
					}
					String description = new StringBuilder(title).append(
							String.valueOf(data.get("description"))).toString();

					webview.getSettings().setLoadWithOverviewMode(true);
					webview.getSettings().setJavaScriptEnabled(true);
					webview.setHorizontalScrollBarEnabled(false);
					webview.setWebChromeClient(new WebChromeClient());

					settings = webview.getSettings();
					settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
					settings.setJavaScriptCanOpenWindowsAutomatically(true);
//					webview.loadDataWithBaseURL(null, description, "text/html",
//							"UTF-8", null);

					try {
						InputStream input = getAssets().open("theme.html");
						int size = input.available();
						byte[] buffer = new byte[size];
						input.read(buffer);
						input.close();
						String htmlData = new String(buffer);

						description = description.replaceAll(
								"(width=\").*?[0-9]{3}(\")", "width=100%");
						description = description.replaceAll(
								"(width:).*?[0-9]{3}(px;)", "width=100%");
						description = description.replaceAll(
								"(height=\").*?[0-9]{3}(\")", "height=auto");

						htmlData = htmlData.replaceAll(
								"<!--client_generated-->", description);

						webview.loadDataWithBaseURL(null, description,
								"text/html", "UTF-8", "");

						webview.setWebViewClient(new WebViewClient() {

							@Override
							public void onReceivedError(WebView view,
									int errorCode, String description,
									String failingUrl) {
								super.onReceivedError(view, errorCode,
										description, failingUrl);
								webview.loadDataWithBaseURL(
										"file:///android_asset/error.html",
										null, "text/html", "utf-8", null);
							}

						});

					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					showDialodAndExit();
				}
			} 
		});
	}

	private void showDialodAndExit() {
		Builder builder = new Builder(this);
		builder.setTitle("");
		builder.setMessage(R.string.alerttextandexit);
		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						onClickExit(getCurrentFocus());
					}
				});
		builder.show();
	}
	

	@Override
	public void onPause() {

		callHiddenWebViewMethod("onPause");
		
		super.onPause();
	}

	@Override
	public void onResume() {		
		
		callHiddenWebViewMethod("onResume");
		
		super.onResume();
	}

	@Override
	public void onDestroy() {
		
		callHiddenWebViewMethod("onDestroy");
		
		super.onDestroy();
	}

	private void callHiddenWebViewMethod(String name){
	    if( webview != null ){
	    	try {
		        Class.forName("android.webkit.WebView").getMethod(name, (Class[]) null).invoke(webview, (Object[]) null);

		    } catch(ClassNotFoundException cnfe) {
		        
		    } catch(NoSuchMethodException nsme) {
		        
		    } catch(InvocationTargetException ite) {
		        
		    } catch (IllegalAccessException iae) {
		        
		    }
	    }
	}

}
