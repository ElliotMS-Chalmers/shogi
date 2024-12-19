package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StandardTest {

    @Test
    public void testGetStartSfen() {
        Variant variant = new Standard();
        Sfen expectedSfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
        assertEquals(expectedSfen.toString(), variant.getStartSfen().toString(), "The start SFEN should match the expected SFEN.");
    }

    @Test
    public void testGetHand() {
        Variant variant = new Standard();
        List<Class<? extends Piece>> expectedHand = List.of(
                Pawn.class,
                Lance.class,
                Knight.class,
                SilverGeneral.class,
                GoldGeneral.class,
                Bishop.class,
                Rook.class
        );
        assertEquals(expectedHand, variant.getHand(), "The hand should match the expected list of pieces.");
    }

    @Test
    public void testGetPieceSetType() {
        Variant variant = new Standard();
        PieceSetType expectedPieceSetType = PieceSetType.STANDARD;
        assertEquals(expectedPieceSetType, variant.getPieceSetType(), "The piece set type should be STANDARD.");
    }

    @Test
    public void testGetPromotionZones() {
        Variant variant = new Standard();
        Map<Side, List<Integer>> expectedPromotionZones = Map.ofEntries(
                Map.entry(Side.SENTE, List.of(6, 7, 8)),
                Map.entry(Side.GOTE, List.of(0, 1, 2))
        );
        assertEquals(expectedPromotionZones, variant.getPromotionZones(), "The promotion zones should match the expected values.");
    }
}
