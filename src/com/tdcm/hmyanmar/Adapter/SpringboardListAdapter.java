package com.tdcm.hmyanmar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Dataset.SBEntry;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

public class SpringboardListAdapter extends BaseAdapter {

	private AQuery aq;
	private LayoutInflater mInflater;
	private List<SBEntry> items = new ArrayList<SBEntry>();

	protected Context context;

	public class ViewHolder {
		public TextView text;
		public ImageView image;
		public int position;
	}

	public SpringboardListAdapter(Context context, List<SBEntry> items) {
		this.mInflater = LayoutInflater.from(context);
		this.items = items;

		this.context = context;
		aq = new AQuery(context);

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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		SBEntry item = items.get(position);

		View view = convertView;
		ViewHolder holder;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_springboard, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.txtTitle);
			holder.image = (ImageView) view.findViewById(R.id.imgThum);
			holder.position = position;

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.text.setText(item.getTitle());

		try {
			holder.text.setTypeface(Util.getPadaukBookFont(context));
			holder.text.setTextSize(20f);
		} catch (Exception e) {
		}

		aq.id(holder.image).image(item.getIcon(), true, true, 0,R.drawable.ic_launcher);

		return view;
	}
}
