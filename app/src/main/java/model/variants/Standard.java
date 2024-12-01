package model.variants;

import model.pieces.*;
import util.Sfen;
import util.Side;

import java.util.Arrays;
import java.util.Collections;

public class Standard extends Variant {
    private final Sfen startSfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
    private final Class[] hand = new Class[]{ Pawn.class, Lance.class, Knight.class, SilverGeneral.class, GoldGeneral.class, Bishop.class, Rook.class };

    public Standard(){
        width = 9;
        height = 9;
    }

    public Sfen getStartSfen() {
        return startSfen;
    }

    public Class<? extends Piece>[] getHand(Side side) {
        Class<? extends Piece>[] reversedHand = hand.clone();
        Collections.reverse(Arrays.asList(hand.clone()));
        return switch (side) {
            case GOTE -> hand;
            case SENTE -> reversedHand;
        };
    }
}
