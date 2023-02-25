package com.luismichu.chess.Model;

import com.badlogic.gdx.graphics.Texture;

public class Piece {
    public enum Color{ BLACK, WHITE }
    public enum Type{ PAWN, BISHOP, KNIGHT, ROOK, QUEEN, KING }

    private final Color color;
    private Type type;
    private Texture texture;

    public Piece(Color color, Type type){
        this.color = color;
        this.type = type;
    }

    public void promote(Type type) {
        if(this.type == Type.PAWN)
            this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
