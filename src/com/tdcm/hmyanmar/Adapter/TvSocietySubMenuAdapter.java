package com.tdcm.hmyanmar.Adapter;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TvSocietySubMenuAdapter extends BaseAdapter{
	
	protected Context context;
	private String[] items;
	private int[] imageItem = {R.drawable.icn_submenu01, R.drawable.icn_submenu02, R.drawable.icn_submenu03,
			R.drawable.icn_submenu04, R.drawable.icn_submenu05, R.drawable.icn_submenu06};
	private int[] imageItemInactive = {R.drawable.icn_submenu01_inactive, R.drawable.icn_submenu02_inactive, 
			R.drawable.icn_submenu03_inactive, R.drawable.icn_submenu04_inactive, R.drawable.icn_submenu05_inactive,
			R.drawable.icn_submenu06_inactive};
	private String[] contentItem;
	private LayoutInflater inflater;
	private Boolean island;
	ViewHolder holder;
	AQuery aq;

	public class ViewHolder {
		public TextView txtTitle;
		public ImageView image;
	}
	
	public TvSocietySubMenuAdapter(Context context, String[] content, Boolean island){
		// TODO Auto-generated constructor stub
		this.context = context;
		this.items = context.getResources().getStringArray(R.array.tvsocityleveldmenu);
		this.inflater = LayoutInflater.from(context);
		this.contentItem = content;
		this.island = island;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public String[] getItem(int position) {
		// TODO Auto-generated method stub
		String[] item = {items[position],contentItem[position]};
		return item;
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
				view = (View) inflater.inflate(R.layout.row_channel_land, null);
			else
				view = (View) inflater.inflate(R.layout.row_channel, null);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.rowchannel_name);
			holder.txtTitle.setTypeface(Util.getPadaukFont(context));
			holder.image = (ImageView) view.findViewById(R.id.rowchannel_image);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtTitle.setText(items[position]);
		
		if(contentItem[position].equals("0")){
			holder.image.setBackgroundResource(imageItemInactive[position]);
			holder.txtTitle.setTextColor(Color.GRAY);
		}else{
			holder.image.setBackgroundResource(imageItem[position]);
			holder.txtTitle.setTextColor(Color.BLACK);
		}
		
		return view;
	}

}
