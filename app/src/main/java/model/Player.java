package model;

import model.pieces.Piece;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import util.Side;
import java.lang.reflect.Constructor;

public class Player {
    private Side side;
    private int timeLeft;
    // private Clock clock;
    private Map<Class<? extends Piece>, Integer> capturedPieces = new LinkedHashMap<>() {};

    public Player(Side side){
        this.side = side;
    }

    public String getHandAsSfen() {
        StringBuilder sfen = new StringBuilder();
         capturedPieces.forEach((pieceClass, count) -> {
             try {
                 Constructor<?> constructor = pieceClass.getConstructor(Side.class);
                 Piece piece = (Piece) constructor.newInstance(side);
                 String abbr = piece.getSfenAbbreviation();
                 if (count > 1) {
                     sfen.append(count).append(abbr);
                 } else if (count == 1) {
                     sfen.append(abbr);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         });
        return sfen.toString();
    }

    public void addCapturedPiece(Class<? extends Piece> pieceClass, int amount){
        if (capturedPieces.get(pieceClass) != null)
            capturedPieces.put(pieceClass, capturedPieces.get(pieceClass) + amount);
    }

    public void addCapturedPiece(Class<? extends Piece> pieceClass){
        addCapturedPiece(pieceClass,1);
    }

    public void removeCapturedPiece(Class<? extends Piece> pieceClass){
        addCapturedPiece(pieceClass,-1);
    }

    public void intializeHand(List<Class<? extends Piece>> hand) {
        for (Class<? extends Piece> pieceClass : hand) {
            capturedPieces.put(pieceClass, 0);
        }
    };

    public void setTimeLeft(int time){
        this.timeLeft = time;
    }

    public int getTimeLeft(){
        return timeLeft;
    }
}
