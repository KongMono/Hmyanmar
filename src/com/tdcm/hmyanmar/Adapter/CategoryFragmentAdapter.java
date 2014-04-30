package com.tdcm.hmyanmar.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


public class CategoryFragmentAdapter extends FragmentStatePagerAdapter {
	Context mContext;
	private String[] content;
	private int mCount = 5;
	final ArrayList<String> titles = new ArrayList<String>();
	public CategoryFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setTitle(String[] title, Context c) {
		content = title;
		mContext = c;
	}

	@Override
	public int getItemPosition(Object object) {

	    return POSITION_NONE;
    }

	@Override
	public Fragment getItem(int position) {
//		switch (position) {
//		case 0:
//			return new PremieLeagueFragment();
//		case 1:
//			return new ThaiLeagueFragment();
//		case 2:
//			return new BundesligaFragment();
//		case 3:
//			return new LaligaFragment();
//		case 4:
//			return new CalcioFragment();
//		}
		return null;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return content[position % content.length];
	}

}