package com.tdcm.hmyanmar.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tdcm.hmyanmar.Api.API;
import com.tdcm.hmyanmar.Dataset.NovelEntry;
import com.tdcm.hmyanmar.Json.NovelParser;
import com.tdcm.hmyanmar.R;


public class WebNovelFragment extends SherlockFragment {
	int position,pageId;
	String description;
	WebView webView;
	NovelParser novelParser = new NovelParser();
	WebSettings settings;
	private AQuery aq;
	private API api;
	
	public WebNovelFragment(int pageId,int position){
		this.position = position;
		this.pageId = pageId;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_novel_fragment, null, false);
        api = new API(getActivity());
        aq = new AQuery(getActivity());
        webView = (WebView) view.findViewById(R.id.webview);
        
        return view;

    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	String strApi = api.getApiNovelInfo(pageId, position + 1);
    	
    	aq.ajax(strApi, JSONObject.class, new AjaxCallback<JSONObject>() {
    		ArrayList<NovelEntry> arrayList = new ArrayList<NovelEntry>();
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				 try {
					arrayList = novelParser.getData(json);
					description = arrayList.get(0).getDescription();
					
				 	webView.getSettings().setLoadWithOverviewMode(true);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.setHorizontalScrollBarEnabled(false);

					settings = webView.getSettings();
					settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
					settings.setJavaScriptCanOpenWindowsAutomatically(true);
//					webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8",null);

					try {
						InputStream input = getActivity().getAssets().open("theme.html");
						int size = input.available();
				        byte[] buffer = new byte[size];
				        input.read(buffer);
				        input.close();
				        String htmlData = new String(buffer);

				        description = description.replaceAll("(width:).*?[0-9]{3}(px;)", "width=100%");
				        description = description.replaceAll("(width=\").*?[0-9]{3}(\")", "width=100%");
				        description = description.replaceAll("(height=\").*?[0-9]{3}(\")", "height=auto");
				        
				        htmlData = htmlData.replaceAll("<!--client_generated-->", description);

						webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8","");
						
						webView.setWebViewClient(new WebViewClient(){

					        @Override
					        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
					            super.onReceivedError(view, errorCode, description, failingUrl);
					            webView.loadDataWithBaseURL("file:///android_asset/error.html", null, "text/html", "utf-8", null);
					        }

					    }); 
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				 }catch(Exception e){
					 e.printStackTrace();
				 }
			}
	 });
        
    }
}
