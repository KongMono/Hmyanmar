package com.tdcm.hmyanmar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.callback.AjaxStatus;
import com.truelife.mobile.android.lib.DataUsage;

public class CoreFragment extends SherlockFragment {

    protected String TAG = getClass().getSimpleName();

    private String title = "";

    public CoreFragment() {
        this.title = "";
    }

    public CoreFragment(String title) {
        this.title = title;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (this.title == null)
            this.title = getString(R.string.app_name_display);

        try {
            getSherlockActivity().getSupportActionBar().setTitle(title);
        } catch (Exception e) {
        }


    }


    protected void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof HomeFragmentActivity) {
            HomeFragmentActivity fca = (HomeFragmentActivity) getActivity();
            fca.switchContent(fragment);

        }
    }

    protected void toggleLeftMenu() {
        toggleLeftMenu(null);
    }

    protected void toggleLeftMenu(View view) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof HomeFragmentActivity) {
            HomeFragmentActivity fca = (HomeFragmentActivity) getActivity();
            fca.toggleLeftMenu(null);
        }
    }

    protected void toggleRightMenu() {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof HomeFragmentActivity) {
            HomeFragmentActivity fca = (HomeFragmentActivity) getActivity();
            fca.toggleRightMenu(null);
        }
    }

    protected void addFragmentToStack(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof HomeFragmentActivity) {
            HomeFragmentActivity fca = (HomeFragmentActivity) getActivity();
            fca.addFragmentToStack(fragment);
        }
    }

    protected int showMeta(AjaxStatus status) {

        if (status != null) {
            return status.getCode();
        }
        return 0;
    }
    
}
