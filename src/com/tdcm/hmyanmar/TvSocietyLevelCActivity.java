package com.tdcm.hmyanmar;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tdcm.hmyanmar.Adapter.TvSocietyLevelCAdapter;
import com.tdcm.hmyanmar.Json.TvSocietyLevelDParser;
import com.tdcm.hmyanmar.Util.Util;
import com.truelife.mobile.android.access_blocking.util.Statistic;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TvSocietyLevelCActivity extends CoreActivity {

	private ListView list;
	private TextView header_title;

	private AQuery aq;
	private TvSocietyLevelCAdapter adapter;
	private TvSocietyLevelDParser jsonParser;
	private Bundle bundle;
	private GoogleAnalyticsTracker tracker;

	private String url, header;
	private List<HashMap<String, Object>> listItem;
	int refresh_count = 3;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activty_list_episode);

		bundle = getIntent().getExtras();
		url = bundle.getString("url");
		header = bundle.getString("header");

		init();
		callAPI();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(getString(R.string.analytic), this);
		tracker.trackPageView(String.format(getString(R.string.stat),header) + "C");
		Statistic.tracking(String.format(getString(R.string.stat),header) + "C", header);

	}

	private void init() {

		aq = new AQuery(this);
		jsonParser = new TvSocietyLevelDParser();

		header_title = (TextView) findViewById(R.id.header_title);
		list = (ListView) findViewById(R.id.list);
		list.setPadding(10, 3, 10, 0);

		header_title.setTypeface(Util.getPadaukBookFont(this));
		header_title.setText(header);

	}

	private void callAPI() {

		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				if (object != null) {
					try {

						JSONObject content = object.getJSONObject("content");
						JSONObject data = content.getJSONObject("data");
						listItem = jsonParser.getItemsList(data.getJSONArray("item"));
						if (listItem != null && listItem.size()>0) {
							adapter = new TvSocietyLevelCAdapter(
									TvSocietyLevelCActivity.this, listItem);
							list.setAdapter(adapter);
							list.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View view, int position, long arg) {
									// TODO Auto-generated method stub
									HashMap<String, Object> item = adapter
											.getItem(position);
									String url = "";
									if (header
											.equals(getString(R.string.tvsocityleveldmenu4))) {
										url = String
												.format(getString(R.string.api_getdramaquote_leveld),
														String.valueOf(item
																.get("chapter_id")))
												+ String.valueOf(item
														.get("parent_id"));
									} else {
										url = String
												.format(getString(R.string.api_getdramanews_leveld),
														String.valueOf(item
																.get("chapter_id")))
												+ String.valueOf(item
														.get("parent_id"));
									}
									Intent intent = new Intent(
											TvSocietyLevelCActivity.this,
											TvSocietyLevelDNewsActivity.class);
									intent.putExtra("header", header);
									intent.putExtra("url", url);
									intent.putExtra("thumbnail", String
											.valueOf(item.get("thumbnail")));
									startActivity(intent);
								}
							});
						}else{
							showDialodAndExit();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						showDialodAndExit();
					}
				}else{
					if(refresh_count>0){
						refresh_count -= 1;
						callAPI();
					}
				}
			}
		});

	}
	
	private void showDialodAndExit(){
		Builder builder = new Builder(this);
		builder.setTitle("");
		builder.setMessage(R.string.alerttextandexit);
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onClickExit(getCurrentFocus());
			}
		});
		builder.show();
	}

}
