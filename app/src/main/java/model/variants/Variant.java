package model.variants;

import model.Board;
import util.Sfen;

public abstract class Variant {
    protected int width;
    protected int height;
    protected Sfen startSfen;

    //Sets all pieces at their starting position
    // public abstract void initiatePieces(Board board);
//    protected int reverseX(int x, boolean side){ /*Helper functions to easily initiate the same pieces on opposite sides,
//                                               avoids making lots of if-statements or repeating code in initiatePieces*/
//        if(side){return width-1-x;}
//        return x;
//    }
//    protected int reverseY(int y, boolean side){
//        if(side){return height-1-y;}
//        return y;
//    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Sfen getStartSfen() { return startSfen; }
}
