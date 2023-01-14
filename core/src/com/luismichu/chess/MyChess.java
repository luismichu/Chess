package com.luismichu.chess;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyChess extends Game {
	SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		setScreen(new Board());
	}
}
