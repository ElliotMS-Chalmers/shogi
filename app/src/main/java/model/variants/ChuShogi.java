package model.variants;

import model.PieceSetType;
import model.pieces.*;
import model.Sfen;

import java.util.List;

public class ChuShogi extends Variant {

    private final Sfen startSfen = new Sfen("l(leo)(copper)sg(elephant)kgs(copper)(leo)l/" +
            "(revChariot)1b1(tiger)(phoenix)(kirin)(tiger)1b1(revChariot)/" +
            "(side)(vert)r(horse)(dragon)(queen)(lion)(dragon)(horse)r(vert)(side)/" +
            "pppppppppppp/3(goBet)4(goBet)3/12/" +
            "12/3(GOBET)4(GOBET)3/PPPPPPPPPPPP/" +
            "(SIDE)(VERT)R(HORSE)(DRAGON)(LION)(QUEEN)(DRAGON)(HORSE)R(VERT)(SIDE)/" +
            "(REVCHARIOT)1B1(TIGER)(KIRIN)(PHOENIX)(TIGER)1B1(REVCHARIOT)/" +
            "L(LEO)(COPPER)SGK(ELEPHANT)GS(COPPER)(LEO)L");
    private final List<Class<? extends Piece>> hand = List.of(
            Pawn.class,
            Lance.class,
            Knight.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
            /*
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

    public ChuShogi(){
        width = 12;
        height = 12;
        ruleSet = new ShogiRuleSet();
    }

    public Sfen getStartSfen() {
        return startSfen;
    }

    public List<Class<? extends Piece>> getHand() {
        return hand;
    }

    @Override
    public PieceSetType getPieceSetType() {
        return PieceSetType.CHU;
    }
}
