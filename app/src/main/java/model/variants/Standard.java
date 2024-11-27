package model.variants;

import util.Sfen;

public class Standard extends Variant {
    private final Sfen startSfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");

    public Standard(){
        width = 9;
        height = 9;
    }

    public Sfen getStartSfen() {
        return startSfen;
    }
}
