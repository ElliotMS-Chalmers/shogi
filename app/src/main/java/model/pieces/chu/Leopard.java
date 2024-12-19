package model.pieces.chu;

import model.Board;
import model.pieces.Promotable;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Leopard extends Promotable {
    private final int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,-1}, {0,-1}, {1,-1}};
    private final int[][] promotedMoves = {}; //bishop

    public Leopard(Side side) {super(side);}

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board, Variant variant) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        for (int[] ints : moves) {
            int availableCol = pos.col() + ints[0];
            int availableRow = pos.row() + ints[1];
            if (checkLegalMove(new Pos(availableRow,availableCol), board, variant) != null) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }
}
