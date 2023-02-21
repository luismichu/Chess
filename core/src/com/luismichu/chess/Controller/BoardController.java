package com.luismichu.chess.Controller;

import com.badlogic.gdx.InputProcessor;
import com.luismichu.chess.Model.Board;
import com.luismichu.chess.Model.Piece;
import com.luismichu.chess.Model.Position;

import java.util.ArrayList;
import java.util.List;

public class BoardController implements InputProcessor {
    private boolean screenClicked;
    private int[] screenClickedPos;
    private Board board;


    // Constructor TODO add color to board
    public BoardController(){
        screenClicked = false;
        screenClickedPos = new int[2];

        board = new Board(Piece.Color.WHITE);
        board.placeStandardPieces();

        //board.setPiece(new Piece(Piece.Color.WHITE, Piece.Type.QUEEN), new Position(3, 3));
    }


    // Getter and Setter
    public Piece getPiece(Position position){
        try {
            return board.getPieces()[position.X][position.Y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public List<Position> allowedMoves(Position position){
        List<Position> result = new ArrayList<>();
        List<Position> aux = new ArrayList<>();
        Piece currPiece = getPiece(position);
        if(currPiece != null) {
            switch (currPiece.getType()) {
                case PAWN:
                    aux.add(new Position(position.X, position.Y + 1));
                    break;

                case ROOK:
                    for (int i = 0; i < 8; i++) {
                        aux.add(new Position(position.X, i));
                        aux.add(new Position(i, position.Y));
                    }
                    break;

                case KNIGHT:
                    aux.add(new Position(position.X + 1, position.Y + 2));
                    aux.add(new Position(position.X + 2, position.Y + 1));
                    aux.add(new Position(position.X + 2, position.Y - 1));
                    aux.add(new Position(position.X + 1, position.Y - 2));
                    aux.add(new Position(position.X - 1, position.Y - 2));
                    aux.add(new Position(position.X - 2, position.Y - 1));
                    aux.add(new Position(position.X - 2, position.Y + 1));
                    aux.add(new Position(position.X - 1, position.Y + 2));
                    break;

                case BISHOP:
                    for (int i = 1; i < 8; i++) {
                        aux.add(new Position(position.X + i, position.Y + i));
                        aux.add(new Position(position.X + i, position.Y - i));
                        aux.add(new Position(position.X - i, position.Y + i));
                        aux.add(new Position(position.X - i, position.Y - i));
                    }
                    break;

                case KING:
                    aux.add(new Position(position.X + 1, position.Y));
                    aux.add(new Position(position.X + 1, position.Y + 1));
                    aux.add(new Position(position.X + 1, position.Y - 1));
                    aux.add(new Position(position.X, position.Y + 1));
                    aux.add(new Position(position.X - 1, position.Y + 1));
                    aux.add(new Position(position.X - 1, position.Y));
                    aux.add(new Position(position.X - 1, position.Y - 1));
                    aux.add(new Position(position.X, position.Y - 1));
                    break;

                case QUEEN:
                    for (int i = 0; i < 8; i++) {
                        aux.add(new Position(position.X, i));
                        aux.add(new Position(i, position.Y));
                        aux.add(new Position(position.X + i, position.Y + i));
                        aux.add(new Position(position.X + i, position.Y - i));
                        aux.add(new Position(position.X - i, position.Y + i));
                        aux.add(new Position(position.X - i, position.Y - i));
                    }
                    break;
            }
        }

        // Add positions that are not occupied
        for(Position pos : aux){
            if(pos.X >= 0 &&
                pos.Y >= 0 &&
                getPiece(pos) == null)
                result.add(pos);
        }

        return result;
    }

    public void move(Position initPos, Position newPos) {
        Piece toMove = board.delPiece(initPos);
        board.setPiece(toMove, newPos);
    }

    public Piece.Color getColor(){
        return board.getColor();
    }

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
