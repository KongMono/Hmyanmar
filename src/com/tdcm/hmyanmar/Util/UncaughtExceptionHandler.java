package com.tdcm.hmyanmar.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	public void uncaughtException(Thread thread, Throwable exception) {

		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

		if (exception instanceof IOException) {
			Log.e(getClass().getName(), "IOException", exception);
		} else if (exception instanceof NullPointerException) {
			Log.e(getClass().getName(), "NullPointerException", exception);
		} else if (exception instanceof OutOfMemoryError) {
			Log.e(getClass().getName(), "OutOfMemoryError", exception);
		} else if (exception instanceof RuntimeException) {
			Log.e(getClass().getName(), "RuntimeException", exception);
		} else if (exception instanceof FileNotFoundException) {
			Log.e(getClass().getName(), "FileNotFoundException", exception);
		} else if (exception instanceof Exception) {
			Log.e(getClass().getName(), "Exception", exception);
		}
		
		android.os.Process.killProcess(android.os.Process.myPid());

		System.gc();
		System.exit(0);
	}
}
