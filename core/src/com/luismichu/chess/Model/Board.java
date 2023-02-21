package com.luismichu.chess.Model;

public class Board {
    private Piece[][] pieces;
    // This color will be in the lower part of the board
    private Piece.Color color;

    public Board(Piece.Color color){
        this.color = color;
        pieces = new Piece[8][8];
    }

    public void placeStandardPieces(){
        Piece.Color enemyColor = color == Piece.Color.WHITE? Piece.Color.BLACK : Piece.Color.WHITE;
        for (int i = 0; i < 8; i++) {
            pieces[i][1] = new Piece(color, Piece.Type.PAWN);
            pieces[i][6] = new Piece(enemyColor, Piece.Type.PAWN);
        }

        // Rook
        pieces[0][0] = new Piece(color, Piece.Type.ROOK);
        pieces[7][0] = new Piece(color, Piece.Type.ROOK);
        pieces[0][7] = new Piece(enemyColor, Piece.Type.ROOK);
        pieces[7][7] = new Piece(enemyColor, Piece.Type.ROOK);

        // Knight
        pieces[1][0] = new Piece(color, Piece.Type.KNIGHT);
        pieces[6][0] = new Piece(color, Piece.Type.KNIGHT);
        pieces[1][7] = new Piece(enemyColor, Piece.Type.KNIGHT);
        pieces[6][7] = new Piece(enemyColor, Piece.Type.KNIGHT);

        // Bishop
        pieces[2][0] = new Piece(color, Piece.Type.BISHOP);
        pieces[5][0] = new Piece(color, Piece.Type.BISHOP);
        pieces[2][7] = new Piece(enemyColor, Piece.Type.BISHOP);
        pieces[5][7] = new Piece(enemyColor, Piece.Type.BISHOP);

        // Knight and Queen
        if(color == Piece.Color.WHITE) {
            pieces[3][0] = new Piece(color, Piece.Type.QUEEN);
            pieces[4][0] = new Piece(color, Piece.Type.KING);
            pieces[3][7] = new Piece(enemyColor, Piece.Type.QUEEN);
            pieces[4][7] = new Piece(enemyColor, Piece.Type.KING);
        }
        else {
            pieces[3][0] = new Piece(color, Piece.Type.KING);
            pieces[4][0] = new Piece(color, Piece.Type.QUEEN);
            pieces[3][7] = new Piece(enemyColor, Piece.Type.KING);
            pieces[4][7] = new Piece(enemyColor, Piece.Type.QUEEN);
        }
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPiece(Piece piece, Position position){
        pieces[position.X][position.Y] = piece;
    }

    public Piece delPiece(Position position){
        Piece piece = pieces[position.X][position.Y];
        pieces[position.X][position.Y] = null;

        return piece;
    }

    public Piece.Color getColor() {
        return color;
    }
}
