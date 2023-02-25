package com.luismichu.chess.Controller;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class MyAssetManager {
    private AssetManager manager;
    private Array<MyAssetDescriptor> queue;

    public MyAssetManager(){
        manager = new AssetManager();
        queue = new Array<>();
    }

    public Texture loadPiece(Assets.Shadow shadow, Assets.Px px, Assets.Color color, Assets.Type type){
        return loadPiece(shadow, px,color, type, true);
    }

    public Texture loadPiece(Assets.Shadow shadow, Assets.Px px, Assets.Color color, Assets.Type type, boolean finishLoading){
        String piece = Assets.BASE_PATH + shadow + px + color + type + Assets.EXT;
        MyAssetDescriptor pieceAssetDescriptor = new MyAssetDescriptor(piece, Texture.class);
        manager.load(pieceAssetDescriptor);

        if(finishLoading)
            manager.finishLoading();

        return getTexture(pieceAssetDescriptor);
    }

    public Array<Texture> loadPieces(Assets.Shadow shadow, Assets.Px px){
        Array<Texture> assetTextures = new Array<>();
        for(Assets.Type type : Assets.Type.getTypes()){
            assetTextures.add(loadPiece(shadow, px, Assets.Color.WHITE, type, false));
            assetTextures.add(loadPiece(shadow, px, Assets.Color.BLACK, type, false));
        }

        manager.finishLoading();

        return assetTextures;
    }

    public Texture loadSquare(Assets.Shadow shadow, Assets.Px px, Assets.SquareColor squareColor, Assets.SquareBright squareBright){
        String square = Assets.BASE_PATH + shadow + px + squareColor + squareBright + Assets.EXT;
        MyAssetDescriptor squareAssetDescriptor = new MyAssetDescriptor(square, Texture.class);
        manager.load(squareAssetDescriptor);

        manager.finishLoading();

        return getTexture(squareAssetDescriptor);
    }

    public void addToQueue(MyAssetDescriptor assetDescriptor){
        queue.add(assetDescriptor);
    }

    public void addToQueue(Array<MyAssetDescriptor> assetDescriptors){
        for(MyAssetDescriptor assetDescriptor : assetDescriptors)
            queue.add(assetDescriptor);
    }

    public void loadQueue(){
        for(MyAssetDescriptor assetDescriptor : queue)
            manager.load(assetDescriptor);

        manager.finishLoading();
        queue.clear();
    }

    public float getProgress(){
        return manager.getProgress() * 100;
    }

    public boolean update(){
        return manager.update();
    }

    public Object get(MyAssetDescriptor t){ return manager.get(t); }

    public Texture getTexture(MyAssetDescriptor t){
        return (Texture) manager.get(t);
    }

    public Array<Texture> getTextures(Array<MyAssetDescriptor> arrayAssetDescriptorTexture){
        Array<Texture> arrayTexture = new Array<>();

        for(MyAssetDescriptor t : arrayAssetDescriptorTexture)
            arrayTexture.add((Texture) manager.get(t));
        return arrayTexture;
    }

    public Music getMusic(MyAssetDescriptor assetDescriptorMusic){
        return (Music) manager.get(assetDescriptorMusic);
    }

    public Array<Sound> getSounds(Array<MyAssetDescriptor> arrayAssetDescriptorSound){
        Array<Sound> arraySound = new Array<>();

        for(MyAssetDescriptor t : arrayAssetDescriptorSound)
            arraySound.add((Sound) manager.get(t));
        return arraySound;
    }

    public Sound getSound(MyAssetDescriptor assetDescriptorSound){
        return (Sound) manager.get(assetDescriptorSound);
    }

    public Skin getSkin(MyAssetDescriptor skin){
        return (Skin) manager.get(skin);
    }

    public static class MyAssetDescriptor extends AssetDescriptor{
        public MyAssetDescriptor(String fileName, Class c) {
            super(fileName, c);
        }
    }

    public static class Assets {
        public static final String BASE_PATH = "images/";
        public static final String EXT = ".png";

        public static class Shadow {
            public static final Shadow NO_SHADOW = new Shadow("no_shadow/");
            public static final Shadow SHADOW = new Shadow("shadow/");
            private final String shadow;

            private Shadow(String shadow) {
                this.shadow = shadow;
            }
            public String toString() {
                return shadow;
            }
        }

        public static class Px {
            public static final Px PX128 = new Px("128/");
            public static final Px PX256 = new Px("256/");
            public static final Px PX512 = new Px("512/");
            public static final Px PX1024 = new Px("1024/");
            private final String px;
            private Px(String px){this.px = px;}
            public String toString(){return px;}
        }

        public static class Color {
            public static final Color BLACK = new Color("b_");
            public static final Color WHITE = new Color("w_");
            private final String color;
            private Color(String color){this.color = color;}
            public String toString(){return color;}
        }

        public static class Type {
            public static final Type PAWN = new Type("pawn");
            public static final Type ROOK = new Type("rook");
            public static final Type KNIGHT = new Type("knight");
            public static final Type BISHOP = new Type("bishop");
            public static final Type KING = new Type("king");
            public static final Type QUEEN = new Type("queen");
            private final String type;

            public Type(String type){this.type = type;}
            public String toString(){return type;}

            public static Array<Type> getTypes(){
                return new Array<>(new Type[]{Type.PAWN, Type.ROOK, Type.KNIGHT, Type.BISHOP, Type.KING, Type.QUEEN});
            }
        }

        public static class SquareColor {
            public static final SquareColor BROWN_SQUARE = new SquareColor("square_brown_");
            public static final SquareColor GRAY_SQUARE = new SquareColor("square_gray_");
            private final String squareColor;

            private SquareColor(String squareColor){ this.squareColor = squareColor;}
            public String toString(){return squareColor;}
        }

        public static class SquareBright {
            public static final SquareBright BLACK_SQUARE = new SquareBright("dark");
            public static final SquareBright WHITE_SQUARE = new SquareBright("light");
            private final String squareColor;
            private SquareBright(String squareColor){this.squareColor = squareColor;}
            public String toString(){return squareColor;}
        }

        private Assets(){}
    }
}
