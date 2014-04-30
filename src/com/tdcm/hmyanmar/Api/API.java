package com.tdcm.hmyanmar.Api;


import com.tdcm.hmyanmar.R;
import com.truelife.mobile.android.access_blocking.util.TrueAppUtility;

import android.content.Context;
import android.util.Log;

public class API {
	private Context context;
	private String TAG = getClass().getSimpleName();

	public API(Context c) {
		context = c;
	}
	public String getApiSpringBoard() {
		
		StringBuilder br = new StringBuilder();
		
		br.append("http://widget3.truelife.com/truelifes/services/rest/?method=springboard");
		br.append("&appname=").append(context.getResources().getString(R.string.app_name));
		br.append("&appversion=").append(context.getResources().getString(R.string.app_version));
		br.append("&app_id=").append(R.string.app_id);
		br.append("&device=").append(R.string.device);
		br.append("&format=").append("json");
		br.append("&config_ver=").append(context.getResources().getString(R.string.app_version));
		Log.i(TAG, br.toString());
		return br.toString();
	}

	public String getApiListTVSocietyData(String subCategory, int offset, int limit) {
		
		String [] Category = context.getResources().getStringArray(R.array.CatDialogArray);
		StringBuilder br = new StringBuilder();
		br.append("http://api-movie.truelife.com/wrap_api/mod/news/getlist?method=getlist&category=tv");
		br.append("&offset="+ String.valueOf(offset));
		br.append("&limit="+ String.valueOf(limit)).append("&format=json");
		
		if (subCategory.equalsIgnoreCase(Category[0])) {
			br.append("");
		}else if(subCategory.equalsIgnoreCase(Category[1])){
			br.append("");
		}else if(subCategory.equalsIgnoreCase(Category[2])){
			br.append("");
		}
		Log.i("getApiListTVSocietyData", br.toString());
		return br.toString();
	}
	
	public String getApiListHClusiveData(int offset, int limit) {
		StringBuilder br = new StringBuilder();
		br.append("http://api-movie.truelife.com/wrap_api/mod/novel/home?method=getlist");
		br.append("&offset="+ String.valueOf(offset));
		br.append("&limit="+ String.valueOf(limit));
		br.append("&carrier_name=" + TrueAppUtility.isTrueMoveOperator(context));
		br.append("&version=" + context.getString(R.string.app_version)).append("&format=json");
		
		Log.i("ApiListHClusive", br.toString());
		return br.toString();
	}
	
	public String getApiEpisodeData(int id) {
		StringBuilder br = new StringBuilder();
		br.append("http://api-movie.truelife.com/wrap_api/mod/novel/getlist?method=getlist");
		br.append("&content_id=").append(String.valueOf(id)).append("&format=json");
		
		Log.i("ApiEpisodeData", br.toString());
		return br.toString();
	}
	
	public String getApiNovelInfo(int id,int page) {
		StringBuilder br = new StringBuilder();
		br.append("http://api-movie.truelife.com/wrap_api/mod/novel/novelinfo?method=getinfo");
		br.append("&content_id=").append(String.valueOf(id));
		br.append("&page=").append(String.valueOf(page));
		br.append("&format=json");
		
		Log.i("getApiNovelInfo", br.toString());
		return br.toString();
	}
	
	public String getHomeTVSocietyData() {
		StringBuilder br = new StringBuilder();
		br.append("http://mstage.truelife.com/api_movietv/drama/home?method=getlist&format=json");
		
		Log.i("getHomeTVSocietyData", br.toString());
		return br.toString();
	}
	
	public String getAllTVSocietyData(int offset, int limit) {
		StringBuilder br = new StringBuilder();
		br.append("http://mstage.truelife.com/api_movietv/drama/getlist?method=getlist");
		br.append("&offset="+ String.valueOf(offset));
		br.append("&limit="+ String.valueOf(limit));
		br.append("&format=json");
		
		Log.i("getAllTVSocietyData", br.toString());
		return br.toString();
	}
	
	public String getHotShotSocietyData(int limit) {
		StringBuilder br = new StringBuilder();
		br.append("http://mstage.truelife.com/api_movietv/instagram/getlist?method=getlist");
		br.append("&limit="+ String.valueOf(limit));
		br.append("&format=json");
		
		Log.i("getHotShotSocietyData", br.toString());
		return br.toString();
	}
	
	public String getInstagramData(String content_id) {
		StringBuilder br = new StringBuilder();
		br.append("http://mstage.truelife.com/api_movietv/instagram/instagraminfo?method=getinfo");
		br.append("&content_id="+ String.valueOf(content_id));
		br.append("&format=json");
		
		Log.i("getInstagramData", br.toString());
		return br.toString();
	}
}
