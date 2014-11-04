package com.meldead.game;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.meldead.game.utils.Constants;

public class MainMenuLayer extends CCColorLayer {
	protected CCLabel _labelStartGame;
	protected CCLabel _bestScores;
	private CCSprite _keyPad;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		MainMenuLayer layer = new MainMenuLayer(ccColor4B.ccc4(255, 255, 255,
				255));

		scene.addChild(layer);

		return scene;
	}

	protected MainMenuLayer(ccColor4B color) {
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
		_keyPad = CCSprite.sprite(Constants.FOLDER + "game.png");

		_keyPad.setPosition(winSize.width / 2.0f, winSize.height / 2.0f
				+ _keyPad.getTexture().getHeight() / 3);

		_labelStartGame = CCLabel.makeLabel("Start", "fonts/game_over.ttf",
				Constants.TEXT_SIZE_MAIN_MENU_START);
		_labelStartGame.setColor(ccColor3B.ccBLACK);
		_labelStartGame.setPosition(winSize.width / 2.0f, winSize.height / 2.0f
				- _keyPad.getTexture().getHeight() / 4);
		addChild(_labelStartGame);
		addChild(_keyPad);

		_bestScores = CCLabel.makeLabel(
				"Best scores: "
						+ PreferenceManager.getDefaultSharedPreferences(
								CCDirector.sharedDirector().getActivity())
								.getInt("BEST", 0), "fonts/game_over.ttf",
				Constants.TEXT_SIZE_MAIN_MENU_BEST);
		_bestScores.setColor(ccColor3B.ccBLACK);
		_bestScores.setPosition(winSize.width / 2.0f, _bestScores.getTexture()
				.getHeight() / 2.0f);
		addChild(_bestScores);

	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint location = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));
		if (CGRect.containsPoint((_keyPad.getBoundingBox()), location)
				|| CGRect.containsPoint((_labelStartGame.getBoundingBox()),
						location)) {
			CCDirector.sharedDirector().replaceScene(GameLayer.scene());
		}

		return true;
	}
}
