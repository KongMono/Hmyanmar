package com.tdcm.hmyanmar.Adapter;

import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TvSocietyLevelCAdapter extends BaseAdapter{
	
	private Context context;
	private LayoutInflater mInflater;
	private List<HashMap<String, Object>> items;

	private AQuery aq;
	ViewHolder holder;
	
	public TvSocietyLevelCAdapter(Context context, List<HashMap<String, Object>> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.items = list;
		aq = new AQuery(context);
	}
	
	public class ViewHolder {
		public TextView txtTitle;
		public ImageView imgThumbnail;		
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
		HashMap<String, Object> item = items.get(position);

		View view = convertView;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_list_news, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
			holder.imgThumbnail = (ImageView) view.findViewById(R.id.image);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtTitle.setText(String.valueOf(item.get("chapter_title")));
		holder.txtTitle.setTypeface(Util.getPadaukBookFont(context));
		
		Bitmap preset = aq.getCachedImage(R.drawable.bg_button);
		aq.id(holder.imgThumbnail).image(String.valueOf(item.get("thumbnail")), true, true, 0, 0, preset,AQuery.FADE_IN_FILE,AQuery.CACHE_DEFAULT);
		return view;
	}

}
