package com.tdcm.hmyanmar.Util;

import android.app.Application;

public class UIApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());

	}

}
