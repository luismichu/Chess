package com.luismichu.chess.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.chess.Controller.BoardController;
import com.luismichu.chess.Controller.MyAssetManager;
import com.luismichu.chess.Model.Piece;
import com.luismichu.chess.Model.Position;

import java.awt.*;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class BoardScreen implements Screen {
	private SpriteBatch batch;
	private Texture square_black, square_white;
	private Stage stage;
	private Table boardTable;
	private BoardController myBoardController;
	private boolean clicked;
	private boolean released;
	private boolean clickedFlag;
	private Position pxToBoard;
	private ShapeRenderer shapeRenderer;
	private Array<Texture> pieces;
	private MyAssetManager myAssetManager;
	private OrthographicCamera camera;
	private FitViewport viewport;

	private static final int ORIGINAL = 8;
	private static final float RESIZED = 0.8f;
	private static final int WORLD_PX = 1000;

	@Override
	public void show () {
		myBoardController = new BoardController(Piece.Color.WHITE);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		myAssetManager = new MyAssetManager();

		int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		viewport = new FitViewport(WORLD_PX, WORLD_PX, camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

		stage = new Stage(new FitViewport(size, size));
		//Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(myBoardController);

		clicked = false;
		released = true;
		clickedFlag = false;
		pxToBoard = new Position();

		//createBoardTable();
		//board();

	}

	@Override
	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
		stage.act(delta);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		drawBoard();
		batch.end();

		if(clickedFlag) {
			for (Position allowedPosition : myBoardController.allowedMoves(pxToBoard)) {
				Piece currPiece = myBoardController.getPiece(allowedPosition);
				if(currPiece != null){
					int length = WORLD_PX / ORIGINAL;
					Position pos = boardToPX(allowedPosition);
					int positionX = pos.X - length / 2;
					int positionY = pos.Y  - length / 2;

					shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
					shapeRenderer.rect(positionX, positionY, length, length);
					shapeRenderer.end();

					Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
					Gdx.gl.glDepthFunc(GL20.GL_LESS);
					Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
					Gdx.gl.glColorMask(false, false, false, false);
					shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
					shapeRenderer.circle(pos.X, pos.Y, length / 1.75f);
					shapeRenderer.flush();
					shapeRenderer.end();

					Gdx.gl.glColorMask(true, true, true, true);
					Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

					batch.begin();
					drawSquareAndPiece(allowedPosition.X, allowedPosition.Y);
					batch.end();

					Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
				}
				else {
					shapeRenderer.setColor(Color.CYAN);
					shapeRenderer.setProjectionMatrix(camera.combined);
					shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
					Position pos = boardToPX(allowedPosition);
					shapeRenderer.circle(pos.X, pos.Y, 20);
					shapeRenderer.end();
				}

			}
		}

		if(myBoardController.isScreenClicked()){
			Vector3 v3 = viewport.unproject(new Vector3(myBoardController.getScreenClickedPos()[0], myBoardController.getScreenClickedPos()[1], 0));
			Position clickedPos = new Position(((int) v3.x), ((int) v3.y));
			if(released) {
				pxToBoard.X = clickedPos.X / (WORLD_PX / 8);
				pxToBoard.Y = clickedPos.Y / (WORLD_PX / 8);
				released = false;
			}
			Piece currPiece = myBoardController.getPiece(pxToBoard);
			if(currPiece != null && currPiece.getColor() == myBoardController.getCurrColor() /* 2 PLAYER/SINGLE PLAYER */ /* && currPiece.getColor() == myBoardController.getColor() */) {
				int length = WORLD_PX / ORIGINAL;
				int height = (int)(length * 0.9);
				int width = currPiece.getTexture().getWidth() * height / currPiece.getTexture().getHeight();
				batch.begin();
				batch.draw(currPiece.getTexture(), clickedPos.X - width / 2, clickedPos.Y - height / 2, width, height);
				batch.end();
				clicked = true;
				clickedFlag = true;
			}
			else
				clickedFlag = false;
		}
		else {
			released = true;

			if(clicked) {
				Position releasePos = new Position();
				Vector3 v3 = viewport.unproject(new Vector3(myBoardController.getScreenClickedPos()[0], myBoardController.getScreenClickedPos()[1], 0));
				releasePos.X = (int) (v3.x / (WORLD_PX / 8));
				releasePos.Y = (int) (v3.y / (WORLD_PX / 8));
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

	private void drawSquareAndPiece(int i, int j){
		int length = WORLD_PX / ORIGINAL;
		int positionX = length * i;
		int positionY = length * j;

		if ((i + j) % 2 == 0)
			batch.draw(square_black, positionX, positionY, length, length);
		else
			batch.draw(square_white, positionX, positionY, length, length);

		Piece currPiece = myBoardController.getPiece(new Position(i, j));
		if(currPiece != null) {
			if(!clicked || (pxToBoard.X != i || pxToBoard.Y != j /* 2 PLAYER/SINGLE PLAYER */ /* || currPiece.getColor() != myBoardController.getColor() */)) {
				int height = (int)(length * RESIZED);
				int width = currPiece.getTexture().getWidth() * height / currPiece.getTexture().getHeight();
				positionX += (length - width) / 2;
				positionY += (length - height) / 2;
				batch.draw(currPiece.getTexture(), positionX, positionY, width, height);
			}
		}
	}

	private void drawBoard(){
		for(int i = 0; i < ORIGINAL; i++){
			for (int j = 0; j < ORIGINAL; j++) {
				drawSquareAndPiece(j, i);
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
		int x = (int)(position.X * (WORLD_PX / 8f) + WORLD_PX / 8f / 2);
		int i = (int)(position.Y * (WORLD_PX / 8f) + WORLD_PX / 8f / 2);

		return new Position(x, i);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
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
