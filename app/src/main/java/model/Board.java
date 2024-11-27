package model;

import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.Promotable;
import util.Pos;
import util.Sfen;

public class Board {
    private Piece[][] grid;

    public Board(int width, int height){
        grid = new Piece[width][height];
    }
    public void move(Pos from, Pos to){ //Is run throuhg Game to alse change the turn
        Piece piece = grid[from.row()][from.col()];
        grid[to.row()][to.col()] = piece;
        grid[from.row()][from.col()] = null;
    }
    public Piece[][] getCurrentBoard(){
        return grid;
    }
    public void setAtPosition(Pos pos, Piece piece){
        grid[pos.row()][pos.col()] = piece;
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
}
