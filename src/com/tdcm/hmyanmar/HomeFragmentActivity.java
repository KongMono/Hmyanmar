package com.tdcm.hmyanmar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tdcm.hmyanmar.Fragment.LoadingFragment;
import com.tdcm.hmyanmar.Fragment.SpringboardChild;
import com.tdcm.hmyanmar.Fragment.SpringboardFragment;

public class HomeFragmentActivity extends SlidingFragmentActivity implements SlidingMenu.OnClosedListener {

	protected Fragment leftFrag;
	protected ListFragment rightFrag;
	protected String TAG = getClass().getSimpleName();
	Handler mHandler = new Handler();
	Handler handler = new Handler();
	private SlidingMenu sm;
	private GoogleAnalyticsTracker tracker;
	private boolean isNewContent;
	private Fragment newFragment;
	public String selectItem = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.lib_content_frame);

		initSlidingMenu(savedInstanceState);

		initMainContent();
		initLeftMenu(savedInstanceState);

		if (sm.getMode() == SlidingMenu.LEFT_RIGHT) {
			initRightMenu(savedInstanceState);
		}

		try {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			setSupportProgressBarIndeterminateVisibility(false);

		} catch (Exception e) {

		}

	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent intent) {
		super.onActivityResult(reqCode, resCode, intent);
		try {
			if (resCode == RESULT_OK) {

				if (intent.getExtras().containsKey("callback_url")) {
				}

			} else if (resCode == RESULT_CANCELED) {
			} else {
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		menu.add(Menu.NONE, Menu.FIRST + 99, 99, "info")
//				.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark)
//				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		int i = item.getItemId();
		if (i == android.R.id.home) {

			FragmentManager fm = getSupportFragmentManager();

			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				toggle();
			} else {
				fm.popBackStack();
			}

		} else if (item.getTitle().equals("load")) {
			setSupportProgress(Window.PROGRESS_END);
			setSupportProgressBarIndeterminateVisibility(true);
		}

		return super.onOptionsItemSelected(item);
	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		try {

			sm = getSlidingMenu();
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setShadowDrawable(R.drawable.shadow);
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);

			sm.setFadeDegree(0.35f);
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

			sm.setOnClosedListener(this);

			if (isEnableRightMenu()) {
				sm.setMode(SlidingMenu.LEFT_RIGHT);

				sm.setSecondaryMenu(R.layout.lib_menu_frame_two);
				sm.setSecondaryShadowDrawable(R.drawable.shadowright);
			}

		} catch (Exception e) {
			Log.e(TAG, "Error initSlidingMenu", e);
		}

	}

	protected boolean isEnableRightMenu() {
		return false;
	}

	protected void initMainContent() {

		try {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new LoadingFragment())
					.commit();
		} catch (Exception e) {
			Log.e(TAG, "Error initMainContent", e);
		}
	}

	
	protected void initLeftMenu(Bundle savedInstanceState) {
		try {
			setBehindContentView(R.layout.lib_menu_frame);

			if (savedInstanceState == null) {
				FragmentTransaction t = this.getSupportFragmentManager()
						.beginTransaction();
				leftFrag = new SpringboardChild(this);
				t.replace(R.id.menu_frame, leftFrag);
				t.commit();
			} else {
				leftFrag = (ListFragment) this.getSupportFragmentManager()
						.findFragmentById(R.id.menu_frame);
			}

		} catch (Exception e) {
			Log.e(TAG, "Error initLeftMenu", e);
		}

	}
	
	protected void initRightMenu(Bundle savedInstanceState) {
		sm.setSecondaryMenu(R.layout.lib_menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);

		try {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.menu_frame_two, new SpringboardFragment(this))
					.commit();

		} catch (Exception e) {
			Log.e(TAG, "Error initRightMenu", e);
		}
	}
	

	public void toggleLeftMenu(View v) {
		try {
			if (sm.isMenuShowing()) {
				sm.showContent();
			} else {
				sm.showMenu();
			}
		} catch (Exception e) {
			Log.e(TAG, "Error ToggleLeftMenu", e);
		}
	}

	public void toggleRightMenu(View v) {
		try {
			if (sm.isSecondaryMenuShowing()) {
				sm.showContent();
			} else {
				sm.showSecondaryMenu();
			}
		} catch (Exception e) {
			Log.e(TAG, "Error ToggleRightMenu", e);
		}
	}

	public void switchContent(final Fragment fragment) {

		this.isNewContent = true;
		this.newFragment = fragment;

		if (sm.isMenuShowing()) {

			handler.post(new Runnable() {
				@Override
				public void run() {

					FragmentManager fm = getSupportFragmentManager();

					try {
						for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
							fm.popBackStack();
						}
					} catch (Exception e) {
					}

					getSlidingMenu().showContent();
				}
			});
		} else {
			onClosed();
		}

	}

	public void addFragmentToStack(Fragment newFragment) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();

	}

	public void showContent() {
		getSlidingMenu().showContent();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() == 0) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (!sm.isMenuShowing()) {
					showAlertExitDialog();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showAlertExitDialog() {

        Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_h_tv);
        builder.setTitle(getString(R.string.app_displayname));
        builder.setMessage(getString(R.string.exit_alert));
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                finish();
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        builder.setNegativeButton(getString(R.string.button_cancel), null);
        builder.show();
    }

	public void setSlidingMode(int mode) {
		try {
			getSlidingMenu().setTouchModeAbove(mode);
		} catch (Exception e) {
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		}

	}

	@Override
	public void onClosed() {
		if (this.isNewContent && newFragment != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					try {
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.content_frame, newFragment)
								.commit();
					} catch (Exception e) {
						Log.e(TAG, "Error On Switch Menu", e);
					}

				}
			});
		}

		this.isNewContent = false;

	}
	
	public void setSelectItem(String item) {
		this.selectItem = item;
	}

	public String getSelectItem() {
		return selectItem;
	}


}
