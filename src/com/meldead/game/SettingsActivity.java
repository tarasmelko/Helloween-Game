package com.meldead.game;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SettingsActivity extends Activity {
	ImageView mSwitcher;
	ImageView mSwicherPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.settings);

		mSwitcher = (ImageView) findViewById(R.id.switcher_imv);
		mSwicherPath = (ImageView) findViewById(R.id.switcher_path);
		setSwitcherPos();

		mSwitcher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwitcher.setEnabled(false);
				mSwitcher.setSelected(!mSwitcher.isSelected());

				final int move = mSwitcher.isSelected() ? 1 : -1;
				final RelativeLayout.LayoutParams paramsSwitch = (RelativeLayout.LayoutParams) mSwitcher.getLayoutParams();

				TranslateAnimation anim = new TranslateAnimation(0, move * (mSwicherPath.getWidth() - mSwitcher.getHeight()), 0, 0);
				anim.setDuration(150);
				anim.setFillAfter(true);
				anim.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if (!PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).getBoolean("SOUND", false)) {
							PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putBoolean("SOUND", true).commit();
						} else {
							PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putBoolean("SOUND", false).commit();
						}
						paramsSwitch.leftMargin = move == 1 ? mSwicherPath.getWidth() - mSwitcher.getHeight() : 0;
						mSwitcher.setLayoutParams(paramsSwitch);
						mSwitcher.clearAnimation();
						mSwitcher.setEnabled(true);
						Log.e("sound",PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).getBoolean("SOUND", false)+"");
						playClikcSound();
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				mSwitcher.startAnimation(anim);

			}
		});

		setSwitcherState();

	}

	public void playClikcSound() {
		MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
		mp.start();
	}

	public void setSwitcherState() {
		boolean isSelected = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("SOUND", false);
		final RelativeLayout.LayoutParams paramsSwitch = (RelativeLayout.LayoutParams) mSwitcher.getLayoutParams();
		mSwitcher.setSelected(isSelected);
		paramsSwitch.leftMargin = isSelected ? mSwicherPath.getWidth() - mSwitcher.getHeight() : 0;
		mSwitcher.setLayoutParams(paramsSwitch);
	}

	private void setSwitcherPos() {
		final RelativeLayout.LayoutParams paramsSwitch = (RelativeLayout.LayoutParams) mSwitcher.getLayoutParams();
		mSwitcher.setLayoutParams(paramsSwitch);
		mSwitcher.clearAnimation();
		mSwitcher.setEnabled(true);

	}

}