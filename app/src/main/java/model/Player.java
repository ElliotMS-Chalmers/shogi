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

public class Player {
    private boolean team;
    private List<Piece> capturedPieces = new ArrayList<>();
    // private Map<Class<? extends Piece>, Integer> capturedPieces = new LinkedHashMap<>() {};

    public Player(Piece[] pieces, Piece[] capturedPieces, boolean team){
        this.team = team;
        this.capturedPieces.addAll(List.of(capturedPieces));

        // capturedPieces.put(Pawn.class, 0);
        // capturedPieces.put(Lance.class, 0);
        // capturedPieces.put(Knight.class, 0);
        // capturedPieces.put(GoldGeneral.class, 0);
        // capturedPieces.put(SilverGeneral.class, 0);
        // capturedPieces.put(Rook.class, 0);
    }

    public String getHandAsSfen() {
        Map<Character, Integer> countMap = new HashMap<>();
        // capturedPieces.forEach((piece, count) -> {
        //    char abbr = piece.getSfenAbbreviation();
        //    countMap.put(abbr, countMap.getOrDefault(abbr, 0) + 1);
        // });
        for (Piece piece : capturedPieces) {
            char abbr = piece.getSfenAbbreviation();
            countMap.put(abbr, countMap.getOrDefault(abbr, 0) + 1);
        }

        StringBuilder sfen = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            int count = entry.getValue();
            char abbr = entry.getKey();

            if (count > 1) {
                sfen.append(count).append(abbr);
            } else {
                sfen.append(abbr);
            }
        }
        return sfen.toString();
    }

    public void addCapturedPiece(Class<? extends Piece> pieceClass, int amount){
        //capturedPieces.put(pieceClass,capturedPieces.get(pieceClass)+amount);
    }

    public void addCapturedPiece(Class<? extends Piece> pieceClass){
        //addCapturedPiece(pieceClass,1);
    }
    public void removeCapturedPiece(Class<? extends Piece> pieceClass){
        //This is used to undo moves
        //addCapturedPiece(pieceClass,-1);
    }
}
