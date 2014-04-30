package com.tdcm.hmyanmar.pushnotifications;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;

public final class PushConfig {
	
//	static String SERVER_URL = "http://widget2.truelife.com/mobilePush/rest/?method=register&device=android&format=json";
	static String SERVER_URL = "http://widget2.truelife.com/mobilePush/rest/?method=register&device=android&format=json";
//	public static String SENDER_ID = "331247588306";
	public static String SENDER_ID = "1030004850875";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidGCM";

    public static final String DISPLAY_MESSAGE_ACTION =  "com.tdcm.htv.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public static String getIMEI(Context ctx) {
        TelephonyManager TelephonyMgr = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = TelephonyMgr.getDeviceId();
        return imei;
    }
    
    public static String getDeviceModel() {
    	String manufacturer = Build.MANUFACTURER;
    	String model = Build.MODEL;
    	if (model.startsWith(manufacturer)) {
    		return model;
    	} else {
    		return manufacturer + "_" + model;
    	}
	}
    
    public static String getDeviceOSVersion() {
    	return Build.VERSION.RELEASE;
	}
    
    public static String getIdentifier(Context ctx) {
		return ctx.getPackageName();
	}
}
