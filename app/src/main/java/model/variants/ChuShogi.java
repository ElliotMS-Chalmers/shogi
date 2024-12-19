package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;

import java.util.List;
import java.util.Map;

public class ChuShogi extends Variant {

    private final Sfen startSfen = new Sfen("lfcsgekgscfl/a1b1txot1b1a/mvrhdqndhrvm/pppppppppppp/3i4i3/12/12/3I4I3/PPPPPPPPPPPP/MVRHDNQDHRVM/A1B1TOXT1B1A/LFCSGKEGSCFL b - 1");

    private final List<Class<? extends Piece>> hand = List.of(
            /*
            Pawn.class,
            Lance.class,
            Knight.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class

            Lion.class,
            Queen.class,
            CopperGeneral.class,
            Elephant.class,
            Leopard.class,
            Tiger.class,
            Kirin.class,
            Phoenix.class,
            GoBetween.class,
            SideMover.class,
            VerticalMover.class,
            Dragon.class,
            Horse.class,
            ReverseChariot.class,
            //Promoted pieces
            Prince.class,
            Stag.class,
            Boar.class,
            Ox.class,
            Eagle.class,
            Falcon.class,
            WhiteHorse.class,
            Whale.class
             */
    );
    protected Map<Side, List<Integer>> promotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(8, 9, 10, 11)),
            Map.entry(Side.GOTE, List.of(0, 1, 2, 3))
    );

    public ChuShogi(){
        width = 12;
        height = 12;
        ruleSet = new ShogiRuleSet();
    }

    public Sfen getStartSfen() {
        return startSfen;
    }

    public List<Class<? extends Piece>> getHand() {
        return List.of();
    }

    @Override
    public PieceSetType getPieceSetType() {
        return PieceSetType.CHU;
    }

    public Map<Side, List<Integer>> getPromotionZones() {
        return promotionZones;
    }
}
