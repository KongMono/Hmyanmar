package com.tdcm.hmyanmar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tdcm.hmyanmar.Adapter.TvSocietyLevelCAdapter;
import com.tdcm.hmyanmar.Adapter.TvSocietyLevelDListAdapter;
import com.tdcm.hmyanmar.Adapter.TvSocietySubMenuAdapter;
import com.tdcm.hmyanmar.Json.JsonParser;
import com.tdcm.hmyanmar.Json.TvSocietyLevelDParser;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.youtube.DeveloperKey;
import com.tdcm.hmyanmar.youtube.YouTubeActivity;
import com.truelife.mobile.android.access_blocking.util.Statistic;
import com.truelife.mobile.android.media.StreamingGenerator;
import com.truelife.mobile.android.media.StreamingGenerator.StreamingListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.RelativeLayout.LayoutParams;

public class AllTvSocietyLevelDAcyivity extends YouTubeActivity implements OnClickListener, StreamingListener, OnItemClickListener, OnInitializedListener, OnFullscreenListener{
	
	private LinearLayout alltvsocietyleveld_loadingvideoview,alltvsocietyleveld_menu,alltvsocietyleveld_info,
		alltvsocietyleveld_headerframe,alltvsocietyleveld_info_land;
	private ImageView alltvsocietyleveld_infobutton,alltvsocietyleveld_sharebutton, alltvsocietyleveld_image
		,alltvsocietyleveld_imageplay,alltvsocietyleveld_playpausebutton,alltvsocietyleveld_fullscreenbutton;
	private TextView alltvsocietyleveld_viewtext,alltvsocietyleveld_viewnum,alltvsocietyleveld_titlevideoinfo
		,alltvsocietyleveld_textinfo,header_title,alltvsocietyleveld_donebutton,alltvsocietyleveld_titlevideoinfo_land,
		alltvsocietyleveld_textinfo_land;
	private GridView alltvsocietyleveld_gridviewmenu,alltvsocietyleveld_gridviewmenu_land;
//	private WebView alltvsocietyleveld_webview;
	private RelativeLayout alltvsocietyleveld_imageframe,alltvsocietyleveld_videoframe,alltvsocietyleveld_playpauseframe
		,alltvsocietyleveld_buttonframe,alltvsocietyleveld_midframe;
	private VideoView alltvsocietyleveld_videoview;
//	private FrameLayout alltvsocietyleveld_webviewframe,alltvsocietyleveld_webview_fullscreen;
	private YouTubePlayerView alltvsocietyleveld_youtube;
	
	private Bundle bundle;
	private String content_id,share_url,type;
	HashMap<String, Object> streaming = null;

	AQuery aq;
	JsonParser json = new JsonParser();
	TvSocietySubMenuAdapter adapterMenu,adapterMenu_land;
	DecimalFormat formatter;
	StreamingGenerator streamingGenerator;
	GoogleAnalyticsTracker tracker;
	ProgressDialog progressDialog;
	AlertDialog helpDialog;
	int refresh_count = 3;
	
	ListView list;
	TextView header_title_dialog;
	
	private TvSocietyLevelDListAdapter adapterImage;
	private TvSocietyLevelCAdapter adapterNews;
	private TvSocietyLevelDParser jsonParser = new TvSocietyLevelDParser();
	private List<HashMap<String,Object>> items;
	
	Boolean isFullscreen = false;
	Boolean island = false;
	Boolean isTablet = false;
	String youtubeId = "";
	
	YouTubePlayer player;
	
	private Handler mHandler = new Handler();
	
    Runnable runFadeInPlayPauseFrame = new Runnable() {
        @Override
        public void run() {
        	
            Animation anim = AnimationUtils.loadAnimation(AllTvSocietyLevelDAcyivity.this, R.anim.push_up_in);
            anim.setDuration(500);
            anim.setFillAfter(true);
            alltvsocietyleveld_playpauseframe.startAnimation(anim);
            alltvsocietyleveld_playpauseframe.setVisibility(View.VISIBLE);

            mHandler.postDelayed(runFadeOutPlayPauseFrame, 3000);
        }
    };

    Runnable runFadeOutPlayPauseFrame = new Runnable() {

        @Override
        public void run() {
        	
            Animation anim = AnimationUtils.loadAnimation(AllTvSocietyLevelDAcyivity.this, R.anim.push_down_in);
            anim.setDuration(500);
            anim.setFillAfter(true);
            alltvsocietyleveld_playpauseframe.startAnimation(anim);
            alltvsocietyleveld_playpauseframe.setVisibility(View.INVISIBLE);

        }
    };
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.alltvsocietyleveld);
//		if(Boolean.parseBoolean(getString(R.string.istablet))){
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//		}
		
		bundle = getIntent().getExtras();
		content_id = bundle.getString("content_id");
		share_url = bundle.getString("share_url");
		type = bundle.getString("type");
		
		isTablet = Boolean.parseBoolean(getString(R.string.istablet));
		
		init();
		
		aq = new AQuery(this);
		formatter = new DecimalFormat("###,###,###");
		streamingGenerator = new StreamingGenerator(this, this, getString(R.string.ref_app_name));
		
		streaming = new HashMap<String, Object>();
		
		getInfo();
		
	}
	
	private void init(){
		
		header_title = (TextView) findViewById(R.id.header_title);
		alltvsocietyleveld_menu = (LinearLayout) findViewById(R.id.alltvsocietyleveld_menu);
		alltvsocietyleveld_info = (LinearLayout) findViewById(R.id.alltvsocietyleveld_info);
		alltvsocietyleveld_viewtext = (TextView) findViewById(R.id.alltvsocietyleveld_viewtext);
		alltvsocietyleveld_viewnum = (TextView) findViewById(R.id.alltvsocietyleveld_viewnum);
		alltvsocietyleveld_infobutton = (ImageView) findViewById(R.id.alltvsocietyleveld_infobutton);
		alltvsocietyleveld_sharebutton = (ImageView) findViewById(R.id.alltvsocietyleveld_sharebutton);
		alltvsocietyleveld_titlevideoinfo = (TextView) findViewById(R.id.alltvsocietyleveld_titlevideoinfo);
		alltvsocietyleveld_textinfo = (TextView) findViewById(R.id.alltvsocietyleveld_textinfo);
		alltvsocietyleveld_gridviewmenu = (GridView) findViewById(R.id.alltvsocietyleveld_gridviewmenu);
//		alltvsocietyleveld_webview = (WebView) findViewById(R.id.alltvsocietyleveld_webview);
		alltvsocietyleveld_imageframe = (RelativeLayout) findViewById(R.id.alltvsocietyleveld_imageframe);
		alltvsocietyleveld_image = (ImageView) findViewById(R.id.alltvsocietyleveld_image);
		alltvsocietyleveld_imageplay = (ImageView) findViewById(R.id.alltvsocietyleveld_imageplay);
		alltvsocietyleveld_videoframe = (RelativeLayout) findViewById(R.id.alltvsocietyleveld_videoframe);
		alltvsocietyleveld_videoview = (VideoView) findViewById(R.id.alltvsocietyleveld_videoview);
		alltvsocietyleveld_playpauseframe = (RelativeLayout) findViewById(R.id.alltvsocietyleveld_playpauseframe);
		alltvsocietyleveld_playpausebutton = (ImageView) findViewById(R.id.alltvsocietyleveld_playpausebutton);
		alltvsocietyleveld_loadingvideoview = (LinearLayout) findViewById(R.id.alltvsocietyleveld_loadingvideoview);
		alltvsocietyleveld_donebutton = (TextView) findViewById(R.id.alltvsocietyleveld_donebutton);
		alltvsocietyleveld_headerframe = (LinearLayout) findViewById(R.id.alltvsocietyleveld_headerframe);
		alltvsocietyleveld_buttonframe = (RelativeLayout) findViewById(R.id.alltvsocietyleveld_buttonframe);
		alltvsocietyleveld_midframe = (RelativeLayout) findViewById(R.id.alltvsocietyleveld_midframe);
		
		alltvsocietyleveld_info_land = (LinearLayout) findViewById(R.id.alltvsocietyleveld_info_land);
		alltvsocietyleveld_titlevideoinfo_land = (TextView) findViewById(R.id.alltvsocietyleveld_titlevideoinfo_land);
		alltvsocietyleveld_textinfo_land = (TextView) findViewById(R.id.alltvsocietyleveld_textinfo_land);
		alltvsocietyleveld_gridviewmenu_land = (GridView) findViewById(R.id.alltvsocietyleveld_gridviewmenu_land);
		alltvsocietyleveld_fullscreenbutton = (ImageView) findViewById(R.id.alltvsocietyleveld_fullscreenbutton);
//		alltvsocietyleveld_webviewframe = (FrameLayout) findViewById(R.id.alltvsocietyleveld_webviewframe);
//		alltvsocietyleveld_webview_fullscreen = (FrameLayout) findViewById(R.id.alltvsocietyleveld_webview_fullscreen);
		alltvsocietyleveld_youtube = (YouTubePlayerView) findViewById(R.id.alltvsocietyleveld_youtube);
		
		header_title.setTypeface(Util.getPadaukBookFont(this));
		alltvsocietyleveld_viewtext.setTypeface(Util.getPadaukFont(this));
		alltvsocietyleveld_viewnum.setTypeface(Util.getPadaukFont(this));
		alltvsocietyleveld_titlevideoinfo.setTypeface(Util.getPadaukBookFont(this));
		alltvsocietyleveld_textinfo.setTypeface(Util.getPadaukFont(this));
		alltvsocietyleveld_donebutton.setTypeface(Util.getPadaukBookFont(this));
		
		alltvsocietyleveld_titlevideoinfo_land.setTypeface(Util.getPadaukBookFont(this));
		alltvsocietyleveld_textinfo_land.setTypeface(Util.getPadaukFont(this));
		
		alltvsocietyleveld_infobutton.setOnClickListener(this);
		alltvsocietyleveld_sharebutton.setOnClickListener(this);
		alltvsocietyleveld_imageframe.setOnClickListener(this);
		alltvsocietyleveld_playpausebutton.setOnClickListener(this);
		alltvsocietyleveld_donebutton.setOnClickListener(this);
		alltvsocietyleveld_fullscreenbutton.setOnClickListener(this);
		
		alltvsocietyleveld_videoview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(alltvsocietyleveld_playpauseframe.isShown()){
					mHandler.removeCallbacks(runFadeOutPlayPauseFrame);
					mHandler.postDelayed(runFadeOutPlayPauseFrame, 3000);
				}else{
					mHandler.post(runFadeInPlayPauseFrame);
				}
				
				return false;
			}
		});
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		
		if(!isTablet){
			alltvsocietyleveld_fullscreenbutton.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.alltvsocietyleveld_donebutton:
			alltvsocietyleveld_info.setVisibility(View.GONE);
			alltvsocietyleveld_menu.setVisibility(View.VISIBLE);
			Intent intent = YouTubeStandalonePlayer.createVideoIntent(
			          this, DeveloperKey.DEVELOPER_KEY, youtubeId, 0, true, true);
			startActivity(intent);
			break;
		case R.id.alltvsocietyleveld_infobutton:
			alltvsocietyleveld_info.setVisibility(View.VISIBLE);
			alltvsocietyleveld_menu.setVisibility(View.GONE);			
			break;
		case R.id.alltvsocietyleveld_sharebutton:
			openDialogShare();
			break;
		case R.id.alltvsocietyleveld_imageframe:
			if(type.equalsIgnoreCase("home")&&!String.valueOf(streaming.get("live_content_id")).equals("null")){
				genStreaming();
			}
//			Builder builder = new Builder(this);
//			builder.setTitle("");
//			builder.setMessage(R.string.alerttextcannotplay);
//			builder.setPositiveButton(R.string.button_ok,null);
//			builder.show();
			break;
		case R.id.alltvsocietyleveld_playpausebutton:
			if(alltvsocietyleveld_videoview.isPlaying()){
				try{
					alltvsocietyleveld_videoview.stopPlayback();
				}catch(Exception e){}
				alltvsocietyleveld_playpausebutton.setBackgroundResource(R.drawable.play);
			} else {
				genStreaming();
				alltvsocietyleveld_playpausebutton.setBackgroundResource(R.drawable.pause);
			}
			break;
		case R.id.alltvsocietyleveld_fullscreenbutton:
			if(isFullscreen){
				exitfullscreen();
			}else{
				fullscreen();
			}
			break;
		default:
			break;
		}
		
	}
	
	private void getInfo(){
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressDialog.show();
			}
		});
		
		String api = getString(R.string.api_getinfo) + content_id;		
		aq.ajax(api, JSONObject.class, new AjaxCallback<JSONObject>(){
			
			@Override
			public void callback(String url, JSONObject object,AjaxStatus status) {
				// TODO Auto-generated method stub
				
				if(object!=null){
					
					final HashMap<String, Object> result = json.parserInfo(object.toString());
					
					if(String.valueOf(result.get("code")).equalsIgnoreCase("200")){
						
						streaming = result;
						
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								header_title.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
								
								tracker = GoogleAnalyticsTracker.getInstance();
								tracker.startNewSession(getString(R.string.analytic), AllTvSocietyLevelDAcyivity.this);
								tracker.trackPageView(String.format(getString(R.string.stat),header_title.getText().toString()) + "B");
								Statistic.tracking(String.format(getString(R.string.stat),header_title.getText().toString()) + "B", header_title.getText().toString());
						
								String[] menu = {"1","1","1","1","1","1"};
								
								if(result.get("menu_synopsis")!=null){
									menu[0] = String.valueOf(result.get("menu_synopsis"));
								}
								if(result.get("menu_gallery")!=null){
									menu[1] = String.valueOf(result.get("menu_gallery"));
								}
								if(result.get("menu_archive")!=null){
									menu[2] = String.valueOf(result.get("menu_archive"));
								}
								if(result.get("menu_quote")!=null){
									menu[3] = String.valueOf(result.get("menu_quote"));
								}
								if(result.get("menu_music")!=null){
									menu[4] = String.valueOf(result.get("menu_music"));
								}
								if(result.get("menu_news")!=null){
									menu[5] = String.valueOf(result.get("menu_news"));
								}
								
								adapterMenu = new TvSocietySubMenuAdapter(AllTvSocietyLevelDAcyivity.this, menu, false);
								alltvsocietyleveld_gridviewmenu.setAdapter(adapterMenu);
								alltvsocietyleveld_gridviewmenu.setOnItemClickListener(AllTvSocietyLevelDAcyivity.this);
								
								if(isTablet){
									adapterMenu_land = new TvSocietySubMenuAdapter(AllTvSocietyLevelDAcyivity.this, menu, true);
									alltvsocietyleveld_gridviewmenu_land.setAdapter(adapterMenu_land);
									alltvsocietyleveld_gridviewmenu_land.setOnItemClickListener(AllTvSocietyLevelDAcyivity.this);
								}
																
								if(type.equalsIgnoreCase("all")&&!String.valueOf(result.get("embed_url")).equals("")){
									
									showWebView();
									
								}else{
									
//									alltvsocietyleveld_webviewframe.setVisibility(View.GONE);
//									alltvsocietyleveld_webview.setVisibility(View.GONE);
									alltvsocietyleveld_youtube.setVisibility(View.GONE);
									alltvsocietyleveld_videoframe.setVisibility(View.GONE);
									alltvsocietyleveld_imageframe.setVisibility(View.VISIBLE);
									Bitmap bmp_default = BitmapFactory.decodeResource(getResources(), R.drawable.htv_placeholder);
									aq.id(alltvsocietyleveld_image).image(String.valueOf(result.get("thumbnail_live")), true, true, 0, 0, bmp_default, 0);
									
									if(type.equalsIgnoreCase("home")&&!String.valueOf(result.get("live_content_id")).equals("null")){
										alltvsocietyleveld_imageplay.setVisibility(View.VISIBLE);
									}else{
										alltvsocietyleveld_imageplay.setVisibility(View.GONE);
										if(!isTablet)
											AllTvSocietyLevelDAcyivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
									}
									
								}
								
								alltvsocietyleveld_titlevideoinfo.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
								alltvsocietyleveld_textinfo.setText(String.valueOf(result.get("detail")).replaceAll("&nbsp;|&ndash;", " "));
								alltvsocietyleveld_titlevideoinfo_land.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
								alltvsocietyleveld_textinfo_land.setText(String.valueOf(result.get("detail")).replaceAll("&nbsp;|&ndash;", " "));
								double viewnum = Integer.parseInt(String.valueOf(result.get("views")).equals("null")?"0":String.valueOf(result.get("views")));
								alltvsocietyleveld_viewnum.setText("("+formatter.format(viewnum)+")");
								
								progressDialog.dismiss();
								
							}
						});
					}else {
						if(refresh_count>0){
							refresh_count -= 1;
							getInfo();
						}
						progressDialog.dismiss();
					}
					
				}else{
					if(refresh_count>0){
						refresh_count -= 1;
						getInfo();
					}
					progressDialog.dismiss();
				}
				
			}
			
		});
		
	}
	
	private void showWebView(){
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				alltvsocietyleveld_youtube.setVisibility(View.VISIBLE);
				alltvsocietyleveld_imageframe.setVisibility(View.GONE);
				alltvsocietyleveld_videoframe.setVisibility(View.GONE);
				
			}
		});
		
		String description = new StringBuilder(String.valueOf(streaming.get("embed_url"))).toString();
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
		if(!youtubeId.equals("")){
			alltvsocietyleveld_youtube.initialize(DeveloperKey.DEVELOPER_KEY, this);
		}
		
	}

	private void openDialogShare(){
		
		final Dialog dialog = new Dialog(this);
		LayoutInflater inflater = getLayoutInflater();
		View viewdialog = inflater.inflate(R.layout.dialog_truemoveonly, null);
		Button cancel = (Button) viewdialog.findViewById(R.id.dialog_truemove_button);
		LinearLayout layout = (LinearLayout) viewdialog.findViewById(R.id.dialog_truemove_layout);
		
		Button shareButton = new Button(this);
		shareButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		shareButton.setBackgroundColor(Color.WHITE);
		shareButton.setText("Share");
		shareButton.setTextColor(Color.parseColor("#ff6602"));
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, String.valueOf(streaming.get("channel_title")));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,share_url);
				startActivity(Intent.createChooser(sharingIntent, "Share Action"));
				dialog.dismiss();
			}
		});
		
		ImageView line = new ImageView(this);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		line.setBackgroundResource(R.drawable.line);
		
		Button copyButton = new Button(this);
		copyButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		copyButton.setBackgroundColor(Color.WHITE);
		copyButton.setText("Copy Link");
		copyButton.setTextColor(Color.parseColor("#ff6602"));
		copyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText(String.valueOf(streaming.get("channel_title")), share_url);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(AllTvSocietyLevelDAcyivity.this, getString(R.string.copytext), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		
		layout.addView(shareButton);
		layout.addView(line);
		layout.addView(copyButton);
		
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
	
	public void genStreaming(){
		if(streaming!=null){
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					alltvsocietyleveld_videoframe.setVisibility(View.VISIBLE);
					alltvsocietyleveld_imageframe.setVisibility(View.GONE);
					alltvsocietyleveld_playpausebutton.setBackgroundResource(R.drawable.play);
					alltvsocietyleveld_loadingvideoview.setVisibility(View.VISIBLE);
					streamingGenerator.start(String.valueOf(streaming.get("live_content_id")), String.valueOf(streaming.get("live_product_id")), String.valueOf(streaming.get("live_project_id")), String.valueOf(streaming.get("live_scope")), String.valueOf(streaming.get("live_content_type")), String.valueOf(streaming.get("live_lifestyle")));
				}
			});
		}
	}
	
	private void prepareVideo(final String url) {

		try {
			alltvsocietyleveld_videoview.stopPlayback();
		} catch (Exception e) {
		}
		 
		alltvsocietyleveld_videoview.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						alltvsocietyleveld_videoview.start();
						alltvsocietyleveld_playpausebutton.setBackgroundResource(R.drawable.pause);
						alltvsocietyleveld_loadingvideoview.setVisibility(View.GONE);
					}
				});
				
			}
		});
		alltvsocietyleveld_videoview.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(AllTvSocietyLevelDAcyivity.this, "Can't Play", Toast.LENGTH_SHORT).show();
						try {
							alltvsocietyleveld_videoview.stopPlayback();
						} catch (Exception e) {
						}
						alltvsocietyleveld_imageframe.setVisibility(View.VISIBLE);
						alltvsocietyleveld_videoview.setVisibility(View.GONE);
					}
				});
				
				return false;
			}
		});
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				alltvsocietyleveld_videoview.setVideoPath(url);
				alltvsocietyleveld_videoview.start();
			}
		});
		
			
	}
	
	private void stopVideoPlaying(){
		try {
			alltvsocietyleveld_videoview.stopPlayback();
		} catch (Exception e) {
		}
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				alltvsocietyleveld_playpausebutton.setBackgroundResource(R.drawable.play);
				alltvsocietyleveld_imageframe.setVisibility(View.VISIBLE);
				alltvsocietyleveld_videoframe.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onStreamingSuccess(String URL) {
		// TODO Auto-generated method stub
		prepareVideo(URL);
	}

	@Override
	public void onStreamingError(String message) {
		// TODO Auto-generated method stub
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AllTvSocietyLevelDAcyivity.this, "Can't Play", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void fullscreen(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFullscreen = true;
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);	
				alltvsocietyleveld_headerframe.setVisibility(View.GONE);
				alltvsocietyleveld_buttonframe.setVisibility(View.GONE);
				alltvsocietyleveld_gridviewmenu_land.setVisibility(View.GONE);
				alltvsocietyleveld_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen_exit);
			}
		});
	}
	
	private void exitfullscreen(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFullscreen = false;
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				alltvsocietyleveld_headerframe.setVisibility(View.VISIBLE);
				alltvsocietyleveld_buttonframe.setVisibility(View.VISIBLE);
				alltvsocietyleveld_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen);
				if(isTablet){
					if(island){
						landTabet();
					}else{
						portTabet();
					}
				}else{
//					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}
			}
		});
	}
	
	private void portTabet(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 0, 1.0f);
				alltvsocietyleveld_buttonframe.setLayoutParams(param);
				alltvsocietyleveld_info_land.setVisibility(View.GONE);
				alltvsocietyleveld_gridviewmenu_land.setVisibility(View.GONE);
				alltvsocietyleveld_gridviewmenu.setVisibility(View.VISIBLE);
				alltvsocietyleveld_infobutton.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void landTabet(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 0, 0.4f);
				alltvsocietyleveld_buttonframe.setLayoutParams(param);
				alltvsocietyleveld_info_land.setVisibility(View.VISIBLE);
				alltvsocietyleveld_gridviewmenu_land.setVisibility(View.VISIBLE);
				alltvsocietyleveld_gridviewmenu.setVisibility(View.GONE);
				alltvsocietyleveld_infobutton.setVisibility(View.GONE);
				if(alltvsocietyleveld_info.getVisibility()==View.VISIBLE){
					alltvsocietyleveld_donebutton.performClick();
				}
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			island = true;
			if(isTablet){
				if(!isFullscreen){
					landTabet();
				}
			}else{
				if(player!=null){
					player.setFullscreen(true);
				}else{
					fullscreen();
				}
			}
		} else {
			island = false;
			if(isTablet){
				if(!isFullscreen){
					portTabet();
				}
			}else{
				if(player!=null){
					player.setFullscreen(false);
				}else{
					exitfullscreen();
				}
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,int position, long arg) {
		// TODO Auto-generated method stub
		if(type.equalsIgnoreCase("home")&&!String.valueOf(streaming.get("live_content_id")).equals("null")){
			stopVideoPlaying();	
		}
		
		String[] item = adapterMenu.getItem(position);
		if(position==0&&item[1].equals("1")){
			
			Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TvSocietyLevelDActivity.class);
			intent.putExtra("header", item[0]);
			intent.putExtra("url", getString(R.string.api_getinfodrama)+content_id);
			startActivity(intent);
			
		}else if(position==1&&item[1].equals("1")){
			
			if(isTablet){
				openDialogListForTablet(String.format(getString(R.string.api_getdramagallery), content_id)+content_id, item[0]);
			}else{
				Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TVSocietyLevelCListActivity.class);
				intent.putExtra("header", item[0]);
				intent.putExtra("url", String.format(getString(R.string.api_getdramagallery), content_id)+content_id);
				intent.putExtra("content_id", content_id);
				startActivity(intent);
			}
			
		}else if(position==2&&item[1].equals("1")){

			Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TVSocietyLevelDPlayBackActivity.class);
			intent.putExtra("header", item[0]);
			intent.putExtra("url", String.format(getString(R.string.api_getdramaarchive), content_id)+content_id);
			startActivity(intent);
			
		}else if(position==3&&item[1].equals("1")){
			
			if(isTablet){
				openDialogListForTablet(getString(R.string.api_getdramaquote)+content_id, item[0]);
			}else{
				Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TvSocietyLevelCActivity.class);
				intent.putExtra("header", item[0]);
				intent.putExtra("url", getString(R.string.api_getdramaquote)+content_id);
				startActivity(intent);
			}
			
		}else if(position==4&&item[1].equals("1")){
			
			Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TvSocietyLevelDActivity.class);
			intent.putExtra("header", item[0]);
			intent.putExtra("url", getString(R.string.api_getmusic)+content_id);
			startActivity(intent);
			
		}else if(position==5&&item[1].equals("1")){
			
			if(isTablet){
				openDialogListForTablet(getString(R.string.api_getdramanews)+content_id, item[0]);
			}else{
				Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TvSocietyLevelCActivity.class);
				intent.putExtra("header", item[0]);
				intent.putExtra("url", getString(R.string.api_getdramanews)+content_id);
				startActivity(intent);
			}
			
		}
	};
	
	private void openDialogListForTablet(String url, final String title){
		
		progressDialog.show();
		
		if((helpDialog == null)||(helpDialog!=null&&!helpDialog.isShowing())){
			Builder helpBuilder = new Builder(this);
			helpBuilder.setTitle("");
	
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.activty_list_episode,null);
			helpBuilder.setView(view);
			
			helpDialog = helpBuilder.create();
	
			list = (ListView) view.findViewById(R.id.list);
			header_title_dialog = (TextView) view.findViewById(R.id.header_title);
			
			header_title_dialog.setTypeface(Util.getPadaukBookFont(this));
			header_title_dialog.setText(title);
			
			list.setDividerHeight(0);
			
			aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
				
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					
					try {
						if(json!=null){
							if(title.equals(getString(R.string.tvsocityleveldmenu2))){
								JSONObject content = object.getJSONObject("content");
								JSONObject album = content.getJSONObject("album");
								
								Object item = album.get("item");
								if(item instanceof JSONArray){
									items = jsonParser.getItemsList((JSONArray)item);
								}else{
									HashMap<String, Object> temp = jsonParser.getInfo((JSONObject)item);
									items = new ArrayList<HashMap<String,Object>>();
									items.add(temp);
								}
								
								if(items!=null && items.size()>0){
									adapterImage = new TvSocietyLevelDListAdapter(AllTvSocietyLevelDAcyivity.this, items, title, false);
									list.setAdapter(adapterImage);
									list.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(AdapterView<?> arg0,
												View arg1, int arg2, long arg3) {
											// TODO Auto-generated method stub
											HashMap<String, Object> item = adapterImage.getItem(arg2);
											Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this, TVSocietyLevelDGalleryActivity.class);
											intent.putExtra("url", String.format(getString(R.string.api_getdramagallery), content_id)+String.valueOf(item.get("album_id")));
											intent.putExtra("header", title);
											startActivity(intent);
										}
									});
								}
							}else{
								JSONObject content = object.getJSONObject("content");
								JSONObject data = content.getJSONObject("data");
								items = jsonParser.getItemsList(data.getJSONArray("item"));
								if (items != null && items.size()>0) {
									adapterNews = new TvSocietyLevelCAdapter(AllTvSocietyLevelDAcyivity.this, items);
									list.setAdapter(adapterNews);
									list.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(AdapterView<?> arg0,
												View view, int position, long arg) {
											// TODO Auto-generated method stub
											HashMap<String, Object> item = adapterNews.getItem(position);
											String url = "";
											if (title.equals(getString(R.string.tvsocityleveldmenu4))) {
												url = String.format(getString(R.string.api_getdramaquote_leveld),String.valueOf(item.get("chapter_id")))
														+ String.valueOf(item.get("parent_id"));
											} else {
												url = String.format(getString(R.string.api_getdramanews_leveld),String.valueOf(item.get("chapter_id")))
														+ String.valueOf(item.get("parent_id"));
											}
											Intent intent = new Intent(AllTvSocietyLevelDAcyivity.this,TvSocietyLevelDNewsActivity.class);
											intent.putExtra("header", title);
											intent.putExtra("url", url);
											intent.putExtra("thumbnail", String.valueOf(item.get("thumbnail")));
											startActivity(intent);
										}
									});
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						helpDialog.show();
						progressDialog.dismiss();
					}
				}
			});
			
		}
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
			this.player.cueVideo(youtubeId);
		}
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
