package model.pieces;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.Board;
import util.Side;
import util.Pos;

import java.util.ArrayList;

/**
 * Abstract class representing a piece
 */
@JsonDeserialize(using = PieceDeserializer.class)
public abstract class Piece {
    protected Side side;

    public Piece(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    @JsonValue
    public String getSfenAbbreviation(){
        char letter = this.getClass().getSimpleName().charAt(0);
        return switch(side){
            case SENTE -> String.valueOf(letter);
            case GOTE -> String.valueOf(letter).toLowerCase();
        };
    }

    public abstract ArrayList<Pos> getAvailableMoves(Pos pos, Board board);

    public abstract ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board);

    public boolean checkLegalMoveWithinBounds (Pos pos, Board board){
        return pos.col() >= 0 && pos.col() <= (board.getWidth() - 1) && pos.row() >= 0 && pos.row() <= (board.getHeight() - 1);
    }

    public boolean checkLegalMoveNotCapturingOwnPiece (Pos pos, Board board){
        if (board.getPieceAt(pos) != null) {
            return board.getPieceAt(pos).getSide() != side;
        }
        return true;
    }

    public Pos checkLegalMove (Pos pos, Board board){
        boolean valid = true;
        if (pos.col() >= 0 && pos.col() <= (board.getWidth()-1) && pos.row() >= 0 && pos.row() <= (board.getHeight()-1)) {
            if (board.getPieceAt(pos) != null) {
                if (board.getPieceAt(pos).getSide() == side) {
                    valid = false;
                }
            }
            if (valid) {
                return pos;
            }
        }
        return null;
    }

}
