package com.tdcm.hmyanmar.Adapter;

import com.tdcm.hmyanmar.Fragment.WebNovelFragment;
import com.tdcm.hmyanmar.R;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NovelPagerAdapter extends FragmentStatePagerAdapter {
	Context context;
	String description;
	int pageId,totalPage;
	
	public NovelPagerAdapter(FragmentManager fm) {
		super(fm);

	}
	
	public void setData(Context c,int id,int t) {
		context = c;
		pageId = id;
		totalPage = t;
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
		return new WebNovelFragment(pageId,position);
	}

}
