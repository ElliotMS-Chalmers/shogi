package model.variants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import model.Sfen;
import model.pieces.Bishop;
import model.pieces.GoldGeneral;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.Rook;
import model.pieces.SilverGeneral;
import model.settings.PieceSetType;
import util.Side;

public class MiniTest {
    @Test
    public void testGetStartSfen() {
        Variant variant = new Mini();
        Sfen expectedSfen = new Sfen("rbsgk/4p/5/P4/KGSBR b - 1");
        assertEquals(expectedSfen.toString(), variant.getStartSfen().toString(), "The start SFEN should match the expected SFEN.");
    }

    @Test
    public void testGetHand() {
        Variant variant = new Mini();
        List<Class<? extends Piece>> expectedHand = List.of(
            Pawn.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
        );
        assertEquals(expectedHand, variant.getHand(), "The hand should match the expected list of pieces.");
    }

    @Test
    public void testGetPieceSetType() {
        Variant variant = new Mini();
        PieceSetType expectedPieceSetType = PieceSetType.STANDARD;
        assertEquals(expectedPieceSetType, variant.getPieceSetType(), "The piece set type should be STANDARD.");
    }

    @Test
    public void testGetPromotionZones() {
        Variant variant = new Mini();
        Map<Side, List<Integer>> expectedPromotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(4)),
            Map.entry(Side.GOTE, List.of(0))
    );
        assertEquals(expectedPromotionZones, variant.getPromotionZones(), "The promotion zones should match the expected values.");
    }
}
