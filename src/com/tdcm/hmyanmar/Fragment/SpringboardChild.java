package com.tdcm.hmyanmar.Fragment;

import com.tdcm.hmyanmar.Dataset.SBEntry;
import com.tdcm.hmyanmar.AboutActivity;
import com.tdcm.hmyanmar.MainActivity;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;


public class SpringboardChild extends SpringboardFragment {
	
	private Context context;
	private String TAG = getClass().getSimpleName();
	
	public SpringboardChild(Context c) {
		super(c);
		this.context = c;
	}

	@Override
	protected int specificMenu(SBEntry springboardITem, ListView l, int position) {

		String template = springboardITem.getTemplate();
		Log.i(TAG, "template " + template);

		String title = springboardITem.getTitle();


		if (template.equals("home")) {
			switchFragment(new MainActivity(title));
		}else if (template.equals("about")) {
			switchFragment(new AboutActivity(title));
		}
		return position;
	}

}
