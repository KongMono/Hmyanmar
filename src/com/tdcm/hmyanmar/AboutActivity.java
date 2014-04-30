package com.tdcm.hmyanmar;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.truelife.mobile.android.access_blocking.util.Statistic;
import com.truelife.mobile.android.truelifecommon.Views.FeedbackDialog;
import com.truelife.mobile.android.util.file.CacheObj;
import com.truelife.mobile.android.util.file.UtilFileObject;

public class AboutActivity extends CoreFragment implements OnClickListener {

	private Button about_feedback;
	private ImageView logo, about;
	TextView about_call_support;
	private LinearLayout about_contactframe,about_iconframe,about_contentframe;
	private String EMAIL = "";
	private String USER_ID = "";
	
	private CacheObj cache = null;
	private String fObject_config_name = "";
	private String fObject_config_path = "";
	private UtilFileObject file = null;
	private HashMap<String, String> login_config = null;
	String title;
	
	public AboutActivity() {
		
	}
	
	public AboutActivity(String title) {
		this.title = title;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			about_contentframe.setGravity(Gravity.CENTER_HORIZONTAL);
			about_iconframe.setGravity(Gravity.CENTER_HORIZONTAL);
			about_contactframe.setGravity(Gravity.CENTER_HORIZONTAL);
		}else{
			about_contentframe.setGravity(Gravity.LEFT);
			about_iconframe.setGravity(Gravity.LEFT);
			about_contactframe.setGravity(Gravity.LEFT);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_views, container, false);
		if(MainActivity.isTablet){
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}else{
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		init(view);
		initUserData();
		
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			about_contentframe.setGravity(Gravity.CENTER_HORIZONTAL);
			about_iconframe.setGravity(Gravity.CENTER_HORIZONTAL);
			about_contactframe.setGravity(Gravity.CENTER_HORIZONTAL);
		}else{
			about_contentframe.setGravity(Gravity.LEFT);
			about_iconframe.setGravity(Gravity.LEFT);
			about_contactframe.setGravity(Gravity.LEFT);
		}
		
		return view;
	}
	@Override
	public void onClick(View v) {
		if(v == about_feedback ){
			FeedbackDialog feedback = new FeedbackDialog(getActivity(), getActivity().getString(R.string.app_name),"", USER_ID, Statistic.getLocation());
            feedback.show();
		}else if(v == about_call_support){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		builder.setIcon(R.drawable.ic_h_tv);
    		builder.setTitle(getString(R.string.app_name));
    		builder.setMessage("Call "+ getString(R.string.call_support_android));
    		builder.setPositiveButton("Cancel", null);
    		builder.setNegativeButton(R.string.button_call_support, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int id) {startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +getString(R.string.call_support_android))));
    			}
    		});
    		builder.show();
		}else if(v == about){
			toggleLeftMenu();
		}
	}
	
	private void init(View view) {
		about_feedback = (Button) view.findViewById(R.id.about_feedback);
		about_feedback.setOnClickListener(this);
		about_call_support = (TextView) view.findViewById(R.id.about_call_support);
		about_call_support.setOnClickListener(this);
		logo = (ImageView) view.findViewById(R.id.logo_cen);
		logo.setVisibility(View.INVISIBLE);
		about = (ImageView) view.findViewById(R.id.logo_left);
		about.setVisibility(View.VISIBLE);
		about.setBackgroundResource(R.drawable.btn_mainmenu);
		about.setOnClickListener(this);
		about_contactframe = (LinearLayout) view.findViewById(R.id.about_contactframe);
		about_iconframe = (LinearLayout) view.findViewById(R.id.about_iconframe);
		about_contentframe = (LinearLayout) view.findViewById(R.id.about_contentframe);
	}
	
	private void initUserData(){
		cache = new CacheObj(getActivity(), getActivity().getString(R.string.cache_name));
		fObject_config_name = getString(R.string.fobject_login_config);
		fObject_config_path = Environment.getExternalStorageDirectory() + "/"
				+ cache.getcacheId() + "/cache/setting";
		UtilFileObject.chkDir(fObject_config_path);

		file = new UtilFileObject(fObject_config_path + "/" + fObject_config_name);
		login_config = (HashMap<String, String>) file.read();
		
		
		try {
			USER_ID = String.valueOf(login_config.get("userid"));
			EMAIL = String.valueOf(login_config.get("email"));
		} catch (Exception e) {
			e.printStackTrace();
			EMAIL = "";
		}
	}
	
}
