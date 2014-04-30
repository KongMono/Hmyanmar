package com.tdcm.hmyanmar.view;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Database.UtilDatabase;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.TabstreamingActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChannelView extends LinearLayout{
	
	Context context;
	private View main_view = null;
	ImageView thumbnail;
	TextView channelname;
	ImageView deleteButton;
	
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private String content_id = "";

	public ChannelView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ChannelView(Context context, String title, String thumb, String contentid, Boolean island) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(island)
			main_view = (View) mInflater.inflate(R.layout.row_channel_land, null);
		else
			main_view = (View) mInflater.inflate(R.layout.row_channel, null);
		
		channelname = (TextView) main_view.findViewById(R.id.rowchannel_name);
		thumbnail = (ImageView) main_view.findViewById(R.id.rowchannel_image);
		deleteButton = (ImageView) main_view.findViewById(R.id.rowchannel_delete);
		
		content_id = contentid;
		
		AQuery aq = new AQuery(context);
		
		if(thumb.equals("")){
			
			thumbnail.setImageResource(R.drawable.addfav);
		
		}else{
			
			Bitmap bmp_default = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.htv_placeholder);
			WIDTH = bmp_default.getWidth();
			HEIGHT = bmp_default.getHeight();
			thumbnail.setLayoutParams(new RelativeLayout.LayoutParams(WIDTH, HEIGHT));
			thumbnail.setBackgroundResource(R.color.white);
			
			aq.id(thumbnail).image(thumb, true, true, 0, 0, bmp_default, 0);
			
		}
		
//		thumbnail.setTag(UtilAPICommon.getImageResizeAPI(context, tumb, "" + WIDTH, "" + HEIGHT));
//		CacheImageLoader.getInstance(context).load(thumbnail, UtilAPICommon.getImageResizeAPI(context, tumb, "" + WIDTH, "" + HEIGHT), true);
		
		channelname.setText(title);
		channelname.setTypeface(Util.getPadaukFont(context));
		channelname.setMaxWidth(WIDTH);
		
		addView(main_view);
		
	}
	
	public void delete(final TextView categorytitle){
		if(!content_id.equals("")){
			deleteButton.setVisibility(View.VISIBLE);
			deleteButton.setPadding(5, 0, 0, 5);
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Builder builder = new AlertDialog.Builder(context);
			        builder.setTitle("");
			        builder.setMessage(context.getString(R.string.addfavorite_alert_delete));
			        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int id) {
			            	UtilDatabase.deleteDataInDB(context, "0", content_id);
							LinearLayout parent = (LinearLayout) ChannelView.this.getParent();
							parent.removeView(ChannelView.this);
							categorytitle.setText(String.format(context.getString(R.string.categorytitle), String.valueOf(UtilDatabase.getSizeInDataBase(context))));
						
			            }
			        });
			        builder.setNegativeButton(context.getString(R.string.button_cancel), null);
			        builder.show();
				}
			});
		}
	}
	
	public void hideButton(){
		if(!content_id.equals("")){
			deleteButton.setVisibility(View.GONE);
		}
	}
	
	public String getContentID() {
		return content_id;
	}

}
