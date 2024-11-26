package model.variants;

import model.Board;
import pieces.*;

public class Standard extends variants.Variant {
    public Standard(){
        width = 9;
        height = 9;
    }
    public void initiatePieces(Board board){

        for(boolean side = false; side == false; side = true) { //Runs two times to set the same pieces on both sides

            for (int x = 0; x < 9; x++) {
                board.setAtPosition(reverseX(x,side), reverseY(2,side),new Pawn(side));
            }
            board.setAtPosition(reverseX(1,side), reverseY(1,side),new Bishop(side));

            board.setAtPosition(reverseX(7,side), reverseY(1,side),new Rook(side));

            board.setAtPosition(reverseX(0,side),reverseY(0,side),new Lance(side));
            board.setAtPosition(reverseX(8,side),reverseY(0,side),new Lance(side));

            board.setAtPosition(reverseX(1,side),reverseY(0,side),new Knight(side));
            board.setAtPosition(reverseX(7,side),reverseY(0,side),new Knight(side));

            board.setAtPosition(reverseX(2,side),reverseY(0,side),new SilverGeneral(side));
            board.setAtPosition(reverseX(6,side),reverseY(0,side),new SilverGeneral(side));

            board.setAtPosition(reverseX(3,side),reverseY(0,side),new GoldGeneral(side));
            board.setAtPosition(reverseX(5,side),reverseY(0,side),new GoldGeneral(side));

            board.setAtPosition(reverseX(4,side),reverseY(0,side),new King(side));
        }
    }

}
