package com.tdcm.hmyanmar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Dataset.ListHClusiveEntry;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

import java.util.ArrayList;
import java.util.List;

public class ListHClusiveAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ListHClusiveEntry> items = new ArrayList<ListHClusiveEntry>();
	protected Context context;
	private AQuery aq;
	ViewHolder holder;
	
	public class ViewHolder {
		public TextView txtTitle;
		public ImageView imgThumbnail;
		public int position;
		
	}

	public ListHClusiveAdapter(Context context, ArrayList<ListHClusiveEntry> items) {
		this.mInflater = LayoutInflater.from(context);
		this.items = items;
		this.context = context;
		aq = new AQuery(context);
		
		//Enable hardware acceleration if the device has API 11 or above
		if (Build.VERSION.SDK_INT > 11) {
			aq.hardwareAccelerated11();
		}
	}

	@Override
	public int getCount() {

		return items.size();
	}

	@Override
	public Object getItem(int pos) {
		return items.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ListHClusiveEntry item = items.get(position);

		View view = convertView;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_list_news, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
			holder.imgThumbnail = (ImageView) view.findViewById(R.id.image);

			holder.position = position;

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtTitle.setText(item.getTitle());
		holder.txtTitle.setTypeface(Util.getPadaukBookFont(context));
		
		AQuery AqLoad = aq.recycle(view);
		Bitmap preset = AqLoad.getCachedImage(R.drawable.bg_button);
		AqLoad.id(holder.imgThumbnail).image(item.getThumbnail(), true, true, 0, 0, preset,AQuery.FADE_IN_FILE,AQuery.CACHE_DEFAULT);
		return view;
	}

}
