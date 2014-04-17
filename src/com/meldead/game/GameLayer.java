package com.meldead.game;

import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GameLayer extends CCColorLayer implements SensorEventListener {
	protected ArrayList<CCSprite> _targets;
	protected ArrayList<CCSprite> _weapons;
	CCSprite player;
	CCMoveTo actionMoveDown;
	CCMoveTo actionMoveUp;

	// used to control registration of Accelerometer events
	protected boolean isAccelerometerEnabled_;

	protected SensorManager sensorManager;
	protected Sensor accelerometer;
	public float x, y = 0;
	public float margenMaxX, margenMaxY = 30;
	public float margenMinX, margenMinY = 30;
	private CCSprite target;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));

		scene.addChild(layer);

		return scene;
	}

	public boolean isAccelerometerEnabled() {
		return isAccelerometerEnabled_;
	}

	public void setIsAccelerometerEnabled(boolean enabled) {
		if (isAccelerometerEnabled_ != enabled) {
			isAccelerometerEnabled_ = enabled;
			if (isRunning()) {
				if (enabled)
					registerWithAccelerometer();
				else
					unregisterWithAccelerometer();
			}
		}
	}

	protected void registerWithAccelerometer() {
		if (accelerometer != null) {
			boolean registered = sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
			if (!registered) {
				Log.e("Layer", "Could not register accelerometer sensor listener!");
			}
		}
	}

	protected void unregisterWithAccelerometer() {
		if (accelerometer != null) {
			sensorManager.unregisterListener(this, accelerometer);
		}
	}

	@Override
	public void onEnter() {
		super.onEnter();
		if (isAccelerometerEnabled_)
			registerWithAccelerometer();
	}

	@Override
	public void onExit() {

		if (isAccelerometerEnabled_)
			unregisterWithAccelerometer();

		super.onExit();
	}

	public void ccAccelerometerChanged(float accelX, float accelY, float accelZ) {
		// Override to process accelerometer events.
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Override to process sensor accuracy changes (for any registered
		// sensors).
	}

	public void onSensorChanged(SensorEvent event) {
		// Override to process other sensor change events.
		// Make sure you this base implementation if you want accelerometer
		// events passed.

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			ccAccelerometerChanged(event.values[0], event.values[1], event.values[2]);
		}
		// Positive values to move on x
		if (event.values[1] > 0) {
			// In margenMax I save the margin of the screen this value depends
			// of the screen where we run the application. With this the ball
			// not disapears of the screen
			if (x <= margenMaxX) {
				// We plus in x to move the ball
				x = x + (int) Math.pow(event.values[1], 2);
			}
		} else {
			// Move the ball to the other side
			if (x >= margenMinX) {
				x = x - (int) Math.pow(event.values[1], 2);
			}
		}
		// Same in y
		if (event.values[0] < 0) {
			if (y <= margenMaxY) {
				y = y + (int) Math.pow(event.values[0], 2);
			}
		} else {
			if (y >= margenMinY) {
				y = y - (int) Math.pow(event.values[0], 2);
			}
		}

	}

	protected GameLayer(ccColor4B color) {
		super(color);

		sensorManager = (SensorManager) CCDirector.sharedDirector().getActivity().getSystemService(Context.SENSOR_SERVICE);

		// if we have a SensorManager then get the accelerometer Sensor
		if (sensorManager != null)
			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		else
			accelerometer = null;

		isAccelerometerEnabled_ = true;
		this.setIsTouchEnabled(true);
		CGSize winSize = CCDirector.sharedDirector().displaySize();

		CCSprite background = CCSprite.sprite("game_back.png");
		background.setScaleX(winSize.getWidth() / background.getTexture().getWidth());
		background.setScaleY(winSize.getHeight() / background.getTexture().getHeight());
		background.setPosition(CGPoint.make(winSize.width / 2, winSize.height / 2));
		addChild(background);

		margenMaxX = (float) winSize.getWidth() - 50;
		margenMaxY = (float) winSize.getHeight() - 50;

		_targets = new ArrayList<CCSprite>();

		CGRect rect = new CGRect();
		rect.set(5, 5, 128, 128);

		player = CCSprite.sprite("witch_2.png");

		player.setPosition(CGPoint.ccp(winSize.width / 3.0f, winSize.height / 2.0f));

		addChild(player);

		this.schedule("playerMove");
		this.schedule("gameLogic", 1.0f);
		this.schedule("update");
		this.schedule("addWeapon", 3.0f);

	}

	public void addWeapon(float dt) {
		addWeapon();
	}

	public void gameLogic(float dt) {
		addTarget();
	}

	public void playerMove(float dt) {
		player.setPosition(x, y);
	}

	public void update(float dt) {

		ArrayList<CCSprite> targetsToDelete = new ArrayList<CCSprite>();
		CGRect palyerRect = CGRect.make(player.getPosition().x, player.getPosition().y, player.getContentSize().width / 2f, player.getContentSize().height / 2f);

		for (CCSprite target : _targets) {
			CGRect targetRect = CGRect.make(target.getPosition().x, target.getPosition().y, target.getContentSize().width / 2f, target.getContentSize().height / 2f);
			if (CGRect.intersects(palyerRect, targetRect))
				targetsToDelete.add(target);

		}

		for (CCSprite target : targetsToDelete) {
			_targets.remove(target);
			removeChild(target, true);
		}

	}

	protected void addTarget() {
		Random rand = new Random();
		target = CCSprite.sprite("enemy_1.png");

		// Determine where to spawn the target along the Y axis
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		int minY = (int) (target.getContentSize().height / 2.0f);
		int maxY = (int) (winSize.height - target.getContentSize().height / 2.0f);
		int rangeY = maxY - minY;
		int actualY = rand.nextInt(rangeY) + minY;

		// Create the target slightly off-screen along the right edge,
		// and along a random position along the Y axis as calculated above
		target.setPosition(winSize.width + (target.getContentSize().width / 2.0f), actualY);
		addChild(target);

		target.setTag(1);
		_targets.add(target);

		// Determine speed of the target
		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		// Determine angle to face
		double angleRadians = 360;
		double angleDegrees = Math.toDegrees(angleRadians);
		double cocosAngle = -1 * angleDegrees;
		double rotationSpeed = 0.5 / Math.PI;
		double rotationDuration = Math.abs(angleRadians * rotationSpeed);

		CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(-target.getContentSize().width / 2.0f, actualY));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
		CCIntervalAction rotateInterval = CCRotateTo.action((float) rotationDuration, (float) cocosAngle);
		CCSequence rotate = CCSequence.actions(rotateInterval);
		target.runAction(actions);
		target.runAction(rotate);
		target.runAction(CCRepeatForever.action(rotateInterval));

	}

	public void spriteMoveFinished(Object sender) {
		CCSprite sprite = (CCSprite) sender;

		if (sprite.getTag() == 1)
			_targets.remove(sprite);

		this.removeChild(sprite, true);
	}

	private void addWeapon() {
		CGSize winSize = CCDirector.sharedDirector().displaySize();

		CCSprite weapon = CCSprite.sprite("weapon.png");

		weapon.setPosition(-weapon.getContentSize().width, weapon.getContentSize().height + 20);
		addChild(weapon);
		weapon.setTag(1);
		_weapons.add(weapon);

		Random rand = new Random();
		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(winSize.width + weapon.getTexture().getWidth(), weapon.getTexture().getHeight() + 20));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "weaponMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
		weapon.runAction(actions);

	}

	public void weaponMoveFinished(Object sender) {
		CCSprite sprite = (CCSprite) sender;

		if (sprite.getTag() == 1)
			_weapons.remove(sprite);

		this.removeChild(sprite, true);
	}

}
