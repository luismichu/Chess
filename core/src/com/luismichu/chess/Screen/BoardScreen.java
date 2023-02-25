package com.luismichu.chess.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.chess.Controller.BoardController;
import com.luismichu.chess.Controller.MyAssetManager;
import com.luismichu.chess.Model.Piece;
import com.luismichu.chess.Model.Position;

import java.awt.*;


public class BoardScreen implements Screen {
	private SpriteBatch batch;
	private Texture square_black, square_white;
	private Stage stage;
	private Table boardTable;
	private BoardController myBoardController;
	private boolean clicked;
	private boolean clickedFlag;
	private Position pxToBoard;
	private ShapeRenderer shapeRenderer;
	private Array<Texture> pieces;
	private MyAssetManager myAssetManager;

	private static final int ORIGINAL = 8;
	private static final float RESIZED = 0.8f;

	@Override
	public void show () {
		myBoardController = new BoardController(Piece.Color.WHITE);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		myAssetManager = new MyAssetManager();

		int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//img = resizeTexture("images/no_shadow/128/b_bishop.png", size/RESIZED);
		//img = resizeTexture("images/no_shadow/128/b_king.png", size/RESIZED);
		square_white = resizeTexture("images/no_shadow/128/square_gray_light.png", size/ORIGINAL);
		square_black = resizeTexture("images/no_shadow/128/square_gray_dark.png", size/ORIGINAL);

		square_white = myAssetManager.loadSquare(MyAssetManager.Assets.Shadow.NO_SHADOW,
												MyAssetManager.Assets.Px.PX512,
												MyAssetManager.Assets.SquareColor.GRAY_SQUARE,
												MyAssetManager.Assets.SquareBright.WHITE_SQUARE);
		square_black = myAssetManager.loadSquare(MyAssetManager.Assets.Shadow.NO_SHADOW,
												MyAssetManager.Assets.Px.PX512,
												MyAssetManager.Assets.SquareColor.GRAY_SQUARE,
												MyAssetManager.Assets.SquareBright.BLACK_SQUARE);

		for(Piece[] line : myBoardController.getPieces()){
			for(Piece piece : line){
				if(piece != null) {
					MyAssetManager.Assets.Type type = new MyAssetManager.Assets.Type(piece.getType().toString());
					MyAssetManager.Assets.Color color = piece.getColor() == Piece.Color.WHITE? MyAssetManager.Assets.Color.WHITE : MyAssetManager.Assets.Color.BLACK;
					piece.setTexture(myAssetManager.loadPiece(MyAssetManager.Assets.Shadow.NO_SHADOW,
																MyAssetManager.Assets.Px.PX256,
																color,
																type));
				}
			}
		}

		stage = new Stage(new FitViewport(size, size));
		//Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(myBoardController);

		clicked = false;
		clickedFlag = false;
		pxToBoard = new Position();

		//createBoardTable();
		//board();

	}

	@Override
	public void render (float delta) {
		ScreenUtils.clear(1, 1, 1, 1);
		stage.act(delta);

		batch.begin();
		//batch.draw(img, 0, 0);
		drawBoard();
		batch.end();

		if(clickedFlag) {
			for (Position allowedPosition : myBoardController.allowedMoves(pxToBoard)) {
				shapeRenderer.setColor(Color.CYAN);
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				Position pos = boardToPX(allowedPosition);
				shapeRenderer.circle(pos.X, pos.Y, 20);
				shapeRenderer.end();
			}
		}

		if(myBoardController.isScreenClicked()){
			if(!clicked) {
				if(pxToBoard.X != myBoardController.getScreenClickedPos()[0] / (Gdx.graphics.getWidth() / 8) ||
					pxToBoard.Y != 7 - myBoardController.getScreenClickedPos()[1] / (Gdx.graphics.getHeight() / 8))
					clickedFlag = true;

				pxToBoard.X = myBoardController.getScreenClickedPos()[0] / (Gdx.graphics.getWidth() / 8);
				pxToBoard.Y = 7 - myBoardController.getScreenClickedPos()[1] / (Gdx.graphics.getHeight() / 8);
			}
			Piece currPiece = myBoardController.getPiece(pxToBoard);
			if(currPiece != null && currPiece.getColor() == myBoardController.getColor()) {
				int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				int length = size/ORIGINAL;
				int height = (int)(length * 0.9);
				int width = currPiece.getTexture().getWidth() * height / currPiece.getTexture().getHeight();
				batch.begin();
				batch.draw(currPiece.getTexture(), myBoardController.getScreenClickedPos()[0] - width / 2, Gdx.graphics.getHeight() - myBoardController.getScreenClickedPos()[1] - height / 2, width, height);
				batch.end();
			}
			clicked = true;
		}
		else {
			if(clicked) {
				Position releasePos = new Position();
				releasePos.X = myBoardController.getScreenClickedPos()[0] / (Gdx.graphics.getWidth() / 8);
				releasePos.Y = 7 - myBoardController.getScreenClickedPos()[1] / (Gdx.graphics.getHeight() / 8);
				clickedFlag = true;
				for(Position pos : myBoardController.allowedMoves(pxToBoard)) {
					if (pos.X == releasePos.X && pos.Y == releasePos.Y) {
						myBoardController.move(pxToBoard, releasePos);
						clickedFlag = false;
						break;
					}
				}
				clicked = false;
			}
		}

		stage.draw();
	}

	private void drawBoard(){
		int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for(int i = 0; i < ORIGINAL; i++){
			for (int j = 0; j < ORIGINAL; j++) {
				int length = size/ORIGINAL;
				int positionX = length * j;
				int positionY = length * i;

				if ((j + i) % 2 == 0)
					batch.draw(square_black, positionX, positionY, length, length);
				else
					batch.draw(square_white, positionX, positionY, length, length);

				Piece currPiece = myBoardController.getPiece(new Position(j, i));
				if(currPiece != null) {
					if(!clicked || (pxToBoard.X != j || pxToBoard.Y != i || currPiece.getColor() != myBoardController.getColor())) {
						int height = (int)(length * RESIZED);
						int width = currPiece.getTexture().getWidth() * height / currPiece.getTexture().getHeight();
						positionX += (length - width) / 2;
						positionY += (length - height) / 2;
						batch.draw(currPiece.getTexture(), positionX, positionY, width, height);
					}
				}
			}
		}
	}

	private void createBoardTable(){
		boardTable = new Table();
		boardTable.setWidth(stage.getWidth());
		boardTable.align(Align.center | Align.top);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		boardTable.setPosition(0, screenSize.height);

		Image image = new Image();
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

	private Texture resizeTexture(String file, int size){
		Pixmap pixmapOriginal = new Pixmap(Gdx.files.internal(file));
		Pixmap pixmapResized = new Pixmap(pixmapOriginal.getWidth() * size / pixmapOriginal.getHeight(), size, pixmapOriginal.getFormat());
		pixmapResized.drawPixmap(pixmapOriginal,
				0, 0, pixmapOriginal.getWidth(), pixmapOriginal.getHeight(),
				0, 0, pixmapResized.getWidth(), pixmapResized.getHeight()
		);
		Texture texture = new Texture(pixmapResized);
		pixmapOriginal.dispose();
		pixmapResized.dispose();

		return texture;
	}

	private Position boardToPX(Position position){
		int x = (int)(position.X * (Gdx.graphics.getWidth() / 8f) + Gdx.graphics.getWidth() / 8f / 2);
		int i = (int)(position.Y * (Gdx.graphics.getWidth() / 8f) + Gdx.graphics.getWidth() / 8f / 2);

		return new Position(x, i);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	// Screen implemented methods
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
	}
}
