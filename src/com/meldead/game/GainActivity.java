package com.meldead.game;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.meldead.game.utils.Constants;

public class GainActivity extends Activity {

	ImageView startGame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Constants.FOLDER = "360/";

		final Configuration config = getResources().getConfiguration();
		Log.i(this.getClass().getSimpleName(), String.format(
				"Smallest width is [%s]", config.smallestScreenWidthDp));
		switch (config.smallestScreenWidthDp) {
		case 360:
			Constants.FOLDER = "360/";
			Constants.TEXT_SIZE_MAIN_MENU_BEST = 100;
			Constants.TEXT_SIZE_MAIN_MENU_START = 250;
			Constants.TEXT_SIZE_GAME_SCORE_LABEL = 100;
			Constants.TEXT_SIZE_GAME_RESUME_LABEL = 200;
			Constants.TEXT_SIZE_GAME_OVER_LABEL = 170;
			Constants.TEXT_SIZE_GAME_OVER_SCORES_LABEL = 150;
			Constants.SPEED = -10;
			break;
		case 320:
			Constants.FOLDER = "320/";
			Constants.TEXT_SIZE_MAIN_MENU_BEST = 40;
			Constants.TEXT_SIZE_MAIN_MENU_START = 80;
			Constants.TEXT_SIZE_GAME_SCORE_LABEL = 45;
			Constants.TEXT_SIZE_GAME_RESUME_LABEL = 120;
			Constants.TEXT_SIZE_GAME_OVER_LABEL = 90;
			Constants.TEXT_SIZE_GAME_OVER_SCORES_LABEL = 45;
			Constants.SPEED = -5;
			break;

		}

		setContentView(R.layout.main);
		Intent gameIntent = new Intent(GainActivity.this, GameActivity.class);
		startActivity(gameIntent);

	}
}
