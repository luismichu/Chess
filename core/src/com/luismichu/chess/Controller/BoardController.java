package com.luismichu.chess.Controller;

import com.badlogic.gdx.InputProcessor;

public class BoardController implements InputProcessor {
    private boolean screenClicked;
    private int[] screenClickedPos;


    // Constructor

    public BoardController(){
        screenClicked = false;
        screenClickedPos = new int[2];
    }


    // Getter and Setter

    public boolean isScreenClicked() {
        return screenClicked;
    }

    public void setScreenClicked(boolean screenClicked) {
        this.screenClicked = screenClicked;
    }

    public int[] getScreenClickedPos() {
        return screenClickedPos;
    }

    public void setScreenClickedPos(int[] screenClickedPos) {
        this.screenClickedPos = screenClickedPos;
    }


    // InputProcessor implemented methods

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
        screenClicked = true;

        screenClickedPos[0] = screenX;
        screenClickedPos[1] = screenY;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenClicked = false;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenClickedPos[0] = screenX;
        screenClickedPos[1] = screenY;

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
