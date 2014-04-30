package com.tdcm.hmyanmar.Fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tdcm.hmyanmar.Adapter.SpringboardListAdapter;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.SBEntry;
import com.tdcm.hmyanmar.Json.SpringBoardParser;
import com.tdcm.hmyanmar.HomeFragmentActivity;
import com.tdcm.hmyanmar.R;

public class SpringboardFragment extends ListFragment {
	private Context context;
	private ImageView quickContactBadge;
	private List<SBEntry> menuList = null;
	private ArrayList<SBEntry> arrData = new ArrayList<SBEntry>();
	private Handler handler = new Handler();
	SpringboardListAdapter adapter;
	String TAG = getClass().getSimpleName();
	SpringBoardParser springBoardParser = new SpringBoardParser();
	String strApi;
	API api;
	AQuery aq;
	int lastPosition = 0;
	int failedCount = 0;
	
	public SpringboardFragment(Context c) {
		context = c;
		api = new API(context);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.springboard, null);

		quickContactBadge = (ImageView) view.findViewById(R.id.quickContactBadge);
		quickContactBadge.setImageResource(R.drawable.ic_launcher);

		adapter = new SpringboardListAdapter(getActivity(), arrData);
		setListAdapter(adapter);
		
		aq = new AQuery(getActivity());
		
		callSpringBoard();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	
		getListView().setTextFilterEnabled(true);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setItemChecked(1, true);
		getListView().setSelection(1);
		getListView().setSelected(true);

		if (menuList != null && arrData.size() == 0 && menuList.size() > 0) {
			arrData.addAll(menuList);
			adapter.notifyDataSetChanged();
		}

	}

	public void callSpringBoard() {
        callSpringBoard(Integer.parseInt(getString(R.string.cache_timeout)));
    }

	public void callSpringBoard(int timeCach) {
		
		strApi = api.getApiSpringBoard();
		Log.d(TAG, "spring board url = " + strApi);
		aq.ajax(api.getApiSpringBoard(), JSONObject.class, new AjaxCallback<JSONObject>() {
			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				
				Log.d(TAG,url);

 				if (json != null) {
					try {
						arrData = springBoardParser.getData(json);
						adapter = new SpringboardListAdapter(getActivity(), arrData);
						setListAdapter(adapter);
						
						handler.post(new Runnable() {
							@Override
							public void run() {
						
								if (getActivity() instanceof HomeFragmentActivity) {
									setSelectItem(0);
								}

								adapter.notifyDataSetChanged();
							}
						});
						
					} catch (Exception e) {
						status.invalidate();
						Log.e(TAG + ".callSpringBoard","Springboard Error ", e);
					}
					
				} else {
					status.invalidate();
					Log.e(TAG + ".callSpringBoard","springBoardCallback : Fail!!!");
				}
 				
				if(arrData.size() == 0){
	                 status.invalidate();
	
		         if(failedCount < 5){
		             callSpringBoard(-1);
		
		         }else{
		             Toast.makeText(getActivity(), "Get SpringBoard Fail!", Toast.LENGTH_SHORT).show();
		             getActivity().finish();
		         }
	                 failedCount++;
	             }else{
	                 failedCount = 0;
	             }
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		SBEntry item = arrData.get(position);
		String title = item.getTitle();
		String template = item.getTemplate();
		String pageTitle = String.valueOf(item.getTitle());
		String pageName = "";

		if (item.getType().equals("webview")) {
			
		} else {
			position = specificMenu(item, l, position);
		}
		
		lastPosition = position;
		l.setItemChecked(lastPosition, true);
		l.setSelected(true);
		l.setSelection(lastPosition);

	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
	protected void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof HomeFragmentActivity) {
			HomeFragmentActivity fca = (HomeFragmentActivity) getActivity();
			fca.switchContent(fragment);
		}
	}
	
	/**
	 * @param springboardITem
	 *            Data Entry.
	 * @param listview
	 * @param position
	 *            index of new selected.
	 * @return : last posiyion selected.
	 */
	protected int specificMenu(SBEntry springboardITem, ListView listview,int position) {
		return position;
	}

	public void setSelectItem(int pos) {
		onListItemClick(getListView(), null, pos, 0);
	}


    public void setSelectItem() {
        onListItemClick(getListView(), null, lastPosition, 0);
    }
    
    public int getPageIndexFromTemplate(String template) {

		for (int i = 0; i < arrData.size(); i++) {
			if (arrData.get(i).getTemplate().equalsIgnoreCase(template))
				return i;
		}

		return -1;
	}

	protected void clearData() {
		this.arrData.clear();
		this.adapter.notifyDataSetChanged();
	}


}
