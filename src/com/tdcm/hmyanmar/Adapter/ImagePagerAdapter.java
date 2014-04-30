package com.tdcm.hmyanmar.Adapter;

import java.util.HashMap;
import java.util.List;

import com.tdcm.hmyanmar.Fragment.ImageFragment;
import com.tdcm.hmyanmar.Fragment.WebNovelFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {
	
	Context context;
	List<HashMap<String, Object>> items;
	int totalPage;

	public ImagePagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	public void setData(Context c,int t, List<HashMap<String, Object>> list) {
		context = c;
		totalPage = t;
		items = list;
	}
	
	@Override
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
    }
    
    @Override
    public int getCount() {
        return totalPage;
    }
    
	@Override
	public Fragment getItem(int position) {
		return new ImageFragment(String.valueOf(items.get(position).get("pic_thumbnail")));
	}

}
