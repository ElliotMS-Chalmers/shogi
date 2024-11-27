package model;

import model.pieces.Piece;
import util.Sfen;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Player {
    private boolean team;
    private List<Piece> capturedPieces = new ArrayList<>();

    public Player(Piece[] pieces, Piece[] capturedPieces, boolean team){
        this.team = team;
        this.capturedPieces.addAll(List.of(capturedPieces));
    }

    public String getHandAsSfen() {
        Map<Character, Integer> countMap = new HashMap<>();
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
}
