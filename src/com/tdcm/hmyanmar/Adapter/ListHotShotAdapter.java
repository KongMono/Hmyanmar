package com.tdcm.hmyanmar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.Adapter.AllTvSocietyListAdapter.ViewHolder;
import com.tdcm.hmyanmar.Dataset.ListNewsEntry;
import com.tdcm.hmyanmar.Util.Util;
import com.tdcm.hmyanmar.R;
import com.tdcm.hmyanmar.TVSocietyInstagramActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ListHotShotAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<HashMap<String, String>> items = new Vector<HashMap<String,String>>();
	protected Context context;
	private AQuery aq;
	ViewHolder holder;
	int resId = 0;
	Handler handler = new Handler();
	
	public class ViewHolder {
		public ImageView horizontal_scroll_item_img_syncs;
		public ImageView horizontal_scroll_item_img;
		public TextView horizontal_scroll_item_txt;
		public int position;
		
	}

	public ListHotShotAdapter(Context context, List<HashMap<String, String>> items , int resId) {
		this.mInflater = LayoutInflater.from(context);
		this.items = items;
		this.context = context;
		this.resId = resId;
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
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(resId, null);

			holder = new ViewHolder();

			holder.horizontal_scroll_item_img = (ImageView) v.findViewById(R.id.horizontal_scroll_item_img);
			holder.horizontal_scroll_item_img_syncs = (ImageView) v.findViewById(R.id.horizontal_scroll_item_img_syncs);

			holder.horizontal_scroll_item_txt = (TextView) v.findViewById(R.id.horizontal_scroll_item_txt);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		

//		v.setLayoutParams(new AbsListView.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.poster_w_tumb),context.getResources().getDimensionPixelSize(R.dimen.poster_h_tumb)));
		
		
		final HashMap<String, String> o = items.get(position);
		if (o != null) {

			holder.horizontal_scroll_item_txt.setText(String.valueOf(o.get("title")));

			String url = "http://imageservice.truelife.com/file.php?size=500x375&image=" + String.valueOf(o.get("thumbnail"));

			// aq.id(holder.ondemand_item_img).progress(holder.ondemand_item_img_syncs).image(String.valueOf(o.get("thumbnail")));
			aq.id(holder.horizontal_scroll_item_img).progress(holder.horizontal_scroll_item_img_syncs).image(url);

			final View vs = holder.horizontal_scroll_item_img;

			vs.setTag(null);
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					//check clickable
					
					if (vs.getTag() != null) {
						return;
					}

					vs.setTag("click");
					handler.postDelayed(new Runnable() {
						public void run() {
							vs.setTag(null);
						}
					}, 1000);

					// TODO when clicked
					
					try {
						Intent intent = new Intent(context, TVSocietyInstagramActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("content_id", String.valueOf(o.get("content_id")));
						intent.putExtra("title", String.valueOf(o.get("title")));
						intent.putExtra("share_url", String.valueOf(o.get("share_url")));
					    context.startActivity(intent);
						((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});

		}
		
		return v;
	}

}
