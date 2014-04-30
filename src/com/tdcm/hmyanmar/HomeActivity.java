package com.tdcm.hmyanmar;

import com.google.android.gcm.GCMRegistrar;
import com.tdcm.hmyanmar.pushnotifications.PushAlertDialogManager;
import com.tdcm.hmyanmar.pushnotifications.PushConfig;
import com.tdcm.hmyanmar.pushnotifications.PushServerUtilities;
import com.tdcm.hmyanmar.pushnotifications.PushWakeLocker;
import com.truelife.mobile.android.lib.DataUsage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class HomeActivity extends HomeFragmentActivity {

	Fragment mFragment;
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString("message");

			PushWakeLocker.acquire(getApplicationContext());
			//
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();

			PushWakeLocker.release();
		}
	};

	AsyncTask<Void, Void, Void> mRegisterTask;

	PushAlertDialogManager alert = new PushAlertDialogManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window w = getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}

		DataUsage.SetupDataUsage(this, this.getString(R.string.checkdata_app_id), this.getString(R.string.checkdata_app_secret));

		// ///////////// Push
		Log.e("IMEI", "IMEI : " + PushConfig.getIMEI(this));
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(PushConfig.DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {

			GCMRegistrar.register(this, PushConfig.SENDER_ID);
		} else {

			if (GCMRegistrar.isRegisteredOnServer(this)) {

				// Toast.makeText(getApplicationContext(),
				// "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {

				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						PushServerUtilities.register(context, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Log.e("onResume",
		// "prefs onResume : "+this.getLocalClassName()+" "+getClass().getSimpleName());
		DataUsage.checkOnResumeCheckDataUsage(this, getClass().getSimpleName());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DataUsage.setActivityNameForOnPauseOnDestroyed(getClass().getSimpleName());
    	
    	/////////////////// Push
    	if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DataUsage.setActivityNameForOnPauseOnDestroyed(getClass().getSimpleName());
	}

}
