package com.tdcm.hmyanmar;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.truelife.mobile.android.access_blocking.util.Statistic;
import com.truelife.mobile.android.lib.DataUsage;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Util.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class TVSocietyInstagramActivity extends FragmentActivity {
	
	private Bundle bundle;
	private String content_id = "";
	private String share_url = "";
	private String headertitle = "";
	private int refresh_count = 3;
	String urlToShare, description, thumbnail;
	private GoogleAnalyticsTracker tracker;
	private String strApi = "";
	private API api;
	private AQuery aq;
	private ImageView imageview, share_image;
	private TextView textview_des;
	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.instagram_popup_detail);
		
		if(Boolean.parseBoolean(getString(R.string.istablet))){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}

		init();

		bundle = getIntent().getExtras();
		content_id = bundle.getString("content_id");
		headertitle = bundle.getString("title");
		share_url = bundle.getString("share_url");
		
		callFirstApi();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),getString(R.string.socity_bt3)) + "D");
		Statistic.tracking(String.format(getString(R.string.stat),getString(R.string.socity_bt3)) + "D", headertitle);

	}

	private void callFirstApi() {
		progressDialog.show();
		strApi = api.getInstagramData(content_id);
		aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				
				try {
					JSONObject content = json.getJSONObject("content");
					JSONObject entry = content.getJSONObject("entry");
//					textview_title.setText(headertitle);
					textview_des.setText(String.valueOf(entry.get("title")));
					aq.id(imageview).image(String.valueOf(entry.get("description")));
				} catch (Exception e) {
					e.printStackTrace();
					if (refresh_count > 0) {
						refresh_count -= 1;
						callFirstApi();
					}
				}finally{
					progressDialog.dismiss();
				}
			}
		});
	}
	
	private void init() {
		api = new API(this);
		aq = new AQuery(this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");

		imageview = (ImageView)findViewById(R.id.imageview);
//		textview_title = (TextView)findViewById(R.id.textview_title);
		textview_des = (TextView) findViewById(R.id.textview_des);
//		header_title = (TextView) findViewById(R.id.header_title);
		share_image = (ImageView) findViewById(R.id.share_image);
		
//		textview_title.setTypeface(Util.getTMediumFont(this));
		textview_des.setTypeface(Util.getPadaukFont(this));
//		header_title.setTypeface(Util.getTBoldFont(this));
//		header_title.setText(getString(R.string.socity_bt3));
		
		share_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDialogShare();
			}
		});
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DataUsage.checkOnResumeCheckDataUsage(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DataUsage.setActivityNameForOnPauseOnDestroyed(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		DataUsage.setActivityNameForOnPauseOnDestroyed(this);
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
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, headertitle);
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share_url);
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
				ClipData clip = ClipData.newPlainText(headertitle, share_url);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(TVSocietyInstagramActivity.this,
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

}
