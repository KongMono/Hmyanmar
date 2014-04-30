package com.tdcm.hmyanmar.Adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tdcm.hmyanmar.Database.UtilDatabase;
import com.tdcm.hmyanmar.Json.JsonParser;
import com.tdcm.hmyanmar.view.ChannelView;
import com.tdcm.hmyanmar.AddFavoriteActivity;
import com.tdcm.hmyanmar.MainActivity;
import com.tdcm.hmyanmar.R;
import com.truelife.mobile.android.media.StreamingGenerator;
import com.truelife.mobile.android.media.StreamingGenerator.StreamingListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ChannelTVAdapter implements StreamingListener  {
	
	Context context;
	LinearLayout listframe, listframeland = null;
	VideoView videoview = null;
	ImageView loading, playpausebutton = null;
	TextView tltle, info, titleinfo, categorytitle, viewtext = null;
	TextView titleinfoland, categorytitleland, editbuttonland;
	LinearLayout loadinghorizontal, loadinghorizontallistviewland, loadingvideoview = null;
	
	List<HashMap<String, Object>> catelist;
	List<HashMap<String, Object>> datalist;
	List<HashMap<String, Object>> favoriteList;
	HashMap<String, Object> streaming = null;
	HashMap<String, Object> tv = null;
	int refresh_count = 3;	
	
	JsonParser json = new JsonParser();
	Handler handler = new Handler();
	AlertDialog.Builder builder = null;
	
	StreamingGenerator streamingGenerator;
	DecimalFormat formatter;
	
	AQuery aq;
	
	String category = "";
	String titleCategory = "";
	Boolean firsttime = true;
	Boolean isTablet;
	
	public ChannelTVAdapter(Context mcontext, LinearLayout linearframe, VideoView videoView, ImageView loadingImage, 
			TextView titlevideo, TextView textinfo, LinearLayout loadinghorizontallistview, TextView titlevideoinfo, 
			ImageView playpausebutton, LinearLayout loadingvideoview, TextView categorytext, TextView viewnumtext, 
			TextView textinfoland, TextView categorytitleland,LinearLayout listframeland, 
			LinearLayout loadinghorizontallistviewland) {
		// TODO Auto-generated constructor stub
		
		this.context = mcontext;
		this.listframe = linearframe;
		this.videoview = videoView;
		this.loading = loadingImage;
		this.tltle = titlevideo;
		this.info = textinfo;
		this.loadinghorizontal = loadinghorizontallistview;
		this.titleinfo = titlevideoinfo;
		this.playpausebutton = playpausebutton;
		this.loadingvideoview = loadingvideoview;
		this.categorytitle = categorytext;
		this.viewtext = viewnumtext;
		this.titleinfoland = textinfoland;
		this.categorytitleland = categorytitleland;
		this.listframeland = listframeland;
		this.loadinghorizontallistviewland = loadinghorizontallistviewland;
		
		catelist = new ArrayList<HashMap<String,Object>>();
		datalist = new ArrayList<HashMap<String,Object>>();
		favoriteList = new ArrayList<HashMap<String,Object>>();
		
		streamingGenerator = new StreamingGenerator(context, this, context.getString(R.string.ref_app_name));
		aq = new AQuery(context);
		formatter = new DecimalFormat("###,###,###");
		isTablet = Boolean.parseBoolean(context.getString(R.string.istablet));
	}
	
	public void fetchingdata (String cate, String title){
		if(!category.equalsIgnoreCase(cate)){
			loadinghorizontal.setVisibility(View.VISIBLE);
			loadinghorizontallistviewland.setVisibility(View.VISIBLE);
			category = cate;
			titleCategory = title;
			if(category.equalsIgnoreCase(context.getString(R.string.category1))){
				favoriteList = UtilDatabase.getDataListInDB(context);
				if(favoriteList.size()>0){
					changeDataList(favoriteList);
				}else{
					String api = context.getString(R.string.api_gettoplist);
					Log.e("ChannelTVAdapter", "ChannelTVAdapter apigettoplist : "+api);
					aq.ajax(api, JSONObject.class, ChannelTVAdapter.this, "getTopListCallback");
				}
			}else{
				String api = context.getString(R.string.api_getlist) + category;
				Log.e("ChannelTVAdapter", "ChannelTVAdapter apigetlist : "+api);
				aq.ajax(api, JSONObject.class, ChannelTVAdapter.this, "getListCallback");
			}
		}
	}
	
	private void getInfo(String contentid){
		
		try {
			this.videoview.stopPlayback();
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
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							((View) ChannelTVAdapter.this.loading.getParent()).setVisibility(View.VISIBLE);
							((View) ChannelTVAdapter.this.loading.getParent()).setBackgroundResource(R.color.white);
							ChannelTVAdapter.this.loading.setImageBitmap(null);
								
//							HashMap<String, Object> o = datalist.get(positionPlay);
							
							aq.id(loading).image(String.valueOf(tv.get("thumbnail")), true, true);
							
							ChannelTVAdapter.this.tltle.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
							ChannelTVAdapter.this.titleinfo.setText(String.valueOf(result.get("channel_title")).replaceAll("&nbsp;", " "));
							ChannelTVAdapter.this.info.setText(String.valueOf(result.get("detail")).replaceAll("&nbsp;", " "));
							ChannelTVAdapter.this.titleinfoland.setText(String.valueOf(result.get("detail")).replaceAll("&nbsp;", " "));
							double viewnum = Integer.parseInt(String.valueOf(result.get("views")).equals("null")?"0":String.valueOf(result.get("views")));
							ChannelTVAdapter.this.viewtext.setText("("+formatter.format(viewnum)+")");
							
							genStreaming();
							
						}
					});
				}
			}
			
		});
		
	}

	public void genStreaming(){
		if(streaming!=null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					ChannelTVAdapter.this.playpausebutton.setBackgroundResource(R.drawable.play);
					ChannelTVAdapter.this.loadingvideoview.setVisibility(View.VISIBLE);
					((View) ChannelTVAdapter.this.loading.getParent()).setVisibility(View.VISIBLE);
					streamingGenerator.start(String.valueOf(streaming.get("stream_content_id")), String.valueOf(streaming.get("stream_product_id")), String.valueOf(streaming.get("stream_project_id")), String.valueOf(streaming.get("stream_scope")), String.valueOf(streaming.get("stream_content_type")), String.valueOf(streaming.get("stream_lifestyle")));
				}
			});
		}
	}
	
	public void getListCallback(String url, JSONObject json, AjaxStatus status){
		if(json != null){
			refresh_count = 3;		
			try {
				JSONObject response = json.getJSONObject("response");
				if(catelist.size()==0){
					HashMap<String, Object> result = this.json.parserListCate(response.getString("menu"));
					if(result.get("code").equals("200"))
						catelist = (List<HashMap<String, Object>>) result.get("menu");
					
				}
				
				HashMap<String, Object> result = this.json.parserList(response.getString("data"));
				if(result.get("code").equals("200")&&!category.equalsIgnoreCase(context.getString(R.string.category1))){
					changeDataList((List<HashMap<String, Object>>) result.get("data"));
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(refresh_count>0){
			refresh_count -= 1;
			String api = context.getString(R.string.api_getlist) + category;
			aq.ajax(api, JSONObject.class, ChannelTVAdapter.this, "getListCallback");
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
					if(category.equalsIgnoreCase(context.getString(R.string.category1))){
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
			aq.ajax(api, JSONObject.class, ChannelTVAdapter.this, "getTopListCallback");
		}else{
			refresh_count = 3;
		}
	}
	
	private void changeDataList(final List<HashMap<String, Object>> temp_list) {
		try {
			datalist.clear();
		} catch(Exception e) {
			
		}
		
		for(int i=0; i<temp_list.size(); i++) {
			datalist.add((HashMap<String, Object>) temp_list.get(i));
		}
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				listframe.removeAllViews();
				listframeland.removeAllViews();
				for(int i=0;i<temp_list.size();i++){
//					final int po = i;
					final HashMap<String, Object> o = temp_list.get(i);
					
					ChannelView channelView = new ChannelView(context, String.valueOf(o.get("channel_name")).replaceAll("&nbsp;", " "), String.valueOf( o.get("thumbnail")), String.valueOf(o.get("content_id")), false);
					
					channelView.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(tv!=o){
								tv = o;
								getInfo(String.valueOf(o.get("content_id")));
							}
						}
					});
					
					listframe.addView(channelView);
					
					if(isTablet){

						ChannelView channelView2 = new ChannelView(context, String.valueOf(o.get("channel_name")).replaceAll("&nbsp;", " "), String.valueOf( o.get("thumbnail")), String.valueOf(o.get("content_id")), true);
						
						channelView2.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(tv!=o){
									tv = o;
									getInfo(String.valueOf(o.get("content_id")));
								}
							}
						});
						
						
						listframeland.addView(channelView2);
					}
					
				}
				
				if(titleCategory.equals(context.getString(R.string.category1))){
					categorytitle.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(favoriteList.size())));
					categorytitleland.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(favoriteList.size())));
					if(listframe.getChildCount()<20){
						ChannelView chView = new ChannelView(context, "", "", "", false);
						chView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context, AddFavoriteActivity.class);
								context.startActivity(intent);
							}
						});
						listframe.addView(chView);
						
						if(isTablet){
							ChannelView chView2 = new ChannelView(context, "", "", "", true);
							chView2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(context, AddFavoriteActivity.class);
									context.startActivity(intent);
								}
							});
							listframeland.addView(chView2);
						}
					}
				}else{
					for(int i=0;i<catelist.size();i++){
						HashMap<String, Object> obj = catelist.get(i);
						if(category.equals(String.valueOf(obj.get("id")))){
							titleCategory = String.valueOf(obj.get("title"));
							break;
						}
					}
					categorytitle.setText(titleCategory);
					categorytitleland.setText(titleCategory);
				}
				
				try {
					((HorizontalScrollView) listframe.getParent()).scrollTo(0, 0);
					((ScrollView) listframeland.getParent()).scrollTo(0, 0);
				} catch (Exception e) {
					// TODO: handle exception
										
				}
				
				loadinghorizontal.setVisibility(View.GONE);
				loadinghorizontallistviewland.setVisibility(View.GONE);
				
			}
		});
		
		if(firsttime){
			firsttime = false;
			performItemClickAt(0);
		}
	}
	
	private void performItemClickAt(int position) {
		
		if(datalist!=null&&datalist.size()>0){
			HashMap<String, Object> o = datalist.get(position);
//			positionPlay = position;
			if(tv!=o){
				tv = o;
				getInfo(String.valueOf(o.get("content_id")));
			}
//			autoclick = false;
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
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Can't Play", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void prepareVideo(final String url) {

		try {
			this.videoview.stopPlayback();
		} catch (Exception e) {
		}
		 
		this.videoview.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(MainActivity.btnStreaming.isSelected()){
							ChannelTVAdapter.this.videoview.start();
							ChannelTVAdapter.this.playpausebutton.setBackgroundResource(R.drawable.pause);
							ChannelTVAdapter.this.loadingvideoview.setVisibility(View.GONE);
							((View) ChannelTVAdapter.this.loading.getParent()).setVisibility(View.GONE);
						}
					}
				});
				
			}
		});
		this.videoview.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(context, "Can't Play", Toast.LENGTH_SHORT).show();
						try {
							videoview.stopPlayback();
						} catch (Exception e) {
						}
						ChannelTVAdapter.this.loadingvideoview.setVisibility(View.GONE);
						((View) ChannelTVAdapter.this.loading.getParent()).setVisibility(View.VISIBLE);
					}
				});
				
				return false;
			}
		});
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ChannelTVAdapter.this.videoview.setVideoPath(url);
			}
		});
		
			
	}
	
	public void autoPlay() {
		
		if(tv!=null){
			genStreaming();
		}else{
			performItemClickAt(0);
		}
		
	}
	
	public List<HashMap<String, Object>> getListCategoty(){
		return catelist;
	}
	
	public void addFavorite() {
		if(tv!=null){
			boolean isInsert = UtilDatabase.insert(context, String.valueOf(tv.get("content_id")), "0", String.valueOf(tv.get("channel_name")), String.valueOf(tv.get("thumbnail")), String.valueOf(tv.get("share_url")), String.valueOf(tv.get("view")), String.valueOf(favoriteList.size()));
			
			if(isInsert){
				refreshList();
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
	
	public void deleteFavorite(){
		for(int i=0;i<listframe.getChildCount();i++){
			ChannelView view = (ChannelView) listframe.getChildAt(i);
			view.delete(categorytitle);
			if(view.getContentID().equals("")){
				listframe.removeView(view);
			}
			if(isTablet){
				ChannelView viewland = (ChannelView) listframeland.getChildAt(i);
				viewland.delete(categorytitleland);
				if(viewland.getContentID().equals("")){
					listframe.removeView(viewland);
				}
			}
		}
	}
	
	public void hideDeleteButton(){
		for(int i=0;i<listframe.getChildCount();i++){
			ChannelView view = (ChannelView) listframe.getChildAt(i);
			view.hideButton();
			if(isTablet){
				ChannelView viewland = (ChannelView) listframeland.getChildAt(i);
				viewland.hideButton();
			}
		}
	}
	
	public void refreshList() {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(category.equalsIgnoreCase(context.getString(R.string.category1))){
					favoriteList = UtilDatabase.getDataListInDB(context);
					if(favoriteList.size()>0){
						changeDataList(favoriteList);
					}else if(favoriteList.size()==0){
						categorytitle.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(favoriteList.size())));
						categorytitleland.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(favoriteList.size())));
						listframe.removeAllViews();
						listframeland.removeAllViews();
						ChannelView chView = new ChannelView(context, "", "", "", false);
						chView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context, AddFavoriteActivity.class);
								context.startActivity(intent);
							}
						});
						listframe.addView(chView);
						
						if(isTablet){
							ChannelView chView2 = new ChannelView(context, "", "", "", true);
							chView2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(context, AddFavoriteActivity.class);
									context.startActivity(intent);
								}
							});
							listframeland.addView(chView2);
						}
					}
					
				}else{
					String api = context.getString(R.string.api_getlist) + category;
					Log.e("ChannelTVAdapter", "ChannelTVAdapter apigetlist : "+api);
					aq.ajax(api, JSONObject.class, ChannelTVAdapter.this, "getListCallback");
				}
			}
		});
		
	}
	
	public HashMap<String, Object> getTVObject() {
		return tv;
	}

}
