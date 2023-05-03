package com.luismichu.chess.Controller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.luismichu.chess.Model.Board;
import com.luismichu.chess.Model.Piece;
import com.luismichu.chess.Model.Position;

import java.util.List;

public class BoardController implements InputProcessor {
    private boolean screenClicked;
    private int[] screenClickedPos;
    private Board board;


    // Constructor TODO add color to board
    public BoardController(Piece.Color color){
        screenClicked = false;
        screenClickedPos = new int[2];

        board = new Board(color);
        board.placeStandardPieces();
        board.setPiece(new Piece(Piece.Color.WHITE, Piece.Type.QUEEN), new Position(3, 3));
        board.setPiece(new Piece(Piece.Color.BLACK, Piece.Type.BISHOP), new Position(4, 4));

        /*for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(new Piece(Piece.Color.BLACK, Piece.Type.PAWN), new Position(j, i));
            }
        }*/

    }

    // Getter and Setter
    public Piece getPiece(Position position){
        try {
            return board.getPieces()[position.X][position.Y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Piece[][] getPieces() {
        return board.getPieces();
    }

    public Array<Position> allowedMoves(Position position){
        Array<Position> result = new Array<>();
        Piece currPiece = getPiece(position);
        if(currPiece != null) {
            switch (currPiece.getType()) {
                case PAWN:
                    int sign = 1;
                    int doubleStepPos = 1;
                    if(currPiece.getColor() != board.getColor()) {
                        sign = -1;
                        doubleStepPos = 6;
                    }

                    result.add(new Position(position.X, position.Y + sign));
                    if(position.Y == doubleStepPos)
                        result.add(new Position(position.X, position.Y + 2 * sign));

                    cleanAllowedMoves(result);
                    checkPawn(result, position, currPiece);
                    break;

                case ROOK:
                    for (int i = 0; i < 8; i++) {
                        if(i != position.Y)
                            result.add(new Position(position.X, i));
                        if(i != position.X)
                            result.add(new Position(i, position.Y));
                    }

                    cleanAllowedMoves(result);
                    checkRook(result, position, currPiece);
                    break;

                case KNIGHT:
                    result.add(new Position(position.X + 1, position.Y + 2));
                    result.add(new Position(position.X + 2, position.Y + 1));
                    result.add(new Position(position.X + 2, position.Y - 1));
                    result.add(new Position(position.X + 1, position.Y - 2));
                    result.add(new Position(position.X - 1, position.Y - 2));
                    result.add(new Position(position.X - 2, position.Y - 1));
                    result.add(new Position(position.X - 2, position.Y + 1));
                    result.add(new Position(position.X - 1, position.Y + 2));

                    cleanAllowedMoves(result);
                    checkKnight(result, position, currPiece);
                    break;

                case BISHOP:
                    for (int i = 1; i < 8; i++) {
                        result.add(new Position(position.X + i, position.Y + i));
                        result.add(new Position(position.X + i, position.Y - i));
                        result.add(new Position(position.X - i, position.Y + i));
                        result.add(new Position(position.X - i, position.Y - i));
                    }

                    cleanAllowedMoves(result);
                    checkBishop(result, position, currPiece);
                    break;

                case KING:
                    result.add(new Position(position.X + 1, position.Y));
                    result.add(new Position(position.X + 1, position.Y + 1));
                    result.add(new Position(position.X + 1, position.Y - 1));
                    result.add(new Position(position.X, position.Y + 1));
                    result.add(new Position(position.X - 1, position.Y + 1));
                    result.add(new Position(position.X - 1, position.Y));
                    result.add(new Position(position.X - 1, position.Y - 1));
                    result.add(new Position(position.X, position.Y - 1));

                    cleanAllowedMoves(result);
                    checkKing(result, position, currPiece);
                    break;

                case QUEEN:
                    // Use an aux array for bishop and rook, calculate their moves
                    // separately then add them together in result array
                    Array<Position> resultBishop = new Array<>();
                    Array<Position> resultRook = new Array<>();
                    for (int i = 0; i < 8; i++) {
                        if(i != position.Y)
                            resultBishop.add(new Position(position.X, i));
                        if(i != position.X)
                            resultBishop.add(new Position(i, position.Y));

                        resultRook.add(new Position(position.X + i, position.Y + i));
                        resultRook.add(new Position(position.X + i, position.Y - i));
                        resultRook.add(new Position(position.X - i, position.Y + i));
                        resultRook.add(new Position(position.X - i, position.Y - i));
                    }

                    cleanAllowedMoves(resultBishop);
                    checkRook(resultBishop, position, currPiece);

                    cleanAllowedMoves(resultRook);
                    checkBishop(resultRook, position, currPiece);

                    result.addAll(resultBishop);
                    result.addAll(resultRook);
                    break;
            }
        }

        return result;
    }

    private void cleanAllowedMoves(Array<Position> result){
        // Remove positions out of bounds or occupied
        // I lost maybe an hour here just because I didn't work with a copy
        // of result, but instead I was deleting values inside the for. Dumbass...
        for(Position pos : new Array<>(result)){
            if(pos.X < 0 || pos.X > 7 ||
                pos.Y < 0 || pos.Y > 7) {
                result.removeValue(pos, false);
            }
        }
    }

    private void checkPawn(Array<Position> result, Position position, Piece currPiece) {
        Array<Position> positionsKill = new Array<>();
        int sign = 1;
        if(currPiece.getColor() != board.getColor())
            sign = -1;

        positionsKill.add(new Position(position.X + sign, position.Y + sign));
        positionsKill.add(new Position(position.X - sign, position.Y + sign));
        for(Position pos : new Array<>(result)){
            if(getPiece(pos) != null)
                result.removeValue(pos, false);
        }
        for(Position posKill : positionsKill){
            if(getPiece(posKill) != null){
                Piece pieceToKill = getPiece(posKill);
                if(pieceToKill.getColor() != currPiece.getColor())
                    result.add(posKill);
            }
        }
    }

    private void checkRook(Array<Position> result, Position position, Piece currPiece) {
        Position pTop = null, pRight = null, pBottom = null, pLeft = null;
        for(Position pos : new Array<>(result)){
            if(getPiece(pos) != null && !getPiece(pos).equals(currPiece)){
                if(pos.X == position.X){
                    if(position.Y < pos.Y) {
                        if(pTop == null || pTop.Y > pos.Y)
                            pTop = pos;
                        else
                            result.removeValue(pos, false);
                    }
                    else {
                        if(pBottom == null || pBottom.Y < pos.Y)
                            pBottom = pos;
                        else
                            result.removeValue(pos, false);
                    }
                }
                else{
                    if(position.X < pos.X) {
                        if(pRight == null || pRight.X > pos.X)
                            pRight = pos;
                        else
                            result.removeValue(pos, false);
                    }
                    else {
                        if(pLeft == null || pLeft.X < pos.X)
                            pLeft = pos;
                        else
                            result.removeValue(pos, false);
                    }
                }
            }
        }
        for(Position pos : new Array<>(result)){
            if(pTop != null && pos.X == pTop.X && pos.Y > pTop.Y)
                result.removeValue(pos, false);
            if(pBottom != null && pos.X == pBottom.X && pos.Y < pBottom.Y)
                result.removeValue(pos, false);
            if(pRight != null && pos.Y == pRight.Y && pos.X > pRight.X)
                result.removeValue(pos, false);
            if(pLeft != null && pos.Y == pLeft.Y && pos.X < pLeft.X)
                result.removeValue(pos, false);
            if(getPiece(pos)!= null && getPiece(pos).getColor() == currPiece.getColor())
                result.removeValue(pos, false);
        }
    }

    private void checkKnight(Array<Position> result, Position position, Piece currPiece) {
        for(Position pos : new Array<>(result)){
            if(getPiece(pos) != null && getPiece(pos).getColor() == currPiece.getColor())
                result.removeValue(pos, false);
        }
    }

    private void checkBishop(Array<Position> result, Position position, Piece currPiece) {
        Position pTopRight = null, pTopLeft = null, pBottomRight = null, pBottomLeft = null;
        for(Position pos : new Array<>(result)){
            if(getPiece(pos) != null && !getPiece(pos).equals(currPiece)){
                if(pos.X < position.X){
                    if(pos.Y < position.Y) {
                        if(pBottomLeft == null || pBottomLeft.Y < pos.Y)
                            pBottomLeft = pos;
                        else
                            result.removeValue(pos, false);
                    }
                    else {
                        if(pTopLeft == null || pTopLeft.Y > pos.Y)
                            pTopLeft = pos;
                        else
                            result.removeValue(pos, false);
                    }
                }
                else{
                    if(pos.Y < position.Y) {
                        if(pBottomRight == null || pBottomRight.Y < pos.Y)
                            pBottomRight = pos;
                        else
                            result.removeValue(pos, false);
                    }
                    else {
                        if(pTopRight == null || pTopRight.Y > pos.Y)
                            pTopRight = pos;
                        else
                            result.removeValue(pos, false);
                    }
                }
            }
        }
        for(Position pos : new Array<>(result)){
            if(pTopLeft != null && pos.X < pTopLeft.X && pos.Y > pTopLeft.Y)
                result.removeValue(pos, false);
            if(pBottomLeft != null && pos.X < pBottomLeft.X && pos.Y < pBottomLeft.Y)
                result.removeValue(pos, false);
            if(pTopRight != null && pos.Y > pTopRight.Y && pos.X > pTopRight.X)
                result.removeValue(pos, false);
            if(pBottomRight != null && pos.Y < pBottomRight.Y && pos.X > pBottomRight.X)
                result.removeValue(pos, false);
            if(getPiece(pos)!= null && getPiece(pos).getColor() == currPiece.getColor())
                result.removeValue(pos, false);
        }
    }

    private void checkKing(Array<Position> result, Position position, Piece currPiece) {
        for(Position pos : new Array<>(result)) {
            if (getPiece(pos) != null && getPiece(pos).getColor() == currPiece.getColor())
                result.removeValue(pos, false);
        }
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
