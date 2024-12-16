package model;

import model.pieces.Piece;
import model.variants.Variant;
import util.Pos;
import util.Side;

import javax.xml.validation.Validator;
import java.util.ArrayList;

public class Board {
    private Piece[][] grid;

    public Board(int width, int height){
        grid = new Piece[height][width];
    }

    public Move move(Pos from, Pos to){ // Is run through Game to also change the turn
        Move move = new Move(from,to,getPieceAt(from),getPieceAt(to));
        setAtPosition(to,grid[from.row()][from.col()]);
        setAtPosition(from,null);
        return move;
    }

    public void setAtPosition(Pos pos, Piece piece){
        grid[pos.row()][pos.col()] = piece;
    }

    public void setSfen(Sfen sfen){
        grid = new Piece[grid.length][grid[0].length];
        initializeBoard(sfen);
    }

    public String getBoardAsSfen() {
        StringBuilder sfen = new StringBuilder();

        for (Piece[] row : grid) {
            int emptyCount = 0;
            for (Piece piece : row) {
                if (piece != null) {
                    if (emptyCount > 0) {
                        sfen.append(emptyCount);
                        emptyCount = 0;
                    }
                    sfen.append(piece.getSfenAbbreviation());
                } else {
                    emptyCount++;
                }
            }

            if (emptyCount > 0) {
                sfen.append(emptyCount);
            }

            sfen.append("/");
        }

        if (!sfen.isEmpty()) {
            sfen.setLength(sfen.length() - 1);
        }
        return sfen.toString();
    }

    public void initializeBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece = PieceFactory.fromSfenAbbreviation(abbr);
            setAtPosition(pos, piece);
        });
    }

    public Piece getPieceAt(Pos pos) {
        return grid[pos.row()][pos.col()];
    }

    public ArrayList<Piece> getEveryPiece(Variant variant){
        Piece piece;
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < variant.getWidth(); i++){
            for (int j = 0; j < variant.getHeight(); j++){
                piece = getPieceAt(new Pos(i,j));
                if (piece != null){
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public ArrayList<Pos> getEveryPiecePos(Variant variant){
        Piece piece;
        ArrayList<Pos> positions = new ArrayList<>();
        for (int i = 0; i < variant.getWidth(); i++){
            for (int j = 0; j < variant.getHeight(); j++){
                piece = getPieceAt(new Pos(i,j));
                if (piece != null){
                    positions.add(new Pos(i,j));
                }
            }
        }
        return positions;
    }

    public Pos getPiecePos(Variant variant, Side side, Class pieceType){
        Piece piece;
        for (int i = 0; i < variant.getWidth(); i++){
            for (int j = 0; j < variant.getHeight(); j++){
                piece = getPieceAt(new Pos(i,j));
                if (piece != null) {
                    if (piece.getClass() == pieceType && piece.getSide() == side) {
                        return new Pos(i, j);
                    }
                }
            }
        }
        return null;
    }
}
