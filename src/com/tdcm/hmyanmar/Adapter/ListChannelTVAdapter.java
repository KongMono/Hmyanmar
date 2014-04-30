package com.tdcm.hmyanmar.Adapter;

import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.view.HorizontalListView;
import com.tdcm.hmyanmar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListChannelTVAdapter extends BaseAdapter{
	
	Context context;
	LayoutInflater mInflater;
	View gridView;
	List<HashMap<String, Object>> items;
	Boolean island , isDelete;
	ViewHolder holder;
	AQuery aq;
	
	Bitmap bmp_default;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	
	public class ViewHolder {
		public TextView title;
		public ImageView image;
		public ImageView imagecheck;		
	}
	
	public ListChannelTVAdapter(Context context, List<HashMap<String, Object>> list, Boolean island, HorizontalListView gridView) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		aq = new AQuery(context);
		this.items = list;
		this.island = island;
		this.gridView = gridView;
		this.isDelete = false;
		
		bmp_default = BitmapFactory.decodeResource(context.getResources(), R.drawable.htv_placeholder);
		WIDTH = bmp_default.getWidth();
		HEIGHT = bmp_default.getHeight();
	}
	
	public ListChannelTVAdapter(Context context, List<HashMap<String, Object>> list, Boolean island, ListView gridView) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		aq = new AQuery(context);
		this.items = list;
		this.island = island;
		this.gridView = gridView;
		this.isDelete = false;
		
		bmp_default = BitmapFactory.decodeResource(context.getResources(), R.drawable.htv_placeholder);
		WIDTH = bmp_default.getWidth();
		HEIGHT = bmp_default.getHeight();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;

		if (convertView == null) {
			
			if(island)
				view = mInflater.inflate(R.layout.row_channel_land, parent, false);
			else
				view = mInflater.inflate(R.layout.row_channel, parent, false);
			
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.rowchannel_name);
			holder.image = (ImageView) view.findViewById(R.id.rowchannel_image);
			holder.imagecheck = (ImageView) view.findViewById(R.id.rowchannel_delete);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		HashMap<String, Object> item = getItem(position);
		String channel_name = String.valueOf(item.get("channel_name"));
		
		if(!channel_name.equalsIgnoreCase("")){
			
			holder.image.setLayoutParams(new RelativeLayout.LayoutParams(WIDTH, HEIGHT));
			holder.image.setBackgroundResource(R.color.white);
			aq.id(holder.image).image(String.valueOf(item.get("thumbnail")), true, true, 100,  R.drawable.htv_placeholder);
			
			holder.title.setText(channel_name);
			holder.title.setTypeface(Util.getPadaukFont(context));
			holder.title.setMaxWidth(WIDTH);
		}else{
			holder.image.setLayoutParams(new RelativeLayout.LayoutParams(WIDTH, HEIGHT));
			holder.image.setBackgroundResource(R.color.white);
			holder.image.setImageResource(R.drawable.addfav);
			holder.title.setText(channel_name);
		}
		
		if(isDelete){
			if(channel_name.equalsIgnoreCase("")){
				return null;
			}
			holder.imagecheck.setVisibility(View.VISIBLE);
//			view.setOnClickListener(new OnClickListener() {
//				
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					items.remove(position);
////					int po = gridView.getFirstVisiblePosition();
//					notifyDataSetChanged();
////					gridView.setSelection(po);
//				}
//			});
		}else{
			holder.imagecheck.setVisibility(View.GONE);
//			view.setOnClickListener(null);
		}
		
		return view;
	}
	
	public void setisDelete(Boolean boolean1){
		isDelete = boolean1;
	}
	
	public boolean getisDelete(){
		return isDelete;
	}

}
