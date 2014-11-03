package com.meldead.game.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

	public static Typeface getFont(Context mContext) {

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/game_over.ttf");

		return tf;
	}

}
