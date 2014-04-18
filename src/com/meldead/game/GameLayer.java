package com.meldead.game;

import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
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

	private CGSize winSize;

	private ArrayList<CCSprite> _targets;
	private ArrayList<CCSprite> _bloods;
	private ArrayList<CCSprite> targetsToDelete;

	private CGRect palyerRect;

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private float x, y = 0;

	private float margenMaxX, margenMaxY = 30;
	private float margenMinX, margenMinY = 30;

	private boolean deletehead;
	private boolean endGame = false;
	private boolean isAccelerometerEnabled_;
	private boolean playerMoving = true;

	private CCSprite fLife;
	private CCSprite sLife;
	private CCSprite tLife;
	private CCSprite blood;
	private CCSprite player;
	private CCSprite target;
	private CCSprite head;
	private CCSprite redSplash;

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

	protected GameLayer(ccColor4B color) {
		super(color);
		// WIN SIZE
		winSize = CCDirector.sharedDirector().displaySize();
		//
		// SENSOR TOUCH
		sensorManager = (SensorManager) CCDirector.sharedDirector().getActivity().getSystemService(Context.SENSOR_SERVICE);
		// if we have a SensorManager then get the accelerometer Sensor
		if (sensorManager != null)
			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		else
			accelerometer = null;
		isAccelerometerEnabled_ = true;
		this.setIsTouchEnabled(true);
		margenMaxX = (float) winSize.getWidth() - 50;
		margenMaxY = (float) winSize.getHeight() - 50;
		// SENSOR TOUCH END

		// ARRAY OBJECT
		_bloods = new ArrayList<CCSprite>();
		_targets = new ArrayList<CCSprite>();
		//
		// BACKGROUND
		CCSprite background = CCSprite.sprite("game_back.png");
		background.setScaleX(winSize.getWidth() / background.getTexture().getWidth());
		background.setScaleY(winSize.getHeight() / background.getTexture().getHeight());
		background.setPosition(CGPoint.make(winSize.width / 2, winSize.height / 2));
		addChild(background);
		//
		// SPRITES OBJCTS
		// palyer
		player = CCSprite.sprite("witch_2.png");
		player.setPosition(CGPoint.ccp(winSize.width / 3.0f, winSize.height / 2.0f));
		addChild(player);

		// life
		fLife = CCSprite.sprite("life.png");
		sLife = CCSprite.sprite("life.png");
		tLife = CCSprite.sprite("life.png");
		fLife.setPosition(CGPoint.ccp(winSize.width - (fLife.getTexture().getWidth() * 3f) / 2f, winSize.height - fLife.getTexture().getHeight() / 2f));
		addChild(fLife);
		sLife.setPosition(CGPoint.ccp(winSize.width - (sLife.getTexture().getWidth() * 2f) / 2f, winSize.height - fLife.getTexture().getHeight() / 2f));
		addChild(sLife);
		tLife.setPosition(CGPoint.ccp(winSize.width - (tLife.getTexture().getWidth() * 1f) / 2f, winSize.height - fLife.getTexture().getHeight() / 2f));
		addChild(tLife);
		//
		// head
		head = CCSprite.sprite("witch_head.png");
		//
		// END SPRITE OBJECTS

		// GAME LOOP
		this.schedule("playerMove");
		this.schedule("gameLogic", 1.0f);
		this.schedule("getCollision");
		//
	}

	public CCMoveTo moveAction(float x, float y, float duration) {
		CCMoveTo moveUpAction = CCMoveTo.action(duration, CGPoint.ccp(x, y));
		return moveUpAction;

	}

	public void getRotation(CCSprite object) {
		double angleRadians = 360;
		double angleDegrees = Math.toDegrees(angleRadians);
		double cocosAngle = -1 * angleDegrees;
		double rotationSpeed = 0.5 / Math.PI;
		double rotationDuration = Math.abs(angleRadians * rotationSpeed);
		CCIntervalAction rotateInterval = CCRotateTo.action((float) rotationDuration, (float) cocosAngle);
		CCSequence rotate = CCSequence.actions(rotateInterval);
		object.runAction(rotate);
		object.runAction(CCRepeatForever.action(rotateInterval));
	}

	public void redSplash() {
		redSplash = CCSprite.sprite("red_back.png");
		redSplash.setScaleX(winSize.getWidth() / redSplash.getTexture().getWidth());
		redSplash.setScaleY(winSize.getHeight() / redSplash.getTexture().getHeight());
		redSplash.setPosition(CGPoint.make(winSize.width / 2, winSize.height / 2));
		addChild(redSplash);
		CCCallFuncN removeSplash = CCCallFuncN.action(this, "removeSplash");
		CCSequence actions = CCSequence.actions(removeSplash);
		redSplash.runAction(actions);

	}

	public void ccAccelerometerChanged(float accelX, float accelY, float accelZ) {
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			ccAccelerometerChanged(event.values[0], event.values[1], event.values[2]);
		}
		if (event.values[1] > 0) {
			if (x <= margenMaxX) {
				x = x + (int) Math.pow(event.values[1], 2);
			}
		} else {
			if (x >= margenMinX) {
				x = x - (int) Math.pow(event.values[1], 2);
			}
		}
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

	public void gameLogic(float dt) {
		if (playerMoving)
			addTarget();
	}

	public void playerMove(float dt) {
		if (playerMoving)
			player.setPosition(x, y);
	}

	public void getCollision(float dt) {
		if (playerMoving)
			collision();
	}

	private void collision() {
		targetsToDelete = new ArrayList<CCSprite>();
		palyerRect = CGRect.make(player.getPosition().x, player.getPosition().y, player.getContentSize().width / 2f, player.getContentSize().height / 2f);
		for (CCSprite target : _targets) {
			CGRect targetRect = CGRect.make(target.getPosition().x, target.getPosition().y, target.getContentSize().width / 2f, target.getContentSize().height / 2f);
			if (CGRect.intersects(palyerRect, targetRect)) {
				targetsToDelete.add(target);
				addBloodAnim(player.getPosition().x, player.getPosition().y);
			}
		}
		for (CCSprite target : targetsToDelete) {
			_targets.remove(target);
			removeChild(target, true);
		}

	}

	protected void addBloodAnim(float x, float y) {
		blood = CCSprite.sprite("blood.png");
		blood.setPosition(x, y);
		addChild(blood);
		blood.setTag(3);
		redSplash();
		if (endGame) {
			playerMoving = false;
			CCSequence actions = CCSequence.actions(moveAction(winSize.width - (tLife.getTexture().getWidth() * 1f) / 2f, winSize.height + 100, 1f));
			tLife.runAction(actions);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "gameOver");
			CCSequence actionPlayerEnd = CCSequence.actions(moveAction(player.getPosition().x, -player.getTexture().getHeight(), 1f), actionMoveDone);
			player.runAction(actionPlayerEnd);

		} else {
			if (deletehead) {
				this.removeChild(player, true);
				player = CCSprite.sprite("witch_unhead.png");
				addChild(player);
				head.setPosition(x, y + player.getTexture().getHeight() / 2);
				addChild(head);
				head.runAction(CCSequence.actions(moveAction(-100, winSize.getHeight() + 100, 2.0f)));
				getRotation(head);
				endGame = true;
				CCSequence actions = CCSequence.actions(moveAction(winSize.width - (sLife.getTexture().getWidth() * 2f) / 2f, winSize.height + 100, 1f));
				sLife.runAction(actions);
			} else {
				// repaint witch
				this.removeChild(player, true);
				player = CCSprite.sprite("witch_blooded.png");
				addChild(player);
				deletehead = true;
				CCSequence actions = CCSequence.actions(moveAction(winSize.width - (fLife.getTexture().getWidth() * 3f) / 2f, winSize.height + 100, 1f));
				fLife.runAction(actions);
			}

		}
		_bloods.add(blood);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "bloodFade");
		CCSequence actions = CCSequence.actions(CCDelayTime.action(2f), actionMoveDone);
		blood.runAction(actions);
	}

	protected void addTarget() {
		Random rand = new Random();
		target = CCSprite.sprite("topor.png");
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		int minY = (int) (target.getContentSize().height / 2.0f);
		int maxY = (int) (winSize.height - target.getContentSize().height / 2.0f);
		int rangeY = maxY - minY;
		int actualY = rand.nextInt(rangeY) + minY;

		target.setPosition(winSize.width + (target.getContentSize().width / 2.0f), actualY);
		addChild(target);

		target.setTag(1);
		_targets.add(target);

		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
		CCSequence actions = CCSequence.actions(moveAction(-target.getContentSize().width / 2.0f, actualY, actualDuration), actionMoveDone);
		target.runAction(actions);
		getRotation(target);
	}

	// FINISHES
	public void bloodRemove(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		if (sprite.getTag() == 3)
			_bloods.remove(sprite);
		this.removeChild(sprite, true);
	}

	public void spriteMoveFinished(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		if (sprite.getTag() == 1)
			_targets.remove(sprite);

		this.removeChild(sprite, true);
	}

	public void bloodFade(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		CCFadeOut fadeOut = CCFadeOut.action(0.5f);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "bloodRemove");
		CCSequence actions = CCSequence.actions(fadeOut, actionMoveDone);
		sprite.runAction(actions);

	}

	public void gameOver(Object sender) {
		Log.e("game", "over");
		CCDirector.sharedDirector().replaceScene(GameOverLayer.scene("Game Over"));
	}

	public void removeSplash(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		CCFadeOut fadeOut = CCFadeOut.action(0.5f);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "deleteSplash");
		CCSequence actions = CCSequence.actions(fadeOut, actionMoveDone);
		sprite.runAction(actions);
	}

	public void deleteSplash(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		this.removeChild(sprite, true);
	}

}
