package com.tdcm.hmyanmar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

public class itemDialogAdapter extends ArrayAdapter<String> {

	protected Context context;
	private String[] itemsData;
	private LayoutInflater inflater;
	ViewHolder holder;
	AQuery aq;

	int textViewResourceId;

	public class ViewHolder {
		public TextView txtTitle;
		public int position;
	}

	public itemDialogAdapter(Context context, int textViewResourceId,String[] str) {
		super(context, textViewResourceId, str);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.itemsData = str;
		this.textViewResourceId = textViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(textViewResourceId, parent, false);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.text);
			holder.txtTitle.setTypeface(Util.getPadaukBookFont(context));
			holder.txtTitle.setTextSize(22f);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtTitle.setText(itemsData[position]);

		return view;
	}
}