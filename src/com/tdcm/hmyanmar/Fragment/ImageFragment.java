package com.tdcm.hmyanmar.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;
import com.tdcm.hmyanmar.R;

public class ImageFragment extends SherlockFragment {
	
	String thumb;
	private ImageView imageView;
	private AQuery aq;
	
	public ImageFragment(String thumbnail) {
		// TODO Auto-generated constructor stub
		this.thumb = thumbnail;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.imagefragment, null, false);
		imageView = (ImageView) view.findViewById(R.id.imageview);
		aq = new AQuery(getActivity());
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bitmap bmp_default = BitmapFactory.decodeResource(getResources(), R.drawable.htv_placeholder);
		aq.id(imageView).image(thumb, true, true, 0, 0, bmp_default, 0);
	}

}
