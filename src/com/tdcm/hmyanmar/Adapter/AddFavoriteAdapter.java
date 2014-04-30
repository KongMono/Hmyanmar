package com.tdcm.hmyanmar.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.Database.UtilDatabase;
import com.tdcm.hmyanmar.Util.Util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddFavoriteAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<HashMap<String, Object>> items;
	private List<HashMap<String, Object>> favoriteList;
	private List<HashMap<String, Object>> newfavoriteList;
	private List<HashMap<String, Object>> deleteList;
	private Context context;
	private AQuery aq;
	private TextView textView;
	private Handler handler;
	ViewHolder holder;
	
	private Bitmap bmp_default,bmp_holder;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	
	public class ViewHolder {
		public TextView title;
		public ImageView image;
		public ImageView imagecheck;		
	}
	
	public AddFavoriteAdapter(Context mContext, List<HashMap<String, Object>> listItem, TextView text){
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.items = listItem;
		this.textView = text;
		this.mInflater = LayoutInflater.from(context);
		aq = new AQuery(context);
		handler = new Handler();
		
		bmp_default = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.btn_more);
		bmp_holder = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.htv_placeholder);
		WIDTH = bmp_default.getWidth();
		HEIGHT = bmp_default.getHeight();
		
		favoriteList = UtilDatabase.getDataListInDB(context);
		newfavoriteList = new ArrayList<HashMap<String,Object>>();
		deleteList = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<items.size();i++){
			HashMap<String, Object> item = items.get(i);
			if(isInFavoriteList(String.valueOf(item.get("content_id")))){
				newfavoriteList.add(item);
			}
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = convertView;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.row_channel, parent, false);
			
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.rowchannel_name);
			holder.image = (ImageView) view.findViewById(R.id.rowchannel_image);
			holder.imagecheck = (ImageView) view.findViewById(R.id.rowchannel_delete);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		final HashMap<String, Object> item = items.get(position);

		holder.title.setText(String.valueOf(item.get("channel_name")));
		holder.title.setTypeface(Util.getPadaukFont(context));
		holder.title.setMaxWidth(WIDTH);
		holder.title.setPadding(0, 0, 0, 30);
		
		holder.image.setLayoutParams(new RelativeLayout.LayoutParams(WIDTH, HEIGHT));
		holder.image.setBackgroundResource(R.color.white);
		holder.image.setScaleType(ScaleType.FIT_CENTER);
		aq.id(holder.image).image(String.valueOf(item.get("thumbnail")), true, true, 0, 0, bmp_holder, 0);
		
		holder.imagecheck.setVisibility(View.VISIBLE);
		if(isInNewFavoriteList(String.valueOf(item.get("content_id")))){
			holder.imagecheck.setBackgroundResource(R.drawable.btn_select_active);
		}else{
			holder.imagecheck.setBackgroundResource(R.drawable.btn_select);
		}
		
		setTextOnCheck();
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isInNewFavoriteList(String.valueOf(item.get("content_id")))){
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							holder.imagecheck.setBackgroundResource(R.drawable.btn_select);
							newfavoriteList.remove(item);
							deleteList.add(item);
							setTextOnCheck();
							notifyDataSetChanged();
						}
					});
				}else if(newfavoriteList.size()<20){
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							holder.imagecheck.setBackgroundResource(R.drawable.btn_select_active);
							newfavoriteList.add(item);
							deleteList.remove(item);
							setTextOnCheck();
							notifyDataSetChanged();
						}
					});
				}else{
					Builder builder = new AlertDialog.Builder(context);
			        builder.setTitle("");
			        builder.setMessage(context.getString(R.string.addfavorite_alert_more));
			        builder.setPositiveButton(R.string.button_ok, null);
			        builder.show();
				}
			}
		});
		
		return view;
	}
	
	private boolean isInFavoriteList(String content_id){
		for(int i=0;i<favoriteList.size();i++){
			HashMap<String, Object> item = favoriteList.get(i);
			if(String.valueOf(item.get("content_id")).equals(content_id)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isInNewFavoriteList(String content_id){
		for(int i=0;i<newfavoriteList.size();i++){
			HashMap<String, Object> item = newfavoriteList.get(i);
			if(String.valueOf(item.get("content_id")).equals(content_id)){
				return true;
			}
		}
		return false;
	}
	
	public List<HashMap<String, Object>> getNewFavoriteList() {
		return newfavoriteList;
	}
	
	public List<HashMap<String, Object>> getDeleteList() {
		return deleteList;
	}
	
	private void setTextOnCheck(){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textView.setText(String.format(context.getString(R.string.categorytitle), newfavoriteList.size()));
			}
		});
	}

}
