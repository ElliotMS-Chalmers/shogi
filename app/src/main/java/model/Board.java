package model;

import model.pieces.Piece;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a game board for managing and interacting with pieces during a game.
 */
public class Board {
    /**
     * A 2D array representing the board grid where each cell may contain a piece or be empty.
     */
    private Piece[][] grid;

    /**
     * Constructs a board with the specified dimensions.
     * 
     * @param width  the width of the board
     * @param height the height of the board
     */
    public Board(int width, int height) {
        grid = new Piece[height][width];
    }

    /**
     * Moves a piece from one position to another.
     * Updates the board and returns a Move object representing the move.
     * 
     * @param from the starting position of the piece
     * @param to   the target position of the piece
     * @return a Move object representing the performed move
     */
    public Move move(Pos from, Pos to) {
        Move move = new Move(from, to, getPieceAt(from), getPieceAt(to));
        setAtPosition(to, grid[from.row()][from.col()]);
        setAtPosition(from, null);
        return move;
    }

    /**
     * Sets a piece at a specific position on the board.
     * 
     * @param pos   the position on the board
     * @param piece the piece to place at the specified position
     */
    public void setAtPosition(Pos pos, Piece piece) {
        grid[pos.row()][pos.col()] = piece;
    }

    /**
     * Sets the board state based on the provided SFEN string.
     * 
     * @param sfen the SFEN string representing the board state
     */
    public void setSfen(Sfen sfen) {
        grid = new Piece[grid.length][grid[0].length];
        initializeBoard(sfen);
    }

    /**
     * Converts the board state to an SFEN string representation.
     * 
     * @return a string representing the current board state in SFEN format
     */
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

    /**
     * Initializes the board using the provided SFEN string.
     * 
     * @param sfen the SFEN string representing the board state
     */
    public void initializeBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece = PieceFactory.fromSfenAbbreviation(abbr);
            setAtPosition(pos, piece);
        });
    }

    /**
     * Retrieves the piece at a specific position on the board.
     * 
     * @param pos the position to query
     * @return the piece at the specified position, or null if the position is empty
     */
    public Piece getPieceAt(Pos pos) {
        return grid[pos.row()][pos.col()];
    }

    /**
     * Gets all pieces currently on the board.
     * 
     * @return a list of all pieces on the board
     */
    public ArrayList<Piece> getEveryPiece() {
        Piece piece;
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                piece = getPieceAt(new Pos(i, j));
                if (piece != null) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Gets the positions of all pieces currently on the board.
     * 
     * @return a list of positions of all pieces on the board
     */
    public ArrayList<Pos> getEveryPiecePos() {
        Piece piece;
        ArrayList<Pos> positions = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                piece = getPieceAt(new Pos(i, j));
                if (piece != null) {
                    positions.add(new Pos(i, j));
                }
            }
        }
        return positions;
    }

    /**
     * Finds the position of a specific piece on the board.
     * 
     * @param side      the side to which the piece belongs
     * @param pieceType the class of the piece to find
     * @return the position of the specified piece, or null if not found
     */
    public Pos getPiecePos(Side side, Class pieceType) {
        Piece piece;
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                piece = getPieceAt(new Pos(i, j));
                if (piece != null) {
                    if (piece.getClass() == pieceType && piece.getSide() == side) {
                        return new Pos(i, j);
                    }
                }
            }
        }
        return null;
    }
    /**
     * @return the width of the board.
     */
    public int getWidth(){return grid.length;}
    /**
     * @return the height of the board.
     */
    public int getHeight(){return grid[0].length;}

}
