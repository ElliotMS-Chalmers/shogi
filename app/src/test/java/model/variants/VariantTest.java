package model.variants;

import model.Board;
import model.Player;
import model.settings.PieceSetType;
import model.pieces.Piece;
import model.Sfen;
import org.junit.jupiter.api.Test;
import util.Pos;
import util.Side;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VariantTest {

    // Minimal concrete subclass for testing
    static class TestVariant extends Variant {
        public TestVariant() {
            this.width = 9;
            this.height = 9;
            this.startSfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
            this.ruleSet = new RuleSet() {
                @Override
                public boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide) {
                    return false;
                }

                @Override
                public boolean isCurrentlyInCheck(Board board, Pos kingPos, Side side) {
                    return false;
                }

                @Override
                public boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side side, Side oppositeSide, Player player) {
                    return false;
                }

                @Override
                public boolean validHandMove(Pos pos, Class pieceClass, Board board, Side side) {
                    return false;
                }
            };
        }

        @Override
        public PieceSetType getPieceSetType() {
            return PieceSetType.STANDARD;
        }

        @Override
        public List<Class<? extends Piece>> getHand() {
            return Collections.emptyList();
        }

        @Override
        public Map<Side, List<Integer>> getPromotionZones() {
            Map<Side, List<Integer>> zones = new HashMap<>();
            zones.put(Side.SENTE, Arrays.asList(0, 1, 2));
            zones.put(Side.GOTE, Arrays.asList(6, 7, 8));
            return zones;
        }
    }

    @Test
    void testGetWidthAndHeight() {
        Variant variant = new TestVariant();
        assertEquals(9, variant.getWidth());
        assertEquals(9, variant.getHeight());
    }

    @Test
    void testGetStartSfen() {
        Variant variant = new TestVariant();
        Sfen startSfen = variant.getStartSfen();
        assertNotNull(startSfen);
        assertEquals("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1", startSfen.toString());
    }

    @Test
    void testGetRuleSet() {
        Variant variant = new TestVariant();
        assertNotNull(variant.getRuleSet());
    }

    @Test
    void testGetPieceSetType() {
        Variant variant = new TestVariant();
        assertEquals(PieceSetType.STANDARD, variant.getPieceSetType());
    }

    @Test
    void testGetHand() {
        Variant variant = new TestVariant();
        assertTrue(variant.getHand().isEmpty());
    }

    @Test
    void testGetPromotionZones() {
        Variant variant = new TestVariant();
        Map<Side, List<Integer>> promotionZones = variant.getPromotionZones();

        assertNotNull(promotionZones);
        assertEquals(Arrays.asList(0, 1, 2), promotionZones.get(Side.SENTE));
        assertEquals(Arrays.asList(6, 7, 8), promotionZones.get(Side.GOTE));
    }

    @Test
    void testInPromotionZone() {
        Variant variant = new TestVariant();

        // Test SENTE side promotion zone
        assertTrue(variant.inPromotionZone(new Pos(1, 4), Side.SENTE));
        assertFalse(variant.inPromotionZone(new Pos(4, 4), Side.SENTE));

        // Test GOTE side promotion zone
        assertTrue(variant.inPromotionZone(new Pos(6, 4), Side.GOTE));
        assertFalse(variant.inPromotionZone(new Pos(4, 4), Side.GOTE));
    }

    @Test
    void testSerialize() {
        Variant variant = new TestVariant();
        assertEquals("TestVariant", variant.serialize());
    }

    @Test
    void testInPromotionZoneWithNullZoneRows() {
        // Create a test variant where promotion zones for one side are null
        Variant variant = new TestVariant() {
            @Override
            public Map<Side, List<Integer>> getPromotionZones() {
                Map<Side, List<Integer>> zones = new HashMap<>();
                zones.put(Side.SENTE, Arrays.asList(0, 1, 2));
                // GOTE has no promotion zone (null)
                zones.put(Side.GOTE, null);
                return zones;
            }
        };

        // Test that inPromotionZone returns false for gote because no zone is defined
        assertFalse(variant.inPromotionZone(new Pos(6, 4), Side.GOTE));

        // Test for sente, where the zone is still defined and should return true for a position within the zone
        assertTrue(variant.inPromotionZone(new Pos(1, 4), Side.SENTE));
    }

}
