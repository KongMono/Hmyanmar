package com.tdcm.hmyanmar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tdcm.hmyanmar.Adapter.ChannelTVAdapter;
import com.tdcm.hmyanmar.Adapter.ListChannelTVAdapter;
import com.tdcm.hmyanmar.Adapter.itemDialogAdapter;
import com.tdcm.hmyanmar.Database.UtilDatabase;
import com.tdcm.hmyanmar.Json.JsonParser;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.view.ChannelView;
import com.tdcm.hmyanmar.view.HorizontalListView;
import com.truelife.mobile.android.media.StreamingGenerator;
import com.truelife.mobile.android.media.StreamingGenerator.StreamingListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

public class TabStreamingActivity_New extends FragmentActivity implements OnClickListener, StreamingListener, OnItemClickListener {
	
	private static Context context;
	private ProgressBar pgb;
	private LinearLayout layout = null;
	private String catagory = null;
	
	VideoView streaming_videoview;
	LinearLayout streaming_loadingvideoview, streaming_channelframe;
	LinearLayout streaming_loading, streaming_loading_right;
	static LinearLayout streaming_textinfobottomframe, streaming_rightframe, streaming_portbottomframe, streaming_infoframe;
	RelativeLayout streaming_playpauseframe;
	static RelativeLayout streaming_buttomframe;
	Button streaming_categorybutton1, streaming_categorybutton2, streaming_categorybutton1_right, streaming_categorybutton2_right;
	ImageView streaming_imagevideo, streaming_playpausebutton;
	ImageView streaming_sharebutton, streaming_favouritebutton, streaming_capturebutton;
	static ImageView streaming_fullscreenbutton, streaming_infobutton;
	TextView streaming_titlevideo, streaming_textinfo;
	TextView streaming_titlevideoinfo, streaming_viewnum, streaming_categorytitle_right, streaming_editbutton_right;
	TextView streaming_categorytitle, streaming_editbutton, streaming_viewtext, streaming_textinfobottom;
	static TextView streaming_donebutton;
	HorizontalListView streaming_gridview;
	ListView streaming_gridview_right;
	
	int width;
	int height;
	
	List<HashMap<String, Object>> cateList = new ArrayList<HashMap<String,Object>>();
	List<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
	List<HashMap<String, Object>> favoriteList = new ArrayList<HashMap<String,Object>>();
	HashMap<String, Object> streaming = null;
	HashMap<String, Object> tv = null;
	HashMap<String, Object> add = new HashMap<String, Object>();
	
	static private Handler mHandler = new Handler();
	JsonParser json = new JsonParser();
	AlertDialog helpDialog;
	
	StreamingGenerator streamingGenerator;
	DecimalFormat formatter;
	AQuery aq;
	ListChannelTVAdapter adapter, adapter_right;
	
	String categoryNum = "";
	String titleCategory = "";
	Boolean firsttime = true;
	int refresh_count = 3;	
	
	Runnable runFadeIn = new Runnable() {
        @Override
        public void run() {
        	
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            anim.setDuration(500);
            anim.setFillAfter(true);
            streaming_playpauseframe.startAnimation(anim);
            streaming_playpauseframe.setVisibility(View.VISIBLE);

            mHandler.postDelayed(runFadeOut, 3000);
        }
    };

    Runnable runFadeOut = new Runnable() {

        @Override
        public void run() {
        	
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.push_down_in);
            anim.setDuration(500);
            anim.setFillAfter(true);
            streaming_playpauseframe.startAnimation(anim);
            streaming_playpauseframe.setVisibility(View.INVISIBLE);

        }
    };


	public TabStreamingActivity_New(String cat, LinearLayout layout,final ProgressBar pgb, Context context) throws Exception{
		this.pgb = pgb;
		this.layout = layout;
		this.catagory = cat;
		this.context = context;
		
		aq = new AQuery(context);
		streamingGenerator = new StreamingGenerator(context, this, context.getString(R.string.ref_app_name));
		aq = new AQuery(context);
		formatter = new DecimalFormat("###,###,###");
		
		add.put("channel_name", "");
		add.put("content_id", "add");
		
		((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

		init();
		
		if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			MainActivity.island = true;
			if (MainActivity.isTablet) {
				if(!MainActivity.isFullScreen){
					TabStreamingActivity_New.landScreen();
				}
			} else {
				MainActivity.fullScreen(TabStreamingActivity_New.context);
				fullScreen();
			}
		}else{
			MainActivity.island = false;
			if(MainActivity.isTablet){
				if(!MainActivity.isFullScreen){
					portScreen();
				}
			}else{
				MainActivity.exitFullScreen(context);		
				notFullScreen();
			}
		}
		
		this.pgb.setVisibility(View.GONE);
		
		adapter = new ListChannelTVAdapter(context, datalist, false, streaming_gridview);
		adapter_right = new ListChannelTVAdapter(context, datalist, true, streaming_gridview_right);
		
		streaming_gridview.setAdapter(adapter);
		streaming_gridview_right.setAdapter(adapter_right);

		streaming_categorybutton2.performClick();

	}

	private void init() {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activty_streaming_new, null);
		
		streaming_videoview = (VideoView) view.findViewById(R.id.streaming_videoview);
		streaming_imagevideo = (ImageView) view.findViewById(R.id.streaming_imagevideo);
		streaming_playpauseframe = (RelativeLayout) view.findViewById(R.id.streaming_playpauseframe);
		streaming_playpausebutton = (ImageView) view.findViewById(R.id.streaming_playpausebutton);
		streaming_loadingvideoview = (LinearLayout) view.findViewById(R.id.streaming_loadingvideoview);
		streaming_channelframe = (LinearLayout) view.findViewById(R.id.streaming_channelframe);
		streaming_titlevideo = (TextView) view.findViewById(R.id.streaming_titlevideo);
		streaming_favouritebutton = (ImageView) view.findViewById(R.id.streaming_favouritebutton);
		streaming_infobutton = (ImageView) view.findViewById(R.id.streaming_infobutton);
		streaming_sharebutton = (ImageView) view.findViewById(R.id.streaming_sharebutton);
		streaming_viewnum = (TextView) view.findViewById(R.id.streaming_viewnum);
		streaming_categorybutton1 = (Button) view.findViewById(R.id.streaming_categorybutton1);
		streaming_categorybutton2 = (Button) view.findViewById(R.id.streaming_categorybutton2);
		streaming_categorytitle = (TextView) view.findViewById(R.id.streaming_categorytitle);
		streaming_editbutton = (TextView) view.findViewById(R.id.streaming_editbutton);
		streaming_loading = (LinearLayout) view.findViewById(R.id.streaming_loadinghorizontallistview);
		streaming_infoframe = (LinearLayout) view.findViewById(R.id.streaming_infoframe);
		streaming_buttomframe = (RelativeLayout) view.findViewById(R.id.streaming_buttomframe);
		streaming_donebutton = (TextView) view.findViewById(R.id.streaming_donebutton);
		streaming_titlevideoinfo = (TextView) view.findViewById(R.id.streaming_titlevideoinfo);
		streaming_textinfo = (TextView) view.findViewById(R.id.streaming_textinfo);
		streaming_viewtext = (TextView) view.findViewById(R.id.streaming_viewtext);
		streaming_capturebutton = (ImageView) view.findViewById(R.id.streaming_capturebutton);
		
		streaming_portbottomframe = (LinearLayout) view.findViewById(R.id.streaming_portbottomframe);
		streaming_textinfobottomframe = (LinearLayout) view.findViewById(R.id.streaming_textinfobottomframe);
		streaming_textinfobottom = (TextView) view.findViewById(R.id.streaming_textinfobottom);
		streaming_rightframe = (LinearLayout) view.findViewById(R.id.streaming_rightframe);
		streaming_categorybutton1_right = (Button) view.findViewById(R.id.streaming_categorybutton1_right);
		streaming_categorybutton2_right = (Button) view.findViewById(R.id.streaming_categorybutton2_right);
		streaming_categorytitle_right = (TextView) view.findViewById(R.id.streaming_categorytitle_right);
		streaming_editbutton_right = (TextView) view.findViewById(R.id.streaming_editbutton_right);
		streaming_loading_right = (LinearLayout) view.findViewById(R.id.streaming_loading_right);
		streaming_fullscreenbutton = (ImageView) view.findViewById(R.id.streaming_fullscreenbutton);
		
		streaming_gridview = (HorizontalListView) view.findViewById(R.id.streaming_gridview);
		streaming_gridview_right = (ListView) view.findViewById(R.id.streaming_gridview_right);
		
		streaming_gridview_right.setDividerHeight(0);
		
		Util.logo_right.setBackgroundResource(R.drawable.btn_submenu);
		Util.logo_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopUp();
			}
		});
		Util.logo_right.setVisibility(View.GONE);
		
		streaming_categorybutton1.setOnClickListener(TabStreamingActivity_New.this);
		streaming_categorybutton2.setOnClickListener(TabStreamingActivity_New.this);
		streaming_infobutton.setOnClickListener(TabStreamingActivity_New.this);
		streaming_sharebutton.setOnClickListener(TabStreamingActivity_New.this);
		streaming_favouritebutton.setOnClickListener(TabStreamingActivity_New.this);
		streaming_donebutton.setOnClickListener(TabStreamingActivity_New.this);
		streaming_playpausebutton.setOnClickListener(TabStreamingActivity_New.this);
		streaming_editbutton.setOnClickListener(TabStreamingActivity_New.this);
		streaming_capturebutton.setOnClickListener(TabStreamingActivity_New.this);
		
		streaming_categorybutton1_right.setOnClickListener(TabStreamingActivity_New.this);
		streaming_categorybutton2_right.setOnClickListener(TabStreamingActivity_New.this);
		streaming_editbutton_right.setOnClickListener(TabStreamingActivity_New.this);
		streaming_fullscreenbutton.setOnClickListener(TabStreamingActivity_New.this);
		
		streaming_videoview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(streaming_playpauseframe.isShown()){
					mHandler.removeCallbacks(runFadeOut);
					mHandler.postDelayed(runFadeOut, 3000);
				}else{
					mHandler.post(runFadeIn);
				}
				
				return false;
			}
		});
		
		DisplayMetrics disp = context.getResources().getDisplayMetrics();
		width = disp.widthPixels;
		height = disp.heightPixels;
		
		streaming_categorybutton1.setSelected(true);
		streaming_categorybutton2.setSelected(false);
		
		streaming_categorybutton1.setTypeface(Util.getPadaukFont(context));
		streaming_categorybutton2.setTypeface(Util.getPadaukFont(context));
		
		streaming_categorybutton1_right.setSelected(true);
		streaming_categorybutton2_right.setSelected(false);
		
		streaming_categorybutton1_right.setTypeface(Util.getPadaukFont(context));
		streaming_categorybutton2_right.setTypeface(Util.getPadaukFont(context));
		
		streaming_textinfo.setTypeface(Util.getPadaukFont(context));
		streaming_donebutton.setTypeface(Util.getPadaukBookFont(context));
		streaming_titlevideoinfo.setTypeface(Util.getPadaukBookFont(context));
//		streaming_titlevideo.setTypeface(Util.getTMediumFont(context));
		streaming_viewtext.setTypeface(Util.getPadaukFont(context));
		streaming_viewnum.setTypeface(Util.getPadaukFont(context));
		streaming_categorytitle.setTypeface(Util.getPadaukFont(context));
		streaming_editbutton.setTypeface(Util.getPadaukFont(context));
		streaming_titlevideo.setTypeface(Util.getPadaukFont(context));
		
		streaming_textinfobottom.setTypeface(Util.getPadaukFont(context));
		streaming_categorytitle_right.setTypeface(Util.getPadaukFont(context));
		streaming_editbutton_right.setTypeface(Util.getPadaukFont(context));
		
		float required_width = width/3;
		Util.autoScalebuttonToHeight(context, streaming_categorybutton1, required_width, context.getString(R.string.category1));
		Util.autoScalebuttonToHeight(context, streaming_categorybutton2, required_width, context.getString(R.string.category2));
		streaming_titlevideo.setLayoutParams(new LayoutParams((int) required_width, LayoutParams.WRAP_CONTENT));
		streaming_titlevideo.setLineSpacing(-9, 1);
		
		required_width = width/5;
		Util.autoScalebuttonToHeight(context, streaming_categorybutton1_right, required_width, context.getString(R.string.category1));
		Util.autoScalebuttonToHeight(context, streaming_categorybutton2_right, required_width, context.getString(R.string.category2));
		
		if(!Boolean.parseBoolean(context.getString(R.string.istablet))){
			streaming_fullscreenbutton.setVisibility(View.GONE);
		}
		
		this.layout.addView(view);
		
	}
	
	public void fetchingdata (String cate, String title){
		if(!categoryNum.equalsIgnoreCase(cate)){
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					streaming_loading.setVisibility(View.VISIBLE);
					streaming_loading_right.setVisibility(View.VISIBLE);
				}
			});
			
			categoryNum = cate;
			titleCategory = title;
			
			if(categoryNum.equalsIgnoreCase(context.getString(R.string.category1))){
				favoriteList = UtilDatabase.getDataListInDB(context);
				if(favoriteList.size()>0){
					changeDataList(favoriteList);
				}else{
					String api = context.getString(R.string.api_gettoplist);
					Log.e("ChannelTVAdapter", "ChannelTVAdapter apigettoplist : "+api);
					aq.ajax(api, JSONObject.class, TabStreamingActivity_New.this, "getTopListCallback");
				}
			}else{
				String api = context.getString(R.string.api_getlist) + categoryNum;
				Log.e("ChannelTVAdapter", "ChannelTVAdapter apigetlist : "+api);
				aq.ajax(api, JSONObject.class, TabStreamingActivity_New.this, "getListCallback");
			}
		}
	}
	
	public void getListCallback(String url, JSONObject json, AjaxStatus status){
		if(json != null){
			refresh_count = 3;		
			try {
				JSONObject response = json.getJSONObject("response");
				if(cateList.size()==0){
					HashMap<String, Object> result = this.json.parserListCate(response.getString("menu"));
					if(result.get("code").equals("200"))
						cateList = (List<HashMap<String, Object>>) result.get("menu");
					
				}
				
				HashMap<String, Object> result = this.json.parserList(response.getString("data"));
				if(result.get("code").equals("200")&&!categoryNum.equalsIgnoreCase(context.getString(R.string.category1))){
					changeDataList((List<HashMap<String, Object>>) result.get("data"));
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(refresh_count>0){
			refresh_count -= 1;
			String api = context.getString(R.string.api_getlist) + categoryNum;
			aq.ajax(api, JSONObject.class, TabStreamingActivity_New.this, "getListCallback");
		}else{
			refresh_count = 3;
		}
	}
	
	public void getTopListCallback(String url, JSONObject json, AjaxStatus status){
		if(json != null){
			refresh_count = 3;		
			try {
				JSONObject response = json.getJSONObject("response");
				HashMap<String, Object> result = this.json.parserList(response.getString("data"));
				if(result.get("code").equals("200")){
					favoriteList = (List<HashMap<String, Object>>) result.get("data");
					for(int i=0;i<favoriteList.size();i++){
						boolean t = UtilDatabase.insert(context, String.valueOf(favoriteList.get(i).get("content_id")), "0", String.valueOf(favoriteList.get(i).get("channel_name")), String.valueOf(favoriteList.get(i).get("thumbnail")), String.valueOf(favoriteList.get(i).get("share_url")), String.valueOf(favoriteList.get(i).get("views")), String.valueOf(i));
						Log.e("add db", "add db : "+String.valueOf(favoriteList.get(i).get("title"))+" insert : "+t);
					}
					if(categoryNum.equalsIgnoreCase(context.getString(R.string.category1))){
						changeDataList(favoriteList);
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(refresh_count>0){
			refresh_count -= 1;
			String api = context.getString(R.string.api_gettoplist);
			aq.ajax(api, JSONObject.class, TabStreamingActivity_New.this, "getTopListCallback");
		}else{
			refresh_count = 3;
		}
	}
	
	private void changeDataList(List<HashMap<String, Object>> data){
		
		try {
			datalist.clear();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		for(int i=0; i<data.size(); i++) {
			datalist.add((HashMap<String, Object>) data.get(i));
		}
		
		streaming_gridview.setOnItemClickListener(this);
		
		streaming_gridview_right.setOnItemClickListener(this);
		
		if(titleCategory.equals(context.getString(R.string.category1))){
			streaming_categorytitle.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(favoriteList.size())));
			streaming_categorytitle_right.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(favoriteList.size())));
			if(datalist.size()<20){
				datalist.add(add);
			}
		}else{
			for(int i=0;i<cateList.size();i++){
				HashMap<String, Object> obj = cateList.get(i);
				if(categoryNum.equals(String.valueOf(obj.get("id")))){
					titleCategory = String.valueOf(obj.get("title"));
					break;
				}
			}
			streaming_categorytitle.setText(titleCategory);
			streaming_categorytitle_right.setText(titleCategory);
		}
		
		adapter.notifyDataSetChanged();
		adapter_right.notifyDataSetChanged();
		
		streaming_gridview.scrollTo(0);
		streaming_gridview_right.smoothScrollToPosition(0);
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				streaming_loading.setVisibility(View.GONE);
				streaming_loading_right.setVisibility(View.GONE);
			}
		});
		
		if(firsttime){
			firsttime = false;
			HashMap<String, Object> o = datalist.get(0);
			if(tv!=o){
				tv = o;
				getInfo(String.valueOf(o.get("content_id")));
			}
		}
		
	}
	
	public void getInfo(String contentid) {
		try {
			streaming_videoview.stopPlayback();
		} catch (Exception e) {
		}
		
		String api = context.getString(R.string.api_getinfo) + contentid;		
		aq.ajax(api, JSONObject.class, new AjaxCallback<JSONObject>(){
			
			@Override
			public void callback(String url, JSONObject object,AjaxStatus status) {
				// TODO Auto-generated method stub
				final HashMap<String, Object> result = json.parserInfo(object.toString());
				
				if(String.valueOf(result.get("code")).equalsIgnoreCase("200")){
					
					streaming = result;
					
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							((View) streaming_imagevideo.getParent()).setVisibility(View.VISIBLE);
							((View) streaming_imagevideo.getParent()).setBackgroundResource(R.color.white);
							streaming_imagevideo.setImageBitmap(null);
								
//							HashMap<String, Object> o = datalist.get(positionPlay);
							
							aq.id(streaming_imagevideo).image(String.valueOf(tv.get("thumbnail")), true, true);
							
							streaming_titlevideo.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
							streaming_titlevideoinfo.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
							streaming_textinfo.setText(String.valueOf(result.get("detail")).replaceAll("&nbsp;", " "));
							streaming_textinfobottom.setText(String.valueOf(result.get("detail")).replaceAll("&nbsp;", " "));
							double viewnum = Integer.parseInt(String.valueOf(result.get("views")).equals("null")?"0":String.valueOf(result.get("views")));
							streaming_viewnum.setText("("+formatter.format(viewnum)+")");
							
							genStreaming();
							
						}
					});
				}
			}
			
		});
	}
	
	public void genStreaming(){
		if(streaming!=null){
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					streaming_playpausebutton.setBackgroundResource(R.drawable.play);
					streaming_loadingvideoview.setVisibility(View.VISIBLE);
					((View) streaming_imagevideo.getParent()).setVisibility(View.VISIBLE);
					streamingGenerator.start(String.valueOf(streaming.get("stream_content_id")), String.valueOf(streaming.get("stream_product_id")), String.valueOf(streaming.get("stream_project_id")), String.valueOf(streaming.get("stream_scope")), String.valueOf(streaming.get("stream_content_type")), String.valueOf(streaming.get("stream_lifestyle")));
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.streaming_categorybutton1:
		case R.id.streaming_categorybutton1_right:
			fetchingdata(context.getString(R.string.category1),context.getString(R.string.category1));
			streaming_categorybutton1.setSelected(true);
			streaming_categorybutton2.setSelected(false);
			streaming_categorybutton1_right.setSelected(true);
			streaming_categorybutton2_right.setSelected(false);
			Util.logo_right.setVisibility(View.GONE);
			streaming_editbutton.setVisibility(View.VISIBLE);
			streaming_editbutton_right.setVisibility(View.VISIBLE);
			break;
		case R.id.streaming_categorybutton2:
		case R.id.streaming_categorybutton2_right:
			if(streaming_editbutton.getText().equals(context.getString(R.string.done))){
				adapter.setisDelete(false);
				adapter_right.setisDelete(false);
				adapter.notifyDataSetChanged();
				adapter_right.notifyDataSetChanged();
				streaming_editbutton.setText(R.string.edit);
				streaming_editbutton_right.setText(R.string.edit);
			}
			if(!streaming_categorybutton2.isSelected()&&!streaming_categorybutton2_right.isSelected()){
				fetchingdata("1","All");
			}
			streaming_categorybutton1.setSelected(false);
			streaming_categorybutton2.setSelected(true);
			streaming_categorybutton1_right.setSelected(false);
			streaming_categorybutton2_right.setSelected(true);
			Util.logo_right.setVisibility(View.VISIBLE);
			streaming_editbutton.setVisibility(View.GONE);
			streaming_editbutton_right.setVisibility(View.GONE);
			break;
		case R.id.streaming_favouritebutton:
			openDialogAddFavorite();
			break;
		case R.id.streaming_infobutton:
			streaming_channelframe.setVisibility(View.GONE);
			streaming_infoframe.setVisibility(View.VISIBLE);
			break;
		case R.id.streaming_sharebutton:
			openDialogShare();
			break;
		case R.id.streaming_donebutton:
			streaming_channelframe.setVisibility(View.VISIBLE);
			streaming_infoframe.setVisibility(View.GONE);
			break;
		case R.id.streaming_playpausebutton:
			if(streaming_videoview.isPlaying()){
				try{
				streaming_videoview.stopPlayback();
				}catch(Exception e){}
				streaming_playpausebutton.setBackgroundResource(R.drawable.play);
			} else {
				genStreaming();
				streaming_playpausebutton.setBackgroundResource(R.drawable.pause);
			}
			break;
		case R.id.streaming_editbutton:
		case R.id.streaming_editbutton_right:
//			Log.e("onclick", "onclick : "+streaming_editbutton.getText());
			if(streaming_editbutton.getText().equals(context.getString(R.string.edit))){
				streaming_editbutton.setText(R.string.done);
				streaming_editbutton_right.setText(R.string.done);
				adapter.setisDelete(true);
				adapter_right.setisDelete(true);
				if(favoriteList.size()<20){
					datalist.remove(add);
				}
				adapter.notifyDataSetChanged();
				adapter_right.notifyDataSetChanged();
			}else{
				streaming_editbutton.setText(R.string.edit);
				streaming_editbutton_right.setText(R.string.edit);
				adapter.setisDelete(false);
				adapter_right.setisDelete(false);
				refreshList(categoryNum, titleCategory);
			}
			break;
		case R.id.streaming_fullscreenbutton:
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(MainActivity.isFullScreen){
						notFullScreen();
						MainActivity.exitFullScreen(context);
						streaming_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen);
					}else{
						fullScreen();
						MainActivity.fullScreen(context);
						streaming_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen_exit);
					}
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void onStreamingSuccess(String URL) {
		// TODO Auto-generated method stub
		prepareVideo(URL);
	}

	@Override
	public void onStreamingError(String message) {
		// TODO Auto-generated method stub
		
	}
	
	private void prepareVideo(final String url) {

		try {
			streaming_videoview.stopPlayback();
		} catch (Exception e) {
		}
		 
		streaming_videoview.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(MainActivity.btnStreaming.isSelected()){
							streaming_videoview.start();
							streaming_playpausebutton.setBackgroundResource(R.drawable.pause);
							streaming_loadingvideoview.setVisibility(View.GONE);
							((View) streaming_imagevideo.getParent()).setVisibility(View.GONE);
						}
					}
				});
				
			}
		});
		streaming_videoview.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(context, "Can't Play", Toast.LENGTH_SHORT).show();
						try {
							streaming_videoview.stopPlayback();
						} catch (Exception e) {
						}
						streaming_loadingvideoview.setVisibility(View.GONE);
						((View) streaming_imagevideo.getParent()).setVisibility(View.VISIBLE);
					}
				});
				
				return false;
			}
		});
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				streaming_videoview.setVideoPath(url);
			}
		});
		
			
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		final HashMap<String, Object> o = datalist.get(position);
		if(titleCategory.equals(context.getString(R.string.category1))&&(adapter.getisDelete()||adapter_right.getisDelete())){
			Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("");
	        builder.setMessage(context.getString(R.string.addfavorite_alert_delete));
	        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int id) {
	            	UtilDatabase.deleteDataInDB(context, "0", String.valueOf(o.get("content_id")));
					datalist.remove(o);
					adapter.notifyDataSetChanged();
					adapter_right.notifyDataSetChanged();
					streaming_categorytitle.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(UtilDatabase.getSizeInDataBase(context))));
					streaming_categorytitle_right.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(UtilDatabase.getSizeInDataBase(context))));
	            }
	        });
	        builder.setNegativeButton(context.getString(R.string.button_cancel), null);
	        builder.show();
		}else if(titleCategory.equals(context.getString(R.string.category1))&&String.valueOf(o.get("content_id")).equals("add")){
			Intent intent = new Intent(context, AddFavoriteActivity.class);
			context.startActivity(intent);
		}else{
			if(tv!=o){
				tv = o;
				getInfo(String.valueOf(o.get("content_id")));
			}
		}
	}
	
	public static void fullScreen() {
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				streaming_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen_exit);
				streaming_buttomframe.setVisibility(View.GONE);
				streaming_rightframe.setVisibility(View.GONE);
			}
		});
		
	}

	public static void notFullScreen() {

		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				streaming_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen);
				streaming_buttomframe.setVisibility(View.VISIBLE);
				if(MainActivity.isTablet){
					if(MainActivity.island){
						landScreen();
					}else{
						portScreen();
					}
				}
			}
		});
		
	}
	
	public static void landScreen() {
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 0, 0.4f);
				streaming_buttomframe.setLayoutParams(param);
				streaming_portbottomframe.setVisibility(View.GONE);
				streaming_infobutton.setVisibility(View.GONE);
				streaming_textinfobottomframe.setVisibility(View.VISIBLE);
				if(streaming_infoframe.getVisibility()==View.VISIBLE){
					streaming_donebutton.performClick();
				}
				if(!MainActivity.isFullScreen){
					streaming_rightframe.setVisibility(View.VISIBLE);
				}
			}
		});
		
	}

	public static void portScreen() {

		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 0, 1.2f);
				streaming_buttomframe.setLayoutParams(param);
				streaming_rightframe.setVisibility(View.GONE);
				streaming_textinfobottomframe.setVisibility(View.GONE);
				streaming_portbottomframe.setVisibility(View.VISIBLE);
				streaming_infobutton.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	public void puaseVideo() {
		if(streaming_videoview.isPlaying()){
			streaming_playpausebutton.performClick();
		}
	}
	
	public void playVideo() {
		if(tv!=null){
			genStreaming();
		}else if(datalist.size()>0){
			HashMap<String, Object> o = datalist.get(0);
			if(tv!=o){
				tv = o;
				getInfo(String.valueOf(o.get("content_id")));
			}
		}
	}
	
	public void refreshList(String cate, String title){
		
		if(categoryNum.equalsIgnoreCase(context.getString(R.string.category1))){

			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					streaming_loading.setVisibility(View.VISIBLE);
					streaming_loading_right.setVisibility(View.VISIBLE);
				}
			});
			
			categoryNum = cate;
			titleCategory = title;
			
			favoriteList = UtilDatabase.getDataListInDB(context);
			if(favoriteList.size()>0){
				changeDataList(favoriteList);
			}else{
				String api = context.getString(R.string.api_gettoplist);
				Log.e("ChannelTVAdapter", "ChannelTVAdapter apigettoplist : "+api);
				aq.ajax(api, JSONObject.class, TabStreamingActivity_New.this, "getTopListCallback");
			}
		}else if(!categoryNum.equalsIgnoreCase(cate)){
			
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					streaming_loading.setVisibility(View.VISIBLE);
					streaming_loading_right.setVisibility(View.VISIBLE);
				}
			});
			
			categoryNum = cate;
			titleCategory = title;
			
			String api = context.getString(R.string.api_getlist) + categoryNum;
			Log.e("ChannelTVAdapter", "ChannelTVAdapter apigetlist : "+api);
			aq.ajax(api, JSONObject.class, TabStreamingActivity_New.this, "getListCallback");
		}
		
	}
	
	private void showPopUp() {
		if((helpDialog == null)||(helpDialog!=null&&!helpDialog.isShowing())){
			Builder helpBuilder = new Builder(context);
			helpBuilder.setTitle("");
	
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View view = inflater.inflate(R.layout.dialog_category_news,null);
			TextView txtheader = (TextView) view.findViewById(R.id.text);
			txtheader.setTypeface(Util.getPadaukBookFont(context));
			helpBuilder.setView(view);
	
			helpDialog = helpBuilder.create();
			helpDialog.show();
	
			ListView list = (ListView) view.findViewById(R.id.ls);
			String[] values = parserListHasmapToStringArr();
	
			list.setAdapter(new itemDialogAdapter(context,R.layout.item_category_dialog, values));
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,int position, long arg) {
					fetchingdata(String.valueOf(cateList.get(position).get("id")),String.valueOf(cateList.get(position).get("title")));
					helpDialog.dismiss();
				}
			});
		}
	}
	
	private String[] parserListHasmapToStringArr(){
		
		String[] item = new String[cateList.size()];
		for(int i=0;i<item.length;i++){
			item[i] = String.valueOf(cateList.get(i).get("title"));
		}
		
		return item;
	}
	
	public void isVisibleLogoRight() {
		if(streaming_categorybutton1.isSelected())
			Util.logo_right.setVisibility(View.GONE);
		else
			Util.logo_right.setVisibility(View.VISIBLE);
	}
	
	public void addFavorite() {
		if(tv!=null){
			boolean isInsert = UtilDatabase.insert(context, String.valueOf(tv.get("content_id")), "0", String.valueOf(tv.get("channel_name")), String.valueOf(tv.get("thumbnail")), String.valueOf(tv.get("share_url")), String.valueOf(tv.get("view")), String.valueOf(favoriteList.size()));
			
			if(isInsert){
				refreshList(categoryNum, titleCategory);
			}else if(!isInsert&&UtilDatabase.isInDataBase(context, String.valueOf(tv.get("content_id")), "0")){
				Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("");
		        builder.setMessage(context.getString(R.string.addfavorite_alert_added));
		        builder.setPositiveButton(R.string.button_ok, null);
		        builder.show();
			}else{
				Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("");
		        builder.setMessage(context.getString(R.string.addfavorite_alert_more));
		        builder.setPositiveButton(R.string.button_ok, null);
		        builder.show();
			}
		}
	}
	
	private void openDialogAddFavorite(){
		final Dialog dialog = new Dialog(context);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View viewdialog = inflater.inflate(R.layout.dialog_truemoveonly, null);
		Button cancel = (Button) viewdialog.findViewById(R.id.dialog_truemove_button);
		LinearLayout layout = (LinearLayout) viewdialog.findViewById(R.id.dialog_truemove_layout);
		
		Button addButton = new Button(context);
		addButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addButton.setBackgroundColor(Color.WHITE);
		addButton.setText(context.getString(R.string.addfavorite_alert_add));
		addButton.setTextColor(Color.parseColor("#ff6602"));
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFavorite();
				dialog.dismiss();
			}
		});
		
		layout.addView(addButton);
		
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
	
	private void openDialogShare(){
		
		final Dialog dialog = new Dialog(context);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View viewdialog = inflater.inflate(R.layout.dialog_truemoveonly, null);
		Button cancel = (Button) viewdialog.findViewById(R.id.dialog_truemove_button);
		LinearLayout layout = (LinearLayout) viewdialog.findViewById(R.id.dialog_truemove_layout);
		
		Button shareButton = new Button(context);
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		params.setMargins(0, 0, 0, 20);
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
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, String.valueOf(tv.get("channel_name")));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,String.valueOf(tv.get("share_url")));
				context.startActivity(Intent.createChooser(sharingIntent, "Share Action"));
				dialog.dismiss();
			}
		});
		
		ImageView line = new ImageView(context);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		line.setBackgroundResource(R.drawable.line);
		
		Button copyButton = new Button(context);
		copyButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		copyButton.setBackgroundColor(Color.WHITE);
		copyButton.setText("Copy Link");
		copyButton.setTextColor(Color.parseColor("#ff6602"));
		copyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText(String.valueOf(tv.get("channel_name")), String.valueOf(tv.get("share_url")));
				clipboard.setPrimaryClip(clip);
				Toast.makeText(context, context.getString(R.string.copytext), Toast.LENGTH_SHORT).show();
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

}
