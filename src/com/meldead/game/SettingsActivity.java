package com.meldead.game;

import org.cocos2d.sound.SoundEngine;

import com.meldead.game.utils.FontManager;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	ImageView mSwitcher;
	ImageView mSwicherPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.settings);

		((TextView) findViewById(R.id.back_tv)).setTypeface(FontManager
				.getFont(this));
		((TextView) findViewById(R.id.back_tv))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});

		((TextView) findViewById(R.id.settings_tv)).setTypeface(FontManager
				.getFont(this));
		((TextView) findViewById(R.id.sound_tv)).setTypeface(FontManager
				.getFont(this));
		((TextView) findViewById(R.id.on_tv)).setTypeface(FontManager
				.getFont(this));
		((TextView) findViewById(R.id.off_tv)).setTypeface(FontManager
				.getFont(this));

		((TextView) findViewById(R.id.off_tv)).setOnClickListener(this);
		((TextView) findViewById(R.id.on_tv)).setOnClickListener(this);

		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				"SOUND", true)) {
			((TextView) findViewById(R.id.on_tv)).setTextColor(getResources()
					.getColor(android.R.color.black));
			((TextView) findViewById(R.id.off_tv)).setTextColor(getResources()
					.getColor(android.R.color.darker_gray));
		} else {
			((TextView) findViewById(R.id.on_tv)).setTextColor(getResources()
					.getColor(android.R.color.darker_gray));
			((TextView) findViewById(R.id.off_tv)).setTextColor(getResources()
					.getColor(android.R.color.black));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.on_tv:
			((TextView) findViewById(R.id.off_tv)).setTextColor(getResources()
					.getColor(android.R.color.darker_gray));
			((TextView) findViewById(R.id.on_tv)).setTextColor(getResources()
					.getColor(android.R.color.black));
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putBoolean("SOUND", true);
			SoundEngine.sharedEngine().resumeSound();
			break;
		case R.id.off_tv:
			((TextView) findViewById(R.id.off_tv)).setTextColor(getResources()
					.getColor(android.R.color.black));
			((TextView) findViewById(R.id.on_tv)).setTextColor(getResources()
					.getColor(android.R.color.darker_gray));
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putBoolean("SOUND", false);
			SoundEngine.sharedEngine().pauseSound();
			break;
		}

	}

}