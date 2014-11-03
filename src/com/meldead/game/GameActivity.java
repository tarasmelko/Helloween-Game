package com.meldead.game;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	protected CCGLSurfaceView _glSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		_glSurfaceView = new CCGLSurfaceView(this);

		setContentView(_glSurfaceView);

		CCDirector.sharedDirector().attachInView(_glSurfaceView);

		CCDirector.sharedDirector().setDisplayFPS(false);

		CCDirector.sharedDirector().setAnimationInterval(1.0f / 150.0f);

		CCScene scene = MainMenuLayer.scene();
		CCDirector.sharedDirector().runWithScene(scene);
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		MediaPlayer mp = MediaPlayer.create(this, R.raw.crow);
		mp.start();
		return true;
	}

	@Override
	protected void onDestroy() {
		// Clean everything up
		SoundEngine.sharedEngine().realesAllSounds();
		SoundEngine.sharedEngine().realesAllEffects();

		// Completely shut down the sound system
		SoundEngine.purgeSharedEngine();
		CCDirector.sharedDirector().end();
		super.onDestroy();
	}
}