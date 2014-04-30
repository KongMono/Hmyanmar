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
import com.jess.ui.TwoWayGridView;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ListAllTVSocietyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<List<HashMap<String, String>>> items = new Vector<List<HashMap<String,String>>>();
	private List<HashMap<String, String>> ot_item;
	protected Context context;
	private AQuery aq;
	ViewHolder holder;
	
	public class ViewHolder {
		public TextView horizontal_scroll_list_title;
		public TwoWayGridView gridview;
		public int position;
		
	}

	public ListAllTVSocietyAdapter(Context context, List<List<HashMap<String, String>>> items , List<HashMap<String, String>> ot_item) {
		this.mInflater = LayoutInflater.from(context);
		this.items = items;
		this.ot_item = ot_item;
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
		return 0;
//		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		List<HashMap<String, String>> item = items.get(position);

		View view = convertView;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.horizontal_scroll_list, parent, false);
			
			holder = new ViewHolder();
			holder.horizontal_scroll_list_title = (TextView) view.findViewById(R.id.horizontal_scroll_list_title);
			holder.gridview = (TwoWayGridView) view.findViewById(R.id.gridview);

			holder.position = position;

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.horizontal_scroll_list_title.setText(String.valueOf(ot_item.get(position).get("channel_name")));
		holder.horizontal_scroll_list_title.setTypeface(Util.getPadaukBookFont(context));
		
		holder.gridview.setAdapter(new AllTvSocietyListAdapter(context, R.layout.horizontal_list_items, items.get(position) ));
		
		return view;
	}

}
