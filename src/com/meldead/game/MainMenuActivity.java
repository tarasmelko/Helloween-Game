package com.meldead.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainMenuActivity extends Activity {

	ImageView startGame;
	ImageView settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.main);

		startGame = (ImageView) findViewById(R.id.start_game);
		startGame.setVisibility(View.GONE);
		startGame.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		startGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent gameIntent = new Intent(MainMenuActivity.this, GameActivity.class);
				startActivity(gameIntent);
			}
		});

		startGame.setVisibility(View.VISIBLE);

		settings = (ImageView) findViewById(R.id.settings);
		settings.setVisibility(View.GONE);
		settings.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent gameIntent = new Intent(MainMenuActivity.this, SettingsActivity.class);
				startActivity(gameIntent);
			}
		});

		settings.setVisibility(View.VISIBLE);
		
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("SOUND", false)){
			
		}else{
			
		}
	}

}
