package com.meldead.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.utils.CCFormatter;

import com.meldead.game.utils.Constants;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

public class GameLayer extends CCColorLayer {

	private CGSize winSize;

	private ArrayList<CCSprite> _targets;
	private ArrayList<CCSprite> _buildings;
	private ArrayList<CCSprite> _buildingsLow;
	private ArrayList<CCSprite> _bloods;
	private ArrayList<CCSprite> targetsToDelete;

	private CGRect palyerRect;

	private float x, y = 0;
	private int iterator = 0;
	private float motion = -3;
	private int score = 0;

	private boolean deletehead;
	private boolean endGame = false;
	private boolean playerMoving = true;
	private boolean isDrawed = false;
	private boolean isDrawedE = false;
	private boolean isDrawedT = false;
	private int iteratorDraw = 0;
	private int resumeIterator = 0;

	private CCSprite fLife;
	private CCSprite sLife;
	private CCSprite tLife;
	private CCSprite player;
	private CCSprite redSplash;
	private CCSprite exit;
	private CCLabel scoreLabel;
	private CCLabel resume;
	private CCSprite pen;
	private CCSprite build;
	private CCSprite pause;
	private CCSprite fire;

	private float enemyLevelSpeed = 0.3f;
	private float actualDuration = 2.5f;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));

		scene.addChild(layer);
		return scene;
	}

	@Override
	public void onEnter() {
		super.onEnter();
	}

	@Override
	public void onExit() {
		super.onExit();
	}

	protected GameLayer(ccColor4B color) {
		super(color);

		CCDirector.sharedDirector().setDisplayFPS(true);
		// WIN SIZE
		winSize = CCDirector.sharedDirector().displaySize();
		//

		this.setIsTouchEnabled(true);

		// ARRAY OBJECT
		_bloods = new ArrayList<CCSprite>();
		_targets = new ArrayList<CCSprite>();
		_buildings = new ArrayList<CCSprite>();
		_buildingsLow = new ArrayList<CCSprite>();
		//
		// BACKGROUND
		// red back

		redSplash = CCSprite.sprite(Constants.FOLDER + "red_back.png");

		redSplash.setScaleX(winSize.getWidth()
				/ redSplash.getTexture().getWidth());
		redSplash.setScaleY(winSize.getHeight()
				/ redSplash.getTexture().getHeight());
		redSplash.setPosition(CGPoint.make(winSize.width / 2,
				winSize.height / 2));
		addChild(redSplash);

		build = CCSprite.sprite(Constants.FOLDER + "build/build_00.png");

		CCSprite background = CCSprite.sprite(Constants.FOLDER + "back.jpg");
		background.setScaleX(winSize.getWidth()
				/ background.getTexture().getWidth());
		background.setScaleY(winSize.getHeight()
				/ background.getTexture().getHeight());
		background.setPosition(CGPoint.make(winSize.width / 2,
				winSize.height / 2));
		addChild(background);

		scoreLabel = CCLabel.makeLabel("Start", "fonts/game_over.ttf",
				Constants.TEXT_SIZE_GAME_SCORE_LABEL);
		scoreLabel.setColor(ccColor3B.ccBLACK);
		scoreLabel.setPosition(winSize.width / 2.0f, winSize.height
				- scoreLabel.getTexture().getHeight() / 1.5f);
		addChild(scoreLabel);

		fLife = CCSprite.sprite(Constants.FOLDER + "life.png");
		sLife = CCSprite.sprite(Constants.FOLDER + "life.png");
		tLife = CCSprite.sprite(Constants.FOLDER + "life.png");
		fLife.setPosition(CGPoint.ccp(winSize.width
				- (fLife.getTexture().getWidth() * 3.5f), winSize.height
				- fLife.getTexture().getHeight()));
		addChild(fLife);
		sLife.setPosition(CGPoint.ccp(winSize.width
				- (sLife.getTexture().getWidth() * 2.2f), winSize.height
				- fLife.getTexture().getHeight()));
		addChild(sLife);
		tLife.setPosition(CGPoint.ccp(winSize.width
				- (tLife.getTexture().getWidth() * 1f), winSize.height
				- fLife.getTexture().getHeight()));
		addChild(tLife);

		// palyer

		pen = CCSprite.sprite(Constants.FOLDER + "pen.png");
		pen.setPosition(-200, winSize.getHeight() + 200);

		player = CCSprite.sprite(Constants.FOLDER + "bird/bird0.png");
		x = winSize.width / 4.0f;
		y = winSize.height / 2.0f;
		player.setPosition(CGPoint.ccp(x + pen.getTexture().getWidth() / 4, y));
		addChild(player);
		addChild(pen);
		player.setVisible(false);

		// life
		fire = CCSprite.sprite(Constants.FOLDER + "shit.png");
		fire.setPosition(winSize.width - fire.getTexture().getWidth() / 1.3f,
				fire.getTexture().getHeight() / 1.3f);
		addChild(fire);
		//
		// head
		exit = CCSprite.sprite(Constants.FOLDER + "exit.png");
		exit.setPosition(exit.getTexture().getWidth() * 1.3f, winSize.height
				- exit.getTexture().getHeight() / 1.5f);

		addChild(exit);

		pause = CCSprite.sprite(Constants.FOLDER + "pause.png");
		pause.setPosition(pause.getTexture().getWidth() * 2.3f, winSize.height
				- pause.getTexture().getHeight() / 1.5f);

		addChild(pause);

		resume = CCLabel.makeLabel("Resume", "fonts/game_over.ttf",
				Constants.TEXT_SIZE_GAME_RESUME_LABEL);
		resume.setColor(ccColor3B.ccBLACK);
		resume.setPosition(winSize.width / 2.0f, winSize.height / 2);
		addChild(resume);
		resume.setVisible(false);
		//
		// END SPRITE OBJECTS

		startAction();

	}

	public void startPlayerAnim() {

		CCAnimation anim = CCAnimation.animation("", 0.1f);
		for (int i = 0; i <= 9; i++) {
			anim.addFrame(Constants.FOLDER + "bird/bird" + i + ".png");
		}
		CCIntervalAction rotateInterval = CCAnimate.action(0.5f, anim, true);
		player.runAction(CCRepeatForever.action(rotateInterval));
	}

	public void startAction() {
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "startGame");
		CCMoveTo moveAction = CCMoveTo.action(
				0.8f,
				CGPoint.ccp(winSize.getWidth() / 4.0f
						+ (pen.getTexture().getWidth() / 2), winSize.height
						/ 2.0f + (pen.getTexture().getHeight() / 2)));
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		pen.runAction(actions);
	}

	public boolean ccTouchesEnded(MotionEvent event) {
		motion = Constants.SPEED;
		return super.ccTouchesEnded(event);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint location = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));
		if (CGRect.containsPoint((exit.getBoundingBox()), location)) {
			CCDirector.sharedDirector().replaceScene(MainMenuLayer.scene());
		} else if (CGRect.containsPoint((pause.getBoundingBox()), location)) {
			pauseGame();
		} else if (CGRect.containsPoint((resume.getBoundingBox()), location)) {
			resumeIterator = 3;
			resumeGame();
		} else if (CGRect.containsPoint((fire.getBoundingBox()), location)) {
			addMissile();
		} else {
			motion = 10;
		}
		return super.ccTouchesBegan(event);
	}

	private void addMissile() {

	}

	public void resumeGame() {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (resumeIterator != 0) {
					resume.setString("" + resumeIterator--);
				} else {
					timer.cancel();
					resumeGameAll();
				}
			}
		};

		timer.scheduleAtFixedRate(task, 0, 1000);

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
		CCIntervalAction rotateInterval = CCRotateTo.action(
				(float) rotationDuration, (float) cocosAngle);
		CCSequence rotate = CCSequence.actions(rotateInterval);
		object.runAction(rotate);
		object.runAction(CCRepeatForever.action(rotateInterval));
	}

	public void redSplash() {
		addChild(redSplash);
		CCCallFuncN removeSplash = CCCallFuncN.action(this, "removeSplash");
		CCSequence actions = CCSequence.actions(removeSplash);
		redSplash.runAction(actions);

	}

	public void playerMove(float dt) {
		if (playerMoving) {

			y = player.getPosition().y + motion;
			x = player.getPosition().x;
			if (y > winSize.getHeight() || y < 0)
				endGame();
			player.setPosition(x, y);

		}
	}

	public void getCollision(float dt) {
		if (playerMoving)
			collision();
	}

	private void collision() {
		targetsToDelete = new ArrayList<CCSprite>();
		palyerRect = CGRect.make(player.getPosition().x,
				player.getPosition().y, player.getContentSize().width / 2f,
				player.getContentSize().height / 2f);
		for (CCSprite target : _targets) {
			CGRect targetRect = CGRect.make(target.getPosition().x,
					target.getPosition().y, target.getContentSize().width / 2f,
					target.getContentSize().height / 2f);
			if (CGRect.intersects(palyerRect, targetRect)) {
				targetsToDelete.add(target);
				addBloodAnim(player.getPosition().x, player.getPosition().y);
			}
		}

		for (CCSprite target : _buildings) {
			CGRect targetRect = CGRect.make(target.getPosition().x
					- (target.getTexture().getWidth() / 3f),
					target.getPosition().y
							- (target.getTexture().getHeight() / 3f),
					target.getContentSize().width
							- target.getTexture().getWidth() / 5,
					target.getContentSize().height
							- target.getTexture().getHeight() / 5);
			if (CGRect.intersects(palyerRect, targetRect)) {
				endGame = true;
				addBloodAnim(player.getPosition().x, player.getPosition().y);
			}
		}

		for (CCSprite target : targetsToDelete) {
			_targets.remove(target);
			removeChild(target, true);
		}

	}

	protected void addBloodAnim(float x, float y) {
		// blood
		CCSprite blood = CCSprite.sprite(Constants.FOLDER + "blood.png");
		blood.setPosition(x, y);
		addChild(blood);
		blood.setTag(3);
		redSplash();
		if (endGame) {
			endGame();
		} else {
			if (deletehead) {
				player.setPosition(x, y);
				addChild(player);
				endGame = true;
				CCSequence actions = CCSequence.actions(moveAction(
						winSize.width - (sLife.getTexture().getWidth() * 2f)
								/ 2f, winSize.height + 100, 1f));
				sLife.runAction(actions);
			} else {
				// repaint witch
				player.setPosition(x, y);
				addChild(player);

				deletehead = true;
				CCSequence actions = CCSequence.actions(moveAction(
						winSize.width - (fLife.getTexture().getWidth() * 3f)
								/ 2f, winSize.height + 100, 1f));
				fLife.runAction(actions);
			}

		}
		_bloods.add(blood);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "bloodFade");
		CCSequence actions = CCSequence.actions(CCDelayTime.action(0.5f),
				actionMoveDone);
		blood.runAction(actions);
	}

	private void endGame() {

		for (CCSprite target : _buildings) {
			target.stopAllActions();
		}
		for (CCSprite target : _targets) {
			target.stopAllActions();
		}
		for (CCSprite target : _buildingsLow) {
			target.stopAllActions();
		}
		pen.stopAllActions();
		player.stopAllActions();
		player.setPosition(x, y);
		addChild(player);
		pen.setPosition(pen.getPosition().x, pen.getPosition().y);
		addChild(pen);
		playerMoving = false;
		CCSequence actions = CCSequence.actions(moveAction(winSize.width
				- (tLife.getTexture().getWidth() * 1f) / 2f,
				winSize.height + 100, 1f));
		tLife.runAction(actions);

		// pen
		iterator = 0;
		CCCallFuncN actionMoveFinish = CCCallFuncN.action(this, "erasePl");
		CCMoveTo moveAction = CCMoveTo.action(0.8f, CGPoint.ccp(
				player.getPosition().x + pen.getTexture().getWidth() / 2,
				player.getPosition().y + player.getTexture().getHeight() / 2));
		CCSequence action = CCSequence.actions(moveAction, actionMoveFinish);
		pen.runAction(action);

		double angleRadians = 260;
		double angleDegrees = Math.toDegrees(angleRadians);
		double cocosAngle = -1 * angleDegrees;

		CCIntervalAction rotateInterval = CCRotateTo.action(0.7f,
				(float) cocosAngle);
		CCSequence rotate = CCSequence.actions(rotateInterval);
		pen.runAction(rotate);

	}

	protected void addTarget(float y) {

		CCSprite target = CCSprite.sprite(Constants.FOLDER + "cross.png");

		Random rand = new Random();
		int minY = (int) (build.getContentSize().height);
		int maxY = (int) (winSize.height - target.getContentSize().height / 3.0f);
		int rangeY = maxY - minY;
		int actualY = rand.nextInt(rangeY) + minY;

		target.setPosition(winSize.width - (pen.getContentSize().width / 3.0f),
				y);
		addChild(target);

		target.setTag(1);
		_targets.add(target);

		CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
				"spriteMoveFinished");

		CCMoveTo moveAction = CCMoveTo.action(actualDuration,
				CGPoint.ccp(-target.getContentSize().width / 2.0f, actualY));
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		target.runAction(actions);
		getRotation(target);
	}

	public void bloodRemove(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		if (sprite.getTag() == 3)
			_bloods.remove(sprite);
		this.removeChild(sprite, true);
	}

	public void spriteMoveFinished(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		if (sprite.getTag() == 1) {
			_targets.remove(sprite);
		}

		if (sprite.getTag() == 5)
			_buildings.remove(sprite);
		if (sprite.getTag() == 6) {
			_buildingsLow.remove(sprite);
		}
		this.removeChild(sprite, true);
	}

	public void startGame(Object sender) {
		CCCallFuncN actionMoveDone;
		if (iterator < 5) {
			actionMoveDone = CCCallFuncN.action(this, "startGame");
		} else {
			actionMoveDone = CCCallFuncN.action(this, "startReal");
		}
		CCMoveTo moveAction;
		if (!isDrawed) {
			moveAction = CCMoveTo
					.action(0.1f, CGPoint.ccp(winSize.getWidth() / 4.0f
							+ (pen.getTexture().getWidth() / 2) - 30,
							winSize.height / 2.0f
									+ (pen.getTexture().getHeight() / 2)));
			isDrawed = true;
		} else {
			moveAction = CCMoveTo
					.action(0.1f, CGPoint.ccp(winSize.getWidth() / 4.0f
							+ (pen.getTexture().getWidth() / 2) + 30,
							winSize.height / 2.0f
									+ (pen.getTexture().getHeight() / 2)));
			isDrawed = false;
		}
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		pen.runAction(actions);
		iterator = iterator + 1;

	}

	public void startReal(Object sender) {
		player.setVisible(true);
		CCCallFuncN actionMoveDone = CCCallFuncN
				.action(this, "startPlayerMove");
		CCMoveTo moveAction = CCMoveTo.action(
				0.8f,
				CGPoint.ccp(winSize.getWidth(), winSize.height
						- (pen.getTexture().getHeight() / 3)));
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		pen.runAction(actions);
	}

	public void startPlayerMove(Object sender) {
		// this.schedule("addBuildings", 0.8f);
		// this.schedule("addBuildingsLow", 0.2f);
		this.schedule("playerMove");
		this.schedule("getCollision");
		this.schedule("updateScoreLabel", 4f);

		checkBackground();
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "movePen");
		CCSequence actions = CCSequence.actions(actionMoveDone);
		runAction(actions);
		startPlayerAnim();

	}

	public void bloodFade(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		CCFadeOut fadeOut = CCFadeOut.action(0.3f);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "bloodRemove");
		CCSequence actions = CCSequence.actions(fadeOut, actionMoveDone);
		sprite.runAction(actions);

	}

	public void erasePl(Object sender) {
		CCCallFuncN actionMoveDone;
		if (iterator < 10) {
			actionMoveDone = CCCallFuncN.action(this, "erasePl");
		} else {
			actionMoveDone = CCCallFuncN.action(this, "finish");
		}
		CCMoveTo moveAction;
		if (!isDrawed) {
			moveAction = CCMoveTo.action(0.1f,
					CGPoint.ccp(pen.getPosition().x - 30, pen.getPosition().y));
			isDrawed = true;
		} else {
			moveAction = CCMoveTo.action(0.1f,
					CGPoint.ccp(pen.getPosition().x + 30, pen.getPosition().y));
			isDrawed = false;
		}
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		pen.runAction(actions);
		iterator = iterator + 1;
	}

	public void finish(Object sender) {

		CCSequence actionD = null;
		for (CCSprite target : _buildings) {
			actionD = CCSequence.actions(moveDown(target.getPosition().x));
			target.runAction(actionD);
		}
		for (CCSprite target : _targets) {
			actionD = CCSequence.actions(moveDown(target.getPosition().x));
			target.runAction(actionD);
		}
		for (CCSprite target : _buildingsLow) {
			actionD = CCSequence.actions(moveDown(target.getPosition().x));
			target.runAction(actionD);
		}
		player.setVisible(false);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "gameOver");
		CCSequence actions = CCSequence.actions(CCDelayTime.action(4f),
				actionMoveDone);
		player.runAction(actions);
	}

	private CCMoveTo moveDown(float x) {
		return CCMoveTo.action(1f, CGPoint.ccp(x, -200));
	}

	public void gameOver(Object sender) {
		if (score > PreferenceManager.getDefaultSharedPreferences(
				CCDirector.sharedDirector().getActivity()).getInt("BEST", 0))
			PreferenceManager
					.getDefaultSharedPreferences(
							CCDirector.sharedDirector().getActivity()).edit()
					.putInt("BEST", score).commit();
		CCDirector.sharedDirector().replaceScene(
				GameOverLayer.scene("Your score " + score));

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

	public void movePen(Object sender) {
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "drawEnemy");
		Random rand = new Random();
		int minY = (int) (pen.getContentSize().height);
		int maxY = (int) (winSize.height);
		int rangeY = maxY - minY;
		int actualY = rand.nextInt(rangeY) + minY;
		CCMoveTo moveAction = CCMoveTo.action(enemyLevelSpeed,
				CGPoint.ccp(winSize.getWidth(), actualY));
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		iteratorDraw = 0;
		isDrawedE = false;
		if (playerMoving)
			pen.runAction(actions);
	}

	public void drawEnemy(Object sender) {
		CCCallFuncN actionMoveDone;
		if (iteratorDraw < 3) {
			actionMoveDone = CCCallFuncN.action(this, "drawEnemy");
		} else {
			addTarget(pen.getPosition().y - pen.getTexture().getHeight() / 2);
			actionMoveDone = CCCallFuncN.action(this, "movePen");
		}
		CCMoveTo moveAction = null;
		if (!isDrawedE) {
			moveAction = CCMoveTo.action(0.07f, CGPoint.ccp(
					pen.getPosition().x - 40, pen.getPosition().y + 40));
			isDrawedE = true;
		} else if (isDrawedE && !isDrawedT) {

			moveAction = CCMoveTo.action(0.07f,
					CGPoint.ccp(pen.getPosition().x + 40, pen.getPosition().y));
			isDrawedT = true;
		} else if (isDrawedT) {
			moveAction = CCMoveTo.action(0.07f, CGPoint.ccp(
					pen.getPosition().x - 40, pen.getPosition().y - 40));
			isDrawedT = false;
			isDrawedE = false;
		}
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		pen.runAction(actions);
		iteratorDraw = iteratorDraw + 1;

	}

	public void addBuildings(float dt) {
		Random randB = new Random();
		int type = randB.nextInt(6);
		CCSprite target = null;

		target = CCSprite.sprite(Constants.FOLDER + "build/build_0" + type
				+ ".png");

		int high = 2 + randB.nextInt(2);

		target.setPosition(winSize.width + target.getContentSize().width / 2,
				target.getContentSize().height / high);
		if (playerMoving)
			addChild(target);

		target.setTag(5);

		_buildings.add(target);

		CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
				"spriteMoveFinished");
		CCMoveTo moveAction = CCMoveTo.action(
				4,
				CGPoint.ccp(-target.getContentSize().width / 2.0f,
						target.getContentSize().height / high));
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		target.runAction(actions);

	}

	public void addBuildingsLow(float dt) {
		Random randB = new Random();
		int type = randB.nextInt(5);
		CCSprite target = null;

		target = CCSprite.sprite(Constants.FOLDER + "build/build_0" + type
				+ ".png");

		int high = -10;

		target.setPosition(winSize.width + target.getContentSize().width / 2,
				high);
		if (playerMoving)
			addChild(target);

		target.setTag(6);

		_buildingsLow.add(target);

		CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
				"spriteMoveFinished");
		CCMoveTo moveAction = CCMoveTo.action(
				4,
				CGPoint.ccp(-target.getContentSize().width / 2.0f * 2,
						target.getContentSize().height / high));
		CCSequence actions = CCSequence.actions(moveAction, actionMoveDone);
		target.runAction(actions);
	}

	private boolean isApplicationBroughtToBackground() {
		ActivityManager am = (ActivityManager) CCDirector.sharedDirector()
				.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(
					CCDirector.sharedDirector().getActivity().getPackageName())) {
				return true;
			}
		}

		return false;
	}

	public void checkBackground() {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (isApplicationBroughtToBackground()) {
					pauseGame();
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000);

	}

	private void resumeGameAll() {
		CCDirector.sharedDirector().resume();
		resume.setVisible(false);
	}

	private void pauseGame() {
		CCDirector.sharedDirector().pause();
		resume.setString("Resume");
		resume.setVisible(true);
	}

	public void updateScoreLabel(float dt) {
		score += 1;
		String string = CCFormatter.format("%04d", score);
		scoreLabel.setString(string + " km");
	}

}
