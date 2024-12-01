package model;

import model.pieces.Piece;
import util.Sfen;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import model.pieces.*;
import util.Side;
import java.lang.reflect.Constructor;

public class Player {
    private Side side;
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
                 char abbr = piece.getSfenAbbreviation();
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
        capturedPieces.put(pieceClass, capturedPieces.get(pieceClass) + amount);
    }

    public void addCapturedPiece(Class<? extends Piece> pieceClass){
        addCapturedPiece(pieceClass,1);
    }

    public void removeCapturedPiece(Class<? extends Piece> pieceClass){
        //This is used to undo moves
        //addCapturedPiece(pieceClass,-1);
    }

    public void intializeHand(Class<? extends Piece>[] hand) {
        for (Class<? extends Piece> pieceClass : hand) {
            capturedPieces.put(pieceClass, 0);
        }
    };
}
