package com.tdcm.hmyanmar;

import java.util.HashMap;
import java.util.List;

import com.tdcm.hmyanmar.Adapter.ChannelTVAdapter;
import com.tdcm.hmyanmar.Adapter.itemDialogAdapter;
import com.tdcm.hmyanmar.Util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

public class TabstreamingActivity extends FragmentActivity implements OnClickListener {
	
	private static Context context;
	private ProgressBar pgb;
	private LinearLayout layout = null;
	private String catagory = null;
	
	static VideoView streaming_videoview;
	LinearLayout streaming_loadingvideoview, streaming_channelframe;
	static LinearLayout streaming_listframe;
	LinearLayout streaming_listframe_right;
	LinearLayout streaming_loading,streaming_loadinghorizontallistview_right;
	static LinearLayout streaming_textinfobottomframe, streaming_rightframe, streaming_portbottomframe, streaming_infoframe;
	RelativeLayout streaming_playpauseframe;
	static RelativeLayout streaming_buttomframe;
	Boolean isTouch;
	static Button streaming_categorybutton1;
	Button streaming_categorybutton2, streaming_categorybutton1_right, streaming_categorybutton2_right;
	static ImageView streaming_imagevideo, streaming_playpausebutton, streaming_infobutton;
	ImageView streaming_sharebutton, streaming_favouritebutton, streaming_capturebutton;
	static ImageView streaming_fullscreenbutton;
	TextView streaming_titlevideo, streaming_textinfo;
	static TextView streaming_donebutton;
	TextView streaming_titlevideoinfo, streaming_viewnum, streaming_categorytitle_right, streaming_editbutton_right;
	TextView streaming_categorytitle, streaming_editbutton, streaming_viewtext, streaming_textinfobottom;
	
	static ChannelTVAdapter adapter;
	
	int width;
	int height;
	
	private List<HashMap<String, Object>> listcate;
	
	static private Handler mHandler = new Handler();
	AlertDialog helpDialog;
	
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


	public TabstreamingActivity(String cat, LinearLayout layout,final ProgressBar pgb, Context context) throws Exception{
		this.pgb = pgb;
		this.layout = layout;
		this.catagory = cat;
		this.context = context;
		
		((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

		init();
		
		if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			MainActivity.island = true;
			if (MainActivity.isTablet) {
				if(!MainActivity.isFullScreen){
					TabstreamingActivity.landScreen();
				}
			} else {
				MainActivity.fullScreen(TabstreamingActivity.context);
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
		
		adapter = new ChannelTVAdapter(context, streaming_listframe, streaming_videoview, streaming_imagevideo, 
				streaming_titlevideo, streaming_textinfo, streaming_loading, streaming_titlevideoinfo, 
				streaming_playpausebutton, streaming_loadingvideoview, streaming_categorytitle, streaming_viewnum,
				streaming_textinfobottom, streaming_categorytitle_right, streaming_listframe_right, 
				streaming_loadinghorizontallistview_right);
		
		
		this.pgb.setVisibility(View.GONE);

		streaming_categorybutton2.performClick();
//		adapter.fetchingdata(context.getString(R.string.category1),context.getString(R.string.category1));
		
//		adapter.fetchingdata("1","All");
//		streaming_editbutton.setVisibility(View.GONE);
//		streaming_editbutton_right.setVisibility(View.GONE);
	}

	private void init() {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activty_streaming, null);
		
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
		streaming_listframe = (LinearLayout) view.findViewById(R.id.streaming_listframe);
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
		streaming_listframe_right = (LinearLayout) view.findViewById(R.id.streaming_listframe_right);
		streaming_loadinghorizontallistview_right = (LinearLayout) view.findViewById(R.id.streaming_loadinghorizontallistview_right);
		streaming_fullscreenbutton = (ImageView) view.findViewById(R.id.streaming_fullscreenbutton);
		
		Util.logo_right.setBackgroundResource(R.drawable.btn_submenu);
		Util.logo_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopUp();
			}
		});
		Util.logo_right.setVisibility(View.GONE);
		
		streaming_categorybutton1.setOnClickListener(TabstreamingActivity.this);
		streaming_categorybutton2.setOnClickListener(TabstreamingActivity.this);
		streaming_infobutton.setOnClickListener(TabstreamingActivity.this);
		streaming_sharebutton.setOnClickListener(TabstreamingActivity.this);
		streaming_favouritebutton.setOnClickListener(TabstreamingActivity.this);
		streaming_donebutton.setOnClickListener(TabstreamingActivity.this);
		streaming_playpausebutton.setOnClickListener(TabstreamingActivity.this);
		streaming_editbutton.setOnClickListener(TabstreamingActivity.this);
		streaming_capturebutton.setOnClickListener(TabstreamingActivity.this);
		
		streaming_categorybutton1_right.setOnClickListener(TabstreamingActivity.this);
		streaming_categorybutton2_right.setOnClickListener(TabstreamingActivity.this);
		streaming_editbutton_right.setOnClickListener(TabstreamingActivity.this);
		streaming_fullscreenbutton.setOnClickListener(TabstreamingActivity.this);
		
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

	public static void fullScreen() {
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				streaming_fullscreenbutton.setBackgroundResource(R.drawable.fullscreen_exit);
				streaming_listframe.setVisibility(View.GONE);
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
				streaming_listframe.setVisibility(View.VISIBLE);
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.streaming_categorybutton1:
		case R.id.streaming_categorybutton1_right:
			adapter.fetchingdata(context.getString(R.string.category1),context.getString(R.string.category1));
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
				adapter.hideDeleteButton();
				streaming_editbutton.setText(R.string.edit);
			}
			if(!streaming_categorybutton2.isSelected()&&!streaming_categorybutton2_right.isSelected()){
				adapter.fetchingdata("1","All");
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
				adapter.genStreaming();
				streaming_playpausebutton.setBackgroundResource(R.drawable.pause);
			}
			break;
		case R.id.streaming_editbutton:
		case R.id.streaming_editbutton_right:
//			Log.e("onclick", "onclick : "+streaming_editbutton.getText());
			if(streaming_editbutton.getText().equals(context.getString(R.string.edit))){
				streaming_editbutton.setText(R.string.done);
				streaming_editbutton_right.setText(R.string.done);
				adapter.deleteFavorite();
			}else{
				streaming_editbutton.setText(R.string.edit);
				streaming_editbutton_right.setText(R.string.edit);
				adapter.refreshList();
			}
			break;
		case R.id.streaming_capturebutton:			
//			Bitmap bitmap = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);                
//		    Canvas c = new Canvas(bitmap);
//		    streaming_videoview.layout(0, 0, width, height);
//		    streaming_videoview.draw(c);
//			streaming_videoview.setDrawingCacheEnabled(true);
//			Bitmap bitmap = streaming_videoview.getDrawingCache();
//            File root = Environment.getExternalStorageDirectory();
//            File file = new File(root.getAbsolutePath()+"/DCIM/Camera/img.jpg");
//            try 
//            {
//                file.createNewFile();
//                FileOutputStream ostream = new FileOutputStream(file);
//                bitmap.compress(CompressFormat.JPEG, 100, ostream);
//                ostream.close();
//            } 
//            catch (Exception e) 
//            {
//                e.printStackTrace();
//            }
//            Toast.makeText(context, "Save Image", Toast.LENGTH_SHORT).show();
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
	
	public static void puaseVideo() {
		if(streaming_videoview.isPlaying()){
			streaming_playpausebutton.performClick();
		}
	}
	
	public static void playVideo() {
		adapter.autoPlay();
	}
	
	public static void refreshList(){
		adapter.refreshList();
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
			listcate = adapter.getListCategoty();
			String[] values = parserListHasmapToStringArr();
	
			list.setAdapter(new itemDialogAdapter(context,R.layout.item_category_dialog, values));
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,int position, long arg) {
					adapter.fetchingdata(String.valueOf(listcate.get(position).get("id")),String.valueOf(listcate.get(position).get("title")));
					helpDialog.dismiss();
				}
			});
		}
	}
	
	private String[] parserListHasmapToStringArr(){
		
		String[] item = new String[listcate.size()];
		for(int i=0;i<item.length;i++){
			item[i] = String.valueOf(listcate.get(i).get("title"));
		}
		
		return item;
	}
	
	public static void isVisibleLogoRight() {
		if(streaming_categorybutton1.isSelected())
			Util.logo_right.setVisibility(View.GONE);
		else
			Util.logo_right.setVisibility(View.VISIBLE);
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
				adapter.addFavorite();
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
				HashMap<String, Object> item = adapter.getTVObject();
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, String.valueOf(item.get("channel_name")));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,String.valueOf(item.get("share_url")));
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
				HashMap<String, Object> item = adapter.getTVObject();
				ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText(String.valueOf(item.get("channel_name")), String.valueOf(item.get("share_url")));
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
