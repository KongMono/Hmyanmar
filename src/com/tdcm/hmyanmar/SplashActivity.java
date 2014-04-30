package com.tdcm.hmyanmar;

import com.truelife.mobile.android.access_blocking.util.AccessBlocking;
import com.truelife.mobile.android.access_blocking.util.TrueAppUtility;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity{

	private Handler mHandler = new Handler();
	private ImageView splash;
	
	public void onDestroy() {
        super.onDestroy();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initeFade();
    }
    
    boolean forcedStop = false;


    public void initeFade() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);

        splash = (ImageView) findViewById(R.id.splashimage);

        splash.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mHandler.removeCallbacks(runApp);
                goToNextActivity();
            }
        });

        mHandler.postDelayed(runFadeIn, 1500);


    }


    private void goToNextActivity() {
        if (!forcedStop) {

            forcedStop = true;

            Intent intent;

            intent = new Intent(SplashActivity.this, HomeActivity.class);

            AccessBlocking ac = AccessBlocking.getInstance(SplashActivity.this, intent, new Intent());
            ac.checking(getString(R.string.app_name), getString(R.string.app_version), TrueAppUtility.getIMEI(SplashActivity.this), "android");
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean r = false;
        if (keyCode == 4) {
            System.exit(0);
            r = true;
        }
        return r;
    }

    Runnable runFadeIn = new Runnable() {
        @Override
        public void run() {
            splash.setImageResource(R.drawable.truelifeplus);
            Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.fade_in);
            anim.setDuration(1500);
            anim.setFillAfter(true);
            splash.startAnimation(anim);

            mHandler.postDelayed(runFadeOut, 1500);
        }
    };

    Runnable runFadeOut = new Runnable() {

        @Override
        public void run() {
            splash.setImageResource(R.drawable.truelifeplus);
            Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.fade_out);
            anim.setDuration(1500);
            anim.setFillAfter(true);
            splash.startAnimation(anim);

            mHandler.postDelayed(runApp, 1500);
        }
    };

    Runnable runApp = new Runnable() {
        @Override
        public void run() {
            goToNextActivity();
        }
    };
	
}
