package com.tdcm.hmyanmar.Adapter;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tdcm.hmyanmar.AllTvSocietyLevelDAcyivity;
import com.tdcm.hmyanmar.R;

public class AllTvSocietyListAdapter extends ArrayAdapter<HashMap<String, String>> {

	Handler handle = new Handler();
	
	public class ViewHolder {
		public ImageView horizontal_scroll_item_img_syncs;
		public ImageView horizontal_scroll_item_img;
		public TextView horizontal_scroll_item_txt;
		public int position;

	}

	Context context;
	private List<HashMap<String, String>> items;
	AQuery aq;
	int resId = 0;

	public AllTvSocietyListAdapter(Context context, int textViewResourceId, List<HashMap<String, String>> items) {

		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
		resId = textViewResourceId;
		aq = new AQuery(context);
		//Enable hardware acceleration if the device has API 11 or above
		if (Build.VERSION.SDK_INT > 11) {
			aq.hardwareAccelerated11();
		}
		
	}

	public int getCount() {
		return items.size();
	}

	public HashMap<String, String> getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		View v = convertView;
		final int positions = position;

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


		final HashMap<String, String> o = items.get(positions);
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
					handle.postDelayed(new Runnable() {
						public void run() {
							vs.setTag(null);
						}
					}, 1000);

					// TODO when clicked

					Intent intent = new Intent(context, AllTvSocietyLevelDAcyivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("content_id", String.valueOf(o.get("content_id")));
					intent.putExtra("share_url", String.valueOf(o.get("share_url")));
					intent.putExtra("type", "all");
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

				}
			});

		}

		return v;
	}
}