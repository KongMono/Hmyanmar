package com.tdcm.hmyanmar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tdcm.hmyanmar.Adapter.TvSocietyLevelDListAdapter;
import com.tdcm.hmyanmar.Json.TvSocietyLevelDParser;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.youtube.DeveloperKey;
import com.tdcm.hmyanmar.youtube.YouTubeActivity;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TVSocietyLevelDPlayBackActivity extends YouTubeActivity implements
		OnClickListener, OnInitializedListener, OnFullscreenListener {

	Bundle bundle;
	Handler handler = new Handler();
	AQuery aq;
	TvSocietyLevelDListAdapter adapter, adapterland;
	TvSocietyLevelDParser jsonParser = new TvSocietyLevelDParser();
	WebSettings settings;
	ProgressDialog progressDialog;
	GoogleAnalyticsTracker tracker;

	TextView header_title, leveld_playback_viewtext, leveld_playback_viewnum,
			leveld_playback_title, leveld_playback_donebutton,leveld_playback_textinfo;
	YouTubePlayerView leveld_playback_youtube;
	LinearLayout leveld_playback_listframe, leveld_playback_infoframe, leveld_playback_headerframe, leveld_playback_vdoframe,
			leveld_playback_listport_frame, leveld_playback_listland_frame;
	ImageView leveld_playback_sharebutton, leveld_playback_infobutton,
			leveld_playback_image;
	GridView leveld_playback_list, leveld_playback_listland;
	RelativeLayout leveld_playback_imageframe, leveld_playback_bottomframe, leveld_playback_donebutton_frame,
			leveld_playback_viewframe;

	String url, header;
	List<HashMap<String, Object>> items;
	HashMap<String, Object> item;
	DecimalFormat formatter;
	int refresh_count = 3;
	
	Boolean isFullscreen = false;
	Boolean island = false;
	Boolean isTablet = false;
	String youtubeId = "";
	
	YouTubePlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tvsociety_leveld_playback);
		
//		if(MainActivity.isTablet){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//		}
		
		isTablet = Boolean.valueOf(getString(R.string.istablet));

		bundle = getIntent().getExtras();
		url = bundle.getString("url");
		header = bundle.getString("header");

		init();
		callAPI(url);

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat), header)
				+ "D");
		Statistic.tracking(String.format(getString(R.string.stat), header)
				+ "D", header);

	}

	private void init() {

		aq = new AQuery(this);
		formatter = new DecimalFormat("###,###,###");
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");

		header_title = (TextView) findViewById(R.id.header_title);
		leveld_playback_youtube = (YouTubePlayerView) findViewById(R.id.leveld_playback_youtube);
		leveld_playback_listframe = (LinearLayout) findViewById(R.id.leveld_playback_listframe);
		leveld_playback_viewtext = (TextView) findViewById(R.id.leveld_playback_viewtext);
		leveld_playback_viewnum = (TextView) findViewById(R.id.leveld_playback_viewnum);
		leveld_playback_sharebutton = (ImageView) findViewById(R.id.leveld_playback_sharebutton);
		leveld_playback_infobutton = (ImageView) findViewById(R.id.leveld_playback_infobutton);
		leveld_playback_list = (GridView) findViewById(R.id.leveld_playback_list);
		leveld_playback_infoframe = (LinearLayout) findViewById(R.id.leveld_playback_infoframe);
		leveld_playback_donebutton = (TextView) findViewById(R.id.leveld_playback_donebutton);
		leveld_playback_textinfo = (TextView) findViewById(R.id.leveld_playback_textinfo);
		leveld_playback_image = (ImageView) findViewById(R.id.leveld_playback_image);
		leveld_playback_imageframe = (RelativeLayout) findViewById(R.id.leveld_playback_imageframe);
		leveld_playback_title = (TextView) findViewById(R.id.leveld_playback_title);
		leveld_playback_headerframe = (LinearLayout) findViewById(R.id.leveld_playback_headerframe);
		leveld_playback_bottomframe = (RelativeLayout) findViewById(R.id.leveld_playback_bottomframe);
		
		leveld_playback_listport_frame = (LinearLayout) findViewById(R.id.leveld_playback_listport_frame);
		leveld_playback_listland_frame = (LinearLayout) findViewById(R.id.leveld_playback_listland_frame);
		leveld_playback_listland = (GridView) findViewById(R.id.leveld_playback_listland);
		leveld_playback_donebutton_frame = (RelativeLayout) findViewById(R.id.leveld_playback_donebutton_frame);
		leveld_playback_viewframe = (RelativeLayout) findViewById(R.id.leveld_playback_viewframe);

//		leveld_playback_youtube.initialize(DeveloperKey.DEVELOPER_KEY, this);

		header_title.setTypeface(Util.getPadaukBookFont(this));
		leveld_playback_viewtext.setTypeface(Util.getPadaukFont(this));
		leveld_playback_viewnum.setTypeface(Util.getPadaukFont(this));
		leveld_playback_donebutton.setTypeface(Util.getPadaukBookFont(this));
		leveld_playback_textinfo.setTypeface(Util.getPadaukFont(this));
		leveld_playback_title.setTypeface(Util.getPadaukBookFont(this));

		leveld_playback_sharebutton.setOnClickListener(this);
		leveld_playback_infobutton.setOnClickListener(this);
		leveld_playback_donebutton.setOnClickListener(this);
		leveld_playback_imageframe.setOnClickListener(this);

		header_title.setText(header);
	
	}

	private void callAPI(final String api) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressDialog.show();
			}
		});
		aq.ajax(api, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				if (object != null) {
					try {
						JSONObject content = object.getJSONObject("content");

						item = jsonParser.getInfo(content.getJSONObject("entry"));

						JSONObject chapter = content.getJSONObject("chapter");
						Object itemtemp = chapter.get("item");
						if (itemtemp instanceof JSONArray) {
							items = jsonParser
									.getItemsList((JSONArray) itemtemp);
						} else {
							HashMap<String, Object> temp = jsonParser
									.getInfo((JSONObject) itemtemp);
							items = new ArrayList<HashMap<String, Object>>();
							items.add(temp);
						}

						adapter = new TvSocietyLevelDListAdapter(TVSocietyLevelDPlayBackActivity.this, items, header, false);
						leveld_playback_list.setAdapter(adapter);
						leveld_playback_list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int position, long arg3) {
										// TODO Auto-generated method stub
										HashMap<String, Object> item = adapter.getItem(position);
										String stringurl = String.format(getString(R.string.api_getdramaarchive),
														String.valueOf(item.get("chapter_id")))
												+ String.valueOf(item.get("parent_id"));
										callAPI(stringurl);
									}
								});
						
						if(isTablet){
							adapterland = new TvSocietyLevelDListAdapter(TVSocietyLevelDPlayBackActivity.this, items, header, true);
							leveld_playback_listland.setAdapter(adapterland);
							leveld_playback_listland.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int position, long arg3) {
											// TODO Auto-generated method stub
											HashMap<String, Object> item = adapterland.getItem(position);
											String stringurl = String.format(getString(R.string.api_getdramaarchive),
															String.valueOf(item.get("chapter_id")))
													+ String.valueOf(item.get("parent_id"));
											callAPI(stringurl);
										}
									});
						}

						showData();

						refresh_count = 3;

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//						if (refresh_count > 0) {
//							refresh_count -= 1;
//							callAPI(api);
//						} else {
//							handler.post(new Runnable() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									progressDialog.dismiss();
//									showDialodAndExit();
//								}
//							});
//						}
					}finally{
						progressDialog.dismiss();
					}
				} else {
					if (refresh_count > 0) {
						refresh_count -= 1;
						callAPI(api);
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								progressDialog.dismiss();
							}
						});
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leveld_playback_sharebutton:
			openDialogShare();
			break;
		case R.id.leveld_playback_infobutton:
			leveld_playback_viewframe.setVisibility(View.GONE);
			leveld_playback_listframe.setVisibility(View.GONE);
			leveld_playback_infoframe.setVisibility(View.VISIBLE);
			break;
		case R.id.leveld_playback_donebutton:
			leveld_playback_viewframe.setVisibility(View.VISIBLE);
			leveld_playback_listframe.setVisibility(View.VISIBLE);
			leveld_playback_infoframe.setVisibility(View.GONE);
			break;
		case R.id.leveld_playback_imageframe:
			// Builder builder = new Builder(this);
			// builder.setTitle("");
			// builder.setMessage(R.string.alerttextcannotplay);
			// builder.setPositiveButton(R.string.button_ok,null);
			// builder.show();
			break;
		default:
			break;
		}
	}

	private void showData() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (String.valueOf(item.get("stream")).equals("")) {
					leveld_playback_youtube.setVisibility(View.GONE);
					leveld_playback_imageframe.setVisibility(View.VISIBLE);
					aq.id(leveld_playback_image).image(String.valueOf(item.get("thumbnail")));
				} else {
					leveld_playback_imageframe.setVisibility(View.GONE);
					leveld_playback_youtube.setVisibility(View.VISIBLE);
				}

				leveld_playback_title.setText(String.valueOf(item.get("content_title")));
				leveld_playback_textinfo.setText(String.valueOf(
						item.get("description")).replaceAll("&nbsp;|&ndash;",""));
				double viewnum = Integer.parseInt(String.valueOf(
						item.get("view")).equals("null") ? "0" : String.valueOf(item.get("view")));
				leveld_playback_viewnum.setText("(" + formatter.format(viewnum)	+ ")");
				
			}
		});
		
		if (!String.valueOf(item.get("stream")).equals("")) {
			String description = String.valueOf(item.get("stream"));
			StringTokenizer tokens = new StringTokenizer(description, "\"");
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if(token.contains("http://www.youtube.com")){
					youtubeId = token.substring(token.lastIndexOf("/")+1, token.lastIndexOf("?"));
					break;
				}else{
					youtubeId = "";
				}
			}

			if(player!=null&&!youtubeId.equals("")){
				player.cueVideo(youtubeId);
			}else{
				leveld_playback_youtube.initialize(DeveloperKey.DEVELOPER_KEY, this);
			}
		}
		
	}

	private void openDialogShare() {

		final Dialog dialog = new Dialog(this);
		LayoutInflater inflater = getLayoutInflater();
		View viewdialog = inflater.inflate(R.layout.dialog_truemoveonly, null);
		Button cancel = (Button) viewdialog.findViewById(R.id.dialog_truemove_button);
		LinearLayout layout = (LinearLayout) viewdialog.findViewById(R.id.dialog_truemove_layout);

		Button shareButton = new Button(this);
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// params.setMargins(0, 0, 0, 20);
		shareButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		shareButton.setBackgroundColor(Color.WHITE);
		shareButton.setText("Share");
		shareButton.setTextColor(Color.parseColor("#ff6602"));
		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						String.valueOf(item.get("content_title")));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						String.valueOf(item.get("share_url")));
				startActivity(Intent.createChooser(sharingIntent,
						"Share Action"));
				dialog.dismiss();
			}
		});

		ImageView line = new ImageView(this);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		line.setBackgroundResource(R.drawable.line);

		Button copyButton = new Button(this);
		copyButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		copyButton.setBackgroundColor(Color.WHITE);
		copyButton.setText("Copy Link");
		copyButton.setTextColor(Color.parseColor("#ff6602"));
		copyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText(
						String.valueOf(item.get("content_title")),
						String.valueOf(item.get("share_url")));
				clipboard.setPrimaryClip(clip);
				Toast.makeText(TVSocietyLevelDPlayBackActivity.this,
						getString(R.string.copytext), Toast.LENGTH_SHORT)
						.show();
				dialog.dismiss();
			}
		});

		layout.addView(shareButton);
		layout.addView(line);
		layout.addView(copyButton);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			island = true;
			if(isTablet){
				if(!isFullscreen)
					landTablet();
			}else{
				if(player!=null){
					player.setFullscreen(true);
				}
			}
		} else {
			island = false;
			if(isTablet){
				if(!isFullscreen){
					portTablet();
				}
			}else{
				if(player!=null){
					player.setFullscreen(false);
				}
			}
		}
	}
	
	private void portTablet(){
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1.0f);
		leveld_playback_bottomframe.setLayoutParams(param);
		
		leveld_playback_listland_frame.setVisibility(View.GONE);
		leveld_playback_donebutton_frame.setVisibility(View.VISIBLE);
		leveld_playback_infoframe.setVisibility(View.GONE);
		leveld_playback_infobutton.setVisibility(View.VISIBLE);
		leveld_playback_listframe.setVisibility(View.VISIBLE);
	}
	
	private void landTablet(){
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 0.4f);
		leveld_playback_bottomframe.setLayoutParams(param);
		
		leveld_playback_listland_frame.setVisibility(View.VISIBLE);
		leveld_playback_donebutton.performClick();
		leveld_playback_donebutton_frame.setVisibility(View.GONE);
		leveld_playback_infoframe.setVisibility(View.VISIBLE);
		leveld_playback_infobutton.setVisibility(View.GONE);
		leveld_playback_listframe.setVisibility(View.GONE);
	}
	
	private void fullscreen(){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFullscreen = true;
				leveld_playback_headerframe.setVisibility(View.GONE);
				leveld_playback_bottomframe.setVisibility(View.GONE);
				leveld_playback_listland_frame.setVisibility(View.GONE);
				leveld_playback_title.setVisibility(View.GONE);
				TVSocietyLevelDPlayBackActivity.this.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				TVSocietyLevelDPlayBackActivity.this.getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		});
	}
	
	private void exitfullscreen(){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFullscreen = false;
				leveld_playback_headerframe.setVisibility(View.VISIBLE);
				leveld_playback_bottomframe.setVisibility(View.VISIBLE);
				leveld_playback_title.setVisibility(View.VISIBLE);
				TVSocietyLevelDPlayBackActivity.this.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				TVSocietyLevelDPlayBackActivity.this.getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				if(isTablet){
					if(island){
						landTablet();
					}else{
						portTablet();
					}
				}
			}
		});
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		Log.e("youtube", "youtube : error");
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		// TODO Auto-generated method stub
		if (!wasRestored) {
			this.player = player;
			this.player.setOnFullscreenListener(this);
			this.player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		}
		if(!youtubeId.equals(""))
			player.cueVideo(youtubeId);
	}

	@Override
	public void onFullscreen(boolean arg0) {
		// TODO Auto-generated method stub
		if(arg0){
			fullscreen();
		}else if(isTablet||!island){
			exitfullscreen();
		}else{
			player.setFullscreen(true);
		}
	}

}
