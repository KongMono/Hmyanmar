package com.tdcm.hmyanmar.Adapter;

import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Adapter.EpisodeAdapter.ViewHolder;
import com.tdcm.hmyanmar.Dataset.EpisodeEntry;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TvSocietyLevelDListAdapter extends BaseAdapter{
	
	Context context;
	List<HashMap<String, Object>> items;
	String type;
	LayoutInflater inflater;
	AQuery aq;
	ViewHolder holder;
	Boolean island;
	
	public class ViewHolder {
		public TextView txtTitle;		
	}
	
	public TvSocietyLevelDListAdapter(Context context, List<HashMap<String, Object>> list, String type, Boolean island) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.items = list;
		this.type = type;
		this.inflater = LayoutInflater.from(context);
		this.aq = new AQuery(context);
		this.island = island;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public HashMap<String, Object> getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
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
			view = inflater.inflate(R.layout.item_list_episode, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.txtTitle.setTypeface(Util.getPadaukFont(context));
		holder.txtTitle.setTextColor(context.getResources().getColor(R.color.orange));
		if(island){
			holder.txtTitle.setSingleLine(false);
			holder.txtTitle.setMaxLines(2);
		}
		
		if(type.equals(context.getString(R.string.tvsocityleveldmenu2))){
			holder.txtTitle.setText(String.valueOf(item.get("album_title")));
		}else if(type.equals(context.getString(R.string.tvsocityleveldmenu3))){
			holder.txtTitle.setText(String.valueOf(item.get("chapter_title")));
		}else{
			holder.txtTitle.setText("");
		}
		
		return view;
	}

}
