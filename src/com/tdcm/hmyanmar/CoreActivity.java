package com.tdcm.hmyanmar;

import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.truelife.mobile.android.lib.DataUsage;

public class CoreActivity extends SherlockFragmentActivity {

    protected String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onExit();
    }

    private void onExit() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void onClickExit(View view) {
        finish();
        onExit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {

            finish();
            onExit();

        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
//    	Log.e("onResume", "prefs onResume : "+this.getLocalClassName()+" "+getClass().getSimpleName());
    	DataUsage.checkOnResumeCheckDataUsage(this, getClass().getSimpleName());
    }
    
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	DataUsage.setActivityNameForOnPauseOnDestroyed(getClass().getSimpleName());
    }
    
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	DataUsage.setActivityNameForOnPauseOnDestroyed(getClass().getSimpleName());
    }
}
