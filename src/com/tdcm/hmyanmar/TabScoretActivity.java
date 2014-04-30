package com.tdcm.hmyanmar;

import com.tdcm.hmyanmar.Adapter.CategoryFragmentAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.OnCenterItemClickListener;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class TabScoretActivity extends FragmentActivity implements OnCenterItemClickListener {
	private ProgressBar pgb;
	private LinearLayout layout = null;
	private String catagory = null;
	CategoryFragmentAdapter mAdapter;
	Context context;
	ViewPager mPager;
	PageIndicator mIndicator;
	TitlePageIndicator indicator;

	public TabScoretActivity(String cat, LinearLayout layout,final ProgressBar pgb, Context context) {
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		pgb.setVisibility(View.GONE);
	}

	@Override
	public void onCenterItemClick(int position) {

	}

}
