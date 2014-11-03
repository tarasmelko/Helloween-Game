package com.meldead.game;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import com.meldead.game.utils.Constants;

import android.view.MotionEvent;

public class GameOverLayer extends CCColorLayer {
	protected CCLabel _label;
	protected CCLabel score;

	public static CCScene scene(String message) {
		CCScene scene = CCScene.node();
		GameOverLayer layer = new GameOverLayer(ccColor4B.ccc4(255, 255, 255,
				255));

		layer.getLabel().setString(message);

		scene.addChild(layer);

		return scene;
	}

	public CCLabel getLabel() {
		return score;
	}

	public GameOverLayer(ccColor4B color) {
		super(color);

		this.setIsTouchEnabled(true);

		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite(Constants.FOLDER + "back.jpg");
		background.setScaleX(winSize.getWidth()
				/ background.getTexture().getWidth());
		background.setScaleY(winSize.getHeight()
				/ background.getTexture().getHeight());
		background.setPosition(CGPoint.make(winSize.width / 2,
				winSize.height / 2));
		addChild(background);

		_label = CCLabel.makeLabel("Game Over", "fonts/game_over.ttf",
				Constants.TEXT_SIZE_GAME_OVER_LABEL);
		_label.setColor(ccColor3B.ccRED);
		_label.setPosition(winSize.width / 2.0f, winSize.height / 2.0f
				+ _label.getTexture().getHeight() / 2);
		addChild(_label);

		score = CCLabel.makeLabel("Your score", "fonts/game_over.ttf",
				Constants.TEXT_SIZE_GAME_OVER_SCORES_LABEL);
		score.setColor(ccColor3B.ccBLACK);
		score.setPosition(winSize.width / 2.0f, winSize.height / 2.0f
				- _label.getTexture().getHeight() / 3);
		addChild(score);

		this.runAction(CCSequence.actions(CCDelayTime.action(15.0f),
				CCCallFunc.action(this, "gameOverDone")));
	}

	public void gameOverDone() {
		CCDirector.sharedDirector().replaceScene(MainMenuLayer.scene());
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		gameOverDone();
		return true;
	}
}
