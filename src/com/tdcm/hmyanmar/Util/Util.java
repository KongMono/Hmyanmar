package com.tdcm.hmyanmar.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;




import com.tdcm.hmyanmar.Adapter.CategoryFragmentAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Util extends Activity {
	
	public static int idIconTab2 = 111;
	public static int idIconTab3 = 222;
	public static ImageView logo_cen,logo_left,logo_right, catagory, refreshList;
	public static LinearLayout layout;
	public static ViewPager mPager;
	public static TextView titleCatagory;
	public static CategoryFragmentAdapter mAdapter;
	public static Boolean isError = false;

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static int averageARGB(Bitmap pic) {
		int A, R, G, B;
		A = R = G = B = 0;
		int pixelColor;
		int width = pic.getWidth();
		int height = pic.getHeight();
		int size = width * height;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				pixelColor = pic.getPixel(x, y);
				A += Color.alpha(pixelColor);
				R += Color.red(pixelColor);
				G += Color.green(pixelColor);
				B += Color.blue(pixelColor);
			}
		}

		A /= size;
		R /= size;
		G /= size;
		B /= size;

		int color = Color.argb(A, R, G, B);

		return color;

	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;

		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static boolean checkNotNullNotEmptyNotWhiteSpace(final String string) {
		if (string != null && !string.isEmpty() && !string.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static String isNotNullNotEmptyNotWhiteSpace(final String string) {
		if (string != null && !string.isEmpty() && !string.trim().isEmpty()) {
			return string;
		}
		return "";
	}

	public static Calendar getCalendar() {
		return Calendar.getInstance(Locale.ENGLISH);
	}

	public static Date getCurrentDate() {
		return getCalendar().getTime();
	}

	public static String datetoStringFormat(Date date, String formatOutput) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatOutput);
		return sdf.format(date);
	}

	public static Date toDate(String date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}

	public static String getCurrentDate(String format) {
		return datetoStringFormat(getCalendar().getTime(), format);
	}

	public static Date addDay(Date current, int days) {
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, +days);

		return cal.getTime();
	}

	public static Date subDay(Date current, int days) {
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, -days);

		return cal.getTime();
	}

	public static String addDay(Date current, int days, String outFormat) {
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, +days);

		return datetoStringFormat(cal.getTime(), outFormat);
	}

	public static String subDay(Date current, int days, String outFormat) {
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, -days);

		return datetoStringFormat(cal.getTime(), outFormat);
	}

	public static boolean isPhone(Context context) {
		if ((((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
				|| (((TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE))
						.getLine1Number() == null)) {
			return false;
		}
		return true;
	}

	public static Typeface getPadaukFont(Context context) {
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Padauk.ttf");
		return face;
	}

	public static Typeface getPadaukBookFont(Context context) {
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Padaukbook.ttf");
		return face;
	}

	public static void autoScaleTextViewTextToHeight(Context context,
			TextView tv, float required_width) {
		String s = tv.getText().toString();
		float currentWidth = tv.getPaint().measureText(s);
		float phoneDensity = context.getResources().getDisplayMetrics().density;

		Log.e("resize", "required_width " + String.valueOf(required_width)
				+ " currentWidth" + String.valueOf(currentWidth));
		while (currentWidth > required_width) {
			Log.e("resize loop",
					"currentWidth " + String.valueOf(currentWidth)
							+ " required_width "
							+ String.valueOf((required_width * phoneDensity)));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tv.getTextSize() - 1.0f);
			currentWidth = tv.getPaint().measureText(s);
		}
	}

	public static void autoScalebuttonToHeight(Context context, Button button,
			float required_width, String s) {

		float currentWidth = button.getPaint().measureText(s);
		float phoneDensity = context.getResources().getDisplayMetrics().density;

		while (currentWidth > required_width) {
			Log.e("resize loop",
					"currentWidth " + String.valueOf(currentWidth)
							+ " required_width "
							+ String.valueOf((required_width * phoneDensity)));
			button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					button.getTextSize() - 1.0f);
			currentWidth = button.getPaint().measureText(s);
		}
	}

}
