package com.luismichu.chess;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.*;


public class Board implements Screen, InputProcessor {
	private SpriteBatch batch;
	private Texture img, bg_dark, bg_brigth;
	private Stage stage;
	private Table boardTable;
	private boolean clicked = false;
	private int[] clicked_pos;


	private static final int ORIGINAL = 8;
	private static final int RESIZED = 11;

	@Override
	public void show () {
		batch = new SpriteBatch();

		int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		img = resizeTexture("images/no_shadow/128h/b_bishop_png_128px.png", size/RESIZED, size/RESIZED);
		bg_brigth = resizeTexture("images/no_shadow/128h/square gray light _png_128px.png", size/ORIGINAL, size/ORIGINAL);
		bg_dark = resizeTexture("images/no_shadow/128h/square gray dark _png_128px.png", size/ORIGINAL, size/ORIGINAL);

		stage = new Stage(new FitViewport(size, size));
		//Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(this);

		createBoardTable();
		//board();

		clicked_pos = new int[2];
	}

	@Override
	public void render (float delta) {
		ScreenUtils.clear(1, 1, 1, 1);
		stage.act(delta);

		batch.begin();
		//batch.draw(img, 0, 0);
		drawBoard();
		if(clicked)
			batch.draw(img, clicked_pos[0] - (int)(img.getWidth() / 2), Gdx.graphics.getHeight() - clicked_pos[1] - (int)(img.getHeight() / 2));

		batch.end();

		stage.draw();
	}

	private void drawBoard(){
		int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for(int i = 0; i < ORIGINAL; i++){
			for (int j = 0; j < ORIGINAL; j++) {
				int positionX = size/ORIGINAL * j;
				int positionY = size/ORIGINAL * i;

				if ((j + i) % 2 == 0)
					batch.draw(bg_dark, positionX, positionY);
				else
					batch.draw(bg_brigth, positionX, positionY);


				positionX += (size/ORIGINAL - size/RESIZED) / 2;
				positionY += (size/ORIGINAL - size/RESIZED) / 2;
				batch.draw(img, positionX, positionY);



				//System.out.println(positionX);
			}
		}
	}

	private void createBoardTable(){
		boardTable = new Table();
		boardTable.setWidth(stage.getWidth());
		boardTable.align(Align.center | Align.top);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		boardTable.setPosition(0, screenSize.height);

		Image image = new Image(img);
		boardTable.padTop(20);
		boardTable.row();
		boardTable.add(image);
		boardTable.row();
		Skin skin = new Skin(Gdx.files.internal("skins/craftacular/skin/craftacular-ui.json"));
		boardTable.add(new Label("test", skin));
	}

	private void board(){
		stage.clear();
		stage.addActor(boardTable);
		stage.addListener(new InputListener());
	}

	private Texture resizeTexture(String file, int sizeX, int sizeY){
		Pixmap pixmapOriginal = new Pixmap(Gdx.files.internal(file));
		Pixmap pixmapResized = new Pixmap(sizeX, sizeY, pixmapOriginal.getFormat());
		pixmapResized.drawPixmap(pixmapOriginal,
				0, 0, pixmapOriginal.getWidth(), pixmapOriginal.getHeight(),
				0, 0, pixmapResized.getWidth(), pixmapResized.getHeight()
		);
		Texture texture = new Texture(pixmapResized);
		pixmapOriginal.dispose();
		pixmapResized.dispose();

		return texture;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
		img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		clicked = true;

		clicked_pos[0] = screenX;
		clicked_pos[1] = screenY;

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		clicked = false;

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		clicked_pos[0] = screenX;
		clicked_pos[1] = screenY;

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
