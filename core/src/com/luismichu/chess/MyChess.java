package com.luismichu.chess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.luismichu.chess.Screen.BoardScreen;

public class MyChess extends Game {
	SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		setScreen(new BoardScreen());
	}
}
